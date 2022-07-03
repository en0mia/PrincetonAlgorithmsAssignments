/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 15/02/22
    @copyright: Check the repository license.
*/

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment []segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        LinkedList tmpSegments = new LinkedList();
        Point[] sorted = new Point[points.length];

        System.arraycopy(points, 0, sorted, 0, points.length);

        for (Point reference : points) {
            int i = 0;
            Arrays.sort(sorted, reference.slopeOrder());

            while (i < sorted.length) {
                if (sorted[i].compareTo(reference) == 0) {
                    i++;
                    continue;
                }

                int j = 0;
                double slope = reference.slopeTo(sorted[i]);

                while ((i + j < sorted.length) && slope == reference.slopeTo(sorted[i + j])) {
                    j ++;
                }

                Point[] tmp = new Point[j+1];

                System.arraycopy(sorted, i, tmp, 0, j);

                if (j >= 3) {
                    tmp[tmp.length - 1] = reference;
                    Arrays.sort(tmp);
                    if (reference.equals(tmp[0])) {
                        tmpSegments.insert(new LineSegment(tmp[0], tmp[tmp.length - 1]));
                    }
                }

                i += j;
            }
        }

        this.segments = tmpSegments.toArray();
    }

    // the number of line segments
    public int numberOfSegments() {
        if (this.segments == null) {
            return 0;
        }

        return this.segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        if (this.segments == null) {
            this.segments = new LineSegment[0];
        }

        return Arrays.copyOf(this.segments, this.segments.length);
    }

    private static class LinkedList {
        Node head;
        int elements = 0;

        static class Node {
            private final LineSegment data;
            private Node next;

            public Node(LineSegment data) {
                this.data = data;
            }
        }

        public void insert(LineSegment data) {
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

        public LineSegment[] toArray()
        {
            Node currNode = this.head;
            LineSegment []tmp = new LineSegment[this.elements];

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
