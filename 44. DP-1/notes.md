# 44. DP-1

## 1. Core Idea

### What is DP?

Dynamic Programming is a technique that efficiently solves problems built
from smaller repeated subproblems, by solving each subproblem once and
reusing the answer instead of recomputing it.

DP is optimized recursion. The classic example is Fibonacci:

```text
fib(n) = fib(n-1) + fib(n-2)
```

Plain recursion recomputes `fib(n-2)` many times (once directly, once
inside the `fib(n-1)` branch, and so on). DP removes that waste.

A problem is a DP problem when it has both properties:

1. **Overlapping subproblems** - the same smaller input is needed
   multiple times while solving the bigger input.
2. **Optimal substructure** - the answer to the full problem can be built
   directly from the answers to its subproblems.

### How to identify DP

Ask two questions:

- **Is it an optimization or counting problem?** ("minimum," "maximum,"
  "count the number of ways," "is it possible.")
- **Is there a choice?** Does the recursion tree branch into multiple
  options at each step (take it / skip it, go left / go right, use 1
  step / use 2 steps)? A choice is what creates the overlapping calls in
  the first place.

If both are true, the brute-force recursive solution almost always
becomes a DP solution.

### Ways of DP

There are two ways to remove the repeated work, and both compute the
exact same answer:

**Memoization (top-down)**

Keep the plain recursive structure, but cache each result the first time
it's computed.

```java
// pseudocode
solve(n) {
    if (base case) return base value;
    if (memo[n] already set) return memo[n];
    memo[n] = combine(solve(smaller inputs));
    return memo[n];
}
```

**Tabulation (bottom-up)**

Skip recursion entirely. Build an array from the smallest state up to
`n`, filling each cell from cells that are already known.

```java
// pseudocode
dp[base index] = base value;
for (int i = next; i <= n; i++) {
    dp[i] = combine(dp[smaller indices]);
}
answer = dp[n];
```

Memoization is usually easier to write first, since it mirrors the
brute-force recursion directly. Tabulation is usually faster in practice
(no call-stack overhead) and is what "space-optimized" solutions build
on later.

### Where this fits

This module covers seven core DP shapes, each with roughly ten problems
built on the same recurrence idea:

```text
Fibonacci            <- this folder (DP-1)
0-1 Knapsack
Unbounded Knapsack
LCS (Longest Common Subsequence)
Kadane's Algorithm (Arrays)
Catalan Number
DP on Grid (2D Arrays)
```

DP-1 covers the **Fibonacci pattern**: problems where `answer(n)` depends
on a small fixed set of smaller answers, the way `fib(n)` depends on
`fib(n-1)` and `fib(n-2)`.

## 2. How to Recognize This Pattern

Consider the Fibonacci DP pattern when:

- the answer for `n` is defined directly in terms of the answer for a
  few smaller values of `n` (not a whole subarray or subset);
- there's a "how many ways" or "what's the count" framing;
- the naive recursive solution has the shape `f(n) = f(n - a) + f(n - b) + ...`.

## 3. Problems in This Folder

### Climbing Stairs

**What the question asks**

Count the number of distinct ways to reach step `n`, where each move
climbs either 1 or 2 steps.

**Brute-force approach (recursion)**

- `ways(n) = ways(n-1) + ways(n-2)`.
- Base cases: `ways(0) = 1` (already there), `ways(n) = 0` for `n < 0`
  (overshot).
- Time: O(2^n) - the call tree branches in two at every step.
- Space: O(n) recursion stack.

**Optimized approach**

**Memoization**

- Keep the exact same recurrence and base cases.
- Before recursing, check a `memo[]` array (sentinel value `-1` means
  "not computed yet"). Store the result before returning.
- Time: O(n) - each of the `n` states is computed once.
- Space: O(n) memo array + O(n) recursion stack.

**Tabulation**

- `dp[0] = dp[1] = 1`.
- For `i` from `2` to `n`: `dp[i] = dp[i-1] + dp[i-2]`.
- Return `dp[n]`.
- Time: O(n). Space: O(n) array, no recursion stack.

**Why it works**

Any way to reach step `n` takes its last move from either step `n-1`
(a 1-step move) or step `n-2` (a 2-step move), and those two cases never
overlap. So the total count is exactly the sum of the ways to reach
those two earlier steps.

**Interview explanation**

"This is Fibonacci in disguise: the number of ways to reach step n is
the ways to reach n-1 plus the ways to reach n-2, because the last move
is either a single step or a double step. Plain recursion recomputes the
same smaller steps repeatedly, so I cache them - either top-down with a
memo array or bottom-up with a dp array - to bring it from exponential
down to linear time."

**Common follow-up questions**

- Why does `ways(0) = 1` and not `0`? It represents the single "do
  nothing, you're already there" path, which is what makes the
  recurrence correct for `ways(2) = ways(1) + ways(0)`.
- Why is plain recursion exponential? Every call spawns two more calls
  until the base case, so the call count roughly doubles per level.
- Can this be done in O(1) space? Yes - since `dp[i]` only needs the
  previous two values, two variables can replace the whole array.

**Dry run**

For `n = 5`:

```text
ways(5) = ways(4) + ways(3)
ways(4) = ways(3) + ways(2)
ways(3) = ways(2) + ways(1)
ways(2) = ways(1) + ways(0) = 1 + 1 = 2
ways(1) = 1
ways(3) = 2 + 1 = 3
ways(4) = 3 + 2 = 5
ways(5) = 5 + 3 = 8
```

