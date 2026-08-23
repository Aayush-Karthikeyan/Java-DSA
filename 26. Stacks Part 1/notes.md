# 26. Stacks Part 1 — Key Notes

---

## What is a Stack?
**LIFO — Last In, First Out.** Like a stack of plates — you add/remove from the top only.

```
push(3) → [1, 2, 3]  ← top
pop()   → returns 3, stack = [1, 2]
peek()  → returns 2 (just look, don't remove)
```

---

## Stack using ArrayList
End of ArrayList = top of stack.
```java
static ArrayList<Integer> list = new ArrayList<>();

isEmpty() → list.size() == 0
push(data)→ list.add(data)
pop()     → top = list.get(size-1); list.remove(size-1); return top;
peek()    → list.get(list.size() - 1)
```
**All operations: O(1)**

---

## Stack using Linked List
Head of linked list = top of stack. Push adds at head, pop removes head.
```java
//push
newNode.next = head;   // new node points to old top
head = newNode;        // new node IS the new top

//pop
int top = head.data;
head = head.next;
return top;
```
**All operations: O(1)**

---

## Stack using Collections Framework
```java
import java.util.Stack;
Stack<Integer> s = new Stack<>();

s.push(x);      // add to top
s.pop();        // remove and return top
s.peek();       // look at top (no remove)
s.isEmpty();    // true if empty
```
> Use `java.util.Stack` in interviews unless asked to implement from scratch.

---

## Push at Bottom of Stack
Insert element at the very bottom using recursion — no extra data structure.
```
Stack = [1, 2, 3] top   →   push 4 at bottom   →   [4, 1, 2, 3]

Step 1: pop 3 → recurse → pop 2 → recurse → pop 1 → recurse
Step 2: stack empty → push 4 (base case)
Step 3: push 1 back → push 2 back → push 3 back
```
```java
static void pushAtBottom(Stack<Integer> s, int data) {
    if (s.isEmpty()) { s.push(data); return; }
    int top = s.pop();
    pushAtBottom(s, data);
    s.push(top);
}
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Reverse a String using Stack
Push all characters, then pop them (LIFO gives reverse order).
```java
Stack<Character> s = new Stack<>();
while (idx < str.length()) { s.push(str.charAt(idx++)); }
StringBuilder result = new StringBuilder("");
while (!s.isEmpty()) { result.append(s.pop()); }
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Reverse a Stack
Reverse all elements in a stack using recursion + pushAtBottom.
```java
static void reverseStack(Stack<Integer> s) {
    if (s.isEmpty()) return;
    int top = s.pop();      // remove top
    reverseStack(s);        // reverse remaining
    pushAtBottom(s, top);   // push old top to bottom
}
```
```
[1, 2, 3] → pop 3 → reverse [1,2] → [2,1] → pushBottom(3) → [3,2,1]
```
**Time: O(n²) &nbsp; Space: O(n)**
> Why O(n²)? reverseStack calls pushAtBottom for each element. pushAtBottom is O(n). So n × O(n) = O(n²).

---

## Stock Span Problem
For each day: how many consecutive days (including today) had price ≤ today's price?

**Stack stores indices of days with unresolved higher prices.**
```
stocks = [100, 80, 60, 70, 60, 85, 100]
span   = [1,   1,  1,  2,  1,  4,  6 ]
```

**3 steps per day:**
1. Pop indices while `currPrice > stocks[peek]` (those days are "covered")
2. If stack empty → span = `i + 1` (all previous days had lower price)  
   Else → span = `i - peek` (gap between today and nearest higher day)
3. Push current index `i`

```java
while (!s.isEmpty() && currPrice > stocks[s.peek()]) s.pop();
span[i] = s.isEmpty() ? i + 1 : i - s.peek();
s.push(i);
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Next Greater Element
For each element, find the first element to its **right** that is strictly greater. If none → -1.

**Traverse RIGHT TO LEFT. Stack stores indices of candidate "next greater" elements.**

**3 steps per element (right to left):**
1. While: pop indices where `arr[peek] <= arr[i]` (they're useless — arr[i] is bigger)
2. If-else: `nxtGreater[i] = s.isEmpty() ? -1 : arr[s.peek()]`
3. Push current index `i`

```java
for (int i = arr.length-1; i >= 0; i--) {
    while (!s.isEmpty() && arr[s.peek()] <= arr[i]) s.pop();  //1
    nxtGreater[i] = s.isEmpty() ? -1 : arr[s.peek()];         //2
    s.push(i);                                                  //3
}
```
```
arr      = [6, 8, 0, 1, 3]
nxtGreater = [8,-1, 1, 3,-1]
```
**Time: O(n) &nbsp; Space: O(n)**

---

## Summary

| Implementation | push | pop | peek | Space |
|----------------|------|-----|------|-------|
| ArrayList | O(1) | O(1) | O(1) | O(n) |
| Linked List | O(1) | O(1) | O(1) | O(n) |
| java.util.Stack | O(1) | O(1) | O(1) | O(n) |

| Problem | Time | Space |
|---------|------|-------|
| Push at Bottom | O(n) | O(n) |
| Reverse String | O(n) | O(n) |
| Reverse Stack | O(n²) | O(n) |
| Stock Span | O(n) | O(n) |
| Next Greater Element | O(n) | O(n) |

---

## Common Pitfalls
1. **Pop before checking isEmpty** → EmptyStackException. Always check `isEmpty()` first.
2. **ArrayList pop**: use `list.remove(list.size()-1)` (by index), NOT `list.remove(value)`.
3. **Stock Span**: stack stores **indices**, not prices. `stocks[s.peek()]` gets the actual price.
4. **Next Greater**: traverse **right to left**, not left to right.
5. **reverseStack is O(n²)** — pushAtBottom is called n times and each takes O(n).
