public class AdvancedPatterns {

    // ================================================================
    // GROUP 1: BORDER-CONDITION PATTERNS
    // Print a star only on the edges; blank inside. Same skeleton --
    // the only difference is whether leading spaces slant the shape.
    // ================================================================

    /*
     * Hollow Rectangle
     *
     * Skeleton: full n x n grid, star only if the cell is on a border.
     * Border test: first row, last row, first col, or last col.
     */
    static void hollowRectangle(int n) {
        for (int i = 1; i <= n; i++) {           // rows
            for (int j = 1; j <= n; j++) {       // columns
                if (i == 1 || i == n || j == 1 || j == n) {
                    System.out.print("* ");
                } else {
                    System.out.print("  ");      // inside stays blank
                }
            }
            System.out.println();
        }
    }

    /*
     * Hollow Rhombus
     *
     * Skeleton: identical border test to Hollow Rectangle, plus
     * leading spaces that decrease each row to slant the shape.
     */
    static void hollowRhombus(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n - i; j++) {   // leading spaces (slant)
                System.out.print(" ");
            }
            for (int j = 1; j <= n; j++) {
                if (i == 1 || i == n || j == 1 || j == n) {
                    System.out.print("* ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    // ================================================================
    // GROUP 2: MIRRORED-HALF PATTERNS
    // Build an upper half with a growing count, then a lower half with
    // a shrinking count. The per-row formula is the thing to remember.
    // ================================================================

    /*
     * Butterfly Pattern
     *
     * Per row i: left stars = i, middle spaces = 2*(n-i), right stars = i.
     * Upper half runs i = 1..n, lower half runs i = n..1.
     */
    static void butterfly(int n) {
        for (int i = 1; i <= n; i++) {           // upper half
            printStars(i);
            printSpaces(2 * (n - i));
            printStars(i);
            System.out.println();
        }
        for (int i = n; i >= 1; i--) {           // lower half
            printStars(i);
            printSpaces(2 * (n - i));
            printStars(i);
            System.out.println();
        }
    }

    /*
     * Diamond Pattern
     *
     * Per row i: leading spaces = n-i, stars = 2*i - 1 (odd numbers).
     * Lower half starts at n-1 so the widest row isn't printed twice.
     */
    static void diamond(int n) {
        for (int i = 1; i <= n; i++) {           // upper half
            printSpaces(n - i);
            printStars(2 * i - 1);
            System.out.println();
        }
        for (int i = n - 1; i >= 1; i--) {       // lower half
            printSpaces(n - i);
            printStars(2 * i - 1);
            System.out.println();
        }
    }

    // ================================================================
    // GROUP 3: COUNTER / VALUE-DRIVEN PATTERNS
    // The shape is a plain triangle; what changes is *what* prints.
    // ================================================================

    /*
     * Floyd's Triangle
     *
     * A single counter keeps incrementing across every row, so numbers
     * run 1, 2, 3, ... continuously instead of restarting each row.
     */
    static void floydsTriangle(int n) {
        int num = 1;                             // continuous across rows
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(num + " ");
                num++;
            }
            System.out.println();
        }
    }

    /*
     * 0-1 Triangle
     *
     * Prints 1 when (row + col) is even, 0 when odd, giving an
     * alternating checkerboard down the triangle.
     */
    static void zeroOneTriangle(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                if ((i + j) % 2 == 0) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    /*
     * Inverted Half Pyramid with Numbers
     *
     * Rows shrink from n down to 1; each row prints 1..i.
     */
    static void invertedHalfPyramidNumbers(int n) {
        for (int i = n; i >= 1; i--) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    // ================================================================
    // GROUP 4: SPACE-SHIFTED TRIANGLES
    // Leading spaces push each row right; the star count does the rest.
    // ================================================================

    /*
     * Inverted & Rotated Half Pyramid
     *
     * Per row i: leading spaces = n-i, stars = i. The shrinking space
     * count is what rotates the triangle to face the other way.
     */
    static void invertedRotatedHalfPyramid(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n - i; j++) {   // leading spaces
                System.out.print("  ");
            }
            for (int j = 1; j <= i; j++) {       // stars increase each row
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    /*
     * Solid Rhombus
     *
     * Per row i: leading spaces = n-i, then a full row of n stars.
     * The constant star count is what makes it a rhombus, not a triangle.
     */
    static void solidRhombus(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n - i; j++) {   // leading spaces
                System.out.print(" ");
            }
            for (int j = 1; j <= n; j++) {       // always n stars
                System.out.print("* ");
            }
            System.out.println();
        }
    }

    // ================================================================
    // HELPERS
    // ================================================================

    private static void printStars(int count) {
        for (int i = 1; i <= count; i++) {
            System.out.print("*");
        }
    }

    private static void printSpaces(int count) {
        for (int i = 1; i <= count; i++) {
            System.out.print(" ");
        }
    }

    public static void main(String[] args) {
        int n = 4;

        System.out.println("=== Hollow Rectangle ===");
        hollowRectangle(n);

        System.out.println("\n=== Hollow Rhombus ===");
        hollowRhombus(n);

        System.out.println("\n=== Butterfly ===");
        butterfly(n);

        System.out.println("\n=== Diamond ===");
        diamond(n);

        System.out.println("\n=== Floyd's Triangle ===");
        floydsTriangle(n);

        System.out.println("\n=== 0-1 Triangle ===");
        zeroOneTriangle(n);

        System.out.println("\n=== Inverted Half Pyramid (Numbers) ===");
        invertedHalfPyramidNumbers(n);

        System.out.println("\n=== Inverted & Rotated Half Pyramid ===");
        invertedRotatedHalfPyramid(n);

        System.out.println("\n=== Solid Rhombus ===");
        solidRhombus(n);
    }
}
