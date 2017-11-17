package flingball;

import static org.junit.Assert.*;

import org.junit.Test;

import physics.Circle;
import physics.Vect;


public class BallTest {
    // Testing strategy for Ball:
    // TODO document testing strategy
    
    @Test
    public void testName() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        String expected = "ball";
        assertEquals("expected same name", expected, ball.name());
    }
    
    @Test
    public void testGetCenter() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        Vect expected = new Vect(10, 10);
        assertEquals("expected same center", expected, ball.getCenter());
    }
    
    @Test
    public void testSetCenter() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        assertEquals("expected same center", new Vect(10, 10), ball.getCenter());
        Vect expected = new Vect(5.5, 5.5);
        ball.setCenter(5.5, 5.5);
        assertEquals("expected changed center", expected, ball.getCenter());
    }
    
    @Test
    public void testGetVelocity() {
        Ball ball = new Ball("ball", 10, 10, 2, 2);
        Vect expected = new Vect(2, 2);
        assertEquals("expected same velocity", expected, ball.getVelocity());
    }
    
    @Test
    public void testSetVelocity() {
        Ball ball = new Ball("ball", 10, 10, 2, 2);
        assertEquals("expected same center", new Vect(2, 2), ball.getVelocity());
        Vect expected = new Vect(3.6, -7.12);
        ball.setVelocity(3.6, -7.12);
        assertEquals("expected changed velocity", expected, ball.getVelocity());
    }
    
    @Test
    public void testGetCircle() {
        Ball ball = new Ball("ball", 19.2, 3.28, 2, 9);
        Circle expected = new Circle(19.2, 3.28, 0.25);
        assertEquals("expected same circle", expected, ball.getCircle());
    }
    
    @Test
    public void testToString() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        String expected = "name: ball" + "\n" +
                          "center: (10.0,10.0)" + "\n" +
                          "velocity: (0.0,0.0)";
        assertEquals("expected same string", expected, ball.toString());
    }
    
    @Test
    public void testEqualsDifferentType() {
        Ball ball = new Ball("ball", 8, 4, 2, 6.3);
        Vect vect = new Vect(2, 9.1);
        assertFalse("expected not equal", ball.equals(vect));
    }
    
    @Test
    public void testEqualsDifferentBall() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        Ball diff = new Ball("different", 2, 10, 9, 4);
        assertFalse("expected not equal", ball.equals(diff));
    }
    
    @Test
    public void testEqualsSameBall() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        Ball copy = new Ball("ball", 10, 10, 0, 0);
        assertTrue("expected equal", ball.equals(copy));
    }
    
    @Test
    public void testHashCode() {
        Ball ball = new Ball("ball", 10, 10, 0, 0);
        Ball copy = new Ball("ball", 10, 10, 0, 0);
        assertEquals("expected same hash code", ball.hashCode(), copy.hashCode());
    }
    
    @Test
    public void testCopy() {
        Ball ball = new Ball("ball", 18, 3.4, 9, -4.2);
        assertEquals("expected same ball", ball, ball.copy());
    }
    
    @Test
    public void testGravity() {
        Ball ball = new Ball("ball", 10, 10, 3.6, 4.2);
        Vect expected = new Vect(3.6, 4.5);
        ball.gravity(0.3,1);
        // weird issue with doubles
        Vect difference = ball.getVelocity().minus(expected);
        assertTrue("expected low error", difference.length() < 0.00000001);
    }
    
    @Test
    public void testFriction() {
        Ball ball = new Ball("ball", 10, 10, 3, 4);
        Vect expected = new Vect(1.5, 2);
        ball.friction(0.25, 0.05,1);
        assertEquals("expected changed velocity", expected, ball.getVelocity());
    }
}
