# 17. Object Oriented Programming (OOPs) — Key Notes (Java)

---

## The 4 Pillars of OOPs
| Pillar | One Line |
|--------|----------|
| **Encapsulation** | Bundle data + methods; hide internals |
| **Inheritance** | Child class reuses parent class code |
| **Polymorphism** | Same method, different behaviour |
| **Abstraction** | Hide implementation, show only what's needed |

---

## 1. Classes & Objects
- **Class** = blueprint / template
- **Object** = actual instance created from the blueprint
```java
class Dog { String name; void bark() { ... } }   // class
Dog d = new Dog();                                // object (instance)
```
- `new` allocates memory on the heap and calls the constructor.
- Each object has its own copy of instance fields.

---

## 2. Access Modifiers
| Modifier | Same Class | Same Package | Subclass | Anywhere |
|----------|-----------|-------------|----------|---------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (default) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

> **Rule of thumb:** make fields `private`, methods `public`. Use `protected` in inheritance.

---

## 3. Getters & Setters
- Allow **controlled access** to private fields.
- Setters can include validation before assigning a value.
```java
public String getName() { return name; }         // getter
public void setAge(int age) {
    if (age > 0) this.age = age;                 // setter with validation
}
```

---

## 4. Encapsulation
- Wrapping data (fields) and methods together, while **hiding** the internal state.
- Achieved by: `private` fields + `public` getters/setters.
- **Why:** prevents invalid data, makes code maintainable, hides complexity.

---

## 5. Constructors
- Same name as the class, no return type.
- Called automatically with `new`.
- If you write no constructor, Java provides a **default no-arg constructor**.
- If you write ANY constructor, Java removes the default one.

### Types of Constructors
| Type | When |
|------|------|
| Default (no-arg) | `new Student()` — no arguments |
| Parameterized | `new Student("Aayush", 20)` — with arguments |
| Copy | `new Student(otherStudent)` — copies another object |

### `this` keyword
- `this` refers to the **current object**.
- `this.name = name` — distinguishes the field from the parameter when names clash.
- `this()` — calls another constructor of the same class (constructor chaining).

---

## 6. Shallow vs Deep Copy
| | Shallow Copy | Deep Copy |
|--|-------------|----------|
| What copies | Reference (points to same object) | Creates a new independent object |
| Change in copy affects original? | **Yes** (same memory) | **No** (separate memory) |
| How | Default assignment `a = b` | Copy constructor / manual copy |

```java
// Shallow — both point to the same Student object
Student a = new Student("X", 20, "Y");
Student b = a;           // b IS a (same reference)
b.setName("Z");          // also changes a.name!

// Deep — two separate objects with same values
Student c = new Student(a);  // copy constructor
c.setName("Z");              // a.name unchanged
```

---

## 7. Destructors
- Java has **no destructors**. Memory is managed by the **Garbage Collector (GC)**.
- GC automatically frees objects with no references.
- You can use `finalize()` (deprecated in Java 9+) — never rely on it.

---

## 8. Inheritance
- `extends` keyword. Child gets all non-private members of the parent.
- **IS-A relationship:** `Dog extends Animal` → Dog IS-A Animal.

### Types
```
Single:       A → B
Multilevel:   A → B → C
Hierarchical: A → B, A → C
Multiple:     NOT supported with classes; use interfaces instead
Hybrid:       Combination of above (via interfaces)
```

### `super` keyword
- `super(args)` — calls the **parent constructor** (must be first line in child constructor)
- `super.method()` — calls the **parent version** of an overridden method
- `super.field` — accesses parent's field (if not private)

> Parent constructor is ALWAYS called first, even if you don't write `super()` — Java inserts `super()` automatically.

---

## 9. Polymorphism

### Method Overloading (Compile-time)
- Same method name, **different parameters** (type, count, or order).
- Resolved at **compile time**.
- Return type alone is NOT enough to distinguish overloaded methods.
```java
int add(int a, int b)           // ✓
double add(double a, double b)  // ✓ — different type
int add(int a, int b, int c)    // ✓ — different count
```

### Method Overriding (Runtime)
- Child class provides its **own version** of a parent method.
- Same name, same parameters, same return type.
- Resolved at **runtime** based on the actual object (not the reference type).
- Use `@Override` annotation — compiler verifies you're actually overriding.
```java
Shape s = new Circle(5);  // Shape reference, Circle object
s.area();                 // calls Circle's area() at runtime → Runtime Polymorphism
```

