package flingball;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    private float friction1;
    private float friction2;
    private String name;
    private float gravity = 25.0f;
    private Map<String, Ball> balls = new HashMap<>();
    private Map<String, Gadget> gadgets = new HashMap<>();
    private Map<Gadget,Gadget> interactions = new HashMap<>();
    
    
    
    public Game(String name, Float gravity, Float friction1, Float friction2, List<Ball> balls, List<Gadget> gadgets, Map<String,String> interactions) {
        this.name = name;
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

    
    // check every frame
    public void checkTriggers() {
        for (Gadget triggerObject: interactions.keySet()) {
            if (triggerObject.trigger()) {
                Gadget actionObject = interactions.get(triggerObject);
                actionObject.action();
            }
        }
    }
    
    
    
    
}
