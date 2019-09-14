---
title: JS继承
date: 2017/09/01 00:00:01
tags: 
- 前端
- JS
- 原型
- prototype
- JS继承
categories: 
- 前端
---
继承是指一个对象直接使用另一对象的属性和方法。
<!--more-->

继承是指一个对象直接使用另一对象的属性和方法。

JS并不提供原生的继承机制，我们自己实现的方法很多，介绍一种最为通用的。
通过上面定义我们可以看出我们如果实现了两点的话就可以说我们实现了继承
- 1，得到一个类的属性
- 2，得到一个类的方法
举例讨论一下，先定义2个类，Person和Male
```
    function Person(name,sex) {
        this.name = name;
        this.sex = sex;
    }
    Person.prototype.sayName = function () {
        console.log(this.name);
    }
    function Male(age) {
        this.age = age;
    }
    Male.prototype.saySex = function () {
        console.log(this.sex);
    }
```
我们想定义一个新的类male，他有person的属性，所以不需要再将person的属性重新再复制一遍，怎么能让male获取到person的属性和方法就是实现继承的关键。
###属性获取
对象属性的获取是通过构造函数的执行，我们在**一个类中执行另一个类的构造函数，就可以把属性赋值到自己内部，但是我们需要把环境改到自己的作用域内（使用call）**，这就要借助我们讲过的函数call了。
可以这样改写Male
```
    function Male(name,age,sex) {
        Person.call(this,name,age);//在Male内部执行Person构造函数，就能获得属性。
      // 也可以写成 Person.bind(this)(name,age);
        this.sex = sex;
    }
    Male.prototype.saySex = function () {
        console.log(this.sex);
    }
    Male.prototype.sayName = function () {
        console.log(this.name);
    }
    var male1 = new Male('deejay',21,'male');
    male1.sayName();
```
###方法获取
上面的方法仅仅获取了属性，但是方法是我们自己写的，并没有得到Person的方法，那我们获取方法的方式如下：

我们都知道类的方法都定义在了prototype里，所以我们只要把子类的prototype改为父类的prototype的备份就好了

`Male.prototype = Object.create(Person.prototype);`
等价于
`Male.prototype.__proto__ = Person.prototype`

Object.create() 方法会使用指定的原型对象及其属性去创建一个新的对象。

这里我们通过Object.create直接clone了一个新的prototype而不是直接把Person.prototype直接赋值，因为引用关系，这样会导致后续修改子类的prototype也修改了父类的prototype，因为修改的是一个值

另外Object.create是ES5方法，之前版本通过遍历属性也可以实现浅拷贝

这样做需要注意一点就是对子类添加方法，必须在修改其prototype之后，如果在之前会被覆盖掉
```
    Male.prototype.saySex = function () {
        console.log(this.sex);
    }
    Male.prototype = Object.create(Person.prototype);
```
这样写的话，saySex方法在赋值之后就没了，因此得这么写。
```
    function Male(name,age,sex) {
        Person.call(this,name,age);
        this.sex = sex;
    }
    Male.prototype = Object.create(Person.prototype);
    Male.prototype.saySex = function () {
        console.log(this.sex);
    }
```
这么写基本上没问题了，但是prototype对象存在一个constuctor属性指向其类型，因为我们复制的父元素的prototype，这时候constructor属性指向是不对的，导致我们判断类型出错。

`Male.prototype.constructor; //Person` 

因此我们需要再重新指定一下constructor属性到自己的类型
`Male.prototype.constructor = Male`
注意不能写成
`Male.prototype.__proto__.constructor = Male`这样其实是在修改Person的prototype中的constructor

##最终方案
我们可以包装一下代码，实现刚才所说的内容
```
    function inherit(superType,subType) {
        var _prototype = Object.create(superType.prototype);
        _prototype.constructor = subType;
        subType.prototype = _prototype;
    }
```
不使用Object.create()实现继承的方式
```
    function fn() {}//创建一个空函数
    fn.prototype = Person.prototype;
    Male.prototype = new fn();
```

####使用方式
```
    function Person(name,age) {
        this.name = name;
        this.age = age;
    }
    Person.prototype.sayName = function () {
        console.log(this.name);
    }
    function Male(name,age,sex) {
        Person.call(this,name,age);
        this.sex = sex;
    }
    inherit(Person,Male);
//然后在继承函数后面写自己的方法，否则会被覆盖
```
特别需要注意，**一定要在在继承函数后面写自己的方法，否则子类自己定义的方法会被覆盖**。

####hasOwnProperty
是Object.prototype的一个方法，可以判断一个对象是否包含自定义属性而不是原型链上的属性，**hasOwnProperty是JS中唯一一个处理属性但是不查找原型链的函数**
