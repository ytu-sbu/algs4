import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.ArrayList;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private final HashMap<Integer, Bag<String>> map;
    private final HashMap<String, Bag<Integer>> contains;
//    private final Digraph hyper;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException();
        }
        map = new HashMap<>();
        contains = new HashMap<>();

        // create a symbol table which convert node to noun
        In file = new In(synsets);
        String line;
//        String delimiter = ",";
        String[] elements;
        String[] nouns;
        Bag<String> intToStr;
        Bag<Integer> strToInt;
        while (file.hasNextLine()) {
            line = file.readLine();
            elements = line.split(",");
            nouns = elements[1].split(" ");
            intToStr = new Bag<>();
            for (String s : nouns) {
                intToStr.add(s);
                // add string - integer to contains map
                if (contains.containsKey(s)) {
                    contains.get(s).add(Integer.parseInt(elements[0]));
                }
                else {
                    strToInt = new Bag<>();
                    strToInt.add(Integer.parseInt(elements[0]));
                    contains.put(s, strToInt);
                }
            }
            map.put(Integer.parseInt(elements[0]), intToStr);
        }
        file.close();

        // create a digraph, represent the hypernyms relation
        Digraph hyper = new Digraph(map.size());
        file = new In(hypernyms);
        int from;
        while (file.hasNextLine()) {
            line = file.readLine();
            elements = line.split(",");
            from = Integer.parseInt(elements[0]);
            for (int i = 1; i < elements.length; i++) {
                hyper.addEdge(from, Integer.parseInt(elements[i]));
            }
        }
        file.close();
        DirectedCycle detectCycle = new DirectedCycle(hyper);
        if (detectCycle.hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
        int rootNum = 0;
        for (int v = 0; v < hyper.V(); v++) {
            if (hyper.outdegree(v) == 0 && hyper.indegree(v) > 0) {
                rootNum += 1;
                if (rootNum > 1) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        sap = new SAP(hyper);
    }


    public Iterable<String> nouns() {
        ArrayList<String> n = new ArrayList<>(map.size());
        for (int k : map.keySet()) {
            Bag<String> b = map.get(k);
            for (String s : b) {
                n.add(s);
            }
        }
        return n;
    }

    public boolean isNoun(String word) {
        return contains.containsKey(word);
    }

    private void validatNoun(String s) {
        if (!isNoun(s)) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public int distance(String nounA, String nounB) {
        validatNoun(nounA);
        validatNoun(nounB);

        Iterable<Integer> nA = contains.get(nounA);
        Iterable<Integer> nB = contains.get(nounB);

        return sap.length(nA, nB);
    }

    /*
    private Iterable<Integer> wordtoInt(String noun) {
        return contains.get(noun);
    }
    */

    public String sap(String nounA, String nounB) {
        validatNoun(nounA);
        validatNoun(nounB);

        Iterable<Integer> nA = contains.get(nounA);
        Iterable<Integer> nB = contains.get(nounB);

        int ans = sap.ancestor(nA, nB);
        StringBuilder sb = new StringBuilder();
        for (String s : map.get(ans)) {
            sb.append(s);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
    }


}
