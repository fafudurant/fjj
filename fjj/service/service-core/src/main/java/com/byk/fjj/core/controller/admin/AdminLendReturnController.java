package com.byk.fjj.core.controller.admin;

import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.LendReturn;
import com.byk.fjj.core.service.LendReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 还款记录
 * @author byk
 */
@Api(tags = "还款记录")
@Slf4j
@RestController
@RequestMapping("/admin/core/lendReturn")
public class AdminLendReturnController {

    @Resource
    private LendReturnService lendReturnService;

    //获取列表
    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public Result list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        List<LendReturn> list = lendReturnService.selectByLendId(lendId);
        return Result.ok().data("list", list);
    }
}