Tabulation builds the same numbers left to right instead of top down:

```text
dp = [1, 1, 2, 3, 5, 8]
      0  1  2  3  4  5
```

**Common mistakes**

- Setting `ways(0) = 0` instead of `1`, which breaks every value built
  on top of it.
- Forgetting the `n < 0` base case, causing infinite recursion or an
  off-by-one wrong answer.
- Re-deriving `dp[i]` from scratch instead of reusing `dp[i-1]` and
  `dp[i-2]` (defeats the purpose of tabulation).

### Climbing Stairs Variation (k steps at a time)

**What the question asks**

Same as Climbing Stairs, but each move can climb anywhere from `1` to
`k` steps instead of only `1` or `2`.

**Brute-force approach**

- `ways(n) = ways(n-1) + ways(n-2) + ... + ways(n-k)`.
- Same idea as before, just summed over `k` smaller states instead of 2.
- Time: O(k^n) in the worst case. Space: O(n) recursion stack.

**Optimized approach**

- Tabulated directly: `dp[0] = 1`, and for each `i` from `1` to `n`, sum
  `dp[i - step]` for every `step` from `1` to `k` where `i - step >= 0`.
- Time: O(n * k) - for each of the `n` states, look back at up to `k`
  previous states.
- Space: O(n) for the dp array.

**Why it works**

Same reasoning as plain Climbing Stairs, generalized: the last move to
reach step `i` used some `step` between `1` and `k`, and those `k` cases
are mutually exclusive and cover every path, so summing them gives the
total.

**Interview explanation**

"It's the same recurrence as Climbing Stairs, just widened from 2 choices
to k choices per step. Instead of `dp[i-1] + dp[i-2]`, I sum `dp[i-step]`
for every step from 1 to k that doesn't go below 0. Setting k = 2
collapses this back to the exact same numbers as plain Climbing Stairs."

**Common follow-up questions**

- What happens when `k = 2`? It reduces to exactly the original Climbing
  Stairs problem and produces identical output.
- Why guard `i - step >= 0`? Early steps (small `i`) don't have `k` full
  smaller states available yet - only sum the ones that exist.
- What's the time cost of widening from 2 to k? It grows from O(n) to
  O(n * k), since each state now looks back over k values instead of 2.

**Dry run**

For `k = 3`:

```text
dp[0] = 1
dp[1] = dp[0]                         = 1
dp[2] = dp[1] + dp[0]                 = 1 + 1         = 2
dp[3] = dp[2] + dp[1] + dp[0]         = 2 + 1 + 1     = 4
dp[4] = dp[3] + dp[2] + dp[1]         = 4 + 2 + 1     = 7
```

**Common mistakes**

- Summing `dp[i - step]` without checking `i - step >= 0`, causing an
  array index out of bounds for small `i`.
- Hardcoding 2 terms instead of looping `step` from `1` to `k`.
- Forgetting that `dp[0] = 1` (the empty "already there" path) still
  applies here, just like in plain Climbing Stairs.

## 4. Topic-Level Interview Questions

**What is DP?**
A technique for solving problems with overlapping subproblems and
optimal substructure by solving each subproblem once and reusing the
result.

**What are the two properties every DP problem has?**
Overlapping subproblems and optimal substructure.

**How do you recognize a DP problem?**
It's an optimization/counting problem, and a choice exists that creates
multiple branches in the recursion tree.

**Memoization vs. tabulation?**
Memoization is top-down: keep the recursion, cache results. Tabulation
is bottom-up: build an array iteratively from the base case up, no
recursion needed.

**Why is plain recursion on Climbing Stairs O(2^n)?**
Every call with `n > 0` branches into two more calls, so the number of
calls roughly doubles with each additional step until the base case.

**Why do DP and Fibonacci get mentioned together so often?**
Fibonacci is the simplest possible case of the pattern: `answer(n)`
depends only on `answer(n-1)` and `answer(n-2)`. Many "count the ways"
problems reduce to this same shape with different base cases or a wider
window (like the k-steps variation).

## 5. Quick Revision Sheet

### Templates

```text
Memoization (top-down):
  solve(n):
    if base case: return base value
    if memo[n] set: return memo[n]
    memo[n] = combine(solve(smaller n))
    return memo[n]

Tabulation (bottom-up):
  dp[base index] = base value
  for i = next .. n:
    dp[i] = combine(dp[smaller indices])
  answer = dp[n]
```

### Time & space complexity

| Problem | Approach | Time | Space |
|---|---|---:|---:|
| Climbing Stairs | Recursion | O(2^n) | O(n) |
| Climbing Stairs | Memoization | O(n) | O(n) |
| Climbing Stairs | Tabulation | O(n) | O(n) |
| Climbing Stairs (k steps) | Tabulation | O(n·k) | O(n) |

Space for every version here is O(n): the recursive version spends it on
the call stack, memoization spends it on the call stack *and* the memo
array, and tabulation spends it purely on the dp array with no stack at
all. (This carries into 45. DP-2's table too, for comparison - Knapsack
problems need O(n·W) on both axes since their state has two dimensions
instead of one.)

### One-line reminders

- DP = optimized recursion; you're removing repeated work, not changing
  the answer.
- Two identifying questions: is it optimize/count, and is there a
  choice?
- `ways(0) = 1` is the "already there" base case that makes the whole
  recurrence line up correctly.
- Memoization mirrors the recursion tree; tabulation replaces it with a
  loop.
- A k-steps generalization is just summing more previous states, not a
  new algorithm.
