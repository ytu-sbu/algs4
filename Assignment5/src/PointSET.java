/******************************************************************************
 * Author: Yawei Tu
 * Date  : 18/08/2018
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.TreeSet;
import java.util.LinkedList;

public class PointSET {
    private final TreeSet<Point2D> ps;
    private int size;

    public PointSET() {
        ps = new TreeSet<>();
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void validate(Object p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public void insert(Point2D p) {
        validate(p);
        if (!ps.contains(p)) {
            ps.add(p);
            size += 1;
        }
    }

    public boolean contains(Point2D p) {
        validate(p);
        return ps.contains(p);
    }

    public void draw() {
        for (Point2D p : ps) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        LinkedList<Point2D> inner = new LinkedList<>();
        for (Point2D p : ps) {
            if (rect.contains(p)) {
                inner.add(p);
            }
        }
        return inner;
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        Point2D nearest = null;
        double dis = Double.POSITIVE_INFINITY;
        for (Point2D i : ps) {
            double newDis = i.distanceTo(p);
            if (newDis < dis) {
                dis = newDis;
                nearest = i;
            }
        }
        return nearest;
    }
}
