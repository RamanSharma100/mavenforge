package com.mavenforge.Decoders;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassfileDecoder {
    private String classFilePath = "";
    private int minorVersion = 0, majorVersion = 0;

    public ClassfileDecoder() {
    }

    public ClassfileDecoder(String classFilePath) {
        this.classFilePath = classFilePath;
    }

    public ClassfileDecoder setClassFilePath(String classFilePath) {
        this.classFilePath = classFilePath;
        return this;
    }

    public String getClassFilePath() {
        return this.classFilePath;
    }

    // decode the class file and return the full class
    public String decode() {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(this.classFilePath));) {
            int magicNumber = dis.readInt();

            if (magicNumber != 0xCAFEBABE) {
                throw new RuntimeException("Invalid class file");
            }

            this.minorVersion = dis.readUnsignedShort();
            this.majorVersion = dis.readUnsignedShort();

            int constantPoolCount = dis.readUnsignedShort();
            ConstantPoolEntry[] constantPool = new ConstantPoolEntry[constantPoolCount];
            for (int i = 1; i < constantPoolCount; i++) {
                constantPool[i] = readPollEntry(dis);

                if (constantPool[i] != null && (constantPool[i].getTag() == 5 || constantPool[i].getTag() == 6)) {
                    i++;
                }
            }

            int accessFlags = dis.readUnsignedShort();
            int thisClass = dis.readUnsignedShort();
            int superClass = dis.readUnsignedShort();

            int interfacesCount = dis.readUnsignedShort();
            int[] interfaces = new int[interfacesCount];
            for (int i = 0; i < interfacesCount; i++) {
                interfaces[i] = dis.readUnsignedShort();
            }

            int fieldsCount = dis.readUnsignedShort();
            FieldInfo[] fields = new FieldInfo[fieldsCount];
            for (int i = 0; i < fieldsCount; i++) {
                fields[i] = FieldInfo.readFieldInfo(dis, constantPool);
            }

            int methodsCount = dis.readUnsignedShort();
            MethodInfo[] methods = new MethodInfo[methodsCount];
            for (int i = 0; i < methodsCount; i++) {
                methods[i] = MethodInfo.readMethodInfo(dis, constantPool);
            }

            int attributesCount = dis.readUnsignedShort();
            AttributeInfo[] attributes = new AttributeInfo[attributesCount];
            for (int i = 0; i < attributesCount; i++) {
                attributes[i] = AttributeInfo.readAttributeInfo(dis, constantPool);
            }

            System.out.println("Access Flags: " + accessFlags);
            System.out.println("This Class: " + thisClass);
            System.out.println("Super Class: " + superClass);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }

        return "";
    }

    private static ConstantPoolEntry readPollEntry(DataInputStream dis) throws IOException {
        int tag = dis.readUnsignedByte();

        switch (tag) {
            case 7:
                return new ConstantPoolEntry(tag, dis.readUnsignedShort());
            case 9:
            case 10:
            case 11:
                return new ConstantPoolEntry(tag, dis.readUnsignedShort(), dis.readUnsignedShort());
            case 8:
                return new ConstantPoolEntry(tag, dis.readUnsignedShort());
            case 3:
                return new ConstantPoolEntry(tag, dis.readInt());
            case 4:
                return new ConstantPoolEntry(tag, dis.readFloat());
            case 5:
                return new ConstantPoolEntry(tag, dis.readLong());
            case 6:
                return new ConstantPoolEntry(tag, dis.readDouble());
            case 12:
                return new ConstantPoolEntry(tag, dis.readUnsignedByte(), dis.readUnsignedByte());
            case 1:
                int length = dis.readUnsignedShort();
                byte[] bytes = new byte[length];
                dis.readFully(bytes);
                return new ConstantPoolEntry(tag, new String(bytes, "UTF-8"));
            default:

                throw new IOException("Invalid tag (constant pool): " + tag);
        }

    }

}
