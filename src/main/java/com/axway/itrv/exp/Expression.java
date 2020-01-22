package com.axway.itrv.exp;

public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor);
}
