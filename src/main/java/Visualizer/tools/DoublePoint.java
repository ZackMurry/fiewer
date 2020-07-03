package Visualizer.tools;

import Visualizer.display.Display;

import java.awt.*;

/*
    todo maybe remove this

    this is kind of a relic of an old system
    i used to draw functions by getting points of a function and then drawing the points to the screen
    exponential functions caused problems because there wasn't enough points to fill the high slope
    so i switched to polyline, which is like points but better
    now a polyline connects the points
 */

public class DoublePoint {

    double x;
    double y;

    Color color;

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;

        this.color = Display.DEFAULT_LINE_COLOR;
    }

    public DoublePoint(Color color, double x, double y) {
        this.x = x;
        this.y = y;

        this.color = color;
    }

    public void render(Graphics g) {
        g.setColor(this.color);
        g.fillOval((int) Math.round(this.x + (float)Display.WIDTH/2), (int) Math.round((float)Display.HEIGHT/2 - this.y), 5,5);

    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
