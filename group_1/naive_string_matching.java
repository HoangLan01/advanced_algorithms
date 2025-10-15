package group_1;

import java.util.*;

public class naive_string_matching {
    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i ++) res.add(i);
            return res;
        }
        for (int i = 0; i <= n - m; i ++) {
            int j = 0;
            while (j < m && text.charAt(i + j) == pattern.charAt(j)) j ++;
            if (j == m) res.add(i);
        }
        return res;
    }
    public static void main(String[] args) {
        System.out.println(search("ABAAABCD", "ABC"));
    }
}
