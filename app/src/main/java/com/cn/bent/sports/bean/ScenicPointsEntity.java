package com.cn.bent.sports.bean;

import org.aisen.android.component.orm.annotation.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dawn on 2018/3/22.
 */

public class ScenicPointsEntity implements Serializable {

    /**
     * scenicSpotName : SYH
     * longitude : 113.088702
     * latitude : 28.013602
     * points : [{"id":43,"major":10001,"pointName":"龙舟基地","longitude":113.088702,"latitude":28.013602,"type":2,"mp3":"/Upload/video/longzhoujidi.mp3","imagesUrl":"/Upload/img/longzhoujidi.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":44,"major":10003,"pointName":"和平鸽广场","longitude":113.088348,"latitude":28.006678,"type":2,"mp3":"/Upload/video/11hepinggeguangchang.mp3","imagesUrl":"/Upload/img/HPGGC.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":45,"major":10005,"pointName":"高空飞索","longitude":113.090805,"latitude":28.012987,"type":2,"mp3":"/Upload/video/feisuo.mp3","imagesUrl":"/Upload/img/KDFS.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":46,"major":10017,"pointName":"财神庙","longitude":113.089641,"latitude":28.010695,"type":2,"mp3":"/Upload/video/caishengmiao.mp3","imagesUrl":"/Upload/img/CSM.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":47,"major":10016,"pointName":"欢乐广场","longitude":113.086004,"latitude":28.005295,"type":2,"mp3":"/Upload/video/huanleguangchang.mp3","imagesUrl":"/Upload/img/HLGC.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":49,"major":10006,"pointName":"五子登科树","longitude":113.090826,"latitude":28.013214,"type":2,"mp3":"/Upload/video/fiveson.mp3","imagesUrl":"/Upload/img/fiveSon.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":50,"major":10007,"pointName":"跳马涧","longitude":113.089839,"latitude":28.017164,"type":2,"mp3":"/Upload/video/jumphouse.mp3","imagesUrl":"/Upload/img/jumpHorse.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":51,"major":10019,"pointName":"外公桥","longitude":113.090001,"latitude":28.008705,"type":2,"mp3":"/Upload/video/grandfa.mp3","imagesUrl":"/Upload/img/grandFa.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":53,"major":10023,"pointName":"游乐场","longitude":113.081981,"latitude":28.005606,"type":2,"mp3":"/Upload/video/youlechang.mp3","imagesUrl":"/Upload/img/youlechang.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":54,"major":10018,"pointName":"月亮岛","longitude":113.089174,"latitude":28.007308,"type":2,"mp3":"/Upload/video/moon.mp3","imagesUrl":"/Upload/img/moon.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":60,"major":10032,"pointName":"快乐小路","longitude":113.089432,"latitude":28.011702,"type":2,"mp3":"/Upload/video/happyroad.mp3","imagesUrl":"/Upload/img/happyRoad.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":62,"major":10044,"pointName":"关帝古泉","longitude":113.086761,"latitude":28.013132,"type":2,"mp3":"/Upload/video/guandiguquan.mp3","imagesUrl":"/Upload/img/gdgq.jpg","pointImg":"/Upload/img/syh.jpg"},{"id":10,"pointName":"游客中心旁","longitude":113.085237,"latitude":28.013309,"type":3},{"id":11,"pointName":"玻璃桥入口","longitude":113.087903,"latitude":28.012456,"type":3},{"id":12,"pointName":"观音庙旁","longitude":113.090585,"latitude":28.013328,"type":3},{"id":13,"pointName":"飞索终点旁","longitude":113.089195,"latitude":28.011462,"type":3},{"id":14,"pointName":"财神庙旁","longitude":113.089362,"latitude":28.010676,"type":3},{"id":15,"pointName":"野战俱乐部旁","longitude":113.088858,"latitude":28.009037,"type":3},{"id":16,"pointName":"烧烤场旁","longitude":113.088788,"latitude":28.007758,"type":3},{"id":17,"pointName":"后山镭战场","longitude":113.086132,"latitude":28.006333,"type":3},{"id":18,"pointName":"欢乐广场旁","longitude":113.085784,"latitude":28.005221,"type":3},{"id":19,"pointName":"攀岩旁","longitude":113.084679,"latitude":28.004419,"type":3},{"id":20,"pointName":"爬行动物园旁","longitude":113.081235,"latitude":28.007019,"type":3},{"id":21,"pointName":"动物竞技场","longitude":113.080956,"latitude":28.003917,"type":3},{"id":22,"pointName":"水上大冲关","longitude":113.082249,"latitude":28.003151,"type":3},{"id":23,"pointName":"游船码头商店","longitude":113.087677,"latitude":28.012859,"type":4},{"id":24,"pointName":"竹林购物超市","longitude":113.086615,"latitude":28.005878,"type":4},{"id":25,"pointName":"和平鸽广场商店","longitude":113.088305,"latitude":28.006816,"type":4},{"id":26,"pointName":"游客中心商店","longitude":113.086057,"latitude":28.013446,"type":4},{"id":27,"pointName":"财神庙商店","longitude":113.089356,"latitude":28.010642,"type":4},{"id":28,"pointName":"欢乐广场商店","longitude":113.085511,"latitude":28.005111,"type":4},{"id":29,"pointName":"游乐场商店","longitude":113.081557,"latitude":28.005992,"type":4},{"id":30,"pointName":"金茂大酒店","longitude":113.095697,"latitude":28.015374,"type":5},{"id":31,"pointName":"古堡餐厅","longitude":113.087819,"latitude":28.006191,"type":5},{"id":32,"pointName":"石燕湖烧烤吧","longitude":113.088723,"latitude":28.007441,"type":5},{"id":33,"pointName":"快活林柴火饭店","longitude":113.087803,"latitude":28.007361,"type":5},{"id":34,"pointName":"石燕湖土菜馆","longitude":113.085419,"latitude":28.005006,"type":5},{"id":35,"pointName":"活鱼村","longitude":113.084657,"latitude":28.004736,"type":5},{"id":36,"pointName":"第一家饭店","longitude":113.095359,"latitude":28.017183,"type":5},{"id":37,"pointName":"金茂大酒店","longitude":113.095697,"latitude":28.015374,"type":6},{"id":38,"pointName":"融景宾馆","longitude":113.086256,"latitude":28.005215,"type":6},{"id":39,"pointName":"大门停车场","longitude":113.084984,"latitude":28.013702,"type":7},{"id":40,"pointName":"欢乐广场停车场","longitude":113.086047,"latitude":28.004954,"type":7},{"id":41,"pointName":"金茂大酒店停车场","longitude":113.094941,"latitude":28.015516,"type":7},{"id":42,"pointName":"景区大门","longitude":113.085934,"latitude":28.013768,"type":8},{"id":48,"major":10004,"pointName":"黑龙潭（岔路口）","longitude":113.085215,"latitude":28.004727,"type":2},{"id":52,"major":10020,"pointName":"塑身门","longitude":113.089641,"latitude":28.008222,"type":2},{"id":55,"major":10008,"pointName":"游客中心","longitude":113.085891,"latitude":28.013555,"type":2},{"id":56,"major":10029,"pointName":"古堡餐厅","longitude":113.087791,"latitude":28.006037,"type":2},{"id":57,"major":10031,"pointName":"卡丁车岔路","longitude":113.081106,"latitude":28.005128,"type":2},{"id":58,"major":10002,"pointName":"石燕湖亲水栈道","longitude":113.087747,"latitude":28.012722,"type":2},{"id":59,"major":10022,"pointName":"游乐场岔路口","longitude":113.083321,"latitude":28.005133,"type":2},{"id":61,"major":10041,"pointName":"财神庙与外公桥之间","longitude":113.089732,"latitude":28.009661,"type":2}]
     */

