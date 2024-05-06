package cn.moon.git.service;

import cn.moon.git.dao.CredentialDao;
import cn.moon.git.entity.Credential;
import cn.moon.lang.web.persistence.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class CredentialService extends BaseService<Credential> {


    @Resource
    CredentialDao dao;





}
