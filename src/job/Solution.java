package job;

import org.junit.Test;

/**
 *
 * 在一个二维数组中，每一行都按照从左到右递增的顺序排序
 * 每一列都按照从上到下递增的顺序排序。请完成一个函数
 * 输入这样的一个二维数组和一个整数，判断数组中是否含有该整数
 *
 *
 * Created by Ericwyn on 18-4-24.
 */
public class Solution {
    public boolean Find(int target, int [][] array) {
        if (target < array[0][0] || target > array[array.length-1][array[0].length-1]){
            return false;
        }
        int inArr = intInArr(array, 0, array.length, target);
        intInArr2(array[inArr],0,array[inArr].length,target);

        return false;
    }


    private static int intInArr(int[][] arr,int a,int b,int n){
        if (arr[(b-a)/2+a][0] > n){
            return intInArr(arr,a,(b-a)/2+a,n);
        }else if (arr[(b-a)/2+a][0]<n){
            return intInArr(arr,(b-a)/2+a,b,n);
        }else if (arr[(b-a)/2+a][0] == n){
            return (b-a)/2+a;
        }
        return n;
    }

    private static int intInArr2(int[] arr,int a,int b,int n){
        if (arr[(b-a)/2+a] > n){
            return intInArr2(arr,a,(b-a)/2+a,n);
        }else if (arr[(b-a)/2+a]<n){
            return intInArr2(arr,(b-a)/2+a,b,n);
        }else if (arr[(b-a)/2+a] == n){
            return (b-a)/2+a;
        }
        return -1;
    }

    @Test
    public void test(){
        int a[][] = new int[2][6];
        a[0] = new int[]{1,2,5,6,10,15};
        a[1] = new int[]{0,0,0,0,0, 0};

        int[] temp2 = new int[]{1,2,5,6,10,15};
        int[] temp = a[0];
        System.out.println(intInArr2(a[0],0,a[0].length,7));
    }

}
