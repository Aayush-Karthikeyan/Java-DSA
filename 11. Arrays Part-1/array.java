public class array {
    // ============================================================
    // 1. PASS BY REFERENCE
    // Arrays are passed by reference — original gets modified
    // ============================================================
    public static void update(int marks[]) {
        for (int i = 0; i < marks.length; i++) {
            marks[i] = marks[i] + 1; // modifies original array directly
        }
    }
 
    // ============================================================
    // 2. LINEAR SEARCH — O(n)
    // Check every element one by one
    // ============================================================
    public static int linearSearch(int arr[], int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) return i; // return index if found
        }
        return -1; // not found
    }
 
    // ============================================================
    // 3. LARGEST IN ARRAY — O(n)
    // Assume first is largest, update if bigger found
    // ============================================================
    public static int largest(int arr[]) {
        int max = arr[0]; // assume first element is the largest
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i]; // found a bigger one, update
            }
        }
        return max;
    }
 
    // ============================================================
    // 4. BINARY SEARCH — O(log n)
    // Only works on sorted arrays. Halves search space each step.
    // ============================================================
    public static int binarySearch(int arr[], int target) {
        int start = 0;
        int end = arr.length - 1;
 
        while (start <= end) {
            int mid = start + (end - start) / 2; // safe mid, avoids overflow
 
            if (arr[mid] == target) return mid;        // found it
            else if (arr[mid] < target) start = mid + 1; // target is in right half
            else end = mid - 1;                           // target is in left half
        }
        return -1; // not found
    }
 
    // ============================================================
    // 5. REVERSE AN ARRAY — O(n)
    // Two pointers swap from both ends moving inward
    // ============================================================
    public static void reverse(int arr[]) {
        int start = 0;
        int end = arr.length - 1;
 
        while (start < end) {
            // swap arr[start] and arr[end]
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++; // move inward from left
            end--;   // move inward from right
        }
    }
 
    // ============================================================
    // 6. PAIRS IN ARRAY — O(n²)
    // For every element, pair with every element after it
    // ============================================================
    public static void pairs(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) { // j starts at i+1 to avoid duplicates
                System.out.println(arr[i] + " " + arr[j]);
            }
        }
    }
 
    // ============================================================
    // 7. PRINT SUBARRAYS — O(n³)
    // i = start (freezes), j = end (walks), k = printer (sprints i to j)
    // ============================================================
    public static void subArrays(int arr[]) {
        for (int i = 0; i < arr.length; i++) {        // i freezes as subarray start
            for (int j = i; j < arr.length; j++) {    // j walks forward as subarray end
                for (int k = i; k <= j; k++) {        // k sprints from i to j and prints
                    System.out.print(arr[k] + " ");
                }
                System.out.println(); // new line after each subarray
            }
        }
    }
 
    // ============================================================
    // 8a. MAX SUBARRAY SUM — BRUTE FORCE — O(n³)
    // Check every subarray, calculate sum, track max
    // ============================================================
    public static int maxSumBrute(int arr[]) {
        int maxSum = Integer.MIN_VALUE; // most negative number so any sum beats it
 
        for (int i = 0; i < arr.length; i++) {        // i = subarray start
            for (int j = i; j < arr.length; j++) {    // j = subarray end
                int sum = 0;
                for (int k = i; k <= j; k++) {        // k = calculate sum from i to j
                    sum += arr[k];
                }
                maxSum = Math.max(maxSum, sum);        // keep the bigger of maxSum and sum
            }
        }
        return maxSum;
    }
 
    // ============================================================
    // 8b. MAX SUBARRAY SUM — PREFIX SUM — O(n²)
    // Pre-calculate running totals. Sum(i to j) = prefix[j] - prefix[i-1]
    // Think: bank balance. Subtract what came before to get just that period.
    // ============================================================
    public static int maxSumPrefix(int arr[]) {
        int maxSum = Integer.MIN_VALUE;
        int prefix[] = new int[arr.length]; // running total array
 
        prefix[0] = arr[0];                             // first element is just itself
        for (int i = 1; i < arr.length; i++) {
            prefix[i] = prefix[i-1] + arr[i];           // prev balance + today's amount
        }
 
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                int sum = i == 0                         // if subarray starts at index 0...
                        ? prefix[j]                      // ...sum is just prefix[j]
                        : prefix[j] - prefix[i-1];      // ...else subtract what came before i
                maxSum = Math.max(maxSum, sum);
            }
        }
        return maxSum;
    }
 
    // ============================================================
    // 8c. MAX SUBARRAY SUM — KADANE'S ALGORITHM — O(n) ✅
    // If running sum goes negative → reset to 0 (start fresh)
    // A negative sum only drags future elements down
    // THIS IS THE ONE TO USE IN INTERVIEWS
    // ============================================================
    public static int maxSumKadane(int arr[]) {
        int maxSum = Integer.MIN_VALUE; // track overall best sum
        int currentSum = 0;             // track current running sum
 
        for (int i = 0; i < arr.length; i++) {
            currentSum += arr[i];                   // add current element to running sum
            maxSum = Math.max(maxSum, currentSum);  // update max if current is bigger
            if (currentSum < 0) {                   // if sum went negative...
                currentSum = 0;                     // ...throw it away, start fresh
            }
        }
        return maxSum;
    }
 
    // ============================================================
    // MAIN — test everything
    // ============================================================
    public static void main(String[] args) {
        int arr[] = {-2, 1, -3, 4, -1, 2, 1};
        int sorted[] = {1, 2, 3, 4, 5};
        int marks[] = {97, 98, 99};
 
        System.out.println("=== Pass by Reference ===");
        update(marks);
        for (int m : marks) System.out.print(m + " "); // 98 99 100
        System.out.println();
 
        System.out.println("\n=== Linear Search ===");
        System.out.println(linearSearch(arr, 4)); // 3
 
        System.out.println("\n=== Largest ===");
        System.out.println(largest(arr)); // 4
 
        System.out.println("\n=== Binary Search ===");
        System.out.println(binarySearch(sorted, 3)); // 2
 
        System.out.println("\n=== Reverse ===");
        reverse(sorted);
        for (int x : sorted) System.out.print(x + " "); // 5 4 3 2 1
        System.out.println();
 
        System.out.println("\n=== Pairs ===");
        pairs(new int[]{1, 2, 3});
 
        System.out.println("\n=== Subarrays ===");
        subArrays(new int[]{1, 2, 3});
 
        System.out.println("\n=== Max Subarray Sum ===");
        System.out.println("Brute:  " + maxSumBrute(arr));   // 6
        System.out.println("Prefix: " + maxSumPrefix(arr));  // 6
        System.out.println("Kadane: " + maxSumKadane(arr));  // 6
    }

}
