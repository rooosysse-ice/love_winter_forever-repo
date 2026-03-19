package com.Easylive.api.consumer;

import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.po.VideoInfoFile;
import com.Easylive.entity.po.VideoInfoFilePost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constants.SERVER_NAME_WEB)
public interface VideoClient {

    @RequestMapping(Constants.INNER_API_PREFIX + "/video/getVideoInfoFileByFileId")
    VideoInfoFile getVideoInfoFileByFileId(@RequestParam String fileId);

    @PostMapping(Constants.INNER_API_PREFIX + "/video/transferVideoFile4Db")
    VideoInfoFile transferVideoFile4Db(@RequestParam String videoId,
                                       @RequestParam String uploadId,
                                       @RequestParam String userId,
                                       @RequestBody VideoInfoFilePost updateFilePost);
}
