package flingball;

import java.awt.Color;
import java.awt.Graphics2D;

import physics.Circle;
import physics.Vect;

/**
 * Represents a Ball in a Flingball game.
 */
class Ball {
    private String name;
    private Circle ball;
    private Vect velocity;
    private Boolean status = true;
    
    // Abstract Function:
    //   AF(ball, velocity) = ball of diameter 0.5L with a velocity in the Flingball playing area  
    // Rep invariant:
    //   diameter is 0.5L
    //   ball must be within the playing area
    // Safety from rep exposure:
    //   fields are final
    //   defensive copies are returned
    
    /**
     * Create a Ball object.
     * @param x coordinate of the center of the ball
     * @param y coordinate of the center of the ball
     * @param xVelocity x value of the ball velocity
     * @param yVelocity y value of the ball velocity
     */
    public Ball(String name, double x, double y, double xVelocity, double yVelocity) {
        System.out.println(name);
        System.out.println("ball x coord: " + x);
        System.out.println("ball y coord: " + y);
        this.name = name;
        this.ball = new Circle(x, y, 0.25);
        this.velocity = new Vect(xVelocity, yVelocity);
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        assert(ball.getRadius() == 0.25) : "radius must be equal to 0.25";
        Vect center = ball.getCenter();
        assert(center.x() >= 0.25 && center.x() <= 19.75) : "ball x pos must be in playing area";
        assert(center.y() >= 0.25 && center.y() <= 19.75) : "ball y pos must be in playing area";
    }
    
    /**
     * Get the name of this Ball.
     * @return the name of this Ball.
     */
    public String name() {
        return this.name;
    }
    
    /**
     * Get the coordinate of the center of the ball.
     * @return coordinate of the center of the ball
     */
    public Vect getCenter() {
        return new Vect(this.ball.getCenter().x(), this.ball.getCenter().y());
    }
    
    /**
     * Set the coordinate of the center of the ball.
     * @param x coordinate of the center of the ball
     * @param y coordinate of the center of the ball
     */
    public void setCenter(double x, double y) {
        this.ball = new Circle(x, y, 0.25);
    }
    
    /**
     * Get the velocity of the ball.
     * @return velocity of the ball
     */
    public Vect getVelocity() {
        return new Vect(this.velocity.x(), this.velocity.y());
    }
    
    /**
     * Set the velocity of the ball.
     * @x x value of the velocity of the ball
     * @y y value of the velocity of the ball
     */
    public void setVelocity(double x, double y) {
        this.velocity = new Vect(x, y);
    }
    
    /**
     * Get the circle representing the ball.
     * @return circle representing the ball
     */
    public Circle getCircle() {
        return new Circle(this.ball.getCenter().x(), this.ball.getCenter().y(), 0.25);
    }
    
    /**
     * Update the ball's position and velocity as affected by a given gravity value in L/sec2.
     * Time delta = 1
     * @param gravity value in L/sec2
     */
    public void gravity(double gravity, double deltaT) {
        setVelocity(velocity.x(), velocity.y() + gravity*deltaT);
    }
    
    /**
     * Update the ball's position and velocity as affected by the given 
     * frictional constants friction1 and friction2 (mu and mu2).
     */
    public void friction(double friction1, double friction2, double deltaT) {
        Double scale = 1 - friction1*deltaT - friction2 * velocity.length()*deltaT;
        Vect newVel = velocity.times(scale);
        setVelocity(newVel.x(), newVel.y());
    }
    
    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                       "center: (" + this.ball.getCenter().x() + "," + this.ball.getCenter().y() + ")" + "\n" +
                       "velocity: (" + this.velocity.x() + "," + this.velocity.y() + ")";
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Ball)) {
            return false;
        } 
        Ball thatBall = (Ball) that;
        if (this.name.equals(thatBall.name()) &&
            this.ball.equals(thatBall.getCircle()) &&
            this.velocity.equals(thatBall.getVelocity())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.ball.hashCode() + this.velocity.hashCode();
    }
    
    /**
     * Make a defensive copy of the ball
     * @return copy of the ball
     */
    public Ball copy() {
        return new Ball(this.name, ball.getCenter().x(), ball.getCenter().y(),
                        velocity.x(), velocity.y());
    }
    
    /**
     * Check if a ball is active. Balls held in an Absorber are inactive.
     * @return the status of the ball
     */
    public boolean isActive() {
        return this.status;
    }
    
    /**s
     * Update the status of the ball.
     * @param status new status the ball is updated with
     */
    public void setActive(boolean status) {
        this.status = status;
    }
    
    /**
     * Draw a ball icon on the given graphics with given scaler
     * @param g graphics that the icon is drawn on, mutated
     * @param scaler that the position and dimension is scaled,
     *        correspond with the value of L in the overall spec
     */
    public void drawIcon(final Graphics2D g, final int scaler) {

        g.setColor(Color.BLUE);
        int displayX = (int) Math.round(ball.getCenter().x()*scaler);
        int displayY = (int) Math.round(ball.getCenter().y()*scaler);
        int displayRadius = (int) Math.round(ball.getRadius()*scaler);
        
        g.fillOval(displayX, displayY, displayRadius*2, displayRadius*2);
    }
}
