package com.byk.fjj.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byk.fjj.core.pojo.entity.BorrowerAttach;
import com.byk.fjj.core.mapper.BorrowerAttachMapper;
import com.byk.fjj.core.pojo.vo.BorrowerAttachVO;
import com.byk.fjj.core.service.BorrowerAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人上传资源表 服务实现类
 * </p>
 *
 * @author byk
 * @since 2022-11-30
 */
@Service
public class BorrowerAttachServiceImpl extends ServiceImpl<BorrowerAttachMapper, BorrowerAttach> implements BorrowerAttachService {

    //获取附件VO列表
    @Override
    public List<BorrowerAttachVO> selectBorrowerAttachVOList(Long borrowerId) {
        QueryWrapper<BorrowerAttach> borrowerAttachQueryWrapper = new QueryWrapper<>();
        borrowerAttachQueryWrapper.eq("borrower_id", borrowerId);
        List<BorrowerAttach> borrowerAttachList = baseMapper.selectList(borrowerAttachQueryWrapper);
        List<BorrowerAttachVO> borrowerAttachVOList = new ArrayList<>();
        borrowerAttachList.forEach(borrowerAttach -> {
            BorrowerAttachVO borrowerAttachVO = new BorrowerAttachVO();
            borrowerAttachVO.setImageType(borrowerAttach.getImageType());
            borrowerAttachVO.setImageUrl(borrowerAttach.getImageUrl());
            borrowerAttachVOList.add(borrowerAttachVO);
        });
        return borrowerAttachVOList;
    }
}
