package com.byk.fjj.core.controller.admin;

import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.LendItem;
import com.byk.fjj.core.service.LendItemService;
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
 * 标的的投资
 * @author byk
 */
@Api(tags = "标的的投资")
@Slf4j
@RestController
@RequestMapping("/admin/core/lendItem")
public class AdminLendItemController {

    @Resource
    private LendItemService lendItemService;

    //获取列表
    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public Result list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        List<LendItem> list = lendItemService.selectByLendId(lendId);
        return Result.ok().data("list", list);
    }
}
