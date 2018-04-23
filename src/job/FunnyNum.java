package job;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
     小Q今天在上厕所时想到了这个问题：有n个数，两两组成二元组，差最小的有多少对呢？差最大呢？


     输入描述:

     输入包含多组测试数据。

     对于每组测试数据：

     N - 本组测试数据有n个数

     a1,a2...an - 需要计算的数据

     保证:

     1<=N<=100000,0<=ai<=INT_MAX.




     输出描述:

     对于每组数据，输出两个数，第一个数表示差最小的对数，第二个数表示差最大的对数。


     输入例子1:
     6
     45 12 45 32 5 6

     输出例子1:
     1 2
 *
 *
 *      这一题一开始用的是 二维数组来存储二元组计算的结果
 *      结果 outofmemory
 *      所以才发现能用一维直接解决，没必要用二维
 *
 *      一维数组也会，总而言之如果暴力的一个个比对的话，一定会outofmemory
 *
 *      正确做法
        1. 排序（采用快排，直接插入排序也试过了，会超时）
        2. 差最大的对数就是最小值的个数乘以最大数的个数
        3. 差最小分两种情况。当数组中有相同元素时，最小值为0，则找出每个相同元素的个数，
        用等差数列求和的方式求出这个元素能组成的对数，然后将所有具有相同元素的对数和相加；
        当数组中没有相同元素，则想冒泡的方式一样一次找出最小的差值并累加对数。
 *
 *
 * Created by Ericwyn on 18-4-23.
 */
public class FunnyNum {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()){
            int n = in.nextInt();
            int[] nums = new int[n];
            for (int i=0;i<n;i++){
                nums[i] = in.nextInt();
            }
            Arrays.sort(nums);
            int minNums = nums[0];
            int minNumCount = 1;
            for (int i=1;i<nums.length;i++){
                if (minNums == nums[i]){
                    minNumCount++;
                }else {
                    break;
                }
            }
            int maxNums = nums[nums.length-1];
            int maxNumCount = 1;
            for (int i = nums.length-2;i>=0;i--){
                if (maxNums == nums[i]){
                    maxNumCount++;
                }else {
                    break;
                }
            }
            int bigCount = maxNumCount * minNumCount;

            boolean hasEqualsNumFlag = false;
            for (int i=1;i<nums.length;i++){
                if (nums[i]-nums[i-1] ==0 ){
                    hasEqualsNumFlag = true;
                    break;
                }
            }
            //用以存储相等的数字
            HashMap<Integer,Integer> equalsMap = new HashMap<>();
            //是否有相等的数字，如果有那么最小差的话一定为 0
            if (hasEqualsNumFlag){
                //找出有多少组相同数字
                for (int i=1;i<nums.length;i++){
                    if (nums[i] == nums[i-1]){
                        if (equalsMap.get(nums[i]) == null){
                            equalsMap.put(nums[i],2);
                        }else {
                            equalsMap.put(nums[i],equalsMap.get(nums[i])+1);
                        }
                    }
                }
                int minCount = 0;
                for (int i : equalsMap.keySet()){
                    minCount += equalsMap.get(i) * (equalsMap.get(i)-1) / 2;
                }
                System.out.println(minCount+" "+bigCount);
            }else {
                int[] absTemp = new int[nums.length-1];
                for (int i=1;i<nums.length;i++){
                    absTemp[i-1] = nums[i]-nums[i-1];
                }
                Arrays.sort(absTemp);
                int minCount = 1;
                for (int i=1;i<absTemp.length;i++){
                    if (absTemp[i]==absTemp[0]){
                        minCount++;
                    }else {
                        break;
                    }
                }
                System.out.println(minCount+" "+bigCount);
            }
        }
    }
}
