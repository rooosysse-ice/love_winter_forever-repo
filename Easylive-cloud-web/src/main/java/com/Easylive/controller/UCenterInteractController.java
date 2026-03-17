package com.Easylive.controller;

import com.Easylive.annotation.GlobalInterceptor;
import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.po.VideoInfoPost;
import com.Easylive.entity.query.VideoInfoPostQuery;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.service.VideoInfoPostService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Validated
@RequestMapping("/ucenter")
public class UCenterInteractController extends ABaseController {

    @Resource
    private VideoInfoPostService videoInfoPostService;

    @RequestMapping("/loadAllVideo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO loadAllVideo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        VideoInfoPostQuery videoInfoPostQuery = new VideoInfoPostQuery();
        videoInfoPostQuery.setUserId(tokenUserInfoDto.getUserId());
        videoInfoPostQuery.setOrderBy("create_time desc");
        List<VideoInfoPost> videoInfoPostList = videoInfoPostService.findListByParam(videoInfoPostQuery);
        return getSuccessResponseVO(videoInfoPostList);
    }

}
