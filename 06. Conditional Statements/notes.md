## Conditional Statements

### if-else
```java
if (condition) {
    // runs if true
} else if (condition2) {
    // runs if condition2 true
} else {
    // runs if all above false
}
```

### Ternary Operator
```java
// condition ? valueIfTrue : valueIfFalse
int max = (a > b) ? a : b;
```
> 💡 One-liner replacement for simple if-else. Common in interviews.

### Switch Statement
```java
switch (variable) {
    case value1:
        // code
        break;
    case value2:
        // code
        break;
    default:
        // runs if no case matches
}
```
> ⚠️ Always add `break` — without it, execution **falls through** to the next case.  
> 💡 `default` is like the `else` of switch.  
> 🧠 Switch works with: `int`, `char`, `String`, `byte`, `short`, enums. NOT `float`/`double`.

### Key Interview Points
> 🧠 Difference between `==` and `.equals()` in conditions — `==` checks reference, `.equals()` checks value (for Strings).  
> 🧠 Fall-through in switch is an interview trap — always ask "is there a break?"  
> 🧠 Ternary can be nested but avoid it — kills readability.