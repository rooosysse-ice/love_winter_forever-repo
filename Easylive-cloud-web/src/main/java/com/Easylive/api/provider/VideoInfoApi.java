package com.Easylive.api.provider;

import com.Easylive.annotation.RecordUserMessage;
import com.Easylive.component.EsSearchComponent;
import com.Easylive.component.RedisComponent;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.enums.MessageTypeEnum;
import com.Easylive.entity.enums.SearchOrderTypeEnum;
import com.Easylive.entity.po.VideoInfo;
import com.Easylive.entity.po.VideoInfoFile;
import com.Easylive.entity.po.VideoInfoFilePost;
import com.Easylive.entity.po.VideoInfoPost;
import com.Easylive.entity.query.VideoInfoFilePostQuery;
import com.Easylive.entity.query.VideoInfoPostQuery;
import com.Easylive.entity.query.VideoInfoQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.mappers.VideoInfoPostMapper;
import com.Easylive.service.VideoInfoFilePostService;
import com.Easylive.service.VideoInfoFileService;
import com.Easylive.service.VideoInfoService;
import com.Easylive.service.impl.VideoInfoPostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/video")
@Validated
public class VideoInfoApi {

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private VideoInfoFileService videoInfoFileService;

    @Resource
    private VideoInfoPostServiceImpl videoInfoPostService;

    @Resource
    private VideoInfoFilePostService videoInfoFilePostService;

    @Resource
    private EsSearchComponent esSearchComponent;

    @RequestMapping("/getVideoInfoFileByFileId")
    public VideoInfoFile getVideoInfo(@NotEmpty String fileId) {
        return videoInfoFileService.getVideoInfoFileByFileId(fileId);
    }

    @RequestMapping("/getVideoInfoByVideoId")
    public VideoInfo getVideoInfoByVideoId(@NotEmpty String videoId) {
        return videoInfoService.getVideoInfoByVideoId(videoId);
    }

    @RequestMapping("/updateCountInfo")
    public void updateCountInfo(@NotEmpty String videoId, @NotEmpty String fileId, @NotNull Integer changeCount) {
       videoInfoService.updateCountInfo(videoId,fileId,changeCount);
    }

    @RequestMapping("/getVideoInfoPostByVideoId")
    public VideoInfoPost getVideoInfoPostByVideoId(String videoId) {
        return videoInfoPostService.getVideoInfoPostByVideoId(videoId);
    }

    @RequestMapping("/updateDocCount")
    public void updateDocCount(String videoId,SearchOrderTypeEnum searchOrderTypeEnum,Integer changeCOunt) {
        esSearchComponent.updateDocCount(videoId, searchOrderTypeEnum.getField(), changeCOunt);
    }
    @RequestMapping("/admin/loadVideoList")
    public PaginationResultVO loadVideoList(VideoInfoPostQuery videoInfoPostQuery) {
        videoInfoPostQuery.setOrderBy("last_update_time desc");
        videoInfoPostQuery.setQueryCountInfo(true);
        videoInfoPostQuery.setQueryUserInfo(true);
        PaginationResultVO resultVO = videoInfoPostService.findListByPage(videoInfoPostQuery);
        return resultVO;
    }

    @RequestMapping("/admin/auditVideo")
    @RecordUserMessage(messageType = MessageTypeEnum.SYS)
    public void auditVideo(@NotEmpty String videoId, @NotNull Integer status, String reason) {
        videoInfoPostService.auditVideo(videoId, status, reason);
    }

    @RequestMapping("/admin/deleteVideo")
    public void deleteVideo(@NotEmpty String videoId) {
        videoInfoService.deleteVideo(videoId, null);
    }

    @RequestMapping("/admin/recommendVideo")
    public void recommendVideo(@NotEmpty String videoId) {
        videoInfoPostService.recommendVideo(videoId);
    }

    @RequestMapping("/admin/loadVideoPList")
    public List<VideoInfoFilePost> loadVideoPList(@NotEmpty String videoId) {
        VideoInfoFilePostQuery postQuery = new VideoInfoFilePostQuery();
        postQuery.setOrderBy("file_index asc");
        postQuery.setVideoId(videoId);
        List<VideoInfoFilePost> videoInfoFilePostsList = videoInfoFilePostService.findListByParam(postQuery);
        return videoInfoFilePostsList;
    }

    @RequestMapping("/getVideoCount")
    public Integer getVideoCount(@RequestBody VideoInfoQuery videoInfoQuery) {
        return videoInfoService.findCountByParam(videoInfoQuery);
    }

    @RequestMapping("/transferVideoFile4Db")
    public void transferVideoFile4Db(@RequestParam String videoId, @RequestParam String uploadId, @RequestParam String userId,
                                     @RequestBody VideoInfoFilePost updateFilePost) {
        videoInfoPostService.transferVideoFile4Db(videoId, uploadId, userId, updateFilePost);
    }

}
