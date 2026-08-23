# 18. Recursion Basics (Part 1) — Key Notes

---

## What is Recursion?
- A function that **calls itself** to solve a smaller version of the same problem.
- Works by breaking a big problem into identical smaller subproblems.
- Java uses the **Call Stack** to track each recursive call.

---

## Two Essential Parts of Every Recursive Function
```
1. BASE CASE   — when to STOP (returns a value directly, no recursive call)
2. RECURSIVE CASE — calls itself with a SMALLER/SIMPLER input
```
> Missing base case = **infinite recursion** = Stack Overflow error.

---

## How the Call Stack Works
Each recursive call creates a new **stack frame** (local variables, return address).
When the base case is hit, frames are popped off and results are returned upward.

```
factorial(3)
  └─ 3 * factorial(2)
         └─ 2 * factorial(1)
                └─ 1 * factorial(0)  ← base case returns 1
              ← returns 1*1 = 1
       ← returns 2*1 = 2
  ← returns 3*2 = 6
```

---

## Stack Overflow
- Occurs when recursion is **too deep** — call stack runs out of memory.
- Caused by: missing base case, base case never reached, or too large input.
- Java's default stack depth is typically ~1000–10000 calls depending on JVM.

---

## Pre-order vs Post-order Recursion

| | Pre-order | Post-order |
|--|-----------|------------|
| Work done | **Before** recursive call | **After** recursive call (on the way back) |
| Effect | Top-down | Bottom-up |
| Example | Print Decreasing | Print Increasing |

```java
// Pre-order (decreasing): print THEN recurse
void dec(int n) { print(n); dec(n-1); }   // prints: 5 4 3 2 1

// Post-order (increasing): recurse THEN print
void inc(int n) { inc(n-1); print(n); }   // prints: 1 2 3 4 5
```

---

## Problems Covered

### Print Decreasing (n → 1)
- Print `n`, then recurse with `n-1`.
- Base case: `n == 0`.

### Print Increasing (1 → n)
- Recurse with `n-1` first, then print `n` on the way back.
- Base case: `n == 0`.
- Same function as decreasing — just swapped order of print and recurse!

### Factorial
```
n! = n × (n-1)!
Base: 0! = 1, 1! = 1
Time: O(n)
```

### Sum of N Natural Numbers
```
Sum(n) = n + Sum(n-1)
Base: Sum(0) = 0
Time: O(n)   (same as: n*(n+1)/2 formula, but recursion is for practice)
```

### Nth Fibonacci
```
fib(n) = fib(n-1) + fib(n-2)
Base: fib(0)=0, fib(1)=1
Time: O(2^n) — EXPONENTIAL (bad for large n, good to understand the concept)
```
> For large n, use **memoization** (store results you've already computed).

### Check if Array is Sorted
```
Compare arr[i] and arr[i+1].
If out of order → false.
If reached last element → true.
Time: O(n)
```

### First Occurrence
```
Search left to right.
Return index immediately when found.
Return -1 if whole array searched.
Time: O(n)
```

### Last Occurrence
```
Search the REST of the array first (recurse).
If rest found something → that's the last.
Else check current index.
Time: O(n)
```

### x^n — Naive
```
x^n = x × x^(n-1)
Base: x^0 = 1
Time: O(n)
```

### x^n — Optimized (Fast Exponentiation)
```
x^n = (x^(n/2))² if n is even
x^n = x × (x^(n/2))² if n is odd
Base: x^0 = 1
Time: O(log n)  ← halves the problem each step
```
Example: `2^10` → `2^5` → `2^2` → `2^1` → `2^0` (only 4 levels deep instead of 10).

---

## Recursion vs Iteration
| | Recursion | Iteration (loop) |
|--|-----------|-----------------|
| Cleaner code for | Tree/graph problems, divide & conquer | Simple linear problems |
| Stack usage | Uses call stack (limited) | No stack overhead |
| Risk | Stack overflow | No such risk |
| Time | Often same, sometimes worse due to overhead | Usually faster |

---

## Key Pattern: Identify the Recursive Structure
Ask yourself:
1. What is the **smallest version** of this problem? → Base case
2. If I assume `f(n-1)` is solved, how do I solve `f(n)`? → Recursive case

---

## Common Pitfalls
1. **No base case** → infinite recursion → StackOverflowError.
2. **Base case never reached** → same as above (e.g. decrementing wrong variable).
3. **Not returning the recursive result** → `return func(n-1)` not just `func(n-1)`.
4. **Naive Fibonacci on large n** → extremely slow due to repeated subproblems.
