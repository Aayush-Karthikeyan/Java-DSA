# 21. Time & Space Complexity — Key Notes

---

## Why It Matters
Two algorithms can both solve a problem correctly but one might be 1000× faster.
Complexity analysis tells you **how performance scales as input grows** — without needing to run the code.

---

## Time Complexity
Measures **how many steps** an algorithm takes as a function of input size `n`.
We care about the **growth rate**, not exact counts — constants and small terms are ignored.

## Space Complexity
Measures **how much extra memory** an algorithm uses.
Includes: variables, arrays allocated, and recursion call stack depth.

---

## The 3 Notations

| Notation | Meaning | Used for |
|----------|---------|---------|
| **O (Big O)** | Upper bound — worst case | Most common, "at most this slow" |
| **Ω (Omega)** | Lower bound — best case | "at least this fast" |
| **Θ (Theta)** | Tight bound — exact | When best = worst case |

> In interviews and practice, **Big O** is what everyone means.

---

## Common Complexities (fastest → slowest)

| Big O | Name | Example |
|-------|------|---------|
| O(1) | Constant | Array access, hash map get |
| O(log n) | Logarithmic | Binary search |
| O(n) | Linear | Single loop |
| O(n log n) | Linearithmic | Merge Sort, Quick Sort (avg) |
| O(n²) | Quadratic | Nested loops, Bubble Sort |
| O(2^n) | Exponential | Naive Fibonacci, subsets |
| O(n!) | Factorial | Permutations |

```
O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(2^n) < O(n!)
```

---

## How to Read Code and Find Complexity

### Rule 1 — Single loop → O(n)
```java
for (int i = 0; i < n; i++) { ... }   // O(n)
```

### Rule 2 — Nested loops → multiply
```java
for (int i = 0; i < n; i++)           // O(n)
    for (int j = 0; j < n; j++) { }   // O(n)
// Total: O(n × n) = O(n²)
```

### Rule 3 — Loop that halves → O(log n)
```java
while (n > 1) { n = n / 2; }          // O(log n)
// n=8: 8→4→2→1  (3 steps = log₂8)
```

### Rule 4 — Sequential blocks → add, then keep the dominant term
```java
for (...) { }     // O(n)
for (...) { }     // O(n)
// Total: O(n) + O(n) = O(2n) = O(n)   ← drop the constant
```

### Rule 5 — Drop constants and lower-order terms
```
O(5n² + 3n + 100)  →  O(n²)
O(2 log n + n)     →  O(n)
```

---

## Loop Analysis Examples

### Simple Loop — O(n)
```java
for (int i = 0; i < n; i++) { }       // runs exactly n times
```

### Nested Loop 1 — O(n²)
```java
for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++) { }   // n × n = n²
```

### Nested Loop 2 — O(n²) still
```java
for (int i = 0; i < n; i++)
    for (int j = i; j < n; j++) { }   // inner starts at i, not 0
// Total steps = n + (n-1) + ... + 1 = n(n+1)/2  →  O(n²)
```

### Nested Loop 3 — O(n log n)
```java
for (int i = 0; i < n; i++)           // outer: O(n)
    for (int j = 1; j < n; j *= 2) { } // inner: O(log n) — j doubles each time
// Total: O(n × log n)
```

---

## Algorithm Analyses

### Bubble Sort — O(n²)
- Outer loop: n passes
- Inner loop: up to n comparisons per pass
- Total ≈ n²/2 comparisons → **O(n²)**
- Space: **O(1)** — sorts in-place, no extra memory

### Binary Search — O(log n)
- Each step cuts the search space in half
- n → n/2 → n/4 → ... → 1 takes **log₂(n) steps**
- Space: **O(1)** iterative / O(log n) recursive (call stack)

### Merge Sort — O(n log n)
- **log n levels** of recursion (halving each time)
- At **each level**, merge does O(n) total work across all calls
- Total: log n levels × O(n) = **O(n log n)**
- Space: **O(n)** — needs a temp array for merging

---

## Recursive Complexity Analysis

### How to analyze recursion
1. Count how many recursive calls are made
2. Count the work done at each call (excluding the recursive part)
3. Multiply (or use the recurrence relation)

### Factorial / Sum of N — O(n) time, O(n) space
```
factorial(n) calls factorial(n-1) calls ... factorial(1)
→ n calls total, O(1) work each
→ Time: O(n),  Space: O(n) — call stack is n levels deep
```

### Fibonacci (naive) — O(2^n) time, O(n) space
```
fib(n) makes 2 calls → each makes 2 calls → tree doubles each level
→ ~2^n total calls
→ Time: O(2^n),  Space: O(n) — max depth of call stack is n
```

### Power Function — 3 versions
| Version | Recurrence | Time | Space |
|---------|-----------|------|-------|
| Naive: `x * power(x, n-1)` | T(n) = T(n-1) + O(1) | O(n) | O(n) |
| Fast: halve n each call | T(n) = T(n/2) + O(1) | O(log n) | O(log n) |
| Iterative (bit trick) | — | O(log n) | O(1) |

### Merge Sort Recurrence
```
T(n) = 2 × T(n/2) + O(n)
     ↑ two halves   ↑ merge step

Solving: T(n) = O(n log n)
```

---

## Space Complexity Quick Rules

| Code pattern | Space |
|-------------|-------|
| Fixed variables only | O(1) |
| Array of size n | O(n) |
| 2D array n×n | O(n²) |
| Recursion depth d | O(d) for call stack |
| Recursion + new array each call | O(n × d) |

---

## How to Approach a Coding Question
1. **Understand** — read carefully, note constraints (n ≤ 10⁵? n ≤ 10?)
2. **Brute force first** — simplest correct solution, even if slow
3. **Analyze** — what's the time/space of brute force?
4. **Optimize** — can you reduce it? What bottleneck can you eliminate?
5. **Code** — implement the optimized version
6. **Test** — edge cases: empty input, single element, duplicates, negatives

### Constraint → Complexity guide (what the expected solution probably is)
| n | Target complexity |
|---|------------------|
| n ≤ 10 | O(n!) or O(2^n) fine |
| n ≤ 20 | O(2^n) fine |
| n ≤ 500 | O(n²) fine |
| n ≤ 10⁵ | O(n log n) needed |
| n ≤ 10⁶ | O(n) needed |
| n ≤ 10⁹ | O(log n) needed |

---

## Master's Theorem (for D&C recurrences)
```
T(n) = a × T(n/b) + O(n^d)

Compare  log_b(a)  vs  d:

If log_b(a) > d  →  T(n) = O(n^(log_b a))
If log_b(a) = d  →  T(n) = O(n^d × log n)
If log_b(a) < d  →  T(n) = O(n^d)
```
Merge Sort: a=2, b=2, d=1 → log₂2 = 1 = d → **O(n log n)** ✓
Binary Search: a=1, b=2, d=0 → log₂1 = 0 = d → **O(log n)** ✓
