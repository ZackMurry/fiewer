package Visualizer.functions;

import Visualizer.display.Display;
import Visualizer.tools.DoublePoint;

public class LinearFunction extends Function {


    public static final double LIN_M_SCALING_FACTOR = 1d;
    public static final double LIN_B_SCALING_FACTOR = 10d;

    double m;
    double b;

    public LinearFunction(double m, double b) {
        this.m = m;
        this.b = b;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];


        for(int i = 0; i < points.length; i++) {
            double val = (float)Display.HEIGHT/2 - (LIN_M_SCALING_FACTOR * m*(i - (float) Display.WIDTH/2) + LIN_B_SCALING_FACTOR*b);

            if(val >= Integer.MAX_VALUE) val = Integer.MAX_VALUE;

            points[i] = new DoublePoint(i, val);
            this.xs[i] = (int) Math.round(points[i].getX());
            this.ys[i] = (int) Math.round(points[i].getY());
        }
    }

    public double getM() {
        return this.m;
    }

    public double getB() {
        return this.b;
    }
}
