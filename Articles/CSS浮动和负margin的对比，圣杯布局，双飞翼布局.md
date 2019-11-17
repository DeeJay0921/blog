---
title: CSS浮动和负margin的对比，圣杯布局，双飞翼布局
date: 2017/08/05 00:00:01
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS浮动和负margin的对比，圣杯布局，双飞翼布局
<!--more-->

### 浮动 VS 负margin

对于相邻的两个浮动元素，如果因为空间不够导致第二个浮动元素下移，可以通过给第二个浮动元素设置`margin-left: 负值；`来让第二个元素上移，其中这个负值>=元素上移后和第一个元素重合的临界值。


![三个浮动元素](http://upload-images.jianshu.io/upload_images/7113407-53fe1a8053813542.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![给最后一个元素设置了负margin之后](http://upload-images.jianshu.io/upload_images/7113407-eb59827771fc732d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


实例：水平等距排列

```
<style>
    *{
        margin: 0;
        padding: 0;
        list-style: none;
    }
    .clearfix:after{
        content: '';
        display: block;
        clear: both;
    }
    .content{
        width: 640px;
        margin: 0 auto;
        border: 1px solid black;
    }
    .item{
        width: 200px;
        height: 200px;
        background: #ff8fce;
        float: left;
        margin-left: 20px;
        /*左右之间间隔20px,然后此时第三个元素会被挤到下一行*/
        margin-top: 20px;
        /*让上下li之间也有边距*/
    }
    ul{
        margin-left: -20px;
        /*第三个元素被挤到下一行，缺少20px的空间，给父元素ul设置margin-left：-20px，让其向左移动20px，第三个元素就能放下了*/
    }
</style>
<div class="content clearfix">
    <ul>
        <li class="item">1</li>
        <li class="item">2</li>
        <li class="item">3</li>
        <li class="item">4</li>
        <li class="item">5</li>
        <li class="item">6</li>
        <li class="item">7</li>
        <li class="item">8</li>
    </ul>
</div>

```

### 圣杯布局

即三列布局，两边固定宽度，中间宽度自适应，并且中间内容的元素在DOM元素次序中是优先位置

```
<style>
    *{
        margin: 0;
        padding: 0;
    }
    .clearfix:after{
        content: '';
        display: block;
        clear: both;
    }
    .content{
        padding-left: 200px;
        padding-right: 210px;
    }
    .left{
        width: 200px;
        height: 500px;
        background: #93ffdb;
        float: left;
        margin-left: -100%;
        /*使用负margin让lef和right上去*/
        /*以为main的宽度为100%，所以重合的最低值是left的宽度，所以-200px就上去了，但是还要继续增加，一直要到最左边，即一个main的宽度，直接设为-100%即可*/

        position: relative;
        /*这时候content设置了padding给left和right留出了空间，但是left和right不会左右移动到相应的位置，需要用relative进行移动*/
        left: -210px;
        /*content设置了200px的空间，可以移动210px，留出点空间*/
    }
    .right{
        width: 200px;
        height: 500px;
        background: #c680ff;
        float: left;
        margin-left: -200px;
        /*right只需要上去就可以，不需要移动到左边，所以只需要加自身宽度的负margin*/

        position: relative;
        left: 210px;
    }
    .main{
        background: #b7ff61;
        float: left;
        width: 100%;
    }
    /*圣杯布局的缺点是main的宽度不能小于left的宽度，因为main的宽度就是content的宽度，即父元素的宽度*/
</style>

<div class="content clearfix">
    <div class="main">main</div>
    <div class="left">left</div>
    <div class="right">right</div>
</div>
```
