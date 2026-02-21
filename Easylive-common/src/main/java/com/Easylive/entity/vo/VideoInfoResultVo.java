package com.Easylive.entity.vo;

import com.Easylive.entity.po.VideoInfo;

public class VideoInfoResultVo {

    public VideoInfoResultVo() {}

    public VideoInfoResultVo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    VideoInfo videoInfo;

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }
}
