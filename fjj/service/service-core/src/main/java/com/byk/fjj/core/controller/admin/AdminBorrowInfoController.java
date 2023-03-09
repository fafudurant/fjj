package com.byk.fjj.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.byk.fjj.core.pojo.vo.BorrowInfoApprovalVO;
import com.byk.fjj.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-12-1
 */
@Api(tags = "借款管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/borrowInfo")
public class AdminBorrowInfoController {

    @Resource
    private BorrowInfoService borrowInfoService;

    //借款信息列表(分页)
    @ApiOperation("借款信息列表")
    @GetMapping("/list/{page}/{limit}")
    public Result list(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit) {
        List<BorrowInfo> borrowInfoList = borrowInfoService.selectList(page,limit);
        return Result.ok().data("list", borrowInfoList);
    }
    //借款信息列表
    //@ApiOperation("借款信息列表")
    //@GetMapping("/list")
    //public Result list() {
    //    List<BorrowInfo>  borrowInfoList = borrowInfoService.selectList();
    //    return Result.ok().data("list", borrowInfoList);
    //}

    //获取借款信息
    @ApiOperation("获取借款信息")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "借款id", required = true)
            @PathVariable Long id) {
        Map<String, Object> borrowInfoDetail = borrowInfoService.getBorrowInfoDetail(id);
        return Result.ok().data("borrowInfoDetail", borrowInfoDetail);
    }

    //审批借款信息
    @ApiOperation("审批借款信息")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO) {
        borrowInfoService.approval(borrowInfoApprovalVO);
        return Result.ok().message("审批完成");
    }
}

