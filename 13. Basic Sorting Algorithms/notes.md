# Sorting Algorithms

## What is Sorting?
Arranging array elements in a specific order. Foundation for many interview problems.

---

## Bubble Sort
- Compare adjacent pairs, swap if out of order → largest element "bubbles" to end each pass
- Optimization: if no swap in a pass → array is already sorted (break early)

| Case    | Time   | Space |
|---------|--------|-------|
| Best    | O(n)   | O(1)  |
| Average | O(n²)  | O(1)  |
| Worst   | O(n²)  | O(1)  |

🧠 Best case O(n) only with the `swapped` flag — always add it
⚠️ Inner loop goes up to `n - 1 - i` (sorted tail shrinks each pass)

---

## Selection Sort
- Find the minimum in the unsorted portion → swap it to the front
- No optimization possible — always does n² comparisons

| Case    | Time   | Space |
|---------|--------|-------|
| All     | O(n²)  | O(1)  |

🧠 Minimum swaps among O(n²) sorts → useful when write cost is high
⚠️ Does NOT benefit from a nearly sorted array (unlike bubble/insertion)

---

## Insertion Sort
- Like sorting cards in hand: pick element, shift larger elements right, insert in correct spot

| Case    | Time   | Space |
|---------|--------|-------|
| Best    | O(n)   | O(1)  |
| Average | O(n²)  | O(1)  |
| Worst   | O(n²)  | O(1)  |

🧠 Best for small arrays or nearly sorted data — used internally by Timsort
💡 Inner loop shifts (not swaps) → fewer writes than bubble sort

---

## Inbuilt Sort — Arrays.sort()
- Primitives: Dual-Pivot Quicksort → O(n log n) avg, O(log n) space
- Objects: Timsort → O(n log n) guaranteed, O(n) space

🧠 In interviews, always mention what type you're sorting
⚠️ Arrays.sort() on int[] sorts ascending by default — no comparator support for primitives

---

## Counting Sort
- Count frequency of each value, then reconstruct array in order
- NOT comparison-based → can beat O(n log n)

| Case | Time       | Space |
|------|------------|-------|
| All  | O(n + k)   | O(k)  |

> k = range of values (max element)

🧠 Use when: integers only, range k is small (k ≈ n)
⚠️ Does NOT work with negatives (without offset) or floats
⚠️ Wasteful if k >> n (e.g., single element with value 1,000,000)