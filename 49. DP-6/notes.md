# 49. DP-6

## 1. Core Idea

### Matrix Basics: the rule behind MCM's cost formula

Two matrices `A (a x b)` and `B (c x d)` can only be multiplied if
`b == c` (the first matrix's column count matches the second's row
count); the result is an `a x d` matrix, produced at a cost of
`a * b * d` scalar multiplications. Example: `A (1x2) . B (2x3)` is
valid (`2 == 2`), produces a `1x3` result, at a cost of `1*2*3 = 6`
multiplications. This single fact - cost = `rows(left) * shared *
cols(right)` - is the entire cost formula Matrix Chain Multiplication
is built on.

### Matrix Chain Multiplication: a genuinely new DP shape - interval DP

Every DP so far in this series has had a state built from one or two
*positions* (an index, a pair of prefix lengths, a capacity). MCM's
state is a **range**: `(i, j)` means "the matrices from `i` to `j`,
multiplied together in whatever order is cheapest." The recurrence
tries every possible place to split that range:

```text
mcm(i, j):
  if i == j: return 0                      // a single matrix, nothing to multiply
  best = infinity
  for k = i .. j-1:
    cost = mcm(i, k) + mcm(k+1, j) + arr[i-1]*arr[k]*arr[j]
    best = min(best, cost)
  return best
```

`arr[i-1] * arr[k] * arr[j]` is exactly the Matrix Basics cost formula:
the combined left group has `arr[i-1]` rows, the combined right group
has `arr[j]` columns, and `arr[k]` is the shared dimension where they
meet.

This "try every split point over a range" shape is a cousin of
Catalan's Number (48. DP-5) - both sum/minimize over every split - but
MCM's split cost is a concrete multiplication cost formula, not a
count, and the DP table is genuinely 2D over `(i, j)` ranges rather
than 1D over a single size.

### Tabulation over ranges: fill by increasing length, not by row

Because `dp[i][j]` depends on every smaller range strictly inside it,
a plain "row by row" fill order doesn't work here - a cell might need
`dp[i][k]` for some `k` that hasn't been computed yet if rows are
filled left to right. Instead, the table is filled by **increasing
range length**: first every range of length 2, then every range of
length 3, and so on, so that whenever `dp[i][j]` is being computed,
every range strictly inside `(i, j)` is already known.

### Minimum Partitioning: 0-1 Knapsack again, with a max instead of a boolean

Splitting an array into two subsets with the smallest possible sum
difference reuses 45. DP-2's exact 0-1 Knapsack table shape - "value"
and "weight" are both just the element itself, and "capacity" is half
the total sum:

```text
dp[i][j] = largest subset sum achievable from the first i elements, not exceeding j
W = totalSum / 2
answer = totalSum - 2 * dp[n][W]
```

This is a third variant of the same 0-1 Knapsack table alongside
maximize-value (DP-2) and does-a-subset-exist (Target Sum Subset,
DP-2): here it tracks the best achievable value directly, which is
exactly what "closest possible partition" needs.

### Minimum Array Jumps: a new "steps to reach a target" shape

`dp[i]` = minimum jumps from index `i` to the last index, filled
**backward** from the end. Unlike DP-1's Climbing Stairs (a fixed
window of the previous 1-2 states), each index here looks forward over
a **variable-size window** determined by its own value (`nums[i]`),
and takes the minimum, not a sum:

```text
dp[n-1] = 0
for i = n-2 down to 0:
  dp[i] = min(dp[j] + 1) over every j reachable from i (i+1 .. i+nums[i])
          that already has a known answer
```

## 2. How to Recognize This Pattern

- **Interval DP (MCM):** the problem is about a *sequence* of items
  where you choose an order/grouping to combine them (matrix
  multiplication order, optimal BST construction, polygon
  triangulation), and cost depends on *how* they're grouped. State =
  a range `(i, j)`; recurrence tries every split point inside it.
- **Knapsack-shaped, max-value variant (Minimum Partitioning):**
  "split into two groups as evenly as possible" - reduce it to
  "find the best achievable sum not exceeding half the total," which
  is 0-1 Knapsack with value = weight = the element itself.
- **Backward reachability DP (Minimum Array Jumps):** "minimum
  steps/cost to reach a target," where each position can reach a
  *range* of future positions (not just a fixed offset) - fill from
  the target backward, taking the best of everything reachable.

