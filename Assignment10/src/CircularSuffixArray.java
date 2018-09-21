import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.LinkedList;

public class CircularSuffixArray {
    private static final int R = 256;
    private final int length;
    private final int[] index;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new java.lang.IllegalArgumentException();
        }
        // initialize
        length = s.length();
        index = new int[length];

        //processing data
        LinkedList<Integer> list = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            list.add(i);
        }
        strSort(s, list, 0, 0);
    }

    private int strSort(String s, LinkedList<Integer> list, int offset, int start) {
        // otherwise there is only one element in bucket
        if (list.size() ==1) {
            index[start] = list.remove();
            return start + 1;
        }
        // more than one elements in bucket, sorting on
        IndexMinPQ<Character> pq = new IndexMinPQ<>(length);
        // put subStr id and key into minPQ
        int size = list.size();
        for (int i = 0, j; i < size; i++) {
            j = list.remove();
            int pos = j + offset;
            if (pos >= length) {
                pos -= length;
            }
            pq.insert(j, s.charAt(pos));
        }
        // pop items and sort
        char preKey = pq.minKey();
        list.add(pq.delMin());
        while (!pq.isEmpty()) {
            if (pq.minKey() != preKey) {
                start = strSort(s, list, offset + 1, start);
                preKey = pq.minKey();
            }
            list.add(pq.delMin());
        }
        if (!list.isEmpty()) {
            start = strSort(s, list, offset + 1, start);
        }
        return start;
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
