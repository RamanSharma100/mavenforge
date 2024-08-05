package com.mavenforge.Decoders;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassfileDecoder {
    public String classFilePath = "";
    public int minorVersion = 0, majorVersion = 0;

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

    public String decode() {
        StringBuilder sb = new StringBuilder();
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
                constantPool[i] = readPoolEntry(dis);

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

            sb.append(getFormattedAccessFlags(accessFlags))
                    .append("class ")
                    .append(getConstantPoolClassName(thisClass, constantPool));

            if (superClass != 0 && superClass != 1) {
                sb.append(" extends ")
                        .append(getConstantPoolClassName(superClass, constantPool));
            }

            if (interfacesCount > 0) {
                sb.append(" implements ");
                for (int i = 0; i < interfacesCount; i++) {
                    sb.append(getConstantPoolClassName(interfaces[i], constantPool));
                    if (i < interfacesCount - 1) {
                        sb.append(", ");
                    }
                }
            }

            sb.append(" {\n");

            for (FieldInfo field : fields) {
                sb.append(field).append("\n");
            }

            for (MethodInfo method : methods) {
                sb.append(method).append("\n");
            }

            sb.append("}");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    private static ConstantPoolEntry readPoolEntry(DataInputStream dis) throws IOException {
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
                return new ConstantPoolEntry(tag, dis.readUnsignedShort(), dis.readUnsignedShort());
            case 1:
                int length = dis.readUnsignedShort();
                byte[] bytes = new byte[length];
                dis.readFully(bytes);
                return new ConstantPoolEntry(tag, new String(bytes, "UTF-8"));
            default:
                throw new IOException("Invalid tag (constant pool): " + tag);
        }

    }

    private static String getFormattedAccessFlags(int accessFlags) {
        StringBuilder sb = new StringBuilder();

        if ((accessFlags & 0x0001) != 0)
            sb.append("public ");
        if ((accessFlags & 0x0010) != 0)
            sb.append("final ");
        if ((accessFlags & 0x0020) != 0)
            sb.append("super ");
        if ((accessFlags & 0x0200) != 0)
            sb.append("interface ");
        if ((accessFlags & 0x0400) != 0)
            sb.append("abstract ");
        if ((accessFlags & 0x1000) != 0)
            sb.append("synthetic ");
        if ((accessFlags & 0x2000) != 0)
            sb.append("annotation ");
        if ((accessFlags & 0x4000) != 0)
            sb.append("enum ");

        return sb.toString();
    }

    private static String getConstantPoolClassName(int index, ConstantPoolEntry[] constantPool) {
        if (index == 0 || index >= constantPool.length) {
            return "";
        }

        ConstantPoolEntry entry = constantPool[index];
        if (entry.getTag() == 7) {
            int nameIndex = (int) entry.getValue();
            entry = constantPool[nameIndex];
            if (entry.getTag() == 1) {
                return (String) entry.getValue();
            }
        }
        return "";
    }
}
