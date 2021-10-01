package com.jerry.savior_web.aspect;


import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.response.CommonResponse;
import com.jerry.savior_common.util.ObjectMapperHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author 22454
 */
@Slf4j
@ControllerAdvice
public class RestResponseAspect implements ResponseBodyAdvice<Object> {

    private final ObjectMapperHelper objectMapperHelper;

    public RestResponseAspect(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter parameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> converterClass,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 假如已经是统一返回类型（例如：GlobalExceptionHandler将出现该情况），直接返回
        if (body instanceof CommonResponse) {
            return body;
        }
        // 重新设置Content-Type
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // 构建CommonResponse
        CommonResponse<Object> commonResponse = CommonResponse.build(body);
        // 直接写入response
        OutputStream outputStream = response.getBody();
        // 序列化为 json
        String json = objectMapperHelper.toJson(commonResponse);
        // 写入 outputStream
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        // 返回null，不走框架自带converter
        return null;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleException(Exception exception) {
        log.error("异常，", exception);
        //TODO push to mq
        return CommonResponse.build(
                StandardResponse.ERROR.getCode(),
                null,
                StandardResponse.ERROR.getMessage()
        );
    }
}
