/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 14/02/22
    @copyright: Check the repository license.
*/

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment []segments;

    // Finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }

            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    if (points[i].equals(points[j])) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        if (points.length < 4) {
            this.segments = new LineSegment[0];
            return;
        }

        this.segments = new LineSegment[points.length];
        LinkedList toSegments = new LinkedList();

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                for (int k = 0; k < points.length; k++) {
                    for (int w = 0; w < points.length; w++) {
                        if (allDistinct(i, j, k, w)) {
                            // Working on different points, draw the segment.
                            Point []workingOn = {
                                    points[i],
                                    points[j],
                                    points[k],
                                    points[w],
                            };
                            if (
                                    this.collinear(workingOn)
                                    && this.getMin(workingOn).equals(points[i])
                                    && this.getMax(workingOn).equals(points[w])
                            ) {
                                toSegments.insert(new PointToPoint(points[i], points[w]));
                            }
                        }
                    }
                }
            }
        }

        PointToPoint []tmp = toSegments.toArray();

        if (tmp.length == 0) {
            this.segments = new LineSegment[0];
            return;
        }

        Arrays.sort(tmp);
        int distinct = 0;

        for (int i = 1; i < tmp.length; i++) {
            if (!tmp[i].equals(tmp[i-1])) {
                distinct++;
            }
        }

        this.segments = new LineSegment[distinct + 1];

        int j = 1;
        for (int i = 1; i < tmp.length; i++) {
            if (!tmp[i].equals(tmp[i-1])) {
                this.segments[j++] = new LineSegment(tmp[i].p1, tmp[i].p2);
            }
        }

        this.segments[0] = new LineSegment(tmp[0].p1, tmp[0].p2);
    }

    // The number of line segments
    public int numberOfSegments() {
        if (this.segments == null) {
            return 0;
        }

        return this.segments.length;
    }

    // The line segments
    public LineSegment[] segments() {
        if (this.segments == null) {
            this.segments = new LineSegment[0];
        }

        return Arrays.copyOf(this.segments, this.segments.length);
    }

    private Point getMin(Point[] points) {
        Point min = points[0];

        for (Point p : points) {
            if (p.compareTo(min) < 0) {
                min = p;
            }
        }

        return min;
    }

    private Point getMax(Point[] points) {
        Point max = points[0];

        for (Point p : points) {
            if (p.compareTo(max) > 0) {
                max = p;
            }
        }

        return max;
    }

    private boolean allDistinct(int o1, int o2, int o3, int o4) {
        int [] points = {
                o1,
                o2,
                o3,
                o4
        };

        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j && points[i] == points[j]) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean collinear(Point []points) {
        Point reference = points[0];
        double line = reference.slopeTo(points[1]);

        for (int i = 1; i < points.length; i++) {
            if (reference.slopeTo(points[i]) != line) {
                return false;
            }
        }

        return true;
    }

    private class PointToPoint implements Comparable<PointToPoint> {
        private final Point p1;
        private final Point p2;

        public PointToPoint(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PointToPoint)) {
                return false;
            }

            PointToPoint other = (PointToPoint) o;

            if (this.p1.compareTo(other.p1) == 0) {
                return this.p2.compareTo(other.p2) == 0;
            }

            if (this.p2.compareTo(other.p1) == 0) {
                return this.p1.compareTo(other.p2) == 0;
            }

            return false;
        }

        @Override
        public String toString() {
            if (this.p1.compareTo(this.p2) < 0) {
                return String.format("%s,%s", this.p1, this.p2);
            }

            return String.format("%s,%s", this.p2, this.p1);
        }

        @Override
        public int compareTo(PointToPoint o) {
            Point min1, min2, max1, max2;
            min1 = getMin(new Point[]{this.p1, this.p2});
            min2 = getMin(new Point[]{o.p1, o.p2});

            max1 = getMax(new Point[]{this.p1, this.p2});
            max2 = getMax(new Point[]{o.p1, o.p2});

            if (min1.compareTo(min2) == 0) {
                if (max1.compareTo(max2) == 0) {
                    return 0;
                } else {
                    return max1.compareTo(max2);
                }
            }

            return min1.compareTo(min2);
        }

        @Override
        public int hashCode() {
            int result = p1 != null ? p1.hashCode() : 0;
            result = 31 * result + (p2 != null ? p2.hashCode() : 0);
            return result;
        }
    }

    private class LinkedList {
        Node head;
        int elements = 0;

        class Node {
            private final PointToPoint data;
            private Node next;

            public Node(PointToPoint data) {
                this.data = data;
            }
        }

        public void insert(PointToPoint data) {
            // Create a new node with given data
            Node new_node = new Node(data);
            new_node.next = null;

            // If the Linked List is empty,
            // then make the new node as head
            if (this.head != null) {
                // Else insert at the head
                new_node.next = this.head;
            }
            this.head = new_node;
            this.elements ++;
        }

        public PointToPoint[] toArray()
        {
            Node currNode = this.head;
            PointToPoint []tmp = new PointToPoint[this.elements];

            int i = 0;

            while (currNode != null) {
                tmp[i] = currNode.data;
                currNode = currNode.next;
                i++;
            }

            return tmp;
        }
    }
}
