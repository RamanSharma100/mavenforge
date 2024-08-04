package com.mavenforge.Decoders;

import java.io.DataInputStream;
import java.io.IOException;

public class AttributeInfo {
    private final int attributeNameIndex;
    private final int attributeLength;
    private final byte[] info;

    public AttributeInfo(int attributeNameIndex, byte[] info) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = info.length;
        this.info = info;
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "attributeNameIndex=" + this.attributeNameIndex +
                ", attributeLength=" + this.attributeLength +
                ", info=" + new String(this.info) +
                '}';
    }

    public static AttributeInfo readAttributeInfo(DataInputStream dis, ConstantPoolEntry[] constantPool)
            throws IOException {
        int attributeNameIndex = dis.readUnsignedShort();
        int attributeLength = dis.readInt();
        byte[] info = new byte[attributeLength];
        dis.readFully(info);
        return new AttributeInfo(attributeNameIndex, info);
    }
}
