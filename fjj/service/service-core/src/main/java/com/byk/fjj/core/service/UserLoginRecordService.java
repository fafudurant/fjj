package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-24
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    //获取会员登录日志列表
    List<UserLoginRecord> listTop50(Long userId);
}
