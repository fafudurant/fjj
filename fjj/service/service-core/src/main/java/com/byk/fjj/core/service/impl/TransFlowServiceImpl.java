package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byk.fjj.core.mapper.UserInfoMapper;
import com.byk.fjj.core.pojo.bo.TransFlowBO;
import com.byk.fjj.core.pojo.entity.TransFlow;
import com.byk.fjj.core.mapper.TransFlowMapper;
import com.byk.fjj.core.pojo.entity.UserInfo;
import com.byk.fjj.core.service.TransFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 交易流水表 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-12-7
 */
@Service
public class TransFlowServiceImpl extends ServiceImpl<TransFlowMapper, TransFlow> implements TransFlowService {

    @Resource
    private UserInfoMapper userInfoMapper;

    //记录账号流水
    @Override
    public void saveTransFlow(TransFlowBO transFlowBO) {
        //获取用户基本信息 user_info
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", transFlowBO.getBindCode());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        //存储交易流水数据
        TransFlow transFlow = new TransFlow();
        transFlow.setUserId(userInfo.getId());
        transFlow.setUserName(userInfo.getName());
        transFlow.setTransNo(transFlowBO.getAgentBillNo());
        transFlow.setTransType(transFlowBO.getTransTypeEnum().getTransType());
        transFlow.setTransTypeName(transFlowBO.getTransTypeEnum().getTransTypeName());
        transFlow.setTransAmount(transFlowBO.getAmount());
        transFlow.setMemo(transFlowBO.getMemo());
        baseMapper.insert(transFlow);
    }

    //判断流水如果存在，则从业务方法中直接退出
    @Override
    public boolean isSaveTransFlow(String agentBillNo) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper();
        queryWrapper.eq("trans_no", agentBillNo);
        int count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    //获取列表
    @Override
    public List<TransFlow> selectByUserId(Long userId) {
        QueryWrapper<TransFlow> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .orderByDesc("id");
        return baseMapper.selectList(queryWrapper);
    }
}
