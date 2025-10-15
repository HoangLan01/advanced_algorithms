package group_3;

// import java.lang.reflect.Array;
import java.util.*;
public class alpha_skip_search {
    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        int[] alpha = new int[256];
        Arrays.fill(alpha, -1);
        for (int j = 0; j < m; j++) {
            alpha[pattern.charAt(j)] = j;
        }

        int i = 0;
        while (i <= n - m) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                j--;
            }
            if (j < 0) {
                res.add(i);
                i += m;
            } else {
                char c = text.charAt(i + m -1);
                if (alpha[c] == - 1) {
                    i += m;
                } else {
                    i++;
                }
            }
        }
        return res;
    }
    public static void main(String[] args) {
        System.out.println(search("ABCDABDABCD", "BCD"));
    }
}
