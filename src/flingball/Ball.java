package flingball;

import physics.Circle;
import physics.Vect;

class Ball {
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
    public Ball(double x, double y, double xVelocity, double yVelocity) {
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
     * @param gravityValue in L/sec2
     */
    public void gravity(double gravityValue) {
        // TODO
    }
    
    /**
     * Update the ball's position and velocity as affected by the default frictional constants
     * mu = 0.025/sec and mu2 = 0.025/L.
     */
    public void friction() {
        // TODO
    }
    
    /**
     * Update the ball's position and velocity as affected by the given frictional constants mu and mu2.
     */
    public void friction(double mu, double mu2) {
        // TODO
    }
}