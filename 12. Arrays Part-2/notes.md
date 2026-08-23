# Trapping Rainwater 🌧️

## The Question
Given bar heights, it rains. Count total units of water trapped in the dips.
`[4,2,0,6,3,2,5]` → **11**

## The One Insight 🧠
Water on top of ANY single bar:
> **min(tallest left wall, tallest right wall) − own height**

- Water rises only to the **shorter** wall (spills over the lower side).
- Subtract the bar's own height (it takes up space).
- A bar needs taller walls on **both** sides to hold water.

## Approaches

| Approach      | Idea                                   | Time  | Space |
|---------------|----------------------------------------|-------|-------|
| Brute force   | For each bar, scan left + right        | O(n²) | O(1)  |
| Prefix arrays | Precompute leftMax[] & rightMax[]      | O(n)  | O(n)  |
| Two pointers  | Walk inward from both ends (optimal)   | O(n)  | O(1)  |

## Prefix-Array Solution (3 passes)
1. **leftMax** (left→right): `leftMax[i] = max(height[i], leftMax[i-1])`
2. **rightMax** (right→left): `rightMax[i] = max(height[i], rightMax[i+1])`
3. **count**: `water += min(leftMax[i], rightMax[i]) − height[i]`

First two passes cache the walls once → third pass is pure lookup.

## Trace: [4,2,0,6,3,2,5]
- leftMax  = [4,4,4,6,6,6,6]
- rightMax = [6,6,6,6,5,5,5]
- water    = [0,2,4,0,2,3,0] → **11**

## Traps ⚠️
- `leftMax`/`rightMax` include the bar itself → `level − height[i]` is never negative (worst case 0). Safe to add directly.
- leftMax walks forward, rightMax walks BACKWARD — easy to mix up the seed (`[0]` vs `[n-1]`) and the loop direction.

## Pattern to Remember 💡
"Answer at position i depends on the max to my LEFT and max to my RIGHT"
→ precompute both sides, then one pass.
Same skeleton as: **Product of Array Except Self**.

## Interview Note
- LeetCode #42, rated **Hard**.
- Medium priority for Canadian SWE loops — know it, don't over-drill it.
- Strong answer narrates: brute O(n²) → prefix O(n) → two-pointer O(1) space.