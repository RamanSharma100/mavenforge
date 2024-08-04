package com.mavenforge.Decoders;

import java.io.DataInputStream;
import java.io.IOException;

public class MethodInfo {
    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final AttributeInfo[] attributes;

    public MethodInfo(int accessFlags, int nameIndex, int descriptorIndex, AttributeInfo[] attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "accessFlags=" + this.accessFlags +
                ", nameIndex=" + this.nameIndex +
                ", descriptorIndex=" + this.descriptorIndex +
                ", attributes=" + this.attributes +
                '}';
    }

    public static MethodInfo readMethodInfo(DataInputStream dis, ConstantPoolEntry[] constantPool) throws IOException {
        int accessFlags = dis.readUnsignedShort();
        int nameIndex = dis.readUnsignedShort();
        int descriptorIndex = dis.readUnsignedShort();
        int attributesCount = dis.readUnsignedShort();
        AttributeInfo[] attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = AttributeInfo.readAttributeInfo(dis, constantPool);
        }
        return new MethodInfo(accessFlags, nameIndex, descriptorIndex, attributes);
    }
}
