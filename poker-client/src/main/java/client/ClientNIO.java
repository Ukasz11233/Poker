package client;

import logs.*;
import bufferOperations.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientNIO {
    Logger logger = Logger.getAnonymousLogger();
    private SocketChannel client;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);
    private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferOperations.BUFFER_SIZE);
    private Scanner scanner;
    private String msgToSend = "";
    private String msgReceived = "";
    private boolean hasGameStarted = false;


    public static void main(String ... args) throws IOException {
        ClientNIO newClient = new ClientNIO();
        newClient.runClient();
    }

    public void runClient() {
        try {
            initalizeClient(8090);
            while (true) {
                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    selector.selectedKeys().remove(key);

                    if(key.isWritable()) {
                        sendMessage(key);
                    }else if (key.isReadable()) {
                        receiveMessage(key);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.OFF, "context", e);
        }

    }



    private void receiveMessage(SelectionKey key) throws IOException {
        bufferOperations.clearBuffer(readBuffer);
        if (client.read(readBuffer) == -1) {
            logs.debug("empty message");
        }
        msgReceived = new String(readBuffer.array()).trim();
        if (msgReceived.contains("starting game")) {
            hasGameStarted = true;
        }
        System.out.println("response = " + msgReceived);
        if (hasGameStarted) {
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void sendMessage(SelectionKey key) throws IOException {
        writeBuffer.clear();
        msgToSend = scanner.nextLine();
        if (msgToSend.isEmpty()) {
            logs.debug("empty string");
            msgToSend = "empty string";
        }
        writeBuffer = ByteBuffer.wrap(msgToSend.getBytes());
        client.write(writeBuffer);
        key.interestOps(SelectionKey.OP_READ);
        writeBuffer.clear();
    }

    private void initalizeClient(int portNumber)throws IOException {
        scanner = new Scanner(System.in);
        client = SocketChannel.open(new InetSocketAddress("localhost", portNumber));
        client.configureBlocking(false);
        selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ);
    }




}
