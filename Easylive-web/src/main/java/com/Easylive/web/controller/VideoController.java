package com.Easylive.web.controller;

import com.Easylive.component.RedisComponent;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.enums.*;
import com.Easylive.entity.po.VideoInfo;
import com.Easylive.entity.po.VideoInfoFile;
import com.Easylive.entity.query.VideoInfoFileQuery;
import com.Easylive.entity.query.VideoInfoQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.entity.vo.VideoInfoResultVo;
import com.Easylive.exception.BusinessException;
import com.Easylive.service.VideoInfoFileService;
import com.Easylive.service.VideoInfoService;
import com.Easylive.utils.CopyTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/video")
@Slf4j
public class VideoController extends ABaseController {

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private VideoInfoFileService videoInfoFileService;


    @Resource
    private RedisComponent redisComponent;


    @RequestMapping("/loadRecommendVideo")
    public ResponseVO loadRecommendVideo() {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("create_time desc");
        videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.RECOMMEND.getType());
        List<VideoInfo> recommendVideoList = videoInfoService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(recommendVideoList);
    }

    @RequestMapping("/loadVideo")
    public ResponseVO postVideo(Integer pCategoryId, Integer categoryId, Integer pageNo) {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryId(categoryId);
        videoInfoQuery.setpCategoryId(pCategoryId);
        videoInfoQuery.setPageNo(pageNo);
        videoInfoQuery.setQueryUserInfo(true);
        videoInfoQuery.setOrderBy("create_time desc");
        if (categoryId == null && pCategoryId == null) {
            videoInfoQuery.setRecommendType(VideoRecommendTypeEnum.NO_RECOMMEND.getType());
        }
        PaginationResultVO resultVO = videoInfoService.findListByPage(videoInfoQuery);
        return getSuccessResponseVO(resultVO);
    }

    @RequestMapping("/getVideoInfo")
    public ResponseVO getVideoInfo(@NotEmpty String videoId) {
        VideoInfo videoInfo = videoInfoService.getVideoInfoByVideoId(videoId);
        if (null == videoInfo) {
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }

        //TODO:获取用户行为：是否点赞、是否收藏等
        VideoInfoResultVo resultVo = new VideoInfoResultVo(videoInfo);
        return getSuccessResponseVO(resultVo);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        VideoInfoFileQuery videoInfoQuery = new VideoInfoFileQuery();
        videoInfoQuery.setVideoId(videoId);
        videoInfoQuery.setOrderBy("file_index asc");
        List<VideoInfoFile> fileList = videoInfoFileService.findListByParam(videoInfoQuery);
        return getSuccessResponseVO(fileList);
    }




}
