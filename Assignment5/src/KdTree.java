/******************************************************************************
 * Author: Yawei Tu
 * Date  : 18/08/2018
 *  Compilation:
 *  Execution:
 *
******************************************************************************/

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {
    private static final boolean V = false;
    // private static final boolean H = true;
    private int size;
    private Node root;

    private static class Node {

        private final Point2D p;
        private final boolean dirction;
        private Node left, right;
        private final RectHV rect;

        public Node(Point2D p, boolean dir, double xmin, double ymin, double xmax, double ymax) {
            this.p = p;
            dirction = dir;
            left = null;
            right = null;
            rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    public KdTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void validate(Object obj) {
        if (obj == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    public void insert(Point2D p) {
        validate(p);
        root = insert(root, p, V, 0.0, 0.0, 1.0, 1.0);
    }

    private Node insert(Node n, Point2D p, boolean dir, double xmin, double ymin, double xmax, double ymax) {
        if (n == null) {
            size += 1;
            return new Node(p, dir, xmin, ymin, xmax, ymax);
        }
        if (dir == V) {
            double cmp = n.p.x() - p.x();
            // leftplane
            if (cmp > 0) {
                n.left = insert(n.left, p, !dir, xmin, ymin, n.p.x(), ymax);
            }
            // rightplane
            else if (cmp < 0 || cmp == 0 && n.p.y() - p.y() != 0) {
                n.right = insert(n.right, p, !dir, n.p.x(), ymin, xmax, ymax);
            }
            // same location
            else {
                return n;
            }
        }
        // dir == H
        else {
            double cmp = n.p.y() - p.y();
            // downside
            if (cmp > 0) {
                n.left = insert(n.left, p, !dir, xmin, ymin, xmax, n.p.y());
            }
            // upside
            else if (cmp < 0 || cmp == 0 && n.p.x() - p.x() != 0) {
                n.right = insert(n.right, p, !dir, xmin, n.p.y(), xmax, ymax);
            }
            // same location
            else {
                return n;
            }
        }
        return n;
    }

    public boolean contains(Point2D p) {
        validate(p);
        return contains(root, p);
    }

    private boolean contains(Node n, Point2D p) {
        if (n == null) {
            return false;
        }
        if (n.dirction == V) {
            double cmp = n.p.x() - p.x();
            if (cmp > 0) {
                return contains(n.left, p);
            }
            else if (cmp < 0 || cmp == 0 && n.p.y() - p.y() != 0) {
                return contains(n.right, p);
            }
            // else if (n.p.y() - p.y() == 0) {
            else {
                return true;
            }
            // return false;
        }
        // n dir == H
        else {
            double cmp = n.p.y() - p.y();
            if (cmp > 0) {
                return contains(n.left, p);
            }
            else if (cmp < 0 || cmp == 0 && n.p.x() - p.x() != 0) {
                return contains(n.right, p);
            }
            // else if (n.p.x() - p.x() == 0) {
            else {
                return true;
            }
            // return false;
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node n) {
        if (n == null) {
            return;
        }
        // draw a line
        // current node is Vertical
        if (n.dirction == V) {
            StdDraw.setPenColor(StdDraw.RED);
            drawLine(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        }
        // current node is Horizontal
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            drawLine(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        // draw children
        drawPoint(n);
        draw(n.left);
        draw(n.right);
    }

    private void drawLine(double xmin, double ymin, double xmax, double ymax) {
        StdDraw.setPenRadius(0.003);
        StdDraw.line(xmin, ymin, xmax, ymax);
    }

    private void drawPoint(Node n) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        LinkedList<Point2D> contained = new LinkedList<>();
        if (root != null) {
            range(rect, root, contained);
        }
        return contained;
    }

    private void range(RectHV rect, Node n, LinkedList<Point2D> linked) {
        /* if (n == null) {
            return;
        } */
        if (rect.contains(n.p)) {
            linked.add(n.p);
        }
        if (n.left != null && rect.intersects(n.left.rect)) {
            range(rect, n.left, linked);
        }
        if (n.right != null && rect.intersects(n.right.rect)) {
            range(rect, n.right, linked);
        }
    }

    public Point2D nearest(Point2D p) {
        validate(p);
        if (root == null) {
            return null;
        }

        return nearest(p, root, root.p);
    }

    private Point2D nearest(Point2D p, Node n, Point2D min) {
        if (n == null) {
            return min;
        }
        // double minDist = min.distanceTo(p);
        double minDist = min.distanceSquaredTo(p);
        // if (n.rect.distanceTo(p) > minDist) {
        if (n.rect.distanceSquaredTo(p) > minDist) {
            return min;
        }
        double dist = n.p.distanceSquaredTo(p);
        if (dist < minDist) {
            min = n.p;
        }
        double cmp;
        // vertical Node
        if (n.dirction == V) {
            cmp = n.p.x() - p.x();
            // on left plane
            if (cmp > 0) {
                min = nearest(p, n.left, min);
                min = nearest(p, n.right, min);
            }
            // on right plane
            else {
                min = nearest(p, n.right, min);
                min = nearest(p, n.left, min);
            }
        }
        // horizontal Node
        else {
            cmp = n.p.y() - p.y();
            // on down plane
            if (cmp > 0) {
                min = nearest(p, n.left, min);
                min = nearest(p, n.right, min);
            }
            // on up plane
            else {
                min = nearest(p, n.right, min);
                min = nearest(p, n.left, min);
            }
        }
        return min;
    }

    public static void main(String[] args) {
        KdTree kdt = new KdTree();
        /*
        kdt.insert(new Point2D(0.5, 0.5));
        System.out.println(kdt.size());
        kdt.insert(new Point2D(0.5, 0.5));
        System.out.println(kdt.size());

        kdt.draw();
        Point2D query = new Point2D(0.378, 0.7);
        Point2D nearest = kdt.nearest(query);
        System.out.println(nearest.toString());
        */
        kdt.insert(new Point2D(0.7, 0.2));
        kdt.insert(new Point2D(0.5, 0.4));
        kdt.insert(new Point2D(0.2, 0.3));
        kdt.insert(new Point2D(0.2, 0.35));
        kdt.insert(new Point2D(0.4, 0.7));
        kdt.insert(new Point2D(0.9, 0.6));
        kdt.draw();
        System.out.println(kdt.contains(new Point2D(0.7, 0.2)));
        System.out.println(kdt.contains(new Point2D(0.5, 0.4)));
        System.out.println(kdt.contains(new Point2D(0.2, 0.3)));
        System.out.println(kdt.contains(new Point2D(0.2, 0.35)));
        System.out.println(kdt.contains(new Point2D(0.4, 0.7)));
        System.out.println(kdt.contains(new Point2D(0.9, 0.6)));
    }
}
