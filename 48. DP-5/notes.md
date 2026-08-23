# 48. DP-5

## 1. Core Idea

This folder covers two unrelated ideas: a new 2D string-matching
pattern (Wildcard Matching), and Catalan's Number - a "split at every
point" recurrence that turns out to secretly be the answer to two very
different-sounding problems (Counting Trees, Mountain Ranges).

### Wildcard Matching: two-string DP with a special '*' rule

Same `dp[i][j]` prefix-matching shape as LCS / Edit Distance, but `*`
in the pattern isn't a single character - it's a choice with two
outcomes:

```text
match / '?':  dp[i][j] = dp[i-1][j-1]              // consume one of each
'*':          dp[i][j] = dp[i][j-1] || dp[i-1][j]  // match nothing new, or absorb one more text char
literal miss: dp[i][j] = false
```

`dp[i][j-1]` means "the star matches an empty sequence here - check if
the rest of the pattern already matched." `dp[i-1][j]` means "the star
keeps growing to swallow one more character of text, and is still
`*` for the next comparison."

### Catalan's Number: split the problem at every possible point

```text
C0 = 1, C1 = 1
Cn = sum over i = 0..n-1 of  Ci * C(n-1-i)
```

This is a different DP shape from anything earlier in this DP series:
instead of a choice per item (Knapsack family) or a comparison between
two sequences (LCS family), the state is a single size `n`, and the
recurrence considers **every way to split that size into two smaller
parts** (`i` and `n-1-i`), multiplying the ways to fill each part and
summing over every split point.

### The same recurrence, two different stories

Both of these problems reduce to the exact Catalan recurrence, with no
new code - only a new interpretation of what "split into two smaller
parts" means:

- **Counting Trees (Unique BSTs):** for `n` keys, any key can be the
  root; choosing the `k`th smallest as root forces the smaller `k-1`
  keys into the left subtree and the larger `n-k` keys into the right
  subtree. The count only depends on *how many* keys are on each side,
  not their values - so summing `(ways to arrange the left) *
  (ways to arrange the right)` over every possible root is exactly
  `dp[i] = sum(dp[j] * dp[i-1-j])`.
- **Mountain Ranges:** for `n` pairs of up/down strokes, the first
  up-stroke's matching down-stroke splits the range into an "inside"
  mountain range (nested underneath that pair) and an "outside"
  mountain range (everything after it closes) - the same split-and-
  multiply structure again.

## 2. How to Recognize This Pattern

**Wildcard Matching family:**

- two strings being compared where one has special "wildcard"
  characters with their own matching rules;
- a wildcard that can match a *variable-length* span (like `*`) means
  its DP transition needs to consider more than one possibility, not
  just a single previous cell.

**Catalan-shaped family:**

- the question asks to *count structurally distinct arrangements* of
  something recursive (trees, balanced sequences, ways to parenthesize,
  ways to triangulate, ways to pair things up);
- there's a natural "pick a splitting point" framing - a root, a
  matching bracket, a diagonal - that divides the problem into two
  independent smaller versions of the same problem;
- the recurrence has the shape `f(n) = sum over splits of f(left) * f(right)`.
  Recognizing this shape means not having to re-derive the DP: reuse
  the Catalan tabulation directly.

## 3. Problems in This Folder

### Wildcard Matching

**What the question asks**

Given a text and a wildcard pattern (`?` = any single character, `*` =
any sequence of characters, including empty), determine whether the
pattern matches the *entire* text.

**Optimized approach (tabulation only)**

- `dp[i][j]` = does the pattern's first `j` characters match the
  text's first `i` characters.
