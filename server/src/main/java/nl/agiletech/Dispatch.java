package nl.agiletech;

import nl.agiletech.proto.Message;
import nl.agiletech.proto.Server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dispatch {
    public Message handleRequest(Message request, Server server) throws NoSuchMethodException, ServerException {
        Method method = server.getClass().getDeclaredMethod(request.getName(), Message.class);
        if (!method.getReturnType().isAssignableFrom(Message.class)) {
            throw new ServerException("bad request '" + request.getName() + "'");
        }
        try {
            return (Message) method.invoke(server, request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ServerException("bad request", e);
        }
    }
}
