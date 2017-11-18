package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.Physics;
import physics.Vect;

/**
 * Represents a Circle Bumper Gadget in a Flingball game.
 */
class CircleBumper implements Gadget {
    private final String name;
    private final int x, y;
    private final Circle circle;
    private final List<Gadget> actionObjects = new ArrayList<>();
    
    // Abstract Function:
    //   AF(name, x, y, circle, actionObjects) = Circle bumper represented as a circle with a name, its 
    //      upper left corner of bounding box at (x, y), and actionObjects representing objects to be affected
    //      when the circle bumper is triggered,
    //      actionObjects representing objects to be affected when absorber is triggered
    // Rep Invariant:
    //   x and y must be between 0 and 19
    //   center of the circle must be at (x+0.5, y+0.5)
    //   radius of the circle must be 0.5
    //   actionObjects must be empty
    // Safety from rep exposure:
    //   all fields private and final
    
    /**
     * Create a Circle Bumper.
     * @param name of the circle bumper
     * @param x coord of the top left corner of the bounding box
     * @param y coord of the top left corner of the bounding box
     */
    public CircleBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.circle = new Circle(x+0.5, y+0.5, 0.5);
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        assert(x >= 0 && x <= 19);
        assert(y >= 0 && y <= 19);
        Vect center = circle.getCenter();
        assert(center.x() == x+0.5);
        assert(center.y() == y+0.5);
        assert(circle.getRadius() == 0.5);
    }
    
    /**
     * Get the origin of the Circle Bumper bounding box.
     * @return origin of circle bumper bounding box
     */
    public Vect getOrigin() {
        return new Vect(this.x, this.y);
    }
    
    /**
     * Get the Circle object representing the cirlce bumper
     * @return circle representing circle bumper
     */
    public Circle getCircle() {
        return new Circle(this.x+0.5, this.y+0.5, 0.5);
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public void addActionObject(Gadget actionObject) {
        actionObjects.add(actionObject);
    }
    
    @Override
    public List<Gadget> getActionObjects() {
        List<Gadget> actionObjectsCopy = new ArrayList<>();
        for (Gadget gadget : this.actionObjects) actionObjectsCopy.add(gadget.copy());
        return actionObjectsCopy;
    }
    
    @Override
    public Double timeUntilCollision(Ball ball) {
        return Physics.timeUntilCircleCollision(this.circle, ball.getCircle(), ball.getVelocity());
    }
    
   @Override
    public Vect velocityAfterCollision(Ball ball) {
        return Physics.reflectCircle(this.circle.getCenter(), ball.getCenter(), ball.getVelocity());
    }

    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                       "origin: (" + this.x + "," + this.y + ")";
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof CircleBumper)) {
            return false;
        } 
        CircleBumper thatBumper = (CircleBumper) that;
        if (this.name.equals(thatBumper.name()) &&
            this.getOrigin().equals(thatBumper.getOrigin()) &&
            this.circle.equals(thatBumper.getCircle())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.x + this.y + this.circle.hashCode();
    }
    
    @Override
    public boolean trigger(Ball ball, double deltaT) {
        if (timeUntilCollision(ball) < deltaT) {
            Vect newVel = this.velocityAfterCollision(ball);
            ball.setVelocity(newVel.x(), newVel.y());
            Vect displacement = new Vect(ball.getVelocity().x()*deltaT, ball.getVelocity().y()*deltaT);
            Vect newCenter = ball.getCenter().plus(displacement);
            ball.setCenter(newCenter.x(), newCenter.y());
            
            for (Gadget actionObject: actionObjects) {
                actionObject.action();
            }
            
            return true;
        }
        return false;
    }
    
    @Override
    public void action() {
        // no action
    }
    
    @Override
    public CircleBumper copy() {
        return new CircleBumper(this.name, this.x, this.y);
    }
    
    @Override
    public void drawIcon(Graphics2D g, final int scaler, List<Ball> balls, double deltaT) {
        
        g.setColor(Color.PINK); 
        for (Ball ball : balls) {
            if (trigger(ball, deltaT))  g.setColor(Color.YELLOW);
        }

        int displayX = (int) Math.round(x*scaler);
        int displayY = (int) Math.round(y*scaler);
        int displayRadius = (int) Math.round(circle.getRadius()*scaler);
        
        g.fillOval(displayX, displayY, displayRadius*2, displayRadius*2);
    }
}
