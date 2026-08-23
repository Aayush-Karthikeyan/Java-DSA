# 23. ArrayLists — Key Notes

---

## What is an ArrayList?
- A **resizable array** from `java.util.ArrayList`.
- Unlike arrays (`int[]`), it **grows/shrinks** automatically.
- Can only hold **objects** — use wrapper classes: `Integer`, `Double`, `Character`, etc.
- Declared with generics: `ArrayList<Integer> list = new ArrayList<>();`

---

## ArrayList vs Array

| | Array | ArrayList |
|--|-------|-----------|
| Size | Fixed | Resizable |
| Type | Primitives + objects | Objects only (wrapper classes) |
| Length | `.length` (field) | `.size()` (method) |
| Access | `arr[i]` | `.get(i)` |

---

## Operations on ArrayList

| Method | What it does |
|--------|--------------|
| `add(value)` | Adds to the end |
| `add(index, value)` | Inserts at index, shifts rest right |
| `get(index)` | Returns element at index |
| `set(index, value)` | Replaces element at index |
| `remove(index)` | Removes by index (int argument) |
| `remove(Integer.valueOf(x))` | Removes by value |
| `contains(value)` | Returns true/false |
| `indexOf(value)` | First index of value, or -1 |
| `size()` | Number of elements |

> **Gotcha:** `list.remove(2)` removes element AT index 2. `list.remove(Integer.valueOf(2))` removes the VALUE 2. Java picks the index version for `int` arguments!

---

## Print in Reverse
```java
for (int i = list.size() - 1; i >= 0; i--) {
    System.out.print(list.get(i) + " ");
}
```

---

## Find Maximum
Start with `Integer.MIN_VALUE` so any element in the list beats it:
```java
int max = Integer.MIN_VALUE;
for (int i = 0; i < list.size(); i++) {
    if (max < list.get(i)) max = list.get(i);
}
```

---

## Swap 2 Numbers (by index)
```java
int temp = list.get(idx1);
list.set(idx1, list.get(idx2));
list.set(idx2, temp);
```
> Can't do `arr[i] = arr[j]` style — must use `get`/`set`.

---

## Sorting
```java
import java.util.Collections;

Collections.sort(list);                          // ascending
Collections.sort(list, Collections.reverseOrder()); // descending
```

---

## How ArrayList Works Internally
- Backed by a regular array (default capacity 10).
- When full, Java creates a new array (~1.5× size) and copies everything over.
- `add()` → O(1) normally, O(n) when resizing. `get()`/`set()` → O(1). `remove(index)`/`add(index)` → O(n) (elements shift).

---

## Multi-dimensional ArrayList
An ArrayList of ArrayLists — used for dynamic 2D structures:
```java
ArrayList<ArrayList<Integer>> mainList = new ArrayList<>();

ArrayList<Integer> list1 = new ArrayList<>();
list1.add(1); list1.add(2);
mainList.add(list1);

// Access: mainList.get(row).get(col)
// Iterate:
for (int i = 0; i < mainList.size(); i++) {
    ArrayList<Integer> currList = mainList.get(i);
    for (int j = 0; j < currList.size(); j++) {
        System.out.print(currList.get(j));
    }
}
```
> Common use: **graph adjacency lists**, where each row can have a different size.

---

## Container With Most Water

**Problem:** Given wall heights, pick 2 walls to hold the most water.
`water = min(height[lp], height[rp]) × (rp - lp)`

### Brute Force — O(n²)
Try every pair `(i, j)`, compute `ht * width`, track max.

### Two Pointer — O(n)
```
lp = 0 (left end), rp = last index (right end)
while lp < rp:
    ht = min(height[lp], height[rp])
    currWater = ht * (rp - lp)
    update maxWater
    if height[lp] < height[rp]: lp++   // shorter wall → move it inward
    else: rp--
```
**Why move the shorter wall?** The shorter wall is the bottleneck — keeping it and moving the other pointer can never give more water. Moving past it might find a taller wall.

---

## Pair Sum 1 — Does any pair sum to target?

### Brute Force — O(n²)
Nested loops, check every pair.

### Two Pointer — O(n) — list must be SORTED
```
lp = 0, rp = list.size()-1
while lp != rp:
    case 1: sum == target → return true
    case 2: sum < target  → lp++   (need bigger number)
    case 3: sum > target  → rp--   (need smaller number)
return false
```

---

## Pair Sum 2 — Pair Sum on a Sorted + Rotated Array

**Problem:** Array is sorted but rotated (e.g. `[4, 5, 6, 7, 1, 2, 3]`). Find a pair summing to target.

**Key idea:** Can't use normal two-pointer (array isn't sorted in order) — but we can use a **circular** two-pointer.

**Steps:**
1. Find the **breaking point (bp)** — index where `arr[i] > arr[i+1]` (where the array wraps from max to min).
2. Set `lp = bp + 1` → points to the **smallest** element (just after the break).
3. Set `rp = bp` → points to the **largest** element (at the break).
4. Move pointers **circularly** using modular arithmetic:
   - `lp = (lp + 1) % n` → move lp right (wraps around)
   - `rp = (n + rp - 1) % n` → move rp left (wraps around)

```java
int bp = -1;
for (int i = 0; i < n-1; i++) {
    if (list.get(i) > list.get(i+1)) { bp = i; break; }
}
int lp = bp + 1;   // smallest
int rp = bp;       // largest
while (lp != rp) {
    if (list.get(lp) + list.get(rp) == target) return true;
    if (list.get(lp) + list.get(rp) < target) lp = (lp+1) % n;
    else rp = (n + rp - 1) % n;
}
```

---

## Common Pitfalls
1. **`list.remove(5)` vs `list.remove(Integer.valueOf(5))`** — int removes by index, Integer removes by value.
2. **`.size()` not `.length`** — ArrayList uses a method, arrays use a field.
3. **Can't store primitives** — `ArrayList<int>` is invalid; use `ArrayList<Integer>`.
4. **Pair sum two-pointer requires sorted input** — sort first, or use the rotated-array approach for Pair Sum 2.
5. **`Integer.MIN_VALUE` for find max** — safer than assuming `list.get(0)` is the max.
