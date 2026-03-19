package com.Easylive.api.provider;

import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.po.UserInfo;
import com.Easylive.entity.query.UserInfoQuery;
import com.Easylive.entity.vo.PaginationResultVO;
import com.Easylive.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping(Constants.INNER_API_PREFIX + "/user/")
public class UserInfoApi {


    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/updateCoinCountInfo")
    public Integer updateCoinCountInfo(@NotEmpty String userId, @NotNull Integer count) {
        return userInfoService.updateCoinCountInfo(userId, count);
    }


    @RequestMapping("/getUserInfoByUserId")
    public UserInfo getUserInfoByUserId(@NotEmpty String userId) {
        return userInfoService.getUserInfoByUserId(userId);
    }

    @RequestMapping("/loadUser")
    public PaginationResultVO loadUser(@RequestBody UserInfoQuery userInfoQuery) {
        userInfoQuery.setOrderBy("join_time desc");
        return userInfoService.findListByPage(userInfoQuery);
    }

    @RequestMapping("/changeStatus")
    public void changeStatus(@RequestParam String userId, @RequestParam Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setStatus(status);
        userInfoService.updateUserInfoByUserId(userInfo, userId);
    }
}