- Base cases: `dp[0][0] = true`. `dp[i][0] = false` for `i >= 1` (a
  non-empty text can't match nothing). `dp[0][j]` is only ever true
  while every pattern character seen so far is `*` (a leading run of
  stars can all match the empty text).
- For `i = 1..n`, `j = 1..m`:
  - if `s[i-1] == p[j-1]` or `p[j-1] == '?'`:
    `dp[i][j] = dp[i-1][j-1]`.
  - else if `p[j-1] == '*'`:
    `dp[i][j] = dp[i][j-1] || dp[i-1][j]`.
  - else: `dp[i][j] = false`.
- Time: O(n · m). Space: O(n · m) for the dp table.

**Why it works**

A `*` doesn't commit to matching a fixed number of characters, so its
cell has to check both ways it could be used: matching zero more
characters (fall back to `dp[i][j-1]`, as if the star weren't there
yet) or matching one more character of text while still being
available to match further ones (`dp[i-1][j]`, staying on the same
pattern position). Trying both and OR-ing them covers every possible
length the star could expand to, without enumerating each length
explicitly.

**Interview explanation**

"This is a 2D prefix DP like Edit Distance, but `*` needs an OR of two
possibilities instead of a single transition: either it matches
nothing new - dp[i][j-1] - or it swallows one more text character and
stays a star for the next comparison - dp[i-1][j]. A literal character
or `?` just consumes one of each and inherits the diagonal, same as
LCS's match case. Any other literal mismatch is simply false."

**Common follow-up questions**

- Why does `dp[0][j]` need its own initialization loop instead of
  falling out of the base cases automatically? Because an empty text
  can still match a non-empty pattern, but only if that pattern is
  entirely made of `*` so far - that's a special case the general
  recurrence (which assumes `i >= 1`) doesn't cover.
- What's the difference between `?` and a literal character in this
  recurrence? None, structurally - both fall into the same
  `dp[i-1][j-1]` branch, since `?` just relaxes the equality check.
- Why is `dp[i][0] = false` for `i >= 1`, even if the pattern could
  theoretically be empty? Because an empty pattern (`j=0`) can only
  ever match an empty text - there's nothing in an empty pattern that
  could match non-empty text.

**Dry run**

`s = "aa"`, `p = "*"`: `dp[0][1] = true` (a lone `*` matches empty).
`dp[1][1] = dp[1][0] || dp[0][1] = false || true = true`.
`dp[2][1] = dp[2][0] || dp[1][1] = false || true = true`. Result:
`true` - a single `*` matches any text.

**Common mistakes**

- Forgetting the `dp[0][j]` leading-star initialization, which makes
  every pattern starting with `*` incorrectly fail against short or
  empty text prefixes.
- Only checking one of the two `*` branches (just `dp[i][j-1]` or just
  `dp[i-1][j]`) instead of OR-ing both.
- Treating `*` as if it must match at least one character - it must
  also be allowed to match zero.

### Catalan's Number

**What the question asks**

Compute the `n`th Catalan number: `C0 = 1`, `C1 = 1`, and
`Cn = sum(Ci * C(n-1-i))` for `i = 0..n-1`. Example: `C4 = 14`.

**Brute-force approach (recursion)**

- Direct translation of the recurrence: base case returns `1` for
  `n = 0` or `n = 1`; otherwise sum `catalan(i) * catalan(n-1-i)` over
  every split point `i`.
- Time: exponential - grows on the order of the Catalan numbers' own
  asymptotic growth rate (roughly `4^n / n^1.5`), since the same
  smaller `Ck` values are recomputed many times across different
  branches.
- Space: O(n) recursion stack.

**Optimized approach**

**Memoization**

- Same recurrence, cached in a `memo[]` array (sentinel `-1`).
- Time: O(n²) - computing `Ck` the first time costs O(k) work, and
  each `k` from `0` to `n` is computed exactly once, so total work is
  `0 + 1 + ... + n = O(n²)`.
- Space: O(n) memo array + O(n) recursion stack.

**Tabulation**

- `dp[0] = dp[1] = 1`. For `i = 2..n`, sum `dp[j] * dp[i-1-j]` over
  `j = 0..i-1` into `dp[i]`.
- Time: O(n²). Space: O(n) for the dp array, no recursion stack.

**Why it works**

Every valid structure counted by `Cn` can be broken at exactly one
"first" split point into two independent smaller structures of sizes
`i` and `n-1-i`. Since the two parts don't interact, the number of ways
to build both together is the product of the ways to build each one
separately, and summing that product over every valid split point
counts every structure exactly once.

**Interview explanation**

"Catalan's Number sums over every way to split a problem of size n into
two independent smaller problems of sizes i and n-1-i, multiplying the
ways to solve each side. Recursion re-explores the same smaller Ck
values many times, so I cache them - memoization keeps the recursive
shape, tabulation fills a 1D array bottom-up - bringing the total work
down to O(n²)."

**Common follow-up questions**

- Why does the recursion overlap so heavily? Because computing `Cn`
  needs every smaller `Ck`, and computing `C(n-1)` needs almost all of
  those same smaller values again, and so on down the chain.
- Why is tabulation's inner loop bound by `i`, not a fixed `n`? Because
  computing `dp[i]` only ever needs splits within that size - `j` from
  `0` to `i-1` - never anything larger.
- What's a closed-form alternative to this DP? Catalan numbers have a
  direct formula, `Cn = (2n choose n) / (n+1)`, but that requires
  factorials/combinatorics rather than this split-and-sum DP.

**Dry run**

`C2 = C0·C1 + C1·C0 = 1+1 = 2`. `C3 = C0·C2 + C1·C1 + C2·C0 = 2+1+2 = 5`.
`C4 = C0·C3 + C1·C2 + C2·C1 + C3·C0 = 5+2+2+5 = 14`.

**Common mistakes**

- Off-by-one in the split: the two parts must sum to `n-1` (i.e.
  `i + (n-1-i) = n-1`), not `n` - easy to get wrong since one "unit" of
  the split point itself is being accounted for implicitly (the root,
  the matched pair, etc. depending on which application this is used
  for).
- In the recursive/memoized versions, forgetting that *every* recursive
  call must go through the memoized method, or the cache never gets
  used.
- Confusing this with a simple Fibonacci-style DP (DP-1) because both
  are 1D - Catalan's inner loop over every split point is what makes it
  a different, more expensive shape (O(n²), not O(n)).

### Counting Trees (Unique Binary Search Trees)

**What the question asks**

Given `n` distinct keys (e.g. `10, 20, 30, 40`), count how many
structurally different binary search trees can be built from them.
Example: `n = 4` -> `14`.

**Approach**

No new DP - this *is* Catalan's Number. For `n` keys, choosing the
`k`th smallest as root forces the smaller `k-1` keys left and the
larger `n-k` keys right; the tree count for a set only depends on its
size, not the actual key values. Summing
`(left subtree count) * (right subtree count)` over every possible
root gives exactly the Catalan recurrence, so
`countUniqueBSTs(n)` simply calls `catalanTabulation(n)`.

- Time: O(n²). Space: O(n) for the dp array.

**Why it works**

Every unique BST is fully determined by which key is the root and how
its left/right key-sets are each arranged - and since left/right
arrangements are independent of each other and of the rest of the
tree, the total is a product summed over every root choice, which is
precisely the "split at every point" shape Catalan's Number already
solves.

**Interview explanation**

"I recognized this as Catalan's Number in disguise: picking the kth
smallest key as root always splits the remaining keys into a left group
of size k-1 and a right group of size n-k, and the number of distinct
trees only depends on group size, not which values are in it. So the
count for root choice k is dp[k-1] * dp[n-k], and summing over every
root gives the same recurrence as Catalan's Number - no new code
needed."

**Common follow-up questions**

- Why doesn't the actual value of each key matter, only the count?
  Because BST structure is determined purely by relative order, and any
  set of `k` distinct keys has the same number of possible BST shapes
  regardless of which specific values they are.
- How does this differ from counting *labeled* trees in general (not
  BSTs)? BSTs are constrained by the ordering property, which is
  exactly what pins down a unique valid arrangement for each root
  choice - unconstrained binary trees would need a different count.
- What's `n = 0` and why does it matter? An empty set of keys has
  exactly one "tree" (the empty tree), which is why `dp[0] = 1` - this
  is what makes a root's "missing" child subtree count correctly as 1
  way, not 0.

**Dry run**

`n = 4`, keys `{10, 20, 30, 40}`: root `10` -> `5` trees (right side has
3 keys, `C3 = 5`); root `20` -> `1 * 2 = 2`; root `30` -> `2 * 1 = 2`;
root `40` -> `5` trees (left side has 3 keys). Total:
`5 + 2 + 2 + 5 = 14`, matching `C4`.

**Common mistakes**

- Forgetting `dp[0] = 1` (the empty subtree still counts as exactly one
  valid arrangement, not zero).
- Assuming the actual key values matter to the count - only the
  quantity on each side of the root matters.

### Mountain Ranges

**What the question asks**

Given `n` pairs of up-strokes and down-strokes, count how many distinct
"mountain ranges" can be formed - sequences that never dip below the
starting level and return to it at the end. Example: `n = 4` -> `14`.

**Approach**

Also Catalan's Number, shown here with its own explicit tabulation:

- `dp[0] = dp[1] = 1`.
- For `i = 2..n`, sum over `j = 0..i-1`:
  `inside = dp[j]` (mountain ranges nested underneath the first pair's
  up-and-down-stroke), `outside = dp[i-j-1]` (mountain ranges after
  that first pair closes) - `dp[i] += inside * outside`.
- Time: O(n²). Space: O(n) for the dp array.

**Why it works**

The very first up-stroke has some matching down-stroke that returns to
the starting level - everything between them forms an independent
"inside" mountain range (nested one level up), and everything after the
down-stroke forms an independent "outside" mountain range. Since where
that first matching down-stroke falls can vary, summing over every
possible split point covers every valid arrangement exactly once - the
same split-and-multiply shape as Catalan's Number and Counting Trees.

**Interview explanation**

"The first up-stroke closes at some point, splitting the range into an
inside part (nested under that first stroke) and an outside part
(everything after it closes). Both parts are themselves valid smaller
mountain ranges, and they're independent of each other, so I sum
inside-count times outside-count over every possible closing point -
identical in shape to the BST root-choice sum and to Catalan's Number
directly."

**Common follow-up questions**

- How is this the same shape as Counting Trees? Both split a problem of
  size `n` (or `n` pairs) into two independent smaller versions of the
  same problem at every possible split point, and sum the products -
  only the concrete meaning of "split point" differs (root choice vs.
  first pair's closing point).
- What's a more familiar name for this problem? It's equivalent to
  counting balanced parenthesis sequences of length `2n` - each
  up-stroke is an open paren, each down-stroke is a close paren.
- Why `inside`/`outside` instead of `left`/`right` like the BST case?
  Purely naming - the underlying recurrence and reasoning are identical.

**Dry run**

`n = 4`: same numbers as Catalan's Number and Counting Trees -
`dp[4] = 14`.

**Common mistakes**

- Writing `dp[i] = inside * outside` (overwrite) instead of
  `dp[i] += inside * outside` (accumulate) - since every split point
  contributes a valid set of arrangements, they must all be summed, not
  just the last one computed.
- Not recognizing this as Catalan's Number and attempting to design a
  new DP from scratch.

## 4. Topic-Level Interview Questions

**What makes Wildcard Matching's `*` handling different from a normal
DP transition?**
A `*` can represent zero or more characters, so its cell must consider
two possibilities (match nothing new, or absorb one more character)
via an OR, rather than a single deterministic transition.

**What is the defining shape of a Catalan-family recurrence?**
`f(n) = sum over every split point of f(left part) * f(right part)` -
splitting a problem of size n into two independent smaller problems and
summing the product of their counts over every possible split.

**Why are Counting Trees and Mountain Ranges the same recurrence as
Catalan's Number?**
Both have a natural "first choice" (a root, a first closing stroke)
that divides the remaining problem into two independent smaller
sub-problems whose sizes always sum to one less than the current size -
exactly Catalan's split-and-multiply structure.

**Why is Catalan's Number O(n²) when tabulated, not O(n) like
Fibonacci-shaped DP?**
Because `dp[i]` depends on *every* earlier split `j = 0..i-1`, not just
the previous one or two values - the inner loop makes it quadratic.

**How would you recognize a brand-new problem as "Catalan-shaped"
during an interview?**
Look for counting distinct recursive structures (trees, bracket
sequences, polygon triangulations, ways to pair things) where there's
an identifiable "first" element that splits everything else into two
independent smaller versions of the same problem.

## 5. Quick Revision Sheet

### Templates

```text
Wildcard Matching:
  match / '?':  dp[i][j] = dp[i-1][j-1]
  '*':          dp[i][j] = dp[i][j-1] || dp[i-1][j]
  literal miss: dp[i][j] = false

Catalan-shaped (Catalan's Number / Counting Trees / Mountain Ranges):
  dp[0] = dp[1] = 1
  dp[i] = sum over j = 0..i-1 of dp[j] * dp[i-1-j]
```

### Time & space complexity

| Problem | Approach | Time | Space |
|---|---|---:|---:|
| Wildcard Matching | Tabulation | O(n·m) | O(n·m) |
| Catalan's Number | Recursion | exponential | O(n) |
| Catalan's Number | Memoization | O(n²) | O(n) |
| Catalan's Number | Tabulation | O(n²) | O(n) |
| Counting Trees (Unique BSTs) | Tabulation (= Catalan) | O(n²) | O(n) |
| Mountain Ranges | Tabulation | O(n²) | O(n) |

### One-line reminders

- `*` in Wildcard Matching needs an OR of two transitions, not one -
  "match nothing new" or "absorb one more character."
- Catalan's shape: split size n into two parts summing to n-1, sum the
  products over every split point.
- Counting Trees = Catalan's Number with "root choice" as the split.
- Mountain Ranges = Catalan's Number with "first closing stroke" as the
  split.
- Catalan tabulation is O(n²), not O(n) - the inner loop over every
  split point is what makes it quadratic, unlike DP-1's Fibonacci
  pattern.
