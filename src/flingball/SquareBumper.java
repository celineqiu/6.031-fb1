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
 * Represents a Square Bumper Gadget in a Flingball game.
 */
class SquareBumper implements Gadget {
    private final String name;
    private final int x, y;
    private final LineSegment bottom, top, left, right;
    private final Circle bottomLeft, bottomRight, topLeft, topRight;
    private final List<LineSegment> edges = new ArrayList<>();
    private final List<Circle> corners = new ArrayList<>();
    private final List<Gadget> actionObjects = new ArrayList<>();
    private static final int MAX_COORD = 19;
    private static final int LENGTH = 20;
    
    // Abstract Function:
    //   AF(name, x, y, bottom, top, left, right, bottomLeft, bottomRight, topLeft, topRight, edges, corners, actionObjects) 
    //     = Square bumper named name with upper left corner at (x, y),
    //       list of edges represented by bottom, top, left, right and 
    //       list of corners represented by bottomLeft, bottomRight, topLeft, topRight,
    //       and actionObjects representing objects to be affected
    //       when the circle bumper is triggered
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   coordinates in bottom, top, left, right, bottomLeft, bottomRight, topLeft, and topRight 
    //     must be between 0 and 20
    //   bottomLeft and bottomRight must be endpoints of bottom
    //   bottomLeft and topLeft must be endpoints of left
    //   topLeft and topRight must be endpoints of top
    //   topRight and bottomRight must be endpoints of right
    //   actionObjects must be empty
    // Safety from rep exposure:
    //   all fields private and final
    
    public SquareBumper(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.bottom = new LineSegment(x+1, y+1, x, y+1);
        this.top = new LineSegment(x, y, x+1, y);
        this.left = new LineSegment(x, y+1, x, y);
        this.right = new LineSegment(x+1, y, x+1, y+1);
        this.bottomLeft = new Circle(x, y+1, 0);
        this.bottomRight = new Circle(x+1, y+1, 0);
        this.topLeft = new Circle(x, y, 0);
        this.topRight = new Circle(x+1, y, 0);
        
        edges.add(this.bottom);
        edges.add(this.top);
        edges.add(this.left);
        edges.add(this.right);
        
        corners.add(this.bottomLeft);
        corners.add(this.bottomRight);
        corners.add(this.topLeft);
        corners.add(this.topRight);
        
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        assert(x >= 0 && x <= MAX_COORD);
        assert(y >= 0 && y <= MAX_COORD);
        
        for (LineSegment edge : edges) {
            Vect p1 = edge.p1();
            Vect p2 = edge.p2();
            assert(p1.x() >= 0 && p1.x() <= LENGTH) : "edge point p1.x out of range, value is "+ p1.x();
            assert(p1.y() >= 0 && p1.y() <= LENGTH) : "edge point p1.y out of range, value is "+ p1.y();
            assert(p2.x() >= 0 && p2.x() <= LENGTH) : "edge point p2.x out of range, value is "+ p2.x();
            assert(p2.y() >= 0 && p2.y() <= LENGTH) : "edge point p2.y out of range, value is "+ p2.y();
        }
        
        for (Circle corner : corners) {
            Vect center = corner.getCenter();
            assert(center.x() >= 0 && center.x() <= LENGTH) : "center of corner out of bound (x), value is " + center.x();
            assert(center.y() >= 0 && center.y() <= LENGTH) : "center of corner out of bound (y), value is " + center.y();
        }
        
        assert(bottomRight.getCenter().equals(bottom.p1()) && bottomLeft.getCenter().equals(bottom.p2())) : "edge point and corner not consistent, bottom";
        assert(topLeft.getCenter().equals(top.p1()) && topRight.getCenter().equals(top.p2())) : "edge point and corner not consistent, top";
        assert(bottomLeft.getCenter().equals(left.p1()) && topLeft.getCenter().equals(left.p2())) : "edge point and corner not consistent, left";
        assert(topRight.getCenter().equals(right.p1()) && bottomRight.getCenter().equals(right.p2())) : "edge point and corner not consistent, right";
        // actionObjects must be empty
        assert actionObjects.isEmpty();
    }
       
    @Override
    public String name() {
        return this.name;
    }
    
    /**
     * Get the origin of the top left corner of the bounding box
     * @return origin of square bumper bounding box
     */
    public Vect getOrigin() {
        return new Vect(this.x, this.y);
    }
    
