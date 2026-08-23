// ================================================================
// TOPIC: Linked Lists Part 1
// A linked list is a chain of nodes.
// Each node holds data + a pointer (next) to the next node.
// Unlike arrays, nodes are NOT stored in a continuous memory block.
// ================================================================


// ===== NODE CLASS =====
// Every element in a linked list is a Node.
class Node {
    int data;    // the value stored
    Node next;   // pointer to the next node (null if last node)

    Node(int data) {
        this.data = data;
        this.next = null;   // by default, new node points to nothing
    }
}


// ===== LINKED LIST CLASS =====
class LinkedList {

    Node head;   // points to the FIRST node
    Node tail;   // points to the LAST node
    int size;    // tracks number of nodes

    // empty list starts with head=null, tail=null, size=0
    LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }


    // ===== ADD FIRST =====
    // New node goes BEFORE the current head.
    // Time: O(1)
    public void addFirst(int data) {
        Node newNode = new Node(data);

        if (head == null) {              // list is empty
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;         // new node points to old head
            head = newNode;              // head now points to new node
        }
        size++;
    }


    // ===== ADD LAST =====
    // New node goes AFTER the current tail.
    // Time: O(1) — because we have the tail pointer
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (head == null) {              // list is empty
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;         // old tail now points to new node
            tail = newNode;              // tail updated to new node
        }
        size++;
    }


    // ===== PRINT =====
    // Walk from head to tail using a temp pointer, print each node.
    // NEVER move head itself — you'd lose the list!
    public void print() {
        Node curr = head;               // temp pointer, starts at head
        while (curr != null) {
            System.out.print(curr.data + " -> ");
            curr = curr.next;           // move to next node
        }
        System.out.println("null");
    }


    // ===== ADD IN THE MIDDLE (at index) =====
    // Index 0 = addFirst, index = size = addLast, otherwise insert between nodes.
    // Time: O(n)
    public void addMiddle(int index, int data) {
        if (index == 0) { addFirst(data); return; }
        if (index == size) { addLast(data); return; }

        Node newNode = new Node(data);
        Node curr = head;

        // walk to the node JUST BEFORE the target index
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }
        // curr is now at index-1
        newNode.next = curr.next;       // new node points to the node that was at 'index'
        curr.next = newNode;            // previous node now points to new node
        size++;
    }


    // ===== SIZE =====
    public int size() {
        return size;
    }


    // ===== REMOVE FIRST =====
    // Remove the head node.
    // Time: O(1)
    public int removeFirst() {
        if (head == null) {
            System.out.println("List is empty");
            return -1;
        }
        int val = head.data;            // save the value to return
        head = head.next;               // head moves to the next node
        if (head == null) tail = null;  // list became empty — update tail too
        size--;
        return val;
    }


    // ===== REMOVE LAST =====
    // Remove the tail node.
    // Time: O(n) — need to reach the second-to-last node to update tail
    public int removeLast() {
        if (head == null) {
            System.out.println("List is empty");
            return -1;
        }
        if (head == tail) {             // only one node
            int val = head.data;
            head = null;
            tail = null;
            size--;
            return val;
        }

        // walk to second-to-last node
        Node curr = head;
        while (curr.next != tail) {
            curr = curr.next;
        }
        int val = tail.data;
        curr.next = null;               // second-to-last node now points to null
        tail = curr;                    // update tail
        size--;
        return val;
    }


    // ===== ITERATIVE SEARCH =====
    // Walk through the list, compare each node's data to key.
    // Returns the index (0-based) if found, -1 if not found.
    // Time: O(n)
    public int iterativeSearch(int key) {
        Node curr = head;
        int index = 0;
        while (curr != null) {
            if (curr.data == key) return index;  // found
            curr = curr.next;
            index++;
        }
        return -1;   // not found
    }


    // ===== RECURSIVE SEARCH =====
    // Public method — calls the private helper starting from head.
    public int recursiveSearch(int key) {
        return searchHelper(head, key, 0);
    }

    // Recursive helper: check current node, then recurse on rest of list.
    private int searchHelper(Node node, int key, int index) {
        if (node == null) return -1;                    // base case: end of list, not found
        if (node.data == key) return index;             // base case: found
        return searchHelper(node.next, key, index + 1); // recurse on next node
    }


    // ===== REVERSE A LINKED LIST =====
    // Reverse all the next pointers so the list goes backwards.
    // Uses 3 pointers: prev, curr, next
    // Time: O(n)
    public void reverse() {
        Node prev = null;
        Node curr = head;

        tail = head;   // old head becomes the new tail

        while (curr != null) {
            Node next = curr.next;   // save next before we overwrite it
            curr.next = prev;        // flip the pointer: curr now points BACK to prev
            prev = curr;             // move prev forward
            curr = next;             // move curr forward (using saved next)
        }
        head = prev;   // prev is now the last node we visited = new head
    }


    // ===== FIND AND REMOVE Nth NODE FROM END =====
    // e.g. in [1->2->3->4->5], N=2 → remove 4 (2nd from end)
    //
    // Trick: use TWO pointers — fast and slow — both start at head.
    // Move fast N steps ahead. Then move both together until fast reaches the end.
    // At that point, slow is right before the node to remove.
    // Time: O(n)
    public void removeNthFromEnd(int n) {
        Node fast = head;
        Node slow = head;

        // move fast n steps ahead
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }

        // if fast is null, the node to remove is the head
        if (fast == null) {
            removeFirst();
            return;
        }

        // move both until fast reaches the last node
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // slow is now just before the node to remove
        slow.next = slow.next.next;   // skip over the target node
        size--;
    }


    // ===== FIND MIDDLE NODE (helper for palindrome) =====
    // Uses slow/fast pointer trick:
    //   - slow moves 1 step at a time, fast moves 2 steps at a time
    //   - when fast reaches end, slow is at the middle
    private Node findMidNode(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;         // 1 step
            fast = fast.next.next;    // 2 steps
        }
        return slow;   // slow is now at the middle
    }


    // ===== CHECK IF LINKED LIST IS A PALINDROME =====
    // Strategy:
    //   1. Find the middle of the list.
    //   2. Reverse the second half.
    //   3. Compare left half (from head) with reversed right half.
    //   4. If all values match → palindrome.
    // Time: O(n)
    public boolean checkPalindrome() {
        if (head == null || head.next == null) return true;  // 0 or 1 node is always palindrome

        // step 1: find middle
        Node mid = findMidNode(head);

        // step 2: reverse second half (starting from mid)
        Node curr = mid;
        Node prev = null;
        while (curr != null) {
            Node next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        Node right = prev;   // right now points to the start of reversed second half

        // step 3: compare
        Node left = head;
        while (right != null) {
            if (left.data != right.data) return false;  // mismatch → not palindrome
            left = left.next;
            right = right.next;
        }
        return true;
    }
}


