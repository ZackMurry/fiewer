package main.java.Visualizer.functions;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.tools.DoublePoint;

public class CosFunction extends Function{

    public static final double COS_A_SCALING_FACTOR = 100d;
    public static final double COS_B_SCALING_FACTOR = 0.01d;
    public static final double COS_C_SCALING_FACTOR = 100d;

    double a;
    double b;
    double c;

    public CosFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {
            double val = Display.HEIGHT - (COS_A_SCALING_FACTOR * a*(Math.cos(COS_B_SCALING_FACTOR*b*(i-(float)Display.WIDTH/2))) + COS_C_SCALING_FACTOR*c + (float)Display.HEIGHT/2);

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
