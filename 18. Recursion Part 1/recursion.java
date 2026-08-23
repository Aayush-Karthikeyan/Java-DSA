public class recursion {

    // ===================================================================
    // TOPIC: RECURSION BASICS — PART 1
    // Recursion = a function that calls ITSELF to solve a smaller version
    // of the same problem, until it hits a base case and stops.
    //
    // Every recursive function needs:
    //   1. BASE CASE  — the condition where it stops calling itself
    //   2. RECURSIVE CASE — the call that moves toward the base case
    // ===================================================================


    // ===== 1. PRINT NUMBERS IN DECREASING ORDER (n down to 1) =====
    // Idea: print n first, THEN recurse for n-1
    //       (work happens BEFORE the recursive call — "pre-order")
    public static void printDecreasing(int n) {
        if (n == 0) return;          // base case: stop when n reaches 0
        System.out.print(n + " ");   // print current number FIRST
        printDecreasing(n - 1);      // then recurse for the smaller problem
    }


    // ===== 2. PRINT NUMBERS IN INCREASING ORDER (1 up to n) =====
    // Idea: recurse FIRST for n-1, THEN print n on the way back
    //       (work happens AFTER the recursive call — "post-order")
    public static void printIncreasing(int n) {
        if (n == 0) return;          // base case: stop
        printIncreasing(n - 1);      // recurse FIRST (go all the way down)
        System.out.print(n + " ");   // print on the way BACK UP the call stack
    }


    // ===== 3. FACTORIAL OF N =====
    // n! = n * (n-1) * (n-2) * ... * 1
    // Base case: 0! = 1  and  1! = 1
    // Recursive case: n! = n * (n-1)!
    public static int factorial(int n) {
        if (n <= 1) return 1;             // base case: 0! = 1, 1! = 1
        return n * factorial(n - 1);      // e.g. factorial(5) = 5 * factorial(4)
    }


    // ===== 4. SUM OF FIRST N NATURAL NUMBERS =====
    // Sum(n) = 1 + 2 + ... + n
    // Recursive: Sum(n) = n + Sum(n-1)
    public static int sumN(int n) {
        if (n == 0) return 0;        // base case: sum of 0 numbers is 0
        return n + sumN(n - 1);      // add current n to sum of smaller problem
    }


    // ===== 5. NTH FIBONACCI NUMBER =====
    // Fib sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, ...
    // fib(0) = 0, fib(1) = 1, fib(n) = fib(n-1) + fib(n-2)
    // NOTE: This naive recursion is O(2^n) — exponential. Fine for learning,
    //       but use memoization or DP for large n.
    public static int fibonacci(int n) {
        if (n == 0) return 0;                        // base case 1
        if (n == 1) return 1;                        // base case 2
        return fibonacci(n - 1) + fibonacci(n - 2); // sum of two previous fib numbers
    }


    // ===== 6. CHECK IF ARRAY IS SORTED =====
    // Compare pairs of adjacent elements recursively.
    // If any pair is out of order, the array is not sorted.
    // index starts at 0; checks arr[index] vs arr[index+1]
    public static boolean isSorted(int[] arr, int index) {
        if (index == arr.length - 1) return true;       // base case: reached the last element, sorted
        if (arr[index] > arr[index + 1]) return false;  // current pair is out of order → not sorted
        return isSorted(arr, index + 1);                // check the rest of the array
    }


    // ===== 7. FIRST OCCURRENCE OF KEY IN ARRAY =====
    // Search from left to right; return index of first match, or -1 if not found.
    public static int firstOccurrence(int[] arr, int key, int index) {
        if (index == arr.length) return -1;         // base case: went past the end, not found
        if (arr[index] == key) return index;        // found it! return current index
        return firstOccurrence(arr, key, index + 1); // not here, search rest of array
    }


    // ===== 8. LAST OCCURRENCE OF KEY IN ARRAY =====
    // Search from left to right but DON'T stop at the first match.
    // Keep going — if a later match exists it will override earlier ones on the way back.
    public static int lastOccurrence(int[] arr, int key, int index) {
        if (index == arr.length) return -1;           // base case: end of array
        int restResult = lastOccurrence(arr, key, index + 1); // search the rest FIRST
        if (restResult != -1) return restResult;      // if found later, that's the last occurrence
        if (arr[index] == key) return index;          // no later match, check current index
        return -1;                                    // not found at all
    }


    // ===== 9. x TO THE POWER n (Naive) =====
    // x^n = x * x^(n-1)
    // O(n) — n multiplications
    public static long power(int x, int n) {
        if (n == 0) return 1;           // base case: anything^0 = 1
        return x * power(x, n - 1);    // x^n = x * x^(n-1)
    }


    // ===== 10. x TO THE POWER n (OPTIMIZED — Fast Exponentiation) =====
    // Key insight: x^n = (x^(n/2))^2  when n is even
    //              x^n = x * (x^(n/2))^2  when n is odd
    // This halves the problem each step → O(log n) instead of O(n)
    public static long powerOptimized(int x, int n) {
        if (n == 0) return 1;                          // base case
        long half = powerOptimized(x, n / 2);          // solve for half the exponent
        if (n % 2 == 0) {
            return half * half;                         // even: just square the half result
        } else {
            return x * half * half;                     // odd: multiply one extra x
        }
    }


    public static void main(String[] args) {

        // 1. Decreasing
        System.out.print("Decreasing (5): ");
        printDecreasing(5);          // 5 4 3 2 1
        System.out.println();

        // 2. Increasing
        System.out.print("Increasing (5): ");
        printIncreasing(5);          // 1 2 3 4 5
        System.out.println();

        // 3. Factorial
        System.out.println("5! = " + factorial(5));    // 120
        System.out.println("0! = " + factorial(0));    // 1

        // 4. Sum of N
        System.out.println("Sum(10) = " + sumN(10));  // 55

        // 5. Fibonacci
        System.out.print("Fib(0 to 9): ");
        for (int i = 0; i < 10; i++) System.out.print(fibonacci(i) + " ");  // 0 1 1 2 3 5 8 13 21 34
        System.out.println();

        // 6. Is Sorted
        int[] sorted   = {1, 2, 3, 4, 5};
        int[] unsorted = {1, 3, 2, 4, 5};
        System.out.println("Sorted? " + isSorted(sorted, 0));    // true
        System.out.println("Sorted? " + isSorted(unsorted, 0));  // false

        // 7. First Occurrence
        int[] arr = {3, 1, 4, 1, 5, 1};
        System.out.println("First '1': index " + firstOccurrence(arr, 1, 0));  // 1

        // 8. Last Occurrence
        System.out.println("Last '1':  index " + lastOccurrence(arr, 1, 0));   // 5

        // 9. Power (naive)
        System.out.println("2^10 (naive):     " + power(2, 10));           // 1024

        // 10. Power (optimized)
        System.out.println("2^10 (optimized): " + powerOptimized(2, 10)); // 1024
        System.out.println("3^5  (optimized): " + powerOptimized(3, 5));  // 243
    }
}
