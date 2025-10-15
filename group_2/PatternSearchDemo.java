package group_2;

/**
 *
 * Các lớp:
 * 1. BoyerMoore
 * 2. Horspool
 * 3. QuickSearch
 * 4. TurboBoyerMoore
 * 5. TunedBoyerMoore
 * 6. ZhuTakaoka
 * 7. BerryRavindran
 * 8. Raita
 * 9. OptimalMismatch
 * 10. MaximalShift
 *
 * Mỗi lớp có constructor(String pat) và int search(String txt) -> index or -1.
 */

public class PatternSearchDemo {

    ////////////////////////////
    // 1) Boyer–Moore (BM)
    // Bad character + Good suffix.
    ////////////////////////////
    static class BoyerMoore {
        private final String pat;
        private final int[] bad; // bad char
        private final int[] suffix; // for good suffix
        private final int[] prefix;  // for good suffix

        public BoyerMoore(String pat) {
            this.pat = pat;
            this.bad = buildBadChar(pat);
            int m = pat.length();
            suffix = new int[m];
            prefix = new int[m];
            buildGoodSuffix(pat, suffix, prefix);
        }

        private int[] buildBadChar(String p) {
            int[] bad = new int[256];
            for (int i = 0; i < bad.length; i++) bad[i] = -1;
            for (int j = 0; j < p.length(); j++) bad[p.charAt(j)] = j;
            return bad;
        }

        // build good suffix arrays: suffix[k] = start index of suffix of length k matched, prefix[k]=true if suffix of length k is also prefix
        private void buildGoodSuffix(String p, int[] suffix, int[] prefix) {
            int m = p.length();
            for (int i = 0; i < m; i++) { suffix[i] = -1; prefix[i] = 0; }
            for (int i = 0; i < m - 1; i++) {
                int j = i;
                int k = 0; // length of matching suffix
                while (j >= 0 && p.charAt(j) == p.charAt(m - 1 - k)) {
                    j--; k++;
                    suffix[k] = j + 1;
                }
                if (j == -1) prefix[k] = 1;
            }
        }

        // calculate shift by good suffix rule when mismatch at position j
        private int moveByGoodSuffix(int j) {
            int m = pat.length();
            int k = m - 1 - j; // length of suffix matched
            if (k == 0) return 0;
            if (suffix[k] != -1) return j - suffix[k] + 1;
            for (int r = j + 2; r <= m - 1; r++) {
                if (prefix[m - r] == 1) return r;
            }
            return m;
        }

