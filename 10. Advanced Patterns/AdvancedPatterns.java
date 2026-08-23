public class AdvancedPatterns {
    public static void main(String[] args) {
        int n = 4;

        // ===================== HOLLOW RECTANGLE =====================
        for (int i = 1; i <= n; i++) {          // loop through rows
            for (int j = 1; j <= n; j++) {      // loop through columns
                if (i == 1 || i == n ||          // first or last row → print star
                    j == 1 || j == n) {          // first or last col → print star
                    System.out.print("* ");
                } else {
                    System.out.print("  ");      // inside → print space
                }
            }
            System.out.println();                // move to next row
        }

        // ===================== INVERTED & ROTATED HALF PYRAMID =====================
        // for (int i = 1; i <= n; i++) {              // loop rows
        //     for (int j = 1; j <= n - i; j++) {      // print leading spaces
        //         System.out.print("  ");
        //     }
        //     for (int j = 1; j <= i; j++) {          // print stars (increases each row)
        //         System.out.print("* ");
        //     }
        //     System.out.println();
        // }

        // ===================== INVERTED HALF PYRAMID WITH NUMBERS =====================
        // for (int i = n; i >= 1; i--) {          // start from n, go down to 1
        //     for (int j = 1; j <= i; j++) {      // print j numbers per row
        //         System.out.print(j + " ");      // print column number
        //     }
        //     System.out.println();
        // }

        // ===================== FLOYD'S TRIANGLE =====================
        // int num = 1;                            // continuous counter across all rows
        // for (int i = 1; i <= n; i++) {          // loop rows
        //     for (int j = 1; j <= i; j++) {      // print i numbers per row
        //         System.out.print(num + " ");    // print current number
        //         num++;                          // increment continuously
        //     }
        //     System.out.println();
        // }

        // ===================== 0-1 TRIANGLE =====================
        // for (int i = 1; i <= n; i++) {              // loop rows
        //     for (int j = 1; j <= i; j++) {          // loop columns
        //         if ((i + j) % 2 == 0) {             // even sum → print 1
        //             System.out.print("1 ");
        //         } else {                            // odd sum → print 0
        //             System.out.print("0 ");
        //         }
        //     }
        //     System.out.println();
        // }

        // ===================== BUTTERFLY PATTERN =====================
        // // upper half
        // for (int i = 1; i <= n; i++) {              // rows increase
        //     for (int j = 1; j <= i; j++) {          // left stars increase
        //         System.out.print("*");
        //     }
        //     for (int j = 1; j <= 2*(n-i); j++) {    // middle spaces decrease
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= i; j++) {          // right stars increase
        //         System.out.print("*");
        //     }
        //     System.out.println();
        // }
        // // lower half
        // for (int i = n; i >= 1; i--) {              // rows decrease
        //     for (int j = 1; j <= i; j++) {          // left stars decrease
        //         System.out.print("*");
        //     }
        //     for (int j = 1; j <= 2*(n-i); j++) {    // middle spaces increase
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= i; j++) {          // right stars decrease
        //         System.out.print("*");
        //     }
        //     System.out.println();
        // }

        // ===================== SOLID RHOMBUS =====================
        // for (int i = 1; i <= n; i++) {              // loop rows
        //     for (int j = 1; j <= n - i; j++) {      // leading spaces (decreases each row)
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= n; j++) {          // print n stars
        //         System.out.print("* ");
        //     }
        //     System.out.println();
        // }

        // ===================== HOLLOW RHOMBUS =====================
        // for (int i = 1; i <= n; i++) {              // loop rows
        //     for (int j = 1; j <= n - i; j++) {      // leading spaces
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= n; j++) {          // print star or space
        //         if (i == 1 || i == n ||             // first/last row → star
        //             j == 1 || j == n) {             // first/last col → star
        //             System.out.print("* ");
        //         } else {
        //             System.out.print("  ");         // inside → space
        //         }
        //     }
        //     System.out.println();
        // }

        // ===================== DIAMOND PATTERN =====================
        // // upper half
        // for (int i = 1; i <= n; i++) {              // rows increase
        //     for (int j = 1; j <= n - i; j++) {      // spaces decrease
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= 2*i - 1; j++) {    // stars = 2*row-1 (odd numbers)
        //         System.out.print("*");
        //     }
        //     System.out.println();
        // }
        // // lower half
        // for (int i = n - 1; i >= 1; i--) {          // rows decrease
        //     for (int j = 1; j <= n - i; j++) {      // spaces increase
        //         System.out.print(" ");
        //     }
        //     for (int j = 1; j <= 2*i - 1; j++) {    // stars decrease
        //         System.out.print("*");
        //     }
        //     System.out.println();
        // }
    }
}