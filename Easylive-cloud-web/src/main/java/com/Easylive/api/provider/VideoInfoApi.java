package com.Easylive.api.provider;

import com.Easylive.component.RedisComponent;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.po.VideoInfo;
import com.Easylive.entity.po.VideoInfoFile;
import com.Easylive.service.VideoInfoFileService;
import com.Easylive.service.VideoInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(Constants.INNER_API_PREFIX + "/video")
@Validated
public class VideoInfoApi {

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private VideoInfoFileService videoInfoFileService;

    @RequestMapping("/getVideoInfoFileByFileId")
    public VideoInfoFile getVideoInfo(@NotEmpty String fileId) {
        return videoInfoFileService.getVideoInfoFileByFileId(fileId);
    }

}
