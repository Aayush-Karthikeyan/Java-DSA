public class code {

    // ============================================================
    // TIME & SPACE COMPLEXITY — Code Examples
    // Each method shows a pattern and its complexity.
    // Read the notes.md for the full theory.
    // ============================================================


    // O(1) — Constant: no loops, no recursion, fixed number of steps
    static int getFirst(int[] arr) {
        return arr[0];                          // always 1 step, regardless of array size
    }


    // O(n) — Linear: one loop through n elements
    static int sumArray(int[] arr) {
        int sum = 0;
        for (int x : arr) sum += x;            // runs n times
        return sum;
    }


    // O(n²) — Quadratic: nested loop, each runs n times
    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {          // outer: n-1 times
            for (int j = 0; j < n - i - 1; j++) {  // inner: shrinks each pass
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = t;
                }
            }
        }
        // total comparisons ≈ n*(n-1)/2  →  O(n²)
    }


    // O(log n) — Logarithmic: problem halves each step
    static int binarySearch(int[] arr, int target) {
        int lo = 0, hi = arr.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] == target) return mid;
            else if (arr[mid] < target) lo = mid + 1; // discard left half
            else hi = mid - 1;                         // discard right half
            // each step: search space halved → O(log n) steps total
        }
        return -1;
    }


    // O(n log n) — Merge Sort (see section 20 for full code)
    // At each of the log n levels of recursion, we do O(n) work in the merge step.
    // log n levels × O(n) work per level = O(n log n)
    static void mergeSort(int[] arr, int s, int e) {
        if (s >= e) return;
        int m = s + (e - s) / 2;
        mergeSort(arr, s, m);
        mergeSort(arr, m + 1, e);
        merge(arr, s, m, e);                    // O(n) work at each level
    }
    static void merge(int[] arr, int s, int m, int e) {
        int[] tmp = new int[e - s + 1];
        int i = s, j = m + 1, k = 0;
        while (i <= m && j <= e) tmp[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++];
        while (i <= m) tmp[k++] = arr[i++];
        while (j <= e) tmp[k++] = arr[j++];
        for (int l = 0; l < tmp.length; l++) arr[s + l] = tmp[l];
    }


    // O(2^n) — Exponential: naive Fibonacci makes 2 calls per step
    static int fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);        // 2 recursive calls → call tree doubles each level
        // fib(5) makes ~30 calls, fib(10) makes ~177 calls, fib(30) makes ~2 million calls
    }


    // ---- RECURSIVE COMPLEXITY ANALYSIS ----

    // factorial(n): makes n recursive calls, O(1) work each → O(n) time, O(n) space (call stack)
    static int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);            // n calls deep, 1 call per level → O(n)
    }

    // sumN(n): same pattern as factorial → O(n) time, O(n) space
    static int sumN(int n) {
        if (n == 0) return 0;
        return n + sumN(n - 1);
    }

    // power(x, n) naive: n recursive calls → O(n)
    static long powerNaive(int x, int n) {
        if (n == 0) return 1;
        return x * powerNaive(x, n - 1);       // n levels deep → O(n)
    }

    // power(x, n) optimized: n halves each call → O(log n)
    static long powerFast(int x, int n) {
        if (n == 0) return 1;
        long half = powerFast(x, n / 2);       // log n levels deep → O(log n)
        return (n % 2 == 0) ? half * half : x * half * half;
    }


    public static void main(String[] args) {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        int[] sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        System.out.println("O(1)  getFirst:      " + getFirst(arr));
        System.out.println("O(n)  sumArray:      " + sumArray(arr));
        System.out.println("O(log n) binarySearch for 7: " + binarySearch(sorted, 7));

        bubbleSort(arr);
        System.out.print("O(n²) bubbleSort:    ");
        for (int x : arr) System.out.print(x + " ");
        System.out.println();

        System.out.println("O(n)  factorial(5): " + factorial(5));
        System.out.println("O(n)  powerNaive(2,10): " + powerNaive(2, 10));
        System.out.println("O(log n) powerFast(2,10): " + powerFast(2, 10));
        System.out.println("O(2^n) fib(10): " + fib(10));
    }
}
