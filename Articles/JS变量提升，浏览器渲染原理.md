---
title: JS变量提升，浏览器渲染原理
date: 2017/08/10 23:00:01
cover: https://cn.bing.com/images/search?view=detailV2&ccid=6ryaBgW%2b&id=9296CE8243DE018F08451305079F1790FDFEB3BC&thid=OIP.6ryaBgW-1j262kiScIakAQHaEK&mediaurl=https%3a%2f%2fcdn-images-1.medium.com%2fmax%2f1200%2f1*0hfm3TfurQboq6KlJrG56g.jpeg&exph=450&expw=800&q=js+declare+variable&simid=608016365936313972&selectedIndex=39&ajaxhist=0
tags: 
- 前端
- JS
categories: 
- 前端
---
JS变量提升，浏览器渲染原理
<!--more-->

##标识符
 
定义变量，函数，属性的时候的标识符，是**区分大小写**的。
第一个字符必须是**字母**，**_**和**$**。
后面的还可以是数字。

##变量提升

js的引擎工作方式是，先解析代码，**获取所有的被声明的变量**，然后再一行一行地运行。所以，**所有变量的声明语句，都会被提升到代码的头部**，称为变量提升。
`var a =2;`这个赋值语句，进行解析时，
```
  var a;
  a =2;
```
，先解析出变量声明，a的初始值为undefined，然后才逐句执行程序。
举例：
```
    var a = 100;
    b = 10;
    console.log(a);
    console.log(c);
    
    var c = 20;
    var d = 30;
    
```

解析完成后的代码其实为：

```
        var a;
        var c;
        var d;
    //    变量提升


        a = 100;
        b = 10;
        console.log(a); // 100
        console.log(c); //undefined

        c = 20;
        d = 30;
```

## 浏览器渲染机制



1. CSS和JS在网页中的放置顺序是怎样的？
**css样式一般要使用link标签放到顶部**。body的前面，即head标签内。
对于js，一般要将**js放在底部**，因为脚本会阻塞后面内容的呈现以及后面组件的下载。
css加载的时候是允许并发加载的，但是对于js来说，会禁用并发并且阻止其他内容的下载，所以当把js放到顶部的时候也可能会出现白屏现象。

2. 白屏问题和FOUC
如果把样式放在底部，对于IE浏览器，在某些场景下（新窗口打开，刷星等）页面会出现白屏，而不是逐步展现内容。
另外，如果使用@import，即使CSS放入link，并且放在头部，也可能出现白屏。
FOUC(flash of unstyled content)，无样式内容闪烁，如果把样式放在页面底部，对于IE浏览器，在某些场景下(点击链接，输入url，使用书签进入等)，会出现FOUC现象(即逐步加载无样式的内容，等到CSS全部加载完成之后页面突然展现样式。)对于firefox会一直表现出FOUC。

3. 加载异步，async和defer
-  `<script src="xxx.js"></script>`没有defer和async，浏览器会立即加载并且执行指定的脚本，即不等待后续载入的文档元素，读到就加载并执行。
- `<script async src="xxx.js"></script>` 有async，**加载和渲染后续文档元素的过程将和script.js的加载与执行并行进行(即异步)**。
- `<script defer src="xxx.js"></script>`有defer，**加载后续文档元素的过程将和script.js的加载并行进行(异步)**，但是script.js的**执行要在所有的元素解析完成之后**，将DOMContentLoaded事件触发之前完成。
- defer和async的区别： defer脚本延迟到文档解析和显示之后才执行，是由顺序的。而async不保证顺序。


4.浏览器的渲染机制

- 解析HTML标签，构建DOM树
- 解析CSS标签，构建CSSOM树
- 把DOM和CSSOM组合成渲染树(render tree)
- 把渲染树的基础上进行布局，计算每个节点的几何结构
- 把每个节点绘制到屏幕上(painting)

5.基本调试的方法

- alert
- console.log
- 设置断点
- 二分法
