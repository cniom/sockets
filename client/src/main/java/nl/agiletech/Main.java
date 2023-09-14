package nl.agiletech;

import nl.agiletech.proto.Client;
import nl.agiletech.proto.MessageFactory;
import nl.agiletech.proto.ServerError;

import java.io.IOException;

public class Main {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 10000;

    public static void main(String[] args) throws IOException {
        System.out.println("Client start");
        Client client = new Client(HOSTNAME, PORT, new MessageFactory());
        try {
            client.hello();
            // ... do stuff ...
            client.closeSession();
        } catch (ServerError e) {
            e.printStackTrace();
        }
        System.out.println("Client stop");
    }
}