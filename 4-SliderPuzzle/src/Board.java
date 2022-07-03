/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 19/02/22
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] board;
    private Board twin = null;
    private int manhattan = -1;
    private int hamming = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.board = new int[tiles.length][tiles.length];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }

        this.manhattan = this.manhattan();
        this.hamming = this.hamming();
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.board.length);

        for (int[] ints : this.board) {
            sb.append("\n");
            for (int j = 0; j < this.board.length; j++) {
                sb.append(" ").append(ints[j]);
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.board.length;
    }

    // number of tiles out of place
    public int hamming() {
        if (this.hamming != -1) {
            return this.hamming;
        }

        int outOfPlace = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] != 0 && this.board[i][j] != this.linearize(i, j)) {
                    outOfPlace ++;
                }
            }
        }

        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (this.manhattan != -1) {
            return this.manhattan;
        }

        int distance = 0;

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == 0) {
                    // Do not count the distance if it's the empty tile!
                    continue;
                }

                int[] goal = this.delinearize(this.board[i][j]);
                int goalX = goal[0];
                int goalY = goal[1];

                distance += Math.abs(goalX - i);
                distance += Math.abs(goalY - j);
            }
        }

        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (!(y instanceof Board)) {
            return false;
        }

        Board other = (Board) y;
        if (this.board.length != other.board.length) {
            return false;
        }

        return Arrays.deepEquals(this.board, other.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int[] coordinates = this.getZero();
        int x = coordinates[0];
        int y = coordinates[1];

        ArrayList<Board> list = new ArrayList<>();

        if (x > 0) {
            // Left
            this.swap(x, y, x-1, y);
            list.add(new Board(this.board));
            // UnSwap
            this.swap(x-1, y, x, y);
        }

        if (x < this.board.length - 1) {
            // Right
            this.swap(x, y, x+1, y);
            list.add(new Board(this.board));
            // UnSwap
            this.swap(x+1, y, x, y);
        }

        if (y > 0) {
            // Up
            this.swap(x, y, x, y-1);
            list.add(new Board(this.board));
            // UnSwap
            this.swap(x, y-1, x, y);
        }

        if (y < this.board.length - 1) {
            // Down
            this.swap(x, y, x, y+1);
            list.add(new Board(this.board));
            // UnSwap
            this.swap(x, y+1, x, y);
        }

        return list;
    }

    // a board that is obtained by exchanging any pair of tiles
    // randomly choose a tile and swap it horizontally
    public Board twin() {
        if (this.twin != null) {
            return new Board(this.twin.board);
        }

        this.twin = new Board(this.board);

        boolean ok = false;
        int x1 = 0, y1 = 0, x2, y2;

        while (!ok) {
            x1 = StdRandom.uniform(this.board.length);
            y1 = StdRandom.uniform(this.board.length);

            // The 0 tile is not a valid one
            if (this.twin.board[x1][y1] != 0) {
                ok = true;
            }
        }

        x2 = x1;
        y2 = y1;

        if (x1 < this.board.length - 1) {
            // I can swap with the right tile
            if (this.board[x1 + 1][y1] != 0) {
                x2 = x1 + 1;
            }
        } else {
            // I can swap with the left tile
            if (this.board[x1 - 1][y1] != 0) {
                x2 = x1 - 1;
            }
        }

        // I can't swap horizontally, let's try vertically
        if (x1 == x2) {
            if (y1 < this.board.length - 1) {
                y2 = y1 + 1;
            } else {
                y2 = y1 - 1;
            }
        }

        this.twin.swap(x1, y1, x2, y2);

        return new Board(this.twin.board);
    }

    private int linearize(int row, int col) {
        return row * this.board.length + col + 1;
    }

    private int[] delinearize(int n) {
        int[] res = {
                n / this.board.length,
                n % this.board.length - 1
        };

        if (n == 0) {
            res[0] = res[1] = this.board.length - 1;
        }

        if (res[1] < 0) {
            res[0]--;
            res[1] = this.board.length - 1;
        }

        return res;
    }

    private int[] getZero() {
        int[] coordinates = new int[2];

        int x = 0;
        int y = 0;
        boolean cont = true;

        for (int i = 0; i < this.board.length && cont; i++) {
            for (int j = 0; j < this.board.length && cont; j++) {
                if (this.board[i][j] == 0) {
                    x = i;
                    y = j;
                    cont = false;
                }
            }
        }

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }

    private void swap(int x1, int y1, int x2, int y2) {
        int support = this.board[x1][y1];
        this.board[x1][y1] = this.board[x2][y2];
        this.board[x2][y2] = support;
    }

    // unit testing (not graded)
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

        System.out.println("Initial board:");
        System.out.println(initial);

        // Checking twin()
        System.out.println("Twin:");
        System.out.println(initial.twin());
        System.out.println(initial.twin());
    }
}
