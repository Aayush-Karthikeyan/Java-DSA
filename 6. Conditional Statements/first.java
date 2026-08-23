import java.util.*;

public class first {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter year: ");
        int year = sc.nextInt();

        if (year % 4 != 0) {
            System.out.println("Not a Leap Year");
        } else if (year % 100 != 0) {
            System.out.println("Leap Year");
        } else if (year % 400 != 0) {
            System.out.println("Not a Leap Year");
        } else {
            System.out.println("Leap Year");
        }

        sc.close();
    }
}
