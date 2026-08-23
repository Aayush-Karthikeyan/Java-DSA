# 27. Stacks Part 2 — Key Notes

---

## Valid Parentheses
Check if every opening bracket `( { [` has a matching closing bracket `) } ]`, in the correct order.

```
"{[()]}" → true    (well nested)
"{[(])}" → false   (crosses over — [ ( ] ) is wrong order)
"(()"    → false   (unclosed opening)
```

**Approach — 3 steps per char:**
1. Opening bracket → push it.
2. Closing bracket → if stack empty, fail immediately (nothing to match).
3. Closing bracket → if `peek()` is the matching opening, pop it; else fail (wrong bracket type, e.g. `"(]"`).
4. At the end, stack must be **empty** — any leftover opening bracket means something was never closed.

```java
if (ch=='('||ch=='{'||ch=='[') {
    s.push(ch);
} else {
    if (s.isEmpty()) return false;
    if ((s.peek()=='(' && ch==')') || (s.peek()=='{' && ch=='}') || (s.peek()=='[' && ch==']')) {
        s.pop();
    } else {
        return false;
    }
}
...
return s.isEmpty();
```
**Time: O(n) &nbsp; Space: O(n)**

> ⚠️ **Gotcha:** this version only handles strings made of *pure brackets*. Any other character (letters, `+`, `-`, digits) falls into the `else` branch, gets treated as an unexpected "closing" char, and fails the match checks → returns `false`. If you need to validate brackets *inside* an expression like `"(a+b)"`, you must first skip/ignore non-bracket characters instead of routing them into the closing-bracket branch.

---

## Duplicate (Redundant) Parentheses
Detect a parentheses pair that wraps something that's already a complete unit — i.e. it didn't need to be there.

```
"((a+b))" → true   — outer ( ) is redundant, "(a+b)" alone is already valid
"(a-b)"   → false  — exactly one necessary pair
```

**Approach:**
- Push every character **except `)`** onto the stack (so `(`, letters, operators all get pushed as-is).
- On hitting `)`: pop characters until you hit the matching `(`, counting how many you popped.
  - `count < 1` (i.e. `(` was *immediately* below the top) → nothing sat between the pair → **duplicate**.
  - Otherwise, there was real content between them → pop that `(` too and keep scanning.

```java
if (ch == ')') {
    int count = 0;
    while (s.peek() != '(') { s.pop(); count++; }
    if (count < 1) return true;   // duplicate
    else s.pop();                  // consume the matching '('
} else {
    s.push(ch);
}
...
return false;   // no redundant pair found anywhere
```

**Trace of `"((a+b))"`:**
```
push ( ( a + b            stack: ( ( a + b
) → pop b,+,a (count=3) → hit '(' → not <1 → pop that '(' → stack: (
) → peek is already '(' (count=0) → count<1 → return true
```
**Time: O(n) &nbsp; Space: O(n)**

> ⚠️ **Gotcha:** calling `s.peek()` in the `while` loop assumes the string is well-formed (every `)` has a matching `(`) — an unbalanced string will throw `EmptyStackException`. Run `isValid()` first if the input isn't guaranteed balanced.

---

## Maximum Rectangular Area in Histogram
Given bar heights, find the biggest rectangle that fits entirely under the histogram outline.

```
heights = [2, 1, 5, 6, 2, 3]  →  max area = 10   (bars 5,6 → height 5 × width 2)
```

**Key idea:** for bar `i`, the widest rectangle that can use `arr[i]` as its height stretches from the
nearest strictly-smaller bar on the **left** to the nearest strictly-smaller bar on the **right**
(both exclusive — you stop right before them, since a shorter bar would break the rectangle).

```
width(i) = nsr[i] - nsl[i] - 1
area(i)  = arr[i] * width(i)
answer   = max(area(i)) over all i
```

**`nsr[i]` / `nsl[i']`** are found with the classic **Next Smaller Element** stack pattern — same shape as Next Greater Element from Part 1, just flipped to "smaller" and run in both directions.

### Next Smaller Right (traverse right → left)
```java
Stack<Integer> s = new Stack<>();
for (int i = arr.length-1; i >= 0; i--) {
    while (!s.isEmpty() && arr[s.peek()] >= arr[i]) s.pop();   //1: discard bars that can't be "next smaller"
    nsr[i] = s.isEmpty() ? arr.length : s.peek();                //2: boundary index (or end of array)
    s.push(i);                                                    //3
}
```

### Next Smaller Left (traverse left → right)
```java
s = new Stack<>();   // reuse a fresh stack
for (int i = 0; i < arr.length; i++) {
    while (!s.isEmpty() && arr[s.peek()] >= arr[i]) s.pop();
    nsl[i] = s.isEmpty() ? -1 : s.peek();                        // boundary index (or before start)
    s.push(i);
}
```

### Combine
```java
for (int i = 0; i < arr.length; i++) {
    int height = arr[i];
    int width  = nsr[i] - nsl[i] - 1;
    maxArea = Math.max(maxArea, height * width);
}
```
**Time: O(n) &nbsp; Space: O(n)**

> **Why `>=` and not `>` in the while condition?** Using `>=` (not just `>`) correctly handles duplicate heights — it prevents two equal-height bars from both claiming the same width span, which would double count area for repeated values.

> **Boundary values matter:** `nsr[i] = arr.length` (not some in-bounds index) when no smaller bar exists to the right, and `nsl[i] = -1` when none exists to the left — these act as "off the edge" markers so the width formula `nsr[i]-nsl[i]-1` still works correctly at the array's ends.

---

## Summary

| Problem | Core Trick | Time | Space |
|---------|-----------|------|-------|
| Valid Parentheses | push opening, pop-match on closing, end stack empty | O(n) | O(n) |
| Duplicate Parentheses | push all but `)`, count chars popped before matching `(` | O(n) | O(n) |
| Max Area in Histogram | Next Smaller Element both directions, then `nsr[i]-nsl[i]-1` | O(n) | O(n) |

---

## Common Pitfalls
1. **Valid Parentheses** only works for pure bracket strings here — non-bracket chars are *not* skipped, they're treated as closing brackets and will fail the match.
2. **Duplicate Parentheses** calls `s.peek()`/`s.pop()` assuming a balanced string — validate with `isValid()` first on untrusted input to avoid `EmptyStackException`.
3. **Histogram**: `nsr`/`nsl` store **indices**, not heights — `arr[i]` (the height) is separate from `nsr[i]`/`nsl[i]` (the boundary positions).
4. **Histogram**: use `>=` in the while-loop, not `>`, to correctly handle equal-height bars.
5. **Histogram boundaries**: `nsr[i] = arr.length` and `nsl[i] = -1` are intentional sentinel values, not bugs — they make `width = nsr[i]-nsl[i]-1` work even when a bar is the tallest in the whole array.
