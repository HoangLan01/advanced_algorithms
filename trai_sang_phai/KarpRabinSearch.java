package trai_sang_phai;

public class KarpRabinSearch {

    // Hàm tìm kiếm chuỗi bằng thuật toán Karp-Rabin
    public static void search(String text, String pattern, int q) {
        int d = 256; // Số lượng ký tự trong bảng ASCII
        int n = text.length();
        int m = pattern.length();
        int p = 0; // Giá trị băm của mẫu
        int t = 0; // Giá trị băm của cửa sổ hiện tại
        int h = 1;

        // Tính h = (d^(m-1)) % q
        for (int i = 0; i < m - 1; i++)
            h = (h * d) % q;

        // Tính giá trị băm ban đầu cho mẫu và cửa sổ đầu tiên của văn bản
        for (int i = 0; i < m; i++) {
            p = (d * p + pattern.charAt(i)) % q;
            t = (d * t + text.charAt(i)) % q;
        }

        // Duyệt từng cửa sổ trong văn bản
        for (int i = 0; i <= n - m; i++) {

            // Nếu giá trị băm khớp, kiểm tra ký tự thực tế
            if (p == t) {
                int j;
                for (j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j))
                        break;
                }
                if (j == m) // Nếu toàn bộ ký tự khớp
                    System.out.println("Mẫu xuất hiện tại vị trí: " + i);
            }

            // Tính giá trị băm cho cửa sổ tiếp theo
            if (i < n - m) {
                t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
                if (t < 0)
                    t += q; // Tránh giá trị băm âm
            }
        }
    }

    // Hàm kiểm thử
    public static void main(String[] args) {
        String text = "2359023141526739921";
        String pattern = "31415";
        int q = 101; // Số nguyên tố dùng trong phép băm
        search(text, pattern, q);
    }
}

