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
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        // TODO
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
    public void getVelocity(double x, double y) {
        this.velocity = new Vect(x, y);
    }
    
    /**
     * Update the ball's position and velocity as affected by the default gravity value 25 L/sec2.
     */
    public void gravity() {
        // TODO
    }
    
    /**
     * Update the ball's position and velocity as affected by a given gravity value in L/sec2.
     * @param gravity value in L/sec2
     */
    public void gravity(double gravity) {
        // TODO
    }
    
    /**
     * Update the ball's position and velocity as affected by the default frictional constants
     * friction1 (mu) = 0.025/sec and friction2 (mu2) = 0.025/L.
     */
    public void friction() {
        // TODO
    }
    
    /**
     * Update the ball's position and velocity as affected by the given 
     * frictional constants friction1 and friction2 (mu and mu2).
     */
    public void friction(double friction1, double friction2) {
        // TODO
    }
    
    public void drawIcon(final Graphics2D g, final int scaler) {
        g.setColor(Color.BLUE);
        int displayX = (int) Math.round(ball.getCenter().x()*scaler);
        int displayY = (int) Math.round(ball.getCenter().y()*scaler);
        int displayRadius = (int) Math.round(ball.getRadius()*scaler);
        
        g.fillOval(displayX, displayY, displayRadius, displayRadius);
        
    }
}
