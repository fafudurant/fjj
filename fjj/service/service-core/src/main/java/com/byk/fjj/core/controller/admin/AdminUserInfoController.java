package com.byk.fjj.core.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.common.result.Result;
import com.byk.common.util.RegexValidateUtils;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.entity.UserInfo;
import com.byk.fjj.core.pojo.query.UserInfoQuery;
import com.byk.fjj.core.pojo.vo.LoginVO;
import com.byk.fjj.core.pojo.vo.RegisterVO;
import com.byk.fjj.core.pojo.vo.UserInfoVO;
import com.byk.fjj.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-24
 */
@Api(tags = "会员管理")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/userInfo")
public class AdminUserInfoController {

    @Resource
    private UserInfoService userInfoService;

    //获取会员分页列表
    @ApiOperation("获取会员分页列表")
    @GetMapping("/list/{page}/{limit}")
    public Result listPage(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "查询对象", required = false)
            UserInfoQuery userInfoQuery) {
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pageModel =  userInfoService.listPage(pageParam,userInfoQuery);
        return Result.ok().data("pageModel",pageModel);
    }

    //锁定和解锁
    @ApiOperation("锁定和解锁")
    @PutMapping("/lock/{id}/{status}")
    public Result lock(
            @ApiParam(value = "用户id", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "锁定状态（0：锁定 1：解锁）", required = true)
            @PathVariable("status") Integer status){
        userInfoService.lock(id,status);
        return Result.ok().message(status==1?"解锁成功":"锁定成功");
    }
}

