package warmup;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;
import physics.Physics;
import physics.Vect;

public class Main {

    public static void main(String[] args) {
        // create board
        
        // create walls
        LineSegment top = new LineSegment(0, 0, 20, 0);
        LineSegment left = new LineSegment(0, 0, 0, 20);
        LineSegment right = new LineSegment(20, 0, 20, 20);
        LineSegment bottom = new LineSegment(0, 20, 20, 20);
        
        List<LineSegment> edges = new ArrayList<>();
        edges.add(top);
        edges.add(left);
        edges.add(right);
        edges.add(bottom);
        
        // create corners
        Circle topLeft = new Circle(0, 0, 0);
        Circle topRight = new Circle(20, 0, 0);
        Circle bottomLeft = new Circle(0, 20, 0);
        Circle bottomRight = new Circle(20, 20, 0);
        
        List<Circle> corners = new ArrayList<>();
        corners.add(topLeft);
        corners.add(topRight);
        corners.add(bottomLeft);
        corners.add(bottomRight);
        
        // create ball & initialize velocity
        Circle ball = new Circle(10, 10, 1);
        Vect vel = new Vect(3, 4);
        
        try {
            while (true) {
                // initialize values
                LineSegment closestEdge = new LineSegment(0, 0, 1, 1);
                Circle closestCorner = new Circle(0, 0, 0);
                Double minEdge = Double.MAX_VALUE;
                Double minCorner = Double.MAX_VALUE;
                
                // find closest edge and corner
                for (LineSegment edge : edges) {
                    Double time = Physics.timeUntilWallCollision(edge, ball, vel);
                    if (time < minEdge) {
                        minEdge = time;
                        closestEdge = edge;
                    }
                }
                for (Circle corner : corners) {
                    Double time = Physics.timeUntilCircleCollision(corner, ball, vel);
                    if (time < minCorner) {
                        minCorner = time;
                        closestCorner = corner;
                    }
                }
                
                // find closest object & post collision velocity
                Vect newVel;
                Double minTime;
                if (minCorner <= minEdge) {
                    minTime = minCorner;
                    newVel = Physics.reflectCircle(closestCorner.getCenter(), ball.getCenter(), vel);
                } else {
                    minTime = minEdge;
                    newVel = Physics.reflectWall(closestEdge, vel);
                }
                
                for (int i = 0; i < minTime-1; i++) {
                    // update position
                    ball = new Circle(ball.getCenter().plus(vel), 1);
                    
                    // stop the thread for 50 milliseconds to yield ~20 frames per second
                    Thread.sleep(50);
                    System.out.println("Ball position: " + ball.getCenter());
                }
                // update position so ball collides & set post collision velocity
                ball = new Circle(ball.getCenter().plus(vel.times(minTime % 1)), 1);
                vel = newVel;
                
                // stop the thread for 50 milliseconds to yield ~20 frames per second
                Thread.sleep(50);
                System.out.println("Ball position: " + ball.getCenter());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
