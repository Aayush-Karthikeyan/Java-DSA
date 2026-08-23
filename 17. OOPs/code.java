// ===================================================================
// TOPIC: Object Oriented Programming (OOPs) in Java
// NOTE: Java allows only ONE public class per file.
//       All supporting classes here are package-private (no modifier).
//       Run this file: the main() is inside the 'code' class at the bottom.
// ===================================================================


// ============================================================
// SECTION 1: CLASSES & OBJECTS + ACCESS MODIFIERS
//            + GETTERS & SETTERS + ENCAPSULATION + CONSTRUCTORS
// ============================================================

class Student {

    // ----- ACCESS MODIFIERS -----
    // private  -> accessible only inside this class
    // public   -> accessible from anywhere
    // protected-> accessible in same package + subclasses
    // (default)-> accessible only within the same package

    private String name;   // private: can't be accessed directly from outside
    private int age;       // encapsulated fields — accessed only via getters/setters
    public String school;  // public: directly accessible from anywhere

    // ----- CONSTRUCTOR -----
    // Special method: same name as class, no return type.
    // Called automatically when you do: new Student(...)
    // If you don't write one, Java provides a default no-arg constructor.
    public Student(String name, int age, String school) {
        this.name   = name;    // 'this' refers to the current object's field
        this.age    = age;
        this.school = school;
    }

    // ----- COPY CONSTRUCTOR -----
    // Creates a new object by copying values from another object of the same class.
    // This is a DEEP copy — a separate object with the same values.
    public Student(Student other) {
        this.name   = other.name;   // copy each field manually
        this.age    = other.age;
        this.school = other.school;
    }

    // ----- DEFAULT (no-arg) CONSTRUCTOR -----
    // Allows creating a Student with no arguments: new Student()
    public Student() {
        this.name   = "Unknown";
        this.age    = 0;
        this.school = "None";
    }

    // ----- GETTERS & SETTERS (Encapsulation) -----
    // Getters: read a private field
    // Setters: write a private field (can add validation here)
    public String getName() { return name; }
    public int    getAge()  { return age; }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {  // validation before setting
            this.name = name;
        }
    }
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {           // validation: reject invalid ages
            this.age = age;
        }
    }

    // display info
    public void display() {
        System.out.println("Name: " + name + ", Age: " + age + ", School: " + school);
    }
}


// ============================================================
// SECTION 2: INHERITANCE
// "extends" gives a child class all non-private members of the parent.
// Java supports: Single, Multilevel, Hierarchical inheritance.
// Java does NOT support multiple inheritance with classes (use interfaces).
// ============================================================

// ----- SINGLE LEVEL INHERITANCE -----
class Animal {
    String name;

    Animal(String name) { this.name = name; }

    public void eat() {
        System.out.println(name + " is eating.");
    }
    public void breathe() {
        System.out.println(name + " is breathing.");
    }
}

class Dog extends Animal {   // Dog IS-A Animal (single level)
    String breed;

    Dog(String name, String breed) {
        super(name);         // calls Animal's constructor — MUST be the first statement
        this.breed = breed;
    }

    public void bark() {
        System.out.println(name + " (" + breed + ") says: Woof!");
    }
}


// ----- MULTI LEVEL INHERITANCE -----
// GoldenRetriever -> Dog -> Animal  (chain of 3 levels)
class GoldenRetriever extends Dog {
    GoldenRetriever(String name) {
        super(name, "Golden Retriever");  // calls Dog's constructor
    }

    public void fetch() {
        System.out.println(name + " fetches the ball!");
    }
}


// ----- HIERARCHICAL INHERITANCE -----
// Multiple child classes from the SAME parent
class Cat extends Animal {
    Cat(String name) { super(name); }

    public void meow() {
        System.out.println(name + " says: Meow!");
    }
}


// ----- HYBRID INHERITANCE (via Interface — explained in Section 5) -----
// Java solves the "diamond problem" by using interfaces for multiple inheritance.


// ============================================================
// SECTION 3: POLYMORPHISM
// Same method name, different behaviour depending on context.
// Two types: Compile-time (Overloading) & Runtime (Overriding)
// ============================================================

