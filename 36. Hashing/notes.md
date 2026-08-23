# 36. Hashing

## 1. Core Idea

Hashing lets us store and find data using a **key**. A hash function converts the key into a bucket location, so lookup, insertion, and deletion are usually fast.

- `HashMap<K, V>` stores key-value pairs. Keys are unique; values may repeat.
- `HashSet<E>` stores unique values only.
- Average `put`, `get`, `contains`, and `remove`: **O(1)**.
- Worst case: **O(n)** if many keys collide. Interview solutions normally state average/expected O(1).

```java
HashMap<String, Integer> map = new HashMap<>();
map.put("India", 142);
map.put("India", 143);              // overwrites the value for this key
map.get("India");                   // 143
map.getOrDefault("Canada", 0);      // 0
map.containsKey("India");           // true
map.remove("India");

for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " " + entry.getValue());
}
```

HashSet operations and iteration:

```java
HashSet<Integer> set = new HashSet<>();
set.add(5);                  // true: inserted
set.add(5);                  // false: duplicate ignored
set.contains(5);             // true
set.remove(5);

for (int value : set) {      // order is not guaranteed
    System.out.println(value);
}
```

### Collisions, buckets, and rehashing

Two keys may produce the same bucket index. This is a **collision**. Separate chaining handles it by keeping multiple entries in that bucket. Java also uses `equals()` to identify the correct key.

The load factor compares entries with bucket count. When the table becomes crowded, it creates more buckets and inserts entries again. This is **rehashing**. A single rehash costs O(n), but normal operations remain O(1) on average over many operations.

### Choosing the correct collection

| Collection | Order | Typical operations | Best use |
|---|---|---:|---|
| `HashMap` | No guaranteed order | O(1) average | Fast key-value lookup |
| `LinkedHashMap` | Insertion order | O(1) average | Fast lookup with predictable iteration |
| `TreeMap` | Keys sorted | O(log n) | Sorted keys/range operations |
| `HashSet` | No guaranteed order | O(1) average | Unique values/fast membership |
| `LinkedHashSet` | Insertion order | O(1) average | Unique values in insertion order |
| `TreeSet` | Values sorted | O(log n) | Unique values in sorted order |

## 2. How to Recognize This Pattern

Consider hashing when the problem asks you to:

- count frequencies;
- detect duplicates or keep only distinct values;
- quickly check whether a value was seen before;
- match a key with another value;
- find pairs or complements;
- optimize nested-loop subarray checks using prefix sums;
- preserve insertion order (`LinkedHashMap`) or sorted order (`TreeMap`).

Two especially useful templates are:

```java
// Frequency counting
frequency.put(value, frequency.getOrDefault(value, 0) + 1);

// Prefix-sum equation for a subarray ending here
earlierPrefix = currentPrefix - target;
```

## 3. Problems in This Folder

### Majority Elements More Than n/3 Times

**What the question asks**

Return every number whose frequency is greater than `n / 3`. There can be at most two such numbers.

**Brute-force approach**

- For every element, scan the entire array and count it.
- Time: O(n²). Space: O(1), ignoring the answer.
- It repeats the same counting work.

**Optimized approach**

- Count each value in a `HashMap<Integer, Integer>`.
- Add entries with frequency greater than `n / 3`.
- Time: O(n) average. Space: O(n).
- A Boyer-Moore variation can use O(1) space, but the HashMap method is simpler for a first solution.

**Interview explanation**

“The direct approach counts each number by scanning the array again, which is O(n²). I can count all frequencies in one pass with a HashMap, then check which counts are greater than n/3. That takes O(n) expected time and O(n) extra space.”

**Common follow-up questions**

- Why can there be at most two answers? Three values occurring more than n/3 times would require more than n total positions.
- Does `n / 3` use integer division? Yes. The condition must still be strictly `frequency > n / 3`.
- Can space be improved? Yes, a two-candidate Boyer-Moore solution uses O(1) space, but it is less beginner-friendly.

**Dry run**

For `[1, 2, 1, 1, 3]`, frequencies are `{1=3, 2=1, 3=1}`. Since `n/3 = 1`, only `1` has a count greater than 1.

**Common mistakes**

- Using `>= n / 3` instead of `> n / 3`.
- Adding a number during every array visit and producing duplicates.
- Assuming there can only be one result.

### Valid Anagram

**What the question asks**

Check whether two strings have exactly the same characters with the same frequencies.

**Brute-force approach**

- Sort both character arrays and compare them.
- Time: O(n log n). Space: O(n) in Java because character arrays are created.
- Sorting does more work than frequency counting.

**Optimized approach**

- If lengths differ, return false.
- Count characters from the first string in a HashMap.
- Decrease/remove counts while reading the second string.
- Time: O(n) average. Space: O(k), where `k` is the number of distinct characters.

**Interview explanation**

“Anagrams must have equal lengths and equal character counts. I count the first string’s characters in a HashMap, then consume those counts using the second string. If a character is missing, I return false. An empty map at the end means every count matched.”

**Common follow-up questions**

