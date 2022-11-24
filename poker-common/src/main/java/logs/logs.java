package logs;

import java.nio.ByteBuffer;

public class logs {
    public static void debug(String msg) {
        System.out.println("DBG: " + msg);
    }
    public void debugPosition(ByteBuffer bb) {
        System.out.println("Pozycja: " + bb.position() + " Limit: " + bb.limit() + " Capacity: " + bb.capacity());
    }
}
