/*
    @author: Simone Nicol <en0mia.dev@gmail.com>
    @created: 14/09/21
    @copyright: Check the repository license.
*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] results;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        this.results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            while (!p.percolates()) {
                int a = StdRandom.uniform(1, n + 1);
                int b = StdRandom.uniform(1, n + 1);
                p.open(a, b);
            }

            this.results[i] = (double) p.numberOfOpenSites() / (n*n);
        }

    }

    public double mean() {
        return StdStats.mean(this.results);
    }

    public double stddev() {
        return StdStats.stddev(this.results);
    }

    public double confidenceLo() {
        return this.mean() - ((CONFIDENCE_95 * this.stddev()) / Math.sqrt(this.results.length));
    }

    public double confidenceHi() {
        return this.mean() + ((CONFIDENCE_95 * this.stddev()) / Math.sqrt(this.results.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);

        StdOut.println("mean = " + String.format("%f", ps.mean()));
        StdOut.println("stddev = " + String.format("%f", ps.stddev()));
        StdOut.println("95% confidence interval = " + String.format("[%f, %f]", ps.confidenceLo(), ps.confidenceHi()));
    }
}
