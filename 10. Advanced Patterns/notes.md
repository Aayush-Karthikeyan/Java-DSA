# 10. Advanced Patterns — Key Notes

> **Skim tier.** Pattern problems almost never appear in real
> interviews. What they *do* build is fluency with nested loops and
> index arithmetic, which everything else depends on. Read once,
> reproduce two or three, move on.

## The one idea behind every pattern

```text
outer loop  ->  controls ROWS
inner loop  ->  controls WHAT PRINTS on that row
```

Every pattern in this folder is the same skeleton. The only things that
change are:

1. **How many spaces** before the content on row `i`
2. **How many symbols** on row `i`
3. **Which symbol** gets printed

So don't memorize 9 programs — memorize **the per-row formula** for
each shape. That's the compressed fact worth keeping.

## The 4 skeletons

The 9 patterns collapse into 4 groups. Learn the group, and the
individual patterns are small variations.

### Group 1 — Border-condition patterns

Print a star only on the edge of the shape; leave the inside blank.

```java
if (i == 1 || i == n || j == 1 || j == n) print("* ");
else                                      print("  ");
```

| Pattern | Difference |
|---|---|
| Hollow Rectangle | Border test only |
| Hollow Rhombus | Same test **+ `n-i` leading spaces** to slant it |

> 💡 The blank branch prints **two** spaces (`"  "`) to match the width
> of `"* "` — otherwise the shape collapses sideways.

### Group 2 — Mirrored-half patterns

Build an upper half with a growing count, then a lower half with a
shrinking count.

| Pattern | Per row `i` |
|---|---|
| Butterfly | left stars `i`, middle spaces `2*(n-i)`, right stars `i` |
| Diamond | spaces `n-i`, stars `2*i - 1` |

> ⚠️ **Trap:** Diamond's lower half starts at `n-1`, not `n` — otherwise
> the widest row prints twice. Butterfly's *does* start at `n`, because
> its two middle rows are genuinely both full-width.

### Group 3 — Counter / value-driven patterns

The shape is a plain triangle; only *what prints* changes.

| Pattern | Rule |
|---|---|
| Floyd's Triangle | One counter incrementing across **all** rows |
| 0-1 Triangle | `1` if `(i + j)` is even, else `0` |
| Inverted Half Pyramid (Numbers) | Rows shrink `n → 1`, print `1..i` |

> ⚠️ **Trap:** Floyd's counter must be declared **outside both loops**.
> Declaring it inside resets it every row and breaks the continuity.

### Group 4 — Space-shifted triangles

Leading spaces push each row right; the star count does the rest.

| Pattern | Per row `i` |
|---|---|
| Inverted & Rotated Half Pyramid | spaces `n-i`, stars `i` |
| Solid Rhombus | spaces `n-i`, stars **`n`** (constant) |

> 💡 Constant star count = rhombus. Growing star count = triangle.
> That single difference is the whole distinction.

## Quick reference — all 9

| Pattern | Spaces per row | Symbols per row |
|---|---|---|
| Hollow Rectangle | 0 | `n` (border only) |
| Hollow Rhombus | `n-i` | `n` (border only) |
| Butterfly | `2*(n-i)` in middle | `i` each side |
| Diamond | `n-i` | `2*i - 1` |
| Floyd's Triangle | 0 | `i` (running counter) |
| 0-1 Triangle | 0 | `i` (parity of `i+j`) |
| Inverted Half Pyramid (Numbers) | 0 | `i`, counting down |
| Inverted & Rotated Half Pyramid | `n-i` | `i` |
| Solid Rhombus | `n-i` | `n` |

## How to practise this folder

1. Run `AdvancedPatterns.java` — all 9 print at once, so you can see
   every shape immediately.
2. Pick one. Write down **only** its row formula from the table above.
3. Close the file, write that pattern from scratch, run it, compare.
4. Move to the next. Don't try to reproduce all 9 in one sitting.

## Common mistakes

- Printing `" "` instead of `"  "` in a border pattern's blank branch —
  the shape collapses horizontally.
- Declaring Floyd's counter inside the outer loop (resets every row).
- Reprinting the middle row in Diamond by starting the lower half at
  `n` instead of `n-1`.
- Mixing up `n-i` (shrinking, used for leading spaces) with `i`
  (growing, used for star counts).
