package com.jerry.savior_web.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author 22454
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
@ConfigurationProperties(prefix = "swagger.config")
public class Knife4jConfig {
    private String apiTitle;
    private String baseScanPackage;
    private Long serverPort;

    @Value("${server.port:8080}")
    public void setServerPort(Long serverPort) {
        this.serverPort = serverPort;
    }

    @Value("${title:Api Docment}")
    public void setApiTitle(String apiTitle) {
        this.apiTitle = apiTitle;
    }

    @Value("${baseScanPackage:com.jerry}")
    public void setBaseScanPackage(String baseScanPackage) {
        this.baseScanPackage = baseScanPackage;
    }

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        log.info("初始化knife4j在线文档,访问地址：http://127.0.0.1:{}/doc.html",serverPort);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(apiTitle)
                        .description("# swagger-bootstrap-ui-demo RESTful APIs")
                        .termsOfServiceUrl(String.format("http://localhost:%s/swagger-ui.html", serverPort))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(baseScanPackage))
                .paths(PathSelectors.any())
                .build();
    }
}
