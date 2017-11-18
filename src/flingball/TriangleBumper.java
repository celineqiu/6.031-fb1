package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * Represents a Triangle Bumper Gadget in a Flingball game.
 */
class TriangleBumper implements Gadget {
    private final String name;
    private final int x, y, orientation;
    private final LineSegment legA, legB, hypotenuse;
    private final Circle cornerA, cornerB, rightAngleCorner;
    private final List<LineSegment> legs = new ArrayList<>();
    private final List<Circle> corners = new ArrayList<>();
    private final List<Gadget> actionObjects = new ArrayList<>();
    
    // Abstract Function:
    //   AF(name, x, y, orientation, legA, legB, hypotenuse, cornerA, cornerB, rightAngleCorner, legs, corners, actionObjects) 
    //     = Triangle bumper with named name with upper left corner of its bounding box at (x, y) 
    //       and with an orientation. The default orientation (0 degrees) places cornerA 
    //       in the northeast, rightAngleCorner in the northwest, and the cornerB in the southwest.
    //       legA is the side opposite of cornerA, legB is the side opposite of cornerB, 
    //         and hypotenuse, the longest side, is the side opposite of rightAngleCorner
    //       The hypotenuse goes from the southwest corner to the northeast corner,
    //      legs of the triangle, corners of the triangle,
    //      and actionObjects representing objects to be affected
    //      when the circle bumper is triggered
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   orientation must be 0, 90, 180, 270
    //   legA, legB and hypotenuse form a right triangle
    //   legA is the side opposite of cornerA, legB is the side opposite of cornerB, 
    //     and hypotenuse, the longest side, is the side opposite of rightAngleCorner
    //   actionObjects must be empty
    // Safety from rep exposure:
    //   all fields private and final
    
    public TriangleBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = 0;
        
        // triangles legs drawn counterclockwise
        Vect p1 = new Vect(x+1, y);
        Vect p2 = new Vect(x, y);
        Vect p3 = new Vect(x, y+1);
        
        rightAngleCorner = new Circle(p2, 0);
        cornerA = new Circle(p3, 0);
        cornerB = new Circle(p1, 0);
        corners.add(rightAngleCorner);
        corners.add(cornerA);
        corners.add(cornerB);
        
        legA = new LineSegment(p1, p2);
        legB = new LineSegment(p2, p3);
        hypotenuse = new LineSegment(p3, p1);
        legs.add(legA);
        legs.add(legB);
        legs.add(hypotenuse);
        
