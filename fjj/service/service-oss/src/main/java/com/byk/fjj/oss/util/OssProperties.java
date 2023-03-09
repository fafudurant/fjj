package com.byk.fjj.oss.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author byk
 */
@Component
public class OssProperties implements InitializingBean {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.keyId}")
    private String keyId;

    @Value("${aliyun.oss.keySecret}")
    private String keySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public static String ENDPOINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
