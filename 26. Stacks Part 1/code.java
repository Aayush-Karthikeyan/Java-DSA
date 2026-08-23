import java.util.ArrayList;
import java.util.Stack;

// ================================================================
// TOPIC: Stacks Part 1
// Stack = LIFO (Last In, First Out). Like a stack of plates.
// Operations: push (add to top), pop (remove from top), peek (look at top)
// ================================================================


// ================================================================
// 1. STACK USING ARRAYLIST
// End of ArrayList = top of stack.
// push → list.add(data)        — O(1)
// pop  → remove last element   — O(1)
// peek → get last element      — O(1)
// ================================================================
class StackAL {
    static ArrayList<Integer> list = new ArrayList<>();

    public static boolean isEmpty() {
        return list.size() == 0;
    }

    //push
    public static void push(int data) {
        list.add(data);   // add to end = top of stack
    }

    //pop
    public static int pop() {
        int top = list.get(list.size() - 1);   // get last element
        list.remove(list.size() - 1);          // remove last element
        return top;
    }

    //peek
    public static int peek() {
        return list.get(list.size() - 1);      // just look, don't remove
    }
}


// ================================================================
// 2. STACK USING LINKED LIST
// Head of linked list = top of stack.
// push → add new node at head (addFirst)  — O(1)
// pop  → remove head                      — O(1)
// peek → return head.data                 — O(1)
// ================================================================
class StackLL {
    static class Node {
        int data;
        Node next;
        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    static Node head = null;   // head = top of stack

    public static boolean isEmpty() {
        return head == null;
    }

    //push
    public static void push(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
            return;
        }
        newNode.next = head;   // new node points to old top
        head = newNode;        // new node becomes the new top
    }

    //pop
    public static int pop() {
        if (isEmpty()) return -1;
        int top = head.data;   // save top value
        head = head.next;      // move head to next node
        return top;
    }

    //peek
    public static int peek() {
        if (isEmpty()) return -1;
        return head.data;
    }
}


// ================================================================
// MAIN CLASS — all problems use java.util.Stack (Collections Framework)
// java.util.Stack has: push(), pop(), peek(), isEmpty()
// ================================================================
public class code {

    // ================================================================
    // 3. PUSH AT BOTTOM OF STACK
    // Insert an element at the very bottom of a stack using recursion.
    // Idea: pop all elements out → push data at bottom → push them back.
    // Time: O(n)  Space: O(n) call stack
    // ================================================================
    static void pushAtBottom(Stack<Integer> s, int data) {
        if (s.isEmpty()) {
            s.push(data);   // base case: stack empty → just push
            return;
        }
        int top = s.pop();           // remove top
        pushAtBottom(s, data);       // recurse: push data to bottom of remaining stack
        s.push(top);                 // restore top
    }


