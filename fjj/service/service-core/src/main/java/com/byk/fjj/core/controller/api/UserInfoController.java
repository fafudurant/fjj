package com.byk.fjj.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.common.result.Result;
import com.byk.common.util.RegexValidateUtils;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.vo.LoginVO;
import com.byk.fjj.core.pojo.vo.RegisterVO;
import com.byk.fjj.core.pojo.vo.UserIndexVO;
import com.byk.fjj.core.pojo.vo.UserInfoVO;
import com.byk.fjj.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-23
 */
@Api(tags = "会员接口")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/api/core/userInfo")
public class UserInfoController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserInfoService userInfoService;

    //会员注册
    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVO registerVO){
        String mobile = registerVO.getMobile();
        String password = registerVO.getPassword();
        String code = registerVO.getCode();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(code,ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        //校验验证码是否正确
        String codeGen = (String)redisTemplate.opsForValue().get("fjj:sms:code:" + mobile);
        Assert.equals(code,codeGen, ResponseEnum.CODE_ERROR);
        //注册
        userInfoService.register(registerVO);
        return Result.ok().message("注册成功");
    }

    //会员登录
    @ApiOperation("会员登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVO loginVO, HttpServletRequest request){
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Assert.notEmpty(mobile,ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);
        String ip = request.getRemoteAddr();
        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);
        return Result.ok().data("userInfo",userInfoVO);
    }

    //校验令牌
    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        boolean result = JwtHelper.checkToken(token);
        if(result){
            return Result.ok();
        }else {
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    //校验手机号是否注册
    @ApiOperation("校验手机号是否注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return userInfoService.checkMobile(mobile);
    }

    //获取个人空间用户信息
    @ApiOperation("获取个人空间用户信息")
    @GetMapping("/auth/getIndexUserInfo")
    public Result getIndexUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        UserIndexVO userIndexVO = userInfoService.getIndexUserInfo(userId);
        return Result.ok().data("userIndexVO", userIndexVO);
    }
}

