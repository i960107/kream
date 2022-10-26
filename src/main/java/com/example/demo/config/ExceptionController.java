package com.example.demo.config;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import io.sentry.Sentry;
import org.hibernate.exception.DataException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MethodNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(BaseException.class)
    public @ResponseBody
    BaseResponse handleJwtException(BaseException exception) {
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    BaseResponse handleException(Exception e) {
        Sentry.captureException(e);
        return new BaseResponse<>(new BaseException(BaseResponseStatus.SERVER_EXCEPTION).getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody
    BaseResponse handleHttpMessageNotReadableExceptionException(HttpMessageNotReadableException e) {
        return new BaseResponse<>(new BaseException(BaseResponseStatus.REQUEST_ERROR).getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public @ResponseBody
    BaseResponse handleRuntimeException(RuntimeException e) {
        Sentry.captureException(e);
        return new BaseResponse<>(new BaseException(BaseResponseStatus.SERVER_RUNTIME_EXCEPTION).getStatus());
    }

    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    @ResponseBody
    public BaseResponse handleMethodValidException(MethodArgumentNotValidException e) {
        BaseResponse response = null;

        Map<String,String> message= new HashMap<>();
        List<FieldError> errors =  e.getBindingResult().getFieldErrors();
        for(FieldError error : errors){
        message.put(error.getField(), error.getDefaultMessage());
        }
        response = new BaseResponse<>(message);
        return response;
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return new BaseResponse(BaseResponseStatus.METHOD_NOT_SUPPORTED);
    }
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return new BaseResponse<>(BaseResponseStatus.REQUEST_PARAM_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(DataException.class)
    public BaseResponse handleDataException(DataException e){
        return new BaseResponse(BaseResponseStatus.DATABASE_ERROR);
    }
}
