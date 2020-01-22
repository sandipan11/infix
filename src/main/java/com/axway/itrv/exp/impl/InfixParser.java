package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.Expression;
import jdk.jshell.spi.ExecutionControl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Infix parser parses an infix expression and creates an AST.
 *
 * (2*(-3))+4/2
 */
public class InfixParser {

    private Expression parsePosTerm(Tokenizer tokenizer) throws IOException, ParseException {
        var token = tokenizer.peek();
        switch (tokenizer.peek().getType()) {
            case LPAREN:
                tokenizer.next();
                var exp = parseExpression(tokenizer, 0);
                if (tokenizer.peek().getType() != Token.Type.RPAREN) {
                    throw new ParseException(String.format("Unexpected token: %s", token), 0);
                }
                tokenizer.next();
                return exp;
            case OPERAND:
                var res = new Constant(token.getValue());
                tokenizer.next();
                return res;
            default:
                throw new ParseException(String.format("Unexpected token: %s", token), 0);
        }
    }

    private Expression parseTerm(Tokenizer tokenizer) throws IOException, ParseException {
        var token = tokenizer.peek();
        if (token.getType() == Token.Type.SUB) {
            tokenizer.next();
            return new UnaryOp(UnaryOp.Type.NEGATION, parsePosTerm(tokenizer));
        }

        return parsePosTerm(tokenizer);
    }

    private Expression parseExpression(Tokenizer tokenizer, int precedence) throws IOException, ParseException {
        var left = precedence <= 1 ? parseExpression(tokenizer, precedence+1) : parseTerm(tokenizer);
        var token = tokenizer.peek();
        switch (token.getType()) {
            case EOF:
            case RPAREN:
                return left;
            case ADD:
            case SUB:
                if (precedence > 0) {
                    return left;
                }
                break;
            case DIV:
            case MUL:
                if (precedence > 1) {
                    return left;
                }
                break;
            default:
                throw new ParseException(String.format("Unexpected token: %s", token), 0);
        }
        tokenizer.next();
        return new BinaryOp(token.getType(), left, parseExpression(tokenizer, precedence));
    }

    public Expression parse(String input) throws IOException, ParseException {
            var tokenizer = new Tokenizer(new BufferedInputStream(new ByteArrayInputStream(input.getBytes())));

            var result = parseExpression(tokenizer, 0);

            if (tokenizer.peek().getType() != Token.Type.EOF) {
                throw new ParseException(String.format("Expected EOF"), 0);
            }

            return result;
    };
}
