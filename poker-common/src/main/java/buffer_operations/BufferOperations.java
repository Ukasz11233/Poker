package buffer_operations;

import java.nio.ByteBuffer;

public class BufferOperations {

    private BufferOperations() {
        throw new IllegalStateException("bufferOperations - utility class");
    }
    public static final int BUFFER_SIZE = 500;
    public static void clearBuffer(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        byteBuffer.put(new byte[BUFFER_SIZE]);
        byteBuffer.flip();
    }
}
