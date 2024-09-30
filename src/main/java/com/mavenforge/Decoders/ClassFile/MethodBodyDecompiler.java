package com.mavenforge.Decoders.ClassFile;

import java.util.Map;

public class MethodBodyDecompiler {

    private Map<Integer, String> constantPool;

    public MethodBodyDecompiler(Map<Integer, String> constantPool) {
        this.constantPool = constantPool;
    }

    public String parseBytecode(byte[] code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length; i++) {
            int opcode = code[i] & 0xFF;
            switch (opcode) {
                case 0xB1: // return
                    sb.append("return;\n");
                    break;
                case 0x2A: // aload_0
                    sb.append("this");
                    break;
                case 0x2B: // aload_1
                    sb.append("arg1");
                    break;
                case 0x2C: // aload_2
                    sb.append("arg2");
                    break;
                case 0xB2: // getstatic
                    int indexB2 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("getstatic ").append(getFieldName(indexB2)).append(";\n");
                    break;
                case 0xB3: // putstatic
                    int indexB3 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("putstatic ").append(getFieldName(indexB3)).append(";\n");
                    break;
                case 0xB4: // getfield
                    int indexB4 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("getfield ").append(getFieldName(indexB4)).append(";\n");
                    break;
                case 0xB5: // putfield
                    int indexB5 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("putfield ").append(getFieldName(indexB5)).append(";\n");
                    break;
                case 0xB6: // invokevirtual
                    int indexB6 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("invokevirtual ").append(getMethodName(indexB6)).append(";\n");
                    break;
                case 0xB7: // invokespecial
                    int indexB7 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("invokespecial ").append(getMethodName(indexB7)).append(";\n");
                    break;
                case 0xB8: // invokestatic
                    int indexB8 = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("invokestatic ").append(getMethodName(indexB8)).append(";\n");
                    break;
                case 0x12: // ldc
                    int index12 = code[++i] & 0xFF;
                    sb.append("ldc ").append(getConstant(index12)).append(";\n");
                    break;
                case 0x57: // pop
                    sb.append("pop;\n");
                    break;
                case 0xBB: // new
                    int indexBB = ((code[++i] & 0xFF) << 8) | (code[++i] & 0xFF);
                    sb.append("new ").append(getClassName(indexBB)).append(";\n");
                    break;
                // Add more cases for other opcodes as needed
                default:
                    sb.append(String.format("%02X ", opcode));
                    break;
            }
        }
        return sb.toString().trim();
    }

    private String getFieldName(int index) {
        String value = constantPool.get(index);
        if (value != null && value.startsWith("Field:")) {
            int classIndex = Integer.parseInt(value.substring(6, value.indexOf(':', 6)));
            int nameAndTypeIndex = Integer.parseInt(value.substring(value.indexOf(':', 6) + 1));
            String className = getClassName(classIndex);
            String nameAndType = constantPool.get(nameAndTypeIndex);
            if (nameAndType != null && nameAndType.startsWith("NameAndType:")) {
                int nameIndex = Integer.parseInt(nameAndType.substring(12, nameAndType.indexOf(':', 12)));
                int descriptorIndex = Integer.parseInt(nameAndType.substring(nameAndType.indexOf(':', 12) + 1));
                String fieldName = constantPool.get(nameIndex);
                String fieldDescriptor = constantPool.get(descriptorIndex);
                return className + "." + fieldName + " : " + fieldDescriptor;
            }
        }
        return "UnknownField";
    }

    private String getConstant(int index) {
        String value = constantPool.get(index);
        if (value != null) {
            if (value.startsWith("String:")) {
                int stringIndex = Integer.parseInt(value.substring(7));
                return "\"" + constantPool.get(stringIndex) + "\"";
            } else if (value.startsWith("Integer:") || value.startsWith("Float:") || value.startsWith("Long:")
                    || value.startsWith("Double:")) {
                return value.substring(value.indexOf(':') + 1);
            }
        }
        return "UnknownConstant";
    }

    private String getClassName(int index) {
        String value = constantPool.get(index);
        if (value != null && value.startsWith("Class:")) {
            int nameIndex = Integer.parseInt(value.substring(6));
            return constantPool.get(nameIndex);
        }
        return "UnknownClass";
    }

    private String getMethodName(int index) {
        String value = constantPool.get(index);
        if (value != null && value.startsWith("Method:")) {
            int classIndex = Integer.parseInt(value.substring(7, value.indexOf(':', 7)));
            int nameAndTypeIndex = Integer.parseInt(value.substring(value.indexOf(':', 7) + 1));
            String className = getClassName(classIndex);
            String nameAndType = constantPool.get(nameAndTypeIndex);
            if (nameAndType != null && nameAndType.startsWith("NameAndType:")) {
                int nameIndex = Integer.parseInt(nameAndType.substring(12, nameAndType.indexOf(':', 12)));
                int descriptorIndex = Integer.parseInt(nameAndType.substring(nameAndType.indexOf(':', 12) + 1));
                String methodName = constantPool.get(nameIndex);
                String methodDescriptor = constantPool.get(descriptorIndex);
                return className + "." + methodName + methodDescriptor;
            }
        }
        return "UnknownMethod";
    }
}