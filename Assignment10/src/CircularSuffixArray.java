import edu.princeton.cs.algs4.IndexMinPQ;

public class CircularSuffixArray {
    private final int length;
    private final int[] index;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new java.lang.IllegalArgumentException();
        }
        // initialize
        length = s.length();
        index = new int[length];

        // processing data
        for (int i = 0; i < length; i++) {
            index[i] = i;
        }
        IndexMinPQ<Character> pq = new IndexMinPQ<>(length);
        strSort(s, 0, length - 1, 0, pq);
    }

    private int getPos(int i, int d) {
        int pos = i + d;
        if (pos >= length) {
            return pos - length;
        }
        return pos;
    }

    private void strSort(String s, int lo, int hi, int d, IndexMinPQ<Character> pq) {
        if (lo >= hi) {
            return;
        }
        for (int i = lo; i <= hi; i++) {
            pq.insert(index[i], s.charAt(getPos(index[i], d)));
        }
        for (int i = lo; i <= hi; i++) {
            index[i] = pq.delMin();
        }
        char c = s.charAt(getPos(index[lo], d));
        int start = lo;
        for (int i = lo + 1; i <= hi; i++) {
            if (c == s.charAt(getPos(index[i], d))) {
                if (i == hi) {
                    strSort(s, start, i, d + 1, pq);
                }
                else {
                    continue;
                }
            }
            strSort(s, start, i - 1, d + 1, pq);
            start = i;
            c = s.charAt(getPos(index[i], d));
        }
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new java.lang.IllegalArgumentException();
        }
        return index[i];
    }

    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i));
        }
    }
}
