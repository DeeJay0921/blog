---
title: 前端小tip
date: 2018/09/13 00:00:01
tags: 
- 前端
- 多行文本
categories: 
- 前端
---
前端小tip
<!--more-->

-  对于class来说，这个引号不是必须的，只有在有多个类的时候，中间有空格，""才是必须的。
```
        <div class=box></div>
        <div class="box2 deejay"></div>
```

- css单行文本省略
```
  div {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
    }
 /*给定宽度*/
```
- css 多行文本 
```
  p {
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
  }
```
