public class Solution {
    
    /**
     * let f[i][j] means whether ptn[:i] matches str[:j], so:
     * 
     * f[i][j] = ((ptn[i-1]==str[j-1] || ptn[i-1]=='?') && f[i-1][j-1]) ||
     *           (ptn[i-1]=='*' && (f[i-1][j] || f[i-1][j-1] || f[i-1][j-2] || ... || f[i-1][0]))
     *          
     *         = ((ptn[i-1]==str[j-1] || ptn[i-1]=='?') && f[i-1][j-1]) ||
     *           (ptn[i-1]=='*' && (f[i-1][j] || f[i][j-1]))
     * 
     */
    public boolean isMatch(String str, String ptn) {
        boolean[][] dp = new boolean[ptn.length() + 1][str.length() + 1];

        dp[0][0] = true;

        for (int i = 1; i <= ptn.length(); i++) {
            dp[i][0] = dp[i - 1][0] && ptn.charAt(i - 1) == '*';
        }

//        for (int j = 1; j <= str.length(); j++) {
//            dp[0][j] = false;
//        }

        for (int i = 1; i <= ptn.length(); i++) {
            for (int j = 1; j <= str.length(); j++) {
                dp[i][j] = ((ptn.charAt(i - 1) == str.charAt(j - 1) || ptn.charAt(i - 1) == '?') && dp[i - 1][j - 1]) ||
                        (ptn.charAt(i - 1) == '*' && (dp[i - 1][j] || dp[i][j - 1]));
            }
        }

        return dp[ptn.length()][str.length()];
    }
}
