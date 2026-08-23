# Company Practice Tracker

Source: the course's "Plus Library" — company-tagged practice problems
(Amazon, Microsoft, Google, Atlassian, Goldman Sachs, Adobe), each
split into Easy/Medium/Hard. No specific target company, so this
tracks broad practice rather than one company's exact list.

**Mappings below were verified** by reading every topic's actual
`notes.md` and code, not guessed from topic titles.

## How to use this

This is a **testing log, not a documentation project**. For each
problem: check the "Maps to" column, re-skim that topic's notes if it's
been a while, then attempt the problem cold. Update **Status** yourself
as `solved` / `struggled` / `stuck`.

## Confidence markers

| Marker | Meaning |
|---|---|
| **[DITTO]** | The **exact same problem** is already solved in that folder. Go read your own code first — you've done this one. |
| **[close]** | A direct variant is solved there (e.g. "Mirror a BST" vs "Invert Binary Tree"). Small step from what you have. |
| *(plain)* | The **technique** is taught there, but this specific problem isn't solved. This is the normal case — real practice. |
| **[NEW]** | No coverage anywhere in the repo. Nothing to fall back on yet. |

## Gaps: what's genuinely missing

Only **two** patterns in this whole list have no coverage in the repo:

- **Sliding Window** — Longest Substring, Minimum Window Substring,
  Sliding Window Maximum, Max occurrences of a substring, Shortest
  subarray with Sum ≥ K, Number of Substrings with 1s. *(Note: the
  Deque tool that Sliding Window Maximum needs IS covered in
  `28. Queues`.)*
- **Design problems** — LRU Cache, Design Hit Counter, Design File
  System, Logger Rate Limiter, Design a Stack With Increment.
  *(All building blocks exist: `36. Hashing`, `24/25. Linked Lists`,
  `26/27. Stacks`, `37. Tries` — it's the design skill that's new.)*

**DP on Grid** is a partial gap: `22. Backtracking` covers "Grid Ways"
(= Unique Paths, via recursion + math formula), but the DP-table
formulation and Minimum Path Sum / Triangle variants aren't covered.

---

## Amazon [Easy]

| Problem | Maps to | Status |
|---|---|---|
| Remove Duplicates from Sorted Array | 11. Arrays Part-1 (two pointers) | - |
| Merge Sorted Array | 20. Divide & Conquer — merge step **[close]** | - |
| Pascal's Triangle | 14. 2D Arrays | - |
| Missing Number | 16. Bit Manipulation (XOR) / 11. Arrays | - |
| Merge Two Sorted Lists | 25. Linked Lists Part 2 — `merge()` **[DITTO]** | - |
| Intersection of Two Linked Lists | 24/25. Linked Lists (two pointers) | - |
| Palindromic Linked List | 24. Linked Lists Part 1 — "Check if LL is Palindrome" **[DITTO]** | - |
| Last Stone Weight | 35. Heaps (max heap) | - |

## Amazon [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Partition List | 24/25. Linked Lists | - |
| Sum of Subarray Ranges | 26/27. Stacks (Next Greater/Smaller Element) | - |
| Car Pooling | 29. Greedy Algorithms (+ 13. Sorting) | - |
| Find K Closest Elements | 35. Heaps / 20. Divide & Conquer | - |
| Kth Largest Element in an Array | 35. Heaps | - |
| Meeting Rooms II | 29. Greedy — Activity Selection (+ 35. Heaps) | - |
| Group Anagram | 36. Hashing — "Valid Anagram" **[close]** | - |
| All Nodes Distance K from Binary Tree | 30-32. Binary Trees (+ Graphs BFS) | - |
| Flatten Binary Tree to Linked List | 30-32. Binary Trees | - |
| Minimum No. of Swaps | 13. Basic Sorting Algorithms | - |
| Rotting Oranges | 38. Graphs-1 (BFS) + 42. Flood Fill | - |
| Number of Islands | 42. Graphs-5 — "Flood Fill" **[close]** | - |
| Course Schedule | 39/40. Graphs — cycle detection + topological sort **[close]** | - |
| Longest Palindromic Substring | 15. Strings — "Palindrome Check" (partial: needs expand-around-center or DP) | - |
| Find Good Days to Rob the Bank | 11. Arrays Part-1 (prefix sums) | - |
| Jump Game | 49. DP-6 — "Minimum Array Jumps" **[close]** | - |
| Unique Paths | 22. Backtracking — "Grid Ways" **[close]** (DP-table version not covered) | - |
| LRU Cache | **[NEW]** Design (blocks: 36. Hashing + 24/25. Linked Lists) | - |

