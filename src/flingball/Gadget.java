package flingball;
import physics.Vect;

public interface Gadget {
    
    // Datatype definition:
    //   Bumper = SquareBumper(name: String, x: int, y: int) 
    //          + CircleBumper(name: String, x: int, y: int, orientation: int) 
    //          + TriangleBumper(name: String, x: int, y: int, orientation: int) 
    //          + Absorber(name: String, x: int, y: int, width: int, height: int)
    //          + Wall(startX: int, startY: int, endX: int, endY: int)
    
    
    /**
     * Calculate the time until the ball collides with this gadget.
     * @param ball in the playing area 
     * @return time until the ball colides with this gadget
     */
    public Double timeUntilCollision(Ball ball);
    
    /**
     * Calculate the new velocity of the ball after collision.
     * @return velocity of the ball after collision
     */
    public Vect velocityAfterCollision(Ball ball);
    
    /**
     * @return a human-readable representation of this gadget.
     * Formatted such that the name, position, and orientation (if applicable) are returned.
     */
    @Override 
    public String toString();
    
    /**
     * @param that any object
     * @return true if and only if this and that are of the same type
     *         and have the same field values.
     */
    @Override
    public boolean equals(Object that);
    
    /**
     * @return hash code value consistent with the equals() definition of
     * equality, such that for all g1,g2:Gadget,
     *     g1.equals(g2) implies g1.hashCode() == g2.hashCode()
     */
    @Override
    public int hashCode();
}