- Is it case-sensitive? This implementation is, so `A` and `a` differ. Normalize first if the requirements say otherwise.
- Why not use an array of size 26? That is simpler and faster only when input is guaranteed to be lowercase English letters.
- Why remove zero counts? It makes `map.isEmpty()` a simple final check.

**Dry run**

`race` builds `{r=1, a=1, c=1, e=1}`. Reading `care` removes `c`, `a`, `r`, then `e`; the map becomes empty, so the result is true.

**Common mistakes**

- Not checking lengths first.
- Checking only whether a character exists, not whether enough copies exist.
- Assuming lowercase letters without the problem stating it.

### Count Distinct Elements

**What the question asks**

Return the number of different values in an array.

**Brute-force approach**

- For each element, search earlier elements to see whether it already occurred.
- Time: O(n²). Space: O(1).

**Optimized approach**

- Add every value to a `HashSet`; duplicates are ignored.
- Return `set.size()`.
- Time: O(n) average. Space: O(n).

**Interview explanation**

“I only care whether a value has appeared, not how many times. A HashSet stores each value once, so I add the entire array and return the set size. This is O(n) expected time and O(n) space.”

**Common follow-up questions**

- Why a set instead of a map? No associated value or frequency is needed.
- Are duplicates stored? No; adding an existing value leaves the set unchanged.

**Dry run**

For `[4, 3, 4, 2]`, the set changes `{}` → `{4}` → `{4,3}` → unchanged → `{4,3,2}`. Answer: 3.

**Common mistakes**

- Returning the array length instead of the set size.
- Depending on HashSet iteration order.

### Union and Intersection

**What the question asks**

Find all distinct values present in either array (union) and in both arrays (intersection).

**Brute-force approach**

- Compare values across both arrays and manually avoid duplicate output.
- Time: O(nm) or worse with repeated duplicate checks. Space depends on the output.

**Optimized approach**

- Put the first array in a HashSet.
- Add both arrays to a union set.
- A second-array value belongs to the intersection if the first set contains it.
- Time: O(n + m) average. Space: O(n + m).

**Interview explanation**

“Sets fit because both results must be distinct. I store the first array for fast membership checks, add values from both arrays to the union, and add a second-array value to the intersection only when it exists in the first set. This takes O(n+m) expected time.”

**Common follow-up questions**

- What if either array contains duplicates? Sets remove them automatically.
- Is output order guaranteed? No. Use `LinkedHashSet` for insertion order or `TreeSet` for sorted output.

**Dry run**

For `[1, 2, 2]` and `[2, 3]`, union is `{1,2,3}` and intersection is `{2}`.

**Common mistakes**

- Counting duplicate intersection values more than once.
- Removing from the first set when it is still needed.
- Promising a particular order from `HashSet`.

### Find Itinerary for Tickets

**What the question asks**

Given `source -> destination` tickets that form one path, return the cities in travel order.

**Brute-force approach**

- Try each source as the start and repeatedly scan all tickets for the next city.
- Time: O(n²). Space: O(n) for the route.

**Optimized approach**

- Put all destinations in a HashSet.
- The starting city is the one source that is not a destination.
- Follow the HashMap links until no next ticket exists.
- Time: O(n) average. Space: O(n).

**Interview explanation**

“Every city except the starting city appears as a destination. I collect all destinations, find the source missing from that set, then follow each source-to-destination mapping until the route ends. Each ticket is processed a constant number of times, so expected time is O(n).”

**Common follow-up questions**

- What assumptions are required? The input must form one non-branching path with one start; each source has at most one destination.
- What if tickets branch or form a cycle? This simple method is not sufficient; that becomes a graph traversal/Eulerian-path problem.
- Does HashMap order matter? No, because the start is found by membership and the route follows direct mappings.

**Dry run**

Tickets `{Mumbai=Delhi, Delhi=Goa, Goa=Chennai}` have destinations `{Delhi, Goa, Chennai}`. `Mumbai` is the missing source, so the route is Mumbai → Delhi → Goa → Chennai.

**Common mistakes**

- Starting from the first HashMap entry.
- Searching for a destination that is not a source; that finds the end, not the start.
- Ignoring invalid input assumptions.

### Largest Subarray With Sum 0

**What the question asks**

Return the maximum length of a contiguous subarray whose sum is zero.

**Brute-force approach**

- Start at every index, extend the ending index, and maintain the sum.
- Time: O(n²). Space: O(1).

**Optimized approach**

- Keep a prefix sum and map each sum to its **first** index.
- If the same prefix sum appears at indexes `j` and `i`, the values from `j+1` through `i` sum to zero.
- Seed `(0, -1)` so a zero-sum prefix has length `i - (-1)`.
- Time: O(n) average. Space: O(n).

**Interview explanation**

“If the same prefix sum occurs twice, the values added between those positions must total zero. I store the earliest index of every prefix sum and use the distance when it repeats. Keeping the earliest index maximizes the length, and mapping zero to minus one handles a valid subarray starting at index zero.”

**Common follow-up questions**

