package com.Easylive.entity.vo;

import com.Easylive.entity.po.VideoInfo;

import java.util.List;

public class VideoInfoResultVo {

    private VideoInfoVo videoInfo;
    private List userActionList;

    public VideoInfoResultVo() {}

    public VideoInfoResultVo(VideoInfoVo videoInfo, List userActionList) {
        this.videoInfo = videoInfo;
        this.userActionList = userActionList;
    }

    public VideoInfoVo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfoVo videoInfo) {
        this.videoInfo = videoInfo;
    }

    public List getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List userActionList) {
        this.userActionList = userActionList;
    }
}
