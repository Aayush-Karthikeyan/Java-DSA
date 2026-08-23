import java.util.*;

public class func {
    // ===================== FACTORIAL =====================
    static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    // ===================== PRIME CHECK (OPTIMIZED) =====================
    static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // ===================== BINOMIAL COEFFICIENT =====================
    static int nCr(int n, int r) {
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    // ===================== BINARY TO DECIMAL =====================
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

    // ===================== DECIMAL TO BINARY =====================
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

    // ===================== PRIMES IN RANGE =====================
    static void primesInRange(int start, int end) {
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) System.out.print(i + " ");
        }
        System.out.println();
    }

    // ===================== FUNCTION OVERLOADING =====================
    static int add(int a, int b) { return a + b; }
    static double add(double a, double b) { return a + b; }
    static int add(int a, int b, int c) { return a + b + c; }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Test factorial
        // System.out.println(factorial(5)); // 120

        // Test prime
        // System.out.println(isPrime(17)); // true

        // Test nCr
        // System.out.println(nCr(5, 2)); // 10

        // Test binary to decimal
        // System.out.println(binaryToDecimal(101)); // 5

        // Test decimal to binary
        // decimalToBinary(5); // 101

        // Test primes in range
        // primesInRange(1, 20); // 2 3 5 7 11 13 17 19

        // Test overloading
        // System.out.println(add(2, 3));       // 5
        // System.out.println(add(2.5, 3.5));   // 6.0
        // System.out.println(add(1, 2, 3));    // 6

        sc.close();
    }
}
