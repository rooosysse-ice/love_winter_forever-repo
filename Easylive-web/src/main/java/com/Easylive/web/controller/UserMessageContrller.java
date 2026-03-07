package com.Easylive.web.controller;

import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.dto.UserMessageCountDto;
import com.Easylive.entity.enums.MessageReadTypeEnum;
import com.Easylive.entity.po.UserMessage;
import com.Easylive.entity.query.UserMessageQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.service.UserMessageService;
import com.Easylive.web.annotation.GlobalInterceptor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping("/message")
public class UserMessageContrller extends ABaseController {

    @Resource
    private UserMessageService userMessageService;

    @RequestMapping("/getNoReadCount")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getNoReadCount() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        if (tokenUserInfoDto == null) {
            return getSuccessResponseVO(0);
        }
        UserMessageQuery messageQuery = new UserMessageQuery();
        messageQuery.setUserId(tokenUserInfoDto.getUserId());
        messageQuery.setReadType(MessageReadTypeEnum.NO_READ.getType());
        Integer count = userMessageService.findCountByParam(messageQuery);
        return getSuccessResponseVO(count);
    }

}
