package cn.moon.git.service;

import cn.moon.git.dao.CredentialDao;
import cn.moon.git.entity.Credential;
import cn.moon.lang.web.persistence.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class CredentialService extends BaseService<Credential> {


    @Resource
    CredentialDao dao;


    public UsernamePasswordCredentialsProvider getProvider(String url) {
        List<Credential> list = dao.findAll();

        // 按长度排序
        Collections.sort(list, Comparator.comparingInt(o -> o.getUrl().length()));

        // 倒叙，先匹配长的，后匹配短的
        Collections.reverse(list);
        for (Credential credential : list) {
            if (url.contains(credential.getUrl())) {
                return new UsernamePasswordCredentialsProvider(credential.getUsername(), credential.getPassword());
            }
        }

        return null;
    }




}
