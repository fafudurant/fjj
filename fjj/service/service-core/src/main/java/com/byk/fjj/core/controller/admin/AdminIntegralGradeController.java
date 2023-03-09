package com.byk.fjj.core.controller.admin;


import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.IntegralGrade;
import com.byk.fjj.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-15
 */
@Api(tags = "积分等级管理")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    //获取积分等级列表
    @ApiOperation("获取积分等级列表")
    @GetMapping("/list")
    public Result listAll(){
        log.info("hi this is log info");
        log.warn("hi this is log warn");
        log.error("hi this is log error");
        List<IntegralGrade> list = integralGradeService.list();
        return Result.ok().data("list",list).message("获取列表成功");
    }

    //根据id删除积分等级
    @ApiOperation("根据id删除积分等级")
    @DeleteMapping("/remove/{id}")
    public Result removeById(
            @ApiParam(value = "数据id",example = "100",required = true)
            @PathVariable Long id){
        boolean result = integralGradeService.removeById(id);
        if (result){
            return Result.ok().message("删除成功");
        }else {
            return Result.error().message("删除失败");
        }
    }

    //新增积分等级
    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
        //if (integralGrade.getBorrowAmount() == null){
        //    throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        //}
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
        boolean result = integralGradeService.save(integralGrade);
        if(result){
            return Result.ok().message("保存成功");
        }else {
            return Result.error().message("保存失败");
        }
    }

    //根据id获取积分等级
    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public Result getById(
            @ApiParam(value = "数据id",required = true,example = "1")
            @PathVariable Long id){
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if(integralGrade != null){
            return Result.ok().data("record",integralGrade);
        }else {
            return Result.error().message("数据获取失败");
        }
    }

    //更新积分等级
    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public Result updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
        boolean result = integralGradeService.updateById(integralGrade);
        if(result){
            return Result.ok().message("更新成功");
        }else {
            return Result.error().message("更新失败");
        }
    }
}

