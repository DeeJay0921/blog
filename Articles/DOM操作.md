---
title: DOM操作
date: 2017/08/17 00:00:01
cover: http://brainoteka.com/Content/uploads/users/4/20151120193818_js-0-DOM.jpg
tags: 
- 前端
- DOM
categories: 
- 前端
---
DOM操作
<!--more-->


##Document Object Model 文档对象模型
DOM是HTML和XML文档的编程接口。将web页面和脚本或编程语言连接起来了。
要改变页面的某个东西，JS就要获得对HTML文档中所有元素进行访问的入口。都是通过DOM来获得的。
###document对象
每个载入浏览器的HTML文档都会成为document对象。包括了文档的基本信息，可以通过JS对HTML页面中的所有元素进行访问修改。

####document对象的常用属性
- doctype
```
document.doctype; //<!DOCTYPE html>
document.doctype.name; //"html"
```
- body，head
```
document.head;
document.body;
```
可以通过这俩属性获取文档的head节点和body节点
- activeElement    返回当前文档中获得焦点的那个元素
- documentURL , domain , lastModified
1,documentURL属性返回当前文档的网址
2，domain属性返回文档域名
3，lastModified返回当前文档的最后修改时间
- location
location属性返回一个只读对象，提供了当前文档的URL信息
- title,  characterSet
1,title属性返回当前文档的标题，该属性可写
2，characterSet属性返回渲染当前文档的字符集

- readyState
readyState属性返回当前文档的状态，有三个值
1,loading 加载html代码阶段，尚未完成解析
2，interactive 加载外部资源阶段
3，complete 

- compatMode
compatMode属性返回浏览器处理文档的模式，
1，BackCompat: 向后兼容模式，即没有添加DOCTYPE
2，CSS1Compat  严格模式，添加了DOCTYPE
- cookie
- innerText
innerText是一个可写属性，返回元素内包含的文本内容，在多层次的时候会按照元素由浅到深的顺序拼接其内容
```
<div>
    <p>
        123
        <span>456</span>
    </p>
</div>
```
外层div的innerText的返回内容是"123456"
- innerHTML , outerHTML
innerHTML属性作用和innerText属性类似，但是不是返回元素的文本内容，而是返回元素的HTML结构，在写入的时候也会自动构建DOM
```
<div>
    <p>
        123
        <span>456</span>
    </p>
</div>
```
外层div的innerHTML的返回内容是`<p>123<span>456</span>></p>`
outerHTML返回内容还包括本身

####document对象的常用方法
- open()  ,close()
document.open方法用于新建一个文档，供write方法写入内容。实际上等于清除当前文档，重新写入内容
document.close()方法用于关闭open方法所新建的文档，一旦关闭，write方法就无法写入内容
- write()
docuement.write()方法用于向当前文档写入内容，只要当前文档还没有用close方法关闭，它所写入的内容就会追加在已有内容的后面。
```
    document.open();
    document.write('hello');
    document.write('deejay');
    document.close();
```
**一般不建议使用document.write()**

## Element对象

除了document对象，最常用的就是Element对象了，Element对象表示HTML元素。
Elment有几个重要属性：
- nodeName: 元素标签名 还有个类似的tagName
- nodeType:  元素类型
- className： 类名
- id： 元素id
- children：子元素列表（HTMLCollection）
- childNodes： 子元素列表（NodeList）
- firstChild 第一个子元素
- lastChild 最后一个子元素
- nextSibling: 下一个兄弟元素
- previousSibling 上一个兄弟元素
- parentNode,parentElement ：父元素

##查询元素

- getElementById()

- getElementsByClassName()

- getElementsByTagName()

