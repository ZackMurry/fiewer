package Visualizer.functions;

import Visualizer.display.Display;
import Visualizer.tools.DoublePoint;

public class QuadraticFunction extends Function{

    public static final double QUAD_A_SCALING_FACTOR = 0.1d;
    public static final double QUAD_B_SCALING_FACTOR = 1d;
    public static final double QUAD_C_SCALING_FACTOR = 5d;

    double a;
    double b;
    double c;

    public QuadraticFunction(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {

            double val = Display.HEIGHT - (QUAD_A_SCALING_FACTOR * a*(i-(float)Display.WIDTH/2)*(i-(float)Display.WIDTH/2) + QUAD_B_SCALING_FACTOR * b*(i-(float)Display.WIDTH/2) + QUAD_C_SCALING_FACTOR * c + (float)Display.HEIGHT/2);

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
