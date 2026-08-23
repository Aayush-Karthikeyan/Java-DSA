import java.util.*;

// ================================================================
// TOPIC: Binary Search Trees Part 2
// Covers:
// 1. Sorted Array to Balanced BST
// 2. Convert BST to Balanced BST
// 3. Size of Largest BST in a Binary Tree
// 4. AVL Tree insertion with rotations
// ================================================================

public class BinarySearchTreesPart2 {

    // ================================================================
    // NODE CLASS
    // data   -> value of node
    // left   -> left child
    // right  -> right child
    // height -> used by AVL tree logic
    // ================================================================
    static class Node {
        int data;
        int height;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
            this.height = 1;
            this.left = null;
            this.right = null;
        }
    }

    // ================================================================
    // 1. SORTED ARRAY TO BALANCED BST
    // Pick middle element as root, recursively build left and right.
    //
    // Why middle?
    // Middle keeps left side and right side almost equal in size.
    //
    // Time: O(n)
    // ================================================================
    static Node sortedArrayToBalancedBST(int[] arr, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        Node root = new Node(arr[mid]);

        root.left = sortedArrayToBalancedBST(arr, start, mid - 1);
        root.right = sortedArrayToBalancedBST(arr, mid + 1, end);

        return root;
    }

    // ================================================================
    // INSERT INTO BST
    // Used to create an unbalanced/skewed BST for balancing example.
    // Time: O(h)
    // ================================================================
    static Node insertBST(Node root, int val) {
        if (root == null) {
            return new Node(val);
        }

        if (val < root.data) {
            root.left = insertBST(root.left, val);
        } else if (val > root.data) {
            root.right = insertBST(root.right, val);
        }

        return root;
    }

    // Store inorder traversal of BST. Inorder of BST is sorted.
    static void getInorder(Node root, ArrayList<Integer> inorder) {
        if (root == null) {
            return;
        }

        getInorder(root.left, inorder);
        inorder.add(root.data);
        getInorder(root.right, inorder);
    }

    // Creates balanced BST from sorted inorder list.
    static Node createBSTFromInorder(ArrayList<Integer> inorder, int start, int end) {
        if (start > end) {
            return null;
        }

        int mid = (start + end) / 2;
        Node root = new Node(inorder.get(mid));

        root.left = createBSTFromInorder(inorder, start, mid - 1);
        root.right = createBSTFromInorder(inorder, mid + 1, end);

        return root;
    }

    // ================================================================
    // 2. CONVERT BST TO BALANCED BST
    // Step 1: Get inorder traversal from old BST.
    // Step 2: Use sorted inorder list to build a balanced BST.
    //
    // Time: O(n)
    // Space: O(n)
    // ================================================================
    static Node balanceBST(Node root) {
        ArrayList<Integer> inorder = new ArrayList<>();
        getInorder(root, inorder);
        return createBSTFromInorder(inorder, 0, inorder.size() - 1);
    }

    // ================================================================
    // INFO CLASS FOR LARGEST BST IN BINARY TREE
    // For each subtree we need:
    // isBST -> whether this subtree is a valid BST
    // size  -> number of nodes in this subtree
    // min   -> minimum value in this subtree
    // max   -> maximum value in this subtree
    // ================================================================
    static class Info {
        boolean isBST;
        int size;
        int min;
        int max;

        Info(boolean isBST, int size, int min, int max) {
            this.isBST = isBST;
            this.size = size;
            this.min = min;
            this.max = max;
        }
    }

    static int maxBST = 0;

    // ================================================================
    // 3. SIZE OF LARGEST BST IN A BINARY TREE
    // A normal binary tree may contain smaller subtrees that are BSTs.
    //
    // A subtree rooted at root is BST only if:
    // - left subtree is BST
    // - right subtree is BST
    // - root.data > left max
    // - root.data < right min
    //
    // Time: O(n)
    // ================================================================
    static Info largestBST(Node root) {
        if (root == null) {
            return new Info(true, 0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        Info leftInfo = largestBST(root.left);
        Info rightInfo = largestBST(root.right);

        int size = leftInfo.size + rightInfo.size + 1;
        int min = Math.min(root.data, Math.min(leftInfo.min, rightInfo.min));
        int max = Math.max(root.data, Math.max(leftInfo.max, rightInfo.max));

        if (leftInfo.isBST && rightInfo.isBST
                && root.data > leftInfo.max
                && root.data < rightInfo.min) {
            maxBST = Math.max(maxBST, size);
            return new Info(true, size, min, max);
        }

        return new Info(false, size, min, max);
    }

    // ================================================================
    // 4. AVL TREE HELPERS
    // AVL Tree = self-balancing BST.
    // For every node:
    // balance factor = height(left) - height(right)
    // valid balance factor must be -1, 0, or +1.
    // ================================================================
    static int height(Node root) {
        return root == null ? 0 : root.height;
    }

    static int getBalance(Node root) {
        return root == null ? 0 : height(root.left) - height(root.right);
    }

    // Right rotation fixes Left-Left imbalance.
    static Node rightRotate(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Left rotation fixes Right-Right imbalance.
    static Node leftRotate(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // ================================================================
    // INSERT INTO AVL TREE
    // First insert like normal BST, then check balance and rotate.
    //
    // 4 imbalance cases:
    // LL -> right rotate
    // RR -> left rotate
    // LR -> left rotate child, then right rotate
    // RL -> right rotate child, then left rotate
    //
    // Time: O(log n)
    // ================================================================
    static Node insertAVL(Node root, int key) {
        if (root == null) {
            return new Node(key);
        }

        if (key < root.data) {
            root.left = insertAVL(root.left, key);
        } else if (key > root.data) {
            root.right = insertAVL(root.right, key);
        } else {
            return root; // duplicate keys ignored
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // Left Left case
        if (balance > 1 && key < root.left.data) {
            return rightRotate(root);
        }

        // Right Right case
        if (balance < -1 && key > root.right.data) {
            return leftRotate(root);
        }

        // Left Right case
        if (balance > 1 && key > root.left.data) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Left case
        if (balance < -1 && key < root.right.data) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    // ================================================================
    // TRAVERSAL HELPERS
    // ================================================================
    static void preorder(Node root) {
        if (root == null) {
            return;
        }

        System.out.print(root.data + " ");
        preorder(root.left);
        preorder(root.right);
    }

    static void inorder(Node root) {
        if (root == null) {
            return;
        }

        inorder(root.left);
        System.out.print(root.data + " ");
        inorder(root.right);
    }

    // Sample binary tree from the largest BST problem.
    //
    //              50
    //            /    \
    //          30      60
    //         /  \    /  \
    //        5   20  45   70
    //                    /  \
    //                   65   80
    //
    // Largest BST is rooted at 60 and has size 5.
    static Node createLargestBSTSampleTree() {
        Node root = new Node(50);
        root.left = new Node(30);
        root.right = new Node(60);
        root.left.left = new Node(5);
        root.left.right = new Node(20);
        root.right.left = new Node(45);
        root.right.right = new Node(70);
        root.right.right.left = new Node(65);
        root.right.right.right = new Node(80);
        return root;
    }

    // ================================================================
    // MAIN: sample run for BST Part 2
    // ================================================================
    public static void main(String[] args) {

        // ----- Sorted Array to Balanced BST -----
        int[] sortedArray = {3, 5, 6, 8, 10, 11, 12};
        Node balancedFromArray = sortedArrayToBalancedBST(sortedArray, 0, sortedArray.length - 1);

        System.out.print("Balanced BST from sorted array preorder: ");
        preorder(balancedFromArray); // 8 5 3 6 11 10 12
        System.out.println();

        // ----- Convert BST to Balanced BST -----
        int[] skewedValues = {1, 2, 3, 4, 5, 6, 7};
        Node skewedBST = null;
        for (int val : skewedValues) {
            skewedBST = insertBST(skewedBST, val);
        }

        Node balancedBST = balanceBST(skewedBST);
        System.out.print("Balanced old BST preorder: ");
        preorder(balancedBST); // 4 2 1 3 6 5 7
        System.out.println();

        // ----- Size of Largest BST in Binary Tree -----
        Node btRoot = createLargestBSTSampleTree();
        maxBST = 0;
        largestBST(btRoot);
        System.out.println("Size of largest BST in BT: " + maxBST);

        // ----- AVL Tree -----
        int[] avlValues = {10, 20, 30, 40, 50, 25};
        Node avlRoot = null;
        for (int key : avlValues) {
            avlRoot = insertAVL(avlRoot, key);
        }

        System.out.print("AVL preorder after insertions: ");
        preorder(avlRoot); // 30 20 10 25 40 50
        System.out.println();

        System.out.print("AVL inorder: ");
        inorder(avlRoot); // sorted order
        System.out.println();
    }
}
