package com.cn.bent.sports.view.activity.youle.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/4/004.
 */

public class QueryInfo implements Serializable {

    /**
     * count : true
     * pageNum : 1
     * pageSize : 10
     * queryPo : 0
     */

    private boolean count;
    private int pageNum;
    private int pageSize;
    private int queryPo;

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getQueryPo() {
        return queryPo;
    }

    public void setQueryPo(int queryPo) {
        this.queryPo = queryPo;
    }

    public QueryInfo(boolean count, int pageNum, int pageSize, int queryPo) {
        this.count = count;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.queryPo = queryPo;
    }
}
