package group_1;

import java.util.*;
public class rabin_karp {
    static final int BASE = 256;
    static final int MOD = 1000000007;

    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();
        if (m == 0) {
            for (int i = 0; i <= n; i ++) res.add(i);
            return res;
        }
        if (m > n) return res;
        long h = 1;
        for (int i = 0; i < m - 1; i++) h = (h * BASE) % MOD;
        long ph = 0;
        long th = 0;
        for (int i = 0; i < m; i++) {
            ph = (ph * BASE + pattern.charAt(i)) % MOD;
            th = (th * BASE + text.charAt(i)) % MOD;
        }
        for (int i = 0; i <= n -m; i++) {
            if (ph == th && text.substring(i, i +m).equals(pattern)) res.add(i);
            if (i < n - m) {
                th = (th - text.charAt(i) * h % MOD + MOD) % MOD;
                th = (th * BASE + text.charAt(i + m)) % MOD;
            }
        }
        return res;
    }
    public static void main(String[] args) {
        System.out.println(search("GEEKS FOR GEEKS", "GEEK"));
    }
}
