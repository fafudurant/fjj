package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.dto.ExcelDictDTO;
import com.byk.fjj.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-15
 */
public interface DictService extends IService<Dict> {

    //Excel导入
    void importData(InputStream inputStream);

    //Excel导出
    List<ExcelDictDTO> listDictData();

    //根据上级id获取子节点数据列表
    List<Dict> listByParentId(Long parentId);

    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);

    //计算下拉列表选中内容
    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
