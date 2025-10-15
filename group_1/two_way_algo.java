package group_1;

import java.util.*;
public class two_way_algo {
    private static int[] maxSuffix(String s, boolean leq) {
        int n = s.length(), i = -1, j = 0, k = 1, p = 1;
        while (j + k < n) {
            char a = s.charAt(j + k);
            char b = (i + k >= 0) ? s.charAt(i + k) : 0;
            if ((leq && a < b) || (!leq && a <= b)) {
                j += k; k = 1; p = j - 1;
            } else if (a == b) {
                if (k != p) k++;
                else {j += p; k = 1;}
            } else {
                i = j; j ++; k = p = 1;
            }
        }
        return new int[]{i + 1, p};
    }
    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i++) res.add(i);
            return res;
        }
        int[] ms1 = maxSuffix(pattern, true);
        int[] ms2 = maxSuffix(pattern, false);
        int pos = Math.max(ms1[0], ms2[0]);
        int period = Math.max(ms1[1], ms2[1]);

        int i = 0, j = 0;
        while (i <= n - m) {
            while (j < m && pattern.charAt(pos + j) == text.charAt(i + pos + j)) j ++;
            if (j < m) {i += Math.max(1, j - period + 1); j = 0; continue;}
            int k = 1;
            while (k <= pos && pattern.charAt(pos - k) == text.charAt(i + pos - k)) k++;
            if (k > pos) res.add(i);
            i += period; j = 0;
        }
        return res;
    
    }
    public static void main(String[] args) {
        System.out.println(search("ABCABCDABCDABCDA", "ABCDABC"));
    }
}
