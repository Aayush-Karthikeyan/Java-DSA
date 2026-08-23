# 33. Binary Search Trees Part 1 - Key Notes

---

## What is a BST?

A Binary Search Tree, or BST, is a binary tree with one extra rule:

```text
left subtree values  < root value
right subtree values > root value
```

Example:

```text
        8
      /   \
     5     10
    / \      \
   3   6      11
  / \           \
 1   4           14
```

Because of the BST rule, we can search like binary search:

- smaller value -> go left
- larger value -> go right

If the tree is balanced, operations are usually `O(log n)`.  
If the tree becomes skewed like a linked list, operations become `O(n)`.

---

## Build a BST

Build a BST by inserting values one by one.

Insertion rule:

```text
if val < root.data -> insert in left subtree
if val > root.data -> insert in right subtree
```

Code pattern:

```java
if (root == null) {
    return new Node(val);
}

if (val < root.data) {
    root.left = insert(root.left, val);
} else if (val > root.data) {
    root.right = insert(root.right, val);
}

return root;
```

Time: `O(h)` per insertion  
`h` = height of tree

---

## Inorder Traversal

In a BST, inorder traversal prints values in sorted order.

Order:

```text
left, root, right
```

For the sample tree:

```text
1 3 4 5 6 8 10 11 14
```

This is one of the easiest ways to check whether a BST was built correctly.

Time: `O(n)`

---

## Search in a BST

Use the BST property:

```java
if (root == null) return false;
if (root.data == key) return true;

if (key < root.data) return search(root.left, key);
else return search(root.right, key);
```

Example:

Searching for `6`:

```text
8 -> left to 5 -> right to 6
```

Time: `O(h)`

Common mistake:
- Searching both left and right like a normal binary tree. In a BST, only one side is needed.

---

## Delete a Node

First find the node using BST search rules. Then handle one of three cases.

### Case 1: Leaf Node

The node has no children.

```java
if (root.left == null && root.right == null) {
    return null;
}
```

Just remove it.

---

### Case 2: One Child

The node has only one child.

```java
if (root.left == null) return root.right;
else if (root.right == null) return root.left;
```

Return the child so it replaces the deleted node.

---

### Case 3: Two Children

Use the inorder successor.

Inorder successor = smallest node in the right subtree.

Steps:

1. Find inorder successor.
2. Copy successor value into current node.
3. Delete successor from right subtree.

```java
Node inorderSuccessor = findInorderSuccessor(root.right);
root.data = inorderSuccessor.data;
root.right = delete(root.right, inorderSuccessor.data);
```

Time: `O(h)`

Common mistake:
- Replacing with successor but forgetting to delete the successor from its old position.

---

## Print in Range

Goal:

Print all values between `k1` and `k2` in sorted order.

For range `[4, 11]`, sample output:

```text
4 5 6 8 10 11
```

Use BST pruning:

```text
if root.data is inside range:
    check left
    print root
    check right

if root.data < k1:
    only right subtree can have valid values

if root.data > k2:
    only left subtree can have valid values
```

Code pattern:

```java
if (root.data >= k1 && root.data <= k2) {
    printInRange(root.left, k1, k2);
    System.out.print(root.data + " ");
    printInRange(root.right, k1, k2);
} else if (root.data < k1) {
    printInRange(root.right, k1, k2);
} else {
    printInRange(root.left, k1, k2);
}
```

Time: `O(n)` worst case, but often faster because branches get skipped.

---

## Root to Leaf Paths

Goal:

Print every path from root to a leaf.

For the sample tree:

```text
8 5 3 1
8 5 3 4
8 5 6
8 10 11 14
```

Use backtracking:

```java
path.add(root.data);

if (root.left == null && root.right == null) {
    printPath(path);
}

printRootToLeaf(root.left, path);
printRootToLeaf(root.right, path);

path.remove(path.size() - 1);
```

Why remove at the end?

The same `path` list is reused for other branches. Removing the last node restores the previous path.

Time: `O(n)`

---

## Validate BST

Goal:

Check whether a binary tree follows BST rules.

Do not only compare a node with its immediate children. A deeper node can break the rule.

Use min/max bounds:

```text
root must be greater than min
root must be smaller than max
```

Code pattern:

```java
if (root == null) return true;

if (min != null && root.data <= min.data) return false;
if (max != null && root.data >= max.data) return false;

return isValidBST(root.left, min, root)
    && isValidBST(root.right, root, max);
```

Time: `O(n)`

Common mistake:
- Only checking `root.left.data < root.data` and `root.right.data > root.data`.

---

## Mirror a BST

Mirror means swap left and right children at every node.

Code pattern:

```java
Node leftMirror = createMirror(root.left);
Node rightMirror = createMirror(root.right);

root.left = rightMirror;
root.right = leftMirror;

return root;
```

Important:
- A mirrored BST is usually not a valid BST anymore.
- It is a mirror image of the original tree.

Time: `O(n)`

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| Build BST | Insert values using BST rule | O(h) each |
| Inorder | Prints BST in sorted order | O(n) |
| Search | Move left or right, not both | O(h) |
| Delete | Handle leaf, one child, two children | O(h) |
| Print in Range | Inorder + skip invalid branches | O(n) worst |
| Root to Leaf | DFS + backtracking | O(n) |
| Validate BST | Use min/max bounds | O(n) |
| Mirror BST | Swap left and right everywhere | O(n) |

---

## Common Mistakes

1. Forgetting that inorder traversal of a BST is sorted.
2. Searching both subtrees instead of using the BST property.
3. In delete case 2, not returning the single child.
4. In delete case 3, copying the inorder successor but not deleting it.
5. In print range, going left when `root.data < k1`; the valid values must be on the right.
6. In root-to-leaf paths, forgetting to backtrack with `path.remove(...)`.
7. Validating only parent-child relationships instead of full min/max ranges.
8. Expecting a mirrored BST to still be a valid BST.

---

## Memory Tricks

BST rule:

```text
small left, big right
```

Delete:

```text
0 child -> null
1 child -> return child
2 child -> inorder successor
```

Validate:

```text
every node carries a min and max allowed range
```
