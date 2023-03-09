package com.byk.fjj.core.controller.admin;


import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.UserLoginRecord;
import com.byk.fjj.core.service.UserLoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户登录记录表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-24
 */
@Api(tags = "会员登录日志接口")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/userLoginRecord")
public class AdminUserLoginRecordController {

    @Resource
    private UserLoginRecordService userLoginRecordService;

    //获取会员登录日志列表
    @ApiOperation("获取会员登录日志列表")
    @GetMapping("/listTop50/{userId}")
    public Result listTop50(
            @ApiParam(value = "用户id", required = true)
            @PathVariable Long userId) {
        List<UserLoginRecord> userLoginRecordList = userLoginRecordService.listTop50(userId);
        return Result.ok().data("list", userLoginRecordList);
    }
}

