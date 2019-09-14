---
title: jQuery常见问题
date: 2017/08/26 00:00:01
tags: 
- 前端
- JS
- jQuery
categories: 
- 前端
---
jQuery常见问题
<!--more-->

###1， jQuery 能做什么？



- 选择网页元素
- 改变结果集
- 元素的操作：取值和赋值
- 元素的操作：移动
- 元素的操作： 复制，删除和创建
- 工具方法
- 特殊效果
- ajax

###2， jQuery 对象和 DOM 原生对象有什么区别？如何转化？

jQuery对象和DOM原生对象的区别
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

###3，jQuery中如何绑定事件？bind、unbind、delegate、live、on、off都有什么作用？推荐使用哪种？使用on绑定事件使用事件代理的写法？
- 在jQuery中，绑定事件的方法包括：.bind()，.delegate()，.live()，.on()等。
- .bind()：绑定事件，这是一个相对而言比较高效、快捷，且兼容性较好的绑定方法；
.unbind()：解绑事件，与.bind()方法相对应；
.delegate()：绑定事件，它可以自行选择它要附加的DOM元素进行绑定；
.live()：绑定事件，这种方法的原理相当于直接使用根一级的document来进行事件代理，目前该方法已被废弃；
.on()：绑定事件，在jQuery1.8版本之后，我们基本统一使用.on()替代其它的方法来进行事件的绑定；
.off()：解绑事件，与.on()方法相对应。

- 在目前的jQuery版本中通常推荐使用on/off来对事件进行绑定和解绑。
- #####on(event[,selector][,data],handler(eventObject))
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

###4，jQuery 如何展示/隐藏元素？
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

###5，jQuery 动画如何使用？

####.animate(properties [duration][,easing][,complete])
- properties是一个css属性和值的对象，动画将根据这组对象移动。
- duration：动画过程持续的时长。默认值为"normal"，可以将其设置为"slow"或 "fast"，也可以直接设置数值来规定时长，单位为毫秒
- easing：过渡使用哪种缓动函数。jQuery自身提供"linear" 和 "swing"，其他效果可以使用jQuery Easing Plugin插件
- complete：在动画完成时执行的函数。

- 停止动画 finish() 和 stop()
当一个元素调用.stop()时，当前正在运行的动画将会立即停止。例如，一个元素正在被.slideUp()隐藏的过程中，调用.stop()，该元素依然会有一部分是处于显示状态的。由于元素上的动画尚未执行完成，所以动画完成时执行的回调函数是不会被调用的。
如果同一元素调用多个动画方法，尚未被执行的动画被放置在元素的效果队列中，这些动画不会开始，直到第一个完成。当调用.stop()的时候，队列中的下一个动画立即开始。如果clearQueue参数提供true值，那么在队列中的动画其余被删除并永远不会运行。
如果提供jumpToEnd参数，并且值为true时，当前动画将停止，但该元素上的 CSS 属性会被立刻修改成动画的目标值。用上面的.slideUp()为例子，该元素将立即隐藏。如果提供回调函数将立即被调用。

###6，如何设置和获取元素内部 HTML 内容？如何设置和获取元素内部文本？

######html()
html()方法读写两用，可以修改和获取元素的innerHTML

- 没有传递参数的时候，返回元素的innerHTML
- 传递了参数时，修改元素的innerHTML为传递的参数

读写两用的方法基本都类似。
- 如果结果是多个赋值操作，会给每个结果都赋值
- 如果结果多个，获取值的时候，返回结果集中的第一个对象的相应值

######text()
和html()类似，对应DOM的innerText
###7，如何设置和获取表单用户输入或者选择的内容？如何设置和获取元素属性？
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
