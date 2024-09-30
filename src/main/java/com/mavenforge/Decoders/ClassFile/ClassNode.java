package com.mavenforge.Decoders.ClassFile;

public class ClassNode {
    public int minorVersion;
    public int majorVersion;
    public int constantPoolCount;
    public int accessFlags;
    public int thisClass;
    public int superClass;
    public int[] interfaces;
    public FieldNode[] fields;
    public MethodNode[] methods;

    public ClassNode(int minorVersion, int majorVersion, int constantPoolCount, int accessFlags, int thisClass,
            int superClass, int[] interfaces, FieldNode[] fields, MethodNode[] methods) {
        this.minorVersion = minorVersion;
        this.majorVersion = majorVersion;
        this.constantPoolCount = constantPoolCount;
        this.accessFlags = accessFlags;
        this.thisClass = thisClass;
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "ClassNode{" +
                "minorVersion=" + minorVersion +
                ", majorVersion=" + majorVersion +
                ", constantPoolCount=" + constantPoolCount +
                ", accessFlags=" + accessFlags +
                ", thisClass=" + thisClass +
                ", superClass=" + superClass +
                ", interfaces=" + interfaces.length +
                ", fields=" + fields.length +
                ", methods=" + methods.length +
                '}';
    }
}