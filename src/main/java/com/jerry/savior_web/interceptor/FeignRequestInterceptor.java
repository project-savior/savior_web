package com.jerry.savior_web.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;


/**
 * @author 22454
 */
@Slf4j
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("request header set internal access attributes");
        requestTemplate.header(GlobalInterceptor.INTERNAL_ACCESS_HEADER_NAME,
                GlobalInterceptor.INTERNAL_ACCESS_HEADER_VALUE);
    }
}
