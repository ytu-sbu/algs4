/******************************************************************************
 *  Compilation:  javac xxxxx.java
 *  Execution:    java xxxxx xxx
 *  Dependencies: ffff.java
 *  Author:       Yawei Tu
 *  Date:         08/14/2018
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
// import java.lang.StringBuilder;
import java.util.List;
import java.util.LinkedList;

public class Board {
    private final int[][] board;
    private final int dim;
    private final int swOne, swTwo;
    // private final int[][] twinBoard;

    /** construct a board from an n-by-n array of blocks
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        dim = blocks.length;
        board = new int[dim][dim];
        copyBoard(blocks, board);
        // twinBoard = new int[dim][dim];
        // createTwin();
        int[] blockOne = new int[2];
        int[] blockTwo = new int[2];
        getRandomIndex(blockOne);
        getRandomIndex(blockTwo);
        while (java.util.Arrays.equals(blockOne, blockTwo)) {
            getRandomIndex(blockTwo);
        }
        swOne = blockOne[0] * dim + blockOne[1];
        swTwo = blockTwo[0] * dim + blockTwo[1];
    }

    private void copyBoard(int[][] from, int[][] to) {
        for (int i = 0; i < from.length; i++) {
            System.arraycopy(from[i], 0, to[i], 0, from.length);
        }
    }

    /** return board dimension
     *
     * @return
     */
    public int dimension() {
        return dim;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == dim - 1 && j == dim - 1) {
                    continue;
                }
                if (board[i][j] != dim * i + j + 1) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public int manhattan() {
        int count = 0;
        int x;
        int ii, jj;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    continue;
                }
                x = board[i][j] - 1;
                ii = x / dim;
                jj = x % dim;
                count += Math.abs(i - ii) + Math.abs(j - jj);
            }
        }
        return count;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int[][] twinboard = new int[dim][dim];
        copyBoard(board, twinboard);
        int tmp = twinboard[swOne / dim][swOne % dim];
        twinboard[swOne / dim][swOne % dim] = twinboard[swTwo / dim][swTwo % dim];
        twinboard[swTwo / dim][swTwo % dim] = tmp;

        return new Board(twinboard);
    }

    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (dim != ((Board) y).dimension()) {
            return false;
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getRandomIndex(int[] index) {
        assert index.length > 1;
        index[0] = StdRandom.uniform(dim);
        index[1] = StdRandom.uniform(dim);
        while (board[index[0]][index[1]] == 0) {
            index[0] = StdRandom.uniform(dim);
            index[1] = StdRandom.uniform(dim);
        }
    }
    public Iterable<Board> neighbors() {
        List<Board> nb = new LinkedList<>();
        int row = 0, col = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }
        int[][] neighbor = new int[dim][dim];
        // top neighbor
        if (row != 0) {
            neighbor = createNeighbor(row, col, row - 1, col);
            //((LinkedList<Board>) nb).addFirst(new Board(neighbor));
            nb.add(new Board(neighbor));
        }
        // left neighbor
        if (col != 0) {
            neighbor = createNeighbor(row, col, row, col - 1);
            nb.add(new Board(neighbor));
        }
        // right neighbor
        if (col != dim - 1) {
            neighbor = createNeighbor(row, col, row, col + 1);
            nb.add(new Board(neighbor));
        }
        // bottom neighbor
        if (row != dim - 1) {
            neighbor = createNeighbor(row, col, row + 1, col);
            nb.add(new Board(neighbor));
        }
        return nb;
    }

    private int[][] createNeighbor(int row, int col, int nrow, int ncol) {
        int[][] neighbor = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(board[i], 0, neighbor[i], 0, dim);
        }
        neighbor[row][col] = neighbor[nrow][ncol];
        neighbor[nrow][ncol] = 0;
        return neighbor;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                sb.append(" " + board[i][j]);
                // sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int[][] a = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        // int[][] a = {{0, 2, 3}, {4, 5, 6}, {7, 8, 1}};
        Board b = new Board(a);
        System.out.println("board dimension");
        System.out.println(b.dimension());
        System.out.println("print board");
        System.out.println(b.toString());
        System.out.println("hamming distance");
        System.out.println(b.hamming());
        System.out.println("manhattan distance");
        System.out.println(b.manhattan());
        System.out.println("is Goal?");
        System.out.println(b.isGoal());
        System.out.println("twin");
        Board twin = b.twin();
        System.out.println(twin);
        System.out.println("is twin equals to board");
        System.out.println(b.equals(twin));
        Board twin2 = b.twin();
        System.out.println("is twin2 equals to twin");
        System.out.println(twin2.equals(twin));
        int[][] aa = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board bb = new Board(aa);
        System.out.println("is self equals to dup board");
        System.out.println(b.equals(bb));
        System.out.println("get all neighbors");
        Iterable<Board> neighbors = b.neighbors();
        for (Board n : neighbors) {
            System.out.print(n);
            System.out.println();
        }
    }

}
