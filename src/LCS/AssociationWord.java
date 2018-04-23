package LCS;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * 联想词获取算法，搜索引擎联想词下拉选择的实现，利用最长子串匹配
 * 依赖于字典
 *      汉字拼音字典
 *
 * Created by Ericwyn on 18-4-23.
 */
public class AssociationWord {

    /**
     * 载入联想的问题
     * @return
     * @throws IOException
     */
    public static ArrayList<String> loadQuestion(HashMap<String, String> pinyinMap) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader("documents/Top1000Q_Zhihu"));
        String line = null;
        ArrayList<String> res = new ArrayList<>();
        while ((line = bufferedReader.readLine())!=null){
            res.add(transQuestion(line.trim().split("-")[0],pinyinMap));
        }
        bufferedReader.close();
        System.out.println("load "+res.size()+" questions ");
        return res;
    }

    /**
     * 载入拼音字典
     * @return
     * @throws IOException
     */
    public static HashMap<String, String> loadPinyin() throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new FileReader("documents/pinyin_dic"));
        String line = null;
        HashMap<String, String> res = new HashMap<String, String>();
        while ((line = bufferedReader.readLine())!=null){
            String[] split = line.split("&");
            if (split.length == 3){
                res.put(split[0],transPinyin(split[2]));
            }else {
                res.put(split[0],"");
            }
        }
        bufferedReader.close();
        System.out.println("load "+res.size()+" pinyin ");
        return res;
    }

    /**
     * 拼音转换
     * háo 变为 hao
     *
     * @param old
     * @return
     */
    public static String transPinyin(String old){
        StringBuilder res = new StringBuilder();
        for (int i = 0;i<old.length();i++){
            switch (old.charAt(i)){
                case 'ā':
                case 'á':
                case 'ǎ':
                case 'à':
                    res.append("a");
                    break;
                case 'ō':
                case 'ó':
                case 'ǒ':
                case 'ò':
                    res.append("o");
                    break;
                case 'ē':
                case 'é':
                case 'ě':
                case 'è':
                    res.append("e");
                    break;
                case 'ī':
                case 'í':
                case 'ǐ':
                case 'ì':
                    res.append("i");
                    break;
                case 'ū':
                case 'ú':
                case 'ǔ':
                case 'ù':
                case 'ǘ':
                case 'ǚ':
                case 'ǜ':
                    res.append("u");
                    break;
                default:
                    res.append(old.charAt(i));
            }
        }
        return res.toString();
    }

    /**
     * 给所有问题标注上拼音
     *
     * 比如 “ 为什么你这么可爱 ” 变成 “ 为wei什shen么me你ni这zhe么me可ke爱ai ”
     *
     * @return
     * @throws IOException
     */
    public static String transQuestion(String que, HashMap<String, String> pinyinMap) throws IOException {
        StringBuilder builder = new StringBuilder();
        String pinyinTemp = null;
        for (int i=0;i<que.length();i++){
            if ((pinyinTemp = pinyinMap.get(""+que.charAt(i)))!=null){
                builder.append(que.charAt(i));
                builder.append(pinyinTemp);
            }else {
                builder.append(que.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * 将所有问题的拼音去除
     *
     * 比如“ 为wei什shen么me你ni这zhe么me可ke爱ai ” 变成 “ 为什么你这么可爱 ”
     *
     * @return
     * @throws IOException
     */
    public static String transQuestionWithPinyin(String que) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<que.length();i++){
            char temp = que.charAt(i);
            if (que.charAt(i) < 'a' || que.charAt(i) > 'z'){
                builder.append(que.charAt(i));
            }
        }
        return builder.toString();
    }

    /**
     * LCS 算法，来自于 LongestCommonSubsequence.java 的 compute1
     * @param str1
     * @param str2
     * @return
     */
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
        //输出右下角，矩阵最后一个空的数，就是 LCS 的值
        return flag[ch1.length][ch2.length];
    }

    private static class WordLCS{
        String word;
        int LCS;
    }

    public static void main(String[] args) throws IOException {
        //载入问题
        HashMap<String, String> pinyinMap = loadPinyin();
        ArrayList<String> questions = loadQuestion(pinyinMap);
        System.out.println("请输入问题，输入 exit() 退出");
        Scanner scanner = new Scanner(System.in);
        //记录 LCS 值
        int[] LCS = new int[questions.size()];
        String input;
        //输出的 list
        ArrayList<WordLCS > output;
        while (!(input = scanner.next()).equals("exit()")){
            input = transQuestion(input,pinyinMap);
            System.out.println("输入为："+input);
            output = new ArrayList<>();
            int i=0;
            for (String qu:questions){
                LCS[i++] = LCS(qu,input);
            }
            for (i =0;i<LCS.length;i++){
                if (LCS[i] >= 2){
                    WordLCS wordLCS = new WordLCS();
                    wordLCS.word = questions.get(i);
                    wordLCS.LCS = LCS[i];
                    output.add(wordLCS);
                }
            }
            output.sort(new Comparator<WordLCS>() {
                @Override
                public int compare(WordLCS o1, WordLCS o2) {
                    return o2.LCS-o1.LCS;
                }
            });
            for (int t=0;t<10;t++){
                System.out.println(transQuestionWithPinyin(output.get(t).word));
            }

        }
    }

}
