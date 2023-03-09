package com.byk.fjj.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.byk.fjj.core.pojo.entity.Borrower;
import com.byk.fjj.core.pojo.entity.Lend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.vo.BorrowInfoApprovalVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-12-3
 */
public interface LendService extends IService<Lend> {

    //创建新的标记录
    void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo);

    //标的列表
    List<Lend> selectList1();
    //标的列表（分页）
    List<Lend> selectList(Long page, Long limit);

    //获取标的信息
    Map<String, Object> getLendDetail(Long id);

    //计算投资收益
    BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalmonth, Integer returnMethod);

    //放款
    void makeLoan(Long lendId);
}
