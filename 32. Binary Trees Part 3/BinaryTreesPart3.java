import java.util.*;

// ================================================================
// TOPIC: Binary Trees Part 3
// Covers:
// 1. Kth Level of a tree
// 2. Lowest Common Ancestor - Approach 1 using paths
// 3. Lowest Common Ancestor - Approach 2 using recursion
// 4. Minimum Distance between 2 nodes
// 5. Kth Ancestor of a node
// 6. Transform to Sum Tree
// ================================================================

public class BinaryTreesPart3 {

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
    // KTH LEVEL
    // Prints all nodes that are present at level k.
    //
    // We start root at level 1:
    // level 1 -> root
    // level 2 -> root's children
    // level 3 -> grandchildren
    //
    // Time: O(n)
    // ================================================================
    static void printKLevel(Node root, int level, int k) {
        if (root == null) {
            return;
        }

        if (level == k) {
            System.out.print(root.data + " ");
            return;
        }

        printKLevel(root.left, level + 1, k);
        printKLevel(root.right, level + 1, k);
    }

    // ================================================================
    // GET PATH FROM ROOT TO A NODE
    // Used by LCA Approach 1.
    //
    // path stores the route from root to target node n.
    // If current path is wrong, remove the last node while backtracking.
    // ================================================================
    static boolean getPath(Node root, int n, ArrayList<Node> path) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (root.data == n) {
            return true;
        }

        boolean foundLeft = getPath(root.left, n, path);
        boolean foundRight = getPath(root.right, n, path);

        if (foundLeft || foundRight) {
            return true;
        }

