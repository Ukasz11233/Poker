import org.junit.Test;
import server.ServerNIO;

import static org.junit.Assert.*;

public class TestServerNIO {

    private ServerNIO serverNIO;
    @Test
    public void testServerNIOConstructor() {
        serverNIO = new ServerNIO();
        assertNotNull(serverNIO);
    }


}
