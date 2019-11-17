---
title: CSS居中
date: 2017/08/01 23:00:01
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS居中
<!--more-->

#居中的分类

###一，块级元素内的内联元素水平居中，text-align: center;
- div.box里面有文字，让文字在盒子内居中
```
<style type="text/css">
    .box {
        border: 1px solid red;
        text-align: center;
    }
</style>
<div class="box">
    hello deejay
</div>
```

- 盒子内部是按钮的情况
```
<style type="text/css">
    .box {
        border: 1px solid red;
        text-align: center;
    }
    
</style>
<div class="box">
    <a href="#">click me!</a>
    <a href="#">click me!</a>
</div>
```
###二，固定宽度的块级元素水平居中，margin:0 auto
```
<style type="text/css">
    .box {
        border: 1px solid red;
        text-align: center;   /*让a链接也居中*/
    }
    .box2 {
        width: 500px;
        border: 1px solid green;
        margin: 0 auto;   /*固定宽度的块级元素居中*/
    }
</style>
<div class="box">
    <div class="box2">
        <a href="#">Click me!</a>
    </div>
</div>
```

###三，盒子内部的文字垂直居中

可以设置padding，使得上下padding相等，就做到了垂直居中

```
<style type="text/css">
    .box{
        border: 1px solid red;
        padding: 30px 0; /*不设置高度，box里面的内容不管有多少行都是垂直居中的，只要设置上下padding相等。*/
    }
</style>
<div class="box">
    <p>hello deejay ello deejay</p>
    <p>hello deejay ello deejay</p>
    <p>hello deejay ello deejay</p>
</div>
```

- 针对单行文本的垂直居中
可以通过设置height=line-height来实现单行文本的居中


###四，绝对水平垂直居中
对于没有固定的宽高的父元素要进行子元素的垂直水平居中，可以采用绝对定位，left: 50%,结合负的margin-left来进行定位。

```
<style type="text/css">
    *{
        margin: 0;          /*初始化样式，不然body会有默认margin等*/
        padding: 0;
    }
    body,html{             /*box要宽高都设置100%全屏的话，要将其父元素都设置成宽高100%，本例中就是body和html*/
        width: 100%;
        height: 100%;
    }
    .box{
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.4);
        position: relative;
    }
    .box2{
        width: 500px;;
        height: 400px;
        background: green;
        position: absolute;
        left: 50%;
        margin-left: -250px;  /*自身width的一半*/
        top: 50%;
        margin-top: -200px;  /*同理，自身height的一半*/

    }
</style>
<div class="box">
    <div class="box2">
        <h1>hello,deejay</h1>
    </div>
</div>
```


- 上述例子中，子元素的宽高并没有固定的情况：

可以使用CSS3的属性transform: translate(-50%,-50%)；来进行居中

```
<style type="text/css">
    *{
        margin: 0;          /*初始化样式，不然body会有默认margin等*/
        padding: 0;
    }
    body,html{             /*box要宽高都设置100%全屏的话，要将其父元素都设置成宽高100%，本例中就是body和html*/
        width: 100%;
        height: 100%;
    }
    .box{
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.4);
        position: relative;
    }
    .box2{
        /*width: 500px;*/
        /*height: 400px;*/
        background: green;
        position: absolute;
        transform: translate(-50%,-50%);
        left: 50%;
        /*margin-left: -250px;  !*自身width的一半*!*/
        top: 50%;
        /*margin-top: -200px;  !*同理，自身height的一半*!*/

    }
</style>
<div class="box">
    <div class="box2">
        <h1>hello,deejay</h1>
    </div>
</div>
```
