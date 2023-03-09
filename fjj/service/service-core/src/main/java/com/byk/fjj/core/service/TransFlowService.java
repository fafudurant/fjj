package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.bo.TransFlowBO;
import com.byk.fjj.core.pojo.entity.TransFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 交易流水表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-7
 */
public interface TransFlowService extends IService<TransFlow> {

    //记录账号流水
    void saveTransFlow(TransFlowBO transFlowBO);

    //判断流水如果存在，则从业务方法中直接退出
    boolean isSaveTransFlow(String agentBillNo);

    //获取列表
    List<TransFlow> selectByUserId(Long userId);
}
