package com.byk.fjj.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byk.fjj.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byk.fjj.core.pojo.query.UserInfoQuery;
import com.byk.fjj.core.pojo.vo.LoginVO;
import com.byk.fjj.core.pojo.vo.RegisterVO;
import com.byk.fjj.core.pojo.vo.UserIndexVO;
import com.byk.fjj.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author byk
 * @since 2022-11-23
 */
public interface UserInfoService extends IService<UserInfo> {

    //注册
    void register(RegisterVO registerVO);

    //会员登录
    UserInfoVO login(LoginVO loginVO, String ip);

    //获取会员分页列表
    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    //锁定和解锁
    void lock(Long id, Integer status);

    //校验手机号是否注册
    boolean checkMobile(String mobile);

    //获取个人空间用户信息
    UserIndexVO getIndexUserInfo(Long userId);

    //根据bindCode获取手机号
    String getMobileByBindCode(String bindCode);
}
