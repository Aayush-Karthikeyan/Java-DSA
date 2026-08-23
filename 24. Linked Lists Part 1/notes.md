# 24. Linked Lists Part 1 — Key Notes

---

## What is a Linked List?
- A chain of **nodes**. Each node holds: `data` + `next` (pointer to next node).
- Last node's `next` is `null` — that's how you know it's the end.
- Unlike arrays, nodes are **NOT stored next to each other in memory**.

```
head                          tail
 |                             |
[1] -> [2] -> [3] -> [4] -> [5] -> null
```

---

## Node Class
```java
class Node {
    int data;
    Node next;
    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
```

---

## Head & Tail
- `head` → first node. Entry point of the list.
- `tail` → last node. Allows O(1) addLast.
- Always update both when the list becomes empty or gets a first element.

---

## Operations & Time Complexity

| Operation | How | Time |
|-----------|-----|------|
| Add First | New node → old head. Update head. | O(1) |
| Add Last | tail.next → new node. Update tail. | O(1) |
| Add at Index | Walk to index-1, rewire next pointers. | O(n) |
| Remove First | head = head.next | O(1) |
| Remove Last | Walk to second-to-last, set .next = null | O(n) |
| Search | Walk from head, compare each node | O(n) |
| Print | Walk from head to null | O(n) |

---

## Add in the Middle (at index)
```
Before: [1] -> [2] -> [3] -> null   insert 99 at index 2
After:  [1] -> [2] -> [99] -> [3] -> null
```
Walk to node at `index-1`. Then:
```java
newNode.next = curr.next;   // new node points to what was at index
curr.next = newNode;        // previous node points to new node
```

---

## Remove Nth Node from End
Trick: use **two pointers** (fast and slow), both start at head.
1. Move `fast` N steps ahead.
2. Move both `fast` and `slow` together until `fast.next == null`.
3. Now `slow` is just before the node to delete.
4. `slow.next = slow.next.next` → skips over the target.

```
List: 1 -> 2 -> 3 -> 4 -> 5    Remove 2nd from end (= node 4)

fast starts N=2 steps ahead of slow:
slow=1, fast=3
Move together: slow=2, fast=4 → slow=3, fast=5 (fast.next == null, stop)
slow is at 3. slow.next = slow.next.next → skips 4.
Result: 1 -> 2 -> 3 -> 5
```

---

## Reverse a Linked List
Use 3 pointers: `prev = null`, `curr = head`, save `next` before overwriting.
```
prev=null  curr=1 -> 2 -> 3

Step 1: next=2, flip: 1->null, prev=1, curr=2
Step 2: next=3, flip: 2->1,    prev=2, curr=3
Step 3: next=null, flip: 3->2, prev=3, curr=null

head = prev (= 3)
Result: 3 -> 2 -> 1 -> null
```
```java
while (curr != null) {
    Node next = curr.next;
    curr.next = prev;
    prev = curr;
    curr = next;
}
head = prev;
```

---

## Find Middle Node (Slow/Fast Pointer)
- `slow` moves 1 step, `fast` moves 2 steps.
- When `fast` reaches the end, `slow` is at the middle.
```java
while (fast != null && fast.next != null) {
    slow = slow.next;
    fast = fast.next.next;
}
// slow = middle node
```

---

## Check if LL is Palindrome
Three steps:
1. **Find middle** using slow/fast pointers.
2. **Reverse the second half** (starting from mid).
3. **Compare** left half (from head) with reversed right half node by node.

```
[1 -> 2 -> 3 -> 2 -> 1]
             ^mid

Reverse from mid: [3 -> 2 -> 1] becomes [1 -> 2 -> 3]
right = 1 -> 2 -> 3
left  = 1 -> 2 -> 3

Compare: all match → palindrome!
```

---

## Iterative vs Recursive Search

| | Iterative | Recursive |
|--|-----------|-----------|
| How | while loop, walk node by node | call itself on node.next |
| Base case | curr == null | node == null |
| Stack usage | O(1) | O(n) call stack |

---

## LL vs Array

| | Array | Linked List |
|--|-------|-------------|
| Memory | Contiguous block | Scattered nodes |
| Access by index | O(1) — direct | O(n) — must walk |
| Add/Remove at start | O(n) — shifts | O(1) |
| Add/Remove at end | O(1) | O(1) with tail pointer |
| Size | Fixed | Dynamic |

---

## Common Pitfalls
1. **Never move `head` while traversing** — use a separate `curr` pointer.
2. **Update both head AND tail** when list becomes empty (1 node removed).
3. **Check for null before `.next`** — always guard with `if (head == null)`.
4. **Slow/fast pointer**: condition is `fast != null && fast.next != null` — order matters (check fast first).
