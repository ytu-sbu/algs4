import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private int[][] pic;
    private double[][] energy;
    private boolean dataDirection;
    private boolean picDirection;

    public SeamCarver(Picture picture) {

        validateInput(picture);
        int w = picture.width();
        int h = picture.height();
        dataDirection = VERTICAL;
        picDirection = VERTICAL;

        // h rows, w columns
        pic = new int[h][w];
        energy = new double[h][w];

        // convert picture to rgb array
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                pic[i][j] = picture.getRGB(j, i);
            }
        }

        // initialize energy array
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                energy[i][j] = energy(j, i);
            }
        }
        // printEnergy();
    }

    /*
    private void printEnergy() {
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                System.out.print(energy[y][x] + "   ");
            }
            System.out.println();
        }
    }
    */

    public Picture picture() {
        /*
        if (picDirection == HORIZONTAL) {
            pic = transpose(pic);
            picDirection = VERTICAL;
        }
        */

        int w = width();
        int h = height();

        Picture picture = new Picture(w, h);
        if (picDirection == HORIZONTAL) {
            for (int j = 0; j < w; j++) {
                for (int i = 0; i < h; i++) {
                    picture.setRGB(j, i, pic[j][i]);
                }
            }
        }
        else {
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    picture.setRGB(j, i, pic[i][j]);
                }
            }
        }
        return picture;
    }

    // interface width
    public int width() {
        if (picDirection == VERTICAL) {
            return pic[0].length;
        }
        return pic.length;
    }

    // interface height
    public int height() {
        if (picDirection == VERTICAL) {
            return pic.length;
        }
        return pic[0].length;
    }

    // real data width
    private int dataWidth() {
        return energy[0].length;
    }

    // real data height
    private int dataHeight() {
        return energy.length;
    }

    private void validateInput(Object input) {
        if (input == null) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void validHorizontalPos(int x) {
        if (x < 0 || x >= dataWidth()) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    /*
    private void validVerticalPos(int y) {
        if (y < 0 || y >= dataHeight()) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    */
    private int sq(int n) {
        return n * n;
    }

    // interface x, y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new java.lang.IllegalArgumentException();
        }

        if (picDirection == VERTICAL) {
            return pEnergy(x, y);
        }
        // picDirection == HORIZONTAL
        return pEnergy(y, x);
    }

    private double pEnergy(int x, int y) {

        if (y == 0 || y == pic.length - 1 || x == 0 || x == pic[0].length - 1) {
            return 1000.0;
        }

        int left = pic[y][x - 1];
        int r = pic[y][x + 1];
        int t = pic[y - 1][x];
        int b = pic[y + 1][x];

        int sqx = rgb2deltaSquare(r, left);
        int sqy = rgb2deltaSquare(b, t);
        return Math.sqrt(sqx + sqy);
    }

    private int rgb2deltaSquare(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1 >> 0) & 0xFF;

        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2 >> 0) & 0xFF;

        return sq(r1 - r2) + sq(g1 - g2) + sq(b1 - b2);
    }

    private static int[][] transpose(int[][] array) {
        int L1 = array.length;
        int L2 = array[0].length;

        int[][] newArray = new int[L2][L1];
        for (int i = 0; i < L2; i++) {
            for (int j = 0; j < L1; j++) {
                newArray[i][j] = array[j][i];
            }
        }
        return newArray;
    }

    private static double[][] transpose(double[][] array) {
        int L1 = array.length;
        int L2 = array[0].length;

        double[][] newArray = new double[L2][L1];
        for (int i = 0; i < L2; i++) {
            for (int j = 0; j < L1; j++) {
                newArray[i][j] = array[j][i];
            }
        }
        return newArray;
    }

    /*
    private void transposeData() {
        energy = transpose(energy);
        pic = transpose(pic);
//        distTo = transpose(distTo);
//        edgeTo = transpose(edgeTo);
    }
    */

    public int[] findHorizontalSeam() {
        if (dataDirection == VERTICAL) {
            energy = transpose(energy);
            dataDirection = HORIZONTAL;
        }
        return findSeam();
    }

    public int[] findVerticalSeam() {
        if (dataDirection == HORIZONTAL) {
            energy = transpose(energy);
            dataDirection = VERTICAL;
        }
        return findSeam();
    }

    private int[] findSeam() {

        int w = dataWidth();
        int h = dataHeight();

        double[][] distTo = new double[h][w];
        int[][] edgeTo = new int[h][w];

        int[] seam = new int[h];

        // set up the first row
        for (int i = 0; i < w; i++) {
            distTo[0][i] = 1000;
        }

        for (int i = 0; i < h - 1; i++) {
            for (int j = 0; j < w; j++) {
                // bottom three pixels
                int nextRow = i + 1;
                for (int k = j - 1; k < j + 2; k++) {
                    if (k < 0 || k >= w) {
                        continue;
                    }
                    double dis = distTo[i][j] + energy[nextRow][k];
                    if (distTo[nextRow][k] == 0.0 || dis < distTo[nextRow][k]) {
                        distTo[nextRow][k] = dis;
                        // top row, jth column
                        edgeTo[nextRow][k] = j;
                    }
                }
            }
        }
        // find the ending point
        double min = Double.POSITIVE_INFINITY;
        int indexMin = Integer.MAX_VALUE;
        for (int j = 0; j < w; j++) {
            double dis = distTo[h - 1][j];
            if (dis < min) {
                min = dis;
                indexMin = j;
            }
        }
        seam[h - 1] = indexMin;
        for (int row = h - 2; row >= 0; row--) {
            int botRow = row + 1;
            int botCol = seam[botRow];
            int col = edgeTo[botRow][botCol];
            seam[row] = col;
        }
        return seam;
    }

    private void errorIfShortLength(int leng) {
        if (leng < 2) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void validateVSeam(int[] seam) {
        if (seam.length != dataHeight()) {
            throw new java.lang.IllegalArgumentException();
        }
        int pre = seam[0];
        int cur;
        for (int i = 0; i < seam.length; i++) {
            cur = seam[i];
            validHorizontalPos(cur);
            if (Math.abs(pre - cur) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
            pre = cur;
        }
    }
/*
    private void validateHSeam(int[] seam) {
        if (seam.length != width()) {
            throw new java.lang.IllegalArgumentException();
        }
        int pre = seam[0];
        int cur;
        for (int i = 0; i < seam.length; i++) {
            cur = seam[i];
            validVerticalPos(cur);
            if (Math.abs(pre - cur) > 1) {
                throw new java.lang.IllegalArgumentException();
            }
            pre = cur;
        }
    }
*/
    public void removeHorizontalSeam(int[] seam) {
//        validateInput(seam);
//        errorIfShortLength(width());
//        validateHSeam(seam);

        if (dataDirection == VERTICAL) {
            energy = transpose(energy);
            dataDirection = HORIZONTAL;
        }

        if (picDirection == VERTICAL) {
            pic = transpose(pic);
            picDirection = HORIZONTAL;
        }

//        System.out.println(width() + " pic width");
//        System.out.println(height() + " pic height");
//        System.out.println("rm H seal");

        removeSeam(seam);


//        System.out.println(width() + " pic width");
//        System.out.println(height() + " pic height");
    }

    public void removeVerticalSeam(int[] seam) {
        if (dataDirection == HORIZONTAL) {
            energy = transpose(energy);
            dataDirection = VERTICAL;
        }
        if (picDirection == HORIZONTAL) {
            pic = transpose(pic);
            picDirection = VERTICAL;
        }
        removeSeam(seam);
    }

    private void removeSeam(int[] seam) {
        validateInput(seam);
        errorIfShortLength(dataWidth());
        validateVSeam(seam);

        int w = dataWidth();
        int h = dataHeight();
//        System.out.println("data: h " + dataHeight() + " w " + dataWidth());
//        System.out.println("pic: h " + height() + " w " + width());

        int[][] newPic = new int[h][w - 1];
        double[][] newEngy = new double[h][w - 1];

        for (int i = 0; i < h; i++) {
            int breakPoint = seam[i];
            System.arraycopy(pic[i], 0, newPic[i], 0, breakPoint);
            System.arraycopy(pic[i], breakPoint + 1, newPic[i], breakPoint, w - breakPoint - 1);

            // shift and fix energy array
            if (breakPoint == 0) {
//                newEngy[i][0] = pEnergy(0, i);
                System.arraycopy(energy[i], 2, newEngy[i], 1, w - 2);
            }
            else if (breakPoint == w - 1) {
                System.arraycopy(energy[i], 0, newEngy[i], 0, w - 2);
//                newEngy[i][w - 2] = pEnergy(w - 2, i);
            }
            else {
                System.arraycopy(energy[i], 0, newEngy[i], 0, breakPoint - 1);
                System.arraycopy(energy[i], breakPoint + 2, newEngy[i], breakPoint + 1, w - breakPoint - 2);
//                newEngy[i][breakPoint - 1] = energy(breakPoint - 1, i);
//                newEngy[i][breakPoint] = energy(breakPoint, i);
            }
        }
        pic = newPic;
        energy = newEngy;
        for (int i = 0; i < h; i++) {
            int bp = seam[i];
            if (bp == 0) {
                energy[i][0] = pEnergy(0, i);
            }
            else if (bp == w - 1) {
                energy[i][w - 2] = pEnergy(w - 2, i);
            }
            else {
                energy[i][bp - 1] = pEnergy(bp - 1, i);
                energy[i][bp] = pEnergy(bp, i);
            }
        }
    }

    public static void main(String[] args) {
        Picture p = new Picture(6, 6);
        int rgb = 0x040601;
        p.setRGB(0, 0, rgb);
        rgb = 0x30905;
        p.setRGB(1, 0, rgb);
        rgb = 0x80602;
        p.setRGB(2, 0, rgb);
        rgb = 0x20907;
        p.setRGB(3, 0, rgb);
        rgb = 0x80209;
        p.setRGB(4, 0, rgb);
        rgb = 0x30609;
        p.setRGB(5, 0, rgb);
        rgb = 0x10601;
        p.setRGB(0, 1, rgb);
        rgb = 0x70007;
        p.setRGB(1, 1, rgb);
        rgb = 0x20109;
        p.setRGB(2, 1, rgb);
        rgb = 0x70801;
        p.setRGB(3, 1, rgb);
        rgb = 0x20001;
        p.setRGB(4, 1, rgb);
        rgb = 0x502;
        p.setRGB(5, 1, rgb);
        rgb = 0x70909;
        p.setRGB(0, 2, rgb);
        rgb = 0x1;
        p.setRGB(1, 2, rgb);
        rgb = 0x90902;
        p.setRGB(2, 2, rgb);
        rgb = 0x60109;
        p.setRGB(3, 2, rgb);
        rgb = 0x80300;
        p.setRGB(4, 2, rgb);
        rgb = 0x40304;
        p.setRGB(5, 2, rgb);
        rgb = 0x50205;
        p.setRGB(0, 3, rgb);
        rgb = 0x30807;
        p.setRGB(1, 3, rgb);
        rgb = 0x90607;
        p.setRGB(2, 3, rgb);
        rgb = 0x60609;
        p.setRGB(3, 3, rgb);
        rgb = 0x60703;
        p.setRGB(4, 3, rgb);
        rgb = 0x90402;
        p.setRGB(5, 3, rgb);
        rgb = 0x90901;
        p.setRGB(0, 4, rgb);
        rgb = 0x10901;
        p.setRGB(1, 4, rgb);
        rgb = 0x70902;
        p.setRGB(2, 4, rgb);
        rgb = 0x90504;
        p.setRGB(3, 4, rgb);
        rgb = 0x90800;
        p.setRGB(4, 4, rgb);
        rgb = 0x60107;
        p.setRGB(5, 4, rgb);
        rgb = 0x60903;
        p.setRGB(0, 5, rgb);
        rgb = 0x20900;
        p.setRGB(1, 5, rgb);
        rgb = 0x306;
        p.setRGB(2, 5, rgb);
        rgb = 0x80405;
        p.setRGB(3, 5, rgb);
        rgb = 0x80005;
        p.setRGB(4, 5, rgb);
        rgb = 0x070109;
        p.setRGB(5, 5, rgb);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
//                StdOut.printf("%x ", p.getRGB(i, j));
                System.out.printf("%x ", p.getRGB(j, i));
            }
            System.out.println();
        }

        SeamCarver sc = new SeamCarver(p);
        System.out.println("the original e12 is: ");
        System.out.println(sc.energy(1, 2));
        sc.findVerticalSeam();
        System.out.println("after find V Seam, e12 is: ");
        System.out.println(sc.energy(1, 2));
        sc.findHorizontalSeam();
        System.out.println("after 1st find H Seam, e12 is: ");
        System.out.println(sc.energy(1, 2));
        sc.findHorizontalSeam();
        System.out.println("after 2nd find H Seam, e12 is: ");
        System.out.println(sc.energy(1, 2));
        sc.findHorizontalSeam();
//        sc.transposeData();
        System.out.println("after 3nd find H Seam followed by transpose, e12 is: ");
        System.out.println(sc.energy(1, 2));
    }
}
