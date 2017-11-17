package flingball;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;

/**
 * Flingball Parser that parses a game file into a Flingball game.
 */
public class FlingballParser {
    /**
     * Main method. Parses and then reprints an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        // Example input for parsing
//        final String input = "#comment in the first line\n"
//                + "board name=Example\n"
//                + "squareBumper name=Square x=0 y=2\n"
//                + "#this is comment\n"
//                + "ball name=Ball x=1.8 y=4.5 xVelocity=-3.4 yVelocity=-2.3\n"
//                + "triangleBumper name=Tri x=1 y=1 orientation=270\n"
//                + "absorber name=Abs x=0 y=19 width=20 height=1 \n"
//                + "fire trigger=Tri action=Abs"; 
        // default.fb
        final String input = "board name=Default gravity = 25.0\n" + 
                "\n" + 
                "# define a ball\n" + 
                "ball name=BallA x=1.25 y=1.25 xVelocity=0 yVelocity=0\n" + 
                "\n" + 
                "# define a series of square bumpers\n" + 
                "squareBumper name=SquareA x=0 y=17\n" + 
                "squareBumper name=SquareB x=1 y=17\n" + 
                "squareBumper name=SquareC x=2 y=17\n" + 
                "\n" + 
                "# define a series of circle bumpers\n" + 
                "circleBumper name=CircleA x=1 y=10\n" + 
                "circleBumper name=CircleB x=7 y=18\n" + 
                "circleBumper name=CircleC x=8 y=18\n" + 
                "circleBumper name=CircleD x=9 y=18\n" + 
                "\n" + 
                "# define a triangle bumper\n" + 
                "triangleBumper name=Tri x=12 y=15 orientation=180";
        System.out.println("INPUT: " + input);
        
        FlingballParser.parse(input);
    }
    
    private enum FlingballGrammar {
        GAME, BOARD, GADGET, INTERACTION,
        SQUARE, CIRCLE, TRIANGLE, ABSORBER, BALL, 
        GRAVITY, FRICTION1, FRICTION2,
        INTEGER, NAME, FLOAT, ANGLE, COMMENT, WHITESPACE
    }

    private static Parser<FlingballGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<FlingballGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/flingball/Flingball.g");
            return Parser.compile(grammarFile, FlingballGrammar.GAME);            

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }

    }
    
    
    /**
     * Parse a string into an expression.
     * @param string string to parse
     * @return IntegerExpression parsed from the string
     * @throws UnableToParseException if the string doesn't match the IntegerExpression grammar
     */
    public static Game parse(final String string) throws UnableToParseException {
        final ParseTree<FlingballGrammar> parseTree = parser.parse(string);
        //System.out.println("parse tree " + parseTree);

       // Visualizer.showInBrowser(parseTree);

        final Game game = makeGameAST(parseTree);
        System.out.println("AST " + game);
        return game;
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * @param parseTree constructed according to the grammar in FlingballExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Game makeGameAST(final ParseTree<FlingballGrammar> parseTree) {
        switch (parseTree.name()) {
        case GAME: // game ::= board definition*;
            {
                final List<ParseTree<FlingballGrammar>> children = parseTree.children();
                int currentIndex = 0;
                while (children.get(currentIndex).name() != FlingballGrammar.BOARD) {
                    currentIndex++;
                }
                
                final float defaultGravity = 25.0f;
                final float defaultFriction = 0.025f;

                // extract basic information from board
                final ParseTree<FlingballGrammar> board = children.get(currentIndex);
                assert(board.name() == FlingballGrammar.BOARD);
                
                final List<ParseTree<FlingballGrammar>> information = board.children();

                final String name = information.get(0).text();
                float gravity = defaultGravity;
                float friction1 = defaultFriction;
                float friction2 = defaultFriction;
                
                for (ParseTree<FlingballGrammar> fact : information) {
                    switch (fact.name()) {
                    case GRAVITY:
                        gravity = Float.parseFloat(fact.children().get(0).text());
                        break;
                    case FRICTION1:
                        friction1 = Float.parseFloat(fact.children().get(0).text());
                        break;
                    case FRICTION2:
                        friction2 = Float.parseFloat(fact.children().get(0).text());
                        break;
                    default:
                        break;
                    }
                }
                
                final List<Ball> balls = new ArrayList<>();
                final List<Gadget> gadgets = new ArrayList<>();
                final Map<String, String> interactions = new HashMap<>();
                
                for (int i = currentIndex + 1; i < children.size(); i++) {
                    ParseTree<FlingballGrammar> child = children.get(i);
                    
                    switch (child.name()) {
                    case COMMENT:
                        break;
                    case BALL:
                        {
                            balls.add(makeBallAST(child));
                            break;
                        }
                    case GADGET:
                        {
                            gadgets.add(makeGadgetAST(child));
                            break;
                        }
                    case INTERACTION:
                    {
                        // handle interaction between two objects
                        System.out.println("NAME: "+child.name());
                        System.out.println("SIZE: "+child.children().size());
                        System.out.println("FIRST CHILD: "+child.children().get(0).name());
                        String triggerName = child.children().get(0).text();
                        String actionName = child.children().get(1).text(); 
                        interactions.put(triggerName, actionName);
                        break;
                    }
                    default:
                        break;
                    }
                }

                return new Game(name, gravity, friction1, friction2, balls, gadgets, interactions);
            }

        default:
            {
                System.out.println(parseTree.name());
                System.out.println(parseTree.text());
                throw new AssertionError("should never get here");
            }
        }

    }

    /**
     * make a Abstract syntax tree for Gadgets
     * @param parseTree constructed according to the grammar in FlingballExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Gadget makeGadgetAST(ParseTree<FlingballGrammar> parseTree) {
        assert(parseTree.children().size() == 1);
        ParseTree<FlingballGrammar> specific = parseTree.children().get(0);
        switch (specific.name()) {
        case CIRCLE:
        {
            assert(specific.children().size() == 3);
            String name = specific.children().get(0).text();
            int x = Integer.parseInt(specific.children().get(1).text());
            int y = Integer.parseInt(specific.children().get(2).text());
            return new CircleBumper(name, x, y);
        }
        
        case SQUARE:
        {   
            assert(specific.children().size() == 3);
            String name = specific.children().get(0).text();
            int x = Integer.parseInt(specific.children().get(1).text());
            int y = Integer.parseInt(specific.children().get(2).text());
            return new SquareBumper(name, x, y);
        }
        
        case TRIANGLE:
        {
            assert(specific.children().size() == 3 || specific.children().size() == 4);
            String name = specific.children().get(0).text();
            int x = Integer.parseInt(specific.children().get(1).text());
            int y = Integer.parseInt(specific.children().get(2).text());
            if (specific.children().size() == 4) {
                int angle = Integer.parseInt(specific.children().get(3).text());
                return new TriangleBumper(name, x, y, angle);
            }
            return new TriangleBumper(name, x, y);
            
        }
        
        case ABSORBER:
        {
            assert(specific.children().size() == 5);
            String name = specific.children().get(0).text();
            int x = Integer.parseInt(specific.children().get(1).text());
            int y = Integer.parseInt(specific.children().get(2).text());
            int width = Integer.parseInt(specific.children().get(3).text());
            int height = Integer.parseInt(specific.children().get(4).text());
            return new Absorber(name, x, y, width, height);
        }

        default:
            throw new AssertionError("unknown gadget type");
        }
    }
    
    /**
     * make a Abstract syntax tree for Ball class
     * @param parseTree constructed according to the grammar in FlingballExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Ball makeBallAST(ParseTree<FlingballGrammar> parseTree) {
        assert(parseTree.children().size() == 5);
        String name = parseTree.children().get(0).text();
        Float x = Float.parseFloat(parseTree.children().get(1).text());
        Float y = Float.parseFloat(parseTree.children().get(2).text());
        Float xVelocity = Float.parseFloat(parseTree.children().get(3).text());
        Float yVelocity = Float.parseFloat(parseTree.children().get(4).text());
        return new Ball(name, x, y, xVelocity, yVelocity);
    }

}
