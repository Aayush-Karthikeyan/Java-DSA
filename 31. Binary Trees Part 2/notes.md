# 31. Binary Trees Part 2 - Key Notes

---

## Topics Covered

This part continues binary tree questions from Part 1:

- Diameter of a Tree - Approach 1
- Diameter of a Tree - Approach 2
- Subtree of another tree
- Top View of a Tree

Sample tree used in the Java file:

```text
        1
      /   \
     2     3
    / \     \
   4   5     6
```

---

## Diameter of a Tree

Diameter is the longest path between any two nodes in the tree.

In this tutorial style, diameter is counted as number of nodes on that path.

For the sample tree:

```text
4 -> 2 -> 1 -> 3 -> 6
```

Diameter = `5`

Note:
- Some books count diameter in edges.
- If counting edges, this same path has diameter `4`.

---

## Diameter Approach 1 - O(n^2)

For every node, there are 3 possible answers:

1. Diameter lies completely in the left subtree.
2. Diameter lies completely in the right subtree.
3. Diameter passes through the current root.

Formula:

```text
selfDiameter = height(left) + height(right) + 1
diameter(root) = max(selfDiameter, leftDiameter, rightDiameter)
```

Code pattern:

```java
int leftDiam = diameter(root.left);
int leftHeight = height(root.left);

int rightDiam = diameter(root.right);
int rightHeight = height(root.right);

int selfDiam = leftHeight + rightHeight + 1;

return Math.max(selfDiam, Math.max(leftDiam, rightDiam));
```

Why it is slow:
- For each node, we call `height()`.
- `height()` again visits many nodes.
- So work gets repeated.

Time: `O(n^2)` in the worst case  
Space: `O(h)` recursion stack

---

## Diameter Approach 2 - O(n)

Approach 2 improves the solution by returning two things together:

```text
diameter
height
```

Helper class:

```java
static class DiameterInfo {
    int diam;
    int ht;
}
```

For each node:

```text
diam = max(left diameter, right diameter, left height + right height + 1)
height = max(left height, right height) + 1
```

Code pattern:

```java
DiameterInfo leftInfo = diameter(root.left);
DiameterInfo rightInfo = diameter(root.right);

int diam = Math.max(
    Math.max(leftInfo.diam, rightInfo.diam),
    leftInfo.ht + rightInfo.ht + 1
);

int ht = Math.max(leftInfo.ht, rightInfo.ht) + 1;

return new DiameterInfo(diam, ht);
```

Why it is faster:
- Each node is visited only once.
- Height is calculated while returning from recursion.
- No repeated `height()` calls.

Time: `O(n)`  
Space: `O(h)` recursion stack

---

## Subtree of Another Tree

Goal:

Check whether `subRoot` exists inside `root` with the same structure and same values.

Example:

```text
Main tree:
        1
      /   \
     2     3
    / \     \
   4   5     6

Subtree:
     2
    / \
   4   5
```

This subtree exists, so answer is `true`.

---

## isIdentical()

Before checking subtree, we need a helper that checks if two trees are exactly the same.

Two trees are identical if:

- both are null, OR
- both are non-null
- root data is the same
- left subtree is identical
- right subtree is identical

Code pattern:

```java
if (node == null && subRoot == null) return true;
else if (node == null || subRoot == null || node.data != subRoot.data) return false;

return isIdentical(node.left, subRoot.left)
    && isIdentical(node.right, subRoot.right);
```

---

## isSubtree()

Steps:

1. Traverse the main tree.
2. If current node data matches `subRoot.data`, call `isIdentical()`.
3. If identical, return true.
4. Otherwise check left subtree or right subtree.

Code pattern:

```java
if (root == null) return false;

if (root.data == subRoot.data) {
    if (isIdentical(root, subRoot)) return true;
}

return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
```

Time: `O(n * m)` in the worst case  
Space: `O(h)` recursion stack

Where:
- `n` = nodes in main tree
- `m` = nodes in subtree

---

## Top View of a Tree

Top view means the nodes visible when looking at the tree from above.

For the sample tree:

```text
        1
      /   \
     2     3
    / \     \
   4   5     6
```

Top view:

```text
4 2 1 3 6
```

Node `5` is not visible because it has the same horizontal distance as `1`, and `1` is above it.

---

## Horizontal Distance

Horizontal distance, or `hd`, tells how far a node is from the root horizontally.

Rules:

```text
root hd = 0
left child hd = parent hd - 1
right child hd = parent hd + 1
```

For the sample tree:

```text
        1(0)
      /      \
   2(-1)    3(1)
   /  \       \
4(-2) 5(0)    6(2)
```

Top view keeps the first node seen at every horizontal distance:

```text
hd -2 -> 4
hd -1 -> 2
hd  0 -> 1
hd  1 -> 3
hd  2 -> 6
```

---

## Top View Approach

Use level order traversal with a queue.

Why level order?
- It visits upper nodes before lower nodes.
- So the first node at each horizontal distance is the visible one.

Data structures:

```java
Queue<TopViewInfo> q = new LinkedList<>();
HashMap<Integer, Node> map = new HashMap<>();
```

`TopViewInfo` stores:

```java
Node node;
int hd;
```

Algorithm:

1. Add root with `hd = 0`.
2. Remove front from queue.
3. If this `hd` is not already in map, store the node.
4. Add left child with `hd - 1`.
5. Add right child with `hd + 1`.
6. Track minimum and maximum horizontal distance.
7. Print values from `min` to `max`.

Code pattern:

```java
if (!map.containsKey(curr.hd)) {
    map.put(curr.hd, curr.node);
}

if (curr.node.left != null) {
    q.add(new TopViewInfo(curr.node.left, curr.hd - 1));
    min = Math.min(min, curr.hd - 1);
}

if (curr.node.right != null) {
    q.add(new TopViewInfo(curr.node.right, curr.hd + 1));
    max = Math.max(max, curr.hd + 1);
}
```

Time: `O(n)`  
Space: `O(n)`

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| Diameter Approach 1 | Try left, right, and through-root diameter; recalculates height | O(n^2) |
| Diameter Approach 2 | Return diameter and height together | O(n) |
| isIdentical | Check if two trees match exactly | O(m) |
| isSubtree | Search root, then compare matching candidates | O(n * m) |
| Top View | BFS + horizontal distance + first node per hd | O(n) |

---

## Common Mistakes

1. Forgetting that this tutorial counts diameter in nodes, not edges.
2. In diameter Approach 1, forgetting `+ 1` for the current root.
3. In diameter Approach 2, returning only diameter and losing height.
4. In `isIdentical`, checking only node values but not structure.
5. In `isSubtree`, returning false too early instead of searching left and right.
6. In top view, replacing a node already stored for a horizontal distance. The first one is the visible one.
7. Forgetting to update `min` and `max` horizontal distances.

---

## Memory Tricks

Diameter:

```text
left diameter
right diameter
left height + right height + 1
```

Top View:

```text
BFS + hd + first time seen
```

Subtree:

```text
find matching root value, then check identical structure
```
