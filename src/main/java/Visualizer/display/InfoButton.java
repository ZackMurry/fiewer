package main.java.Visualizer.display;

import main.java.Visualizer.functions.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class InfoButton extends JButton {

    public InfoButton() {
        Dimension size = new Dimension(100, 30);
        setPreferredSize(size);
        setMaximumSize(size);
        setBackground(Color.WHITE);
        setText("Show info");

        setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(5,5,5,5),
                BorderFactory.createLineBorder(Color.BLACK, 1)));

        addActionListener(e -> Display.infoShowing = !Display.infoShowing);
    }

    public static void showInfo(Graphics g) {
        //drawing a rect over the old stuff lol
        g.setColor(Color.white);
        g.fillRect(-Display.WIDTH, -Display.HEIGHT, 2*Display.WIDTH, 2*Display.HEIGHT);
        g.setColor(Color.black);
        g.setFont(Display.infoFont);
        drawString(
                g,
                "Made by Zack Murry\n\n" +
                        "This program draws functions to the screen.\n" +
                        "Available functions: \n" +
                        "   Linear functions (y=mx+b)\n" +
                        "   Quadratic functions (y=ax^2+bx+c)\n" +
                        "   Cubic functions (y=ax^3+bx^2+cx+d)\n" +
                        "   Exponential and square root functions (y=a*x^b+c, y=a*sqrt(x)+c,\n and y=a*b^cx)\n" +
                        "   Sine and cosine functions (y=a*sin(bx)+c and y=a*cos(bx)+c)\n" +
                        "   Logarithmic functions (y=a*log_b(cx)+d)\n" +
                        "   Absolute value functions (y=a*abs(bx)+c)\n\n" +
                        "Functions on screen: \n" +
                        getFunctionDescriptions(),
                100,
                100);

    }

    private static String getFunctionDescriptions() {
        StringBuilder out = new StringBuilder();

        DecimalFormat formatter = new DecimalFormat("0.##"); //converts 1.0 to 1 (and similar things)

        //todo switch these to string builder
        for(Function f : Display.functions) {
            StringBuilder tempout = new StringBuilder("y=");
            if(f instanceof LinearFunction) {
                LinearFunction lf = (LinearFunction) f;
                if(lf.getM() != 0d) {
                    if(lf.getM() != 1d)
                        tempout.append(formatter.format(lf.getM())).append("x");
                    else
                        tempout.append("x");
                }

                if(lf.getB() != 0d) {
                    if(lf.getB() > 0) {
                        tempout.append("+").append(formatter.format(lf.getB()));
                    }
                    else {
                        tempout.append(formatter.format(lf.getB()));
                    }
                }
            }

            else if(f instanceof QuadraticFunction) {
                QuadraticFunction qf = (QuadraticFunction) f;
                if(qf.getA() != 0d) {
                    if(qf.getA() == 1d)
                        tempout.append("x^2");
                    else if(qf.getA() == -1d) {
                        tempout.append("-x^2");
                    }
                    else {
                        tempout.append(formatter.format(qf.getA())).append("x^2");
                    }
                }

                if(qf.getB() != 0d) {
                    if(qf.getB() == 1d) {
                        tempout.append("+x");
                    }
                    else if(qf.getB() > 0) {
                        tempout.append("+").append(formatter.format(qf.getB())).append("x");
                    }
                    else if(qf.getB() == -1d) {
                        tempout.append("-x");
                    }
                    else {
                        tempout.append(formatter.format(qf.getB())).append("x");
                    }
                }

                if(qf.getC() != 0d) {
                    if(qf.getC() > 0) {
                        tempout.append("+").append(formatter.format(qf.getC()));
                    }
                    else {
                        tempout.append(formatter.format(qf.getC()));
                    }
                }
            }

            else if(f instanceof CubicFunction) {
                CubicFunction cf = (CubicFunction) f;

                if(cf.getX1() != 0d) {
                    if(cf.getX1() == 1d) {
                        tempout.append("x^3");
                    }
                    else if(cf.getX1() == -1d) {
                        tempout.append("-x^3");
                    }
                    else tempout.append(formatter.format(cf.getX1())).append("x^3");
                }

                if(cf.getX2() != 0d) {
                    if(cf.getX2() == 1d) {
                        tempout.append("+x^2");
                    }
                    else if(cf.getX2() == -1d) {
                        tempout.append("-x^2");
                    }
                    else if(cf.getX2() > 0) {
                        tempout.append("+").append(formatter.format(cf.getX2())).append("x^2");
                    }
                    else {
                        tempout.append(formatter.format(cf.getX2())).append("x^2");
                    }
                }

                if(cf.getX3() != 0d) {
                    if(cf.getX3() == 1d) {
                        tempout.append("+x");
                    }
                    else if(cf.getX3() == -1d) {
                        tempout.append("-x");
                    }
                    else if(cf.getX3() > 0) {
                        tempout.append("+").append(formatter.format(cf.getX3())).append("x");
                    }
                    else {
                        tempout.append(formatter.format(cf.getX3())).append("x");
                    }
                }

                if(cf.getX4() != 0d) {
                    if(cf.getX4() > 0) {
                        tempout.append("+").append(formatter.format(cf.getX4()));
                    }
                    else {
                        tempout.append(formatter.format(cf.getX4()));
                    }

                }

            }

            else if(f instanceof SinFunction) {
                SinFunction sf = (SinFunction) f;
                if(sf.getA() != 1) {
                    tempout.append(formatter.format(sf.getA()));
                }

                tempout.append("sin(");

                if(sf.getB() != 1) {
                    if(sf.getB() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(sf.getB()));
                    }
                }

                tempout.append("x)");

                if(sf.getC() != 0) {
                    if(sf.getC() > 0) {
                        tempout.append("+").append(formatter.format(sf.getC()));
                    }
                    else {
                        tempout.append(formatter.format(sf.getC()));
                    }
                }


            }

            else if(f instanceof CosFunction) {
                CosFunction cf = (CosFunction) f;
                if(cf.getA() != 1) {
                    tempout.append(formatter.format(cf.getA()));
                }

                tempout.append("cos(");

                if(cf.getB() != 1) {
                    if(cf.getB() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(cf.getB()));
                    }
                }

                tempout.append("x)");

                if(cf.getC() != 0) {
                    if(cf.getC() > 0) {
                        tempout.append("+").append(formatter.format(cf.getC()));
                    }
                    else {
                        tempout.append(formatter.format(cf.getC()));
                    }
                }


            }

            else if(f instanceof ExponentialFunction) {
                ExponentialFunction ef = (ExponentialFunction) f;

                if(ef.getA() != 1d) {
                    tempout.append(formatter.format(ef.getA())).append("*");
                }

                tempout.append(formatter.format(ef.getB())).append("^");

                if(ef.getC() != 1d) {
                    if(ef.getC() == -1d) {
                        tempout.append("-1");
                    }
                    else {
                        tempout.append(formatter.format(ef.getC()));
                    }
                }

                tempout.append("x");

                if(ef.getD() != 0d) {
                    if(ef.getD() > 0) {
                        tempout.append("+").append(formatter.format(ef.getD()));
                    }
                    else {
                        tempout.append(formatter.format(ef.getD()));
                    }

                }

            }

            else if(f instanceof XNFunction) {
                XNFunction xf = (XNFunction) f;

                if(xf.getA() != 1d) {
                    if(xf.getA() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(xf.getA()));
                    }
                }

                tempout.append("x^");

                tempout.append(formatter.format(xf.getB()));

                if(xf.getC() != 0d) {
                    if(xf.getC() > 0d) {
                        tempout.append("+").append(formatter.format(xf.getC()));
                    }
                    else {
                        tempout.append(formatter.format(xf.getC()));
                    }

                }

            }
            else if(f instanceof LogFunction) {
                LogFunction lf = (LogFunction) f;

                if(lf.getA() != 1d) {
                    if(lf.getA() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(lf.getA()));
                    }
                }

                tempout.append("log");

                if(lf.getB() != 10d) {
                    tempout.append("_").append(formatter.format(lf.getB()));
                }

                tempout.append("(");

                if(lf.getC() != 1d) {
                    if(lf.getC() == -1d) {
                        tempout.append("-");
                    }
                    else if(lf.getC() > 0d) {
                        tempout.append("+").append(formatter.format(lf.getC()));
                    }
                    else {
                        //c is negative
                        tempout.append(formatter.format(lf.getC()));
                    }
                }

                if(lf.getC() != 0d) {
                    tempout.append("x)");
                }
                else tempout.append(")");

                if(lf.getD() != 0d) {
                    if(lf.getD() > 0d) {
                        tempout.append("+").append(formatter.format(lf.getD()));
                    }
                    else tempout.append(formatter.format(lf.getD()));
                }

            }

            else if(f instanceof AbsFunction) {
                AbsFunction af = (AbsFunction) f;

                if(af.getA() != 1d) {
                    if(af.getA() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(af.getA()));
                    }
                }

                tempout.append("abs(");

                if(af.getB() != 1d) {
                    if(af.getB() == -1d) {
                        tempout.append("-");
                    }
                    else {
                        tempout.append(formatter.format(af.getB()));
                    }
                }

                tempout.append("x)");

                if(af.getC() != 0d) {
                    if(af.getC() > 0d) {
                        tempout.append("+").append(formatter.format(af.getC()));
                    }
                    else tempout.append(formatter.format(af.getC()));
                }

            }

            if(!tempout.toString().equals("")) {
                out.append(tempout.toString()).append("\n");
            }

        }

        return out.toString();
        
    }

    private static void drawString(Graphics g, String text, int x, int y) {
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }


}
