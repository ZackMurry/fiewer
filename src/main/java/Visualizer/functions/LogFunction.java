package main.java.Visualizer.functions;

import main.java.Visualizer.display.Display;
import main.java.Visualizer.tools.DoublePoint;

public class LogFunction extends Function {

    public static final double LOG_A_SCALING_FACTOR = 10d;
    public static final double LOG_B_SCALING_FACTOR = 1d;
    public static final double LOG_C_SCALING_FACTOR = 1d;
    public static final double LOG_D_SCALING_FACTOR = 1d;

    double a;
    double b;
    double c;
    double d;

    public LogFunction(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.points = new DoublePoint[Display.WIDTH];

        this.xs = new int[points.length];
        this.ys = new int[points.length];

        for(int i = 0; i < points.length; i++) {
            //using change of base formula to get it to base ten
            double val = Display.HEIGHT - (LOG_A_SCALING_FACTOR*a * (Math.log(LOG_C_SCALING_FACTOR * c * (i - (float)Display.WIDTH/2)) / Math.log(LOG_B_SCALING_FACTOR * b)) + LOG_D_SCALING_FACTOR*d + (float)Display.HEIGHT/2);

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

    public double getD() {
        return d;
    }
}
