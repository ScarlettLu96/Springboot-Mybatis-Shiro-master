package com.pjb.result;

import java.util.List;

/**
 * 系统提示消息封装类
 * @author lsj
 * @date 2019.8
 */
public class ResultInfo {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    //响应中的List
    private List<Object> dataList;

    public ResultInfo() {

    }

    public static ResultInfo build(Integer status, String msg, Object data, List<Object> dataList) {
        return new ResultInfo(status, msg, data, dataList);
    }

    public static ResultInfo build(Integer status, String msg, Object data) {
        return new ResultInfo(status, msg, data, null);
    }

    public static ResultInfo build(Integer status, String msg) {
        return new ResultInfo(status, msg, null, null);
    }

    public static ResultInfo ok(Object data) {
        return new ResultInfo(data);
    }

    public static ResultInfo ok(List<Object> dataList) {
        return new ResultInfo(dataList);
    }

    public static ResultInfo ok() {
        return new ResultInfo(null);
    }


    public ResultInfo(Integer status, String msg, Object data, List<Object> dataList) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.dataList = dataList;
    }

    public ResultInfo(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
        this.dataList = null;
    }

    public ResultInfo(List<Object> dataList) {
        this.status = 200;
        this.msg = "OK";
        this.data = null;
        this.dataList = dataList;
    }

}
