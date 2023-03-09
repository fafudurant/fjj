package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.LendReturn;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-19
 */
public interface LendReturnService extends IService<LendReturn> {

    //获取列表
    List<LendReturn> selectByLendId(Long lendId);

    //用户还款
    String commitReturn(Long lendReturnId, Long userId);

    //还款异步回调
    void notify(Map<String, Object> paramMap);

    //获取所有列表
    List<LendReturn> selectByUserId(Long userId);
}