        checkRep();
    }
       
    public TriangleBumper(String name, int x, int y, int orientation) throws IllegalArgumentException {
        this.name = name;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        
        // triangles legs drawn counterclockwise
        Vect p1, p2, p3;
        if (orientation == 0) {
            p1 = new Vect(x+1, y);
            p2 = new Vect(x, y);
            p3 = new Vect(x, y+1);
        } else if (orientation == 90) {
            p1 = new Vect(x+1, y+1);
            p2 = new Vect(x+1, y);
            p3 = new Vect(x, y);
        } else if (orientation == 180) {
            p1 = new Vect(x, y+1);
            p2 = new Vect(x+1, y+1);
            p3 = new Vect(x+1, y);
        } else if (orientation == 270) {
            p1 = new Vect(x, y);
            p2 = new Vect(x, y+1);
            p3 = new Vect(x+1, y+1);
        } else {
            throw new IllegalArgumentException("invalid orientation");
        }
        
        rightAngleCorner = new Circle(p2, 0);
        cornerA = new Circle(p3, 0);
        cornerB = new Circle(p1, 0);
        corners.add(rightAngleCorner);
        corners.add(cornerA);
        corners.add(cornerB);
        
        legA = new LineSegment(p1, p2);
        legB = new LineSegment(p2, p3);
        hypotenuse = new LineSegment(p3, p1);
        legs.add(legA);
        legs.add(legB);
        legs.add(hypotenuse);
        
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        assert(x >= 0 && x <= 19);
        assert(y >= 0 && y <= 19);
        assert(orientation == 0 || orientation == 90 || orientation == 180 || orientation == 270);
        
        // form right triangle
        assert(hypotenuse.length()*hypotenuse.length() - legA.length()*legA.length() - legB.length()*legB.length()) <= 0.01 : "not a valid right triangle";
        assert(hypotenuse.p2().equals(legA.p1()));
        assert(legA.p2().equals(legB.p1()));
        assert(legB.p2().equals(hypotenuse.p1()));
        
        // opposite correct corners
        assert(!(cornerA.getCenter().equals(legA.p1()) || cornerA.getCenter().equals(legA.p2())));
        assert(!(cornerB.getCenter().equals(legB.p1()) || cornerB.getCenter().equals(legB.p2())));
        assert(!(rightAngleCorner.getCenter().equals(hypotenuse.p1()) || rightAngleCorner.getCenter().equals(hypotenuse.p2())));
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public void addActionObject(Gadget actionObject) {
        actionObjects.add(actionObject);
    }
    
    @Override
    public List<Gadget> getActionObjects() {
        List<Gadget> actionObjectsCopy = new ArrayList<>();
        for (Gadget gadget : this.actionObjects) actionObjectsCopy.add(gadget.copy());
        return actionObjectsCopy;
    }
    
    /**
     * Get the top left corner of the triangle bumper bounding box
     * @return origin of the triangle bumper bounding box
     */
    public Vect getOrigin() {
        return new Vect(this.x, this.y);
    }
    
    /**
     * Get the orientation of the triangle bumper
     * @return orientation of the triangle bumper. 0, 90, 180, or 270
     */
    public int getOrientation() {
        return this.orientation;
    }
    
    /**
     * Get the legs of the Triangle Bumper
     * @return list of the legs of the triangle bumper
     */
    public List<LineSegment> getLegs() {
        List<LineSegment> legsCopy = new ArrayList<>();
        legsCopy.add(new LineSegment(legA.p1(), legA.p2()));
        legsCopy.add(new LineSegment(legB.p1(), legB.p2()));
        legsCopy.add(new LineSegment(hypotenuse.p1(), hypotenuse.p2()));
        return legsCopy;
    }
    
    /**
     * Get the corners of the Triangle Bumper
     * @return list of the corners of the triangle bumper
     */
    public List<Circle> getCorners() {
        List<Circle> cornersCopy = new ArrayList<>();
        cornersCopy.add(new Circle(rightAngleCorner.getCenter(), rightAngleCorner.getRadius()));
        cornersCopy.add(new Circle(cornerA.getCenter(), cornerA.getRadius()));
        cornersCopy.add(new Circle(cornerB.getCenter(), cornerB.getRadius()));
        return cornersCopy;
    }
    
    @Override
    public Double timeUntilCollision(Ball ball) {
        // initialize values
        Double minLeg = Double.MAX_VALUE;
        Double minCorner = Double.MAX_VALUE;
        
        // find closest edge and corner
        for (LineSegment leg : legs) {
            Double time = Physics.timeUntilWallCollision(leg, ball.getCircle(), ball.getVelocity());
            if (time < minLeg) {
                minLeg = time;
            }
        }
        for (Circle corner : corners) {
            Double time = Physics.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if (time < minCorner) {
                minCorner = time;
            }
        }
        checkRep();
        return Math.min(minLeg, minCorner);    
    }
    
    @Override
    public Vect velocityAfterCollision(Ball ball) {
        // initialize values
        LineSegment closestLeg = new LineSegment(0, 0, 1, 1);
        Circle closestCorner = new Circle(0, 0, 0);
        Double minLeg = Double.MAX_VALUE;
        Double minCorner = Double.MAX_VALUE;
        
        // find closest edge and corner
        for (LineSegment leg : legs) {
            Double time = Physics.timeUntilWallCollision(leg, ball.getCircle(), ball.getVelocity());
            if (time < minLeg) {
                minLeg = time;
                closestLeg = leg;
            }
        }
        for (Circle corner : corners) {
            Double time = Physics.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if (time < minCorner) {
                minCorner = time;
                closestCorner = corner;
            }
        }
        
        // find closest object & post collision velocity
        Vect newVel;
        if (minCorner <= minLeg) {
            newVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            newVel = Physics.reflectWall(closestLeg, ball.getVelocity());
        }
        checkRep();
        return newVel;
    }
    
    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                       "origin: (" + this.x + "," + this.y + ")" + "\n" +
                       "orientation: " + this.orientation;
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof TriangleBumper)) {
            return false;
        }
        TriangleBumper thatBumper = (TriangleBumper) that;
        if (this.name.equals(thatBumper.name()) &&
            this.getOrigin().equals(thatBumper.getOrigin()) &&
            this.orientation == thatBumper.getOrientation() &&
            this.getLegs().equals(thatBumper.getLegs()) &&
            this.getCorners().equals(thatBumper.getCorners())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.x + this.y + this.orientation;
    }
    
    @Override
    public boolean trigger(Ball ball, double deltaT) {
        if (timeUntilCollision(ball) < deltaT) {
            Vect newVel = this.velocityAfterCollision(ball);
            ball.setVelocity(newVel.x(), newVel.y());
            Vect displacement = new Vect(ball.getVelocity().x()*deltaT, ball.getVelocity().y()*deltaT);
            Vect newCenter = ball.getCenter().plus(displacement);
            ball.setCenter(newCenter.x(), newCenter.y());
            
            for (Gadget actionObject: actionObjects) {
                actionObject.action();
            }
            
            return true;
        }
        return false;
    }
    
    @Override
    public void action() {
        // no action
    }
    
    @Override
    public TriangleBumper copy() {
        return new TriangleBumper(this.name, this.x, this.y, this.orientation);
    }

    @Override
    public void drawIcon(Graphics2D g, final int scaler, List<Ball> balls, double deltaT) {        
        
        g.setColor(Color.ORANGE); 
        for (Ball ball : balls) {
        if (trigger(ball, deltaT))  g.setColor(Color.YELLOW);
        }
        
        final int[] xValues = new int[] {
                (int) Math.round(cornerA.getCenter().x()*scaler),
                (int) Math.round(cornerB.getCenter().x()*scaler),
                (int) Math.round(rightAngleCorner.getCenter().x()*scaler)                   
        };
        
        final int[] yValues = new int[] {
                (int) Math.round(cornerA.getCenter().y()*scaler),
                (int) Math.round(cornerB.getCenter().y()*scaler),
                (int) Math.round(rightAngleCorner.getCenter().y()*scaler)                   
        };
        
        final int nPoints = 3;
        
        g.fillPolygon(xValues, yValues, nPoints);
    }   
}
