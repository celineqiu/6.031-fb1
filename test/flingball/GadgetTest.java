package flingball;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;


public class GadgetTest {
    // Testing strategy for Gadget 
    //   Test of variant of gadget
    //     Square Bumper
    //     Circle Bumper
    //     Triangle Bumper
    //     Absorber
    //     Wall
    //   Test general methods of gadget
    //     name
    //     timeUntilCollison
    //     velocityAfterCollision
    //     trigger
    //     action
    //     copy
    //     toString
    //     equals
    //     hashCode
    //     addActionObject
    //     getActionObjects
    //   Test specific methods of gadgets
    
    // covers Absorber
    //     all general methods of gadget
    //     specific methods of Absorber
    //       getOrigin
    //       getWidth
    //       getHeight
    //       getEdges
    //       getCorners
    @Test
    public void testAbsorber() {
        final int X = 1;
        final int Y = 2;
        final int WIDTH = 5;
        final int HEIGHT = 6;
        
        Absorber abs = new Absorber("absorber", X, Y, WIDTH, HEIGHT);
        
        // define edges
        List<LineSegment> expectedEdges = new ArrayList<>();
        LineSegment bottom = new LineSegment(X+WIDTH, Y+HEIGHT, X, Y+HEIGHT);
        LineSegment top = new LineSegment(X, Y, X+WIDTH, Y);
        LineSegment left = new LineSegment(X, Y+HEIGHT, X, Y);
        LineSegment right = new LineSegment(X+WIDTH, Y, X+WIDTH, Y+HEIGHT);
        expectedEdges.add(bottom);
        expectedEdges.add(top);
        expectedEdges.add(left);
        expectedEdges.add(right);
        
        // define corners
        List<Circle> expectedCorners = new ArrayList<>();
        Circle bottomLeft = new Circle(X, Y+HEIGHT, 0);
        Circle bottomRight = new Circle(X+WIDTH, Y+HEIGHT, 0);
        Circle topLeft = new Circle(X, Y, 0);
        Circle topRight = new Circle(X+WIDTH, Y, 0);
        expectedCorners.add(bottomLeft);
        expectedCorners.add(bottomRight);
        expectedCorners.add(topLeft);
        expectedCorners.add(topRight);
        
        Ball ball = new Ball("ball", 1, 1, 5, 5);
        
        // name
        assertEquals("expected correct name", "absorber", abs.name());
        
        // timeUntilCollision
        LineSegment closestEdge = new LineSegment(0, 0, 1, 1);
        Circle closestCorner = new Circle(0, 0, 0);
        Double minEdge = Double.MAX_VALUE;
        Double minCorner = Double.MAX_VALUE;
        
        // find closest edge and corner
        for (LineSegment edge : expectedEdges) {
            Double time = Physics.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            if (time < minEdge) {
                minEdge = time;
                closestEdge = edge;
            }
        }
        for (Circle corner : expectedCorners) {
            Double time = Physics.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if (time < minCorner) {
                minCorner = time;
                closestCorner = corner;
            }
        }
        Double expectedTimeUntilCollision = Math.min(minEdge, minCorner);
        assertEquals("expected correct time until collision", expectedTimeUntilCollision, abs.timeUntilCollision(ball));
        
        // velocityAfterCollision 
        // find closest object & post collision velocity
        Vect expectedNewVel;
        if (minCorner <= minEdge) {
            expectedNewVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            expectedNewVel = Physics.reflectWall(closestEdge, ball.getVelocity());
        }
        assertEquals("expected same new velocity after collision", expectedNewVel, abs.velocityAfterCollision(ball));
        
        // trigger
        assertFalse("expected no trigger", abs.trigger(ball, 50*0.0001));
        
        // copy
        Absorber absCopy = abs.copy();
        assertEquals("expected same origin", abs.getOrigin(), absCopy.getOrigin());
        assertEquals("expected same width", abs.getWidth(), absCopy.getWidth());
        assertEquals("expected same height", abs.getHeight(), absCopy.getHeight());
        assertEquals("expected same edges", abs.getEdges(), absCopy.getEdges());
        assertEquals("expected same corners", abs.getCorners(), absCopy.getCorners());
        assertEquals("expected same balls", abs.getBalls(), absCopy.getBalls());
        
        // toString
        String expectedString = "name: absorber" + "\n" +
                          "origin: (1,2)" + "\n" +
                          "width: 5" + "\n" +
                          "height: 6";
        assertEquals("expected same string", expectedString, abs.toString());
        
        // equals
        Absorber absSame = new Absorber("absorber", X, Y, WIDTH, HEIGHT);
        assertEquals("expected same absorbers to be equal", absSame, abs);
        assertEquals("expected copies to be equal", absCopy, abs);
        assertEquals("expected symmetric equality", abs, abs);
        
        // hashCode
        int expectedHashCode = abs.name().hashCode() + (int)abs.getOrigin().x() + (int)abs.getOrigin().y() + abs.getWidth() + abs.getHeight();
        assertEquals("expected correct hashCode", expectedHashCode, abs.hashCode());
        
        // addActionObject, getActionObjects
        abs.addActionObject(absCopy);
        assertTrue("expected added action object to be in actionObjects", abs.getActionObjects().contains(absCopy));
        
        // getOrigin
        assertEquals("expected correct origin", new Vect(X, Y), abs.getOrigin());
        
        // getWidth
        assertEquals("expected correct width", WIDTH, abs.getWidth());
        
        // getHeight
        assertEquals("expected correct height", HEIGHT, abs.getHeight());
        
        // getEdges
        assertEquals("expected same edges", expectedEdges, abs.getEdges());
        
        // getCorners
        assertEquals("expected same corners", expectedCorners, abs.getCorners());
    }
    
