
package cn.moon.base;

import cn.moon.lang.web.ExceptionHumanTool;
import cn.moon.lang.web.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(UnauthorizedException.class)
    public Result ex(UnauthorizedException e) {
        log.error("认证异常", e);
        return Result.err().msg(e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()).code(403);
    }


    @ExceptionHandler(Exception.class)
    public Result ex(Exception e) {
        log.error("全局异常", e);


        String msg = ExceptionHumanTool.covert(e);
        return Result.err().msg(msg).code(500);
    }
}


