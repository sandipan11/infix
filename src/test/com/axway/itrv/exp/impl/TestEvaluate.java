package com.axway.itrv.exp.impl;

import com.axway.itrv.exp.Expression;
import org.junit.jupiter.api.Test;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class TestEvaluate {

    class TestCase {
        String exp;
        double result;

        public TestCase(String exp, double result) {
            this.exp = exp;
            this.result = result;
        }
    }

    @Test
    public void testEval() throws Exception {
        InfixParser parser = new InfixParser();
        TestCase[] testCases = {
                new TestCase("1", 1),
                new TestCase("1+2", 3),
                new TestCase("1-2", -1),
                new TestCase("-1",  -1),
                new TestCase("2*3+1", 7),
                new TestCase("2*(3+1)",  8),
                new TestCase("2*(-3+1)", -4),
        };


        for (var testCase: testCases) {
            try {
                assertEquals(testCase.result, parser.parse(testCase.exp).accept(new EvaluatorVisitor()), String.format("Expected %s=%.2f", testCase.exp, testCase.result));
            }
            catch (Exception e) {
                throw new Exception("Failed to parse/evaluate "  + testCase.exp, e);
            }
        };
    }
}
