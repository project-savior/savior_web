package com.jerry.savior_web.config;

import com.jerry.savior_web.interceptor.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 22454
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final GlobalInterceptor globalInterceptor;

    public WebConfig(GlobalInterceptor globalInterceptor) {
        this.globalInterceptor = globalInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("global interceptor registry.");
        registry.addInterceptor(globalInterceptor)
                .addPathPatterns("/**");
    }

}
