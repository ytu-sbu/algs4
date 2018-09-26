import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void transform() {
        String sb = BinaryStdIn.readString();
//        BinaryStdIn.close();
        CircularSuffixArray csa = new CircularSuffixArray(sb);
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < csa.length(); i++) {
            int pos = csa.index(i) - 1;
            if (pos < 0) {
                pos += csa.length();
            }
            BinaryStdOut.write(sb.charAt(pos), 8);
        }
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String sb = BinaryStdIn.readString();
//        BinaryStdIn.close();
        int length = sb.length();
        char[] t = sb.toCharArray();
        char[] origin = new char[length];
        int[] next = new int[length];
        int[] count = new int[R + 1];

        for (int i = 0; i < length; i++) {
            count[t[i] + 1] += 1;
        }
        for (int i = 1; i <= R; i++) {
            count[i] += count[i - 1];
        }
        char c;
        int j;
        for (int i = 0; i < length; i++) {
            c = t[i];
            j = count[c];
            origin[j] = t[i];
            next[j] = i;
            count[c] += 1;
        }

        for (int i = 0; i < length; i++) {
            BinaryStdOut.write(origin[first], 8);
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
