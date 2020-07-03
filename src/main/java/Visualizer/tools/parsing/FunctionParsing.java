package main.java.Visualizer.tools.parsing;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.functions.*;

import java.util.ArrayList;

/*

    this class basically just converts a string to a function object.

    assumes everything is in standard form (might make a class converting stuff to standard form if i literally want to die

    term notation:
    coefficients and lone numbers are called 'digit's
    x^2 is shown as xpow.2
    2^x is shown as digpow.x
    2^2x is shown as digpow.digx
    x^3+x^2+x+1 is shown as xpow.3_xpow.2_x_digit
    2x+1 is shown as digit_x_digit
    sin(3x) is shown as sin.digx
    cos(3x) is shown as cos.digx
    log_3(3x) is shown as log,dig.digx
    abs(3x) is shown as abs.digx

 */



public class FunctionParsing {
    

    public static void stringToFunction(String function) {

        //adjusting some input cases
        if(function.contains("ln(")) function = function.replace("ln", "log_2.71828"); //easiest way to handle ln
        if(function.contains("sqrt(x)")) function = function.replace("sqrt(x)", "x^0.5");
        if(function.contains("e")) function = function.replace("e", "2.71828"); //todo check for other functions that have e in them if i add them


        //first get function type
        String type = getFunctionType(function);

        //get function term types
        String termTypes = getFunctionTerms(function);

        //make a list of numbers to use in the function
        ArrayList<Double> functionVariables = new ArrayList<>();

        /*
            for linear: m, b, where y=mx+b
            for quadratic: a, b, c, where y=ax^2+bx+c
            for cubic: x1, x2, x3, x4, where y=x1*x^3+x2*x^2+x3*x+x4
            for exponential: a, b, c, d where y=ab^(cx)+d
            for xn: a, b, c where y=ax^b+c
            for sin: a, b, c, where y=a*sin(bx)+c
            for cos: a, b, c, where y=a*cos(bx)+c
            for log: a, b, c, d, where y=a*log_b(cx)+d
            for abs: a, b, c, where y=a*abs(bx)+c

            to be continued...

         */

        //then get coefficients

        //linear
        switch (type) {
            case "linear":

                if (termTypes.startsWith("digit")) {
                    if (termTypes.startsWith("digit_")) {
                        //get m
                        try {
                            functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                            function = skipToNextTerm(function);
                            function = skipToNextTerm(function);
                        } catch (Exception e) {
                            System.out.println("error in parsing double in string in FunctionParsing.stringToFunction: " + nextDigitAsString(function));
                            e.printStackTrace();
                            return;
                        }

                        termTypes = getFunctionTerms(function);
                        System.out.println(termTypes);

                        if (termTypes.startsWith("digit")) {
                            try {
                                functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                            } catch (Exception e) {
                                System.out.println("error in parsing double in string in FunctionParsing.stringToFunction: " + nextDigitAsString(function));
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            functionVariables.add(0d);
                        }
                    } else {

                        //if there's no coefficient to x, set it to 0
                        functionVariables.add(0d);

                        //add the y-intercept to the function
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));

                    }
                } else if (termTypes.startsWith("x")) {
                    functionVariables.add(1d);
                    function = skipToNextTerm(function);
                    termTypes = getFunctionTerms(function);

                    if (termTypes.startsWith("digit")) {
                        try {
                            functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        } catch (Exception e) {
                            System.out.println("error in parsing double in string in FunctionParsing.stringToFunction: " + nextDigitAsString(function));
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        //if b == 0
                        functionVariables.add(0d);
                    }


                }

                System.out.println(functionVariables.get(0) + "," + functionVariables.get(1));

                //creating new LinearFunction
                Display.functions.add(new LinearFunction(functionVariables.get(0), functionVariables.get(1)));
                break;

            //quadratic
            case "quadratic":

                //if a is non-one
                if (termTypes.startsWith("digit_")) {
                    //get a
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //this is when a == 1
                    functionVariables.add(1d);
                }


                //skipping x^2 part
                function = skipToNextTerm(function);

                termTypes = getFunctionTerms(function);

                if (termTypes.startsWith("digit_")) {
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        function = skipToNextTerm(function); //skipping to c if possible
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                }
                //this would be activated if we're on the last term
                else if (termTypes.startsWith("digit")) {
                    try {
                        functionVariables.add(0d); //adding 0 for x^1
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function); //should be equal to ""
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                }
                //this is triggered when b == 1
                else if (termTypes.equals("x")) {
                    functionVariables.add(1d);
                    function = skipToNextTerm(function);
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function); //should be equal to ""
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //triggered if b == 0 and c == 0
                    functionVariables.add(0d);
                }

                if (!function.equals("")) {
                    termTypes = getFunctionTerms(function);
                    if (termTypes.startsWith("digit")) {
                        try {
                            functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                            function = skipToNextTerm(function);
                        } catch (Exception e) {
                            System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                            e.printStackTrace();
                            return;
                        }
                    }

                } else {
                    functionVariables.add(0d);
                }

                //creating new QuadraticFunction and adding it to the arraylist in Display
                Display.functions.add(new QuadraticFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2)));

                break;

            //cubic
            case "cubic":
                //if x1 isn't one
                if (termTypes.startsWith("digit_")) {
                    //register x1
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        function = skipToNextTerm(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                }
                //if x1 is one
                else {
                    functionVariables.add(1d);
                    function = skipToNextTerm(function);
                }

                termTypes = getFunctionTerms(function);

                //currently at x^2 part, or at least where it should be
                if (termTypes.startsWith("digit_")) {
                    //verify that we're at x^2 part
                    if (termTypes.startsWith("digit_xpow.2")) {
                        //register x2
                        try {
                            functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                            function = skipToNextTerm(function); //should be equal to ""
                            function = skipToNextTerm(function);
                        } catch (Exception e) {
                            System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                            e.printStackTrace();
                            return;
                        }
                    }
                    //if we aren't at the x^2 component, that means that x2 is zerop
                    else {
                        functionVariables.add(0d);
                        function = skipToNextTerm(function);
                    }
                }
                //if there x2 isn't explicitly stated, it's 1
                else if (termTypes.startsWith("xpow.2")) {
                    functionVariables.add(1d);
                    function = skipToNextTerm(function);
                }
                //if there's nothing to show here, x2 is zero
                else {
                    functionVariables.add(0d);
                }

                termTypes = getFunctionTerms(function);


                //currently at x^1 part

                //if there's a coefficient
                if (termTypes.startsWith("digit_")) {
                    //we already know that this is the x^1 part because of the underscore at the end
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function); //should be equal to ""
                        function = skipToNextTerm(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                }
                //if there's not a coefficient, but there's an x, it implies x3 is 1
                else if (termTypes.startsWith("x")) {
                    functionVariables.add(1d);
                    function = skipToNextTerm(function);
                } else {
                    //if there's no x period, x3 is 0
                    functionVariables.add(0d);
                }

                termTypes = getFunctionTerms(function);

                //currently at x^0 part
                if (termTypes.startsWith("digit")) {
                    functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                    function = skipToNextTerm(function); //not really necessary, but useful for debugging
                } else {
                    functionVariables.add(0d);
                }


                if (!function.equals("")) {
                    System.out.println("problem alert: " + function);
                }

                Display.functions.add(new CubicFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2), functionVariables.get(3)));

                break;

            //exponential
            case "exponential":
                //if a != 1
                if (termTypes.startsWith("digit_")) {
                    //register a
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function); //skipping to multiply
                        function = skipToNextTerm(function); //skipping to
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                }
                //if a == 1 (not explicitly stated)
                else {
                    functionVariables.add(1d);
                    //function = skipToNextTerm(function); //i don't think this is necessary
                }

                //now we're at b
                termTypes = getFunctionTerms(function);

                if (termTypes.startsWith("digpow.x")) {
                    //register b and c (c as 1)
                    try {
                        functionVariables.add(Double.valueOf(getFirstDigitOfDigpow(function)));
                        functionVariables.add(1d);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else if (termTypes.startsWith("digpow.digx")) {
                    //register b and c
                    try {
                        functionVariables.add(Double.valueOf(getFirstDigitOfDigpow(function)));
                        functionVariables.add(Double.valueOf(getFirstDigitOfDigpow(getSecondDigitOfDigpow(function))));
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    System.out.println("error in exponential function parsing.");
                }

                function = skipToNextTerm(function);
                termTypes = getFunctionTerms(function);

                //now at d

                //if there's a d specified
                if (termTypes.startsWith("digit")) {
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else d is zero
                    functionVariables.add(0d);
                }

                if (!function.equals("")) {
                    System.out.println("problem alert: " + function);
                }

                Display.functions.add(new ExponentialFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2), functionVariables.get(3)));

                break;

            //sin
            case "sin":
                //TODO could implement sin.dig but idk who would use it

                if (termTypes.startsWith("digit_")) {
                    //add the digit to functionVariables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    functionVariables.add(1d);
                }


                //at start of the sin...
                if (termTypes.startsWith("sin.x")) {
                    functionVariables.add(1d);
                } else if (termTypes.startsWith("sin.digx")) {
                    try {
                        functionVariables.add(Double.valueOf(getDigInSin(function)));
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    showError();
                }

                function = skipToNextTerm(function);
                termTypes = getFunctionTerms(function);

                //now at +c
                if (termTypes.startsWith("digit")) {
                    functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                    function = skipToNextTerm(function);
                } else {
                    functionVariables.add(0d);
                }

                if (!function.equals("")) {
                    showError();
                }


                Display.functions.add(new SinFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2)));


                break;

            //cos. almost the same as sin
            case "cos":

                if (termTypes.startsWith("digit_")) {
                    //add the digit to functionVariables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    functionVariables.add(1d);
                }


                //at start of the cos...
                if (termTypes.startsWith("cos.x")) {
                    functionVariables.add(1d);
                } else if (termTypes.startsWith("cos.digx")) {
                    try {
                        functionVariables.add(Double.valueOf(getDigInCos(function)));
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    showError();
                }

                function = skipToNextTerm(function);
                termTypes = getFunctionTerms(function);

                //now at +c
                if (termTypes.startsWith("digit")) {
                    functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                    function = skipToNextTerm(function);
                } else {
                    functionVariables.add(0d);
                }

                if (!function.equals("")) {
                    showError();
                }

                Display.functions.add(new CosFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2)));

                break;

            //xn
            case "xn":
                //if it starts with a digit
                if (termTypes.startsWith("digit_")) {
                    //find the digit and add it to the function variables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 1
                    functionVariables.add(1d);
                }

                //currently at the xpow part

                //this should be true
                if (termTypes.startsWith("xpow.")) {
                    //get the digit in xpow
                    try {
                        functionVariables.add(Double.valueOf(getDigInXpow(termTypes)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //this shouldn't ever happen
                    functionVariables.add(2d);
                    showError();
                    System.out.println("couldn't find xpow.");
                }

                //now at c

                //if there is a c
                if (termTypes.startsWith("digit")) {
                    //find c and add it to functionVariables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 0
                    functionVariables.add(0d);
                }


                //should always be false
                if (!termTypes.equals("")) {
                    System.out.println("expression should be blank at this point");
                    showError();
                } else {
                    Display.functions.add(new XNFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2)));
                }

                break;

            case "log":

                //if it starts with a digit
                if (termTypes.startsWith("digit_")) {
                    //find the digit and add it to the function variables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 1
                    functionVariables.add(1d);
                }

                //currently at log part

                //this should be true
                if (termTypes.startsWith("log")) {
                    try {
                        //getting the base
                        functionVariables.add(Double.valueOf(getBaseInLog(function)));

                        //getting the inside of the function
                        functionVariables.add(Double.valueOf(getCoefficientInLog(function)));

                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);

                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes + ", " + getCoefficientInLog(function));
                        e.printStackTrace();
                        return;
                    }

                } else {
                    //this shouldn't ever happen
                    functionVariables.add(10d);
                    showError();
                    System.out.println("couldn't find log.");
                }

                //now at d

                //if there is a d
                if (termTypes.startsWith("digit")) {
                    //find d and add it to functionVariables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 0
                    functionVariables.add(0d);
                }

                if (!termTypes.equals("")) {
                    System.out.println("expression should be blank at this point");
                    showError();
                } else {
                    Display.functions.add(new LogFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2), functionVariables.get(3)));
                }

                break;

            //this case is pretty similar to the log case, but it just does simple absolute value functions
            case "abs":
                //if it starts with a digit
                if (termTypes.startsWith("digit_")) {
                    //find the digit and add it to the function variables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 1
                    functionVariables.add(1d);
                }

                //now at abs part

                //this should be true
                if (termTypes.startsWith("abs")) {
                    try {
                        //getting the coefficient
                        functionVariables.add(Double.valueOf(getCoefficientInAbs(function)));

                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);

                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes + ", " + getCoefficientInLog(function));
                        e.printStackTrace();
                        return;
                    }

                } else {
                    //this shouldn't ever happen
                    functionVariables.add(1d);
                    showError();
                    System.out.println("couldn't find log.");
                }

                //now at c

                //if there is a c
                if (termTypes.startsWith("digit")) {
                    //find c and add it to functionVariables
                    try {
                        functionVariables.add(Double.valueOf(nextDigitAsString(function)));
                        function = skipToNextTerm(function);
                        termTypes = getFunctionTerms(function);
                    } catch (Exception e) {
                        System.out.println("parsing error in stringToFunction: " + function + "; " + termTypes);
                        e.printStackTrace();
                        return;
                    }
                } else {
                    //else it's implicitly 0
                    functionVariables.add(0d);
                }

                if (!termTypes.equals("")) {
                    System.out.println("expression should be blank at this point");
                    showError();
                } else {
                    Display.functions.add(new AbsFunction(functionVariables.get(0), functionVariables.get(1), functionVariables.get(2)));
                }

                break;

            default:
                showError();
                break;
        }

    }



    //TODO might wanna change this to return a double
    public static String nextDigitAsString(String subfunction) {
        String type = determineFirstTermType(subfunction);

        if(type.startsWith("xpow.")) {
            type = type.replace("xpow.","");
            return type;
        }


        while(!type.equals("digit")){
            subfunction = skipToNextTerm(subfunction);
            if(subfunction.equals("")) return "";
            type = determineFirstTermType(subfunction);
        }



        int endIndex = 0;

        if(subfunction.length() == 0) {
            return "";
        }

        for(int i = 0; i < subfunction.length(); i++) {
            char charAt = subfunction.charAt(i);
            if(Character.isDigit(charAt) || (charAt == '-') || (charAt == '+') || (charAt == '.')){
                endIndex++;
            }
            else break;
        }

        String out = subfunction.substring(0, endIndex);

        if(out.equals("-"))
            out = "-1";
        else if(out.equals("+"))
            out = "1";
        return out;

    }

    public static String getFunctionType(String function) {
        String terms = getFunctionTerms(function);

        if(isLinear(terms)){
            return "linear";
        }
        if(isQuadratic(terms)) {
            return "quadratic";
        }
        if(isCubic(terms)) {
            return "cubic";
        }
        if(isExponential(terms)) {
            return "exponential";
        }
        if(isSin(terms)) {
            return "sin";
        }
        if(isCos(terms)) {
            return "cos";
        }
        if(isXN(terms)) {
            return "xn";
        }
        if(isLog(terms)) {
            return "log";
        }
        if(isAbs(terms)) {
            return "abs";
        }

        System.out.println("undefined behavior: " + function + "; " + terms);
        showError();
        return "n/a";
    }

    public static boolean isLinear(String terms) {
        if(terms.equals("digit_x_digit") || terms.equals("x_digit") || terms.equals("digit_x") || terms.equals("x") || terms.equals("digit")){
            return true;
        }
        return false;
    }

    public static boolean isQuadratic(String terms) {
        if(terms.equals("digit_xpow.2_digit_x_digit") || terms.equals("xpow.2_digit_x_digit") || terms.equals("xpow.2_x_digit") || terms.equals("digit_xpow.2_x_digit") || terms.equals("xpow.2_x") || terms.equals("digit_xpow.2_x") || terms.equals("xpow.2_digit") || terms.equals("digit_xpow.2_digit") || terms.equals("xpow.2") || terms.equals("digit_xpow.2") || terms.equals("xpow.2_digit_x") || terms.equals("digit_xpow.2_digit_x")) {
            return true;
        }
        return false;
    }

    public static boolean isCubic(String terms) {
        //eliminating quadratic and linear equations -- might not be necessary because this method is run after isLinear and isQuadratic
        if(isQuadratic(terms) || isLinear(terms)) return false;

        //finding simple cubic equations that can't really be found in another way
        if(terms.equals("digit_xpow.3") || terms.equals("xpow.3")) return true;

        //making sure that it's probably cubic
        if(!terms.contains("xpow.3")) return false;

        //skipping past the x^3
        if(terms.startsWith("digit")) terms = skipToNextTermType(terms);

        terms = skipToNextTermType(terms);

        //now that we have the cubic element taken out, if the rest is quadratic or linear then the equation is cubic (cubic element + quadratic/linear element = cubic equation)
        if(isQuadratic(terms) || isLinear(terms)) return true;

        //if none of these tests have triggered, return false
        return false;
    }

    public static boolean isExponential(String terms)    {
        //input is in the termType format

        if(terms.equals("digit_digpow.x") || terms.equals("digit_digpow.digx") || terms.equals("digpow.x") || terms.equals("digpow.digx") || terms.equals("digit_digpow.x_digit") || terms.equals("digit_digpow.digx_digit") || terms.equals("digpow.x_digit") || terms.equals("digpow.digx_digit")) return true;

        return false;

    }

    public static boolean isSin(String terms) {
        if(terms.equals("sin.x") || terms.equals("sin.dig") || terms.equals("sin.digx") || terms.equals("digit_sin.x") || terms.equals("digit_sin.dig") || terms.equals("digit_sin.digx") || terms.equals("sin.x_digit") || terms.equals("sin.dig_digit") || terms.equals("sin.digx_digit") || terms.equals("digit_sin.x_digit") || terms.equals("digit_sin.dig_digit") || terms.equals("digit_sin.digx_digit")) return true;
        return false;
    }

    public static boolean isCos(String terms) {
        if(terms.equals("cos.x") || terms.equals("cos.dig") || terms.equals("cos.digx") || terms.equals("digit_cos.x") || terms.equals("digit_cos.dig") || terms.equals("digit_cos.digx") || terms.equals("cos.x_digit") || terms.equals("cos.dig_digit") || terms.equals("cos.digx_digit") || terms.equals("digit_cos.x_digit") || terms.equals("digit_cos.dig_digit") || terms.equals("digit_cos.digx_digit")) return true;
        return false;
    }

    public static boolean isXN(String terms) {

        //since all the other xpows are already tested, if it contains xpow then it is probly XN
        if(terms.contains("xpow.")) {
            //skipping past a possible digit at the start
            if(terms.startsWith("digit_")) {
                terms = skipToNextTermType(terms);
            }
            //this should be present
            if(terms.startsWith("xpow.")) {
                terms = skipToNextTermType(terms);
            }
            else return false;
            //just skipping past a last digit
            if(terms.startsWith("digit")) {
                terms = skipToNextTermType(terms);
            }

            if(!terms.equals("")) {
                System.out.println("this probably isn't supposed to happen");
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    //figures out of a sequence of terms are in a proper logarithmic form
    public static boolean isLog(String terms) {
        if(terms.equals("log.x") || terms.equals("log.digx") || terms.equals("log,dig.x") || terms.equals("log,dig.digx") || terms.equals("digit_log.x") || terms.equals("digit_log,dig.x") || terms.equals("digit_log,dig.digx") || terms.equals("log.x_digit") || terms.equals("log.digx_digit") || terms.equals("log,dig.x_digit") || terms.equals("log,dig.digx_digit") || terms.equals("digit_log.x_digit") || terms.equals("digit_log,dig.x_digit") || terms.equals("digit_log,dig.digx_digit") || terms.equals("digit_log.digx_digit")) {
            return true;
        }
        return false;
    }

    public static boolean isAbs(String terms) {
        if(terms.equals("abs.x") || terms.equals("abs.digx") || terms.equals("digit_abs.x") || terms.equals("digit_abs.digx") || terms.equals("abs.x_digit") || terms.equals("abs.digx_digit") || terms.equals("digit_abs.x_digit") || terms.equals("digit_abs.digx_digit")) {
            return true;
        }
        return false;
    }


    public static String getFunctionTerms(String function) {
        String out  = "";

        if(function.equals("")) return "";

        while(!function.equals("") && !Display.showErrorMessage) {
            try{
                out += determineFirstTermType(function) + "_";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ex");
            }

            function = skipToNextTerm(function);
        }

        if(!out.equals("")){
            out = out.substring(0, out.length()-1);
        }

        if(out.startsWith("digit_multiply_")) {
            out = out.replace("digit_multiply_", "digit_");
        }

        return out;
    }

    public static String determineFirstTermType(String subfunction) {

        if(subfunction.startsWith("+")) subfunction = subfunction.substring(1);

        char[] sfchars = subfunction.toCharArray();


        if(Character.isDigit(sfchars[0]) || sfchars[0] == '+' || sfchars[0] == '-' || sfchars[0] == '.') {
            //digpow
            //skipping to where the digits end
            if(sfchars.length > 1) {
                for (int i = 1; i < sfchars.length; i++) {
                    if(sfchars[i] == 'x') break;

                    if(sfchars[i] == '^') {
                        //sfchars[i] is where the power is, so sfc..[i+1] should be where x is if this is digpow.x
                        if(sfchars[i+1] == 'x') return "digpow.x";
                        //it could still be digpow.digx
                        if(Character.isDigit(sfchars[i+1]) || sfchars[i+1] == '-') {
                            //skipping to where the digits end, so basically where x is
                            for (int j = i+2; j < sfchars.length; j++) {
                                if(sfchars[j] == 'x') return "digpow.digx";
                            }
                            //if that doesn't happen, then it's probably an invalid input
                            showError();
                        }


                    }
                    if(sfchars[i] == '*') return "digit";

                }

            }

            return "digit";
        }
        else if(sfchars[0] == 'x') {
            if(sfchars.length > 1 && sfchars[1] == '^') {
                StringBuilder pow = new StringBuilder();
                for (int i = 2; i < sfchars.length; i++) {
                    if(i == 2 && sfchars[i] == '-') {
                        pow.append(sfchars[i]);
                    } else if(Character.isDigit(sfchars[i]) || sfchars[i] == '.'){
                        pow.append(sfchars[i]);
                    } else{
                        break;
                    }
                }
                return "xpow." + pow.toString();
            }
            return "x";
        }
        else if(sfchars[0] == '/') return "divide"; //TODO implement divide
        else if(sfchars[0] == '*') return "multiply";
        else if(subfunction.startsWith("sin")) {
            //only supporting dig, x, and dig*x for now at least
            if(sfchars[4] == '-' || Character.isDigit(sfchars[4])) {
                if(sfchars.length > 5) {
                    //finding out if there is an x
                    for (int i = 5; i < sfchars.length; i++) {
                        if(sfchars[i] == 'x') {
                            return "sin.digx";
                        }
                    }
                }
                return "sin.dig";
            }
            //if there is only an x
            else if(sfchars[4] == 'x') return "sin.x";
        }
        else if(subfunction.startsWith("cos")) {
            //only supporting dig, x, and dig*x for now at least
            if(sfchars[4] == '-' || Character.isDigit(sfchars[4])) {
                if(sfchars.length > 5) {
                    //finding out if there is an x
                    for (int i = 5; i < sfchars.length; i++) {
                        if(sfchars[i] == 'x') {
                            return "cos.digx";
                        }
                    }
                }
                return "cos.dig";
            }
            //if there is only an x
            else if(sfchars[4] == 'x') return "cos.x";
        }
        else if(subfunction.startsWith("log")) {
            //if not base ten (or if it's explicitly called based ten)
            if(sfchars[3] == '_') {
                //skip to the start of the part in the parentheses
                for (int i = 5; i < sfchars.length; i++) {
                    if(sfchars[i] == '(') {
                        //i+1 is the start of the part inside of the parentheses

                        //if it's just x in the parentheses
                        if(sfchars[i+1] == 'x') {
                            return "log,dig.x";
                        }
                        else {
                            //skip to the end of the digs
                            for (int j = i+1; j < sfchars.length; j++) {
                                if(sfchars[j] == 'x') return "log,dig.digx";
                            }
                        }
                    }
                }
            }
            //if it's base ten
            else if(sfchars[3] == '('){
                //if it's just x in the parentheses
                if(sfchars[4] == 'x') {
                    return "log.x";
                }
                else {
                    //skip to the end of the digs
                    for (int j = 4; j < sfchars.length; j++) {
                        if(sfchars[j] == 'x') return "log.digx";
                    }
                }

            }
        }
        else if(subfunction.startsWith("abs(")) {
            //starting at 4 to get inside the parentheses
            if(sfchars[4] == 'x') return "abs.x";
            else {
                for (int i = 5; i < sfchars.length; i++) {
                    if(sfchars[i] == 'x') return "abs.digx";
                }
            }
        }


        System.out.println("undefined behavior: " + subfunction);
        showError();
        return "fail";


    }


    //this isn't really skipping terms, just subterms (coefficients and x^n are called terms)
    public static String skipToNextTerm(String subfunction) {
        if(subfunction.startsWith("+")) subfunction = subfunction.substring(1);

        String type = determineFirstTermType(subfunction);


        if(type.startsWith("xpow")) {
            type = "xpow";
        }

        int newStartIndex = 0;

        if(subfunction.length() == 0) {
            return "";
        }

        loop: for(int i = 0; i < subfunction.length(); i++) {
            char charAt = subfunction.charAt(i);
            switch (type) {
                case "digit":
                    if(Character.isDigit(charAt) || charAt == '-' || /*charAt == '+' ||*/  charAt == '.'){
                        newStartIndex++;
                    }
                    else {
                        break loop;
                    }
                    break;
                case "x": case "plus": case "multiply":
                    return subfunction.substring(1);
                case "xpow":
                    if(charAt == 'x' || charAt == '^' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        if(i > 0 && charAt == '-') {
                            if(subfunction.charAt(i-1) == '^') {
                                newStartIndex++;
                            }
                            else return subfunction.substring(newStartIndex);
                        }
                        else newStartIndex++;
                    }
                    else break loop;
                    break;
                case "digpow.x": case "digpow.digx":
                    if(charAt == 'x' || charAt == '^' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == 'x') break loop; //since it'll be the last thing. this prevents a case of 2^x-2 being registered as one term because of the - sign
                    }
                    break;
                case "sin.x":
                    if(charAt == 's' || charAt == 'i' || charAt == 'n' || charAt == 'x' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "sin.dig":
                    if(charAt == 's' || charAt == 'i' || charAt == 'n' || Character.isDigit(charAt) || charAt == '-' || charAt == '.' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "sin.digx":
                    if(charAt == 's' || charAt == 'i' || charAt == 'n' || charAt == 'x' || charAt == '(' || charAt == ')' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "cos.x":
                    if(charAt == 'c' || charAt == 'o' || charAt == 's' || charAt == 'x' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "cos.dig":
                    if(charAt == 'c' || charAt == 'o' || charAt == 's' || Character.isDigit(charAt) || charAt == '-' || charAt == '.' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "cos.digx":
                    if(charAt == 'c' || charAt == 'o' || charAt == 's' || charAt == 'x' || charAt == '(' || charAt == ')' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "log.x":
                    if(charAt == 'l' || charAt == 'o' || charAt == 'g' || charAt == 'x' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    //todo might need to add an else statement at the end of these "else break loop;"
                    break;
                case "log,dig.x": case "log,dig.digx":
                    if(charAt == 'l' || charAt == 'o' || charAt == 'g' || charAt == '_' || charAt == 'x' || charAt == '(' || charAt == ')' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "log.digx":
                    if(charAt == 'l' || charAt == 'o' || charAt == 'g' || charAt == 'x' || charAt == '(' || charAt == ')' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "abs.x":
                    if(charAt == 'a' || charAt == 'b' || charAt == 's' || charAt == 'x' || charAt == '(' || charAt == ')') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                case "abs.digx":
                    if(charAt == 'a' || charAt == 'b' || charAt == 's' || charAt == 'x' || charAt == '(' || charAt == ')' || Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                        newStartIndex++;
                        if(charAt == ')') break loop;
                    }
                    break;
                default:
                    return subfunction.substring(newStartIndex);
            }

        }

        return subfunction.substring(newStartIndex);

    }

    public static String skipToNextTermType(String terms) {

        //TODO these tests might overflow

        int startIndex = 0;

        if(terms.equals("")) return "";

        if(terms.startsWith("digit")) {
            if(terms.startsWith("digit_")) {
                startIndex = 6;
            } else{
                startIndex = 5;
            }
        } else if(terms.startsWith("xpow.")) {
            startIndex = 5;
            for (int i = 5; i < terms.length(); i++) {
                char charAt = terms.charAt(i);
                if(Character.isDigit(charAt) || charAt == '-' || charAt == '.') {
                    startIndex++;
                } else {
                    startIndex++;
                    break;
                }
            }
        } else if(terms.startsWith("x")) {
            startIndex = 1;
        } else if(terms.startsWith("sin.")) {
            startIndex = 4; //4 bc that's right after the '.' in 'sin.'
            if(terms.substring(4).startsWith("x")) startIndex = 5;
            else if(terms.substring(4).startsWith("digx")) startIndex = 8;
            else if(terms.substring(4).startsWith("dig")) startIndex = 7;
        } else if(terms.startsWith("cos.")) {
            startIndex = 4; //4 bc that's right after the '.' in 'cos.'
            if(terms.substring(4).startsWith("x")) startIndex = 5;
            else if(terms.substring(4).startsWith("digx")) startIndex = 8;
            else if(terms.substring(4).startsWith("dig")) startIndex = 7;

        }

        if(terms.substring(startIndex).startsWith("_")) startIndex++;

        return terms.substring(startIndex);

    }

    //this is never called on but it could probably maybe be useful at some point T.T
    //todo if i do want to do this i need to add log and abs
    public static String nextTermAsString(String subfunction){
        String type = determineFirstTermType(subfunction);

        int endIndex = 0;

        if(subfunction.length() == 0) {
            return "";
        }

        for(int i = 0; i < subfunction.length(); i++) {
            char charAt = subfunction.charAt(i);

            //todo probly switch to a switch case T.T
            if(type.equals("digit") && (Character.isDigit(charAt) || charAt == '-' || charAt == '+' || charAt == '.')){
                endIndex++;
            }
            else if(type.equals("x")){
                endIndex = 1;
                break;
            }
            else if(type.startsWith("xpow") && (charAt == 'x' || charAt == '^' || Character.isDigit(charAt) || charAt == '-' || charAt == '.')){
                if(i > 0 && charAt == '-') {
                    if (subfunction.charAt(i - 1) == '^') {
                        endIndex++;
                    } else break;
                }
                else endIndex++;
            }
            else if(type.equals("plus")){
                endIndex = 1;
                break;
            }
            else break;
        }

        String out = subfunction.substring(0, endIndex);

        if(out.equals("-")){
            out = "-1";
        }

        return out;

    }

    public static String getFirstDigitOfDigpow(String subfunction) {
        char[] sfchars = subfunction.toCharArray();

        int endIndex = 1;

        if(!Character.isDigit(sfchars[0]) && sfchars[0] != '-') {
            System.out.println("getFirstDigitOfDigpow error: bruh moment: " + subfunction);
            return "1";
        }

        //determining where the digit ends
        for (int i = 1; i < sfchars.length; i++) {
            if(Character.isDigit(sfchars[i]) || sfchars[i] == '.') {
                endIndex++;
            }
            else break;
        }

        String out = subfunction.substring(0, endIndex);

        if(out.equals("-")) out = "-1";

        return out;

    }

    public static String getSecondDigitOfDigpow(String subfunction) {
        char[] sfchars = subfunction.toCharArray();

        int endIndex = 0;
        int firstDigitEndIndex = 1;

        if(!Character.isDigit(sfchars[0]) && sfchars[0] != '-') {
            System.out.println("getSecondDigitOfDigpow error: bruh moment: " + subfunction);
            return "1";
        }

        //determining where the first digit ends
        for (int i = 1; i < sfchars.length; i++) {
            if(Character.isDigit(sfchars[i]) || sfchars[i] == '-' || sfchars[i] == '.') {
                firstDigitEndIndex++;
            }
            else break;
        }


        //cutting subfunction to where the ^ is
        subfunction = subfunction.substring(firstDigitEndIndex);

        //this should always be true
        if(subfunction.startsWith("^")) {
            subfunction = subfunction.substring(1);
        }
        else {
            System.out.println("subfunction should start with ^ at this point");
        }

        //getting the string of numbers
        char[] newSfchars = subfunction.toCharArray();
        for (int i = 0; i < sfchars.length; i++) {
            if(Character.isDigit(newSfchars[i]) || newSfchars[i] == '-' || newSfchars[i] == '.') endIndex++;
            else break;
        }

        String out = subfunction.substring(0, endIndex);

        if(out.equals("-")) out = "-1";

        return out;

    }

    public static String getDigInSin(String subfunction) {
        if(subfunction.startsWith("sin(")) {
            subfunction = subfunction.replace("sin(","");
        }
        else {
            showError();
        }

        String out = "";
        char[] sfchars = subfunction.toCharArray();

        for (int i = 0; i < subfunction.length(); i++) {
            if(i == 0 && sfchars[i] == '-') out = "-";
            else if(Character.isDigit(sfchars[i]) || sfchars[i] == '.') {
                out += sfchars[i];
            }
            else break;
        }

        if(out.equals("-")) out = "-1";

        return out;
    }

    public static String getDigInCos(String subfunction) {
        if(subfunction.startsWith("cos(")) {
            subfunction = subfunction.replace("cos(","");
        }
        else {
            showError();
        }

        String out = "";
        char[] sfchars = subfunction.toCharArray();

        for (int i = 0; i < subfunction.length(); i++) {
            if(i == 0 && sfchars[i] == '-') out = "-";
            else if(Character.isDigit(sfchars[i]) || sfchars[i] == '.') {
                out += sfchars[i];
            }
            else break;
        }

        if(out.equals("-")) out = "-1";

        return out;
    }

    public static String getDigInXpow(String subfunction) {

        int endIndex = 0;

        if(subfunction.startsWith("xpow.")) {
            subfunction = subfunction.substring(5);

            char[] sfchars = subfunction.toCharArray();

            if(sfchars[0] == '-') {
                endIndex++;
            }

            //determining where the digit ends
            for (int i = endIndex; i < sfchars.length; i++) {
                if(Character.isDigit(sfchars[i]) || sfchars[i] == '.') {
                    endIndex++;
                }
                else break;
            }
        }
        else {
            showError();
            return "2";
        }

        String out = subfunction.substring(0,endIndex);
        if(out.equals("-")) out = "-1";
        return out;
    }

    //just returns the stuff between the '_' and the '('
    //input is in standard function form with _ denoting start of the base and ( denoting the start of the inside
    public static String getBaseInLog(String subfunction) {
        int startIndex = 0;
        int endIndex = 0;

        char[] sfchars = subfunction.toCharArray();

        //finding the '_'; starts at 3 bc that's the first place it could appear
        for (int i = 3; i < sfchars.length; i++) {
            if(sfchars[i] == '_') {
                startIndex = i+1;
                break;
            }
            //if it's implicitly stated (base 10)
            else if(sfchars[i] == '(') {
                return "10";
            }
        }

        //finding the '('; starts at 5 bc that's where it could first reasonably appear
        for (int i = 5; i < sfchars.length; i++) {
            if(sfchars[i] == '(') {
                endIndex = i;
                break;
            }
        }

        if(startIndex == 0 || endIndex == 0) {
            System.out.println("getBaseInLog error | si: " + startIndex + ", ei: " + endIndex);
            showError();
            return "10";
        }

        return subfunction.substring(startIndex, endIndex);

    }

    //basically the same thing as getBaseInLog, but this time it starts at '(' and ends at either ')' or 'x' (most likely 'x')
    public static String getCoefficientInLog(String subfunction) {
        int startIndex = 0;
        int endIndex = 0;

        char[] sfchars = subfunction.toCharArray();

        //finding the '('; starts at 3 bc that's the first place it could appear
        for (int i = 3; i < sfchars.length; i++) {
            if(sfchars[i] == '(') {
                startIndex = i+1;
                break;
            }
        }

        //finding the ')' or 'x'; starts at 4 bc that's where it could first reasonably appear
        for (int i = 5; i < sfchars.length; i++) {
            if(sfchars[i] == ')' ||sfchars[i] == 'x') {
                endIndex = i;
                break;
            }
        }

        if(startIndex == 0 || endIndex == 0) {
            System.out.println("getCoefficientInLog error | si: " + startIndex + ", ei: " + endIndex);
            showError();
            return "10";
        }

        String out = subfunction.substring(startIndex, endIndex);

        if(out.equals("-")) out = "-1";
        if(out.equals("x") || out.equals("")) out = "1";

        return out;

    }

    //takes an abs and gets the coefficient from the inside
    public static String getCoefficientInAbs(String subfunction) {
        int startIndex = 0;
        int endIndex = 0;

        char[] sfchars = subfunction.toCharArray();

        //finding the '('; starts at 3 bc that's the first place it could appear
        for (int i = 3; i < sfchars.length; i++) {
            if(sfchars[i] == '(') {
                startIndex = i+1;
                break;
            }
        }

        //finding the ')' or 'x'; starts at 4 bc that's where it could first reasonably appear
        for (int i = 5; i < sfchars.length; i++) {
            if(sfchars[i] == ')' ||sfchars[i] == 'x') {
                endIndex = i;
                break;
            }
        }

        if(startIndex == 0 || endIndex == 0) {
            System.out.println("getCoefficientInAbs error | si: " + startIndex + ", ei: " + endIndex);
            showError();
            return "10";
        }

        String out = subfunction.substring(startIndex, endIndex);

        if(out.equals("-")) out = "-1";
        if(out.equals("x") || out.equals("")) out = "1";

        return out;

    }

    public static void showError() {
        System.out.println("Error: invalid input.");
        Display.showErrorMessage = true;
        Display.errorMessageStartTime = System.currentTimeMillis();
        Display.etf.setText("");
    }

}