package com.byk.fjj.core.controller.api;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.vo.BorrowerVO;
import com.byk.fjj.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-28
 */
@Api(tags = "借款人")
@Slf4j
@RestController
@RequestMapping("/api/core/borrower")
public class BorrowerController {

    @Resource
    private BorrowerService borrowerService;

    //保存借款人信息
    @ApiOperation("保存借款人信息")
    @PostMapping("/auth/save")
    public Result save(@RequestBody BorrowerVO borrowerVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        borrowerService.saveBorrowerVOByUserId(borrowerVO,userId);
        return Result.ok().message("信息提交成功");
    }

    //获取借款人认证状态
    @ApiOperation("获取借款人认证状态")
    @GetMapping("/auth/getBorrowerStatus")
    public Result getBorrowerStatus(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        Integer status = borrowerService.getStatusByUserId(userId);
        return Result.ok().data("borrowerStatus", status);
    }
}

