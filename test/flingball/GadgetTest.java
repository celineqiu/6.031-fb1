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
    
    // covers Square
    //     all general methods of gadget
    //     specific methods of Square
    //       getOrigin
    //       getEdges
    //       getCorners
    @Test
    public void testSquare() {
        final int X = 1;
        final int Y = 2;
        
        SquareBumper square = new SquareBumper("square", X, Y);
        
        // define edges and corners
        List<LineSegment> expectedEdges = new ArrayList<>();
        LineSegment bottom = new LineSegment(X+1, Y+1, X, Y+1);
        LineSegment top = new LineSegment(X, Y, X+1, Y);
        LineSegment left = new LineSegment(X, Y+1, X, Y);
        LineSegment right = new LineSegment(X+1, Y, X+1, Y+1);
        expectedEdges.add(bottom);
        expectedEdges.add(top);
        expectedEdges.add(left);
        expectedEdges.add(right);
        
        List<Circle> expectedCorners = new ArrayList<>();
        Circle bottomLeft = new Circle(X, Y+1, 0);
        Circle bottomRight = new Circle(X+1, Y+1, 0);
        Circle topLeft = new Circle(X, Y, 0);
        Circle topRight = new Circle(X+1, Y, 0);
        expectedCorners.add(bottomLeft);
        expectedCorners.add(bottomRight);
        expectedCorners.add(topLeft);
        expectedCorners.add(topRight);
        
        Ball ball = new Ball("ball", 1, 1, 5, 5);
        
        // name
        assertEquals("expected correct name", "square", square.name());
        
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
        assertEquals("expected correct time until collision", expectedTimeUntilCollision, square.timeUntilCollision(ball));
        
        // velocityAfterCollision 
        // find closest object & post collision velocity
        Vect expectedNewVel;
        if (minCorner <= minEdge) {
            expectedNewVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            expectedNewVel = Physics.reflectWall(closestEdge, ball.getVelocity());
        }
        assertEquals("expected same new velocity after collision", expectedNewVel, square.velocityAfterCollision(ball));
        
        // trigger
        assertFalse("expected no trigger", square.trigger(ball, 50*0.0001));
        
        // copy
        SquareBumper squareCopy = square.copy();
        assertEquals("expected same origin", square.getOrigin(), squareCopy.getOrigin());
        assertEquals("expected same edges", square.getEdges(), squareCopy.getEdges());
        assertEquals("expected same corners", square.getCorners(), squareCopy.getCorners());
        
        // toString
        String expectedString = "name: square" + "\n" +
                "top left: (1,2)," + "\n" +
                "top right: (2,2)," + "\n" +
                "bottom right: (2,3)," + "\n" +
                "bottom left: (1,3)";
        assertEquals("expected same string", expectedString, square.toString());
        
        // equals
        SquareBumper squareSame = new SquareBumper("square", X, Y);
        assertEquals("expected same squares to be equal", squareSame, square);
        assertEquals("expected copies to be equal", squareCopy, square);
        assertEquals("expected symmetric equality", square, square);
        
        // hashCode
        int expectedHashCode = square.name().hashCode() + (int)square.getOrigin().x() + (int)square.getOrigin().y() + expectedEdges.hashCode() + expectedCorners.hashCode();
        assertEquals("expected correct hashCode", expectedHashCode, square.hashCode());
        
        // addActionObject, getActionObjects
        square.addActionObject(squareCopy);
        assertTrue("expected added action object to be in actionObjects", square.getActionObjects().contains(squareCopy));
        
        // getOrigin
        assertEquals("expected correct origin", new Vect(X, Y), square.getOrigin());
        
        // getEdges
        assertEquals("expected same edges", expectedEdges, square.getEdges());
        
        // getCorners
        assertEquals("expected same corners", expectedCorners, square.getCorners());
    }
    
    // covers Square
    //     all general methods of gadget
    //     specific methods of Square
    //       getOrigin
    //       getOrientation
    //       getLegs
    //       getCorners
    @Test
    public void testTriangle() {
        final int X = 1;
        final int Y = 2;
        final int ORIENTATION = 180;
        
        TriangleBumper triangle = new TriangleBumper("triangle", X, Y, ORIENTATION);
        
        // define edges and corners
        List<LineSegment> expectedEdges = new ArrayList<>();
        List<Circle> expectedCorners = new ArrayList<>();
        
        // triangles legs drawn counterclockwise
        Vect p1 = new Vect(X, Y+1);
        Vect p2 = new Vect(X+1, Y+1);
        Vect p3 = new Vect(X+1, Y);
 
        Circle rightAngleCorner = new Circle(p2, 0);
        Circle cornerA = new Circle(p3, 0);
        Circle cornerB = new Circle(p1, 0);
        expectedCorners.add(rightAngleCorner);
        expectedCorners.add(cornerA);
        expectedCorners.add(cornerB);
        
        LineSegment legA = new LineSegment(p1, p2);
        LineSegment legB = new LineSegment(p2, p3);
        LineSegment hypotenuse = new LineSegment(p3, p1);
        expectedEdges.add(legA);
        expectedEdges.add(legB);
        expectedEdges.add(hypotenuse);
        
        Ball ball = new Ball("ball", 1, 1, 5, 5);
        
        // name
        assertEquals("expected correct name", "triangle", triangle.name());
        
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
        assertEquals("expected correct time until collision", expectedTimeUntilCollision, triangle.timeUntilCollision(ball));
        
        // velocityAfterCollision 
        // find closest object & post collision velocity
        Vect expectedNewVel;
        if (minCorner <= minEdge) {
            expectedNewVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            expectedNewVel = Physics.reflectWall(closestEdge, ball.getVelocity());
        }
        assertEquals("expected same new velocity after collision", expectedNewVel, triangle.velocityAfterCollision(ball));
        
        // trigger
        assertFalse("expected no trigger", triangle.trigger(ball, 50*0.0001));
        
        // copy
        TriangleBumper triangleCopy = triangle.copy();
        assertEquals("expected same origin", triangle.getOrigin(), triangleCopy.getOrigin());
        assertEquals("expected same edges", triangle.getLegs(), triangleCopy.getLegs());
        assertEquals("expected same corners", triangle.getCorners(), triangleCopy.getCorners());
        
        // toString
        String expectedTriangle = "name: triangle" + "\n" +
                "origin: (1,2)" + "\n" +
                "orientation: 180";
        assertEquals("expected same string", expectedTriangle, triangle.toString());
        
        // equals
        TriangleBumper triangleSame = new TriangleBumper("triangle", X, Y, ORIENTATION);
        assertEquals("expected same triangles to be equal", triangleSame, triangle);
        assertEquals("expected copies to be equal", triangleCopy, triangle);
        assertEquals("expected symmetric equality", triangle, triangle);
        
        // hashCode
        int expectedHashCode = triangle.name().hashCode() + (int)triangle.getOrigin().x() + (int)triangle.getOrigin().y() + ORIENTATION;
        assertEquals("expected correct hashCode", expectedHashCode, triangle.hashCode());
        
        // addActionObject, getActionObjects
        triangle.addActionObject(triangleCopy);
        assertTrue("expected added action object to be in actionObjects", triangle.getActionObjects().contains(triangleCopy));
        
        // getOrigin
        assertEquals("expected correct origin", new Vect(X, Y), triangle.getOrigin());
        
        // getOrientation
        assertEquals("expected correct orientation", ORIENTATION, triangle.getOrientation());
        
        // getEdges
        assertEquals("expected same edges", expectedEdges, triangle.getLegs());
        
        // getCorners
        assertEquals("expected same corners", expectedCorners, triangle.getCorners());
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