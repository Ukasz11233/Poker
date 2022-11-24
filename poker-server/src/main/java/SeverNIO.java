import gameplay.Player;
import gameplay.Table;
import logs.*;
import bufferOperations.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

import static logs.logs.debug;

class ServerNIO {
    public static void main(String... args) throws IOException {
        ServerNIO server = new ServerNIO();
        server.runServer(8090, 2);
    }

    private ServerSocketChannel ssc;
    private Selector selector;

    // statystyki rozgrywki
    private int conenctedUsers = 0;
    private int numberOfPlayers;
    private int numberOfMesseges = 0;
    private int currClient;

    private boolean gameStarted = false;

    private Map<SocketChannel, Integer> playersId;
    private Map<Integer, Boolean> hasGameStarted;
    private ByteBuffer readBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);

    Table table;
    public void runServer(int portNumber, int _numberOfPlayers) throws IOException {
        initalizeServer(portNumber, _numberOfPlayers);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();

            // oczekwianie na graczy
            while(it.hasNext() && conenctedUsers < numberOfPlayers) {
                SelectionKey sk = it.next();
                if (sk.isValid() && sk.isAcceptable()) {
                    handleAccept();
                }
                it.remove();
                gameStarted = false;
            }
            if (conenctedUsers == numberOfPlayers) {
                if (!gameStarted) {
                    gameStarted = true;
                    table.startGame();
                }
            }
            // rozpoczecie rozgrywki
            while(it.hasNext() && conenctedUsers == numberOfPlayers) {
                SelectionKey sk = it.next();
                currClient = numberOfMesseges % conenctedUsers;
                if (sk.isValid() && sk.isReadable()) {
                    handleRead(sk);
                } else if (sk.isValid() && sk.isWritable()) {
                    handleWrite(sk, "your move!");
                }
                it.remove();
            }
        }
    }

    private void initalizeServer(int portNumber, int _numberOfPlayers) {
        numberOfPlayers = _numberOfPlayers;
        playersId = new HashMap<>();
        hasGameStarted = new HashMap<>();

        table = new Table(10);
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("localhost", portNumber));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            logs.debug("Wyjatek w inicjalizacji serwera");
            e.printStackTrace();
        }
    }

    private void handleAccept() throws IOException{
        SocketChannel sc = ssc.accept();

        if (sc != null) {
            playersId.put(sc, conenctedUsers);
            hasGameStarted.put(conenctedUsers, false);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_WRITE);
            debug("dodalem uzytkownika");
            conenctedUsers++;

            table.addPlayer(new Player(playersId.get(sc).toString()));
            //Jesli gracz dolaczyl jako pierwszy, to ustawiam go jako tego na ktorym konczymy runde
            if (conenctedUsers == 1) {
                table.setPlayerToStop(playersId.get(sc));
            }

            // Sending first message to player.
            bufferOperations.clearBuffer(writeBuffer);
            writeBuffer.put("We are waiting for players".getBytes());
            writeBuffer.clear();
            sc.write(writeBuffer);

        }
    }

    private void handleRead(SelectionKey sk) throws IOException {
        SocketChannel client = (SocketChannel) sk.channel();
        String receivedMessage;
        bufferOperations.clearBuffer(readBuffer);
        int read = client.read(readBuffer);
        if (read == -1) {
            logs.debug("read==-1");
            removeClient(client);
        } else if (read > 0) {
            receivedMessage = new String(readBuffer.array()).trim();
            logs.debug(receivedMessage);
            sk.interestOps(SelectionKey.OP_WRITE);
            if (table.readPlayerMove(receivedMessage, playersId.get(client))) {
                numberOfMesseges++;
                //zrob cos z tym ruchem
                if (table.isEndOFRound()) {
                    int winner = table.endRound();
                    handleWrite(sk, "winner id: " + winner);
                }
                debug("Wiadomosc poprawna!");
            }
            if (receivedMessage.equalsIgnoreCase("status")) {
                handleWrite(sk, "status");
            }
        }
    }

    private void handleWrite(SelectionKey sk, String msg) throws IOException {
        SocketChannel client = (SocketChannel) sk.channel();
        bufferOperations.clearBuffer(writeBuffer);

        if (!hasGameStarted.get(playersId.get(client))) {
            writeBuffer.put("starting game\n".getBytes());
            writeBuffer.put(( table + "\n" + table.playerInfo(playersId.get(client))).getBytes());
            if (currClient == playersId.get(client)) {
                writeBuffer.put(("\n" + table.tellWhatMoves(playersId.get(client)) + "\nIt is your move\n").getBytes());
            } else {
                writeBuffer.put(("\nIt is player " + currClient + " move\n").getBytes());
            }
            hasGameStarted.put(playersId.get(client), true);
        }
        else {
            if (msg.equalsIgnoreCase("your move!")) {
                debug("Curr client: " + currClient);
                if (currClient == playersId.get(client)) {
                    writeBuffer.put(table.tellWhatMoves(playersId.get(client)).getBytes());
                } else {
                    writeBuffer.put("It is not your move. You can check status of the game.".getBytes());
                }
            }
            if (msg.equalsIgnoreCase("status")) {
                writeBuffer.put(("Status: Ruch wykonuje gracz o id: " + currClient + " Twoje id: " + playersId.get(client) + "\n" + table + "\n" +
                        table.playerInfo(playersId.get(client)) + "\n" + table.tellWhatMoves(playersId.get(client))).getBytes());
            } else if (msg.contains("winner")) {
                writeBuffer.put(msg.getBytes());
            }else  {
                writeBuffer.put("empty message".getBytes());
            }
        }
        writeBuffer.flip();
        int write = client.write(writeBuffer);

        if (write == -1) {
            debug("write==-1");
            removeClient(client);
            return;
        }
        sk.interestOps(SelectionKey.OP_READ);
    }

    private void removeClient(SocketChannel client) throws IOException{
        client.close();
        table.removePlayer(playersId.get(client));
        table.resetAllStatistics();
        playersId.remove(client);
        conenctedUsers--;
        hasGameStarted.put(playersId.get(client), false);
    }


}
