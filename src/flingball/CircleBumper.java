package flingball;

import physics.Circle;
import physics.Vect;

class CircleBumper implements Gadget {
    private final String name;
    private final int x, y;
    private final Circle circle;
    
    // Abstract Function:
    //   AF(name, circle) = Circle bumper represented as a circle named name with 
    //      upper left corner of bounding box at (x, y)   
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   center of the circle must be at (x+0.5, y+0.5)
    //   radius of the circle must be 0.5
    // Safety from rep exposure:
    //   all fields private and final
    
    public CircleBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.circle = new Circle(x+0.5, y+0.5, 0.5);
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
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
}
