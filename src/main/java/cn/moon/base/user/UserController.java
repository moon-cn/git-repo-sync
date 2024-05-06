package cn.moon.base.user;

import cn.moon.lang.web.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("api/user")
@RestController
public class UserController {

    @Resource
    UserDao userDao;

    @PostMapping("changePwd")
    public Result changePwd(String pwd) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();

        SysUser usr = userDao.findByUsername(username);
        if (usr == null) {
            usr = new SysUser();
            usr.setUsername(username);
        }

        usr.setPassword(pwd);
        userDao.save(usr);
        return Result.ok().msg("修改密码成功");

    }

}
