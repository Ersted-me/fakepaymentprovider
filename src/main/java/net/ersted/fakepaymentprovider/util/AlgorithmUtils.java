package net.ersted.fakepaymentprovider.util;

public class AlgorithmUtils {
    public static int fibonacci(int n) {
        int a = 0;
        int b = 1;
        if (n == 0) {
            return a;
        }
        if (n == 1) {
            return b;
        }
        for (int i = 2; i <= n; ++i) {
            int next = a + b;
            a = b;
            b = next;
        }
        return b;
    }
}
