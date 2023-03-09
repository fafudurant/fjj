package com.byk.fjj.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.byk.fjj.core.mapper.DictMapper;
import com.byk.fjj.core.pojo.dto.ExcelDictDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byk
 */
@Slf4j
//@AllArgsConstructor //全参
@NoArgsConstructor //无参
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    private DictMapper dictMapper;

    //每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
    private static final int BATCH_COUNT = 5;
    //数据列表
    List<ExcelDictDTO> list = new ArrayList();

    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /**
     *遍历每一行的记录
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext context) {
        log.info("解析到一条记录: {}", data);
        //将数据存入数据列表
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData(){
        log.info("{}条数被存储到数据库......",list.size());
        //批量保存
        dictMapper.insertBatch(list);
        log.info("{}条数被存储到数据库成功！",list.size());
    }
}
