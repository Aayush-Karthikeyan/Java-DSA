# 16. Bit Manipulation — Key Notes (Java)

---

## 1. Why Bit Manipulation?
- Computers store everything as **binary (0s and 1s)**.
- Bit operations run at the **hardware level** — faster than arithmetic.
- Most bit tricks are **O(1)** time and **O(1)** space.
- Common in: competitive programming, embedded systems, cryptography, flags/permissions.

---

## 2. Binary Number System
- Base-2 system. Each digit is called a **bit**.
- Rightmost bit = **bit 0** (Least Significant Bit, LSB)
- Leftmost bit  = **Most Significant Bit (MSB)**

| Decimal | Binary |
|---------|--------|
| 0 | 0000 |
| 1 | 0001 |
| 2 | 0010 |
| 4 | 0100 |
| 8 | 1000 |
| 13 | 1101 |
| 15 | 1111 |

> **Quick conversion:** `Integer.toBinaryString(n)` in Java prints binary.

---

## 3. Bitwise Operators

| Operator | Symbol | Rule |
|----------|--------|------|
| AND | `&` | 1 only if **both** bits are 1 |
| OR | `\|` | 1 if **at least one** bit is 1 |
| XOR | `^` | 1 if bits are **different** |
| NOT (1's complement) | `~` | Flips every bit (0→1, 1→0) |
| Left Shift | `<<` | Shifts bits left, fills with 0 on right |
| Right Shift | `>>` | Shifts bits right, fills with sign bit on left |

### Truth Tables (per bit)
```
AND:          OR:           XOR:
0 & 0 = 0    0 | 0 = 0    0 ^ 0 = 0
0 & 1 = 0    0 | 1 = 1    0 ^ 1 = 1
1 & 0 = 0    1 | 0 = 1    1 ^ 0 = 1
1 & 1 = 1    1 | 1 = 1    1 ^ 1 = 0
```

---

## 4. Binary AND (`&`)
- **Use case:** Checking/isolating specific bits.
- Any bit ANDed with 0 → 0 (clears it)
- Any bit ANDed with 1 → unchanged (keeps it)
```java
13 & 7   ->   1101 & 0111 = 0101 = 5
n & 1    ->   checks if last bit is 1 (odd check)
```

---

## 5. Binary OR (`|`)
- **Use case:** Setting specific bits.
- Any bit ORed with 1 → 1 (sets it)
- Any bit ORed with 0 → unchanged
```java
13 | 2   ->   1101 | 0010 = 1111 = 15
```

---

## 6. Binary XOR (`^`)
- **Use case:** Toggling bits, finding unique elements.
- Same bits → 0, Different bits → 1
- `n ^ n = 0` (any number XORed with itself is 0)
- `n ^ 0 = n` (any number XORed with 0 is itself)
```java
// Classic trick: find the single non-duplicate in an array
// XOR all elements -> duplicates cancel out (n^n=0), leaving the unique one
13 ^ 5  ->  1101 ^ 0101 = 1000 = 8
```

---

## 7. Binary 1's Complement (`~`)
- Flips ALL bits: 0→1 and 1→0
- `~n = -(n+1)` for signed integers in Java (two's complement system)
```java
~0  = -1
~1  = -2
~13 = -14
```
> **Key use:** `~(1 << i)` creates a mask with 0 only at position i (used for clearing a bit).

---

## 8. Left Shift (`<<`)
- `n << i` = multiply n by 2^i
- Shifts all bits left by i positions, fills right with 0s
```java
1 << 3  =  8    (0001 -> 1000)
5 << 2  =  20   (0101 -> 10100)
```
> Shortcut: `n << 1` doubles n, `n << 2` quadruples n.

---

## 9. Right Shift (`>>`)
- `n >> i` = divide n by 2^i (integer division)
- Shifts all bits right by i positions, fills left with the sign bit (0 for positive)
```java
16 >> 1  =  8   (10000 -> 01000)
13 >> 2  =  3   (1101  -> 0011)
```
> Shortcut: `n >> 1` halves n (floor division by 2).

---

## 10. The "Mask" Pattern (Core Concept)
Almost every bit trick uses a **mask**: a number with specific bits set to achieve a goal.

```
To work on bit i:
    mask = 1 << i       // 000...1...000  (1 only at position i)
   ~mask = ~(1 << i)    // 111...0...111  (0 only at position i)
```

---

## 11. Core Bit Tricks

### Get ith Bit — read the value (0 or 1) at position i
```java
int bit = (n >> i) & 1;
// OR: if ((n & (1 << i)) != 0) -> bit is 1
```

### Set ith Bit — force bit i to 1
```java
n = n | (1 << i);
```

### Clear ith Bit — force bit i to 0
```java
n = n & ~(1 << i);
```

### Update ith Bit — set bit i to value v (0 or 1)
```java
n = clearIthBit(n, i);     // clear first
n = n | (v << i);          // then OR with new value
```

### Clear Last i Bits — zero out bits 0 to i-1
```java
n = n & ((-1) << i);       // -1 = all 1s in binary
```

### Clear Range i to j — zero out bits i through j
```java
int a = (-1) << (j + 1);   // 1s above j
int b = (1 << i) - 1;      // 1s below i
n = n & (a | b);
```

---

## 12. Check Odd or Even
```java
if ((n & 1) == 0)  -> Even    // last bit is 0
else               -> Odd     // last bit is 1
```
Faster than `n % 2` (no division needed).

---

## 13. Check Power of 2
- Powers of 2 have **exactly one bit set**: `1, 10, 100, 1000...`
- `n - 1` flips all bits up to and including the lowest set bit.
- So `n & (n-1)` clears the lowest set bit. If result is 0 → was a power of 2.
```java
n > 0 && (n & (n-1)) == 0   // true if power of 2
```
Examples:
```
8  = 1000
7  = 0111
8 & 7 = 0  -> power of 2 ✓

6  = 0110
5  = 0101
6 & 5 = 0100 ≠ 0  -> not power of 2 ✓
```

---

## 14. Count Set Bits (Brian Kernighan approach)
```java
int count = 0;
while (n > 0) {
    n = n & (n - 1);   // clears the lowest set bit each time
    count++;
}
```
Or the simple loop (right shift approach):
```java
while (n > 0) {
    count += (n & 1);  // add last bit
    n >>= 1;           // drop last bit
}
```

---

## 15. Fast Exponentiation (Binary Exponentiation)
- Normal: compute base^exp by multiplying exp times → **O(exp)**
- Fast: use the binary representation of exp → **O(log exp)**

**Idea:** write exp in binary. For each bit from right to left:
- If bit is 1 → multiply result by current base power
- Square the base every step
- Halve the exponent (right shift)

```java
long fastPow(long base, int exp) {
    long result = 1;
    while (exp > 0) {
        if ((exp & 1) == 1) result *= base;  // odd exponent -> take one factor
        base *= base;                         // square
        exp >>= 1;                            // halve
    }
    return result;
}
```
Example: `2^10` → only needs ~4 multiplications instead of 10.

---

## Quick Reference Cheat Sheet
| Goal | Code |
|------|------|
| Get bit i | `(n >> i) & 1` |
| Set bit i | `n \| (1 << i)` |
| Clear bit i | `n & ~(1 << i)` |
| Toggle bit i | `n ^ (1 << i)` |
| Check odd | `(n & 1) == 1` |
| Check power of 2 | `n > 0 && (n & (n-1)) == 0` |
| Multiply by 2^i | `n << i` |
| Divide by 2^i | `n >> i` |
| Clear lowest set bit | `n & (n-1)` |
| Isolate lowest set bit | `n & (-n)` |
| All bits set (32-bit) | `-1` or `0xFFFFFFFF` |

---

## Common Pitfalls
1. **Operator precedence:** `&`, `|`, `^` have **lower** priority than `==`. Always wrap in parentheses: `(n & 1) == 0` not `n & 1 == 0`.
2. **`~` on int gives int:** `~13 = -14`, not what you'd expect from pure bit flip thinking — due to two's complement.
3. **Left shift overflow:** shifting a 32-bit int beyond 31 positions is undefined behavior — use `long` for large shifts.
4. **Right shift vs unsigned right shift:** `>>` preserves sign bit; `>>>` fills with 0 (use `>>>` when working with negative numbers and want logical shift).
