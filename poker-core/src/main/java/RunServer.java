import client.ClientNIO;
import server.ServerNIO;
import logs.Logs;
import java.io.IOException;

public class RunServer {
    public static final int defaultPortNumber = 8090;
    public static void main(String[] args) throws IOException {
        ServerNIO server = new ServerNIO();
        if (args.length == 0) {
            Logs.info("Podaj liczbe graczy i numer portu");
            return;
        } else if (args.length == 1) {
            Logs.info("Uruchamiam server na defaultowym porcie: " + defaultPortNumber);
            server.runServer(defaultPortNumber, Integer.parseInt(args[0]));
        } else if (args.length == 2) {
            server.runServer(Integer.parseInt(args[1]), Integer.parseInt(args[0]));
        }
    return;
    }
}
class RunClient {
    public static void main(String[] args) {
        ClientNIO client = new ClientNIO();
        if (args.length == 0) {
            Logs.info("Uruchamiam klienta na defaultowym porcie: " + RunServer.defaultPortNumber);
            client.runClient(RunServer.defaultPortNumber);
        } else if (args.length == 1) {
            client.runClient(Integer.parseInt(args[0]));
        }
    }
}

