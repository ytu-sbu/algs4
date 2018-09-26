import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class SAP {
    private final Digraph g;
//    private final int V;
//    private boolean[] markv, markw;
    private int[] distToV, distToW;
    private final LinkedList<Integer> resetV;
    private final LinkedList<Integer> resetW;


    public SAP(Digraph G) {
        validateNull(G);
        g = new Digraph(G);
//        V = G.V();
        // markv = new boolean[V];
        // markw = new boolean[V];
        distToV = new int[g.V()];
        distToW = new int[g.V()];
        resetV = new LinkedList<>();
        resetW = new LinkedList<>();
        for (int v = 0; v < g.V(); v++) {
            distToV[v] = -1;
            distToW[v] = -1;
        }
    }

    private void validateNull(Object arg) {
        if (arg == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void validateOutRange(int i) {
        if (i < 0 || i >= g.V()) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void testInvalideContent(Iterable<Integer> i) {
        for (int t : i) {
            validateNull(t);
            validateOutRange(t);
        }
    }

    /** Run BFS on v, mark all vertices reachable from v
     * then run BFS on w, the first marked vertex encountered is the ancestor of them.
     * Make two arrays of distTo[] thus the length = distToV[ancestor] + distToW[ancestor]
     * @param v
     * @param w
     * @return
     */
    public int length(int v, int w) {
        int a = ancestor(v, w);
        if (a == -1) {
            return -1;
        }
        return distToV[a] + distToW[a];
    }

    private void clear() {
//        markv = new boolean[V];
//        markw = new boolean[V];
//        distToV = new int[V];
//        distToW = new int[V];
        for (int v : resetV) {
            distToV[v] = -1;
        }
        resetV.clear();
        for (int w : resetW) {
            distToW[w] = -1;
        }
        resetW.clear();
    }

    public int ancestor(int v, int w) {
        validateOutRange(v);
        validateOutRange(w);

        if (v == w) {
            distToV[v] = 0;
            distToW[v] = 0;
            resetV.add(v);
            resetW.add(v);
            return v;
        }
        clear();

        bfs(v, distToV, resetV);

        Queue<Integer> q = new Queue<>();
        distToW[w] = 0;
        q.enqueue(w);
        resetW.add(w);
        int n;
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        int dis;
        while (!q.isEmpty()) {
            n = q.dequeue();
            if (distToV[n] != -1) {
                dis = distToV[n] + distToW[n];
                if (dis < min) {
                    min = dis;
                    ancestor = n;
                }
            }
            for (int m :g.adj(n)) {
                if (distToW[m] == -1) {
                    q.enqueue(m);
                    distToW[m] = distToW[n] + 1;
                    resetW.add(m);
//                    if (distToV[m] != -1) {
//                        return m;
//                    }
                }
            }
        }
        // no ancestor
        return ancestor;
    }

    private void bfs(int s, int[] distTo, LinkedList<Integer> reset) {
        Queue<Integer> q = new Queue<>();
        distTo[s] = 0;
        q.enqueue(s);
        reset.add(s);
        int v;
        while (!q.isEmpty()) {
            v = q.dequeue();
            for (int w : g.adj(v)) {
                if (distTo[w] == -1) {
                    distTo[w] = distTo[v] + 1;
                    reset.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    private void bfs(Iterable<Integer> source, int[] distTo, LinkedList<Integer> reset) {
        Queue<Integer> q = new Queue<>();
        for (int s : source) {
            distTo[s] = 0;
            reset.add(s);
            q.enqueue(s);
        }
        int v;
        while (!q.isEmpty()) {
            v = q.dequeue();
            for (int w : g.adj(v)) {
                if (distTo[w] == -1) {
                    distTo[w] = distTo[v] + 1;
                    reset.add(w);
                    q.enqueue(w);
                }
            }
        }
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int a = ancestor(v, w);
        if (a == -1) {
            return -1;
        }
        return distToV[a] + distToW[a];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateNull(v);
        validateNull(w);
        testInvalideContent(v);
        testInvalideContent(w);

        clear();

        bfs(v, distToV, resetV);

        Queue<Integer> q = new Queue<>();
        for (int s : w) {
            distToW[s] = 0;
            q.enqueue(s);
            resetW.add(s);
        }
        int n;
        int min = Integer.MAX_VALUE;
        int ancestor = -1;
        int dis;
        while (!q.isEmpty()) {
            n = q.dequeue();
            if (distToV[n] != -1) {
                dis = distToV[n] + distToW[n];
                if (dis < min) {
                    min = dis;
                    ancestor = n;
                }
            }
            for (int m : g.adj(n)) {
                if (distToW[m] == -1) {
                    q.enqueue(m);
                    resetW.add(m);
                    distToW[m] = distToW[n] + 1;
                }
            }
        }
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
