import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;

public class BoggleSolver {
    private final Trie dict;

    public BoggleSolver(String[] dictionary) {
        dict = new Trie();
        for (String s : dictionary) {
            dict.add(s);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int row = board.rows();
        int col = board.cols();
        HashSet<String> words = new HashSet<>();

        int[][] neighbors = new int[row * col][];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                neighbors[twoToOne(i, j, col)] = constructNeighbors(i, j, board);
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                StringBuilder word = new StringBuilder();
                boolean[] mark = new boolean[row * col];

                Trie.Node next = dict.hasChar(dict.getRoot(), board.getLetter(i, j));
                if (next == null) {
                    continue;
                }
                dfs(i, j, next, mark, word, words, board, neighbors);
            }
        }
        return words;
    }

    private void dfs(int i, int j, Trie.Node node, boolean[] mark, StringBuilder word, HashSet<String> words, BoggleBoard board,
                     int[][] neighbors) {

        int index = twoToOne(i, j, board.cols());
        mark[index] = true;
        char nodeChar = board.getLetter(i, j);
        if (nodeChar != 'Q') {
            word.append(nodeChar);
        }
        else {
            // search for u after Q
            node = dict.hasChar(node, 'U');
            if (node == null) {
                return;
            }
            word.append('Q');
            word.append('U');
        }
        if (node.isString() && word.length() > 2) {
            words.add(word.toString());
        }

        for (int n = 0; n < neighbors[index].length; n++) {
            int childId = neighbors[index][n];
            int childi = childId / board.cols();
            int childj = childId % board.cols();

            Trie.Node nextNode = dict.hasChar(node, board.getLetter(childi, childj));

            if (nextNode != null && !mark[childId]) {
                dfs(childi, childj, nextNode, mark, word, words, board, neighbors);
            }
        }
        mark[index] = false;
        if (nodeChar == 'Q') {
            word.deleteCharAt(word.length() - 1);
        }
        word.deleteCharAt(word.length() - 1);
    }

    private int[] constructNeighbors(int i, int j, BoggleBoard board) {
        int row = board.rows();
        int col = board.cols();
        int[] cache = new int[8];

        int count = 0;
        for (int r = i - 1; r <= i + 1; r++) {
            for (int c = j - 1; c <= j + 1; c++) {
                if (r < 0 || r >= row || c < 0 || c >= col || r == i && c == j) {
                    continue;
                }
                cache[count++] = twoToOne(r, c, col);
            }
        }
        int[] neighbors = new int[count];
        System.arraycopy(cache, 0, neighbors, 0, count);
        return neighbors;
    }

    private int twoToOne(int i, int j, int col) {
        return i * col + j;
    }

    public int scoreOf(String word) {
        if (word.length() < 3 || !dict.contains(word)) {
            return 0;
        }
        if (word.length() < 5) {
            return 1;
        }
        if (word.length() == 5) {
            return 2;
        }
        if (word.length() == 6) {
            return 3;
        }
        if (word.length() == 7) {
            return 5;
        }
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        System.out.println("number of words found: " + solver.getAllValidWords(board));
    }
}
