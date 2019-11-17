---
title: CSS选择器
date: 2017/07/30 23:22:24
cover: https://content.linkedin.com/content/dam/me/learning/blog/2016/september/CSS.jpg
tags: 
- 前端
- CSS
categories: 
- 前端
---
CSS选择器
<!--more-->

###一，class 和 id 的使用场景?
id选择器，匹配特定id的元素。
class是类选择器，匹配class包含（**不是等于**）特定类的元素。
- 两者的区别
id选择器是唯一的，一个id在一个页面中只可以使用一次，不允许出现相同id的两个元素，但是同个class在一个页面中可以有多个。
在选择器优先级上，id的优先级要高于class。
- 应用的场景
1. id

根据提供的唯一的id，快速获取标签对象。例如
`document.getElementById(id);`
在<label>标签中使用，用id和label标签的for属性进行配对,例如
```
<label for="username">用户名</label>
<input type="text" name="username" id="username">
```

2. class

把一些要用到的特定的样式放到一个class类中，当需要用到这些特定的样式的时候，给相应的元素加上这个class类就好。

id一般多用于外部结构，例如header，content，footer等有着唯一特定id的元素上，而class一般是用于内部结构的可能需要相同样式的一些元素（比如说几个需要相同样式的div）上。另外一般让class做id的子元素。


###二，CSS选择器常见的有几种?

常见的选择器可以分为：

1, 基础选择器

选择器 |  含义
--  | --
* | 通用元素选择器，匹配页面任何元素（很少使用）
#id | id选择器，匹配特定id的元素
.class |  类选择器，匹配class包含（不是等于）特定类的元素
element | 标签选择器


2. 组合选择器

选择器 | 含义
-- | --
E,F | 多元素选择器，用逗号,分隔，**同时选中**元素E或元素F
E F | 后代选择器，用空格分隔，匹配E元素所有的后代，（不只是子元素，子元素向下递归）元素F
E>F | 子元素选择器，用>分隔，匹配E元素的所有**直接子元素**，和E F相区别点在于只会选择**向下一级**的子元素，再向下的不会被选中
E+F | 直接相邻选择器，匹配E元素之后的**相邻的同级元素F**，只匹配相邻的
E~F | 普通相邻选择器（弟弟选择器），匹配E元素之后的同级元素F（无论直接相邻与否），会匹配所有的同级元素，无论是否直接相邻。
.class1.class2 | id和class选择器和选择器连写的时候，**中间没有分隔符**， .和 #本身充当分隔符的元素
element#id | id和class选择器和选择器连写的时候，**中间没有分隔符**，.和#本身充当分隔符的元素，表示既满足第一个选择器又满足第二个选择器的元素。

E>F使用的比E F多，因为E F可能选择多了一些元素，导致一些误操作。

3. 属性选择器

选择器 | 含义
-- | --
E[attr] | 匹配所以**具有属性attr**的元素，例如div[id]就能取到所有id属性的div
E[attr=value] | 匹配属性attr的**值为value**的元素，div[id=test]，匹配id=test的div
E[attr~=value] | 匹配所有属性attr具有多个空格分隔，**其中一个值等于value**的元素
E[attr^=value] | 匹配属性attr的值以value**开头**的元素
E[attr$=value] | 匹配属性attr的值以value**结尾**的元素
E[attr*=value] | 匹配属性attr的**值包含value**的元素

4. 伪类选择器
可以理解为一个元素的另外一种状态

选择器 | 含义
-- | --
E:firsr-child | 匹配元素E的**第一个**子元素
E:link | 匹配所有**未被点击**过的链接
E:visited | 匹配所有**已被点击**的链接
E:active | 匹配鼠标**已经其上按下，还没有释放**的E元素
E:hover | 匹配鼠标**悬停其上**的E元素
E:focus | 匹配**获得当前焦点**的E元素
E:lang(c) | 匹配**lang属性等于c**的E元素
E:enabled | 匹配**表单中可用**的元素
E:disabled | 匹配**表单中禁用**的元素
E:checked | 匹配表单中**被选中的radio或checkbox**元素
E::selection | 匹配用户**当前选中**的元素
E:nth-child(n) | 匹配其**父元素下**的的第n个子元素，第一个编号为1
E:nth-last-child(n) | 匹配其**父元素下**的倒数的第n个子元素,第一个编号为1
E:nth-of-type(n) | 和nth_child(n)类似，但是仅匹配**同种标签**的元素
E:nth-last-of-type(n) | 和nth-last-of-child(n)类似，但是仅匹配**同种标签**的元素
E:first-of-type | 匹配**父元素下**使用同种标签的第一个子元素，等同于nth-of-type(1)
E:last-of-type | 匹配**父元素下**使用同种标签的最后一个子元素，等同于nth-last-of-type(1)
E:root | 匹配文档的根元素，对于HTML文档，就是HTML元素

