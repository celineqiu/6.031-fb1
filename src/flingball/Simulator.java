package flingball;

import java.awt.Graphics;

/**
 * Simulator draws the game board and animates the movement of the balls.
 */
public class Simulator {
    private static final int GAMEBOARD_SIZE = 20;
    private static final int PIXELS_PER_L = 20;
    private static final int DRAWING_AREA_SIZE_IN_PIXELS = GAMEBOARD_SIZE * PIXELS_PER_L;
    
    private static final int TIMER_INTERVAL_MILLISECONDS = 50; // for ~20 frames per second
    
    /**
     * Main method. Make a window containing the drawing area.
     */
    public static void main(String[] args) {
        
    }
    
    /**
     * Initialize the game board animation. Adds the various gadgets (bumpers) to the window.
     */
    private static void initializeGame() {
    }
    
    /**
     * Animate the balls moving around the game board.
     * @param g graphics for the drawing buffer for the window.  Modifies this graphics by drawing the balls on it, at a 
     * position determined by the current clock time and the ball's current position and velocity.
     */
    private static void drawBalls(final Graphics g) {
        
    }
}
