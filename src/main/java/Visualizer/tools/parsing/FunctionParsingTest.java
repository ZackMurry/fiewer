package Visualizer.tools.parsing;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
    Unit testing :(
 */

class FunctionParsingTest {

    @Nested
    class determineFirstTermTest {
        @Test
        void digitTest() {
            assertEquals("digit", FunctionParsing.determineFirstTermType("12x^2+2x+6"));
            assertEquals("digit", FunctionParsing.determineFirstTermType("-2x"));
        }

        @Test
        void xpowTest() {
            assertEquals("xpow.2", FunctionParsing.determineFirstTermType("x^2+2x+5"));
        }

        @Test
        void xTest() {
            assertEquals("x", FunctionParsing.determineFirstTermType("x+6"));
        }

        @Test
        void digpowxTest() {
            assertEquals("digpow.x", FunctionParsing.determineFirstTermType("3^x"));
            assertEquals("digpow.digx", FunctionParsing.determineFirstTermType("3^3x"));
            assertEquals("digpow.digx", FunctionParsing.determineFirstTermType("3^-3x"));
            assertEquals("digit", FunctionParsing.determineFirstTermType("2*3^x"));
            assertEquals("multiply", FunctionParsing.determineFirstTermType("*3^x"));
        }

        @Test
        void sinTest() {
            assertEquals("sin.x", FunctionParsing.determineFirstTermType("sin(x)"));
            assertEquals("sin.digx", FunctionParsing.determineFirstTermType("sin(3x)"));
            assertEquals("sin.dig", FunctionParsing.determineFirstTermType("sin(3)"));

        }

        @Test
        void cosTest() {
            assertEquals("cos.x", FunctionParsing.determineFirstTermType("cos(x)"));
            assertEquals("cos.digx", FunctionParsing.determineFirstTermType("cos(3x)"));
            assertEquals("cos.dig", FunctionParsing.determineFirstTermType("cos(3)"));

        }

        @Test
        void cubicTest() {
            assertEquals("xpow.3_xpow.2", FunctionParsing.getFunctionTerms("x^3+x^2"));
        }

        @Test
        void logTest() {
            assertEquals("log,dig.x", FunctionParsing.determineFirstTermType("log_2(x)"));
            assertEquals("log,dig.digx", FunctionParsing.determineFirstTermType("log_14(-x)"));
            assertEquals("log,dig.digx", FunctionParsing.determineFirstTermType("log_14(-15x)"));
            assertEquals("log.x", FunctionParsing.determineFirstTermType("log(x)"));
            assertEquals("log.digx", FunctionParsing.determineFirstTermType("log(3x)"));
            assertEquals("log.digx", FunctionParsing.determineFirstTermType("log(-x)"));
        }

        @Test
        void absTest() {
            assertEquals("abs.x", FunctionParsing.determineFirstTermType("abs(x)"));
            assertEquals("abs.digx", FunctionParsing.determineFirstTermType("abs(3x)"));

        }

    }



    @Test
    void skipToNextTermTest() {
        assertEquals("x^2+2x+6", FunctionParsing.skipToNextTerm("12x^2+2x+6"));
        assertEquals("+6", FunctionParsing.skipToNextTerm("x+6"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("2^x+3"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("sin(x)+3"));
        assertEquals("+4", FunctionParsing.skipToNextTerm("sin(3)+4"));
        assertEquals("-7", FunctionParsing.skipToNextTerm("sin(3x)-7"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("cos(x)+3"));
        assertEquals("+4", FunctionParsing.skipToNextTerm("cos(3)+4"));
        assertEquals("-7", FunctionParsing.skipToNextTerm("cos(3x)-7"));
        assertEquals("+x^2", FunctionParsing.skipToNextTerm("x^3+x^2"));
        assertEquals("", FunctionParsing.skipToNextTerm("+x^2"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("log(x)+3"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("log_2(x)+3"));
        assertEquals("-3", FunctionParsing.skipToNextTerm("log_2(x)-3"));
        assertEquals("-5", FunctionParsing.skipToNextTerm("log_3(2x)-5"));
        assertEquals("+7", FunctionParsing.skipToNextTerm("log(3x)+7"));
        assertEquals("+log(x)", FunctionParsing.skipToNextTerm("log(3x)+log(x)"));
        assertEquals("-log(x)", FunctionParsing.skipToNextTerm("log(3x)-log(x)"));
        assertEquals("+3", FunctionParsing.skipToNextTerm("abs(x)+3"));
        assertEquals("-3", FunctionParsing.skipToNextTerm("abs(x)-3"));
        assertEquals("-7", FunctionParsing.skipToNextTerm("abs(3x)-7"));


    }

    @Test
    void getFunctionTermsTest() {
        assertEquals("digit_xpow.2_digit_x_digit", FunctionParsing.getFunctionTerms("3x^2+6x+2"));
        assertEquals("digit_x_digit", FunctionParsing.getFunctionTerms("3x+2"));
        assertEquals("digit_digpow.x", FunctionParsing.getFunctionTerms("3*3^x"));
        assertEquals("digit_xpow.5_digit", FunctionParsing.getFunctionTerms("2x^5+5"));
        assertEquals("xpow.2_digit", FunctionParsing.getFunctionTerms("x^2+2"));
        assertEquals("digit_log,dig.x_digit", FunctionParsing.getFunctionTerms("3log_3(x)+2"));
        assertEquals("abs.x_abs.digx", FunctionParsing.getFunctionTerms("abs(x)+abs(3x)"));
    }

    @Nested
    class FunctionTypeTests {

        @Test
        void linearTest() {
            assertEquals("linear", FunctionParsing.getFunctionType("3x+2"));
            assertEquals("linear", FunctionParsing.getFunctionType("3x-5"));
            assertEquals("linear", FunctionParsing.getFunctionType("3x"));
            assertEquals("linear", FunctionParsing.getFunctionType("x+2"));
            assertEquals("linear", FunctionParsing.getFunctionType("2"));
        }

        @Test
        void quadraticTest() {
            assertEquals("quadratic", FunctionParsing.getFunctionType("2x^2+4x+8"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("x^2+4x+8"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("2x^2-8"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("x^2+4"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("x^2+4x"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("x^2"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("2x^2"));
            assertEquals("quadratic", FunctionParsing.getFunctionType("-x^2+2x"));


        }

        @Test
        void isCubicTest() {
            assertTrue(FunctionParsing.isCubic("digit_xpow.3_digit_xpow.2_digit_x_digit"));
            assertTrue(FunctionParsing.isCubic("xpow.3_digit_xpow.2_digit_x_digit"));
            assertTrue(FunctionParsing.isCubic("digit_xpow.3_xpow.2_digit_x_digit"));
            assertTrue(FunctionParsing.isCubic("digit_xpow.3_digit_xpow.2_x_digit"));
            assertTrue(FunctionParsing.isCubic("digit_xpow.3_digit"));
            assertTrue(FunctionParsing.isCubic("xpow.3_xpow.2"));

            assertFalse(FunctionParsing.isCubic("digit_xpow.2_digit"));
            assertFalse(FunctionParsing.isCubic("xpow.4_digit"));

        }

        @Test
        void isExponentialTest() {
            assertTrue(FunctionParsing.isExponential("digit_digpow.x"));
            assertTrue(FunctionParsing.isExponential("digpow.x"));
            assertTrue(FunctionParsing.isExponential("digpow.digx"));

            assertFalse(FunctionParsing.isExponential("digit_x"));
            assertFalse(FunctionParsing.isExponential("xpow.2"));

        }

        @Test
        void isSinTest() {
            assertTrue(FunctionParsing.isSin("sin.x"));
            assertTrue(FunctionParsing.isSin("sin.digx"));
            assertTrue(FunctionParsing.isSin("sin.dig"));

            assertEquals("sin", FunctionParsing.getFunctionType("sin(3x)"));

        }

        @Test
        void isCosTest() {
            assertTrue(FunctionParsing.isCos("cos.x"));
            assertTrue(FunctionParsing.isCos("cos.digx"));
            assertTrue(FunctionParsing.isCos("cos.dig"));

            assertEquals("cos", FunctionParsing.getFunctionType("cos(3x)"));

        }

        @Test
        void isXNTest() {
            assertTrue(FunctionParsing.isXN("digit_xpow.4_digit"));
            assertTrue(FunctionParsing.isXN("xpow.3_digit"));
            assertTrue(FunctionParsing.isXN("digit_xpow.4"));
            assertTrue(FunctionParsing.isXN("xpow.5"));

            assertFalse(FunctionParsing.isXN("digit_x_digit"));

        }

        @Test
        void logTest() {
            assertTrue(FunctionParsing.isLog("digit_log,dig.digx_digit"));
        }

    }



    @Test
    void nextTermAsStringTest() {
        assertEquals("3", FunctionParsing.nextTermAsString("3x+2"));
        assertEquals("-1", FunctionParsing.nextTermAsString("-x+1")); //nice
        assertEquals("x^2", FunctionParsing.nextTermAsString("x^2+3x+2"));
    }

    @Test
    void nextDigitAsStingTest() {
        assertEquals("3", FunctionParsing.nextDigitAsString("3x+2"));
        assertEquals("2", FunctionParsing.nextDigitAsString("x^2+2"));
        assertEquals("5",FunctionParsing.nextDigitAsString("5x"));
        assertEquals("-5", FunctionParsing.nextDigitAsString("-5x+2"));
        assertEquals("3", FunctionParsing.nextDigitAsString("3sin(x)"));
    }



    @Test
    void skipToNextTermTypeTest() {
        assertEquals("digit_x", FunctionParsing.skipToNextTermType("xpow.3_digit_x"));
        assertEquals("x", FunctionParsing.skipToNextTermType("xpow.2_x"));
        assertEquals("x_digit", FunctionParsing.skipToNextTermType("digit_x_digit"));
        assertEquals("digit", FunctionParsing.skipToNextTermType("sin.x_digit"));
        assertEquals("digit", FunctionParsing.skipToNextTermType("sin.digx_digit"));
        assertEquals("x", FunctionParsing.skipToNextTermType("sin.dig_x"));
        assertEquals("", FunctionParsing.skipToNextTermType("sin.x"));
        assertEquals("x", FunctionParsing.skipToNextTermType("cos.dig_x"));
        assertEquals("", FunctionParsing.skipToNextTermType("cos.x"));
        assertEquals("xpow.5", FunctionParsing.skipToNextTermType("digit_xpow.5"));
        assertEquals("digit", FunctionParsing.skipToNextTermType("xpow.5_digit"));
    }

    @Test
    void getFirstDigitOfDigpowTest() {
        assertEquals("12", FunctionParsing.getFirstDigitOfDigpow("12^x"));
        assertEquals("2", FunctionParsing.getFirstDigitOfDigpow("2^x"));
    }

    @Test
    void getSecondDigitOfDigpowTest() {
        assertEquals("3", FunctionParsing.getSecondDigitOfDigpow("2^3x"));
        assertEquals("-3", FunctionParsing.getSecondDigitOfDigpow("-2^-3x"));
    }

    @Test
    void getDigInSinTest() {
        assertEquals("3", FunctionParsing.getDigInSin("sin(3x)"));
        assertEquals("-3", FunctionParsing.getDigInSin("sin(-3x)"));
        assertEquals("-1", FunctionParsing.getDigInSin("sin(-x)"));
        assertEquals("1", FunctionParsing.getDigInSin("sin(1)"));
    }

    @Test
    void getDigInDigPowTest() {
        assertEquals("2", FunctionParsing.getDigInXpow("xpow.2"));
        assertEquals("-2", FunctionParsing.getDigInXpow("xpow.-2"));
        assertEquals("5", FunctionParsing.getDigInXpow("xpow.5"));
        assertEquals("19", FunctionParsing.getDigInXpow("xpow.19"));
        assertEquals("-19.5", FunctionParsing.getDigInXpow("xpow.-19.5"));

    }

    @Test
    void getBaseInLogTest() {
        assertEquals("2", FunctionParsing.getBaseInLog("log_2(x)"));
        assertEquals("-122", FunctionParsing.getBaseInLog("log_-122(34x)"));
        assertEquals("10", FunctionParsing.getBaseInLog("log(x)"));
    }

    @Test
    void getCoefficientInLogTest() {
        assertEquals("2", FunctionParsing.getCoefficientInLog("log_4(2x)"));
        assertEquals("-2", FunctionParsing.getCoefficientInLog("log_10(-2x)"));
        assertEquals("-5.3", FunctionParsing.getCoefficientInLog("log_7(-5.3x)"));
        assertEquals("-5.3", FunctionParsing.getCoefficientInLog("log(-5.3)"));
        assertEquals("-1", FunctionParsing.getCoefficientInLog("log(-x)"));
    }
}