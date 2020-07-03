package main.java.Visualizer.display;

import main.java.Visualizer.tools.parsing.FunctionParsing;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/*
    how the user adds functions to the screen
 */

public class EquationTextField extends JTextField{

    public EquationTextField() {
        Dimension size = new Dimension(100, 30);
        setPreferredSize(size);
        setMaximumSize(size);
        setText("-x^2+2x"); //the default text for the field, showing how it works

        //creating a compound border for margins
        setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createLineBorder(Color.BLACK, 1)));

        //activated upon hitting the enter button
        addActionListener(ae -> {
            String textFieldValue = getText().toLowerCase().replace(" ","").replace("y=","").replace("=y", ""); //normalizing the text in it
            FunctionParsing.stringToFunction(textFieldValue); //sending the text to FunctionParsing to get parsed
            setText("");
        });

    }

}
