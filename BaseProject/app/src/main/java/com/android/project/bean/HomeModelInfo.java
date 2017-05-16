package com.android.project.bean;


import com.android.project.base.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leon on 17/4/27.
 */

public class HomeModelInfo extends BaseInfo implements Serializable {

    public List<BannerBean> banner;
    public List<DataBean> data;
    public int type;

    public static class BannerBean implements Serializable{

        public long id;
        public String tip;
        public String imageUrl;
        public int type;
        public long discountId;
        public long merchantId;
        public String webLink;
    }


    public static class DataBean implements Serializable{

        public long id;
        public String imageUrl;
        public long merchantId;
        public String merchantName;
        public long discountId;
        public int discountTotal;
        public int discountRemaining;
        public String discountTitle;
        public String discountDescribe;
    }

}
