import java.util.LinkedList;

// ================================================================
// TOPIC: Linked Lists Part 2
// Covers: Cycle detection/removal, Merge Sort, Zig-Zag,
//         Doubly LL, Reverse DLL, Circular LL, Java Collections LL
// ================================================================


// ===== NODE (Singly Linked List) =====
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}


// ===== SINGLY LINKED LIST =====
class SinglyLL {
    Node head;

    // helper: add to end
    public void addLast(int data) {
        Node newNode = new Node(data);
        if (head == null) { head = newNode; return; }
        Node curr = head;
        while (curr.next != null) curr = curr.next;
        curr.next = newNode;
    }

    // helper: print
    public void print() {
        Node curr = head;
        while (curr != null) {
            System.out.print(curr.data + " -> ");
            curr = curr.next;
        }
        System.out.println("null");
    }


    // ===== DETECT CYCLE — Floyd's Algorithm =====
    // slow moves +1, fast moves +2.
    // If there's a cycle, fast will "lap" slow and they'll meet inside the loop.
    // If no cycle, fast reaches null first.
    // Time: O(n)  Space: O(1)
    public boolean isCycle() {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;         // +1 step
            fast = fast.next.next;    // +2 steps
            if (slow == fast) return true;   // they met → cycle exists
        }
        return false;   // fast hit null → no cycle
    }


    // ===== REMOVE CYCLE =====
    // Step 1: Detect cycle (find where slow and fast meet).
    // Step 2: Reset slow to head. Move slow +1 and fast +1 together.
    //         Also track prev (the node just before fast).
    //         When slow == fast, they're at the START of the cycle.
    //         prev.next = null → removes the cycle.
    //
    // Why this works (math): the distance from head to cycle-start
    // equals the distance from the meeting point to cycle-start.
    // Time: O(n)  Space: O(1)
    public void removeCycle() {
        Node slow = head;
        Node fast = head;

        // step 1: detect — find meeting point
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;   // meeting point found
        }

        if (fast == null || fast.next == null) return;   // no cycle

        // step 2: find start of cycle
        slow = head;                // reset slow to head
        Node prev = null;           // will track node just before fast

        while (slow != fast) {
            prev = fast;            // keep track of node before fast
            slow = slow.next;
            fast = fast.next;       // now both move +1
        }
        // slow == fast == start of cycle
        prev.next = null;           // cut the cycle: last node's next = null
    }


    // ===== HELPER: GET MIDDLE NODE (for merge sort) =====
    // slow starts at head, fast starts at head.next.
    // This ensures for even-length lists, mid is the LEFT-middle.
    // Time: O(n)
    private Node getMid(Node head) {
        Node slow = head;
        Node fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;   // slow is at the middle
    }


    // ===== HELPER: MERGE TWO SORTED LINKED LISTS =====
    // Use a dummy node to simplify the logic (avoids special-casing the head).
    // Compare head1 and head2, attach the smaller one, advance that pointer.
    // Time: O(n+m)
    private Node merge(Node head1, Node head2) {
        Node dummy = new Node(-1);   // dummy starting node
        Node temp = dummy;           // temp walks along the merged list

        while (head1 != null && head2 != null) {
            if (head1.data <= head2.data) {
                temp.next = head1;   // attach head1
                head1 = head1.next;  // advance head1
            } else {
                temp.next = head2;   // attach head2
                head2 = head2.next;  // advance head2
            }
            temp = temp.next;        // move temp forward
        }
        // attach any remaining nodes
        if (head1 != null) temp.next = head1;
        if (head2 != null) temp.next = head2;

        return dummy.next;   // actual merged list starts after dummy
    }


    // ===== MERGE SORT ON LINKED LIST =====
    // Divide: find middle, split list into left and right halves.
    // Conquer: recursively sort each half.
    // Combine: merge the two sorted halves.
    // Time: O(n log n)  Space: O(log n) call stack
    public Node mergeSort(Node head) {
        if (head == null || head.next == null) return head;   // base case: 0 or 1 node

        // find mid and split
        Node mid = getMid(head);
        Node rightHead = mid.next;
        mid.next = null;             // cut the list in half

        // sort each half
        Node newLeft  = mergeSort(head);
        Node newRight = mergeSort(rightHead);

        // merge sorted halves
        return merge(newLeft, newRight);
    }


    // ===== ZIG-ZAG LINKED LIST =====
    // Rearrange so elements alternate: 1st, last, 2nd, 2nd-last, ...
    // e.g. [1,2,3,4,5] → [1,5,2,4,3]
    //
    // Steps:
    //   1. Find middle (split point).
    //   2. Reverse the 2nd half.
    //   3. Alternate-merge: pick 1 from left, 1 from right, repeat.
    // Time: O(n)  Space: O(1)
    public void zigZag() {
        // step 1: find mid (fast starts at head.next for cleaner split)
        Node slow = head;
        Node fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        Node mid = slow;

        // step 2: reverse 2nd half
        Node curr = mid.next;
        mid.next = null;           // cut list at mid
        Node prev = null;
        Node next;
        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        // now prev = head of reversed 2nd half

        Node left  = head;         // pointer into 1st half
        Node right = prev;         // pointer into reversed 2nd half

        // step 3: alternate merge
        Node dummy = new Node(-1);
        Node temp = dummy;
        while (left != null && right != null) {
            temp.next = left;   left  = left.next;   temp = temp.next;
            temp.next = right;  right = right.next;  temp = temp.next;
        }
        if (left  != null) temp.next = left;
        if (right != null) temp.next = right;

        head = dummy.next;
    }
}


