---
title: CSS浮动定位和BFC边距合并
date: 2017/08/01 18:22:24
tags: 
- 前端
- CSS
- BFC
categories: 
- 前端
---
CSS浮动定位和BFC边距合并
<!--more-->

###一，浮动元素有什么特征？对父容器、其他浮动元素、普通元素、文字分别有什么影响?

浮动模型是一种可视化格式模型，浮动的框可以左右移动（根据float的值而定），直到它的外边缘碰到包含框或者另一个福鼎元素的框的边缘。**浮动元素不在文档的普通流当中**，文档的**普通流中的元素表现的就像浮动元素不存在一样**。

- 三个元素都浮动且宽度足够

```
  <style type="text/css">
    .box{
      border: 4px solid green;
    }
    .red,.blue,.yellow{
      width: 100px;
      height: 100px;
    }
    .red{
      float:left;
      background: red;
    }
    .blue{
      float:left;
      background: blue;
    }
    .yellow{
      float:left;
      background: yellow;
    }
  </style>
</head>
<body>
  <div class="box">
    <div class="red"></div>
    <div class="blue"></div>
    <div class="yellow"></div>
  </div>
</body>
```
上述代码的效果为，

![三个同样大小的元素一起左浮动](http://upload-images.jianshu.io/upload_images/7113407-39f74384859c6b0c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
此时三个元素都为浮动，所以**父元素的高度就为0**了。

- 三个元素都浮动，但是宽度不够

```
<style type="text/css">
    .box{
      border: 4px solid green;
      width: 250px;
    }
    .red,.blue,.yellow{
      width: 100px;
      height: 100px;
    }
    .red{
      float:left;
      background: red;
    }
    .blue{
      float:left;
      background: blue;
    }
    .yellow{
      float:left;
      background: yellow;
    }
  </style>
</head>
<body>
  <div class="box">
    <div class="red"></div>
    <div class="blue"></div>
    <div class="yellow"></div>
  </div>
</body>
```
而当父元素的宽度不足以并排放下三个元素时，后面浮动的元素会被挤到下一行显示，如图：
![父元素宽度不够时](http://upload-images.jianshu.io/upload_images/7113407-9f125e63e3c3b6a8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 宽度不够时，如果前面元素高度过高的时候，后面被挤下来的元素会被卡住。
```
 <style type="text/css">
    .box{
      border: 4px solid green;
      width: 250px;
    }
    .red{
     width: 100px;
      height: 120px;
    }
    .blue,.yellow{
      width: 100px;
      height: 100px;
    }
    .red{
      float:left;
      background: red;
    }
    .blue{
      float:left;
      background: blue;
    }
    .yellow{
      float:left;
      background: yellow;
    }
  </style>
</head>
<body>
  <div class="box">
    <div class="red"></div>
    <div class="blue"></div>
    <div class="yellow"></div>
  </div>
</body>
```
运行效果如图：

![出现卡住的情况](http://upload-images.jianshu.io/upload_images/7113407-70a1f2f355c0f4e7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


- 行框（对普通元素和文字元素带来的影响）
元素浮动之后会脱离文档流，如果后面有一个正常文档流元素，那么这个**正常文档流内的元素的框会无视浮动元素，但是框内的文字内容会受到浮动元素的影响，会产生移动留出空间**。总结就是，浮动元素旁边的行框被缩短，从而给浮动元素留出空间，因而**行框围绕浮动框**。

 ```
<style type="text/css">
    .box{
      border: 5px solid blue;
    }
    .red{
      width:100px;
      height: 100px;
      background: red;
      /* float:left; */
    }
    .green{
      width:200px;
      height: 200px;
      background: green;
    }
    
  </style>
</head>
<body>
  <div class="box">
    <div class="red"></div>
    <div class="green">
      deejaydeejaydeejaydeejaydeejaydeejaydeejaydeejaydeejay
</body
```
正常情况下效果为：
![正常情况下](http://upload-images.jianshu.io/upload_images/7113407-9214566cb9c4e146.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

给.red左浮动之后：

![浮动之后](http://upload-images.jianshu.io/upload_images/7113407-43555d6a26c39ebe.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看到浮动后**绿色div不受红色div的影响，正常布局**，但是**里面的文字部分却被挤到了红色div的外边**。

那么要想组阻止行框在浮动元素外边围绕，可以使用**clear属性**，通过设置属性值为left,right,both和none来表示框的那些边不挨着浮动框。

对于上述例子，我们给绿色的div加上clear:left。效果如图：

![clear:left之后](http://upload-images.jianshu.io/upload_images/7113407-cae1c061818b08c4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





###二，清除浮动指什么? 如何清除浮动? 两种以上方法

指元素的**侧边不允许出现浮动元素**，从而使得浮动元素的不占据普通文档流空间的使得**父元素的高度塌陷问题得到解决**，主要通过clear属性实现

在一中，介绍了父容器中三个元素都浮动之后，父元素的高度会塌陷成0，为了让父元素有高度可以包裹浮动元素，我们有几种方法来清除浮动：
1. 在最后再添加一个空div，对其使用clear:both。
```
<style type="text/css">
    .box{
        border: 5px solid green;
        width: 600px;
    }
    .red,.blue,.yellow{
        width: 100px;
        height: 100px;
    }
    .red{
        background: red;
        float: left;
    }
    .blue{
        background: blue;
        float: left;
    }
    .yellow{
        background: yellow;
        float: left;
    }
    .empty{
        clear: both;
    }
</style>
<div class="box">
    <div class="red"></div>
    <div class="blue"></div>
    <div class="yellow"></div>
    <div class="empty"></div>  /*最后加一个空div设置clear属性*/
</div>
```
效果如图:
![最后加空div，设置clear:both](http://upload-images.jianshu.io/upload_images/7113407-c4c4a71fc86b1eaf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

2. BFC清理浮动

BFC（Block Format Context）块格式化上下文，即一个设置了float或者绝对/固定定位（fixed/absolute）的inline-block元素，table-cell元素以及设置了overflow属性的block元素就会新建一个块级格式化上下文区域。在一个BFC中，元素和元素之间从上到下排列，他们的外边距会进行合并。
总结一下就是，设置了上述属性（浮动，绝对定位，overflow等）的元素，他们会另外新建一个BFC，导致他们不会遵守当前所在的BFC的规矩，比如外边距合并等。

- BFC的特性1  阻止外边距折叠：
BFC会阻止外边距折叠，只有处于同一个BFC中的元素才会发生外边距的折叠，所以要解决重叠问题，只需要让他们不在同一个BFC中就可以。
- BFC的特性2 不会重叠浮动元素
我们上面提到的例子。两个元素，元素1浮动之后，元素2会无视元素1正常布局，但是元素2中的文字内容不会无视元素1，所以会产生文字内容围绕元素1的情况，这时候我们可以给元素2设置display：inline-block等属性，让其在一个新的BFC中，两个不同BFC中的元素就不会发生上述情况了。
- BFC的特性3  BFC可以包含浮动（用作清除浮动）
举例来说明：
1. 同一BFC中外边距合并的情况
```
<style type="text/css">
    .box1{
        width: 100px;
        height: 100px;
        background: red;
        margin: 40px;
    }
    .box2{
        width: 100px;
        height: 100px;
        background: blue;
        margin: 40px;
    }
    /*两个box都设置外边距为40px,但是彼此之间距离还是为40px，因为处于同一BFC中，外边距合并了*/
</style>

<div class="box1"></div>
<div class="box2"></div>
```
2. 父子外边距合并的情况
 ```
<style type="text/css">
    .box1{
        width: 100px;
        height: 100px;
        background: red;
    }
    .box2{
        width: 100px;
        height: 100px;
        background: blue;
    }
    .ct{
        background: pink;
    }
    .ct h1{
        background: yellow;
        margin: 40px;
    }
</style>

<div class="box1"></div>
<div class="box2"></div>
<div class="ct">
    <h1>h1</h1>
</div>
```
运行效果为：
![父子元素外边距合并](http://upload-images.jianshu.io/upload_images/7113407-c516569c51c3e7e2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
那么针对这种情况，一般可以给父元素.ct设置一个border或者是设置padding，就能避免这种情况，当然，也可以通过BFC来避免：**给父元素.ct设置overflow:auto(也可以用其他属性overflow,display:inline-block,float;left等)来产生新的BFC从而避免合并**。代码如下：
```
 .ct{
        background: pink;
        /*border: 1px solid #ccc;*/
        /*padding: 1px;*/
        overflow: auto;
    }
```

3. BFC不会重叠浮动元素
文字内容围绕浮动元素的情况
```
<style type="text/css">
    .ct{
        border: 1px solid;
        width: 500px;
    }
    .aside{
        width: 50px;
        height: 50px;
        background: red;
        float: left;
    }
    .content{
        background: yellow;
    }
</style>
<div class="ct">
    <div class="aside"></div>
    <div class="content">
        BFC的特性1 阻止外边距折叠：
        BFC会阻止外边距折叠，只有处于同一个BFC中的元素才会发生外边距的折叠，所以要解决重叠问题，只需要让他们不在同一个BFC中就可以。
        BFC的特性2 不会重叠浮动元素
        我们上面提到的例子。两个元素，元素1浮动之后，元素2会无视元素1正常布局，但是元素2中的文字内容不会无视元素1，所以会产生文字内容围绕元素1的情况，这时候我们可以给元素2设置display：inline-block等属性，让其在一个新的BFC中，两个不同BFC中的元素就不会发生上述情况了。
        BFC的特性3 BFC可以包含浮动（用作清除浮动）
    </div>
</div>
```

![文字内容围绕浮动元素的情况](http://upload-images.jianshu.io/upload_images/7113407-f520b0da3b918be7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

给.content建立新的BFC
```
.content{
         background: yellow;
         overflow: hidden;
     }
```
之后，

![利用BFC之后](http://upload-images.jianshu.io/upload_images/7113407-d0117f4b07324d64.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4. BFC可以包含浮动：

```
<style type="text/css">
    .ct{
        background: blue;
        /*float: left;*/
    }
    .box{
        width: 100px;
        height: 100px;
        border: 1px solid red;
        float: left;
    }
</style>
<div class="ct">
    <div class="box"></div>
    <div class="box"></div>
</div>
```
此时运行结果为：
![包含浮动之前](http://upload-images.jianshu.io/upload_images/7113407-4ca9c2bb881f4468.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到父元素的bg-color没显示，即父元素没有撑开，那么这时候给父元素创建BFC（利用float,display,position,overflow等），就能包含住浮动的元素。
```
.ct{
        background: blue;
        float: left;
    }
```

![创建BFC包含浮动之后](http://upload-images.jianshu.io/upload_images/7113407-7dc73b795ee6db5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

所以综上所述：我们可以**利用BFC来清楚浮动，实质上为包含浮动。只要给父容器形成BFC就能达到效果**。
**我们一般形成BFC的方法汇总：**
- float为 left/right
- overflow为 hidden/auto/scroll
- display为 inline-bolck/table-cell/table-caption
- position为 /absoulte/fixed

3. 通用的清理浮动的方式
定义一个clearfix的类样式，给**需要清除浮动的父元素应用**。
```
.clearfix{
    *zoom: 1;
    /*IE6 7用于触发IE的hasLayOut*/
}
.clearfix:after{
    /*给父元素应用：after,意味着在子元素的最后加一个内容为空格的元素，给这个元素应用clear:both即可清除浮动*/
    content: ' ';
    display: block;
    /*display: table;*/
    clear: both;
}
```
这样做的好处是不用在HTML里面增加无用标签。总的来说，清除浮动方法就分为：**1.使用clear属性清除浮动。2.或者让父元素形成BFC。**

###三，有几种定位方式，分别是如何实现定位的，参考点是什么，使用场景是什么？


值 | 属性
-- | -- 
inherit | 规定应该从父元素继承position属性的值（很少用）
static | 默认值，没有定位，正常流元素
relative | 生成相对定位的元素，**相对于元素本身正常位置**进行定位，因此，`left: 20px;`回向元素的left位置添加20px
absolute | 生成绝对定位的元素，**相对于static定位之外的第一个祖先元素进行定位**，元素的位置通过left,top,right和bottom进行确定
fixed | 生成决定定位的元素，相对于**浏览器窗口**进行定位。元素的位置通过left,top,right和bottom进行确定
sticky | css3新属性，兼容性很差



CSS有三种基本的定位机制：普通流，相对定位和绝对定位
- 普通流是默认定位方式，在普通流中元素框的位置由元素在HTML中的位置决定，元素position属性为static或继承来的static时就会按照普通流定位，是最常见的方式。
- 相对定位比较简单，对应position属性的relative值，如果对一个元素进行相对定位，**它将出现在他所在的位置上**，然后可以通过设置垂直或者水平位置，让这个元素**相对于他自己进行移动**，在使用相对定位时，**无论元素是否移动，元素在文档流中占据原来空间，只是表现出来的位置会改变**。(看起来位置改变了，但是其他元素看你还是存在于原来的位置上)
- 绝对定位和固定定位
相对定位可以看做特殊的普通流定位，元素位置是相对于他在普通流中位置发生变化，而**绝对定位是元素的位置与文档流无关，也不占据文档流空间，普通流中的元素布局就像绝对定位不存在一样**。
绝对定位的元素的位置是相对于**距离最近的非static祖先元素**位置决定的。如果元素没有已定位的祖先元素，那么他的位置就相对于初始包含块html来定位。
因为绝对定位与文档流无关，所以绝对定位的元素可以覆盖页面上的其他元素，可以通过z-index属性控制叠放顺序，z-index越高，元素位置越靠上。
另外注意，absolute是相对于初始包含块**html**定位的而不是body，还有，absolute是相对于包含块的**内边框**进行偏移的。

- 浮动和绝对定位的对比
二者都会脱离文档流
浮动元素会对其他浮动元素产生影响，也会对正常元素中的文本内容产生影响，但是绝对定位不会，都无视它。

- 绝对定位的宽度
绝对定位的宽度是收缩的，如果想要撑满父容器，可以设置width:100%;这个width：100%的意思是，当父容器里有 绝对定位 的子元素时，子元素设置width:100%实际上指的是相对于父容器的padding+content的宽度。当子元素是非绝对定位的元素时width:100%才是指子元素的 content 等于父元素的 content宽度

- 绝对定位让元素垂直水平居中
让父元素relative，要居中的子元素absolute，然后left:50%,偏移父元素width的50%，此时再偏移子元素自身的半个width就能达到居中的效果。
```
<style type="text/css">
    .ct{
        position: relative;
        width: 200px;;
        height: 200px;
        border: 5px solid red;
    }
    .box{
        position: absolute;
        width: 100px;
        height: 100px;
        background: green;
        left: 50%;
        margin-left: -50px; /*向右偏移box的width的一半*/
        top: 50%;
        margin-top: -50px; /*向上偏移box的height的一半*/
    }
</style>
<div class="ct">
    <div class="box">
    </div>
</div>
```

###四，z-index 有什么作用? 如何使用?

z-index规定了元素在Z轴（距离用户远近）上的顺序，值越大则越靠近用户，表现就是元素在最上面。

z-index仅在设置了position非static属性的元素生效，且z-index的值只能在兄弟元素之间比较。

Z-index默认值为auto,则不建立层叠上下文。设置为0则会建立层叠上下文。例如：
```
img
  {
  position:absolute;
  left:0px;
  top:0px;
  z-index:1;
  }
```

###五，position:relative和负margin都可以使元素位置发生偏移?二者有什么区别

二者都可以使得元素相对于自身原来的位置进行移动。

- position: relative;偏移之后，周围的元素会以为他还在原先文档流的地方，不会使得后面的元素位置进行变动。

- 而负margin进行的偏移会影响到后面元素的位置。


###六，BFC 是什么？如何生成 BFC？BFC 有什么作用？举例说明

- BFC（Block Format Context）块格式化上下文，即一个设置了float或者绝对/固定定位（fixed/absolute）的inline-block元素，table-cell元素以及设置了overflow属性的block元素就会新建一个块级格式化上下文区域。在一个BFC中，元素和元素之间从上到下排列，他们的外边距会进行合并。
总结一下就是，设置了上述属性（浮动，绝对定位，overflow等）的元素，他们会另外新建一个BFC，导致他们不会遵守当前所在的BFC的规矩，比如外边距合并等。

- BFC的特性1  阻止外边距折叠：
BFC会阻止外边距折叠，只有处于同一个BFC中的元素才会发生外边距的折叠，所以要解决重叠问题，只需要让他们不在同一个BFC中就可以。
- BFC的特性2 不会重叠浮动元素
我们上面提到的例子。两个元素，元素1浮动之后，元素2会无视元素1正常布局，但是元素2中的文字内容不会无视元素1，所以会产生文字内容围绕元素1的情况，这时候我们可以给元素2设置display：inline-block等属性，让其在一个新的BFC中，两个不同BFC中的元素就不会发生上述情况了。
- BFC的特性3  BFC可以包含浮动（用作清除浮动）
举例来说明：
1. 同一BFC中外边距合并的情况
```
<style type="text/css">
    .box1{
        width: 100px;
        height: 100px;
        background: red;
        margin: 40px;
    }
    .box2{
        width: 100px;
        height: 100px;
        background: blue;
        margin: 40px;
    }
    /*两个box都设置外边距为40px,但是彼此之间距离还是为40px，因为处于同一BFC中，外边距合并了*/
</style>

<div class="box1"></div>
<div class="box2"></div>
```
2. 父子外边距合并的情况
 ```
<style type="text/css">
    .box1{
        width: 100px;
        height: 100px;
        background: red;
    }
    .box2{
        width: 100px;
        height: 100px;
        background: blue;
    }
    .ct{
        background: pink;
    }
    .ct h1{
        background: yellow;
        margin: 40px;
    }
</style>

<div class="box1"></div>
<div class="box2"></div>
<div class="ct">
    <h1>h1</h1>
</div>
```
运行效果为：
![父子元素外边距合并](http://upload-images.jianshu.io/upload_images/7113407-c516569c51c3e7e2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
那么针对这种情况，一般可以给父元素.ct设置一个border或者是设置padding，就能避免这种情况，当然，也可以通过BFC来避免：**给父元素.ct设置overflow:auto(也可以用其他属性overflow,display:inline-block,float;left等)来产生新的BFC从而避免合并**。代码如下：
```
 .ct{
        background: pink;
        /*border: 1px solid #ccc;*/
        /*padding: 1px;*/
        overflow: auto;
    }
```

3. BFC不会重叠浮动元素
文字内容围绕浮动元素的情况
```
<style type="text/css">
    .ct{
        border: 1px solid;
        width: 500px;
    }
    .aside{
        width: 50px;
        height: 50px;
        background: red;
        float: left;
    }
    .content{
        background: yellow;
    }
</style>
<div class="ct">
    <div class="aside"></div>
    <div class="content">
        BFC的特性1 阻止外边距折叠：
        BFC会阻止外边距折叠，只有处于同一个BFC中的元素才会发生外边距的折叠，所以要解决重叠问题，只需要让他们不在同一个BFC中就可以。
        BFC的特性2 不会重叠浮动元素
        我们上面提到的例子。两个元素，元素1浮动之后，元素2会无视元素1正常布局，但是元素2中的文字内容不会无视元素1，所以会产生文字内容围绕元素1的情况，这时候我们可以给元素2设置display：inline-block等属性，让其在一个新的BFC中，两个不同BFC中的元素就不会发生上述情况了。
        BFC的特性3 BFC可以包含浮动（用作清除浮动）
    </div>
</div>
```

![文字内容围绕浮动元素的情况](http://upload-images.jianshu.io/upload_images/7113407-f520b0da3b918be7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

给.content建立新的BFC
```
.content{
         background: yellow;
         overflow: hidden;
     }
```
之后，

![利用BFC之后](http://upload-images.jianshu.io/upload_images/7113407-d0117f4b07324d64.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4. BFC可以包含浮动：

```
<style type="text/css">
    .ct{
        background: blue;
        /*float: left;*/
    }
    .box{
        width: 100px;
        height: 100px;
        border: 1px solid red;
        float: left;
    }
</style>
<div class="ct">
    <div class="box"></div>
    <div class="box"></div>
</div>
```
此时运行结果为：
![包含浮动之前](http://upload-images.jianshu.io/upload_images/7113407-4ca9c2bb881f4468.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
可以看到父元素的bg-color没显示，即父元素没有撑开，那么这时候给父元素创建BFC（利用float,display,position,overflow等），就能包含住浮动的元素。
```
.ct{
        background: blue;
        float: left;
    }
```

![创建BFC包含浮动之后](http://upload-images.jianshu.io/upload_images/7113407-7dc73b795ee6db5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

所以综上所述：我们可以**利用BFC来清楚浮动，实质上为包含浮动。只要给父容器形成BFC就能达到效果**。
- **我们一般形成BFC的方法汇总：**
1. float为 left/right
2. overflow为 hidden/auto/scroll
3. display为 inline-bolck/table-cell/table-caption
4. position为 /absoulte/fixed

###七，在什么场景下会出现外边距合并？如何合并？如何不让相邻元素外边距合并？给个父子外边距合并的范例

外边距合并出现的三个场景。

1. 同一个BFC，且同处于普通流中的垂直相邻元素外边距合并。
2. 父子元素的外边距合并。
3. 空元素的外边距合并。

外边距合并的规则：
- 两个margin都是正值的时候，取两者的**最大值**；
- 当 margin 都是负值的时候，取的是其中**绝对值较大**的，然后，从0位置，负向位移；
- 当有正有负的时候，先取出**负 margin 中绝对值中最大的**，然后，和**正 margin 值中最大的 margin 相加**。
- 所有毗邻的margin要一起参与运算，不能分步进行。

阻止外边距合并的方法：
- 被非空内容、padding、border 或 clear 分隔开（加个padding:1px,加个border等）。
- 不在一个普通流中或一个BFC中（形成新的BFC）。
- margin在垂直方向上不相邻。


所以，当处于下列情况时会出现外边距的合并：
- 这些margin都处于普通流中，并在同一个BFC中，
- 这些margin没有被非空内容、padding、border 或 clear 分隔开，
- 这些margin在垂直方向上是相邻的，包括以下几种情况：
 -  一个box的top margin与第一个子box的top margin(父子元素margon-top合并)
 -  一个box的bottom margin与最后一个子box的bottom margin，但须在该box的height 为auto的情况下
 -  一个box的bottom margin与紧接着的下一个box的top margin（相邻同级的margin-bottom和margin-top）
 -  一个box的top margin与其自身的bottom margin，但须满足没创建BFC、零min-height、零或者“auto”的height、没有普通流的子元素。
