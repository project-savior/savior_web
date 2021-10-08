package com.jerry.savior_web.interceptor;

import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import com.jerry.savior_web.utils.JsonResponseWritingHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 22454
 */
@Slf4j
@Component
@Getter
public class GlobalInterceptor implements HandlerInterceptor {
    public static final String INTERNAL_ACCESS_HEADER_NAME = "internal-access";
    public static final String[] INTERNAL_ACCESS_HEADER_VALUE = new String[]{"true"};
    private final ObjectMapperHelper objectMapperHelper;

    public GlobalInterceptor(ObjectMapperHelper objectMapperHelper) {
        log.info("interceptor initializing.");
        this.objectMapperHelper = objectMapperHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws Exception {
        int status = response.getStatus();
        StringBuffer url = request.getRequestURL();

        // 处理bad request
        boolean notBadRequest = badRequestHandler(request, response);
        if (!notBadRequest) {
            return false;
        }
        log.info("access ==> {} ,status: {}", url, status);
        String header = request.getHeader(INTERNAL_ACCESS_HEADER_NAME);
        // 如果请求头没有 gateway access参数或者参数值不对，不允许访问服务
        if (StringUtils.isBlank(header) || !INTERNAL_ACCESS_HEADER_VALUE[0].equals(header)) {
            response.setContentType("application/json; charset=UTF-8");
            // 构建forbidden对象
            CommonResponse<Object> forbidden =
                    CommonResponse.build(
                            StandardResponse.FORBIDDEN.getCode(),
                            null,
                            StandardResponse.FORBIDDEN.getMessage());
            // 写入response
            JsonResponseWritingHelper.writeJsonResponse(response, forbidden);
            log.warn("access denied");
            // TODO push error info to mq
            return false;
        }
        log.info("access normal");
        return true;
    }

    private boolean badRequestHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();

        // 404 handler
        if (status == StandardResponse.NOT_FOUND.getCode()) {
            CommonResponse<Void> notFound = CommonResponse
                    .build(StandardResponse.NOT_FOUND.getCode(),
                            null,
                            StandardResponse.NOT_FOUND.getMessage());

            JsonResponseWritingHelper.writeJsonResponse(response, notFound);
            return false;
        }
        return true;
    }
}
