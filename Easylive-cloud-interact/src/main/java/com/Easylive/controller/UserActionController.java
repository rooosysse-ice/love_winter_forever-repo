package com.Easylive.controller;

import com.Easylive.annotation.RecordUserMessage;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.enums.MessageTypeEnum;
import com.Easylive.entity.po.UserAction;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.service.UserActionService;
import com.Easylive.annotation.GlobalInterceptor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * 用户行为 点赞、评论 Controller
 */
@RestController("userActionController")
@RequestMapping("/userAction")
public class UserActionController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @RequestMapping("doAction")
    @GlobalInterceptor(checkLogin = true)
    @RecordUserMessage(messageType = MessageTypeEnum.LIKE)
    public ResponseVO doAction(@NotEmpty String videoId,
                               @NotEmpty Integer actionType,
                               @Max(2) @Min(1) Integer actionCount,
                               Integer commentId) {
        UserAction userAction = new UserAction();
        userAction.setUserId(getTokenUserInfoDto().getUserId());
        userAction.setVideoId(videoId);
        userAction.setActionType(actionType);
        actionCount = actionCount == null ? Constants.ONE : actionCount;
        userAction.setActionCount(actionCount);
        commentId = commentId == null ? 0 : commentId;
        userAction.setCommentId(commentId);
        userActionService.saveAction(userAction);
        return getSuccessResponseVO(null);
    }

}