- **querySelector()**
querySelector()方法返回匹配指定的**CSS选择器**的元素节点。**如果有多个节点满足匹配条件，则返回第一个匹配的节点**。如果没有发现匹配的节点，则返回null。使用举例：
```
<div id="ct">
    <a href="#">my link</a>
    <h1 class="title">my title</h1>
    <p class="title"></p>
</div>
<a href="#">deejay</a>
<script>
    document.querySelector('#ct a'); //选中了#ct下面的a标签，
    document.querySelector('#ct .title'); //选中了#ct下的第一个.title元素，即只选中了h1标签
    //  querySelector()仅仅只能选中一个元素
```
- **querySelectorAll()**
针对querySelector()只能选择一个元素的情况，querySelectorAll()可以选中多个元素。得到一个类数组对象。
- elementFromPoint()
返回位于页面指定位置的元素

##创建元素

- createElement()
用来生成HTML元素节点，参数为元素的标签名，如"div"，不能带<>或者null，会报错
`var newDiv = document.createElement("div");`
- createTextNode()
生成文本节点，参数为要生成的文本节点的内容
- createDocumentFragment()
生成一个DocumentFragment对象，即一个存在于内存中的DOM片段，但是不属于当前文档，常常用来生成较复杂的DOM结构，然后插入当前文档。 优点是不属于当前文档，所以不需要重新渲染，比起直接修改，性能更好。举例说明：
```
    var arr = [1,2,3,4,5,6,7,8]; //假设有这么一个数组，要把每一项都插入到页面当中去，那么页面每插入一次就要重绘一次，影响性能，所以使用documentfragment
    var docFragment = document.createDocumentFragment(); 
    // 先创建一个documentFragment对象，再把数组遍历，每一项都加入到一个另外的DOM对象中，再把这个DOM对象添加到documentFragment对象中，再将documentFragment对象添加到页面的目标元素中
    for (var i = 0; i < arr.length; i ++) {
        var node = document.createElement('div');
        node.innerText = arr[i];
        docFragment.appendChild(node);
    }
    target.append(docFragment);
```
## 修改元素
- **appendChild()**
把一个DOM对象添加到他的父容器上，在元素末尾
```
    var newDiv = document.createElement('div');
    var newText = document.createTextNode('hello deejay');
    newDiv.appendChild(newText);
```
- insertBefore()
在某个元素之前插入元素
- replaceChild()
接受两个参数，要插入的元素和要替换的元素

##删除元素

**删除元素用`removeChild()`方法即可**，举例：
`parentNode.removeChild(childNode);`

##clone元素
cloneNIode()方法用于克隆元素，方法有一个布尔值的参数，传入true的时候会深复制，会复制元素及其子元素（IE还会复制其事件），false的时候只会复制元素本身
`node.cloneNode(true);`

##属性操作
- **getAttribute()**
用于获取元素的attribute值
`node.getAttribute('id');`
- createAttribute()
生成一个新的属性对象节点，并且返回它
`attribute = document.createAttribute(name);`
createAttribute的方法的参数name，是属性的名称
- **setAttribute()**
用于设置元素属性
```
    var node = document.getElementById('div1');
    node.setAttribute('myAttribute','newValue'); // 设置了myAttribute属性的值为newValue
```
- removeAttribute()
用于删除属性：
`    node.removeAttribute('id');`

## classList
**element.classList使用举例：**
```
    <p class="title"></p>
    var p = document.querySelector('.title');
    console.log(p.classList); //["title"] 输出一个数组，内容为元素的class
    p.classList.add('active'); // 给p元素增加一个叫active的类
    p.classList.remove('active'); //删除active类
    p.classList.contains('active'); //判断p元素是否有active这个类
```
## 一般常用到的DOM操作方法：
```
    var ct = document.querySelector('#ct');
    document.querySelectorAll("#ct .title");
    var img = document.createElement('img');
    img.setAttribute('src','http://wx1.sinaimg.cn/mw690/4abc2d1fgy1fim303kdwdj208m08cgly.jpg');
    ct.appendChild(img);
    img.getAttribute('src');
    img.removeAttribute('src');
    
    ct.innerHTML = '<img src="http://upload-images.jianshu.io/upload_images/7113407-c6bee78dff1ac501.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240">
';
    ct.innerText = 'hello';
```
