package com.byk.fjj.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.entity.Borrower;
import com.byk.fjj.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-30
 */
public interface BorrowInfoService extends IService<BorrowInfo> {

    //获取借款额度
    BigDecimal getBorrowAmount(Long userId);

    //提交借款申请
    void saveBorrowInfo(BorrowInfo borrowInfo, Long userId);

    //获取借款申请审批状态
    Integer getStatusByUserId(Long userId);

    //借款信息列表(分页)
    List<BorrowInfo> selectList(Long page, Long limit);
    //借款信息列表
    //List<BorrowInfo> selectList();

    //获取借款信息
    Map<String, Object> getBorrowInfoDetail(Long id);

    //审批借款信息
    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);

    //获取所有列表
    List<BorrowInfo> selectByUserId(Long userId);
}
