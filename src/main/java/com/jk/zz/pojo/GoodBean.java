package com.jk.zz.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @ClassName GoodBean
 * @Description:
 * @Author: 张治
 * @Date 2021/11/10 16:48
 */
@Data
@Document(indexName = "goods",type = "gg")
public class GoodBean {
    @Id
    private String  goodId;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart",copyTo = "sear")
    private String goodName;
    private Integer goodPrice;
    private Integer goodCount;
    private String gooddate;
    private String goodimg;
    @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart",copyTo = "sear")
    private String detail;
    //业务字段
    @Transient
    private String mindate;
    @Transient
    private String maxdate;
    @Transient
    private String sear;
    /*@Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
   private String searchField;*/

}
