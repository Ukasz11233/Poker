import client.ClientNIO;
import server.ServerNIO;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerNIO server = new ServerNIO();
        server.runServer(8090, 2);


    }
}
class HelperClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClientNIO client = new ClientNIO();
        client.runClient();
    }
}

