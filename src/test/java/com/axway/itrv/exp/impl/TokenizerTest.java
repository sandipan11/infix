package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.impl.Token;
import com.axway.itrv.exp.impl.Tokenizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;


public class TokenizerTest {

    private BufferedInputStream bufferedStream(String input) {
        return new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    public void testTokenizer() throws IOException, ParseException {
        var tokenizer = new Tokenizer(bufferedStream(("1+2/33")));

        assertEquals(tokenizer.peek(), new Token(1));
        assertEquals(tokenizer.peek(), new Token(1));
        tokenizer.next();
        assertEquals(tokenizer.peek(), new Token(Token.Type.ADD));
        assertEquals(tokenizer.peek(), new Token(Token.Type.ADD));
        tokenizer.next();
        assertEquals(tokenizer.peek(), new Token(2));
        tokenizer.next();
        assertEquals(tokenizer.peek(), new Token(Token.Type.DIV));
        tokenizer.next();
        assertEquals(tokenizer.peek(), new Token(33));
        tokenizer.next();
        assertEquals(tokenizer.peek(), new Token(Token.Type.EOF));
        tokenizer.next();
    }
}