// ===== MAIN CLASS =====
public class code {
    public static void main(String[] args) {

        LinkedList ll = new LinkedList();

        // ----- Add First -----
        ll.addFirst(3);
        ll.addFirst(2);
        ll.addFirst(1);
        System.out.print("After addFirst 1,2,3: ");
        ll.print();   // 1 -> 2 -> 3 -> null

        // ----- Add Last -----
        ll.addLast(4);
        ll.addLast(5);
        System.out.print("After addLast 4,5:    ");
        ll.print();   // 1 -> 2 -> 3 -> 4 -> 5 -> null

        // ----- Size -----
        System.out.println("Size: " + ll.size());  // 5

        // ----- Add in Middle -----
        ll.addMiddle(2, 99);   // insert 99 at index 2
        System.out.print("After addMiddle(2,99): ");
        ll.print();   // 1 -> 2 -> 99 -> 3 -> 4 -> 5 -> null

        // ----- Remove First -----
        System.out.println("Removed first: " + ll.removeFirst());  // 1
        System.out.print("After removeFirst: ");
        ll.print();

        // ----- Remove Last -----
        System.out.println("Removed last: " + ll.removeLast());   // 5
        System.out.print("After removeLast: ");
        ll.print();

        // ----- Iterative Search -----
        System.out.println("Iterative search 99: index " + ll.iterativeSearch(99));   // found
        System.out.println("Iterative search 50: index " + ll.iterativeSearch(50));   // -1

        // ----- Recursive Search -----
        System.out.println("Recursive search 3: index " + ll.recursiveSearch(3));    // found
        System.out.println("Recursive search 50: index " + ll.recursiveSearch(50));  // -1

        // ----- Reverse -----
        System.out.print("Before reverse: ");
        ll.print();
        ll.reverse();
        System.out.print("After reverse:  ");
        ll.print();

        // ----- Remove Nth from End -----
        LinkedList ll2 = new LinkedList();
        for (int v : new int[]{1, 2, 3, 4, 5}) ll2.addLast(v);
        System.out.print("Before removeNthFromEnd(2): ");
        ll2.print();   // 1->2->3->4->5
        ll2.removeNthFromEnd(2);
        System.out.print("After  removeNthFromEnd(2): ");
        ll2.print();   // 1->2->3->5 (removed 4, 2nd from end)

        // ----- Palindrome Check -----
        LinkedList ll3 = new LinkedList();
        for (int v : new int[]{1, 2, 3, 2, 1}) ll3.addLast(v);
        System.out.print("Palindrome [1,2,3,2,1]: ");
        System.out.println(ll3.checkPalindrome());   // true

        LinkedList ll4 = new LinkedList();
        for (int v : new int[]{1, 2, 3, 4, 5}) ll4.addLast(v);
        System.out.print("Palindrome [1,2,3,4,5]: ");
        System.out.println(ll4.checkPalindrome());   // false
    }
}
