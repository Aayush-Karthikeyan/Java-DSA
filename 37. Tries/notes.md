# 37. Tries

## 1. Core Idea

A **trie** is a tree built for strings. Each path from the root represents a prefix, and each edge represents a character.

For the words `app` and `apple`:

```text
root
  └─ a
     └─ p
        └─ p  (end of "app")
           └─ l
              └─ e  (end of "apple")
```

The two words share the nodes for `app`. This is why tries are useful for prefix questions.

This chapter uses lowercase English letters only:

```java
Node[] children = new Node[26];
int index = ch - 'a';
```

Each node stores:

- `children[26]`: links to the next characters;
- `endOfWord`: whether a complete inserted word ends here;
- `prefixCount`: how many inserted words pass through this node.

### Main operations

| Operation | Meaning | Time | Extra space |
|---|---|---:|---:|
| Insert | Add a word | O(L) | O(L) worst case |
| Search | Find an exact word | O(L) | O(1) |
| `startsWith` | Find any matching prefix | O(P) | O(1) |

`L` is the word length and `P` is the prefix length. Search must check `endOfWord`; otherwise `appl` would incorrectly count as a stored word when only `apple` was inserted.

### Insertion pattern

```java
Node current = root;
for (char ch : word.toCharArray()) {
    int index = ch - 'a';
    if (current.children[index] == null) {
        current.children[index] = new Node();
    }
    current = current.children[index];
}
current.endOfWord = true;
```

### Space complexity of a trie

If the total number of inserted characters is `S`, the worst-case space is O(S). Shared prefixes usually reduce the actual number of nodes. With a fixed `children[26]` array, each node reserves 26 references even when few are used.

## 2. How to Recognize This Pattern

Consider a trie when a problem involves:

- many string insertions and searches;
- prefix lookup or autocomplete;
- finding the shortest unique prefix;
- repeatedly checking dictionary words;
- storing all suffixes or counting distinct substrings;
- checking whether every prefix of a word exists.

A `HashSet<String>` is often easier for exact whole-word lookup. A trie becomes especially helpful when prefixes or character-by-character matching matter.

## 3. Problems in This Folder

### Word Break

**What the question asks**

Determine whether the entire input string can be separated into one or more dictionary words.

**Brute-force approach**

- Try every possible first piece, and recursively try to split the remaining suffix.
- Time: O(2ⁿ) in the worst case because the same suffixes are solved repeatedly.
- Space: O(n) recursion depth, excluding temporary strings.

**Optimized approach**

- Store the dictionary in a trie.
- From a starting index, follow trie links through the input.
- When `endOfWord` is true, recursively try the next index.
- Memoize each starting index so it is solved once.
- Time: O(n²) worst case after trie construction.
- Auxiliary space: O(n) for memoization and recursion. Dictionary trie: O(S), where `S` is total dictionary characters.

**Interview explanation**

“The plain recursive solution tries every split and repeats the same suffix work. I store the dictionary in a trie and scan forward from each starting index. Whenever I reach a complete word, I try breaking the remaining part. Memoizing each start index reduces the worst case to O(n²).”

**Common follow-up questions**

- Why use memoization? Different split choices can reach the same remaining suffix, so caching prevents repeated recursion.
- What is the base case? If the start index reaches the string length, every character was successfully matched.
- Could a HashSet be used? Yes. A HashSet plus dynamic programming is also a good solution; the trie avoids creating and looking up every substring.

**Dry run**

Dictionary: `{i, like, samsung}`; key: `ilikesamsung`.

```text
i | like | samsung
0   1      5       12
```

The trie finds word endings after `i`, `like`, and `samsung`, so the recursion reaches index 12 and returns true.

**Common mistakes**

- Returning true after matching only the first dictionary word.
- Forgetting the `start == key.length()` base case.
- Omitting memoization and claiming polynomial time.
- Using `search()` only on the complete input instead of trying valid breaks.

### Shortest Unique Prefix

**What the question asks**

For every distinct word, find the shortest starting prefix that no other word shares.

**Brute-force approach**

