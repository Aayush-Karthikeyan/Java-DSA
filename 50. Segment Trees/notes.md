# 50. Segment Trees

## 1. Core Idea

A segment tree stores information about array ranges.

For example, a sum segment tree stores:

```text
Array: [1, 2, 3, 4]

                 10  [0,3]
               /            \
          3 [0,1]          7 [2,3]
          /    \            /    \
      1 [0,0] 2 [1,1]  3 [2,2] 4 [3,3]
```

- The root represents the complete array.
- Each leaf represents one array index.
- Each internal node combines the answers of its two children.

Common information stored at a node:

- range sum;
- range maximum;
- range minimum;
- greatest common divisor or another associative operation.

### Why use a segment tree?

For repeated range queries and point updates:

| Method | Build | Range sum query | Point update |
|---|---:|---:|---:|
| Plain array | O(1) | O(n) | O(1) |
| Prefix sum | O(n) | O(1) | O(n) |
| Segment tree | O(n) | O(log n) | O(log n) |

A segment tree balances both operations. It is most useful when the array
changes and many queries must be answered.

### Array representation

The code stores the tree in an array using zero-based indexes:

```text
current node = i
left child   = 2i + 1
right child  = 2i + 2
```

For a segment `[start, end]`:

```java
int middle = start + (end - start) / 2;
left segment  = [start, middle]
right segment = [middle + 1, end]
```

### Number of nodes and tree size

With `n` array values, this recursive segment tree has:

- `n` leaf nodes;
- `n - 1` internal nodes;
- `2n - 1` logical nodes in total.

When `n` is not a power of two, heap-style indexes can leave unused
positions. Allocating `4 * n` is a simple safe rule:

```java
tree = new long[4 * n];
```

The tree height is O(log n), and total tree storage is O(n).

### The three overlap cases

Every range query compares:

- node segment `[segmentStart, segmentEnd]`;
- requested range `[queryLeft, queryRight]`.

**1. No overlap**

The segment contributes the operation’s neutral value:

```text
Sum: 0
Maximum: negative infinity
Minimum: positive infinity
```

**2. Complete overlap**

The node’s whole segment lies inside the query, so return its stored value.

**3. Partial overlap**

Query both children and combine their answers.

### Construction, query, and update

- Build visits every logical tree node once: O(n).
- A range query follows only relevant branches: O(log n).
- A point update follows one root-to-leaf path: O(log n).

The Java code uses inclusive ranges such as `[2, 5]`.

## 2. How to Recognize This Pattern

Consider a segment tree when:

- there are many range sum/minimum/maximum queries;
- values also change between queries;
- both query and update should be faster than O(n);
- the operation can combine answers from two adjacent ranges;
- the input is mostly static except for point or range updates.

Usually choose something simpler when:

- there are no updates: prefix sums or preprocessing may be enough;
- only point queries are needed: use the array directly;
- only prefix sums and point updates are needed: a Fenwick tree may be
  simpler;
- updates affect whole ranges: a segment tree may need lazy propagation,
  which is beyond this chapter.

## 3. Problems in This Folder

### Range Sum Query and Point Update

**What the question asks**

Build a tree that returns the sum of any inclusive array range and supports
replacing one array value.

**Brute-force approach**

- Query: loop from `left` through `right` and add values.
- Query time: O(n) worst case.
- Update time: O(1).
- Extra space: O(1).

A prefix-sum alternative gives O(1) queries but requires O(n) work after
an update.

**Optimized approach**

**Build**

- Store the array value at each leaf.
- Recursively build both halves.
- Store `leftSum + rightSum` at each parent.
- Time: O(n). Tree space: O(n).

**Query**

- No overlap: return `0`.
- Complete overlap: return `tree[node]`.
- Partial overlap: return left query + right query.
- Time: O(log n). Recursion space: O(log n).

**Update**

- Follow only the segment containing the target index.
- Replace its leaf.
- Recompute sums while returning to the root.
- Time: O(log n). Recursion space: O(log n).

**Why it works**

