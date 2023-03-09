package com.byk.fjj.core.mapper;

import com.byk.fjj.core.pojo.entity.BorrowInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 借款信息表 Mapper 接口
 * </p>
 *
 * @author byk
 * @since 2022-12-1
 */
public interface BorrowInfoMapper extends BaseMapper<BorrowInfo> {

    //借款信息列表
    List<BorrowInfo> selectBorrowInfoList();


    List<BorrowInfo> selectBorrowInfoPageList(@Param("page") Long page,@Param("limit") Long limit);
}
