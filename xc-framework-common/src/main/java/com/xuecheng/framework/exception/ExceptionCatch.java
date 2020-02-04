package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER  = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map 配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建 ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    /**
     * 可预知异常处理
     * @param e
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        //记录日志
        LOGGER.error("catch exception{}",e.getMessage());
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    /**
     * 不可预知异常处理
     * @param e
     * @return : com.xuecheng.framework.model.response.ResponseResult
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        //记录日志
        LOGGER.error("catch exception{}",e.getMessage());
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build(); //EXCEPTIONS 构建成功
        }
        //从EXCEPTIONS 中找异常类型对应的错误代码，找到响应给用户，找不到响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if(resultCode !=null){
            return new ResponseResult(resultCode);
        }else{
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }

    }
    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }

}
