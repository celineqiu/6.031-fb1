package flingball;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
    
    @Test
    public void testToStringCircleBumper() {
        String expected = "name: circle" + "\n" +
                          "origin: (4,4)";
        CircleBumper circle = new CircleBumper("circle", 4, 4);
        assertEquals("expected same string", expected, circle.toString());
    }
    
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