class Calculator {

    // ----- METHOD OVERLOADING (Compile-time Polymorphism) -----
    // Same method name, different parameter list.
    // Java decides WHICH method to call at compile time based on arguments.

    public int add(int a, int b) {              // add two ints
        return a + b;
    }
    public double add(double a, double b) {     // add two doubles (different types)
        return a + b;
    }
    public int add(int a, int b, int c) {       // add three ints (different count)
        return a + b + c;
    }
}


// ----- METHOD OVERRIDING (Runtime Polymorphism) -----
// Child class provides its OWN implementation of a method already in parent.
// Java decides WHICH version to call at runtime based on the actual object type.

class Shape {
    public double area() {
        return 0;   // base version — overridden by each shape
    }
    public void display() {
        System.out.println("Area = " + area());   // calls the overridden version at runtime
    }
}

class Circle extends Shape {
    double radius;
    Circle(double r) { this.radius = r; }

    @Override                                   // @Override annotation: tells compiler to verify this is overriding
    public double area() {
        return Math.PI * radius * radius;       // pi * r^2
    }
}

class Rectangle extends Shape {
    double width, height;
    Rectangle(double w, double h) { this.width = w; this.height = h; }

    @Override
    public double area() {
        return width * height;
    }
}


// ============================================================
// SECTION 4: ABSTRACTION — Abstract Classes & Interfaces
// Abstraction = hiding implementation details, showing only what's necessary.
// ============================================================

// ----- ABSTRACT CLASS -----
// Cannot be instantiated (no 'new AbstractVehicle()').
// Can have both abstract methods (no body) and regular methods (with body).
abstract class Vehicle {
    String brand;

    Vehicle(String brand) { this.brand = brand; }

    // abstract method: subclass MUST implement this
    public abstract void fuelType();

    // regular method: shared by all subclasses
    public void start() {
        System.out.println(brand + " is starting...");
    }
}

class ElectricCar extends Vehicle {
    ElectricCar(String brand) { super(brand); }

    @Override
    public void fuelType() {
        System.out.println(brand + " runs on electricity.");
    }
}

class PetrolCar extends Vehicle {
    PetrolCar(String brand) { super(brand); }

    @Override
    public void fuelType() {
        System.out.println(brand + " runs on petrol.");
    }
}


// ----- INTERFACE -----
// A pure contract: only abstract methods (Java 8+ allows default/static methods too).
// A class can IMPLEMENT multiple interfaces (solves multiple inheritance problem).
// All methods in an interface are implicitly public and abstract.
// All fields are implicitly public, static, and final.

interface Flyable {
    void fly();   // implicitly public abstract
}

interface Swimmable {
    void swim();
}

// Duck implements BOTH interfaces — this is how Java does multiple inheritance
class Duck extends Animal implements Flyable, Swimmable {
    Duck(String name) { super(name); }

    @Override
    public void fly() { System.out.println(name + " is flying!"); }

    @Override
    public void swim() { System.out.println(name + " is swimming!"); }
}


// ============================================================
// SECTION 5: STATIC KEYWORD
// 'static' means the member belongs to the CLASS, not to any specific object.
// ============================================================

class BankAccount {
    private String owner;
    private double balance;

    // static field: shared across ALL BankAccount objects
    private static int totalAccounts = 0;   // one copy, shared by all

    public BankAccount(String owner, double initialBalance) {
        this.owner   = owner;
        this.balance = initialBalance;
        totalAccounts++;     // every time a new account is created, increment the class-level counter
    }

    // static method: called on the class itself, not on an object
    // can only access static fields/methods (no 'this')
    public static int getTotalAccounts() {
        return totalAccounts;
    }

    public void displayBalance() {
        System.out.println(owner + "'s balance: $" + balance);
    }
}


// ============================================================
// SECTION 6: SUPER KEYWORD
// 'super' refers to the immediate parent class.
// Uses: call parent constructor, call parent method, access parent field.
// ============================================================

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    public void introduce() {
        System.out.println("Hi, I'm " + name + ", age " + age);
    }
}

class Employee extends Person {
    String company;

