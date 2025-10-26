package trai_sang_phai;

public class FiniteAutomataSearch {

    static int NO_OF_CHARS = 256;
    
    static void computeTransitionFunction(String pattern, int[][] TF) {
        int m = pattern.length(); 
        int lps = 0; 
        TF[0][pattern.charAt(0)] = 1;

        for (int state = 1; state <= m; state++) {
            for (int x = 0; x < NO_OF_CHARS; x++)
                TF[state][x] = TF[lps][x];

            if (state < m) {
                TF[state][pattern.charAt(state)] = state + 1;
                lps = TF[lps][pattern.charAt(state)];
            }
        }
    }

    
    static void search(String pattern, String text) {
        int m = pattern.length(); 
        int n = text.length();   

        int[][] TF = new int[m + 1][NO_OF_CHARS];

        computeTransitionFunction(pattern, TF);

        int state = 0;
        for (int i = 0; i < n; i++) {
            state = TF[state][text.charAt(i)];
            if (state == m)
                System.out.println("Mau xuat hien tai vi tri: " + (i - m + 1));
        }
    }

    // main
    public static void main(String[] args) {
        String text = "ABABABAB";
        String pattern = "ABAB";

        search(pattern, text);
    }
}

