/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 14/09/21
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int openSites;
    private final int n;
    private final boolean[] open;
    private final WeightedQuickUnionUF quickUnion;
    private final WeightedQuickUnionUF quickUnionBackup;

    private final int upVirtualNodeIndex;
    private final int bottomVirtualNodeIndex;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        this.openSites = 0;
        this.open = new boolean[n*n];

        this.quickUnion = new WeightedQuickUnionUF(n * n + 2);
        this.quickUnionBackup = new WeightedQuickUnionUF(n * n + 1);

        this.upVirtualNodeIndex = n*n;
        this.bottomVirtualNodeIndex = n*n+1;

        // Setting up virtual nodes.
        for (int i = 0; i < n; i++) {
            this.quickUnion.union(i, this.upVirtualNodeIndex);
            this.quickUnionBackup.union(i, this.upVirtualNodeIndex);
        }

        for (int i = n*n - 1; i > n*n - 1 - n; i--) {
            this.quickUnion.union(i, this.bottomVirtualNodeIndex);
        }
    }

    public void open(int row, int col) {
        row -= 1;
        col -= 1;

        if (!this.checkParameters(row) || !this.checkParameters(col)) {
            throw new IllegalArgumentException(String.format("Expected <= %d, but was [%d,%d]", this.n, row, col));
        }

        int currentIndex = this.matrixToArrayIndex(row, col);
        int currentAdjacentIndex;

        if (!this.open[currentIndex]) {
            this.open[currentIndex] = true;
            this.openSites++;
        }

        if (row > 0) {
            currentAdjacentIndex = this.matrixToArrayIndex(row-1, col);
            if (this.open[currentAdjacentIndex]) {
                this.quickUnion.union(currentIndex, currentAdjacentIndex);
                this.quickUnionBackup.union(currentIndex, currentAdjacentIndex);
            }
        }

        if (row < this.n - 1) {
            currentAdjacentIndex = this.matrixToArrayIndex(row+1, col);
            if (this.open[currentAdjacentIndex]) {
                this.quickUnion.union(currentIndex, currentAdjacentIndex);
                this.quickUnionBackup.union(currentIndex, currentAdjacentIndex);
            }
        }

        if (col > 0) {
            currentAdjacentIndex = this.matrixToArrayIndex(row, col-1);
            if (this.open[currentAdjacentIndex]) {
                this.quickUnion.union(currentIndex, currentAdjacentIndex);
                this.quickUnionBackup.union(currentIndex, currentAdjacentIndex);
            }
        }

        if (col < this.n - 1) {
            currentAdjacentIndex = this.matrixToArrayIndex(row, col+1);
            if (this.open[currentAdjacentIndex]) {
                this.quickUnion.union(currentIndex, currentAdjacentIndex);
                this.quickUnionBackup.union(currentIndex, currentAdjacentIndex);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        row -= 1;
        col -= 1;
        if (!this.checkParameters(row) || !this.checkParameters(col)) {
            throw new IllegalArgumentException();
        }

        return this.open[this.matrixToArrayIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        row -= 1;
        col -= 1;

        if (!this.checkParameters(row) || !this.checkParameters(col)) {
            throw new IllegalArgumentException();
        }

        if (!this.isOpen(row + 1, col + 1)) {
            return false;
        }

        return this.quickUnionBackup.find(this.matrixToArrayIndex(row, col)) == this.quickUnionBackup.find(this.upVirtualNodeIndex);
    }

    public int numberOfOpenSites() {
        return this.openSites;
    }

    public boolean percolates() {
        if (this.openSites == 0) {
            return false;
        }

        return this.quickUnion.find(upVirtualNodeIndex) == this.quickUnion.find(bottomVirtualNodeIndex);
    }

    private int matrixToArrayIndex(int row, int col) {
        return (row * this.n) + col;
    }

    private boolean checkParameters(int p) {
        return p >= 0 && p < this.n;
    }
}
