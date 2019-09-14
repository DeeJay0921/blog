---
title: 经典的jQuery实现图片懒加载的一种方法
date: 2017/08/26 00:00:01
tags: 
- 前端
- JS
- jQuery
- 懒加载
categories: 
- 前端
---
经典的jQuery实现图片懒加载的一种方法
<!--more-->


对于一个有多个图片的网站来说，访问的时候不应该直接加载所有图片，而是应该只讲浏览器窗口内的图片进行加载。当滚动的时候，在加载更多的图片。叫做图片的懒加载。

我们可以通过给img自定义一个新属性，来存储图片真实的src地址，当需要加载的时候，再将这个真实的地址赋给src，进行图片加载。

###整体思路：
所以大体的思路为：
- 设置个data-src(自定义一个属性)来存放真实地址
- 当滚动页面时，**检查所有的img标签，看是否出现在视野中**，如果已经出现在了视野中，那继续再进行判断，看其是否已经被加载过了，如果还没有被加载过，那就进行加载。

想到这一步，整体的大概思路可以写为:
```
    $(window).on('scroll',function () {//当页面滚动的时候绑定事件
        $('.container img').each(function () {//遍历所有的img标签
            if (checkShow($(this)) && !isLoaded($(this)) ){
                // 需要写一个checkShow函数来判断当前img是否已经出现在了视野中
                //还需要写一个isLoaded函数判断当前img是否已经被加载过了
                loadImg($(this));//符合上述条件之后，再写一个加载函数加载当前img
            }
        })
    })
    function checkShow($img) { // 传入一个img的jq对象

    }
    function isLoaded ($img) {

    }
    function loadImg ($img) {
        
    }
```
####判断目标标签是否出现在视野中：
此时，判断目标标签是否已经出现在视野中的思路为：
分析标签出现在页面中的两个临界状态：
- 当页面向上滚动，一直滚到首先出现在页面的**下边缘**的时候，此时有**页面向上滚动的距离，加上浏览器自身的高度**，**就等于目标标签本身在页面中距离页面顶部的距离**。
- 页面接着向上滚，当页面出现在了浏览器的**上边沿**的时候，此时**页面向上滚的高度**，**就等于目标标签本身在页面中距离页面顶部的距离**
那么可以checkShow函数可以写为：
.offset()方法允许我们检索一个元素 (包含其 border 边框，不包括 margin) 相对于文档（document）的当前位置。.offset()返回一个包含top 和 left属性的对象 。表示相对于顶部或者左边的坐标。
```
    function checkShow($img) { // 传入一个img的jq对象
        var scrollTop = $(window).scrollTop();  //即页面向上滚动的距离
        var windowHeight = $(window).height(); // 浏览器自身的高度
        var offsetTop = $img.offset().top;  //目标标签img相对于document顶部的位置

        if (offsetTop < (scrollTop + windowHeight) && offsetTop > scrollTop) { //在2个临界状态之间的就为出现在视野中的
            return true;
        }
        return false;
    }
```
####判断目标标签是否已经被加载过：
```
    function isLoaded ($img) {
        return $img.attr('data-src') === $img.attr('src'); //如果data-src和src相等那么就是已经加载过了
    }
```

####加载目标标签
```
    function loadImg ($img) {
        $img.attr('src',$img.attr('data-src')); // 加载就是把自定义属性中存放的真实的src地址赋给src属性
    }
```
###优化代码
那么此时的代码就能实现大体功能了，此时完整代码为：
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>图片懒加载-简单</title>
    <script src="../jquery-3.2.1.min.js"></script>
    <style>
        ul,li{
            list-style:none;
        }
        .container{
            width: 600px;
            margin: 0 auto;
        }
        .container li{
            float: left;
            margin: 10px 10px;
        }
        .container li img{
            width: 240px;
            height: 180px;
        }
        p{
            float: left;
        }
    </style>
</head>
<body>
<ul class="container">
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/1.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/2.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/3.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/4.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/5.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/6.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/7.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/8.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/9.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/10.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/11.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/12.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/13.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/14.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/15.jpg" /></a></li>
    <li><a href="#"><img src="" data-src="http://cdn.jirengu.com/book.jirengu.com/img/16.jpg" /></a></li>
</ul>

<script>
    $(window).on('scroll',function () {//当页面滚动的时候绑定事件
        $('.container img').each(function () {//遍历所有的img标签
            if (checkShow($(this)) && !isLoaded($(this)) ){
                // 需要写一个checkShow函数来判断当前img是否已经出现在了视野中
                //还需要写一个isLoaded函数判断当前img是否已经被加载过了
                loadImg($(this));//符合上述条件之后，再写一个加载函数加载当前img
            }
        })
    })
    function checkShow($img) { // 传入一个img的jq对象
        var scrollTop = $(window).scrollTop();  //即页面向上滚动的距离
        var windowHeight = $(window).height(); // 浏览器自身的高度
        var offsetTop = $img.offset().top;  //目标标签img相对于document顶部的位置

        if (offsetTop < (scrollTop + windowHeight) && offsetTop > scrollTop) { //在2个临界状态之间的就为出现在视野中的
            return true;
        }
        return false;
    }
    function isLoaded ($img) {
        return $img.attr('data-src') === $img.attr('src'); //如果data-src和src相等那么就是已经加载过了
    }
    function loadImg ($img) {
        $img.attr('src',$img.attr('data-src')); // 加载就是把自定义属性中存放的真实的src地址赋给src属性
    }
</script>
</body>
</html>
```
首先有个明显的问题，目前页面上，第一次进来的时候如果不滚动滚轮的话什么都看不到，想修复这个问题，可以先进行一次页面检查，调用checkShow()，然后页面上在视野中的标签就会被加载，然后再进行监听window的scroll事件。
```
    // 先进行一次检查
    $('.container img').each(function () {
        if (checkShow($(this)) && !isLoaded($(this)) ){
            loadImg($(this));
        }
    })
    $(window).on('scroll',function () {
        $('.container img').each(function () {
            if (checkShow($(this)) && !isLoaded($(this)) ){
                loadImg($(this));
            }
        })
    })
```
但是此时代码有重复的，进行优化。将重复的代码块写成一个新的函数。进行替换。
```
    // 先进行一次检查
    lazyRender();
    $(window).on('scroll',function () {
        lazyRender();
    })
    function lazyRender () {
        $('.container img').each(function () {
            if (checkShow($(this)) && !isLoaded($(this)) ){
                loadImg($(this));
            }
        })
    }
```
代码优化已经很好了，对于性能的优化：
$(window).on('scroll',function () {}这个事件鼠标滚动的时候触发次数非常多。对性能影响大，优化思路是当鼠标滚轮停止滚动的时候，再去判定是否存在在视野中，而不是滚动过程中一直触发。
```

    // 先进行一次检查
    lazyRender();
    //为了不在滚轮滚动过程中就一直判定，设置个setTimeout,等停止滚动后再去判定是否出现在视野中。
    var clock; //这里的clock为timeID，
    $(window).on('scroll',function () {
//        lazyRender();
        if (clock) { // 如果在300毫秒内进行scroll的话，都会被clearTimeout掉，setTimeout不会执行。
                    //如果有300毫秒外的操作，会得到一个新的timeID即clock，会执行一次setTimeout,然后保存这次setTimeout的ID，
                      //对于300毫秒内的scroll事件，不会生成新的timeID值，所以会一直被clearTimeout掉，不会执行setTimeout.
            clearTimeout(clock);
        }
        clock = setTimeout(function () {
            console.log('运行了一次');
            lazyRender();
        },300)
    })

```
