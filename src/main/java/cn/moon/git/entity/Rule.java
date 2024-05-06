package cn.moon.git.entity;

import cn.moon.lang.web.persistence.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Rule extends BaseEntity {

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    Repo repo1;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    Repo repo2;

}
