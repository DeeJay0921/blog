---
title: CSS布局
date: 2017/08/04 04:00:01
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS布局
<!--more-->

###常见布局（PC）
-  固定宽度布局
简单但是窗口宽度小的时候会出现滚动条

- 弹性布局（fluid）
页面较为美观但是较为复杂

- 响应式布局——多终端（PC，pad，Phone）

###单列布局

 
![单列布局](http://upload-images.jianshu.io/upload_images/7113407-84dde0103f9dafdc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
-  没有通栏的情况

实现方法：定宽 width:设置固定值，或者设置max-width
然后设置margin-left和margin-right为auto
```
<style type="text/css">
    .layout{
        /*width: 900px;*/
        max-width: 900px;
        margin: 20px auto;
    }
    .header{
        background: #fff564;
        height: 30px;
    }
    .content{
        background: #43fff8;
        height: 500px;
    }
    .footer{
        background: #bbff80;
        height: 30px;
    }
</style>

<div class="layout">
    <div class="header"></div>
    <div class="content"></div>
    <div class="footer"></div>
</div>
```
width和max-width也有一定的区别，设置width当浏览器窗口过小时会出现滚动条.
上述代码html可以简化，省略主layout的div，将.layout的class加到里面子元素上也可以达到效果。
```
    <div class="header layout"></div>
    <div class="content layout"></div>
    <div class="footer layout"></div>
```
- 有通栏的情况
```
<style type="text/css">
    body{
        min-width: 960px;
    }
    .layout{
        width: 960px;
        /*max-width: 960px;*/
        margin: 0px auto;
        background: #ff32ea;
    }
    .header{
        background: #fff564;
        height: 30px;
    }
    .content{
        background: #43fff8;
        height: 500px;
    }
    .footer{
        background: #bbff80;
        height: 30px;
    }
</style>

    <div class="header">
        <div class="layout">header</div>
        <!--有通栏时不能同时应用header和layout，要加个div把header的内容包裹起来，下面footer同理-->
    </div>
    <div class="content layout">content</div>
    <div class="footer">
        <div class="layout">footer</div>
    </div>
    <!--当出现浏览器窗口宽度导致页面出现滚动条或者header长度不够等情况时，可以给body设置mid-width去除背景色的bug-->
```
- 内部元素水平居中
 
范例：
![内部元素水平居中](http://upload-images.jianshu.io/upload_images/7113407-6ff025ea697196bf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一般思路有：
```
.parent{
        text-align: center;
    }
    .child{
        display: inline-block;
        /*设置为inline-block的时候，一般元素的高度如果不相等的话，还得设置其vertical-align使其垂直方向居中*/
        /*IE6不支持display: inline-block; 可以写为display: inline;*zoom: 1;*/
        /**zoom: 1;用来触发hasLayout,使得元素具有block的性质*/
    }
```
或者可以使内部元素浮动，用一个元素包裹住，设置margin: 0 auto;

范例代码：
```
<style type="text/css">
    *{
        margin: 0;
        padding: 0;
    }
    .parent{
        width: 500px;
        margin: 50px auto;
        text-align: center;
        font-size: 0;
        /*消除inline-block的缝隙*/
    }
    .parent a{
        display: inline-block;
        /*设置为inline-block之后要注意inline-block的缝隙问题*/
        border: 1px solid #cccccc;
        color: #111;
        text-decoration: none;
        font-size: 16px;
        padding: 8px; 16px;
    }

</style>
    <div class="parent">
        <a href="#">HTML</a>
        <a href="#">CSS</a>
        <a href="#">JavaSctipt</a>
    </div>
```

###双列布局

一列固定宽度，另一列自适应宽度

![双列布局](http://upload-images.jianshu.io/upload_images/7113407-05781c0c02338ee0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 实现方法：浮动元素+普通元素margin
```
<style type="text/css">
    .clearfix:after{
        content: ' ';
        display: block;
        clear: both;
    }
    .aside{
        float: left;
        /*浮动元素+普通元素margin*/
        width: 200px;
        height: 500px;
        /*一般情况下高度不写死，是为了看代码效果写的height*/
        background: #4bff59;
    }
    .main{
        margin-left: 210px;
        /*浮动元素+普通元素margin*/
        height: 400px;
        background: #86cbff;
    }
    .footer{
        background: #ff9e44;
    }
</style>


<div class="content clearfix">
    <div class="aside">aside</div>
    <div class="main">main</div>
</div>
<div class="footer">footer</div>
```
而当aside在右边的时候，float:right，然后给main设置margin-right即可，但是这时候有一个需要注意的地方，就是**在写html结构的时候，必须要将aside的div写在main的div前面**，即：
```
<div class="content clearfix">
    <div class="aside">aside</div>
    <!--aside写在前面-->
    <div class="main">main</div>
</div>
<style type="text/css">
.aside{
        float: right;
        width: 200px;
        height: 500px;
        background: #4bff59;
    }
    .main{
        margin-right: 210px;
        height: 400px;
        background: #86cbff;
    }
</style>
```
因为如果**先写main的div的话，浏览器解析的时候，main是一个正常流块级元素，会独占一行，使得aside被挤到下一行去**。

- 两栏布局的flex写法
```
<style type="text/css">
    .content{
        max-width: 800px;
        margin: 0 auto;

        display: flex;
        /*应用flex*/
    }
    .aside{
        width: 200px;
        min-height: 500px;
        /*一般情况下高度不写死,是靠内容撑起来的，是为了看代码效果写的height*/
        background: #4bff59;
    }
    .main{
        min-height: 400px;
        background: #86cbff;

        flex-grow: 1;
        /*撑开main*/
    }
    .footer{
        background: #ff9e44;
    }
</style>
<div class="content">
    <div class="aside">aside</div>
    <div class="main">main</div>
</div>
<div class="footer">footer</div>
```

### 三栏布局

两侧两列宽度，中间自适应宽度

![三栏布局](http://upload-images.jianshu.io/upload_images/7113407-af32bdad5297a02e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```
<style type="text/css">
    .clearfix:after{
        content: " ";
        display: block;
        clear: both;
    }
    .left{
        width: 200px;
        height: 500px;
        /*同样写死height是为了体现效果*/
        background: #62fff7;
        float: left;
    }
    .right{
        width: 200px;
        height: 500px;
        background: #ffe767;
        float: right;
    }
    .main{
        margin-left: 210px;
        margin-right: 210px;
        /*设置左右margin留出部分空隙*/
        height: 400px;
        background: #a4ff64;
    }
    .footer{
        background: #f2ceff;
    }
</style>

<div class="content clearfix">
    <div class="left">left</div>
    <div class="right">right</div>
    <!--同理，写html的时候，还是要将浮动的写到前面，正常普通流块级元素写到后面-->
    <div class="main">main</div>
</div>

<div class="footer">footer</div>
```

写三栏布局的时候，还是要注意html中left,rigfh和main的先后顺序，要**考虑到浏览器的渲染顺序，将普通流的块级元素写在后面**。

- 三栏布局的flex实现方法：

```
<style type="text/css">
    .content{
        max-width: 960px;
        margin: 0 auto;
        display: flex;
        /*应用flex*/
    }
    .left{
        width: 200px;
        height: 500px;
        /*同样写死height是为了体现效果*/
        background: #62fff7;
        order: 1;
        /*排第一列*/
    }
    .right{
        width: 200px;
        height: 500px;
        background: #ffe767;
        order: 3;
        /*排第三列*/
    }
    .main{
        height: 400px;
        background: #a4ff64;
        flex-grow: 1;
        /*宽度没给，所以给一行的宽度*/
        order: 2;
        /*排第二列*/
    }
    .footer{
        background: #f2ceff;
    }
</style>

<div class="content">
    <div class="left">left</div>
    <div class="right">right</div>
    <div class="main">main</div>
</div>

<div class="footer">footer</div>
```
