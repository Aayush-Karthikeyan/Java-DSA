# 29. Greedy Algorithms - Key Notes

---

## What is Greedy?

Greedy algorithms build the answer step by step by choosing the best-looking option at the current step.

The important interview question is not just "what do I choose?", but "why is this local choice safe?"

Greedy usually works when:
- A local best choice can be part of a global best answer.
- After making that choice, the remaining problem has the same structure.
- Sorting helps reveal the best next choice.

Greedy does not work for every optimization problem. For example, 0/1 Knapsack cannot be solved by simply taking the highest ratio item first.

---

## Common Greedy Pattern

1. Decide the greedy rule.
2. Sort the input based on that rule.
3. Pick items while checking whether they are valid.
4. Track the answer.

```java
sort(input);
for (each option) {
    if (option is valid) {
        choose it;
        update answer;
    }
}
```

---

## Activity Selection

**Goal:** Select the maximum number of non-overlapping activities.

Each activity has a start time and an end time.

**Greedy rule:** Pick the activity that finishes earliest.

Why? The earlier an activity finishes, the more room it leaves for future activities.

Steps:
- Store each activity as `{index, start, end}`.
- Sort by `end`.
- Pick the first activity.
- Pick the next activity only if `start >= lastEnd`.

Time: `O(n log n)`  
Space: `O(n)`

Common pitfall:
- Sorting by start time does not guarantee the maximum number of activities.

---

## Fractional Knapsack

**Goal:** Maximize value inside a bag with limited capacity.

In fractional knapsack, you are allowed to take part of an item.

**Greedy rule:** Take items with the highest `value / weight` ratio first.

Steps:
- Calculate ratio for each item.
- Sort by ratio in descending order.
- Take full item if it fits.
- Otherwise, take only the fraction that fits.

Time: `O(n log n)`  
Space: `O(n)`

Important:
- This greedy method works for fractional knapsack.
- It does not work for 0/1 knapsack because you cannot take fractions there.

---

## Minimum Sum Absolute Difference Pairs

**Goal:** Pair elements of two arrays so the sum of absolute differences is minimum.

Example:

```text
A = [1, 2, 3]
B = [2, 1, 3]
After sorting:
A = [1, 2, 3]
B = [1, 2, 3]
Answer = 0
```

**Greedy rule:** Sort both arrays and pair same indexes.

Why? Pairing smaller with smaller and larger with larger avoids unnecessary large gaps.

Time: `O(n log n)`  
Space: `O(1)` if sorting in-place

Common pitfall:
- This changes the original order of arrays. If original order matters, copy the arrays first.

---

## Maximum Length Chain of Pairs

**Goal:** Find the longest chain of pairs.

A pair `(a, b)` can be followed by `(c, d)` only when:

```text
b < c
```

**Greedy rule:** Sort pairs by ending value, then pick the pair that ends earliest.

This is very similar to Activity Selection.

Steps:
- Sort pairs by second value.
- Pick the first pair.
- Pick next pair only if `nextStart > chainEnd`.

Time: `O(n log n)`  
Space: `O(1)` if sorting in-place

---

## Indian Coins

**Goal:** Make an amount using the minimum number of coins.

**Greedy rule:** Always take the largest coin possible.

For Indian currency:

```text
2000, 500, 200, 100, 50, 20, 10, 5, 2, 1
```

Example:

```text
Amount = 590
Coins = 500, 50, 20, 20
Number of coins = 4
```

Time: `O(number of coin types)`  
Space: `O(number of coins used)`

Important:
- Greedy works for standard Indian currency.
- Greedy does not work for every possible coin system.

---

## Job Sequencing Problem

**Goal:** Maximize profit by scheduling jobs before their deadlines.

Rules:
- Each job takes 1 unit of time.
- Each job has a deadline and profit.
- Only one job can be done in one time slot.

**Greedy rule:** Do higher profit jobs first.

But placement matters:
- Sort jobs by profit descending.
- For each job, place it in the latest free slot before its deadline.

Why latest slot? It keeps earlier slots open for jobs with smaller deadlines.

Time: `O(n log n + n * maxDeadline)`  
Space: `O(maxDeadline)`

Common pitfall:
- Do not always put a job in the earliest free slot. Use the latest free valid slot.

---

## Chocola Problem

**Goal:** Cut a chocolate board into `1 x 1` pieces with minimum cost.

There are horizontal cut costs and vertical cut costs.

**Greedy rule:** Always perform the most expensive remaining cut first.

Reason:
- A horizontal cut cost is multiplied by the current number of vertical pieces.
- A vertical cut cost is multiplied by the current number of horizontal pieces.
- Expensive cuts should happen while the multiplier is still small.

Steps:
- Sort horizontal costs in descending order.
- Sort vertical costs in descending order.
- Compare the next horizontal and vertical cut.
- Pick the larger cost first.
- Update the number of pieces in that direction.

Time: `O(n log n + m log m)`  
Space: `O(1)` besides sorting

Important variables:

```java
horizontalPieces = 1;
verticalPieces = 1;
```

If you make a horizontal cut:

```java
cost += horizontalCost * verticalPieces;
horizontalPieces++;
```

If you make a vertical cut:

```java
cost += verticalCost * horizontalPieces;
verticalPieces++;
```

---

## Quick Summary

| Problem | Greedy Rule | Complexity |
|---|---|---|
| Activity Selection | Earliest end time first | O(n log n) |
| Fractional Knapsack | Highest value/weight ratio first | O(n log n) |
| Min Absolute Difference | Sort both arrays, pair same indexes | O(n log n) |
| Max Chain of Pairs | Earliest pair end first | O(n log n) |
| Indian Coins | Largest coin first | O(coin types) |
| Job Sequencing | Highest profit first, latest free slot | O(n log n + nD) |
| Chocola | Highest cut cost first | O(n log n + m log m) |

---

## How to Recognize Greedy Problems

Look for phrases like:
- maximum number of non-overlapping things
- minimum coins
- maximize profit before deadlines
- minimum cost after repeated choices
- sort and choose best valid option

The real skill is deciding the sorting rule:
- Sort by end time for scheduling maximum count.
- Sort by ratio for fractional profit.
- Sort descending for largest-value choices.
- Sort both arrays when pairing values.

---

## Common Mistakes

1. Using greedy where dynamic programming is needed.
2. Sorting by the wrong field.
3. Forgetting to check validity before selecting an item.
4. In Job Sequencing, placing jobs in the earliest slot instead of the latest slot.
5. In Chocola, forgetting that horizontal cuts multiply by vertical pieces, and vertical cuts multiply by horizontal pieces.
6. In Fractional Knapsack, applying the same ratio method to 0/1 Knapsack.
