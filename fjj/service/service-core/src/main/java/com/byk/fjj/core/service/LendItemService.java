package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.LendItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.entity.TransFlow;
import com.byk.fjj.core.pojo.vo.InvestVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-8
 */
public interface LendItemService extends IService<LendItem> {

    //构建充值自动提交表单
    String commitInvest(InvestVO investVO);

    //会员投资异步回调
    void notify(Map<String, Object> paramMap);

    //获取投资列表信息
    List<LendItem> selectByLendId(Long lendId, Integer status);

    //获取列表
    List<LendItem> selectByLendId(Long lendId);

    //获取所有列表
    List<LendItem> selectByUserId(Long userId);
}
