/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 27/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;


public class KdTree {
    private static class Node {
        Point2D key;
        Node left = null;
        Node right = null;
        RectHV rect;

        public Node(Point2D point) {
            this.key = point;
            this.rect = null;
        }
    }

    private static class Nearest {
        Point2D point;
        double distance;

        public Nearest (Point2D point, double distance) {
            this.point = point;
            this.distance = distance;
        }
    }

    private Node root;
    private int size;

    // Construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // Is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.contains(p)) {
            return;
        }

        this.root = this.insertRecursive(this.root, p, 0);

        // Size is still 0 if we inserted the first item.
        if (this.isEmpty()) {
            this.root.rect = new RectHV(0, 0, 1, 1);
        }

        this.size ++;
    }

    private Node insertRecursive(Node root, Point2D key, int level) {
        if (root == null) {
            return new Node(key);
        }

        if (level % 2 == 0) {
            // Vertical line.
            if (key.x() < root.key.x()) {
                root.left = this.insertRecursive(root.left, key, level + 1);
                if (root.left.rect == null) {
                    root.left.rect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.key.x(), root.rect.ymax());
                }
            } else {
                root.right = this.insertRecursive(root.right, key, level + 1);
                if (root.right.rect == null) {
                    root.right.rect = new RectHV(root.key.x(), root.rect.ymin(), root.rect.xmax(), root.rect.ymax());
                }
            }
        } else {
            // Horizontal line.
            if (key.y() < root.key.y()) {
                root.left = this.insertRecursive(root.left, key, level + 1);
                if (root.left.rect == null) {
                    root.left.rect = new RectHV(root.rect.xmin(), root.rect.ymin(), root.rect.xmax(), root.key.y());
                }
            } else {
                root.right = this.insertRecursive(root.right, key, level + 1);
                if (root.right.rect == null) {
                    root.right.rect = new RectHV(root.rect.xmin(), root.key.y(), root.rect.xmax(), root.rect.ymax());
                }
            }
        }

        return root;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.root == null) {
            return false;
        }

        return this.searchRecursive(this.root, p, 0) != null;
    }

    private Node searchRecursive(Node root, Point2D key, int level) {
        if (root == null) {
            return null;
        }

        if (root.key.equals(key)) {
            return root;
        }

        Comparator<Point2D> comparator = this.getComparator(level);

        if (comparator.compare(key, root.key) < 0) {
            return this.searchRecursive(root.left, key, level + 1);
        } else {
            return this.searchRecursive(root.right, key, level + 1);
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.clear();
        this.drawRecursive(this.root, 0);
    }

    private void drawRecursive(Node root, int level) {
        if (root != null) {
            this.drawPoint(root.key);

            double sx = root.rect.xmin();
            double ex = root.rect.xmax();
            double sy = root.rect.ymin();
            double ey = root.rect.ymax();

            if (level % 2 == 0) {
                // Vertical
                sx = ex = root.key.x();
            } else {
                // Horizontal
                sy = ey = root.key.y();
            }

            this.drawLine(sx, sy, ex, ey);

            this.drawRecursive(root.left, level + 1);
            this.drawRecursive(root.right, level + 1);
        }
    }

    private void drawPoint(Point2D point) {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);

        point.draw();
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        if (y1 == y2) {
            // Horizontal
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
        }

        if (x1 == x2) {
            // Vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
        }

        StdDraw.line(x1, y1, x2, y2);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        ArrayList<Point2D> list = new ArrayList<>();

        this.rangeRecursive(this.root, rect, list);

        return list;
    }

    private void rangeRecursive(Node root, RectHV rect, ArrayList<Point2D> list) {
        if (root == null) {
            return;
        }

        if (rect.intersects(root.rect)) {
            if (rect.contains(root.key)) {
                list.add(root.key);
            }

            this.rangeRecursive(root.left, rect, list);
            this.rangeRecursive(root.right, rect, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (this.isEmpty()) {
            return null;
        }

        Nearest n = new Nearest(null, Double.MAX_VALUE);

        this.nearestRecursive(this.root, n, p);

        return n.point;
    }

    private void nearestRecursive(Node root, Nearest n, Point2D p) {
        if (root == null) {
            return;
        }

        double distance = root.key.distanceSquaredTo(p);

        if (distance < n.distance) {
            n.distance = distance;
            n.point = root.key;
        }

        if (root.left != null && root.left.rect.distanceSquaredTo(p) < n.distance) {
            this.nearestRecursive(root.left, n, p);
        }

        if (root.right != null && root.right.rect.distanceSquaredTo(p) < n.distance) {
            this.nearestRecursive(root.right, n, p);
        }
    }

    private Comparator<Point2D> getComparator(int level) {
        if (level % 2 == 0) {
            return Point2D.X_ORDER;
        }

        return Point2D.Y_ORDER;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        /*while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            System.out.printf("Inserted point: %8.6f %8.6f\n", x, y);
        }*/

        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.9, 0.6));
        kdtree.insert(new Point2D(0.4, 0.7));

        //kdtree.draw();

        System.out.println("Nearest: " + kdtree.nearest(new Point2D(0.432, 0.675)));
    }
}
