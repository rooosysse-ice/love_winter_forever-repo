package com.Easylive.web.task;


import com.Easylive.component.EsSearchComponent;
import com.Easylive.component.RedisComponent;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.dto.VideoPlayInfoDto;
import com.Easylive.entity.enums.SearchOrderTypeEnum;
import com.Easylive.entity.po.VideoInfoFilePost;
import com.Easylive.entity.po.VideoPlayHistory;
import com.Easylive.redis.RedisUtils;
import com.Easylive.service.VideoInfoPostService;
import com.Easylive.service.VideoInfoService;
import com.Easylive.service.VideoPlayHistoryService;
import com.Easylive.utils.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ExecuteQueueTask {

    private ExecutorService executorService = Executors.newFixedThreadPool(Constants.LENGTH_10);

    @Resource
    private VideoInfoPostService videoInfoPostService;

    @Resource
    private RedisUtils redisUtils;


    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private EsSearchComponent esSearchComponent;

    @Resource
    private VideoPlayHistoryService videoPlayHistoryService;

    @PostConstruct
    public void consumeTransferFileQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoInfoFilePost videoInfoFile = (VideoInfoFilePost) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_TRANSFER);
                    if (videoInfoFile == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    videoInfoPostService.transferVideoFile(videoInfoFile);
                } catch (Exception e) {
                    log.error("获取转码文件队列信息失败", e);
                }
            }
        });
    }

    @PostConstruct
    public void consumeVideoPlayQueue() {
        executorService.execute(() -> {
            while (true) {
                try {
                    VideoPlayInfoDto videoPlayInfoDto = (VideoPlayInfoDto) redisUtils.rpop(Constants.REDIS_KEY_QUEUE_VIDEO_PLAY);
                    if (videoPlayInfoDto == null) {
                        Thread.sleep(1500);
                        continue;
                    }
                    //更新播放数
                    videoInfoService.addReadCount(videoPlayInfoDto.getVideoId());
                    if (!StringTools.isEmpty(videoPlayInfoDto.getUserId())) {
                        //记录历史
                        videoPlayHistoryService.saveHistory(videoPlayInfoDto.getUserId(), videoPlayInfoDto.getVideoId(), videoPlayInfoDto.getFileIndex());
                    }
                    //按天记录播放数
                    redisComponent.recordVideoPlayCount(videoPlayInfoDto.getVideoId());

                    //更新es播放数量
                    esSearchComponent.updateDocCount(videoPlayInfoDto.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(), 1);

                } catch (Exception e) {
                    log.error("获取视频播放文件队列信息失败", e);
                }
            }
        });
    }

}
