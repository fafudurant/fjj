package com.byk.fjj.zjtg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.zjtg.model.UserBind;

import java.util.Map;
/**
 * @author byk
 */
public interface UserBindService extends IService<UserBind> {

	UserBind bind(Map<String, Object> paramMap);

	boolean isBind(String idCard);

	UserBind getByBindCode(String bindCode);

	void checkPassword(String bindCode, String password);
}
