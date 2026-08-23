import java.util.*;

// ================================================================
// TOPIC: Queues
// Queue = FIFO (First In, First Out). Like a line at a counter.
// add/enqueue → rear, remove/dequeue → front, peek → front
// ================================================================


// ================================================================
// 1. QUEUE USING ARRAY (naive)
// add → rear++, O(1). remove → shift everything left, O(n).
// The shifting is exactly what Circular Queue below avoids.
// ================================================================
class QueueArr {
    static int[] arr;
    static int size;
    static int rear = -1;

    QueueArr(int n) {
        arr = new int[n];
        size = n;
    }

    static boolean isEmpty() { return rear == -1; }
    static boolean isFull() { return rear == size - 1; }

    static void add(int data) {
        if (isFull()) { System.out.println("queue is full"); return; }
        rear++;
        arr[rear] = data;
    }

    //remove
    static int remove() {
        if (isEmpty()) { System.out.println("empty queue"); return -1; }
        int front = arr[0];
        for (int i = 0; i < rear; i++) {
            arr[i] = arr[i + 1];   // shift left to fill the gap
        }
        rear--;
        return front;
    }

    static int peek() {
        return isEmpty() ? -1 : arr[0];
    }
}


// ================================================================
// 2. CIRCULAR QUEUE USING ARRAY
// front/rear wrap around with %size, so remove is O(1) — no shifting.
// front == -1 means empty. Full when the next slot would hit front.
// ================================================================
class QueueCircular {
    static int[] arr;
    static int size;
    static int rear = -1;
    static int front = -1;

    QueueCircular(int n) {
        arr = new int[n];
        size = n;
    }

    static boolean isEmpty() { return rear == -1 && front == -1; }
    static boolean isFull() { return (rear + 1) % size == front; }

    //add
    static void add(int data) {
        if (isFull()) { System.out.println("queue is full"); return; }
        if (front == -1) front = 0;     // first element
        rear = (rear + 1) % size;       // wrap around
        arr[rear] = data;
    }

    //remove
    static int remove() {
        if (isEmpty()) { System.out.println("empty queue"); return -1; }
        int data = arr[front];
        if (front == rear) {             // last element removed
            front = rear = -1;
        } else {
            front = (front + 1) % size;
        }
        return data;
    }
}


// ================================================================
// 3. QUEUE USING LINKED LIST
// head = front, tail = rear. Add at tail, remove from head — both O(1).
// ================================================================
class QueueLL {
    static class Node {
        int data;
        Node next;
        Node(int data) { this.data = data; }
    }

    static Node head = null;
    static Node tail = null;

    static boolean isEmpty() { return head == null && tail == null; }

    //add
    static void add(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) { head = tail = newNode; return; }
        tail.next = newNode;
        tail = newNode;
    }

    //remove
    static int remove() {
        if (isEmpty()) { System.out.println("empty queue"); return -1; }
        int front = head.data;
        if (tail == head) {          // single element left
            tail = head = null;
        } else {
            head = head.next;
        }
        return front;
    }
}


// ================================================================
// 4. QUEUE USING TWO STACKS
// add is costly: drain s1 into s2, push new data, drain s2 back —
// this keeps the oldest element on top of s1, so remove is just pop().
// add: O(n)   remove: O(1)
// ================================================================
class QueueTwoStacks {
    static Stack<Integer> s1 = new Stack<>();
    static Stack<Integer> s2 = new Stack<>();

    static boolean isEmpty() { return s1.isEmpty(); }

    //add
    static void add(int data) {
        while (!s1.isEmpty()) s2.push(s1.pop());
        s1.push(data);
        while (!s2.isEmpty()) s1.push(s2.pop());
    }

    //remove
    static int remove() {
        if (isEmpty()) { System.out.println("queue empty"); return -1; }
        return s1.pop();
    }
}


// ================================================================
// 5. STACK USING TWO QUEUES
// push is cheap: just add to whichever queue is non-empty — O(1).
// pop is costly: drain that queue into the other, the LAST value
// removed is the top of stack (most recently pushed) — O(n).
// ================================================================
class StackTwoQueues {
    static Queue<Integer> q1 = new LinkedList<>();
    static Queue<Integer> q2 = new LinkedList<>();

