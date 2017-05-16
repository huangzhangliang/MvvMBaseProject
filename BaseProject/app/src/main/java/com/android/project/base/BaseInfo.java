package com.android.project.base;

import java.io.Serializable;

/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/5
 */

public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 6174001313274251754L;

    /**
     * state : success
     * status : success
     * success : true
     */

    public String msg;
    public int status;
    public boolean success;


}
