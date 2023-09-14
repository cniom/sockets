package nl.agiletech.proto;

public interface Server {
    int VERSION = 1;

    String OK = "ok";
    String ERROR = "error";

    String HELLO = "hello";

    Message hello(Message request);

    String CLOSE_SESSION = "closeSession";

    Message closeSession(Message request);

    String CLOSE_SERVER = "closeServer";

    Message closeServer(Message request);


    // errors:

    Message unknownRequest(Message request, Exception e);

    Message serverException(Message request, Exception e);


    boolean isExit();


}
