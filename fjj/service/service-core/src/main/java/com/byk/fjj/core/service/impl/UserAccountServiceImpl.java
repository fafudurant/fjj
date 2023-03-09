package com.byk.fjj.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.fjj.base.dto.SmsDTO;
import com.byk.fjj.core.enums.TransTypeEnum;
import com.byk.fjj.core.mapper.UserInfoMapper;
import com.byk.fjj.core.pojo.bo.TransFlowBO;
import com.byk.fjj.core.pojo.entity.UserAccount;
import com.byk.fjj.core.mapper.UserAccountMapper;
import com.byk.fjj.core.pojo.entity.UserInfo;
import com.byk.fjj.core.service.TransFlowService;
import com.byk.fjj.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byk.fjj.core.service.UserBindService;
import com.byk.fjj.core.service.UserInfoService;
import com.byk.fjj.core.util.LendNoUtils;
import com.byk.fjj.core.zjtg.FormHelper;
import com.byk.fjj.core.zjtg.RequestHelper;
import com.byk.fjj.core.zjtg.ZjtgConst;
import com.byk.fjj.rabbitutil.constant.MQConst;
import com.byk.fjj.rabbitutil.service.MQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-12-7
 */
@Service
@Slf4j
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private TransFlowService transFlowService;

    @Resource
    private UserBindService userBindService;

    @Resource
    private UserAccountService userAccountService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private MQService mqService;

    //充值
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String commitCharge(BigDecimal chargeAmt, Long userId) {
        //获取充值人绑定协议号
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();
        //判断账户绑定状态
        Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", ZjtgConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("chargeAmt", chargeAmt);
        paramMap.put("feeAmt", new BigDecimal("0"));
        //检查常量是否正确
        paramMap.put("notifyUrl", ZjtgConst.RECHARGE_NOTIFY_URL);
        paramMap.put("returnUrl", ZjtgConst.RECHARGE_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);
        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(ZjtgConst.RECHARGE_URL, paramMap);
        return formStr;
    }

    //同步账号数据
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String notify(Map<String, Object> paramMap) {
        log.info("充值成功：" + JSONObject.toJSONString(paramMap));
        //商户充值订单号
        String agentBillNo = (String)paramMap.get("agentBillNo");
        //幂等性判断？标准= 判断交易流水是否存在
        boolean isSave = transFlowService.isSaveTransFlow(agentBillNo);
        if(isSave){
            log.warn("幂等性返回");
            return "success";
        }
        //账号处理
        //充值人绑定协议号
        String bindCode = (String)paramMap.get("bindCode");
        //充值金额
        String chargeAmt = (String)paramMap.get("chargeAmt");
        //优化
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));
        //记录账号流水
        //商户充值订单号
        //String agentBillNo = (String)paramMap.get("agentBillNo");
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(chargeAmt),
                TransTypeEnum.RECHARGE,
                "充值");
        transFlowService.saveTransFlow(transFlowBO);
        //发消息
        log.info("发消息");
        String mobile = userInfoService.getMobileByBindCode(bindCode);
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setMobile(mobile);
        smsDTO.setMessage("充值成功");
        mqService.sendMessage(MQConst.EXCHANGE_TOPIC_SMS, MQConst.ROUTING_SMS_ITEM, smsDTO);
        return "success";
    }

    //查询账户余额
    @Override
    public BigDecimal getAccount(Long userId) {
        //根据userId查找用户账户
        QueryWrapper<UserAccount> userAccountQueryWrapper = new QueryWrapper<>();
        userAccountQueryWrapper.eq("user_id", userId);
        UserAccount userAccount = baseMapper.selectOne(userAccountQueryWrapper);
        BigDecimal amount = userAccount.getAmount();
        return amount;
    }

    //用户提现
    @Override
    public String commitWithdraw(BigDecimal fetchAmt, Long userId) {
        //账户可用余额充足：当前用户的余额 >= 当前用户的提现金额
        BigDecimal amount = userAccountService.getAccount(userId);//获取当前用户的账户余额
        Assert.isTrue(amount.doubleValue() >= fetchAmt.doubleValue(),
                ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);
        String bindCode = userBindService.getBindCodeByUserId(userId);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", ZjtgConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getWithdrawNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("fetchAmt", fetchAmt);
        paramMap.put("feeAmt", new BigDecimal(0));
        paramMap.put("notifyUrl", ZjtgConst.WITHDRAW_NOTIFY_URL);
        paramMap.put("returnUrl", ZjtgConst.WITHDRAW_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);
        //构建自动提交表单
        String formStr = FormHelper.buildForm(ZjtgConst.WITHDRAW_URL, paramMap);
        return formStr;
    }

    //用户提现异步回调
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyWithdraw(Map<String, Object> paramMap) {
        log.info("提现成功");
        String agentBillNo = (String)paramMap.get("agentBillNo");
        boolean result = transFlowService.isSaveTransFlow(agentBillNo);
        if(result){
            log.warn("幂等性返回");
            return;
        }
        String bindCode = (String)paramMap.get("bindCode");
        String fetchAmt = (String)paramMap.get("fetchAmt");
        //根据用户账户修改账户金额
        baseMapper.updateAccount(bindCode, new BigDecimal("-" + fetchAmt), new BigDecimal(0));
        //增加交易流水
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(fetchAmt),
                TransTypeEnum.WITHDRAW,
                "提现");
        transFlowService.saveTransFlow(transFlowBO);
    }
}
