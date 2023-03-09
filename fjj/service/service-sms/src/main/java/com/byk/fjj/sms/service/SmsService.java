package com.byk.fjj.sms.service;

/**
 * @author byk
 */
public interface SmsService {

    //调用service方法，通过整合短信服务进行发送
    boolean send(String mobile, String code);
}
