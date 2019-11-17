---
title: CSS常见样式2
date: 2017/08/01 01:22:24
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS常见样式
<!--more-->

###一，text-align: center的作用是什么，作用在什么元素上？能让什么元素水平居中

作用在块级元素上面，可以使得块级元素的子元素**水平居中显示**。



###二，IE 盒模型和W3C盒模型有什么区别?

盒模型从外到内分别是：margin border padding content(width和height)

- w3c盒模型:设置宽高的时候，是给content设置的width和height，不考虑元素的margin,padding,border，所以实际的元素要比width和height规定的大
- IE盒模型：设置宽高的时候，是给content,padding以及border设置的width和height,这时的content的宽高要小与给定的width和height。

- 总结：w3c中盒模型padding,border不占width,height。IE盒模型中，width和height包括content,padding和border。


###三，*{ box-sizing: border-box;}的作用是什么？

```
box-sizing: content-box; /*w3c标准盒模型*/ 
box-sizing: border-box;  /*IE盒模型*/
```
给整个页面应用IE盒模型
###四，line-height: 2和line-height: 200%有什么区别?

- line-height: 2 是相对于**文字本身**的2倍。
- line-height: 200%是父元素文字高度的2倍。
- 给父元素增加line-height: 2和line-height: 200%两个属性时，line-height: 2下的子元素的文字不管自身的文字font-size多大，继承父元素的line-height: 2之后，行高都是自身文字大小的2倍，而在line-height: 200%下的子元素的font-size的大小不会改变行高的大小，因为line-height: 200%在计算的时候只计算父元素当时的font-size，然后应用为一个固定的值，不会因为子元素的font-size的值的大小而改变。所以，如果想要父元素下的子元素的行高无论自身font-size为多少，都有行高为自身的2倍的时候，**应该应用line-height:2**。

- 另外可以通过**height=line-height**来垂直居中**单行文本**。

###五，inline-block有什么特性？如何去除缝隙？高度不一样的inline-block元素如何顶端对齐?

- inline-block 既呈现出Inline的特性（不占据一整行，宽度由内容宽度决定），又呈现出block特性（可以设置宽高，内外边距）

- 缝隙问题：元素之间有空白字符（回车换行等）会导致两个inline-block元素产生缝隙
- 消除缝隙的办法：
1. 可以通过修改html文件的方式来消除缝隙。
2. 
```
<style type="text/css">
    .wrap{
        font-size: 0;
    }
    span{
        display: inline-block;
        border: 1px solid;
        width: 100px;
        height: 50px;
        font-size: 14px;
    }
</style>

<div class="wrap">
    <span>deejay</span>
    <span>deejay</span>
</div>
``` 
通过使用父元素包裹，**设置font-size为0之后，再在inline-block元素中将font-size设回来**，但是这种方式具有一定的副作用。
3. 多个元素时不设置inline-block，而是增加一个div包裹这些元素，让里面的元素浮动，然后给包裹的这个div清除浮动，再给这个div设置为inline-block，这样可以清除缝隙，通过外部设置text-align等属性还能设置居中等。
```
<style>
    body{
        margin: 0;
    }
    .clearfix::after{
        content: '';
        display: block;
        clear: both;
    }
    .p{
        border: 1px solid red;
        text-align: center;
        /*4.设置textalign使其居中*/
    }
    .c{
        display: inline-block;
        /*3.设置c为inline-block*/
    }
    span{
        border: 1px solid;
        padding: 8px 16px;
        float: left;
        /*1.让两个span浮动*/
    }
</style>

<div class="p">
    <div class="c clearfix">
        <!--2.给.c清除浮动使其回归文档流-->
        <span>YES</span>
        <span>NO</span>
    </div>
</div>
```
4. 使用css3 flex布局可以消除缝隙,display:flex;justify-content:center居中。
- 高度不一样的inline-block元素如何顶端对齐?

通过增加`vertical-align: top;`来进行顶部对齐。

###六，CSS sprite 是什么?

CSS雪碧图（精灵图）：指将不同的小图片/图标合并在一张图上面，使用CSS sprite可以减少网络请求，提高网页加载性能

###七，让一个元素"看不见"有几种方式？有什么区别?

- opacity: 0; 透明度为0，整体
- visibility: hidden; 和 opacity: 0类似。
- display: none; 消失，**不占用位置**。
background-color: rgba(0,0,0,0) 只是背景色透明
