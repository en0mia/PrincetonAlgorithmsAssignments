/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 21/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private static class TreeNode implements Comparable<TreeNode> {
        final Board b;
        final int moves;
        final int weight;
        final TreeNode parent;

        public TreeNode(Board b, int previousMoves, TreeNode previous) {
            this.b = b;
            this.moves = previousMoves;
            this.weight = (this.moves + this.b.manhattan());
            this.parent = previous;
        }

        @Override
        public int compareTo(TreeNode o) {
            return this.weight - o.weight;
        }
    }

    // Priority queue sorted by manhattan distance.
    private int minMoves;
    private ArrayList<TreeNode> solution;
    private TreeNode minSolution;
    private final Board initial;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.initial = initial;

        Board twin = this.initial.twin();
        MinPQ<TreeNode> queue = new MinPQ<>();
        MinPQ<TreeNode> twinQueue = new MinPQ<>();

        this.solution = new ArrayList<>();

        int moves = 0;
        this.solvable = false;
        TreeNode node = new TreeNode(initial, moves, null);
        TreeNode twinNode = new TreeNode(twin, 0, null);
        queue.insert(node);
        twinQueue.insert(twinNode);

        while (!queue.isEmpty() && !twinQueue.isEmpty()) {
            node = queue.delMin();
            twinNode = twinQueue.delMin();

            if (node.b.isGoal()) {
                this.solvable = true;
                break;
            }

            if (twinNode.b.isGoal()) {
                this.solvable = false;
                break;
            }

            for (Board b : node.b.neighbors()) {
                if (node.parent == null || !b.equals(node.parent.b)) {
                    queue.insert(new TreeNode(b, node.moves + 1, node));
                }
            }

            for (Board b : twinNode.b.neighbors()) {
                if (twinNode.parent == null || !b.equals(twinNode.parent.b)) {
                    twinQueue.insert(new TreeNode(b, twinNode.moves + 1, twinNode));
                }
            }
        }

        if (this.solvable) {
            TreeNode last = node;

            this.minMoves = last.moves;
            this.minSolution = last;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.isSolvable()) {
            return -1;
        }

        return this.minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.isSolvable()) {
            return null;
        }

        if (this.minSolution == null) {
            return null;
        }

        ArrayList<Board> sol = new ArrayList<>();
        TreeNode p = this.minSolution;

        while (p.parent != null) {
            sol.add(p.b);
            p = p.parent;
        }

        sol.add(this.initial);

        Collections.reverse(sol);

        return sol;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
