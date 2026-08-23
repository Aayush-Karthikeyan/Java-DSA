# 11. Arrays Part 1 — Notes

---

## 1. Arrays as Function Arguments (Pass by Reference)

In Java, arrays are passed by **reference** — not a copy.
This means the function modifies the **original** array.

```java
public static void update(int marks[]) {
    for (int i = 0; i < marks.length; i++) {
        marks[i] = marks[i] + 1; // modifies original array
    }
}
```

> Primitives (int, float) → pass by VALUE (copy, original unchanged)
> Arrays → pass by REFERENCE (same memory, original changes)

---

## 2. Linear Search — O(n)

Check every element one by one until target is found.

```java
public static int linearSearch(int arr[], int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) return i; // return index if found
    }
    return -1; // not found
}
```

---

## 3. Largest in Array — O(n)

Assume first element is largest, compare with rest.

```java
public static int largest(int arr[]) {
    int max = arr[0]; // assume first is largest
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] > max) {
            max = arr[i]; // update if bigger found
        }
    }
    return max;
}
```

---

## 4. Binary Search — O(log n)

Only works on **sorted arrays**. Eliminates half the array each step.

```java
public static int binarySearch(int arr[], int target) {
    int start = 0;
    int end = arr.length - 1;

    while (start <= end) {
        int mid = start + (end - start) / 2; // avoids overflow

        if (arr[mid] == target) return mid;       // found
        else if (arr[mid] < target) start = mid + 1; // go right
        else end = mid - 1;                          // go left
    }
    return -1; // not found
}
```

> Why `start + (end - start) / 2` instead of `(start + end) / 2`?
> Avoids integer overflow for large arrays. Same result, safer.

---

## 5. Reverse an Array — O(n)

Two pointers — swap from both ends moving inward.

```java
public static void reverse(int arr[]) {
    int start = 0;
    int end = arr.length - 1;

    while (start < end) {
        int temp = arr[start]; // swap
        arr[start] = arr[end];
        arr[end] = temp;
        start++;
        end--;
    }
}
```

---

## 6. Pairs in Array — O(n²)

For every element, pair it with every element after it.

```java
public static void pairs(int arr[]) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) { // j starts at i+1 to avoid duplicates
            System.out.println(arr[i] + " " + arr[j]);
        }
    }
}
```

> For `[1,2,3]` → (1,2) (1,3) (2,3)

---

## 7. Print Subarrays — O(n³)

- `i` = start of subarray (FREEZES)
- `j` = end of subarray (walks forward)
- `k` = printer (sprints from i to j, then dies)

```java
public static void subArrays(int arr[]) {
    for (int i = 0; i < arr.length; i++) {        // i = start, freezes
        for (int j = i; j < arr.length; j++) {    // j = end, walks forward
            for (int k = i; k <= j; k++) {        // k = printer, sprints i to j
                System.out.print(arr[k] + " ");
            }
            System.out.println();
        }
    }
}
```

> For `[1,2,3,4]`:
> i freezes at 0 → j walks → k prints: [1] [1,2] [1,2,3] [1,2,3,4]
> i freezes at 1 → j walks → k prints: [2] [2,3] [2,3,4]
> ... and so on

---

## 8. Maximum Subarray Sum

Find the subarray whose elements add up to the **largest sum**.

### Approach 1: Brute Force — O(n³)
Check every subarray, calculate its sum, track max.

```java
public static int maxSumBrute(int arr[]) {
    int maxSum = Integer.MIN_VALUE; // start with most negative number possible

    for (int i = 0; i < arr.length; i++) {        // i = start
        for (int j = i; j < arr.length; j++) {    // j = end
            int sum = 0;
            for (int k = i; k <= j; k++) {        // k = calculate sum i to j
                sum += arr[k];
            }
            maxSum = Math.max(maxSum, sum);        // keep bigger of maxSum and sum
        }
    }
    return maxSum;
}
```

---

### Approach 2: Prefix Sum — O(n²)
Pre-calculate running totals. Sum of i to j = `prefix[j] - prefix[i-1]`. No k loop needed.

> Think of it like a bank balance:
> prefix[3] = total balance at day 4
> prefix[0] = balance before your subarray started
> subtract → you get just that period's earnings

```java
public static int maxSumPrefix(int arr[]) {
    int maxSum = Integer.MIN_VALUE;
    int prefix[] = new int[arr.length]; // running total array

    prefix[0] = arr[0];                            // first element is just itself
    for (int i = 1; i < arr.length; i++) {
        prefix[i] = prefix[i-1] + arr[i];          // running total: prev balance + today
    }

    for (int i = 0; i < arr.length; i++) {
        for (int j = i; j < arr.length; j++) {
            int sum = i == 0                        // if subarray starts at 0...
                    ? prefix[j]                     // ...just take prefix[j]
                    : prefix[j] - prefix[i-1];     // ...else subtract what came before
            maxSum = Math.max(maxSum, sum);
        }
    }
    return maxSum;
}
```

---

### Approach 3: Kadane's Algorithm — O(n) ✅ (Use this in interviews)
At every element, ask: extend current subarray or start fresh?
If running sum goes negative → reset. A negative sum only drags future elements down.

```java
public static int maxSumKadane(int arr[]) {
    int maxSum = Integer.MIN_VALUE; // track overall best
    int currentSum = 0;             // track current subarray sum

    for (int i = 0; i < arr.length; i++) {
        currentSum += arr[i];                   // add current element
        maxSum = Math.max(maxSum, currentSum);  // update max if bigger
        if (currentSum < 0) {                   // if sum went negative...
            currentSum = 0;                     // ...ditch it, start fresh
        }
    }
    return maxSum;
}
```

> Trace `[-2, 1, -3, 4, -1, 2, 1]`:
> -2 → reset → max=-2
>  1 → sum=1 → max=1
> -3 → reset → max=1
>  4 → sum=4 → max=4
> -1 → sum=3 → max=4
>  2 → sum=5 → max=5
>  1 → sum=6 → max=6 ✅

---

## Time Complexity Summary

| Topic | Time Complexity |
|---|---|
| Linear Search | O(n) |
| Largest in Array | O(n) |
| Binary Search | O(log n) |
| Reverse Array | O(n) |
| Pairs in Array | O(n²) |
| Print Subarrays | O(n³) |
| Max Subarray - Brute | O(n³) |
| Max Subarray - Prefix | O(n²) |
| Max Subarray - Kadane's | O(n) |
