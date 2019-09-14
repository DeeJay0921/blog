---
title: jQuery-ajax常见问题
date: 2017/08/26 00:00:01
tags: 
- 前端
- JS
- jQuery
- Ajax
categories: 
- 前端
---
jQuery-ajax常见问题
<!--more-->

###1, jQuery 中， $(document).ready()是什么意思？

####.ready(handler)

当DOM准备就绪的时候，指定一个函数执行
等价于下面这两种写法：
```
$.(document).ready(handler)
$(handler)
```
实例：
```
$(function () {
    console.log('ready');
})
```
###2,$node.html()和$node.text()的区别?
$node.html()在没有参数的时候，获取集合中的第一个匹配元素的HTML内容，当有参数的时候表示，设置每个匹配元素的html内容；
$node.text()在没有参数的时候，获取集合中每个元素的文本内容，包括后代（内容从父元素往子元素排列），有参数的时候，设置匹配元素集合中的每个元素的文本内容为指定文本内容。

###3,$.extend 的作用和用法? 
####jQuery.extend([deep,]target[,object1][,objectN])
- 当我们提供两个或多个对象给$.extend(),对象的所有属性都添加到目标对象(target参数)。
- 如果只有一个参数提供给$.extend()。这意味着目标参数被省略；在这种情况下，jquery对象本身被默认为目标对象。这样，我们可以在jQuery的命名空间下添加新的功能。这对于插件开发者希望向jquery中添加新函数时是很有用的

目标对象(第一个参数)将被修改，并且将通过$.extend()返回。然而，如果我们想保留原对象，我们可以通过传递一个空对象作为目标对象：
`var object = $.extend({},object1,oject2);`

在默认情况下，通过$.extend()合并操作不是递归的。

如果第一个对象的属性本身是一个对象或者数组，那么它将完全用第二个对象相同的key重写一个属性。这些值不会被合并。如果将true作为该函数的第一个参数，那么会在对象上进行递归的合并。

**使用范例：**
```
    var obj1 = {a:1};
    var obj2 = {b:2,c:3};
    var obj3 = {b:3,d:5};
    $.extend(obj1,obj2); //把obj2扩展到obj1上去，作一个遍历，如果obj2里面有的属性obj1里面也有，会进行覆盖，如果obj1里面没有，会做一次新增
    // obj1 == {a:1,b:2,c:3}
    $.extend(obj1,obj2,obj3);//修改的还是obj1，此时obj1 == {a:1,b:3,c:3,d:5} 已经存在的属性进行覆盖，没有的进行新增

    
//    如果想得到三个属性扩展的值，但是又不想修改obj1的话
    var obj4 = {};
    $.extend(obj4,obj1,obj2,obj3);
    //也可以向下面这么写，直接写个空对象，将扩展完成的对象赋给新定义的对象就好
    var obj5 = $.extend({},obj1,obj2,obj3);
```
###4, jQuery 的链式调用是什么？
链式调用：使用jQuery方法时，对象方法返回的是对象本身，可以调用对此对象的其他jQuery方法，实现连续调用多个方法
例：$(this).addClass('active').siblings().removeClass('active')


