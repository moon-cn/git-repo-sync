# git-repo-sync

Sync Git repositories.

同步git代码仓库, 
遇到冲突则不再同步


## 安装
## 方式一、docker
```
docker run -p:8080:8080 -v /derby-data:/derby-data --name git-repo-sync -d mooncn/git-repo-sync:latest  
```
http://127.0.0.1:8080
默认账号：admin,密码：123456

## 规则
![img.png](doc/gz.png)

## 凭据
![img.png](doc/pj.png)