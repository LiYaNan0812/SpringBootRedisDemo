package com.neusoft.test.config;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auth lyn
 * @Date 2022/3/24 9:04
 * version 1.0
 */
@Configuration
@EnableSwagger2
@Profile({"dev","test"})//1、指定环境下开启
//3、或者使用条件注入的方式@ConditionOnProperty(name="swagger.enable",havingValue="true")
public class SwagerConfig implements WebMvcConfigurer {
    @Value("${swagger.enable}")
    private Boolean swaggerEnabled;//2、通过配置文件配置是否启动，使用时在new Docket里添加.enable方法即可。

    /**
     * apiInfo()增加API相关信息
     * 1、所有注解可用
     * .apis(RequestHandlerSelectors.any())
     * 2、指定部分注解可用如Api.class(@APi),ApiOperation.class(@ApiOperation),ApiImplicitParam.class(@ApiImplicitParam)
     * .apis(RequestHandlerSelectors.withMethdAnnotation(Api.class))
     * 3、指定包路径
     * .apis(RequestHandlerSelectors.basPackage("com.neusoft.test.controller")
     * .paths()这个是包路径下的路径，PathSelectors.any()是包下的所有路径
     * @return
     */
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                //.useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SpringBoot-Swagger2集成")
                .description("springboot|swagger2")
                .contact(new Contact("liyanan","https://www.baidu.com","aaaa@qq.com"))
                .version("1.0.0")
                .build();
    }

}
