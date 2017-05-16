package com.android.project.bean;


/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/3/31
 */

public class EventBusModel {

    String message;
    Object data;

    public EventBusModel(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

}
