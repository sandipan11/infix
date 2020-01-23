package com.axway.itrv.exp.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;


/**
  * Tokenizer is a 1 char lookahead lexer that creates a string of Tokens from an input stream.
  */
public class Tokenizer  {

    private BufferedInputStream stream;

    public Tokenizer(BufferedInputStream stream) {
        this.stream = stream;
    }

    private Token last;
    private Token readOperand(int c) throws IOException {
        int val = 0;

        while (Character.isDigit(c)) {
            val = c - '0' + val * 10;
            stream.mark(1);
            c = stream.read();
        }
        stream.reset();

        return new Token(val);
    }


    private Token nextHelper() throws IOException, ParseException {
        int c = stream.read();
        switch (c) {
            case -1:
                return new Token(Token.Type.EOF);
            case '+':
                return new Token(Token.Type.ADD);
            case '/':
                return new Token(Token.Type.DIV);
            case '*':
                return new Token(Token.Type.MUL);
            case '-':
                return new Token(Token.Type.SUB);
            case '(':
                return new Token(Token.Type.LPAREN);
            case ')':
                return new Token(Token.Type.RPAREN);
            case '0':
            case '1':
            case '2':
            case '3':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return readOperand(c);
            default:
                throw new ParseException(String.format("Unexpected character: \'%c\'", c), 0);
        }
    }

    /**
     * Tries to read another token and returns it.
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Token peek() throws  IOException, ParseException {
        if (last == null) {
            last = nextHelper();
        }

        return last;
    }


    /**
     * Forces peek to return the next character.
     * @return
     */
    public void next() {
        last = null;
    }
}
