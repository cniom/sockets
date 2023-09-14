package nl.agiletech;

import nl.agiletech.proto.Problem;
import nl.agiletech.proto.RootMessage;
import nl.agiletech.proto.Message;
import nl.agiletech.proto.Server;
import nl.agiletech.proto.SessionId;

public class ServerImpl implements Server {
    private boolean doExitServer = false;

    private int id;

    @Override
    public Message hello(Message request) {
        id++;
        return new RootMessage(OK, VERSION).add(new SessionId(VERSION, id));
    }

    @Override
    public Message closeSession(Message request) {
        return null;
    }

    @Override
    public Message closeServer(Message request) {
        doExitServer = true;
        return new RootMessage(OK, VERSION);
    }

    @Override
    public Message unknownRequest(Message request, Exception e) {
        // note: Careful. It is not a good idea to pass error messages to untrusted clients
        return new RootMessage(ERROR, VERSION, Message.STATUS_ERROR)
                .add(new Problem(VERSION, "unknown request '" + request.getName() + "'"));
    }

    @Override
    public Message serverException(Message request, Exception e) {
        // note: Careful. It is not a good idea to pass error messages to untrusted clients
        return new RootMessage(ERROR, VERSION, Message.STATUS_ERROR)
                .add(new Problem(VERSION, e.getMessage()));
    }

    @Override
    public boolean isExit() {
        return doExitServer;
    }
}
