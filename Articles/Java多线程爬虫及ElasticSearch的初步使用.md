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

# 进行开发

到这里就要开始真正的开发了，记得先创建好自己的分支，不能在master下直接开发

## 确定算法

我们要做到从一个节点出发,遍历所有的节点

> 深度优先是优先访问层次更深的节点，而广度优先是优先访问完同一层次的所有节点，再去访问下一层次的节点

这里采用广度优先的思想

大概思路是维护一个链接池，每次从这个连接池里面去拿一个链接开始处理

首先做判断是否已经处理过该链接，如果没处理过那么访问链接得到页面，如果是非新闻页，那么直接跳过

如果是新闻页，将得到的信息存储在数据库，将新的页面得到的链接加入链接池，如此循环

开发出一个[初步的算法](https://github.com/DeeJay0921/multithread-crawler-demo/commit/fdb27b2aaac06f8cf52db829f04ae7d116c2d16f)

## maven 生命周期 与 插件配置

详细的周期为：[maven lifecycle](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#)

一般我们使用的就是Default Lifecycle，从`validtate`到`deploy`

当我们运行`mvn xxx`时，就会从这个生命周期开始，一直依次往下执行直到我们输入的指令,比如`mvn test`会一直从`validate`执行到`test`

并且，在默认情况下，这些生命周期未经指定的话，是什么逻辑都不执行的，所以需要绑定逻辑到生命周期上，这个就需要maven的插件

maven内置了一些插件，比如`maven-comoile-plugin`，将逻辑绑定到了`compile`周期上，这些插件也不会显示的声明在`pom.xml`里面，而这个绑定的逻辑，被称为一个`goal`

在test周期也有内置插件为`maven-surefire-plugin`,可以在`pom`里面显式的指定想要用那个版本

关于非内置插件，举个`checkStyle`的例子：

```
<plugin>
	<artifactId>maven-checkstyle-plugin</artifactId>
	<version>3.0.0</version>
	<configuration>
		<!--checkstyle文件配置路径-->
		<configLocation>${basedir}/.circleci/checkstyle.xml</configLocation>
		<includeTestSourceDirectory>true</includeTestSourceDirectory>
		<enableRulesSummary>false</enableRulesSummary>
	</configuration>
	<executions>
		<execution>
			<id>compile</id>
			<phase>compile</phase>
			<goals>
				<goal>check</goal>
			</goals>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>com.puppycrawl.tools</groupId>
			<artifactId>checkstyle</artifactId>
			<version>8.22</version>
		</dependency>
	</dependencies>
</plugin>
```

这是一个插件的配置，其中`executions`指定了一个名为`goal`的`goal`，且将其绑定到了`compile`周期。 如果一个maven生命周期绑定了多个`goal`的时候，谁先声明的就先执行谁

## spotBugs插件引入

[官方文档](https://spotbugs.github.io/)

使用maven将其引入，我们额外还得加个配置，使其在`verify`周期再执行：
```
<!--spotbugs-->
<plugin>
	<groupId>com.github.spotbugs</groupId>
	<artifactId>spotbugs-maven-plugin</artifactId>
	<version>3.1.12.2</version>
	<executions>
		<execution>
			<id>spotbugs</id>
			<phase>verify</phase>
			<goals>
				<goal>check</goal>
			</goals>
		</execution>
	</executions>
	<dependencies>
		<!-- overwrite dependency on spotbugs if you want to specify the version of spotbugs -->
		<dependency>
			<groupId>com.github.spotbugs</groupId>
			<artifactId>spotbugs</artifactId>
			<version>4.0.0-beta4</version>
		</dependency>
	</dependencies>
</plugin>
```
 
写一段会出bug的代码测试一下:
```
Integer i = null;
if (i == 1) {
	System.out.println("Test");
}
```

执行`mvn verify`， 会看到`spotbugs`会有提示` Null pointer dereference of i in com.github.DeeJay0921.Main.main(String[]) `


## 引入H2数据库实现数据存储和断点续传

要在项目里引入数据库，将未处理以及已处理的连接池转为数据库存储，同时将爬取到的新闻信息也存储到数据库里。

如上分析，我们需要新建3张表：
1. LINKS_TO_BE_PROCESSED
2. LINKS_ALREADY_PROCESSED
3. NEWS

1和2的表结构都很简单，直接存链接即可，针对存储新闻的表NEWS，暂定字段为id, Title, Content, URL, created_at, updated_at。

引入数据库之后，将之前的操作都改为存储到数据库中,[代码如下](https://github.com/DeeJay0921/multithread-crawler-demo/commit/60c5249ebbef19d7d1168f0b62cea1480715009b)

注意到这里我们仍然使用了一个内存中的`List`去缓存数据库中的连接池，可以[进一步优化掉](https://github.com/DeeJay0921/multithread-crawler-demo/commit/73531e2cd1447f751ad8e6e3c5d43963a2461cd0)

## 使用flyway自动化管理数据库

随着版本迭代，数据库的结构也在不断的进行变更，比如字段的增删等，如果需要将这些变更维护起来，就需要[flyway](https://flywaydb.org/)

我们可以使用[flyway](https://flywaydb.org/)去使得我们的数据库的新建和初始化完全自动化

按照[官方文档](https://flywaydb.org/documentation/migrations#sql-based-migrations)的约定，我们新建一个`/main/resources/db/migration`路径，

在下面按照其约定的命名规则，创建2个sql，一个名为`V1__Create_tables.sql`，作为新建表的sql，一个名为`V2__Init_data.sql`作为初始化数据的sql。代码[在这里](https://github.com/DeeJay0921/multithread-crawler-demo/commit/f009fcf069ba41af1ca335438a46fbf271dc32f8)

然后运行`mvn flyway:migrate`即可自动化创建及初始化数据库的数据

## 抽离数据库操作方式为DAO

ORM（Object Relation Mapping）对象关系映射

因为每次都要手写JDBC真的很烦，我们可以通过ORM，将每张表映射到一个对象上

在引入MyBatis之前，先将目前的代码重构一遍，将数据库操作剥离出来，方便后面改写，这是[重构之后的代码](https://github.com/DeeJay0921/multithread-crawler-demo/commit/007623e298e573ef70e34be6d9def25ffadb0383)

重构之后的好处是，爬虫的逻辑和数据库的逻辑完全剥离开，可以写一个接口将数据库的操作多态化 

之后如果访问数据库的方式要发生改变（比如说切换数据库，使用MyBatis等），可以不涉及修改爬虫的逻辑，新的访问逻辑只需要实现数据库对应的接口即可。

通过抽取公共逻辑为一个接口，这是[重构的代码](https://github.com/DeeJay0921/multithread-crawler-demo/commit/7cd689e3478456d225d9962016667e0de4793de4)

## 改进数据库操作方式，抛弃JDBC，使用 MyBatis

[官方文档](https://mybatis.org/mybatis-3/)

导入maven依赖后，我们首先还是在`/src/main/resources`下创建一个`mybatis-config.xml`,具体的路径为`/src/main/resources/db/mybatis/mybatis-config.xml`

```
<environments default="development">
	<environment id="development">
		<transactionManager type="JDBC"/>
		<dataSource type="POOLED">
			<property name="driver" value="org.h2.Driver"/>
			<property name="url" value="jdbc:h2:file:./news"/>
			<!--<property name="username" value="${username}"/>-->
			<!--<property name="password" value="${password}"/>-->
		</dataSource>
	</environment>
</environments>
<mappers>
	<!--映射关系文件-->
	<mapper resource="db/mybatis/myMapper.xml"/>
</mappers>
```

指定了映射文件的路径之后，就可以在Mapper.xml里面写SQL了，在外部直接使用session调用即可。

> 注意，在`SqlSession session = sqlSessionFactory.openSession(true);`的操作中，所有对数据库造成更新的操作，都应该将`autoCommit`这个参数置为`true`

这里是[引入Mybatis之后的代码](https://github.com/DeeJay0921/multithread-crawler-demo/commit/7942cbb912e4deee544422e7d017168868a17c95)

## 将爬虫改为多线程的

经过上面的开发，爬虫基本可用，但是由于网络IO太慢了，需要将其改成多线程让CPU得到更多的利用。

我们直接将`Crawler`改为`Thread`的子类，通过外部调度即可。

这里要分析一下之前的代码: 

```
    @Override
    public String getNextLinkThenDelete() throws SQLException {
        String link;
        // 这里的openSession 的参数autoCommit一定要为true,否则每次的删除就没有被提交到数据库
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            link = session.selectOne(
                    "com.github.DeeJay0921.mybatis.selectNextLink"); // 这边输入Mapper.xml里面的命名空间加Select语句的id
            if (link != null) {
                session.delete("com.github.DeeJay0921.mybatis.deleteLink", link);
            }
        }
        return link;
    }
```
在上述方法中，取一个`link`出来再删掉的操作很明显不是一个原子操作，在多线程情况下，会出现多次重复删除等操作，所以需要设置锁，简单点设为`synchronized`方法即可。

> PS: 数据库操作天生就是线程安全的


[改为多线程的代码](https://github.com/DeeJay0921/multithread-crawler-demo/commit/8757b41d9173b0087cea614043fdeb029ac8147e)

# 当数据规模大了之后

当数据库存储数据达到一定规模之后，会出现一些性能问题

先来写一些代码用来模拟生成一些百万级别的数据：

[将代码修改为这样](https://github.com/DeeJay0921/multithread-crawler-demo/commit/a130c0ddd2d59781ce27c700c7d98ba3d460401e)

> 在上例中，我们在`news`类里新增了`createdAt`和`updatedAt`, 在mybatis中，默认是不会将其转为snake_case形式的，所以需要配置`mapUnderscoreToCamelCase`这个属性为`true`
> 另外，在mybatis的配置中，`settings`要放到最前面，否则会报错，[完整的配置列表在这](https://mybatis.org/mybatis-3/zh/configuration.html#settings)。

## 索引优化

### create index

现在我们拥有了很大的数据库，要执行一些查询的时候，如果使用的是主键查询，即id等的时候，查询还是很快的，例如：

```
select * from NEWS where id=123
```

查询仍然很快

但是我们做一点小改动，我们将`NEWS`表的`created_at`和`updated_at`列后面的时分秒去掉, 即将`2020-01-10 16:18:41.162189`这种数据改为`2020-01-10`

```
update NEWS set created_at = date(created_at), updated_at = date(updated_at)
```

如果当前数据库不支持date()方法的话，可以执行：

```
update NEWS
set CREATED_AT = TO_CHAR(CREATED_AT, 'yyyy-MM-dd'),
    UPDATED_AT = TO_CHAR(UPDATED_AT, 'yyyy-MM-dd')
```

> date()是sql内置函数， 可以将timestamp转为date, 也可以使用to_char

然后通过`created_at`作为索引执行查询操作，例如：

```
select * from NEWS where created_at = '2019-08-29'
```

这时候查询的动作就很慢了，

那么对于这种非主键的查询，我们可以给目标列建立一个索引，查看[官方文档](https://dev.mysql.com/doc/refman/8.0/en/create-index.html),在本例中，我们可以执行：

```
CREATE INDEX CREATED_AT_INDEX
ON NEWS (CREATED_AT)
```

其他参数都可以选择默认，然后执行一次，给表中的`CREATED_AT`都加上索引，等待都加好之后，可以执行:

```
show index from NEWS
```
可以看到建立的所有索引

再次执行：
```
select * from NEWS where created_at = '2019-08-29'
```
就可以看到查询速度显著提升了。

### explain语句分析sql

另外可以使用`explain`来解释当前语句将会以怎样的方式被执行，比如当我们运行：
```
explain select *
        from NEWS
        WHERE CREATED_AT = '1970-01-01'
```

就可以分析该sql，找到可以优化的点

> 关于explain可以看[这篇](https://www.jianshu.com/p/593e115ffadd)


### 联合索引

还可以为多个列增加联合索引，语法还是一样的：

```
CREATE INDEX CREATED_AT_AND_UPDATED_AT
ON NEWS (CREATED_AT, UPDATED_AT)
-- 一般的原则是，尽量修改原有的索引，然后再考虑新加索引
```

上述语句为`CREATED_AT`和`UPDATED_AT`添加了联合索引`CREATED_AT + UPDATED_AT`，我们这次执行查询：

```
SELECT * FROM NEWS WHERE CREATED_AT = '1970-01-06' AND UPDATED_AT > '1970-01-04'
```
sql查询遵循**最左匹配原则**，上述语句中`CREATED_AT = '1970-01-06' AND UPDATED_AT > '1970-01-04'`

左边的`CREATED_AT = '1970-01-06`这个相等语句，可以匹配到联合索引`CREATED_AT + UPDATED_AT`的左边的`CREATED_AT`,所以查询会比较快,执行`explain`上述语句时，发现该语句`type`为`ref`

但是当我们将sql写为：
```
SELECT * FROM NEWS WHERE CREATED_AT > '1970-01-05' AND UPDATED_AT = '1970-01-05'
```
时，左边语句没有匹配到索引，速度会明显降低。当我们执行`explain`上述语句时，也可以发现该语句`type`为`ALL`

所以我们在实际开发中，要根据具体的业务需要去建立索引，且索引也不是越多越好


## Elatsicsearch原理及初步使用


### 为什么需要Elatsicsearch

上面的例子都是针对索引优化的，但是如果想对于新闻的一些文本内容进行检索的话，sql是没有很好的支持的。

如果使用sql,我们只能写出像下面一样的sql:
```
SELECT * FROM NEWS WHERE CONTENT LIKE '%关键字%'
```

其检索速度非常的慢，因为数据库的长处在于对于非文本的一些数据索引检索。

而对于一些搜索引擎来讲，经常面临千亿万亿级别的文本搜索，这时候就需要Elasticseach

### Elatsicsearch 的原理

Elatsicsearch采用了倒排索引，想象一个场景，给你一个'月'字，让你想出在你脑海中记忆着的所有包含'月'字的古诗词。

传统的数据库检索方式只能将脑海中的所有诗词都遍历一遍，然后判断是否`contains('月')`, 但是倒排索引采用了一种方法

拿《静夜思》来举例子，倒排索引指的是，将"床前明月光"这里的每个字都建立一个索引，指向《静夜思》这个标题，即 ：
```
'床' --> 《静夜思》，
'前' --> 《静夜思》，
'明' --> 《静夜思》，
'月' --> 《静夜思》，
'光' --> 《静夜思》
// ...
```

这样建立了索引之后，如果有新的诗词比如《山居秋暝》，诗中有一句"明月松间照", 那么索引可以继续增加，
```
// ...
'月' --> [《静夜思》，《山居秋暝》]
// ...
```

那么这样一来，我们要根据'月'检索出所有脑海中的古诗词，就很快了，这就是Elasticsearch的原理倒排索引

### 使用Elasticsearch

关于Elasticsearch的一些基本使用，可以参见[Elasticsearch: 权威指南](https://www.elastic.co/guide/cn/elasticsearch/guide/current/foreword_id.html)

安装还是推荐docker安装，启动之后可以访问`http://localhost:9200/?pretty`即可看到ES的返回值了

> 传统的关系型数据库的结构： Databases --> Tables --> Rows --> Columns
> ES的结构：ES Cluster --> Indices --> Types(将被废弃) --> Documents --> Fields

首先还是需要向Elasticsearch中灌入数据，在文档中搜索Java Client来看相关操作，发现有[Java High Level REST Client](https://mvnrepository.com/artifact/org.elasticsearch.client/elasticsearch-rest-high-level-client)可以使用

采用的版本为7.3.1，文档在[这里](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.3/java-rest-high-document-index.html)

最终找到了[迁移指南](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.3/_changing_the_client_8217_s_initialization_code.html)

[初步MockData的代码(]https://github.com/DeeJay0921/multithread-crawler-demo/commit/817e20cb892b083c4ee89a39c51ccae5bae5a68c)

对于这种IO密集型的插入操作，可以考虑多开几个线程插入数据，效率会快一点，且同时可以使用ES的[Bulk批量请求操作](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.5/java-rest-high-document-bulk.html)

插入好数据之后，可以按照基本的文档进行操作，[Elasticsearch Getting Started](https://www.elastic.co/guide/cn/elasticsearch/guide/current/getting-started.html)

比方说直接访问`http://localhost:9200/news/_search?pretty`，发现就算数据量很庞大，ES也能很快的返回

可以按照关键字来进行搜索，例如：`http://localhost:9200/news/_search?q=title:%E4%B9%A0%E8%BF%91%E5%B9%B3`

还有一些条件搜索等，具体可以查看文档。

然后我们可以借助Elasticsearch实现一个简易的搜素引擎来搜索我们爬取到的数据, 搜索API可以见[这里](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.5/java-rest-high-search.html)

[简易的搜索引擎实现](https://github.com/DeeJay0921/multithread-crawler-demo/commit/39512cf1f4678d099291df084e8384ba8a7de385)