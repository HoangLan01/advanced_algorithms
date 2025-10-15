package trai_sang_phai;

public class ApostolicoCrochemoreSearch {

    // Hàm tính mảng prefix function (giống KMP)
    // Mảng này lưu độ dài của tiền tố dài nhất cũng là hậu tố của một chuỗi con của mẫu.
    static int[] computePrefix(String pattern) {
        int m = pattern.length(); // Lấy độ dài của mẫu
        int[] pi = new int[m];    // Khởi tạo mảng pi (prefix function) với kích thước bằng độ dài mẫu
        int k = 0;                // k là độ dài của tiền tố chung dài nhất hiện tại, cũng là chỉ số trong mẫu

        // Duyệt qua mẫu từ ký tự thứ hai (chỉ số 1) để xây dựng mảng pi
        for (int q = 1; q < m; q++) {
            // Nếu ký tự hiện tại không khớp, ta lùi k về giá trị của tiền tố trước đó
            // để tìm một tiền tố ngắn hơn mà vẫn có thể là hậu tố.
            while (k > 0 && pattern.charAt(k) != pattern.charAt(q))
                k = pi[k - 1];
            
            // Nếu ký tự khớp, ta tăng độ dài của tiền tố chung lên 1
            if (pattern.charAt(k) == pattern.charAt(q))
                k++;
            
            // Lưu độ dài tiền tố chung dài nhất kết thúc tại vị trí q vào mảng pi
            pi[q] = k;
        }
        return pi; // Trả về mảng prefix function đã tính toán
    }

    // Hàm tìm kiếm theo thuật toán Apostolico–Crochemore
    static void search(String text, String pattern) {
        int n = text.length();      // Lấy độ dài của văn bản
        int m = pattern.length();   // Lấy độ dài của mẫu
        int[] pi = computePrefix(pattern); // Tính mảng prefix function cho mẫu

        // Xác định độ dài phần khớp đầu tiên giữa hai bản sao của mẫu
        // ell là độ dài của chuỗi ký tự lặp lại ở đầu mẫu (ví dụ: "aaaa" trong "aaaab")
        int ell = 0;
        while (ell < m - 1 && pattern.charAt(ell) == pattern.charAt(ell + 1))
            ell++;

        int j = 0;      // vị trí bắt đầu so khớp hiện tại trong văn bản
        int i = 0;      // số ký tự đã khớp (vị trí hiện tại trong mẫu)
        int k = 0;      // biến tạm để lưu chu kỳ lặp lại khi có không khớp

        // Vòng lặp chính, trượt cửa sổ so khớp trên văn bản
        while (j <= n - m) {
            // So sánh mẫu với văn bản tại vị trí j, bắt đầu từ ký tự thứ i của mẫu
            while (i < m && pattern.charAt(i) == text.charAt(i + j))
                i++;
            
            // Nếu i đạt đến độ dài của mẫu, tức là đã tìm thấy một sự xuất hiện
            if (i >= m) {
                System.out.println("Mau xuat hien tai vi tri: " + j);
            }

            // Nếu không có ký tự nào khớp (i=0), chỉ cần dịch cửa sổ sang phải 1 vị trí
            if (i == 0)
                j++;
            else {
                // Nếu có ít nhất một ký tự khớp, tính toán bước nhảy k
                // Bước nhảy k là độ dài của phần đã khớp trừ đi độ dài của tiền tố-hậu tố chung
                // Điều này đảm bảo không bỏ lỡ bất kỳ sự xuất hiện nào.
                k = Math.max(1, i - pi[i - 1]);
                
                // Dịch chuyển cửa sổ so khớp trong văn bản đi một khoảng k
                j += k;
                
                // Cập nhật lại số ký tự đã khớp (i) cho lần so sánh tiếp theo.
                // Đây là điểm cải tiến của Apostolico-Crochemore: không reset i về 0,
                // mà giữ lại một phần thông tin đã khớp.
                i = Math.max(ell, pi[i - 1]);
            }
        }
    }

    public static void main(String[] args) {
        // Dữ liệu ví dụ để kiểm tra thuật toán
        String text = "ABABABABAB";
        String pattern = "ABABAB";
        // Gọi hàm tìm kiếm để tìm tất cả các lần xuất hiện của pattern trong text
        search(text, pattern);
    }
}

