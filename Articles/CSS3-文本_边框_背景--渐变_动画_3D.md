---
title: CSS3-文本_边框_背景--渐变_动画_3D
date: 2017/09/13 00:00:01
tags: 
- 前端
- CSS
- CSS3
categories: 
- 前端
---
CSS3-文本_边框_背景--渐变_动画_3D
<!--more-->

[css3渐变生成工具](http://www.colorzilla.com/gradient-editor/)

#文本
###text-overflow
- clip 隐藏超出文本
- ellipsis 超出部分使用省略号

对于省略号 还需要配合其他属性进行使用
```
        .panel {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
```
超出部分使用省略号的方法：**设置不换行，超出部分隐藏，最后设置使用省略号才能达到效果。**

#文本换行
###word-wrap
允许长单词或url地址换行到下一行
- normal 只在允许的断字点换行
- break-word 在长单词或url地址内部进行换行
###word-break
规定自动换行的处理方法
- normal 使用浏览器默认的换行规则
- break-all 允许在单词内换行
- keep-all 只能在半角空格或连字符处换行

###white-space
设置如何处理元素内的空白
- normal 连续空白会被忽略
- pre 空白会被浏览器保留，其行为类似html中的标签
- nowrap 文本不会换行，文本会在同一行上继续，直到遇到标签为止
- pre-wrap 保留空白符序列，但是正常的进行换行
- pre-line 合并空白符序列，但是保留换行符
- inherit 规定从父元素继承属性值

###text-shadow
text-shadow: 颜色(color) x轴(X offset) y轴(Y offset) 模糊半径(blur)
text-shadow: x轴(X offset) y轴(Y offset) 模糊半径(blur) 颜色(color) 

和box-shadow很像
可以设置偏移，颜色，阴影大小
```
text-shadow: 2px 2px 3px #333;
```
多个样式可以`,`隔开
结合背景颜色，通过偏移负距离，做出多种效果

#border

###border-radius
通过border-radius设置元素的圆角半径
```
border-radius: 5px;
```
对于正方形，border-radius设置为边长的50% ，就可以变为圆形
###border-image
可以使用图片作为边框的修饰

###box-shadow
box-shadow: inset x-offset y-offset blur-radius spread-radius color
多个样式的使用用`,`隔开
###box-sizing
改变盒模型
- content-box 标准盒模型
- border-box   

#background

###background-size
规定背景图片的尺寸，如果是百分比 那么尺寸相对于父元素的宽度和高度

- length: 设置背景图像的高度和宽度，第一个值为宽度，第二个为高度，如果只设置一个，第二个值会被设为auto
- percentage: 以父元素的百分比来设置背景图像的宽度和高度，第一个值为宽度，第二个为高度，如果只设置一个，第二个值会被设为auto
- cover: 把背景图像扩展到足够大，以使背景图像完全覆盖背景区域，背景图像的某些部分也许无法显示在背景定位区域中。
- contain:  把图像扩展至最大尺寸，以使其宽度和高度完全适应内容区域。
```
        .box {
            width: 180px;
            height: 120px;
            border: 1px solid red;
            background: url("https://unsplash.it/200/300/?random") 0 0 no-repeat;
            /*控制图片展示的大小*/
            /*background-size: 180px 120px;*/
            /*background-size: contain;*/
            /*background-size: cover;*/
        }
```
如果想让box全屏 可以这么设置：
```
        html,body {
            height: 100%;
        }
        .box {
            margin: 0;
            /*不设置宽度*/
            height: 100%;
            /*设置height为100% 没效果 要设置父容器也是100%height，即body html都要设置*/
            background: url("https://unsplash.it/400/600/?random") center center no-repeat;
            background-size: cover;
        }
```
###background-origin
规定background-position属性相对于什么位置来定位
- padding-box 相对于内边距来定位
- border-box 相对于边框盒
- content-box 相对于内容框来定位

如果background-attachment 属性为fixed 则该属性没有效果

###background-clip
规定背景的绘制区域
- padding-box 背景被裁剪到内边距
- border-box 背景被裁剪到边框盒
- content-box 背景被裁剪到内容框

###多背景
css3可以使用多张背景图片
```
            background: url("haoroomCSS1_s.jpg") 0 0 no-repeat,
                        url("haoroomCSS2_s.jpg") 200px 0 no-repeat,
                        url("haoroom.jpg") 400px 201px no-repeat;
```

#Gradient
CSS3渐变分为 liner-gradient 和 radial-gradient

其语法由于每个浏览器都有不同的实现，介绍2012.04w3c推荐标准语法
##线性渐变 liner-gradient

在线性渐变过程中，颜色沿着一条直线过度，从左到右，从右到左，从顶到底，从底到顶或者沿着任何轴

```
linear-gradient([[<angle> | to <side-or-conrner>],]?<color-stop>[,<color-stop>]+)
```
W3C标准线性渐变语法包括3个重要属性参数：

第一个参数指定了渐变的方向，同时决定了渐变颜色的停止位置。这个参数值可以省略，当省略不写的时候其取值为` to bottom`

在决定渐变的方向主要有2种方法：
- angle：通过角度来确定渐变的方向，0度表示渐变方向从下往上，90度表示渐变方向从左到右，如果取值为下值，其角度按顺时针方向旋转。
- 关键词： 通过关键词来确定渐变的方向。比如 to top, to right,to bottom, to left.
这些关键词对应的角度值，除了使用to top, top left之外，还可以使用top left左上角到右下角，top right左上角到右下角等。

第二个参数和第三个参数，表示颜色的起始点和结束点。可以在插入多个颜色值。

水平和垂直渐变举例：
```
            width: 200px;
            height: 50px;
            /*background: linear-gradient(red,blue); 默认从上到下 和to bottom一样*/
            /*background: linear-gradient(to right,red,blue); 从左到右*/
            /*background: linear-gradient(to right,red,blue,yellow,green);可以有多个颜色*/
```
对角的线性渐变 指定水平和垂直的起始位置来制作对角渐变:
```
            /*左上到右下 to bottom 改为 to bottom right*/
            background: linear-gradient(to bottom right, red,blue);
```

###使用角度
如果你想要在渐变的方向上做更多的控制，你可以定义一个角度，而不用预定义方向（to bottom、to top、to right、to left、to bottom right，等等）。

角度是指水平线和渐变线之间的角度，逆时针方向计算。换句话说，0deg 将创建一个从下到上的渐变，90deg 将创建一个从左到右的渐变。

`background: linear-gradient(180deg, red, blue);`
##径向渐变 radial-gradient
是圆形或者椭圆形渐变，颜色不再沿着一条直线轴变化，而是从一个起点朝所有方向混合。
```
radial-gradient([[<shape> || <size>] [at <position>]?, | at <position>,]?<color-stop>[,<color-stop>]+);
```

#过渡和动画
##过渡
在 CSS3引入 transition之前没有时间轴，所有的状态变化都是瞬间完成的。

transition的作用在于，指定状态变化所需要的时间

`transition: all 1s;`

*另外transition **一般加在元素本体上**，而不是伪类上，虽然加到伪类上也能用，但是多个伪类状态的时候，只需要在本体上写一个transition就可以了*

###指定属性
如果不想写all  想针对特定的属性进行过渡的动画变化，例如针对height属性，可以写成
`transition: 1s height;`
这样的话，只有height是过渡的，其他属性比如宽度还是瞬间完成的。
另外同一行transition中，可以分别制定多个属性
`transition: 1s height,1s width;`

###delay
对于上面的例子`transition: 1s height,1s width;`,height和width的变化是同时进行的。如果我们希望height先发生变化，然后width再发生变化。就可以为width指定一个delay参数：
`transition: 1s height,1s 1s width;`
上面代码指定，width在1s之后，再开始变化

delay的意义在于它指定了动画发生的顺序，使得多个不同的transition可以连在一起，形成复杂的效果。

###transition-timing-function
缓动函数，状态变化速度，默认不是匀速的，而是逐渐放慢，叫做ease；
`transition: 1s ease;`

除了ease还有：
- linear： 匀速；
- ease-in： 加速；
- ease-out：减速；
- cubic-bezier函数： 自定义速度演示,例如：`transition: 1s height cubic-bezier(.83,.97.05,1.44);`


*目前，包括IE10在内的各大浏览器都已经支持无前缀的transition*
*transition需要明确的知道，开始的状态和结束的状态的具体数值，才能计算出中间状态，什么none到block之类的是不行的*
*transition是一次性的，不能重复发生，除非一再触发*

#animation
transition比较简单，animation可以实现复杂的动画，使用animation我们需要定义动画过程，也就是关键帧

定义一个动画 rainbow：
```
        @keyframes rainbow {
            0% {background: #c00;}
            50% {background: orange;}
            100% {background: yellowgreen;}
        }
```
使用@keyframes 定义动画，上述rainbow分为3个状态，值是每个状态下的属性值，如果有需要还可以插入更多状态。

定义好关键帧之后，就可以给DOM元素绑定动画了，类似于事件：
```
        .box1:hover {
            animation: 1s rainbow;
        }
```
当hover时，会进行动画效果，默认只播放一次，加入infinite关键字，可以让动画无限次播放。
```
        .box1:hover {
            animation: 1s rainbow infinite;
        }
```
也可以指定具体循环的次数，比如说循环3次:
```
        .box1:hover {
            animation: 1s rainbow 3;
        }
```
###animation-fill-mode
动画结束后会立即从结束跳回起始状态。如果想让动画保持在结束状态，需要使用animation-fill-mode属性：
```
        .box1:hover {
            animation: 1s rainbow forwards;
        }
```
有如下属性值：
- none 默认 回到动画没开始的状态
- forwards: 让动画停留在结束状态
- backwards： 让动画回到第一帧
- both： 根据animation-direction轮流应用forwards和backwards

###animation-direction
动画循环播放时，每次都是从结束状态跳回到起始状态，再开始播放。animation-direction属性，可以改变这种行为。
比如说可以让动画从100%到0%开始播放

另外还有取值为：
- alternate
- reverse
- alternate-reverse
等值，**最常用的就是normal和reverse**，其他属性浏览器兼容不佳，慎用。

###语法
同transition一样，animation也是一个简写形式
```
div:hover {
  animation: 1s 1s rainbow linear 3 forwards normal;
}
```
这是一个简写形式，可以分解成各个单独的属性
```
div:hover {
  animation-name: rainbow;
  animation-duration: 1s;
  animation-timing-function: linear;
  animation-delay: 1s;
  animation-fill-mode:forwards;
  animation-direction: normal;
  animation-iteration-count: 3;
}
```
@keyframes关键字用来定义动画的各个状态，它的写法相当自由：
```
        @keyframes rainbow {
            0% {background: #c00;}
            50% {background: orange;}
            100% {background: yellowgreen;}
        }
```
其中0%可以用from代表，100%可以用to代表，所以也可以写成：
```
        @keyframes rainbow {
            from {background: #c00;}
            50% {background: orange;}
            to {background: yellowgreen;}
        }
```
如果省略某个状态，浏览器会自动推算中间状态，所以下面都是合法的写法。
```
@keyframes rainbow {
  50% { background: orange }
  to { background: yellowgreen }
}
```
```
@keyframes rainbow {
  to { background: yellowgreen }
}
```
甚至，可以把多个状态写在一行。
```
@keyframes pound {
  from，to { transform: none; }
  50% { transform: scale(1.2); }
}
```
另外一点需要注意的是，浏览器从一个状态向另一个状态过渡，是平滑过渡。steps函数可以实现分步过渡。
```
div:hover {
  animation: 1s rainbow infinite steps(10);
}
```
###animation-play-state
有时，动画播放过程中，会突然停止。这时，默认行为是跳回到动画的开始状态，如果想让动画保持突然终止时的状态，就要使用animation-play-state属性。
比如说页面上有个滚动的div,一直在循环滚动，hover时设置停止，那么就可以设置为
```
    <style>
        @keyframes scroll {
            from {left: 0;}
            to {left: 100%;}
        }
        .box1 {
            width: 200px;
            height: 200px;
            border: 1px solid red;
            position: relative;
            animation-play-state: running; 
            animation: 10s scroll infinite;
        }
        .box1:hover {
            animation-play-state: paused;
        }
    </style>
</head>
<body>
<div class="box1">我在滚动，hover我就停下来了</div>
```
注意animation-play-state的用法，hover时停下来，所以hover时设置paused.


#变形
CSS3中可以通过transform属性修改元素的变形、旋转、缩放、倾斜特性，在CSS3中transform主要包括以下几种：旋转rotate、扭曲skew、缩放scale和移动translate以及矩阵变形matrix

`transform ： none | <transform-function> [ <transform-function> ]*`

transform中使用多个属性时却需要有空格隔开，可用于内联(inline)元素和块级(block)元素
##旋转 rotate
可以通过rotate使元素旋转一定的度数

`transform:rotate(30deg);`

旋转是*顺时针*的
旋转之后元素仍占据原来位置，实际上**所有的transform都是这样，缩放、位移等都不会改变元素占据的位置**

元素旋转的的基点默认是中心，可以通过transform-origin属性改变

`transform:rotate(30deg); transform-origin: 0% 0%;`

transform-origin的取值可以是:
- top, bottom, left, right, center
- 百分数

transform-origin属性对下面介绍的transform都有作用

###位移

可以通过translate使元素平移

`transform:translate(x,y);`

`transform:translate(200px,150px);`

也可以简单只移动一个坐标

```
transform:translateX(100px);
transform:translateY(100px);
```
**translate可以写成百分比，在绝对居中写负margin的时候可以写成-50%,-50%,即 `transform: translate(-50%,-50%)`**

###缩放

可以通过scale使元素缩放一定的比例，和translate类似，也有三个方法

- scale(x,y)：使元素水平方向和垂直方向同时缩放
- scaleX(x)：元素仅水平方向缩放
- scaleY(y)：元素仅垂直方向缩放

对于scale只设置一个参数，一相同的比例缩放两个方向

```
transform:scale(2, 0.5);
transform:scaleY(0.3);
transform:scaleY(2);
transform:scale(3);
```

###扭曲

可以通过skew使元素扭曲一定的度数，和上面一样也有三中类似的用法
```
transform:skew(10deg, 20deg);
transform:skewX(10deg);
transform:skewY(10deg);
transform:skew(10deg);
```

![1](http://upload-images.jianshu.io/upload_images/7113407-47dea771bb356fae.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![2](http://upload-images.jianshu.io/upload_images/7113407-a902705fed80329c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![3](http://upload-images.jianshu.io/upload_images/7113407-b4e9dd46e8f96a14.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

###矩阵

`matrix(<number>, <number>, <number>, <number>, <number>, <number>)`
以一个含六值的(a,b,c,d,e,f)变换矩阵的形式指定一个2D变换，相当于直接应用一个[a b c d e f]变换矩阵。

就是基于水平方向（X轴）和垂直方向（Y轴）重新定位元素,此属性值使用涉及到数学中的矩阵，可以参考[css3-transform-matrix-矩阵](http://www.zhangxinxu.com/wordpress/2012/06/css3-transform-matrix-%E7%9F%A9%E9%98%B5/)

#3D
CSS3带来了DOM的3D效果，元素需要设置需要设置perspective来激活3D效果，可以通过两种方式实现

- 在transform属性中使用perspective方法
` transform: perspective( 600px );`

- 直接使用perspective属性
` perspective: 600px;`

perspective属性的值决定了3D效果的强烈程度，可以认为是观察者到页面的距离。值越大距离越远，视觉上的3D效果就会相应的减弱。perspective: 2000px; 会产生一个好像我们使用望远镜看远方物体的3D效果，perspective: 100px;会产生一个小昆虫看大象的效果。

看个例子
```
<style>
    .container {
        width: 200px;
        height: 200px;
        border: 1px solid #CCC;
        margin: 0 auto 40px;
    }
    .box {
        width: 100%;
        height: 100%;
    }
    #red1 .box {
      background-color: red;
      transform: perspective( 600px ) rotateY( 45deg );
    }
</style>
<section id="red1" class="container">
    <div class="box red"></div>
</section>
```
![red](http://upload-images.jianshu.io/upload_images/7113407-661cb07cffc4c0b1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
```
<style>
    #blue1{
        perspective: 600px;
    }
    #blue1 .box {
      background-color: blue;
      transform: rotateY( 45deg );
    }
</style>

<section id="blue1" class="container">
    <div class="box blue1"></div>
</section>
```

![blue](http://upload-images.jianshu.io/upload_images/7113407-6a473cc63e067884.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这两种方式都会触发3D效果，但是有一点不同：第一种方式直接在一个元素上触发3D变形，但是当多个元素的时候变形效果和预期会有所不同，如果使用同样的方法作用于不同位置的元素的时候，每个元素会有自己的轴心，为了解决这个问题，需要**在父元素使用perspective属性**，这样每个子元素都共享相同的3D空间

##3D变形方法

作为一个web者，可能很熟悉两个方向：X & Y，表示元素的水平方向和垂直方向，在perspective激活的3D空间中我们可以在X、Y、Z三个坐标轴上对元素进行变形处理。3D变形使用的变形方法和2D变形一样，如果熟悉2D变形方法很容易掌握3D变形.

![坐标系](http://upload-images.jianshu.io/upload_images/7113407-2b2e27eeaf7120f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


rotateX( angle )
rotateY( angle )
rotateZ( angle )
translateZ( tz )
scaleZ( sz )
translateX()方法使元素延X轴移动，translateZ()使元素延Z轴（在3D空间中方向从前到后）移动。正值使元素离观察者更近，负值使元素变远。


![1](http://upload-images.jianshu.io/upload_images/7113407-9a4740f5ba75117e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![2](http://upload-images.jianshu.io/upload_images/7113407-c8b55a0b13145a39.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
