package flingball;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import physics.Vect;

/**
 * A Flingball game containing balls and gadgets and characterized by gravity, friction constants, 
 * and trigger and action events.
 */
public class Game {

    private final String name;
    private final float gravity;
    private final float friction1;
    private final float friction2;
    private final Map<String, Ball> balls = new HashMap<>();
    private final Map<String, Gadget> gadgets = new HashMap<>();
    private final Map<Gadget, Gadget> interactions = new HashMap<>();
    
    private static final int TIMER_INTERVAL_MILLISECONDS = 50;
    private static final double TIMER_INTERVAL = TIMER_INTERVAL_MILLISECONDS *0.001;
    
    // Abstract Function:
    //   AF(name, gravity, friction1, friction2, balls, gadgets, interactions)
    //     = a Flingball Game named name with gravity, friction1 and friction2 constants, 
    //       balls and gadgets in the playing area
    //       and trigger / action event interactions 
    // Rep Invariant:
    //   String name of keys in balls and gadgets must match the name of its value object
    //   keys and values in interactions must be in gadgets
    // Safety from rep exposure:
    //   fields are private and final
    //   defensive copies of inputs are stored and returned
    
    /**
     * Check that the rep invariant is true.
     */
    private void checkRep() {
        // String name of keys in balls and gadgets must match the name of its value object
        for (String ballName : balls.keySet()) {
            ballName.equals(balls.get(ballName).name());
        }
        for (String gadgetName : gadgets.keySet()) {
            gadgetName.equals(gadgets.get(gadgetName).name());
        }
            
        // keys and values in interactions must be in gadgets
        for (Gadget trigger : interactions.keySet()) {
            assert gadgets.values().contains(trigger): "Trigger gadgets must be in gadgets map ";
            assert gadgets.values().contains(interactions.get(trigger)) : "Action gadgets must be in gadgets map";
        }
    }
    
    /**
     * Make a Game object for Flingball.
     * @param name of the game
     * @param gravity value. default value is 25.0f
     * @param friction1 value. default value is 0.025f
     * @param friction2 value. default value is 0.025f
     * @param balls in the Flingball game playing area
     * @param gadgets in the Flingball game playing area
     * @param interactions specifying trigger and action events between gadgets
     */
    public Game(String name, Float gravity, Float friction1, Float friction2, List<Ball> balls, List<Gadget> gadgets, Map<String,String> interactions) {
        this.name = name;
        this.gravity = gravity;
        this.friction1 = friction1;
        this.friction2 = friction2;

        for (Ball ball : balls) {
            // make a defensive copy of the ball before storing it to prevent rep exposure
            this.balls.put(ball.name(), ball.copy());
        }
        
        // create walls
        Wall top = new Wall("top", 0, 0, 20, 0);
        Wall left = new Wall("left", 0, 20, 0, 0);
        Wall right = new Wall("right", 20, 0, 20, 20);
        Wall bottom = new Wall("bottom", 20, 20, 0, 20);
        gadgets.add(top);
        gadgets.add(left);
        gadgets.add(right);
        gadgets.add(bottom);
        
        for (Gadget gadget : gadgets) {
            // make a defensive copy of the gadget before storing it to prevent rep exposure
            this.gadgets.put(gadget.name(), gadget);
        }
        
        for (String triggerName: interactions.keySet()) {
            Gadget triggerObject = this.gadgets.get(triggerName);
            Gadget actionObject = this.gadgets.get(interactions.get(triggerName));
            this.interactions.put(triggerObject, actionObject);
            triggerObject.addActionObject(actionObject);
        }
        
        
        
        checkRep();
    }
    
