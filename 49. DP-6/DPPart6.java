import java.util.Arrays;

public class DPPart6 {

    // ================================================================
    // MATRIX CHAIN MULTIPLICATION (MCM)
    // Given a chain of matrices (described by their dimensions), find
    // the cheapest order to parenthesize the multiplications. Shown in
    // three forms: recursion, memoization, tabulation.
    //
    // arr[] describes n-1 matrices: matrix k has dimensions
    // arr[k-1] x arr[k]. Multiplying a p x q matrix by a q x r matrix
    // costs p*q*r scalar multiplications and produces a p x r result
    // (from Matrix Basics: for A(a x b) . B(c x d) to be valid, b must
    // equal c; the result is a x d, at a cost of a*b*d).
    // ================================================================

    /*
     * Problem:
     * Given arr[] describing a chain of matrices (matrix k has
     * dimensions arr[k-1] x arr[k]), find the minimum total scalar
     * multiplications needed to multiply the whole chain together, by
     * choosing the cheapest place to split it at every level.
     *
     * Pattern:
     * Interval DP - the state is a range (i, j) representing "the
     * matrices from i to j," not a single index. The recurrence tries
     * every possible split point k inside that range.
     *
     * Approach:
     * Base case: i == j (a single matrix needs no multiplication) -> 0.
     * Otherwise, try every split point k from i to j-1: multiplying
     * matrices i..k first, then k+1..j, then combining those two
     * results costs arr[i-1] * arr[k] * arr[j] (the combined result's
     * row count, the shared middle dimension, and its column count).
     * Take whichever split minimizes cost1 + cost2 + cost3.
     *
     * Time: exponential - heavily overlapping ranges get re-explored
     * Space: O(j - i) recursion stack
     */
    static int mcmRecursive(int[] arr, int i, int j) {
        if (i == j) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        for (int k = i; k <= j - 1; k++) {
            int cost1 = mcmRecursive(arr, i, k);
            int cost2 = mcmRecursive(arr, k + 1, j);
            int cost3 = arr[i - 1] * arr[k] * arr[j];
            int finalCost = cost1 + cost2 + cost3;
            ans = Math.min(ans, finalCost);
        }
        return ans;
    }

    /*
     * Problem:
     * Same as above, but cache each (i, j) range's answer so
     * overlapping ranges are computed once.
     *
     * Pattern:
     * Memoization (top-down DP) over a 2D range instead of a single
     * index.
     *
     * Approach:
     * Same recurrence. Before recursing, check memo[i][j]. A sentinel
     * of -1 means "not yet computed."
     *
     * Time: O(n^3) - O(n^2) distinct (i, j) ranges, each doing O(n)
     * work trying every split point
     * Space: O(n^2) memo table + O(n) recursion stack
     */
    static int mcmMemo(int[] arr, int i, int j, int[][] memo) {
        if (i == j) {
            return 0;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int ans = Integer.MAX_VALUE;
        for (int k = i; k <= j - 1; k++) {
            int cost1 = mcmMemo(arr, i, k, memo);
            int cost2 = mcmMemo(arr, k + 1, j, memo);
            int cost3 = arr[i - 1] * arr[k] * arr[j];
            int finalCost = cost1 + cost2 + cost3;
            ans = Math.min(ans, finalCost);
        }
        memo[i][j] = ans;
        return memo[i][j];
    }

    /*
     * Problem:
     * Same as above, built bottom-up with no recursion at all.
     *
     * Pattern:
     * Tabulation over ranges, filled by increasing range length rather
     * than by row or column - a range (i, j) needs every smaller range
     * strictly inside it to already be filled in, so the fill order
     * has to be "all ranges of length 2, then length 3, ..." instead
     * of a simple nested i/j loop.
     *
     * Approach:
     * dp[i][i] = 0 for every i (a single matrix, free by default -
     * Java zero-initializes the array).
     * For len = 2..n-1 (n-1 is the number of matrices, so the longest
     * range), and for each starting point i = 1..(n-len), let
     * j = i + len - 1 (the range's end). Try every split k = i..j-1,
     * same cost formula as the recursive version, keeping the minimum.
     *
     * Time: O(n^3). Space: O(n^2) for the dp table.
     */
    static int mcmTabulation(int[] arr) {
        int n = arr.length;
        int[][] dp = new int[n][n];
        for (int len = 2; len <= n - 1; len++) {
            for (int i = 1; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k <= j - 1; k++) {
                    int cost1 = dp[i][k];
                    int cost2 = dp[k + 1][j];
                    int cost3 = arr[i - 1] * arr[k] * arr[j];
                    int finalCost = cost1 + cost2 + cost3;
                    dp[i][j] = Math.min(dp[i][j], finalCost);
                }
            }
        }
        return dp[1][n - 1];
    }