    private String scenicSpotName;
    private double longitude;
    private double latitude;
    private List<PointsBean> points;

    public String getScenicSpotName() {
        return scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<PointsBean> getPoints() {
        return points;
    }

    public void setPoints(List<PointsBean> points) {
        this.points = points;
    }

    public static class PointsBean implements Serializable{
        /**
         * id : 43
         * major : 10001
         * pointName : 龙舟基地
         * longitude : 113.088702
         * latitude : 28.013602
         * type : 2
         * mp3 : /Upload/video/longzhoujidi.mp3
         * imagesUrl : /Upload/img/longzhoujidi.jpg
         * pointImg : /Upload/img/syh.jpg
         */
        @PrimaryKey(column = "pointId")
        private int id;
        private int major;
        private String pointName;
        private double longitude;
        private double latitude;
        private int type;
        private String mp3;
        private String imagesUrl;
        private String pointImg;
        private boolean isShow;
        private boolean isPlay;//以播放
        private  boolean isNow;//当前播放点
        private  boolean isQuen;//在队列

        public boolean isPlay() {
            return isPlay;
        }

        public void setPlay(boolean play) {
            isPlay = play;
        }

        public boolean isNow() {
            return isNow;
        }

        public void setNow(boolean now) {
            isNow = now;
        }

        public boolean isQuen() {
            return isQuen;
        }

        public void setQuen(boolean quen) {
            isQuen = quen;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMp3() {
            return mp3;
        }

        public void setMp3(String mp3) {
            this.mp3 = mp3;
        }

        public String getImagesUrl() {
            return imagesUrl;
        }

        public void setImagesUrl(String imagesUrl) {
            this.imagesUrl = imagesUrl;
        }

        public String getPointImg() {
            return pointImg;
        }

        public void setPointImg(String pointImg) {
            this.pointImg = pointImg;
        }
    }
}
