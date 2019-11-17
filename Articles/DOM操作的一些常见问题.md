---
title: DOM操作的一些常见问题
date: 2017/08/17 00:00:01
cover: http://brainoteka.com/Content/uploads/users/4/20151120193818_js-0-DOM.jpg
tags: 
- 前端
- DOM
categories: 
- 前端
---
DOM操作的一些常见问题
<!--more-->

###1,dom对象的innerText和innerHTML有什么区别？

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
- innerHTML 
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

###2,elem.children和elem.childNodes的区别？

- elem.childNodes返回所有的子节点，包括HTML元素，属性，文本 
- elem.children只返回HTML元素节点
###3,查询元素有几种常见的方法？ES5的元素选择方法是什么?
- getElementById() 
- getElementByClassName() 
- getElementsByTagName() 
- getElementsByName()

ES5:
- querySelector() 
- querySelectorAll()
###4,如何创建一个元素？如何给元素设置属性？如何删除属性

```
    var ct = document.querySelector('#ct');
    document.querySelectorAll("#ct .title");
    var img = document.createElement('img');
    img.setAttribute('src','http://wx1.sinaimg.cn/mw690/4abc2d1fgy1fim303kdwdj208m08cgly.jpg');
    ct.appendChild(img);
    img.getAttribute('src');
    img.removeAttribute('src');
```

###5,如何给页面元素添加子元素？如何删除页面元素下的子元素?

```
    var ct = document.querySelector('#ct');
    var img = document.createElement('img');
    var p = document.createElement('p');
    ct.appendChild(img);
    ct.appendChild(p);
    ct.removeChild(img);
    ct.removeChild(p);
```

###6,element.classList有哪些方法？如何判断一个元素的 class 列表中是包含某个 class？如何添加一个class？如何删除一个class?

```
    <p class="title"></p>
    var p = document.querySelector('.title');
    console.log(p.classList); //["title"] 输出一个数组，内容为元素的class
    p.classList.add('active'); // 给p元素增加一个叫active的类
    p.classList.remove('active'); //删除active类
    p.classList.contains('active'); //判断p元素是否有active这个类
```



###7,如何选中如下代码所有的li元素？ 如何选中btn元素？
```
<div class="mod-tabs">
   <ul>
       <li>list1</li>
       <li>list2</li>
       <li>list3</li>
   </ul>
   <button class="btn">点我</button>
</div>
```

```
    var allLi = document.querySelectorAll(".mod-tabs ul li");
    var btn = document.querySelector('.btn');
```
