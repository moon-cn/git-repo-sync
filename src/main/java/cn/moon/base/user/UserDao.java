package cn.moon.base.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<SysUser,String> {
    SysUser findByUsername(String username);
}