Each node stores the exact sum of its segment. A query separates the
requested range into stored segments, while an update changes only the
ancestors whose segments contain the updated index.

**Interview explanation**

“I build the tree recursively so every node stores the sum of its segment.
For a query, no overlap returns zero, complete overlap returns the stored
sum, and partial overlap combines both children. A point update follows
one path to the leaf and recomputes ancestors. Building is O(n), while
queries and updates are O(log n).”

**Common follow-up questions**

- Why is no-overlap sum zero? Zero does not change an addition result.
- Why use `long` for sums? Adding many `int` values can exceed the integer
  range.
- Does update need a difference? No. Directly replacing the leaf and
  recomputing parents is simple and works correctly.
- Are ranges inclusive? Yes, both boundaries are included.
- Does the code modify the original input array? No. The tree stores its
  updated values internally.

**Dry run**

For `[1, 2, 3, 4]`, query `[1, 3]`:

```text
[0,3] partially overlaps
  [0,1] partially overlaps -> [1,1] gives 2
  [2,3] completely overlaps -> gives 7
Answer = 2 + 7 = 9
```

Update index `2` from `3` to `10`:

```text
leaf [2,2] becomes 10
[2,3] becomes 10 + 4 = 14
[0,3] becomes 3 + 14 = 17
```

**Common mistakes**

- Using incorrect no-overlap conditions.
- Forgetting that both query boundaries are inclusive.
- Recursing into `[middle, end]` instead of `[middle + 1, end]`.
- Updating the leaf but not recomputing its ancestors.
- Returning a stored node for partial overlap.

### Range Maximum Query and Point Update

**What the question asks**

Return the largest value in an inclusive range and support replacing one
value.

**Brute-force approach**

- Scan every value in the requested range.
- Query time: O(n) worst case.
- Update time: O(1).
- Extra space: O(1).

**Optimized approach**

- Build leaves from array values.
- Every parent stores:
  `max(left child, right child)`.
- Query cases:
  - no overlap: `Integer.MIN_VALUE`;
  - complete overlap: stored maximum;
  - partial overlap: maximum of both child answers.
- Point update replaces a leaf and recomputes maximums upward.
- Build: O(n).
- Query: O(log n).
- Update: O(log n).
- Tree space: O(n); recursion space: O(log n).

**Why it works**

The maximum of two adjacent ranges is the maximum of their individual
maximums. Negative infinity safely represents a range that contributes no
real value.

**Interview explanation**

“The structure is the same as a sum tree, but every internal node stores
the maximum of its children. During a query, no overlap returns negative
infinity so it cannot incorrectly win. Complete overlap returns the node,
and partial overlap takes the maximum of both answers. Point updates
recompute maximums along one path.”

**Common follow-up questions**

- Why not return zero for no overlap? The real range might contain only
  negative numbers, so zero would incorrectly become the maximum.
- What is the neutral maximum value? Negative infinity, represented by
  `Integer.MIN_VALUE`.
- Can a new update reduce the maximum? Yes. Recomputing from both children
  handles both increases and decreases.
- Why is `tree[node] = max(tree[node], newValue)` incorrect? It cannot
  handle replacing the current maximum with a smaller value.

**Dry run**

Array: `[6, 8, -1, 2, 17, 1]`.

```text
maximum [2,5] = max(-1, 2, 17, 1) = 17
update index 4 to 0
maximum [2,5] = max(-1, 2, 0, 1) = 2
```

**Common mistakes**

- Returning `0` for no overlap.
- Combining child answers with addition or `Math.min`.
- Updating parents using only the new value.
- Forgetting that maximum updates may lower the answer.

### Range Minimum Query and Point Update

**What the question asks**

Return the smallest value in an inclusive range and support replacing one
value.

**Brute-force approach**

- Scan every value in the requested range.
- Query time: O(n) worst case.
- Update time: O(1).
- Extra space: O(1).

**Optimized approach**

- Every internal node stores:
  `min(left child, right child)`.
- Query cases:
  - no overlap: `Integer.MAX_VALUE`;
  - complete overlap: stored minimum;
  - partial overlap: minimum of both child answers.