## 3. Problems in This Folder

### Matrix Chain Multiplication (MCM)

**What the question asks**

Given `arr = [1, 2, 3, 4, 3]`, describing 4 matrices (matrix `k` has
dimensions `arr[k-1] x arr[k]`: `A1=1x2, A2=2x3, A3=3x4, A4=4x3`), find
the minimum total scalar multiplications needed to multiply the whole
chain, by choosing the cheapest parenthesization.

**Brute-force approach (recursion)**

- Base case: `i == j` (single matrix) -> `0`.
- Otherwise, try every split point `k` from `i` to `j-1`: cost =
  `mcm(i,k) + mcm(k+1,j) + arr[i-1]*arr[k]*arr[j]`. Take the minimum
  over every `k`.
- Time: exponential - ranges overlap heavily and get re-explored.
- Space: O(n) recursion stack.

**Optimized approach**

**Memoization**

- Same recurrence, cached in a 2D `memo[i][j]` array (sentinel `-1`).
- Time: O(n³) - O(n²) distinct ranges, each trying up to O(n) split
  points.
- Space: O(n²) memo table + O(n) recursion stack.

**Tabulation**

- `dp[i][i] = 0` for every `i` (default zero-init).
- For `len = 2..(n-1)` (chain lengths, `n-1` = number of matrices),
  for each start `i = 1..(n-len)`, let `j = i+len-1`. Try every split
  `k = i..j-1` with the same cost formula, keeping the minimum.
- Return `dp[1][n-1]`.
- Time: O(n³). Space: O(n²) for the dp table.

**Why it works**

Multiplying a chain of matrices left to right isn't always cheapest -
grouping choices change how large the intermediate matrices get, which
changes cost. Every valid full parenthesization has *some* outermost
split point `k` (the last multiplication performed), dividing the
chain into a left group `i..k` and a right group `k+1..j`, each
independently optimized. Trying every possible outermost split and
keeping the cheapest guarantees the true minimum is found.

**Interview explanation**

"The state is a range of matrices, not a single index. For range
(i, j), I try every split point k - multiply i..k optimally, multiply
k+1..j optimally, then combine those two results at a cost of
arr[i-1]*arr[k]*arr[j], since that's the row count, shared dimension,
and column count of the two intermediate results. Because dp[i][j]
depends on smaller ranges strictly inside it, tabulation has to fill
by increasing range length, not by row - every range of length 2
first, then length 3, and so on."

**Common follow-up questions**

- Why does tabulation loop by `len` instead of a simple nested `i, j`
  loop? Because `dp[i][j]` depends on `dp[i][k]` and `dp[k+1][j]` for
  various `k` inside the range, both of which must already be
  computed - guaranteed only if shorter ranges are filled first.
- What does `arr[i-1] * arr[k] * arr[j]` represent? The cost of the
  final combining multiplication: the left group's row count
  (`arr[i-1]`), the shared middle dimension (`arr[k]`), and the right
  group's column count (`arr[j]`).
- How does this relate to Catalan's Number (48. DP-5)? Both try every
  split point over a range and combine two independent sub-results -
  MCM minimizes a concrete cost per split, Catalan counts
  arrangements per split.

**Dry run**

For `arr = [1, 2, 3, 4, 3]`: the cheapest grouping is
`((A1 A2) A3) A4`: `A1A2` costs `1*2*3=6` (result `1x3`); `(A1A2)A3`
costs `1*3*4=12` (result `1x4`); `((A1A2)A3)A4` costs `1*4*3=12`.
Total: `6+12+12 = 30`.

**Common mistakes**

- Filling the tabulation table by row/column instead of by increasing
  range length - reads uncomputed cells.
- Forgetting `dp[i][i] = 0` matters as the base every larger range
  ultimately builds on.
- Mixing up which array index represents which matrix boundary -
  `arr[i-1]` and `arr[j]` are the outer dimensions of the *combined*
  range, not of a single matrix.

### Minimum Partitioning

**What the question asks**

Given `arr = [1, 6, 11, 5]`, split it into two subsets so the absolute
difference between their sums is as small as possible.

**Optimized approach (0-1 Knapsack reuse)**

