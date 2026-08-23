# 45. DP-2

## 1. Core Idea

### Types of Knapsack problems

Every knapsack problem gives you items with a value and a weight, plus a
capacity limit, and asks you to choose items that fit within the
capacity while optimizing something. There are three variants:

1. **Fractional Knapsack** - items can be split into fractions. Solved
   greedily (sort by value/weight ratio), not with DP. Out of scope
   here.
2. **0-1 Knapsack** - each item is taken whole or not at all, and at
   most once.
3. **Unbounded Knapsack** - each item can be taken whole or not at all,
   but any number of times (unlimited supply).

### The general recursion shape

Every knapsack recursion takes four things: `val[]` and `wt[]` (fixed,
never change between calls), a remaining capacity `W`, and a count of
items still under consideration `n`. At each item there's a binary
choice:

```text
knapsack(val[], wt[], W, n):
  if W == 0 or n == 0: return 0          // base case
  if wt[n-1] <= W:                        // item fits, choice exists
    include -> val[n-1] + knapsack(val, wt, W - wt[n-1], n-1)
    exclude -> knapsack(val, wt, W, n-1)
    return max(include, exclude)
  else:                                    // item doesn't fit
    return knapsack(val, wt, W, n-1)       // forced exclude
```

### Why the state is 2D now

In DP-1 (Fibonacci pattern), the only thing that changed between calls
was `n`, so one array indexed by `n` was enough to cache every state.
Here, **two** things change between calls: the item count `n` and the
remaining capacity `W`. So the cache needs two dimensions:

```java
int[][] dp = new int[n + 1][W + 1];
```

`dp[i][j]` always means "the best answer using the first `i` items with
capacity `j`." This is the general shape for every knapsack-family
problem, including the two variations below.

### 0-1 vs. Unbounded: the one-line difference

Both use the exact same tabulation loop. The only difference is which
row the "include" case reads from:

```text
0-1 Knapsack (item used at most once):
  dp[i][j] = max(val[i-1] + dp[i-1][j - wt[i-1]], dp[i-1][j])
                              ^^^^^ previous row: item i is now used up

Unbounded Knapsack (item reusable):
  dp[i][j] = max(val[i-1] + dp[i][j - wt[i-1]],   dp[i-1][j])
                              ^^^ same row: item i can be picked again
```

## 2. How to Recognize This Pattern

Consider the Knapsack DP pattern when:

- the problem gives items with a value/cost trade-off and a single
  capacity constraint (weight, budget, time);
- the question asks for a max/min optimal selection, or whether some
  target is reachable at all;
- at each item there's a clear binary choice (take it or don't) that
  branches the recursion tree - this is the "choice" signal from DP-1's
  recognition checklist, just with two dimensions of state instead of
  one.

## 3. Problems in This Folder

### 0-1 Knapsack

**What the question asks**

Given `val[] = [15, 14, 10, 45, 30]`, `wt[] = [2, 5, 1, 3, 4]`, and
capacity `W = 7`, choose a subset of items (each used at most once)
that fits within `W` and maximizes total value.

**Brute-force approach (recursion)**

- At each item: if it fits, take the better of including or excluding
  it; if it doesn't fit, it's forced out.
- Base case: `W == 0` or `n == 0` -> `0`.
- Time: O(2^n) - two branches per item.
- Space: O(n) recursion stack.

**Optimized approach**

**Memoization**

- Same recurrence, cached in a 2D `memo[n+1][W+1]` array (sentinel
  `-1`).
- Time: O(n · W) - each `(item, capacity)` pair computed once.
- Space: O(n · W) memo table + O(n) recursion stack.

**Tabulation**

- `dp[i][j]` = best value using the first `i` items with capacity `j`.
- `dp[i][0] = 0` and `dp[0][j] = 0` for all `i`, `j` (Java arrays
  zero-init, so this is free).
- For `i = 1..n`, `j = 1..W`: if `wt[i-1] <= j`,
  `dp[i][j] = max(val[i-1] + dp[i-1][j-wt[i-1]], dp[i-1][j])`,
  otherwise `dp[i][j] = dp[i-1][j]`.
- Time: O(n · W). Space: O(n · W) table, no recursion stack.

**Why it works**

For item `i`, either it's in the optimal subset or it isn't. If it is,
the rest of the optimal subset is the best answer using the remaining
`i-1` items and the reduced capacity `j - wt[i-1]`. If it isn't, the
best answer is just whatever the first `i-1` items achieve with the
same capacity `j`. Taking the max of those two covers every
possibility exactly once.

**Interview explanation**

"At every item I have a binary choice: take it or skip it. If I take
it, I add its value and reduce my remaining capacity by its weight,
moving to the previous item since it's now used up. If I skip it, I
keep the same capacity and just move to the previous item. I take
whichever choice gives more value. Plain recursion revisits the same
(item, capacity) pairs repeatedly, so I cache them in a 2D table -
either top-down with memoization or bottom-up with tabulation - to go
from exponential to O(n·W)."

