package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.Expression;
import com.axway.itrv.exp.ExpressionVisitor;

public class UnaryOp implements Expression {

    enum Type {
        NEGATION
    }

    private Type operator;
    private Expression operand;

    public UnaryOp(Type operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Type getType() { return operator; }
    public Expression getOperand() {
        return operand;
    }
}
