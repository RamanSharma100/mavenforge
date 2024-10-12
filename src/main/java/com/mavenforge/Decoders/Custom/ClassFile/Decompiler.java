package com.mavenforge.Decoders.Custom.ClassFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Decompiler {

    private Map<Integer, String> constantPool = new HashMap<>();

    public ClassNode decompile(String classFilePath) throws IOException {
        System.out.println("Decompiling class file: " + classFilePath);
        Reader reader = new Reader(classFilePath);

        try {
            reader.readU4(); // Skip magic number

            int minorVersion = reader.readU2();
            int majorVersion = reader.readU2();

            int constantPoolCount = reader.readU2();
            // Read constant pool
            for (int i = 1; i < constantPoolCount; i++) {
                int tag = reader.readU1();
                switch (tag) {
                    case 7: // Class reference
                        int nameIndex = reader.readU2();
                        constantPool.put(i, "Class:" + nameIndex);
                        break;
                    case 9: // Field reference
                    case 10: // Method reference
                    case 11: // Interface method reference
                        reader.readU2();
                        reader.readU2();
                        break;
                    case 8: // String reference
                        int stringIndex = reader.readU2();
                        constantPool.put(i, "String:" + stringIndex);
                        break;
                    case 3: // Integer
                    case 4: // Float
                        reader.readU4();
                        break;
                    case 5: // Long
                    case 6: // Double
                        reader.readU4();
                        reader.readU4();
                        i++; // These take up two entries in the constant pool
                        break;
                    case 12: // Name and type descriptor
                        reader.readU2();
                        reader.readU2();
                        break;
                    case 1: // UTF-8 string
                        int length = reader.readU2();
                        byte[] bytes = reader.readBytes(length);
                        constantPool.put(i, new String(bytes));
                        break;
                    default:
                        throw new IOException("Invalid constant pool tag: " + tag);
                }
            }

            int accessFlags = reader.readU2();
            int thisClass = reader.readU2();
            int superClass = reader.readU2();

            int interfacesCount = reader.readU2();
            int[] interfaces = new int[interfacesCount];
            for (int i = 0; i < interfacesCount; i++) {
                interfaces[i] = reader.readU2();
            }

            int fieldsCount = reader.readU2();
            FieldNode[] fields = new FieldNode[fieldsCount];
            for (int i = 0; i < fieldsCount; i++) {
                fields[i] = readField(reader);
            }

            int methodsCount = reader.readU2();
            MethodNode[] methods = new MethodNode[methodsCount];
            for (int i = 0; i < methodsCount; i++) {
                methods[i] = readMethod(reader);
            }

            return new ClassNode(minorVersion, majorVersion, constantPoolCount, accessFlags, thisClass, superClass,
                    interfaces, fields, methods);
        } finally {
            reader.close();
        }
    }

    private FieldNode readField(Reader reader) throws IOException {
        int accessFlags = reader.readU2();
        int nameIndex = reader.readU2();
        int descriptorIndex = reader.readU2();
        int attributesCount = reader.readU2();
        AttributeNode[] attributes = new AttributeNode[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = readAttribute(reader);
        }
        return new FieldNode(accessFlags, nameIndex, descriptorIndex, attributes);
    }

    private MethodNode readMethod(Reader reader) throws IOException {
        int accessFlags = reader.readU2();
        int nameIndex = reader.readU2();
        int descriptorIndex = reader.readU2();
        int attributesCount = reader.readU2();
        AttributeNode[] attributes = new AttributeNode[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = readAttribute(reader);
        }
        return new MethodNode(accessFlags, nameIndex, descriptorIndex, attributes);
    }

    private AttributeNode readAttribute(Reader reader) throws IOException {
        int attributeNameIndex = reader.readU2();
        int attributeLength = reader.readU4();
        byte[] info = reader.readBytes(attributeLength);
        return new AttributeNode(attributeNameIndex, attributeLength, info);
    }

    public String reconstructClass(ClassNode classNode) {
        StringBuilder sb = new StringBuilder();

        // Add class declaration
        sb.append(getAccessFlags(classNode.accessFlags)).append(" class ").append(getClassName(classNode.thisClass))
                .append(" ");

        if (classNode.superClass != 0) {
            sb.append("extends ").append(getClassName(classNode.superClass)).append(" ");
        }

        if (classNode.interfaces.length > 0) {
            sb.append("implements ");
            for (int i = 0; i < classNode.interfaces.length; i++) {
                sb.append(getClassName(classNode.interfaces[i]));
                if (i < classNode.interfaces.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" ");
        }

        sb.append("{\n");

        // Add fields
        for (FieldNode field : classNode.fields) {
            sb.append("    ").append(getAccessFlags(field.accessFlags)).append(" ")
                    .append(getDescriptor(field.descriptorIndex)).append(" ")
                    .append(getName(field.nameIndex)).append(";\n");
        }

        // Add methods
        for (MethodNode method : classNode.methods) {
            sb.append("    ").append(getAccessFlags(method.accessFlags)).append(" ")
                    .append(getDescriptor(method.descriptorIndex)).append(" ")
                    .append(getName(method.nameIndex)).append("() {\n");
            sb.append("        ").append(getMethodBody(method)).append("\n");
            sb.append("    }\n");
        }

        sb.append("}\n");

        return sb.toString();
    }

    private String getMethodBody(MethodNode method) {
        for (AttributeNode attribute : method.attributes) {
            if (constantPool.get(attribute.attributeNameIndex).equals("Code")) {
                Reader reader = new Reader(attribute.info);
                try {
                    int maxStack = reader.readU2();
                    int maxLocals = reader.readU2();
                    int codeLength = reader.readU4();
                    byte[] code = reader.readBytes(codeLength);
                    MethodBodyDecompiler methodBodyDecompiler = new MethodBodyDecompiler(constantPool);
                    return methodBodyDecompiler.parseBytecode(code);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "// Method body (bytecode instructions)";
    }

    private String getClassName(int index) {
        String value = constantPool.get(index);
        if (value != null && value.startsWith("Class:")) {
            int nameIndex = Integer.parseInt(value.substring(6));
            return constantPool.get(nameIndex);
        }
        return "UnknownClass";
    }

    private String getAccessFlags(int accessFlags) {
        // Implement logic to convert access flags to string representation
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
        return sb.toString().trim();
    }

    private String getDescriptor(int index) {
        return constantPool.get(index);
    }

    private String getName(int index) {
        return constantPool.get(index);
    }
}