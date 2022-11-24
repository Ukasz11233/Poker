import java.lang.ref.Cleaner;
import java.nio.ByteBuffer;

public class testByteBuffer {
    static ByteBuffer bb = ByteBuffer.allocate(120);

    public static void main(String[] args) {

        t();
        bb.put("elo".getBytes());
        t();
        bb.put("cos".getBytes());
        bb.clear();
        bb.put(new byte[119]);
        bb.flip();
        bb.put("elo".getBytes());
        bb.flip();
        t();
        System.out.println(new String(bb.array()).trim());
    }


    private static void t() {
        System.out.println("Position: " + bb.position() + " Limit: " + bb.limit());
    }
}
