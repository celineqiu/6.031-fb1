package flingball;
import java.awt.Color;
import java.awt.Graphics2D;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/**
 * Represents a Square Bumper Gadget in a Flingball game.
 */
class SquareBumper implements Gadget {
    private final String name;
    private final int x, y;
    private final LineSegment bottom, top, left, right;
    private final Circle bottomLeft, bottomRight, topLeft, topRight;
    
    // Abstract Function:
    //   AF(name, x, y, bottom, top, left, right, bottomLeft, bottomRight, topLeft, topRight) 
    //     = Square bumper named name with upper left corner at (x, y),
    //       edges represented by bottom, top, left, right and 
    //       corners represented by bottomLeft, bottomRight, topLeft, topRight
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   coordinates in bottom, top, left, right, bottomLeft, bottomRight, topLeft, and topRight 
    //     must be between 0 and 20
    //   bottomLeft and bottomRight must be endpoints of bottom
    //   bottomLeft and topLeft must be endpoints of left
    //   topLeft and topRight must be endpoints of top
    //   topRight and bottomRight must be endpoints of right
    // Safety from rep exposure:
    //   all fields private and final
    
    public SquareBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.bottom = new LineSegment(x+1, y+1, x, y+1);
        this.top = new LineSegment(x, y, x+1, y);
        this.left = new LineSegment(x, y+1, x, y);
        this.right = new LineSegment(x+1, y, x+1, y+1);
        this.bottomLeft = new Circle(x, y+1, 0);
        this.bottomRight = new Circle(x+1, y+1, 0);
        this.topLeft = new Circle(x, y, 0);
        this.topRight = new Circle(x+1, y, 0);
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        // TODO
    }
    
    @Override
    public String name() {
        // TODO
    }
    
    /**
     * Calculate the time until the ball collides with this gadget.
     * @param ball in the playing area 
     * @return time until the ball colides with this gadget
     */
    public Double timeUntilCollision(Ball ball) {
        // TODO
    }
    
    /**
     * Calculate the new velocity of the ball after collision.
     * @return velocity of the ball after collision
     */
    public Vect velocityAfterCollision(Ball ball) {
        // TODO
    }
    
    @Override 
    public String toString() {
        // TODO
    }
    
    @Override
    public boolean equals(Object that) {
        // TODO
    }
    
    @Override
    public int hashCode() {
        // TODO
    }
    
    @Override
    public boolean trigger() {
        // TODO
    }
    
    @Override
    public void action() {
        // TODO
    }
    
    @Override
    public void drawIcon(Graphics2D g, final int scaler) {
        final int width = 1;
        final int height = 1;
        
        if (trigger()) {
            g.setColor(Color.YELLOW);
        }else {
            g.setColor(Color.RED); 
        }
        
        int displayX = (int) Math.round(x*scaler);
        int displayY = (int) Math.round(y*scaler);
        int displayWidth = (int) Math.round(width*scaler);
        int displayHeight = (int) Math.round(height*scaler);
        
        g.fillRect(displayX, displayY, displayWidth, displayHeight);
    }
}
