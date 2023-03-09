package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.BorrowerAttach;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.vo.BorrowerAttachVO;

import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-30
 */
public interface BorrowerAttachService extends IService<BorrowerAttach> {

    //获取附件VO列表
    List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId);
}
