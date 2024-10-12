package com.mavenforge.Decoders.Custom.ClassFile;

public class MethodNode {
    public int accessFlags;
    public int nameIndex;
    public int descriptorIndex;
    public AttributeNode[] attributes;

    public MethodNode(int accessFlags, int nameIndex, int descriptorIndex, AttributeNode[] attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }
}