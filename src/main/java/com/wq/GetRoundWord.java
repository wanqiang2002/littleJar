package com.wq;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 获取随机词语
 */
public class GetRoundWord {

    public static void main(String[] args) {

        String words = readFileContentByStream(GetRoundWord.class.getResourceAsStream("/word.txt"));
        //File file = new File("src/main/resources/word.txt");
        List<String> list =new ArrayList(Arrays.asList(words.split("、")));
        list = getRandomList(list,10);
        System.out.printf(StringUtils.join(list.toArray(),","));
        //System.out.printf(readFileContentByUrl("src/main/resources/word.txt"));
    }

    public static List getRandomList(List list,int count){
        List olist = new ArrayList();
        if(list.size()<=count){
            return list;
        }else{
            Random random = new Random();
            for(int i=0;i<count;i++){
                int intRandom=random.nextInt(list.size()-1);
                olist.add(list.get(intRandom).toString().trim());
                list.remove(list.get(intRandom));
            }
            return olist;
        }


    }

    public static String readFileContentByStream(InputStream inputStream) {
        //File file = new File(fileName);
        BufferedReader reader = null;

        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
    public static String readFileContentByUrl(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;

        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
