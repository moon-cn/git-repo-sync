
# 开发说明


参考 https://github.com/centic9/jgit-cookbook

## ssh key 生成

根据github文档
https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent

最新方式（不行）
ssh-keygen -t ed25519 -C "410518072@qq.com"

旧的的方式（也不行）
ssh-keygen -t rsa -b 4096 -C "410518072@qq.com"

网上搜索下 ,增加 -m PEM  https://juejin.cn/post/7210688516916592677
ssh-keygen  -m PEM  -t rsa  -b 4096 -C "410518072@qq.com"