- `sum` = total of all elements (`23` here). `W = sum / 2` (`11`).
- `dp[i][j]` = largest subset sum achievable from the first `i`
  elements, not exceeding `j` - identical table shape to 0-1 Knapsack
  (45. DP-2), with `val[i-1] = wt[i-1] = arr[i-1]`.
- For `i = 1..n`, `j = 1..W`: if `arr[i-1] <= j`,
  `dp[i][j] = max(arr[i-1] + dp[i-1][j-arr[i-1]], dp[i-1][j])`;
  otherwise `dp[i][j] = dp[i-1][j]`.
- The best achievable subset sum is `dp[n][W]`; the answer is
  `sum - 2 * dp[n][W]`.
- Time: O(n · sum). Space: O(n · sum) for the dp table.

**Why it works**

Any partition's two subsets sum to `sum` together, so minimizing their
difference is the same as finding a subset sum as close as possible to
`sum/2` without exceeding it (going over `sum/2` would just produce
the mirror-image partition anyway, so capping the search at `sum/2` is
sufficient and avoids double-counting complementary partitions). Once
the best such subset sum `S` is found, the other subset is `sum - S`,
and the difference is `(sum - S) - S = sum - 2S`.

**Interview explanation**

"I reduce this to 0-1 Knapsack: value and weight are both just the
element itself, and capacity is half the total sum. dp[n][W] gives the
largest subset sum I can hit without going over half the total - the
closest I can get one side to being equal. The other side is whatever
sum is left over, so the minimum difference is the total sum minus
twice that best achievable half."

**Common follow-up questions**

- Why cap the capacity at `sum/2` instead of searching the full range?
  Because any subset summing to more than half just leaves its
  complement summing to less than half - the same partition, found
  from the other side - so capping at half avoids redundant search.
- Why `max` here instead of the boolean OR from Target Sum Subset
  (DP-2)? Because this problem wants the *best* achievable sum, not
  just whether some specific sum is reachable - same table shape,
  different question, same as the Coin Change vs. Rod Cutting
  distinction from 46. DP-3.
- What if all elements are identical? The best partition is as even a
  split of the count as possible - the DP handles this automatically,
  no special case needed.

**Dry run**

`arr = [1, 6, 11, 5]`, `sum = 23`, `W = 11`. Best achievable subset sum
`<= 11` is exactly `11` (the single element `11`). Answer:
`23 - 2*11 = 1` (partition `{11}` vs. `{1, 6, 5} = 12`, difference `1`).

**Common mistakes**

- Writing `dp[i-1][j-arr[i-1]]` correctly but forgetting the guard
  `arr[i-1] <= j` first - indexes negative otherwise.
- Returning `dp[n][W]` directly instead of `sum - 2*dp[n][W]` - that
  gives the best subset sum, not the minimum *difference*.
- Not recognizing this as 0-1 Knapsack and attempting to design a new
  DP from scratch.

### Minimum Array Jumps

**What the question asks**

Given `nums = [2, 3, 1, 1, 4]`, where `nums[i]` is the farthest jump
allowed from index `i`, find the minimum number of jumps needed to go
from index `0` to the last index.

**Optimized approach (tabulation, filled backward)**

- `dp[i]` = minimum jumps from index `i` to the last index.
  `dp[n-1] = 0`; every other index starts at the sentinel `-1`
  ("not yet reachable").
- Fill from `i = n-2` down to `0`: look at every index `j` reachable
  in one jump from `i` (`j` from `i+1` up to `i + nums[i]`, capped at
  `n-1`), and take the smallest `dp[j] + 1` among reachable indices
  whose answer is already known (`dp[j] != -1`).
- Return `dp[0]`.
- Time: O(n²) worst case - each index's inner loop can scan up to
  `nums[i]` positions. Space: O(n) for the dp array.

**Why it works**

Filling backward means that by the time index `i` is being processed,
every index it could possibly jump to already has a correct, final
answer. So `dp[i]` just needs to look at all of its immediate jump
options and pick whichever leads to the target in the fewest total
jumps - one jump to reach `j`, plus however many more jumps `j` itself
still needs.

**Interview explanation**

"I fill this backward from the last index, since dp[i] needs to know
the answer for everything it could jump to, and those are all indices
after i. For each i, I look at every index reachable in one jump -
up to nums[i] steps forward - and take the smallest dp[j] + 1 among
those that already have a known path to the end. dp[0] is then the
answer for the whole array."

**Common follow-up questions**