    /**
     * Get the edges of the Square Bumper
     * @return list of edges of the square bumper
     */
    public List<LineSegment> getEdges() {
        List<LineSegment> edgesCopy = new ArrayList<>();
        edgesCopy.add(new LineSegment(bottom.p1(), bottom.p2()));
        edgesCopy.add(new LineSegment(top.p1(), top.p2()));
        edgesCopy.add(new LineSegment(left.p1(), left.p2()));
        edgesCopy.add(new LineSegment(right.p1(), right.p2()));
        return edgesCopy;
    }
    
    /**
     * Get the corners of the Square Bumper
     * @return list of corners of the square bumper
     */
    public List<Circle> getCorners() {
        List<Circle> cornersCopy = new ArrayList<>();
        cornersCopy.add(new Circle(bottomLeft.getCenter(), bottomLeft.getRadius()));
        cornersCopy.add(new Circle(bottomRight.getCenter(), bottomRight.getRadius()));
        cornersCopy.add(new Circle(topLeft.getCenter(), topLeft.getRadius()));
        cornersCopy.add(new Circle(topRight.getCenter(), topRight.getRadius()));
        return cornersCopy;
    }
    
    @Override
    public Double timeUntilCollision(Ball ball) {
        // initialize values
        Double minEdge = Double.MAX_VALUE;
        Double minCorner = Double.MAX_VALUE;
        
        // find closest edge and corner
        for (LineSegment edge : edges) {
            Double time = Physics.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            if (time < minEdge) {
                minEdge = time;
            }
        }
        for (Circle corner : corners) {
            Double time = Physics.timeUntilCircleCollision(corner, ball.getCircle(), ball.getVelocity());
            if (time < minCorner) {
                minCorner = time;
            }
        }
        
        return Math.min(minEdge, minCorner);        
    }
    
    @Override
    public Vect velocityAfterCollision(Ball ball) {
        // initialize values
        LineSegment closestEdge = new LineSegment(0, 0, 1, 1);
        Circle closestCorner = new Circle(0, 0, 0);
        Double minEdge = Double.MAX_VALUE;
        Double minCorner = Double.MAX_VALUE;
        
        // find closest edge and corner
        for (LineSegment edge : edges) {
            Double time = Physics.timeUntilWallCollision(edge, ball.getCircle(), ball.getVelocity());
            if (time < minEdge) {
                minEdge = time;
                closestEdge = edge;
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
        if (minCorner <= minEdge) {
            newVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), ball.getVelocity());
        } else {
            newVel = Physics.reflectWall(closestEdge, ball.getVelocity());
        }
        
        return newVel;
    }
    
    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                        "top left: (" + (int) this.topLeft.getCenter().x() + "," + (int) this.topLeft.getCenter().y() + ")," + "\n" +
                        "top right: (" + (int) this.topRight.getCenter().x() + "," + (int) this.topRight.getCenter().y() + ")," + "\n" +
                        "bottom right: (" + (int) this.bottomRight.getCenter().x() + "," + (int) this.bottomRight.getCenter().y() + ")," + "\n" +
                        "bottom left: (" + (int) this.bottomLeft.getCenter().x() + "," + (int) this.bottomLeft.getCenter().y() + ")";
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof SquareBumper)) {
            return false;
        }
        SquareBumper thatBumper = (SquareBumper) that;
        if (this.name.equals(thatBumper.name()) &&
            this.getOrigin().equals(thatBumper.getOrigin()) &&
            this.getEdges().equals(thatBumper.getEdges()) &&
            this.getCorners().equals(thatBumper.getCorners())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.x + this.y +
               this.edges.hashCode() + this.corners.hashCode();
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
    public void addActionObject(Gadget actionObject) {
        actionObjects.add(actionObject);
    }
    
    @Override
    public List<Gadget> getActionObjects() {
        List<Gadget> actionObjectsCopy = new ArrayList<>();
        for (Gadget gadget : this.actionObjects) actionObjectsCopy.add(gadget.copy());
        return actionObjectsCopy;
    }
    
    @Override
    public SquareBumper copy() {
        return new SquareBumper(this.name, this.x, this.y);
    }
    
    @Override
    public void drawIcon(Graphics2D g, final int scaler, List<Ball> balls, double deltaT) {
        final int width = 1;
        final int height = 1;
        
        g.setColor(Color.RED); 
        for (Ball ball : balls) {
        if (trigger(ball, deltaT))  g.setColor(Color.YELLOW);
        }
        
        int displayX = (int) Math.round(x*scaler);
        int displayY = (int) Math.round(y*scaler);
        int displayWidth = (int) Math.round(width*scaler);
        int displayHeight = (int) Math.round(height*scaler);
        
        g.fillRect(displayX, displayY, displayWidth, displayHeight);
    }
}
