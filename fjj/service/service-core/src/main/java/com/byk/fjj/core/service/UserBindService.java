package com.byk.fjj.core.service;

import com.byk.fjj.core.pojo.entity.UserBind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.vo.UserBindVO;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-26
 */
public interface UserBindService extends IService<UserBind> {

    //根据userId做账号绑定,生成一个动态表单的字符串
    String commitBindUser(UserBindVO userBindVO, Long userId);

    //修改绑定状态
    void notify(Map<String, Object> paramMap);

    //获取投资人的绑定协议号
    String getBindCodeByUserId(Long userId);
}