选择器使用举例分析(first-child)：
```
<style type="text/css">
    .child:first-child{
        color: red;
    }
</style>

<div class="warp">
    <h1 class="child">h1</h1>
    <h2 class="child">h2</h2>
    <div class="ct">
        <h1 class="child">ct-h1</h1>
    </div>
</div>
```
在上述代码中，.warp的div中包含了.child的h1和h2，以及.ct的div，.ct的div中有.child的ct-h1。
当给当前结构加上上述样式的时候, .child的元素有三个，前2个为h1和h2，他们的父元素是.warp的div，所以父元素下的第一个子元素为.child的h1,然后第三个.child的ct-h1的父元素为.ct的div，.ct的div的第一个子元素是.child的ct-h1,所以，在本例中，**.child的h1和.child的ct-h1会应用样式**。
first-of-type举例：
```
<style type="text/css">
    .child:first-of-type{
        color: red;
    }
</style>

<div class="warp">
    <h1 class="child">h1</h1>
    <h2 class="child">h2</h2>
    <div class="ct">
        <h1 class="child">ct-h1</h1>
    </div>
    <h2 class="child">h2-btm</h2>
</div>
```
上述代码中，应用first-of-type选择器的时候，.child的元素有h1,h2,ct-h1,h2-btm四个元素。h1,h2，h2-btm的父元素为.warp的div，其中h1和h2都是父元素下的同种标签的第一个元素，所以会被选中，而后面的h2-btm因为前面已经有h2了，所以不是第一个h2标签而是第二个h2标签，故此不会被选中。而ct-h1的父元素是ct， ct-h1是父元素下的第一个同类标签，故此也会被选中。

nth-child(n)和nth-of-type(n)当n=1时，和first-child，first-of-type相同。
对于n的取值：
- 1,2,3,4,5
- 2n+1(奇数),2n(偶数),4n-1
- odd(奇数),even(偶数)

5. 伪元素选择器

选择器 | 含义
-- | -- 
E::first-line | 匹配E元素内容的第一行
E::first-letter | 匹配E元素内容的第一个字母
E::before | 在E元素之前插入生成的内容
E::after | 在E元素之后插入生成的内容

before和after使用的时候必须要有**content属性**。
应用举例：
```
<style type="text/css">
    .warp::before{
        content: 'before';
    }
</style>

<div class="warp">
    <h1 class="child">h1</h1>
    <h2 class="child">h2</h2>
    <div class="ct">
        <h1 class="child">ct-h1</h1>
    </div>
    <h2 class="child">h2-btm</h2>
</div>
```

###三，选择器的优先级是怎样的?对于复杂场景如何计算优先级？

多种样式应用在同一元素上时：
```
<style type="text/css">
    #text{
        color: red;
    }
    p{
        color: blue;
    }
</style>

<p id="text">text</p>
```
- CSS优先级排序（从高到底）：
1. **！important**加到元素后，会覆盖掉页面中任何位置定义的元素样式
```
p{
    color: red!important;
}
```
2. 作为style属性写在元素标签上的**内联样式**
3. id选择器
4. 类选择器
5. 伪类选择器
6. 属性选择器
7. 标签选择器
8. 通配符选择器
9. 浏览器自定义

- 复杂情景下的优先级计算

我们给**行内样式**`<div style="xxx"></div>`设为==>a
**ID**选择器设为==>b
**类**，**属性**选择器和**伪类**选择器设为==>c
**标签**选择器，**伪元素**设为==>d
*选择器不计数


计算举例：
1. *    a=0 b=0 c=0 d=0  --->0,0,0,0
2. p    a=0 b=0 c=0 d=1 --->0,0,0,1
3. a:hover a=0 b=0 c=1 d=1(a算一个标签选择器所以d=1，hover算伪类，c=1)  ---> 0,0,1,1
4. ul li  a=0 b=0 c=0 d=2(2个标签选择器) ---> 0,0,0,2
5. ul ol+li  a=0 b=0 c=0 d=3(3个标签选择器)  --->0,0,0,3
6. h1+input[type=hidden]  a=0 b=0 c=1(type=hidden一个属性选择器) d=2(h1和input 2个标签选择器)  ---> 0,0,1,2
7. ul ol li:active a=0 b=0 c=1(active c=1) d=3(3个标签选择器) ---> 0,0,1,3
8. \#ct .box p   a=0 b=1(#ct) c=1(.box) d=1(p)  --->0,1,1,1
9. div#header:after   a=0 b=1(#header) c=0 d=2(divh和：after)  --->0,1,0,2
10. style=""   a=1 b=0 c=0 d=0 ----> 1,0,0,0

通过比较计算得到的值来确定优先级，先比较a的大小，a相等再比较b，b相等再比较c，以此类推。


- 样式覆盖
```
div{
    color: red;
}
div{
    color: blue;
}
```
后面的样式会覆盖前面的样式，只有后面的样式会起作用

