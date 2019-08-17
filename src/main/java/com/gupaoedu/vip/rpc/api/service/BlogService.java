package com.gupaoedu.vip.rpc.api.service;

import com.gupaoedu.vip.rpc.api.bean.Blog;

import java.util.List;

/**
 * Blog对外api服务层
 *
 * @author tzf
 */
public interface BlogService {
    /**
     * 根据id获取
     */
    Blog get(int id);
    /**
     * 查询
     */
    List<Blog> query();
    /**
     * 新增
     */
    int insert(List<Blog> list);
    /**
     * 修改
     */
    int update(List<Blog> list);
    /**
     * 删除
     */
    int delete(List<Blog> list);

}
