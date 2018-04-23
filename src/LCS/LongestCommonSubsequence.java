package LCS;

import org.junit.Test;

/**
 *
 * Longest Common Subsequence
 * 最长公共子序列
 *  基于最长公共子串来求得
 *      0                                       (如果i=0且j=0)
 *      dp[i][j] = dp[i-1][j-1] + 1            （如果i和j>0，且A[i]=B[j]）
 *      max(dp[i][j-1], dp[i-1][j])            （如果i和j>0，且A[i]!=B[j]）
 *
 *      构造一个大两边的矩阵，然后像求最长公共子串那样 排列两串 str1 和 str2
 *      从最上角 [1][1] 位置开始遍历，如果该位置的   str1[i] 和 str[2] 相等的话，就将值设置为（左上角的值+1）
 *      如果不等的话，就从上或者左两个值里面选一个最大的，作为这个位置的值
 *      [i][j] 位置实际存储的是 str1[0]~str1[i] 与 str2[0]~str2[j] 两串字符串之间有多少个相似的
 *
 *           g  o  g  l  e
 *        0  0  0  0  0  0
 *
 *      g 0  1  1  1  1  1
 *
 *      g 0  1  1  2  2  2
 *
 *      e 0  1  1  2  2  3
 *
 * Created by Ericwyn on 18-4-21.
 */
public class LongestCommonSubsequence {

    /**
     * 自己的代码
     *
     * 此处从矩阵左上角往右下角计算
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute0(String str1, String str2){
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();

        int[][] flag = new int[ch1.length+1][ch2.length+1];

        for (int i=1;i<flag.length;i++){
            for (int j=1;j<flag[0].length;j++){
                if (ch1[i-1] == ch2[j-1]){
                    flag[i][j] = flag[i-1][j-1] + 1;
                }else {
                    flag[i][j] = Math.max(flag[i-1][j],flag[i][j-1]);
                }
            }
        }
        //输出右下角，矩阵最后一个空的数，就是 LCS 的值
        return flag[ch1.length][ch2.length];
    }

    /**
     * 自己的代码，带最长字符串的输出
     *
     * 此处从矩阵左上角往右下角计算
     *
     * 对于最长公共子序列的输出是遍历已经计算好的 LCS dp 矩阵，从 str1 和 str2 的第一个位置开始
     * 如果 str1[i] = str2[j] 的话，那么这个位置就输出，并且 i 和 j 都移动到右下角
     * 否则
     *      如果右边的值大于下边的值，那么 j++ ，向右边移动，然后继续这步判断
     *      否则
     *          i++ ，向下方移动，然后继续这步判断
     *
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute1(String str1,String str2){
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();

        int[][] flag = new int[ch1.length+1][ch2.length+1];

        for (int i=1;i<flag.length;i++){
            for (int j=1;j<flag[0].length;j++){
                if (ch1[i-1] == ch2[j-1]){
                    flag[i][j] = flag[i-1][j-1] + 1;
                }else {
                    flag[i][j] = Math.max(flag[i-1][j],flag[i][j-1]);
                }
            }
        }

        //输出最长公共子串
        for (int i=1;i<flag.length;){
            for (int j=1;j<flag[0].length;){
                if (ch1[i-1] == ch2[j-1]){
                    System.out.print(ch1[i-1]);
                    i++;
                    j++;
                }else {
                    //先判断 i+1 和 j+1 是否已经超出边界
                    if ((i+1) > (flag.length-1)){
                        j++;
                    }else if ((j+1) > (flag[0].length-1)) {
                        i++;
                    }else {
                        if (flag[i][j+1] > flag[i+1][j]){
                            j++;
                        }else {
                            i++;
                        }
                    }
                }
            }
        }
        System.out.println();
        //输出右下角，矩阵最后一个空的数，就是 LCS 的值
        return flag[ch1.length][ch2.length];
    }


    /**
     * 计算两个词之间的 LCS 长度
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute(char[] str1, char[] str2)
    {
        int substringLength1 = str1.length;
        int substringLength2 = str2.length;

        // 构造二维数组记录子问题A[i]和B[j]的LCS的长度
        int[][] opt = new int[substringLength1 + 1][substringLength2 + 1];

        // 从后向前，动态规划计算所有子问题。也可从前到后。
        for (int i = substringLength1 - 1; i >= 0; i--)
        {
            for (int j = substringLength2 - 1; j >= 0; j--)
            {
                if (str1[i] == str2[j])
                    opt[i][j] = opt[i + 1][j + 1] + 1;// 状态转移方程
                else
                    opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);// 状态转移方程
            }
        }
        System.out.println("substring1:" + new String(str1));
        System.out.println("substring2:" + new String(str2));
        System.out.print("LCS:");

        int i = 0, j = 0;
        while (i < substringLength1 && j < substringLength2)
        {
            if (str1[i] == str2[j])
            {
                System.out.print(str1[i]);
                i++;
                j++;
            }
            else if (opt[i + 1][j] >= opt[i][j + 1])
                i++;
            else
                j++;
        }
        System.out.println();
        return opt[0][0];
    }

    @Test
    public void test(){
        System.out.println(compute1("goge","ggle"));
    }

}
