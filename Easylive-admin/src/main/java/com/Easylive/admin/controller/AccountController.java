package com.Easylive.admin.controller;

import com.Easylive.component.RedisComponent;
import com.Easylive.entity.config.AppConfig;
import com.Easylive.entity.constants.Constants;
import com.Easylive.entity.dto.TokenUserInfoDto;
import com.Easylive.entity.vo.ResponseVO;
import com.Easylive.exception.BusinessException;
import com.Easylive.service.UserInfoService;
import com.Easylive.utils.StringTools;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private AppConfig appConfig;
    /**
     * 验证码
     */
    @RequestMapping(value = "/checkCode")
    public ResponseVO checkCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
//      从图形验证码中获取验证码的答案
        String code = captcha.text();
//      将这个验证码内容写入redis缓存，并且返回uuid
        String checkCodeKey = redisComponent.saveCheckCode(code);
        String checkCodeBase64 = captcha.toBase64();
//      使用一个mapper来存放两个内容
        Map<String, String> result = new HashMap<>();
        result.put("checkCode", checkCodeBase64);
        result.put("checkCodeKey", checkCodeKey);
        return getSuccessResponseVO(result);
    }

    @RequestMapping(value = "/login")
    public ResponseVO login(HttpServletRequest request,
                            HttpServletResponse response,
                            @NotEmpty String account,
                            @NotEmpty String password, @NotEmpty String checkCode,
                            @NotEmpty String checkCodeKey) {
        try {
            if (!checkCode.equalsIgnoreCase(redisComponent.getCheckCode(checkCodeKey))) {
                throw new BusinessException("图片验证码不正确");
            }
            if (!account.equals(appConfig.getAdminAccount()) || !password.equals(StringTools.encodeByMD5(appConfig.getAdminPassword()))) {
                throw new BusinessException("账号或者密码错误");
            }
            String token = redisComponent.saveTokenInfo4Admin(account);
            saveToken2Cookie(response, token);
            return getSuccessResponseVO(account);
        } finally {
            redisComponent.cleanCheckCode(checkCodeKey);
            Cookie[] cookies = request.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(Constants.TOKEN_ADMIN)) {
                        token = cookie.getValue();
                    }
                }
            }
            if (!StringTools.isEmpty(token)) {
                redisComponent.cleanToken4Admin(token);
            }
        }
    }

    @RequestMapping(value = "/logout")
    public ResponseVO logout(HttpServletResponse response) {
        cleanCookie(response);
        return getSuccessResponseVO(null);
    }

}