# 46. DP-3

## 1. Core Idea

This folder covers three problems that split into two different
families:

- **Coin Change** and **Rod Cutting** are both still Knapsack-shaped
  (capacity + choice per item), continuing directly from 45. DP-2.
- **Longest Common Subsequence (LCS)** is a genuinely new shape: instead
  of items and a capacity, the state is "how much of two strings is
  left to compare."

### Coin Change is Unbounded Knapsack, but counting

45. DP-2's Unbounded Knapsack asked for the *maximum value*. Coin
Change asks a different question over the same shape: *how many
combinations* of coins (reused freely) add up to exactly a target sum?
The only change from the Unbounded Knapsack tabulation is the combine
step - addition instead of `max`:

```text
Unbounded Knapsack (maximize):  dp[i][j] = max(val[i-1] + dp[i][j-wt[i-1]], dp[i-1][j])
Coin Change (count ways):       dp[i][j] =      dp[i][j-coins[i-1]]  +  dp[i-1][j]
                                            (include count)      (exclude count)
```

Base cases flip too: `dp[i][0] = 1` (one way to make 0 - use nothing),
`dp[0][j] = 0` for `j > 0` (no coins, can't make a positive sum).

### Rod Cutting is Unbounded Knapsack wearing a different name

Rod Cutting asks: given prices for pieces of length `1..n`, cut a rod
of length `rodLength` to maximize total sale price. Map it onto the
Knapsack vocabulary and it's identical to Unbounded Knapsack:

```text
"weight" wt[i-1]   -> piece length i        (pieces are always length 1, 2, 3, ...)
"value"  val[i-1]  -> prices[i-1]
"capacity" W       -> rodLength
```

Same recognition skill as Target Sum Subset in DP-2: once you can see a
new-sounding problem is really an old pattern with relabeled inputs,
you don't need to re-derive the DP from scratch.

### LCS: a new pattern - comparing two sequences

Longest Common Subsequence finds the longest sequence of characters
that appears, in order, in both strings (not necessarily contiguous).
The state here is a pair of prefix lengths `(n, m)` - how much of
`str1` and `str2` remain to be compared - not an item index and a
capacity.

```text
lcs(str1, str2, n, m):
  if n == 0 or m == 0: return 0                    // one string exhausted
  if str1[n-1] == str2[m-1]:                         // last chars match
    return lcs(str1, str2, n-1, m-1) + 1              // both shrink together
  else:                                                // last chars differ
    return max(lcs(str1, str2, n-1, m), lcs(str1, str2, n, m-1))
```

When the characters match, there's no choice - that character must be
part of the LCS, so both pointers move together. When they don't
match, the LCS can't use both characters, so try dropping one side at
a time and keep the better result. That "match = no choice, forced
move" vs. "mismatch = choice, branch" distinction is the key thing to
recognize.

## 2. How to Recognize This Pattern

**Knapsack-family (Coin Change, Rod Cutting):**

- items with a per-unit value and a capacity constraint - even if the
  problem doesn't literally say "items" (coin denominations, piece
  lengths);
- "unlimited supply" language (coins can repeat, cut as many pieces of
  a length as you want) points to the unbounded variant specifically;
- the question being "how many ways" instead of "what's the max"
  changes the combine step from `max` to `+`, but the table shape and
  loop structure stay identical.

**LCS-family (comparing two sequences):**

- two strings or sequences are being compared;
- the answer depends on aligning positions in both sequences at once,
  so the state is a *pair* of indices/lengths, one per sequence;
- there's a clean "if they match, no choice; if they don't, branch"
  structure at each step.

## 3. Problems in This Folder

### Coin Change (count the number of ways)

**What the question asks**

Given `coins = [2, 5, 3, 6]` (unlimited supply of each) and `sum = 10`,
count how many distinct combinations of coins add up to exactly `10`
(order doesn't matter - using a 2 then a 3 is the same combination as a
3 then a 2).

**Optimized approach (tabulation only)**

