package com.wq.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class HttpRequestUtil {


    /**
     * http get方法
     * @param url
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     * @throws IOException
     */
    public static String sendGet(String url, String param) throws IOException {

        BufferedReader in = null;
        String urlString = "" ;
        //String urlString = url + "?" + param;
        if(StringUtils.isBlank(param)){
            urlString = url;
        }else{
            urlString = url + "?" + param;
        }
        URLConnection urlConnection = new URL(urlString).openConnection();
        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        StringBuilder sb = new StringBuilder();
        try {
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String tempStr;
            while ((tempStr = in.readLine()) != null) {
                sb = sb.append(tempStr);
            }
        } catch (IOException e) {
            System.out.println("发送 GET 请求出现异常！"+e);
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     *
     * @param url
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, String param) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setConnectTimeout(30 * 1000);
        urlConnection.setRequestProperty("accept", "*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(urlConnection.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();

        StringBuilder sb = new StringBuilder();
        try {
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String tempStr;
            while ((tempStr = in.readLine()) != null) {
                sb = sb.append(tempStr);
            }
        } catch (IOException e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
