package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SessionId extends MessageBase {
    public static final int MESSAGE_ID_SIZE = 4;
    public int id;

    public SessionId(int version, int id) {
        super(SessionId.class.getSimpleName(), version);
        this.id = id;
    }

    SessionId(String name, int version, int status, ByteBuffer byteBuffer, MessageFactory messageFactory) throws IOException {
        super(name, version, status, byteBuffer, messageFactory);
    }

    public int getId() {
        return id;
    }

    @Override
    protected int getBodySize() {
        return MESSAGE_ID_SIZE;
    }

    @Override
    protected void serialize(ByteBuffer byteBuffer) {
        byteBuffer.putInt(id);
    }

    @Override
    protected void deserialize(ByteBuffer byteBuffer, MessageFactory messageFactory) {
        id = byteBuffer.getInt();
    }
}
