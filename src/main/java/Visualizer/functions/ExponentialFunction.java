package main.java.Visualizer.functions;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.tools.DoublePoint;

public class ExponentialFunction extends Function {

    public static final double EXP_A_SCALING_FACTOR = 1d;
    public static final double EXP_B_SCALING_FACTOR = 1d;
    public static final double EXP_C_SCALING_FACTOR = 0.05d;
    public static final double EXP_D_SCALING_FACTOR = 1d;

    double a;
    double b;
    double c;
    double d;

    public ExponentialFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];


        for(int i = 0; i < points.length; i++) {
            double val = Display.HEIGHT - (EXP_A_SCALING_FACTOR * a * Math.pow(EXP_B_SCALING_FACTOR * b, EXP_C_SCALING_FACTOR * c * (i-(float)Display.WIDTH/2)) + EXP_D_SCALING_FACTOR * d + (float)Display.HEIGHT/2);

            if(val >= Integer.MAX_VALUE) val = Integer.MAX_VALUE;
            int intVal = (int) val;

            this.points[i] = new DoublePoint(i, intVal);
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

    public double getD() {
        return d;
    }
}
