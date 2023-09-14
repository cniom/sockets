package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class MessageBase implements Message {
    public static final int MESSAGE_SIZE = 4;
    public static final int MESSAGE_NAME_SIZE = 20;
    public static final int MESSAGE_VERSION_SIZE = 2;
    public static final int MESSAGE_STATUS_SIZE = 2;
    public byte[] name;
    public short version;
    public short status;

    public MessageBase(String name, int version) {
        this(name, version, STATUS_OK);
    }

    public MessageBase(String name, int version, int status) {
        this.name = StringUtil.toBytes(name, MESSAGE_NAME_SIZE);
        this.version = (short) version;
        this.status = (short) status;
    }

    MessageBase(String name, int version, int status, ByteBuffer byteBuffer, MessageFactory messageFactory) throws IOException {
        this(name, version, status);
        deserialize(byteBuffer, messageFactory);
    }

    protected abstract void deserialize(ByteBuffer byteBuffer, MessageFactory messageFactory) throws IOException;

    @Override
    public final int size() {
        int sz = MESSAGE_SIZE + MESSAGE_NAME_SIZE + MESSAGE_VERSION_SIZE + MESSAGE_STATUS_SIZE;
        sz += getBodySize();
        return sz;
    }

    protected abstract int getBodySize();

    @Override
    public final ByteBuffer write() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(size());
        byteBuffer.putInt(size());
        byteBuffer.put(name);
        byteBuffer.putShort(version);
        byteBuffer.putShort(status);
        serialize(byteBuffer);
        byteBuffer.flip();
        return byteBuffer;
    }

    protected abstract void serialize(ByteBuffer byteBuffer) throws IOException;

    @Override
    public String getName() {
        return StringUtil.getName(name);
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "{name=" + getName() + ", version=" + getVersion() + ", status=" + getStatus() + "}";
    }
}
