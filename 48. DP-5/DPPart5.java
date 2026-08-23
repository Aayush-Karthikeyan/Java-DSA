import java.util.Arrays;

public class DPPart5 {

    // ================================================================
    // WILDCARD MATCHING
    // Decide whether a wildcard pattern ('?' = any single character,
    // '*' = any sequence, including empty) matches an entire text.
    // ================================================================

    /*
     * Problem:
     * Given a text s and a pattern p containing literal characters,
     * '?' (matches any single character), and '*' (matches any
     * sequence of characters, including the empty sequence), decide
     * whether p matches the entire text s.
     *
     * Pattern:
     * Two-string prefix DP (same family as LCS / Edit Distance), but
     * with a special rule for '*' that offers two different ways to
     * use it.
     *
     * Approach:
     * dp[i][j] = does p's first j characters match s's first i characters.
     * Base cases:
     *   dp[0][0] = true (empty pattern matches empty text).
     *   dp[i][0] = false for i >= 1 (a non-empty text can't match an
     *     empty pattern).
     *   dp[0][j]: only reachable by a leading run of '*' - true only
     *     while every pattern character seen so far is '*'.
     * For i = 1..n, j = 1..m:
     *   if s[i-1] == p[j-1] or p[j-1] == '?': dp[i][j] = dp[i-1][j-1]
     *     (this character is used up on both sides).
     *   else if p[j-1] == '*': dp[i][j] = dp[i][j-1] || dp[i-1][j]
     *     (the star matches nothing new - dp[i][j-1] - or the star
     *     absorbs one more text character and stays a star - dp[i-1][j]).
     *   else: dp[i][j] = false (a literal character that doesn't match).
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static boolean isMatch(String s, String p) {
        int n = s.length();
        int m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];

        dp[0][0] = true;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = false;
        }
        for (int j = 1; j <= m; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1];
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char patternChar = p.charAt(j - 1);
                if (s.charAt(i - 1) == patternChar || patternChar == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (patternChar == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else {
                    dp[i][j] = false;
                }
            }
        }

        return dp[n][m];
    }

    // ================================================================
    // CATALAN'S NUMBER
    // Cn = sum of Ci * C(n-1-i) for i = 0..n-1, with C0 = C1 = 1.
    // Shown in three forms: recursion, memoization, tabulation.
    // ================================================================

    /*
     * Problem:
     * Compute the nth Catalan number.
     *
     * Pattern:
     * "Split the problem at every possible point" DP - unlike the
     * Knapsack or LCS families, the state is a single size n, but the
     * recurrence sums over every way to split that size into two
     * smaller parts.
     *
     * Approach:
     * Base case: C0 = C1 = 1.
     * Cn = sum over i = 0..n-1 of Ci * C(n-1-i) - i and n-1-i are the
     * sizes of the two parts produced by "splitting" at position i.
     *
     * Time: exponential - grows on the order of the Catalan numbers'
     * own asymptotic growth rate (roughly 4^n / n^1.5), since every
     * call re-explores heavily overlapping smaller subproblems.
     * Space: O(n) recursion stack.
     */
    static int catalanRecursive(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int ans = 0;
        for (int i = 0; i <= n - 1; i++) {
            ans += catalanRecursive(i) * catalanRecursive(n - i - 1);
        }
        return ans;
    }

    /*
     * Problem:
     * Same as above, but cache each Ci so overlapping subproblems are
     * computed once.
     *
     * Pattern:
     * Memoization (top-down DP).
     *
     * Approach:
     * Same recurrence as the plain recursion. Before computing Cn,
     * check memo[n]. A sentinel of -1 means "not yet computed."
     *
     * Time: O(n^2) - computing Ck the first time does O(k) work, and
     * every 0..n is computed exactly once, so total work is
     * 0 + 1 + ... + n = O(n^2).
     * Space: O(n) memo array + O(n) recursion stack.
     */
    static int catalanMemo(int n, int[] memo) {
        if (n == 0 || n == 1) {
            return 1;
        }
        if (memo[n] != -1) {
            return memo[n];
        }
        int ans = 0;
        for (int i = 0; i <= n - 1; i++) {
            ans += catalanMemo(i, memo) * catalanMemo(n - i - 1, memo);
        }
        memo[n] = ans;
        return memo[n];
    }

