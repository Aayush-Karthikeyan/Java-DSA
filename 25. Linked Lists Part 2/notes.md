# 25. Linked Lists Part 2 — Key Notes

---

## Detect Cycle — Floyd's Algorithm
slow moves +1, fast moves +2. If cycle exists, fast laps slow and they meet inside the loop.

```java
Node slow = head, fast = head;
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
    if (slow == fast) return true;   // cycle exists
}
return false;   // fast hit null → no cycle
```
**Time:** O(n) &nbsp; **Space:** O(1)

---

## Remove Cycle
After detecting cycle (slow == fast at meeting point inside cycle):

1. Reset `slow = head`. Keep `fast` at meeting point. Track `prev` (node before fast).
2. Move both `slow` and `fast` by +1 each step.
3. When `slow == fast` again → they're at the **start of the cycle**.
4. `prev.next = null` → cycle removed.

```java
slow = head;
Node prev = null;
while (slow != fast) {
    prev = fast;
    slow = slow.next;
    fast = fast.next;   // both move +1 now
}
prev.next = null;   // cut the cycle
```

**Why does slow == fast at cycle start?**
The distance from `head` to cycle-start = distance from meeting-point to cycle-start. So resetting slow to head and moving both +1 makes them converge exactly at cycle-start.

**Time:** O(n) &nbsp; **Space:** O(1)

---

## Merge Sort on Linked List
**Divide:** find mid, split into two halves. **Conquer:** sort each half recursively. **Combine:** merge the two sorted halves.

```
getMid: slow=head, fast=head.next → slow lands at left-middle
Split:  mid.next = null → two separate lists
Merge:  dummy node approach — compare heads, attach smaller one
```

### merge() — O(n+m)
```java
Node dummy = new Node(-1);
Node temp = dummy;
while (head1 != null && head2 != null) {
    if (head1.data <= head2.data) { temp.next = head1; head1 = head1.next; }
    else                          { temp.next = head2; head2 = head2.next; }
    temp = temp.next;
}
if (head1 != null) temp.next = head1;
if (head2 != null) temp.next = head2;
return dummy.next;
```

### mergeSort() — O(n log n)
```java
if (head == null || head.next == null) return head;   // base case
Node mid = getMid(head);
Node rightHead = mid.next;
mid.next = null;                     // split
Node newLeft  = mergeSort(head);
Node newRight = mergeSort(rightHead);
return merge(newLeft, newRight);
```
**Time:** O(n log n) &nbsp; **Space:** O(log n) call stack

> Why LL merge sort is better than array merge sort: no extra array needed for merging, just pointer rewiring → Space O(log n) vs O(n).

---

## Zig-Zag Linked List
Rearrange: 1st, last, 2nd, 2nd-last ... e.g. `[1,2,3,4,5]` → `[1,5,2,4,3]`

**Steps:**
1. **Find middle** — slow=head, fast=head.next (cleaner split for even lists)
2. **Reverse 2nd half** — standard 3-pointer reverse starting from mid.next
3. **Alternate merge** — pick one from left, one from right, repeat

```
left  = 1 -> 2 -> 3 -> null
right = 5 -> 4 -> null   (reversed 2nd half)

merge: 1, 5, 2, 4, 3 → [1->5->2->4->3->null]
```
**Time:** O(n) &nbsp; **Space:** O(1)

---

## Doubly Linked List (DLL)
Each node has **two pointers**: `prev` (backward) and `next` (forward).

```
null <-- [1] <--> [2] <--> [3] --> null
```

- Traverse forward AND backward.
- `addLast` must also set `newNode.prev = curr` (the backward link).

---

## Reverse a Doubly LL
Same 3-variable structure as singly LL reverse, but also flip `curr.prev`.

```java
// 3 variables, 4 steps:
DNode curr = head, prev = null;
while (curr != null) {
    DNode next = curr.next;
    curr.next = prev;    // flip next pointer
    curr.prev = next;    // flip prev pointer (extra step vs singly LL)
    prev = curr;
    curr = next;
}
head = prev;
```
**Time:** O(n) &nbsp; **Space:** O(1)

---

## Circular Linked List — Concept
- Last node's `next` = `head` (not null).
- Can be singly or doubly circular.
- Traverse with a `do-while` — stop when you loop back to head.

```java
n3.next = cHead;   // make it circular

Node curr = cHead;
do {
    System.out.print(curr.data + " ");
    curr = curr.next;
} while (curr != cHead);
```

---

## Java Collections Framework — LinkedList
`java.util.LinkedList` is a **built-in doubly linked list**. Also implements `Deque` (double-ended queue).

```java
import java.util.LinkedList;
LinkedList<Integer> ll = new LinkedList<>();

ll.addFirst(x);    // O(1) — add to front
ll.addLast(x);     // O(1) — add to back
ll.removeFirst();  // O(1)
ll.removeLast();   // O(1)
ll.peekFirst();    // look at front without removing
ll.peekLast();     // look at back without removing
ll.size();
```
> Use `java.util.LinkedList` in interviews/projects instead of writing your own, unless asked to implement from scratch.

---

## Summary — Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Detect Cycle | O(n) | O(1) |
| Remove Cycle | O(n) | O(1) |
| Merge Sort | O(n log n) | O(log n) |
| Zig-Zag | O(n) | O(1) |
| Reverse DLL | O(n) | O(1) |

---

## Common Pitfalls
1. **`fast.next.next` without checking `fast.next != null` first** → NullPointerException.
2. **Forgetting `mid.next = null`** in merge sort — without this, the two halves are still connected and recursion breaks.
3. **Circular LL traversal: use `do-while`**, not `while` — otherwise you never enter the loop.
4. **DLL addLast: always set `newNode.prev`** — easy to forget the back-pointer.
5. **mergeSort returns a new head** — always do `ll.head = ll.mergeSort(ll.head)` to update head.
