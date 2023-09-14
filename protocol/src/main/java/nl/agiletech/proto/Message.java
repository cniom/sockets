package nl.agiletech.proto;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface Message {
    int STATUS_OK = 0;
    int STATUS_ERROR = 1;

    int size();

    ByteBuffer write() throws IOException;

    String getName();

    int getVersion();

    int getStatus();
}
