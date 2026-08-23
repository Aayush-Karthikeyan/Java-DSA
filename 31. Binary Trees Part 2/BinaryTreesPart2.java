import java.util.*;

// ================================================================
// TOPIC: Binary Trees Part 2
// Covers:
// 1. Diameter of a tree - Approach 1: O(n^2)
// 2. Diameter of a tree - Approach 2: O(n)
// 3. Subtree of another tree
// 4. Top view of a tree
// ================================================================

public class BinaryTreesPart2 {

    // ================================================================
    // NODE CLASS
    // Same basic tree node from Binary Trees Part 1.
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
    // HELPER CLASS FOR DIAMETER APPROACH 2
    // diam = diameter of this subtree
    // ht   = height of this subtree
    //
    // Returning both together avoids recalculating height again and again.
    // ================================================================
    static class DiameterInfo {
        int diam;
        int ht;

        DiameterInfo(int diam, int ht) {
            this.diam = diam;
            this.ht = ht;
        }
    }

    // ================================================================
    // HELPER CLASS FOR TOP VIEW
    // node = current tree node
    // hd   = horizontal distance from root
    //
    // root hd = 0
    // left child hd = parent hd - 1
    // right child hd = parent hd + 1
    // ================================================================
    static class TopViewInfo {
        Node node;
        int hd;

        TopViewInfo(Node node, int hd) {
            this.node = node;
            this.hd = hd;
        }
    }

    // ================================================================
    // HEIGHT OF A TREE
    // Height = number of nodes in the longest path from root to leaf.
    // Used by diameter approach 1.
    // Time: O(n)
    // ================================================================
    static int height(Node root) {
        if (root == null) {
            return 0;
        }

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    // ================================================================
    // DIAMETER OF A TREE - APPROACH 1
    // Diameter = number of nodes on the longest path between any 2 nodes.
    //
    // For every root, there are 3 possibilities:
    // 1. Diameter lies completely in left subtree.
    // 2. Diameter lies completely in right subtree.
    // 3. Diameter passes through current root.
    //
    // Time: O(n^2), because height() is recalculated for many nodes.
    // ================================================================
    static int diameterApproach1(Node root) {
        if (root == null) {
            return 0;
        }

        int leftDiam = diameterApproach1(root.left);
        int leftHeight = height(root.left);

        int rightDiam = diameterApproach1(root.right);
        int rightHeight = height(root.right);

        int selfDiam = leftHeight + rightHeight + 1;

        return Math.max(selfDiam, Math.max(leftDiam, rightDiam));
    }

    // ================================================================
    // DIAMETER OF A TREE - APPROACH 2
    // Returns both diameter and height in one recursion.
    //
    // This removes the repeated height() calls from Approach 1.
    // Time: O(n)
    // ================================================================
    static DiameterInfo diameterApproach2(Node root) {
        if (root == null) {
            return new DiameterInfo(0, 0);
        }

        DiameterInfo leftInfo = diameterApproach2(root.left);
        DiameterInfo rightInfo = diameterApproach2(root.right);

        int diam = Math.max(
                Math.max(leftInfo.diam, rightInfo.diam),
                leftInfo.ht + rightInfo.ht + 1
        );

        int ht = Math.max(leftInfo.ht, rightInfo.ht) + 1;

        return new DiameterInfo(diam, ht);
    }

    // ================================================================
    // CHECK IF TWO TREES ARE IDENTICAL
    // Used by isSubtree().
    //
    // Two trees are identical only if:
    // - both roots are null, OR
    // - both roots are non-null,
    // - root data matches,
    // - left subtrees match,
    // - right subtrees match.
    // ================================================================
    static boolean isIdentical(Node node, Node subRoot) {
        if (node == null && subRoot == null) {
            return true;
        } else if (node == null || subRoot == null || node.data != subRoot.data) {
            return false;
        }

        if (!isIdentical(node.left, subRoot.left)) {
            return false;
        }

        if (!isIdentical(node.right, subRoot.right)) {
            return false;
        }

        return true;
    }

    // ================================================================
    // SUBTREE OF ANOTHER TREE
    // Checks whether subRoot exists inside root with the same structure
    // and same values.
    //
    // Idea:
    // 1. Traverse the main tree.
    // 2. When data matches subRoot.data, check if both trees are identical.
    // 3. Otherwise search in left and right subtree.
    //
    // Time: O(n * m) in worst case.
    // ================================================================
    static boolean isSubtree(Node root, Node subRoot) {
        if (subRoot == null) {
            return true;
        }

        if (root == null) {
            return false;
        }

        if (root.data == subRoot.data) {
            if (isIdentical(root, subRoot)) {
                return true;
            }
        }

        return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
    }

    // ================================================================
    // TOP VIEW OF A TREE
    // Top view = nodes visible when looking at the tree from above.
    //
    // Use horizontal distance:
    // root = 0
    // left child = hd - 1
    // right child = hd + 1
    //
    // Use level order traversal. The first node seen at each horizontal
    // distance is part of the top view.
    // Time: O(n)
    // ================================================================
    static ArrayList<Integer> topView(Node root) {
        ArrayList<Integer> answer = new ArrayList<>();

        if (root == null) {
            return answer;
        }

        Queue<TopViewInfo> q = new LinkedList<>();
        HashMap<Integer, Node> map = new HashMap<>();

        int min = 0;
        int max = 0;

        q.add(new TopViewInfo(root, 0));

        while (!q.isEmpty()) {
            TopViewInfo curr = q.remove();

            // First time this horizontal distance appears, store it.
            // Because BFS goes level by level, first node is the topmost one.
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
        }

        for (int i = min; i <= max; i++) {
            answer.add(map.get(i).data);
        }

        return answer;
    }

    // ================================================================
    // SAMPLE TREE USED IN MAIN
    //
    //          1
    //        /   \
    //       2     3
    //      / \     \
    //     4   5     6
    //
    // Diameter = 5 nodes: 4 -> 2 -> 1 -> 3 -> 6
    // Top view = 4 2 1 3 6
    // ================================================================
    static Node createMainTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.right = new Node(6);
        return root;
    }

    // Subtree:
    //       2
    //      / \
    //     4   5
    static Node createSubtree() {
        Node subRoot = new Node(2);
        subRoot.left = new Node(4);
        subRoot.right = new Node(5);
        return subRoot;
    }

    // ================================================================
    // MAIN: sample run for Binary Trees Part 2
    // ================================================================
    public static void main(String[] args) {
        Node root = createMainTree();
        Node subRoot = createSubtree();

        System.out.println("Diameter Approach 1: " + diameterApproach1(root));

        DiameterInfo info = diameterApproach2(root);
        System.out.println("Diameter Approach 2: " + info.diam);
        System.out.println("Height from Approach 2: " + info.ht);

        System.out.println("Is Subtree: " + isSubtree(root, subRoot));

        System.out.println("Top View: " + topView(root));
    }
}
