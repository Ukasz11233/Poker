package server;

import gameplay.Player;
import gameplay.Table;
import logs.*;
import buffer_operations.*;

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

import static logs.Logs.debug;

public class ServerNIO {

    private final Logger logger = Logger.getAnonymousLogger();
    private ServerSocketChannel ssc;
    private Selector selector;

    // statystyki rozgrywki
    private int conenctedUsers = 0;
    private int numberOfPlayers;
    private int numberOfMoves = 0;
    private int currPlayerMove;
    private int winnerId;
    private int playerToStart = 0;

    private boolean killServer = false;
    private boolean gameStarted = false;

    private Map<SocketChannel, Integer> playersId;

    private Map<Integer, Boolean> hasGameStarted;

    private Map<Integer, Boolean> wasRoundFinished;

    private final ByteBuffer readBuffer = ByteBuffer.allocate(BufferOperations.BUFFER_SIZE);
    private final ByteBuffer writeBuffer = ByteBuffer.allocate(BufferOperations.BUFFER_SIZE);
    private Table table;

    private ArrayList<Integer> availableIds;

    public void runServer(int portNumber, int numberOfPlayersToSet) {
        initalizeServer(portNumber, numberOfPlayersToSet);

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
            logger.log(Level.OFF, "catch runServer", e);
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
        if (conenctedUsers == numberOfPlayers && !gameStarted) {
            gameStarted = true;
            table.startGame();
        }
    }

    private void runGame(Iterator<SelectionKey> it) throws IOException{
        while(it.hasNext()) {
            SelectionKey sk = it.next();
            BufferOperations.clearBuffer(writeBuffer);
            BufferOperations.clearBuffer(readBuffer);
            if (table.isEndOFRound(currPlayerMove)) {
                winnerId = table.endRound();
                for(int i = 0; i < numberOfPlayers; ++i)
                    wasRoundFinished.put(i, true);
                debug("Koniec rundy ");
                // kolejna runde rozpoczyna kolejny gracz
                playerToStart = (playerToStart+1)%numberOfPlayers;
                numberOfMoves = playerToStart;
                currPlayerMove = numberOfMoves;
            }
            int k = playersId.get((SocketChannel) sk.channel());

            if (sk.isValid() && sk.isReadable()) {
                handleRead(sk);
            } else if (sk.isValid() && sk.isWritable()) {
                handleWrite(sk, "your move!");
            }
            it.remove();

        }
    }
    private void initalizeServer(int portNumber, int numberOfPlayersToSet) {
        numberOfPlayers = numberOfPlayersToSet;
        playersId = new HashMap<>();
        hasGameStarted = new HashMap<>();
        wasRoundFinished = new HashMap<>();
        availableIds = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; ++i) {
            availableIds.add(i);
        }
        table = new Table(10);
        winnerId = -1;
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("localhost", portNumber));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            Logs.debug("Wyjatek w inicjalizacji serwera");
            logger.log(Level.OFF, "catch initalize", e);
        }
    }

    private void handleAccept() throws IOException{
        SocketChannel sc = ssc.accept();

        int playerIdxToInsert = availableIds.remove(0);
        if (sc != null) {
            playersId.put(sc, playerIdxToInsert);
            hasGameStarted.put(playerIdxToInsert, false);
            wasRoundFinished.put(playerIdxToInsert, false);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_WRITE);
            debug("dodalem uzytkownika");
            conenctedUsers++;

            table.addPlayer(new Player());

            // Sending first message to player.
            BufferOperations.clearBuffer(writeBuffer);
            writeBuffer.put("We are waiting for players".getBytes());
            writeBuffer.clear();
            sc.write(writeBuffer);

        }
    }

    private void handleRead(SelectionKey sk) {
        try {
            SocketChannel client = (SocketChannel) sk.channel();
            int clientId = playersId.get(client);
            BufferOperations.clearBuffer(readBuffer);
            int read = client.read(readBuffer);
            if (read == -1) {
                removeClient(client);
                table.resetAllStatistics();
                Logs.debug("read==-1");
            } else if (read > 0) {
                sk.interestOps(SelectionKey.OP_WRITE);
                readMessage(sk, clientId);
            }
        } catch (IOException e) {
            logger.log(Level.OFF, "catch handleRead", e);
        }
    }

    private void readMessage(SelectionKey sk, int clientId) throws IOException{
        String receivedMessage = new String(readBuffer.array()).trim();
        debug(receivedMessage);

        if (table.readPlayerMove(receivedMessage, clientId)) {
            currPlayerMove = table.getNextPlayerMove(currPlayerMove);
            debug("Wiadomosc poprawna!");
        }
        if (receivedMessage.equalsIgnoreCase("status")) {
            handleWrite(sk, receivedMessage);
        }
    }
    private void handleWrite(SelectionKey sk, String msg) throws IOException {
        SocketChannel client = (SocketChannel) sk.channel();
        int clientId = playersId.get(client);
        if (clientId >= conenctedUsers) {
            return;
        }
        BufferOperations.clearBuffer(writeBuffer);
        String messageToInsert = "";
        if (Boolean.FALSE.equals(hasGameStarted.get(clientId))) {
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
        if (currPlayerMove == clientId) {
            messageToInsert += "\n" + table.tellWhatMoves(clientId) + "\nIt is your move\n";
        } else {
            messageToInsert += "\nIt is player " + currPlayerMove + " move\n";
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

            if (currPlayerMove == clientId) {
                messageToInsert +="It is your move!\n" + table.tellWhatMoves(clientId);
            } else {
                messageToInsert += "It's not your move. It's player " + currPlayerMove + " move. You can check status of the game.";
            }
        }
        if (msg.equalsIgnoreCase("status")) {
            messageToInsert += "Status: Ruch wykonuje gracz o id: " + currPlayerMove + " Twoje id: " + clientId + "\n" + table + "\n" +
                    table.playerInfo(clientId) + "\n" + table.tellWhatMoves(clientId);
        } else if (msg.contains("winner")) {
            messageToInsert += msg.getBytes();
        }else if(msg.isEmpty())  {
            messageToInsert += "empty message";
        }
        return messageToInsert;
    }

    private void removeClient(SocketChannel client) throws IOException{

        debug("remove client: " + playersId.get(client));
        client.close();
        table.removePlayer(playersId.get(client));
        availableIds.add(playersId.get(client));
        playersId.remove(client);
        conenctedUsers--;
        currPlayerMove = 0;
        for (int i = 0; i < numberOfPlayers; i++) {
            hasGameStarted.put(i, false);
        }
        gameStarted = false;
        debug("reset");

    }


}
