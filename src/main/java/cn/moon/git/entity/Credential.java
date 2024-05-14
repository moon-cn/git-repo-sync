package cn.moon.git.entity;

import cn.moon.lang.web.persistence.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Credential extends BaseEntity {



    String username;

    String password;


    @NotNull
    String hostname;

    // 私钥
    @Column(length = 3000)
    String sshKey;


}
