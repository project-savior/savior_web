package com.jerry.savior_web.aspect;


import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.error.BusinessException;
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
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author 22454
 */
@Slf4j
@RestControllerAdvice
public class RestResponseAspect implements ResponseBodyAdvice<Object> {

    private final ObjectMapperHelper objectMapperHelper;
    private final String[] SWAGGER_RESOURCE={
            "/swagger-resources",
            "/v2/api-docs"
    };

    public RestResponseAspect(ObjectMapperHelper objectMapperHelper) {
        this.objectMapperHelper = objectMapperHelper;
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter parameter,
                                  @NonNull MediaType mediaType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> converterClass,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        String path = request.getURI().getPath();
        for (String resource : SWAGGER_RESOURCE) {
            if (resource.equals(path)){
                return body;
            }
        }
        // ?????????????????????????????????????????????GlobalExceptionHandler????????????????????????????????????
        if (body instanceof CommonResponse) {
            return body;
        }

        // ????????????Content-Type
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        // ??????CommonResponse
        CommonResponse<Object> commonResponse = CommonResponse.build(body);
        // ????????????response
        OutputStream outputStream = response.getBody();
        // ???????????? json
        String json = objectMapperHelper.toJson(commonResponse);
        // ?????? outputStream
        outputStream.write(json.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        // ??????null?????????????????????converter
        return null;
    }





}
