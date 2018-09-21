/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: none
 *
 *
 ******************************************************************************/

import java.util.Arrays;
public class BruteCollinearPoints {
    private int size;
    private final LineSegment[] lines;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.IllegalArgumentException();
            }
            pointsCopy[i] = points[i];
        }

        // size = 0;
        int num = points.length;
        lines = new LineSegment[num];

        // since add the lineSegment as from a to d, so a must be the smallest point
        // wont look back
        Arrays.sort(pointsCopy);
        for (int i = 1; i < pointsCopy.length; i++) {
            if (pointsCopy[i - 1].compareTo(pointsCopy[i]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        for (int a = 0; a < num; a++) {
            for (int b = a + 1; b < num; b++) {
                for (int c = b + 1; c < num; c++) {
                    double slopeAB = pointsCopy[a].slopeTo(pointsCopy[b]);
                    double slopeAC = pointsCopy[a].slopeTo(pointsCopy[c]);
                    // if (Math.abs(slopeAB - slopeAC) >= ESPILON) {
                    if (slopeAB != slopeAC) {
                        continue;
                    }
                    for (int d = c + 1; d < num; d++) {
                        double slopeAD = pointsCopy[a].slopeTo(pointsCopy[d]);

                        if (slopeAB == slopeAD) {
                        // if (Math.abs(slopeAB - slopeAD) < ESPILON) {
                            lines[size++] = new LineSegment(pointsCopy[a], pointsCopy[d]);
                        }
                    }
                }
            }
        }
    }
    public int numberOfSegments() {
        return size;
    }
    public LineSegment[] segments() {
        LineSegment[] results = new LineSegment[size];
        System.arraycopy(lines, 0, results, 0, size);
        return results;
    }

}