- `dp[i][j]` = number of ways to make sum `j` using the first `i` coins.
- `dp[i][0] = 1` for every `i` (one way to make 0: use nothing).
  `dp[0][j] = 0` for `j > 0` (no coins, can't make a positive sum).
- For `i = 1..n`, `j = 1..sum`: if `coins[i-1] <= j`,
  `dp[i][j] = dp[i][j-coins[i-1]] + dp[i-1][j]` (include, staying on row
  `i` since a coin can repeat, plus exclude); otherwise
  `dp[i][j] = dp[i-1][j]`.
- Time: O(n · sum). Space: O(n · sum) for the dp table.

**Why it works**

Every way to make sum `j` using the first `i` coins either uses coin
`i` at least once (in which case, remove one copy of it and the rest is
some way to make `j - coins[i-1]` - still allowed to use coin `i` again,
hence row `i`), or doesn't use coin `i` at all (some way to make `j`
using only the first `i-1` coins). These two cases are disjoint and
cover every combination, so adding their counts gives the total.

**Interview explanation**

"This is Unbounded Knapsack's table, but I'm counting combinations
instead of maximizing value, so the combine step is addition instead of
max. dp[i][j] is the number of ways to make sum j with the first i
coins: either I use coin i again (dp[i][j-coins[i-1]], same row since
coins repeat) or I don't touch coin i at all (dp[i-1][j]). Adding those
two counts gives every combination exactly once, with no double
counting, because fixing the coin processing order (i from 1 to n)
means each combination is only ever built in one order."

**Common follow-up questions**

- Why does `dp[i][0] = 1`? The empty selection is always exactly one
  valid way to make a sum of 0.
- Why addition instead of max here, when Rod Cutting (right below) uses
  max? Coin Change counts *how many* combinations exist; Rod Cutting
  finds the *best* value. Same table shape, different question.
- Does coin order in the input array matter? No - each combination is
  counted once regardless of array order, since the DP processes coins
  one at a time and never revisits an earlier coin after moving on.

**Dry run**

For `coins = [2, 5, 3, 6], sum = 10`, the 5 combinations are:
`{2,2,2,2,2}`, `{2,2,3,3}`, `{2,3,5}`, `{2,2,6}`, `{5,5}` - matching
`dp[4][10] = 5`.

**Common mistakes**

- Using `dp[i-1][j-coins[i-1]]` instead of `dp[i][j-coins[i-1]]` in the
  include case - that would forbid reusing a coin, turning this into a
  (wrong) 0-1 version that undercounts.
- Using `max` instead of `+` - that answers "what's the best," not "how
  many ways."
- Forgetting `dp[i][0] = 1`, which breaks every other value in that row.

### Rod Cutting

**What the question asks**

Given `prices = [1, 5, 8, 9, 10, 17, 17, 20]` (the price of pieces of
length `1` through `8`) and a rod of `rodLength = 8`, find the maximum
total value obtainable by cutting the rod into pieces (including not
cutting it at all) and selling each piece.

**Optimized approach (tabulation only)**

- Exactly the Unbounded Knapsack tabulation from 45. DP-2, with
  `wt[i-1]` replaced by the piece length `i` itself (piece lengths are
  always `1, 2, 3, ..., n`) and `val[i-1] = prices[i-1]`.
- For `i = 1..n`, `j = 1..rodLength`: if piece length `i` fits in
  remaining length `j`,
  `dp[i][j] = max(prices[i-1] + dp[i][j-i], dp[i-1][j])` (same row: a
  piece of this length can be cut again); otherwise
  `dp[i][j] = dp[i-1][j]`.
- Time: O(n · rodLength). Space: O(n · rodLength) for the dp table.

**Why it works**

Identical reasoning to Unbounded Knapsack: for a rod of length `j`,
either the best cutting uses at least one more piece of length `i`
(take its price, recurse on the remaining `j - i` length, still allowed
to cut another piece of length `i`), or it doesn't use length `i` at
all (fall back to the best answer using only shorter piece lengths).