    Employee(String name, int age, String company) {
        super(name, age);        // call parent constructor with super()
        this.company = company;
    }

    @Override
    public void introduce() {
        super.introduce();       // call parent version of introduce()
        System.out.println("I work at " + company);  // then add extra info
    }
}


// ============================================================
// MAIN CLASS — runs all demonstrations
// ============================================================

public class code {
    public static void main(String[] args) {

        // ----- 1. Classes & Objects -----
        System.out.println("===== 1. Classes & Objects =====");
        Student s1 = new Student("Aayush", 20, "IIT");  // calls parameterized constructor
        Student s2 = new Student();                       // calls no-arg constructor
        Student s3 = new Student(s1);                     // calls copy constructor (deep copy)
        s1.display();
        s2.display();
        s3.display();
        s3.setName("Copy of Aayush");  // modify s3 — s1 is unaffected (deep copy)
        s3.display();
        System.out.println();

        // ----- 2. Encapsulation -----
        System.out.println("===== 2. Encapsulation =====");
        s1.setAge(200);          // setter rejects invalid age
        System.out.println("Age after invalid set: " + s1.getAge());  // still 20
        s1.setAge(21);
        System.out.println("Age after valid set: "   + s1.getAge());  // 21
        System.out.println();

        // ----- 3. Inheritance -----
        System.out.println("===== 3. Inheritance =====");
        Dog dog = new Dog("Buddy", "Labrador");
        dog.eat();       // inherited from Animal
        dog.bark();      // Dog's own method
        GoldenRetriever gr = new GoldenRetriever("Max");
        gr.eat();        // inherited from Animal (2 levels up)
        gr.bark();       // inherited from Dog
        gr.fetch();      // GoldenRetriever's own
        Cat cat = new Cat("Whiskers");
        cat.eat();       // inherited from Animal (hierarchical)
        cat.meow();
        System.out.println();

        // ----- 4. Polymorphism -----
        System.out.println("===== 4. Polymorphism =====");
        // Overloading (compile-time)
        Calculator calc = new Calculator();
        System.out.println("add(2,3): "     + calc.add(2, 3));
        System.out.println("add(2.5,3.5): " + calc.add(2.5, 3.5));
        System.out.println("add(1,2,3): "   + calc.add(1, 2, 3));

        // Overriding (runtime) — parent reference can hold child object
        Shape sh1 = new Circle(5);       // Shape reference, Circle object
        Shape sh2 = new Rectangle(4, 6); // Shape reference, Rectangle object
        sh1.display();  // calls Circle's area() at runtime
        sh2.display();  // calls Rectangle's area() at runtime
        System.out.println();

        // ----- 5. Abstract Classes -----
        System.out.println("===== 5. Abstract Classes =====");
        Vehicle v1 = new ElectricCar("Tesla");   // parent reference, child object
        Vehicle v2 = new PetrolCar("Toyota");
        v1.start();       // shared method from Vehicle
        v1.fuelType();    // each implements differently
        v2.start();
        v2.fuelType();
        System.out.println();

        // ----- 6. Interfaces -----
        System.out.println("===== 6. Interfaces =====");
        Duck duck = new Duck("Donald");
        duck.eat();    // from Animal
        duck.fly();    // from Flyable
        duck.swim();   // from Swimmable
        // Interface as type reference:
        Flyable f = new Duck("Daffy");
        f.fly();       // only fly() accessible through Flyable reference
        System.out.println();

        // ----- 7. Static keyword -----
        System.out.println("===== 7. Static =====");
        BankAccount acc1 = new BankAccount("Alice", 1000);
        BankAccount acc2 = new BankAccount("Bob",   500);
        BankAccount acc3 = new BankAccount("Carol", 750);
        acc1.displayBalance();
        acc2.displayBalance();
        acc3.displayBalance();
        System.out.println("Total accounts: " + BankAccount.getTotalAccounts()); // called on class, not object
        System.out.println();

        // ----- 8. Super keyword -----
        System.out.println("===== 8. Super =====");
        Employee emp = new Employee("Aayush", 20, "Google");
        emp.introduce();  // calls super.introduce() then adds company line
    }
}
