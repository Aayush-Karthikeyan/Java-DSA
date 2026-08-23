# 34. Binary Search Trees Part 2 - Key Notes

---

## Topics Covered

This section continues BST problems:

- Sorted Array to Balanced BST
- Convert BST to Balanced BST
- Size of Largest BST in a Binary Tree
- AVL Trees
- Red-Black Trees reading material summary

---

## Balanced BST

A balanced BST keeps height small, so operations stay fast.

For a balanced BST:

```text
search, insert, delete -> O(log n)
```

For a skewed BST:

```text
search, insert, delete -> O(n)
```

Example of a skewed BST:

```text
1
 \
  2
   \
    3
     \
      4
```

This behaves like a linked list.

---

## Sorted Array to Balanced BST

Goal:

Convert a sorted array into a balanced BST.

Example:

```text
arr = [3, 5, 6, 8, 10, 11, 12]
```

Pick middle as root:

```text
        8
      /   \
     5     11
    / \   /  \
   3   6 10  12
```

Approach:

1. Pick middle element as root.
2. Recursively build left half.
3. Recursively build right half.

Code pattern:

```java
if (start > end) return null;

int mid = (start + end) / 2;
Node root = new Node(arr[mid]);

root.left = createBST(arr, start, mid - 1);
root.right = createBST(arr, mid + 1, end);

return root;
```

Time: `O(n)`  
Space: `O(log n)` recursion stack for balanced output

---

## Convert BST to Balanced BST

Goal:

Take any BST, even a skewed one, and rebuild it as a balanced BST.

Key fact:

```text
Inorder traversal of BST gives sorted sequence.
```

Steps:

1. Store inorder traversal in an ArrayList.
2. Use the sorted list to create a balanced BST.

Code pattern:

```java
ArrayList<Integer> inorder = new ArrayList<>();
getInorder(root, inorder);

root = createBST(inorder, 0, inorder.size() - 1);
return root;
```

Time: `O(n)`  
Space: `O(n)`

---

## Size of Largest BST in a Binary Tree

Goal:

Given any binary tree, find the size of the largest subtree that is a valid BST.

Example:

```text
              50
            /    \
          30      60
         /  \    /  \
        5   20  45   70
                    /  \
                   65   80
```

The largest BST is:

```text
        60
       /  \
      45   70
          /  \
         65   80
```

Size = `5`

---

## Largest BST Info Class

For every subtree, return four things:

```text
isBST
size
min
max
```

Java helper:

```java
static class Info {
    boolean isBST;
    int size;
    int min;
    int max;
}
```

For null:

```java
isBST = true
size = 0
min = Integer.MAX_VALUE
max = Integer.MIN_VALUE
```

Why those min/max values?

They make comparisons work cleanly when a node has an empty left or right subtree.

---

## Largest BST Conditions

A subtree rooted at `root` is a BST if:

```text
left subtree is BST
right subtree is BST
root.data > leftInfo.max
root.data < rightInfo.min
```

If true:

```java
maxBST = Math.max(maxBST, size);
return new Info(true, size, min, max);
```

If false:

```java
return new Info(false, size, min, max);
```

Time: `O(n)`  
Space: `O(h)` recursion stack

Common mistake:
- Checking only the root's immediate children. You need min and max from the full left/right subtrees.

---

## AVL Trees

An AVL tree is a self-balancing BST.

It keeps this rule at every node:

```text
balance factor = height(left) - height(right)
```

Allowed balance factors:

```text
-1, 0, +1
```

If balance factor becomes outside this range, the tree rotates to fix itself.

Why AVL?

AVL keeps height `O(log n)`, so search, insert, and delete remain `O(log n)`.

---

## AVL Rotations

There are 4 imbalance cases.

### LL Case

New node inserted into left side of left subtree.

Fix:

```text
Right rotation
```

### RR Case

New node inserted into right side of right subtree.

Fix:

```text
Left rotation
```

### LR Case

New node inserted into right side of left subtree.

Fix:

```text
Left rotate left child, then right rotate root
```

### RL Case

New node inserted into left side of right subtree.

Fix:

```text
Right rotate right child, then left rotate root
```

---

## AVL Insert Pattern

Steps:

1. Insert like a normal BST.
2. Update height.
3. Calculate balance factor.
4. Apply one of the 4 rotations if needed.

Code idea:

```java
root.height = Math.max(height(root.left), height(root.right)) + 1;
int balance = getBalance(root);

if (balance > 1 && key < root.left.data) return rightRotate(root); // LL
if (balance < -1 && key > root.right.data) return leftRotate(root); // RR
if (balance > 1 && key > root.left.data) {                         // LR
    root.left = leftRotate(root.left);
    return rightRotate(root);
}
if (balance < -1 && key < root.right.data) {                       // RL
    root.right = rightRotate(root.right);
    return leftRotate(root);
}
```

Time: `O(log n)`

---

## Red-Black Trees

Red-Black Trees are another kind of self-balancing BST.

They are less strictly balanced than AVL trees, but they usually need fewer rotations during insertion/deletion.

Common rules:

- Each node is red or black.
- Root is black.
- Null leaves are treated as black.
- A red node cannot have a red child.
- Every path from a node to descendant null leaves has the same number of black nodes.

Big idea:

Red-Black Trees keep height `O(log n)`, so operations stay efficient.

AVL vs Red-Black:

| Tree | Balance Strictness | Search | Insert/Delete |
|---|---|---|---|
| AVL | More strict | Very fast | More rotations |
| Red-Black | Less strict | Fast | Usually fewer rotations |

Java's `TreeMap` and `TreeSet` use Red-Black Tree ideas internally.

---

## Quick Summary

| Topic | Main Idea | Time |
|---|---|---|
| Sorted Array to Balanced BST | Pick middle as root recursively | O(n) |
| Convert BST to Balanced BST | Inorder list, then build balanced BST | O(n) |
| Largest BST in BT | Return isBST, size, min, max | O(n) |
| AVL Insert | Normal BST insert plus rotations | O(log n) |
| Red-Black Tree | Balanced BST using colors | O(log n) |

---

## Common Mistakes

1. Using the first element of sorted array as root, which creates a skewed tree.
2. Forgetting that BST inorder traversal is sorted.
3. In balanced BST creation, using wrong recursive ranges around `mid`.
4. In largest BST, not using min/max for the whole subtree.
5. In largest BST null base case, using bad min/max values.
6. In AVL insertion, forgetting to update height before checking balance.
7. Mixing up LL/RR/LR/RL rotation cases.

---

## Memory Tricks

Balanced BST from sorted array:

```text
middle becomes root
```

Convert BST to balanced BST:

```text
BST -> inorder sorted list -> balanced BST
```

Largest BST:

```text
return 4 things: isBST, size, min, max
```

AVL:

```text
balance factor must be -1, 0, or +1
```
