# 28. Queues — Key Notes

---

## What is a Queue?
**FIFO — First In, First Out.** Like a line at a counter — join at the rear, get served from the front.

```
add(1) → add(2) → add(3)  → front[1, 2, 3]rear
remove() → returns 1 (front), queue = [2, 3]
peek()   → returns 2 (front, no remove)
```

---

## Queue using Array (naive)
`rear` tracks the last filled index. `add` is O(1), but `remove` has to **shift every element left** — that's the whole reason Circular Queue exists.
```java
static void add(int data) { rear++; arr[rear] = data; }         // O(1)
static int remove() {
    int front = arr[0];
    for (int i = 0; i < rear; i++) arr[i] = arr[i+1];             // shift left
    rear--;
    return front;
}
```
**add: O(1) &nbsp; remove: O(n)**

---

## Circular Queue using Array
Wraps `front`/`rear` around with `% size`, so no shifting is needed — `remove` becomes O(1).
```java
static boolean isFull()  { return (rear+1) % size == front; }
static void add(int data) {
    if (front == -1) front = 0;      // first element
    rear = (rear+1) % size;           // wrap around
    arr[rear] = data;
}
static int remove() {
    int data = arr[front];
    if (front == rear) front = rear = -1;   // last element removed
    else front = (front+1) % size;
    return data;
}
```
**All operations: O(1)**
> `front == -1 && rear == -1` = empty. `(rear+1)%size == front` = full (next slot would collide with front).

---

## Queue using Linked List
`head` = front, `tail` = rear. Add at tail, remove from head.
```java
//add
tail.next = newNode;
tail = newNode;

//remove
int front = head.data;
if (tail == head) tail = head = null;   // single element
else head = head.next;
```
**All operations: O(1)**

---

## Queue using Collections Framework (JCF)
```java
Queue<Integer> q = new LinkedList<>();
q.add(x);      // enqueue
q.remove();    // dequeue, returns front
q.peek();      // look at front
```
> Use `java.util.Queue` (backed by `LinkedList`) in interviews unless asked to implement from scratch.

---

## Queue using Two Stacks
`add` is costly, `remove` is cheap.
```java
static void add(int data) {
    while (!s1.isEmpty()) s2.push(s1.pop());   // drain s1 → s2
    s1.push(data);                               // new data goes to bottom of s1
    while (!s2.isEmpty()) s1.push(s2.pop());   // drain back, oldest ends up on top
}
static int remove() { return s1.pop(); }        // top of s1 = front of queue
```
**add: O(n) &nbsp; remove: O(1)**

---

## Stack using Two Queues
`push` is cheap, `pop` is costly (opposite tradeoff of above).
```java
static void push(int data) {
    if (!q1.isEmpty()) q1.add(data); else q2.add(data);   // O(1)
}
static int pop() {
    // drain the non-empty queue into the other, but stop 1 short —
    // the last value removed is the most recently pushed = top of stack
    while (!q1.isEmpty()) {
        top = q1.remove();
        if (q1.isEmpty()) break;
        q2.add(top);
    }
    return top;
}
```
**push: O(1) &nbsp; pop: O(n)**

---

## First Non-Repeating Letter in a Stream
For each prefix of the string, print the first character that hasn't repeated yet (or `-1`).
```
"aabccxb" → a  -1  b  b  b  b  x
```
**Approach:** queue holds candidates not yet disqualified; `freq[]` (size 26) counts occurrences.
```java
q.add(ch);
freq[ch-'a']++;
while (!q.isEmpty() && freq[q.peek()-'a'] > 1) q.remove();   // front repeated → drop it
// answer for this prefix = q.isEmpty() ? -1 : q.peek()
```
**Time: O(n) &nbsp; Space: O(1)** (26-letter freq array)
> Must use `if/else` to print, not a ternary — `(isEmpty ? -1 : q.peek())` auto-promotes the `Character` to `int`, so you'd print char **codes** (97, 98...) instead of letters.

---

## Interleave Two Halves of a Queue
`[1,2,3,4,5,6]` → `[1,4,2,5,3,6]`
```java
for (i = 0; i < size/2; i++) firstHalf.add(q.remove());   // move 1st half out
while (!firstHalf.isEmpty()) {
    q.add(firstHalf.remove());   // one from first half
    q.add(q.remove());           // one from (rotated) second half
}
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Queue Reversal
`[1,2,3,4]` → `[4,3,2,1]`. A stack flips order for free (LIFO vs FIFO).
```java
while (!q.isEmpty()) s.push(q.remove());
while (!s.isEmpty()) q.add(s.pop());
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Deque (Double Ended Queue)
Add/remove from **both** ends.
```java
Deque<Integer> dq = new LinkedList<>();
dq.addFirst(x); dq.addLast(x);
dq.removeFirst(); dq.removeLast();
dq.getFirst(); dq.getLast();
```
Because it can act from either end, a Deque can implement **both** a Stack and a Queue:
```java
// Stack:  push→addLast   pop→removeLast   peek→getLast
// Queue:  add→addLast    remove→removeFirst   peek→getFirst
```

---

## Summary

| Implementation | add/push | remove/pop |
|---|---|---|
| Queue: Array (naive) | O(1) | O(n) — shifting |
| Queue: Circular Array | O(1) | O(1) |
| Queue: Linked List | O(1) | O(1) |
| Queue: Two Stacks | O(n) | O(1) |
| Stack: Two Queues | O(1) | O(n) |
| Stack/Queue: Deque | O(1) | O(1) |

| Problem | Time | Space |
|---|---|---|
| First Non-Repeating Letter | O(n) | O(1) |
| Interleave Two Halves | O(n) | O(n) |
| Queue Reversal | O(n) | O(n) |

---

## Common Pitfalls
1. **Naive array queue**: `remove()` shifts every element — O(n). Circular queue exists specifically to avoid this.
2. **Circular queue**: `isFull()` compares `(rear+1)%size` to `front`, not `rear==size-1` — a plain index check breaks once you've wrapped around.
3. **Queue-via-2-stacks / Stack-via-2-queues**: one operation is always O(1) and the other O(n) — there's no way to make both O(1) with just two of the opposite structure.
4. **First Non-Repeating Letter**: printing with a ternary mixing `int` and `Character` silently prints char *codes*, not the letters — use `if/else`.
5. **Deque as Stack vs Queue**: same class, opposite pairing of methods — mixing them up (e.g. `addFirst`+`removeFirst` for a "stack") silently turns it into a queue instead.
