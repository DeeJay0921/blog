---
title: server-mock的使用
date: 2017/08/20 00:00:01
tags: 
- 前端
categories: 
- 前端
---
server-mock的使用
<!--more-->


##server-mock是什么
一款nodejs命令行工具，用于搭建web服务器，模拟网站后端。方便前端开发者Mock数据。基于express。
###安装
`npm install -g server-mock`
镜像：`npm install -g server-mock --registry=https://registry.npm.taobao.org`
##使用
###场景1：搭建web服务器
1 在终端cd到你的文件所在的文件夹
2 执行`mock start`可将当前文件夹路径作为根目录启动一个web服务器
3 在浏览器输入http://localhost:8080/xxx.html
###场景2：为ajax请求mock数据
1 在终端cd到你的文件所在的文件夹
2 在当前文件夹创建`router.js`，该文件是接收并处理后端请求的文件（可以认为是网站的后端）
3 `mock init`可创建范例文件
