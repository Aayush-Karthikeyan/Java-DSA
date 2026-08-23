public class DPPart3 {

    // ================================================================
    // COIN CHANGE (count the number of ways to make a sum)
    // Coins can be reused any number of times - only the tabulated
    // form is covered here, matching what the video walks through.
    // ================================================================

    /*
     * Problem:
     * Given an array of coin denominations (unlimited supply of each)
     * and a target sum, count how many distinct combinations of coins
     * add up to exactly the sum. Order doesn't matter (2+3 and 3+2
     * count as the same combination).
     *
     * Pattern:
     * Unbounded Knapsack shape, but counting combinations instead of
     * maximizing value - "include" and "exclude" are added together
     * instead of compared with max.
     *
     * Approach:
     * dp[i][j] = number of ways to make sum j using the first i coins.
     * dp[i][0] = 1 for every i (there's exactly one way to make 0: use
     * no coins). dp[0][j] = 0 for j > 0 (no coins available, can't make
     * a positive sum).
     * For i = 1..n, j = 1..sum:
     *   if coins[i-1] <= j: dp[i][j] = dp[i][j-coins[i-1]] + dp[i-1][j]
     *                                   ^^^^ same row: coin i can be reused
     *   else: dp[i][j] = dp[i-1][j]
     *
     * Time: O(n * sum)
     * Space: O(n * sum) for the dp table
     */
    static int coinChangeWays(int[] coins, int sum) {
        int n = coins.length;
        int[][] dp = new int[n + 1][sum + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= sum; j++) {
                if (coins[i - 1] <= j) {
                    dp[i][j] = dp[i][j - coins[i - 1]] + dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][sum];
    }

    // ================================================================
    // ROD CUTTING
    // Cut a rod into pieces (any lengths, any number of pieces) to
    // maximize total price. This is Unbounded Knapsack wearing a
    // different name: "weight" is piece length, "value" is price, and
    // capacity is the total rod length - only tabulation is covered
    // here, matching the video.
    // ================================================================

    /*
     * Problem:
     * Given a rod of length rodLength and prices[i-1] = the price of a
     * piece of length i, determine the maximum total value obtainable
     * by cutting the rod into pieces (including "no cut" - selling it
     * whole) and selling each piece.
     *
     * Pattern:
     * Unbounded Knapsack: a piece of any length can be cut out any
     * number of times, so this is exactly the Unbounded Knapsack
     * tabulation with wt[i-1] replaced by the piece length i itself
     * (piece lengths are always 1, 2, 3, ... n) and val[i-1] = prices[i-1].
     *
     * Approach:
     * dp[i][j] = best value obtainable from a rod of length j, using
     * only piece lengths up to i.
     * For i = 1..n, j = 1..rodLength:
     *   if piece length i fits in remaining length j:
     *     dp[i][j] = max(prices[i-1] + dp[i][j-i], dp[i-1][j])
     *                                   ^^^^ same row: this length can be reused
     *   else: dp[i][j] = dp[i-1][j]
     *
     * Time: O(n * rodLength)
     * Space: O(n * rodLength) for the dp table
     */
    static int rodCutting(int[] prices, int rodLength) {
        int n = prices.length;
        int[][] dp = new int[n + 1][rodLength + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= rodLength; j++) {
                int pieceLength = i;
                if (pieceLength <= j) {
                    dp[i][j] = Math.max(
                            prices[i - 1] + dp[i][j - pieceLength],
                            dp[i - 1][j]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[n][rodLength];
    }

    // ================================================================
    // LONGEST COMMON SUBSEQUENCE (LCS)
    // Find the length of the longest sequence of characters that
    // appears, in order (not necessarily contiguous), in both strings.
    // Shown in three forms: recursion, memoization, tabulation.
    // ================================================================

    /*
     * Problem:
     * Given two strings, find the length of their longest common
     * subsequence.
     *
     * Pattern:
     * Two-pointer-shaped DP over string prefixes - the state is how
     * much of each string remains to be compared.
     *
     * Approach:
     * Compare the last characters of the two remaining prefixes
     * (str1[0..n), str2[0..m)):
     *   if they match: that character is part of the LCS - add 1 and
     *     recurse on both strings with their last character removed.
     *   if they don't match: the LCS can't use both of these last
     *     characters, so try dropping one, then the other, and keep
     *     the better result.
     * Base case: either string is empty -> 0.
     *
     * Time: O(2^(n+m)) - a mismatch branches into two recursive calls
     * Space: O(n+m) recursion stack
     */
    static int lcsRecursive(String str1, String str2, int n, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }
        if (str1.charAt(n - 1) == str2.charAt(m - 1)) {
            return lcsRecursive(str1, str2, n - 1, m - 1) + 1;
        } else {
            int dropFromStr1 = lcsRecursive(str1, str2, n - 1, m);
            int dropFromStr2 = lcsRecursive(str1, str2, n, m - 1);
            return Math.max(dropFromStr1, dropFromStr2);
        }
    }

    /*
     * Problem:
     * Same as above, but cache each (n, m) result so overlapping
     * subproblems are computed once.
     *
     * Pattern:
     * Memoization (top-down DP).
     *
     * Approach:
     * Same recurrence as the plain recursion. Before recursing, check
     * memo[n][m]. A sentinel of -1 means "not yet computed." Every
     * recursive call must go through this memoized method (not the
     * plain recursive one) so the cache actually gets used.
     *
     * Time: O(n * m) - each (n, m) pair is computed once
     * Space: O(n * m) memo table + O(n+m) recursion stack
     */
    static int lcsMemo(String str1, String str2, int n, int m, int[][] memo) {
        if (n == 0 || m == 0) {
            return 0;
        }
        if (memo[n][m] != -1) {
            return memo[n][m];
        }
        if (str1.charAt(n - 1) == str2.charAt(m - 1)) {
            memo[n][m] = lcsMemo(str1, str2, n - 1, m - 1, memo) + 1;
        } else {
            int dropFromStr1 = lcsMemo(str1, str2, n - 1, m, memo);
            int dropFromStr2 = lcsMemo(str1, str2, n, m - 1, memo);
            memo[n][m] = Math.max(dropFromStr1, dropFromStr2);
        }
        return memo[n][m];
    }

    /*
     * Problem:
     * Same as above, built bottom-up with no recursion at all.
     *
     * Pattern:
     * Tabulation (bottom-up DP), 2D table over string prefixes.
     *
     * Approach:
     * dp[i][j] = LCS length between str1's first i characters and
     * str2's first j characters. dp[i][0] and dp[0][j] are 0 by
     * default (Java zero-initializes).
     * For i = 1..n, j = 1..m:
     *   if str1[i-1] == str2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
     *   else: dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static int lcsTabulation(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    public static void main(String[] args) {
        int[] coins = {2, 5, 3, 6};
        int sum = 10;
        System.out.println("Coin Change, coins = [2, 5, 3, 6], sum = " + sum);
        System.out.println("  Ways: " + coinChangeWays(coins, sum));

        System.out.println();
        int[] prices = {1, 5, 8, 9, 10, 17, 17, 20};
        int rodLength = 8;
        System.out.println("Rod Cutting, prices = [1,5,8,9,10,17,17,20], rodLength = " + rodLength);
        System.out.println("  Max value: " + rodCutting(prices, rodLength));

        System.out.println();
        String str1 = "abcdge";
        String str2 = "abedg";
        System.out.println("LCS, str1 = \"" + str1 + "\", str2 = \"" + str2 + "\"");
        System.out.println("  Recursive: "
                + lcsRecursive(str1, str2, str1.length(), str2.length()));

        int[][] memo = new int[str1.length() + 1][str2.length() + 1];
        for (int[] row : memo) {
            java.util.Arrays.fill(row, -1);
        }
        System.out.println("  Memoized:  "
                + lcsMemo(str1, str2, str1.length(), str2.length(), memo));
        System.out.println("  Tabulated: " + lcsTabulation(str1, str2));

        System.out.println();
        System.out.println("LCS tabulation dry-run example: \"abcde\" vs \"ace\"");
        System.out.println("  Tabulated: " + lcsTabulation("abcde", "ace"));
    }
}
