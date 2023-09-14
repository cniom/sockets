package nl.agiletech;

import nl.agiletech.proto.MessageFactory;
import nl.agiletech.proto.Server;

import java.io.IOException;

public class Main {
    private static final int PORT = 10000;

    public static void main(String[] args) throws IOException {
        MessageFactory messageFactory = new MessageFactory();
        Server server = new ServerImpl();
        Dispatch dispatch = new Dispatch();
        Listener listener = new Listener(messageFactory, server, dispatch, PORT);
        listener.start();
    }
}