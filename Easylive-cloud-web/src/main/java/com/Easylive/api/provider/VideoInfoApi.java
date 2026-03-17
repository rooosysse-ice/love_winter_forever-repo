package com.Easylive.api.provider;

import com.Easylive.component.EsSearchComponent;
import com.Easylive.component.RedisComponent;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.enums.SearchOrderTypeEnum;
import com.Easylive.entity.po.VideoInfo;
import com.Easylive.entity.po.VideoInfoFile;
import com.Easylive.entity.po.VideoInfoPost;
import com.Easylive.mappers.VideoInfoPostMapper;
import com.Easylive.service.VideoInfoFileService;
import com.Easylive.service.VideoInfoService;
import com.Easylive.service.impl.VideoInfoPostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

}
