package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.common.exception.Assert;
import com.byk.common.result.ResponseEnum;
import com.byk.fjj.core.enums.BorrowInfoStatusEnum;
import com.byk.fjj.core.enums.BorrowerStatusEnum;
import com.byk.fjj.core.enums.UserBindEnum;
import com.byk.fjj.core.mapper.BorrowerMapper;
import com.byk.fjj.core.mapper.IntegralGradeMapper;
import com.byk.fjj.core.mapper.UserInfoMapper;
import com.byk.fjj.core.pojo.entity.*;
import com.byk.fjj.core.mapper.BorrowInfoMapper;
import com.byk.fjj.core.pojo.vo.BorrowInfoApprovalVO;
import com.byk.fjj.core.pojo.vo.BorrowerDetailVO;
import com.byk.fjj.core.service.BorrowInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byk.fjj.core.service.BorrowerService;
import com.byk.fjj.core.service.DictService;
import com.byk.fjj.core.service.LendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-11-30
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private IntegralGradeMapper integralGradeMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerMapper borrowerMapper;

    @Resource
    private BorrowerService borrowerService;

    @Resource
    private LendService lendService;

    //获取借款额度
    @Override
    public BigDecimal getBorrowAmount(Long userId) {
        //获取用户积分
        UserInfo userInfo = userInfoMapper.selectById(userId);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();
        //根据积分查询借款额度
        QueryWrapper<IntegralGrade> integralGradeQueryWrapper = new QueryWrapper<>();
        integralGradeQueryWrapper
                .le("integral_start", integral)
                .ge("integral_end", integral);
        IntegralGrade integralGrade = integralGradeMapper.selectOne(integralGradeQueryWrapper);
        if(integralGrade == null){
            return new BigDecimal("0");
        }
        return integralGrade.getBorrowAmount();
    }

    //提交借款申请
    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {
        //判断用户绑定状态
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //判断借款人额度申请状态
        Assert.isTrue(
                userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_BIND_ERROR);
        //判断借款人额度申请状态
        Assert.isTrue(
                userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_AMOUNT_ERROR);
        //判断借款人额度是否充足
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(
                borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR);
        //存储borrowInfo数据
        borrowInfo.setUserId(userId);
        //百分比转成小数
        borrowInfo.setBorrowYearRate( borrowInfo.getBorrowYearRate().divide(new BigDecimal(100)));
        //设置借款申请的审核状态
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    //获取借款申请审批状态
    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> borrowInfoQueryWrapper = new QueryWrapper<>();
        borrowInfoQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowInfoQueryWrapper);
        if(objects.size() == 0){
            //借款人尚未提交信息
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer)objects.get(0);
        return status;
    }

    //借款信息列表(分页)
    @Override
    public List<BorrowInfo> selectList(Long page,Long limit) {
        List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoPageList(page,limit);
        Page<BorrowInfo> pageParam = new Page<>(page, limit);
        baseMapper.selectPage(pageParam,null);
        long total = pageParam.getTotal();
        borrowInfoList.forEach(borrowInfo -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
            String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
            borrowInfo.getParam().put("returnMethod", returnMethod);
            borrowInfo.getParam().put("moneyUse", moneyUse);
            borrowInfo.getParam().put("status", status);
            borrowInfo.setTotal(total);
        });
        return borrowInfoList;
    }
    //借款信息列表
    //@Override
    //public List<BorrowInfo> selectList() {
    //    List<BorrowInfo> borrowInfoList = baseMapper.selectBorrowInfoList();
    //    borrowInfoList.forEach(borrowInfo -> {
    //        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
    //        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
    //        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
    //        borrowInfo.getParam().put("returnMethod", returnMethod);
    //        borrowInfo.getParam().put("moneyUse", moneyUse);
    //        borrowInfo.getParam().put("status", status);
    //    });
    //    return borrowInfoList;
    //}

    //获取借款信息
    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {
        //查询借款对象:BorrowInfo
        BorrowInfo borrowInfo = baseMapper.selectById(id);
        //组装数据
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
        borrowInfo.getParam().put("returnMethod", returnMethod);
        borrowInfo.getParam().put("moneyUse", moneyUse);
        borrowInfo.getParam().put("status", status);
        //根据user_id获取借款人对象
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<Borrower>();
        borrowerQueryWrapper.eq("user_id", borrowInfo.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        //组装借款人对象:Borrower(BorrowerDetailVO)
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());
        //组装数据
        Map<String, Object> result = new HashMap<>();
        result.put("borrowInfo", borrowInfo);
        result.put("borrower", borrowerDetailVO);
        return result;
    }

    //审批借款信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        //修改借款审核的状态 borrow_info
        Long borrowInfoId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);
        //如果审核通过则产生新的标记录 lend
        if (borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()) {
            //创建新的标记录
            lendService.createLend(borrowInfoApprovalVO, borrowInfo);
        }
    }

    //获取所有列表
    @Override
    public List<BorrowInfo> selectByUserId(Long userId) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id", userId)
                .orderByDesc("id");
        return baseMapper.selectList(queryWrapper);
    }
}

