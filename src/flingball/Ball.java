package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.channels.MulticastChannel;

import physics.Circle;
import physics.Vect;

/**
 * Represents a Ball in a Flingball game.
 */
class Ball {
    private String name;
    private Circle ball;
    private Vect velocity;
    
    // Abstract Function:
    //   AF(ball, velocity) = ball of diameter 0.5L with a velocity in the Flingball playing area  
    // Rep invariant:
    //   diameter is 0.5L
    //   velocities must range at least from 0 L/sec to 200 L/sec 
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
        this.name = name;
        this.ball = new Circle(x, y, 0.25);
        this.velocity = new Vect(xVelocity, yVelocity);
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        Double magSquared = velocity.dot(velocity);
        assert(magSquared >= 0 && magSquared <= 400) : "magSquared MulticastChannel be between 0 and 400";
        assert(ball.getRadius() == 0.25) : "radius must be equal to 0.25";
        Vect center = ball.getCenter();
        assert(center.x() >= 0.25 && center.x() <= 19.75);
        assert(center.y() >= 0.25 && center.y() <=19.75);
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
    public void gravity(double gravity) {
        setVelocity(velocity.x(), velocity.y() - gravity);
    }
    
    /**
     * Update the ball's position and velocity as affected by the given 
     * Time delta = 1
     * frictional constants friction1 and friction2 (mu and mu2).
     */
    public void friction(double friction1, double friction2) {
        // Vnew = Vold × ( 1 - mu × deltat - mu2 × |Vold| × deltat )
        Double scale = 1 - friction1 - friction2 * velocity.length();
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
    
    public void drawIcon(final Graphics2D g, final int scaler) {
        g.setColor(Color.BLUE);
        int displayX = (int) Math.round(ball.getCenter().x()*scaler);
        int displayY = (int) Math.round(ball.getCenter().y()*scaler);
        int displayRadius = (int) Math.round(ball.getRadius()*scaler);
        
        g.fillOval(displayX, displayY, displayRadius, displayRadius);
        
    }
}
