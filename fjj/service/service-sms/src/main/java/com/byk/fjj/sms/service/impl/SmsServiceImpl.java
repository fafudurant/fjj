package com.byk.fjj.sms.service.impl;

import com.byk.common.exception.BusinessException;
import com.byk.common.result.ResponseEnum;
import com.byk.fjj.sms.service.SmsService;
import com.byk.fjj.sms.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author byk
 */
@Service
public class SmsServiceImpl implements SmsService {

    //调用service方法，通过整合短信服务进行发送
    @Override
    public boolean send(String mobile, String code) {
        //判断手机号是否为空
        System.out.println(code);
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        if(!StringUtils.isEmpty(code)) {
            String host = "https://dfsns.market.alicloudapi.com";
            String path = "/data/send_sms";
            String method = "POST";
            String appcode = "05b88f5e0fb6476a80d43b2ffe2b9546";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 05b88f5e0fb6476a80d43b2ffe2b9546
            headers.put("Authorization", "APPCODE " + appcode);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            Map<String, String> bodys = new HashMap<String, String>();
            bodys.put("content", "code:" + code);
            bodys.put("phone_number", mobile);
            bodys.put("template_id", "TPL_0000");
            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                System.out.println(response.toString());
                return true;
                //获取response的body
                //System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR , e);
            }
        }
        return false;
    }
}
