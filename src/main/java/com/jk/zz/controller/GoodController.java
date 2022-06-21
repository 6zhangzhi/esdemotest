package com.jk.zz.controller;

import com.alibaba.fastjson.JSON;
import com.jk.zz.pojo.GoodBean;
import com.jk.zz.service.GoodService;
import com.jk.zz.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName GoodController
 * @Description:
 * @Author: 张治
 * @Date 2021/11/10 17:11
 */
@RestController
@RequestMapping("/good")
public class GoodController {
    @Autowired
    private GoodService goodService;
    //上传图片
    @RequestMapping("uploadImg")
    @ResponseBody
    public String uploadImg(MultipartFile img, HttpServletRequest request){
        String str= FileUtil.FileUpload(img, request);
        String ss= JSON.toJSONString(str);
        return ss;
    }
    //查询
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public HashMap<String,Object> getGoods(Integer page,Integer rows,GoodBean good){
        return goodService.getGoods(page,rows,good);
    }
    //新增
    @RequestMapping(value = "/",method = RequestMethod.POST)
    public void postGood(GoodBean good){
        goodService.postGood(good);
    }
    //回显
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public GoodBean getGood(@PathVariable String id){
        return goodService.getGood(id);
    }
    //删除
    @RequestMapping(value = "/{ids}",method = RequestMethod.DELETE)
    public void deleteGood(@PathVariable String[]ids){
        goodService.deleteGood(ids);
    }


}
