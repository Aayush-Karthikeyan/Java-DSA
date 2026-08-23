# 19. Recursion Basics (Part 2) — Key Notes

---

## Theme of Part 2
Part 1 covered straightforward recursion (factorial, fibonacci, search).
Part 2 covers **counting problems** — "in how many ways can X be done?" — and **string recursion** with extra state passed along. These are the patterns that appear in real interviews.

---

## 1. Tiling Problem
**Problem:** Tile a `2×n` board with `2×1` dominoes. Count the number of ways.

**Key insight:** At the leftmost unfilled column, you have exactly 2 choices:
- Place 1 domino **vertically** → fills 1 column → `tiling(n-1)` ways for the rest
- Place 2 dominoes **horizontally** → must fill 2 columns at once → `tiling(n-2)` ways for the rest

```
tiling(n) = tiling(n-1) + tiling(n-2)
Base: tiling(0) = 1  (empty board = 1 way)
      tiling(1) = 1  (only 1 vertical fits)
```

**Result is Fibonacci!**
| n | ways |
|---|------|
| 1 | 1 |
| 2 | 2 |
| 3 | 3 |
| 4 | 5 |
| 5 | 8 |
| 6 | 13 |

> Any problem where you have 2 choices at each step, one consuming 1 unit and one consuming 2, gives Fibonacci.

---

## 2. Remove Duplicates in a String
**Problem:** Remove all duplicate characters, keeping only the first occurrence.
`"banana"` → `"ban"`

**Strategy:** Pass a `boolean[] seen` array of size 26 (one slot per letter `a-z`) alongside the recursion.
- Map character to index: `ch - 'a'`  (`'a'`=0, `'b'`=1, ... `'z'`=25)
- At each character: if `seen[pos]` → skip; else → include and mark seen.

```java
// Base case: reached end of string → return ""
// Recursive:
//   if seen[ch-'a'] → skip → removeDups(str, idx+1, seen)
//   else            → include → ch + removeDups(str, idx+1, seen)
```

**Key pattern:** Passing extra state (the `seen` array) through the recursion so all calls share it.

---

## 3. Friends Pairing Problem
**Problem:** `n` friends. Each can either stay **single** or **pair** with exactly one other friend. Count total ways.

**Decision tree at person `n`:**
- **Stay single:** the remaining `n-1` people solve the problem → `f(n-1)` ways
- **Pair up:** choose any of the `n-1` others as partner → `(n-1)` choices × `f(n-2)` ways for the rest

```
f(n) = f(n-1) + (n-1) × f(n-2)
Base: f(1) = 1
      f(2) = 2
```

| n | f(n) |
|---|------|
| 1 | 1 |
| 2 | 2 |
| 3 | 4 |
| 4 | 10 |
| 5 | 26 |

---

## 4. Binary Strings with No Consecutive 1s
**Problem:** Count (or print) all binary strings of length `n` that have no two consecutive `1`s.

**Strategy:** Build the string one bit at a time. Track `lastBit`.
- If `lastBit == 0` → next can be **0 or 1** (both allowed)
- If `lastBit == 1` → next can **only be 0** (can't have two consecutive 1s)

```
countBinaryStrings(n, lastBit):
    if n == 0: return 1               // completed 1 valid string
    if lastBit == 0: return f(n-1, 0) + f(n-1, 1)
    if lastBit == 1: return f(n-1, 0)
```

| n | count |
|---|-------|
| 1 | 2 (0, 1) |
| 2 | 3 (00, 01, 10) |
| 3 | 5 (000, 001, 010, 100, 101) |
| 4 | 8 |
| 5 | 13 |

Again Fibonacci! (n+2 th Fibonacci number)

---

## Core Patterns in Recursive Counting Problems

### Pattern 1: Choice at each step
```
f(n) = (ways if choice A) + (ways if choice B)
```
Used in: Tiling, Binary Strings, Friends Pairing.

### Pattern 2: Passing state through recursion
```java
// Boolean array, index, or a StringBuilder passed alongside
removeDuplicates(str, index, seen[])
```
Lets all recursive calls share information about what's happened so far.

### Pattern 3: Recognize Fibonacci disguises
Any time you see `f(n) = f(n-1) + f(n-2)` come up in a counting problem, it's Fibonacci in disguise. Common triggers: 2 choices where one consumes 1 step and other consumes 2 steps.

---

## Stack Analysis — Binary Strings
For `printBinaryStrings(n, lastBit, current)`:
- At each level, `lastBit=0` branches into **2 calls**, `lastBit=1` branches into **1 call**.
- The call tree is like a binary tree but some nodes have only 1 child.
- Total valid strings = leaves of this tree = count returned by `countBinaryStrings`.
- Total calls ≈ 2× the count (internal + leaf nodes).

---

## Comparison: Part 1 vs Part 2

| Part 1 | Part 2 |
|--------|--------|
| Linear recursion (one call per step) | Branching recursion (2 calls per step) |
| Computing a value (factorial, sum) | Counting possibilities |
| Simple base cases | Sometimes 2 base cases needed |
| Mostly O(n) | Often O(2^n) — tree of calls |

---

## Tips for Recursive Counting Problems
1. **Draw the decision tree** — at each node, what choices do you have?
2. **Each branch is a recursive call**, leaves are base cases (valid completions).
3. **Trust the recursion** — assume `f(n-1)` and `f(n-2)` work, just write what `f(n)` does with them.
4. **Check small cases by hand** — verify `n=1`, `n=2`, `n=3` match your formula before coding.
5. **Spot Fibonacci** — if `f(n) = f(n-1) + f(n-2)`, you can often optimize with DP later.
