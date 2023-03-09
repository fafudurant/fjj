package com.byk.fjj.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.byk.fjj.core.pojo.entity.LendItemReturn;
import com.byk.fjj.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-30
 */
@Api(tags = "借款信息")
@Slf4j
@RestController
@RequestMapping("/api/core/borrowInfo")
public class BorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    //获取借款额度
    @ApiOperation("获取借款额度")
    @GetMapping("/auth/getBorrowAmount")
    public Result getBorrowAmount(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        BigDecimal borrowAmount = borrowInfoService.getBorrowAmount(userId);
        return Result.ok().data("borrowAmount", borrowAmount);
    }

    //提交借款申请
    @ApiOperation("提交借款申请")
    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowInfo borrowInfo, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        borrowInfoService.saveBorrowInfo(borrowInfo, userId);
        return Result.ok().message("提交成功");
    }

    //获取借款申请审批状态
    @ApiOperation("获取借款申请审批状态")
    @GetMapping("/auth/getBorrowInfoStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        Integer status = borrowInfoService.getStatusByUserId(userId);
        return Result.ok().data("borrowInfoStatus", status);
    }

    //获取所有列表
    @ApiOperation("获取列表")
    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        List<BorrowInfo> list = borrowInfoService.selectByUserId(userId);
        return Result.ok().data("list", list);
    }
}