    // covers CircleBumper
    //     all general methods of gadget
    //     specific methods of CircleBumper
    //          getOrigin
    //          getCircle
    
    @Test
    public void testGetOriginCircleBumper() {
        Vect expected = new Vect(10, 10);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertEquals("expected same origin", expected, circle.getOrigin());
    }
    
    @Test
    public void testGetCircleCircleBumper() {
        Circle expected = new Circle(10.5, 10.5, 0.5);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertEquals("expected same circle", expected, circle.getCircle());
    }
    
    @Test
    public void testNameCircleBumper() {
        String expected = "circle";
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertEquals("expected same name", expected, circle.name());
    }
    
    @Test
    public void testAddActionObject() {
        Absorber abs = new Absorber("absorber", 0, 0, 5, 5);
        List<Gadget> expected = new ArrayList<>();
        expected.add(abs);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        circle.addActionObject(abs);
        assertEquals("expected same action objects", expected, circle.getActionObjects());
    }
    
    @Test
    public void testTimeUntilCollisionCircleBumper() {
        Circle bumperCenter = new Circle(7.5, 7.5, 0.5);
        Circle ballCenter = new Circle(10, 10, 0.25);
        Vect vel = new Vect(-2, -2);
        Ball ball = new Ball("ball", 10, 10, -2, -2);
        CircleBumper circle = new CircleBumper("circle", 7, 7);
        Double expected = Physics.timeUntilCircleCollision(bumperCenter, ballCenter, vel);
        assertEquals("expected same time", expected, circle.timeUntilCollision(ball));
    }
    
    @Test
    public void testVelocityAfterCollisionCircleBumper() {
        Vect bumperCenter = new Vect(7.5, 7.5);
        Vect ballCenter = new Vect(10, 10);
        Vect vel = new Vect(-2, -2);
        Ball ball = new Ball("ball", 10, 10, -2, -2);
        CircleBumper circle = new CircleBumper("circle", 7, 7);
        Vect expected = Physics.reflectCircle(bumperCenter, ballCenter, vel);
        assertEquals("expected same time", expected, circle.velocityAfterCollision(ball));
    }
    
    @Test
    public void testEqualsCircleBumperDifferentType() {
        Circle diff = new Circle(10, 10, 1);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertFalse("expected not equal", circle.equals(diff));
    }
    
    @Test
    public void testEqualsCircleBumperDifferentCircleBumper() {
        CircleBumper diff = new CircleBumper("diff", 9, 10);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertFalse("expected not equal", circle.equals(diff));
    }
    
