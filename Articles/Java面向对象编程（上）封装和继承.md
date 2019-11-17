---
title: Java面向对象编程（上）封装和继承
date: 2019/02/08 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java面向对象编程（上）封装和继承
<!--more-->

## java类型模型
- 类： 类是一个模板，描述一类对象的行为和形态；
- 对象： 对象是一个类的实例，有具体的状态；
- 方法： 类中定义的该类的实例对象所具有的行为；
- 静态方法: 隶属于类本身的方法；
>Java中的常量和变量都可以对应于一个对象，这个对象所具有的行为和所具有的属性是由类来进行定义的。

>Java程序的基本结构就是一系列类的定义，类之间的关系和具体类的对象之间的互相操作。

## 封装 
- 类和外部的关系
- 在面向对象程式设计方法中，封装是指将类的实现细节部分包装，隐藏起来的方式
- 封装方式： 类
- 对象的内部状态的访问进行控制，只提供该提供的信息
- 把代码分为2个部分：接口和实现
- 接口因为涉及到和外部的交互，对用户暴露，所以应该保持稳定，例如API和库
- 内部实现不要暴露给用户，在接口功能不影响的前提下，可以随意修改和重构
- 良好的封装是解耦的基础
>写代码时一开始就要从封装的角度来进行思考，将代码分为暴露出来的接口和内部的具体实现。该暴露的暴露出去，不该暴露的就不暴露(一般从private开始写起，如果需要暴露再改)。

main.java:
```
package com.DeeJay;


import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileTest file = new FileTest();
        System.out.println(file.console("src/com/DeeJay/test.txt")); // Breaking News!
    }

}

```
FileTest.java
```
package com.DeeJay;

import java.io.*;

public class FileTest {
    private String title; // 这边将不需要暴露出去的变量隐藏掉

    public String console (String fileUrl) throws IOException { // 外部只需要通过调用console方法来获取相应信息
        BufferedReader bf = new BufferedReader(new FileReader(new File(fileUrl)));
        this.title = bf.readLine();
        return this.title;
    }
}

```

# 封装的概念
封装指的是将类内部的实现细节包装，隐藏起来的方式
# 封装的好处
- 安全性： 例如对于类内部的成员变量进行私有化，设置getter setter等限制外部对其进行直接修改
- 便捷性： 对于一些重复逻辑，可以写一个方法将其封装起来多次调用即可。
- 降低代码耦合程度
- 对于类的内部代码可以随意修改，只需要保证对外接口的一致性即可。
## 继承 
- 类和下一级类的关系
- 继承是面向对象编程技术的基石，因为它允许创建分等级层次的类封装方式
- 子类继承父类的特征和行为，使得子类对象具有父类对象的特征和方法
- 继承需要符合一个关系，子类是更加具体的父类
- 在声明子类时，通过关键字`extends`表达继承
>一般写代码时，一开始是没法知道整个完整的分层结构的，对于不同的东西有不同的处理逻辑，当写完代码发现2者之间有部分重复的逻辑时，即可以将其重复的代码提取出来，作为父类，然后让这2者继承即可。

# 继承代码举例
来看继承的一个例子： 现有一个News.java类，只允许通过构造方法来进行传入标题和内容, 用户通过调用display方法获取到信息。
```
package com.DeeJay;

public class News {
    private final String title;
    private final String content; // 不对外暴露成员变量
//    设置为final是为了达到只在构造函数中改变成员变量的目的
//    设置为final之后  不可以通过设置setter来修改成员变量

//    将构造的自由和责任交给用户
    public News(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

//    控制如何显示  这部分是开发自己负责的
    public String display() {
        return  title + "\n" + content;
    }
}

```
然后我们现在需要从其他来源中获取标题和内容时，我们要对News这个类进行拓展，就可以写一个子类进行扩展，现在来定义一个FileNews类来支持从文件中获取标题和内容。

写的时候我们发现，在子类中无法获取到父类的成员变量title和content，这是因为我们将其设为了private，此时要进行扩展，需要获取到成员变量，即需要将其改为protected, 同时取消掉final.
News.java
```
    protected String title;
    protected String content; //  子类需要继承到成员变量  所以需要修改为protected 
```
>对于可能被继承的类来说，如果子类需要获取到父类的成员变量，那么定义父类的时候要考虑写成protected

这时候将子类代码写完
FileNews.java
```
package com.DeeJay;

import java.io.*;

public class FileNews extends News {
    public FileNews() {}

    public FileNews(String title, String content) {
        super(title, content); // 对于父类有参的构造方法  必须显示的调用super
    }

    public void readFromFile(String fileUrl) throws IOException { // 从文件中进行读取
        BufferedReader bf = new BufferedReader(new FileReader(new File(fileUrl)));
        title = bf.readLine(); // 继承自News 成员变量也继承了过来  可以直接使用
        content = bf.readLine();
    }
}


```
进行调用试试看：
```
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileNews news = new FileNews();
        news.readFromFile("src/com/DeeJay/test.txt");
        System.out.println(news.display()); // FileNews类继承自News类，所以具有display方法，可以直接调用
    }

}
```
当然子类也可以override父类的方法：
```
    @Override
    public String display() {
        return "subTitle: " + title + "\n" + "subContent: " + content;
    }
```
再次运行就可以看到输出已经改变了。
### 继承的一些特性
1. 子类会拥有父类的**非private**的成员变量和方法
2. 子类可以拥有自己的成员变量和方法，即可以对父类进行扩展
3. 子类可以重新实现父类的方法，即`override`
4. java中只支持单继承
5. `super`关键字:  可以通过super关键字来实现对于父类成员的访问，用来引用当前对象的父类
6. `this`关键字：指向自己的引用

### 构造方法
1. 子类**不可以继承**父类的构造方法，如果父类的构造方法带有参数，则必须在子类的构造函数中显示的通过`super`关键字调用父类的构造函数并配以相应的参数。
2. 如果父类构造函数中没有参数，那么子类的构造函数中可以不用显示的使用`super`来调用父类的构造函数，系统会自动调用父类的无参的构造函数。