    // ================================================================
    // MINIMUM PARTITIONING
    // Split an array into two subsets so their sums are as close to
    // equal as possible. Reuses 0-1 Knapsack's exact table shape from
    // 45. DP-2, tracking the best achievable sum instead of a boolean.
    // ================================================================

    /*
     * Problem:
     * Given an array of positive numbers, partition it into two
     * subsets so that the absolute difference between their sums is
     * as small as possible. Return that minimum difference.
     *
     * Pattern:
     * 0-1 Knapsack shape: "value" and "weight" are both just the
     * element itself, and "capacity" is half the total sum - find the
     * best achievable subset sum that doesn't exceed it.
     *
     * Approach:
     * Let sum = total of all elements, W = sum / 2.
     * dp[i][j] = the largest subset sum achievable using the first i
     * elements that does not exceed j.
     * For i = 1..n, j = 1..W:
     *   if arr[i-1] <= j: dp[i][j] = max(arr[i-1] + dp[i-1][j-arr[i-1]], dp[i-1][j])
     *   else: dp[i][j] = dp[i-1][j]
     * The best subset sum found is dp[n][W]; the other subset is
     * exactly sum - dp[n][W], so the answer is
     * sum - 2 * dp[n][W] (equivalent to |secondSubset - firstSubset|).
     *
     * Time: O(n * sum)
     * Space: O(n * sum) for the dp table
     */
    static int minPartition(int[] arr) {
        int n = arr.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        int W = sum / 2;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= W; j++) {
                if (arr[i - 1] <= j) {
                    dp[i][j] = Math.max(
                            arr[i - 1] + dp[i - 1][j - arr[i - 1]],
                            dp[i - 1][j]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        int bestSubsetSum = dp[n][W];
        return sum - 2 * bestSubsetSum;
    }

    // ================================================================
    // MINIMUM ARRAY JUMPS
    // Each element is the maximum jump length from that position; find
    // the minimum number of jumps needed to reach the last index.
    // ================================================================

    /*
     * Problem:
     * Given nums[] where nums[i] is the farthest you can jump from
     * index i, find the minimum number of jumps to reach the last
     * index, starting at index 0.
     *
     * Pattern:
     * "Minimum steps to reach a target" DP, computed backward from the
     * end - each state looks forward over a variable-size window
     * (bounded by that position's own jump length) instead of a fixed
     * window like DP-1's Climbing Stairs.
     *
     * Approach:
     * dp[i] = minimum jumps from index i to the last index. dp[n-1] = 0.
     * Fill from i = n-2 down to 0: look at every index j reachable in
     * one jump from i (j from i+1 up to i+nums[i], capped at n-1), and
     * take the smallest dp[j] + 1 among reachable indices that already
     * have a known answer (dp[j] != -1, the "unreached" sentinel).
     *
     * Time: O(n^2) worst case - for each i, the inner loop can scan up
     * to nums[i] positions
     * Space: O(n) for the dp array
     */
    static int minArrayJumps(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        dp[n - 1] = 0;

        for (int i = n - 2; i >= 0; i--) {
            int steps = nums[i];
            int ans = Integer.MAX_VALUE;
            for (int j = i + 1; j <= i + steps && j < n; j++) {
                if (dp[j] != -1) {
                    ans = Math.min(ans, dp[j] + 1);
                }
            }
            if (ans != Integer.MAX_VALUE) {
                dp[i] = ans;
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 3};
        int n = arr.length;
        System.out.println("Matrix Chain Multiplication, arr = [1, 2, 3, 4, 3]");
        System.out.println("  Recursive: " + mcmRecursive(arr, 1, n - 1));

        int[][] memo = new int[n][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        System.out.println("  Memoized:  " + mcmMemo(arr, 1, n - 1, memo));
        System.out.println("  Tabulated: " + mcmTabulation(arr));

        System.out.println();
        int[] nums = {1, 6, 11, 5};
        System.out.println("Minimum Partitioning, arr = [1, 6, 11, 5]");
        System.out.println("  Min difference: " + minPartition(nums));

        System.out.println();
        int[] jumps = {2, 3, 1, 1, 4};
        System.out.println("Minimum Array Jumps, arr = [2, 3, 1, 1, 4]");
        System.out.println("  Min jumps: " + minArrayJumps(jumps));
    }
}
