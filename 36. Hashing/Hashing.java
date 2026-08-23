import java.util.*;

public class Hashing {

    // ================================================================
    // HASHMAP BASICS
    // HashMap stores key-value pairs. Keys are unique; values may repeat.
    // put/get/containsKey/remove are O(1) on average.
    // Iteration order is not guaranteed.
    // ================================================================
    static void hashMapDemo() {
        HashMap<String, Integer> population = new HashMap<>();

        population.put("India", 140);
        population.put("China", 141);
        population.put("USA", 34);
        population.put("India", 142); // Existing key: replaces its old value.

        System.out.println("India: " + population.get("India"));
        System.out.println("Contains USA? " + population.containsKey("USA"));

        // entrySet gives both the key and value without doing another lookup.
        for (Map.Entry<String, Integer> entry : population.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    // ================================================================
    // ORDERED MAPS AND SETS
    // LinkedHashMap/LinkedHashSet keep insertion order.
    // TreeMap/TreeSet keep elements sorted.
    // ================================================================
    static void orderedCollectionsDemo() {
        Map<String, Integer> insertionOrder = new LinkedHashMap<>();
        insertionOrder.put("B", 2);
        insertionOrder.put("A", 1);
        insertionOrder.put("C", 3);

        Map<String, Integer> sortedKeys = new TreeMap<>(insertionOrder);
        System.out.println("LinkedHashMap: " + insertionOrder); // B, A, C
        System.out.println("TreeMap: " + sortedKeys);           // A, B, C

        Set<Integer> insertionOrderSet = new LinkedHashSet<>();
        insertionOrderSet.add(3);
        insertionOrderSet.add(1);
        insertionOrderSet.add(2);

        Set<Integer> sortedSet = new TreeSet<>(insertionOrderSet);
        System.out.println("LinkedHashSet: " + insertionOrderSet); // 3, 1, 2
        System.out.println("TreeSet: " + sortedSet);                // 1, 2, 3
    }

    // ================================================================
    // HASHSET BASICS AND ITERATION
    // HashSet stores unique values and has no guaranteed order.
    // add/contains/remove are O(1) on average.
    // ================================================================
    static void hashSetDemo() {
        HashSet<Integer> numbers = new HashSet<>();
        numbers.add(2);
        numbers.add(1);
        numbers.add(2); // Duplicate: the set remains unchanged.

        System.out.println("Contains 1? " + numbers.contains(1));
        numbers.remove(1);

        System.out.print("Enhanced for-loop: ");
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();

        System.out.print("Iterator: ");
        Iterator<Integer> iterator = numbers.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    /*
     * Problem:
     * Return all values that occur more than n/3 times.
     *
     * Pattern:
     * Frequency Counting with HashMap
     *
     * Approach:
     * 1. Count the frequency of every number.
     * 2. Check each number-frequency pair.
     * 3. Add numbers whose frequency is greater than n/3.
     *
     * Time: O(n) average
     * Space: O(n)
     */
    static List<Integer> majorityElements(int[] nums) {
        HashMap<Integer, Integer> frequency = new HashMap<>();

        for (int number : nums) {
            frequency.put(number, frequency.getOrDefault(number, 0) + 1);
        }

        List<Integer> answer = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > nums.length / 3) {
                answer.add(entry.getKey());
            }
        }
        return answer;
    }

    /*
     * Problem:
     * Check whether two strings contain exactly the same character counts.
     *
     * Pattern:
     * Frequency Counting with HashMap
     *
     * Approach:
     * 1. Different lengths cannot be anagrams.
     * 2. Count every character in the first string.
     * 3. Decrease the matching count for the second string.
     * 4. A missing character or zero count means the strings differ.
     *
     * Time: O(n) average
     * Space: O(k), where k is the number of distinct characters
     */
    static boolean isAnagram(String first, String second) {
        if (first.length() != second.length()) {
            return false;
        }

        HashMap<Character, Integer> frequency = new HashMap<>();
        for (char ch : first.toCharArray()) {
            frequency.put(ch, frequency.getOrDefault(ch, 0) + 1);
        }

        for (char ch : second.toCharArray()) {
            if (!frequency.containsKey(ch)) {
                return false;
            }

            if (frequency.get(ch) == 1) {
                frequency.remove(ch);
            } else {
                frequency.put(ch, frequency.get(ch) - 1);
            }
        }
        return frequency.isEmpty();
    }

    /*
     * Problem:
     * Count how many different values appear in an array.
     *
     * Pattern:
     * HashSet for Uniqueness
     *
     * Approach:
     * 1. Add every array value to a HashSet.
     * 2. Duplicate additions are ignored automatically.
     * 3. Return the set size.
     *
     * Time: O(n) average
     * Space: O(n)
     */
    static int countDistinct(int[] nums) {
        HashSet<Integer> unique = new HashSet<>();
        for (int number : nums) {
            unique.add(number);
        }
        return unique.size();
    }

    static class UnionIntersectionResult {
        Set<Integer> union;
        Set<Integer> intersection;

        UnionIntersectionResult(Set<Integer> union, Set<Integer> intersection) {
            this.union = union;
            this.intersection = intersection;
        }

        @Override
        public String toString() {
            return "union=" + union + ", intersection=" + intersection;
        }
    }

    /*
     * Problem:
     * Find the distinct union and distinct intersection of two arrays.
     *
     * Pattern:
     * HashSet for Membership and Uniqueness
     *
     * Approach:
     * 1. Put the first array in a set.
     * 2. Copy that set for the union, then add the second array.
     * 3. Add a second-array value to the intersection if it is in the first set.
     * 4. Sets automatically prevent duplicate answers.
     *
     * Time: O(n + m) average
     * Space: O(n + m)
     */
    static UnionIntersectionResult unionAndIntersection(int[] first, int[] second) {
        Set<Integer> firstValues = new HashSet<>();
        for (int number : first) {
            firstValues.add(number);
        }

        Set<Integer> union = new HashSet<>(firstValues);
        Set<Integer> intersection = new HashSet<>();

        for (int number : second) {
            union.add(number);
            if (firstValues.contains(number)) {
                intersection.add(number);
            }
        }
        return new UnionIntersectionResult(union, intersection);
    }

    /*
     * Problem:
     * Reconstruct a single trip from source-destination ticket pairs.
     * Each city has at most one outgoing ticket and the tickets form one path.
     *
     * Pattern:
     * HashMap + Find the Unique Starting Point
     *
     * Approach:
     * 1. Store every destination city in a HashSet.
     * 2. The start is the source that never appears as a destination.
     * 3. Follow source -> destination links until the path ends.
     *
     * Time: O(n) average
     * Space: O(n)
     */
    static List<String> findItinerary(Map<String, String> tickets) {
        HashSet<String> destinations = new HashSet<>(tickets.values());
        String start = null;

        for (String source : tickets.keySet()) {
            if (!destinations.contains(source)) {
                start = source;
                break;
            }
        }

        List<String> route = new ArrayList<>();
        if (start == null) {
            return route; // Empty input or invalid data with no unique start.
        }

        route.add(start);
        while (tickets.containsKey(start)) {
            start = tickets.get(start);
            route.add(start);
        }
        return route;
    }

    /*
     * Problem:
     * Find the length of the longest contiguous subarray whose sum is 0.
     *
     * Pattern:
     * Prefix Sum + HashMap
     *
     * Approach:
     * 1. Keep a running prefix sum.
     * 2. Store the first index where each prefix sum appears.
     * 3. If the same sum appears again, values between the indexes sum to 0.
     * 4. Use the distance between indexes to update the longest length.
     *
     * Time: O(n) average
     * Space: O(n)
     */
    static int largestZeroSumSubarray(int[] nums) {
        HashMap<Integer, Integer> firstIndex = new HashMap<>();
        firstIndex.put(0, -1); // Allows a zero-sum subarray starting at index 0.

        int prefixSum = 0;
        int longest = 0;

        for (int i = 0; i < nums.length; i++) {
            prefixSum += nums[i];

            if (firstIndex.containsKey(prefixSum)) {
                longest = Math.max(longest, i - firstIndex.get(prefixSum));
            } else {
                // Keep only the earliest index to get the longest distance later.
                firstIndex.put(prefixSum, i);
            }
        }
        return longest;
    }

    /*
     * Problem:
     * Count contiguous subarrays whose sum equals k.
     *
     * Pattern:
     * Prefix Sum + Frequency HashMap
     *
     * Approach:
     * 1. Keep a running prefix sum.
     * 2. A valid earlier prefix must equal currentSum - k.
     * 3. Add how many times that earlier prefix has appeared.
     * 4. Store the current prefix sum for future subarrays.
     *
     * Time: O(n) average
     * Space: O(n)
     */
    static int countSubarraysWithSumK(int[] nums, int k) {
        HashMap<Integer, Integer> prefixFrequency = new HashMap<>();
        prefixFrequency.put(0, 1); // One empty prefix exists before the array.

        int prefixSum = 0;
        int count = 0;

        for (int number : nums) {
            prefixSum += number;
            count += prefixFrequency.getOrDefault(prefixSum - k, 0);
            prefixFrequency.put(prefixSum,
                    prefixFrequency.getOrDefault(prefixSum, 0) + 1);
        }
        return count;
    }

    // ================================================================
    // SIMPLE HASHMAP IMPLEMENTATION (SEPARATE CHAINING)
    // This learning version supports put, get, containsKey, and remove.
    // Colliding keys are stored together in the same LinkedList bucket.
    // ================================================================
    static class SimpleHashMap<K, V> {
        private static class Node<K, V> {
            K key;
            V value;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        private LinkedList<Node<K, V>>[] buckets;
        private int size;

        @SuppressWarnings({"unchecked", "rawtypes"})
        SimpleHashMap() {
            buckets = new LinkedList[4];
            createEmptyBuckets();
        }

        private void createEmptyBuckets() {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new LinkedList<>();
            }
        }

        // Convert any hash code to a valid bucket index.
        private int bucketIndex(K key) {
            return (key == null) ? 0 : (key.hashCode() & 0x7fffffff) % buckets.length;
        }

        private int nodeIndex(K key, int bucketIndex) {
            LinkedList<Node<K, V>> bucket = buckets[bucketIndex];
            for (int i = 0; i < bucket.size(); i++) {
                if (Objects.equals(bucket.get(i).key, key)) {
                    return i;
                }
            }
            return -1;
        }

        // Average O(1); O(n) worst case if many keys collide.
        void put(K key, V value) {
            int bucketIndex = bucketIndex(key);
            int nodeIndex = nodeIndex(key, bucketIndex);

            if (nodeIndex == -1) {
                buckets[bucketIndex].add(new Node<>(key, value));
                size++;
            } else {
                buckets[bucketIndex].get(nodeIndex).value = value;
            }

            // Load factor > 2 means buckets are getting crowded.
            if ((double) size / buckets.length > 2.0) {
                rehash();
            }
        }

        // Average O(1); O(n) worst case.
        V get(K key) {
            int bucketIndex = bucketIndex(key);
            int nodeIndex = nodeIndex(key, bucketIndex);
            return nodeIndex == -1 ? null : buckets[bucketIndex].get(nodeIndex).value;
        }

        boolean containsKey(K key) {
            int bucketIndex = bucketIndex(key);
            return nodeIndex(key, bucketIndex) != -1;
        }

        // Average O(1); O(n) worst case.
        V remove(K key) {
            int bucketIndex = bucketIndex(key);
            int nodeIndex = nodeIndex(key, bucketIndex);
            if (nodeIndex == -1) {
                return null;
            }

            size--;
            return buckets[bucketIndex].remove(nodeIndex).value;
        }

        int size() {
            return size;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private void rehash() {
            LinkedList<Node<K, V>>[] oldBuckets = buckets;
            buckets = new LinkedList[oldBuckets.length * 2];
            createEmptyBuckets();
            size = 0;

            for (LinkedList<Node<K, V>> bucket : oldBuckets) {
                for (Node<K, V> node : bucket) {
                    put(node.key, node.value);
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("--- HashMap demo ---");
        hashMapDemo();

        System.out.println("\n--- Ordered collections ---");
        orderedCollectionsDemo();

        System.out.println("\n--- HashSet demo ---");
        hashSetDemo();

        System.out.println("\n--- Problems ---");
        System.out.println("Majority (> n/3): "
                + majorityElements(new int[]{1, 3, 2, 5, 1, 3, 1, 5, 1}));
        System.out.println("Anagram: " + isAnagram("race", "care"));
        System.out.println("Distinct: " + countDistinct(new int[]{4, 3, 2, 5, 6, 7, 3, 4, 2, 1}));
        System.out.println("Union/intersection: "
                + unionAndIntersection(new int[]{7, 3, 9}, new int[]{6, 3, 9, 2, 9, 4}));

        Map<String, String> tickets = new HashMap<>();
        tickets.put("Chennai", "Bengaluru");
        tickets.put("Mumbai", "Delhi");
        tickets.put("Goa", "Chennai");
        tickets.put("Delhi", "Goa");
        System.out.println("Itinerary: " + findItinerary(tickets));

        System.out.println("Largest zero-sum length: "
                + largestZeroSumSubarray(new int[]{15, -2, 2, -8, 1, 7, 10, 23}));
        System.out.println("Subarrays with sum -10: "
                + countSubarraysWithSumK(new int[]{10, 2, -2, -20, 10}, -10));

        SimpleHashMap<String, Integer> map = new SimpleHashMap<>();
        map.put("India", 142);
        map.put("USA", 34);
        map.put("India", 143);
        System.out.println("SimpleHashMap India: " + map.get("India"));
    }
}
