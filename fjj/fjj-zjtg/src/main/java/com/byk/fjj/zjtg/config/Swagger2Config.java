package com.byk.fjj.zjtg.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置信息
 * @author byk
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket apiConfig(){

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo(){

        return new ApiInfoBuilder()
                .title("FAFU基金资金托管平台后台管理系统-API文档")
                .description("本文档描述了FAFU基金资金托管平台后台管理系统接口定义")
                .version("1.0")
                .contact(new Contact("byk", "https://bykllh.cn", "3486913908@qq.com"))
                .build();
    }


}
