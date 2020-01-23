package com.axway.itrv.exp.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestEvaluate {

    class TestCase {
        String exp;
        double expected;

        public TestCase(String exp, double expected) {
            this.exp = exp;
            this.expected = expected;
        }
    }

    @Test
    public void workingTests() throws Exception {
        var parser = new InfixParser();
        var evaluator = new EvaluatorVisitor();
        
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
                // Parse the expression
                var expression = parser.parse(testCase.exp);
                double result = expression.accept(evaluator);

                // Evaluate expression
                assertEquals(testCase.expected,
                             result,
                             String.format("Expected %s=%.2f", testCase.exp, testCase.expected));
            }
            catch (Exception e) {
                throw new Exception("Failed to parse/evaluate " + testCase.exp, e);
            }
        };
    }

    @Test
    public void whitespaceTest() throws Exception {
        var parser = new InfixParser();
        var evaluator = new EvaluatorVisitor();

        TestCase[] testCases = {
            new TestCase(" 1", 1),
            new TestCase("1 ", 1),
            new TestCase("(1  + 2)", 3),
        };

        for (var testCase: testCases) {
            try {
                // Parse the expression
                var expression = parser.parse(testCase.exp);
                double result = expression.accept(evaluator);

                // Evaluate expression
                assertEquals(testCase.expected,
                             result,
                             String.format("Expected %s=%.2f", testCase.exp, testCase.expected));
            }
            catch (Exception e) {
                throw new Exception("Failed to parse/evaluate "  + testCase.exp, e);
            }
        };
    }

    @Test
    public void factorialTest() throws Exception {
        var parser = new InfixParser();
        var evaluator = new EvaluatorVisitor();
                
        TestCase[] testCases = {
            new TestCase("5!", 1),
            new TestCase("(-1  + 2*3!)", 3),
        };

        for (var testCase: testCases) {
            try {
                // Parse the expression
                var expression = parser.parse(testCase.exp);
                double result = expression.accept(evaluator);

                // Evaluate expression
                assertEquals(testCase.expected,
                             result,
                             String.format("Expected %s=%.2f", testCase.exp, testCase.expected));
            }
            catch (Exception e) {
                throw new Exception("Failed to parse/evaluate " + testCase.exp, e);
            }
        };
    }
}
