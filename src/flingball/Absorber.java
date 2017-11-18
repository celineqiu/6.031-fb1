package flingball;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

/**
 * Represents an Absorber Gadget in a Flingball game.
 */
class Absorber implements Gadget {
    private final String name;
    private final int x, y, width, height;
    private final LineSegment bottom, top, left, right;
    private final Circle bottomLeft, bottomRight, topLeft, topRight;
    private final List<Ball> holdBalls = new LinkedList<>();
    private final List<LineSegment> edges = new ArrayList<>();
    private final List<Circle> corners = new ArrayList<>();
    private final List<Gadget> actionObjects = new ArrayList<>();
    private Ball ejected = new Ball("", 10, 10, 0, 0);
    private final double HELD_BALL_OFFSET = 0.5;
    
    // Abstract Function:
    //   AF(name, x, y, width, height, bottom, top, left, right, bottomLeft, bottomRight, topLeft, topRight,
    //      edges, corners, holdBalls, actionObjects, ejected, HELD_BALL_OFFSET) 
    //     = Absorber with a name with upper left corner at (x, y), a width and a height,
    //         edges represented by bottom, top, left, right and 
    //         corners represented by bottomLeft, bottomRight, topLeft, topRight,
    //         holdBalls list of balls being held in absorber,
    //         actionObjects representing objects to be affected when absorber is triggered
    //         ejected maps to the previously ejected ball, 
    //         HELD_BALL_OFFSET is the offset in the x and y positions from the bottom right of the absorber
    //           of balls held in the absorber
    // Rep Invariant:
    //   x and y must be between 0 and 19
    //   width and height must be positive integer <= 20
    //   coordinates in bottom, top, left, right, bottomLeft, bottomRight, topLeft, and topRight 
    //     must be between 0 and 20
    //   bottomLeft and bottomRight must be endpoints of bottom
    //   bottomLeft and topLeft must be endpoints of left
    //   topLeft and topRight must be endpoints of top
    //   topRight and bottomRight must be endpoints of right
    // Safety from rep exposure:
    //   all fields private and final
    
    /**
     * Create an Absorber.
     * @param name of the absorber
     * @param x coord of the top left corner
     * @param y coord of the top left corner
     * @param width of the absorber
     * @param height of the absorber
     */
    public Absorber(String name, int x, int y, int width, int height) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bottom = new LineSegment(x+width, y+height, x, y+height);
        this.top = new LineSegment(x, y, x+width, y);
        this.left = new LineSegment(x, y+height, x, y);
        this.right = new LineSegment(x+width, y, x+width, y+height);
        this.bottomLeft = new Circle(x, y+height, 0);
        this.bottomRight = new Circle(x+width, y+height, 0);
        this.topLeft = new Circle(x, y, 0);
        this.topRight = new Circle(x+width, y, 0);
        
        edges.add(bottom);
        edges.add(top);
        edges.add(left);
        edges.add(right);
        
