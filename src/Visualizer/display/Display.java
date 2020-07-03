package Visualizer.display;

/**
 * like desmos if desmos was bad
 *
 */



import Visualizer.controls.KeyDetector;
import Visualizer.controls.MouseWheelDetector;
import Visualizer.functions.Function;
import Visualizer.tools.parsing.FunctionParsing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Display extends Canvas implements Runnable {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    private Thread thread;
    private static final String title = "Fiewer";
    private static Image icon;

    private JFrame frame;

    private static boolean running = false;

    public static final Color DEFAULT_LINE_COLOR = new Color(49, 146, 173);

    public static ArrayList<Function> functions = new ArrayList<>();

    public static EquationTextField etf;

    public static boolean showErrorMessage = false;
    public static double errorMessageStartTime;
    public static final int errorShowTimer = 5000;
    public static final String errorMessage = "Unsupported function.";
    private final Color errorFillColor = new Color(204, 96, 96, 225);
    private final Color errorOutlineColor = new Color(173, 80, 80,225);

    public static boolean infoShowing = false;

    Font font = new Font("Serif", Font.PLAIN, 24);
    public static final Font infoFont = new Font("Verdana", Font.PLAIN, 18);


    public static double zoomAmount = 100d;
    public static double zoomSpeed = 1d;

    private boolean showFps = false;

    private long lastTime;
    private double fps;

    public Display() {
        this.frame = new JFrame();

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        addKeyListener(new KeyDetector());
        addMouseWheelListener(new MouseWheelDetector());

    }


    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "Display");
        this.thread.start();
    }


    public synchronized void stop(){
        running = false;
        try{
            this.thread.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            FunctionParsing.stringToFunction("-x^2+2x");

            while(running) {
                render();

                //fps counter is from stack overflow but it's turned off
                if(showFps) {
                    fps = 1000000000.0 / (System.nanoTime() - lastTime);
                    lastTime = System.nanoTime();
                    this.frame.setTitle(title + " | " + Math.round(fps / 100) * 100 + " fps");
                }

            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    //a lot of JFrame things here. all my homies hate JFrame
    public static void main(String[] args) {

        //creating the display
        Display display = new Display();

        //etf is the thing you type equations into
        etf = new EquationTextField();

        ClearButton cb = new ClearButton();
        InfoButton ib = new InfoButton();

        display.frame.setTitle(title);

        try{
            icon = ImageIO.read(new File("resources/images/fiewericonsmall.png"));
            display.frame.setIconImage(icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

        display.frame.setLayout(new GridBagLayout());

        //mainPanel is the graph part and side panel is the buttons
        JPanel mainPanel = new JPanel();
        JPanel sidePanel = new JPanel();

        sidePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 25));

        mainPanel.add(display);
        display.frame.add(mainPanel);

        //adding buttons to the side panel
        sidePanel.add(etf);
        sidePanel.add(cb);
        sidePanel.add(ib);

        sidePanel.setPreferredSize(new Dimension(200, Display.HEIGHT));

        display.frame.add(sidePanel);

        SwingUtilities.updateComponentTreeUI(display.frame);

        //default JFrame things
        display.frame.pack();
        display.frame.getContentPane().setBackground(Color.WHITE);
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setBackground(Color.BLACK);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        //starting the thread in display
        display.start();
    }


    /*
        all of this is within the scope of the main panel, which contains the graph
     */
    private synchronized void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        if(infoShowing) {
            InfoButton.showInfo(g);
        }
        else {
            g.setFont(font);

            g.setColor(Color.WHITE);
            g.fillRect(0,0,WIDTH,HEIGHT);

            //drawing the graph
            draw(g);

            //drawing functions
            try{
                for(Function f : functions) {
                    Function.render(g, f.points, f.xs, f.ys);
                }
            } catch (ConcurrentModificationException cme) {
                //literally just trying again if there's a ConcurrentModificationException
                for(Function f : functions) {
                    Function.render(g, f.points, f.xs, f.ys);
                }
            }

            //error message
            if(showErrorMessage) {
                g.setColor(errorFillColor);
                g.fillRoundRect(WIDTH/3, Math.round(7*(float)HEIGHT/8), WIDTH/3, HEIGHT/12, 15, 15);

                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(errorOutlineColor);
                g2d.setStroke(new BasicStroke(10));
                g2d.drawRoundRect(WIDTH/3, Math.round(7*(float)HEIGHT/8), WIDTH/3, HEIGHT/12, 15, 15);
                g.setColor(Color.WHITE);
                g.drawString(errorMessage, WIDTH/3 + 25, 7*HEIGHT/8 + HEIGHT/24 + 7);
                if(System.currentTimeMillis() - errorMessageStartTime > errorShowTimer) showErrorMessage = false; //making the message disappear after a bit
            }
        }

        g.dispose();
        bs.show();

    }

    public void draw(Graphics g) {
        g.setColor(new Color(27, 60, 69));
        g.fillRect(-WIDTH, -HEIGHT, 2*WIDTH, 2*HEIGHT);

        g.setColor(Color.WHITE);
        g.fillRect(0, HEIGHT/2 - 2, WIDTH, 4);
        g.fillRect(WIDTH/2 -2, 0, 4, HEIGHT);
    }


}
