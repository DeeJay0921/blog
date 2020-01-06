---
title: Java多线程爬虫及ElasticSearch的初步使用
date: 2020/01/06 10:09:01
cover: http://allzhere.in/wp-content/uploads/2013/05/Java_Multithreading_Live_Scenario.png
tags: 
- Java
- 线程
- Elasticsearch
categories: 
- Java
- Elasticsearch
---
本文主要记录了一个使用多线程爬取数据之后使用ES进行分析的一个练手项目的开发体验
<!--more-->


# 项目开发的一般原则

- 使用GitHub + 主干/分支模型进行开发
- **禁止直接push master分支**
- 所有的改动通过开分支，然后pull request进行合并（这样可以执行自动化代码检查和测试）
- 提交内容不多余，且尽量做到没有本地依赖，使得其他使用者clone下来之后可以无障碍运行

# 初始化新项目的常用方法

- `mvn archetype:generate` 可以使用maven提供的项目骨架去新搭建一个项目
- 也可以通过IDEA的new project去创建


> 其实更多情况下是通过cp已有项目来新建项目的

## pom文件配置

值得注意的是，`pom.xml`下一般要进行阿里云maven镜像的配置：

```
// pom.xml
    <!--阿里云镜像-->
    <repositories>
        <repository>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        </repository>
    </repositories>
```

完整的`pom.xml`在[这里](https://github.com/DeeJay0921/multithread-crawler-demo/blob/master/pom.xml)可以看到

## 初步开发项目的源码及测试代码

本次爬取的是新浪的新闻网站`https://sina.cn`和`https://www.sina.com.cn`，我们使用`apache.httpcomponents`进行爬取

项目代码目录为：`src/main/java/com/github/DeeJay0921`

测试代码的目录为：`src/test/java/com/github/DeeJay0921`

先初步的测试一下是否可以爬取到数据：

```
package com.github.DeeJay0921;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpHost proxy = new HttpHost("10.30.6.49", 9090);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .build();
        // 上述方式创建的 httpclient 是因为需要通过内网代理才能访问外网 仅供笔者自身使用
        // CloseableHttpClient httpclient = HttpClients.createDefault(); 如无需要 直接使用本句创建的 httpclient 即可
        HttpGet httpGet = new HttpGet("http://sina.cn");
        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
//            EntityUtils.consume(entity1);
            System.out.println("EntityUtils.toString(entity1) = " + EntityUtils.toString(entity1));
        }
    }
}
```

我们编写一个`SmokeTest`来进行测试刚才编写的代码是否可以通过冒烟测试:
```
package com.github.DeeJay0921;

import org.junit.jupiter.api.Test;

public class SmokeTest {
    @Test
    public void test() {
        System.out.println("This is smoke Test");
    }
}
```

<details>
<summary>
    解决使用公司内网，通过Java程序访问外网被forbidden的问题：
</summary>
因为笔者所在公司为内网环境，只能依靠配置代理访问外部网络，但是在使用`httpClient`进行访问`https://sina.cn`时，被拦截了

首先我通过`httpClient`的文档，搜索到了[代理配置](http://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/connmgmt.html#d5e485)：
```
HttpHost proxy = new HttpHost("someproxy", 8080);
DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
CloseableHttpClient httpclient = HttpClients.custom()
        .setRoutePlanner(routePlanner)
        .build();
```
而不是通过传统的`CloseableHttpClient httpclient = HttpClients.createDefault();`去创建`httpclient`实例

这样访问之后，开始报一个`unable to find valid certification path to requested target`的错误，一看就是证书出现的问题

先通过`chrome`访问`https://sina.cn`然后点击证书，复制到文件，导出`.cer`文件到本地

再打开本地`jdk`的文件夹，比如`C:\Program Files\Java\jdk-10.0.1\lib\security`下，通过终端运行：
```
keytool -import -file ./sinaCer.cer -keystore cacerts -alias server
```
其中`./sinaCer.cer`就是刚才导出的证书，默认密码为：`changeit`,输入密码后输入`y`进行确定即可。

至此，使用Java程序也能通过代理从内网访问外网进行爬取数据了。
</details>


## 配置`circleci`配置

[circleCI文档](https://circleci.com/docs/2.0/about-circleci/#section=welcome)

在项目的根目录创建一个名为`.circleci`的文件夹，并新建`config.yml`文件, 在[CI的控制台](https://circleci.com/dashboard)去Add Projects,选择好项目和OS以及语言之后， 就可以直接Start Building去执行第一次构建了。

### 关于`config.yml`

CI的配置由三部分组成

- version
- jobs
要执行的`job`清单，集合中的键为`job`的名称，值是具体执行`job`的内容，如果你使用工作流(`workflows`)，则`job`的名称在配置文件中必须唯一，如果你不使用 工作流(`workflows`)，则必须包含名称为`build`的`job`来作为用户提交代码时的默认`job`
- workflow

比如说:

```
version: 2
jobs:
  test:
    docker:
      - image: circleci/openjdk:8u212-jdk-stretch
    steps:
      - checkout
      - restore_cache:
          key: DeeJay0921-{{ checksum "pom.xml" }}
      - run:
          name: Run Maven tests
          command: mvn clean test
      - save_cache: # saves the project dependencies
          paths:
            - ~/.m2
          key: DeeJay0921-{{ checksum "pom.xml" }}
workflows:
  version: 2
  default:
    jobs:
      - test
```

上述配置中`jobs`下只有一个名为`test`的`job`，这个名为`test`的`job`下有一些常用属性：
- docker: 指定CI当前` job 使用 `docker`, 其值`image`是指` docker `所使用的镜像，比如说本例我们制定了`JDK8`, 必要时你可以同时指定多个镜像，比如你的项目需要依赖 `mysql `或者` redis`。 第一个列出的容器为主容器，`steps `都会在主容器中进行。
- steps: 当前`job`要运行的 命令 (`command`) 列表



