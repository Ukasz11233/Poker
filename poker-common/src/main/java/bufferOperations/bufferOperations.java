package bufferOperations;

import java.nio.ByteBuffer;

public class bufferOperations {
    public static int BUFFER_SIZE = 500;
    public static void clearBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        byteBuffer.put(new byte[BUFFER_SIZE]);
        byteBuffer.flip();
    }
}