    @Test
    public void testEqualsCircleBumperSameCircleBumper() {
        CircleBumper copy = new CircleBumper("circle", 10, 10);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertTrue("expected equal", circle.equals(copy));
    }
    
    @Test
    public void testHashCodeCircleBumper() {
        CircleBumper copy = new CircleBumper("circle", 10, 10);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertEquals("expected same hash code", circle.hashCode(), copy.hashCode());
    }
    
    @Test
    public void testCopyCircleBumper() {
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertEquals("expected same bumper", circle, circle.copy());
    }
    
    @Test
    public void testToStringCircleBumper() {
        String expected = "name: circle" + "\n" +
                          "origin: (4,4)";
        CircleBumper circle = new CircleBumper("circle", 4, 4);
        assertEquals("expected same string", expected, circle.toString());
    }
    
    @Test
    public void testTriggerCircleBumperTrigered() {
        Ball ball = new Ball("ball", 10.5, 8, 0, 4);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertTrue("expected triggered", circle.trigger(ball, 10));
    }
    
    @Test
    public void testTriggerCircleBumperNotTriggered() {
        Ball ball = new Ball("ball", 10.5, 8, 0, 0.5);
        CircleBumper circle = new CircleBumper("circle", 10, 10);
        assertFalse("expected not triggered", circle.trigger(ball, 1));
    }
    
    // SquareBumper
    
    @Test
    public void testToStringSquareBumper() {
        String expected = "name: square" + "\n" +
                          "top left: (1,1)," + "\n" +
                          "top right: (2,1)," + "\n" +
                          "bottom right: (2,2)," + "\n" +
                          "bottom left: (1,2)";
        SquareBumper square = new SquareBumper("square", 1, 1);
        assertEquals("expected same string", expected, square.toString());
    }
    
    @Test
    public void testToStringTriangleBumper() {
        String expected = "name: triangle" + "\n" +
                          "origin: (5,5)" + "\n" +
                          "orientation: 180";
        TriangleBumper triangle = new TriangleBumper("triangle", 5, 5, 180);
        assertEquals("expected same string", expected, triangle.toString());
    }
    
    // covers Wall
    //     all general methods of gadget
    //     specific methods of Absorber
    //          getLine
    //          getCorner
    
    @Test
    public void testWall() {
        Wall wall = new Wall("top", 0, 0, 20, 0);
        
        // name()
        String name = "top";
        assertEquals("expected same name", name, wall.name());
        
        // getLine()
        LineSegment line = new LineSegment(0, 0, 20, 0);
        assertEquals("expected same line segment", line, wall.getLine());
        
        // getCorner()
        Circle corner = new Circle(0, 0, 0);
        assertEquals("expected same corner", corner, wall.getCorner());
        
        // timeUntilCollision()
        Circle ballCenter = new Circle(10, 10, 0.25);
        Vect ballVel = new Vect(0, -1);
        Ball ball = new Ball("ball", 10, 10, 0, -1);
        Double time = Physics.timeUntilWallCollision(line, ballCenter, ballVel);
        assertEquals("expected same time", time, wall.timeUntilCollision(ball));
        
        // velocityAfterCollision()
        Vect vel = Physics.reflectWall(line, ballVel);
        assertEquals("expected same velocity", vel, wall.velocityAfterCollision(ball));
        
        // equals()
        Wall wallCopy = new Wall("top", 0, 0, 20, 0);
        assertTrue("expected equal", wall.equals(wallCopy));
        
        // hashCode()
        assertEquals("expected same hash code", wall.hashCode(), wallCopy.hashCode());
        
        // trigger()
        assertTrue("expected triggered", wall.trigger(ball, 15));
        assertFalse("expected not triggered", wall.trigger(ball, 3));
        
        // copy()
        assertEquals("expected same wall", wall, wall.copy());
    }
    
    @Test
    public void testToStringWall() {
        String expected = "name: left" + "\n" +
                          "start: (0,0)" + "\n" +
                          "end: (0,20)";
        Wall wall = new Wall("left", 0, 0, 0, 20);
        assertEquals("expected same string", expected, wall.toString());
    }
}