**Common follow-up questions**

- Why does the memo/dp table need two dimensions here but not in
  Fibonacci-style DP? Because two things vary between calls here (item
  index and remaining capacity), not just one.
- What if an item's weight exceeds the current capacity? It can't be
  included at all - the exclude branch is the only option.
- Can this be space-optimized? Yes - since row `i` only depends on row
  `i-1`, two 1D arrays (or one, updated carefully right-to-left) can
  replace the full 2D table.

**Dry run**

For `n = 5, W = 7` (items 1-indexed by position in `val`/`wt`):

```text
Trying item 5 (wt=4, val=30): include -> n=4, W=3 | exclude -> n=4, W=7
Best path: take item 5 (wt 4, val 30) and item 4 (wt 3, val 45)
Combined weight = 4 + 3 = 7 (exactly fits)
Combined value  = 30 + 45 = 75
max = ans = 75
```

**Common mistakes**

- Using `dp[i-1][...]` in the include branch of Unbounded Knapsack (see
  next problem) - that's the 0-1 rule, not the unbounded one.
- Forgetting the `wt[i-1] <= j` guard before indexing
  `dp[?][j - wt[i-1]]`, which can go negative.
- Off-by-one between 1-indexed items (`i`) and 0-indexed arrays
  (`val[i-1]`, `wt[i-1]`).

### Target Sum Subset

**What the question asks**

Given `numbers = [4, 2, 7, 1, 3]` and `target = 10`, decide whether any
subset of `numbers` sums to exactly `10`. This is a variation of 0-1
Knapsack: there's no value array, "weight" is the number itself,
"capacity" is the target, and the answer is boolean instead of a
maximum.

**Optimized approach (tabulation only)**

- `dp[i][j]` = true if some subset of the first `i` numbers sums to
  exactly `j`.
- `dp[i][0] = true` for every `i` (the empty subset always sums to 0).
- For `i = 1..n`, `j = 1..target`: let `v = numbers[i-1]`.
  - include: usable if `v <= j` and `dp[i-1][j-v]` is true.
  - exclude: usable if `dp[i-1][j]` is already true.
  - `dp[i][j]` is true if either case holds.
- Time: O(n · target). Space: O(n · target) for the dp table.

**Why it works**

Same include/exclude reasoning as 0-1 Knapsack, just tracking
reachability instead of a maximum. A sum `j` is reachable using the
first `i` numbers exactly when it was already reachable without number
`i` (exclude), or it becomes reachable by adding number `i` to some
smaller reachable sum (include).

**Interview explanation**

"This is 0-1 Knapsack with the value array removed and the max
replaced by a boolean OR. dp[i][j] asks: can the first i numbers hit
sum j? Either I skip number i and rely on dp[i-1][j], or I use it and
rely on dp[i-1][j - numbers[i-1]] having already been true. Since every
number can only be used once, this is exactly the 0-1 shape."

**Common follow-up questions**

- Why is `dp[i][0]` always true? Because summing zero numbers gives 0 -
  the empty subset is always a valid (trivial) answer for target 0.
- What's the difference from 0-1 Knapsack's tabulation? No value array
  and no `max` - just an OR between the include and exclude cases.
- How would you recover *which* subset was used, not just whether one
  exists? Backtrack through the table from `dp[n][target]`, checking at
  each step whether the exclude case alone explains `true`; if not, the
  current number was included.

**Dry run**

For `numbers = [4, 2, 7, 1, 3], target = 10`: `{7, 2, 1}` sums to 10, so
`dp[5][10] = true`. (`{7, 3}` and `{4, 2, 1, 3}` also work - only one
valid subset needs to exist.)

**Common mistakes**

- Forgetting `dp[i][0] = true` and getting every answer wrong, since
  every row's base case depends on it.
- Using `max`/addition instead of a boolean OR - this problem asks
  "is it possible," not "what's the best."

### Unbounded Knapsack

**What the question asks**

Same setup as 0-1 Knapsack (`val[]`, `wt[]`, capacity `W`), but each
item may be taken any number of times.

**Optimized approach (tabulation only)**

- Identical loop structure to 0-1 Knapsack's tabulation, with exactly
  one change: the include case reads from **row `i`** instead of row
  `i-1`.
  - `wt[i-1] <= j`: `dp[i][j] = max(val[i-1] + dp[i][j-wt[i-1]], dp[i-1][j])`
  - otherwise: `dp[i][j] = dp[i-1][j]`
- Time: O(n · W). Space: O(n · W) for the dp table.

**Why it works**

Staying on row `i` for the include case means "item `i` is still
available" - the recurrence can immediately reuse it again on the next
capacity step, which is exactly what "unlimited supply" means. Dropping
to row `i-1` (like 0-1 Knapsack does) would consume the item
permanently after one use.