**Interview explanation**

"I recognized this as Unbounded Knapsack in disguise: the rod length is
the capacity, each possible piece length is an 'item' whose weight
equals its own length and whose value is its price, and since I can cut
as many pieces of the same length as I want, it's the unbounded
variant - same one-line change from 0-1 Knapsack as before, staying on
row i in the include case."

**Common follow-up questions**

- Why is a piece's "weight" just its length, not looked up from a
  separate array? Because piece lengths are always `1, 2, 3, ..., n` in
  order, so index `i-1` and length `i` are the same information.
- What does `dp[i][j]` mean here concretely? The best value obtainable
  from a rod of length `j`, using only cut lengths up to `i`.
- Is "no cuts at all" considered? Yes - it's included automatically as
  the case where the best answer for capacity `rodLength` comes from a
  single piece of length `rodLength` itself (price `prices[rodLength-1]`).

**Dry run**

For `rodLength = 8`: the optimal cut is one piece of length `2`
(price `5`) and one piece of length `6` (price `17`), total `22` -
better than selling the rod whole (`prices[7] = 20`).

**Common mistakes**

- Re-deriving this from scratch instead of recognizing it as Unbounded
  Knapsack - wastes time under interview pressure.
- Using `dp[i-1][j-i]` (0-1 style) instead of `dp[i][j-i]` - forbids
  cutting more than one piece of the same length.
- Forgetting that "sell the rod whole" is just the `i = rodLength` case,
  not something that needs special-casing.

### Longest Common Subsequence (LCS)

**What the question asks**

Given two strings, find the length of their longest common
subsequence - the longest sequence of characters that appears, in
order, in both strings, without requiring the characters to be
adjacent. Example: `str1 = "abcdge"`, `str2 = "abedg"` ->
LCS = `"abdg"`, length `4`.

**Brute-force approach (recursion)**

- Compare the last characters of the two remaining prefixes
  (lengths `n` and `m`).
- If they match: that character is part of the LCS - add 1 and recurse
  with both prefixes shortened by one.
- If they don't match: the LCS can't use both, so try dropping the last
  character of `str1` or of `str2`, and keep the larger result.
- Base case: either prefix length is `0` -> `0`.
- Time: O(2^(n+m)) - every mismatch branches into two recursive calls.
- Space: O(n+m) recursion stack.

**Optimized approach**

**Memoization**

- Same recurrence, cached in a 2D `memo[n+1][m+1]` array (sentinel
  `-1`). Every recursive call must go through the memoized method
  itself so the cache is actually used.
- Time: O(n · m) - each `(n, m)` pair computed once.
- Space: O(n · m) memo table + O(n+m) recursion stack.

**Tabulation**

- `dp[i][j]` = LCS length between `str1`'s first `i` characters and
  `str2`'s first `j` characters. `dp[i][0]` and `dp[0][j]` are `0` by
  default.
- For `i = 1..n`, `j = 1..m`: if `str1[i-1] == str2[j-1]`,
  `dp[i][j] = dp[i-1][j-1] + 1`; otherwise
  `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`.
- Time: O(n · m). Space: O(n · m) table, no recursion stack.

**Why it works**

If the last characters match, they must both be the final character of
some longest common subsequence (using a match here can never make the
answer worse), so the rest of the LCS is exactly the LCS of both
strings with that character removed, plus 1. If they don't match, the
final character of the true LCS came from dropping *one* of the two
strings' last characters - trying both and keeping the max is
guaranteed to find the real answer, since one of those two branches
must be consistent with whatever the optimal alignment actually is.

**Interview explanation**

"I compare the last characters of both remaining prefixes. A match is
forced - it has to be part of the LCS, so I take it and shrink both
strings by one. A mismatch means I don't know yet which string's last
character to drop, so I try both and take the max. Naive recursion
re-explores the same (n, m) prefix pairs many times, so I cache by
(n, m) - memoization keeps the recursive shape, tabulation replaces it
with a bottom-up grid fill - bringing it from exponential to O(n·m)."

