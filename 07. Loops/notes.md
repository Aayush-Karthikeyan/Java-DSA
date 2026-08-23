## Loops (Flow Control)

### while Loop
```java
while (condition) {
    // runs as long as condition is true
}
```
> Use when you don't know how many iterations upfront.

### for Loop
```java
for (int i = 0; i < n; i++) {
    // runs n times
}
```
> Use when you know exact number of iterations.

### do-while Loop
```java
do {
    // runs at least once
} while (condition);
```
> Only loop that **guarantees at least one execution** even if condition is false.

### break
- Exits the loop immediately.
- Used when a condition is met and no further iteration needed.

### continue
- Skips current iteration, jumps to next.
- Does NOT exit the loop.

### 🧠 Interview Points
> `for` vs `while` → same power, different readability. `for` when count known, `while` when condition-based.  
> `do-while` → used for menu-driven programs (run once, then check).  
> `break` vs `continue` → break exits, continue skips.  
> Infinite loop → `while(true)` with a `break` inside is valid and common.


2. 

## Loop Problem Patterns

### Print 1 to N
- Use a simple for/while loop from 1 to n
- `System.out.print(i + " ")` to print on same line

### Sum of N Natural Numbers
- Initialize `sum = 0`, add `i` each iteration
- Formula shortcut: `n*(n+1)/2` → O(1), no loop needed
> 🧠 Interviewer may ask for both approaches

### Reverse a Number
- `% 10` → extract last digit
- `/ 10` → remove last digit  
- `rev = rev * 10 + LD` → build reversed number
> 🧠 Classic interview problem. Memorize this pattern.

### Square Pattern (n x n)
- Outer loop = rows, inner loop = columns
- Nested loops = O(n²)

### Prime Number Check
- Brute force: loop from 2 to n-1 → O(n)
- Optimized: loop from 2 to √n → O(√n) ✅ use this
- Edge case: n ≤ 1 is NOT prime, n = 2 IS prime
> 🧠 Always use √n approach in interviews, mention the optimization explicitly

### Break vs Continue
- `break` → exits loop entirely
- `continue` → skips current iteration, continues loop
> 🧠 Interview trap: what's the output when break/continue is inside nested loop?
> Answer: only breaks/continues the **inner** loop, not the outer one