    // ================================================================
    // 4. REVERSE A STRING USING STACK
    // Push all chars → pop them out in reverse order.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static String reverseString(String str) {
        Stack<Character> s = new Stack<>();
        int idx = 0;
        while (idx < str.length()) {
            s.push(str.charAt(idx));   // push each character
            idx++;
        }
        StringBuilder result = new StringBuilder("");
        while (!s.isEmpty()) {
            char curr = s.pop();       // pop gives chars in reverse order
            result.append(curr);
        }
        return result.toString();
    }


    // ================================================================
    // 5. REVERSE A STACK
    // Reverse the order of all elements in a stack using recursion.
    // Idea: pop top → reverse rest of stack → push top to BOTTOM.
    // Uses pushAtBottom as a helper.
    // Time: O(n²)  Space: O(n)
    // ================================================================
    static void reverseStack(Stack<Integer> s) {
        if (s.isEmpty()) return;      // base case: nothing to reverse
        int top = s.pop();            // remove top element
        reverseStack(s);              // reverse remaining stack
        pushAtBottom(s, top);         // push original top to the bottom
    }


    // ================================================================
    // 6. STOCK SPAN PROBLEM
    // For each day, find the number of consecutive days (including today)
    // where price <= today's price.
    //
    // Approach: stack stores INDICES of days with "unresolved" higher prices.
    //   - Pop all days where price <= currPrice (they are "covered" by today).
    //   - If stack is empty: all previous days had lower price → span = i+1
    //   - Else: span = i - prevHighIndex (previous day with a HIGHER price)
    //   - Push current index.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static void stockSpan(int[] stocks, int[] span) {
        Stack<Integer> s = new Stack<>();
        span[0] = 1;   // first day always has span 1
        s.push(0);     // push index 0

        for (int i = 1; i < stocks.length; i++) {
            int currPrice = stocks[i];

            // pop all days where price <= currPrice (today covers them)
            while (!s.isEmpty() && currPrice > stocks[s.peek()]) {
                s.pop();
            }

            if (s.isEmpty()) {
                span[i] = i + 1;           // no higher price exists → span is all days so far
            } else {
                int prevHigh = s.peek();   // index of nearest day with HIGHER price
                span[i] = i - prevHigh;   // days between today and that higher-price day
            }

            s.push(i);   // push today's index
        }
    }


    // ================================================================
    // 7. NEXT GREATER ELEMENT
    // For each element, find the first element to its RIGHT that is greater.
    // If no such element exists, answer is -1.
    //
    // Approach: traverse RIGHT TO LEFT. Stack stores indices of candidates.
    //   Step 1 (while): pop all elements that are <= arr[i] (they can never
    //           be the "next greater" for arr[i] since arr[i] is bigger).
    //   Step 2 (if-else): if stack empty → no greater element to right → -1
    //                     else → arr[peek()] is the next greater element
    //   Step 3: push current index for future elements to use.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static void nextGreater(int[] arr) {
        Stack<Integer> s = new Stack<>();   // stores indices
        int[] nxtGreater = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; i--) {   // right to left

            //1 while: pop elements that are not greater than arr[i]
            while (!s.isEmpty() && arr[s.peek()] <= arr[i]) {
                s.pop();
            }

            //2 if-else: assign next greater
            if (s.isEmpty()) {
                nxtGreater[i] = -1;            // no element to the right is greater
            } else {
                nxtGreater[i] = arr[s.peek()]; // peek is the next greater element's index
            }

            //3 push current index
            s.push(i);
        }

        // print result
        for (int i = 0; i < nxtGreater.length; i++) {
            System.out.print(nxtGreater[i] + " ");
        }
        System.out.println();
    }


    // ================================================================
    // MAIN
    // ================================================================
    public static void main(String[] args) {

        // ----- 1. Stack using ArrayList -----
        System.out.println("===== Stack using ArrayList =====");
        StackAL.push(1); StackAL.push(2); StackAL.push(3);
        System.out.println("peek: " + StackAL.peek());   // 3
        System.out.println("pop:  " + StackAL.pop());    // 3
        System.out.println("pop:  " + StackAL.pop());    // 2
        System.out.println();


        // ----- 2. Stack using Linked List -----
        System.out.println("===== Stack using Linked List =====");
        StackLL.push(1); StackLL.push(2); StackLL.push(3);
        System.out.println("peek: " + StackLL.peek());   // 3
        System.out.println("pop:  " + StackLL.pop());    // 3
        System.out.println("pop:  " + StackLL.pop());    // 2
        System.out.println();


        // ----- 3. Stack using Collections Framework -----
        System.out.println("===== Stack using Collections Framework =====");
        Stack<Integer> s = new Stack<>();
        s.push(1); s.push(2); s.push(3);
        System.out.println("peek: " + s.peek());   // 3
        System.out.println("pop:  " + s.pop());    // 3
        System.out.println("isEmpty: " + s.isEmpty());
        System.out.println();


        // ----- 4. Push at Bottom -----
        System.out.println("===== Push at Bottom =====");
        Stack<Integer> s2 = new Stack<>();
        s2.push(1); s2.push(2); s2.push(3);   // stack: bottom[1,2,3]top
        pushAtBottom(s2, 4);                    // should become [4,1,2,3]
        while (!s2.isEmpty()) System.out.print(s2.pop() + " ");  // prints 3 2 1 4
        System.out.println();
        System.out.println();


        // ----- 5. Reverse String -----
        System.out.println("===== Reverse String =====");
        System.out.println(reverseString("hello"));   // olleh
        System.out.println(reverseString("abcd"));    // dcba
        System.out.println();


        // ----- 6. Reverse Stack -----
        System.out.println("===== Reverse Stack =====");
        Stack<Integer> s3 = new Stack<>();
        s3.push(1); s3.push(2); s3.push(3);
        System.out.print("Before: ");
        System.out.println(s3);   // [1, 2, 3]
        reverseStack(s3);
        System.out.print("After:  ");
        while (!s3.isEmpty()) System.out.print(s3.pop() + " ");  // 1 2 3 (reversed)
        System.out.println();
        System.out.println();


        // ----- 7. Stock Span -----
        System.out.println("===== Stock Span =====");
        int[] stocks = {100, 80, 60, 70, 60, 85, 100};
        int[] span   = new int[stocks.length];
        stockSpan(stocks, span);
        System.out.print("Stocks: ");
        for (int x : stocks) System.out.print(x + " ");
        System.out.println();
        System.out.print("Span:   ");
        for (int x : span) System.out.print(x + " ");   // 1 1 1 2 1 4 6
        System.out.println();
        System.out.println();


        // ----- 8. Next Greater Element -----
        System.out.println("===== Next Greater Element =====");
        System.out.print("arr    = [6, 8, 0, 1, 3]: ");
        System.out.println();
        System.out.print("nxtGtr = ");
        nextGreater(new int[]{6, 8, 0, 1, 3});   // 8 -1 1 3 -1
    }
}
