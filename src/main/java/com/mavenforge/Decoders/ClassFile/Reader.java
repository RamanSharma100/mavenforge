package com.mavenforge.Decoders.ClassFile;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Reader {
    private DataInputStream dataInputStream;

    public Reader(String filePath) throws IOException {
        this.dataInputStream = new DataInputStream(new FileInputStream(filePath));
    }

    public Reader(byte[] data) {
        this.dataInputStream = new DataInputStream(new java.io.ByteArrayInputStream(data));
    }

    public int readU1() throws IOException {
        return dataInputStream.readUnsignedByte();
    }

    public int readU2() throws IOException {
        return dataInputStream.readUnsignedShort();
    }

    public int readU4() throws IOException {
        return dataInputStream.readInt();
    }

    public byte[] readBytes(int length) throws IOException {
        byte[] bytes = new byte[length];
        dataInputStream.readFully(bytes);
        return bytes;
    }

    public void close() throws IOException {
        dataInputStream.close();
    }
}