        // Current node is not part of the final path, so remove it.
        path.remove(path.size() - 1);
        return false;
    }

    // ================================================================
    // LOWEST COMMON ANCESTOR - APPROACH 1
    // Build root-to-node paths for both nodes.
    // The last same node in both paths is the LCA.
    //
    // Time: O(n)
    // Space: O(n)
    // ================================================================
    static Node lcaApproach1(Node root, int n1, int n2) {
        ArrayList<Node> path1 = new ArrayList<>();
        ArrayList<Node> path2 = new ArrayList<>();

        boolean found1 = getPath(root, n1, path1);
        boolean found2 = getPath(root, n2, path2);

        if (!found1 || !found2) {
            return null;
        }

        int i = 0;
        for (; i < path1.size() && i < path2.size(); i++) {
            if (path1.get(i) != path2.get(i)) {
                break;
            }
        }

        // i stopped at the first different node, so answer is i - 1.
        return path1.get(i - 1);
    }

    // ================================================================
    // LOWEST COMMON ANCESTOR - APPROACH 2
    // More direct recursive method.
    //
    // Cases:
    // - If root is null, return null.
    // - If root is one of the targets, return root.
    // - Ask left subtree and right subtree.
    // - If both sides return non-null, current root is LCA.
    // - Otherwise return whichever side is non-null.
    //
    // Time: O(n)
    // Space: O(h)
    // ================================================================
    static Node lcaApproach2(Node root, int n1, int n2) {
        if (root == null || root.data == n1 || root.data == n2) {
            return root;
        }

        Node leftLca = lcaApproach2(root.left, n1, n2);
        Node rightLca = lcaApproach2(root.right, n1, n2);

        if (rightLca == null) {
            return leftLca;
        }

        if (leftLca == null) {
            return rightLca;
        }

        return root;
    }

    // ================================================================
    // DISTANCE FROM ROOT TO TARGET NODE
    // Returns:
    // distance in edges if target is found
    // -1 if target is not found
    //
    // Used for minimum distance between two nodes.
    // ================================================================
    static int lcaDistance(Node root, int n) {
        if (root == null) {
            return -1;
        }

        if (root.data == n) {
            return 0;
        }

        int leftDistance = lcaDistance(root.left, n);
        int rightDistance = lcaDistance(root.right, n);

        if (leftDistance == -1 && rightDistance == -1) {
            return -1;
        } else if (leftDistance == -1) {
            return rightDistance + 1;
        } else {
            return leftDistance + 1;
        }
    }

    // ================================================================
    // MINIMUM DISTANCE BETWEEN 2 NODES
    // Find LCA first, then:
    //
    // minDist(n1, n2) = distance(lca, n1) + distance(lca, n2)
    //
    // Distance here is counted in edges.
    // Time: O(n)
    // ================================================================
    static int minDistance(Node root, int n1, int n2) {
        Node lca = lcaApproach2(root, n1, n2);

        if (lca == null) {
            return -1;
        }

        int dist1 = lcaDistance(lca, n1);
        int dist2 = lcaDistance(lca, n2);

        if (dist1 == -1 || dist2 == -1) {
            return -1;
        }

        return dist1 + dist2;
    }

    // ================================================================
    // KTH ANCESTOR OF A NODE
    // Prints the kth ancestor of node n.
    //
    // Return value means distance from current root to target node:
    // -1 -> target not found in this subtree
    //  0 -> current node is target
    //  x -> target is x edges below current node
    //
    // When maxDistance + 1 == k, current root is kth ancestor.
    // Time: O(n)
    // ================================================================
    static int kthAncestor(Node root, int n, int k) {
        if (root == null) {
            return -1;
        }

        if (root.data == n) {
            return 0;
        }

        int leftDistance = kthAncestor(root.left, n, k);
        int rightDistance = kthAncestor(root.right, n, k);

        if (leftDistance == -1 && rightDistance == -1) {
            return -1;
        }

        int maxDistance = Math.max(leftDistance, rightDistance);

        if (maxDistance + 1 == k) {
            System.out.println(root.data);
        }

        return maxDistance + 1;
    }

    // ================================================================
    // TRANSFORM TO SUM TREE
    // Converts each node's data to the sum of all nodes in its left
    // and right subtrees from the original tree.
    //
    // Leaves become 0 because they have no children.
    //
    // This function returns the old value of the subtree root plus all
    // old values below it, so the parent can use that total.
    //
    // Time: O(n)
    // ================================================================
    static int transformToSumTree(Node root) {
        if (root == null) {
            return 0;
        }

        int leftChildSum = transformToSumTree(root.left);
        int rightChildSum = transformToSumTree(root.right);

        int oldData = root.data;

        int newLeftData = root.left == null ? 0 : root.left.data;
        int newRightData = root.right == null ? 0 : root.right.data;

        root.data = newLeftData + leftChildSum + newRightData + rightChildSum;

        return oldData;
    }

    // ================================================================
    // PREORDER TRAVERSAL
    // Used to print the tree before and after sum tree transformation.
    // ================================================================
    static void preorder(Node root) {
        if (root == null) {
            return;
        }

        System.out.print(root.data + " ");
        preorder(root.left);
        preorder(root.right);
    }

    // ================================================================
    // SAMPLE TREE
    //
    //          1
    //        /   \
    //       2     3
    //      / \   / \
    //     4   5 6   7
    // ================================================================
    static Node createSampleTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        return root;
    }

    // ================================================================
    // MAIN: sample run for Binary Trees Part 3
    // ================================================================
    public static void main(String[] args) {
        Node root = createSampleTree();

        System.out.print("Kth Level k=3: ");
        printKLevel(root, 1, 3); // 4 5 6 7
        System.out.println();

        Node lca1 = lcaApproach1(root, 4, 5);
        System.out.println("LCA Approach 1 of 4 and 5: " + lca1.data);

        Node lca2 = lcaApproach2(root, 4, 6);
        System.out.println("LCA Approach 2 of 4 and 6: " + lca2.data);

        System.out.println("Min Distance between 4 and 6: " + minDistance(root, 4, 6));

        System.out.print("2nd Ancestor of 5: ");
        kthAncestor(root, 5, 2); // 1

        System.out.print("Original tree preorder: ");
        preorder(root);
        System.out.println();

        transformToSumTree(root);

        System.out.print("Sum tree preorder:      ");
        preorder(root);
        System.out.println();
    }
}
