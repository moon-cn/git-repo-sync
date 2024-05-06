package cn.moon.base;

import cn.moon.lang.web.Result;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录验证
 */
@RestController
@RequestMapping("api")
public class LoginController {


    @RequestMapping("login")
    public Result login(   String username,    String password) {

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            SecurityUtils.getSubject().login(token);
            return Result.ok().msg("登录成功");
        } catch (Exception e) {
            return Result.err().msg("登录失败" + e.getMessage());
        }
    }


    @RequestMapping("login/check")
    public Result checkLogin() {
        boolean isLogin = SecurityUtils.getSubject().isAuthenticated();

        return Result.err().msg("检查登录结果：未登录");
    }



    @RequestMapping("logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok().msg("退出成功");
    }



}
