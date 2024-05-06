package cn.moon.git.controller;

import cn.moon.git.entity.Rule;
import cn.moon.git.service.RuleService;
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

@RestController
@Slf4j
@RequestMapping(value = "api/rule")
public class RuleController {


    @Resource
    private RuleService service;


    @RequestMapping("list")
    public Page<Rule> list(@PageableDefault(sort = BaseEntity.Fields.modifyTime, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Rule> list = service.findAll(pageable);
        return list;
    }

    @RequestMapping("save")
    public Result save(@RequestBody Rule gitRule) {
        Rule db = service.saveRule(gitRule);

        Result rs = Result.ok().msg("保存成功");
        return rs;
    }

    @RequestMapping("sync")
    public Result sync(@RequestBody Rule rule) {
        Rule db = service.findOne(rule.getId());
        service.sync(db);

        Result rs = Result.ok().msg("同步成功");
        return rs;
    }


    @RequestMapping("delete")
    public Result delete(String id) {
        service.deleteById(id);
        return Result.ok().msg("删除成功");
    }


}
