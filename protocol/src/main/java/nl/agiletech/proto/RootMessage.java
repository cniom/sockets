package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RootMessage extends MessageBase {
    public static final int MESSAGE_NUMBER_OF_ATTACHMENTS_SIZE = 2;
    public static final int MESSAGE_ATTACHMENT_SIZE_SIZE = 4;
    public List<Message> attachments;

    public RootMessage(String name, int version) {
        super(name, version);
    }

    public RootMessage(String name, int version, int status) {
        super(name, version, status);
    }

    RootMessage(String name, int version, int status, ByteBuffer byteBuffer, MessageFactory messageFactory)
            throws IOException {
        super(name, version, status, byteBuffer, messageFactory);
    }

    public RootMessage add(Message attachment) {
        getAttachments().add(attachment);
        return this;
    }

    public List<Message> getAttachments() {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        return attachments;
    }

    @Override
    protected int getBodySize() {
        int sz = MESSAGE_NUMBER_OF_ATTACHMENTS_SIZE;
        for (Message attachment : getAttachments()) {
            sz += MESSAGE_ATTACHMENT_SIZE_SIZE;
            sz += attachment.size();
        }
        return sz;
    }

    @Override
    protected void serialize(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.putShort((short) getAttachments().size());
        for (Message attachment : getAttachments()) {
            byteBuffer.putInt(attachment.size());
            byteBuffer.put(attachment.write());
        }
    }

    @Override
    protected void deserialize(ByteBuffer byteBuffer, MessageFactory messageFactory) throws IOException {
        short numberOfAttachments = byteBuffer.getShort();
        for (short attachmentIndex = 0; attachmentIndex < numberOfAttachments; attachmentIndex++) {
            add(messageFactory.createAttachment(ByteBufferUtil.copy(byteBuffer, byteBuffer.getInt())));
        }
    }
}