    static boolean isEmpty() { return q1.isEmpty() && q2.isEmpty(); }

    //push
    static void push(int data) {
        if (!q1.isEmpty()) q1.add(data);
        else q2.add(data);
    }

    //pop
    static int pop() {
        if (isEmpty()) { System.out.println("empty stack"); return -1; }
        int top = -1;
        if (!q1.isEmpty()) {
            while (!q1.isEmpty()) {
                top = q1.remove();
                if (q1.isEmpty()) break;   // last one out = top, keep it out
                q2.add(top);
            }
        } else {
            while (!q2.isEmpty()) {
                top = q2.remove();
                if (q2.isEmpty()) break;
                q1.add(top);
            }
        }
        return top;
    }
}


// ================================================================
// 6 & 7. STACK / QUEUE USING DEQUE (java.util.Deque)
// Deque can add/remove from both ends, so it can back either ADT.
// Stack: end of deque = top.   Queue: front = head, rear = tail.
// ================================================================
class StackDeque {
    Deque<Integer> deque = new LinkedList<>();
    void push(int data) { deque.addLast(data); }
    int pop() { return deque.removeLast(); }
    int peek() { return deque.getLast(); }
}

class QueueDeque {
    Deque<Integer> deque = new LinkedList<>();
    void add(int data) { deque.addLast(data); }
    int remove() { return deque.removeFirst(); }
    int peek() { return deque.getFirst(); }
}


// ================================================================
// MAIN CLASS — problems using java.util.Queue / Deque directly
// ================================================================
public class code {

