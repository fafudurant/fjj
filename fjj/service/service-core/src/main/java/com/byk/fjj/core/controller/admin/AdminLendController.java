package com.byk.fjj.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.byk.fjj.core.pojo.entity.Borrower;
import com.byk.fjj.core.pojo.entity.Lend;
import com.byk.fjj.core.service.LendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 标的管理
 * @author byk
 */
@Api(tags = "标的管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/lend")
public class AdminLendController {

    @Resource
    private LendService lendService;

    //标的列表
    //@ApiOperation("标的列表")
    //@GetMapping("/list")
    //public Result list() {
    //    List<Lend> lendList = lendService.selectList();
    //    return Result.ok().data("list", lendList);
    //}
    //标的列表(分页)
    @ApiOperation("标的列表")
    @GetMapping("/list/{page}/{limit}")
    public Result list(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit) {
        List<Lend> lendList = lendService.selectList(page,limit);
        return Result.ok().data("list", lendList);
    }

    //获取标的信息
    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long id) {
        Map<String, Object> result = lendService.getLendDetail(id);
        return Result.ok().data("lendDetail", result);
    }

    //放款
    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public Result makeLoan(
            @ApiParam(value = "标的id", required = true)
            @PathVariable("id") Long id) {
        lendService.makeLoan(id);
        return Result.ok().message("放款成功");
    }
}
