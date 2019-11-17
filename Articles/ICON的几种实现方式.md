---
title: ICON的几种实现方式
date: 2017/08/01 04:22:24
cover: https://www.iconsdb.com/icons/download/black/info-512.gif
tags: 
- 前端
- Icon
categories: 
- 前端
---
ICON的几种实现方式
<!--more-->


需求：一个页面上有很多小图标


###一，image

使用image来实现，要注意的问题：
1. img的大小设置
2. img的vertical-align
3. 请求数过多

代码举例：
```
<style type="text/css">
    img{
        height: 5px; /*根据需求写*/
        /*写完后发现icon的高度自使用，但是跟输入框有点对不齐，可以设置icon的vertical-align来调整使其对齐*/
        vertical-align: middle; /*也可能是top,bottom等。*/
    }
</style>

    <div class="search">
        <input type="text">
        ![](目标icon文件)
    </div>
    <div class="buy">
        <button>加入购物车</button>
        ![](目标icon文件)
    </div>
```

###二，CSS Sprites

[线上精灵图合并地址](https://www.toptal.com/developers/css/sprite-generator)

[线上精灵图合并地址2](http://www.cn.spritegen.website-performance.org/)

解决了image方法过多次的请求

- 缺点：无法缩放，不好修改


###三，Icon Font



把字体做成图标
1. 制作字体文件 [iconfont网站](http://www.iconfont.cn/)

2. 声明font-family
- 使用本地链接
- 使用第三方链接
3. 使用font-family

- 使用HTML实体
- 使用css: before。


###四，CSS Icon

用CSS画
[css icon网站](http://cssicon.space)

###五，SVG

img=svg
svg "sprites"
