import org.junit.Test;
import buffer_operations.BufferOperations;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class TestBufferOperations {

    @Test
    public void testClearBuffer() {
        ByteBuffer testBuffer = ByteBuffer.allocate(BufferOperations.BUFFER_SIZE);

        testBuffer.put("Testing if cleaning ByteBuffer is correct".getBytes());
        BufferOperations.clearBuffer(testBuffer);
        String outputFromBuffer = new String(testBuffer.array()).trim();
        assertEquals(outputFromBuffer, "");


    }
}
