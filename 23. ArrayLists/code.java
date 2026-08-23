import java.util.ArrayList;
import java.util.Collections;

public class code {

    // ================================================================
    // TOPIC: ArrayLists in Java
    // ArrayList = resizable array. Grows/shrinks automatically.
    // Only holds objects (use Integer, not int).
    // ================================================================


    // ===== SWAP 2 NUMBERS by index =====
    static void swap(ArrayList<Integer> list, int idx1, int idx2) {
        int temp = list.get(idx1);              // save idx1's value
        list.set(idx1, list.get(idx2));         // put idx2's value into idx1
        list.set(idx2, temp);                   // put saved value into idx2
    }


    // ===== CONTAINER WITH MOST WATER — Brute Force O(n²) =====
    // Try every pair (i, j). Water = min height × width.
    static int storeWaterBrute(ArrayList<Integer> height) {
        int maxWater = 0;
        for (int i = 0; i < height.size(); i++) {
            for (int j = i + 1; j < height.size(); j++) {
                int ht = Math.min(height.get(i), height.get(j)); // shorter wall is the limit
                int width = j - i;                                // distance between walls
                int currWater = ht * width;
                maxWater = Math.max(maxWater, currWater);
            }
        }
        return maxWater;
    }


    // ===== CONTAINER WITH MOST WATER — Two Pointer O(n) =====
    // Start with widest container (lp=0, rp=end).
    // Move the pointer with the SHORTER wall inward — shorter wall is the bottleneck.
    static int storeWater(ArrayList<Integer> height) {
        int lp = 0, rp = height.size() - 1;
        int maxWater = 0;
        while (lp < rp) {
            int ht = Math.min(height.get(lp), height.get(rp)); // shorter wall limits water
            int width = rp - lp;
            int currWater = ht * width;
            maxWater = Math.max(maxWater, currWater);
            if (height.get(lp) < height.get(rp)) lp++;  // left wall shorter → move lp inward
            else rp--;                                    // right wall shorter → move rp inward
        }
        return maxWater;
    }


