package main.java.Visualizer.functions;

import main.java.Visualizer.tools.DoublePoint;

import java.awt.*;

/*
    parent class of all other functions
 */

public class Function {

    public DoublePoint[] points;

    public int[] xs;
    public int[] ys;

    //draws a polyline of the points of the function
    public static void render(Graphics g, DoublePoint[] points, int[] xs, int[] ys) {

        //converting to Graphics2D because Graphics doesn't have polyline
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(points[0].getColor());
        g2d.drawPolyline(xs, ys, points.length);
    }


}
