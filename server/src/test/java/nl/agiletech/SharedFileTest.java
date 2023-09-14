package nl.agiletech;

import org.junit.Test;

import java.io.File;

public class SharedFileTest {
    @Test
    public void test() {
        File f = new File(System.getenv("HOME"));
        System.out.println(System.getProperties());
    }
}

