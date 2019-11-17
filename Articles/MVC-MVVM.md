---
title: MVC-MVVM
date: 2017/10/10 00:00:01
cover: http://frugalisminds.com/wp-content/uploads/2018/01/porwnanie-architektur-mvvm-i-mvc-ios-8-638.jpg
tags: 
- 前端
- MVC
- MVVM
categories: 
- 前端
---
MVC-MVVM
<!--more-->

[阮一峰谈MVC](http://www.ruanyifeng.com/blog/2007/11/mvc.html)
[阮一峰讲MVC,MVP,MVVM](http://www.ruanyifeng.com/blog/2015/02/mvcmvp_mvvm.html)
#MVC
MVC模式认为，程序不论简单或复杂，从结构上看，都可以分成三层。

- 1，最上面的一层，是直接面向最终用户的"视图层"（View）。它是提供给用户的操作界面，是程序的外壳。
- 2，最底下的一层，是核心的"数据层"（Model），也就是程序需要操作的数据或信息。
- 3，中间的一层，就是"控制层"（Controller），它负责根据用户从"视图层"输入的指令，选取"数据层"中的数据，然后对其进行相应的操作，产生最终结果。

这三层是紧密联系在一起的，但又是互相独立的，每一层内部的变化不影响其他层。每一层都对外提供接口（Interface），供上面一层调用。这样一来，软件就可以实现模块化，修改外观或者变更数据都不用修改其他层，大大方便了维护和升级。

个人的简单理解：
- Controller： controller中负责监听view层中的事件，然后调用model中暴露出来的借口来进行改变数据，在controller中**不能直接操作model中的数据**。
- model： model中就是只操作数据，和dom没有关系。进行数据的操作，然后暴露出一些接口给controller使用。
- view： 视图层中的html以及占位符（例如{{number}}）。

要注意的就是**view和model层并不能直接相互控制，而是要交给controller**来进行控制。
MVC中一定会有一个主动render()的操作，因为不会自动刷新页面

#MVVM
M: js内存  （deejay）
V:  html/css 表现层
VM:  监听model和view的变化，model发生改变之后随着改变view,view改变了之后随着改变Model，互相监听，互相去改变。


##MVVM的缺点
首先明确**MVVM的改变是同步的**，另外**普通DOM的事件处理也是同步的**。
同步造成的缺点就是：view每改动一次，model就会改变 ，VM中的每一次变化也都会重新渲染view，所以造成很大的性能问题。
而且如果view中有新增内容的话，model中没有事先设置，是不能进行改变的。

