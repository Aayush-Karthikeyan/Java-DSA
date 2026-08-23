import java.util.Scanner;

public class code {

    // ===================================================================
    // TOPIC: STRINGS IN JAVA
    // Covers: basics, I/O, length, concat, charAt, palindrome,
    //         shortest path, compare, substring, largest string,
    //         immutability, StringBuilder, toUpperCase, compression
    // ===================================================================


    // ===== 1. PALINDROME CHECK =====
    // A string is a palindrome if it reads the same forwards and backwards
    // e.g. "racecar" -> palindrome, "hello" -> not
    public static boolean isPalindrome(String s) {
        int left = 0;                        // pointer starting from the beginning
        int right = s.length() - 1;         // pointer starting from the end
        while (left < right) {              // keep going until both pointers meet in the middle
            if (s.charAt(left) != s.charAt(right)) {  // if characters don't match, not a palindrome
                return false;
            }
            left++;   // move left pointer rightward
            right--;  // move right pointer leftward
        }
        return true; // all characters matched -> palindrome
    }


    // ===== 2. SHORTEST PATH (Direction String) =====
    // Given a path string like "NESW", find net displacement from origin
    // N = North (+y), S = South (-y), E = East (+x), W = West (-x)
    public static void shortestPath(String path) {
        int x = 0, y = 0;                   // start at origin (0,0)
        for (int i = 0; i < path.length(); i++) {  // go through each direction character
            char dir = path.charAt(i);       // get current direction
            if (dir == 'N') y++;             // move up
            else if (dir == 'S') y--;        // move down
            else if (dir == 'E') x++;        // move right
            else if (dir == 'W') x--;        // move left
        }
        // distance formula: sqrt(x^2 + y^2)
        double distance = Math.sqrt(x * x + y * y);
        System.out.println("Shortest Distance: " + distance);
    }


    // ===== 3. PRINT LARGEST STRING =====
    // Among an array of strings, find the lexicographically largest one
    // "lexicographically" means dictionary order — 'z' > 'a', "mango" > "apple"
    public static String largestString(String[] arr) {
        String largest = arr[0];              // assume first string is the largest
        for (int i = 1; i < arr.length; i++) {
            // compareTo() returns positive if largest < arr[i] (i.e., arr[i] comes later in dictionary)
            if (largest.compareTo(arr[i]) < 0) {
                largest = arr[i];             // update largest if a bigger string is found
            }
        }
        return largest;
    }


    // ===== 4. STRING COMPRESSION =====
    // Compress consecutive repeated characters with their count
    // e.g. "aaabbbcc" -> "a3b3c2"
    // e.g. "abcd"     -> "a1b1c1d1"  (standard compression — count 1 still written)
    public static String compress(String str) {
        StringBuilder sb = new StringBuilder(); // StringBuilder is mutable, efficient for building strings
        int i = 0;
        while (i < str.length()) {
            char current = str.charAt(i);  // the character we are currently counting
            int count = 0;
            // count how many times this character appears consecutively
            while (i < str.length() && str.charAt(i) == current) {
                count++;   // increment count for each match
                i++;       // move to next character
            }
            sb.append(current); // add the character to the result
            sb.append(count);   // add its count right after
        }
        return sb.toString(); // convert StringBuilder back to a regular String
    }