###5, jQuery 中 data 函数的作用
在匹配元素上存储任意相关的数据　或　返回匹配的元素集合中的第一个元素的给定名称的数据存储的值。
- data(key,value
描述：在匹配元素上存储任意相关数据
```
$("body").data("foo" , 18);
$("body").data("abc", { name: "text", sex: 20 });
$("body").data({cba:[a,b,c]});
$("body").data("foo"); // 18
$("body").data() // {foo: 18, abc: {name: "text", sex: 20}, cba:[a,b,c]}
```

###6,写出以下功能对应的 jQuery 方法：
- 给元素 $node 添加 class active，给元素 $noed 删除 class active
```
    $node.addClass('active');
    $node.removeClass('active');
```
- 展示元素$node, 隐藏元素$node
```
$node.hide();
 $node.show()
```
- 获取元素$node 的 属性: id、src、title， 修改以上属性
```
    $node.attr('id','newID');
    $node.attr('src','newsrc');
    $node.attr('tile','newtitle');
```
- 给$node 添加自定义属性data-src
`$node.data('data-src','src');`
- 在$ct 内部最开头添加元素$node
`$ct.prepend($node);`
- 在$ct 内部最末尾添加元素$node
`$ct.append($node);`
- 删除$node
`$node.remove();`
- 把$ct里内容清空
` $ct.empty()`
- 在$ct 里设置 html <div class="btn"></div>
`$ct.html('<div class="btn"></div>')`
- 获取、设置$node 的宽度、高度(分别不包括内边距、包括内边距、包括边框、包括外边距)
```
不包括内边距
$node.height();
$node.width();
包括内边距
$node.innerHeight();
$node.innerWidth();
包括边框
$node.outerHeight();
$node.outerWidth();
包括边框
$node.outerHeight(true);
$node.outerWidth(true);

设置
不包括内边距
$node.height( '100px' );
$node.width( '100px' );
包括内边距
$node.innerHeight( '100px' );
$node.innerWidth( '100px' );
包括边框
$node.outerHeight( '100px' );
$node.outerWidth( '100px' );
包括边框
$node.outerHeight( '100px', true );
$node.outerWidth( '100px', true );

```
- 获取窗口滚动条垂直滚动距离
`$(window).scrollTop()`
- 获取$node 到根节点水平、垂直偏移距离
`$node.offset().left`
`$node.offset().top`
- 修改$node 的样式，字体颜色设置红色，字体大小设置14px
`$node.css({color:'red,fontSize:'14px'})`
- 遍历节点，把每个节点里面的文本内容重复一遍
```
$node.each(function(){
    $(this).text().+$(this).text();
});
```
- 从$ct 里查找 class 为 .item的子元素
`$ct.find('.item')`
- 获取$ct 里面的所有孩子
`$ct.children()`
- 对于$node，向上找到 class 为'.ct'的父亲，在从该父亲找到'.panel'的孩子
`$node.parents('.ct').find('.panel')`
- 获取选择元素的数量
`$('#id').length`
- 获取当前元素在兄弟中的排行
`$('ul').index();`


###7,用jQuery实现以下操作
- 当点击$btn 时，让 $btn 的背景色变为红色再变为蓝色
- 当窗口滚动时，获取垂直滚动距离
- 当鼠标放置到$div 上，把$div 背景色改为红色，移出鼠标背景色变为白色
- 当鼠标激活 input 输入框时让输入框边框变为蓝色，当输入框内容改变时把输入框里的文字小写变为大写，当输入框失去焦点时-
- 去掉边框蓝色，控制台展示输入框里的文字
- 当选择 select 后，获取用户选择的内容
[demo](http://js.jirengu.com/sihuqasimu/1/edit?output)

###8,用 jQuery ajax 实现加载更多
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <!--<script src="../jquery-3.2.1.min.js"></script>-->
    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
</head>
<style>
    * {
        margin: 0;
        padding: 0;
        list-style: none;
    }
    #ct li {
        border: 1px solid #ccc;
        padding: 10px;
        margin-top: 10px;
        margin-left: 8px;
        margin-right: 8px;
        cursor: pointer;
    }
    #load-more {
        display: block;
        margin: 10px auto;
        text-align: center;
        cursor: pointer;
    }
    #load-more img {
        width: 40px;
        height: 40px;
    }
    .btn {
        display: inline-block;
        height: 40px;
        line-height: 40px;
        width: 80px;
        border: 1px solid #e27272;
        border-radius: 3px;
        text-align: center;
        text-decoration: none;
        color: #e27272;
    }
    .btn:hover,li:hover {
        background: green;
        color: #fff;
    }
</style>
<body>
<ul id="ct">
    <li>内容1</li>
    <li>内容2</li>
    <!--加载更多，从后端得到更多的li放到ul中就可以了-->
</ul>
<a href="javascript:void(0)" id="load-more" class="btn">加载更多</a>

<script>
    var currentIndex = 2;
    var currentLength = 5;

    $('#load-more').on('click',function () {
        $.ajax({
            url: '/loadMore',
            method: 'GET',
            data: {
                index: currentIndex,
                length: currentLength,
            }
        }).done(function (ret) {
            render(ret);
            currentIndex += currentLength;
        })
    })
    function render (data) {
        var html = '';
        var $data = $(data);
        $data.each(function (i,e) {
            html += '<li>' + e + '</li>';
        })
        $('#ct').append(html);
    }
</script>
</body>
</html>
```

router.js
```
app.get('/loadMore',function (req,res) {
    var index = req.query.index;
    var length = req.query.length;
    var data = [];
    for (var i = 0; i < length; i ++ ) {
        index++;
        data[i] = '内容' + parseInt(index);
    }
    res.send(data);
})
```
