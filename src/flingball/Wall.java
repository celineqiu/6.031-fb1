package flingball;
import physics.Circle;
import physics.LineSegment;
import physics.Vect;

/**
 * Represents a Wall Gadget in a Flingball game.
 */
class Wall implements Gadget {
    private final LineSegment line;
    private final Circle startCorner;
    
    // Abstract Function:
    //   AF(line, startCorner) = a wall represented by a line and 
    //     its starting point startCorner
    // Rep Invariant:
    //   the start point of line must correspond to startCorner
    //   startCorner coordinates must be between 0 and 20
    // Safety from rep exposure:
    //   fields are private and final
    
    public Wall(int startX, int startY, int endX, int endY) {
        this.line = new LineSegment(startX, startY, endX, endY);
        this.startCorner = new Circle(startX, startY, 0);
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

}
