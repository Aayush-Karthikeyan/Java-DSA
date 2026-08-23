import java.util.*;

public class Tries {

    // A node represents one character along a path.
    // endOfWord distinguishes a complete word from only a prefix.
    static class Node {
        Node[] children = new Node[26];
        boolean endOfWord;
        int prefixCount;
    }

    static class Trie {
        private final Node root = new Node();

        /*
         * Problem:
         * Insert a lowercase English word into the trie.
         *
         * Pattern:
         * Trie Traversal
         *
         * Approach:
         * 1. Start at the root.
         * 2. Convert each character to an index from 0 to 25.
         * 3. Create a missing child and move to it.
         * 4. Mark the final node as the end of a complete word.
         *
         * Time: O(L), where L is the word length
         * Space: O(L) in the worst case for newly created nodes
         */
        void insert(String word) {
            Node current = root;

            for (char ch : word.toCharArray()) {
                int index = toIndex(ch);
                if (current.children[index] == null) {
                    current.children[index] = new Node();
                }

                current = current.children[index];
                current.prefixCount++;
            }
            current.endOfWord = true;
        }

        /*
         * Problem:
         * Check whether a complete word exists in the trie.
         *
         * Pattern:
         * Trie Traversal
         *
         * Approach:
         * 1. Follow the child for each character.
         * 2. Return false as soon as a link is missing.
         * 3. After the final character, check endOfWord.
         *
         * Time: O(L)
         * Space: O(1)
         */
        boolean search(String word) {
            Node node = findNode(word);
            return node != null && node.endOfWord;
        }

        /*
         * Problem:
         * Check whether any stored word begins with the given prefix.
         *
         * Pattern:
         * Trie Prefix Search
         *
         * Approach:
         * 1. Follow the child for every prefix character.
         * 2. A missing child means no word has that prefix.
         * 3. Reaching the final prefix character means the answer is true.
         *
         * Time: O(P), where P is the prefix length
         * Space: O(1)
         */
        boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }

