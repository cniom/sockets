package nl.agiletech;

import nl.agiletech.proto.Message;
import nl.agiletech.proto.MessageFactory;
import nl.agiletech.proto.Server;
import nl.agiletech.proto.SocketUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Listener {
    private static final int MAX_MESSAGE_SIZE = 4096;
    private final MessageFactory messageFactory;
    private final Server server;
    private final Dispatch dispatch;
    private final int port;
    private boolean exitRequested;
    private Selector selector;

    public Listener(MessageFactory messageFactory, Server server, Dispatch dispatch, int port) {
        this.messageFactory = messageFactory;
        this.server = server;
        this.dispatch = dispatch;
        this.port = port;
    }

    public void start() throws IOException {
        System.out.println("server start");
        selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, serverSocketChannel.validOps(), null);
        System.out.println("listening to: " + serverSocketChannel.socket().getLocalSocketAddress().toString());
        while (!shouldExit()) {
            selector.select();
            if (shouldExit()) {
                System.out.println("exit requested");
                break;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept(serverSocketChannel, key);
                } else if (key.isReadable()) {
                    handleRequest(key);
                }
                iterator.remove();
            }
        }
        selector.close();
        System.out.println("server stop");
    }

    public void stop() {
        exitRequested = true;
    }

    boolean shouldExit() {
        return exitRequested || server.isExit();
    }

    void handleAccept(ServerSocketChannel serverSocketChannel, SelectionKey selectionKey) throws IOException {
        System.out.println("accept connection");
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    void handleRequest(SelectionKey selectionKey) throws IOException {
        System.out.println("handle request");
        try (SocketChannel socketChannel = (SocketChannel) selectionKey.channel()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_MESSAGE_SIZE);
            SocketUtil.read(socketChannel, byteBuffer);

            Message request = messageFactory.create(byteBuffer);
            System.out.println("Request: " + request);

            Message response = dispatchRequest(request);
            System.out.println("Response: " + response);
            socketChannel.write(response.write());
        }
    }

    Message dispatchRequest(Message request) {
        Message response;
        try {
            response = dispatch.handleRequest(request, server);
        } catch (NoSuchMethodException e) {
            response = server.unknownRequest(request, e);
        } catch (ServerException e) {
            response = server.serverException(request, e);
        }
        if (response == null) {
            response = server.serverException(request,
                    new ServerException("unknown server error"));
        }
        return response;
    }
}
