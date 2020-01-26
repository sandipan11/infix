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
    // - a factor
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
            case FACT:
            	 if (precedence > 1) {
            		 
            		 Tokenizer tokenizerFact = null;
            		 if(left instanceof Constant) {
            			 tokenizerFact = new Tokenizer(new BufferedInputStream(new ByteArrayInputStream(generateFactorialExpression((int)((Constant)left).getValue()).getBytes())));
            		 }
            		 
            	     if(left instanceof UnaryOp) { 
            	    	 Constant unaryObj =  (Constant)((UnaryOp)left).getOperand();
            	    	 
            			 tokenizerFact = new Tokenizer(new BufferedInputStream(new ByteArrayInputStream(generateFactorialExpression((int)unaryObj.getValue()).getBytes())));
            		 }
            	   
            	     if(left instanceof BinaryOp) { 
            	    	 BinaryOp binaryOp =  ((BinaryOp)left);
            	    	 
            			 tokenizerFact = new Tokenizer(new BufferedInputStream(new ByteArrayInputStream(generateFactorialExpression(binaryOp).getBytes())));
            		 }

            		 tokenizer.next();
            		 return new BinaryOp(token.getType().MUL, left, parseExpression(tokenizerFact, 1));
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
    
    
    /**
     * This method generate expression for factorial if you provide a number, So if left is only number, we can use this method
     * If you prove 3, it will return (3-1)*(3-2)
     * 
     */
    private String generateFactorialExpression(int number) {
    	   	
    	int factNumberCount = 1;
    	
    	StringBuffer expression = new StringBuffer();
    	    	
    	while(number> factNumberCount) {
    		
    		if(factNumberCount>1)
    			expression.append("*");
    		
    		expression.append("(").append(number).append('-').append(factNumberCount).append(")");
    		factNumberCount++;
    	}
    	
    	return expression.toString();
    	
    }
    
    /**
     * This method generate expression for factorial if left expression is of BinaryOp Type
     * 
     */
     private String generateFactorialExpression(BinaryOp binaryOp) {
    	 
    	 int leftNumber = 0;
    	 int rightNumber = 0;
    	 
    	 if(binaryOp.getLeft() instanceof Constant) { 
    		 leftNumber = (int) ((Constant)binaryOp.getLeft()).getValue();
    	 }
    	 
    	 if(binaryOp.getLeft() instanceof UnaryOp) { 
    		 leftNumber = leftNumber - (int) ((Constant)((UnaryOp)binaryOp.getLeft()).getOperand()).getValue();
    	 }
    	 
    	 if(binaryOp.getRight() instanceof Constant) { 
    		 rightNumber = (int) ((Constant)binaryOp.getRight()).getValue();
    	 }
    	 
    	 if(binaryOp.getRight() instanceof UnaryOp) { 
    		 rightNumber = rightNumber - (int) ((Constant)((UnaryOp)binaryOp.getRight()).getOperand()).getValue();
    	 }  	 
    	 
    	 String operation = binaryOp.getType().name();
    	 
    	 int factNumber = 0;
    	 if(operation.equalsIgnoreCase("ADD")) {    	 
    		 factNumber = leftNumber + rightNumber;
    		 operation = "+";
    	 } else if(operation.equalsIgnoreCase("SUB")) {    	 
    		 factNumber = leftNumber - rightNumber;
    		 operation = "-";
    	 }
    	 
    	 if(factNumber<0) {
    		 factNumber = 0 - factNumber;
    	 }
	   	
    	int factNumberCount = 1;
    	
    	StringBuffer expression = new StringBuffer();
    	
    	while(factNumber> factNumberCount) {
    		
    		if(factNumberCount>1)
    			expression.append("*");
    		
    		expression.append("(").append(leftNumber).append(operation).append(rightNumber).append('-').append(factNumberCount).append(")");
    		factNumberCount++;
    	}
    	
    	return expression.toString();
    	
    }
}
