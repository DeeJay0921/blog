---
title: Maven的简单使用
date: 2019/02/22 00:00:01
cover: https://udemy-images.udemy.com/course/480x270/1184808_e345.jpg
tags: 
- Java
- Maven
categories: 
- Java
---
Maven的简单使用
<!--more-->

[maven详细教程](https://www.yiibai.com/maven/maven_environment_setup.html)

# 安装

- 下载
 [下载地址](http://www-us.apache.org/dist/maven/maven-3/) （选择binaries）

- 安装及环境变量的设置

新建`M2_HOME`环境变量为maven的路径，添加`%M2_HOME%\bin`到系统环境变量path中

- 修改配置conf/setting.xml为下面的内容：
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">

      <mirrors>
        <mirror>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
      </mirrors>
      <!-- 修改maven下载jar到本地的仓库路径 -->
      <localRepository>E:\MavenRepository</localRepository>
</settings>
```

# 基本概念

- 基本功能
Maven是一个用于管理项目构建, 依赖和发布的工具
    - 项目构建配置
    - 简化项目构建
    - 项目依赖管理
    - 项目持续集成
- 库地址 [https://mvnrepository.com/](https://mvnrepository.com/)
搜索想导入的包， 如commons io，复制配置项到pom.xml，（需要用<dependencies>包裹）

pom.xml大概如下：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>learnMaven</groupId>
    <artifactId>maven-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
    </dependencies>

</project>
```

- 运行Maven项目
    1. 可以点击Execute Maven Goal,输入`mvn exec:java -Dexec.mainClass=你的包名.Main -Dexec.args`
    2. 也可以Add Configurations, 输入`exec:java -Dexec.mainClass=你的包名.Main -Dexec.args`保存，点击run

- 设置Java编译器版本和运行环境版本

修改pom.xml,新增
```
<properties>
        <maven.compiler.source>10</maven.compiler.source>
        <maven.compiler.target>1.10</maven.compiler.target>
</properties>
```