**Common follow-up questions**

- Why is a matching character never wrong to include? Any common
  subsequence not using that matched pair could have it appended (or
  the pair could replace a later, no-better match), so including it is
  never worse than skipping it.
- Why try both drop-str1 and drop-str2 on a mismatch, instead of a
  fixed rule? Because either string's last character might be the one
  that isn't part of the true LCS - there's no way to know without
  computing both.
- How would you recover the actual LCS string, not just its length?
  Walk back from `dp[n][m]`: on a match move diagonally and record the
  character; on a mismatch move toward whichever neighbor (`dp[i-1][j]`
  or `dp[i][j-1]`) holds the larger value.

**Dry run**

For `str1 = "abcde"`, `str2 = "ace"` (the tabulation walkthrough
example):

```text
        ""  a   c   e
    ""   0  0   0   0
    a    0  1   1   1
    b    0  1   1   1
    c    0  1   2   2
    d    0  1   2   2
    e    0  1   2   3
```

`dp[5][3] = 3`, matching LCS `"ace"`.

**Common mistakes**

- Comparing `str1.charAt(n-1)` against `str2.charAt(m-1)` using `n` and
  `m` as 0-indexed positions instead of remembering they're lengths (an
  off-by-one that reads past the string or compares the wrong
  characters).
- On a mismatch, only trying one of the two drop options instead of
  both and taking the max.
- In the memoized version, recursing into the plain (uncached) method
  by mistake instead of the memoized one - silently throws away the
  whole point of memoization even though the answer is still correct.

## 4. Topic-Level Interview Questions

**How is Coin Change related to Unbounded Knapsack?**
Identical table shape and loop structure; the only change is the
combine step - addition (count combinations) instead of max (best
value).

**How is Rod Cutting related to Unbounded Knapsack?**
It *is* Unbounded Knapsack, with piece length playing the role of
weight and price playing the role of value.

**What makes LCS a different pattern from the Knapsack family?**
Its state is a pair of prefix lengths from two different sequences,
not an item index paired with a capacity. The recursion moves through
both sequences at once rather than deciding in/out for a list of
items.

**Why is naive LCS recursion O(2^(n+m))?**
Every mismatch branches the call tree in two, and the maximum
recursion depth before hitting a base case is bounded by n + m.

**What's the one-line rule for the LCS recurrence?**
Characters match -> take it, move both pointers. Characters differ ->
try dropping either one, keep the max.

## 5. Quick Revision Sheet

### Templates

```text
Unbounded-Knapsack-shaped counting (Coin Change):
  dp[i][j] = dp[i][j - coins[i-1]] + dp[i-1][j]     (include, same row + exclude)

Unbounded-Knapsack-shaped maximizing (Rod Cutting):
  dp[i][j] = max(prices[i-1] + dp[i][j - i], dp[i-1][j])

LCS (two-sequence prefix DP):
  match:    dp[i][j] = dp[i-1][j-1] + 1
  mismatch: dp[i][j] = max(dp[i-1][j], dp[i][j-1])
```

### Time & space complexity

| Problem | Approach | Time | Space |
|---|---|---:|---:|
| Coin Change (count ways) | Tabulation | O(n·sum) | O(n·sum) |
| Rod Cutting | Tabulation | O(n·rodLength) | O(n·rodLength) |
| LCS | Recursion | O(2^(n+m)) | O(n+m) |
| LCS | Memoization | O(n·m) | O(n·m) |
| LCS | Tabulation | O(n·m) | O(n·m) |

### One-line reminders

- Coin Change = Unbounded Knapsack with `+` instead of `max`.
- Rod Cutting = Unbounded Knapsack with piece length standing in for
  weight.
- LCS's state is (position in str1, position in str2) - a different
  shape from every Knapsack-family problem so far.
- LCS match -> forced move, no choice. LCS mismatch -> branch and take
  the max.
- Always double-check memoized recursive calls actually call the
  memoized method, not the plain one.
