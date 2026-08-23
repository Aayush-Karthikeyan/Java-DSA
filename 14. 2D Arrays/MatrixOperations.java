import java.util.Scanner;

public class MatrixOperations {

    // ===== 1. Linear Search in 2D Array =====
    public static boolean search(int matrix[][], int key) {
        int n = matrix.length, m = matrix[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == key) {
                    System.out.println("found at cell (" + i + "," + j + ")");
                    return true;
                }
            }
        }
        System.out.println("key not found");
        return false;
    }

    // ===== 2. Print Matrix in Spiral Order =====
    public static void printSpiral(int matrix[][]) {
        int startRow = 0;
        int startCol = 0;
        int endRow = matrix.length - 1;
        int endCol = matrix[0].length - 1;

        while (startRow <= endRow && startCol <= endCol) {
            // top
            for (int j = startCol; j <= endCol; j++) {
                System.out.print(matrix[startRow][j] + " ");
            }
            // right
            for (int i = startRow + 1; i <= endRow; i++) {
                System.out.print(matrix[i][endCol] + " ");
            }
            // bottom
            for (int j = endCol - 1; j >= startCol; j--) {
                System.out.print(matrix[endRow][j] + " ");
            }
            // left
            for (int i = endRow - 1; i >= startRow + 1; i--) {
                System.out.print(matrix[i][startCol] + " ");
            }

            startCol++;
            startRow++;
            endCol--;
            endRow--;
        }
    }

    // ===== 3. Sum of Both Diagonals =====
    public static int diagonalSum(int matrix[][]) {
        int sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            // pd (primary diagonal)
            sum += matrix[i][i];
            // sd (secondary diagonal) - skip if same cell as pd (center of odd matrix)
            if (i != matrix.length - 1 - i) {
                sum += matrix[i][matrix.length - 1 - i];
            }
        }
        return sum;
    }

    // ===== 4. Search in Sorted Matrix (Staircase Search) =====
    public static boolean staircaseSearch(int matrix[][], int key) {
        int row = 0, col = matrix[0].length - 1;
        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == key) {
                System.out.println("found key at (" + row + "," + col + ")");
                return true;
            } else if (key < matrix[row][col]) {
                col--;
            } else {
                row++;
            }
        }
        System.out.println("key not found!");
        return false;
    }

    public static void main(String args[]) {

        // ----- Test 1: Linear Search (you provide 9 numbers as input) -----
        System.out.println("Test 1: Linear Search");
        int matrix1[][] = new int[3][3];
        int n = matrix1.length, m = matrix1[0].length;
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix1[i][j] = sc.nextInt();
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix1[i][j] + " ");
            }
            System.out.println();
        }
        search(matrix1, 5);
        System.out.println();

        // ----- Test 2: Spiral Print -----
        System.out.println("Test 2: Spiral Order");
        int matrix2[][] = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        printSpiral(matrix2);
        System.out.println();

        // ----- Test 3: Diagonal Sum -----
        System.out.println("Test 3: Diagonal Sum");
        System.out.println(diagonalSum(matrix2));
        System.out.println();

        // ----- Test 4: Staircase Search -----
        System.out.println("Test 4: Staircase Search");
        int matrix4[][] = {{10, 20, 30, 40},
                            {15, 25, 35, 45},
                            {27, 29, 37, 48},
                            {32, 33, 39, 50}};
        staircaseSearch(matrix4, 33);

        sc.close();
    }
}