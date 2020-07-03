package main.java.Visualizer.controls;

import main.java.Visualizer.display.Display;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/*
    todo zooming
    work in progress because zooming is kinda hard
 */

public class MouseWheelDetector implements MouseWheelListener {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Display.zoomAmount += e.getWheelRotation() * Display.zoomSpeed;
        if(Display.zoomAmount <= 0)
            Display.zoomAmount = Display.zoomSpeed;
        System.out.println(Display.zoomAmount);
    }
}
