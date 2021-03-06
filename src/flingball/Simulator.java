package flingball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Simulator draws the game board and animates the movement of the balls.
 */
public class Simulator {
    // Abstraction Function:
    //   AF(GAMEBOARD_SIZE, PIXELS_PER_L, DRAWING_AREA_SIZE_IN_PIXELS, TIMER_INTERVAL_MILLISECONDS, game) = 
    //      simulator that simulates the running game flingball game of size GAMEBOARD_SIZE and displays it with resolution PIXELS_PER_L
    //      in new window of DRAWING_AREA_SIZE_IN_PIXELS and updates object positions every TIMER_INTERVAL_MILLISECONDS milliseconds
    // Rep Invariant:
    //      true
    // Safety from rep exposure:
    //   all fields private and final
    private final int GAMEBOARD_SIZE = 20;
    private final int PIXELS_PER_L = 20;
    private final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;
    
    private final int TIMER_INTERVAL_MILLISECONDS = 50; // for ~20 frames per second
    private final Game game;
    
    /**
     * Create a Flingball game simulator.
     * @param game to simulate
     */
    public Simulator(Game game) {
        this.game = game;
        checkRep();
    }
    
    private void checkRep() {
        assert true;
    }
    
    /**
     * Draw the Flingball game at TIMER_INTERVAL_MILLISECONDS milliseconds per frame
     */
    public void draw() {
        final JFrame window = new JFrame("flingball");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JPanel drawingArea = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                drawItems(g);
            }
        };
        drawingArea.setPreferredSize(new Dimension(DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        window.add(drawingArea);
        window.pack();
        window.setVisible(true);

        // note: the time must be javax.swing.Timer, not java.util.Timer
        new Timer(TIMER_INTERVAL_MILLISECONDS, (ActionEvent e) -> {
            drawingArea.repaint();
        }).start();
    }
    
    /**
     * Animate the balls moving around the game board.
     * @param g graphics for the drawing buffer for the window.  Modifies this graphics by drawing the balls on it, at a 
     * position determined by the current clock time and the ball's current position and velocity.
     */
    private void drawItems(final Graphics graphics) {
       Graphics2D g2 = (Graphics2D) graphics;  // every Graphics object is also a Graphics2D, which is a stronger spec
        
        // fill the background to erase everything
        g2.setColor(Color.black);
        g2.fill(new Rectangle2D.Double(0, 0, DRAWING_AREA_SIZE_IN_PIXELS, DRAWING_AREA_SIZE_IN_PIXELS));
        
        for (Ball ball : this.game.balls().values()) {
            ball.drawIcon(g2, PIXELS_PER_L);
        }
        
        for (Gadget gadget : this.game.gadgets().values()) {
            gadget.drawIcon(g2, PIXELS_PER_L, new ArrayList<Ball>(this.game.balls().values()), TIMER_INTERVAL_MILLISECONDS*0.001);
        }   
    }
}
