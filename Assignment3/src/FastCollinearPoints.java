/******************************************************************************
 *  Compilation:  javac
 *  Execution:    java
 *  Dependencies: none
 *
 *
 ******************************************************************************/

// import javax.sound.sampled.Line;
import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private int size;
    //private final LineSegment[] lines;
    private final ArrayList<LineSegment> lines;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        // size = 0;
        // lines = new LineSegment[points.length];
        lines = new ArrayList<>(points.length);
        if (points.length < 4) {
            return;
        }

        // make a copy of points, cuz we need to sorting it again and again
        Point[] sortpoints = new Point[points.length];
        // System.arraycopy(points, 0, sortpoints, 0, points.length);
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.IllegalArgumentException();
            }
            sortpoints[i] = points[i];
        }
        Arrays.sort(sortpoints);
        for (int i = 1; i < sortpoints.length; i++) {
            if (sortpoints[i - 1].compareTo(sortpoints[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        // check for each point
        // for (int i = 0; i < points.length; i++) {
        for (Point org : points) {
            // sort the array sortpoints, according to the slope to point[i]
            Arrays.sort(sortpoints, org.slopeOrder());
            // slope reference
            double slope = 0.0; // = org.slopeTo(sortpoints[1]);
            // count indicates the number of points in the line
            int count = 2;
            // record the other end of the lineSegment
            Point top = org, bottom = org; // = sortpoints[1];
            slope = org.slopeTo(sortpoints[1]);
            top = higherPoint(org, sortpoints[1]);
            bottom = lowerPoint(org, sortpoints[1]);
            // start from the second point
            for (int j = 2; j < points.length; j++) {
                /* if (sortpoints[0].compareTo(sortpoints[j]) > 0) {
                    continue;
                } */
                Point current = sortpoints[j];
                // found a point on the line
                // if (Math.abs(slope - org.slopeTo(current)) < ESPILON) {
                if (slope == org.slopeTo(current)) {
                    count += 1;
                    top = higherPoint(top, current);
                    bottom = lowerPoint(bottom, current);
                    if (j == points.length - 1 && count > 3 && bottom == org) {
                        // lines[size++] = new LineSegment(bottom, top); // add to the result;
                        size++;
                        lines.add(new LineSegment(bottom, top));
                    }
                }
                else { // not on the line
                    if (count > 3 && bottom == org) { // already found a set contains more than or equal to 4 points
                        //lines[size++] = new LineSegment(bottom, top); // add to the result;
                        size++;
                        lines.add(new LineSegment(bottom, top)); // add to the result;
                    }
                    // update all the flags
                    slope = org.slopeTo(current);
                    count = 2;
                    top = higherPoint(org, current);
                    bottom = lowerPoint(org, current);
                }
            }
        }
    }

    private Point higherPoint(Point p1, Point p2) {
        int cmp = p1.compareTo(p2);
        if (cmp >= 0) {
            return p1;
        }
        else {
            return p2;
        }
    }

    private Point lowerPoint(Point p1, Point p2) {
        int cmp = p1.compareTo(p2);
        if (cmp <= 0) {
            return p1;
        }
        else {
            return p2;
        }
    }

    public int numberOfSegments() {
        return size;
    }
    public LineSegment[] segments() {
        LineSegment[] results = new LineSegment[size];
        // System.arraycopy(lines, 0, results, 0, size);
        for (int i = 0; i < size; i++) {
            results[i] = lines.get(i);
        }
        return results;
    }
}
