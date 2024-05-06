package cn.moon.git.entity;

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



    @ManyToOne
    Credential credential;

    @Transient
    public UsernamePasswordCredentialsProvider getCredentialsProvider(){
        if (credential != null) {
            UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(credential.getUsername(), credential.getPassword());

            return provider;
        }
        return null;
    }


    public Repo() {
    }

    public Repo(String url, String branch, Credential credential) {
        this.url = url;
        this.branch = branch;
        this.credential = credential;
    }
}
