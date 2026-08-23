# 47. DP-4

## 1. Core Idea

This folder continues the LCS family from 46. DP-3: every problem here
reuses the same two-sequence prefix DP shape, either directly or as a
building block inside a slightly different question.

### Longest Common Substring vs. Longest Common Subsequence

Same setup as LCS (two strings, a 2D dp table over prefixes), but with
one rule changed: a **substring** must be contiguous, while a
**subsequence** just needs to preserve order. That single difference
changes what happens on a mismatch:

```text
LCS (subsequence), on mismatch:
  dp[i][j] = max(dp[i-1][j], dp[i][j-1])     // fall back, keep the best run going

Longest Common Substring, on mismatch:
  dp[i][j] = 0                                // the run is broken, start over
```

Because the best substring can end at any cell (not necessarily the
last one), the answer is a running `ans = max(ans, dp[i][j])` taken
during the fill, not just whatever ends up in `dp[n][m]`.

### Longest Increasing Subsequence: solved by reducing to LCS

LIS asks for the longest strictly increasing run of elements from one
array (not necessarily contiguous). The reduction: any increasing
subsequence of `arr` is, by definition, a subsequence that also reads
in sorted order - so it must be a common subsequence between `arr` and
a **sorted, duplicate-free** copy of `arr`. That makes:

```text
LIS(arr) = LCS(arr, sortedUnique(arr))
```

Deduping the sorted copy matters: without it, a repeated value in
`arr` could match itself against its own duplicate in the sorted copy,
producing a "common subsequence" that isn't a genuine increasing run.

### Edit Distance: same shape, minimizing cost instead of maximizing length

Edit Distance keeps the exact two-string prefix state as LCS, but each
cell now holds a *cost to minimize* (operations needed) instead of a
*length to maximize*. On a match, no operation is needed, so the cost
carries over unchanged from the diagonal. On a mismatch, there are
three possible operations, and the recurrence takes whichever costs
least:

```text
match:    dp[i][j] = dp[i-1][j-1]                                   // free, no operation
mismatch: dp[i][j] = 1 + min(dp[i][j-1],   // insert
                              dp[i-1][j],   // delete
                              dp[i-1][j-1]) // replace
```

### String Conversion: Edit Distance restricted to insert/delete, solved via LCS

If replace isn't allowed, the only way to turn `str1` into `str2` is to
delete everything in `str1` that isn't shared with `str2`, and insert
everything in `str2` that isn't shared with `str1` - and "shared, in
order" is exactly the definition of their LCS:

```text
deletions  = length(str1) - LCS(str1, str2)
insertions = length(str2) - LCS(str1, str2)
```

No new DP is needed here - it's ordinary LCS plus two subtractions.

## 2. How to Recognize This Pattern

- Two sequences (strings or arrays) being compared -> two-string prefix
  DP, the LCS/Edit-Distance family.
- "Contiguous" or "substring" in the problem wording -> a mismatch must
  reset to 0, not fall back to a neighbor (Longest Common Substring's
  rule, not LCS's).
- A single-array "longest increasing/ordered run" question -> consider
  reducing to LCS against a sorted, duplicate-free copy of the same
  array.
- "Minimum operations to transform one string into another" -> Edit
  Distance shape; check which operations are allowed (insert/delete
  only vs. insert/delete/replace) before picking the recurrence.
- "Minimum insertions/deletions only" (no replace) -> don't write a new
  DP at all; compute the ordinary LCS and subtract.

## 3. Problems in This Folder

### Longest Common Substring

**What the question asks**

Given two strings, find the length of the longest substring (a
contiguous run of characters) common to both. Example:
`str1 = "abcdge"`, `str2 = "abedg"` -> the longest common substring is
`"ab"` or `"dg"` (both length `2`) - notice this is a *different*
answer from these same two strings' LCS (`"abdg"`, length `4`, from
46. DP-3), because `"abdg"` isn't contiguous in either string.

**Optimized approach (tabulation only)**

- `dp[i][j]` = length of a common substring that ends exactly at
  `str1[i-1]` and `str2[j-1]`.
- For `i = 1..n`, `j = 1..m`:
  - match: `dp[i][j] = dp[i-1][j-1] + 1`, and update
    `ans = max(ans, dp[i][j])`.
  - mismatch: `dp[i][j] = 0` (no partial credit - the run is broken).
- Return the running `ans`, not `dp[n][m]` (the best substring can end
  anywhere in the table).
- Time: O(n · m). Space: O(n · m) for the dp table.

**Why it works**

