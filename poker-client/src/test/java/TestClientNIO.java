import client.ClientNIO;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestClientNIO {

    private ClientNIO clientNIO;
    @Test
    public void testServerNIOConstructor() {
        clientNIO = new ClientNIO();
        assertNotNull(clientNIO);
    }


}