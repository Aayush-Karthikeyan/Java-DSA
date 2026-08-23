import java.util.*;

// ================================================================
// TOPIC: Heaps
// A heap is a Complete Binary Tree that follows a priority rule.
//
// Min Heap: parent <= children, so smallest value is at root.
// Max Heap: parent >= children, so largest value is at root.
//
// In Java, PriorityQueue is implemented using a heap.
// ================================================================

public class Heaps {

    // ================================================================
    // STUDENT CLASS FOR PRIORITYQUEUE OF OBJECTS
    // Comparable tells Java how to compare two Student objects.
    //
    // this.rank - s2.rank means smaller rank has higher priority.
    // Example: rank 1 comes before rank 4.
    // ================================================================
    static class Student implements Comparable<Student> {
        String name;
        int rank;

        Student(String name, int rank) {
            this.name = name;
            this.rank = rank;
        }

        @Override
        public int compareTo(Student s2) {
            return this.rank - s2.rank;
        }

        @Override
        public String toString() {
            return name + " rank=" + rank;
        }
    }

    // ================================================================
    // CUSTOM MIN HEAP USING ARRAYLIST
    //
    // Heap is stored as an array:
    // parent index = (childIndex - 1) / 2
    // left child   = 2 * parentIndex + 1
    // right child  = 2 * parentIndex + 2
    // ================================================================
    static class Heap {
        ArrayList<Integer> arr = new ArrayList<>();

        boolean isEmpty() {
            return arr.isEmpty();
        }

        // ================================================================
        // ADD IN HEAP
        // 1. Add new data at the end.
        // 2. Keep swapping with parent while child is smaller.
        //
        // This upward movement is called upheap / bubble up.
        // Time: O(log n)
        // ================================================================
        void add(int data) {
            arr.add(data);

            int child = arr.size() - 1;
            int parent = (child - 1) / 2;

            while (child > 0 && arr.get(child) < arr.get(parent)) {
                swap(child, parent);
                child = parent;
                parent = (child - 1) / 2;
            }
        }

        // Peek returns the highest priority value.
        // In min heap, highest priority = smallest value.
        // Time: O(1)
        int peek() {
            if (arr.isEmpty()) {
                throw new NoSuchElementException("heap is empty");
            }
            return arr.get(0);
        }

        // ================================================================
        // REMOVE FROM HEAP
        // In min heap, remove returns the smallest value.
        //
        // 1. Store root value.
        // 2. Swap root with last element.
        // 3. Remove last element.
        // 4. Heapify root downward to fix heap.
        //
        // Time: O(log n)
        // ================================================================
        int remove() {
            if (arr.isEmpty()) {
                throw new NoSuchElementException("heap is empty");
            }

            int data = arr.get(0);

            swap(0, arr.size() - 1);
            arr.remove(arr.size() - 1);

            if (!arr.isEmpty()) {
                heapify(0);
            }

            return data;
        }

        // ================================================================
        // HEAPIFY FOR MIN HEAP
        // Assumes left and right subtrees are already valid heaps.
        // Fixes the node at index i by moving it down.
        //
        // Time: O(log n)
        // ================================================================
        private void heapify(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int minIdx = i;

            if (left < arr.size() && arr.get(left) < arr.get(minIdx)) {
                minIdx = left;
            }

            if (right < arr.size() && arr.get(right) < arr.get(minIdx)) {
                minIdx = right;
            }

            if (minIdx != i) {
                swap(i, minIdx);
                heapify(minIdx);
            }
        }

        private void swap(int i, int j) {
            int temp = arr.get(i);
            arr.set(i, arr.get(j));
            arr.set(j, temp);
        }
    }

    // ================================================================
    // HEAPIFY FOR HEAP SORT
    // This version works on an int[] and treats only arr[0..size-1]
    // as the active heap.
    //
    // It builds a max heap because heap sort puts the largest element
    // at the end one by one to sort in ascending order.
    // ================================================================
    static void heapifyMax(int[] arr, int i, int size) {
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int maxIdx = i;

        if (left < size && arr[left] > arr[maxIdx]) {
            maxIdx = left;
        }

        if (right < size && arr[right] > arr[maxIdx]) {
            maxIdx = right;
        }

        if (maxIdx != i) {
            int temp = arr[i];
            arr[i] = arr[maxIdx];
            arr[maxIdx] = temp;

            heapifyMax(arr, maxIdx, size);
        }
    }

    // ================================================================
    // HEAP SORT
    // Step 1: Build max heap.
    // Step 2: Repeatedly swap largest/root with last active element.
    // Step 3: Reduce heap size and heapify root.
    //
    // Time: O(n log n)
    // Space: O(1)
    // ================================================================
    static void heapSort(int[] arr) {
        int n = arr.length;

        // Build max heap from bottom non-leaf nodes upward.
        for (int i = n / 2; i >= 0; i--) {
            heapifyMax(arr, i, n);
        }

        // Move current largest to the end.
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapifyMax(arr, 0, i);
        }
    }

    // ================================================================
    // DEMO 1: JAVA PRIORITYQUEUE WITH INTEGERS
    // Default PriorityQueue is a min heap.
    // Comparator.reverseOrder() makes it behave like a max heap.
    // ================================================================
    static void priorityQueueDemo() {
        PriorityQueue<Integer> minPQ = new PriorityQueue<>();
        minPQ.add(3);
        minPQ.add(4);
        minPQ.add(1);
        minPQ.add(7);

        System.out.print("Java min PriorityQueue: ");
        while (!minPQ.isEmpty()) {
            System.out.print(minPQ.peek() + " ");
            minPQ.remove();
        }
        System.out.println();

        PriorityQueue<Integer> maxPQ = new PriorityQueue<>(Comparator.reverseOrder());
        maxPQ.add(3);
        maxPQ.add(4);
        maxPQ.add(1);
        maxPQ.add(7);

        System.out.print("Java max PriorityQueue: ");
        while (!maxPQ.isEmpty()) {
            System.out.print(maxPQ.peek() + " ");
            maxPQ.remove();
        }
        System.out.println();
    }

    // ================================================================
    // DEMO 2: PRIORITYQUEUE WITH CUSTOM OBJECTS
    // Student implements Comparable, so PriorityQueue knows rank order.
    // ================================================================
    static void studentPriorityQueueDemo() {
        PriorityQueue<Student> pq = new PriorityQueue<>();

        pq.add(new Student("A", 4));
        pq.add(new Student("B", 5));
        pq.add(new Student("C", 2));
        pq.add(new Student("D", 12));

        System.out.println("Student PriorityQueue by rank:");
        while (!pq.isEmpty()) {
            System.out.println(pq.peek());
            pq.remove();
        }
    }

    // ================================================================
    // MAIN: sample run for all heap topics
    // ================================================================
    public static void main(String[] args) {
        priorityQueueDemo();
        System.out.println();

        studentPriorityQueueDemo();
        System.out.println();

        Heap heap = new Heap();
        heap.add(3);
        heap.add(4);
        heap.add(1);
        heap.add(5);

        System.out.print("Custom min heap remove order: ");
        while (!heap.isEmpty()) {
            System.out.print(heap.remove() + " ");
        }
        System.out.println();

        int[] arr = {1, 2, 4, 5, 3};
        heapSort(arr);
        System.out.println("Heap sort: " + Arrays.toString(arr));
    }
}
