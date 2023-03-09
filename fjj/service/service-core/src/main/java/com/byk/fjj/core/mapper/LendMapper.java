package com.byk.fjj.core.mapper;

import com.byk.fjj.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 标的准备表 Mapper 接口
 * </p>
 *
 * @author byk
 * @since 2022-12-3
 */
public interface LendMapper extends BaseMapper<Lend> {

    //标的列表(分页)
    List<Lend> selectLendPageList(Long page, Long limit);
}
