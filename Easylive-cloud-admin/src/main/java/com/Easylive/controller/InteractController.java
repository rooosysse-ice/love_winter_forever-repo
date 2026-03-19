package com.Easylive.controller;

import com.Easylive.api.consumer.InteractClient;
import com.Easylive.entity.vo.ResponseVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/interact")
@Validated
public class InteractController extends ABaseController {

    @Resource
    private InteractClient interactClient;


    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(Integer pageNo, String videoNameFuzzy) {
        return getSuccessResponseVO(interactClient.loadDanmu(pageNo, videoNameFuzzy));
    }


    @RequestMapping("/delDanmu")
    public ResponseVO delDanmu(@NotNull Integer danmuId) {
        interactClient.delDanmu(danmuId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadComment")
    public ResponseVO loadComment(Integer pageNo, String videoNameFuzzy) {
        return getSuccessResponseVO(interactClient.loadComment(pageNo, videoNameFuzzy));
    }

    @RequestMapping("/delComment")
    public ResponseVO delComment(@NotNull Integer commentId) {
        interactClient.delComment(commentId);
        return getSuccessResponseVO(null);
    }
}
