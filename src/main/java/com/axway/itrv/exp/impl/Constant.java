package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.Expression;
import com.axway.itrv.exp.ExpressionVisitor;

public class Constant implements Expression {

    private double value;

    public Constant(int value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
