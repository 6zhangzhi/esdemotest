package com.jk.zz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jk.zz.dao.GoodDao;
import com.jk.zz.pojo.GoodBean;
import com.jk.zz.service.GoodService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName GoodServiceImpl
 * @Description:
 * @Author: 张治
 * @Date 2021/11/10 16:51
 */
@Service
public class GoodServiceImpl implements GoodService {
    @Autowired
    private GoodDao goodDao;
    @Autowired
    private ElasticsearchTemplate esTemplate;
    //测试新增
    @Override
    public void postGood(GoodBean good) {
       if(good.getGoodId().equals("")){
            good.setGoodId(null);
       }
        goodDao.save(good);
    }
    //查询
    @Override
    public HashMap getGoods(Integer page,Integer rows,GoodBean good) {
        HashMap<Object, Object> map = new HashMap<>();
        int start=(page-1)*rows;//开始位置
        //获取到当前elasticsearch的客户端对象
        Client client = esTemplate.getClient();
        //创建查询请求对象
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("goods").setTypes("gg");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();//复合查询对象
        //查询两个条件
        /*if(!StringUtils.isEmpty(good.getSearchField())){
            MatchQueryBuilder goodName = QueryBuilders.matchQuery("goodName", good.getSearchField());
            MatchQueryBuilder detail = QueryBuilders.matchQuery("detail", good.getSearchField());
            boolQueryBuilder.should(goodName).minimumShouldMatch(1);
            boolQueryBuilder.should(detail).minimumShouldMatch(1);
        }*/
        if(!StringUtils.isEmpty(good.getGoodName())){
            MatchQueryBuilder sear = QueryBuilders.matchQuery("sear", good.getGoodName());
            boolQueryBuilder.must(sear);
        }
        RangeQueryBuilder gooddate = QueryBuilders.rangeQuery("gooddate");
        if(!StringUtils.isEmpty(good.getMindate())){
            gooddate.gte(good.getMindate());//拼接查询条件
        }
        if(!StringUtils.isEmpty(good.getMaxdate())){
            gooddate.lte(good.getMaxdate());//拼接查询条件
        }
        //时间条查
        if(!StringUtils.isEmpty(good.getMindate())||!StringUtils.isEmpty(good.getMaxdate())){
            boolQueryBuilder.must(gooddate);//放入复合查询中
        }
        //请求对象中加入复合查询
        searchRequestBuilder.setQuery(boolQueryBuilder);
        //设置分页信息
        searchRequestBuilder.setFrom(start);
        searchRequestBuilder.setSize(rows);
        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //highlightBuilder.field("goodName");//设置高亮字段
        //highlightBuilder.field("detail");//设置高亮字段
        highlightBuilder.field("sear");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        //排序
        searchRequestBuilder.addSort("goodPrice", SortOrder.ASC);
        searchRequestBuilder.addSort("gooddate", SortOrder.DESC);
        //请求对象中加入高亮
        searchRequestBuilder.highlighter(highlightBuilder);
        //执行当前查询对象 拿到返回结果集
        SearchResponse searchResponse = searchRequestBuilder.get();
        //获取到所有搜索出的结果 也叫所有命中对象
        SearchHits hits = searchResponse.getHits();
        //使用迭代器 循环迭代查询内容
        Iterator<SearchHit> iterator = hits.iterator();
        long total = hits.getTotalHits();
        List<GoodBean> list = new ArrayList<>();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            String id = next.getId();
            String str = next.getSourceAsString();
            GoodBean goodBean = JSONObject.parseObject(str, GoodBean.class);
            //获取到所有高亮字段结果集
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            ///根据高亮字段名称 获取高亮字段内容
            //HighlightField goodName = highlightFields.get("goodName");
            HighlightField sear = highlightFields.get("sear");

            if(sear!=null){
                Text[] fragments = sear.getFragments();
                for (int i = 0; i <fragments.length ; i++) {
                        String replace=fragments[i].toString().replace("<font color='red'>","").replace("</font>","");
                        if(goodBean.getGoodName().equals(replace)){
                            goodBean.setGoodName(fragments[i].toString());
                        }
                        if(goodBean.getDetail().equals(replace)){
                            goodBean.setDetail(fragments[i].toString());
                        }
                }
            }
           /* if(goodName!=null){
                goodBean.setGoodName(goodName.getFragments()[0].toString());
            }*/
            goodBean.setGoodId(id);
            list.add(goodBean);
        }
        map.put("total",total);
        map.put("rows",list);
        return map;
    }
    //回显
    @Override
    public GoodBean getGood(String id) {
        return goodDao.findById(id).get();
    }
    //删除
    @Override
    public void deleteGood(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            goodDao.deleteById(ids[i]);
        }
    }

   /* public static void main(String[] args) {
        //冒泡排序算法
        int[] numbers=new int[]{1,5,8,2,3,9,4};
        //需进行length-1次冒泡
        for(int i=0;i<numbers.length-1;i++) {
            for(int j=0;j<numbers.length-1-i;j++) {
                if(numbers[j]<numbers[j+1]) {
                    int temp=numbers[j];
                    numbers[j]=numbers[j+1];
                    numbers[j+1]=temp;
                }
            }
        }
        System.out.println("从小到大排序后的结果是:");
        for(int i=0;i<numbers.length;i++){
            System.out.print(numbers[i]+" ");
        }


        select r.date
        ,sum(case when r.result=1 then 1 else 0 end) as win
        ,sum(case when r.result=2 then 1 else 0 end) as faluat
        from a_result r
        group by r.date

    }*/



}
