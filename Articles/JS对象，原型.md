---
title: JS对象，原型
date: 2017/08/31 00:00:01
cover: https://www.scriptonitejs.com/wp-content/uploads/2016/01/proto.jpg
tags: 
- 前端
- JS
- 原型
- prototype
categories: 
- 前端
---
JS对象，原型
<!--more-->

##面向对象
是一种思维方式
三大特点：继承，多态，封装
几个概念: 类，对象，属性，方法，成员，类成员，实例成员，静态类，抽象类
##创建对象的几种方式：
###1，工厂模式
```
    function createObj (name,age) {
        var obj = {
            name: name,
            age: age,
            printName: function () {
                console.log(this.name);
            }
        }
        return obj;
    }
    var obj = createObj('zhangsan',20);
    obj.printName();
//    解决了构造过程复杂，需要了解对象细节的问题，但是构造出来的对象类型都是Object，没有识别度。
```
解决了构造过程复杂，需要了解对象细节的问题，但是构造出来的对象类型都是Object，没有识别度。

###2,构造函数创建实例
####关于function的一些知识：
- function作为构造函数（通过new操作符调用）的时候会返回一个类型为function的name的对象
- function可以接受参数，可以根据参数来创建相同类型不同值的对象
- function实例作用域内有一个constructor属性，这个属性就可以指示其构造器
```
//      构造函数创建实例
    function People (name,age) {
        this.name = name;
        this.age = age;
        this.sayName = function () {
            console.log(this.name);
        }
    }
    var p1 = new People('deejay',21); // new People表示将People函数作为构造函数来创建对象
    p1.sayName();
//构造函数在解决了上面的问题，同时为实例带来了类型，但可以注意到每个实例sayName方法实际上作用一样，但是每个实例要重复一遍，大量对象存在的时候是浪费内存
```
####关于new操作符：
new运算符接受一个函数F及其参数： new F(arguments)，这一过程分为3步：
- 1.创建类的实例。这步是把一个空的对象的proto属性设为F.prototype   p1 = {};
- 2.初始化实例。函数F被传入参数并调用，关键字this被设定为该实例  p1.name = 'deejay' p1.sayName=...  此时的this为这个p1实例
- 3. 返回实例   return p1
构造函数内部一般不写return，默认return这个实例，但是如果写了return并且return的不是简单类型的话，就会真的return这个复杂类型
####关于instanceof 操作符
instanceof 是一个操作符，可以判断对象是否为某个类型的实例
    p1 instanceof  People; // true
    p1 instanceof  Object; // true
instanceof判断的是**对象**
    1 instanceof  Number; // false

构造函数在解决了上面的问题，同时为实例带来了类型，但可以注意到每个实例sayName方法实际上作用一样，但是每个实例要重复一遍，大量对象存在的时候是浪费内存。
###构造函数
- 任何函数使用new表达式就是构造函数
- 每个**函数**都自动添加一个名称为prototype属性，这是一个对象
- 每个**对象**都有一个内部属性_proto_(规范中没有指定这个名称，但是浏览器都是这么实现的)指向其类型的prototype属性，类的实例也是对象，其_proto_属性指向**类**的prototype

- prototype图解：
![prototype](http://upload-images.jianshu.io/upload_images/7113407-83dd4fddab077144.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

对于实例中的属性，如果存在则输出，不存在就会向构造函数的prototype中寻找。所以对于刚才的问题，可以这样解决：
- 所有实例都会通过原型链引用到类型的prototype
- prototype相当于特定类型所有实例都可以访问到的一个**公共容器**
- 重复的东西移动到公共容器里放一份就好了

所以对于刚才的构造函数，可以这么写：
```
    function People (name,age) {
        this.name = name;
        this.age = age;
        People.prototype.sayName = function () {
            console.log(this.name);
        }
    }
    var p1 = new People('deejay',21);
    p1.sayName();
```
这个时候的对应关系是这样的：

![](http://upload-images.jianshu.io/upload_images/7113407-9063a4ebdf3b2191.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
