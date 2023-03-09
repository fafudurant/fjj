package com.byk.fjj.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.Lend;
import com.byk.fjj.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-12-4
 */
@Api(tags = "标的")
@Slf4j
@RestController
@RequestMapping("/api/core/lend")
public class LendController {

    @Resource
    private LendService lendService;

    //获取标的列表
    @ApiOperation("获取标的列表")
    @GetMapping("/list")
    public Result list() {
        List<Lend> lendList = lendService.selectList1();
        return Result.ok().data("lendList", lendList);
    }
    //标的列表(分页)
    @ApiOperation("标的列表(分页)")
    @GetMapping("/list/{page}/{limit}")
    public Result list(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit) {
        List<Lend> lendList = lendService.selectList(page,limit);
        return Result.ok().data("lendList", lendList);
    }

    //获取标的信息
    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long id) {
        Map<String, Object> lendDetail = lendService.getLendDetail(id);
        return Result.ok().data("lendDetail", lendDetail);
    }

    //计算投资收益
    @ApiOperation("计算投资收益")
    @GetMapping("/getInterestCount/{invest}/{yearRate}/{totalmonth}/{returnMethod}")
    public Result getInterestCount(
            @ApiParam(value = "投资金额", required = true)
            @PathVariable("invest") BigDecimal invest,
            @ApiParam(value = "年化收益", required = true)
            @PathVariable("yearRate")BigDecimal yearRate,
            @ApiParam(value = "期数", required = true)
            @PathVariable("totalmonth")Integer totalmonth,
            @ApiParam(value = "还款方式", required = true)
            @PathVariable("returnMethod")Integer returnMethod) {
        BigDecimal  interestCount = lendService.getInterestCount(invest, yearRate, totalmonth, returnMethod);
        return Result.ok().data("interestCount", interestCount);
    }
}

