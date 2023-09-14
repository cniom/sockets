package nl.agiletech.proto;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StringUtil {
    public static byte[] readSizeAndString(ByteBuffer byteBuffer) {
        short size = byteBuffer.getShort();
        return toBytes(byteBuffer, size);
    }

    public static byte[] toBytes(String str) {
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] toBytes(ByteBuffer byteBuffer, int bufferSize) {
        byte[] s = new byte[bufferSize];
        Arrays.fill(s, (byte) 32);
        byteBuffer.get(s, 0, Math.min(byteBuffer.limit(), bufferSize));
        return s;
    }

    public static byte[] toBytes(String str, int bufferSize) {
        byte[] a = str.getBytes(StandardCharsets.UTF_8);
        byte[] s = new byte[bufferSize];
        Arrays.fill(s, (byte) 32);
        System.arraycopy(a, 0, s, 0, Math.min(a.length, bufferSize));
        return s;
    }

    public static String getString(byte[] str) {
        return new String(str, StandardCharsets.UTF_8);
    }

    public static String getName(byte[] name) {
        return getString(name).trim();
    }
}
