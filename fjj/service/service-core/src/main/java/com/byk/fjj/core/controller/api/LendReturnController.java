package com.byk.fjj.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.entity.LendItemReturn;
import com.byk.fjj.core.pojo.entity.LendReturn;
import com.byk.fjj.core.service.LendReturnService;
import com.byk.fjj.core.zjtg.RequestHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-12-19
 */
@Api(tags = "还款计划")
@Slf4j
@RestController
@RequestMapping("/api/core/lendReturn")
public class LendReturnController {

    @Resource
    private LendReturnService lendReturnService;

    //获取列表
    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public Result list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        List<LendReturn> list = lendReturnService.selectByLendId(lendId);
        return Result.ok().data("list", list);
    }

    //用户还款
    @ApiOperation("用户还款")
    @PostMapping("/auth/commitReturn/{lendReturnId}")
    public Result commitReturn(
            @ApiParam(value = "还款计划id", required = true)
            @PathVariable Long lendReturnId, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        String formStr = lendReturnService.commitReturn(lendReturnId, userId);
        return Result.ok().data("formStr", formStr);
    }

    //还款异步回调
    @ApiOperation("还款异步回调")
    @PostMapping("/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("还款异步回调：" + JSON.toJSONString(paramMap));
        //校验签名
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendReturnService.notify(paramMap);
            } else {
                log.info("还款异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("还款异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }

    //获取所有列表
    @ApiOperation("获取列表")
    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        List<LendReturn> list = lendReturnService.selectByUserId(userId);
        return Result.ok().data("list", list);
    }
}

