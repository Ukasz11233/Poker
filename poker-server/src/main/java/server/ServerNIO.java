package server;

import gameplay.Player;
import gameplay.Table;
import logs.*;
import bufferOperations.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static logs.logs.debug;

public class ServerNIO {

    Logger logger = Logger.getAnonymousLogger();
    private ServerSocketChannel ssc;
    private Selector selector;

    // statystyki rozgrywki
    private int conenctedUsers = 0;
    private int numberOfPlayers;
    private int numberOfMesseges = 0;
    private int currClient;
    private int winnerId;

    private boolean killServer = false;
    private boolean gameStarted = false;

    private Map<SocketChannel, Integer> playersId;

    private Map<Integer, Boolean> hasGameStarted;

    private Map<Integer, Boolean> wasRoundFinished;

    final private ByteBuffer readBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);
    final private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);
    Table table;

    public void runServer(int portNumber, int _numberOfPlayers) throws IOException {
        initalizeServer(portNumber, _numberOfPlayers);

        try {
            while (!killServer) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                // oczekwianie na graczy
                waitForPlayer(it);
                // sprawdz czy gra juz sie rozpoczynala
                checkGameStarted();
                // rozpoczecie rozgrywki
                runGame(it);
            }
        } catch (IOException e) {
            logger.log(Level.OFF, "catch", e);
            killServer = true;
        }
    }

    private void waitForPlayer(Iterator<SelectionKey> it) throws IOException{
        while(it.hasNext() && conenctedUsers < numberOfPlayers) {
            SelectionKey sk = it.next();
            if (sk.isValid() && sk.isAcceptable()) {
                handleAccept();
            }
            it.remove();
            gameStarted = false;
        }
    }

    private void checkGameStarted() {
        if (conenctedUsers == numberOfPlayers) {
            if (!gameStarted) {
                gameStarted = true;
                table.startGame();
            }
        }
    }

    private void runGame(Iterator<SelectionKey> it) throws IOException{
        while(it.hasNext() && conenctedUsers == numberOfPlayers) {
            SelectionKey sk = it.next();
            currClient = numberOfMesseges % conenctedUsers;
            if (table.isEndOFRound(currClient)) {
                winnerId = table.endRound();
                for(int i = 0; i < numberOfPlayers; ++i)
                    wasRoundFinished.put(i, true);
                debug("Koniec rundy ");
            }
            if (sk.isValid() && sk.isReadable()) {
                handleRead(sk);
            } else if (sk.isValid() && sk.isWritable()) {
                handleWrite(sk, "your move!");
            }
            it.remove();
        }
    }
    private void initalizeServer(int portNumber, int _numberOfPlayers) {
        numberOfPlayers = _numberOfPlayers;
        playersId = new HashMap<>();
        hasGameStarted = new HashMap<>();
        wasRoundFinished = new HashMap<>();

        table = new Table(10);
        winnerId = -1;
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
            wasRoundFinished.put(conenctedUsers, false);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_WRITE);
            debug("dodalem uzytkownika");
            conenctedUsers++;

            table.addPlayer(new Player());
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
        try {
            SocketChannel client = (SocketChannel) sk.channel();
            int clientId = playersId.get(client);
            bufferOperations.clearBuffer(readBuffer);
            int read = client.read(readBuffer);
            if (read == -1) {
                logs.debug("read==-1");
                removeClient(client);
            } else if (read > 0) {
                sk.interestOps(SelectionKey.OP_WRITE);
                readMessage(sk, clientId);
            }
        } catch (IOException e) {
            logger.log(Level.OFF, "catch", e);
        }
    }

    private void readMessage(SelectionKey sk, int clientId) throws IOException{
        String receivedMessage = new String(readBuffer.array()).trim();
        debug(receivedMessage);

        if (table.readPlayerMove(receivedMessage, clientId)) {
            numberOfMesseges++;
            debug("Wiadomosc poprawna!");
        }
        if (receivedMessage.equalsIgnoreCase("status")) {
            handleWrite(sk, receivedMessage);
        }
    }
    private void handleWrite(SelectionKey sk, String msg) throws IOException {
        SocketChannel client = (SocketChannel) sk.channel();
        int clientId = playersId.get(client);
        bufferOperations.clearBuffer(writeBuffer);
        String messageToInsert = "";

        if (!hasGameStarted.get(playersId.get(client))) {
            messageToInsert += fillMessageBeforeGameStart(messageToInsert, clientId);
        }
        else {
            messageToInsert += fillMessageAfterGameStart(messageToInsert, msg, clientId);
        }
        writeBuffer.put(messageToInsert.getBytes());
        writeBuffer.flip();
        int write = client.write(writeBuffer);
        if (write == -1) {
            debug("write==-1");
            removeClient(client);
            return;
        }
        sk.interestOps(SelectionKey.OP_READ);
    }

    private String fillMessageBeforeGameStart(String messageToInsert, int clientId) {
        messageToInsert += "starting game\n" + table + "\n" + table.playerInfo(clientId);
        if (currClient == clientId) {
            messageToInsert += "\n" + table.tellWhatMoves(clientId) + "\nIt is your move\n";
        } else {
            messageToInsert += "\nIt is player " + currClient + " move\n";
        }
        hasGameStarted.put(clientId, true);

        return messageToInsert;
    }

    private String fillMessageAfterGameStart(String messageToInsert, String msg, int clientId) {
        if (wasRoundFinished.get(clientId).equals(true)) {
            messageToInsert += "Round win player: " + winnerId + "\n";
            messageToInsert += "We are beginning a new round!!!";
            wasRoundFinished.put(clientId, false);
        }
        if (msg.equalsIgnoreCase("your move!")) {

            if (currClient == clientId) {
                messageToInsert +="It is your move!\n" + table.tellWhatMoves(clientId);
            } else {
                messageToInsert += "It is not your move. You can check status of the game.";
            }
        }
        if (msg.equalsIgnoreCase("status")) {
            messageToInsert += "Status: Ruch wykonuje gracz o id: " + currClient + " Twoje id: " + clientId + "\n" + table + "\n" +
                    table.playerInfo(clientId) + "\n" + table.tellWhatMoves(clientId);
        } else if (msg.contains("winner")) {
            messageToInsert += msg.getBytes();
        }else if(msg.isEmpty())  {
            messageToInsert += "empty message";
        }
        return messageToInsert;
    }

    private void removeClient(SocketChannel client) throws IOException{
        client.close();
        table.removePlayer(playersId.get(client));
        table.resetAllStatistics();
        playersId.remove(client);
        conenctedUsers--;
        hasGameStarted.put(playersId.get(client), false);
        gameStarted = false;
    }
}