## Amazon [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Kth Smallest Pair Distance | 20. Divide & Conquer (binary search on answer) | - |
| Median of Two Sorted Arrays | 20. Divide & Conquer (binary search) | - |
| Merge K Sorted Lists | 35. Heaps + 25. Linked Lists `merge()` | - |
| Find Median from Data Stream | 35. Heaps (two-heap technique) | - |
| Race Car | 44-49. DP / BFS hybrid — weak coverage | - |
| Longest Valid Parentheses | 27. Stacks Part 2 — "Valid Parentheses" **[close]** | - |

## Microsoft [Easy]

| Problem | Maps to | Status |
|---|---|---|
| Sign of the product of an array | 11. Arrays Part-1 | - |
| Palindrome Number | 15. Strings — "Palindrome Check" | - |
| Valid Palindrome | 15. Strings — "Palindrome Check" **[DITTO]** | - |
| Defanged IP Address | 15. Strings | - |

## Microsoft [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Number of Substrings with 1s | **[NEW]** Sliding Window / counting | - |
| Next permutation | 11/12. Arrays | - |
| Linked List Cycle | 25. Linked Lists Part 2 — "Floyd's Algorithm" **[DITTO]** | - |
| Number of 1 bits | 16. Bit Manipulation — "Count Set Bits" **[DITTO]** | - |
| Reverse a Linked List | 24. Linked Lists Part 1 — "Reverse a Linked List" **[DITTO]** | - |
| Generate Parentheses | 22. Backtracking (+ 27. Valid Parentheses) | - |
| Delete a node Explanation | 24/25. Linked Lists | - |
| Longest Substring | **[NEW]** Sliding Window | - |
| 4 Sum | 23. ArrayLists — "Pair Sum" two-pointer **[close]** | - |
| Valid Stack Sequence Explanation | 26/27. Stacks | - |
| Reorganize Strings | 35. Heaps | - |

## Microsoft [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Longest Increasing Subsequence | 47. DP-4 **[DITTO]** | - |

## Google [Easy]

| Problem | Maps to | Status |
|---|---|---|
| Kth row of Pascal's triangle | 14. 2D Arrays | - |
| Invert Binary Tree | 33. BST Part 1 — "Mirror a BST" **[close]** | - |
| Longest Common Prefix | 15. Strings / 37. Tries — "Shortest Unique Prefix" | - |
| Greatest Common Divisor of Strings | 15. Strings (+ math) | - |
| Climbing stairs | 44. DP-1 **[DITTO]** | - |

## Google [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Palindrome Partitioning | 22. Backtracking | - |
| Rotate image-matrix | 14. 2D Arrays | - |
| Merge Intervals | 29. Greedy Algorithms (+ 13. Sorting) | - |
| Word Search | 22. Backtracking (grid DFS) | - |
| Permutations | 22. Backtracking — "Find All Permutations" **[DITTO]** | - |
| Sum Root to Leaf Numbers | 33. BST Part 1 — "Root to Leaf Paths" **[close]** | - |
| Clone graph | 38/39. Graphs (DFS/BFS + hashing) | - |
| Sort list | 25. Linked Lists Part 2 — "Merge Sort on Linked List" **[DITTO]** | - |
| Longest Consecutive Sequence | 36. Hashing | - |
| Longest Increasing Path in a Matrix | 38-42. Graphs (DFS + memo) | - |
| String To Integer | 15. Strings | - |
| Kth smallest element in a BST | 33. BST Part 1 — inorder traversal **[close]** | - |

