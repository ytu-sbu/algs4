import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.ArrayList;

public class BaseballElimination {
    private final TrieST<Integer> getId;
    private final String[] getName;
    private final int num;
    private final int[] w, loss, r;
    private final int[][] g;
    private final boolean[] run;
    private final ArrayList<LinkedList<String>> certificate;

    public BaseballElimination(String filename) {
        getId = new TrieST<>();
        In in = new In(filename);
        num = in.readInt();
        getName = new String[num];
        run = new boolean[num];
        w = new int[num];
        loss = new int[num];
        r = new int[num];
        g = new int[num][num];
        certificate = new ArrayList<>((int) (num * 1.5));
        for (int i = 0; i < num; i++) {
            certificate.add(i, new LinkedList<>());
        }

        for (int i = 0; i < num; i++) {
            String name = in.readString();
            getId.put(name, i);
            getName[i] = name;
            w[i] = in.readInt();
            loss[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < num; j++) {
                g[i][j] = in.readInt();
            }
        }
        in.close();
    }

    public int numberOfTeams() {
        return num;
    }

    public Iterable<String> teams() {
        return getId.keys();
    }

    public int wins(String team) {
        validateTeam(team);
        return w[getId.get(team)];
    }

    public int losses(String team) {
        validateTeam(team);
        return loss[getId.get(team)];
    }

    public int remaining(String team) {
        validateTeam(team);
        return r[getId.get(team)];
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return g[getId.get(team1)][getId.get(team2)];
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        checkElimination(team);
        if (certificate.get(getId.get(team)).isEmpty()) {
            return false;
        }
        return true;
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        int id = getId.get(team);
        checkElimination(team);
        if (certificate.get(id).isEmpty()) {
            return null;
        }
        return certificate.get(id);
    }

    private void checkElimination(String team) {
        int id = getId.get(team);
        if (!run[id]) {
            trivialElimination(team);
            if (certificate.get(id).isEmpty()) {
                nonTrivialElimination(team);
            }
            run[id] = true;
        }
    }

    private void trivialElimination(String team) {
        int id = getId.get(team);
        for (int i = 0; i < num; i++) {
            if (i == id) {
                continue;
            }
            if (w[id] + r[id] < w[i]) {
                certificate.get(id).addLast(getName[i]);
                break;
            }
        }
    }

    private void nonTrivialElimination(String team) {
        int ndNum = (num - 1) * (num - 2) / 2 + 2 + num;
        int s = ndNum - 2, t = ndNum - 1;
        int id = getId.get(team);

        FordFulkerson ff = new FordFulkerson(constructFlow(team), s, t);
        for (int i = 0; i < num; i++) {
            if (ff.inCut(i)) {
                certificate.get(id).addLast(getName[i]);
            }
        }
    }

    private FlowNetwork constructFlow(String team) {
        int ndNum = (num - 1) * (num - 2) / 2 + 2 + num;
        int id = getId.get(team);
        int totalWin = w[id] + r[id];
        FlowNetwork fn = new FlowNetwork(ndNum);
        int s = ndNum - 2, t = ndNum - 1;
        int count = num;
        FlowEdge e;
        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                // construct the remaining games vertex
                if (i == id || j == id) {
                    continue;
                }
                // from s to remaining games vertex
                e = new FlowEdge(s, count, g[i][j]);
                fn.addEdge(e);
                // from remaining games vertex to team v
                e = new FlowEdge(count, i, Double.POSITIVE_INFINITY);
                fn.addEdge(e);
                e = new FlowEdge(count, j, Double.POSITIVE_INFINITY);
                fn.addEdge(e);
                count += 1;
            }
            // from team node to t node
            if (i == id) {
                continue;
            }
            e = new FlowEdge(i, t, totalWin - w[i]);
            fn.addEdge(e);
        }
        return fn;
    }

    private void validateTeam(String team) {
        if (!getId.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }
    }
}
