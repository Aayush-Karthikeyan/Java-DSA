public class code {

    // ===================================================================
    // TOPIC: DIVIDE & CONQUER
    //
    // The strategy:
    //   1. DIVIDE   — split the problem into smaller subproblems
    //   2. CONQUER  — solve each subproblem recursively
    //   3. COMBINE  — merge the results back together
    //
    // Covered: Merge Sort, Quick Sort, Search in Sorted+Rotated Array
    // ===================================================================


    // ============================================================
    // 1. MERGE SORT
    // Idea: split array in half, sort each half, then merge them.
    // Time:  O(n log n)  always (best, average, worst)
    // Space: O(n)        needs a temporary array during merge
    // Stable sort: yes (equal elements keep their relative order)
    // ============================================================

    // --- MERGE step: combine two sorted halves into one sorted array ---
    // arr[start..mid] is sorted, arr[mid+1..end] is sorted
    // We merge them into the same arr[start..end]
    public static void merge(int[] arr, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];  // temporary array to hold merged result

        int i = start;       // pointer for the LEFT half
        int j = mid + 1;     // pointer for the RIGHT half
        int k = 0;           // pointer for the temp array

        // compare front elements of both halves, pick the smaller one
        while (i <= mid && j <= end) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];   // left element is smaller, take it
            } else {
                temp[k++] = arr[j++];   // right element is smaller, take it
            }
        }

        // if any elements remain in the left half, copy them
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // if any elements remain in the right half, copy them
        while (j <= end) {
            temp[k++] = arr[j++];
        }

        // copy merged result back into the original array
        for (int l = 0; l < temp.length; l++) {
            arr[start + l] = temp[l];   // start + l maps temp index back to original array index
        }
    }

    // --- MERGE SORT: recursively split then merge ---
    public static void mergeSort(int[] arr, int start, int end) {
        if (start >= end) return;           // base case: 0 or 1 element is already sorted

        int mid = start + (end - start) / 2;  // find midpoint (avoids overflow vs (start+end)/2)

        mergeSort(arr, start, mid);         // DIVIDE + CONQUER: sort left half
        mergeSort(arr, mid + 1, end);       // DIVIDE + CONQUER: sort right half
        merge(arr, start, mid, end);        // COMBINE: merge both sorted halves
    }


    // ============================================================
    // 2. QUICK SORT
    // Idea: pick a pivot, put everything smaller to its left and
    //       everything larger to its right (partition), then
    //       recursively sort both sides.
    // Time:  O(n log n) average,  O(n²) worst case (sorted array + bad pivot)
    // Space: O(log n) call stack (in-place, no extra array)
    // NOT stable: equal elements may change relative order
    // ============================================================

    // --- PARTITION step: place pivot in its correct sorted position ---
    // We use the LAST element as pivot.
    // All elements < pivot go to the left, all > pivot go to the right.
    // Returns the final index of the pivot.
    public static int partition(int[] arr, int start, int end) {
        int pivot = arr[end];   // choose last element as pivot
        int i = start - 1;     // i tracks the boundary of "elements less than pivot"

        for (int j = start; j < end; j++) {       // j scans through the array
            if (arr[j] <= pivot) {                 // current element belongs on the left side
                i++;                               // expand the "less than" region
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // place pivot in its correct position (right after the "less than" region)
        int temp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = temp;

        return i + 1;           // return pivot's final index
    }

    // --- QUICK SORT: recursively sort around the pivot ---
    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) return;           // base case: 0 or 1 element

        int pivotIdx = partition(arr, start, end);  // put pivot in correct place

        quickSort(arr, start, pivotIdx - 1);         // sort left side (elements < pivot)
        quickSort(arr, pivotIdx + 1, end);           // sort right side (elements > pivot)
        // NOTE: pivot itself is already in correct place — no need to include it
    }


    // ============================================================
    // 3. SEARCH IN SORTED & ROTATED ARRAY
    // A sorted array that has been rotated at some pivot point.
    // e.g. [1,2,3,4,5] rotated → [4,5,1,2,3]
    //
    // Normal binary search doesn't work directly because not fully sorted.
    // Key observation: when you split in half, AT LEAST ONE HALF is always sorted.
    // Use that sorted half to decide which side to search.
    //
    // Time: O(log n)
    // ============================================================

    public static int searchRotated(int[] arr, int target, int start, int end) {
        if (start > end) return -1;   // base case: target not found

        int mid = start + (end - start) / 2;

        if (arr[mid] == target) return mid;   // found it at mid

        // Check which half is sorted
        if (arr[start] <= arr[mid]) {
            // LEFT half is sorted (arr[start] to arr[mid] is in order)
            if (target >= arr[start] && target < arr[mid]) {
                // target lies within the sorted left half → search left
                return searchRotated(arr, target, start, mid - 1);
            } else {
                // target is outside the left half → must be in right half
                return searchRotated(arr, target, mid + 1, end);
            }
        } else {
            // RIGHT half is sorted (arr[mid] to arr[end] is in order)
            if (target > arr[mid] && target <= arr[end]) {
                // target lies within the sorted right half → search right
                return searchRotated(arr, target, mid + 1, end);
            } else {
                // target is outside the right half → must be in left half
                return searchRotated(arr, target, start, mid - 1);
            }
        }
    }


    // --- UTILITY: print an array ---
    public static void printArr(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }


    public static void main(String[] args) {

        // ----- 1. MERGE SORT -----
        System.out.println("===== Merge Sort =====");
        int[] arr1 = {38, 27, 43, 3, 9, 82, 10};
        System.out.print("Before: "); printArr(arr1);
        mergeSort(arr1, 0, arr1.length - 1);
        System.out.print("After:  "); printArr(arr1);  // [3, 9, 10, 27, 38, 43, 82]
        System.out.println();

        int[] arr2 = {5, 1, 4, 2, 8};
        System.out.print("Before: "); printArr(arr2);
        mergeSort(arr2, 0, arr2.length - 1);
        System.out.print("After:  "); printArr(arr2);  // [1, 2, 4, 5, 8]
        System.out.println();


        // ----- 2. QUICK SORT -----
        System.out.println("===== Quick Sort =====");
        int[] arr3 = {64, 34, 25, 12, 22, 11, 90};
        System.out.print("Before: "); printArr(arr3);
        quickSort(arr3, 0, arr3.length - 1);
        System.out.print("After:  "); printArr(arr3);  // [11, 12, 22, 25, 34, 64, 90]
        System.out.println();

        int[] arr4 = {10, 7, 8, 9, 1, 5};
        System.out.print("Before: "); printArr(arr4);
        quickSort(arr4, 0, arr4.length - 1);
        System.out.print("After:  "); printArr(arr4);  // [1, 5, 7, 8, 9, 10]
        System.out.println();


        // ----- 3. SEARCH IN SORTED & ROTATED ARRAY -----
        System.out.println("===== Search in Sorted & Rotated Array =====");
        int[] rotated1 = {4, 5, 6, 7, 0, 1, 2};   // [0,1,2,4,5,6,7] rotated at index 4
        System.out.println("Array: "); printArr(rotated1);
        System.out.println("Search 0: index " + searchRotated(rotated1, 0, 0, rotated1.length - 1));  // 4
        System.out.println("Search 4: index " + searchRotated(rotated1, 4, 0, rotated1.length - 1));  // 0
        System.out.println("Search 3: index " + searchRotated(rotated1, 3, 0, rotated1.length - 1));  // -1 (not found)
        System.out.println();

        int[] rotated2 = {6, 7, 1, 2, 3, 4, 5};
        System.out.println("Array: "); printArr(rotated2);
        System.out.println("Search 3: index " + searchRotated(rotated2, 3, 0, rotated2.length - 1));  // 4
        System.out.println("Search 6: index " + searchRotated(rotated2, 6, 0, rotated2.length - 1));  // 0
    }
}
