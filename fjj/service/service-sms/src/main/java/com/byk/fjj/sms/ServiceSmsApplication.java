package com.byk.fjj.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author byk
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.byk.fjj","com.byk.common"})
public class ServiceSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSmsApplication.class, args);
    }
}
