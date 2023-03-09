package com.byk.fjj.core.controller.api;

import com.baomidou.mybatisplus.extension.api.R;
import com.byk.common.result.Result;
import com.byk.fjj.core.pojo.entity.Dict;
import com.byk.fjj.core.service.DictService;
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
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author byk
 * @since 2022-11-28
 */
@Api(tags = "数据字典")
@Slf4j
@RestController
@RequestMapping("/api/core/dict")
public class DictController {

    @Resource
    private DictService dictService;

    //根据dictCode获取下级节点
    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(
            @ApiParam(value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok().data("dictList",list);
    }
}