    // ===== PAIR SUM 1 — Brute Force O(n²) =====
    // Check every pair to see if any sum equals target.
    static boolean pairSum1Brute(ArrayList<Integer> list, int target) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) + list.get(j) == target) return true;
            }
        }
        return false;
    }


    // ===== PAIR SUM 1 — Two Pointer O(n). List must be SORTED. =====
    // lp starts at smallest, rp starts at largest.
    // Sum too small → lp++ (need bigger). Sum too big → rp-- (need smaller).
    static boolean pairSum1(ArrayList<Integer> list, int target) {
        int lp = 0, rp = list.size() - 1;
        while (lp != rp) {
            if (list.get(lp) + list.get(rp) == target) return true;       // case 1: found
            if (list.get(lp) + list.get(rp) < target)  lp++;              // case 2: too small
            else                                        rp--;              // case 3: too big
        }
        return false;
    }


    // ===== PAIR SUM 2 — Two Pointer on a SORTED + ROTATED array =====
    // e.g. [4, 5, 6, 7, 1, 2, 3] — sorted but rotated at some point.
    //
    // Step 1: Find the "breaking point" (bp) — where arr[i] > arr[i+1].
    //         That's where the array wraps from max back to min.
    //
    // Step 2: lp = bp+1 (the smallest element, just after the break)
    //         rp = bp   (the largest element, at the break)
    //
    // Step 3: Move pointers in a CIRCULAR manner using modular arithmetic.
    //         lp moves right: (lp+1) % n
    //         rp moves left:  (n + rp - 1) % n
    static boolean pairSum2(ArrayList<Integer> list, int target) {
        int n = list.size();

        // find breaking point
        int bp = -1;
        for (int i = 0; i < n - 1; i++) {
            if (list.get(i) > list.get(i + 1)) { // array drops here — this is the break
                bp = i;
                break;
            }
        }

        int lp = bp + 1;  // smallest element (just after break)
        int rp = bp;      // largest element (at break)

        while (lp != rp) {
            if (list.get(lp) + list.get(rp) == target) return true;
            if (list.get(lp) + list.get(rp) < target)  lp = (lp + 1) % n;       // move lp right (circular)
            else                                        rp = (n + rp - 1) % n;   // move rp left  (circular)
        }
        return false;
    }


    public static void main(String[] args) {

        // ----- 1. Introduction — Create an ArrayList -----
        ArrayList<Integer> list = new ArrayList<>();   // <Integer> — can't use primitive int
        System.out.println("Empty list: " + list);    // []


        // ----- 2. Operations -----
        list.add(3);
        list.add(2);
        list.add(5);
        list.add(1);
        list.add(4);
        System.out.println("After adds: " + list);    // [3, 2, 5, 1, 4]

        list.add(1, 10);                              // add(index, value) — inserts at index 1
        System.out.println("After add(1,10): " + list);

        list.remove(Integer.valueOf(10));             // remove by VALUE — must wrap in Integer
        list.remove(0);                               // remove by INDEX
        System.out.println("After removes: " + list);

        list.set(0, 99);                              // set(index, value) — replace element
        System.out.println("After set(0,99): " + list);

        System.out.println("get(1): "      + list.get(1));
        System.out.println("contains 99: " + list.contains(99));
        System.out.println("indexOf 5: "   + list.indexOf(5));


        // ----- 3. Size -----
        System.out.println("Size: " + list.size());   // size() — NOT .length


        // ----- 4. Print Reverse -----
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(1); nums.add(2); nums.add(3); nums.add(4); nums.add(5);
        System.out.print("Reverse: ");
        for (int i = nums.size() - 1; i >= 0; i--) { // start from last index, go to 0
            System.out.print(nums.get(i) + " ");
        }
        System.out.println();


        // ----- 5. Find Maximum -----
        // Start with smallest possible value so any element in the list beats it
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.size(); i++) {
            if (max < nums.get(i)) max = nums.get(i);
        }
        System.out.println("max element = " + max);  // 5


        // ----- 6. Swap 2 Numbers -----
        System.out.println("Before swap: " + nums);   // [1, 2, 3, 4, 5]
        int idx1 = 1, idx2 = 3;
        swap(nums, idx1, idx2);
        System.out.println("After swap(1,3): " + nums); // [1, 4, 3, 2, 5]


        // ----- 7. Sorting -----
        ArrayList<Integer> toSort = new ArrayList<>();
        toSort.add(2); toSort.add(5); toSort.add(9); toSort.add(3); toSort.add(6);
        System.out.println("Before sort: " + toSort);
        Collections.sort(toSort);                            // ascending
        System.out.println("Ascending:   " + toSort);
        Collections.sort(toSort, Collections.reverseOrder()); // descending
        System.out.println("Descending:  " + toSort);


        // ----- 8. Multi-dimensional ArrayList (ArrayList of ArrayLists) -----
        ArrayList<ArrayList<Integer>> mainList = new ArrayList<>();

        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1); list1.add(2);
        mainList.add(list1);

        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(3); list2.add(4);
        mainList.add(list2);

        // Print all elements
        for (int i = 0; i < mainList.size(); i++) {
            ArrayList<Integer> currList = mainList.get(i);
            for (int j = 0; j < currList.size(); j++) {
                System.out.print(currList.get(j) + " ");
            }
        }
        System.out.println();


        // ----- 9. Container with Most Water -----
        ArrayList<Integer> height = new ArrayList<>();
        for (int h : new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}) height.add(h);

        System.out.println("Container (brute):      " + storeWaterBrute(height)); // 49
        System.out.println("Container (2 pointer):  " + storeWater(height));      // 49


        // ----- 10. Pair Sum 1 — sorted list -----
        ArrayList<Integer> sorted = new ArrayList<>();
        for (int v : new int[]{1, 2, 3, 4, 5, 6}) sorted.add(v);

        System.out.println("Pair sum=9 (brute):     " + pairSum1Brute(sorted, 9)); // true
        System.out.println("Pair sum=9 (2 pointer): " + pairSum1(sorted, 9));      // true
        System.out.println("Pair sum=15(2 pointer): " + pairSum1(sorted, 15));     // false


        // ----- 11. Pair Sum 2 — sorted + ROTATED list -----
        // [4, 5, 6, 7, 1, 2, 3] — sorted but rotated. Breaking point is at index 3 (7→1).
        ArrayList<Integer> rotated = new ArrayList<>();
        for (int v : new int[]{4, 5, 6, 7, 1, 2, 3}) rotated.add(v);

        System.out.println("Pair sum=9 in rotated:  " + pairSum2(rotated, 9));  // true (2+7)
        System.out.println("Pair sum=20 in rotated: " + pairSum2(rotated, 20)); // false
    }
}
