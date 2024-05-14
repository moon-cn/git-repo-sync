package cn.moon.git.service;

import cn.hutool.core.date.DateUtil;
import cn.moon.git.dao.RuleDao;
import cn.moon.git.entity.Repo;
import cn.moon.git.entity.Rule;
import cn.moon.lang.web.persistence.BaseService;
import cn.moon.ssh.AutoTransportConfigCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.URIish;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class RuleService extends BaseService<Rule> {


    @Resource
    RuleDao dao;


    @Resource
    CredentialService credentialService;

    @Resource
    AutoTransportConfigCallback transportConfigCallback;


    public void sync(Rule rule) throws Exception {
        Repo repo1 = rule.getRepo1();
        Repo repo2 = rule.getRepo2();
        File workDir = new File("/tmp/git-sync-" + DateUtil.date().toString("yyyyMMddHHmmss"));
        if (workDir.exists()) {
            return;
        }
        log.info("代码下载中...");
        String url = repo1.getUrl();
        String branch = repo1.getBranch();


        log.info("获取仓库1代码 git clone {}", url);
        Git git = Git.cloneRepository()
                .setCloneSubmodules(true)
                .setURI(url)
                .setDirectory(workDir)
                .setBranch(branch)
                .setTransportConfigCallback(transportConfigCallback)
                .call();
        log.info("仓库1代码克隆完成");

        boolean repo1Empty = git.fetch().setTransportConfigCallback(transportConfigCallback).call().getAdvertisedRefs().isEmpty();
        if (repo1Empty) {
            log.info("仓库1为新仓库");
        }

        log.info("切换为仓库2 {}", repo2.getUrl());
        git.remoteSetUrl().setRemoteUri(new URIish(repo2.getUrl())).setRemoteName("origin").call();
        log.info("获取仓库2信息");
        boolean repo2Empty = git.fetch().setTransportConfigCallback(transportConfigCallback).call().getAdvertisedRefs().isEmpty();
        log.info("仓库2是否为空：{}", repo2Empty);

        if (repo1Empty && repo2Empty) {
            log.info("仓库1、2都为新仓库，退出");
            return;
        }

        if (repo2Empty) {
            log.info("仓库2为新仓库");
        } else {
            PullResult call = git.pull()
                    .setStrategy(MergeStrategy.RECURSIVE)
                    .setTransportConfigCallback(transportConfigCallback).call();
            log.info("仓库2拉取结果 {}", call);
            Map<String, int[][]> conflicts = call.getMergeResult().getConflicts();
            int conflictsSize = conflicts != null ? conflicts.size() : 0;
            log.info("冲突个数{}", conflictsSize);
        }


        log.info("推送仓库1");
        git.remoteSetUrl().setRemoteUri(new URIish(repo1.getUrl())).setRemoteName("origin").call();
        git.push().setTransportConfigCallback(transportConfigCallback).call();

        log.info("推送仓库2");
        git.remoteSetUrl().setRemoteUri(new URIish(repo2.getUrl())).setRemoteName("origin").call();
        git.push().setTransportConfigCallback(transportConfigCallback).call();

        git.close();

        log.info("清理文件夹");
        workDir.delete();

        rule.setLastTime(new Date());

    }


    @Transactional
    public Rule saveRule(Rule rule) {
        if (!rule.isNew()) {
            Rule db = dao.findById(rule.getId()).orElse(null);
            db.setRepo1(null);
            db.setRepo2(null);
        }
        return dao.save(rule);
    }
}
