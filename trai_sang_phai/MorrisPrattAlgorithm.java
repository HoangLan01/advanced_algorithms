package trai_sang_phai;

public class MorrisPrattAlgorithm {

    // Hàm tính bảng border (failure function)
    public static int[] computeBorder(String pattern) {
        int m = pattern.length();
        int[] border = new int[m + 1];
        border[0] = -1;
        int i = 0, j = -1;

        while (i < m) {
            while (j >= 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = border[j];
            }
            i++;
            j++;
            border[i] = j;
        }
        return border;
    }

    // Hàm tìm kiếm chuỗi pattern trong text
    public static void MPsearch(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int[] border = computeBorder(pattern);

        int i = 0;  // chỉ số text
        int j = 0;  // chỉ số pattern

        while (i < n) {
            while (j >= 0 && text.charAt(i) != pattern.charAt(j)) {
                j = border[j];  // nhảy theo bảng border khi sai khác
            }
            i++;
            j++;

            if (j == m) {
                System.out.println("Pattern found at index " + (i - j));
                j = border[j];  // chuẩn bị tìm vị trí tiếp theo
            }
        }
    }

    public static void main(String[] args) {
        String text = "ABABACABABA";
        String pattern = "ABABA";
        MPsearch(text, pattern);
    }
}

