import java.util.*;

// ================================================================
// TOPIC: Greedy Algorithms
// Greedy means: at every step, choose the option that looks best
// right now. This works only when local best choices lead to a
// globally best answer for that problem.
// ================================================================

public class GreedyAlgorithms {

    // Small helper class for Fractional Knapsack.
    // ratio = profit per unit weight, so higher ratio is better.
    static class Item {
        int value;
        int weight;
        double ratio;

        Item(int value, int weight) {
            this.value = value;
            this.weight = weight;
            this.ratio = value / (double) weight;
        }
    }

    // Small helper class for Job Sequencing.
    // deadline = latest slot where this job can be done.
    static class Job {
        char id;
        int deadline;
        int profit;

        Job(char id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }
    }

    // ================================================================
    // 1. ACTIVITY SELECTION
    // Pick the maximum number of non-overlapping activities.
    // Greedy choice: always pick the activity that finishes earliest.
    // Time: O(n log n) because we sort by end time.
    // ================================================================
    static ArrayList<Integer> activitySelection(int[] start, int[] end) {
        int n = start.length;

        int[][] activities = new int[n][3];
        for (int i = 0; i < n; i++) {
            activities[i][0] = i;        // original activity number
            activities[i][1] = start[i]; // start time
            activities[i][2] = end[i];   // end time
        }

        // Sort by finishing time. The activity that ends first leaves
        // the maximum remaining time for future activities.
        Arrays.sort(activities, Comparator.comparingInt(a -> a[2]));

        ArrayList<Integer> selected = new ArrayList<>();
        selected.add(activities[0][0]);
        int lastEnd = activities[0][2];

        for (int i = 1; i < n; i++) {
            int currStart = activities[i][1];
            int currEnd = activities[i][2];

            // If current activity starts after the last selected one ends,
            // they do not overlap, so we can safely take it.
            if (currStart >= lastEnd) {
                selected.add(activities[i][0]);
                lastEnd = currEnd;
            }
        }

        return selected;
    }

    // ================================================================
    // 2. FRACTIONAL KNAPSACK
    // We can take full items or fractions of items.
    // Greedy choice: take highest value/weight ratio first.
    // Time: O(n log n)
    // ================================================================
    static double fractionalKnapsack(int[] values, int[] weights, int capacity) {
        Item[] items = new Item[values.length];
        for (int i = 0; i < values.length; i++) {
            items[i] = new Item(values[i], weights[i]);
        }

        // Descending order of ratio: most valuable per kg comes first.
        Arrays.sort(items, (a, b) -> Double.compare(b.ratio, a.ratio));

        double finalValue = 0.0;
        int remainingCapacity = capacity;

        for (Item item : items) {
            if (remainingCapacity == 0) {
                break;
            }

            if (item.weight <= remainingCapacity) {
                // Full item fits.
                finalValue += item.value;
                remainingCapacity -= item.weight;
            } else {
                // Only part of this item fits, so take exactly the leftover
                // capacity worth of value.
                finalValue += item.ratio * remainingCapacity;
                remainingCapacity = 0;
            }
        }

        return finalValue;
    }

    // ================================================================
    // 3. MINIMUM SUM OF ABSOLUTE DIFFERENCE PAIRS
    // Pair elements from two arrays so total |a - b| is minimum.
    // Greedy choice: sort both arrays and pair same indexes.
    // Time: O(n log n)
    // ================================================================
    static int minAbsoluteDifferencePairs(int[] a, int[] b) {
        Arrays.sort(a);
        Arrays.sort(b);

        int minDiff = 0;
        for (int i = 0; i < a.length; i++) {
            minDiff += Math.abs(a[i] - b[i]);
        }

        return minDiff;
    }

    // ================================================================
    // 4. MAXIMUM LENGTH CHAIN OF PAIRS
    // A pair (a,b) can be followed by (c,d) only if b < c.
    // Greedy choice: sort pairs by ending value and pick earliest end.
    // Time: O(n log n)
    // ================================================================
    static int maxLengthChainOfPairs(int[][] pairs) {
        Arrays.sort(pairs, Comparator.comparingInt(p -> p[1]));

        int chainLength = 1;
        int chainEnd = pairs[0][1];

        for (int i = 1; i < pairs.length; i++) {
            if (pairs[i][0] > chainEnd) {
                chainLength++;
                chainEnd = pairs[i][1];
            }
        }

        return chainLength;
    }

