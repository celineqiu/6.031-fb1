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


public class FlingballParser {
    /**
     * Main method. Parses and then reprints an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        final String input = "board name=Example\n";
        System.out.println("INPUT: " + input);
        
        Game game = FlingballParser.parse(input);
    }
    
    private enum FlingballGrammar {
        GAME, BOARD, GADGET, SQUARE, CIRCLE, TRIANGLE, ABSORBER, INTERACTION, BALL, INTEGER, NAME, FLOAT, ANGLE, WHITESPACE
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

        Visualizer.showInBrowser(parseTree);

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
                final float defaultGravity = 25.0f;
                final float defaultFriction = 0.025f;
                
                final List<ParseTree<FlingballGrammar>> children = parseTree.children();
                
                // extract basic information from board
                final ParseTree<FlingballGrammar> board = children.get(0);
                assert(board.name() == FlingballGrammar.BOARD);
                
                final List<ParseTree<FlingballGrammar>> information = board.children();

                final String name = information.get(0).text();
                final float gravity = information.size() >= 2 ? Float.parseFloat(information.get(1).text()) : defaultGravity ; 
                final float friction1 = information.size() >= 3 ? Float.parseFloat(information.get(2).text()) : defaultFriction;
                final float friction2 = information.size() >= 4 ? Float.parseFloat(information.get(3).text()) : defaultFriction;
                
                final List<Ball> balls = new ArrayList<>();
                final List<Gadget> gadgets = new ArrayList<>();
                final Map<String, String> interactions = new HashMap<>();
                
                for (int i = 1; i < children.size(); i++) {
                    ParseTree<FlingballGrammar> child = children.get(i);
                    
                    switch (child.name()) {
                    case BALL:
                        balls.add(makeBallAST(child));
                    case GADGET:
                        gadgets.add(makeGadgetAST(child));
                    case INTERACTION:
                    {
                        // handle interaction between two objects
                        String triggerName = child.children().get(0).text();
                        String actionName = child.children().get(1).text(); 
                        interactions.put(triggerName, actionName);
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
            assert(parseTree.children().size() == 3);
            String name = parseTree.children().get(0).text();
            int x = Integer.parseInt(parseTree.children().get(1).text());
            int y = Integer.parseInt(parseTree.children().get(2).text());
            return new CircleBumper(name, x, y);
        }
        
        case SQUARE:
        {
            assert(parseTree.children().size() == 3);
            String name = parseTree.children().get(0).text();
            int x = Integer.parseInt(parseTree.children().get(1).text());
            int y = Integer.parseInt(parseTree.children().get(2).text());
            return new SquareBumper(name, x, y);
        }
        
        case TRIANGLE:
        {
            assert(parseTree.children().size() == 3 || parseTree.children().size() == 4);
            String name = parseTree.children().get(0).text();
            int x = Integer.parseInt(parseTree.children().get(1).text());
            int y = Integer.parseInt(parseTree.children().get(2).text());
            if (parseTree.children().size() == 4) {
                int angle = Integer.parseInt(parseTree.children().get(3).text());
                return new TriangleBumper(name, x, y, angle);
            }
            return new TriangleBumper(name, x, y);
            
        }
        
        case ABSORBER:
        {
            assert(parseTree.children().size() == 5);
            String name = parseTree.children().get(0).text();
            int x = Integer.parseInt(parseTree.children().get(1).text());
            int y = Integer.parseInt(parseTree.children().get(2).text());
            int width = Integer.parseInt(parseTree.children().get(3).text());
            int height = Integer.parseInt(parseTree.children().get(4).text());
            return new AbsorberBumper(name, x, y, width, height);
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