### Overloading vs Overriding
| | Overloading | Overriding |
|--|-------------|-----------|
| Where | Same class | Parent + Child class |
| Parameters | Must differ | Must be the same |
| When resolved | Compile time | Runtime |
| `@Override` | Not needed | Should use |

---

## 10. Packages
- A package is a **namespace** that groups related classes.
- `package com.myapp.utils;` — declare at top of file.
- `import java.util.Scanner;` — use classes from other packages.
- Prevents naming conflicts between classes.
- Built-in packages: `java.lang` (auto-imported), `java.util`, `java.io`, etc.

---

## 11. Abstraction

### Abstract Class
- Declared with `abstract` keyword.
- **Cannot be instantiated** (`new AbstractClass()` → error).
- Can have both **abstract methods** (no body) and **concrete methods** (with body).
- Subclass MUST implement all abstract methods, or itself be abstract.
```java
abstract class Shape {
    abstract double area();        // no body — subclass must implement
    void display() { ... }         // concrete — shared by all shapes
}
```

### Interface
- Pure contract — defines WHAT a class must do, not HOW.
- All methods implicitly `public abstract` (Java 7).
- Java 8+: can have `default` and `static` methods with bodies.
- All fields implicitly `public static final` (constants).
- A class can implement **multiple interfaces** (solves multiple inheritance).
```java
interface Flyable { void fly(); }
interface Swimmable { void swim(); }
class Duck implements Flyable, Swimmable { ... }  // multiple implementation ✓
```

### Abstract Class vs Interface
| | Abstract Class | Interface |
|--|---------------|-----------|
| Instantiate | ✗ | ✗ |
| Constructor | ✓ | ✗ |
| Fields | Any type | `public static final` only |
| Methods | Abstract + concrete | Abstract (+ default/static in Java 8+) |
| Multiple inherit | ✗ (only one `extends`) | ✓ (multiple `implements`) |
| Use when | Shared base + partial impl | Pure contract / multiple inheritance |

---

## 12. Static Keyword
- `static` = belongs to the **class**, not to any object.

| | Instance | Static |
|--|---------|--------|
| Belongs to | Object | Class |
| Access via | `obj.field` | `ClassName.field` |
| Memory | One per object | One shared copy |
| Can access | static + instance | static only |

```java
class Counter {
    static int count = 0;          // shared across ALL objects
    Counter() { count++; }
    static int getCount() { return count; }  // call as Counter.getCount()
}
```
- `static` methods cannot use `this` or access instance fields directly.
- Common uses: utility methods (`Math.sqrt`), constants (`Math.PI`), counters.

---

## 13. Super Keyword — Summary
```java
super()          // call parent constructor (must be first line)
super.method()   // call parent's version of an overridden method
super.field      // access parent's field
```

---

## Key OOPs Rules to Remember
1. **One public class per `.java` file**, and filename must match the public class name.
2. **Constructor chaining:** `super()` or `this()` must always be the **first line**.
3. **Private members are NOT inherited** — child can't access them directly (only via getters/setters).
4. **`final` class** cannot be extended. **`final` method** cannot be overridden.
5. **`abstract` class** cannot be `final` (contradictory — one forces subclassing, other forbids it).
6. You can store a child object in a parent reference: `Animal a = new Dog()` — but you can only call methods declared in `Animal` unless you cast.
7. `instanceof` checks if an object is of a certain type: `if (a instanceof Dog)`.
8. Multiple inheritance with classes → ✗ (diamond problem). Use interfaces → ✓.

---

## Common Pitfalls
1. **Forgetting `super()` in child constructor** — Java adds it automatically only if no-arg version exists in parent; if parent has only parameterized constructors, you MUST call `super(...)` explicitly.
2. **`==` vs `.equals()`** — `==` compares references for objects; `.equals()` compares content.
3. **Static method can't be overridden** — it's method hiding, not overriding (no runtime polymorphism).
4. **Interface fields are constants** — you can't change them in implementing classes.
5. **Abstract method in non-abstract class** — compile error; if a class has an abstract method, the class itself must be abstract.
