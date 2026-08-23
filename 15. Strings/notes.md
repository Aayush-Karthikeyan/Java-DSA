# 15. Strings — Key Notes (Java)

---

## 1. What are Strings?
- A `String` in Java is a **sequence of characters** and is a **class**, not a primitive type.
- Declared as: `String name = "hello";`
- Internally stored as a `char[]` array under the hood.
- Strings live in the **String Pool** (a special area of heap memory) — if two strings have the same value, Java reuses the same object.

---

## 2. Input / Output
| Method | What it does |
|--------|-------------|
| `sc.next()` | Reads one word (stops at whitespace) |
| `sc.nextLine()` | Reads the entire line including spaces |

> **Gotcha:** After reading an int with `sc.nextInt()`, always call `sc.nextLine()` once to consume the leftover newline before reading a string.

---

## 3. String Length
```java
str.length()   // returns number of characters (int)
```
- Valid index range: `0` to `str.length() - 1`

---

## 4. String Concatenation
```java
String s = "Hello" + " " + "World";   // -> "Hello World"
```
- `+` creates a **new** String object every time (because Strings are immutable).
- In loops, use `StringBuilder` instead — much more efficient.

---

## 5. `charAt(index)`
```java
str.charAt(0)   // first character
str.charAt(str.length() - 1)   // last character
```
- 0-based indexing.
- Returns a `char`, not a `String`.
- Throws `StringIndexOutOfBoundsException` if index is out of range.

---

## 6. Palindrome Check
- Use two pointers: one from the left (`left=0`), one from the right (`right=length-1`).
- Move both inward, comparing characters at each step.
- If any pair mismatches → not a palindrome.
- Time: **O(n)**, Space: **O(1)**

---

## 7. Shortest Path (Direction String)
- Track `x` (East/West) and `y` (North/South) displacement.
- Apply direction characters one by one.
- Final distance = `Math.sqrt(x*x + y*y)` (Pythagorean theorem).

---

## 8. String Comparison
| Method | Purpose |
|--------|---------|
| `a.equals(b)` | Case-sensitive content comparison → `true/false` |
| `a.equalsIgnoreCase(b)` | Case-insensitive content comparison |
| `a.compareTo(b)` | Returns `0` (equal), `<0` (a before b), `>0` (a after b) |
| `a == b` | **DO NOT USE** for strings — compares memory addresses, not content |

> Always use `.equals()` to compare string content, never `==`.

---

## 9. `substring()`
```java
str.substring(startIndex)              // from start to end of string
str.substring(startIndex, endIndex)    // from start up to (NOT including) endIndex
```
- `"Hello World".substring(6)`    → `"World"`
- `"Hello World".substring(0, 5)` → `"Hello"`

---

## 10. Largest String (Lexicographic)
- Use `compareTo()` to compare strings lexicographically (dictionary order).
- `a.compareTo(b) < 0` means `a` comes before `b` → `b` is larger.

---

## 11. Why Strings are Immutable
- Once created, a String's content **cannot be changed**.
- Any "modification" (concat, replace, etc.) creates a **brand new String object**.
- **Reasons for immutability:**
  1. **String Pool** — safe to reuse objects only if they can't change.
  2. **Security** — passwords, file paths etc. can't be modified after creation.
  3. **Thread safety** — multiple threads can safely share the same String object.
  4. **Hash caching** — hashCode can be cached since value never changes.

---

## 12. StringBuilder (Mutable String)
```java
StringBuilder sb = new StringBuilder();
sb.append("Hello");       // add to end
sb.insert(2, "X");        // insert at index
sb.delete(1, 3);          // delete range [1, 3)
sb.reverse();             // reverse in-place
sb.toString();            // convert back to String
sb.length();              // current length
sb.charAt(i);             // access character
```
- **Mutable** — modifies the same object (no new objects created).
- **Use over `+` concatenation whenever building a string in a loop.**
- Time complexity of appending n characters: **O(n)** total vs **O(n²)** with `+`.

---

## 13. Convert to Uppercase (ASCII trick)
- `'a'` has ASCII value `97`, `'A'` has ASCII value `65` → difference is **32**.
- Subtract 32 from any lowercase letter to get its uppercase version.
- Check `ch >= 'a' && ch <= 'z'` before converting to avoid changing non-letters.
- Built-in: `str.toUpperCase()` / `str.toLowerCase()`

---

## 14. String Compression
- Iterate through the string, count consecutive occurrences of each character.
- Append character + count to a `StringBuilder`.
- Example: `"aaabbbcc"` → `"a3b3c2"`
- Use a `while` loop inside the outer loop to count repeats.

---

## Key String Methods Cheat Sheet
| Method | Returns | Description |
|--------|---------|-------------|
| `str.length()` | `int` | Number of characters |
| `str.charAt(i)` | `char` | Character at index i |
| `str.substring(s, e)` | `String` | Slice from s to e (exclusive) |
| `str.equals(other)` | `boolean` | Content equality (case-sensitive) |
| `str.equalsIgnoreCase(other)` | `boolean` | Content equality (case-insensitive) |
| `str.compareTo(other)` | `int` | Lexicographic comparison |
| `str.toUpperCase()` | `String` | All caps version |
| `str.toLowerCase()` | `String` | All lowercase version |
| `str.contains("x")` | `boolean` | Does string contain substring |
| `str.replace('a', 'b')` | `String` | Replace all occurrences |
| `str.trim()` | `String` | Remove leading/trailing spaces |
| `str.indexOf("x")` | `int` | First index of substring (-1 if not found) |
| `str.isEmpty()` | `boolean` | True if length == 0 |

---

## Common Pitfalls
1. **`==` vs `.equals()`** — always use `.equals()` for string content.
2. **Concatenation in loops** — always use `StringBuilder`, not `+`.
3. **`sc.nextLine()` after `sc.nextInt()`** — consume the newline first.
4. **`substring` end index is exclusive** — `substring(0, 5)` gives indices 0,1,2,3,4.
5. **`charAt` returns `char`** — comparing with `==` works fine since `char` is a primitive.
