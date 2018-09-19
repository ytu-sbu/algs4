import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF sites, full;
    // WeightedQuickUnionUF fulledSites;
    private boolean[] open;
    private int openedSites;
    private int sideLength;
    // two virtual sites
    private int top, bottom;

    /** create a WeightedQuickUnionUF data structure represent a square sites
     *
     * @param n the side length of the square sites
     */
    public Percolation(int n) {
        // validate input
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        // extra 2 virtual sites, one for top, another for bottom;
        sites = new WeightedQuickUnionUF(n * n + 2);
        full = new WeightedQuickUnionUF(n * n + 1);
        open = new boolean[n * n];
        openedSites = 0;
        sideLength = n;
        top = n * n; //first site
        bottom = n * n + 1; //last site
    }

    /** convert the row, col index to 1d index
     *
     * @param row
     * @param col
     * @return
     */
    private int to1d(int row, int col) {
        // validate input
        if (row < 1 || row > sideLength || col < 1 || col > sideLength) {
            throw new java.lang.IllegalArgumentException();
        }
        return (row - 1) * sideLength + col - 1;
    }

    /** open a site as the parameters indicate and connect to the neighbors
     *
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        // open operation
        int index = to1d(row, col); //implicitly validate the input
        // if already opened, do nothing
        if (open[index]) {
            return;
        }
        // else
        open[index] = true;
        openedSites += 1;
        // connect to neighbor open sites
        // top
        if (row > 1) { // for the sites from 2nd row
            if (open[to1d(row - 1, col)]) {
                sites.union(to1d(row, col), to1d(row - 1, col));
                full.union(to1d(row, col), to1d(row - 1, col));
            }
        } else { // top row sites should connect to top site;
            sites.union(to1d(row, col), top);
            full.union(to1d(row, col), top);
        }
        // bottom
        if (row < sideLength) {
            if (open[to1d(row + 1, col)]) {
                sites.union(to1d(row, col), to1d(row + 1, col));
                full.union(to1d(row, col), to1d(row + 1, col));
            }
        } else { // bottom row sites should connect to bottom site
            sites.union(to1d(row, col), bottom);
        }
        // left
        if (col > 1 && open[to1d(row, col - 1)]) {
            sites.union(to1d(row, col), to1d(row, col - 1));
            full.union(to1d(row, col), to1d(row, col - 1));
        }
        // right
        if (col < sideLength && open[to1d(row, col + 1)]) {
            sites.union(to1d(row, col), to1d(row, col + 1));
            full.union(to1d(row, col), to1d(row, col + 1));
        }
    }

    /** determine whether the site of (row, col) is open
     *
     * @param row
     * @param col
     * @return true for open site, false for block site
     */
    public boolean isOpen(int row, int col) {
        return open[to1d(row, col)];
    }

    public boolean isFull(int row, int col) {
        return full.connected(to1d(row, col), top);
    }

    public int numberOfOpenSites() {
        return openedSites;
    }

    public boolean percolates() {
        return sites.connected(top, bottom);
    }

    public static void main(String[] args) {
        Percolation dut = new Percolation(4);

        System.out.println("print 0: " + dut.to1d(1, 1));
        System.out.println("print 15: " + dut.to1d(4, 4));
        System.out.println("print 0: " + dut.numberOfOpenSites());
        System.out.println("is site 1, 1 open? print false: " + dut.isOpen(1, 1));
        System.out.println("is site 1, 1 full? print false: " + dut.isFull(1, 1));
        dut.open(1, 1);
        System.out.println("is site 1, 1 open? print true: " + dut.isOpen(1, 1));
        System.out.println("is site 1, 1 full? print true: " + dut.isFull(1, 1));
        System.out.println("print 1: " + dut.numberOfOpenSites());
        System.out.println("is site percolates? print false: " + dut.percolates());
        dut.open(2, 1);
        dut.open(3, 1);
        dut.open(4, 1);
        System.out.println("is site percolates? print true: " + dut.percolates());
        System.out.println(dut.sites.connected(12, 17));
    }

}
