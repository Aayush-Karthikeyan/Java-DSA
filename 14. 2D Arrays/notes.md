2D Arrays — Sigma 10 Notes

1. Linear Search in 2D Array

Goal: find a key by checking every cell.

java
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        if (matrix[i][j] == key) {
            return true;
        }
    }
}
return false;

TermMeaningirow index (top → bottom)jcolumn index (left → right)matrix[i][j]value at row i, column j

🧠 Interview relevance: low on its own — it's the brute force. Interviewers expect you to recognize when the matrix is sorted and switch to staircase search instead.

⚠️ Trap: time complexity is O(n×m) — don't call this "efficient" out loud in an interview.


2. Print Matrix in Spiral Order

Goal: print cells in a spiral, outer ring to inner ring.

4 boundary variables (the "unvisited box"):


startRow / endRow — top and bottom row still left to visit
startCol / endCol — left and right column still left to visit


4 directions per loop, in order:

DirectionFixedMovingRangeTopstartRowcolstartCol → endColRightendColrowstartRow+1 → endRowBottomendRowcolendCol-1 → startColLeftstartColrowendRow-1 → startRow+1

After all 4: shrink inward — startRow++, startCol++, endRow--, endCol--. Loop while startRow <= endRow && startCol <= endCol.

🧠 Interview relevance: high. Common at FAANG/mid-size companies, tests boundary tracking under pressure.

⚠️ Trap: this version has no safety checks, so it can double-print or break on non-square matrices (single row/col). Square matrices only, unless you add if guards before bottom/left.

💡 Tip: the "+1" and "-1" on right/bottom/left exist purely to avoid re-printing corners already done by the previous direction.


3. Diagonal Sum (Primary + Secondary)

Goal: sum both diagonals of a square matrix.

javafor (int i = 0; i < matrix.length; i++) {
    sum += matrix[i][i];                          // primary diagonal (pd)
    if (i != matrix.length - 1 - i) {
        sum += matrix[i][matrix.length - 1 - i];   // secondary diagonal (sd)
    }
}

DiagonalIndex patternPrimary (pd)matrix[i][i] — top-left to bottom-rightSecondary (sd)matrix[i][n-1-i] — top-right to bottom-left

🧠 Interview relevance: rare standalone, but the index pattern (n-1-i) shows up in other matrix problems too — worth knowing cold.

⚠️ Trap: only works correctly on square matrices — matrix.length is used for both row and column count.

⚠️ Trap: without the if check, odd-sized matrices (3x3, 5x5...) double-count the center cell — it sits on both diagonals.


4. Search in Sorted Matrix (Staircase Search)

Goal: find a key in a matrix that's sorted row-wise and column-wise, in better than O(n×m).

Requires: matrix sorted left→right per row AND top→bottom per column.

javaint row = 0, col = matrix[0].length - 1;   // start top-right corner

while (row < matrix.length && col >= 0) {
    if (matrix[row][col] == key) return true;
    else if (key < matrix[row][col]) col--;   // too big, move left
    else row++;                                // too small, move down
}
return false;

🧠 Interview relevance: asked fairly often — binary-search-style elimination on a 2D structure. Good ROI for prep time.

💡 Tip: the trick is starting at the top-right corner — it's the only cell where moving in one direction strictly decreases the value and the other strictly increases it. Starting top-left or bottom-right doesn't give you that clean split.

⚠️ Trap: this only works if the matrix is actually sorted this way — don't apply it blindly to any matrix.

Time complexity: O(n + m) — much better than linear search's O(n×m).


Quick comparison

ProblemTimeNeeds sorted?Interview weightLinear searchO(n·m)NoLowSpiral printO(n·m)NoHighDiagonal sumO(n)Square matrixLowStaircase searchO(n+m)YesHigh