- Try prefixes of each word and compare each prefix against all other words.
- Time: roughly O(W²L²) with direct string-prefix comparisons.
- Space: O(WL) for the answers, depending on stored strings.

**Optimized approach**

- While inserting, increment `prefixCount` on every visited node.
- For each word, follow its path until `prefixCount == 1`.
- That first node gives the shortest unique prefix.
- Time: O(S), where `S` is the total number of input characters.
- Space: O(S) for the trie and returned prefixes.

**Interview explanation**

“I count how many words pass through each trie node during insertion. Then I walk each word again. The first node with a count of one is the first point where no other word shares that path, so it gives the shortest unique prefix. The total work is linear in all input characters.”

**Common follow-up questions**

- Why stop at count one? Exactly one word uses that prefix, so extending it further is unnecessary.
- What if duplicate words exist? A unique prefix does not exist for identical words; this method returns an empty string in that case.
- What if one word is a prefix of another? The shorter word may have no unique character prefix; the implementation returns an empty string for it.

**Dry run**

For `dog`, `duck`, and `dove`:

| Word | Prefix counts followed | Answer |
|---|---|---|
| dog | `d:3`, `do:2`, `dog:1` | `dog` |
| duck | `d:3`, `du:1` | `du` |
| dove | `d:3`, `do:2`, `dov:1` | `dov` |

**Common mistakes**

- Counting only completed words instead of words passing through a node.
- Stopping when `endOfWord` is true rather than when `prefixCount == 1`.
- Forgetting to define behavior for duplicates.

### startsWith

**What the question asks**

Return true if at least one stored word begins with a given prefix.

**Brute-force approach**

- Store all words in a list and call `word.startsWith(prefix)` on each.
- Time: O(WP), where `W` is the number of words and `P` is prefix length.
- Space: O(1) beyond the stored words.

**Optimized approach**

- Follow the prefix characters from the trie root.
- A missing child returns false; reaching the final character returns true.
- Do not require `endOfWord`, because the prefix itself need not be a complete word.
- Time: O(P). Space: O(1).

**Interview explanation**

“I follow one trie edge for every prefix character. If any link is missing, no inserted word can start with that prefix. If I consume the whole prefix, I return true without checking endOfWord, because the question asks about a prefix rather than an exact word.”

**Common follow-up questions**

- How does this differ from `search`? Exact search must finish at an `endOfWord` node; `startsWith` only needs the path to exist.
- What does an empty prefix return? True, because every stored word starts with the empty prefix; this implementation also returns true for an empty trie.

**Dry run**

With `apple` inserted, `startsWith("app")` follows `a → p → p` and returns true. `search("app")` is false unless `app` was inserted separately.

**Common mistakes**

- Checking `endOfWord` at the final prefix node.
- Returning true after matching only part of the prefix.
- Forgetting the lowercase-letter assumption before using `ch - 'a'`.

### Count Unique Substrings

**What the question asks**

Count the different non-empty contiguous substrings of a string.

**Brute-force approach**

- Generate every substring and place it in a `HashSet<String>`.
- There are O(n²) substrings, and copying/hashing each can take O(n).
- Time: O(n³) in a conservative Java analysis. Space: O(n³) characters in the worst case.

**Optimized approach**

- Insert every suffix into a trie character by character.
- Every substring is a prefix of some suffix.
- Every new trie node therefore represents one new distinct substring.
- Return the number of created nodes, excluding the root/empty string.
- Time: O(n²). Space: O(n²) worst case.

**Interview explanation**

“Every substring is a prefix of one of the string’s suffixes. I insert all suffixes into a trie and count only newly created nodes. Shared paths represent repeated substrings, while every new node represents one new distinct non-empty substring. This takes O(n²) time and space.”

**Common follow-up questions**

- Why exclude the root? It represents the empty string, but the question asks for non-empty substrings.
- Is a substring contiguous? Yes. A subsequence is the one that may skip characters.
- Is there a more advanced solution? A suffix array or suffix automaton can improve space/time, but the suffix trie is the intended beginner solution here.

**Dry run**

For `aaa`, the distinct paths represent `a`, `aa`, and `aaa`. Repeated suffix insertions create no additional nodes, so the answer is 3.

