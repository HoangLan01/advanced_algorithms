package group_1;

import java.util.*;
public class galil_seiferas {
    private static int computePeriod(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        for (int i = 1; i < m; i++) {
            int j = pi[i - 1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) j = pi[j - 1];
            if (pattern.charAt(i) == pattern.charAt(j)) j++;
            pi[i] = j;
        }
        int p = m - pi[m - 1];
        return (pi[m - 1] > 0 && m % p == 0) ? p : m;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) res.add(i);
            return res;
        }
        int period = computePeriod(pattern);

        for (int i = 0; i <= n - m;) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) j++;
            if (j == m) {
                res.add(i);
                i += period;
            } else if (j >= m - period) {
                i += Math.max(1, j - (m - period) + 1);
            } else {
                i++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(search("AAAAABAAAABAAAA", "AAAAB"));
    }
}
