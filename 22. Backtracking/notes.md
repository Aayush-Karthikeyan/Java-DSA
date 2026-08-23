# 22. Backtracking — Key Notes

---

## What is Backtracking?
**Try a choice → recurse → UNDO the choice (backtrack).**

Used when you need to explore all possible solutions. If a path fails, you undo the last step and try a different path — like navigating a maze.

```
3 steps every time:
1. Make a choice
2. Recurse
3. Undo the choice  ← this is the "backtracking" step
```

---

## Types of Backtracking

| Type | What it does | Example |
|------|--------------|---------|
| **Decision** | Does a valid solution exist? | Sudoku solvable? |
| **Optimization** | Find best solution | Shortest path |
| **Enumeration** | Find ALL solutions | All permutations, subsets |

---

## Backtracking on Arrays
Simple demo: add 2 to each element going forward, subtract 2 coming back.
```java
arr[i] += 2;              // make change
changeArray(arr, i + 1);  // recurse
arr[i] -= 2;              // undo (backtrack)
```
After the call, the array is fully restored — this is the backtracking guarantee.

---

## Find All Subsets
At each index: **include** the element OR **skip** it → 2 choices per element → **2ⁿ subsets total**.
```java
findSubsets(arr, i + 1, current + arr[i]);   // include arr[i]
findSubsets(arr, i + 1, current);            // skip arr[i]
```
```
[1,2,3] → 123, 12, 13, 1, 23, 2, 3, ""   (8 subsets)
```
**Time:** O(2ⁿ) &nbsp; **Space:** O(n)

---

## Find All Permutations
Use a `boolean[] used` array. Loop through all elements — if not used, pick it, recurse, then unmark.
```java
for (int i = 0; i < arr.length; i++) {
    if (!used[i]) {
        used[i] = true;                            // mark
        findPermutations(arr, used, current + arr[i]); // recurse
        used[i] = false;                           // UNMARK (backtrack)
    }
}
```
```
[1,2,3] → 123, 132, 213, 231, 312, 321   (6 = 3! permutations)
```
**Time:** O(n × n!) &nbsp; **Space:** O(n)

---

## N-Queens
Place N queens on an N×N board so none can attack each other (no shared row, column, or diagonal).

**Strategy:** place one queen per row. For each row, try every column:
- Check if safe → place queen → recurse on next row → remove queen (backtrack).

**`isSafe` checks (only look ABOVE current row):**
- Same column above
- Upper-left diagonal
- Upper-right diagonal

```java
board[row][col] = 'Q';               // place
count += nQueensCount(board, row+1); // recurse
board[row][col] = '.';               // backtrack
```

| N | Solutions |
|---|-----------|
| 4 | 2 |
| 8 | 92 |

**Time:** O(n!) &nbsp; **Space:** O(n²)

---

## Grid Ways
Count paths from top-left (0,0) to bottom-right (m-1, n-1), moving only **right** or **down**.

```java
if (row == m-1 && col == n-1) return 1;   // reached destination
if (row >= m || col >= n) return 0;         // out of bounds

return gridWays(row, col+1, m, n)   // go right
     + gridWays(row+1, col, m, n);  // go down
```

### Grid Ways Trick — Math Formula
Instead of recursion, use combinations:
> Total paths = **C(m+n-2, m-1)** = (m+n-2)! / ((m-1)! × (n-1)!)

For a 3×3 grid: C(4, 2) = 6 paths.

**Why:** You always take exactly (m-1) down moves and (n-1) right moves. Choose which of the total (m+n-2) steps are the "down" moves.

**Time (recursive):** O(2^(m+n)) &nbsp; **Time (math trick):** O(m+n)

---

## Sudoku Solver
Fill 9×9 grid: every row, column, and 3×3 box must have digits 1–9 exactly once.

```
Find empty cell ('.')
  Try digits '1' to '9':
    If valid → place digit → recurse
    If recursion returns false → remove digit (backtrack)
  If no digit works → return false (tell caller to backtrack)
Return true when no empty cell remains (solved!)
```

**`isValidSudoku` checks in one loop:**
```java
for (int i = 0; i < 9; i++) {
    if (board[row][i] == c) return false;       // same row
    if (board[i][col] == c) return false;       // same column
    int boxRow = 3*(row/3) + i/3;
    int boxCol = 3*(col/3) + i%3;
    if (board[boxRow][boxCol] == c) return false; // same 3×3 box
}
```
> The `3*(row/3)` gives the top-left row of the box. `i/3` and `i%3` walk through all 9 cells in that box.

**Time:** O(9^(empty cells)) — worst case huge, but constraints prune it fast.

---

## Summary

| Problem | Key Idea | Time |
|---------|----------|------|
| Subsets | include/skip at each index | O(2ⁿ) |
| Permutations | boolean[] used, try all, unmark | O(n × n!) |
| N-Queens | row by row, isSafe check | O(n!) |
| Grid Ways | right or down at each cell | O(2^(m+n)) |
| Grid Ways Trick | C(m+n-2, m-1) formula | O(m+n) |
| Sudoku | try 1–9 per empty cell | O(9^cells) |

---

## Common Pitfalls
1. **Forgetting to undo the change** — the entire point of backtracking is restoring state after recursion.
2. **`isSafe` only checks above current row** in N-Queens — rows below aren't filled yet so no need to check them.
3. **Sudoku box formula:** `3*(row/3) + i/3` and `3*(col/3) + i%3` — integer division gives the box's top-left corner.
4. **Subsets include the empty string** — 2ⁿ subsets means the empty set is one of them.
