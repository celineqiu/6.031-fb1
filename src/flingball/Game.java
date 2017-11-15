package flingball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
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
    
    // Abstract Function:
    //   AF(name, gravity, friction1, friction2, balls, gadgets, interactions)
    //     = a Flingball Game named name with gravity, friction1 and friction2 constants, 
    //       balls and gadgets in the playing area
    //       and trigger / action event interactions 
    // Rep Invariant:
    //   keys and values in interactions must be in gadgets
    // Safety from rep exposure:
    //   fields are private and final
    //   defensive copies of inputs are stored and returned
    
    /**
     * Check that the rep invariant is true.
     */
    private void checkRep() {
        // keys and values in interactions must be in gadgets
        for (Gadget trigger : interactions.keySet()) {
            assert gadgets.values().contains(trigger): "Trigger gadgets must be in gadgets map or be ";
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
            // TODO? make a defensive copy of the ball before storing it to prevent rep exposure
            this.balls.put(ball.name(), ball.copy());
        }
        
        // create walls
        Wall top = new Wall(0, 0, 20, 0);
        Wall left = new Wall(0, 20, 0, 0);
        Wall right = new Wall(20, 0, 20, 20);
        Wall bottom = new Wall(20, 20, 0, 20);
        gadgets.add(top);
        gadgets.add(left);
        gadgets.add(right);
        gadgets.add(bottom);
        
        for (Gadget gadget : gadgets) {
            // TODO? make a defensive copy of the gadget before storing it to prevent rep exposure
            this.gadgets.put(gadget.name(), gadget.copy());
        }
        
        for (String triggerName: interactions.keySet()) {
            // TODO? make a defensive copy of the interactions before storing it to prevent rep exposure
            Gadget triggerObject = this.gadgets.get(triggerName);
            Gadget actionObject = this.gadgets.get(interactions.get(triggerName));
            this.interactions.put(triggerObject.copy(), actionObject.copy());
        }
        
        checkRep();
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
        for (Gadget triggerObject: interactions.keySet()) {
            if (triggerObject.trigger()) {
                Gadget actionObject = interactions.get(triggerObject);
                actionObject.action();
            }
        }
        checkRep();
    }
    
    /**
     * Calculates Ball positions and velocities at every timestep.
     */
    public void updateBalls() {
        while (true) {  
            for (Ball ball : this.balls.values()) {
                // initialize values
                Double minTime = Double.MAX_VALUE;
                Vect newVel = ball.getVelocity();
                
                // find closest object
                for (Gadget gadget : this.gadgets.values()) {
                    Double time = gadget.timeUntilCollision(ball);
                    if (time < minTime) {
                        minTime = time;
                        newVel = gadget.velocityAfterCollision(ball);
                    }
                }
                
                for (int i = 0; i < minTime-1; i++) {
                    // update position
                    ball.gravity(this.gravity);
                    ball.friction(this.friction1, this.friction2);
                    // add a method for ball to update position given velocity?
                    Vect newCenter = ball.getCenter().plus(ball.getVelocity());
                    ball.setCenter(newCenter.x(), newCenter.y());
                    checkTriggers();
                }

                // update position so ball collides & set post collision velocity
                ball.gravity(this.gravity);
                ball.friction(this.friction1, this.friction2);
                // add a method for ball to update position given velocity?
                Vect newCenter = ball.getCenter().plus(ball.getVelocity().times(minTime % 1));
                ball.setCenter(newCenter.x(), newCenter.y());
                ball.setVelocity(newVel.x(), newVel.y());
                checkTriggers();
            }
        }
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
                || this.friction2 != gameThat.friction2());
        
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
