## Patterns (Part I)

### Key Concept - Nested Loops
- Outer loop → controls rows
- Inner loop → controls what prints on each row
- `System.out.print()` → stays on same line
- `System.out.println()` → moves to next line

### Pattern Cheatsheet
| Pattern | Inner loop condition | Print |
|---------|---------------------|-------|
| Star triangle | `star <= line` | `"* "` |
| Inverted star | `star <= (n - line + 1)` | `"* "` |
| Number pyramid | `star <= line` | `star` |
| Character pyramid | `star <= line` | `ch++` |

> 🧠 `char` is just a number (ASCII). `'A'=65`, so `ch++` moves to next letter.  
> 🧠 Declare `ch` outside both loops so it increments continuously across rows.  
> 🧠 All patterns follow same skeleton — only the inner condition and print changes.