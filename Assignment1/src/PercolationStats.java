import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] testRecords;
    private double mean, stddev;
    private int testTimes;
    // private final double CONFIDENCE_95 = 1.96;
    private double confidenceLo, confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        testRecords = new double[trials];
        testTimes = trials;
        Percolation dut;
        int row, col;
        for (int i = 0; i < trials; i++) {
            dut = new Percolation(n);
            while (!dut.percolates()) {
                row = StdRandom.uniform(n) + 1;
                col = StdRandom.uniform(n) + 1;
                dut.open(row, col);
            }
            testRecords[i] = (double) dut.numberOfOpenSites() / (n * n);
        }
        mean = StdStats.mean(testRecords);
        stddev = StdStats.stddev(testRecords);
        double tmp = 1.96 * stddev / Math.sqrt(testTimes);
        confidenceLo =  mean - tmp;
        confidenceHi =  mean + tmp;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return confidenceLo;
    }

    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new java.lang.IllegalArgumentException();
        }

        PercolationStats dut = new PercolationStats(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        System.out.println("mean                    = "
                + dut.mean());
        System.out.println("stddev                  = "
                + dut.stddev());
        System.out.println("95% confidence interval = ["
                + dut.confidenceLo() + ", " + dut.confidenceHi() + "]");
    }
}
