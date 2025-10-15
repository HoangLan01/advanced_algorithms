package trai_sang_phai;

public class ShiftOrSearch {

    static final int MAX_CHAR = 256;

    // Hàm tìm kiếm mẫu trong văn bản bằng thuật toán Shift-Or
    public static void search(String text, String pattern) {
        int m = pattern.length();
        long[] mask = new long[MAX_CHAR];
        long state = ~0L;  // tất cả bit = 1
        long matchBit = 1L << (m - 1);

        // Khởi tạo mặt nạ bit cho từng ký tự
        for (int i = 0; i < MAX_CHAR; i++)
            mask[i] = ~0L;

        for (int i = 0; i < m; i++)
            mask[pattern.charAt(i)] &= ~(1L << i);

        // Duyệt văn bản
        for (int j = 0; j < text.length(); j++) {
            state = (state << 1) | mask[text.charAt(j)];
            if ((state & matchBit) == 0)
                System.out.println("Mẫu xuất hiện tại vị trí: " + (j - m + 1));
        }
    }

    public static void main(String[] args) {
        String text = "ABABABAB";
        String pattern = "ABAB";
        search(text, pattern);
    }
}

