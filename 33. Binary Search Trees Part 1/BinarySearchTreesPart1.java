import java.util.*;

// ================================================================
// TOPIC: Binary Search Trees Part 1
// A Binary Search Tree (BST) is a binary tree with this rule:
//
// left subtree values  < root value
// right subtree values > root value
//
// Because of this rule, search/insert/delete can be O(height).
// For a balanced BST, height is O(log n). For a skewed BST, height is O(n).
// ================================================================

public class BinarySearchTreesPart1 {

    // ================================================================
    // NODE CLASS
    // Each node stores data and references to left/right children.
    // ================================================================
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

    // ================================================================
    // INSERT / BUILD A BST
    // If value is smaller than root, go left.
    // If value is greater than root, go right.
    //
    // This version ignores duplicate values.
    // Time: O(h), where h = height of tree
    // ================================================================
    static Node insert(Node root, int val) {
        if (root == null) {
            return new Node(val);
        }

        if (val < root.data) {
            root.left = insert(root.left, val);
        } else if (val > root.data) {
            root.right = insert(root.right, val);
        }

        return root;
    }

    // Builds a BST by inserting values one by one.
    static Node buildBST(int[] values) {
        Node root = null;

        for (int val : values) {
            root = insert(root, val);
        }

        return root;
    }

    // ================================================================
    // INORDER TRAVERSAL
    // In a BST, inorder traversal prints values in sorted order.
    //
    // Order: left, root, right
    // Time: O(n)
    // ================================================================
    static void inorder(Node root) {
        if (root == null) {
            return;
        }

        inorder(root.left);
        System.out.print(root.data + " ");
        inorder(root.right);
    }

    // ================================================================
    // SEARCH IN A BST
    // Use BST property to remove half-ish of the search space each step:
    // - key == root.data -> found
    // - key < root.data  -> search left
    // - key > root.data  -> search right
    //
    // Time: O(h)
    // ================================================================
    static boolean search(Node root, int key) {
        if (root == null) {
            return false;
        }

        if (root.data == key) {
            return true;
        }

        if (key < root.data) {
            return search(root.left, key);
        } else {
            return search(root.right, key);
        }
    }

    // ================================================================
    // DELETE A NODE FROM BST
    // First search for the node. Once found, there are 3 cases:
    //
    // Case 1: leaf node       -> return null
    // Case 2: one child       -> return that child
    // Case 3: two children    -> replace with inorder successor,
    //                            then delete successor from right subtree
    //
    // Time: O(h)
    // ================================================================
    static Node delete(Node root, int val) {
        if (root == null) {
            return null;
        }

        if (val < root.data) {
            root.left = delete(root.left, val);
        } else if (val > root.data) {
            root.right = delete(root.right, val);
        } else {
            // Case 1: no child / leaf node
            if (root.left == null && root.right == null) {
                return null;
            }

            // Case 2: one child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Case 3: two children
            Node inorderSuccessor = findInorderSuccessor(root.right);
            root.data = inorderSuccessor.data;
            root.right = delete(root.right, inorderSuccessor.data);
        }

        return root;
    }

    // Inorder successor in a BST = smallest node in the right subtree.
    static Node findInorderSuccessor(Node root) {
        while (root.left != null) {
            root = root.left;
        }

        return root;
    }

    // ================================================================
    // PRINT IN RANGE
    // Prints all values in [k1, k2] in sorted order.
    //
    // If root.data is inside range, both sides might contain answers.
    // If root.data < k1, only the right side can contain larger values.
    // If root.data > k2, only the left side can contain smaller values.
    //
    // Time: O(n) worst case, faster when many branches are skipped.
    // ================================================================
    static void printInRange(Node root, int k1, int k2) {
        if (root == null) {
            return;
        }

        if (root.data >= k1 && root.data <= k2) {
            printInRange(root.left, k1, k2);
            System.out.print(root.data + " ");
            printInRange(root.right, k1, k2);
        } else if (root.data < k1) {
            printInRange(root.right, k1, k2);
        } else {
            printInRange(root.left, k1, k2);
        }
    }

    // ================================================================
    // ROOT TO LEAF PATHS
    // A path is built while going down.
    // When we reach a leaf, print the path.
    // While returning, remove the current node from path (backtracking).
    //
    // Time: O(n)
    // ================================================================
    static void printRootToLeaf(Node root, ArrayList<Integer> path) {
        if (root == null) {
            return;
        }

        path.add(root.data);

        if (root.left == null && root.right == null) {
            printPath(path);
        }

        printRootToLeaf(root.left, path);
        printRootToLeaf(root.right, path);

        // Backtrack so the same path list can be reused for other branches.
        path.remove(path.size() - 1);
    }

    static void printPath(ArrayList<Integer> path) {
        for (int val : path) {
            System.out.print(val + " ");
        }
        System.out.println();
    }

    // ================================================================
    // VALIDATE BST
    // Each node must be inside a valid range:
    // - left child must be smaller than current node
    // - right child must be greater than current node
    //
    // min = lower bound
    // max = upper bound
    //
    // Time: O(n)
    // ================================================================
    static boolean isValidBST(Node root, Node min, Node max) {
        if (root == null) {
            return true;
        }

        if (min != null && root.data <= min.data) {
            return false;
        }

        if (max != null && root.data >= max.data) {
            return false;
        }

        return isValidBST(root.left, min, root)
                && isValidBST(root.right, root, max);
    }

    // ================================================================
    // MIRROR A BST
    // Swaps left and right subtree at every node.
    //
    // Note: after mirroring, it is usually no longer a valid BST.
    // It is now a mirror image of the old tree.
    //
    // Time: O(n)
    // ================================================================
    static Node createMirror(Node root) {
        if (root == null) {
            return null;
        }

        Node leftMirror = createMirror(root.left);
        Node rightMirror = createMirror(root.right);

        root.left = rightMirror;
        root.right = leftMirror;

        return root;
    }

    // ================================================================
    // MAIN: sample run for all BST Part 1 topics
    //
    // Values create this BST:
    //
    //          8
    //        /   \
    //       5     10
    //      / \      \
    //     3   6      11
    //    / \           \
    //   1   4           14
    // ================================================================
    public static void main(String[] args) {
        int[] values = {8, 5, 3, 1, 4, 6, 10, 11, 14};

        Node root = buildBST(values);

        System.out.print("Inorder after build: ");
        inorder(root);
        System.out.println();

        System.out.println("Search 6:  " + search(root, 6));
        System.out.println("Search 12: " + search(root, 12));

        System.out.print("Print in range [4, 11]: ");
        printInRange(root, 4, 11);
        System.out.println();

        System.out.println("Root to leaf paths:");
        printRootToLeaf(root, new ArrayList<>());

        System.out.println("Valid BST: " + isValidBST(root, null, null));

        Node deleteRoot = buildBST(values);
        deleteRoot = delete(deleteRoot, 1);  // leaf node case
        deleteRoot = delete(deleteRoot, 10); // one child case
        deleteRoot = delete(deleteRoot, 5);  // two children case

        System.out.print("After deleting 1, 10, 5: ");
        inorder(deleteRoot);
        System.out.println();

        Node mirrorRoot = buildBST(values);
        createMirror(mirrorRoot);
        System.out.print("Mirror inorder: ");
        inorder(mirrorRoot);
        System.out.println();
        System.out.println("Valid after mirror: " + isValidBST(mirrorRoot, null, null));
    }
}
