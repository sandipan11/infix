package com.axway.itrv.exp.impl;

import java.util.Objects;

public class Token {
    private Type type;
    private int value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return value == token.value &&
                type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    public enum Type {
        OPERAND,
        ADD,
        SUB,
        MUL,
        DIV,
        LPAREN,
        RPAREN,
        EOF;
    }

    public Token(int val) {
        this.type = Type.OPERAND;
        this.value = val;
    }

    public Token(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    Type getType() {
        return type;
    }

    int getValue() {
        return value;
    }
}
