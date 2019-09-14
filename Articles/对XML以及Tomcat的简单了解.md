---
title: 对XML以及Tomcat的简单了解
date: 2019/04/22 00:00:01
tags: 
- Tomcat
- XML
categories: 
- Java
---
Java线程
<!--more-->

# XML
extensible markup language 可扩展标记语言。

## XML的作用
- 存储数据
- 作为配置文件
- 作为数据传输载体

## 定义XML
1. 文档声明： `<?xml version="1.0"?>`
> 指定encoding: `<?xml version="1.0" encoding="utf-8"?>`
还有standalone(yes/no)属性代表文档是否引用其他文件
2. 元素定义
- 元素即标签
- 必须要有一个根元素
- 可以写空标签，即自闭合标签
- 标签名可以完全自定义
3. 属性定义, 定义在元素内部，如`<stu id="111"></stu>`
4. 注释：同html

## CDATA区
所有 XML 文档中的文本均会被解析器解析。
只有 CDATA 区段（CDATA section）中的文本会被解析器忽略。
- 非法字符
严格地讲，在 XML 中仅有字符 "<"和"&" 是非法的。省略号、引号和大于号是合法的，但是把它们替换为实体引用是个好的习惯。

转义字符 | 符号 | 含义
-- | -- | --
`&lt;`  |	<  |  小于
`&gt;`  |	<  |  大于
`&amp;`  |	&  |  和号
`&apos;`  |	' |  省略号
`&quot;`  |	"  |  引号

术语 CDATA 指的是不应由 XML 解析器进行解析的文本数据（Unparsed Character Data）。

在 XML 元素中，"<" 和 "&" 是非法的。

"<" 会产生错误，因为解析器会把该字符解释为新元素的开始。

"&" 也会产生错误，因为解析器会把该字符解释为字符实体的开始。

某些文本，比如 JavaScript 代码，包含大量 "<" 或 "&" 字符。为了避免错误，可以将脚本代码定义为 CDATA。

CDATA 部分中的所有内容都会被解析器忽略。

CDATA 部分由 "<![CDATA[" 开始，由 "]]>" 结束：

## XML解析

获取元素里面的字符数据或者属性数据

分为DOM解析和SAX解析：

DOM解析： document object model,把整个xml全部读到内存当中，形成树状结构。整个文档称为document对象，元素为element,属性为attribute，文本为text。
> 如果xml过大，会造成内存溢出，但是可以对文档进行增删操作

SAX解析：Simple API for Xml, 基于事件驱动，读取一行解析一行。
> SAX不会造成内存溢出，但是相应的，也不可以进行增删，只能查询。

> 针对DOM解析和SAX解析的API: jaxp, jdom, dom4j

### dom4j

现有如下xml:
```
<?xml version="1.0" encoding="utf-8" ?>
<stus>
    <stu id="111">
        <name>Yang</name>
        <age>23</age>
    </stu>
</stus>
```
dom4j的代码使用举例:
```
package com;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SAXReader saxReader = new SAXReader(); // 1.创建SAX读取对象
        try {
//            指定解析的xml文件，并获取org.dom4j.Document对象
            Document document =  saxReader.read(new File("src/main/java/com/demo.xml"));
//            获取到org.dom4j.Document的实例对象,就可以使用其方法获取xml的信息
            System.out.println(document.getRootElement());// org.dom4j.tree.DefaultElement@3966ec04 [Element: <stus attributes: []/>]
            Element rootElement = document.getRootElement();
            System.out.println(rootElement.getName()); // 获取到root元素的name stus

            List<Element> elements = rootElement.elements(); // 返回根元素的所有直接子元素
            System.out.println(elements.size()); // 1
            for (Element element : elements) {
                System.out.println(element.getName()); // stu
            }

//            获取节点的信息
//            element()方法支持链式调用
            System.out.println(rootElement.element("stu").element("name").getText());// Yang
            System.out.println(rootElement.element("stu").element("name").getStringValue());// Yang
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```

### Xpath

