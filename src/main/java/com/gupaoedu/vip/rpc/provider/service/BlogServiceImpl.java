package com.gupaoedu.vip.rpc.provider.service;

import com.gupaoedu.vip.rpc.api.bean.Blog;
import com.gupaoedu.vip.rpc.api.service.BlogService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 服务提供方接口实现
 *
 * @author tzf
 */
public class BlogServiceImpl implements BlogService {

    /**
     * 伪造获取一条博客
     */
    @Override
    public Blog get(int id) {
        Blog blog = new Blog();
        blog.setId(id);
        blog.setAuthor("田泽法");
        blog.setTitle("如何手写一个RPC框架");
        blog.setContent("首先...其次...最后...");
        blog.setDate(String.valueOf(System.currentTimeMillis()));
        System.out.println("------>"+"查询成功");
        System.out.println(blog);
        System.out.println("------------------\n");
        return blog;
    }
    /**
     * 伪造查询全部博客
     */
    @Override
    public List<Blog> query() {
        List<Blog> list = new ArrayList<>();
        Blog blog = new Blog();
        blog.setId(1);
        blog.setAuthor("田泽法");
        blog.setTitle("如何手写一个RPC框架");
        blog.setContent("首先...其次...最后...");
        blog.setDate(String.valueOf(System.currentTimeMillis()));
        list.add(blog);
        Blog blog2 = new Blog();
        blog2.setId(2);
        blog2.setAuthor("曾毫");
        blog2.setTitle("如何手写一个Spring框架");
        blog2.setContent("首先...其次...最后...");
        blog2.setDate(String.valueOf(System.currentTimeMillis()));
        list.add(blog2);
        System.out.println("------>"+"查询成功");
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("------------------\n");
        return list;
    }
    /**
     * 伪造批量插入博客
     */
    @Override
    public int insert(List<Blog> list) {
        System.out.println("------>"+"插入成功");
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("------------------\n");
        return list==null?0:list.size();
    }
    /**
     * 伪造批量修改博客
     */
    @Override
    public int update(List<Blog> list) {
        System.out.println("------>"+"修改成功");
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("------------------\n");
        return list==null?0:list.size();
    }
    /**
     * 伪造批量删除博客
     */
    @Override
    public int delete(List<Blog> list) {
        System.out.println("------>"+"删除成功");
        System.out.println(Arrays.toString(list.toArray()));
        System.out.println("------------------\n");
        return list==null?0:list.size();
    }
}
