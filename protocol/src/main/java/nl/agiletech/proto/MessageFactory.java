package nl.agiletech.proto;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

public class MessageFactory {
    public static final int MESSAGE_NAME_SIZE = 20;

    public RootMessage create(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.getInt(); // skip 4 bytes
        // note: Careful. 'name' is untrusted input
        return new RootMessage(StringUtil.getName(StringUtil.toBytes(byteBuffer, MESSAGE_NAME_SIZE)),
                byteBuffer.getShort(), byteBuffer.getShort(), byteBuffer, this);
    }

    public Message createAttachment(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.getInt(); // skip 4 bytes
        String name = StringUtil.getName(StringUtil.toBytes(byteBuffer, MESSAGE_NAME_SIZE));
        short version = byteBuffer.getShort();
        short status = byteBuffer.getShort();
        try {
            // note: Careful. 'name' is untrusted input
            Class<?> clz = Class.forName(getClass().getPackageName() + "." + name);
            Constructor<?> ctor = clz.getDeclaredConstructor(
                    String.class, int.class, int.class, ByteBuffer.class, MessageFactory.class);
            return (Message) ctor.newInstance(name, version, status, byteBuffer, this);
        } catch (Exception e) {
            throw new IOException("failed to create: " + name, e);
        }
    }
}