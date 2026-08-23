import java.util.*;

public class SegmentTrees {

    // ================================================================
    // RANGE SUM SEGMENT TREE
    // Each node stores the sum of one inclusive array segment.
    // ================================================================
    static class SumSegmentTree {
        private final int size;
        private final long[] tree;

        /*
         * Problem:
         * Build a segment tree that stores range sums.
         *
         * Pattern:
         * Divide and Conquer
         *
         * Approach:
         * 1. A leaf stores one array value.
         * 2. Split every larger segment at its midpoint.
         * 3. Build the left and right child segments.
         * 4. Store left sum + right sum at the parent.
         *
         * Time: O(n)
         * Space: O(n) for the tree; O(log n) recursion stack
         */
        SumSegmentTree(int[] values) {
            requireNonEmpty(values);
            size = values.length;
            tree = new long[4 * size];
            build(values, 0, 0, size - 1);
        }

        private void build(int[] values, int node,
                           int segmentStart, int segmentEnd) {
            if (segmentStart == segmentEnd) {
                tree[node] = values[segmentStart];
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            build(values, leftChild, segmentStart, middle);
            build(values, rightChild, middle + 1, segmentEnd);
            tree[node] = tree[leftChild] + tree[rightChild];
        }

        /*
         * Problem:
         * Return the sum in the inclusive range [queryLeft, queryRight].
         *
         * Pattern:
         * Segment Tree Range Query
         *
         * Approach:
         * 1. No overlap contributes 0.
         * 2. Complete overlap returns the stored node sum.
         * 3. Partial overlap queries both children.
         * 4. Add the two child answers.
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        long rangeSum(int queryLeft, int queryRight) {
            validateRange(queryLeft, queryRight, size);
            return query(
                    0, 0, size - 1, queryLeft, queryRight);
        }

        private long query(int node, int segmentStart, int segmentEnd,
                           int queryLeft, int queryRight) {
            // No overlap: this segment contributes nothing to a sum.
            if (segmentEnd < queryLeft
                    || segmentStart > queryRight) {
                return 0;
            }

            // Complete overlap: use the answer already stored here.
            if (queryLeft <= segmentStart
                    && segmentEnd <= queryRight) {
                return tree[node];
            }

            // Partial overlap: split and combine both relevant parts.
            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            long leftSum = query(
                    2 * node + 1, segmentStart, middle,
                    queryLeft, queryRight);
            long rightSum = query(
                    2 * node + 2, middle + 1, segmentEnd,
                    queryLeft, queryRight);

            return leftSum + rightSum;
        }

        /*
         * Problem:
         * Replace one array value and update all affected range sums.
         *
         * Pattern:
         * Segment Tree Point Update
         *
         * Approach:
         * 1. Follow only the child segment containing the index.
         * 2. Replace the value at the leaf.
         * 3. Recompute each parent while recursion returns.
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        void update(int index, long newValue) {
            validateIndex(index, size);
            updateValue(0, 0, size - 1, index, newValue);
        }

        private void updateValue(int node, int segmentStart,
                                 int segmentEnd, int index,
                                 long newValue) {
            if (segmentStart == segmentEnd) {
                tree[node] = newValue;
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            if (index <= middle) {
                updateValue(leftChild, segmentStart, middle,
                        index, newValue);
            } else {
                updateValue(rightChild, middle + 1, segmentEnd,
                        index, newValue);
            }

            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }

    // ================================================================
    // RANGE MAXIMUM SEGMENT TREE
    // Each node stores the maximum value in one inclusive segment.
    // ================================================================
    static class MaxSegmentTree {
        private final int size;
        private final int[] tree;

        /*
         * Problem:
         * Build a segment tree that stores range maximums.
         *
         * Pattern:
         * Divide and Conquer
         *
         * Approach:
         * 1. Store one input value at each leaf.
         * 2. Build both halves recursively.
         * 3. Store max(left child, right child) at each parent.
         *
         * Time: O(n)
         * Space: O(n) for the tree; O(log n) recursion stack
         */
        MaxSegmentTree(int[] values) {
            requireNonEmpty(values);
            size = values.length;
            tree = new int[4 * size];
            build(values, 0, 0, size - 1);
        }

        private void build(int[] values, int node,
                           int segmentStart, int segmentEnd) {
            if (segmentStart == segmentEnd) {
                tree[node] = values[segmentStart];
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            build(values, leftChild, segmentStart, middle);
            build(values, rightChild, middle + 1, segmentEnd);
            tree[node] = Math.max(
                    tree[leftChild], tree[rightChild]);
        }

        /*
         * Problem:
         * Return the maximum in an inclusive query range.
         *
         * Pattern:
         * Segment Tree Range Query
         *
         * Approach:
         * 1. No overlap returns negative infinity.
         * 2. Complete overlap returns the stored maximum.
         * 3. Partial overlap returns max(left answer, right answer).
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        int rangeMax(int queryLeft, int queryRight) {
            validateRange(queryLeft, queryRight, size);
            return query(
                    0, 0, size - 1, queryLeft, queryRight);
        }

        private int query(int node, int segmentStart, int segmentEnd,
                          int queryLeft, int queryRight) {
            if (segmentEnd < queryLeft
                    || segmentStart > queryRight) {
                return Integer.MIN_VALUE;
            }

            if (queryLeft <= segmentStart
                    && segmentEnd <= queryRight) {
                return tree[node];
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftMax = query(
                    2 * node + 1, segmentStart, middle,
                    queryLeft, queryRight);
            int rightMax = query(
                    2 * node + 2, middle + 1, segmentEnd,
                    queryLeft, queryRight);

            return Math.max(leftMax, rightMax);
        }

        /*
         * Problem:
         * Replace one value and update all affected range maximums.
         *
         * Pattern:
         * Segment Tree Point Update
         *
         * Approach:
         * 1. Follow the path containing the target index.
         * 2. Replace the leaf value.
         * 3. Recompute parent maximums while returning.
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        void update(int index, int newValue) {
            validateIndex(index, size);
            updateValue(0, 0, size - 1, index, newValue);
        }

        private void updateValue(int node, int segmentStart,
                                 int segmentEnd, int index,
                                 int newValue) {
            if (segmentStart == segmentEnd) {
                tree[node] = newValue;
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            if (index <= middle) {
                updateValue(leftChild, segmentStart, middle,
                        index, newValue);
            } else {
                updateValue(rightChild, middle + 1, segmentEnd,
                        index, newValue);
            }

            tree[node] = Math.max(
                    tree[leftChild], tree[rightChild]);
        }
    }

    // ================================================================
    // RANGE MINIMUM SEGMENT TREE
    // Each node stores the minimum value in one inclusive segment.
    // ================================================================
    static class MinSegmentTree {
        private final int size;
        private final int[] tree;

        /*
         * Problem:
         * Build a segment tree that stores range minimums.
         *
         * Pattern:
         * Divide and Conquer
         *
         * Approach:
         * 1. Store one input value at each leaf.
         * 2. Build both halves recursively.
         * 3. Store min(left child, right child) at each parent.
         *
         * Time: O(n)
         * Space: O(n) for the tree; O(log n) recursion stack
         */
        MinSegmentTree(int[] values) {
            requireNonEmpty(values);
            size = values.length;
            tree = new int[4 * size];
            build(values, 0, 0, size - 1);
        }

        private void build(int[] values, int node,
                           int segmentStart, int segmentEnd) {
            if (segmentStart == segmentEnd) {
                tree[node] = values[segmentStart];
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            build(values, leftChild, segmentStart, middle);
            build(values, rightChild, middle + 1, segmentEnd);
            tree[node] = Math.min(
                    tree[leftChild], tree[rightChild]);
        }

        /*
         * Problem:
         * Return the minimum in an inclusive query range.
         *
         * Pattern:
         * Segment Tree Range Query
         *
         * Approach:
         * 1. No overlap returns positive infinity.
         * 2. Complete overlap returns the stored minimum.
         * 3. Partial overlap returns min(left answer, right answer).
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        int rangeMin(int queryLeft, int queryRight) {
            validateRange(queryLeft, queryRight, size);
            return query(
                    0, 0, size - 1, queryLeft, queryRight);
        }

        private int query(int node, int segmentStart, int segmentEnd,
                          int queryLeft, int queryRight) {
            if (segmentEnd < queryLeft
                    || segmentStart > queryRight) {
                return Integer.MAX_VALUE;
            }

            if (queryLeft <= segmentStart
                    && segmentEnd <= queryRight) {
                return tree[node];
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftMin = query(
                    2 * node + 1, segmentStart, middle,
                    queryLeft, queryRight);
            int rightMin = query(
                    2 * node + 2, middle + 1, segmentEnd,
                    queryLeft, queryRight);

            return Math.min(leftMin, rightMin);
        }

        /*
         * Problem:
         * Replace one value and update all affected range minimums.
         *
         * Pattern:
         * Segment Tree Point Update
         *
         * Approach:
         * 1. Follow the path containing the target index.
         * 2. Replace the leaf value.
         * 3. Recompute parent minimums while returning.
         *
         * Time: O(log n)
         * Space: O(log n) recursion stack
         */
        void update(int index, int newValue) {
            validateIndex(index, size);
            updateValue(0, 0, size - 1, index, newValue);
        }

        private void updateValue(int node, int segmentStart,
                                 int segmentEnd, int index,
                                 int newValue) {
            if (segmentStart == segmentEnd) {
                tree[node] = newValue;
                return;
            }

            int middle = segmentStart
                    + (segmentEnd - segmentStart) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            if (index <= middle) {
                updateValue(leftChild, segmentStart, middle,
                        index, newValue);
            } else {
                updateValue(rightChild, middle + 1, segmentEnd,
                        index, newValue);
            }

            tree[node] = Math.min(
                    tree[leftChild], tree[rightChild]);
        }
    }

    private static void requireNonEmpty(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(
                    "Segment tree requires a non-empty array");
        }
    }

    private static void validateIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index must be between 0 and " + (size - 1));
        }
    }

