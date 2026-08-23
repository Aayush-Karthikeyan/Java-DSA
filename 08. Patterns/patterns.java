// import java.util.*;

public class patterns {
    public static void main(String[] args) {
        int n = 4;

        // ===================== STAR TRIANGLE =====================
        // *
        // * *
        // * * *
        // * * * *
        for (int line = 1; line <= n; line++) {
            for (int star = 1; star <= line; star++) {
                System.out.print("* ");
            }
            System.out.println();
        }

        // ===================== INVERTED STAR =====================
        // * * * *
        // * * *
        // * *
        // *
        // for (int line = 1; line <= n; line++) {
        //     for (int star = 1; star <= (n - line + 1); star++) {
        //         System.out.print("* ");
        //     }
        //     System.out.println();
        // }

        // ===================== NUMBER PYRAMID =====================
        // 1
        // 12
        // 123
        // 1234
        // for (int line = 1; line <= n; line++) {
        //     for (int star = 1; star <= line; star++) {
        //         System.out.print(star);
        //     }
        //     System.out.println();
        // }

        // ===================== CHARACTER PYRAMID =====================
        // A
        // BC
        // DEF
        // GHIJ
        // char ch = 'A';
        // for (int line = 1; line <= n; line++) {
        //     for (int star = 1; star <= line; star++) {
        //         System.out.print(ch++);
        //     }
        //     System.out.println();
        // }
    }
}