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
    
    // Abstract Function:
    //   AF(name, x, y, bottom, top, left, right, bottomLeft, bottomRight, topLeft, topRight, edges, corner) 
    //     = Square bumper named name with upper left corner at (x, y),
    //       list of edges represented by bottom, top, left, right and 
    //       list of corners represented by bottomLeft, bottomRight, topLeft, topRight
    // Rep Invariant:
    //   name cannot be the name of other variables
    //   x and y must be between 0 and 19
    //   coordinates in bottom, top, left, right, bottomLeft, bottomRight, topLeft, and topRight 
    //     must be between 0 and 20
    //   bottomLeft and bottomRight must be endpoints of bottom
    //   bottomLeft and topLeft must be endpoints of left
    //   topLeft and topRight must be endpoints of top
    //   topRight and bottomRight must be endpoints of right
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
        assert(x >= 0 && x <= 19);
        assert(y >= 0 && y <= 19);
        
        for (LineSegment edge : edges) {
            Vect p1 = edge.p1();
            Vect p2 = edge.p2();
            assert(p1.x() >= 0 && p1.x() <= 20);
            assert(p1.y() >= 0 && p1.y() <= 20);
            assert(p2.x() >= 0 && p2.x() <= 20);
            assert(p2.y() >= 0 && p2.y() <= 20);
        }
        
        for (Circle corner : corners) {
            Vect center = corner.getCenter();
            assert(center.x() >= 0 && center.x() <= 20);
            assert(center.y() >= 0 && center.y() <= 20);
        }
        
        assert(bottomLeft.getCenter().equals(bottom.p1()) && bottomRight.getCenter().equals(bottom.p2()));
        assert(topLeft.getCenter().equals(top.p1()) && topRight.getCenter().equals(top.p2()));
        assert(topLeft.getCenter().equals(left.p1()) && bottomLeft.getCenter().equals(left.p2()));
        assert(topRight.getCenter().equals(right.p1()) && bottomRight.getCenter().equals(right.p2()));
        
    }
       
    @Override
    public String name() {
        return this.name;
    }
    
    /**
     * @return origin of square bumper bounding box
     */
    public Vect getOrigin() {
        return new Vect(this.x, this.y);
    }
    
    /**
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
                        "(" + this.topLeft.getCenter().x() + "," + this.topLeft.getCenter().y() + ")," + "\n" +
                        "(" + this.topRight.getCenter().x() + "," + this.topRight.getCenter().y() + ")," + "\n" +
                        "(" + this.bottomRight.getCenter().x() + "," + this.bottomRight.getCenter().y() + ")," + "\n" +
                        "(" + this.bottomLeft.getCenter().x() + "," + this.bottomLeft.getCenter().y() + ")";
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
    public boolean trigger(List<Ball> balls) {
        // TODO
    }
    
    @Override
    public void action() {
        // no action
    }
    
    @Override
    public SquareBumper copy() {
        return new SquareBumper(this.name, this.x, this.y);
    }
    
    @Override
    public void drawIcon(Graphics2D g, final int scaler, List<Ball> balls) {
        final int width = 1;
        final int height = 1;
        
        if (trigger(balls)) {
            g.setColor(Color.YELLOW);
        }else {
            g.setColor(Color.RED); 
        }
        
        int displayX = (int) Math.round(x*scaler);
        int displayY = (int) Math.round(y*scaler);
        int displayWidth = (int) Math.round(width*scaler);
        int displayHeight = (int) Math.round(height*scaler);
        
        g.fillRect(displayX, displayY, displayWidth, displayHeight);
    }
}