- Why store only the first index? The earliest index gives the longest possible distance for later repeats.
- Why put `(0, -1)` initially? It represents the empty prefix before index 0.
- Does this work with negative values? Yes; unlike a sliding window, prefix-sum hashing handles negatives.

**Dry run**

For `[1, -1, 3]`:

| i | value | prefix sum | action | longest |
|---:|---:|---:|---|---:|
| start | — | 0 | store `0 -> -1` | 0 |
| 0 | 1 | 1 | store `1 -> 0` | 0 |
| 1 | -1 | 0 | seen at -1, length 2 | 2 |
| 2 | 3 | 3 | store `3 -> 2` | 2 |

**Common mistakes**

- Overwriting the first index of a prefix sum.
- Forgetting `(0, -1)`.
- Returning the number of subarrays instead of the longest length.

### Subarray Sum Equal to K

**What the question asks**

Count contiguous subarrays whose sum equals `k`.

**Brute-force approach**

- Start at each index and extend right while maintaining a sum.
- Time: O(n²). Space: O(1).

**Optimized approach**

- Let the current prefix sum be `sum`.
- A subarray sums to `k` when an earlier prefix equals `sum - k`.
- Store **frequencies**, because several earlier prefixes can create different valid subarrays.
- Seed `(0, 1)` for subarrays starting at index 0.
- Time: O(n) average. Space: O(n).

**Interview explanation**

“For each position, I compute the running prefix sum. If an earlier prefix was current sum minus k, removing it leaves a subarray sum of k. I add that earlier prefix’s frequency to the answer, then record the current prefix. This handles negative numbers and takes O(n) expected time.”

**Common follow-up questions**

- Why store a frequency instead of one index? Every occurrence of `sum-k` creates a different valid starting point.
- Why query before adding the current prefix? Adding first can incorrectly count an empty subarray when `k = 0`.
- Why not sliding window? With negative values, expanding or shrinking does not change the sum predictably.

**Dry run**

For `[1, 1, 1]`, `k = 2`:

| value | sum | needed `sum-k` | added count | frequencies after |
|---:|---:|---:|---:|---|
| start | 0 | — | — | `{0=1}` |
| 1 | 1 | -1 | 0 | `{0=1, 1=1}` |
| 1 | 2 | 0 | 1 | `{0=1, 1=1, 2=1}` |
| 1 | 3 | 1 | 1 | `{0=1, 1=1, 2=1, 3=1}` |

Answer: 2.

**Common mistakes**

- Using a set instead of a frequency map.
- Forgetting the initial `(0, 1)`.
- Using `sum + k` instead of `sum - k`.
- Confusing a contiguous subarray with a subsequence.

## 4. Topic-Level Interview Questions

**What is a HashMap?**  
A data structure that stores unique keys mapped to values and uses hashing for O(1) average lookup, insertion, and removal.

**What is a HashSet?**  
A collection of unique values. It is ideal when only membership or duplicate removal matters.

**What is a collision?**  
It occurs when different keys map to the same bucket. The implementation must compare keys within that bucket to find the correct entry.

**What is the difference between `hashCode()` and `equals()`?**  
`hashCode()` helps choose a bucket; `equals()` confirms whether two keys are logically the same. Equal objects must have equal hash codes.

**What is rehashing?**  
When the table gets crowded, it creates a larger bucket array and redistributes existing entries to keep operations fast.

**Why is HashMap O(1) only on average?**  
Good hash distribution keeps buckets short. Heavy collisions can require searching many entries, making an operation O(n) in the general worst-case explanation.

**Can HashMap contain nulls?**  
Java `HashMap` allows one null key and multiple null values. `TreeMap` with natural ordering generally does not allow null keys.

**Does HashMap preserve order?**  
No. Use `LinkedHashMap` for insertion order or `TreeMap` for sorted keys.

**Why use `entrySet()` for map iteration?**  
It gives each key and value together and avoids an extra `get(key)` lookup.

**Map versus set?**  
Use a map when a key needs associated information such as a count or index. Use a set when only presence or uniqueness matters.

## 5. Quick Revision Sheet

### Patterns

- Frequency: `map.put(x, map.getOrDefault(x, 0) + 1)`
- Uniqueness/membership: `HashSet`
- Preserve insertion order: `LinkedHashMap` / `LinkedHashSet`
- Sorted order: `TreeMap` / `TreeSet`
- Zero-sum longest length: repeated prefix sum; store first index
- Count sum `k`: look for `currentPrefix - k`; store frequencies
- Itinerary start: source that never appears as a destination

### Complexity

| Operation | HashMap/HashSet | TreeMap/TreeSet |
|---|---:|---:|
| Insert | O(1) average | O(log n) |
| Search | O(1) average | O(log n) |
| Remove | O(1) average | O(log n) |
| Iterate all entries | O(n) | O(n) |

### One-line reminders

- HashMap keys and HashSet values are unique.
- `put` on an existing key overwrites its value.
- Never rely on HashMap or HashSet iteration order.
- Prefix sum base cases matter: `(0, -1)` for longest length, `(0, 1)` for counting.
- Store the first index for maximum distance; store frequency for number of ways.
- State that hashing complexities are average/expected unless discussing worst case.
