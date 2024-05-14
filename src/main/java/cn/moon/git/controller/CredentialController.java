package cn.moon.git.controller;

import cn.moon.git.entity.Credential;
import cn.moon.git.service.CredentialService;
import cn.moon.lang.web.Option;
import cn.moon.lang.web.Result;
import cn.moon.lang.web.persistence.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "api/credential")
public class CredentialController {


    @Resource
    private CredentialService service;


    @RequestMapping("list")
    public Page<Credential> list(@PageableDefault(sort = BaseEntity.Fields.modifyTime, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Credential> list = service.findAll(pageable);
        return list;
    }

    @RequestMapping("save")
    public Result save(@RequestBody Credential gitCredential) {
        if(gitCredential.getSshKey() != null){
            gitCredential.setSshKey(gitCredential.getSshKey().trim());
        }
        Credential db = service.save(gitCredential);

        Result rs = Result.ok().msg("保存成功");
        return rs;
    }



    @RequestMapping("delete")
    public Result delete( String id)  {
        service.deleteById(id);
        return Result.ok().msg("删除成功");
    }




    @RequestMapping("options")
    public Result options()  {
        List<Credential> list = service.findAll();
        List<Option> options = Option.convertList(list, BaseEntity::getId, Credential::getUsername);

        return Result.ok().data(options);
    }



}