- Update one leaf and recompute parent minimums.
- Build: O(n).
- Query: O(log n).
- Update: O(log n).
- Tree space: O(n); recursion space: O(log n).

**Why it works**

The minimum of two neighboring ranges is the minimum of their stored
minimums. Positive infinity has no effect when combined with a real value.

**Interview explanation**

“For a minimum tree, every parent stores the smaller child value. A
non-overlapping segment returns positive infinity, complete overlap returns
the node, and partial overlap takes the minimum of both recursive answers.
An update replaces one leaf and recalculates the minimum along its ancestor
path, so query and update are O(log n).”

**Common follow-up questions**

- Why positive infinity for no overlap? It cannot become smaller than a
  real integer value.
- What represents positive infinity? `Integer.MAX_VALUE`.
- Is the minimum query code identical to maximum? The structure is the
  same, but the neutral value and combine operation must change.
- Can updates increase the minimum? Yes; parent recomputation handles it.

**Dry run**

Array: `[6, 8, -1, 2, 17, 1]`.

```text
minimum [2,5] = -1
update index 2 to 10
minimum [2,5] = min(10, 2, 17, 1) = 1
```

**Common mistakes**

- Returning `Integer.MIN_VALUE` for no overlap.
- Accidentally using `Math.max` when combining answers.
- Copying maximum-tree code without changing every required operation.
- Failing to recompute parents after increasing the old minimum.

## 4. Topic-Level Interview Questions

**What is a segment tree?**  
A binary tree in which each node stores combined information for an array
segment.

**Why is build O(n), not O(n log n)?**  
The build processes each of the O(n) logical tree nodes once.

**Why are query and update O(log n)?**  
The tree height is O(log n). A point update follows one path, and a range
query visits only a bounded number of relevant nodes per level.

**How much space does a segment tree use?**  
O(n). A `4n` array is a convenient safe allocation for recursive,
heap-indexed storage.

**How many logical nodes are used?**  
With n leaves and every internal node having two children, `2n - 1`
logical nodes are built.

**What are the three query-overlap cases?**  
No overlap, complete overlap, and partial overlap.

**What is a neutral value?**  
A value that does not change the combine result: `0` for sum, negative
infinity for maximum, and positive infinity for minimum.

**Segment tree versus prefix sum?**  
Prefix sums give O(1) sum queries but O(n) point updates. Segment trees
give O(log n) for both.

**Segment tree versus Fenwick tree?**  
A Fenwick tree is smaller and simpler for prefix-based sums. A segment tree
more naturally supports various range operations and advanced updates.

**What is lazy propagation?**  
A technique for postponing changes when one update affects an entire
range. This chapter implements point updates only.

**Can segment trees store objects other than sums?**  
Yes, when information from adjacent segments can be combined consistently,
such as minimum, maximum, or GCD.

## 5. Quick Revision Sheet

### Formulas

```text
left child  = 2i + 1
right child = 2i + 2
middle      = start + (end - start) / 2
left range  = [start, middle]
right range = [middle + 1, end]
```

### Query cases

```text
No overlap       -> neutral value
Complete overlap -> tree[node]
Partial overlap  -> query left and right, then combine
```

### Neutral values

| Operation | Combine | No-overlap value |
|---|---|---|
| Sum | `left + right` | `0` |
| Maximum | `Math.max(left, right)` | `Integer.MIN_VALUE` |
| Minimum | `Math.min(left, right)` | `Integer.MAX_VALUE` |

### Complexities

| Operation | Time | Recursion space |
|---|---:|---:|
| Build | O(n) | O(log n) |
| Range query | O(log n) | O(log n) |
| Point update | O(log n) | O(log n) |
| Tree storage | — | O(n) |

### One-line reminders

- Allocate `4 * n` for simple recursive storage.
- Build leaves first, then combine upward.
- Use inclusive and consistent boundaries.
- Point update changes one leaf and all its ancestors.
- The no-overlap return value depends on the operation.
- Maximum and minimum updates must support both increases and decreases.
- Lazy propagation is not needed for point updates.
