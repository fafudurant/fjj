package com.byk.fjj.core.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.Borrower;
import com.byk.fjj.core.pojo.vo.BorrowerApprovalVO;
import com.byk.fjj.core.pojo.vo.BorrowerDetailVO;
import com.byk.fjj.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author byk
 */
@Api(tags = "借款人管理")
@Slf4j
@RestController
@RequestMapping("/admin/core/borrower")
public class AdminBorrowerController {

    @Resource
    private BorrowerService borrowerService;

    //获取借款人分页列表
    @ApiOperation("获取借款人分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询关键字", required = false)
            @RequestParam String keyword) {
        Page<Borrower> pageParam = new Page<>(page, limit);
        IPage<Borrower> pageModel = borrowerService.listPage(pageParam,keyword);
        return Result.ok().data("pageModel",pageModel);
    }

    //获取借款人信息
    @ApiOperation("获取借款人信息")
    @GetMapping("/show/{id}")
    public Result show(
            @ApiParam(value = "借款人id", required = true)
            @PathVariable Long id) {
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(id);
        return Result.ok().data("borrowerDetailVO", borrowerDetailVO);
    }

    //借款额度审批
    @ApiOperation("借款额度审批")
    @PostMapping("/approval")
    public Result approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO) {
        borrowerService.approval(borrowerApprovalVO);
        return Result.ok().message("审批完成");
    }
}
