package com.Easylive.controller;

import com.Easylive.annotation.GlobalInterceptor;
import com.Easylive.component.RedisComponent;
import com.Easylive.entity.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/online")
public class OnlineController extends ABaseController {

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/reportVideoPlayOnline")
    @GlobalInterceptor
    public ResponseVO reportVideoPlayOnline(@NotEmpty String fileId, String deviceId) {
        Integer count = redisComponent.reportVideoPlayOnline(fileId, deviceId);
        return getSuccessResponseVO(count);
    }
}
