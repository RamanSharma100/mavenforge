package com.mavenforge.Decoders.ClassFile;

public class AttributeNode {
    public int attributeNameIndex;
    public int attributeLength;
    public byte[] info;

    public AttributeNode(int attributeNameIndex, int attributeLength, byte[] info) {
        this.attributeNameIndex = attributeNameIndex;
        this.attributeLength = attributeLength;
        this.info = info;
    }
}