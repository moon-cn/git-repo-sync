# git-repo-sync

Sync Git repositories.

同步git代码仓库, 
遇到冲突则不再同步
每隔5分钟执行一次

# 安装
## 方式一、docker
```
docker run -p:8080:8080 -v /derby-data:/derby-data --name git-repo-sync -d mooncn/git-repo-sync:latest  
```
http://127.0.0.1:8080
默认账号：admin,密码：123456

# 使用方式
- 创建仓库的凭据，目前只支持账号密码方式。
其中url用来匹配git仓库，可以简单填写域名如https://gitee.com

- 定义规则
一个规则包含两个git仓库地址。可点击【立即同步】按钮测试


# 页面截图
## 规则
![img.png](doc/gz.png)

## 凭据
![img.png](doc/pj.png)