# 30. Binary Trees Part 1 - Key Notes

---

## What is a Binary Tree?

A binary tree is a tree where each node can have at most two children:

```text
left child
right child
```

Basic terms:
- `root`: first/top node of the tree
- `parent`: node that has children
- `child`: node connected below another node
- `leaf`: node with no children
- `subtree`: smaller tree inside the main tree
- `level`: distance-style layer of the tree, starting from root

Example:

```text
        1
      /   \
     2     3
    / \     \
   4   5     6
```

Here:
- `1` is the root.
- `2` and `3` are children of `1`.
- `4`, `5`, and `6` are leaf nodes.
- The tree rooted at `2` is a subtree.

---

## Node Class

Each node stores data and references to its left and right children.

```java
static class Node {
    int data;
    Node left;
    Node right;

    Node(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}
```

If `left == null`, there is no left child.  
If `right == null`, there is no right child.

---

## Build Tree Using Preorder

The tutorial uses a preorder sequence with `-1` meaning null.

Preorder order:

```text
root, left subtree, right subtree
```

Example sequence:

```text
1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1
```

This builds:

```text
        1
      /   \
     2     3
    / \     \
   4   5     6
```

How the recursive build works:

```java
idx++;
if (nodes[idx] == -1) return null;

Node newNode = new Node(nodes[idx]);
newNode.left = buildTree(nodes);
newNode.right = buildTree(nodes);
return newNode;
```

Important:
- The first non-`-1` value becomes a node.
- Every `-1` means that child does not exist.
- The index must move forward once for every value in the array.

Time: `O(n)`  
Space: `O(h)` recursion stack, where `h` is height of the tree

---

## Preorder Traversal

Order:

```text
root, left, right
```

Code pattern:

```java
if (root == null) return;
print root.data;
preorder(root.left);
preorder(root.right);
```

For the sample tree:

```text
1 2 4 5 3 6
```

Use it when you want to process a node before its children.

Time: `O(n)`

---

## Inorder Traversal

Order:

```text
left, root, right
```

Code pattern:

```java
if (root == null) return;
inorder(root.left);
print root.data;
inorder(root.right);
```

For the sample tree:

```text
4 2 5 1 3 6
```

Important:
- In a Binary Search Tree, inorder traversal prints values in sorted order.
- In a normal binary tree, it simply follows left-root-right.

Time: `O(n)`

---

## Postorder Traversal

Order:

```text
left, right, root
```

Code pattern:

```java
if (root == null) return;
postorder(root.left);
postorder(root.right);
print root.data;
```

For the sample tree:

```text
4 5 2 6 3 1
```

Use it when children must be solved before the parent, like deletion or subtree calculations.

Time: `O(n)`

---

## Level Order Traversal

Level order means printing nodes level by level.

For the sample tree:

```text
1
2 3
4 5 6
```

This uses a queue because a queue is FIFO:
- Add root.
- Remove front.
- Print it.
- Add its children.

Tutorial-style code uses `null` as a level separator:

```java
Queue<Node> q = new LinkedList<>();
q.add(root);
q.add(null);

while (!q.isEmpty()) {
    Node currNode = q.remove();

    if (currNode == null) {
        System.out.println();
        if (q.isEmpty()) break;
        else q.add(null);
    } else {
        System.out.print(currNode.data + " ");
        if (currNode.left != null) q.add(currNode.left);
        if (currNode.right != null) q.add(currNode.right);
    }
}
```

Important:
- Use `LinkedList` here because it allows `null` markers.
- `ArrayDeque` does not allow `null`.

Time: `O(n)`  
Space: `O(n)`

---

## Height of a Tree

Height is the number of nodes in the longest path from root to leaf.

Formula:

```text
height(root) = max(height(left), height(right)) + 1
```

Base case:

```java
if (root == null) return 0;
```

For the sample tree, height is `3`:

```text
1 -> 2 -> 4
```

or

```text
1 -> 3 -> 6
```

Time: `O(n)`

---

## Count of Nodes

Count means total number of nodes in the tree.

Formula:

```text
count(root) = count(left) + count(right) + 1
```

Base case:

```java
if (root == null) return 0;
```

For the sample tree:

```text
nodes = 1, 2, 3, 4, 5, 6
count = 6
```

Time: `O(n)`

---

## Sum of Nodes

Sum means adding all node values.

Formula:

```text
sum(root) = sum(left) + sum(right) + root.data
```

Base case:

```java
if (root == null) return 0;
```

For the sample tree:

```text
1 + 2 + 3 + 4 + 5 + 6 = 21
```

Time: `O(n)`

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| Build Tree Preorder | Use `-1` as null marker | O(n) |
| Preorder | root, left, right | O(n) |
| Inorder | left, root, right | O(n) |
| Postorder | left, right, root | O(n) |
| Level Order | BFS using queue | O(n) |
| Height | max(left height, right height) + 1 | O(n) |
| Count Nodes | left count + right count + 1 | O(n) |
| Sum Nodes | left sum + right sum + root data | O(n) |

---

## Common Mistakes

1. Forgetting the base case `if (root == null) return;`.
2. Mixing traversal orders.
3. In `buildTree`, forgetting that `-1` means null.
4. Not incrementing the preorder index before reading the next value.
5. In level order traversal, forgetting to add left and right children only when they are not null.
6. Using `ArrayDeque` with a `null` level marker. Use `LinkedList` for that version.
7. Forgetting `+ 1` in height and count formulas.

---

## Traversal Memory Trick

The position of `root` tells the traversal name:

```text
Preorder:  root first  -> root, left, right
Inorder:   root middle -> left, root, right
Postorder: root last   -> left, right, root
```
