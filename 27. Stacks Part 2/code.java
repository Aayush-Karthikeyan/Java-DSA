import java.util.Stack;

// ================================================================
// TOPIC: Stacks Part 2
// Three classic stack problems:
//   1. Valid Parentheses      — is every bracket properly matched/closed?
//   2. Duplicate Parentheses  — is there a redundant pair of ( )?
//   3. Max Rectangular Area in Histogram — Next Smaller Element trick
// ================================================================
public class code {

    // ================================================================
    // 1. VALID PARENTHESES
    // Check if every opening bracket has a matching closing bracket,
    // in the correct order. Works ONLY on pure bracket strings
    // (any non-bracket char is treated as an unexpected "closing" char
    // and will make this return false — see notes.md).
    //
    // Approach:
    //   - opening bracket → push it
    //   - closing bracket → stack must not be empty, and top of stack
    //     must be the matching opening bracket → pop it
    //   - at the end, stack must be empty (no unmatched openings left)
    // Time: O(n)  Space: O(n)
    // ================================================================
    static boolean isValid(String str) {
        Stack<Character> s = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == '(' || ch == '{' || ch == '[') {
                //opening
                s.push(ch);
            } else {
                //closing
                if (s.isEmpty()) {
                    return false;   // closing bracket with nothing to match
                }
                if ((s.peek() == '(' && ch == ')')
                        || (s.peek() == '{' && ch == '}')
                        || (s.peek() == '[' && ch == ']')) {
                    s.pop();        // matched pair → remove opening bracket
                } else {
                    return false;   // wrong type of bracket, e.g. "(]"
                }
            }
        }

        return s.isEmpty();   // true only if every opening got closed
    }


    // ================================================================
    // 2. DUPLICATE PARENTHESES
    // Detect a redundant pair of parentheses, e.g. "((a+b))" has an
    // outer pair that wraps an already-complete expression — redundant.
    // "(a-b)" has exactly one necessary pair → not redundant.
    //
    // Approach:
    //   - push everything EXCEPT ')' onto the stack (so '(' , letters,
    //     operators all get pushed)
    //   - on ')': pop until we hit the matching '(', counting how many
    //     chars were popped in between
    //   - if count < 1 → nothing was between '(' and ')' → duplicate
    //     (e.g. "(())" inner pair, or a repeated outer wrap)
    //   - otherwise pop the matching '(' itself and keep going
    // Time: O(n)  Space: O(n)
    // ================================================================
    static boolean isDuplicate(String str) {
        Stack<Character> s = new Stack<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == ')') {
                //closing
                int count = 0;
                while (s.peek() != '(') {
                    s.pop();
                    count++;
                }
                if (count < 1) {
                    return true;    // duplicate: found "()" with nothing inside
                } else {
                    s.pop();        // remove the matching opening '('
                }
            } else {
                //opening (also pushes plain chars like letters/operators)
                s.push(ch);
            }
        }

        return false;   // went through whole string, no redundant pair found
    }


    // ================================================================
    // 3. MAXIMUM RECTANGULAR AREA IN HISTOGRAM
    // Given bar heights, find the largest rectangle that fits under the
    // histogram outline.
    //
    // Key idea: for each bar i, the widest rectangle using arr[i] as the
    // height stretches from the nearest SMALLER bar on the left (nsl[i])
    // to the nearest SMALLER bar on the right (nsr[i]), exclusive.
    //   width = nsr[i] - nsl[i] - 1
    //   area  = arr[i] * width
    // Answer = max area over all i.
    //
    // Both nsr[] and nsl[] are computed with the "Next Smaller Element"
    // stack pattern: stack holds indices of bars still "candidates"
    // because nothing smaller has been seen yet.
    // Time: O(n)  Space: O(n)
    // ================================================================
    static int maxArea(int[] arr) {
        int maxArea = 0;
        int[] nsr = new int[arr.length];   // next smaller to the right (index)
        int[] nsl = new int[arr.length];   // next smaller to the left (index)

        //Next Smaller Right — traverse right to left
        Stack<Integer> s = new Stack<>();
        for (int i = arr.length - 1; i >= 0; i--) {
            while (!s.isEmpty() && arr[s.peek()] >= arr[i]) {
                s.pop();   // bars >= arr[i] can never be "next smaller" for i
            }
            if (s.isEmpty()) {
                nsr[i] = arr.length;   // no smaller bar to the right → boundary is the end
            } else {
                nsr[i] = s.peek();
            }
            s.push(i);
        }

        //Next Smaller Left — traverse left to right
        s = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!s.isEmpty() && arr[s.peek()] >= arr[i]) {
                s.pop();
            }
            if (s.isEmpty()) {
                nsl[i] = -1;   // no smaller bar to the left → boundary is before the start
            } else {
                nsl[i] = s.peek();
            }
            s.push(i);
        }

        //Current Area : width = j-i-1 = nsr[i]-nsl[i]-1
        for (int i = 0; i < arr.length; i++) {
            int height = arr[i];
            int width = nsr[i] - nsl[i] - 1;
            int currArea = height * width;
            maxArea = Math.max(currArea, maxArea);
        }

        return maxArea;
    }


    // ================================================================
    // MAIN
    // ================================================================
    public static void main(String[] args) {

        // ----- 1. Valid Parentheses -----
        System.out.println("===== Valid Parentheses =====");
        System.out.println(isValid("{[()]}"));   // true
        System.out.println(isValid("{[(])}"));   // false — wrong order
        System.out.println(isValid("((()))"));   // true
        System.out.println(isValid("(()"));      // false — unclosed
        System.out.println();


        // ----- 2. Duplicate Parentheses -----
        System.out.println("===== Duplicate Parentheses =====");
        String str = "((a+b))";   // true  — outer ( ) is redundant
        String str2 = "(a-b)";    // false — single necessary pair
        System.out.println(isDuplicate(str));    // true
        System.out.println(isDuplicate(str2));   // false
        System.out.println();


        // ----- 3. Max Rectangular Area in Histogram -----
        System.out.println("===== Max Rectangular Area in Histogram =====");
        int[] heights = {2, 1, 5, 6, 2, 3};
        System.out.println("max area is: " + maxArea(heights));   // 10

        int[] heights2 = {6, 2, 5, 4, 5, 1, 6};
        System.out.println("max area is: " + maxArea(heights2));  // 12
    }
}
