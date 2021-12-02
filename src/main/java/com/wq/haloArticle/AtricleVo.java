package com.wq.haloArticle;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客稿件实体类
 */
public class AtricleVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
    private Date createTime;

    public AtricleVo(String title,String content){
        this.title=title;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
