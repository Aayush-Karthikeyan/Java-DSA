# 20. Divide & Conquer — Key Notes

---

## What is Divide & Conquer?
A problem-solving strategy with 3 steps:
1. **DIVIDE** — split the problem into smaller subproblems of the same type
2. **CONQUER** — solve each subproblem recursively (base case: small enough to solve directly)
3. **COMBINE** — merge the subproblem results to get the final answer

Examples: Merge Sort, Quick Sort, Binary Search, Fast Exponentiation.

---

## 1. Merge Sort

### Idea
- Split the array in half repeatedly until you have arrays of size 1 (already sorted).
- Then **merge** pairs of sorted arrays back together.

### Visualization
```
[38, 27, 43, 3, 9, 82, 10]
         DIVIDE
   [38, 27, 43]   [3, 9, 82, 10]
   [38] [27,43]   [3,9] [82,10]
   [38] [27][43]  [3][9] [82][10]
         CONQUER (base cases — size 1)
         COMBINE (merge upward)
   [38] [27,43]   [3,9] [10,82]
   [27,38,43]     [3,9,10,82]
        [3,9,10,27,38,43,82]
```

### Merge Step (the core)
- Two pointers `i` (left half) and `j` (right half), compare and pick the smaller.
- Requires a **temporary array** of size `n`.
- After comparing, copy remaining elements from whichever half is not exhausted.

```java
mergeSort(arr, start, end):
    if start >= end: return          // base case
    mid = start + (end-start)/2
    mergeSort(arr, start, mid)       // sort left
    mergeSort(arr, mid+1, end)       // sort right
    merge(arr, start, mid, end)      // combine
```

### Properties
| Property | Value |
|----------|-------|
| Time (best) | O(n log n) |
| Time (average) | O(n log n) |
| Time (worst) | O(n log n) — always! |
| Space | O(n) — temporary array |
| Stable | **Yes** — equal elements keep original order |
| In-place | No |

> Use Merge Sort when you need **guaranteed O(n log n)** or a **stable sort**.

---

## 2. Quick Sort

### Idea
- Pick a **pivot** element.
- **Partition** the array: all elements < pivot go left, all > pivot go right.
- Pivot is now in its final correct position.
- Recursively sort the left and right sides.

### Partition Step (the core)
```
pivot = arr[end]   (last element)
i = start - 1     (boundary of "less than pivot" region)

for j = start to end-1:
    if arr[j] <= pivot:
        i++
        swap(arr[i], arr[j])    // move small element into left region

swap(arr[i+1], arr[end])        // place pivot in its correct spot
return i+1                      // pivot's final index
```

### Visualization
```
[10, 7, 8, 9, 1, 5]   pivot = 5
 j scans left to right:
   1 ≤ 5 → swap into left region
 After partition: [1, 5, 8, 9, 7, 10]  (pivot 5 at index 1 — its final place)
 Recurse on [1] and [8, 9, 7, 10]
```

### Properties
| Property | Value |
|----------|-------|
| Time (best) | O(n log n) |
| Time (average) | O(n log n) |
| Time (worst) | **O(n²)** — when array is already sorted + pivot is always smallest/largest |
| Space | O(log n) — recursive call stack only |
| Stable | **No** |
| In-place | **Yes** |

> Use Quick Sort when you need **in-place sorting** and average case matters more than worst case.

### Worst Case of Quick Sort
- Happens when the pivot is always the smallest or largest element.
- Example: already sorted array `[1, 2, 3, 4, 5]` with last element as pivot.
- Each partition step only removes 1 element → n partitions → O(n²).
- **Fix:** random pivot selection, or median-of-three pivot.

---

## Merge Sort vs Quick Sort

| | Merge Sort | Quick Sort |
|--|-----------|-----------|
| Worst case | O(n log n) | O(n²) |
| Average case | O(n log n) | O(n log n) |
| Extra space | O(n) | O(log n) |
| Stable | Yes | No |
| In-place | No | Yes |
| Better for | Linked lists, external sorting, stability needed | Arrays, average-case speed, cache performance |

> In practice, **Quick Sort is usually faster** on real data due to better cache behavior, even though Merge Sort has a better worst case.

---

## 3. Search in Sorted & Rotated Array

### What is a Rotated Array?
A sorted array shifted at some pivot point:
```
Original:  [1, 2, 3, 4, 5, 6, 7]
Rotated:   [4, 5, 6, 7, 1, 2, 3]   (rotated at index 3)
```

### Why Normal Binary Search Fails
The array is not fully sorted, so `mid` comparison doesn't tell you which side to go.

### Key Insight
When you split a rotated array at `mid`, **at least one half is always fully sorted**.
- If `arr[start] <= arr[mid]` → **left half is sorted**
- Otherwise → **right half is sorted**

Use the sorted half to check if the target is in its range. If yes → search that half. If no → search the other half.

```
searchRotated(arr, target, start, end):
    if start > end: return -1
    mid = (start + end) / 2
    if arr[mid] == target: return mid

    if arr[start] <= arr[mid]:           // left half is sorted
        if arr[start] <= target < arr[mid]:
            search left (start, mid-1)
        else:
            search right (mid+1, end)
    else:                                // right half is sorted
        if arr[mid] < target <= arr[end]:
            search right (mid+1, end)
        else:
            search left (start, mid-1)
```

### Properties
| Property | Value |
|----------|-------|
| Time | O(log n) |
| Space | O(log n) recursive / O(1) iterative |

---

## General D&C Time Complexity (Master Theorem intuition)
```
T(n) = a × T(n/b) + O(n^d)

Merge Sort: T(n) = 2T(n/2) + O(n)  →  O(n log n)
Quick Sort: T(n) = 2T(n/2) + O(n)  →  O(n log n) average
Binary Search: T(n) = T(n/2) + O(1) → O(log n)
```
Each "level" of recursion does O(n) work, and there are O(log n) levels → O(n log n).

---

## Common Pitfalls
1. **`mid = (start + end) / 2`** — can overflow for large arrays. Use `start + (end - start) / 2` instead.
2. **Merge Sort space:** forgetting to allocate temp array inside `merge()` — make a fresh one each call or pass a shared one.
3. **Quick Sort worst case:** don't use last element as pivot on nearly-sorted data in production — use random pivot.
4. **Rotated array search:** the condition must be `arr[start] <= arr[mid]` (not `<`) to handle the case where `start == mid`.
5. **Off-by-one in merge:** left half is `[start, mid]`, right half is `[mid+1, end]` — the `mid` belongs to the left.
