package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Problem extends MessageBase {
    public static final int MESSAGE_MESSAGE_SIZE = 2;
    public byte[] message;

    public Problem(int version, String message) {
        super(Problem.class.getSimpleName(), version);
        this.message = StringUtil.toBytes(message);
    }

    Problem(String name, int version, int status, ByteBuffer byteBuffer, MessageFactory messageFactory)
            throws IOException {
        super(name, version, status, byteBuffer, messageFactory);
    }

    public String getMessage() {
        return StringUtil.getString(message);
    }

    @Override
    protected int getBodySize() {
        return MESSAGE_MESSAGE_SIZE + message.length;
    }

    @Override
    protected void serialize(ByteBuffer byteBuffer) {
        byteBuffer.putShort((short) message.length);
        byteBuffer.put(message);
    }

    @Override
    protected void deserialize(ByteBuffer byteBuffer, MessageFactory messageFactory) {
        message = StringUtil.readSizeAndString(byteBuffer);
    }
}
