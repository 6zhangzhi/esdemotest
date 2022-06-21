package com.jk.zz.service;

import com.jk.zz.pojo.GoodBean;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName GoodService
 * @Description:
 * @Author: 张治
 * @Date 2021/11/10 16:51
 */
public interface GoodService {

    //测试新增
    void postGood(GoodBean good);
    //查询
    HashMap getGoods(Integer page,Integer rows,GoodBean good);
    //回显
    GoodBean getGood(String id);
    //删除
    void deleteGood(String[] ids);
}
