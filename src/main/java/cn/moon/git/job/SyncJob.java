package cn.moon.git.job;

import cn.moon.git.entity.Rule;
import cn.moon.git.service.RuleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class SyncJob {
    @Resource
    RuleService ruleService;

    @Scheduled(fixedDelay = 5, initialDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void sync() {
        List<Rule> all = ruleService.findAll();

        for (Rule rule : all) {
            ruleService.sync(rule);
        }
    }
}
