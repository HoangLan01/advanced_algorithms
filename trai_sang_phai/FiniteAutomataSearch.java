package trai_sang_phai;

public class FiniteAutomataSearch {

    // Định nghĩa số lượng ký tự trong bảng chữ cái (giả sử là ASCII mở rộng)
    static int NO_OF_CHARS = 256;

    // Hàm xây dựng bảng chuyển trạng thái (Transition Function - TF) cho máy tự động hữu hạn (Finite Automaton)
    // Bảng TF[state][char] sẽ cho biết trạng thái tiếp theo khi đang ở 'state' và đọc được ký tự 'char'.
    static void computeTransitionFunction(String pattern, int[][] TF) {
        int m = pattern.length(); // Lấy độ dài của mẫu
        int lps = 0; // lps: Longest Proper Prefix which is also Suffix (Tiền tố riêng dài nhất cũng là hậu tố)
                     // Biến này giúp xác định trạng thái "dự phòng" khi có ký tự không khớp.

        // Khởi tạo: Từ trạng thái 0, nếu đọc được ký tự đầu tiên của mẫu, sẽ chuyển sang trạng thái 1.
        TF[0][pattern.charAt(0)] = 1;

        // Duyệt qua tất cả các trạng thái từ 1 đến m (trạng thái cuối cùng) để xây dựng bảng TF.
        // 'state' đại diện cho số ký tự của mẫu đã được khớp.
        for (int state = 1; state <= m; state++) {
            // Sao chép các giá trị chuyển trạng thái từ trạng thái 'lps' sang trạng thái hiện tại.
            // Đây là bước xử lý cho trường hợp ký tự không khớp. Nếu ký tự đọc được không phải là
            // ký tự tiếp theo trong mẫu, máy tự động sẽ quay về trạng thái được xác định bởi 'lps'.
            for (int x = 0; x < NO_OF_CHARS; x++)
                TF[state][x] = TF[lps][x];

            // Nếu chưa phải trạng thái cuối cùng (state < m)
            if (state < m) {
                // Ghi đè lên giá trị đã sao chép ở trên. Nếu ký tự đọc được là ký tự tiếp theo
                // trong mẫu (pattern.charAt(state)), thì chuyển sang trạng thái tiếp theo (state + 1).
                TF[state][pattern.charAt(state)] = state + 1;
                
                // Cập nhật giá trị 'lps' cho vòng lặp (trạng thái) tiếp theo.
                // 'lps' mới sẽ là trạng thái mà 'lps' cũ chuyển đến khi đọc được ký tự pattern.charAt(state).
                lps = TF[lps][pattern.charAt(state)];
            }
        }
    }

    // Hàm tìm kiếm mẫu trong văn bản sử dụng máy tự động hữu hạn đã xây dựng
    static void search(String pattern, String text) {
        int m = pattern.length(); // Lấy độ dài của mẫu
        int n = text.length();    // Lấy độ dài của văn bản
        
        // Khởi tạo bảng chuyển trạng thái TF với (m+1) trạng thái và NO_OF_CHARS ký tự
        int[][] TF = new int[m + 1][NO_OF_CHARS];

        // Gọi hàm để xây dựng bảng chuyển trạng thái TF từ mẫu
        computeTransitionFunction(pattern, TF);

        // Bắt đầu tìm kiếm từ trạng thái 0
        int state = 0;
        // Duyệt qua từng ký tự của văn bản
        for (int i = 0; i < n; i++) {
            // Chuyển sang trạng thái tiếp theo dựa trên trạng thái hiện tại và ký tự đang xét
            state = TF[state][text.charAt(i)];
            
            // Nếu trạng thái hiện tại là m, có nghĩa là đã khớp toàn bộ mẫu
            if (state == m)
                // In ra vị trí bắt đầu của mẫu trong văn bản
                System.out.println("Mau xuat hien tai vi tri: " + (i - m + 1));
        }
    }

    // Hàm main để chạy chương trình
    public static void main(String[] args) {
        // Dữ liệu ví dụ
        String text = "ABABABAB";
        String pattern = "ABAB";
        // Gọi hàm tìm kiếm
        search(pattern, text);
    }
}

