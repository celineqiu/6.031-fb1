package flingball;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.mit.eecs.parserlib.UnableToParseException;


public class ParserTest {
    
    // Testing strategy for FlingballParser
    //   Partition the inputs as follows:
    //     Ball (0, 1, >1)
    //     Gadgets (0, 1, >1 of each):
    //       Square Bumper
    //       Circle Bumper
    //       Triangle Bumper
    //         given / not given orientation
    //       Absorber
    //     Board:
    //       given / not given gravity, friction1, and friction2 values
    //       0, 1, >1 trigger / action events defined 
    //         self-triggering or not self-triggering
    //     with / without comments
    
    // covers 1 Ball
    //        Gadgets:
    //          3 Square Bumpers
    //          4 Circle Bumpers
    //          1 Triangle Bumper
    //            given orientation
    //          0 Absorbers
    //        Board:
    //          given gravity value
    //          not given friction1 or friction2 values
    //          no trigger events defined
    //          no action events defined
    //        without comments
    @Test
    public void testDefault() throws UnableToParseException {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball("BallA", 1.25, 1.25, 0, 0));
        
        List<Gadget> gadgets = new ArrayList<>();
        gadgets.add(new SquareBumper("SquareA", 0, 17));
        gadgets.add(new SquareBumper("SquareB", 1, 17));
        gadgets.add(new SquareBumper("SquareC", 2, 17));
        gadgets.add(new CircleBumper("CircleA", 1, 10));
        gadgets.add(new CircleBumper("CircleB", 7, 18));
        gadgets.add(new CircleBumper("CircleC", 8, 18));
        gadgets.add(new CircleBumper("CircleD", 9, 18));
        gadgets.add(new TriangleBumper("Tri", 12, 15, 180));
        gadgets.add(new Wall(0, 0, 20, 0));
        gadgets.add(new Wall(0, 0, 0, 20));
        gadgets.add(new Wall(0, 20, 20, 20));
        gadgets.add(new Wall(0, 20, 20, 0));
        
        Map<String,String> noInteractions = new HashMap<>();
        
        Game expected = new Game("Default", 25.f, 0.025f, 0.025f, balls, gadgets, noInteractions);
        Game parsed = FlingballParser.parse("boards/default.fb");
        
        assertEquals("expected parsed default game to match expected game", expected, parsed);
    }
    
    // covers 3 Balls
    //        Gadgets:
    //          0 Square Bumpers
    //          7 Circle Bumpers
    //          0 Triangle Bumpers
    //            given orientation
    //          2 Absorbers
    //        Board:
    //          given gravity
    //          not given friction1 or friction2 values  
    //          6 trigger / action events defined
    //            1 self-triggering
    //        with comments
    @Test
    public void testAbsorber() throws UnableToParseException {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball("BallA", 10.25, 15.25, 0, 0));
        balls.add(new Ball("BallB", 19.25, 3.25, 0, 0));
        balls.add(new Ball("BallC", 1.25, 5.25, 0, 0));
        
        List<Gadget> gadgets = new ArrayList<>();
        CircleBumper circleA = new CircleBumper("CircleA", 1, 10);
        CircleBumper circleB = new CircleBumper("CircleB", 2, 10);
        CircleBumper circleC = new CircleBumper("CircleC", 3, 10);
        CircleBumper circleD = new CircleBumper("CircleD", 4, 10);
        CircleBumper circleE = new CircleBumper("CircleE", 5, 10);
        CircleBumper circleF = new CircleBumper("CircleF", 10, 18);
        CircleBumper circleG = new CircleBumper("CircleG", 10, 19);
        Absorber abs1 = new Absorber("Abs1", 0, 18, 10, 2);
        Absorber abs2 = new Absorber("Abs2", 11, 18, 9, 2);
        
        gadgets.add(circleA);
        gadgets.add(circleB);
        gadgets.add(circleC);
        gadgets.add(circleD);
        gadgets.add(circleE);
        gadgets.add(circleF);
        gadgets.add(circleG);
        gadgets.add(new TriangleBumper("Tri", 19, 0, 90));
        gadgets.add(abs1);
        gadgets.add(abs2);
        gadgets.add(new Wall(0, 0, 20, 0));
        gadgets.add(new Wall(0, 0, 0, 20));
        gadgets.add(new Wall(0, 20, 20, 20));
        gadgets.add(new Wall(0, 20, 20, 0));
        
        Map<String,String> interactions = new HashMap<>();
        interactions.put("CircleA", "Abs1");
        interactions.put("CircleB", "Abs1");
        interactions.put("CircleC", "Abs1");
        interactions.put("CircleD", "Abs1");
        interactions.put("CircleE", "Abs1");
        interactions.put("Abs2", "Abs2");
        
        Game expected = new Game("Absorber", 25.f, 0.025f, 0.025f, balls, gadgets, interactions);
        Game parsed = FlingballParser.parse("boards/absorber.fb");
        
        assertEquals("expected parsed absorber game to match expected game", expected, parsed);
    }
    
    // covers 0 Balls
    //        No Gadgets (except Outer Walls)
    //        Board:
    //          not given gravity
    //          given friction1 and friction2 values
    //          no trigger / action events
    //        without comments
    @Test
    public void testEmpty() throws UnableToParseException {
        List<Ball> noBalls = new ArrayList<>();
        
        List<Gadget> gadgets = new ArrayList<>();
        gadgets.add(new Wall(0, 0, 20, 0));
        gadgets.add(new Wall(0, 0, 0, 20));
        gadgets.add(new Wall(0, 20, 20, 20));
        gadgets.add(new Wall(0, 20, 20, 0));
        
        Map<String,String> emptyInteractions = new HashMap<>();
        
        Game expected = new Game("Empty", 25.f, 0.03f, 0.02f, noBalls, gadgets, emptyInteractions);
        Game parsed = FlingballParser.parse("boards/empty.fb");
        
        assertEquals("expected parsed empty game to match expected game", expected, parsed);
    }
    
    // covers 0 Balls
    //        No Gadgets (except Outer Walls)
    //        Board:
    //          not given gravity
    //          given friction1 and friction2 values
    //          no trigger / action events
    //        without comments
    @Test
    public void testTriangleDefaultOrientation() throws UnableToParseException {
        List<Ball> balls = new ArrayList<>();
        balls.add(new Ball("BallA", 1.25, 1.25, 0, 0));
        
        List<Gadget> gadgets = new ArrayList<>();
        gadgets.add(new SquareBumper("SquareA", 0, 17));
        gadgets.add(new SquareBumper("SquareB", 1, 17));
        gadgets.add(new SquareBumper("SquareC", 2, 17));
        gadgets.add(new CircleBumper("CircleA", 1, 10));
        gadgets.add(new CircleBumper("CircleB", 7, 18));
        gadgets.add(new CircleBumper("CircleC", 8, 18));
        gadgets.add(new CircleBumper("CircleD", 9, 18));
        gadgets.add(new TriangleBumper("Tri", 12, 15));
        gadgets.add(new Wall(0, 0, 20, 0));
        gadgets.add(new Wall(0, 0, 0, 20));
        gadgets.add(new Wall(0, 20, 20, 20));
        gadgets.add(new Wall(0, 20, 20, 0));
        
        Map<String,String> noInteractions = new HashMap<>();
        
        Game expected = new Game("DefaultTriangle", 25.f, 0.025f, 0.025f, balls, gadgets, noInteractions);
        Game parsed = FlingballParser.parse("boards/defaultTriangle.fb");
        
        assertEquals("expected parsed default triangle game to match expected game", expected, parsed);
    }
}
