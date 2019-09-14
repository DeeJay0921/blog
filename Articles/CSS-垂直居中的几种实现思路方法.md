---
title: CSS-垂直居中的几种实现思路方法
date: 2017/08/06 23:00:01
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS-垂直居中的几种实现思路方法
<!--more-->

1. 上下padding相等

```
<style>
    .content {
        width: 400px;
        border: 1px solid red;
    }
    .content p {
        padding-top: 30px;
        padding-bottom: 30px;
    }
</style>
<div class="content">
    <p>hello,deejay</p>
</div>
```

2. 绝对定位实现垂直居中（定宽高用负margin，不定宽高用transform）
```
<style>
    html,body {
        width: 100%;
        height: 100%;
    }
    .parent {
        position: relative;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.4);
    }
    .child {
        position: absolute;
        top: 50%;
        left: 50%;
        padding: 8px 16px;
        transform: translate(-50%,-50%);
        background: #ff5ce3;
    }
</style>


<div class="parent">
    <div class="child">hello deejay</div>

```

3. vertical-align实现居中（vertical-align: middle），作用于**行内元素**和**表格**，可以设置一个before伪类，display为inline-block,然后给要居中的元素和这个before都设置vertical-align: middle,将before高度设为100%，自然居中

```
<style>
    .parent {
        border: 1px solid;
        width: 400px;
        height: 400px;
    }
    .parent:before {
        content: '';
        display: inline-block;
        height: 100%;
        vertical-align: middle;
    }
    .child {
        display: inline-block;
        padding: 8px 16px;
        background: #b3fdff;
        vertical-align: middle;
    }
</style>

<div class="parent">
    <div class="child">hello deejay</div>
</div>
```

4. table-cell实现居中 （将父元素display为table-cell,设置vertical-align: middle;）


```
<style>
    .parent {
        width: 400px;
        height: 400px;
        border: 1px solid;
        display: table-cell;
        vertical-align: middle;
    }
    .child {
        padding: 8px 16px;
        background: #b3fdff;
        display: inline-block;
    }
</style>
<div class="parent">
    <div class="child">hello deejay</div>
</div>
```
