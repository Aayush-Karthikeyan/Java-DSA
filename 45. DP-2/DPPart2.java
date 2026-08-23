public class DPPart2 {

    // ================================================================
    // 0-1 KNAPSACK
    // Each item can be taken at most once. Choose a subset of items
    // whose total weight fits within capacity W, maximizing total
    // value. Shown in three forms: recursion, memoization, tabulation.
    // ================================================================

    /*
     * Problem:
     * Given values val[i] and weights wt[i] for n items and a capacity
     * W, choose a subset (each item used 0 or 1 times) that fits
     * within W and maximizes total value.
     *
     * Pattern:
     * Knapsack-shaped DP (plain recursion) - at every item there's a
     * binary choice: include it or exclude it.
     *
     * Approach:
     * At item n (1-indexed from the end), if it fits (wt[n-1] <= W):
     *   take the better of including it (add its value, recurse with
     *   reduced capacity and one fewer item) or excluding it (recurse
     *   with the same capacity and one fewer item).
     * If it doesn't fit, it must be excluded.
     * Base case: no capacity left or no items left -> 0.
     *
     * Time: O(2^n) - two branches per item
     * Space: O(n) recursion stack
     */
    static int knapsackRecursive(int[] val, int[] wt, int W, int n) {
        if (W == 0 || n == 0) {
            return 0;
        }
        if (wt[n - 1] <= W) {
            int include = val[n - 1]
                    + knapsackRecursive(val, wt, W - wt[n - 1], n - 1);
            int exclude = knapsackRecursive(val, wt, W, n - 1);
            return Math.max(include, exclude);
        } else {
            return knapsackRecursive(val, wt, W, n - 1);
        }
    }

    /*
     * Problem:
     * Same as above, but cache each (item, capacity) result so
     * overlapping subproblems are computed once.
     *
     * Pattern:
     * Memoization (top-down DP) - the state is two-dimensional here
     * (items remaining, capacity remaining), unlike the Fibonacci
     * pattern in DP-1 which only needed one dimension.
     *
     * Approach:
     * Same recurrence as the plain recursion. Before recursing, check
     * memo[n][W]. A sentinel of -1 means "not yet computed."
     *
     * Time: O(n * W) - each (item, capacity) pair is computed once
     * Space: O(n * W) memo table + O(n) recursion stack
     */
    static int knapsackMemo(int[] val, int[] wt, int W, int n, int[][] memo) {
        if (W == 0 || n == 0) {
            return 0;
        }
        if (memo[n][W] != -1) {
            return memo[n][W];
        }
        if (wt[n - 1] <= W) {
            int include = val[n - 1]
                    + knapsackMemo(val, wt, W - wt[n - 1], n - 1, memo);
            int exclude = knapsackMemo(val, wt, W, n - 1, memo);
            memo[n][W] = Math.max(include, exclude);
        } else {
            memo[n][W] = knapsackMemo(val, wt, W, n - 1, memo);
        }
        return memo[n][W];
    }

    /*
     * Problem:
     * Same as above, built bottom-up with no recursion at all.
     *
     * Pattern:
     * Tabulation (bottom-up DP), 2D table.
     *
     * Approach:
     * dp[i][j] = best value using the first i items with capacity j.
     * dp[i][0] and dp[0][j] are 0 by default (Java zero-initializes
     * int arrays).
     * For each item i and capacity j:
     *   if it fits: dp[i][j] = max(val[i-1] + dp[i-1][j-wt[i-1]], dp[i-1][j])
     *   if it doesn't fit: dp[i][j] = dp[i-1][j]
     * Note the include case drops to row i-1 (this item is now used up)
     * - that's what makes this 0-1 instead of unbounded.
     *
     * Time: O(n * W)
     * Space: O(n * W) for the dp table
     */
    static int knapsackTabulation(int[] val, int[] wt, int W) {
        int n = val.length;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                if (wt[i - 1] <= j) {
                    dp[i][j] = Math.max(
                            val[i - 1] + dp[i - 1][j - wt[i - 1]],
                            dp[i - 1][j]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][W];
    }

    // ================================================================
    // TARGET SUM SUBSET  (variation of 0-1 Knapsack)
    // Instead of maximizing value, just decide: does any subset of
    // the numbers add up to exactly the target sum? Boolean DP over
    // the same include/exclude shape as 0-1 Knapsack.
    // ================================================================

    /*
     * Problem:
     * Given an array of numbers and a target sum, decide whether any
     * subset of the numbers adds up to exactly the target.
     *
     * Pattern:
     * 0-1 Knapsack shape, but boolean instead of max-value: "weight"
     * is the number itself, "capacity" is the target sum, and there's
     * no separate value array.
     *
     * Approach:
     * dp[i][j] = true if some subset of the first i numbers sums to
     * exactly j.
     * dp[i][0] = true for every i (the empty subset always sums to 0).
     * For each number i and sum j:
     *   include: usable if numbers[i-1] <= j and dp[i-1][j-numbers[i-1]]
     *            is true.
     *   exclude: usable if dp[i-1][j] is already true.
     *   dp[i][j] is true if either case is true.
     *
     * Time: O(n * target)
     * Space: O(n * target) for the dp table
     */
    static boolean targetSumSubset(int[] numbers, int target) {
        int n = numbers.length;
        boolean[][] dp = new boolean[n + 1][target + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                int value = numbers[i - 1];
                if (value <= j && dp[i - 1][j - value]) {
                    dp[i][j] = true;
                } else if (dp[i - 1][j]) {
                    dp[i][j] = true;
                }
            }
        }
        return dp[n][target];
    }

    // ================================================================
    // UNBOUNDED KNAPSACK
    // Same as 0-1 Knapsack, except each item can be reused any number
    // of times - only the tabulated form is covered here, matching
    // what the video walks through.
    // ================================================================

    /*
     * Problem:
     * Same setup as 0-1 Knapsack, but each item may be taken any
     * number of times (unlimited supply of every item).
     *
     * Pattern:
     * Knapsack-shaped DP with item reuse allowed.
     *
     * Approach:
     * Identical to the 0-1 tabulation, with exactly one change: the
     * include case stays on row i instead of dropping to row i-1,
     * because the item just used is still available to use again.
     *   if it fits: dp[i][j] = max(val[i-1] + dp[i][j-wt[i-1]], dp[i-1][j])
     *   if it doesn't fit: dp[i][j] = dp[i-1][j]
     *
     * Time: O(n * W)
     * Space: O(n * W) for the dp table
     */
    static int unboundedKnapsack(int[] val, int[] wt, int W) {
        int n = val.length;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                if (wt[i - 1] <= j) {
                    dp[i][j] = Math.max(
                            val[i - 1] + dp[i][j - wt[i - 1]],
                            dp[i - 1][j]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][W];
    }

    public static void main(String[] args) {
        int[] val = {15, 14, 10, 45, 30};
        int[] wt = {2, 5, 1, 3, 4};
        int W = 7;
        int n = val.length;

        System.out.println("0-1 Knapsack, W = " + W);
        System.out.println("  Recursive:  " + knapsackRecursive(val, wt, W, n));

        int[][] memo = new int[n + 1][W + 1];
        for (int[] row : memo) {
            java.util.Arrays.fill(row, -1);
        }
        System.out.println("  Memoized:   " + knapsackMemo(val, wt, W, n, memo));
        System.out.println("  Tabulated:  " + knapsackTabulation(val, wt, W));

        System.out.println();
        int[] numbers = {4, 2, 7, 1, 3};
        int target = 10;
        System.out.println("Target Sum Subset, numbers = [4, 2, 7, 1, 3], target = " + target);
        System.out.println("  Reachable: " + targetSumSubset(numbers, target));

        System.out.println();
        System.out.println("Unbounded Knapsack, same val/wt/W as above (items may repeat)");
        System.out.println("  Max value: " + unboundedKnapsack(val, wt, W));
    }
}
