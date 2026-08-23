// ================================================================
// TOPIC: Backtracking
// Core idea: try a choice → recurse → UNDO the choice (backtrack).
// This explores all possibilities without getting stuck in dead ends.
// ================================================================

public class code {

    // ================================================================
    // 1. BACKTRACKING ON ARRAYS
    // Change array elements forward (recurse), then undo on the way back.
    // This shows the "try → recurse → undo" pattern clearly.
    // ================================================================
    static void changeArray(int[] arr, int i) {
        if (i == arr.length) {
            // base case: printed the fully changed array
            printArr(arr);
            return;
        }
        arr[i] += 2;             // try: change element
        changeArray(arr, i + 1); // recurse on next index
        arr[i] -= 2;             // undo: restore original value (backtrack)
    }

    static void printArr(int[] arr) {
        for (int x : arr) System.out.print(x + " ");
        System.out.println();
    }


    // ================================================================
    // 2. FIND ALL SUBSETS
    // At each index, we have 2 choices: include the element OR skip it.
    // We explore both branches, building the subset as we go.
    // Total subsets = 2^n (each element independently included or not).
    // Time: O(2^n)  Space: O(n) call stack
    // ================================================================
    static void findSubsets(int[] arr, int i, String current) {
        if (i == arr.length) {
            System.out.println(current);   // print completed subset
            return;
        }
        // choice 1: INCLUDE arr[i] in the subset
        findSubsets(arr, i + 1, current + arr[i]);

        // choice 2: SKIP arr[i] (don't include)
        findSubsets(arr, i + 1, current);
    }


    // ================================================================
    // 3. FIND ALL PERMUTATIONS
    // A permutation uses every element exactly once, in different orders.
    // Strategy: maintain a boolean[] used array.
    //   - Loop through all elements.
    //   - If not used yet → add to current string, mark used, recurse.
    //   - After recursion → UNMARK (backtrack) so other paths can use it.
    // Total permutations = n!
    // Time: O(n * n!)  Space: O(n)
    // ================================================================
    static void findPermutations(int[] arr, boolean[] used, String current) {
        if (current.length() == arr.length) {
            System.out.println(current);   // all elements used → one permutation done
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            if (!used[i]) {
                used[i] = true;                                      // mark as used
                findPermutations(arr, used, current + arr[i]);       // recurse
                used[i] = false;                                     // UNDO (backtrack)
            }
        }
    }


    // ================================================================
    // 4. N-QUEENS — Place N queens on an N×N board so none attack each other.
    //
    // Queens attack: same row, same column, same diagonal.
    // Strategy: place queens row by row. For each row, try every column.
    //   - Check if the column/diagonal is safe → place queen → recurse next row.
    //   - After recursion → REMOVE queen (backtrack).
    //
    // isSafe checks: no queen in same column above, no queen on upper diagonals.
    // (No need to check current or lower rows — we haven't placed there yet.)
    // Time: O(n!)  Space: O(n²)
    // ================================================================

    // Check if placing a queen at (row, col) is safe
    static boolean isSafe(char[][] board, int row, int col) {
        int n = board.length;

        // check column above (rows 0 to row-1, same col)
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') return false;
        }

