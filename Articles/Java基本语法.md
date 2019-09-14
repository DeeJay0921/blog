---
title: Java基本语法
date: 2018/12/24 00:00:01
tags: 
- Java
categories: 
- Java
---
Java基本语法
<!--more-->

# 变量和控制语句
## 基本数据类型
- 整数
> byte(1byte): -128 ~ +127
short(2bytes)
int(4bytes)
long(8bytes)
unsigned byte(1byte): -128 ~ +127
unsigned short(2bytes)
unsigned int(4bytes)
 unsigned long(8bytes)
- 浮点数
>float(4bytes) 精度：8,23
double(8bytes) 精度：11,52
- 字符
>char
- boolean
> true
false


# Java中整数相除的问题：
先来看一段代码
```
public class Main {
    public static void main(String[] args) {
        System.out.println(divide(3, 2));  // 1.0
    }

    public static double divide(int a, int b) {
        return a / b;
    }
}
```
输出的结果居然是1.0而不是1.5。
原因分析：
- **Java中的除法是地板除**
- 两个int值相除，得到的结果也是int值，int值赋值给double类型没问题，所以上述代码其实只是拿到了结果的int部分转为了double return了。

那怎么改才能显示正确呢？2种方法：
核心思路都是将其中一个值转为double再参与运算
1. 计算的时候带个1.0 强制其中一个值转为double
```
public class Main {
    public static void main(String[] args) {
        System.out.println(divide(3, 2)); // 1.5
    }

    public static double divide(int a, int b) {
        return 1.0D * a / b;
    }
}
```
2. 
```
public class Main {
    public static void main(String[] args) {
        System.out.println(divide(3, 2)); // 1.5
    }

    public static double divide(int a, int b) {
        return (double) a / b;
    }
}
```

## 变量和赋值
- 变量是对一个java对象的引用
- 声明一个变量时，需要指定其类型，表示他可以引用的对象类型
- `int a;` `long b;` `String str;`
- 赋值是把一个对象绑定到一个变量，即让一个变量引用到一个对象。`int a = 1;`

## 控制流程语句
- if
- if else 
- 代码块： 使用{}包括起来的单个或者多个语句或者表达式

- 循环语句
  - for
    - for(initial; condition; iteration) {}
    - for(T iterator : Collection<T>){}
  - while
    - while(condition){}
- 执行控制语句
   - return 从方法中返回
  - break 退出当前循环
  - throw 抛出异常
- 构造语句
   - new 生成一个对象
    - `Integer a = new Integer(3);`

## 类型模型
- 类： 一个模板，描述一类对象的行为和状态
- 对象： 类的一个实例，有具体的状态
- 方法：类中定义的该类的实例对象所具有的行为
- 静态方法： 隶属于类本身的方法

Java中的常量和变量都可以对应于一个对象，这个对象的行为和具有的属性是由类来定义的

Java程序的基本结构就是一系列类的定义，类之间的关系和具体类的对象之间的互相操作

## Java类
- 类是一个模板，它描述一类对象的行为和状态
- 语义上表达的是一类实体（对象）的抽象
- 在java中的惯例是一个文件中存放一个public类

## java对象
- 是类的一个实例
- 语义上是表达一个类的实体
- 对象是java的一等公民
- 对象可以赋值到一个变量
- 对象通常是通过`new`操作符进行创建

## java方法和成员变量
- 方法： 类中定义的该类的实例对象所具有的行为
- 静态方法： 隶属于类本身的方法
- <返回类型>方法名(<参数类型>参数名...){代码; return <返回值>;}
- 一个方法是由方法名和参数类型确定的
- 相同的方法名，参数类型不同，是不同的方法（重载）
- 成员变量： 对象或者类中的变量，其作用域在类或者对象中
 - 可以使用修饰符控制方法和成员变量的作用域

来看如下例子：
现有如下class
```
package com.DeeJay;

public class Human {
//    static变量隶属于类
    public static int Number = 0;

//    成员变量
    public String name;
    public int weight;

//    无参构造
    public Human () {
        this.name = "default Name";
        this.weight = 100;
    }
//    有参构造
    public Human (String name, int weight) {
//        这里的this指的是要new出来的那个对象
        this.name = name;
        this.weight = weight;
    }

//    普通方法
    public void rename(String newName) {
        this.name = newName; // 这里等价于 name = newName; 因为没有歧义
    }
//    普通方法
    public int workout() {
        this.weight -= 10;
        return this.weight;
    }

//    特殊的方法 （可以在方法中使用构造函数）
    public Human newHuman () {
        return new Human("jason", 120);
    }
}
```
我们在main中使用这个类：
```
public class Main {

    public static void main(String[] args) {
        Human jeff = new Human("jeff", 150);

        Human bob = jeff; // 可以直接将new好的对象赋值个一个新变量 从此这两个变量引用的就是同一个对象了
        System.out.println(bob.name); // jeff
        System.out.println(jeff.name);// jeff
        bob.workout();
        System.out.println(bob.weight); // 140
        System.out.println(jeff.weight); // 140
//       2次console之后  值是一样的  说明了是同一个变量

//        来看static变量
        Human.Number += 99;
        System.out.println(Human.Number); // 建议写法 99
        System.out.println(jeff.Number); // 不建议 99
        System.out.println(bob.Number); // 不建议 99
//        这三个值其实是一个值，都代表的是Human这个类的static变量

    }
}
```

## 修饰符
- 修饰符用于控制变量、类的作用域和一些访问限制
- 访问权限的修饰符有：`public` `protected` `private` `default(即不设置)`
![修饰符 ](https://upload-images.jianshu.io/upload_images/7113407-d1f307a8c8fd96b3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

>一般定义类时，成员变量都为private修饰，通过方法去访问变量
方法一般还是public修饰

- `static`
有一个比较特殊的修饰符：`static`,把方法或者成员变量设置为**类共享**,具有static修饰的方法或者变量的类，称为**静态类**。调用方法<类名>.<方法名或者变量名>,例如`Human.number`。

- `final`
  - final<cls>**防止对象被继承**
  - final<变量> 防止变量被修改引用到另外一个对象，即为**常量**
  - final<方法> 防止方法被override

来看例子：
- 使用get set访问变量,便于权限控制
```
public class getterSetter {
    private  String innerVar;

    public void setInnerVar(String varName) { // 通过setter赋值
        innerVar = varName;
    }

    public String getInnerVar() { // 通过 getter访问
        return innerVar;
    }
}
```

## 包和层次结构
- 包用来表示具有层次结构的命名空间
- 层次结构
  - 包 ->类（对象）-> 方法 -> 代码块