**Interview explanation**

"It's the same table and the same loop as 0-1 Knapsack. The only change
is one array index: when I include item i, I look at dp[i][...] instead
of dp[i-1][...], because the item isn't used up - it can be picked
again on top of itself. That single index change is the entire
difference between 'at most once' and 'unlimited supply.'"

**Common follow-up questions**

- What's the actual code difference from 0-1 Knapsack? One character:
  `dp[i][j - wt[i-1]]` instead of `dp[i-1][j - wt[i-1]]` in the include
  case.
- Using the same val/wt/W as the 0-1 example (W=7), why is the answer
  higher (100 vs. 75)? Because item 4 (wt 3, val 45) can now be taken
  twice (wt 6, val 90) plus item 3 once (wt 1, val 10) = weight 7,
  value 100 - not possible under the "once only" 0-1 rule.
- Does the recursion/memoization version need anything different from
  0-1 Knapsack? Same one-index change applies there too, though only
  the tabulated form is covered in this folder.

**Dry run**

Same `val`/`wt`/`W` as the 0-1 example. Best combination: item 4 twice
(weight 3+3=6, value 45+45=90) plus item 3 once (weight 1, value 10) =
weight 7, value 100.

**Common mistakes**

- Copying the 0-1 Knapsack tabulation exactly and forgetting to change
  `dp[i-1][...]` to `dp[i][...]` in the include case - silently caps
  every item at one use.
- Assuming Unbounded Knapsack always beats 0-1 Knapsack by a lot - the
  gap depends entirely on whether any item has a much better
  value-to-weight ratio worth repeating.

## 4. Topic-Level Interview Questions

**Why does Knapsack need a 2D dp table when Fibonacci-style DP only
needed 1D?**
Because two things change between recursive calls here (item index and
remaining capacity), so both need to be part of the state.

**What's the one-line difference between 0-1 and Unbounded Knapsack?**
In the include case, 0-1 Knapsack drops to the previous item row
(`dp[i-1][...]`); Unbounded Knapsack stays on the current item row
(`dp[i][...]`) since the item can be reused.

**How is Target Sum Subset related to 0-1 Knapsack?**
Same include/exclude recurrence and same 0-1 "used at most once" rule,
just tracking boolean reachability instead of maximum value, with no
separate value array.

**Why is the brute-force recursion O(2^n)?**
Every item creates a binary branch (include/exclude), so the call tree
doubles with each additional item.

**What's the time complexity once memoized or tabulated?**
O(n · W) for value-based Knapsack problems, O(n · target) for Target
Sum Subset - proportional to the number of distinct states in the 2D
table.

## 5. Quick Revision Sheet

### Templates

```text
0-1 Knapsack include/exclude (recursion / memo / tabulation all share this):
  include: value[i-1] + solve(..., capacity - weight[i-1], i-1)
  exclude: solve(..., capacity, i-1)
  answer:  max(include, exclude)   [or boolean OR, for Target Sum Subset]

0-1 vs. Unbounded, tabulation include case:
  0-1:        dp[i][j] = val[i-1] + dp[i-1][j - wt[i-1]]   // row i-1
  Unbounded:  dp[i][j] = val[i-1] + dp[i][j - wt[i-1]]     // row i
```

### Time & space complexity - DP-1 and DP-2

| Folder | Problem | Approach | Time | Space |
|---|---|---|---:|---:|
| 44. DP-1 | Climbing Stairs | Recursion | O(2^n) | O(n) |
| 44. DP-1 | Climbing Stairs | Memoization | O(n) | O(n) |
| 44. DP-1 | Climbing Stairs | Tabulation | O(n) | O(n) |
| 44. DP-1 | Climbing Stairs (k steps) | Tabulation | O(n·k) | O(n) |
| 45. DP-2 | 0-1 Knapsack | Recursion | O(2^n) | O(n) |
| 45. DP-2 | 0-1 Knapsack | Memoization | O(n·W) | O(n·W) |
| 45. DP-2 | 0-1 Knapsack | Tabulation | O(n·W) | O(n·W) |
| 45. DP-2 | Target Sum Subset | Tabulation | O(n·target) | O(n·target) |
| 45. DP-2 | Unbounded Knapsack | Tabulation | O(n·W) | O(n·W) |

### One-line reminders

- Knapsack state = (items remaining, capacity remaining) - that's why
  the table is 2D.
- Binary choice at every item: include (if it fits) vs. exclude.
- 0-1 vs. Unbounded is a single index change in the include case: row
  `i-1` vs. row `i`.
- "Does a subset exist" problems (Target Sum Subset) reuse the exact
  same shape as "maximize value" problems, with OR replacing max.
- Recursion is always O(2^n); memoization and tabulation bring value-
  based Knapsack problems down to O(n·W).
