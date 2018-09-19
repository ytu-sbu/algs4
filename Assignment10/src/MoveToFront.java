import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        LList<Character> alphabet = new LList<>();
        for (char c = R - 1; c >= 0; c--) {
            alphabet.addFirst(c);
        }
        while(!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char index = alphabet.moveToFront(c);
            BinaryStdOut.write(index);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    public static void decode() {
        char[]alphabet = new char[R];
        for (char c = 0; c < R; c++) {
            alphabet[c] = c;
        }
        while(!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readInt();
            char c = alphabet[i];
            System.arraycopy(alphabet, 0, alphabet, 1, i);
            alphabet[0] = c;
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        MoveToFront.encode();
    }
}
