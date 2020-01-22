package com.axway.itrv.exp;

import com.axway.itrv.exp.impl.BinaryOp;
import com.axway.itrv.exp.impl.Constant;
import com.axway.itrv.exp.impl.UnaryOp;

public interface ExpressionVisitor<T> {
    T visit(Constant constant);

    T visit(BinaryOp binaryOp);

    T visit(UnaryOp binaryOp);
}
