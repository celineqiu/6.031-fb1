package flingball;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import javax.management.monitor.GaugeMonitor;

import edu.mit.eecs.parserlib.UnableToParseException;


/**
 * Console interface to the Flingball expression system.
 */
public class Flingball {
    
    /**
     * Main method. Loads and runs Flingball board from file pathname.
     * If no path is specified, then loads and runs default Flingball 
     * board.
     * 
     * Displays the running game as a graphical user interface that 
     * pops up in a new window.
     * 
     * @param args command line arguments -- optional argument specifying
     * file pathname of Flingball board
     */
    public static void main(String[] args) throws IOException{
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        try {
            while (true) {
                System.out.print("Enter your Filepath, enter q to quit\n"
                        + "> ");
                final String input = in.readLine();
                
                if (input.equals("q")) {
                    System.exit(0); // exits the program
                }

                try {
                    final String fileContent = getString(input);
                    final Game game = FlingballParser.parse(fileContent);
                    final Simulator simulator = new Simulator(game);
                    simulator.draw();
                    game.run();
                    System.out.println("displaying the game");
                    } 
                catch (FileNotFoundException fe) {
                    displayDefault();
                    System.out.println("didn't find game, displaying default.fb");
                }
                catch (UnableToParseException npe) {
                    displayDefault();
                    System.out.println("can't parse game, displaying default.fb");
                }
            }
            
        } catch (UnableToParseException npe0) {
            System.out.println("default.fb not correctly setup");
            System.exit(0);
        } catch (FileNotFoundException npe0) {
            System.out.println("can't find default.fb");
            System.exit(0);
        }
        
    }
    
    private static String getString(String filepath) throws FileNotFoundException{
        File file = new File(filepath);
        String wordString = new String();
        Scanner fileWords = new Scanner(file);
        while(fileWords.hasNextLine()) {
            wordString = wordString + "\n" + fileWords.nextLine();
        }
        fileWords.close();
        System.out.println(wordString);
        return wordString;
    }

    public static void displayDefault() throws UnableToParseException, FileNotFoundException{
        final String defaultContent = getString("boards/default.fb");
        final Game defaultGame = FlingballParser.parse(defaultContent);
        final Simulator simulator = new Simulator(defaultGame);
        simulator.draw();
        defaultGame.run();
    }
    
}