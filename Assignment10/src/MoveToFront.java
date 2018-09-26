import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    public static void encode() {
        LList<Character> alphabet = new LList<>();
        for (int c = R - 1; c >= 0; c--) {
            alphabet.addFirst((char) c);
        }
//        System.out.println(alphabet.toString());
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
//            System.out.println((int) c);
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

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();
            char c = alphabet[i];
            System.arraycopy(alphabet, 0, alphabet, 1, i);
            alphabet[0] = c;
            BinaryStdOut.write(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            throw new java.lang.IllegalArgumentException();
        }
        if (args[0].equals("-")) {
            MoveToFront.encode();
        }
        if (args[0].equals("+")) {
            MoveToFront.decode();
        }
    }

}
