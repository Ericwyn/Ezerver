package job;

import java.util.Scanner;

/**
 *
 * 字符移位
 *
 *
 * 小Q最近遇到了一个难题：把一个字符串的大写字母放到字符串的后面，各个字符的相对位置不变，且不能申请额外的空间。
 你能帮帮小Q吗？


 输入描述:

 输入数据有多组，每组包含一个字符串s，且保证:1<=s.length<=1000.




 输出描述:

 对于每组数据，输出移位后的字符串。


 输入例子1:
 AkleBiCeilD

 输出例子1:
 kleieilABCD

 *
 * Created by Ericwyn on 18-4-23.
 */
public class CharacterShift {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {//注意while处理多个case              int a = in.nextInt();
            String in = scanner.next();
            String big = "";
            String small = "";
            for (int i=0;i<in.length();i++){
                if (in.charAt(i) >= 'A' && in.charAt(i) <= 'Z'){
                    big+=in.charAt(i);
                }else {
                    small+=in.charAt(i);
                }
            }
            System.out.println(small+big);
        }
    }
}
