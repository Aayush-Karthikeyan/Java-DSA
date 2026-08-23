import java.util.*;

// ================================================================
// TOPIC: Binary Trees Part 1
// A binary tree is a tree where each node can have at most 2 children:
// left child and right child.
// ================================================================

public class BinaryTreesPart1 {

    // ================================================================
    // NODE CLASS
    // Each Node stores:
    // data  -> value of the node
    // left  -> reference to left child
    // right -> reference to right child
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
    // BINARY TREE CLASS
    // Contains methods to build and process a binary tree.
    // ================================================================
    static class BinaryTree {
        int idx = -1;

        // Public wrapper resets idx, so buildTree can safely be called again.
        Node buildTree(int[] nodes) {
            idx = -1;
            return buildTreePreorder(nodes);
        }

        // ================================================================
        // BUILD TREE USING PREORDER SEQUENCE
        // Preorder order: root, left subtree, right subtree
        // Here -1 means null/no node.
        //
        // Example:
        // nodes = {1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1}
        //
        // Tree:
        //          1
        //        /   \
        //       2     3
        //      / \     \
        //     4   5     6
        // ================================================================
        private Node buildTreePreorder(int[] nodes) {
            idx++;

            // Base case: -1 tells us this child is null.
            if (nodes[idx] == -1) {
                return null;
            }

            // First value is root, then recursively build left and right.
            Node newNode = new Node(nodes[idx]);
            newNode.left = buildTreePreorder(nodes);
            newNode.right = buildTreePreorder(nodes);

            return newNode;
        }

        // ================================================================
        // PREORDER TRAVERSAL
        // Order: root, left, right
        // Useful when you want to process parent before children.
        // Time: O(n)
        // ================================================================
        void preorder(Node root) {
            if (root == null) {
                return;
            }

            System.out.print(root.data + " ");
            preorder(root.left);
            preorder(root.right);
        }

        // ================================================================
        // INORDER TRAVERSAL
        // Order: left, root, right
        // For a Binary Search Tree, inorder prints sorted values.
        // Time: O(n)
        // ================================================================
        void inorder(Node root) {
            if (root == null) {
                return;
            }

            inorder(root.left);
            System.out.print(root.data + " ");
            inorder(root.right);
        }

        // ================================================================
        // POSTORDER TRAVERSAL
        // Order: left, right, root
        // Useful when children must be processed before parent.
        // Time: O(n)
        // ================================================================
        void postorder(Node root) {
            if (root == null) {
                return;
            }

            postorder(root.left);
            postorder(root.right);
            System.out.print(root.data + " ");
        }

        // ================================================================
        // LEVEL ORDER TRAVERSAL
        // Prints tree level by level using a queue.
        // null is used as a marker to know when one level ends.
        // Time: O(n), Space: O(n)
        // ================================================================
        void levelOrder(Node root) {
            if (root == null) {
                return;
            }

            Queue<Node> q = new LinkedList<>();
            q.add(root);
            q.add(null); // level separator

            while (!q.isEmpty()) {
                Node currNode = q.remove();

                if (currNode == null) {
                    System.out.println();

                    // If queue is empty after removing separator, traversal is done.
                    if (q.isEmpty()) {
                        break;
                    } else {
                        q.add(null); // separator for next level
                    }
                } else {
                    System.out.print(currNode.data + " ");

                    if (currNode.left != null) {
                        q.add(currNode.left);
                    }

                    if (currNode.right != null) {
                        q.add(currNode.right);
                    }
                }
            }
        }

        // ================================================================
        // HEIGHT OF A TREE
        // Height = number of nodes in the longest path from root to leaf.
        //
        // Formula:
        // height(root) = max(height(left), height(right)) + 1
        //
        // Time: O(n)
        // ================================================================
        int height(Node root) {
            if (root == null) {
                return 0;
            }

            int leftHeight = height(root.left);
            int rightHeight = height(root.right);

            return Math.max(leftHeight, rightHeight) + 1;
        }

        // ================================================================
        // COUNT OF NODES
        // Count every node in left subtree + right subtree + current root.
        //
        // Formula:
        // count(root) = count(left) + count(right) + 1
        //
        // Time: O(n)
        // ================================================================
        int countNodes(Node root) {
            if (root == null) {
                return 0;
            }

            int leftCount = countNodes(root.left);
            int rightCount = countNodes(root.right);

            return leftCount + rightCount + 1;
        }

        // ================================================================
        // SUM OF NODES
        // Add all values in left subtree + right subtree + current root.
        //
        // Formula:
        // sum(root) = sum(left) + sum(right) + root.data
        //
        // Time: O(n)
        // ================================================================
        int sumOfNodes(Node root) {
            if (root == null) {
                return 0;
            }

            int leftSum = sumOfNodes(root.left);
            int rightSum = sumOfNodes(root.right);

            return leftSum + rightSum + root.data;
        }
    }

    // ================================================================
    // MAIN: sample run for all topics in Binary Trees Part 1
    // ================================================================
    public static void main(String[] args) {
        /*
                    1
                  /   \
                 2     3
                / \     \
               4   5     6

            Preorder with null markers:
            1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1
        */

        int[] nodes = {1, 2, 4, -1, -1, 5, -1, -1, 3, -1, 6, -1, -1};

        BinaryTree tree = new BinaryTree();
        Node root = tree.buildTree(nodes);

        System.out.println("Root node: " + root.data);

        System.out.print("Preorder:  ");
        tree.preorder(root);      // 1 2 4 5 3 6
        System.out.println();

        System.out.print("Inorder:   ");
        tree.inorder(root);       // 4 2 5 1 3 6
        System.out.println();

        System.out.print("Postorder: ");
        tree.postorder(root);     // 4 5 2 6 3 1
        System.out.println();

        System.out.println("Level order:");
        tree.levelOrder(root);

        System.out.println("Height: " + tree.height(root));          // 3
        System.out.println("Count:  " + tree.countNodes(root));      // 6
        System.out.println("Sum:    " + tree.sumOfNodes(root));      // 21
    }
}
