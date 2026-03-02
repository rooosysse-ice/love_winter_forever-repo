package com.Easylive.web.controller;

import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.enums.PageSize;
import com.Easylive.entity.enums.UserActionTypeEnum;
import com.Easylive.entity.po.UserInfo;
import com.Easylive.entity.query.UserActionQuery;
import com.Easylive.entity.query.UserFocusQuery;
import com.Easylive.entity.query.VideoInfoQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.entity.vo.UserInfoVO;
import com.Easylive.service.UserActionService;
import com.Easylive.service.UserFocusService;
import com.Easylive.service.UserInfoService;
import com.Easylive.service.VideoInfoService;
import com.Easylive.utils.CopyTools;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@Validated
@RequestMapping("/uhome")
public class UHomeController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private VideoInfoService videoInfoService;

    @Resource
    private UserFocusService userFocusService;

    @Resource
    private UserActionService userActionService;

    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(@NotEmpty String userId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = userInfoService.getUserDetailInfo(null == tokenUserInfoDto ? null : tokenUserInfoDto.getUserId(), userId);
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        return getSuccessResponseVO(userInfoVO);
    }


    @RequestMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(@NotEmpty @Size(max = 20) String nickName,
                                     @NotEmpty @Size(max = 100) String avatar,
                                     @NotNull Integer sex, String birthday,
                                      @Size(max = 150) String school,
                                     @Size(max = 80) String personIntroduction,
                                     @Size(max = 300) String noticeInfo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfo.setNickName(nickName);
        userInfo.setAvatar(avatar);
        userInfo.setSex(sex);
        userInfo.setBirthday(birthday);
        userInfo.setSchool(school);
        userInfo.setPersonIntroduction(personIntroduction);
        userInfo.setNoticeInfo(noticeInfo);
        userInfoService.updateUserInfo(userInfo, tokenUserInfoDto);

        return getSuccessResponseVO(null);
    }

    @RequestMapping("/saveTheme")
    public ResponseVO saveTheme(Integer theme) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        UserInfo userInfo = new UserInfo();
        userInfo.setTheme(theme);
        userInfoService.updateUserInfoByUserId(userInfo, tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }



}
