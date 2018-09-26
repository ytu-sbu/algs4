
public class CircularSuffixArray {
    private static final int R = 256;
    private static final int M = 15;
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
        // initialize index array
        for (int i = 0; i < length; i++) {
            index[i] = i;
        }
        // MSD radix sort
        int[] aux = new int[length];
        strSort(s, 0, length - 1, 0, aux);
    }

    private int getPos(int i, int d) {
        int pos = i + d;
        if (pos >= length) {
            return pos - length;
        }
        return pos;
    }

    private void strSort(String s, int lo, int hi, int d, int[] aux) {
        // base case
        if (lo >= hi || d >= length) {
            return;
        }
        // do insertion Sort for String array with length less than or equals M
        if (hi <= lo + M) {
            insertSort(s, lo, hi, d);
            return;
        }
        // MSD radix sort
        int[] count = new int[R + 1];
        for (int i = lo; i <= hi; i++) {
            count[s.charAt(getPos(index[i], d)) + 1] += 1;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = lo; i <= hi; i++) {
            aux[count[s.charAt(getPos(index[i], d))]++] = index[i];
        }
        for (int i = lo; i <= hi; i++) {
            index[i] = aux[i - lo];
        }
        strSort(s, lo, lo + count[0] - 1, d + 1, aux);
        for (int i = 0; i < R; i++) {
            strSort(s, lo + count[i], lo + count[i + 1] - 1, d + 1, aux);
        }
    }

    private void insertSort(String s, int lo, int hi, int d) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && aLessB(s, j, j - 1, d); j--) {
                int tmp = index[j];
                index[j] = index[j - 1];
                index[j - 1] = tmp;
            }
        }
    }

    private boolean aLessB(String s, int a, int b, int d) {
        for (int i = d; i < length; i++) {
            if (s.charAt(getPos(index[a], i)) < s.charAt(getPos(index[b], i))) {
                return true;
            }
            if (s.charAt(getPos(index[a], i)) > s.charAt(getPos(index[b], i))) {
                return false;
            }
        }
        // equal
        return true;
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
//        String s = "ABRACADABRA!";
        String s = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++) {
            System.out.println(csa.index(i));
        }
    }
}
