package group_1;

import java.util.*;;
public class dfa_search {
    private static int[][] buildDFA(String pattern, Set<Character> alphabet) {
        int m = pattern.length();
        int[][] dfa = new int[m + 1][alphabet.size()];
        Map<Character, Integer> idx = new HashMap<>();
        int k = 0;
        for (char c: alphabet) idx.put(c, k++);

        dfa[0][idx.get(pattern.charAt(0))] = 1;
        int lps = 0;

        for (int j = 1; j < m; j ++) {
            for (char c : alphabet) {
                dfa[j][idx.get(c)] = dfa[lps][idx.get(c)];
            }
            dfa[j][idx.get(pattern.charAt(j))] = j + 1;
            lps = dfa[lps][idx.get(pattern.charAt(j))];
        }
        for (char c : alphabet) {
            dfa[m][idx.get(c)] = dfa[lps][idx.get(c)];
        }
        return dfa;
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> res = new ArrayList<>();
        if (pattern.isEmpty()) {
            for (int i = 0; i <= text.length(); i++) res.add(i);
            return res;
        }
        Set<Character> alphabet = new HashSet<>();
        for (char c : (text + pattern).toCharArray()) alphabet.add(c);
        int[][] dfa = buildDFA(pattern, alphabet);

        Map<Character, Integer> idx = new HashMap<>();
        int k = 0;
        for (char c : alphabet) idx.put(c, k++);

        int state = 0;
        int m = pattern.length();
        for (int i = 0; i < text.length(); i++) {
            state = dfa[state][idx.getOrDefault(text.charAt(i), 0)];
            if (state == m) res.add(i - m + 1);
        }
        return res;
    }
    public static void main(String[] args) {
        System.out.println(search("AABAACAADAABAABA", "AABA"));
    }
}
