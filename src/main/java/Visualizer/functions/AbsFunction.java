package main.java.Visualizer.functions;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.tools.DoublePoint;

public class AbsFunction extends Function {

    public static final double ABS_A_SCALING_FACTOR = 1d;
    public static final double ABS_B_SCALING_FACTOR = 1d;
    public static final double ABS_C_SCALING_FACTOR = 1d;

    double a;
    double b;
    double c;

    public AbsFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {
            //finding the y value and then kinda preventing overflow
            double val = Display.HEIGHT - (ABS_A_SCALING_FACTOR*a * Math.abs(ABS_B_SCALING_FACTOR*b * (i - (float)Display.WIDTH/2)) + ABS_C_SCALING_FACTOR*c + (float)Display.HEIGHT/2);

            if(val >= Integer.MAX_VALUE) val = Integer.MAX_VALUE;

            points[i] = new DoublePoint(i, val);
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
