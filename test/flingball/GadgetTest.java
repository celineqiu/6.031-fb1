package flingball;

import static org.junit.Assert.*;

import org.junit.Test;


public class GadgetTest {
    // Testing strategy for Gadget 
    // TODO document testing strategy
   
    @Test
    public void testToStringAbsorber() {
        String expected = "name: absorber" + "\n" +
                          "origin: (1,1)" + "\n" +
                          "width: 5" + "\n" +
                          "height: 5";
        Absorber abs = new Absorber("absorber", 1, 1, 5, 5);
        assertEquals("expected same string", expected, abs.toString());
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