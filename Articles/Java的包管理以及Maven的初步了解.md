---
title: Java的包管理以及Maven的初步了解
date: 2019/07/18 00:00:01
tags: 
- Java
- Maven
categories: 
- Java
---
Java的包管理以及Maven的初步了解
<!--more-->

# 什么是包
- JVM的工作相当简单：
  1. 执行一个类的字节码
  2. 如果在该过程中碰到了新的类，再去加载该类

# 类路径 Classpath
- 加载新的类的路径就是在classpath里寻找
`java -cp xxx`
- 类的全限定类名唯一确定了一个类
- 包其实就是把许多类放在一起打的压缩包（jar后缀）

### 传递性依赖
- 加载到新的类发现目标类还依赖了别的类
- classpath hell
  - 全限定包名是类的唯一标识
  - 当多个同名类同时出现在classpath中，就是噩梦的开始

# 什么是包管理
- 既然项目中要使用一些第三方的类，那么总得告诉JVM要去哪加载这些类
- 包管理的本质就是告诉JVM如何找到所需的第三方类库以及成功的解决其中的冲突问题。

# Maven出现之前
- 通过手动写classpath进行编译运行
然后出现了Apache Ant
- Apache Ant
  - 手动下载Jar包，放在一个目录中
  - 写XML配置，指定编译的源代码目录，依赖的Jar包，输出目录等等
这样还存在的缺点有：
1. 每个人都需要自己独立造轮子
2. 依赖的第三方库都需要手动下载，如果依赖众多，那么管理起来极其麻烦
3. 没有解决classpath hell的问题

# Maven包管理
> Maven远远不只是包管理工具

> Convertion over configuration 约定优于配置
- Maven的中央仓库
  - 按照一定的约定存储包
- Maven的本地仓库
  - 默认位于`~/m2`
  - 下载的第三方包放在这里进行缓存
- Maven的包
  - 按照约定为所有的包的编号，方便检索
  - groupId/artifactId/version 格式
  - SNAPSHOT 快照版本一般用于开发包的过程中，是可以一直更新的，但是上线了之后应该提出稳定版
> 关于版本号也是有约定的，比如5.4.2， 5代表主版本号，一般做了不兼容的API修改才会改，4为此版本号，做了向下兼容的功能新增时添加，2为修订号，做了向下兼容的问题修正时添加。

- Maven解决了的问题
  1. 传递性依赖的自动管理(引用到的库引用到的其他库也会一并下载)
  2. 依赖冲突的解决
  3. 依赖的scope


## 包冲突
- 常见的包冲突的一些报错：
  - AbstractMethodError
  - NoClassDefFoundError
  - ClassNotFoundException
  - LinkageError

> 绝对不允许最终的classpath出现同名的不同版本的Jar包

Maven解决包冲突的原则是： 距离最近的包保留。举个例子就是A类依赖B包，B包再依赖C包，又有D类直接依赖C包，那么A类经过多次依赖的C包会丢弃，Maven会默认保留D类依赖的C包。


但是Maven这种默认实现，有时候也会出现问题，因为最近的包可能不是项目所需的包，这种时候可以通过idea的Maven Dependencies进行分析之后进行手动干预。
> 或者使用指令`mvn dependency: tree`来查看依赖树（解决冲突之后的依赖树）
> 如果在idea环境下，可以搜索插件 Maven Helper

- 手动干预的方法
1. 一般的解决方法是，当分析依赖完成后，我们清楚的知道了我们要引的是哪个版本的哪个包之后，直接在pom中引入，因为Maven是距离最近优先，所以其他的冲突包不会再被引用。
2. 通过修改pom配置告知Maven，即添加exclusions排除不要的包。

# Maven中的scope
常用的scope有3种： `test` `compile`和 `provided`
- test: 只在测试代码中可以拿到依赖包
- compile: 在测试和生产环境中都可以拿到依赖包
- provided: 在编译的时候可以拿到，在运行时就拿不到了

# Maven还是一个自动化构建工具
[ 《Maven实战》 ](https://github.com/plkkoko/maven-archtype-resource)
- Maven项目的基本结构
- 基本概念： 坐标和依赖/生命周期/仓库/聚合和继承
- 使用Maven进行测试
- 开发Maven的插件
