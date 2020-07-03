package Visualizer.display;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/*
    the button that clears the graph
 */

public class ClearButton extends JButton {

    public ClearButton() {
        Dimension size = new Dimension(100, 30);
        setPreferredSize(size);
        setMaximumSize(size);
        setBackground(Color.WHITE);
        setText("Clear lines");

        //setting two borders for margins
        setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createLineBorder(Color.BLACK, 1)));

        //activates when the button is clicked
        addActionListener(e ->
        {
            Display.functions.clear(); //clears the arraylist of functions, making them disappear
            Display.etf.setText(""); //clears the JTextField for convenience.
        });

    }

}
