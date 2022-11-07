import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String username;
    private Socket socket;
    private InputStreamReader inputStreamReader;
    private OutputStreamWriter outputStreamWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Scanner scanner;
    private String msgToSend = "";
    private String msgReceived = "";

    public Client(String _username) {
        username = _username;
    }

    public void runClient() {
        try {
            initializeClient(1234);
            while (communicateWithServer()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
        finally {
            debug("finally");
            try {
                if (socket != null) {
                    socket.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean communicateWithServer() throws IOException{
        msgReceived = bufferedReader.readLine();
        if (msgReceived.equalsIgnoreCase("your turn")) {
            System.out.println("Twoj ruch " + username + "!");
            msgToSend = scanner.nextLine();
            sendMessageToServer(msgToSend);
        } else if (msgReceived.equalsIgnoreCase("ack")) {
            debug("ACK dostarczone!");
            sendMessageToServer("acknack");
        } else {
            System.out.println("Server:  " + msgReceived);
        }

        return !msgToSend.equalsIgnoreCase("BYE");
    }
    private void initializeClient(int portNumber) throws IOException {
        scanner = new Scanner(System.in);
        socket = new Socket("localhost", portNumber);
        inputStreamReader = new InputStreamReader(socket.getInputStream());
        outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        bufferedWriter = new BufferedWriter(outputStreamWriter);

        /// wysylam nick do serwera:
       sendMessageToServer(username);
    }

    private void sendMessageToServer(String msg) throws IOException{
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }


    private void debug(String msg) {
        System.out.println("DBG CLIENT: " + msg);
    }
}
