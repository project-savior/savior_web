package com.jerry.savior_web.interceptor;

import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 22454
 */
@Slf4j
@Component
@Getter
public class GlobalInterceptor implements HandlerInterceptor {
    private final String GATEWAY_ACCESS_HEADER_NAME = "gateway-access";
    private final String[] GATEWAY_ACCESS_HEADER_VALUE = new String[]{"true"};
    private final ObjectMapperHelper objectMapperHelper;

    public GlobalInterceptor(ObjectMapperHelper objectMapperHelper) {
        log.info("interceptor initializing.");
        this.objectMapperHelper = objectMapperHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Gateway Access Checking...");
        String header = request.getHeader(GATEWAY_ACCESS_HEADER_NAME);
        // 如果请求头没有 gateway access参数或者参数值不对，不允许访问服务
        if (StringUtils.isBlank(header) || !GATEWAY_ACCESS_HEADER_VALUE[0].equals(header)) {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            // 构建forbidden对象
            CommonResponse<Object> forbidden =
                    CommonResponse.build(
                            StandardResponse.FORBIDDEN.getCode(),
                            null,
                            StandardResponse.FORBIDDEN.getMessage());
            // 写入response
            writer.write(objectMapperHelper.toJson(forbidden));
            writer.flush();
            log.warn("access denied");
            // TODO push error info to mq
            return false;
        }
        log.info("access normal");
        return true;
    }
}
