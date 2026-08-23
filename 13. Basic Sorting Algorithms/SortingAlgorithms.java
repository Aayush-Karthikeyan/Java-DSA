import java.util.Arrays;

public class SortingAlgorithms {

    // ===========================
    // BUBBLE SORT
    // ===========================
    static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {  // -i: last i elements already sorted
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;  // already sorted → O(n) best case
        }
    }

    // ===========================
    // SELECTION SORT
    // ===========================
    static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // swap minimum to current position
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    // ===========================
    // INSERTION SORT
    // ===========================
    static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];   // element to place
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j]; // shift right
                j--;
            }
            arr[j + 1] = key;   // insert in correct spot
        }
    }

    // ===========================
    // COUNTING SORT
    // ===========================
    static void countingSort(int[] arr) {
        int max = 0;
        for (int x : arr) if (x > max) max = x;

        int[] count = new int[max + 1];
        for (int x : arr) count[x]++;

        int idx = 0;
        for (int i = 0; i <= max; i++) {
            while (count[i]-- > 0) arr[idx++] = i;
        }
    }

    public static void main(String[] args) {

        // === Bubble Sort ===
        int[] a1 = {5, 1, 4, 2, 8};
        bubbleSort(a1);
        System.out.println("Bubble:    " + Arrays.toString(a1)); // [1, 2, 4, 5, 8]

        // === Selection Sort ===
        int[] a2 = {64, 25, 12, 22, 11};
        selectionSort(a2);
        System.out.println("Selection: " + Arrays.toString(a2)); // [11, 12, 22, 25, 64]

        // === Insertion Sort ===
        int[] a3 = {12, 11, 13, 5, 6};
        insertionSort(a3);
        System.out.println("Insertion: " + Arrays.toString(a3)); // [5, 6, 11, 12, 13]

        // === Inbuilt Sort ===
        int[] a4 = {3, 1, 4, 1, 5, 9, 2, 6};
        Arrays.sort(a4);
        System.out.println("Inbuilt:   " + Arrays.toString(a4)); // [1, 1, 2, 3, 4, 5, 6, 9]

        // === Counting Sort ===
        int[] a5 = {4, 2, 2, 8, 3, 3, 1};
        countingSort(a5);
        System.out.println("Counting:  " + Arrays.toString(a5)); // [1, 2, 2, 3, 3, 4, 8]
    }
}