- 选择器使用经验

1. 遵守CSS书写规范
2. 使用合理的命名空间，**不建议使用标签选择器**
3. 合理的复用class


###四，a:link, a:hover, a:active, a:visited 的顺序是怎样的？ 为什么？

- 标签伪类的作用：
":link": a标签还未被访问的状态；
":visited": a标签已被访问过的状态；
":hover": 鼠标悬停在a标签上的状态；
":active": a标签被鼠标按着时的状态；

- 写样式时候的顺序：
**LVHA**
1.a:link
2.a:visited
3.a:hover
4.a:active

- 这样写的原因：
这样写的原因是由两点决定的，第一点"相同优先级的样式，后面的会覆盖前面的样式"，第二点是鼠标点击动作的先后顺序。
link和visited是常态，而hover和active是即时状态，h和a触发时，即时状态会覆盖l和v的常态，所以即时状态要放在常态的后面。
常态情况下：访问过之后，就要呈现出visited的样式，所以visited要放到Link后面。
即时状态下：要让active覆盖hover的样式，所以要将active放到后面。

###五，以下选择器分别是什么意思?
```
#header{
}
.header{
}
.header .logo{
}
.header.mobile{
}
.header p, .header h3{
}
#header .nav>li{
}
#header a:hover{
}
#header .logo~p{
}
#header input[type="text"]{
}
```
`#header`   id为header的元素
`.header`  class为header的元素
`.header .log`  class为header的元素下的class为log的子元素
`.header.mobile`   同时具有header类的和mobile类的元素
`.header p, .header h3`   同时选中header类的元素下的p标签元素和h3标签元素
`#header .nav>li`  id为header的元素下的nav类的元素下的直接标签为li的子元素
`#header a:hover`   表示id为header的元素下的标签为a的元素鼠标悬浮下的元素
`#header .logo~p`   表示id为header的元素下的类为logo的元素之后的所有同级的标签为p的元素
`#header input[type="text"] `  表示id为header的元素下的拥有值为text的type属性的input标签

###六，列出你知道的伪类选择器

选择器 | 含义
-- | --
E:firsr-child | 匹配元素E的**第一个**子元素
E:link | 匹配所有**未被点击**过的链接
E:visited | 匹配所有**已被点击**的链接
E:active | 匹配鼠标**已经其上按下，还没有释放**的E元素
E:hover | 匹配鼠标**悬停其上**的E元素
E:focus | 匹配**获得当前焦点**的E元素
E:lang(c) | 匹配**lang属性等于c**的E元素
E:enabled | 匹配**表单中可用**的元素
E:disabled | 匹配**表单中禁用**的元素
E:checked | 匹配表单中**被选中的radio或checkbox**元素
E::selection | 匹配用户**当前选中**的元素
E:nth-child(n) | 匹配其**父元素下**的的第n个子元素，第一个编号为1
E:nth-last-child(n) | 匹配其**父元素下**的倒数的第n个子元素,第一个编号为1
E:nth-of-type(n) | 和nth_child(n)类似，但是仅匹配**同种标签**的元素
E:nth-last-of-type(n) | 和nth-last-of-child(n)类似，但是仅匹配**同种标签**的元素
E:first-of-type | 匹配**父元素下**使用同种标签的第一个子元素，等同于nth-of-type(1)
E:last-of-type | 匹配**父元素下**使用同种标签的最后一个子元素，等同于nth-last-of-type(1)
E:root | 匹配文档的根元素，对于HTML文档，就是HTML元素


###七，div:first-child、div:first-of-type、div :first-child和div :first-of-type的作用和区别 （注意空格的作用）

`div:first-child`表示div同级的第一个而且是div元素
`div:first-of-type` 表示div元素的父元素下的第一个标签为div的子元素
`div :first-child` 表示div元素下的作为其父元素下的所有第一个子元素
`div :first-of-type` 表示div元素下的作为其父元素下的所有标签和其父元素相同的子元素

###八，运行如下代码，解析下输出样式的原因。
```
<style>
.item1:first-child{
  color: red;
}
.item1:first-of-type{
  background: blue;
}
</style>
 <div class="ct">
   <p class="item1">aa</p>
   <h3 class="item1">bb</h3>
   <h3 class="item1">ccc</h3>
 </div>
```

运行结果为aa应用了`color: red`的样式，aa和bb应用了`background: blue`的样式。

结果分析：
- first-child 
.item1的元素有aa，bb和ccc，他们三个元素的父元素都为.ct的div，.ct的div下的第一个元素为.item1的aa，所以只有aa应用了该样式。
- first-of-type
.item1的元素有标签为p的aa，标签为h3的bb和ccc。对于p标签的aa，其父元素.ct下的第一个标签为p的元素就是aa,对于标签为h3的bb和ccc，其父元素.ct下的第一个标签为h3的为bb，所以只有aa和bb应用了样式。
