---
title: JS对象，原型-相关问题
date: 2017/09/02 00:00:01
tags: 
- 前端
- JS
- 原型
- prototype
categories: 
- 前端
---
JS对象，原型-相关问题
<!--more-->

###1， OOP 指什么？有哪些特性

OOP
是Object Oriented Programming 缩写，面向对象编程，
是目前主流的编程范式。

是一种思维方式
三大特点：继承，多态，封装
几个概念: 类，对象，属性，方法，成员，类成员，实例成员，静态类，抽象类

###2，如何通过构造函数的方式创建一个拥有属性和方法的对象? 

```
    function Person(name,age) {
        this.name = name;
        this.age = age;
        this.sayName = function () {
            console.log(this.name);
        }
    }
    var p1 = new Person('deejay',21);
    p1.sayName();
```
###3， prototype 是什么？有什么特性 
- 每个函数都是一个Function对象，Function对象都有一个prototype对象。prototype表示的是函数的原型。当这个函数作为构造函数创建出实例对象时，实例的\_\_proto\_\_属性就等于这个函数的prototype

- prototype相当于是一个公共容器，供所有实例访问，节省空间。当实例对象本身没有某个属性或方法的时候，它会到构造函数的 prototype 属性 指向的对象，去寻找该属性或方法。如果实例对象自身就有某个属性或方法，它就不会再去原型对象寻找这个属性或方法。


###4，画出如下代码的原型图
```
function People (name){
  this.name = name;
  this.sayName = function(){
    console.log('my name is:' + this.name);
  }
}

People.prototype.walk = function(){
  console.log(this.name + ' is walking');  
}

var p1 = new People('deejay');
var p2 = new People('前端');
```


![1.png](http://upload-images.jianshu.io/upload_images/7113407-846cc0ae2900e069.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


###5,创建一个 Car 对象，拥有属性name、color、status；拥有方法run，stop，getStatus 
```
    function ClassCar(name,color,status) {
        this.name = name;
        this.color = color;
        this.status = status;
        this.run = function () {};
        this.stop = function () {};
        this.getStatus = function () {};
    }
    var Car = new ClassCar('name','color','status');
```
###6,创建一个 GoTop 对象，当 new 一个 GotTop 对象则会在页面上创建一个回到顶部的元素，点击页面滚动到顶部。拥有以下属性和方法
```
1. `ct`属性，GoTop 对应的 DOM 元素的容器
2.  `target`属性， GoTop 对应的 DOM 元素
3.  `bindEvent` 方法， 用于绑定事件
4 `createNode` 方法， 用于在容器内创建节点
```

[代码](http://js.jirengu.com/vokebiyasu/1/edit)

###7, 使用木桶布局
http://js.jirengu.com/cibicizape/1
