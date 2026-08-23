public class DPPart1 {

    // ================================================================
    // CLIMBING STAIRS
    // Count the number of ways to reach step n, moving 1 or 2 steps
    // at a time. Shown in three forms: recursion, memoization, and
    // tabulation, to see the same recurrence evolve.
    // ================================================================

    /*
     * Problem:
     * Count ways to reach step n, climbing 1 or 2 steps at a time.
     *
     * Pattern:
     * Fibonacci-shaped DP (plain recursion)
     *
     * Approach:
     * ways(n) = ways(n-1) + ways(n-2)
     * ways(0) = 1 (one way: already there, take no steps)
     * ways(n) = 0 for n < 0 (overshot, invalid path)
     *
     * Time: O(2^n) - every call branches into two more calls
     * Space: O(n) recursion stack
     */
    static int climbStairsRecursive(int n) {
        if (n == 0) {
            return 1;
        }
        if (n < 0) {
            return 0;
        }
        return climbStairsRecursive(n - 1) + climbStairsRecursive(n - 2);
    }

    /*
     * Problem:
     * Same as above, but cache each answer so overlapping subproblems
     * are computed once.
     *
     * Pattern:
     * Memoization (top-down DP)
     *
     * Approach:
     * Same recurrence as the plain recursion. Before recursing, check
     * memo[n]. A sentinel of -1 means "not yet computed."
     *
     * Time: O(n) - each state 0..n is computed once
     * Space: O(n) memo array + O(n) recursion stack
     */
    static int climbStairsMemo(int n, int[] memo) {
        if (n == 0) {
            return 1;
        }
        if (n < 0) {
            return 0;
        }
        if (memo[n] != -1) {
            return memo[n];
        }
        memo[n] = climbStairsMemo(n - 1, memo) + climbStairsMemo(n - 2, memo);
        return memo[n];
    }

    /*
     * Problem:
     * Same as above, built bottom-up with no recursion at all.
     *
     * Pattern:
     * Tabulation (bottom-up DP)
     *
     * Approach:
     * dp[0] = dp[1] = 1 (base cases).
     * dp[i] = dp[i-1] + dp[i-2] for i = 2..n, filling smaller states
     * before the larger states that depend on them.
     *
     * Time: O(n)
     * Space: O(n) for the dp array
     */
    static int climbStairsTabulation(int n) {
        if (n == 0) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    // ================================================================
    // CLIMBING STAIRS VARIATION (k steps at a time)
    // Same idea, generalized: each move can cover 1..k steps instead
    // of just 1 or 2.
    // ================================================================

    /*
     * Problem:
     * Count ways to reach step n, climbing 1 up to k steps at a time.
     *
     * Pattern:
     * Fibonacci-shaped DP, generalized to k choices per step
     * (k=2 reduces to plain Climbing Stairs).
     *
     * Approach:
     * ways(n) = sum of ways(n - step) for step = 1..k, skipping any
     * step that would go below 0.
     * Built bottom-up: dp[0] = 1, then for each i, sum the k
     * preceding dp values that exist.
     *
     * Time: O(n * k)
     * Space: O(n) for the dp array
     */
    static int climbStairsKSteps(int n, int k) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            int ways = 0;
            for (int step = 1; step <= k; step++) {
                if (i - step >= 0) {
                    ways += dp[i - step];
                }
            }
            dp[i] = ways;
        }
        return dp[n];
    }

    public static void main(String[] args) {
        int n = 5;

        System.out.println("Climbing Stairs, n = " + n);
        System.out.println("  Recursive:  " + climbStairsRecursive(n));

        int[] memo = new int[n + 1];
        java.util.Arrays.fill(memo, -1);
        System.out.println("  Memoized:   " + climbStairsMemo(n, memo));

        System.out.println("  Tabulated:  " + climbStairsTabulation(n));

        System.out.println();
        System.out.println("Climbing Stairs Variation, k = 3 steps at a time");
        for (int i = 2; i <= 4; i++) {
            System.out.println("  n = " + i + " -> " + climbStairsKSteps(i, 3));
        }
    }
}
