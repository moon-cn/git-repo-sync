
# 开发说明


参考 https://github.com/centic9/jgit-cookbook



## ssh key 生成

根据github文档
https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent

最新方式（不行）
ssh-keygen  -t ed25519 -C "410518072@qq.com"

旧的的方式（也不行）
ssh-keygen -t rsa -b 4096 -C "410518072@qq.com"

网上搜索下 ,增加 -m PEM  https://juejin.cn/post/7210688516916592677
ssh-keygen  -m PEM  -t rsa  -b 4096 -C "410518072@qq.com"
github 又提示密钥太老，用新的

又试了下最新方式 增加 -m PEM， 提示密钥错误
ssh-keygen -m PEM -t rsa -b 4096 -C "410518072@qq.com"

看来只能用低版本的ssh生成器了
https://github.com/mwiede/jsch

## 问题解决过程
```
Caused by: com.jcraft.jsch.JSchException: UnknownHostKey: github.com.
```
https://stackoverflow.com/questions/2003419/com-jcraft-jsch-jschexception-unknownhostkey