        private Node findNode(String text) {
            Node current = root;
            for (char ch : text.toCharArray()) {
                int index = toIndex(ch);
                if (current.children[index] == null) {
                    return null;
                }
                current = current.children[index];
            }
            return current;
        }
    }

    // This chapter uses lowercase English letters only.
    private static int toIndex(char ch) {
        if (ch < 'a' || ch > 'z') {
            throw new IllegalArgumentException(
                    "Trie supports lowercase English letters a-z only: " + ch);
        }
        return ch - 'a';
    }

    /*
     * Problem:
     * Decide whether a string can be split into one or more dictionary words.
     *
     * Pattern:
     * Trie + Recursion + Memoization
     *
     * Approach:
     * 1. Begin at a start index and follow trie links through the string.
     * 2. Whenever a complete dictionary word ends, try the remaining suffix.
     * 3. Cache the answer for each start index to avoid repeated work.
     * 4. Reaching the end means the entire string was split successfully.
     *
     * Time: O(n^2) worst case, after building the dictionary trie
     * Space: O(n) auxiliary space, excluding the dictionary trie
     */
    static boolean wordBreak(String key, Trie dictionary) {
        boolean[] memo = new boolean[key.length() + 1];
        boolean[] computed = new boolean[key.length() + 1];
        return wordBreakFrom(0, key, dictionary, memo, computed);
    }

    private static boolean wordBreakFrom(int start, String key, Trie dictionary,
                                         boolean[] memo, boolean[] computed) {
        if (start == key.length()) {
            return true;
        }
        if (computed[start]) {
            return memo[start];
        }

        Node current = dictionary.root;
        for (int end = start; end < key.length(); end++) {
            int index = toIndex(key.charAt(end));
            if (current.children[index] == null) {
                break;
            }

            current = current.children[index];
            if (current.endOfWord
                    && wordBreakFrom(end + 1, key, dictionary, memo, computed)) {
                computed[start] = true;
                memo[start] = true;
                return true;
            }
        }

        computed[start] = true;
        memo[start] = false;
        return false;
    }

    /*
     * Problem:
     * Find the shortest prefix that uniquely identifies each distinct word.
     *
     * Pattern:
     * Trie + Prefix Frequency
     *
     * Approach:
     * 1. Insert every word and count how many words pass through each node.
     * 2. Walk each word from the root.
     * 3. Stop at the first node whose prefixCount is 1.
     * 4. Return an empty string if no unique prefix exists.
     *
     * Time: O(S), where S is the total number of input characters
     * Space: O(S) for the trie and answers
     */
    static List<String> shortestUniquePrefixes(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        List<String> answer = new ArrayList<>();
        for (String word : words) {
            Node current = trie.root;
            StringBuilder prefix = new StringBuilder();
            String uniquePrefix = "";

            for (char ch : word.toCharArray()) {
                current = current.children[toIndex(ch)];
                prefix.append(ch);

                if (current.prefixCount == 1) {
                    uniquePrefix = prefix.toString();
                    break;
                }
            }
            answer.add(uniquePrefix);
        }
        return answer;
    }

    /*
     * Problem:
     * Count all different non-empty substrings of a string.
     *
     * Pattern:
     * Suffix Trie
     *
     * Approach:
     * 1. Start a trie path at every string index.
     * 2. Extend that suffix one character at a time.
     * 3. Every newly created trie node represents one new substring.
     * 4. Return the number of new nodes; the root represents the empty string.
     *
     * Time: O(n^2)
     * Space: O(n^2) worst case
     */
    static int countUniqueSubstrings(String text) {
        Node root = new Node();
        int count = 0;

        for (int start = 0; start < text.length(); start++) {
            Node current = root;

            for (int end = start; end < text.length(); end++) {
                int index = toIndex(text.charAt(end));
                if (current.children[index] == null) {
                    current.children[index] = new Node();
                    count++; // A new path is one previously unseen substring.
                }
                current = current.children[index];
            }
        }
        return count;
    }

    /*
     * Problem:
     * Find the longest word for which every prefix is also a complete word.
     * If lengths tie, return the lexicographically smaller word.
     *
     * Pattern:
     * Trie Prefix Validation
     *
     * Approach:
     * 1. Insert all words into a trie.
     * 2. Follow each word's path and require endOfWord at every node.
     * 3. Keep a valid word if it is longer than the current answer.
     * 4. For equal lengths, keep the alphabetically smaller word.
     *
     * Time: O(S), where S is the total number of input characters
     * Space: O(S) for the trie
     */
    static String longestWordWithAllPrefixes(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }

        String answer = "";
        for (String word : words) {
            if (hasAllPrefixes(word, trie)
                    && (word.length() > answer.length()
                    || (word.length() == answer.length()
                    && word.compareTo(answer) < 0))) {
                answer = word;
            }
        }
        return answer;
    }

    private static boolean hasAllPrefixes(String word, Trie trie) {
        Node current = trie.root;
        for (char ch : word.toCharArray()) {
            current = current.children[toIndex(ch)];
            if (current == null || !current.endOfWord) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        trie.insert("app");
        trie.insert("mango");

        System.out.println("Search apple: " + trie.search("apple"));
        System.out.println("Search appl: " + trie.search("appl"));
        System.out.println("Starts with ap: " + trie.startsWith("ap"));

        Trie dictionary = new Trie();
        for (String word : new String[]{"i", "like", "sam", "samsung", "mobile"}) {
            dictionary.insert(word);
        }
        System.out.println("Word break ilikesamsung: "
                + wordBreak("ilikesamsung", dictionary));

        String[] prefixWords = {"zebra", "dog", "duck", "dove"};
        System.out.println("Shortest unique prefixes: "
                + shortestUniquePrefixes(prefixWords));

        System.out.println("Unique substrings in ababa: "
                + countUniqueSubstrings("ababa"));

        String[] words = {"a", "banana", "app", "appl", "ap", "apply", "apple"};
        System.out.println("Longest word with all prefixes: "
                + longestWordWithAllPrefixes(words));
    }
}
