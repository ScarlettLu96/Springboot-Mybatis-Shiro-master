package com.pjb.controller;

import com.alibaba.fastjson.JSONObject;
import com.pjb.entity.Location;
import com.pjb.service.UserInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lsj
 * @date 2019.7
 */
@Controller
@RequestMapping("/dataInfo")
public class DataController {
    @Autowired
    UserInfoService userInfoService;
    /**
     *数据查询
     */
    @RequestMapping("/dataQuery")
    //@RequiresPermissions("dataInfo:view")
    //@ResponseBody
    public String dataQuery(){
        return "dataQuery";
    }

    @GetMapping("/spotInfo")
    public String index(){
        return"spotInfo";
    }


    @ResponseBody
    @RequestMapping(value = "/spotInfo", method = RequestMethod.POST)
    public Location spotInfo(@RequestBody JSONObject jsonObject){

        Integer id = jsonObject.getInteger("id");
        Location location=userInfoService.gis(id);
        return location;
    }

    @RequestMapping("/gisIndex")
    public String gisInfo(){
        return "gisIndex";
    }

    @RequestMapping("/gisGallery")
    public String gisService(){
        return "gisGallery";
    }

    @RequestMapping("/gisContact")
    public String gisContact(){
        return "gisContact";
    }

    @RequestMapping("/gisAbout")
    public String gisAbout(){
        return "gisAbout";
    }

    @RequestMapping("/gisShortCodes")
    public String gisShortCodes(){
        return "gisShortCodes";
    }

    @RequestMapping("/gisGSM")
    public String gisGSM(){
        return "gisGSM";
    }

    @RequestMapping("/gisMD")
    public String gisJump(){
        return "gisMD";
    }

    @RequestMapping("/gisNewIndex")
    public String gisNewIndex(){
        return "gisNewIndex";
    }

    @RequestMapping("/gisTour")
    public String gisTour() {
        return "tour";
    }

}
