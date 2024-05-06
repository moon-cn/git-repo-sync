package cn.moon.base.shiro;

import cn.moon.base.user.SysUser;
import cn.moon.base.user.UserDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyRealm extends AuthorizingRealm {


    @Resource
    UserDao userDao;


    //用于在进行用户身份认证时获取用户的认证信息。
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = new String(token.getPassword());

        // 根据用户名查询用户信息（比如从数据库中查询）
        SysUser sysUser = userDao.findByUsername(username);

        long count = userDao.count();
        if(count == 0){
            sysUser = new SysUser("admin", "123456");
        }

        // 判断用户是否存在
        if (sysUser == null) {
            throw new UnknownAccountException("用户不存在");
        }

        // 验证密码是否匹配
        if (!password.equals(sysUser.getPassword())) {
            throw new IncorrectCredentialsException("密码错误");
        }


        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(sysUser.getUsername(), sysUser.getPassword(), getName());


        return authenticationInfo;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户
        String userId = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }
}