## Google [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Largest Rectangle in Histogram | 27. Stacks Part 2 — "Maximum Rectangular Area in Histogram" **[DITTO]** | - |
| Maximal Rectangle | 27. Stacks Part 2 (histogram) + 14. 2D Arrays **[close]** | - |
| Find the Shortest Superstring | Bitmask DP — not covered | - |
| Wildcard matching | 48. DP-5 **[DITTO]** | - |

## Atlassian [Easy]

| Problem | Maps to | Status |
|---|---|---|
| Logger Rate Limiter | **[NEW]** Design (+ 36. Hashing) | - |
| Single Number | 16. Bit Manipulation (XOR) **[close]** | - |
| Arranging Coins | 20. Divide & Conquer (binary search) / math | - |

## Atlassian [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Finding the Users Active Minutes | 36. Hashing | - |
| Design a Stack With Increment Operation | **[NEW]** Design (+ 26/27. Stacks) | - |
| Design hit counter | **[NEW]** Design (+ 28. Queues) | - |
| Find the City with the Smallest Number of Neighbors at a Threshold Distance | 42. Graphs-5 — "Floyd-Warshall Algorithm" **[DITTO]** (algorithm) | - |
| Design File System | **[NEW]** Design (+ 37. Tries) | - |
| Shortest bridge | 38-42. Graphs (BFS/DFS + Flood Fill) | - |
| Rank Team By votes | 36. Hashing + 13. Sorting (custom comparator) | - |
| Top K frequent words | 35. Heaps + 36. Hashing | - |
| Online election | 20. Divide & Conquer (binary search) + 36. Hashing | - |
| Maximum number of occurrences of a substring | **[NEW]** Sliding Window (+ 36. Hashing) | - |
| Find minimum in rotated sorted array | 20. Divide & Conquer — "Search in Sorted & Rotated Array" **[DITTO]** | - |
| Unique Path II | 22. Backtracking — "Grid Ways" **[close]** (DP-grid version not covered) | - |
| Smallest missing non-negative integer after operations | 36. Hashing | - |

## Atlassian [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Maximum profit in job scheduling | 29. Greedy — "Job Sequencing Problem" **[close]** (+ DP + binary search) | - |
| Sliding window maximum | **[NEW]** Sliding Window (+ 28. Queues — "Deque" is the tool) | - |
| Minimum time to visit a cell in a grid | 40. Graphs-3 — Dijkstra's Algorithm | - |
| Number of ways to form a target string given a dictionary | 46/47. DP (LCS-family, counting) | - |
| Count Palindromic Subsequences | 46/47. DP (LCS-family / interval DP) | - |
| Count vowels permutation | 44-49. DP (simple state DP) | - |
| Consecutive numbers sum | Math / 20. Divide & Conquer — weak coverage | - |

## Goldman Sachs [Easy]

| Problem | Maps to | Status |
|---|---|---|
| First Unique Character in String | 28. Queues — "First Non-Repeating Letter in a Stream" **[close]** (+ 36. Hashing) | - |
| Find Pivot Element | 11. Arrays Part-1 (prefix sums) / 20. Divide & Conquer | - |
| Power of three | 16. Bit Manipulation — "Check Power of 2" **[close]** | - |
| Move Zeroes | 11. Arrays Part-1 (two pointers) | - |
| Robot return to origin | 15. Strings — "Shortest Path (Direction String)" **[close]** | - |
| K-different pairs in an Array | 36. Hashing / 23. ArrayLists — "Pair Sum" | - |

