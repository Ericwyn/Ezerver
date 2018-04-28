package job;

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


 PS:手写10～99之间的质数，然后暴力 if

 * Created by Ericwyn on 18-4-28.
 */
public class MagicNumber {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        int e = in.nextInt();
        int count = 0;
        for (int i=s;i<=e;i++){
            if (isMagicNumber(i)){
                count++;
            }
        }
        System.out.println(count);
    }

    static boolean isMagicNumber(int number){
        String temp = ""+number;
        if (temp.contains("1")){
            int num1Count = 0;
            for (int i=0;i<temp.length();i++){
                if (temp.charAt(i) =='1'){
                    num1Count++;
                }
            }
            if (num1Count>=2){
                return true;
            }
            if (temp.contains("3")          //13 31
                    || temp.contains("6")   //61
                    || temp.contains("4")   //41
                    || temp.contains("7")   //17 71
                    || temp.contains("9")){ //19 91
                return true;
            }
        }
        if (temp.contains("2")){
            if (temp.contains("3")          //23
                    || temp.contains("9")){ //29
                return true;
            }
        }
        if (temp.contains("3")){
            if (temp.contains("7")          //37 73
                    || temp.contains("5")   //53
                    || temp.contains("8")   //83
                    || temp.contains("4")){ //43
                return true;
            }
        }
        if (temp.contains("4")){
            if (temp.contains("7")){        //47
                return true;
            }
        }
        if (temp.contains("5")){
            if (temp.contains("9")){        //59
                return true;
            }
        }
        if (temp.contains("6")){
            if (temp.contains("7")){        //67
                return true;
            }
        }
        if (temp.contains("7")){
            if (temp.contains("9")){        //79 97
                return true;
            }
        }
        if (temp.contains("8")){
            if (temp.contains("9")){        //89
                return true;
            }
        }
        return false;
    }
}
