package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.LendItemReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-19
 */
public interface LendItemReturnService extends IService<LendItemReturn> {

    //获取列表
    List<LendItemReturn> selectByLendId(Long lendId, Long userId);

    //添加还款明细
    List<Map<String, Object>> addReturnDetail(Long lendReturnId);

    //根据还款id获取回款列表
    List<LendItemReturn> selectLendItemReturnList(Long lendReturnId);

    //获取所有列表
    List<LendItemReturn> selectByUserId(Long userId);
}
