package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.fjj.core.enums.BorrowerStatusEnum;
import com.byk.fjj.core.enums.IntegralEnum;
import com.byk.fjj.core.mapper.BorrowerAttachMapper;
import com.byk.fjj.core.mapper.UserInfoMapper;
import com.byk.fjj.core.mapper.UserIntegralMapper;
import com.byk.fjj.core.pojo.entity.Borrower;
import com.byk.fjj.core.mapper.BorrowerMapper;
import com.byk.fjj.core.pojo.entity.BorrowerAttach;
import com.byk.fjj.core.pojo.entity.UserInfo;
import com.byk.fjj.core.pojo.entity.UserIntegral;
import com.byk.fjj.core.pojo.vo.BorrowerApprovalVO;
import com.byk.fjj.core.pojo.vo.BorrowerAttachVO;
import com.byk.fjj.core.pojo.vo.BorrowerDetailVO;
import com.byk.fjj.core.pojo.vo.BorrowerVO;
import com.byk.fjj.core.service.BorrowerAttachService;
import com.byk.fjj.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byk.fjj.core.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-11-28
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private DictService dictService;

    @Resource
    private BorrowerAttachService borrowerAttachService;

    @Resource
    private UserIntegralMapper userIntegralMapper;

    //保存借款人信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBorrowerVOByUserId(BorrowerVO borrowerVO, Long userId) {
        //获取用户基本信息
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO,borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());//认证中
        baseMapper.insert(borrower);
        //保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });
        //更新会员状态，更新为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    //获取借款人认证状态
    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id",userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);
        if(objects.size() == 0){
            //借款人尚未提交信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer)objects.get(0);
        return status;
    }

    //获取借款人分页列表
    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        if(StringUtils.isBlank(keyword)){
            return baseMapper.selectPage(pageParam,null);
        }
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper
                .like("name",keyword)
                .or().like("id_card",keyword)
                .or().like("mobile",keyword)
                .orderByDesc("id");
        return baseMapper.selectPage(pageParam,borrowerQueryWrapper);
    }

    //获取借款人信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        //获取借款人信息
        Borrower borrower = baseMapper.selectById(id);
        //填充基本借款人信息
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower,borrowerDetailVO);
        //婚否
        borrowerDetailVO.setMarry(borrower.getMarry()?"是":"否");
        //性别
        borrowerDetailVO.setSex(borrower.getSex()==1?"男":"女");
        //计算下拉列表选中内容
        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue("moneyUse", borrower.getIndustry());
        String income = dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome());
        String returnSource = dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());
        //设置下拉列表选中内容
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setIncome(income);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);
        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);
        //获取附件VO列表
        List<BorrowerAttachVO> borrowerAttachVOList =  borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);
        return borrowerDetailVO;
    }

    //借款额度审批
    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        //获取借款额度申请id
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        //获取借款申请额度对象
        Borrower borrower = baseMapper.selectById(borrowerId);
        //设置审核状态
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);
        //获取用户id
        Long userId = borrower.getUserId();
        //获取用户对象
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //计算基本信息积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);
        //用户积分
        int currentIntegral = userInfo.getIntegral() + borrowerApprovalVO.getInfoIntegral();
        //计算身份信息积分
        if(borrowerApprovalVO.getIsIdCardOk()){
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
        }
        //计算房产信息积分
        if(borrowerApprovalVO.getIsHouseOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
        }
        //计算车辆信息积分
        if(borrowerApprovalVO.getIsCarOk()) {
            userIntegral = new UserIntegral();
            userIntegral.setUserId(userId);
            userIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userIntegral);
            currentIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
        }
        //设置用户总积分
        userInfo.setIntegral(currentIntegral);
        //修改审核状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        //更新userInfo
        userInfoMapper.updateById(userInfo);
    }
}
