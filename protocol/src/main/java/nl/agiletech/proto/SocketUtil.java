package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketUtil {
    public static void read(SocketChannel socketChannel, ByteBuffer byteBuffer) throws IOException {
        int maxMessageSize = byteBuffer.capacity();
        int messageSize = maxMessageSize;
        while (socketChannel.read(byteBuffer) > -1 && byteBuffer.position() < messageSize) {
            if (messageSize == maxMessageSize && byteBuffer.position() > 3) {
                messageSize = readSize(byteBuffer);
            }
        }
        byteBuffer.flip();
    }

    static int readSize(ByteBuffer byteBuffer) {
        byte[] a = new byte[4];
        System.arraycopy(byteBuffer.array(), 0, a, 0, 4);
        ByteBuffer b = ByteBuffer.wrap(a);
        return b.getInt();
    }
}
