package nl.agiletech.proto;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RootMessageTest {
    @Test
    public void testCreate() {
        RootMessage rootMessage = new RootMessage("Hello", 0);
        Assert.assertArrayEquals(
                new byte[]{72, 101, 108, 108, 111, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32},
                rootMessage.name);
        Assert.assertEquals("Hello", rootMessage.getName());
    }

    @Test
    public void testWrite() throws IOException {
        Message message = new RootMessage("Hello", 0);
        ByteBuffer byteBuffer = message.write();



    }
}
