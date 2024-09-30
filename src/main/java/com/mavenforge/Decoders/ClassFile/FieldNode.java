package com.mavenforge.Decoders.ClassFile;

public class FieldNode {
    public int accessFlags;
    public int nameIndex;
    public int descriptorIndex;
    public AttributeNode[] attributes;

    public FieldNode(int accessFlags, int nameIndex, int descriptorIndex, AttributeNode[] attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }
}