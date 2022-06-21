package com.jk.zz.dao;

import com.jk.zz.pojo.GoodBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @ClassName GoodDao
 * @Description:
 * @Author: 张治
 * @Date 2021/11/10 16:52
 */
public interface GoodDao extends ElasticsearchRepository<GoodBean,String> {

}
