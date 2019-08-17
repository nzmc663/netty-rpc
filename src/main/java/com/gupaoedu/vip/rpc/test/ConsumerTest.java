package com.gupaoedu.vip.rpc.test;

import com.gupaoedu.vip.rpc.api.bean.Blog;
import com.gupaoedu.vip.rpc.api.service.BlogService;
import com.gupaoedu.vip.rpc.consumer.proxy.RpcProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费端启动测试类
 *
 * @author tzf
 */
public class ConsumerTest {

    public static void main(String[] args) {

        BlogService service = RpcProxy.getProxy(BlogService.class);

        System.out.println(service.get(1));

        System.out.println(service.query());

        List<Blog> list = new ArrayList<>();
        Blog blog = new Blog();
        blog.setId(1);
        blog.setTitle("如何手写一个RPC框架");
        blog.setContent("首先这样写.....");
        blog.setAuthor("田泽法");
        blog.setDate(String.valueOf(System.currentTimeMillis()));
        list.add(blog);
        Blog blog2 = new Blog();
        blog2.setId(2);
        blog2.setTitle("如何手写一个Spring框架");
        blog2.setContent("首先这样写.....");
        blog2.setAuthor("曾毫");
        blog2.setDate(String.valueOf(System.currentTimeMillis()));
        list.add(blog2);
        System.out.println(service.delete(list));
        System.out.println(service.insert(list));
        System.out.println(service.update(list));
    }
}
