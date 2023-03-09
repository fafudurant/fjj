package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byk.fjj.core.pojo.entity.UserLoginRecord;
import com.byk.fjj.core.mapper.UserLoginRecordMapper;
import com.byk.fjj.core.service.UserLoginRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-11-24
 */
@Service
public class UserLoginRecordServiceImpl extends ServiceImpl<UserLoginRecordMapper, UserLoginRecord> implements UserLoginRecordService {

    //获取会员登录日志列表
    @Override
    public List<UserLoginRecord> listTop50(Long userId) {
        QueryWrapper<UserLoginRecord> userLoginRecordQueryWrapper = new QueryWrapper<>();
        userLoginRecordQueryWrapper
                .eq("user_id",userId)
                .orderByDesc("id")
                .last("limit 50");
        List<UserLoginRecord> userLoginRecordList = baseMapper.selectList(userLoginRecordQueryWrapper);
        return userLoginRecordList;
    }
}
