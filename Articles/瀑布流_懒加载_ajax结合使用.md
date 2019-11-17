---
title: 瀑布流_懒加载_ajax结合使用
date: 2017/08/29 00:00:01
cover: http://raphamorim.io/waterfall.js/images/waterfall.png
tags: 
- 前端
- JS
- 瀑布流
- 懒加载
- Ajax
categories: 
- 前端
---
瀑布流_懒加载_ajax结合使用
<!--more-->

整体的布局是瀑布流效果，然后滚动到底部进行懒加载。
大体的实现思路：
- 获取数据
- 将数据变为DOM，然后通过瀑布流(绝对定位)的形式将其放到页面上
- 当页面滚动（滚动到底）时，继续执行获取数据的操作。

需要注意的地方：
- 对于判断是否滚动到底，可以**预先设置一个锚点元素。当其出现在视野中时**，触发获取数据的请求。但是由于我们的**容器里的元素是绝对定位，高度是撑不开的，导致整个锚点元素一直在顶部**，这时候我们就要在进行瀑布流布局的时候，每排放一个元素，就要**手动将容器的高度撑开，让其等于瀑布流布局的数组的最大值**，即最大高度。
- 关于瀑布流布局的问题：我们的数据是ajax请求来的，所以在加载完成之前得不到整个元素的高度，不能进行判断。所以要**设置等加载完成之后在执行瀑布流布局**。


进行布局的时候，先将表达思路的伪代码写好，再去一个个写好这些函数：
```
    getData (newsList) {//从后端得到数据数组
        getNode(newsList); //将得到的数组进行渲染，拼接成html字符串
        //判断页面是否完全加载完，然后进行瀑布流布局
        if (isLoaded) {
            waterFall(node); //waterFall()每布局一个元素，就要手动将容易高度撑开，便于进行懒加载
        }
    };
    //然后监听滚动事件，滚动到底进行懒加载
    $(window).on('scroll',function () {
        //里面的代码也是一样的，只是加一个判断是否出现的函数
        if(checkshow($('#load'))) {
            getData (newsList) {//从后端得到数据数组
                getNode(newsList); //将得到的数组进行渲染，拼接成html字符串
                //判断页面是否完全加载完，然后进行瀑布流布局
                if (isLoaded) {
                    waterFall(node); //waterFall()每布局一个元素，就要手动将容易高度撑开，便于进行懒加载
                }
            };
        }
    })
```
写好思路伪代码之后，进行其中各种函数的书写。将重复的代码进行封装，将scroll事件处理进行优化，最终结果为：
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>瀑布流_懒加载_ajax结合使用</title>
</head>
<style>
    * {
        margin: 0;;
        padding: 0;
        box-sizing: border-box;
        list-style-type: none;
    }
    .wrap {
        width: 900px;
        margin: 0 auto;
    }
    .clearfix:after {
        content: '';
        display: block;
        clear: both;
    }
    #pic-ct {
        position: relative;
    }
    #pic-ct .item {
        position: absolute;
        padding: 0 0 10px 0;
        width: 280px;
        margin: 10px;
        border: 1px solid #dfdfdf;
        background: #fff;
        /*opacity: 0;*/
        transition: all 1s;
    }
    #pic-ct .item img {
        margin: 10px;
        width: 260px;
    }
    #pic-ct .item .header {
        height: 25px;
        margin: 0 12px;
        border-bottom: 1px solid #dbdbdb;
    }
    #pic-ct .desp {
        font-size: 12px;
        line-height: 1.8;
        margin: 10px 15px 0;
        color: #777371;
    }
    #load {
        visibility: hidden;
        height: 20px;
    }
    .hide {
        display: none;
    }
</style>
<body>
<div class="wrap">
    <div class="ct-watefall">
        <ul id="pic-ct" class="clearfix">

            <!--<li class="item">-->
                <!--<a href="#" class="link">![](http://upload-images.jianshu.io/upload_images/7113407-b30662b4652b17ae.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)</a>-->
                <!--<h4 class="header">标题</h4>-->
                <!--<p class="desp">asdasdasdasdas</p>-->
            <!--</li>-->


            <!--用于计算item宽度和列数进行瀑布流布局，但是其本身不展示出来-->
            <li class="item hide"></li>
        </ul>
        <div id="load">本身看不见</div>
    </div>
</div>

<script src="../jquery-3.2.1.min.js"></script>
<!--<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>-->
<script>

    var currentPage = 1;
    var waterFallArr = [];
    var waterFallCol = parseInt($('#pic-ct').width() / $('.item').width());
    for (var i = 0; i < waterFallCol; i ++ ) {
       waterFallArr[i] = 0;
    }
    var clock;

    run();
    $(window).on('scroll',function () {
        if (clock) {
            clearTimeout(clock);
        }
        clock = setTimeout(function () {
            if (checkShow($('#load'))) {  //当这个看不见的锚点出现时，进行懒加载
                run();
            }
        },300)
    })
    function run() {
        getData(function (newsList) {
            $(newsList).each(function (i,e) {
                var $node = getNode(e);
                $node.find('img').on('load',function () { //当当前元素的图片加载完成之后，才能进行瀑布流布局，否则没高度不能进行瀑布流布局
                    $('#pic-ct').append($node); //不能在getNode()中就将其添加到页面中，得等到加载完成后再添加到页面中。否则没有width和height，不能进行瀑布流布局
                    waterFall($node);
                })
            })
        });
    }

    function getData (callback) {
        $.ajax({
            url: 'http://platform.sina.com.cn/slide/album_tech',
            method: 'get',
            dataType: 'jsonp',
            jsonp: 'jsoncallback',
            data: {
                app_key: 1271687855,
                num: 10,
                page: currentPage,
            }
        }).done(function (ret) {
            if (ret.status.code == 0) {
                callback(ret.data);
                currentPage++;
            }
        })
    }
    function getNode (e) {
            var html = '';
            html += '<li class="item">';
            html += '<a href=" ' +e.url + ' " class="link">![]( '+e.img_url +' )</a>'
            html += '<h4 class="header">' +e.short_name+ '</h4>';
            html += '<p class="desp">'+e.short_intro+'</p></li>';
            return $(html);
    }
    function waterFall ($node) {
        var minHeight = Math.min.apply(null,waterFallArr);
        var minHeightIndex = waterFallArr.indexOf(minHeight);
        $node.css({
            left: minHeightIndex * $node.outerWidth(true),
            top: minHeight,
            opacity: 1,
        })
        waterFallArr[minHeightIndex] = $node.outerHeight(true) + waterFallArr[minHeightIndex];
        $('#pic-ct').height(Math.max.apply(null,waterFallArr));//因为子元素都是绝对定位，高度撑不开的话顶部的锚点就一直在页面顶部。所以要手动撑开容易高度
    }
    function checkShow($node) {
        var windowHeight = $(window).height();
        var scrollTop = $(window).scrollTop();
        var nodeOffsetTop = $node.offset().top;
        if (nodeOffsetTop < (windowHeight + scrollTop + 500)) {
            return true;
        }
        return false;
    }
</script>
</body>
</html>
```
其实还可以监听窗口的变化，当窗口变化时，瀑布流布局也可以相应的改变
