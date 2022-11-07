import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.runServer(1234, 2);


    }
}

class HelperClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter you username for the game: ");
//        String username = scanner.nextLine();
        Client client = new Client("uzytkownik");
        client.runClient();
    }
}

