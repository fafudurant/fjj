package com.byk.fjj.core.mapper;

import com.byk.fjj.core.pojo.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 用户账户 Mapper 接口
 * </p>
 *
 * @author byk
 * @since 2022-12-7
 */
public interface UserAccountMapper extends BaseMapper<UserAccount> {

    //同步账号数据
    void updateAccount(
            @Param("bindCode")String bindCode,
            @Param("amount")BigDecimal amount,
            @Param("freezeAmount")BigDecimal freezeAmount);
}
