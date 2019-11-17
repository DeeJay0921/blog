---
title: Java中的一些基础知识点整理
date: 2018/07/21 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- Java数据类型
categories: 
- Java
---
Java中的一些基础知识点整理
<!--more-->

### Java中的数据类型：
- 基本数据类型：
    - 整数: byte, short, int(default), long
    - 浮点数: float, double(default)
    - 字符： char
    - 布尔： boolean 
- 引用数据类型：
    `Xxx x = new Xxx();` 都是引用类型

### 基本数据类型的转换：

- 隐式转换(自动类型转换):
    - 转换公式： byte, short, char ===> int ===> long ===> float ===> double， 由小到大
    ```
      byte b = 10;
      int a = b;
    ```
- 显式转换(强制类型转换):
    - 一般是由大到小
    ```
      int a = 10;
      byte b = (byte)a; // 有可能损失精度
    ```


### 运算符

- 算术运算符
    注意`++`和`--`
- 比较运算符
结果都是boolean类型
- 赋值运算符
`+=`隐含了强制类型转换
    ```
    int a = 10;
    byte b = 20;
    b += a; // 隐含了强制类型转换
    ```
- 逻辑运算符
连接boolean类型的表达式
注意`&&`/`||` 和 `&`/`|`的区别，前者如果第一个表达式就已经可以确定结果时，第二个表达式不再进行执行。

### 方法
- 定义格式：
修饰符(例如public static)   返回值类型    方法名(参数类型  参数名) { 方法体 }
- 调用
    - 有明确返回值
        - 赋值调用
        - 输出调用
        - 直接调用
    - 无明确返回值
        - 直接调用

- 重载
重名方法，参数个数或者参数类型不同，都可以视为不同方法，和返回值无关。

### 数组
- 特点
   类型必须一致，
    元素有整数索引
    一旦定义好长度则长度无法改变
    和集合相比，数组可以存基本类型，也能存引用类型，集合只能存对象。

## Switch语句的注意点
1. 可以Switch的那些类型？：
    - int/long/char/byte/short
    -  enum
    - 在JDK7+版本中 也可以Switch String
2. Switch的穿透
switch语句中可以多个case连写，如果不写break的话就会一直向下执行。
3. Switch中的作用域
在swtich里，如果case后面没写`{}`的话，作用域就是几个case最顶层的switch的那一层的`{}`，但是如果case之后也写了`{}`的话，那作用域就是这层的`{}`
