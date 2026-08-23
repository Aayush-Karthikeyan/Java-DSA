## Functions & Methods

### Function Syntax
```java
static returnType methodName(parameters) {
    // body
    return value; // if not void
}

// void = returns nothing
static void greet() {
    System.out.println("Hello");
}

// with return
static int add(int a, int b) {
    return a + b;
}
```

### Parameters vs Arguments
- **Parameter** → variable in method definition `(int a, int b)`
- **Argument** → actual value passed when calling `(5, 10)`

### Call Stack (Memory)
- Each method call gets its own **stack frame**
- Frames stack on top of each other
- Frame is destroyed when method returns
- Stack overflow = too many nested/recursive calls

### Call by Value (Java)
- Java is **always** call by value for primitives
- A copy is passed — original is NOT changed
```java
static void change(int x) { x = 99; }
int a = 5;
change(a);
// a is still 5 ← copy was changed, not original
```
> 🧠 Huge interview point — Java has NO call by reference for primitives.

### Inbuilt vs User Defined Methods
```java
// Inbuilt (Math class)
Math.sqrt(n)       // square root
Math.pow(base, exp) // returns double
Math.abs(n)        // absolute value
Math.max(a, b)
Math.min(a, b)

// User defined = methods you write yourself
```

### Function Overloading
- Same method name, different parameters
```java
static int add(int a, int b) { return a + b; }
static double add(double a, double b) { return a + b; }
static int add(int a, int b, int c) { return a + b + c; }
```
- Overload by: number of params OR data types
> 🧠 Return type alone is NOT enough to overload — compile error.  
> 🧠 Overloading = compile time polymorphism.

### Scope
```java
// Method scope - exists entire method
static void method() {
    int x = 5; // accessible anywhere in method
}

// Block scope - exists only inside {}
for (int i = 0; i < 5; i++) {
    int x = i; // x dies after each iteration
}
```

### Important Algorithms (as functions)

**Factorial**
```java
static int factorial(int n) {
    int fact = 1;
    for (int i = 1; i <= n; i++) {
        fact *= i;
    }
    return fact;
}
```

**Binomial Coefficient (nCr)**
```java
// nCr = n! / (r! * (n-r)!)
static int nCr(int n, int r) {
    return factorial(n) / (factorial(r) * factorial(n - r));
}
```

**Prime Check (Optimized)**
```java
static boolean isPrime(int n) {
    if (n <= 1) return false;
    if (n == 2) return true;
    for (int i = 2; i <= Math.sqrt(n); i++) {
        if (n % i == 0) return false;
    }
    return true;
}
```

**Primes in Range**
```java
static void primesInRange(int start, int end) {
    for (int i = start; i <= end; i++) {
        if (isPrime(i)) System.out.print(i + " ");
    }
}
```

**Binary to Decimal**
```java
// 101 in binary = 1*2² + 0*2¹ + 1*2⁰ = 5
static int binaryToDecimal(int binary) {
    int decimal = 0, power = 0;
    while (binary > 0) {
        int LD = binary % 10;
        decimal += LD * (int) Math.pow(2, power);
        power++;
        binary /= 10;
    }
    return decimal;
}
```

**Decimal to Binary**
```java
// 5 → 101 (keep dividing by 2, collect remainders)
static void decimalToBinary(int decimal) {
    int binary = 0, power = 1;
    while (decimal > 0) {
        int rem = decimal % 2;
        binary += rem * power;
        power *= 10;
        decimal /= 2;
    }
    System.out.println(binary);
}
```

### 🧠 Interview Points
> `%2` and `/2` for decimal→binary is same pattern as reverse number.  
> Binary→decimal uses `Math.pow(2, position)` — know this cold.  
> Always mention `Math.sqrt(n)` optimization for prime — shows you think about efficiency.  
> Overloading is resolved at **compile time**, overriding at **runtime**.