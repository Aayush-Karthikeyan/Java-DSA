import java.util.*;

public class loop {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ===================== WHILE LOOP =====================
        // Print numbers 1 to n
        // int n = sc.nextInt();
        // int i = 1;
        // while (i <= n) {
        //     System.out.print(i + " ");
        //     i++;
        // }

        // Sum of first N natural numbers
        // int n = sc.nextInt();
        // int sum = 0, i = 1;
        // while (i <= n) {
        //     sum += i;
        //     i++;
        // }
        // System.out.println(sum);

        // Reverse a number
        // int n = sc.nextInt();
        // int rev = 0;
        // while (n > 0) {
        //     int LD = n % 10;
        //     rev = (rev * 10) + LD;
        //     n = n / 10;
        // }
        // System.out.println(rev);

        // ===================== FOR LOOP =====================
        // Print numbers 1 to n
        // int n = sc.nextInt();
        // for (int i = 1; i <= n; i++) {
        //     System.out.print(i + " ");
        // }

        // Print square pattern (n x n)
        // int n = sc.nextInt();
        // for (int i = 1; i <= n; i++) {
        //     for (int j = 1; j <= n; j++) {
        //         System.out.print("* ");
        //     }
        //     System.out.println();
        // }

        // ===================== DO-WHILE LOOP =====================
        // Runs at least once
        // int i = 1;
        // do {
        //     System.out.print(i + " ");
        //     i++;
        // } while (i <= 5);

        // ===================== BREAK =====================
        // Stop loop when number found
        // int n = sc.nextInt();
        // for (int i = 1; i <= 10; i++) {
        //     if (i == n) {
        //         System.out.println("Found: " + i);
        //         break;
        //     }
        // }

        // ===================== CONTINUE =====================
        // Print all numbers except n
        // int n = sc.nextInt();
        // for (int i = 1; i <= 10; i++) {
        //     if (i == n) continue;
        //     System.out.print(i + " ");
        // }

        // ===================== PRIME CHECK =====================
        // int n = sc.nextInt();
        // boolean isPrime = true;
        // if (n <= 1) isPrime = false;
        // for (int i = 2; i <= Math.sqrt(n); i++) {
        //     if (n % i == 0) {
        //         isPrime = false;
        //         break;
        //     }
        // }
        // System.out.println(isPrime ? "Prime" : "Not Prime");
        // ===================== REVERSE - PRINT DIGITS REVERSED =====================
        // (just prints digits in reverse order)
        // int n = sc.nextInt();
        // while (n > 0) {
        //     int LD = n % 10;
        //     System.out.print(LD + " ");
        //     n = n / 10;
        // }

        // ===================== REVERSE - ACTUAL REVERSED NUMBER =====================
        // (builds the reversed number)
        // int n = sc.nextInt();
        // int rev = 0;
        // while (n > 0) {
        //     int LD = n % 10;
        //     rev = (rev * 10) + LD;
        //     n = n / 10;
        // }
        // System.out.println(rev);

        sc.close();
    }
}