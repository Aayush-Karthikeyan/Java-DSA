1. ## Operators in Java

### Arithmetic Operators
| Operator | Type   | Description         |
|----------|--------|---------------------|
| +, -, *, /, % | Binary | operates on two operands |
| ++, --   | Unary  | operates on one operand  |

> 💡 `/` on two ints = integer division (truncates decimal)  
> e.g. `10 / 3 = 3` not `3.33`  
> 💡 `%` gives remainder → `10 % 3 = 1`

### Unary Operators
| Operator | Name           | Behaviour                          |
|----------|----------------|------------------------------------|
| `++a`    | Pre Increment  | increment first, then use value    |
| `a++`    | Post Increment | use value first, then increment    |
| `--a`    | Pre Decrement  | decrement first, then use value    |
| `a--`    | Post Decrement | use value first, then decrement    |

> 🧠 Interview trap:
> ```java
> int a = 5;
> int b = a++; // b = 5, a = 6
> int c = ++a; // c = 7, a = 7
> ```

2. ## Logical Operators
| Operator | Name | Returns true when |
|----------|------|-------------------|
| `&&` | AND | both conditions true |
| `\|\|` | OR | at least one condition true |
| `!` | NOT | condition is false |

> 🧠 Short-circuit evaluation:
> - `&&` → if left is false, right is NOT evaluated
> - `\|\|` → if left is true, right is NOT evaluated

---

## Assignment Operators
| Operator | Equivalent |
|----------|-----------|
| `=`  | assign |
| `+=` | a = a + b |
| `-=` | a = a - b |
| `*=` | a = a * b |
| `/=` | a = a / b |
| `%=` | a = a % b |