**Common mistakes**

- Inserting only the complete string rather than every suffix.
- Adding one for the root when only non-empty substrings are requested.
- Claiming O(n) space for a suffix trie.
- Confusing unique substrings with unique characters.

### Longest Word With All Prefixes

**What the question asks**

Find the longest word for which every character prefix is also present as a complete word. Break ties lexicographically.

**Brute-force approach**

- For each word, build each prefix and search for it in a list.
- Time can reach O(W²L²) with repeated scans and string creation.
- Extra space: O(L) for a temporary prefix.

**Optimized approach**

- Insert all words into a trie.
- Follow each candidate word and require `endOfWord == true` at every node.
- Keep the longer valid word; for equal lengths, keep the lexicographically smaller one.
- Time: O(S), where `S` is total input characters, including trie construction and validation.
- Space: O(S) for the trie.

**Interview explanation**

“After inserting every word, I walk each candidate through the trie. Every visited character node must mark the end of a complete word, which proves that every prefix exists. I keep the longest valid candidate and use alphabetical order for ties. Each input character is processed a constant number of times.”

**Common follow-up questions**

- Why check `endOfWord` at every node? A path proves only that a prefix exists as part of some word; the flag proves the prefix was inserted as a complete word.
- How are equal-length answers handled? Compare them with `compareTo` and keep the lexicographically smaller one.
- What if no word qualifies? The method returns the empty string.

**Dry run**

For `{a, ap, app, appl, apple, apply}`, both `apple` and `apply` have every prefix. They tie in length, so `apple` is returned because it is lexicographically smaller.

**Common mistakes**

- Checking only whether the trie path exists.
- Forgetting the alphabetical tie-breaker.
- Sorting and returning the last word without validating all prefixes.

## 4. Topic-Level Interview Questions

**What is a trie?**  
A tree for strings in which paths represent prefixes. It supports operations based on word or prefix length rather than the number of stored words.

**Why is a trie useful for prefixes?**  
Words with the same prefix share the same path, so a prefix can be checked one character at a time in O(P).

**What is the difference between a trie and a HashMap of words?**  
A HashMap is excellent for exact whole-word lookup. A trie naturally supports prefixes, autocomplete, and character-by-character matching, but often uses more memory.

**Why do we need `endOfWord`?**  
A trie path may be only a prefix of a longer word. The flag tells us that a complete inserted word ends at that node.

**What is the time complexity of insert and search?**  
Both are O(L), where `L` is the word length, assuming direct child-array access.

**What is a suffix trie?**  
A trie containing every suffix of a string. Its paths represent the string’s substrings.

**What are the memory trade-offs?**  
A 26-child array gives simple O(1) next-character access but reserves many null references. A map per node can save space for sparse alphabets but adds map overhead.

**Can a trie support uppercase letters or Unicode?**  
Yes, but the child representation must change. A `HashMap<Character, Node>` is a common flexible choice.

## 5. Quick Revision Sheet

### Important patterns

- Character index: `ch - 'a'`
- Exact word: path exists **and** final `endOfWord` is true
- Prefix: path only; no final word flag required
- Unique prefix: first node with `prefixCount == 1`
- Word break: trie traversal + recursion + memoization
- Unique substrings: number of newly created suffix-trie nodes
- All prefixes: every node along the candidate path has `endOfWord == true`

### Complexities

| Task | Time | Space |
|---|---:|---:|
| Insert/search one word | O(L) | O(L) insertion worst case / O(1) search |
| Build trie | O(S) | O(S) |
| Word break | O(n²) | O(n) auxiliary + dictionary trie |
| All shortest unique prefixes | O(S) | O(S) |
| Count unique substrings | O(n²) | O(n²) |
| Longest word with all prefixes | O(S) | O(S) |

### One-line reminders

- The root stores no character.
- Mark `endOfWord` only after the complete word is inserted.
- Shared prefixes reuse nodes.
- State the supported alphabet before using a fixed child array.
- `startsWith` does not check `endOfWord`.
- A suffix trie’s root represents the empty substring, so exclude it when required.