        corners.add(bottomLeft);
        corners.add(bottomRight);
        corners.add(topLeft);
        corners.add(topRight);
        checkRep();
    }
    
    /**
     * Check that the rep invariant is satisfied.
     */
    private void checkRep() {
        final int WALL_LENGTH = 20;
        assert(x >= 0 && x <= WALL_LENGTH-1);
        assert(y >= 0 && y <= WALL_LENGTH-1);
        assert(width >= 1 && width <= WALL_LENGTH);
        assert(height >= 1 && height <= WALL_LENGTH);
        
        for (LineSegment edge : edges) {
            Vect p1 = edge.p1();
            Vect p2 = edge.p2();
            assert(p1.x() >= 0 && p1.x() <= WALL_LENGTH) : "edge center out of range, p1.x(), value is " + p1.x();
            assert(p1.y() >= 0 && p1.y() <= WALL_LENGTH) : "edge center out of range, p1.y(), value is " + p1.y();
            assert(p2.x() >= 0 && p2.x() <= WALL_LENGTH) : "edge center out of range, p2.x(), value is " + p2.x();
            assert(p2.y() >= 0 && p2.y() <= WALL_LENGTH) : "edge center out of range, p2.y(), value is " + p2.y();
        }

        for (Circle corner : corners) {
            Vect center = corner.getCenter();
            assert(center.x() >= 0 && center.x() <= WALL_LENGTH) : "corner center out of range, x";
            assert(center.y() >= 0 && center.y() <= WALL_LENGTH) : "corner center out of range, y";
        }
        
        assert(bottomRight.getCenter().equals(bottom.p1()) && bottomLeft.getCenter().equals(bottom.p2()));
        assert(topLeft.getCenter().equals(top.p1()) && topRight.getCenter().equals(top.p2()));
        assert(bottomLeft.getCenter().equals(left.p1()) && topLeft.getCenter().equals(left.p2()));
        assert(topRight.getCenter().equals(right.p1()) && bottomRight.getCenter().equals(right.p2()));
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public void addActionObject(Gadget actionObject) {
        actionObjects.add(actionObject);
        checkRep();
    }
    
    @Override
    public List<Gadget> getActionObjects() {
        List<Gadget> actionObjectsCopy = new ArrayList<>();
        for (Gadget gadget : this.actionObjects) actionObjectsCopy.add(gadget.copy());
        return actionObjectsCopy;
    }
    
    /**
     * @return origin of absorber bounding box
     */
    public Vect getOrigin() {
        return new Vect(this.x, this.y);
    }
    
    /**
     * @return width of absorber
     */
    public int getWidth() {
        return this.width;
    }
    
    /**
     * @return height of absorber
     */
    public int getHeight() {
        return this.height;
    }
    
    /**
     * @return list of edges of the absorber
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
     * @return list of corners of the absorber
     */
    public List<Circle> getCorners() {
        List<Circle> cornersCopy = new ArrayList<>();
        cornersCopy.add(new Circle(bottomLeft.getCenter(), bottomLeft.getRadius()));
        cornersCopy.add(new Circle(bottomRight.getCenter(), bottomRight.getRadius()));
        cornersCopy.add(new Circle(topLeft.getCenter(), topLeft.getRadius()));
        cornersCopy.add(new Circle(topRight.getCenter(), topRight.getRadius()));
        return cornersCopy;
    }
    
    /**
     * @return list of balls being held by the absorber
     */
    public List<Ball> getBalls() {
        List<Ball> ballsCopy = new ArrayList<>();
        for (Ball b : holdBalls) {
            ballsCopy.add(b.copy());
        }
        return ballsCopy;
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
        checkRep();
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
        checkRep();
        return newVel;
    }

    @Override 
    public String toString() {
        String toStr = "name: " + this.name + "\n" +
                       "origin: (" + this.x + "," + this.y + ")" + "\n" +
                       "width: " + this.width + "\n" +
                       "height: " + this.height;
        return toStr;
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Absorber)) {
            return false;
        }
        Absorber thatAbsorber = (Absorber) that;
        if (this.name.equals(thatAbsorber.name()) &&
            this.getOrigin().equals(thatAbsorber.getOrigin()) &&
            this.getEdges().equals(thatAbsorber.getEdges()) &&
            this.getCorners().equals(thatAbsorber.getCorners()) &&
            this.holdBalls.equals(thatAbsorber.getBalls())) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.x + this.y + this.width + this.height;
    }
    
    @Override
    public boolean trigger(Ball ball, double deltaT) {
        if ((ball.equals(ejected) && !checkInside(ball)) || !ball.equals(ejected)) {
            if (timeUntilCollision(ball) < deltaT) {
                ball.setCenter(this.x + this.width - HELD_BALL_OFFSET, this.y + this.height - HELD_BALL_OFFSET);
                ball.setVelocity(0, 0);
                ball.setActive(false);
                this.holdBalls.add(ball);
                
                for (Gadget actionObject: actionObjects) {
                    actionObject.action();
                }
                
                checkRep();
                return true;
            }
        }
        checkRep();
        return false;   
        }
    
    @Override
    public void action() {
        final int SHOOT_VELOCITY = 50;
        // if there are balls to eject
        if (holdBalls.size() > 0) {
            // if the ejected ball isn't the initialized ball OR
            // the ejected ball has left the absorber OR
            // the ejected ball is being held by the absorber again
            if (ejected.name().equals("") || !(checkInside(ejected)) || holdBalls.contains(ejected)) {
                Ball shoot = holdBalls.remove(0);
                ejected = shoot;
                shoot.setVelocity(0, -SHOOT_VELOCITY);
                shoot.setActive(true);
                checkRep();
                return;
            }
        }
        checkRep();
    }
    
    /**
     * Check if a ball is inside the absorber.
     * @param ball to check location of
     * @return true if ball is inside absorber and false otherwise
     */
    private boolean checkInside(Ball ball) {
        final double BALL_RADIUS = 0.25;
        // return true if ball is inside absorber
        // return false otherwise
        Vect center = ball.getCenter();
        if (center.x() >= x-BALL_RADIUS && center.x() <= x+width+BALL_RADIUS &&
            center.y() >= y-BALL_RADIUS && center.y() <= y + height+BALL_RADIUS) {
            return true;
        }
        return false;
    }
    
    @Override
    public Absorber copy() {
        return new Absorber(this.name, this.x, this.y, this.width, this.height);
    }
    
    @Override
    public void drawIcon(Graphics2D g, final int scaler, List<Ball> balls, double deltaTls) {
        g.setColor(Color.GREEN);
        int displayX = (int) Math.round(x*scaler);
        int displayY = (int) Math.round(y*scaler);
        int displayWidth = (int) Math.round(width*scaler);
        int displayHeight = (int) Math.round(height*scaler);
        
        g.fillRect(displayX, displayY, displayWidth, displayHeight);
        checkRep();
    }
}
