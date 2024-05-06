package cn.moon.git.entity;

import cn.moon.lang.web.persistence.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Credential extends BaseEntity {



    @NotNull
    String username;

    @NotNull
    String password;


    @NotNull
    String url;


}
