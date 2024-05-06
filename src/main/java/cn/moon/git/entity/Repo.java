package cn.moon.git.entity;

import cn.moon.git.service.CredentialService;
import cn.moon.lang.web.SpringTool;
import cn.moon.lang.web.persistence.BaseEntity;
import cn.moon.validation.StartWithLetter;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Repo extends BaseEntity {

    @StartWithLetter
    @NotNull
    String url;

    @StartWithLetter
    @NotNull
    String branch;


    @Transient
    public UsernamePasswordCredentialsProvider getCredentialsProvider() {
        return SpringTool.getBean(CredentialService.class).getProvider(url);
    }


    public Repo() {
    }

    public Repo(String url, String branch) {
        this.url = url;
        this.branch = branch;
    }
}
