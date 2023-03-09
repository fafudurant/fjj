package com.byk.fjj.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.entity.LendItemReturn;
import com.byk.fjj.core.service.LendItemReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 标的出借回款记录表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-12-19
 */
@Api(tags = "回款计划")
@Slf4j
@RestController
@RequestMapping("/api/core/lendItemReturn")
public class LendItemReturnController {

    @Resource
    private LendItemReturnService lendItemReturnService;

    //获取列表
    @ApiOperation("获取列表")
    @GetMapping("/auth/list/{lendId}")
    public Result list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        List<LendItemReturn> list = lendItemReturnService.selectByLendId(lendId, userId);
        return Result.ok().data("list", list);
    }

    //获取所有列表
    @ApiOperation("获取列表")
    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        List<LendItemReturn> list = lendItemReturnService.selectByUserId(userId);
        return Result.ok().data("list", list);
    }
}

