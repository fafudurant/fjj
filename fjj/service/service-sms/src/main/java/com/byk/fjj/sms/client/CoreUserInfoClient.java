package com.byk.fjj.sms.client;

import com.byk.fjj.sms.client.fallback.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author byk
 */
@FeignClient(value = "service-core", fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {

    //校验手机号是否注册
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable String mobile);
}
