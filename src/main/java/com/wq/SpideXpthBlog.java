package com.wq;

import com.overzealous.remark.Remark;
import com.wq.haloArticle.ArticleDao;
import com.wq.haloArticle.AtricleVo;
import com.wq.utils.ProPertiesUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.sql.SQLException;
import java.util.Scanner;


public class SpideXpthBlog implements PageProcessor {

    private static String htmlXpth="";
    private static String titleXpth="";
    private static String contentXpth="";

    private Site site = Site.me()
            //.setDomain("https://www.baidu.com/")
            .setSleepTime(1000)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");


    @Override
    public void process(Page page) {

        Selectable obj = page.getHtml().xpath(htmlXpth);
        Selectable title = obj.xpath(titleXpth);
        Selectable content = obj.xpath(contentXpth);

        System.out.println("title:" + title.replace("<[^>]*>", ""));
        //System.out.println("content:" + content);

        Remark remark = new Remark();
        String markdown = remark.convertFragment(content.toString());
        System.out.printf(markdown);
        System.out.printf("html转换MD成功！！！");
        ArticleDao articleDao = new ArticleDao();
        articleDao.addAtricleVo(new AtricleVo(remark.convertFragment(title.toString()),markdown));

    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void importSinglePost(String url) {
        Spider.create(new SpideXpthBlog())
                .addUrl(url)
                .addPipeline(new ConsolePipeline())
                .thread(1)
                .run();
    }

    public static void main(String[] args) {
        System.out.printf("请输入网址");
        Scanner sc= new Scanner(System.in);
        String url = sc.next();
        System.out.printf(url);
        //String url = "www.cnblogs.com/youcong/p/9404007.html";
        if(url.indexOf("cnblogs")>=-1){
            htmlXpth = ProPertiesUtil.getConfig("cnblogs.html");
            titleXpth = ProPertiesUtil.getConfig("cnblogs.title");
            contentXpth = ProPertiesUtil.getConfig("cnblogs.content");
        }else if(url.indexOf("csdn")>=-1){
            htmlXpth = ProPertiesUtil.getConfig("csdn.html");
            titleXpth = ProPertiesUtil.getConfig("csdn.title");
            contentXpth = ProPertiesUtil.getConfig("csdn.content");
        }
            SpideXpthBlog.importSinglePost("https://"+url);
    }
}