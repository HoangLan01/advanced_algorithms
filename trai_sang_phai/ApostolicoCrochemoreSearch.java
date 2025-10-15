package trai_sang_phai;

public class ApostolicoCrochemoreSearch {

    // Hàm tính mảng prefix function (giống KMP)
    static int[] computePrefix(String pattern) {
        int m = pattern.length();
        int[] pi = new int[m];
        int k = 0;

        for (int q = 1; q < m; q++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(q))
                k = pi[k - 1];
            if (pattern.charAt(k) == pattern.charAt(q))
                k++;
            pi[q] = k;
        }
        return pi;
    }

    // Hàm tìm kiếm theo thuật toán Apostolico–Crochemore
    static void search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] pi = computePrefix(pattern);

        // Xác định độ dài phần khớp đầu tiên giữa hai bản sao của mẫu
        int ell = 0;
        while (ell < m - 1 && pattern.charAt(ell) == pattern.charAt(ell + 1))
            ell++;

        int j = 0;      // vị trí hiện tại trong văn bản
        int i = 0;      // số ký tự khớp
        int k = 0;      // chu kỳ lặp lại

        while (j <= n - m) {
            while (i < m && pattern.charAt(i) == text.charAt(i + j))
                i++;
            if (i >= m) {
                System.out.println("Mẫu xuất hiện tại vị trí: " + j);
            }

            if (i == 0)
                j++;
            else {
                k = Math.max(1, i - pi[i - 1]);
                j += k;
                i = Math.max(ell, pi[i - 1]);
            }
        }
    }

    public static void main(String[] args) {
        String text = "ABABABABAB";
        String pattern = "ABABAB";
        search(text, pattern);
    }
}

