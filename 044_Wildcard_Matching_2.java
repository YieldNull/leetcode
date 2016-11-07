public class Solution {

    public boolean isMatch(String str, String ptn) {
        int p_str = 0; // index of current input str
        int p_ptn = 0; // index of current pattern

        int p_saved_str = -1; // index of latest unmatched str
        int p_saved_ptn = -1; // index of latest unmatched star

        while (p_str < str.length()) {
            char s = str.charAt(p_str);
            char p = p_ptn < ptn.length() ? ptn.charAt(p_ptn) : 0;

            if (s == p || p == '?') {
                p_str++;
                p_ptn++;
            } else if (p == '*') {
                p_saved_ptn = p_ptn++;
                p_saved_str = p_str;
            } else if (p_saved_ptn >= 0) {
                p_ptn = p_saved_ptn + 1;
                p_str = ++p_saved_str;  // '*' matches another char
            } else {
                return false; // characters do not match; p_ptn >= ptn.length() and there is no saved_ptn
            }
        }

        while (p_ptn < ptn.length()) {
            if (ptn.charAt(p_ptn++) != '*') return false;
        }

        return true;
    }
}
