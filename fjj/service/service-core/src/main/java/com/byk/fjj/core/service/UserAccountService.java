package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-7
 */
public interface UserAccountService extends IService<UserAccount> {

    //充值
    String commitCharge(BigDecimal chargeAmt, Long userId);

    //同步账号数据
    String notify(Map<String, Object> paramMap);

    //查询账户余额
    BigDecimal getAccount(Long userId);

    //用户提现
    String commitWithdraw(BigDecimal fetchAmt, Long userId);

    //用户提现异步回调
    void notifyWithdraw(Map<String, Object> paramMap);
}
