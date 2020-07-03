package main.java.Visualizer.functions;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.tools.DoublePoint;

public class XNFunction extends Function{


    private static final double XN_A_SCALING_FACTOR = 1d;
    private static final double XN_B_SCALING_FACTOR = 1d;
    private static final double XN_C_SCALING_FACTOR = 1d;


    double a;
    double b;
    double c;

    public XNFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;


        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {

            double val = Display.HEIGHT - (XN_A_SCALING_FACTOR * a * Math.pow(i - (float)Display.WIDTH/2, XN_B_SCALING_FACTOR * b) + XN_C_SCALING_FACTOR * c + (float)Display.HEIGHT/2);

            if(val >= Integer.MAX_VALUE) val = Integer.MAX_VALUE;

            int intVal = (int) val;

            points[i] = new DoublePoint(i, intVal);
            this.xs[i] = (int) Math.round(points[i].getX());
            this.ys[i] = (int) Math.round(points[i].getY());
        }
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
}
