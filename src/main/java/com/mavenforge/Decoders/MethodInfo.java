package com.mavenforge.Decoders;

import java.io.DataInputStream;
import java.io.IOException;

public class MethodInfo {
    private final int accessFlags;
    private final int nameIndex;
    private final int descriptorIndex;
    private final AttributeInfo[] attributes;
    private final ConstantPoolEntry[] constantPool;

    public MethodInfo(int accessFlags, int nameIndex, int descriptorIndex, AttributeInfo[] attributes,
            ConstantPoolEntry[] constantPool) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
        this.constantPool = constantPool;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(formatAccessFlags(accessFlags)).append(" ");
        sb.append(getConstantPoolName(nameIndex)).append(" ");
        sb.append(getConstantPoolDescriptor(descriptorIndex)).append(" {\n");

        for (AttributeInfo attribute : attributes) {
            sb.append("    ").append(attribute).append("\n");
        }

        sb.append("}");
        return sb.toString();
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
        return new MethodInfo(accessFlags, nameIndex, descriptorIndex, attributes, constantPool);
    }

    private static String formatAccessFlags(int accessFlags) {
        StringBuilder sb = new StringBuilder();
        if ((accessFlags & 0x0001) != 0)
            sb.append("public ");
        if ((accessFlags & 0x0010) != 0)
            sb.append("final ");
        if ((accessFlags & 0x0040) != 0)
            sb.append("static ");
        if ((accessFlags & 0x0080) != 0)
            sb.append("final ");
        if ((accessFlags & 0x0100) != 0)
            sb.append("synchronized ");
        if ((accessFlags & 0x0400) != 0)
            sb.append("native ");
        if ((accessFlags & 0x0800) != 0)
            sb.append("abstract ");
        return sb.toString().trim();
    }

    private String getConstantPoolName(int index) {
        if (index == 0 || index >= constantPool.length) {
            return "";
        }
        ConstantPoolEntry entry = constantPool[index];
        if (entry.getTag() == 1) {
            return (String) entry.getValue();
        }
        return "";
    }

    private String getConstantPoolDescriptor(int index) {
        if (index == 0 || index >= constantPool.length) {
            return "";
        }
        ConstantPoolEntry entry = constantPool[index];
        if (entry.getTag() == 1) {
            return (String) entry.getValue();
        }
        return "";
    }
}
