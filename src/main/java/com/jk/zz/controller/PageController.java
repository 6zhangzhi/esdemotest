package com.jk.zz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName PageController
 * @Description:
 * @Author: 张治
 * @Date 2021/11/9 16:36
 */
@Controller
@RequestMapping("page")
public class PageController {
    //展示页面
    @RequestMapping("show")
    public String show(){
        return "show";
    }
    //新增页面
    @RequestMapping("add")
    public String add(){
        return "add";
    }

}
