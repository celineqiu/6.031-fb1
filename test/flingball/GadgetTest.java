package flingball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import static org.junit.Assert.*;
import physics.Physics;

import org.junit.Test;

import physics.Circle;


import physics.Vect;


public class GadgetTest {
    // Testing strategy for Gadget 
    //   Test of variant of gadget
    //     Square Bumper
    //     Circle Bumper
    //     Triangle Bumper
    //     Absorber
    //     Wall
    //   Test all general methods of gadget
    //     name
    //     timeUntilCollison
    //     velocityAfterCollision
    //     trigger
    //     action
    //     drawIcon
    //     copy
    //     toString
    //     equals
    //     hashCode
    //     addActionObject
    //   Test specific methods of gadgets
    
    // covers Absorber
    //     all general methods of gadget
    //     specific methods of Absorber
    //       getOrigin
    //       getWidth
    //       getHeight
    //       getEdges
    //       getCorners
    //       getBalls
    @Test
    public void testAbsorber() {
        Absorber abs = new Absorber("absorber", 1, 1, 5, 5);
        assertEquals("expected correct name", "absorber", abs.name());
        // timeUntilCollision
        // velocityAfterCollision
        // trigger
        // action
        // drawIcon
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
                          "origin: (1,1)" + "\n" +
                          "width: 5" + "\n" +
                          "height: 5";
        assertEquals("expected same string", expectedString, abs.toString());
        
        // equals
        Absorber absSame = new Absorber("absorber", 1, 1, 5, 5);
        assertEquals("expected same absorbers to be equal", absSame, abs);
        assertEquals("expected copies to be equal", absCopy, abs);
        assertEquals("expected symmetric equality", abs, abs);
        
        // hashCode
        int expectedHashCode = abs.name().hashCode() + (int)abs.getOrigin().x() + (int)abs.getOrigin().y() + abs.getWidth() + abs.getHeight();
        assertEquals("expected correct hashCode", expectedHashCode, abs.hashCode());
        
        // addActionObject
        abs.addActionObject(absCopy);
//        assertTrue("expected added action object to be in actionObjects", abs.getActionObjects().contains(absCopy));
        
        // getOrigin
        assertEquals("expected correct origin", new Vect(1, 1), abs.getOrigin());
        
        // getWidth
        // getEdges
        // getCorners
        // getBalls
    }
    
    // CircleBumper tests
    
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
    
    // TODO addActionObject
    
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
    
    @Test
    public void testToStringWall() {
        String expected = "name: left" + "\n" +
                          "start: (0,0)" + "\n" +
                          "end: (0,20)";
        Wall wall = new Wall("left", 0, 0, 0, 20);
        assertEquals("expected same string", expected, wall.toString());
    }

}