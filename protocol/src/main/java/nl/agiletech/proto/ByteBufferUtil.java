package nl.agiletech.proto;

import java.nio.ByteBuffer;

public class ByteBufferUtil {
    public static ByteBuffer copy(ByteBuffer byteBuffer, int length) {
        byte[] b = new byte[length];
        byteBuffer.get(b, 0, length);
        return ByteBuffer.wrap(b);
    }
}
