package com.jerry.savior_web.aspect;

import com.jerry.savior_common.constants.StandardResponse;
import com.jerry.savior_common.error.BusinessException;
import com.jerry.savior_common.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * @author 22454
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    /**
     * 参数丢失异常
     *
     * @param e 异常
     * @return commonResponse
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleMissingServletRequest(MissingServletRequestParameterException e) {
        log.warn("参数丢失异常");
        return CommonResponse.build(
                StandardResponse.BAD_REQUEST.getCode(),
                null,
                String.format("参数[ %s ]丢失，请求失败", e.getParameterName())
        );
    }

    /**
     * 路径参数丢失异常
     *
     * @param e 异常
     * @return commonResponse
     */
    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleMissingPathVariable(MissingPathVariableException e) {
        log.warn("路径参数丢失异常");
        return CommonResponse.build(
                StandardResponse.BAD_REQUEST.getCode(),
                null,
                String.format("参数[ %s ]丢失，请求失败", e.getVariableName())
        );
    }

    /**
     * 参数请求体异常
     *
     * @param e 异常
     * @return commonResponse
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("参数请求体异常");
        return CommonResponse.build(
                StandardResponse.BAD_REQUEST.getCode(),
                null,
                String.format("请求体异常，当前请求体：%s", e.getHttpInputMessage())
        );
    }

    /**
     * 参数校验异常
     *
     * @param e 异常
     * @return commonResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("参数校验异常");
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        String defaultMessage = "参数格式错误";
        if (fieldError != null) {
            defaultMessage = fieldError.getDefaultMessage();
        }

        return CommonResponse.build(
                StandardResponse.BAD_REQUEST.getCode(),
                null,
                defaultMessage
        );
    }

    /**
     * 请求方式不支持异常
     *
     * @param e 异常
     * @return commonResponse
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方式不支持异常");
        return CommonResponse.build(
                StandardResponse.BAD_REQUEST.getCode(),
                null,
                "Bad Request.".concat(e.getMessage())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleConstraintViolation(ConstraintViolationException e) {
        log.warn("参数校验异常");
        String message = e.getMessage();
        return CommonResponse.build(
                StandardResponse.ERROR.getCode(),
                null,
                message.split(":", 2)[1]
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleException(Exception exception) {
        log.error("未知异常，", exception);
        //TODO push to mq
        return CommonResponse.build(
                StandardResponse.ERROR.getCode(),
                null,
                StandardResponse.ERROR.getMessage()
        );
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<Void> handleBusinessException(BusinessException exception) {
        log.warn("业务异常，", exception);
        return CommonResponse.build(
                exception.getCode(),
                null,
                exception.getMessage()
        );
    }
}