A common substring ending at position `(i, j)` can only be built by
extending a common substring that ended at `(i-1, j-1)` with one more
matching character. If the characters don't match, there is no common
substring ending here at all - not a shorter one, none - so the count
must reset to `0` rather than inherit anything from a neighboring cell.

**Interview explanation**

"This is LCS's table with one change: on a mismatch, I can't fall back
to a neighbor's value, because that would break contiguity. I reset to
zero instead, and since the best run might end in the middle of the
table rather than at the last cell, I track a running max as I fill it,
rather than reading dp[n][m] at the end."

**Common follow-up questions**

- Why track a separate `ans` variable instead of just returning
  `dp[n][m]`? Because the longest common substring might not include
  the very last characters of either string - it could end anywhere.
- What breaks if `Math.max(dp[i-1][j], dp[i][j-1])` (the LCS mismatch
  rule) is used here by mistake? It would silently compute LCS instead
  of Longest Common Substring, since it allows non-contiguous runs.
- Can this run in O(n) extra space? Yes - since row `i` only depends on
  row `i-1`, two 1D arrays can replace the full table.

**Dry run**

For `str1 = "abcdge"`, `str2 = "abedg"`: `dp[2][2] = 2` for `"ab"`
(positions 1-2 in both), and separately `dp[4][5] = 2` for `"dg"`
(positions 4-5 in `str1`, 4-5 in `str2`). Both hit `2`; nothing reaches
`3`, so `ans = 2`.

**Common mistakes**

- Reusing LCS's `max(dp[i-1][j], dp[i][j-1])` fallback on a mismatch -
  the single most common bug when adapting LCS code for this problem.
- Returning `dp[n][m]` instead of the running max.
- Forgetting that the answer can be `0` (no common substring at all,
  when the two strings share no characters).

### Longest Increasing Subsequence

**What the question asks**

Given `arr = [50, 3, 10, 7, 40, 80]`, find the length of the longest
strictly increasing subsequence. Answer: `4` (for example
`3, 7, 40, 80` - indices `1, 3, 4, 5` in the original array, values
increasing).

**Optimized approach (LCS reduction, tabulation)**

- Build a sorted, duplicate-free copy of `arr`.
- Run the ordinary array-based LCS tabulation (identical shape to
  string LCS, just comparing `arr1[i-1] == arr2[j-1]` instead of
  characters) between the original array and the sorted-unique copy.
- The result is the LIS length.
- Time: O(n log n) to sort + O(n²) for the LCS call (the sorted-unique
  array has at most `n` elements) = **O(n²)** overall.
- Space: O(n²) for the dp table inside the LCS call.

**Why it works**

Any increasing subsequence of `arr`, read left to right, is also
non-decreasing when read in numeric order - meaning it's a valid
subsequence of the sorted version of `arr` too. So the longest
increasing subsequence of `arr` is exactly the longest sequence of
values that can be found, in order, in *both* the original array and
its sorted form - which is the definition of their LCS. Deduplication
is required so a repeated value can't be matched against its own
duplicate and inflate the count with a run that isn't truly increasing.

**Interview explanation**

"Instead of writing a new DP, I reduce this to LCS: I sort a
duplicate-free copy of the array, and compute the LCS between the
original array and that sorted copy. Any subsequence common to both
must appear in increasing order in the original, since it also appears
in the fully-sorted copy - which is exactly the definition of an
increasing subsequence. Removing duplicates from the sorted copy is
essential, otherwise a repeated value could match itself and produce a
wrong, inflated answer."

**Common follow-up questions**

- Why remove duplicates from the sorted copy specifically, not from the
  original array? The original array's duplicates are legitimate input
  data; only the *reference* sorted copy needs deduplication, since
  it's just being used as a comparison target.
