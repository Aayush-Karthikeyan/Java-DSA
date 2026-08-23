# 35. Heaps - Key Notes

---

## What is a Heap?

A heap is a Complete Binary Tree with a heap order rule.

Complete Binary Tree means:

- All levels are completely filled except maybe the last.
- The last level is filled from left to right.

Heap order rules:

```text
Min Heap: parent <= children
Max Heap: parent >= children
```

In a min heap, the smallest value is always at the root.  
In a max heap, the largest value is always at the root.

---

## Heap as an Array

A heap is usually stored in an array or `ArrayList`.

For index `i`:

```text
parent = (i - 1) / 2
left child = 2 * i + 1
right child = 2 * i + 2
```

Example:

```text
array: [1, 3, 4, 5]

        1
      /   \
     3     4
    /
   5
```

---

## Java PriorityQueue

Java's `PriorityQueue` is implemented using a heap.

Default behavior:

```java
PriorityQueue<Integer> pq = new PriorityQueue<>();
```

This is a min heap. Smaller values come out first.

Operations:

```java
pq.add(x);      // O(log n)
pq.peek();      // O(1)
pq.remove();    // O(log n)
```

Example:

```java
pq.add(3);
pq.add(4);
pq.add(1);
pq.add(7);
```

Remove order:

```text
1 3 4 7
```

---

## Max Heap with PriorityQueue

Use `Comparator.reverseOrder()`:

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
```

Remove order for `3, 4, 1, 7`:

```text
7 4 3 1
```

---

## PriorityQueue with Objects

If a `PriorityQueue` stores objects, Java needs to know how to compare them.

Use `Comparable`:

```java
static class Student implements Comparable<Student> {
    String name;
    int rank;

    public int compareTo(Student s2) {
        return this.rank - s2.rank;
    }
}
```

This means smaller rank has higher priority.

Common mistake:
- If you put custom objects in a `PriorityQueue` without `Comparable` or a `Comparator`, Java does not know the priority order.

---

## Custom Min Heap - Add

Steps:

1. Add new value at the end.
2. Compare with parent.
3. If child is smaller than parent, swap.
4. Keep moving upward until heap property is fixed.

Code pattern:

```java
arr.add(data);

int child = arr.size() - 1;
int parent = (child - 1) / 2;

while (child > 0 && arr.get(child) < arr.get(parent)) {
    swap(child, parent);
    child = parent;
    parent = (child - 1) / 2;
}
```

Time: `O(log n)`

Important:
- Use `child > 0` in the loop. Once child reaches root, it has no parent.

---

## Custom Min Heap - Peek

Root is stored at index `0`.

```java
int peek() {
    return arr.get(0);
}
```

Time: `O(1)`

---

## Custom Min Heap - Remove

In a min heap, remove returns the smallest value.

Steps:

1. Store root value.
2. Swap root with last value.
3. Remove last value.
4. Heapify root downward.

Code pattern:

```java
int data = arr.get(0);

swap(0, arr.size() - 1);
arr.remove(arr.size() - 1);

heapify(0);
return data;
```

Time: `O(log n)`

---

## Heapify

Heapify fixes a node by moving it downward.

For min heap:

```java
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
```

Time: `O(log n)`

---

## Heap Sort

Heap sort uses a max heap to sort in ascending order.

Steps:

1. Build a max heap.
2. Swap root/largest with last active element.
3. Reduce heap size.
4. Heapify root again.
5. Repeat.

Code idea:

```java
for (int i = n / 2; i >= 0; i--) {
    heapify(arr, i, n);
}

for (int i = n - 1; i > 0; i--) {
    swap arr[0] and arr[i];
    heapify(arr, 0, i);
}
```

Time: `O(n log n)`  
Space: `O(1)`

Important:
- For ascending order, build a max heap.
- For descending order, build a min heap.

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| `PriorityQueue.add()` | Add and bubble up | O(log n) |
| `PriorityQueue.peek()` | Return root | O(1) |
| `PriorityQueue.remove()` | Remove root and heapify | O(log n) |
| Custom heap add | Add at end, move up | O(log n) |
| Custom heap remove | Swap root with last, heapify | O(log n) |
| Heapify | Fix heap downward | O(log n) |
| Heap sort | Build max heap, repeatedly remove max | O(n log n) |

---

## Common Mistakes

1. Forgetting that Java `PriorityQueue` is a min heap by default.
2. Forgetting `Comparator.reverseOrder()` for max heap.
3. For object priority queues, not implementing `Comparable` or giving a `Comparator`.
4. In heap add, not updating child and parent after swapping.
5. In heap add, forgetting `child > 0` in the while loop.
6. In heapify, using wrong child index formulas.
7. In heap sort, building a min heap when trying to sort ascending.
8. In heap sort, forgetting to reduce heap size after moving the largest value to the end.

---

## Memory Tricks

Array indexes:

```text
parent = (i - 1) / 2
left = 2i + 1
right = 2i + 2
```

Min heap:

```text
smallest at root
```

Max heap:

```text
largest at root
```

Heap sort ascending:

```text
build max heap, push largest to end
```
