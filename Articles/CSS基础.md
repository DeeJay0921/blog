---
title: CSS基础
date: 2017/07/30 11:22:24
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS基础
<!--more-->

###一. CSS的全称是什么?
CSS的全称是Cascading Style Sheets，层叠样式表。


###二.CSS有几种引入方式? link 和@import 有什么区别?
- 浏览器默认样式
即 user agent stylesheet
- 内联样式
`<h1 style="color: red; font-size: 20px;"></h1>`

- 内部样式
```
<style type="text/css">
  h1 {
    color: red;
    font-size: 20px;
  }
  </style>
  <h1>hello,deejay!</h1>
```

- 外部样式
1. <link>标签 (一般建议放到<head>标签中)
```
<head>
  <link rel="stylesheet" href="index.css" type="text/css">
</head>
```
2. @import
 ```
  <style>
  @import url("hello.css");
  @import "world.css";
  </style>
```
3. <link>和@import的区别
- 本质区别
<link>标签本质上就是一个HTML标签，跟普通的<h1>,<p>等一样，而@import是一种css语法，是css的语法，而不属于HTML。
- 用法区别
@import是要放到<style>标签中或者外部css文件中的。而<link>标签可以放到html的任何地方。
- 加载顺序区别
<link>标签引用的CSS文件在网页加载同时就会加载，而@import引入的CSS文件会等到页面被加载完成后才开始加载。
- 兼容性区别
@import在是相对较新的属性（在CSS2.1），对于IE5以下的浏览器不支持。
- DOM操作区别
用@import引入的样式不可以通过DOM去改变。<link>标签的可以。

###三.以下这几种文件路径分别用在什么地方，代表什么意思?
- css/a.css
相对路径，引入**当前目录下**的css文件夹中的a.css样式表。

- ./css/a.css
相对路径，引入当前目录下的css文件夹中的a.css样式表。

- b.css
相对路径，引入**同级**目录下的b.css样式表

- ../imgs/a.png
相对路径，引入**上一级**的imgs文件夹中的a.png图片

- /Users/hunger/project/css/a.css
绝对路径，以/开头的表示要引入的css文件在本地的绝对路径。

- /static/css/a.css
网站路径，表示在主域名下的CSS文件的路径。

- http://cdn.jirengu.com/kejian1/8-1.png
网站路径

###四.如果我想在js.jirengu.com上展示一个图片，需要怎么操作？
- 先将本地图片上传到某个地方例如微博微信等，生成一个线上的地址，然后进行引用。

###五.列出5条以上html和 css 的书写规范
- 语法不区分大小写，但是建议**统一小写**。
- **不使用内联的style**属性定义样式。
- id和class使用有意义的单词，分隔符建议使用-，不建议使用下划线。
- 属性值是0的**省略单位**。
- 块内容缩进。
- 属性名**冒号后面添加一个空格**。

###六.对Chrome开发者工具的了解

![开发者调试工具](http://upload-images.jianshu.io/upload_images/7113407-1b9dc2e42c21520b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在element区域中，可以看到页面中所有的元素，并且可以操作相应的元素。在style区域中可以看到页面元素应用的样式，element.style是元素的内联样式。inherited from ...是代表从父元素继承过来的样式。同时也可以操作css样式来改变页面元素样式。
![开发者调试工具](http://upload-images.jianshu.io/upload_images/7113407-17a87ceea8dd5e1f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