    // ================================================================
    // 8. FIRST NON-REPEATING LETTER IN A STREAM
    // Queue holds candidates not yet confirmed repeating; freq[] counts
    // occurrences so far. Drop front of queue while it has repeated.
    // Time: O(n)  Space: O(1) — 26 letters
    // ================================================================
    static void printNonRepeating(String str) {
        int[] freq = new int[26];   // 'a' - 'z'
        Queue<Character> q = new LinkedList<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            q.add(ch);
            freq[ch - 'a']++;

            while (!q.isEmpty() && freq[q.peek() - 'a'] > 1) {
                q.remove();   // front is now a repeat, drop it
            }

            // NOTE: must use if-else, not a ternary — mixing int (-1) and
            // Character in one ternary auto-promotes to int, printing char codes.
            if (q.isEmpty()) System.out.print(-1 + " ");
            else System.out.print(q.peek() + " ");
        }
        System.out.println();
    }


    // ================================================================
    // 9. INTERLEAVE TWO HALVES OF A QUEUE
    // Move first half out. Then repeatedly: push one from firstHalf,
    // then rotate one from the (already-shifted) second half to the rear.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static void interLeave(Queue<Integer> q) {
        Queue<Integer> firstHalf = new LinkedList<>();
        int size = q.size();

        for (int i = 0; i < size / 2; i++) {
            firstHalf.add(q.remove());
        }

        while (!firstHalf.isEmpty()) {
            q.add(firstHalf.remove());   // element from first half
            q.add(q.remove());           // element from second half
        }
    }


    // ================================================================
    // 10. QUEUE REVERSAL
    // Dump everything into a stack (LIFO flips the order), dump back.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static void reverse(Queue<Integer> q) {
        Stack<Integer> s = new Stack<>();
        while (!q.isEmpty()) s.push(q.remove());
        while (!s.isEmpty()) q.add(s.pop());
    }


    // ================================================================
    // MAIN
    // ================================================================
    public static void main(String[] args) {

        // ----- 1. Queue using Array (naive) -----
        System.out.println("===== Queue using Array =====");
        QueueArr qa = new QueueArr(5);
        qa.add(1); qa.add(2); qa.add(3);
        System.out.println("remove: " + qa.remove());   // 1
        System.out.println("peek:   " + qa.peek());      // 2
        System.out.println();


        // ----- 2. Circular Queue using Array -----
        System.out.println("===== Circular Queue using Array =====");
        QueueCircular qc = new QueueCircular(3);
        qc.add(1); qc.add(2); qc.add(3);
        System.out.println("remove: " + qc.remove());   // 1
        qc.add(4);                                        // wraps to index 0
        System.out.println("remove: " + qc.remove());   // 2
        System.out.println("remove: " + qc.remove());   // 3
        System.out.println("remove: " + qc.remove());   // 4
        System.out.println();


        // ----- 3. Queue using Linked List -----
        System.out.println("===== Queue using Linked List =====");
        QueueLL.add(1); QueueLL.add(2); QueueLL.add(3);
        System.out.println("remove: " + QueueLL.remove());   // 1
        System.out.println("remove: " + QueueLL.remove());   // 2
        System.out.println();


        // ----- 4. Queue using Collections Framework (JCF) -----
        System.out.println("===== Queue using JCF =====");
        Queue<Integer> jq = new LinkedList<>();
        jq.add(1); jq.add(2); jq.add(3);
        System.out.println("remove: " + jq.remove());   // 1
        System.out.println("peek:   " + jq.peek());      // 2
        System.out.println();


        // ----- 5. Queue using Two Stacks -----
        System.out.println("===== Queue using Two Stacks =====");
        QueueTwoStacks.add(1); QueueTwoStacks.add(2); QueueTwoStacks.add(3);
        System.out.println("remove: " + QueueTwoStacks.remove());   // 1
        System.out.println("remove: " + QueueTwoStacks.remove());   // 2
        System.out.println();


        // ----- 6. Stack using Two Queues -----
        System.out.println("===== Stack using Two Queues =====");
        StackTwoQueues.push(1); StackTwoQueues.push(2); StackTwoQueues.push(3);
        System.out.println("pop: " + StackTwoQueues.pop());   // 3
        System.out.println("pop: " + StackTwoQueues.pop());   // 2
        System.out.println();


        // ----- 7. First Non-Repeating Letter -----
        System.out.println("===== First Non-Repeating Letter =====");
        System.out.print("aabccxb -> ");
        printNonRepeating("aabccxb");   // a a b b b b x
        System.out.println();


        // ----- 8. Interleave Two Halves of a Queue -----
        System.out.println("===== Interleave Two Halves =====");
        Queue<Integer> qi = new LinkedList<>(List.of(1, 2, 3, 4, 5, 6));
        System.out.println("before: " + qi);
        interLeave(qi);
        System.out.println("after:  " + qi);   // [1, 4, 2, 5, 3, 6]
        System.out.println();


        // ----- 9. Queue Reversal -----
        System.out.println("===== Queue Reversal =====");
        Queue<Integer> qr = new LinkedList<>(List.of(1, 2, 3, 4));
        System.out.println("before: " + qr);
        reverse(qr);
        System.out.println("after:  " + qr);   // [4, 3, 2, 1]
        System.out.println();


        // ----- 10. Deque (JCF) -----
        System.out.println("===== Deque (JCF) =====");
        Deque<Integer> deque = new LinkedList<>();
        deque.addFirst(1);   // [1]
        deque.addFirst(2);   // [2, 1]
        deque.addLast(3);    // [2, 1, 3]
        System.out.println(deque);
        deque.removeFirst();
        System.out.println(deque);   // [1, 3]
        System.out.println();


        // ----- 11. Stack using Deque -----
        System.out.println("===== Stack using Deque =====");
        StackDeque sd = new StackDeque();
        sd.push(1); sd.push(2); sd.push(3);
        System.out.println("pop:  " + sd.pop());    // 3
        System.out.println("peek: " + sd.peek());   // 2
        System.out.println();


        // ----- 12. Queue using Deque -----
        System.out.println("===== Queue using Deque =====");
        QueueDeque qd = new QueueDeque();
        qd.add(1); qd.add(2); qd.add(3);
        System.out.println("remove: " + qd.remove());   // 1
        System.out.println("peek:   " + qd.peek());      // 2
    }
}
