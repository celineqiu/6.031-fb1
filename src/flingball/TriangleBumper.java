package flingball;

import physics.Circle;
import physics.LineSegment;
import physics.Vect;

class TriangleBumper implements Gadget {
    private final String name;
    private final int x, y, orientation;
    private final LineSegment legA, legB, hypotenuse;
    private final Circle cornerA, cornerB, rightAngleCorner;
    
    // Abstract Function:
    //   AF(name, x, y, orientation, legA, legB, hypotenuse, cornerA, cornerB, rightAngleCorner) 
    //     = Triangle bumper with named name with upper left corner of its bounding box at (x, y) 
    //       and with an orientation. The default orientation (0 degrees) places cornerA 
    //       in the northeast, rightAngleCorner in the northwest, and the cornerB in the southwest.
    //       legA is the side opposite of cornerA, legB is the side opposite of cornerB, 
    //         and hypotenuse, the longest side, is the side opposite of rightAngleCorner
    //       The hypotenuse goes from the southwest corner to the northeast corner. 
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   orientation must be 0, 90, 180, 270
    //   legA, legB and hypotenuse form a right triangle
    //   legA is the side opposite of cornerA, legB is the side opposite of cornerB, 
    //     and hypotenuse, the longest side, is the side opposite of rightAngleCorner
    // Safety from rep exposure:
    //   all fields private and final
    
    public TriangleBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = 0;
        // define the triangle sides and corners according to default orientation
    }
    
    public TriangleBumper(String name, int x, int y, int orientation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        // define the triangle sides and corners according to orientation
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
    public Vect getPosition() {
        return new Vect(x, y);
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