    /**
     * Start running the game
     */
    public void run() {
        try {
            while (true) {
                updateBalls();
                Thread.sleep(TIMER_INTERVAL_MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the name of this Game.
     * @return name of this Game
     */
    public String name() {
        return this.name;
    }
    
    /**
     * Get the gravity constant of this game.
     * @return gravity constant
     */
    public float gravity() {
        return this.gravity;
    }
    
    /**
     * Get the friction1 constant of this game.
     * @return friction1 constant
     */
    public float friction1() {
        return this.friction1;
    }
    
    /**
     * Get the friction2 constant of this game.
     * @return friction2 constant
     */
    public float friction2() {
        return this.friction2;
    }
    
    /**
     * Get the balls in this game.
     * @return balls in this game
     */
    public Map<String, Ball> balls() {
        Map<String, Ball> ballsCopy = new HashMap<>();
        for (String ball : this.balls.keySet()) {
            ballsCopy.put(ball, balls.get(ball).copy());
        }
        return ballsCopy;
    }
    
    /**
     * Get the gadgets in this game.
     * @return gadgets in this game
     */
    public Map<String, Gadget> gadgets() {
        Map<String, Gadget> gadgetsCopy = new HashMap<>();
        for (String gadget : this.gadgets.keySet()) {
            gadgetsCopy.put(gadget, gadgets.get(gadget).copy());
        }
        return gadgetsCopy;
    }
    
    /**
     * Get the interactions in this game.
     * @return interactions in this game
     */
    public Map<Gadget, Gadget> interactions() {
        Map<Gadget, Gadget> interactionsCopy = new HashMap<>();
        for (Gadget trigger : this.interactions.keySet()) {
            interactionsCopy.put(trigger.copy(), interactions.get(trigger).copy());
        }
        return interactionsCopy;
    }
    
    /**
     * Check for triggers at every frame of the Flingball Game.
     */
    public void checkTriggers() {
        for (Gadget triggerObject : interactions.keySet()) {
            for (Ball ball : balls.values()) {
                if (ball.isActive()) {
                    if (triggerObject.trigger(ball, TIMER_INTERVAL)) {
                        
                        Gadget actionObject = interactions.get(triggerObject);
                        
                        actionObject.action();
                        System.out.println(triggerObject + " is triggered by " + actionObject);
                    }
                }
            }
        }
        checkRep();
    }
    
    /**
     * Calculates Ball positions and velocities at every timestep.
     */
    public void updateBalls() {

        for (Ball ball : this.balls.values()) {
            if (ball.isActive()) {
                Boolean skipGravity = false;
                // initialize values
                // find closest object
                for (Gadget gadget : this.gadgets.values()) {
                    if (gadget.trigger(ball, TIMER_INTERVAL)) {
                        skipGravity = true;
                    }
                }
                
                
                if (!skipGravity) {
                    // update position
                    ball.gravity(this.gravity, TIMER_INTERVAL);
                    ball.friction(this.friction1, this.friction2, TIMER_INTERVAL);
                    
                    // add a method for ball to update position given velocity and frame rate
                    Vect displacement = new Vect(ball.getVelocity().x()*TIMER_INTERVAL, ball.getVelocity().y()*TIMER_INTERVAL);
                    Vect newCenter = ball.getCenter().plus(displacement);
                    
                    ball.setCenter(newCenter.x(), newCenter.y());
                }
            }
        }
        checkTriggers();
        
    }
    
    @Override
    public String toString() {
        StringBuilder gameString = new StringBuilder("[Game: " + this.name 
                + "; Gravity: " + this.gravity 
                + "; Friction: " + this.friction1 + ", " + this.friction2 
                + "; Balls: ");
        
        // balls
        for (String ball : this.balls.keySet()) gameString.append(ball + ", "); 
        // remove the last comma and space once all balls have been added
        if (!this.balls.isEmpty()) gameString.delete(gameString.length()-2, gameString.length());
        gameString.append("; Gadgets: ");
        
        // gadgets
        for (String gadget : this.gadgets.keySet()) gameString.append(gadget + ", ");
        // remove the last comma and space once all gadgets have been added
        if (!this.gadgets.isEmpty()) gameString.delete(gameString.length()-2, gameString.length());
        gameString.append("; Events: ");
        
        // events
        for (Gadget interaction : this.interactions.keySet()) gameString.append("(Trigger: " + interaction + ", Action: " + this.interactions.get(interaction) + "), ");
        // remove the last comma and space once all gadgets have been added
        if (!this.interactions.isEmpty()) gameString.delete(gameString.length()-2, gameString.length());
        gameString.append("]");
        
        return gameString.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Game)) return false;
        Game gameThat = (Game)that;
        
        // names must be the same
        if (!this.name.equals(gameThat.name())) return false;
        
        // gravity and friction values must be the same
        if (this.gravity != gameThat.gravity() 
                || this.friction1 != gameThat.friction1() 
                || this.friction2 != gameThat.friction2()) return false;
        
        // balls must be the same
        if (!this.balls.equals(gameThat.balls())) return false;
        // gadgets must be the same
        if (!this.gadgets.equals(gameThat.gadgets())) return false;
        // interactions must be the same
        if (!this.interactions.equals(gameThat.interactions())) return false;
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() 
                + (int)this.gravity
                + (int)this.friction1
                + (int)this.friction2
                + this.balls.hashCode() 
                + this.gadgets.hashCode()
                + this.interactions.hashCode();
    }
    
    
}
