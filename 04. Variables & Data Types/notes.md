1. Literals can't be changed but variables can be changed
2. when we say a=69, a is an identifier
3. ## Size of Data Types (Java)

> 1 bit = 0 or 1 | 8 bits = 1 byte

| Data Type | Size   | Range / Values                        |
|-----------|--------|---------------------------------------|
| byte      | 1 byte | -128 to 127 (256 total values)        |
| short     | 2 bytes| -32,768 to 32,767                     |
| char      | 2 bytes| 'a'-'z', 'A'-'Z', '0'-'9', '@', '%'  |
| boolean   | 1 byte | true, false                           |
| int       | 4 bytes| ~ -2B to +2B                          |
| long      | 8 bytes| larger than int                       |
| float     | 4 bytes| decimal (single precision)            |
| double    | 8 bytes| decimal (double precision)            |

4. ## Input in Java (Scanner class)

> `import java.util.*;`
> `Scanner sc = new Scanner(System.in);`

| Method        | Reads                          |
|---------------|-------------------------------|
| next()        | single word (stops at space)  |
| nextLine()    | full line including spaces    |
| nextInt()     | integer value                 |
| nextByte()    | byte value                    |
| nextFloat()   | float value                   |
| nextDouble()  | double value                  |
| nextBoolean() | boolean value                 |
| nextShort()   | short value                   |
| nextLong()    | long value                    |

> ⚠️ `next()` does NOT read spaces — stops at whitespace.  
> ✅ `nextLine()` reads the full line including spaces.
> ⚠️ `nextLine()` after `nextInt()` causes a bug — nextInt() leaves a
> leftover `\n` that nextLine() reads as empty. Fix: add an extra
> `sc.nextLine()` to flush it.
> 💡 The checkmark on next(), nextLine(), nextInt() in the slide = most
> commonly used in interviews/practice.
> 🔒 Close scanner when done: `sc.close()`

5. we need to add 'f' after the decimal number to make it float or it will throw an error

6. ## Type Conversion in Java

Automatic (implicit) conversion happens when:
- a. Types are compatible (e.g. int ↔ float ✅, int ↔ boolean ❌)
- b. Destination type > Source type (widening)

**Widening order (automatic):**
`byte → short → int → float → long → double`

> 💡 Opposite direction = **Narrowing** — must cast manually:
> `int x = (int) 9.99; // x = 9, decimal lost`

> ⚠️ Data loss happens in narrowing — common interview trap.
> e.g. storing a `double` into `int` truncates (not rounds) the decimal.

> 🧠 Interview point: `char` can be converted to `int` (gives ASCII/Unicode value)
> `char c = 'A'; int x = c; // x = 65`

7. ## Type Promotion in Expressions

    1. Java auto-promotes `byte`, `short`, or `char` to **int** during expression evaluation.
    2. If any operand is `long`, `float`, or `double` → whole expression promotes to that type.

**// wrong**
```java
byte b = 5;
b = b * 2; // ❌ b*2 becomes int, can't store back in byte
```

**// right**
```java
byte b = 5;
b = (byte)(b * 2); // ✅ manually cast back to byte
```

> ⚠️ `char a - char b` → result is `int`, not `char` (type promotion kicks in)  
> must cast: `char c = (char)(a - b);`

> 🧠 Interview point: even `byte + byte` gives `int` — always cast if storing back into smaller type.

8. ## How Java Code Runs

**Flow:**
`Source Code (.java)` → `Compiler` → `Byte Code (.class)` → `JVM` → `Native Code`

- **Compilation:** javac converts .java to platform-independent bytecode (.class)
- **Execution:** JVM converts bytecode to native code for the current OS

**JDK > JRE > JVM**
- **JVM** – runs bytecode
- **JRE** – JVM + Libraries (to run Java programs)
- **JDK** – JRE + Dev Tools like compiler (to write & compile Java)

> 💡 "Write once, run anywhere" — bytecode is OS-independent, JVM is OS-specific.

