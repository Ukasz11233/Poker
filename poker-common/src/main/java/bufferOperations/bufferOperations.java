package bufferOperations;

import java.nio.ByteBuffer;

public class bufferOperations {
    public static void clearBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        byteBuffer.put(new byte[256]);
        byteBuffer.flip();
    }
}
