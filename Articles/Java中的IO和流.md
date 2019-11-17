---
title: Java中的IO和流
date: 2018/12/26 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- IO
categories: 
- Java
---
Java中的IO和流
<!--more-->

###I/O和流

- I/O： input/output
- 从读写设备，包括硬盘文件，内存，键盘输入，屏幕输出，访问网络
- 目的就是输入输出“内容”（字节或者文本）

- 流是对输入输出设备的一种抽象
- 从流中读取内容，输出内容到流中
- “linux中万物皆文件”

- 从程序的角度，就是对读写设备进行封装，比如：创建一个对象，然后调用方法读取（输出）内容，然后对象会更新当前文件的位置。
### 标准输入/输出流

- 标准输出流
  - System.out
  - System.out.println(...)
- 标准输入流
  - System.in

### I/O相关层次结构

- 字节流
- 字符流

### 字节流
- InputStream
  - System.in
  - FileInputStream
- OutputStream
  - System.out
  - FileOutputStream
- BufferedInputStream和BufferedOutputStream
- Stream用于直接处理“字节”

### 字符流
- Reader
  - InputStreamReader
    - FileReader
   - BufferedReader
    - bufferedReader.readLine()
- Writer
  - OutputStreamWriter
    - FileWriter
  - BufferedWriter
    - bufferedWriter.write(String);


## IOUtils
- IOUtils是Apache开源项目的一个很广泛使用的IO工具库
- 主要提供更高抽象程度的IO访问工具，方便写IO相关的代码
- 常用类：
  - FileUtils
  - Charset
  - DirectoryWalker
  - copyUtils

### 异常
- java异常是用来在正常程序运行流程中遇到异常情况，跳出正常运行流程，运行出错处理的一种机制。
- 异常类： new Exception()
- 异常捕捉语句
  - try{正常代码} catch(Exception e){错误处理代码}
### 异常和null
- Null是一个值，可以赋值给所有类型的变量
- 表达的是“空”，指这个变量不指向任何变量
- Integer x = null; Object obj = null;
- 常见异常NullPointerException
- 出现这个异常时，要检查变量是否指向null
