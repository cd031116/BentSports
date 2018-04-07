package com.cn.bent.sports.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/4/7.
 */

public class GameRankEntity implements Serializable{

    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 1
     * endRow : 10
     * total : 31
     * pages : 4
     * list : [{"id":139,"teamName":"27测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":1,"timing":2216,"score":1959},{"id":143,"teamName":"88测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":2,"timing":2728,"score":1897},{"id":126,"teamName":"71测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":3,"timing":3484,"score":1844},{"id":120,"teamName":"11测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":4,"timing":3873,"score":1836},{"id":142,"teamName":"39测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":5,"timing":81,"score":1788},{"id":128,"teamName":"84测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":6,"timing":4505,"score":1773},{"id":144,"teamName":"76测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":7,"timing":1086,"score":1545},{"id":136,"teamName":"50测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":8,"timing":815,"score":1542},{"id":131,"teamName":"50测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":9,"timing":3606,"score":1403},{"id":117,"teamName":"28测试队伍","avatar":"http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg","rank":10,"timing":353,"score":1375}]
     * prePage : 0
     * nextPage : 2
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2,3,4]
     * navigateFirstPage : 1
     * navigateLastPage : 4
     * firstPage : 1
     * lastPage : 4
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int firstPage;
    private int lastPage;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean implements Serializable{
        /**
         * id : 139
         * teamName : 27测试队伍
         * avatar : http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg
         * rank : 1
         * timing : 2216
         * score : 1959
         */

        private int id;
        private String teamName;
        private String avatar;
        private int rank;
        private int timing;
        private int score;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getTiming() {
            return timing;
        }

        public void setTiming(int timing) {
            this.timing = timing;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
