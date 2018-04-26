package com.yiming.entity;

import java.security.Timestamp;
import java.util.Date;

public class Video {
    private String videoNum;
    private String viedoName;
    private int watchCount;
    private Date releaseTime;
    private String teacherId;
    private String videoPath;
    private String srcPath;
    private String imgPath;

    public Video(String videoNum, String viedoName, int watchCount, Date releaseTime, String teacherId,
            String videoPath, String srcPath, String imgPath) {
        super();
        this.videoNum = videoNum;
        this.viedoName = viedoName;
        this.watchCount = watchCount;
        this.releaseTime = releaseTime;
        this.teacherId = teacherId;
        this.videoPath = videoPath;
        this.srcPath = srcPath;
        this.imgPath = imgPath;
    }

    public String getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(String videoNum) {
        this.videoNum = videoNum;
    }

    public String getViedoName() {
        return viedoName;
    }

    public void setViedoName(String viedoName) {
        this.viedoName = viedoName;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public String toString() {
        return "Video [videoNum=" + videoNum + ", viedoName=" + viedoName + ", watchCount=" + watchCount
                + ", releaseTime=" + releaseTime + ", teacherId=" + teacherId + ", videoPath=" + videoPath
                + ", srcPath=" + srcPath + ", imgPath=" + imgPath + "]";
    }

}
