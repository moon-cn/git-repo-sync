package cn.moon.base.user;

import cn.moon.lang.web.persistence.BaseEntity;
import cn.moon.validation.StartWithLetter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class SysUser extends BaseEntity {

    @NotNull
    @StartWithLetter
    String username;

    @NotNull
    String password;

    public SysUser() {

    }
    public SysUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
