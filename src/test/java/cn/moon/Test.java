package cn.moon;

import cn.moon.git.entity.Credential;
import cn.moon.git.entity.Repo;
import cn.moon.git.entity.Rule;
import cn.moon.git.service.RuleService;

public class Test {

    public static void main(String[] args) throws Exception {

        Credential credential = new Credential();
        credential.setUsername("moon_cn");
        credential.setPassword(System.getenv("GITEE_PASSWORD")); // gitee 的密码，放环境变量

        Rule rule = new Rule();
        rule.setRepo1(new Repo("https://gitee.com/moon_cn/test1.git", "master"));
        rule.setRepo2(new Repo("https://gitee.com/moon_cn/test2.git", "master"));


        RuleService ruleService = new RuleService();

        ruleService.sync(rule);
    }
}
