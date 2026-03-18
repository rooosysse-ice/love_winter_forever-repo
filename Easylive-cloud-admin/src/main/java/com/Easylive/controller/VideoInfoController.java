package com.Easylive.controller;

import com.Easylive.annotation.RecordUserMessage;
import com.Easylive.api.consumer.WebClient;
import com.Easylive.entity.enums.MessageTypeEnum;
import com.Easylive.entity.po.VideoInfoFilePost;
import com.Easylive.entity.query.VideoInfoFilePostQuery;
import com.Easylive.entity.query.VideoInfoPostQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.entity.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping("/videoInfo")
public class VideoInfoController extends ABaseController {

    @Resource
    private WebClient webClient;

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(VideoInfoPostQuery videoInfoPostQuery) {
        return getSuccessResponseVO(webClient.loadVideoList(videoInfoPostQuery));
    }

    @RequestMapping("/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public ResponseVO auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) {
        webClient.auditVideo(videoId, status, reason);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId) {
        webClient.deleteVideo(videoId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/recommendVideo")
    public ResponseVO recommendVideo(@NotEmpty String videoId) {
        webClient.recommendVideo(videoId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        return getSuccessResponseVO(webClient.loadVideoPList(videoId));
    }
}
