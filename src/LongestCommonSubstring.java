import org.junit.Test;

import java.util.HashMap;

/**
 * 最长公共子串（Longest Common Substring）
 * 参考
 * http://www.hankcs.com/program/algorithm/implementation-and-application-of-nlp-longest-common-subsequence-longest-common-subsequence-of-java.html
 *
 * Created by Ericwyn on 18-4-21.
 */
public class LongestCommonSubstring {
    /**
     * 这个方法当str1 和str2 长度都为 100000 的时候就会 OutOfMemoryError: Java heap space
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute0(String str1,String str2){
        char[] charsa = str1.toCharArray();
        char[] charsb = str2.toCharArray();
        if (charsa.length ==0 || charsb.length == 0){
            return 0;
        }
        int maxLCSSize = 0;
        int flag[][] = new int[charsa.length][charsb.length];
        for (int i=0;i<charsa.length;i++){
            for (int j = 0;j<charsb.length;j++){
                if (charsa[i] == charsb[j]){
                    if (i!=0 && j!=0){
                        flag[i][j] = flag[i-1][j-1]+1;
                        if (flag[i][j]>maxLCSSize){
                            maxLCSSize = flag[i][j];
                        }
                    }else {
                        flag[i][j] = 1;
                    }
                }
            }
        }
        return maxLCSSize;
    }

    /**
     * 方法0的基础上改进
     *
     * OutOfMemory 的话主要是 二维数组占据太多空间，所以要想办法把二维数组去掉
     * 去除了二维数组，转而使用 hashmap 的方法来存储，有点类似于稀疏矩阵
     *
     * 凉凉....hashMap 的读取开销太大了，虽然一定是能算出结果的
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute1(String str1,String str2){
        char[] charsa = str1.toCharArray();
        char[] charsb = str2.toCharArray();
        if (charsa.length ==0 || charsb.length == 0){
            return 0;
        }
        HashMap<String,Integer> flagMap = new HashMap<>();
        int maxLCSSize = 0;
        Integer temp ;
        for (int i=0;i<charsa.length;i++){
            for (int j = 0;j<charsb.length;j++){
                if (charsa[i] == charsb[j]){
                    if (i!=0 && j!=0){
                        if ((temp = flagMap.get(""+(i-1)+(j-1)))!=null){
                            flagMap.put(""+i+j,temp+1);
                        }else {
                            flagMap.put(""+i+j,1);
                        }
                    }else {
                        flagMap.put(""+i+j,1);
                    }
                }
            }
        }
        return maxLCSSize;
    }

    /**
     * 方法2的基础上改进
     *
     * 1 凉凉的原因是hashMap 的读取开销太大，由于hashMap的话我是想记住具体的位置是什么
     * 所以如果不需要的得到具体的字符串的话，直接一条条对角线的计算就好了
     * 这也就和原来参考的方法差不多了
     *
     * 对比 100000 的时间是 18006ms
     *
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compute2(String str1,String str2){
        char[] charsa = str1.toCharArray();
        char[] charsb = str2.toCharArray();
        if (charsa.length ==0 || charsb.length == 0){
            return 0;
        }
        //图的话，str1 纵向摆放，作为i轴
        //str2 的话，横向拜访，作为j轴
        //需要先遍历i=0时候所有的对角线的起点
        //然后再便利j=0时候所有对角线的起点
        int maxLCSSize = 0;
        for (int i=0;i<charsa.length;i++){
            int maxTemp = 0; //记录左上角最大的值
            int j= 0;
            int iTemp = i;
            while (i<charsa.length && j<charsb.length){
                if (charsa[i] == charsb[j]){
                    if (maxTemp == 0){
                        maxTemp = 1;
                    } else {
                        maxTemp = maxTemp+1;
                    }
                }else {
                    //如果此处并不相等的话，那么右上角如果有值，就有可能是这个对角线的最大值，所以要记录下来
                    if (maxTemp > 0){
                        if (maxTemp > maxLCSSize){
                            maxLCSSize = maxTemp;
                        }
                        maxTemp = 0;
                    }
                }
                i++;
                j++;
            }
            i = iTemp;
            if (maxTemp>maxLCSSize){
                maxLCSSize = maxTemp;
            }
        }

        for (int j=1;j<charsa.length;j++){
            int maxTemp = 0; //记录左上角最大的值
            int i= 0;
            int jTemp = j;
            while (i<charsa.length && j<charsb.length){
                if (charsa[i] == charsb[j]){
                    if (maxTemp == 0){
                        maxTemp = 1;
                    } else {
                        maxTemp = maxTemp+1;
                    }
                }else {
                    //如果此处并不相等的话，那么右上角如果有值，就有可能是这个对角线的最大值，所以要记录下来
                    if (maxTemp > 0){
                        if (maxTemp > maxLCSSize){
                            maxLCSSize = maxTemp;
                        }
                        maxTemp = 0;
                    }
                }
                i++;
                j++;
            }
            j = jTemp;
            if (maxTemp>maxLCSSize){
                maxLCSSize = maxTemp;
            }
        }


        return maxLCSSize;
    }

    /**
     * 示例代码
     * 可承受 str1 和 str2 长度都为 100000
     *  对比 100000 的时间是 17823ms
     * @param str1
     * @param str2
     * @return
     */
    public static int compute(char[] str1, char[] str2) {
        int size1 = str1.length;
        int size2 = str2.length;
        if (size1 == 0 || size2 == 0) return 0;

        // the start position of substring in original string
        int start1 = -1;
        int start2 = -1;
        // the longest length of common substring
        int longest = 0;

        // record how many comparisons the solution did;
        // it can be used to know which algorithm is better
        int comparisons = 0;

        for (int i = 0; i < size1; ++i)
        {
            int m = i;
            int n = 0;
            int length = 0;
            while (m < size1 && n < size2)
            {
                ++comparisons;
                if (str1[m] != str2[n])
                {
                    length = 0;
                }
                else
                {
                    ++length;
                    if (longest < length)
                    {
                        longest = length;
                        start1 = m - longest + 1;
                        start2 = n - longest + 1;
                    }
                }

                ++m;
                ++n;
            }
        }

        // shift string2 to find the longest common substring
        for (int j = 1; j < size2; ++j)
        {
            int m = 0;
            int n = j;
            int length = 0;
            while (m < size1 && n < size2)
            {
                ++comparisons;
                if (str1[m] != str2[n])
                {
                    length = 0;
                }
                else
                {
                    ++length;
                    if (longest < length)
                    {
                        longest = length;
                        start1 = m - longest + 1;
                        start2 = n - longest + 1;
                    }
                }

                ++m;
                ++n;
            }
        }
        System.out.printf("from %d of str1 and %d of str2, compared for %d times\n", start1, start2, comparisons);
        return longest;
    }


    @Test
    public void test(){
        String a = "";
        String b = "";
        for (int i =0;i<100000;i++){
            a+=""+(char) (65+(int)(Math.random()*26));
        }
        for (int i =0;i<100000;i++){
            b+=""+(char) (65+(int)(Math.random()*26));
        }
        System.out.println("使用 c2方法");
        Long time = System.currentTimeMillis();
        System.out.println("max LCS size : "+compute2(a,b));
        System.out.println(System.currentTimeMillis()-time);

        System.out.println("使用 c方法");
        Long time2 = System.currentTimeMillis();
        System.out.println("max LCS  size : "+compute(a.toCharArray(),b.toCharArray()));
        System.out.println(System.currentTimeMillis()-time2);
    }
}