    /*
     * Problem:
     * Same as above, built bottom-up with no recursion at all.
     *
     * Pattern:
     * Tabulation (bottom-up DP), 1D table.
     *
     * Approach:
     * dp[0] = dp[1] = 1.
     * For i = 2..n: dp[i] = sum over j = 0..i-1 of dp[j] * dp[i-1-j].
     * Return dp[n].
     *
     * Time: O(n^2) - the outer loop runs n times, the inner loop grows
     * with it.
     * Space: O(n) for the dp array.
     */
    static int catalanTabulation(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                dp[i] += dp[j] * dp[i - j - 1];
            }
        }
        return dp[n];
    }

    // ================================================================
    // COUNTING TREES (Unique Binary Search Trees)
    // How many structurally different BSTs can be built from n
    // distinct keys? This is Catalan's Number wearing a different
    // name - no new code needed, just a new interpretation.
    // ================================================================

    /*
     * Problem:
     * Given n distinct keys, count how many structurally different
     * binary search trees can be built from them.
     *
     * Pattern:
     * Same "split at every point" shape as Catalan's Number.
     *
     * Approach:
     * For n keys, any one of them can be the root. Choosing the kth
     * smallest key as root forces the smaller k-1 keys into the left
     * subtree and the larger n-k keys into the right subtree - and the
     * number of distinct trees for a set of keys only depends on how
     * many keys there are, not their actual values. So the count for
     * root choice k is (ways to arrange k-1 keys) * (ways to arrange
     * n-k keys), and summing over every possible root gives exactly
     * the Catalan recurrence: dp[i] = sum of dp[j] * dp[i-1-j] for
     * j = 0..i-1, where j is the left subtree's size.
     *
     * Time: O(n^2). Space: O(n) for the dp array (reused from
     * catalanTabulation).
     */
    static int countUniqueBSTs(int n) {
        return catalanTabulation(n);
    }

    // ================================================================
    // MOUNTAIN RANGES
    // Count the number of ways to arrange n pairs of up/down strokes
    // into a "mountain range" that never dips below the starting
    // level - also Catalan's Number, shown here with its own explicit
    // tabulation matching the video.
    // ================================================================

    /*
     * Problem:
     * Given n pairs of up-strokes and down-strokes, count how many
     * distinct mountain ranges (sequences that never go below the
     * starting level, and end back at it) can be formed.
     *
     * Pattern:
     * Same "split at every point" shape as Catalan's Number: the first
     * up-stroke's matching down-stroke splits the range into an
     * "inside" mountain range (nested underneath it) and an "outside"
     * mountain range (everything after it closes).
     *
     * Approach:
     * dp[0] = dp[1] = 1.
     * For i = 2..n, sum over j = 0..i-1:
     *   inside  = dp[j]       (mountain ranges nested under the first pair)
     *   outside = dp[i-j-1]   (mountain ranges after the first pair closes)
     *   dp[i] += inside * outside
     * Return dp[n].
     *
     * Time: O(n^2). Space: O(n) for the dp array.
     */
    static int mountainRanges(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                int inside = dp[j];
                int outside = dp[i - j - 1];
                dp[i] += inside * outside;
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("Wildcard Matching:");
        System.out.println("  s=\"aa\", p=\"*\"                            -> "
                + isMatch("aa", "*") + "  (expected true)");
        System.out.println("  s=\"baaabab\", p=\"*****ba*****ab\"          -> "
                + isMatch("baaabab", "*****ba*****ab") + "  (expected true)");
        System.out.println("  s=\"baaabab\", p=\"a*ab\"                    -> "
                + isMatch("baaabab", "a*ab") + "  (expected false)");

        System.out.println();
        int n = 4;
        System.out.println("Catalan's Number, n = " + n);
        System.out.println("  Recursive: " + catalanRecursive(n));

        int[] memo = new int[n + 1];
        Arrays.fill(memo, -1);
        System.out.println("  Memoized:  " + catalanMemo(n, memo));
        System.out.println("  Tabulated: " + catalanTabulation(n));

        System.out.println();
        System.out.println("Counting Trees (Unique BSTs), n = 4 keys (e.g. 10, 20, 30, 40)");
        System.out.println("  Count: " + countUniqueBSTs(4));

        System.out.println();
        System.out.println("Mountain Ranges, n = 4 pairs");
        System.out.println("  Count: " + mountainRanges(4));
    }
}
