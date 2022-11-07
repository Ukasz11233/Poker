import gameplay.Player;
import gameplay.Table;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Socket> sockets;
    private ArrayList<InputStreamReader> inputStreamReaders;
    private ArrayList<OutputStreamWriter> outputStreamWriters;
    private ArrayList<BufferedReader> bufferedReaders;
    private ArrayList<BufferedWriter> bufferedWriters;
    Table table;

    // statystyki rozgrywki
    private int conenctedUsers = 0;
    private int numberOfPlayers;
    private int numberOfMesseges = 0;



    public void runServer(int portNumber, int _numberOfPlayers) {
        initializeServer(portNumber, _numberOfPlayers);
        int currClient;
        while (!serverSocket.isClosed()) {
            try {
                while (conenctedUsers < numberOfPlayers)
                {
                    debug("Czekam na graczy, connected useres: " + conenctedUsers);
                    addClientToServer();
                }
                checkConnection();
                debug("Rozpoczynamy nową rozgrywke");
                broadcastMessage("Rozpoczynamy nową rozgrywke");
                table.startGame();
                while (conenctedUsers == numberOfPlayers) {
                    if(!checkConnection())
                        break;
                    debug("Number of connected useres: " + conenctedUsers);
                    currClient = numberOfMesseges % conenctedUsers;
                    gameplay(currClient);
                }
                if (checkConnection()) {
                    broadcastMessage("Gracz wyszedl z gry...");
                    resetAllStatistics();
                }
            } catch (IOException e) {
                debug("Wyjatek w run Server");
                e.printStackTrace();
                // TODO close socket server
            }
        }

    }

    private void gameplay(int currClient) {
        boolean moveIsCorrect = false;
        try {
            do {

                broadcastMessage("AKTUALNY STAN ROZGRYWKI: " + table);
                broadcastInfo();
                debug("Gameplay " + currClient);
                sendToClient(table.tellWhatMoves(currClient), currClient);
                sendToClient("your turn", currClient);

                String msgFromClient = bufferedReaders.get(currClient).readLine();
                moveIsCorrect = table.readPlayerMove(msgFromClient, currClient);

                if (msgFromClient.equals(null)) {
                    clientDisconnected(currClient);
                }
                System.out.println("Client" + currClient + " :" + msgFromClient);
                numberOfMesseges++;

            }while(!moveIsCorrect);
        } catch (Exception e) {
            debug("Wyjatek w gameplay");
            debug("Klient " + currClient + " rozlaczyl sie");
            clientDisconnected(currClient);
        }
    }

    private boolean checkConnection() {
        for (int i = 0; i < conenctedUsers; ++i) {
            if(!checkConnectionOfClient(i))
                return false;
        }
        return true;
    }

    private boolean checkConnectionOfClient(int idx) {
        String acknack;
        try {
            bufferedWriters.get(idx).write("ack");
            bufferedWriters.get(idx).newLine();
            bufferedWriters.get(idx).flush();

            acknack = bufferedReaders.get(idx).readLine();
            debug("ACKNACK dostarczone do serwera");
            return acknack.equalsIgnoreCase("acknack");
        } catch (Exception e) {
            clientDisconnected(idx);
        }
        return false;
    }

    private void resetAllStatistics() {
        numberOfMesseges = 0;
    }

    private void initializeServer(int portNumber, int _numberOfPlayers) {
        sockets = new ArrayList<>();
        inputStreamReaders = new ArrayList<>();
        outputStreamWriters = new ArrayList<>();
        bufferedReaders = new ArrayList<>();
        bufferedWriters = new ArrayList<>();
        table = new Table(10);
        numberOfPlayers = _numberOfPlayers;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            debug("Wyjatek w initalizeServer");
            e.printStackTrace();
        }

    }

    private void addClientToServer() throws IOException {
        sockets.add(serverSocket.accept());
        inputStreamReaders.add(new InputStreamReader(sockets.get(conenctedUsers).getInputStream()));
        outputStreamWriters.add(new OutputStreamWriter(sockets.get(conenctedUsers).getOutputStream()));

        bufferedReaders.add(new BufferedReader(inputStreamReaders.get(conenctedUsers)));
        bufferedWriters.add(new BufferedWriter(outputStreamWriters.get(conenctedUsers)));

        // czytam nazwe uzytkownika od nowego uzytkownika
        String new_username = bufferedReaders.get(conenctedUsers).readLine();
        conenctedUsers++;
        broadcastMessage("POLACZONYCH GRACZY: " + conenctedUsers);
        //broadcastMessage("Dołączył gracz " + new_username);
        table.addPlayer(new Player(new_username));

        // JESLI GRACZ DALACZYL JAKO PIERWSZY TO USTAWIAM GO JAKO TEGO NA KTORYM KONCZYMY RUNDE
        if (conenctedUsers == 1) {
            table.setPlayerToStop(0);
        }
    }

    private void clientDisconnected(int idx)  {
        try {
            if(bufferedReaders.get(idx) != null)
            {
                bufferedReaders.get(idx).close();
                bufferedReaders.remove(idx);
            }
            if(bufferedWriters.get(idx) != null) {
                bufferedWriters.get(idx).close();
                bufferedWriters.remove(idx);
            }
            if(inputStreamReaders.get(idx) != null) {
                inputStreamReaders.get(idx).close();
                inputStreamReaders.remove(idx);
            }
            if(outputStreamWriters.get(idx) != null) {
                outputStreamWriters.get(idx).close();
                outputStreamWriters.remove(idx);
            }
            if(sockets.get(idx) != null) {
                sockets.get(idx).close();
                sockets.remove(idx);
            }
        } catch (IOException e) {
            debug("Wyajtek w clientDisconnected");
            e.printStackTrace();
        }
        table.removePlayer(idx);
        conenctedUsers--;

    }

    private void broadcastInfo() throws IOException {
        for (int i = 0; i < conenctedUsers; ++i) {
            sendToClient(table.playerInfo(i), i);
        }
    }
    private void broadcastMessage(String msg) {
        try {
            for (int i = 0; i < conenctedUsers; i++) {
                if (sockets.get(i).isConnected()) {
                    sendToClient(msg, i);
                }
            }
        } catch (IOException e) {
            debug("boradcast exception");
            e.printStackTrace();
        }
    }

    private void sendToClient(String msg, int idx) throws IOException {
        bufferedWriters.get(idx).write(msg);
        bufferedWriters.get(idx).newLine();
        bufferedWriters.get(idx).flush();
    }

    private void debug(String msg) {
        System.out.println("DBG SERVER: " + msg);
    }
}
