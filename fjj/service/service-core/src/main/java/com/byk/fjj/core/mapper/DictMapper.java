package com.byk.fjj.core.mapper;

import com.byk.fjj.core.pojo.dto.ExcelDictDTO;
import com.byk.fjj.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author byk
 * @since 2022-11-15
 */
public interface DictMapper extends BaseMapper<Dict> {

    //批量保存
    void insertBatch(List<ExcelDictDTO> list);
}