    // ================================================================
    // 5. INDIAN COINS
    // Make a value using minimum number of coins.
    // Greedy choice: always use the largest coin possible.
    // Works for standard Indian currency denominations.
    // Time: O(number of denominations)
    // ================================================================
    static ArrayList<Integer> indianCoins(int amount) {
        Integer[] coins = {2000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
        ArrayList<Integer> usedCoins = new ArrayList<>();

        for (int coin : coins) {
            while (amount >= coin) {
                usedCoins.add(coin);
                amount -= coin;
            }
        }

        return usedCoins;
    }

    // ================================================================
    // 6. JOB SEQUENCING PROBLEM
    // Each job takes 1 unit of time. Do jobs before their deadlines
    // to get maximum profit.
    // Greedy choice: process jobs by highest profit first, then place
    // each job in the latest free slot before its deadline.
    // Time: O(n log n + n * maxDeadline)
    // ================================================================
    static ArrayList<Character> jobSequencing(Job[] jobs) {
        Arrays.sort(jobs, (a, b) -> b.profit - a.profit);

        int maxDeadline = 0;
        for (Job job : jobs) {
            maxDeadline = Math.max(maxDeadline, job.deadline);
        }

        char[] slots = new char[maxDeadline + 1]; // slot 0 is unused
        boolean[] occupied = new boolean[maxDeadline + 1];

        for (Job job : jobs) {
            // Try latest possible slot first. This keeps earlier slots free
            // for jobs with tighter deadlines.
            for (int slot = job.deadline; slot >= 1; slot--) {
                if (!occupied[slot]) {
                    occupied[slot] = true;
                    slots[slot] = job.id;
                    break;
                }
            }
        }

        ArrayList<Character> sequence = new ArrayList<>();
        for (int slot = 1; slot <= maxDeadline; slot++) {
            if (occupied[slot]) {
                sequence.add(slots[slot]);
            }
        }

        return sequence;
    }

    // ================================================================
    // 7. CHOCOLA PROBLEM
    // Cut a chocolate board into 1x1 pieces with minimum cost.
    // Greedy choice: always do the most expensive remaining cut first.
    // Reason: every cut cost is multiplied by number of pieces in the
    // opposite direction, so expensive cuts should get smaller multipliers.
    // Time: O(n log n + m log m)
    // ================================================================
    static int chocolaProblem(Integer[] horizontalCost, Integer[] verticalCost) {
        Arrays.sort(horizontalCost, Collections.reverseOrder());
        Arrays.sort(verticalCost, Collections.reverseOrder());

        int h = 0;
        int v = 0;
        int horizontalPieces = 1;
        int verticalPieces = 1;
        int totalCost = 0;

        while (h < horizontalCost.length && v < verticalCost.length) {
            if (horizontalCost[h] >= verticalCost[v]) {
                // A horizontal cut crosses all current vertical pieces.
                totalCost += horizontalCost[h] * verticalPieces;
                horizontalPieces++;
                h++;
            } else {
                // A vertical cut crosses all current horizontal pieces.
                totalCost += verticalCost[v] * horizontalPieces;
                verticalPieces++;
                v++;
            }
        }

        // Finish leftover horizontal cuts.
        while (h < horizontalCost.length) {
            totalCost += horizontalCost[h] * verticalPieces;
            horizontalPieces++;
            h++;
        }

        // Finish leftover vertical cuts.
        while (v < verticalCost.length) {
            totalCost += verticalCost[v] * horizontalPieces;
            verticalPieces++;
            v++;
        }

        return totalCost;
    }

    // ================================================================
    // MAIN: sample runs for every topic above
    // ================================================================
    public static void main(String[] args) {

        // ----- Activity Selection -----
        int[] start = {1, 3, 0, 5, 8, 5};
        int[] end = {2, 4, 6, 7, 9, 9};
        ArrayList<Integer> selectedActivities = activitySelection(start, end);
        System.out.println("Activity Selection: " + selectedActivities);

        // ----- Fractional Knapsack -----
        int[] values = {60, 100, 120};
        int[] weights = {10, 20, 30};
        int capacity = 50;
        System.out.println("Fractional Knapsack: " + fractionalKnapsack(values, weights, capacity));

        // ----- Minimum Sum Absolute Difference Pairs -----
        int[] a = {1, 2, 3};
        int[] b = {2, 1, 3};
        System.out.println("Min Absolute Difference: " + minAbsoluteDifferencePairs(a, b));

        // ----- Maximum Length Chain of Pairs -----
        int[][] pairs = {{5, 24}, {39, 60}, {5, 28}, {27, 40}, {50, 90}};
        System.out.println("Max Chain Length: " + maxLengthChainOfPairs(pairs));

        // ----- Indian Coins -----
        int amount = 590;
        ArrayList<Integer> coinsUsed = indianCoins(amount);
        System.out.println("Indian Coins for " + amount + ": " + coinsUsed);
        System.out.println("Number of coins: " + coinsUsed.size());

        // ----- Job Sequencing Problem -----
        Job[] jobs = {
                new Job('A', 4, 20),
                new Job('B', 1, 10),
                new Job('C', 1, 40),
                new Job('D', 1, 30)
        };
        System.out.println("Job Sequence: " + jobSequencing(jobs));

        // ----- Chocola Problem -----
        Integer[] horizontalCost = {4, 1, 2};    // costs for horizontal cuts
        Integer[] verticalCost = {2, 1, 3, 1, 4}; // costs for vertical cuts
        System.out.println("Chocola Minimum Cost: " + chocolaProblem(horizontalCost, verticalCost));
    }
}
