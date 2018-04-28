package job;

import java.util.Scanner;

/**
 *
 *

 牛牛想对一个数做若干次变换，直到这个数只剩下一位数字。
 变换的规则是：将这个数变成 所有位数上的数字的乘积。比如285经过一次变换后转化成2*8*5=80.
 问题是，要做多少次变换，使得这个数变成个位数。
 输入描述:
 输入一个整数。小于等于2,000,000,000。


 输出描述:
 输出一个整数，表示变换次数。

 输入例子1:
 285

 输出例子1:
 2


 PS：如果输入的数字小于10 直接就返回0了，如果不是的话，就递归判断
 减少次数的方法是，如果这个数字里面有 0，那么在下一次运算时候也一定会变成 0

 * Created by Ericwyn on 18-4-28.
 */
public class ChangeTime {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n<10){
            System.out.println(0);
        }else {
            System.out.println(a(n,1));
        }
    }

    public static long a(long a,int time){
        if ((""+a).contains("0")){
            return time;
        }
        String temp=""+a;
        long temp2 = 1;
        for (int i=0;i<temp.length();i++){
            temp2*=Integer.parseInt(""+temp.charAt(i));
        }
        if (temp2<10){
            return time;
        }else {
            return a(temp2,time+1);
        }
    }

}
