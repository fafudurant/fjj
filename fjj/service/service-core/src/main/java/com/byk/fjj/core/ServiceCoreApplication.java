package com.byk.fjj.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author byk
 */
@SpringBootApplication
@ComponentScan({"com.byk.fjj","com.byk.common"})
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class, args);
    }
}
