package com.axway.itrv.exp.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;

import com.axway.itrv.exp.Expression;

/**
 * Infix parser parses an infix expression and creates an AST.
 *
 * (2*(-3))+4/2
 */
public class InfixParser {

    // parses a possible operand of a unary expression (a constant or a paranthesized expression)
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

    // parses an operand, paranthesized expression or a unary operand
    private Expression parseTerm(Tokenizer tokenizer) throws IOException, ParseException {
        var token = tokenizer.peek();
        if (token.getType() == Token.Type.SUB) {
            tokenizer.next();
            return new UnaryOp(UnaryOp.Type.NEGATION, parsePosTerm(tokenizer));
        }

        return parsePosTerm(tokenizer);
    }

    // parses an expression with a certain level of precedence
    // there are two levels of precedence
    // 0 - addition and substraction
    // 1 - multiplication and division
    // for a certain level of precedence there should be a left term :
    // - an expression of a higher level of precedence
    // - a unary operation
    // - a plain constant
    // - a paranthesized expression
    // if a binary operator follows the right term then the right term can be an expression of the same level of precedence
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
