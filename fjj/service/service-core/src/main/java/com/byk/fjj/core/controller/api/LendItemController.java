package com.byk.fjj.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.base.util.JwtHelper;
import com.byk.fjj.core.pojo.entity.LendItem;
import com.byk.fjj.core.pojo.entity.TransFlow;
import com.byk.fjj.core.pojo.vo.InvestVO;
import com.byk.fjj.core.service.LendItemService;
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
 * 标的出借记录表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-12-8
 */
@Api(tags = "标的的投资")
@Slf4j
@RestController
@RequestMapping("/api/core/lendItem")
public class LendItemController {

    @Resource
    LendItemService lendItemService;

    //会员投资提交数据
    @ApiOperation("会员投资提交数据")
    @PostMapping("/auth/commitInvest")
    public Result commitInvest(@RequestBody InvestVO investVO, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUserName(token);
        investVO.setInvestUserId(userId);
        investVO.setInvestName(userName);
        //构建充值自动提交表单
        String formStr = lendItemService.commitInvest(investVO);
        return Result.ok().data("formStr", formStr);
    }

    //会员投资异步回调
    @ApiOperation("会员投资异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        log.info("用户投资异步回调：" + JSON.toJSONString(paramMap));
        //校验签名 P2pInvestNotifyVo
        if(RequestHelper.isSignEquals(paramMap)) {
            if("0001".equals(paramMap.get("resultCode"))) {
                lendItemService.notify(paramMap);
            } else {
                log.info("用户投资异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("用户投资异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }

    //获取列表
    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public Result list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        List<LendItem> list = lendItemService.selectByLendId(lendId);
        return Result.ok().data("list", list);
    }

    //获取所有列表
    @ApiOperation("获取列表")
    @GetMapping("/list")
    public Result list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        List<LendItem> list = lendItemService.selectByUserId(userId);
        return Result.ok().data("list", list);
    }
}

