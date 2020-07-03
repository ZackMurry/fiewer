package Visualizer.functions;

import Visualizer.display.Display;
import Visualizer.tools.DoublePoint;

public class CubicFunction extends Function {

    public static final double CUBIC_X1_SCALING_FACTOR = 0.01d;
    public static final double CUBIC_X2_SCALING_FACTOR = 0.1d;
    public static final double CUBIC_X3_SCALING_FACTOR = 1d;
    public static final double CUBIC_X4_SCALING_FACTOR = 10d;

    double x1;
    double x2;
    double x3;
    double x4;


    public CubicFunction(double x1, double x2, double x3, double x4) { //where x1 is the leading coefficient

        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {

            double val = Display.HEIGHT - (CUBIC_X1_SCALING_FACTOR * (i-(float)Display.WIDTH/2)*(i-(float)Display.WIDTH/2)*(i-(float)Display.WIDTH/2) + CUBIC_X2_SCALING_FACTOR * x2 * (i-(float)Display.WIDTH/2)*(i-(float)Display.WIDTH/2) + CUBIC_X3_SCALING_FACTOR * x3 * (i-(float)Display.WIDTH/2) + CUBIC_X4_SCALING_FACTOR * x1 + (float)Display.HEIGHT/2);

            if(val >= Integer.MAX_VALUE) val = Integer.MAX_VALUE;

            points[i] = new DoublePoint(i, val);
            this.xs[i] = (int) Math.round(points[i].getX());
            this.ys[i] = (int) Math.round(points[i].getY());
        }


    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public double getX4() {
        return x4;
    }

}
