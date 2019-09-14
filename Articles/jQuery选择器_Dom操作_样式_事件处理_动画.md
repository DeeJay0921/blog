---
title: jQuery选择器_Dom操作_样式_事件处理_动画
date: 2017/08/24 00:00:01
tags: 
- 前端
- JS
- jQuery
categories: 
- 前端
---
jQuery选择器_Dom操作_样式_事件处理_动画
<!--more-->

[jQuery 中文文档](http://www.css88.com/jqapi-1.9/)

######jQuey可以做什么

- 选择网页元素
- 改变结果集
- 元素的操作：取值和赋值
- 元素的操作：移动
- 元素的操作： 复制，删除和创建
- 工具方法
- 特殊效果
- ajax
- http://devdocs.io/jquery/

jquery 2.x版本之后不兼容IE 6 7 8

###jQuery选择器
选择器

基本选择器 | 作用
-- | -- 
$('\*') | 匹配页面所有元素
$('#id') | id选择器
$('.class') | 类选择器
$('element') | 标签选择器

组合/层次选择器 | 作用
-- | --
$('E,F') | 多元素选择器，用`,`分隔，**同时匹配元素E或元素F**
$('E F') | 后代选择器，用空格分隔，匹配E元素**所有的后代（不只是子元素，子元素向下递归**）元素F
$('E>F') | 子元素选择器，用`,`分隔，匹配E元素的**所有直接子元素**
$('E+F') | 直接相邻选择器，匹配E元素**之后**的**相邻**的**同级**元素F
$('E~F') | 普通相邻选择器（弟弟选择器），匹配E元素**之后**的**同级**元素F（无论直接相邻与否）
$('.class1 .class2') | 匹配类名中既包含class1又包含class2的元素

其他具体详细选择器参考中文文档

#####jQuery对象和DOM原生对象的区别
对于jquery对象，**只能使用jquery提供的API**
原生对象需要去使用原生对象的属性和方法

- jQuery对象和DOM原生对象的转换
jquery对象可以通过加下标的方法来变成DOM原生对象，从而使用原生对象的方法。而一个原生对象，也可以通过用$包裹一下原生的对象，就能变为jquery对象。
```
<div id="container">
    <ul>
        <li>001</li>
        <li class="active">002</li>
        <li>003</li>
    </ul>
</div>
<script>
    $('#container li');//[li,li.active,li]
    $('#container li')[0]; //变为原生DOM对象 <li>001</li>
    document.querySelector('.active'); //原生DOM对象 <li class="active">002</li>
    $(document.querySelector('.active'));// 变为jquery对象  [li.active, context: li.active]
</script>
```
对于上述例子，如果想得到第二项的元素，又不想将其变为DOM元素对象的话，可以使用.eq()方法。
`$('#container li').eq(1); //[li.active, prevObject: n.fn.init(3), context: document]`
依然是jquery对象

- 常见选择器的使用举例：
结构如下：
```
<div id="container">
    <ul class="wrap">
        <li>001</li>
        <li class="active">002</li>
        <li>003</li>
    </ul>
</div>
```
各种选择器选择到的jquery对象
```
<script>
    $('.wrap').parent(); // [div#container]
    $('.wrap').parents(); // [div#containe, body, htmlr]
    $('.wrap').parents('body'); //[body]
    $('.wrap').children();// [li, li.active, li]
    $('.wrap').children('.active'); // [li.active]
    $('.wrap').children().first();//[li]
    $('.wrap').children().last();//[li]
    $('.wrap').find('.active'); //[li.active]
    $('.wrap').find('.active').next();//[li]
    $('.wrap').find('.active').prev();//[li]
    $('.wrap').find('.active').siblings(); //[li, li]

</script>
```

###jquery的DOM操作

######创建对象 
- 选择一个DOM对象，然后用$()包裹
- 或者可以直接把html字符串用$()包裹，就能创建一个jquery对象
######添加元素
- append()  appendTo()
将一个元素放入另一个元素的尾部
```
<script>
    var $li = $('<li>004</li>'); //var的变量前面加$没有任何实际意义，仅仅表面是个jquery对象，方便理解
    $('#container .wrap').append($li);
    $li.appendTo($('#container .wrap'));//和上面的效果一致，可以换种写法而已
</script>
```
- prepend() prependTo()
同理 prepend为插入到另一个元素最前面
```
    var $li2 = $('<li>000</li>');
    $('#container .wrap').prepend($li2);
    $li2.prependTo($('#container .wrap'));
```
- before    针对并列同级的元素
现在把$li放到$li2前面
`$li2.before($li);`
也可以新建一个
`$li2.before('<li>005</li>');`
- after 和before相似 插入到并列同级元素之后

######删除元素
- remove()
举例：删除第三个li
`    $('.wrap li').eq(2).remove(); //选中所有的li，然后使用eq方法选择要删除的jquery对象，使用remove()
`
- empty()
清空被选择元素内部的**所有子元素，自身还在**
`    $('.wrap').empty(); // ul自身还在，所有的li被删除
`
- detach()
detach()和remove()一样，除了detach()保存所有jquery数据和被移走的元素相关联。当需要移走一个元素，不久又将该元素插入DOM时，这种方法很有用。

######html()
html()方法读写两用，可以修改和获取元素的innerHTML

- 没有传递参数的时候，返回元素的innerHTML
- 传递了参数时，修改元素的innerHTML为传递的参数

读写两用的方法基本都类似。
- 如果结果是多个赋值操作，会给每个结果都赋值
- 如果结果多个，获取值的时候，返回结果集中的第一个对象的相应值

######text()
和html()类似，对应DOM的innerText

## jquery的事件

jquery可以很好的处理浏览器兼容，1.7版本之后，绑定和解除事件用on/off方法即可

 #####on(event[,selector][,data],handler(eventObject))
on()方法的各个参数的意思
1.event 
一个或多个空格分隔的事件类型和可选的命名空间，或仅仅是命名空间，例如： "click" , "keydown.myPlugin"  或者".myPlugin"
2.selector
一个选择器字符串，用于过滤出被选中元素中能触发事件的后代元素。如果选择器是null或者忽略了该选择器，那么被选中的元素总是能触发事件
3.data 
当一个事件被触发时，要传递给事件处理函数的**event.data**
4.handler(eventObject)
当事件被触发时，执行的函数。若该函数只是要执行return false的话，那么参数位置可以简写成false

使用举例：
- 普通事件绑定，最简单的用法
```
    $('div').on('click',function (e) {//可以简写为$('div').click(...)
        console.log(this);
        console.log(e);
    })
```
- **事件委托或者事件代理，想让div下面所有的span绑定事件，可以把事件绑定到div上**
```
    $('div').on('click','span',function (e) {
        console.log(this);
        console.log(e);
    })
```
- 可以在绑定的时候给事件处理程序传递一些参数
```
    $('div').on('click',{name: 'byron',age: 24},function (e){
        console.log(e.data);
        console.log(e.data.name);
        console.log(e.data.age);
    })
```
需要注意的是，**要用this或者e.target的话，因为这两个都是原生的DOM对象，所以要使用jquery的时候，要转换成jquery对象在使用**。例如`var $this = $(this);`

######trigger()
模拟事件触发

##jQuery属性操作

######.val([value])
读写双用，用于处理input的value。
```
<input type="text" value="hello">
<script>
    console.log($('input').val());
    $('input').val('world');
    console.log($('input').val());
</script>
```
#####.attr()
获取/修改元素特定属性的值
```
<input type="text" value="hello">
<script>
    $('input').attr('type'); //text 获取目标元素的type属性的值
    $('input').attr('type','checkbox'); //设置type属性值为checkbox
</script>
```
**移除属性用removeArrt()方法**

######css()
和arrt()很像，用来处理元素的css
```
<div class="box"></div>
<script>
    $('.box').css({
        width: '30px',
        height: '30px',
        border: '1px solid red',
    });
</script>
```
######addClass() / removeClass()
为元素添加class，不是覆盖原class，是追加
- **hasClass()**
判断是否存在某个类

对应原生的classList.add/remove/contains

- **toggleClass()**
切换类

##jQuery动画

###基础

####.hide([duration][,easing][,complete])
用于隐藏元素，没有参数的时候等同于直接设置display属性
`.css('display','none')`
1.duration    动画持续多久
2.easing   表示过渡使用哪种缓动函数，jq自身提供"linear"和"swing"
3.complete  在动画完成时执行的函数
####.show([duration][,easing][,complete])
显示元素，使用和hide类似
####.toggle([duration][,easing][,complete])
用来切换元素的隐藏，显示。类似于toggleClass,使用方法和hide show一样

###渐变
####.fadeIn([duration][,easing][,complete])
通过淡入的方式显示匹配元素
####.fadeOut([duration][,easing][,complete])
淡出的方式隐藏匹配元素
####.fadeToggle([duration][,easing][,complete])

###滑动
####.slideDown([duration][,easing][,complete])
用滑动动画显示一个元素，方法将会给匹配元素的高度的动画，这会导致页面的下面部分滑下去，弥补了显示的方式
####.slideUp([duration][,easing][,complete])

###自定义动画
####.animate(properties [duration][,easing][,complete])
properties是一个css属性和值的对象，动画将根据这组对象移动。

当进行多个连续动画的书写时，不需要一层层的嵌套代码在回调函数内部，jq支持直接按照动画顺序进行书写。也可以将多个动画的properties写到一个数组内，遍历数组调用animate方法传入这个数组。

jquery的**链式调用**：一般来说调用jquery的每一个方法，这个方法最后都会return这个jquery对象本身，所以可以支持链式调用

- 停止动画 finish() 和 stop()
