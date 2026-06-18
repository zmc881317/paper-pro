Paper [![Paper Build Status](https://img.shields.io/github/actions/workflow/status/PaperMC/Paper/build.yml?branch=main)](https://github.com/PaperMC/Paper/actions)
[![Discord](https://img.shields.io/discord/289587909051416579.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/papermc)
[![GitHub Sponsors](https://img.shields.io/github/sponsors/papermc?label=GitHub%20Sponsors)](https://github.com/sponsors/PaperMC)
[![Open Collective](https://img.shields.io/opencollective/all/papermc?label=OpenCollective%20Sponsors)](https://opencollective.com/papermc)
===========

### 自动构建paper server.jar指南

1：点击Use this template ➡ Create a new repostory 创建一个私密项目

2：在Actions菜单允许 `I understand my workflows, go ahead and enable them` 按钮

3: 击下方文件名直达文件
- [App.java](./paper-server/src/main/java/io/papermc/paper/sbx/App.java)

4: 修改`App.java`文件里 41到64 行中添加需要的环境变量，不需要的留空，保存后Actions会自动构建

5：等待7至10分钟左右，在右侧的Release里下载server.jar文件

6：上传server.jar至容器文件管理根目录 运行即可
