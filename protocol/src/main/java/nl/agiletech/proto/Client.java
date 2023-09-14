package nl.agiletech.proto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private static final int VERSION = 1;
    private static final int MAX_RESPONSE_MESSAGE_SIZE = 4096;

    private final MessageFactory messageFactory;
    private final String hostName;
    private final int port;

    public Client(String hostName, int port, MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
        this.hostName = hostName;
        this.port = port;
    }

    public void hello() throws IOException, ServerError {
        send(new RootMessage(Server.HELLO, VERSION));
    }

    public void closeSession() throws IOException, ServerError {
        send(new RootMessage(Server.CLOSE_SESSION, VERSION));
    }

    public void closeServer() throws IOException, ServerError {
        send(new RootMessage(Server.CLOSE_SERVER, VERSION));
    }

    RootMessage send(RootMessage request) throws IOException, ServerError {
        try (SocketChannel server = SocketChannel.open()) {
            SocketAddress socketAddress = new InetSocketAddress(hostName, port);
            server.connect(socketAddress);
            server.write(request.write());
            server.shutdownOutput();
            RootMessage response = getResponse(server);
            handleError(response);
            return response;
        }
    }

    RootMessage getResponse(SocketChannel server) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_RESPONSE_MESSAGE_SIZE);
        SocketUtil.read(server, byteBuffer);
        return messageFactory.create(byteBuffer);
    }

    void handleError(RootMessage response) throws ServerError {
        if (response.getStatus() == Message.STATUS_OK) {
            return;
        }
        if (response.getAttachments().isEmpty()) {
            throw new ServerError("unknown error");
        }
        for (Message attachment : response.getAttachments()) {
            if (attachment instanceof Problem problem) {
                throw new ServerError(problem.getMessage());
            }
        }
    }
}
