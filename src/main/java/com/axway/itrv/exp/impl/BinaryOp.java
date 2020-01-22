package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.Expression;
import com.axway.itrv.exp.ExpressionVisitor;

public class BinaryOp implements Expression {

    static enum OperatorType {
        MUL,
        DIV,
        ADD,
        SUB,
    }

    static OperatorType fromTokenType(Token.Type tokenType) {
        switch (tokenType) {
            case DIV:
                return OperatorType.DIV;
            case SUB:
                return OperatorType.SUB;
            case MUL:
                return OperatorType.MUL;
            case ADD:
                return OperatorType.ADD;
        }
        throw new IllegalArgumentException("Unexpected argument: " + tokenType);
    }

    public BinaryOp(Token.Type tokenType, Expression left, Expression right) {
        this.operator = fromTokenType(tokenType);
        this.left = left;
        this.right = right;
    }

    private Expression left;
    private Expression right;
    private OperatorType operator;

    public OperatorType getType() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
