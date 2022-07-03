/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 27/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final Set<Point2D> pointSet;

    // Construct an empty set of points
    public PointSET() {
        this.pointSet = new TreeSet<>();
    }

    // Is the set empty?
    public boolean isEmpty() {
        return this.pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        this.pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return this.pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        this.pointSet.forEach(Point2D::draw);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> list = new ArrayList<>();

        for (Point2D p : this.pointSet) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }

        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        Point2D nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2D point : this.pointSet) {
            if (point.distanceSquaredTo(p) < minDistance) {
                nearest = point;
                minDistance = point.distanceSquaredTo(p);
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
