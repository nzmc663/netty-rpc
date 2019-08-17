package com.gupaoedu.vip.rpc.api.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 博客实体
 *
 * @author tzf
 */
@Data
public class Blog implements Serializable {

    private static final long serialVersionUID = 5348075059215140828L;

    private int id;

    private String author;

    private String title;

    private String content;

    private String date;

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
