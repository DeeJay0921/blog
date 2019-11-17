---
title: CSS常见样式
date: 2017/07/31 21:22:24
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS常见样式
<!--more-->

###一，块级元素和行内元素分别有哪些？动手测试并列出4条以上的特性区别
- 块级元素(block-level element)
div h1 h2h h3 h4 h5 h6 p hr
form ul dl ol pre table
li dd dt tr td th

- 行内元素(inline-level element)
em strong span a br img
button input label  select textarea
code script

-区别
1. 块级可以包含块级和行内元素，行内只能包含文本和行内
2. 块级占据一整行空间，行内占据自身宽度空间
3. 宽高只对块级元素设置生效，对行内样式不起作用
4. 对于块级元素来说，可以给其设置margin,padding，而对于行内元素来说，对于margin-top,margin-bottom,padding-top,padding-bottom是**不生效**的，只有**左右外内边距才生效**。



###二，什么是 CSS 继承? 哪些属性能继承，哪些不能？

- css继承
css继承就是子标签继承了上级标签的css样式的属性

- 能继承的属性
color font-size font-family font-weight line-height list-style text-indent text-align text-transform letter-spacing
- 不能继承的属性
display border margin padding background height min-height max-height
width min-width max-width overflow position left right top bottom z-index float clear table-layout vertical-align 


###三，如何让块级元素水平居中？如何让行内元素水平居中?

- 块级元素居中
margin: 0 auto;

- 行内元素居中

给包含行内元素的**块级元素**应用text-align,例如
```
<style type="text/css">
    .box{
        text-align: center;
    }
</style>

<div class="box">
    <span>hello deejay!</span>
</div>
```

###四，用 CSS 实现一个三角形
```
<style type="text/css">
    .box{
        width: 0;
        height: 0;
        border-top: 30px solid red;
        border-right: 30px solid transparent;
        border-bottom: 30px solid transparent;
        border-left: 30px solid transparent;
    }
</style>
<div class="box"></div>
```
将width和height设为0，利用border来实现


![css实现三角形](http://upload-images.jianshu.io/upload_images/7113407-44a945179825b089.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


###五，单行文本溢出加 ...如何实现?

```
.box{
        white-space: nowrap;   /*不折行*/
        overflow: hidden;   /*超出部分隐藏*/
        text-overflow: ellipsis;  /*文本溢出之后用...代替*/
    }
```


###六，px, em, rem 有什么区别

px:固定单位
em:相对单位，相对于**父元素**字体大小
rem:相对单位， 相对于**根元素（html）**字体大小

```
<style type="text/css">
    
    .em span{
        font-size: 2em;
    }
    .rem span{
        font-size: 2rem;
    }

</style>

<div class="em">
    <span>deejay</span>
</div>
<div class="rem">
    <span>deejay</span>
</div>
```
对于.em>span来说，他的父元素为.em的div，默认字体大小为16px,给其设置2em，则现在大小为32px，当父元素大小改变时也会相应的改变。
对于.rem>span,由于其是相对于html的，所以不会产生变化。



###七，解释下面代码的作用?为什么要加引号? 字体里\5b8b\4f53代表什么?
```
body{
  font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;
}
```
使用浏览器打开页面时，会读取HTML文件进行解码渲染，当读到文字时会转换成对应的unicode码，然后根据HTML文件中的设置的font-family去查找对应的字体文件。找到文件之后依据unicode码去查找绘制外形到页面上。

在CSS中设置字体时，直接写字体中文或英文名称浏览器都能识别，直接写中文的时候编码不匹配的时候会产生乱码。所以为了保险起见，我们将**字体名称用Unicode码来表示**。

上述代码中，加引号的作用是因为所选字体之间有空格，不加的话会被当成多个对象。
\5b8b\4f53代表的是“宋”和“体”。
