import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void transform() {
        String sb = BinaryStdIn.readString();
        BinaryStdIn.close();
        CircularSuffixArray csa = new CircularSuffixArray(sb);
        int first = -1;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            int pos = csa.index(i) - 1;
            if (pos < 0) {
                pos += csa.length();
            }
            BinaryStdOut.write(sb.charAt(pos));
        }
        BinaryStdOut.close();
    }

    private static char[] radixSort(char[] unSort) {
        int length = unSort.length;
        char[] count = new char[R + 1];
        for (int i = 0; i < length; i++) {
            count[unSort[i] + 1] += 1;
        }
        for (int i = 1; i <= R; i++) {
            count[i] += count[i - 1];
        }
        char[] sorted = new char[length];
        for (int i = 0; i < length; i++) {
            sorted[count[unSort[i]]++] = unSort[i];
        }
        return sorted;
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String sb = BinaryStdIn.readString();
        BinaryStdIn.close();
        int length = sb.length();
        char[] t = sb.toCharArray();
        char[] origin = radixSort(t);
        int[] next = new int[length];

        int[] charCount = new int[R];
        int[] charIndex = new int[length];
        for (int i = 0; i < length; i++) {
            charIndex[i] = ++charCount[origin[i]];
        }
        for (int i = 0; i < length; i++) {
            int count = charIndex[i];
            for (int j = 0; j < length; j++) {
                if (t[j] == origin[i]) {
                    if (count > 1) {
                        count -= 1;
                    }
                    else {
                        next[i] = j;
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(origin[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new java.lang.IllegalArgumentException();
        }
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        }
        if (args[0].equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
