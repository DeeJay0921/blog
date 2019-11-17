---
title: JS原型链
date: 2017/09/01 00:00:01
cover: http://seanamarasinghe.com/wp-content/uploads/2015/08/javascript-prototype-1050x360.jpg
tags: 
- 前端
- JS
- 原型
- prototype
categories: 
- 前端
---
JS原型链
<!--more-->

[JS原型链](https://zhuanlan.zhihu.com/p/23090041?refer=study-fe)

回顾一下类，实例，prototype，\_\_proto\_\_的关系
```
    function People (name,age) {
        this.name = name;
        this.age = age;
    }
    People.prototype = {
        sayName: function () {
          console.log(this.name);
        },
    }
    var p1 = new People('deejay',21);
    p1.sayName();
```

![类，实例，prototype,__proto__](http://upload-images.jianshu.io/upload_images/7113407-65864d00d3935d40.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

另外有：
```
    p1.__proto__.constructor === People; // true
    People.prototype.constructor === People; //true
    p1.constructor === People; //true
```
对于p1.construcror来说，在p1中找不到相应的属性，就回到\_\_proto\_\_中寻找，其实找到的是People.prototype中的constructor，和第2行等价

**任何一个对象，只要是一个对象，那么总有一个函数创建了它，这个对象的\_\_proto\_\_属性等于创建它的函数的prototype**
比如说People.prototype也是一个对象，也有\_\_proto\_\_属性,其中的constructor指向了Object(),即是Object()创建的对象，其prototype属性等于People.prototype.\_\_proto\_\_.

###详细的原型链

![原型链](http://upload-images.jianshu.io/upload_images/7113407-b570d50884afcdcb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在上图中有几个需要注意的特殊点：
- Object.prototype.\_\_proto\_\_为null，其实就是不存在
- Function.prototype也是一个对象，即也是Object()创建的
- 对于Function自身来说，也是一个对象，也有\_\_proto\_\_属性，Function.\_\_proto\_\_.constructor === Function,即Function创建了所有的函数，包括他自己。
- 对于Object.\_\_proto\_\_.constructor === Function,还是那句话，Function创建了所有的函数，包括他自己。
