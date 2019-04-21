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
