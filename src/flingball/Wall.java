package flingball;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * Represents a Wall Gadget in a Flingball game.
 */
class Wall implements Gadget {
    private final String name;
    private final LineSegment line;
    private final Circle startCorner;
    
    // Abstract Function:
    //   AF(line, startCorner) = a wall represented by a line and 
    //     its starting point startCorner
    // Rep Invariant:
    //   the start point of line must correspond to startCorner
    //   startCorner coordinates must be between 0 or 20
    //   length of wall is 20
    //   wall name is top, bottom, left, or right
    // Safety from rep exposure:
    //   fields are private and final
    
    public Wall(String name, int startX, int startY, int endX, int endY) {
        this.name = name;
        this.line = new LineSegment(startX, startY, endX, endY);
        this.startCorner = new Circle(startX, startY, 0);
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        assert(line.p1().equals(startCorner.getCenter()));
        assert(startCorner.getCenter().x() == 0 || startCorner.getCenter().x() == 20);
        assert(startCorner.getCenter().y() == 0 || startCorner.getCenter().y() == 20);
        assert(line.length() == 20);
        assert(this.name.equals("top") || this.name.equals("bottom") || 
               this.name.equals("left") || this.name.equals("right"));
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    /**
     * @return line representing wall edge
     */
    public LineSegment getLine() {
        return new LineSegment(this.line.p1(), this.line.p2());
    }
    
    /**
     * @return start point of line (when moving in clockwise direction)
     */
    public Circle getCorner() {
        return new Circle(this.startCorner.getCenter(), this.startCorner.getRadius());
    }
    
    @Override
    public Double timeUntilCollision(Ball ball) {
        Double lineTime = Physics.timeUntilWallCollision(line, ball.getCircle(), ball.getVelocity());
        Double cornerTime = Physics.timeUntilCircleCollision(startCorner, ball.getCircle(), ball.getVelocity());
        return Math.min(lineTime, cornerTime);
    }
    
    @Override
    public Vect velocityAfterCollision(Ball ball) {
        Double lineTime = Physics.timeUntilWallCollision(line, ball.getCircle(), ball.getVelocity());
        Double cornerTime = Physics.timeUntilCircleCollision(startCorner, ball.getCircle(), ball.getVelocity());
        
        Vect newVel;
        if (cornerTime < lineTime) {
            newVel = Physics.reflectCircle(startCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            newVel = Physics.reflectWall(line, ball.getVelocity());
        }
        return newVel;
    }
    
    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                       "start: (" + (int) this.line.p1().x() + "," + (int) this.line.p1().y() + ")" + "\n" +
                       "end: (" + (int) this.line.p2().x() + "," + (int) this.line.p2().y() + ")";
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Wall)) {
            return false;
        }
        Wall thatWall = (Wall) that;
        if (this.name.equals(thatWall.name()) &&
            this.getLine().equals(thatWall.getLine()) &&
            this.getCorner().equals(thatWall.getCorner())) {
            return true;
        }
    return false;
    }
    
    @Override
    public void addActionObject(Gadget actionObject) {
        // never triggered
    }
    
    @Override
    public List<Gadget> getActionObjects() {
        return new ArrayList<>();
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.line.hashCode() + this.startCorner.hashCode();
    }
    
    @Override
    public boolean trigger(Ball ball, double deltaT) {
        if (timeUntilCollision(ball) < deltaT) {
            Vect newVel = this.velocityAfterCollision(ball);
            ball.setVelocity(newVel.x(), newVel.y());
            Vect displacement = new Vect(ball.getVelocity().x()*deltaT, ball.getVelocity().y()*deltaT);
            Vect newCenter = ball.getCenter().plus(displacement);
            ball.setCenter(newCenter.x(), newCenter.y());
            return true;
        }
        return false;
    }
    
    @Override
    public Wall copy() {
        Vect p1 = this.line.p1();
        Vect p2 = this.line.p2();
        return new Wall(this.name, (int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
    }
    
    @Override
    public void action() {
        // no action
    }
    
    @Override
    public void drawIcon(Graphics2D g, int scaler, List<Ball> balls, double deltaT) {
        // not drawn
    }

}
