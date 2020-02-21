package com.example.mail.interceptor;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobaExceptionInterceptor {
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(HttpServletRequest request,Exception e){
        String failMsg=null;
        if(e instanceof MethodArgumentNotValidException){
            //拿到参数校验具体异常信息提示
            failMsg=((MethodArgumentNotValidException)e).getBindingResult().getFieldError().getDefaultMessage();
        }
        return failMsg;
    }
}
