/******************************************************************************
 *  Compilation:  javac xxxxx.java
 *  Execution:    java xxxxx xxx
 *  Dependencies: ffff.java
 *  Author:       Yawei Tu
 *  Date:         08/15/2018
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.MinPQ;
// import java.util.List;
import java.util.LinkedList;
import java.util.Comparator;

public class Solver {
    // private final MinPQ<SearchNode> pq;
    // private final List<SearchNode> solution = new LinkedList<>();
    private SearchNode result;

    private class SearchNode {
        private final int movesMade;
        private final SearchNode pre;
        private final int distToGoal;
        private final Board board;

        public SearchNode(Board b, int mMade, SearchNode pre) {
            board = b;
            movesMade = mMade;
            this.pre = pre;
            distToGoal = b.manhattan();
        }
    }

    public Solver(Board initial) {
        // pq = new MinPQ<>();
        // solution = new LinkedList<>();
        if (initial == null) {
            throw new java.lang.IllegalArgumentException();
        }
        MinPQ<SearchNode> pq = new MinPQ<>(new Comparator<SearchNode>() {
            public int compare(SearchNode nodeOne, SearchNode nodeTwo) {
                int oneP = nodeOne.movesMade + nodeOne.distToGoal;
                int twoP = nodeTwo.movesMade + nodeTwo.distToGoal;
                if (oneP > twoP) {
                    return 1;
                }
                if (oneP < twoP) {
                    return -1;
                }
                return 0;
            }
        });

        SearchNode origin = new SearchNode(initial, 0, null);
        // pq.insert(new SearchNode(initial, 0, null));
        // SearchNode twin = new SearchNode(initial.twin(), 0, null);
        pq.insert(new SearchNode(initial.twin(), 0, null));
        pq.insert(origin);
        // pq.insert(twin);
        // System.out.print(origin.board);
        // System.out.print(twin.board);
        /* for (Board b : origin.board.neighbors()) {
            pq.insert(new SearchNode(b, origin.movesMade + 1, origin));
        }
        for (Board b : twin.board.neighbors()) {
            pq.insert(new SearchNode(b, twin.movesMade + 1, twin));
        } */
        SearchNode current = null;
        while (!pq.isEmpty()) {
            current = pq.delMin();
            /*
            System.out.println("============================");
            System.out.println("got a new SearchNode");
            // System.out.println("this is current");
            System.out.print(current.board);
            System.out.print(current.movesMade);
            System.out.println();
            System.out.println("this is origin");
            System.out.print(origin.board);
            System.out.println();
            System.out.println("this is twin");
            System.out.print(twin.board);
            System.out.println();
            */
            /* if (current.pre == origin || current == origin) {
                origin = current;
            }
            else if (current.pre == twin || current == twin){
                twin = current;
            }
            else {
                throw new java.lang.RuntimeException();
            } */

            if (current.board.isGoal()) {
                break;
            }

            for (Board b : current.board.neighbors()) {
                SearchNode next = new SearchNode(b, current.movesMade + 1, current);
                // if (searched(current, next)) {
                if (current.pre != null && next.board.equals(current.pre.board)) {
                    continue;
                }
                else {
                    /*
                    System.out.println("************");
                    System.out.println("insert a new SearchNode into pq");
                    System.out.println(next.board);
                    System.out.println();
                    */
                    pq.insert(next);
                }
            }
        }
        if (origin == findStart(current)) {
            result = current;
        }
    }

    private SearchNode findStart(SearchNode sn) {
        SearchNode tmp = sn;
        while (tmp.pre != null) {
            tmp = tmp.pre;
        }
        return tmp;
    }

    /* private boolean searched(SearchNode searchedNodes, SearchNode nextNode) {
        SearchNode check = searchedNodes.pre;
        while (check != null) {
            if (nextNode.board.equals(check.board)) {
                return true;
            }
            check = check.pre;
        }
        return false;
    } */

    public boolean isSolvable() {
        return result != null;
    }

    public int moves() {
        // return isSolvable() ? result.movesMade : -1;
        if (isSolvable()) {
            return result.movesMade;
        }
        return -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        LinkedList<Board> soluSeq = new LinkedList<>();
        SearchNode tmp = result;
        while (tmp != null) {
            soluSeq.addFirst(tmp.board);
            tmp = tmp.pre;
        }
        return soluSeq;
    }
}
