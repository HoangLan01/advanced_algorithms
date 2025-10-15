package trai_sang_phai;

public class KMPA {

    // Hàm tính bảng LPS (Longest Prefix Suffix)
    public static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0; // độ dài của tiền tố trùng khớp hiện tại
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // quay lại giá trị LPS trước đó
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // Hàm tìm kiếm chuỗi pattern trong text
    public static void KMPSearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] lps = computeLPS(pattern);

        int i = 0; // chỉ số text
        int j = 0; // chỉ số pattern

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                System.out.println("Pattern found at index " + (i - j));
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
        }
    }

    // Hàm main để kiểm nghiệm
    public static void main(String[] args) {
        String text = "ABABABCABABAC";
        String pattern = "ABABAC";
        KMPSearch(text, pattern);
    }
}