[Xpath教程](http://www.w3school.com.cn/xpath/xpath_nodes.asp)

Xpath是xml的路径语言。支持在解析xml的时候，可以快速的定位到某一个层级比较深的元素。

> 使用Xpath可以避免上述例子一直链式调用`rootElement.element("stu").element("name")`的这种情况。

```
package com;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        SAXReader saxReader = new SAXReader();
        try {
            Document document =  saxReader.read(new File("src/main/java/com/demo.xml"));
            Element rootElement = document.getRootElement();

            Node node =  rootElement.selectSingleNode("//name");
            System.out.println(node.getText());// Yang
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```


值得一提的是：还需要导入额外的依赖:
```
<!-- https://mvnrepository.com/artifact/jaxen/jaxen -->
        <dependency>
            <groupId>jaxen</groupId>
            <artifactId>jaxen</artifactId>
            <version>1.2.0</version>
        </dependency>
```

### XML的约束
对于如下xml:
```
<?xml version="1.0" encoding="utf-8" ?>
<stus>
    <stu id="111">
        <name>Yang</name>
        <age>23</age>
    </stu>
    <stu id="111">
        <name>Yang</name>
        <name>Zhang</name>
        <name>Wang</name>
        <age>23</age>
    </stu>
</stus>
```
如果想规定id唯一不重复或者name属性只能有一个之类的约束的话，可以使用DTD或者schema
#### DTD
 [DTD教程](http://www.w3school.com.cn/dtd/dtd_intro.asp)

对于上述的xml，可以新建一个dtd文件，对其内部所有的元素做约束，
语法规则为: `<!ELEMENT 元素名 元素类型>` [DTD元素](http://www.w3school.com.cn/dtd/dtd_elements.asp)
对于元素的属性的约束语法规则为: `<!ATTLIST 元素名称 属性名称 属性类型 默认值>`[DTD属性](http://www.w3school.com.cn/dtd/dtd_attributes.asp)
新建一个stus.dtd,按如下规则规定约束：
```
<!ELEMENT stus (stu)+> // 代表stus标签下只能有stu标签 +代表有1个或者多个stu，相应的还有*(任意多个)？（0个或者1个）
<!ELEMENT stu (name,age)>
<!ELEMENT name (#PCDATA)>
<!ELEMENT age (#PCDATA)> // #PCDATA用于简单元素，即没有子标签的元素 详见
<!ATTLIST stu id ID> // 设置stu标签的id属性的类型为ID即不可重复
```
关于属性的类型约束: ID表示不可重复，CDATA表示是普通的文本
写好约束之后，在xml中引入dtd引入：
```
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE stus SYSTEM "stus.dtd">
<stus>
    <stu id="111">
        <name>Yang</name>
        <age>23</age>
    </stu>
    <stu id="111">
        <name>Yang</name>
        <name>Zhang</name>
        <name>Wang</name>
        <age>23</age>
    </stu>
</stus>
```
引入本地外部dtd的语法具体为：`<!DOCTYPE 根元素 SYSTEM "文件名">`
> 也可以不用新建文件，直接在xml内部直接引用 `<!DOCTYPE 根元素 [dtd内容]>`

> 如果要引入外部网络上的dtd约束的话，语法规则为：`<!DOCTYPE 根元素 PUBLIC dtd名称 dtd路径(url)>`

引入成功之后可以看到文件中不符合dtd约束的地方都被标红了。
#### schema
[schema教程](http://www.w3school.com.cn/schema/index.asp)

schema其实就是一个xml，使用xml的语法规则，解析起来比较方便，是为了替代DTD，但是schema约束文本内容比DTD的内容还要多，所以目前也没有真正的替代DTD

schema后缀为xsd文件。对于上述xml，新建一个xsd文件

```
<schema xmlns="http://www.w3.org/2001/XMLSchema"
targetNamespace="http://www.w3school.com.cn"
xmlns="http://www.w3school.com.cn"
elementFormDefault="qualified">

</schema>

```
关于头部属性：[xmlns,targetNamespace,elementFormDefault](http://www.w3school.com.cn/schema/schema_schema.asp)



# Tomcat

## web服务器软件

web服务器软件就是运行在服务器上的一个应用程序，负责下面的功能：
- 客户端输入指定url进行访问，web服务器接收请求响应消息
- 处理客户端的请求， 返回资源/信息

## Tomcat目录结构
bin: 包含了一些jar和脚本文件，如startup.bat
conf: tomcat的配置，server.xml,web.xml
lib: tomcat运行所需的jar包
logs: 日志文件
temp：临时文件
webapps：发布到tomcat服务器上的项目
work：jsp文件翻译成java文件存放地
### 发布项目到tomcat
> 需求：如何通过服务器访问到本地的资源stu.xml
1. 拷贝文件到webapps
2. 如果文件在webapps/ROOT下的话，重启tomcat，访问`localhost:8080/stu.xml`即可
3. 如果文件没放在ROOT文件夹下，那么新建一个文件夹xml，将stu.xml放到xml目录下,重启tomcat，访问`localhost:8080/xml/stu.xml`下即可访问到

上述文件涉及到拷贝文件，如果不想手动拷贝，可以去修改server.xml去配置虚拟路径
1. 打开server.xml，找到<Host>标签，在其后面追加一个`<Context docBase="D:\JavaDemos\src\com\company" path="/testPath"></Context>`
> docBase属性规定了要访问的文件的目录路径，而path规定了页面url访问时的路由地址
2. 重启tomcat，访问`localhost:8080/testPath/stu2.xml`，即可获取到文件

配置虚拟路径还有另外一种方式，详见tomcat Defining a context
1. 在$CATALINA_BASE/conf/[enginename]/[hostname]/目录下，默认为：[tomcat安装目录]/conf/catalina/localhost，新建一个xml文件，名称定义为person.xml
2. 编辑这个persom.xml，写入`<?xml version="1.0" encoding="utf-8" ?>`之后，加入上述配置虚拟路径<Context>的那个元素:`<Context docBase="D:\JavaDemos\src\com\company"></Context>`,但是注意要去掉Path
3. 重启tomcat，访问`localhost:8080/person/stu2.xml`即可获取到资源

> 其实一般使用最多的还是第一种手动拷贝，下面2种配置虚拟路径的方式其实过于繁琐了


### idea中配置Tomcat
安装完tomcat后，
1. 配置环境变量`CATALINA_BASE`: `D:\apache-tomcat-8.5.40       //Tomcat安装目录`
2. 配置环境变量`CATALINA_HOME`: `D:\apache-tomcat-8.5.40       //Tomcat安装目录`
3. 在ClassPath的变量值中加入：`%CATALINA_HOME%\lib\servlet-api.jar;`
4. 在Path的变量值中加入：`%CATALINA_HOME%\bin;%CATALINA_HOME%\lib;`
5. 在idea中设置edit configuration，新增tomcat server，server页选择本地安装目录，**并且要在deployment页面新增ROOT，否则默认不能访问8080**
