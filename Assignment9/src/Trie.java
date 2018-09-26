public class Trie {
    private static final int R = 26;
    private static final int A = 65;
    private Node root;

    public static class Node {
        private Node[] next = new Node[R];
        private boolean isString;

        public boolean isString() {
            return isString;
        }
    }

    public Trie() {
    }

    public void add(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException();
        }
        root = add(root, word, 0);
    }

    private Node add(Node x, String word, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == word.length()) {
            if (!x.isString) {
                x.isString = true;
            }
        }
        else {
            char c = word.charAt(d);
            x.next[c - A] = add(x.next[c - A], word, d + 1);
        }
        return x;
    }

    public Node getRoot() {
        return root;
    }

    public Node hasChar(Node preNode, char c) {
        if (preNode.next[c - A] == null) {
            return null;
        }
        return preNode.next[c - A];
    }

    public boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) {
            return false;
        }
        return x.isString;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return get(x.next[c - A], key, d + 1);
    }
}
