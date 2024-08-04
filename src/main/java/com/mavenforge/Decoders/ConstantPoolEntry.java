package com.mavenforge.Decoders;

public class ConstantPoolEntry {
    private final int tag;
    private final Object value;
    private final Object additionalValue;

    public ConstantPoolEntry(int tag, Object value) {
        this(tag, value, null);
        System.out.println("tag: " + tag + ", value: " + value);
    }

    public ConstantPoolEntry(int tag, Object value, Object additionalValue) {
        this.tag = tag;
        this.value = value;
        this.additionalValue = additionalValue;
    }

    public int getTag() {
        System.out.println("tag: " + tag);
        return this.tag;
    }

    public Object getValue() {
        return this.value;
    }

    public Object getAdditionalValue() {
        return this.additionalValue;
    }

    @Override
    public String toString() {
        return "ConstantPollEntry{" +
                "tag=" + tag +
                ", value=" + value +
                ", additionalValue=" + additionalValue +
                '}';
    }

}