        public int search(String txt) {
            int n = txt.length(), m = pat.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int j;
                for (j = m - 1; j >= 0; j--) {
                    if (pat.charAt(j) != txt.charAt(i + j)) break;
                }
                if (j < 0) return i;
                int bcShift = j - bad[txt.charAt(i + j)];
                int gsShift = moveByGoodSuffix(j);
                int shift = Math.max(1, Math.max(bcShift, gsShift));
                i += shift;
            }
            return -1;
        }
    }

    ////////////////////////////
    // 2) Horspool
    // Only bad-character (based on character in pattern except last char).
    ////////////////////////////
    static class Horspool {
        private final String pat;
        private final int[] shift;

        public Horspool(String pat) {
            this.pat = pat;
            int m = pat.length();
            shift = new int[256];
            for (int i = 0; i < 256; i++) shift[i] = m;
            for (int i = 0; i < m - 1; i++) shift[pat.charAt(i)] = m - 1 - i;
        }

        public int search(String txt) {
            int n = txt.length(), m = pat.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int k = 0;
                while (k < m && pat.charAt(m - 1 - k) == txt.charAt(i + m - 1 - k)) k++;
                if (k == m) return i;
                i += shift[txt.charAt(i + m - 1)];
            }
            return -1;
        }
    }

    ////////////////////////////
    // 3) Quick Search
    // Uses character after the current window to compute shift.
    ////////////////////////////
    static class QuickSearch {
        private final String pat;
        private final int[] shift;

        public QuickSearch(String pat) {
            this.pat = pat;
            int m = pat.length();
            shift = new int[256];
            for (int i = 0; i < 256; i++) shift[i] = m + 1;
            for (int i = 0; i < m; i++) shift[pat.charAt(i)] = m - i;
        }

        public int search(String txt) {
            int n = txt.length(), m = pat.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int j = 0;
                while (j < m && pat.charAt(j) == txt.charAt(i + j)) j++;
                if (j == m) return i;
                if (i + m >= n) break;
                i += shift[txt.charAt(i + m)];
            }
            return -1;
        }
    }

    ////////////////////////////
    // 4) Turbo Boyer–Moore (simplified practical implementation)
    // Reuses BM tables and keeps 'u' (matched suffix reuse) to possibly shift more.
    // Reference idea: when we have partial overlap from previous attempt, can reuse.
    ////////////////////////////
    static class TurboBoyerMoore {
        private final String pat;
        private final int[] bad;
        private final int[] suffix;
        private final int[] prefix;

        public TurboBoyerMoore(String pat) {
            this.pat = pat;
            bad = new int[256];
            for (int i = 0; i < 256; i++) bad[i] = -1;
            for (int j = 0; j < pat.length(); j++) bad[pat.charAt(j)] = j;
            suffix = new int[pat.length()];
            prefix = new int[pat.length()];
            buildGoodSuffix(pat, suffix, prefix);
        }

        private void buildGoodSuffix(String p, int[] suffix, int[] prefix) {
            int m = p.length();
            for (int i = 0; i < m; i++) { suffix[i] = -1; prefix[i] = 0; }
            for (int i = 0; i < m - 1; i++) {
                int j = i, k = 0;
                while (j >= 0 && p.charAt(j) == p.charAt(m - 1 - k)) {
                    j--; k++;
                    suffix[k] = j + 1;
                }
                if (j == -1) prefix[k] = 1;
            }
        }

        private int moveByGoodSuffix(int j) {
            int m = pat.length();
            int k = m - 1 - j;
            if (k == 0) return 0;
            if (suffix[k] != -1) return j - suffix[k] + 1;
            for (int r = j + 2; r <= m - 1; r++) {
                if (prefix[m - r] == 1) return r;
            }
            return m;
        }

        public int search(String txt) {
            int n = txt.length(), m = pat.length();
            if (m == 0) return 0;
            int i = 0;
            int u = 0; // suffix matched from previous attempt
            while (i <= n - m) {
                int j = m - 1;
                while (j >= 0 && pat.charAt(j) == txt.charAt(i + j)) j--;
                if (j < 0) return i;
                int bcShift = j - bad[txt.charAt(i + j)];
                int gsShift = moveByGoodSuffix(j);
                int shift = Math.max(bcShift, gsShift);
                // Turbo idea: if previous suffix matched u > 0 and gsShift < u+1 then use max(shift, u+1)
                if (u > 0 && shift < u + 1) {
                    shift = u + 1;
                }
                // if the shift equals gsShift, we can reuse matched suffix length; otherwise reset u
                if (shift == gsShift) {
                    u = Math.min(m - shift, m - 1 - j);
                } else {
                    if (shift < m - 1 - j) u = 0;
                    else u = m - shift;
                }
                if (shift < 1) shift = 1;
                i += shift;
            }
            return -1;
        }
    }

    ////////////////////////////
    // 5) Tuned Boyer–Moore (practical tuned version)
    // Micro-optimizations in the matching loop: check last char, middle char, first char fast.
    ////////////////////////////
    static class TunedBoyerMoore {
        private final String pat;
        private final int[] bad;
        private final int m;

        public TunedBoyerMoore(String pat) {
            this.pat = pat;
            this.m = pat.length();
            bad = new int[256];
            for (int i = 0; i < 256; i++) bad[i] = -1;
            for (int j = 0; j < m; j++) bad[pat.charAt(j)] = j;
        }

        public int search(String txt) {
            int n = txt.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                // fast checks: last, middle, first
                if (pat.charAt(m - 1) != txt.charAt(i + m - 1)) {
                    i += Math.max(1, m - 1 - bad[txt.charAt(i + m - 1)]);
                    continue;
                }
                int mid = m / 2;
                if (pat.charAt(mid) != txt.charAt(i + mid)) {
                    i += Math.max(1, m - 1 - bad[txt.charAt(i + m - 1)]);
                    continue;
                }
                if (pat.charAt(0) != txt.charAt(i)) {
                    i += Math.max(1, m - bad[txt.charAt(i)]);
                    continue;
                }
                // full compare
                int j;
                for (j = m - 2; j > 0; j--) {
                    if (pat.charAt(j) != txt.charAt(i + j)) break;
                }
                if (j == 0) return i; // found
                i += Math.max(1, j - bad[txt.charAt(i + j)]);
            }
            return -1;
        }
    }

    ////////////////////////////
    // 6) Zhu–Takaoka
    // Uses two-character bad-character shift (pair-based)
    ////////////////////////////
    static class ZhuTakaoka {
        private final String pat;
        private final int[] shift; // size 65536 for pairs
        private final int m;

        public ZhuTakaoka(String pat) {
            this.pat = pat;
            this.m = pat.length();
            int size = 1 << 16;
            shift = new int[size];
            for (int i = 0; i < size; i++) shift[i] = m + 1;
            for (int i = 0; i < m; i++) shift[pat.charAt(i)] = m - i;
            // pairs
            for (int i = 0; i < m - 1; i++) {
                int key = ((pat.charAt(i) & 0xFF) << 8) | (pat.charAt(i + 1) & 0xFF);
                shift[key] = m - i - 1;
            }
        }

        public int search(String txt) {
            int n = txt.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int j = m - 1;
                while (j >= 0 && pat.charAt(j) == txt.charAt(i + j)) j--;
                if (j < 0) return i;
                int nextPos = i + m - 1;
                if (nextPos + 1 >= n) { // fallback to single char shift
                    i += Math.max(1, m - 1 - ( (txt.charAt(i + j)) < 256 ? txt.charAt(i + j) : 0 ));
                } else {
                    int a = txt.charAt(nextPos - 1) & 0xFF;
                    int b = txt.charAt(nextPos) & 0xFF;
                    int key = (a << 8) | b;
                    i += shift[key];
                }
            }
            return -1;
        }
    }

    ////////////////////////////
    // 7) Berry–Ravindran
    // Uses two-character shift from characters just after the window (like QuickSearch but double chars).
    ////////////////////////////
    static class BerryRavindran {
        private final String pat;
        private final int[] shift; // pairs
        private final int m;

        public BerryRavindran(String pat) {
            this.pat = pat;
            this.m = pat.length();
            int size = 1 << 16;
            shift = new int[size];
            for (int i = 0; i < size; i++) shift[i] = m + 2; // default
            for (int i = 0; i < m; i++) shift[pat.charAt(i)] = m - i;
            for (int i = 0; i < m - 1; i++) {
                int key = ((pat.charAt(i) & 0xFF) << 8) | (pat.charAt(i + 1) & 0xFF);
                shift[key] = m - i - 1;
            }
        }

        public int search(String txt) {
            int n = txt.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int j = 0;
                while (j < m && pat.charAt(j) == txt.charAt(i + j)) j++;
                if (j == m) return i;
                int pos = i + m;
                if (pos + 1 < n) {
                    int a = txt.charAt(pos) & 0xFF;
                    int b = txt.charAt(pos + 1) & 0xFF;
                    int key = (a << 8) | b;
                    i += shift[key];
                } else if (pos < n) {
                    i += shift[txt.charAt(pos)];
                } else {
                    break;
                }
            }
            return -1;
        }
    }

    ////////////////////////////
    // 8) Raita Algorithm
    // Order of comparison: middle -> last -> first -> rest
    ////////////////////////////
    static class Raita {
        private final String pat;
        private final int[] bad;
        private final int m;

        public Raita(String pat) {
            this.pat = pat;
            this.m = pat.length();
            bad = new int[256];
            for (int i = 0; i < 256; i++) bad[i] = m;
            for (int i = 0; i < m - 1; i++) bad[pat.charAt(i)] = m - 1 - i;
        }

        public int search(String txt) {
            int n = txt.length();
            if (m == 0) return 0;
            int i = 0;
            int mid = m / 2;
            while (i <= n - m) {
                char lastText = txt.charAt(i + m - 1);
                if (pat.charAt(mid) == txt.charAt(i + mid) &&
                    pat.charAt(m - 1) == lastText &&
                    pat.charAt(0) == txt.charAt(i)) {
                    // check remaining characters
                    int j;
                    for (j = 1; j < m - 1; j++) {
                        if (pat.charAt(j) != txt.charAt(i + j)) break;
                    }
                    if (j == m - 1) return i;
                }
                i += bad[lastText];
            }
            return -1;
        }
    }

    ////////////////////////////
    // 9) Optimal Mismatch
    // Compare pattern positions in order chosen by character rarity in pattern (rarest position first).
    ////////////////////////////
    static class OptimalMismatch {
        private final String pat;
        private final int[] bad;
        private final int[] order; // order of indices to compare
        private final int m;

        public OptimalMismatch(String pat) {
            this.pat = pat;
            this.m = pat.length();
            bad = new int[256];
            for (int i = 0; i < 256; i++) bad[i] = -1;
            for (int i = 0; i < m; i++) bad[pat.charAt(i)] = i;

            // compute frequency of each char in pattern
            int[] freq = new int[256];
            for (int i = 0; i < m; i++) freq[pat.charAt(i)]++;

            // order indices by freq of char at that index (ascending -> rare first), tie-break by distance from end maybe
            Integer[] idx = new Integer[m];
            for (int i = 0; i < m; i++) idx[i] = i;
            java.util.Arrays.sort(idx, (a, b) -> {
                int fa = freq[pat.charAt(a)], fb = freq[pat.charAt(b)];
                if (fa != fb) return Integer.compare(fa, fb);
                // prefer comparing end positions earlier for right-to-left flavor
                return Integer.compare(Math.abs(m - 1 - a), Math.abs(m - 1 - b));
            });
            order = new int[m];
            for (int i = 0; i < m; i++) order[i] = idx[i];
        }

        public int search(String txt) {
            int n = txt.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                boolean ok = true;
                for (int k = 0; k < m; k++) {
                    int j = order[k];
                    if (pat.charAt(j) != txt.charAt(i + j)) {
                        // bad char shift fallback (use last char)
                        int shift = Math.max(1, j - bad[txt.charAt(i + j)]);
                        i += shift;
                        ok = false;
                        break;
                    }
                }
                if (ok) return i;
            }
            return -1;
        }
    }

    ////////////////////////////
    // 10) Maximal Shift
    // Use max(bad-character-shift, good-suffix-shift). (Simple practical implementation)
    ////////////////////////////
    static class MaximalShift {
        private final String pat;
        private final int[] bad;
        private final int[] suffix;
        private final int[] prefix;

        public MaximalShift(String pat) {
            this.pat = pat;
            bad = new int[256];
            for (int i = 0; i < 256; i++) bad[i] = -1;
            for (int j = 0; j < pat.length(); j++) bad[pat.charAt(j)] = j;
            suffix = new int[pat.length()];
            prefix = new int[pat.length()];
            buildGoodSuffix(pat, suffix, prefix);
        }

        private void buildGoodSuffix(String p, int[] suffix, int[] prefix) {
            int m = p.length();
            for (int i = 0; i < m; i++) { suffix[i] = -1; prefix[i] = 0; }
            for (int i = 0; i < m - 1; i++) {
                int j = i, k = 0;
                while (j >= 0 && p.charAt(j) == p.charAt(m - 1 - k)) {
                    j--; k++;
                    suffix[k] = j + 1;
                }
                if (j == -1) prefix[k] = 1;
            }
        }

        private int moveByGoodSuffix(int j) {
            int m = pat.length();
            int k = m - 1 - j;
            if (k == 0) return 0;
            if (suffix[k] != -1) return j - suffix[k] + 1;
            for (int r = j + 2; r <= m - 1; r++) {
                if (prefix[m - r] == 1) return r;
            }
            return m;
        }

        public int search(String txt) {
            int n = txt.length(), m = pat.length();
            if (m == 0) return 0;
            int i = 0;
            while (i <= n - m) {
                int j = m - 1;
                while (j >= 0 && pat.charAt(j) == txt.charAt(i + j)) j--;
                if (j < 0) return i;
                int bcShift = j - bad[txt.charAt(i + j)];
                int gsShift = moveByGoodSuffix(j);
                int shift = Math.max(1, Math.max(bcShift, gsShift)); // maximal of the shifts
                i += shift;
            }
            return -1;
        }
    }

    // ----- quick demo -----
    public static void main(String[] args) {
        String text = "HERE IS A SIMPLE EXAMPLE";
        String pattern = "EXAMPLE";

        System.out.println("Text:    " + text);
        System.out.println("Pattern: " + pattern);
        System.out.println();

        System.out.println("BoyerMoore: " + new BoyerMoore(pattern).search(text));
        System.out.println("Horspool: " + new Horspool(pattern).search(text));
        System.out.println("QuickSearch: " + new QuickSearch(pattern).search(text));
        System.out.println("TurboBoyerMoore: " + new TurboBoyerMoore(pattern).search(text));
        System.out.println("TunedBoyerMoore: " + new TunedBoyerMoore(pattern).search(text));
        System.out.println("ZhuTakaoka: " + new ZhuTakaoka(pattern).search(text));
        System.out.println("BerryRavindran: " + new BerryRavindran(pattern).search(text));
        System.out.println("Raita: " + new Raita(pattern).search(text));
        System.out.println("OptimalMismatch: " + new OptimalMismatch(pattern).search(text));
        System.out.println("MaximalShift: " + new MaximalShift(pattern).search(text));
    }
}
