package com.byk.fjj.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.vo.UserBindVO;
import com.byk.fjj.core.service.UserBindService;
import com.byk.fjj.core.zjtg.RequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-26
 */
@Api(tags = "会员账号绑定")
@Slf4j
@RestController
@RequestMapping("/api/core/userBind")
public class UserBindController {

    @Resource
    private UserBindService userBindService;

    //账户绑定提交数据
    @ApiOperation("账户绑定提交数据")
    @PostMapping("/auth/bind")
    public Result bind(@RequestBody UserBindVO userBindVO, HttpServletRequest request) {
        //从header中获取token,并对token进行校验，确保用户已登录,并才token中获取userId
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        //根据userId做账号绑定,生成一个动态表单的字符串
        String formStr = userBindService.commitBindUser(userBindVO,userId);
        return Result.ok().data("formStr",formStr);
    }

    //账户绑定异步回调
    @ApiOperation("账户绑定异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户账号绑定异步回调：" + JSON.toJSONString(paramMap));
        //校验签名
        if(!RequestHelper.isSignEquals(paramMap)) {
            log.error("用户账号绑定异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        //修改绑定状态
        log.info("验签成功！开始账号绑定");
        userBindService.notify(paramMap);
        return "success";
    }
}