    // ===== 5. CONVERT TO UPPERCASE (without toUpperCase()) =====
    // Each lowercase letter's ASCII value is exactly 32 more than its uppercase version
    // 'a' = 97, 'A' = 65  -> difference = 32
    // So subtracting 32 from a lowercase char gives the uppercase version
    public static String toUpperCase(String str) {
        StringBuilder sb = new StringBuilder();  // use StringBuilder to build result
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);             // get each character
            if (ch >= 'a' && ch <= 'z') {        // if it is a lowercase letter
                ch = (char)(ch - 32);            // convert to uppercase by subtracting 32 from ASCII value
            }
            sb.append(ch);                       // add (possibly converted) character to result
        }
        return sb.toString();
    }


    // ===== MAIN — demonstrations of everything =====
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        // ----- 1. WHAT ARE STRINGS -----
        // A String is a sequence of characters. In Java, String is a class (not a primitive).
        String greeting = "Hello, World!";       // string literal — stored in String Pool
        System.out.println("String: " + greeting);


        // ----- 2. INPUT / OUTPUT -----
        // sc.next()      reads a single word (stops at whitespace)
        // sc.nextLine()  reads the entire line including spaces
        System.out.print("Enter a word: ");
        String word = sc.next();                 // reads one word
        System.out.println("You entered: " + word);


        // ----- 3. STRING LENGTH -----
        // .length() returns the number of characters in the string (0-indexed access, but length is 1-indexed count)
        System.out.println("Length of \"" + greeting + "\": " + greeting.length());


        // ----- 4. STRING CONCATENATION -----
        // '+' operator joins two strings together
        // Java creates a NEW string object every time (because strings are immutable)
        String firstName = "Aayush";
        String lastName  = "Karthikeyan";
        String fullName  = firstName + " " + lastName;   // concatenation
        System.out.println("Full name: " + fullName);


        // ----- 5. charAt() -----
        // .charAt(index) returns the character at a given position (0-based index)
        String str = "Java";
        System.out.println("charAt(0): " + str.charAt(0));  // 'J'
        System.out.println("charAt(2): " + str.charAt(2));  // 'v'
        // You can iterate over all characters using charAt in a loop:
        System.out.print("Characters: ");
        for (int i = 0; i < str.length(); i++) {
            System.out.print(str.charAt(i) + " ");  // prints each char with a space
        }
        System.out.println();


        // ----- 6. PALINDROME CHECK -----
        String[] testWords = {"racecar", "hello", "madam", "java"};
        for (String w : testWords) {
            System.out.println(w + " -> palindrome: " + isPalindrome(w));
        }


        // ----- 7. SHORTEST PATH -----
        // "WNEENESENNN" means move in those directions one step at a time
        shortestPath("WNEENESENNN");


        // ----- 8. STRING COMPARISON (compareTo / equals) -----
        // == compares references (memory address) — DO NOT use for string content
        // .equals()   checks if content is exactly the same (case-sensitive)
        // .compareTo() returns:
        //     0  if strings are equal
        //   < 0  if calling string comes before argument alphabetically
        //   > 0  if calling string comes after argument alphabetically
        String a = "apple";
        String b = "mango";
        System.out.println("equals: " + a.equals(b));           // false
        System.out.println("compareTo: " + a.compareTo(b));     // negative (apple < mango)
        System.out.println("equalsIgnoreCase: " + "JAVA".equalsIgnoreCase("java")); // true


        // ----- 9. SUBSTRING -----
        // .substring(startIndex)           -> from startIndex to end
        // .substring(startIndex, endIndex) -> from startIndex up to (but NOT including) endIndex
        String sentence = "Hello World";
        System.out.println(sentence.substring(6));     // "World"
        System.out.println(sentence.substring(0, 5));  // "Hello"


        // ----- 10. LARGEST STRING -----
        String[] fruits = {"apple", "mango", "banana", "watermelon"};
        System.out.println("Largest string: " + largestString(fruits));  // watermelon


        // ----- 11. WHY STRINGS ARE IMMUTABLE -----
        // Once a String object is created, its content CANNOT be changed.
        // Every operation that seems to "modify" a string actually creates a NEW string.
        // This is by design for: security, caching (String Pool), thread-safety.
        String original = "Hello";
        String modified = original.concat(" World");  // does NOT change 'original'
        System.out.println("original: " + original);  // still "Hello"
        System.out.println("modified: " + modified);  // "Hello World" (new object)


        // ----- 12. StringBuilder -----
        // StringBuilder IS mutable — it modifies the same object in memory.
        // Use it when you need to build strings in a loop (much faster than '+' concatenation).
        StringBuilder sb = new StringBuilder();    // empty StringBuilder
        sb.append("Hello");   // add "Hello"
        sb.append(" ");       // add a space
        sb.append("World");   // add "World"
        sb.insert(5, ",");    // insert ',' at index 5 -> "Hello, World"
        sb.delete(5, 6);      // delete characters from index 5 to 5 (exclusive 6) -> "Hello World"
        sb.reverse();         // reverse the entire content -> "dlroW olleH"
        System.out.println("StringBuilder result: " + sb.toString()); // convert back to String


        // ----- 13. CONVERT TO UPPERCASE -----
        String lower = "hello java world";
        System.out.println("toUpperCase (manual): " + toUpperCase(lower));
        System.out.println("toUpperCase (built-in): " + lower.toUpperCase()); // Java's built-in method


        // ----- 14. STRING COMPRESSION -----
        System.out.println("Compressed \"aaabbbcc\": " + compress("aaabbbcc"));   // a3b3c2
        System.out.println("Compressed \"abcd\": "     + compress("abcd"));        // a1b1c1d1
        System.out.println("Compressed \"aabbccdd\": " + compress("aabbccdd"));    // a2b2c2d2

        sc.close();
    }
}
