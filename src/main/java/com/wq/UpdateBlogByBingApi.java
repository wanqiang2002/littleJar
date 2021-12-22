package com.wq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wq.haloArticle.PhotosDao;
import com.wq.haloArticle.PhotosVo;
import com.wq.utils.HttpRequestUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * bing每日一图api更新至博客
 */
public class UpdateBlogByBingApi {
    private static String api="https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    private static String bing="https://cn.bing.com";

    public static void main(String[] args) throws IOException {
        String apiRet = HttpRequestUtil.sendGet(api,null);

        JSONObject jsonObject = JSON.parseObject(apiRet);
        JSONArray array = jsonObject.getJSONArray("images");
        JSONObject jsonObject1 =array.getJSONObject(0);
        String name = jsonObject1.getString("copyright").split(" \\(")[0];
        String thumbnail = bing+jsonObject1.getString("urlbase")+"_400x240.jpg";
        String url = bing+jsonObject1.getString("url");
        PhotosVo photosVo= new PhotosVo();
        photosVo.setName(name);
        photosVo.setThumbnail(thumbnail);
        photosVo.setUrl(url);

        //System.out.printf(name);
        PhotosDao photosDao = new PhotosDao();
        //添加每日一图至博客
        photosDao.addPhotosVo(photosVo);
        System.out.printf("更新成功！！");

    }
    /*public static void main(String[] args) throws IOException {
        String apiUrl = "https://bing.xinac.net/?page=8";
        String apiRet = HttpRequestUtil.sendGet(apiUrl,null);

        Html html = Html.create(apiRet);
        List<Selectable> aaList = html.xpath("//a[@class='show']").nodes();
        aaList.forEach(e->{
            String url = e.xpath("//a[@class='show']/@href").get();
            String name = e.xpath("//a[@class='show']/@title").get();
            PhotosVo photosVo= new PhotosVo();
            photosVo.setName(name);
            photosVo.setThumbnail(url.replaceAll("1920x1080","400x240"));
            photosVo.setUrl(url);

            PhotosDao photosDao = new PhotosDao();
            //添加每日一图至博客
            photosDao.addPhotosVo(photosVo);
        });


    }*/



}
