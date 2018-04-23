package job;

import java.util.Scanner;

/**
 * 构造回文

 给定一个字符串s，你可以从中删除一些字符，使得剩下的串是一个回文串。如何删除才能使得回文串最长呢？
 输出需要删除的字符个数。

 输入描述:

 输入数据有多组，每组包含一个字符串s，且保证:1<=s.length<=1000.




 输出描述:

 对于每组数据，输出一个整数，代表最少需要删除的字符个数。


 输入例子1:
 abcda
 google

 输出例子1:
 2
 2

 * Created by Ericwyn on 18-4-23.
 */
public class ConstructionPalindrome {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String in = scanner.next();
            String in2  = "";
            for (int i=in.length()-1;i>=0;i--){
                in2 += in.charAt(i);
            }
            System.out.println(in.length()-LCS(in,in2));
        }
    }

    public static int LCS(String str1, String str2){
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
        return flag[str1.length()][str2.length()];
    }

}