- Why fill backward instead of forward from index 0? Because `dp[i]`
  depends on the answers for indices *ahead* of `i`, so those must
  already be computed - only guaranteed by working from the end
  backward.
- What does the `-1` sentinel mean here, concretely? "No known path
  from this index to the end yet" - it's skipped when scanning
  reachable indices so a not-yet-reachable position can't corrupt a
  `min` calculation.
- What if the last index is unreachable from index 0? `dp[0]` stays
  `-1`, correctly signaling no valid path exists.

**Dry run**

`nums = [2, 3, 1, 1, 4]`, `n=5`. `dp[4]=0`. `dp[3]`: jump to index 4
only -> `dp[4]+1=1`. `dp[2]`: jump to index 3 only -> `dp[3]+1=2`.
`dp[1]`: can reach indices 2, 3, 4 -> best is `dp[4]+1=1`. `dp[0]`: can
reach indices 1, 2 -> best is `dp[1]+1=2`. Result: `dp[0] = 2`.

**Common mistakes**

- Filling forward instead of backward - `dp[i]` would depend on
  not-yet-computed future values.
- Forgetting to skip `dp[j] == -1` entries when taking the min -
  would corrupt the calculation with a bogus "reachable in -1+1 = 0
  extra jumps."
- Off-by-one on the jump window: it's `i+1` through `i+nums[i]`
  inclusive, capped at `n-1` so it never reads past the array.

## 4. Topic-Level Interview Questions

**What makes MCM's state different from every earlier DP in this
series?**
It's a range `(i, j)`, not a single index or a pair of prefix lengths -
the recurrence searches over every split point *inside* that range.

**Why must MCM's tabulation fill by increasing range length?**
Because `dp[i][j]` depends on every smaller range strictly inside it -
filling row by row or column by column could read a cell that hasn't
been computed yet.

**How does Minimum Partitioning reuse 0-1 Knapsack?**
Value and weight both equal the element itself, capacity is half the
total sum, and the DP finds the best achievable sum not exceeding that
capacity - the answer is `totalSum - 2 * bestAchievableSum`.

**Why does Minimum Array Jumps fill backward instead of forward?**
Each index's answer depends on the answers of indices ahead of it, so
those must already be known - only guaranteed by starting at the last
index and working back to the first.

**What do MCM and Catalan's Number (48. DP-5) have in common, and what
differs?**
Both try every split point over a range and combine two independent
sub-results. MCM minimizes an explicit multiplication cost per split;
Catalan sums a count of arrangements per split.

## 5. Quick Revision Sheet

### Templates

```text
Interval DP (MCM) - state is a range, split at every point:
  dp[i][j] = min over k=i..j-1 of dp[i][k] + dp[k+1][j] + arr[i-1]*arr[k]*arr[j]
  (tabulation must fill by increasing range length)

Knapsack-shaped max-value (Minimum Partitioning):
  dp[i][j] = max(arr[i-1] + dp[i-1][j-arr[i-1]], dp[i-1][j])
  answer = totalSum - 2 * dp[n][W],  W = totalSum / 2

Backward reachability (Minimum Array Jumps):
  dp[n-1] = 0
  dp[i] = min(dp[j] + 1) over every reachable j with a known dp[j]
```

### Time & space complexity

| Problem | Approach | Time | Space |
|---|---|---:|---:|
| Matrix Chain Multiplication | Recursion | exponential | O(n) |
| Matrix Chain Multiplication | Memoization | O(n³) | O(n²) |
| Matrix Chain Multiplication | Tabulation | O(n³) | O(n²) |
| Minimum Partitioning | Tabulation | O(n·sum) | O(n·sum) |
| Minimum Array Jumps | Tabulation | O(n²) | O(n) |

### One-line reminders

- MCM's state is a range `(i, j)` - a genuinely new shape, not a
  single index or a pair of prefix lengths.
- MCM tabulation fills by increasing range length, never by plain
  row/column order.
- Minimum Partitioning = 0-1 Knapsack with value = weight = the
  element, capacity = half the total, answer = `total - 2*bestSum`.
- Minimum Array Jumps fills backward from the end - each index looks
  forward over a window sized by its own value, not a fixed window.
- Interval DP (MCM) and Catalan's Number (DP-5) both try every split
  point - MCM minimizes a cost, Catalan counts arrangements.
