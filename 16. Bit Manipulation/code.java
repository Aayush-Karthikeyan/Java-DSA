public class code {

    // ===================================================================
    // TOPIC: BIT MANIPULATION
    // All operations work directly on binary (0s and 1s) representation
    // of integers — extremely fast, O(1) for most operations.
    // ===================================================================


    // ===== GET ith BIT =====
    // Find the value (0 or 1) of the bit at position i (0 = rightmost)
    // Strategy: left-shift 1 by i places to create a "mask", then AND with n
    // If the result is non-zero, the bit is 1; else it's 0.
    public static int getIthBit(int n, int i) {
        int mask = 1 << i;           // e.g. i=2 -> mask = 0000...0100
        if ((n & mask) == 0) return 0;  // AND isolates the ith bit; if 0, bit is 0
        return 1;                    // otherwise bit is 1
    }


    // ===== SET ith BIT =====
    // Force the bit at position i to become 1 (regardless of current value)
    // Strategy: OR with a mask that has 1 only at position i
    public static int setIthBit(int n, int i) {
        int mask = 1 << i;           // mask: only bit i is 1
        return n | mask;             // OR sets that bit to 1, leaves others unchanged
    }


    // ===== CLEAR ith BIT =====
    // Force the bit at position i to become 0 (regardless of current value)
    // Strategy: AND with a mask that has 0 only at position i (all others 1)
    public static int clearIthBit(int n, int i) {
        int mask = ~(1 << i);        // ~(mask) flips all bits: 1111...1011 (0 only at position i)
        return n & mask;             // AND clears that bit to 0, leaves others unchanged
    }


    // ===== UPDATE ith BIT =====
    // Set the bit at position i to a given value (0 or 1)
    // Strategy: clear the bit first, then OR with the new value shifted to position i
    public static int updateIthBit(int n, int i, int newBit) {
        n = clearIthBit(n, i);       // first clear the bit (make it 0)
        int mask = newBit << i;      // shift new value to correct position
        return n | mask;             // OR places the new bit value at position i
    }


    // ===== CLEAR LAST i BITS =====
    // Set the rightmost i bits all to 0
    // Strategy: create a mask with i zeros at the end, AND with n
    public static int clearLastIBits(int n, int i) {
        int mask = (-1) << i;        // -1 in binary is all 1s; shift left i places makes i zeros at end
        return n & mask;             // AND clears the last i bits
    }


    // ===== CLEAR RANGE OF BITS (from i to j, inclusive) =====
    // Set bits from position i up to position j all to 0
    // Strategy: create two masks and combine them
    public static int clearRange(int n, int i, int j) {
        int a = (-1) << (j + 1);    // all 1s from bit j+1 upward
        int b = (1 << i) - 1;       // all 1s below bit i (bits 0 to i-1)
        int mask = a | b;            // combine: 1s everywhere EXCEPT positions i to j
        return n & mask;             // AND zeros out the range i to j
    }


    // ===== CHECK ODD OR EVEN =====
    // The last bit (bit 0) tells you: 0 = even, 1 = odd
    public static String oddOrEven(int n) {
        if ((n & 1) == 0) return "Even";  // AND with 1 checks only the last bit
        return "Odd";
    }


    // ===== CHECK POWER OF 2 =====
    // Powers of 2 in binary have exactly one 1-bit: 4=100, 8=1000
    // n & (n-1) clears the lowest set bit. If result is 0, only one bit was set -> power of 2
    public static boolean isPowerOf2(int n) {
        if (n <= 0) return false;       // 0 and negatives are not powers of 2
        return (n & (n - 1)) == 0;     // true only if exactly one bit is set
    }


    // ===== COUNT SET BITS =====
    // Count how many bits are 1 in the binary representation of n
    // Strategy: repeatedly check the last bit, then right-shift
    public static int countSetBits(int n) {
        int count = 0;
        while (n > 0) {
            if ((n & 1) == 1) count++;  // check if last bit is 1
            n = n >> 1;                 // right shift: drop the last bit
        }
        return count;
    }


    // ===== FAST EXPONENTIATION =====
    // Calculate base^exp efficiently using bit tricks
    // Normal: multiply base exp times -> O(exp)
    // Fast:   halve the problem each step -> O(log exp)
    // Key insight: if exp is odd -> multiply once and make it even; if even -> square base and halve exp
    public static long fastExp(long base, int exp) {
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) {       // if current exp is odd (last bit is 1)
                result *= base;          // multiply result by current base
            }
            base *= base;               // square the base (handles the even part)
            exp >>= 1;                  // halve the exponent (right shift by 1)
        }
        return result;
    }


    public static void main(String[] args) {
        int n = 13;  // 13 in binary = 1101

        System.out.println("n = " + n + "  (binary: " + Integer.toBinaryString(n) + ")");
        System.out.println();

        // Get ith bit
        System.out.println("Get bit 0: " + getIthBit(n, 0));   // 1  (1101 -> last bit)
        System.out.println("Get bit 1: " + getIthBit(n, 1));   // 0
        System.out.println("Get bit 2: " + getIthBit(n, 2));   // 1

        // Set ith bit
        int setResult = setIthBit(n, 1);  // set bit 1 of 1101 -> 1111 = 15
        System.out.println("Set bit 1: " + setResult + " (" + Integer.toBinaryString(setResult) + ")");

        // Clear ith bit
        int clearResult = clearIthBit(n, 0);  // clear bit 0 of 1101 -> 1100 = 12
        System.out.println("Clear bit 0: " + clearResult + " (" + Integer.toBinaryString(clearResult) + ")");

        // Update ith bit
        int updateResult = updateIthBit(n, 2, 0);  // set bit 2 to 0 in 1101 -> 1001 = 9
        System.out.println("Update bit 2 to 0: " + updateResult + " (" + Integer.toBinaryString(updateResult) + ")");

        // Clear last i bits
        int clrLast = clearLastIBits(n, 2);  // clear last 2 bits of 1101 -> 1100 = 12
        System.out.println("Clear last 2 bits: " + clrLast + " (" + Integer.toBinaryString(clrLast) + ")");

        // Clear range
        int clrRange = clearRange(15, 1, 2);  // 15=1111, clear bits 1-2 -> 1001 = 9
        System.out.println("Clear bits 1-2 of 15: " + clrRange + " (" + Integer.toBinaryString(clrRange) + ")");

        // Odd / Even
        System.out.println("13 is: " + oddOrEven(13));  // Odd
        System.out.println("8 is:  " + oddOrEven(8));   // Even

        // Power of 2
        System.out.println("16 power of 2? " + isPowerOf2(16));  // true
        System.out.println("13 power of 2? " + isPowerOf2(13));  // false

        // Count set bits
        System.out.println("Set bits in 13 (1101): " + countSetBits(13));  // 3

        // Fast exponentiation
        System.out.println("2^10 = " + fastExp(2, 10));   // 1024
        System.out.println("3^5  = " + fastExp(3, 5));    // 243
    }
}
