package group_3;

import java.util.*;
public class skip_search {
    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        Map<Character, List<Integer>> table = new HashMap<>();
        for (int j = 0; j < m; j++) {
            char c = pattern.charAt(j);
            table.putIfAbsent(c, new ArrayList<>());
            table.get(c).add(j);
        }
        int i = 0;
        while (i <= n - m) {
            char c = text.charAt(i + m - 1);
            if (table.containsKey(c)) {
                int k = 0;
                while (k < m && pattern.charAt(k) == text.charAt(i + k)) {
                    k++;
                }
                if (k == m) res.add(i);
            }
            i += m;
        }
        return res;
    }
    public static void main(String[] args) {
        System.out.println(search("ABCDABCDABCD", "BCD"));
    }
}
