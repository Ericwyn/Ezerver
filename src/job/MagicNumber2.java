package job;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *

 给出一个区间[a, b]，计算区间内“神奇数”的个数。
 神奇数的定义：存在不同位置的两个数位，组成一个两位数（且不含前导0），且这个两位数为质数。
 比如：153，可以使用数字3和数字1组成13，13是质数，满足神奇数。同样153可以找到31和53也为质数，只要找到一个质数即满足神奇数。
 输入描述:
 输入为两个整数a和b，代表[a, b]区间 (1 ≤ a ≤ b ≤ 10000)。


 输出描述:
 输出为一个整数，表示区间内满足条件的整数个数

 输入例子1:
 11 20

 输出例子1:
 6


 PS:判断一个字符串里面有没有另一个字符串的存在，有点类似于是否存在子序列，但是这个有点暴力而已

 * Created by Ericwyn on 18-4-28.
 */
public class MagicNumber2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        int e = in.nextInt();
        int count = 0;
        ArrayList <String> set=new ArrayList<>();
        for (int i=11;i<100;i++){
            boolean flag = true;
            for (int j=2;j<=Math.sqrt(i);j++){
                if (i%j==0){
                    flag = false;
                    break;
                }
            }
            if (flag) {
                    set.add(""+i);
            }
        }
        for (int i=s;i<=e;i++){
            for (String str2:set){
                if (withSonStr(""+i,str2)){
                    count++;
                    break;
                }
            }
        }
        System.out.println(count);
    }
    static boolean withSonStr(String str1,String str2){
        if (str2.equals("11")){
            int count = 0;
            for (int i=0;i<str1.length();i++){
                if (str1.charAt(i)=='1'){
                    count++;
                    if (count>=2){
                        return true;
                    }
                }
            }
            return false;
        }else {
            for (int i=0;i<str2.length();i++){
                if (!str1.contains(""+str2.charAt(i))){
                    return false;
                }
            }
            return true;
        }
    }
}
/*
测试用例:
11 11111

对应输出应该为:

8777


 */