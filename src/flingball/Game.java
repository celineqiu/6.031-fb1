package flingball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //   defensive copies of inputs are stored
    
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
            this.balls.put(ball.name(), ball);
        }
        
        for (Gadget gadget : gadgets) {
            this.gadgets.put(gadget.name(), gadget);
        }
        
        for (String triggerName: interactions.keySet()) {
            Gadget triggerObject = this.gadgets.get(triggerName);
            Gadget actionObject = this.gadgets.get(interactions.get(triggerName));
            this.interactions.put(triggerObject, actionObject);
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
     * Check for triggers at every frame of the Flingball Game.
     */
    public void checkTriggers() {
        for (Gadget triggerObject: interactions.keySet()) {
            if (triggerObject.trigger()) {
                Gadget actionObject = interactions.get(triggerObject);
                actionObject.action();
            }
        }
    }
    
    @Override
    public void toString() {
        // TODO
    }
    
    @Override
    public void equals() {
        // TODO
    }
    
    @Override
    public void hashCode() {
        // TODO
    }
    
    
}
