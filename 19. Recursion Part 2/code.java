public class code {

    // ===================================================================
    // TOPIC: RECURSION BASICS — PART 2
    // More advanced recursive patterns:
    //   - Tiling problem (counting ways)
    //   - Remove duplicates in a string
    //   - Friends pairing problem (counting ways)
    //   - Binary strings with no consecutive 1s
    // ===================================================================


    // ===== 1. TILING PROBLEM =====
    // Given a board of size 2×n, tile it completely using 2×1 dominoes.
    // A domino can be placed:
    //   - Vertically   → covers 1 column  → remaining board = 2×(n-1)
    //   - Horizontally → MUST place 2 tiles in a row → covers 2 columns → remaining = 2×(n-2)
    //
    // This is exactly like Fibonacci: ways(n) = ways(n-1) + ways(n-2)
    // Because at each step you either use 1 column (vertical) or 2 columns (two horizontal).
    public static int tilingWays(int n) {
        if (n == 0) return 1;   // base case: empty board has exactly 1 way (do nothing)
        if (n == 1) return 1;   // base case: only 1 column → only 1 way (place 1 vertical tile)
        // vertical placement: solve for n-1 remaining columns
        // horizontal placement: must pair with another horizontal → solve for n-2 remaining
        return tilingWays(n - 1) + tilingWays(n - 2);
    }


    // ===== 2. REMOVE DUPLICATES IN A STRING =====
    // Keep only the FIRST occurrence of each character.
    // e.g. "aabbccdd" → "abcd",  "aabcbd" → "abcd"
    //
    // Strategy: pass a boolean array 'seen' of size 26 (one slot per lowercase letter).
    // At each character: if already seen, skip it. If not, include it and mark as seen.
    // idx = current index in the string.
    public static String removeDuplicates(String str, int idx, boolean[] seen) {
        if (idx == str.length()) return "";         // base case: processed all characters

        char ch = str.charAt(idx);                  // current character
        int pos = ch - 'a';                         // map 'a'=0, 'b'=1, ..., 'z'=25

        if (seen[pos]) {
            // character already appeared before — skip it, move to next index
            return removeDuplicates(str, idx + 1, seen);
        } else {
            // first time seeing this character — include it
            seen[pos] = true;                       // mark as seen
            return ch + removeDuplicates(str, idx + 1, seen);  // prepend it to the result
        }
    }


    // ===== 3. FRIENDS PAIRING PROBLEM =====
    // n friends. Each friend can either:
    //   a) Stay SINGLE  → remaining n-1 friends solve the problem
    //   b) PAIR UP with any one of the (n-1) other friends → remaining n-2 friends solve it
    //      (n-1 choices for the partner)
    //
    // Formula: f(n) = f(n-1)  +  (n-1) * f(n-2)
    //                  ^single      ^paired with one of n-1 others
    public static int friendsPairing(int n) {
        if (n == 1) return 1;   // base case: 1 person → only 1 way (stay single)
        if (n == 2) return 2;   // base case: 2 people → {both single} or {pair up} = 2 ways
        // either current person stays single (f(n-1))
        // or pairs with one of the (n-1) others ((n-1) * f(n-2))
        return friendsPairing(n - 1) + (n - 1) * friendsPairing(n - 2);
    }


    // ===== 4. BINARY STRINGS WITH NO CONSECUTIVE 1s =====
    // Count all binary strings of length n that have NO two consecutive 1s.
    // e.g. n=3: valid = 000,001,010,100,101 → count = 5
    //
    // Strategy: build the string bit by bit.
    //   - If last placed bit was 0 → next can be 0 OR 1
    //   - If last placed bit was 1 → next can ONLY be 0 (no consecutive 1s allowed)
    //
    // lastBit = the bit we placed in the previous position (0 or 1)
    // n = remaining positions to fill
    public static int countBinaryStrings(int n, int lastBit) {
        if (n == 0) return 1;   // base case: filled all positions, this is 1 valid string

        if (lastBit == 0) {
            // last bit was 0 → we can place 0 or 1 next
            return countBinaryStrings(n - 1, 0) + countBinaryStrings(n - 1, 1);
        } else {
            // last bit was 1 → we can ONLY place 0 next (to avoid consecutive 1s)
            return countBinaryStrings(n - 1, 0);
        }
    }

    // Helper to print all valid binary strings (not just count them)
    // current = the binary string built so far
    public static void printBinaryStrings(int n, int lastBit, String current) {
        if (n == 0) {
            System.out.print(current + " ");  // print completed valid string
            return;
        }
        // always try placing 0
        printBinaryStrings(n - 1, 0, current + "0");
        // try placing 1 only if last bit was 0
        if (lastBit == 0) {
            printBinaryStrings(n - 1, 1, current + "1");
        }
    }


    public static void main(String[] args) {

        // 1. Tiling Problem
        System.out.println("===== Tiling Problem =====");
        for (int i = 1; i <= 6; i++) {
            System.out.println("Ways to tile 2x" + i + ": " + tilingWays(i));
            // 1, 2, 3, 5, 8, 13  (Fibonacci sequence!)
        }
        System.out.println();

        // 2. Remove Duplicates
        System.out.println("===== Remove Duplicates =====");
        String[] tests = {"aabbccdd", "aabcbd", "hello", "banana"};
        for (String t : tests) {
            boolean[] seen = new boolean[26];   // fresh 'seen' array for each string
            System.out.println("\"" + t + "\" → \"" + removeDuplicates(t, 0, seen) + "\"");
        }
        System.out.println();

        // 3. Friends Pairing Problem
        System.out.println("===== Friends Pairing =====");
        for (int i = 1; i <= 5; i++) {
            System.out.println("f(" + i + ") = " + friendsPairing(i));
            // 1, 2, 4, 10, 26
        }
        System.out.println();

        // 4. Binary Strings
        System.out.println("===== Binary Strings (no consecutive 1s) =====");
        System.out.println("Count for n=3: " + countBinaryStrings(3, 0));  // 5
        System.out.println("Count for n=4: " + countBinaryStrings(4, 0));  // 8
        System.out.print("Strings for n=3: ");
        printBinaryStrings(3, 0, "");  // 000 001 010 100 101
        System.out.println();
    }
}
