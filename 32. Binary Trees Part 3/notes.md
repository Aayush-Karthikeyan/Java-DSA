# 32. Binary Trees Part 3 - Key Notes

---

## Topics Covered

This part covers more recursive binary tree problems:

- Kth Level
- Lowest Common Ancestor - Approach 1
- Lowest Common Ancestor - Approach 2
- Minimum Distance between 2 Nodes
- Kth Ancestor of a Node
- Transform to Sum Tree

Sample tree used in the Java file:

```text
        1
      /   \
     2     3
    / \   / \
   4   5 6   7
```

---

## Kth Level

Goal:

Print all nodes at level `k`.

In this tutorial style:

```text
root level = 1
children level = 2
grandchildren level = 3
```

For `k = 3`, output is:

```text
4 5 6 7
```

Code pattern:

```java
if (root == null) return;

if (level == k) {
    System.out.print(root.data + " ");
    return;
}

KLevel(root.left, level + 1, k);
KLevel(root.right, level + 1, k);
```

Time: `O(n)`  
Space: `O(h)` recursion stack

Common mistake:
- Starting root at level `0` when the question/tutorial expects root at level `1`.

---

## Lowest Common Ancestor

Lowest Common Ancestor, or LCA, is the lowest node that has both target nodes in its subtree.

Example:

```text
        1
      /   \
     2     3
    / \   / \
   4   5 6   7
```

LCA of `4` and `5` is `2`.  
LCA of `4` and `6` is `1`.

---

## LCA Approach 1 - Using Paths

Idea:

1. Find path from root to `n1`.
2. Find path from root to `n2`.
3. Compare both paths.
4. The last equal node is the LCA.

Example:

```text
path to 4 = 1, 2, 4
path to 5 = 1, 2, 5
last same node = 2
```

### getPath()

`getPath()` uses backtracking:

```java
path.add(root);

if (root.data == n) return true;

boolean foundLeft = getPath(root.left, n, path);
boolean foundRight = getPath(root.right, n, path);

if (foundLeft || foundRight) return true;

path.remove(path.size() - 1);
return false;
```

Important:
- Add the node first.
- If the target is not found below it, remove it while returning.

Time: `O(n)`  
Space: `O(n)` for paths

---

## LCA Approach 2 - Recursive

This is the cleaner optimized approach.

Rules:

```text
if root is null -> return null
if root is n1 or n2 -> return root
search left subtree
search right subtree
if both sides return non-null -> root is LCA
if one side returns non-null -> pass that answer upward
```

Code pattern:

```java
if (root == null || root.data == n1 || root.data == n2) {
    return root;
}

Node leftLca = lca2(root.left, n1, n2);
Node rightLca = lca2(root.right, n1, n2);

if (rightLca == null) return leftLca;
if (leftLca == null) return rightLca;

return root;
```

Time: `O(n)`  
Space: `O(h)` recursion stack

---

## Minimum Distance Between 2 Nodes

Distance means number of edges between the two nodes.

Formula:

```text
distance(n1, n2) = distance(lca, n1) + distance(lca, n2)
```

Example:

```text
4 -> 2 -> 1 -> 3 -> 6
```

Distance between `4` and `6` is `4` edges.

Steps:

1. Find LCA of `n1` and `n2`.
2. Find distance from LCA to `n1`.
3. Find distance from LCA to `n2`.
4. Add both distances.

`lcaDistance()` returns:

```text
0 if current node is target
-1 if target is not found
distance + 1 while returning upward
```

Time: `O(n)`  
Space: `O(h)` recursion stack

---

## Kth Ancestor of a Node

Goal:

Find the kth ancestor of a target node.

Example:

For node `5`:

```text
1
|
2
|
5
```

1st ancestor = `2`  
2nd ancestor = `1`

Return meaning:

```text
-1 -> target not found in this subtree
0  -> current node is target
x  -> target is x edges below current node
```

Code idea:

```java
if (root == null) return -1;
if (root.data == n) return 0;

int leftDist = KAncestor(root.left, n, k);
int rightDist = KAncestor(root.right, n, k);

if (leftDist == -1 && rightDist == -1) return -1;

int max = Math.max(leftDist, rightDist);

if (max + 1 == k) {
    System.out.println(root.data);
}

return max + 1;
```

Time: `O(n)`  
Space: `O(h)` recursion stack

Common mistake:
- Printing when `max == k`. It should be `max + 1 == k` because the current root is one edge above the child result.

---

## Transform to Sum Tree

Goal:

Change every node's value to the sum of values in its left and right subtrees from the original tree.

Leaves become `0` because they have no children.

Original:

```text
        1
      /   \
     2     3
    / \   / \
   4   5 6   7
```

After transform:

```text
        27
      /    \
     9      13
    / \    /  \
   0   0  0    0
```

Explanation:

```text
node 2 becomes 4 + 5 = 9
node 3 becomes 6 + 7 = 13
node 1 becomes 2 + 4 + 5 + 3 + 6 + 7 = 27
```

### Important Return Value

The function returns the old value of the current node.

Why?

The parent still needs the child's original value to calculate its own sum.

Code pattern:

```java
int leftChildSum = transform(root.left);
int rightChildSum = transform(root.right);

int oldData = root.data;

int newLeftData = root.left == null ? 0 : root.left.data;
int newRightData = root.right == null ? 0 : root.right.data;

root.data = newLeftData + leftChildSum + newRightData + rightChildSum;

return oldData;
```

Time: `O(n)`  
Space: `O(h)` recursion stack

Common mistake:
- Directly writing `root.left.data` or `root.right.data` without checking for null.

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| Kth Level | DFS with current level | O(n) |
| LCA Approach 1 | Compare root-to-node paths | O(n) |
| LCA Approach 2 | Recursive search left and right | O(n) |
| Minimum Distance | dist from LCA to both nodes | O(n) |
| Kth Ancestor | Return distance while backtracking | O(n) |
| Sum Tree | Postorder-style transform | O(n) |

---

## Common Mistakes

1. Mixing up level numbering: root can be level `1` or `0` depending on convention.
2. In `getPath`, forgetting to remove the node while backtracking.
3. In LCA Approach 1, using `path1.get(i)` after the loop instead of `path1.get(i - 1)`.
4. In LCA Approach 2, forgetting the case where one side returns null.
5. In minimum distance, counting nodes instead of edges.
6. In kth ancestor, checking `max == k` instead of `max + 1 == k`.
7. In sum tree, not saving old data before overwriting `root.data`.

---

## Memory Tricks

Kth Level:

```text
level == k -> print
```

LCA:

```text
left answer + right answer -> current root is LCA
```

Minimum Distance:

```text
distance through LCA = left distance + right distance
```

Sum Tree:

```text
children first, then root
```
