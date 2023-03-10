package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byk.fjj.core.mapper.LendItemMapper;
import com.byk.fjj.core.mapper.LendMapper;
import com.byk.fjj.core.mapper.LendReturnMapper;
import com.byk.fjj.core.pojo.entity.Lend;
import com.byk.fjj.core.pojo.entity.LendItem;
import com.byk.fjj.core.pojo.entity.LendItemReturn;
import com.byk.fjj.core.mapper.LendItemReturnMapper;
import com.byk.fjj.core.pojo.entity.LendReturn;
import com.byk.fjj.core.service.LendItemReturnService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byk.fjj.core.service.UserBindService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借回款记录表 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-12-19
 */
@Service
public class LendItemReturnServiceImpl extends ServiceImpl<LendItemReturnMapper, LendItemReturn> implements LendItemReturnService {

    @Resource
    private UserBindService userBindService;

    @Resource
    private LendItemMapper lendItemMapper;

    @Resource
    private LendMapper lendMapper;

    @Resource
    private LendReturnMapper lendReturnMapper;

    //获取列表
    @Override
    public List<LendItemReturn> selectByLendId(Long lendId, Long userId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("lend_id", lendId)
                .eq("invest_user_id", userId)
                .orderByAsc("current_period");
        return baseMapper.selectList(queryWrapper);
    }

    //添加还款明细
    @Override
    public List<Map<String, Object>> addReturnDetail(Long lendReturnId) {
        //获取还款记录
        LendReturn lendReturn = lendReturnMapper.selectById(lendReturnId);
        //获取标的信息
        Lend lend = lendMapper.selectById(lendReturn.getLendId());
        //根据还款id获取回款列表
        List<LendItemReturn> lendItemReturnList = this.selectLendItemReturnList(lendReturnId);
        List<Map<String, Object>> lendItemReturnDetailList = new ArrayList<>();
        for(LendItemReturn lendItemReturn : lendItemReturnList) {
            LendItem lendItem = lendItemMapper.selectById(lendItemReturn.getLendItemId());
            String bindCode = userBindService.getBindCodeByUserId(lendItem.getInvestUserId());
            Map<String, Object> map = new HashMap<>();
            //项目编号
            map.put("agentProjectCode", lend.getLendNo());
            //出借编号
            map.put("voteBillNo", lendItem.getLendItemNo());
            //收款人（出借人）
            map.put("toBindCode", bindCode);
            //还款金额
            map.put("transitAmt", lendItemReturn.getTotal());
            //还款本金
            map.put("baseAmt", lendItemReturn.getPrincipal());
            //还款利息
            map.put("benifitAmt", lendItemReturn.getInterest());
            //商户手续费
            map.put("feeAmt", new BigDecimal("0"));
            lendItemReturnDetailList.add(map);
        }
        return lendItemReturnDetailList;
    }

    //根据还款id获取回款列表
    @Override
    public List<LendItemReturn> selectLendItemReturnList(Long lendReturnId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_return_id", lendReturnId);
        List<LendItemReturn> lendItemReturnList = baseMapper.selectList(queryWrapper);
        return lendItemReturnList;
    }

    //获取所有列表
    @Override
    public List<LendItemReturn> selectByUserId(Long userId) {
        QueryWrapper<LendItemReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("invest_user_id", userId)
                .orderByDesc("id");
        return baseMapper.selectList(queryWrapper);
    }
}
