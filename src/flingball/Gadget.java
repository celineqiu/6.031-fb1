package flingball;
import java.awt.Graphics2D;
import java.util.List;

import physics.Vect;

/**
 * Represents a Gadget in a Flingball game.
 */
public interface Gadget {
    
    // Datatype definition:
    //   Bumper = SquareBumper(name: String, x: int, y: int) 
    //          + CircleBumper(name: String, x: int, y: int, orientation: int) 
    //          + TriangleBumper(name: String, x: int, y: int, orientation: int) 
    //          + Absorber(name: String, x: int, y: int, width: int, height: int)
    //          + Wall(startX: int, startY: int, endX: int, endY: int)
    
    /**
     * @return the name of the gadget
     */
    public String name();
    
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
     * boolean that indicates if a gadget is on trigger from a ball
     * detailed condition varies according to specific gadget types
     * @param ball to check if it is triggering this gadget
     * @param deltaT threshold for collisions
     * @return boolean that indicates if a gadget is on trigger from a ball
     */
    public boolean trigger(Ball ball, double deltaT);
    
    /**
     * action a gadget takes after specific trigger is on
     */
    public void action();
    
    /**
     * draw corresponding icon on the given graphics object with
     * position and dimension scaled.
     * @param g Graphics to be drawn on, mutated
     * @param scaler coefficient that helps turning Gadget size into values;
     * @param balls list of balls
     * @param deltaT timestep of drawing the icon
     */
    public void drawIcon(Graphics2D g, int scaler, List<Ball> balls, double deltaT);
    
    /**
     * make a defensive copy of gadget
     * @return copy of gadget
     */
    public Gadget copy();
    
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
    
    /**
     * Add an object that actions after this gadget is triggered
     * @param actionObject the object that gets added
     */
    public void addActionObject(Gadget actionObject);

}