    private static void validateRange(int left, int right, int size) {
        if (left < 0 || right >= size || left > right) {
            throw new IllegalArgumentException(
                    "Range must satisfy 0 <= left <= right < " + size);
        }
    }

    public static void main(String[] args) {
        int[] sumValues = {1, 2, 3, 4, 5, 6, 7, 8};
        SumSegmentTree sumTree = new SumSegmentTree(sumValues);

        System.out.println("Sum [2, 5]: " + sumTree.rangeSum(2, 5));
        sumTree.update(2, 2);
        System.out.println("Sum [2, 5] after index 2 -> 2: "
                + sumTree.rangeSum(2, 5));

        int[] values = {6, 8, -1, 2, 17, 1, 3, 2, 4};
        MaxSegmentTree maxTree = new MaxSegmentTree(values);
        MinSegmentTree minTree = new MinSegmentTree(values);

        System.out.println("Maximum [2, 5]: "
                + maxTree.rangeMax(2, 5));
        maxTree.update(4, 0);
        System.out.println("Maximum [2, 5] after index 4 -> 0: "
                + maxTree.rangeMax(2, 5));

        System.out.println("Minimum [2, 5]: "
                + minTree.rangeMin(2, 5));
        minTree.update(2, 10);
        System.out.println("Minimum [2, 5] after index 2 -> 10: "
                + minTree.rangeMin(2, 5));
    }
}
