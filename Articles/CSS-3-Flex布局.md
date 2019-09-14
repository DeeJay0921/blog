---
title: CSS-3-Flex布局
date: 2017/08/04 17:00:01
tags: 
- 前端
- CSS
- Flex
categories: 
- 前端
---
CSS-3-Flex布局
<!--more-->

#Flex布局

flex布局可以实现空间自动分配自动对齐，适用于简单的线性布局。

### flex基本概念

![flex基本概念](http://upload-images.jianshu.io/upload_images/7113407-ad8776b7aadb6185.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1. flex container的属性
- flex-direction 方向（row和column）
- flex-wrap 换行（默认nowrap,设置wrap后换行）
- flex-flow 方向和换行的简写
- justify-content 主轴(main axis)方向对齐方式（space-between,space-around,flex-starr,flex-end,center）
- align-item 侧轴对齐方式 (stretch,flex-start,flex-end,center)
- align-content 多行/列内容对齐方式（不常见）

`2. flex item属性
- flex-grow 增长比例（空间过多时）
- flex-shrink  收缩比例（空间不够时）
- flex-basis 默认大小（一般不用）
- flex 上面三个的缩写
- order 顺序（代替双飞翼）
- align-self自身对齐方式


### 使用flex布局

- 手机页面布局（topbar+main+tabs）

```
<style>
    *{
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        list-style: none;
    }
    .container{
        height: 100vh;
        display: flex;
        flex-direction: column;
        /*使用列布局*/
    }
    .header{
        height: 100px;
        background: #98ff79;
    }
    .main{
        flex-grow: 1;
        /*将剩下的空间分配给main*/

        overflow: auto;
        /*中间内容超出时增加滚动条，不用给下面ul>li增加fix定位*/
    }
    .footer{
        height: 100px;
        background: #e69dff;
    }
    .footer ul{
        width: 100%;
        height: 100%;
        display: flex;
    }
    .footer ul li{
        width: 25%;
        height: 100%;
        border: 1px solid black;
    }
</style>

<div class="container">
    <div class="header">header</div>
    <div class="main">main</div>
    <div class="footer">
        <ul>
            <li></li>
            <li></li>
            <li></li>
            <li></li>
        </ul>
    </div>
</div>
```

- 产品列表（li*9）

```
<style>
    *{
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        list-style: none;
    }
    ul{
        display: flex;
        flex-wrap: wrap;
        width: 350px;
        margin: 0 auto;
        border: 1px solid red;
        justify-content: space-between;
    }
    li{
        width: 100px;
        height: 100px;
        background: #83ffe6;
        border: 1px solid black;
    }
</style>

<ul>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
    <li></li>
</ul>
```

- PC页面布局

```
<style>
    *{
        margin: 0;
        padding: 0;
    }
    .header{
        height: 50px;
        background: #a4ff6a;
    }
    .footer{
        height: 50px;
        background: #ff7cf4;
    }
    .content{
        display: flex;
    }
    .left{
        width: 200px;
        background: #c4beff;
    }
    .main{
        height: 400px;
        background: #ffb894;
        flex-grow: 1;
    }
    .right{
        width: 200px;
        background: #ff988a;
    }
</style>

<div class="header">header</div>
<div class="content">
    <div class="left">left</div>
    <div class="main">main</div>
    <div class="right">right</div>
</div>
<div class="footer">footer</div>
```


- 完美居中

```
<style>
    .parent{
        min-height: 400px;
        background: #efafff;
        display: flex;
        justify-content: center;
        /*主轴居中*/
        align-items: center;
        /*侧轴居中*/
    }
    .child{
        border: 1px solid red;
    }
</style>

<div class="parent">
    <div class="child">
        jdashfjasdfah覅好僵啊时代峰峻爱世界净空法师大恒科技凤凰健康设计费
    </div>
</div>
```