// ================================================================
// DOUBLY LINKED LIST
// Each node has BOTH a next pointer AND a prev pointer.
// Can be traversed forwards AND backwards.
// ================================================================

class DNode {
    int data;
    DNode prev;   // points to previous node
    DNode next;   // points to next node

    DNode(int data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}

class DoublyLL {
    DNode head;

    public void addLast(int data) {
        DNode newNode = new DNode(data);
        if (head == null) { head = newNode; return; }
        DNode curr = head;
        while (curr.next != null) curr = curr.next;
        curr.next = newNode;
        newNode.prev = curr;   // set back-pointer too
    }

    public void print() {
        DNode curr = head;
        while (curr != null) {
            System.out.print(curr.data + " <-> ");
            curr = curr.next;
        }
        System.out.println("null");
    }

    // ===== REVERSE A DOUBLY LINKED LIST =====
    // For each node: swap its next and prev pointers.
    // 3 variables, 4 steps per iteration (same structure as singly LL reverse,
    // but also flip the prev pointer → curr.prev = next instead of nothing).
    // Time: O(n)  Space: O(1)
    public void reverse() {
        DNode curr = head;
        DNode prev = null;

        while (curr != null) {
            DNode next = curr.next;   // save next
            curr.next = prev;         // flip next pointer backward
            curr.prev = next;         // flip prev pointer forward (the extra step vs singly LL)
            prev = curr;
            curr = next;
        }
        head = prev;   // prev is now the new head
    }
}


// ================================================================
// CIRCULAR LINKED LIST — Approach only (no full implementation)
// Last node's next = head  (instead of null)
// Can be singly or doubly circular.
// Traversal: stop when curr.next == head again.
// ================================================================


// ===== MAIN =====
public class code {
    public static void main(String[] args) {

        // ----- 1. Detect Cycle -----
        SinglyLL ll = new SinglyLL();
        ll.addLast(1); ll.addLast(2); ll.addLast(3); ll.addLast(4); ll.addLast(5);
        System.out.println("Has cycle (should be false): " + ll.isCycle());

        // manually create a cycle: tail → node at index 2
        Node tail = ll.head;
        Node cycleStart = ll.head.next.next;   // node with data=3
        while (tail.next != null) tail = tail.next;
        tail.next = cycleStart;                // 5 → 3 (cycle!)
        System.out.println("Has cycle (should be true):  " + ll.isCycle());


        // ----- 2. Remove Cycle -----
        ll.removeCycle();
        System.out.println("After removeCycle, has cycle: " + ll.isCycle());   // false
        ll.print();   // 1 -> 2 -> 3 -> 4 -> 5 -> null


        // ----- 3. Merge Sort -----
        SinglyLL ll2 = new SinglyLL();
        for (int v : new int[]{4, 2, 7, 1, 9, 3}) ll2.addLast(v);
        System.out.print("Before merge sort: ");
        ll2.print();
        ll2.head = ll2.mergeSort(ll2.head);   // mergeSort returns new head
        System.out.print("After  merge sort: ");
        ll2.print();


        // ----- 4. Zig-Zag -----
        SinglyLL ll3 = new SinglyLL();
        for (int v : new int[]{1, 2, 3, 4, 5}) ll3.addLast(v);
        System.out.print("Before zig-zag: ");
        ll3.print();
        ll3.zigZag();
        System.out.print("After  zig-zag: ");
        ll3.print();   // 1 -> 5 -> 2 -> 4 -> 3 -> null


        // ----- 5. Doubly Linked List -----
        DoublyLL dll = new DoublyLL();
        for (int v : new int[]{1, 2, 3, 4, 5}) dll.addLast(v);
        System.out.print("DLL:          ");
        dll.print();
        dll.reverse();
        System.out.print("DLL reversed: ");
        dll.print();


        // ----- 6. Java Collections Framework — LinkedList -----
        // java.util.LinkedList implements both List and Deque (double-ended queue).
        // Internally it IS a doubly linked list — no need to write your own for interviews.
        LinkedList<Integer> jll = new LinkedList<>();
        jll.addFirst(1);   // add to front
        jll.addLast(2);    // add to back
        jll.addLast(3);
        System.out.println("Java util LinkedList: " + jll);       // [1, 2, 3]
        System.out.println("peekFirst: " + jll.peekFirst());      // 1 (look without removing)
        System.out.println("peekLast:  " + jll.peekLast());       // 3
        jll.removeFirst();
        System.out.println("After removeFirst: " + jll);          // [2, 3]


        // ----- 7. Circular LL — Concept -----
        // Create: tail.next = head (instead of null)
        Node cHead = new Node(1);
        Node n2    = new Node(2);
        Node n3    = new Node(3);
        cHead.next = n2;
        n2.next    = n3;
        n3.next    = cHead;   // circular: tail points back to head

        // Traverse (stop when we loop back to head)
        System.out.print("Circular LL: ");
        Node curr = cHead;
        do {
            System.out.print(curr.data + " -> ");
            curr = curr.next;
        } while (curr != cHead);
        System.out.println("(back to head)");
    }
}
