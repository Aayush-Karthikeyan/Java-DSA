public class TrappingRainwater {

    // ============================================================
    // TRAPPING RAINWATER — PREFIX ARRAYS — O(n) time, O(n) space
    // water[i] = min(tallest left wall, tallest right wall) - height[i]
    // ============================================================
    public static int trappedRainwater(int height[]) {
        int n = height.length;

        // --- Build leftMax: tallest wall to the left (walk left -> right) ---
        int leftMax[] = new int[n];
        leftMax[0] = height[0];                 // nothing to the left of bar 0
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(height[i], leftMax[i - 1]);
        }

        // --- Build rightMax: tallest wall to the right (walk right -> left) ---
        int rightMax[] = new int[n];
        rightMax[n - 1] = height[n - 1];        // nothing to the right of last bar
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(height[i], rightMax[i + 1]);
        }

        // --- Count water: shorter wall minus own height, per bar ---
        int trappedWater = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(leftMax[i], rightMax[i]);
            trappedWater += waterLevel - height[i];
        }

        return trappedWater;
    }

    public static void main(String[] args) {

        // === EXAMPLE 1: classic case ===
        // int height[] = {4, 2, 0, 6, 3, 2, 5};
        // System.out.println(trappedRainwater(height)); // 11

        // === EXAMPLE 2: no water trapped (only goes up, then nothing) ===
        // int height[] = {1, 2, 3, 4};
        // System.out.println(trappedRainwater(height)); // 0

        // === EXAMPLE 3: simple valley ===
        // int height[] = {3, 0, 3};
        // System.out.println(trappedRainwater(height)); // 3
    }
}