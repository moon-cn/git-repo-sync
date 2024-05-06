package cn.moon.git.service;

import cn.hutool.core.date.DateUtil;
import cn.moon.git.dao.RuleDao;
import cn.moon.git.entity.Repo;
import cn.moon.git.entity.Rule;
import cn.moon.lang.web.persistence.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.URIish;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Service
@Slf4j
public class RuleService extends BaseService<Rule> {


    @Resource
    RuleDao dao;


    @Resource
    CredentialService credentialService;




    @Async
    public void sync(Rule rule) {
        Repo repo1 = rule.getRepo1();
        Repo repo2 = rule.getRepo2();
        File workDir = new File("/tmp/git-sync-" + DateUtil.date().toString("yyyyMMddHHmmss"));
        if (workDir.exists()) {
            return;
        }
        try {
            log.info("代码下载中...");
            String url = repo1.getUrl();
            String branch = repo1.getBranch();


            log.info("获取仓库1代码 git clone {}", url);
            Git git = Git.cloneRepository()
                    .setCloneSubmodules(true)
                    .setURI(url)
                    .setDirectory(workDir)
                    .setBranch(branch)
                    .setCredentialsProvider(repo1.getCredentialsProvider())
                    .call();

            boolean repo1Empty = git.fetch().setCredentialsProvider(repo1.getCredentialsProvider()).call().getAdvertisedRefs().isEmpty();
            if (repo1Empty) {
                log.info("仓库1为新仓库");
            }

            git.remoteSetUrl().setRemoteUri(new URIish(repo2.getUrl())).setRemoteName("origin").call();
            boolean repo2Empty = git.fetch().setCredentialsProvider(repo2.getCredentialsProvider()).call().getAdvertisedRefs().isEmpty();

            if (repo1Empty && repo2Empty) {
                log.info("仓库1、2都为新仓库，退出");
                return;
            }

            if (repo2Empty) {
                log.info("仓库2为新仓库");
            } else {
                PullResult call = git.pull()
                        .setStrategy(MergeStrategy.RECURSIVE)
                        .setCredentialsProvider(repo2.getCredentialsProvider()).call();
                log.info("仓库2拉取结果 {}", call);
                Map<String, int[][]> conflicts = call.getMergeResult().getConflicts();
                int conflictsSize = conflicts != null ? conflicts.size() : 0;
                log.info("冲突个数{}", conflictsSize);
            }


            // 推送仓库1
            git.remoteSetUrl().setRemoteUri(new URIish(repo1.getUrl())).setRemoteName("origin").call();
            git.push().setCredentialsProvider(repo1.getCredentialsProvider()).call();

            // 推送仓库2
            git.remoteSetUrl().setRemoteUri(new URIish(repo2.getUrl())).setRemoteName("origin").call();
            git.push().setCredentialsProvider(repo2.getCredentialsProvider()).call();

            git.close();
            workDir.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Rule saveRule(Rule rule) {
        return null;
    }
}