## Goldman Sachs [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Beautiful Arrangement | 22. Backtracking | - |
| Minimum Area of Rectangle | 36. Hashing | - |
| Gas Station | 29. Greedy Algorithms | - |
| Minimum Path Sum | **[NEW]** DP on Grid | - |
| Product of Array Except Self | 11/12. Arrays (prefix/suffix) | - |
| Minimum Window Substring | **[NEW]** Sliding Window | - |
| Count Number of Teams | 11/12. Arrays | - |
| Robot bounded in a circle | 15. Strings — "Shortest Path (Direction String)" **[close]** | - |
| Spiral Matrix | 14. 2D Arrays — "Print Matrix in Spiral Order" **[DITTO]** | - |
| Longest Word in Dictionary through Deleting | 15. Strings (two pointers, subsequence check) | - |
| Knight Probability in Chessboard | 44-49. DP (+ grid state) | - |
| Fraction to Recurring Decimal | 36. Hashing (+ math) | - |

## Goldman Sachs [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Shortest subarray with Sum at least K | **[NEW]** Sliding Window (+ 28. Queues — Deque) | - |
| Reaching Points | Math / 20. Divide & Conquer — weak coverage | - |
| Last Substring in Lexicographical Order | 15. Strings — "Largest String (Lexicographic)" **[close]** | - |
| Super Egg Drop | 44-49. DP (advanced) + binary search — weak coverage | - |

## Adobe [Easy]

| Problem | Maps to | Status |
|---|---|---|
| Baseball Game | 26. Stacks Part 1 | - |
| Shortest Distance to a Character | 11. Arrays Part-1 (two-pass) | - |
| To Lower Case | 15. Strings — "Convert to Uppercase (ASCII trick)" **[close]** | - |
| Toeplitz Matrix | 14. 2D Arrays | - |
| Reverse String II | 15. Strings (+ StringBuilder) | - |
| Minimum index sum of two lists | 36. Hashing | - |
| Remove Element | 11. Arrays Part-1 (two pointers) | - |
| Length of last word | 15. Strings | - |

## Adobe [Medium]

| Problem | Maps to | Status |
|---|---|---|
| Subsets | 22. Backtracking — "Find All Subsets" **[DITTO]** | - |
| Sum of Square Numbers | 20. Divide & Conquer (binary search) / two pointers | - |
| Equal row and column pairs | 14. 2D Arrays + 36. Hashing | - |
| Number of laser beams in a bank | 14. 2D Arrays | - |
| Determine if two strings are close | 36. Hashing | - |
| Check If a String can break another String | 13. Sorting + 15. Strings | - |
| Wiggle Sort II | 13. Basic Sorting Algorithms | - |
| Minimize Maximum Pair Sum in Array | 29. Greedy — "Minimum Sum Absolute Difference Pairs" **[close]** | - |
| Minimum Number of Swaps to Make The String Balanced | 27. Stacks Part 2 — "Valid Parentheses" **[close]** | - |
| Triangle | **[NEW]** DP on Grid | - |
| Maximum Area of a Piece of Cake After Horizontal And Vertical Cut | 13. Sorting + 29. Greedy | - |
| Sort Colors | 13. Basic Sorting Algorithms (Dutch flag) | - |
| Search a 2D Matrix | 14. 2D Arrays — "Staircase Search" **[DITTO]** | - |
| Sum in a Matrix | 14. 2D Arrays + 13. Sorting | - |
| Valid Sudoku | 22. Backtracking — "Sudoku Solver" **[close]** (+ 36. Hashing) | - |
| Maximum Bags With Full Capacity of Rocks | 29. Greedy Algorithms | - |

## Adobe [Hard]

| Problem | Maps to | Status |
|---|---|---|
| Reducing dishes | 29. Greedy + 13. Sorting | - |
| Unique Paths III | 22. Backtracking — "Grid Ways" **[close]** | - |
| First Missing Positive | 11/12. Arrays (cyclic sort) | - |
| Palindrome Partitioning II | 22. Backtracking + 46/47. DP | - |
