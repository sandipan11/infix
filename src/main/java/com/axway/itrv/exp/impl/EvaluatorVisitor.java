package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.ExpressionVisitor;

// TODO implement an expression evaluator

public class EvaluatorVisitor implements ExpressionVisitor<Double> {

    @Override
    public Double visit(Constant constant) {
      // TODO
        return constant.getValue();
    }

    @Override
    public Double visit(BinaryOp binaryOp) {
        var left = binaryOp.getLeft().accept(this);
        var right = binaryOp.getRight().accept(this);

        switch (binaryOp.getType()) {
            case ADD:
                return left + right;
            case SUB:
                return left - right;
            case MUL:
                return left * right;
            case DIV:
                return left / right;
        }
        throw new IllegalStateException();
    }

    @Override
    public Double visit(UnaryOp unaryOp) {

        switch (unaryOp.getType()) {
            case NEGATION:
                return -unaryOp.getOperand().accept(this);
        }

        throw new IllegalStateException();
    }
}
