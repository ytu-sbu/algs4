import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;

public class Outcast {
    private final WordNet wordnet;
    private final HashMap<WordPair, Integer> cache;


    private class WordPair {
        private final String wordOne;
        private final String wordTwo;
        private static final int OFFSET = 16;

        public WordPair(String wx, String wy) {
            wordOne = wx;
            wordTwo = wy;
        }

        @Override
        public int hashCode() {
            int valueOne = wordOne.hashCode();
            int valueTwo = wordTwo.hashCode();

            if (valueOne < valueTwo) {
                return (valueOne << OFFSET) + valueTwo;
            }
            else {
                return (valueTwo << OFFSET) + valueOne;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            WordPair other = (WordPair) obj;
            return this.wordOne.equals(other.wordOne) && this.wordTwo.equals(other.wordTwo)
                    || this.wordOne.equals(other.wordTwo) && this.wordTwo.equals(other.wordOne);
        }

    }
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
        cache = new HashMap<>();
    }

    public String outcast(String[] nouns) {
        int maxDis = 0;
        String oc = "";
        WordPair wp;
        for (int i = 0; i < nouns.length; i++) {
            int dis = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) {
                    continue;
                }
                wp = new WordPair(nouns[i], nouns[j]);
                if (cache.containsKey(wp)) {
                    dis += cache.get(wp);
                }
                else {
                    int pairDis = wordnet.distance(nouns[i], nouns[j]);
                    dis += pairDis;
                    cache.put(wp, pairDis);
                }
            }
            if (dis > maxDis) {
                maxDis = dis;
                oc = nouns[i];
            }
        }
        return oc;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