        // check upper-left diagonal
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') return false;
        }

        // check upper-right diagonal
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (board[i][j] == 'Q') return false;
        }

        return true;   // safe to place
    }

    // Count all ways to place N queens
    static int nQueensCount(char[][] board, int row) {
        if (row == board.length) return 1;   // all queens placed → 1 valid solution

        int count = 0;
        for (int col = 0; col < board.length; col++) {
            if (isSafe(board, row, col)) {
                board[row][col] = 'Q';               // place queen
                count += nQueensCount(board, row + 1); // recurse next row
                board[row][col] = '.';               // remove queen (backtrack)
            }
        }
        return count;
    }

    // Print ONE valid solution (stop after first found)
    static boolean nQueensPrintOne(char[][] board, int row) {
        if (row == board.length) {
            printBoard(board);   // found a valid solution — print it
            return true;         // return true to stop searching
        }
        for (int col = 0; col < board.length; col++) {
            if (isSafe(board, row, col)) {
                board[row][col] = 'Q';
                if (nQueensPrintOne(board, row + 1)) return true;   // stop if found
                board[row][col] = '.';   // backtrack
            }
        }
        return false;
    }

    static void printBoard(char[][] board) {
        for (char[] row : board) {
            System.out.println(new String(row));
        }
        System.out.println();
    }


    // ================================================================
    // 5. GRID WAYS — Count paths from top-left to bottom-right
    // Can only move RIGHT (R) or DOWN (D).
    //
    // At each cell: try moving right → recurse, try moving down → recurse.
    // Base case: reached bottom-right corner → 1 valid path found.
    // Out of bounds → return 0 (dead end).
    //
    // Total paths = C(m+n-2, m-1) — but backtracking finds all of them.
    // Time: O(2^(m+n))  Space: O(m+n) call stack
    // ================================================================
    static int gridWays(int row, int col, int m, int n) {
        if (row == m - 1 && col == n - 1) return 1;   // reached destination
        if (row >= m || col >= n) return 0;             // out of bounds

        // move right + move down (explore both directions)
        return gridWays(row, col + 1, m, n)   // go right
             + gridWays(row + 1, col, m, n);  // go down
    }

    // Grid Ways Trick: mathematical formula avoids recursion entirely.
    // Total paths in m×n grid = (m+n-2)! / ((m-1)! * (n-1)!)
    // (choosing which of the (m+n-2) steps are "down" moves)
    static long gridWaysTrick(int m, int n) {
        // compute C(m+n-2, m-1)
        int total = m + n - 2;
        int choose = m - 1;
        long result = 1;
        for (int i = 0; i < choose; i++) {
            result = result * (total - i) / (i + 1);
        }
        return result;
    }


    // ================================================================
    // 6. SUDOKU SOLVER
    // Fill a 9×9 grid so every row, column, and 3×3 box has digits 1-9
    // exactly once. Empty cells are represented by '.'.
    //
    // Strategy: find next empty cell → try digits 1-9 → check if valid
    //   → place digit → recurse → if recursion fails → REMOVE digit (backtrack).
    //
    // Time: O(9^(empty cells))  — in worst case 9^81, but constraints prune heavily.
    // ================================================================
    static boolean solveSudoku(char[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == '.') {          // found an empty cell
                    for (char c = '1'; c <= '9'; c++) {
                        if (isValidSudoku(board, row, col, c)) {
                            board[row][col] = c;               // place digit
                            if (solveSudoku(board)) return true; // recurse
                            board[row][col] = '.';             // backtrack (undo)
                        }
                    }
                    return false;   // no digit worked → backtrack to previous cell
                }
            }
        }
        return true;   // no empty cell found → board is solved
    }

    // Check if placing 'c' at (row, col) is valid for Sudoku rules
    static boolean isValidSudoku(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == c) return false;   // same digit in this row
            if (board[i][col] == c) return false;   // same digit in this column
            // check the 3×3 box: map (row,col) to top-left of its box
            int boxRow = 3 * (row / 3) + i / 3;
            int boxCol = 3 * (col / 3) + i % 3;
            if (board[boxRow][boxCol] == c) return false;
        }
        return true;
    }

    static void printSudoku(char[][] board) {
        for (char[] row : board) {
            System.out.println(new String(row));
        }
    }


    // ================================================================
    // MAIN
    // ================================================================
    public static void main(String[] args) {

        // ----- 1. Backtracking on Arrays -----
        System.out.println("===== Backtracking on Array =====");
        int[] arr = {1, 2, 3};
        System.out.print("Original: ");
        printArr(arr);
        changeArray(arr, 0);
        System.out.print("After backtrack (restored): ");
        printArr(arr);   // back to {1,2,3}
        System.out.println();


        // ----- 2. Find Subsets -----
        System.out.println("===== Subsets of [1,2,3] =====");
        findSubsets(new int[]{1, 2, 3}, 0, "");
        // prints: 123, 12, 13, 1, 23, 2, 3, "" (8 = 2^3 subsets)
        System.out.println();


        // ----- 3. Find Permutations -----
        System.out.println("===== Permutations of [1,2,3] =====");
        int[] pArr = {1, 2, 3};
        findPermutations(pArr, new boolean[pArr.length], "");
        // prints all 6 = 3! permutations
        System.out.println();


        // ----- 4. N-Queens -----
        System.out.println("===== 4-Queens: count ways =====");
        char[][] board4 = new char[4][4];
        for (char[] row : board4) java.util.Arrays.fill(row, '.');
        System.out.println("Ways: " + nQueensCount(board4, 0));   // 2
        System.out.println();

        System.out.println("===== 4-Queens: one solution =====");
        char[][] board4b = new char[4][4];
        for (char[] row : board4b) java.util.Arrays.fill(row, '.');
        nQueensPrintOne(board4b, 0);

        System.out.println("===== 8-Queens: count ways =====");
        char[][] board8 = new char[8][8];
        for (char[] row : board8) java.util.Arrays.fill(row, '.');
        System.out.println("Ways: " + nQueensCount(board8, 0));   // 92
        System.out.println();


        // ----- 5. Grid Ways -----
        System.out.println("===== Grid Ways (3x3) =====");
        System.out.println("Recursive: " + gridWays(0, 0, 3, 3));     // 6
        System.out.println("Math trick: " + gridWaysTrick(3, 3));      // 6
        System.out.println();


        // ----- 6. Sudoku -----
        System.out.println("===== Sudoku Solver =====");
        char[][] sudoku = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        solveSudoku(sudoku);
        printSudoku(sudoku);
    }
}
