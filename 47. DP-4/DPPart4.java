import java.util.Arrays;

public class DPPart4 {

    // ================================================================
    // LONGEST COMMON SUBSTRING
    // Like LCS, but the matched characters must be contiguous in both
    // strings, not just in relative order.
    // ================================================================

    /*
     * Problem:
     * Given two strings, find the length of their longest common
     * substring - a run of characters that appears contiguously in
     * both strings.
     *
     * Pattern:
     * Same two-string prefix DP shape as LCS (46. DP-3), but a
     * mismatch breaks the run entirely instead of falling back to a
     * neighboring cell.
     *
     * Approach:
     * dp[i][j] = length of the common substring ending exactly at
     * str1[i-1] and str2[j-1].
     *   if str1[i-1] == str2[j-1]: dp[i][j] = dp[i-1][j-1] + 1, and
     *     update a running max answer (the best substring can end
     *     anywhere, not just at the last cell).
     *   else: dp[i][j] = 0 - the run is broken, there's no partial
     *     credit from neighboring cells the way LCS takes a max.
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static int longestCommonSubstring(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();
        int[][] dp = new int[n + 1][m + 1];
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    ans = Math.max(ans, dp[i][j]);
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        return ans;
    }

    // ================================================================
    // LONGEST INCREASING SUBSEQUENCE (via LCS reduction)
    // Reuses the array version of LCS: LIS(arr) equals the LCS of arr
    // with a sorted, duplicate-free copy of itself.
    // ================================================================

    /*
     * Problem:
     * Same LCS tabulation as before, generalized from characters to
     * array elements so it can be reused for the LIS reduction below.
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static int lcsOfArrays(int[] arr1, int[] arr2) {
        int n = arr1.length;
        int m = arr2.length;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (arr1[i - 1] == arr2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    /*
     * Problem:
     * Given an array, find the length of the longest strictly
     * increasing subsequence (elements in order, not necessarily
     * contiguous).
     *
     * Pattern:
     * LCS reduction - any increasing subsequence of arr is, by
     * definition, a subsequence that also appears in sorted order.
     * So it's exactly the longest common subsequence between arr and
     * a sorted, duplicate-free copy of arr. Duplicates must be removed
     * from the sorted copy, or the LCS could match a repeated value
     * against itself in a way that isn't a genuine increasing run.
     *
     * Approach:
     * Sort a copy of arr, dedupe it, then call lcsOfArrays(arr, sortedUnique).
     *
     * Time: O(n log n) to sort + O(n * n) for the LCS = O(n^2) overall
     * (the sorted-unique copy has at most n elements)
     * Space: O(n^2) for the dp table inside lcsOfArrays
     */
    static int longestIncreasingSubsequence(int[] arr) {
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        int[] sortedUnique = new int[sorted.length];
        int uniqueCount = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0 || sorted[i] != sorted[i - 1]) {
                sortedUnique[uniqueCount++] = sorted[i];
            }
        }
        sortedUnique = Arrays.copyOf(sortedUnique, uniqueCount);
        return lcsOfArrays(arr, sortedUnique);
    }

    // ================================================================
    // EDIT DISTANCE
    // Minimum number of insert / delete / replace operations to turn
    // one string into another.
    // ================================================================

    /*
     * Problem:
     * Given word1 and word2, return the minimum number of operations
     * (insert a character, delete a character, replace a character)
     * needed to convert word1 into word2.
     *
     * Pattern:
     * Two-string prefix DP, same state shape as LCS, but every cell
     * now represents a cost to minimize instead of a length to
     * maximize.
     *
     * Approach:
     * dp[i][j] = min operations to convert word1's first i characters
     * into word2's first j characters.
     * Base cases: dp[0][j] = j (build word2's first j chars purely by
     * inserting), dp[i][0] = i (delete all of word1's first i chars).
     * For i = 1..n, j = 1..m:
     *   if word1[i-1] == word2[j-1]: dp[i][j] = dp[i-1][j-1]
     *     (characters already match, no operation needed here)
     *   else: dp[i][j] = 1 + min(
     *     dp[i][j-1],    // insert word2[j-1] into word1
     *     dp[i-1][j],    // delete word1[i-1]
     *     dp[i-1][j-1])  // replace word1[i-1] with word2[j-1]
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static int editDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                if (j == 0) {
                    dp[i][j] = i;
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int add = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;
                    int replace = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(add, Math.min(delete, replace));
                }
            }
        }
        return dp[n][m];
    }

    // ================================================================
    // STRING CONVERSION (Edit Distance with only insert and delete)
    // Reuses ordinary string LCS: whatever the two strings share (in
    // order) can stay; everything else in str1 must be deleted, and
    // everything else in str2 must be inserted.
    // ================================================================

    /*
     * Problem:
     * Same ordinary LCS tabulation as 46. DP-3, kept local to this
     * file so String Conversion below doesn't reach into another
     * topic folder's file.
     *
     * Time: O(n * m)
     * Space: O(n * m) for the dp table
     */
    static int lcsLength(String str1, String str2) {
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

    /*
     * Problem:
     * Convert str1 into str2 using only insertions and deletions (no
     * replace). Report how many of each are needed.
     *
     * Pattern:
     * LCS reduction - whatever str1 and str2 share, in order, never
     * needs to be touched. Every character of str1 outside that shared
     * subsequence must be deleted; every character of str2 outside it
     * must be inserted.
     *
     * Approach:
     * lcs = lcsLength(str1, str2)
     * deletions  = str1.length() - lcs
     * insertions = str2.length() - lcs
     *
     * Time: O(n * m) (dominated by the LCS computation)
     * Space: O(n * m) for the dp table inside lcsLength
     */
    static int[] stringConversion(String str1, String str2) {
        int lcs = lcsLength(str1, str2);
        int deletions = str1.length() - lcs;
        int insertions = str2.length() - lcs;
        return new int[] {deletions, insertions};
    }

    public static void main(String[] args) {
        String str1 = "abcdge";
        String str2 = "abedg";
        System.out.println("Longest Common Substring, \"" + str1 + "\" vs \"" + str2 + "\"");
        System.out.println("  Length: " + longestCommonSubstring(str1, str2)
                + "  (compare to LCS length 4 from 46. DP-3 - same strings, different question)");

        System.out.println();
        int[] arr = {50, 3, 10, 7, 40, 80};
        System.out.println("Longest Increasing Subsequence, arr = [50, 3, 10, 7, 40, 80]");
        System.out.println("  Length: " + longestIncreasingSubsequence(arr));

        System.out.println();
        String word1 = "intention";
        String word2 = "execution";
        System.out.println("Edit Distance, \"" + word1 + "\" -> \"" + word2 + "\"");
        System.out.println("  Operations: " + editDistance(word1, word2));

        System.out.println();
        String convStr1 = "pear";
        String convStr2 = "sea";
        int[] result = stringConversion(convStr1, convStr2);
        System.out.println("String Conversion, \"" + convStr1 + "\" -> \"" + convStr2 + "\" (insert/delete only)");
        System.out.println("  Deletions: " + result[0] + ", Insertions: " + result[1]
                + ", Total: " + (result[0] + result[1]));
    }
}