- What's the time complexity, and why isn't it O(n·m) like ordinary
  LCS? Because here `m` (the sorted-unique array's length) is at most
  `n`, so O(n·m) collapses to O(n²).
- Is there a faster, non-LCS way to solve LIS? Yes - a patience-sorting
  approach with binary search solves it in O(n log n), but that's a
  different technique, not covered by this reduction.

**Dry run**

`arr = [50, 3, 10, 7, 40, 80]`, sorted-unique = `[3, 7, 10, 40, 50, 80]`.
Their LCS is `3, 7, 40, 80` (length `4`) - matching indices `1, 3, 4, 5`
in the original array, confirming it's a genuine increasing run.

**Common mistakes**

- Forgetting to deduplicate the sorted copy before computing the LCS.
- Assuming this reduction is the most efficient LIS approach - it's a
  clean way to reuse LCS, not the fastest possible solution.
- Comparing `arr1[i-1] == arr2[j-1]` by reference instead of value for
  non-primitive types (not an issue for `int[]`, but worth knowing if
  adapted to boxed types).

### Edit Distance

**What the question asks**

Given `word1 = "intention"`, `word2 = "execution"`, and three allowed
operations (insert a character, delete a character, replace a
character), find the minimum number of operations to convert `word1`
into `word2`. Answer: `5`.

**Optimized approach (tabulation only)**

- `dp[i][j]` = minimum operations to convert `word1`'s first `i`
  characters into `word2`'s first `j` characters.
- Base cases: `dp[0][j] = j` (build `word2`'s first `j` characters
  purely by inserting), `dp[i][0] = i` (delete all of `word1`'s first
  `i` characters).
- For `i = 1..n`, `j = 1..m`:
  - match: `dp[i][j] = dp[i-1][j-1]` (already equal here, free).
  - mismatch: `dp[i][j] = 1 + min(dp[i][j-1], dp[i-1][j], dp[i-1][j-1])`
    (insert, delete, replace - respectively).
- Time: O(n · m). Space: O(n · m) for the dp table.

**Why it works**

On a match, the best conversion of the two prefixes is exactly the best
conversion of both prefixes with that matching character stripped off
- no operation is spent on characters that already agree. On a
mismatch, the last character has to be resolved somehow: insert
`word2`'s character (move only the `word2` pointer), delete `word1`'s
character (move only the `word1` pointer), or replace it (move both).
Each option costs one operation plus whatever the smaller subproblem
costs, so taking the minimum of the three finds the cheapest path.

**Interview explanation**

"dp[i][j] is the edit distance between the first i characters of word1
and the first j of word2. If the last characters already match, that
character is free - I inherit dp[i-1][j-1] directly. If they don't
match, I have three choices - insert, delete, or replace the mismatched
character - each costing 1 plus a smaller subproblem, and I take
whichever is cheapest. The base row and column handle converting
to/from an empty string, which just costs one operation per character."

**Common follow-up questions**

- Why does `dp[0][j] = j` and `dp[i][0] = i`? Converting an empty
  string to a `j`-length string takes exactly `j` insertions (and the
  reverse takes `i` deletions).
- Why is the match case free (`dp[i-1][j-1]`) instead of also adding 1?
  Because no operation is needed when the characters already agree -
  adding 1 would overcount.
- Which of the three operations was used at each step, not just the
  total count? Backtrack from `dp[n][m]`: on a match move diagonally
  with no cost; on a mismatch, move toward whichever of the three
  source cells produced the minimum, and record that operation.

**Dry run**

`"intention"` -> `"execution"` in 5 operations, matching the DP result:

```text
intention -> inention  (remove 't')
inention  -> enention  (replace 'i' with 'e')
enention  -> exention  (replace 'n' with 'x')
exention  -> exection  (replace 'n' with 'c')
exection  -> execution (insert 'u')
```

**Common mistakes**

- Adding 1 in the match case as well as the mismatch case (should only
  add 1 when an operation is actually needed).
- Mixing up which neighbor corresponds to insert vs. delete - `dp[i][j-1]`
  advances `word2` only (insert into `word1`), `dp[i-1][j]` advances
  `word1` only (delete from `word1`).
- Forgetting the base row/column initialization, which every other
  cell ultimately depends on.

### String Conversion (insert and delete only)

**What the question asks**

Given `str1 = "pear"`, `str2 = "sea"`, convert `str1` into `str2` using
only insertions and deletions (no replace), and report how many of
each are needed.

**Optimized approach (reuses ordinary LCS, no new DP)**

- Compute `lcs = LCS(str1, str2)` using the same string-LCS tabulation
  from 46. DP-3 (kept as a local helper in this file).
- `deletions = length(str1) - lcs` (every character of `str1` not part
  of the shared subsequence must go).
- `insertions = length(str2) - lcs` (every character of `str2` not part
  of the shared subsequence must be added).
- Time: O(n · m), dominated by the LCS call. Space: O(n · m) for its dp
  table.

**Why it works**

Whatever `str1` and `str2` share, in order, can be left untouched -
that shared subsequence is exactly their LCS. Every other character in
`str1` is "extra" and must be deleted; every other character in `str2`
is "missing" and must be inserted. Since replace isn't allowed, there's
no cheaper way to resolve a mismatched character than to delete it
(from `str1`'s side) and separately insert whatever `str2` actually
needs there.

**Interview explanation**

"With no replace operation, the cheapest plan is to keep only the
characters str1 and str2 already agree on, in order - their LCS - and
handle everything else with a single insert or delete each. So I don't
need a new DP at all: I compute the ordinary LCS, then deletions is
str1's length minus the LCS, and insertions is str2's length minus the
LCS."

**Common follow-up questions**

- How does this relate to full Edit Distance? It's the special case of
  Edit Distance where replace costs more than a delete+insert pair (or
  simply isn't allowed), so the LCS-based shortcut applies directly.
- Why is deletions vs. insertions each measured against the LCS
  separately, instead of just one combined number? Because they're
  answering separate questions: "how many characters do I remove" and
  "how many do I add," which the problem asks for individually.
- Could full Edit Distance's recurrence be reused here by just removing
  the replace option? Yes - `dp[i][j] = 1 + min(dp[i][j-1], dp[i-1][j])`
  on a mismatch would also work, but it's more code than necessary when
  the LCS-based formula gives the same answer directly.

**Dry run**

`str1 = "pear"` (length 4), `str2 = "sea"` (length 3). Their LCS is
`"ea"` (length 2). Deletions = `4 - 2 = 2` (remove `'p'` and `'r'`).
Insertions = `3 - 2 = 1` (add `'s'`). Total operations: `3`.

**Common mistakes**

- Writing a brand new DP for this instead of recognizing it reduces to
  plain LCS plus subtraction.
- Reporting only the total operation count when the problem asks for
  deletions and insertions separately.
- Forgetting that `deletions + insertions` here can differ from full
  Edit Distance's answer on the same strings, since replace (when
  allowed) can sometimes resolve a mismatch in one operation instead of
  two.

## 4. Topic-Level Interview Questions

**How does Longest Common Substring differ from LCS in code, exactly?**
One line: the mismatch case is `dp[i][j] = 0` instead of
`max(dp[i-1][j], dp[i][j-1])`, and the answer is a running max tracked
during the fill instead of `dp[n][m]`.

**Why does LIS reduce to LCS against a sorted, deduplicated copy?**
An increasing subsequence of `arr`, by definition, also appears in
sorted order - so it must be a subsequence shared by `arr` and its
sorted form. Deduplication prevents a value from being matched against
its own duplicate.

**What's the recurrence difference between LCS and Edit Distance?**
LCS's match case adds 1 (a new shared character extends the answer);
Edit Distance's match case adds 0 (no operation is needed when
characters already agree). Both mismatch cases combine three
neighboring cells, but LCS takes a max of two, Edit Distance takes a
min of three.

**How does String Conversion avoid writing a new DP?**
It reuses plain LCS and subtracts: deletions = `len(str1) - LCS`,
insertions = `len(str2) - LCS`.

**What do all four problems in this folder have in common?**
Every one is built on the same two-sequence prefix DP state
`(i, j)` first introduced for LCS in 46. DP-3 - only the base cases and
the combine step change from problem to problem.

## 5. Quick Revision Sheet

### Templates

```text
Longest Common Substring (mismatch resets to 0, track running max):
  match:    dp[i][j] = dp[i-1][j-1] + 1;  ans = max(ans, dp[i][j])
  mismatch: dp[i][j] = 0

LIS via LCS reduction:
  LIS(arr) = LCS(arr, sortedUnique(arr))

Edit Distance (minimize cost, three operations):
  match:    dp[i][j] = dp[i-1][j-1]
  mismatch: dp[i][j] = 1 + min(dp[i][j-1], dp[i-1][j], dp[i-1][j-1])

String Conversion (insert/delete only, via LCS):
  deletions  = length(str1) - LCS(str1, str2)
  insertions = length(str2) - LCS(str1, str2)
```

### Time & space complexity

| Problem | Approach | Time | Space |
|---|---|---:|---:|
| Longest Common Substring | Tabulation | O(n·m) | O(n·m) |
| Longest Increasing Subsequence | LCS reduction | O(n²) | O(n²) |
| Edit Distance | Tabulation | O(n·m) | O(n·m) |
| String Conversion | LCS reduction | O(n·m) | O(n·m) |

### One-line reminders

- Substring = contiguous, resets to 0 on mismatch. Subsequence = order
  only, falls back to a neighbor on mismatch. That's the entire
  difference between this folder's first problem and 46. DP-3's LCS.
- LIS is LCS against a sorted, deduplicated copy of the same array -
  no new recurrence needed.
- Edit Distance mismatch takes a **min** of three costs (insert,
  delete, replace); LCS mismatch takes a **max** of two lengths - don't
  mix them up.
- No-replace string conversion is just LCS plus subtraction, not a new
  DP.
- Every problem here shares one state shape: `(i, j)` = how far into
  each of two sequences you've compared.
