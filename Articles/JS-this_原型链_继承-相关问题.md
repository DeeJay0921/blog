---
title: JS-this_原型链_继承-相关问题
date: 2017/09/02 00:00:01
tags: 
- 前端
- JS
- 原型
- prototype
- this
categories: 
- 前端
---
JS-this_原型链_继承-相关问题
<!--more-->

##this
###1,apply、call 、bind有什么作用，什么区别
- apply/call:调用一个函数，传入函数执行上下文及参数
```
    fn.call(context,param1,param2...);
    fn.apply(context,paramArray;
```
语法很简单，第一个参数都是希望设置的this对象，不同之处在于call方法接收参数列表，而apply接收参数数组
```
    var value = 100;
    var obj4 = {
        value: 200
    }
    function fn4(a,b) {
        console.log(this.value + a + b);
    }
    fn4(3,4); // 107
    fn4.call(obj4,3,4);//207
    fn4.apply(obj4,[3,4]); //207
```
- bind:返回一个函数，并且使函数内部的this为传入的第一个参数
```
    var obj1 = {
        name: 'deejay',
        age: 20,
        fn: function () {
            console.log(this); 
        }
    };
    var obj3 = {
        a: 3,
    }
    var fn3 = obj1.fn.bind(obj3);
    fn3(); //obj3
```
apply/call是指的调用一个函数，传入这个函数的执行上下文和参数。而bind是指的是返回一个函数，并且使函数内部的this为传入的第一个参数。
call/apply在函数调用时使用，bind在函数声明时使用。 bind() 方法会返回执行上下文被改变的函数而不会立即执行，而call/apply是直接执行该函数。

###2,以下代码输出什么?
```
    var john = {
        firstName: "John"
    }
    function func() {
        alert(this.firstName + ": hi!")
    }
    john.sayHi = func
    john.sayHi()//John: hi!
```

###3,以下代码输出什么?

```
    func()
    function func() {
        alert(this) //window
    }
```
可以看做window.func();
###4，下面代码输出什么

```
    document.addEventListener('click', function(e){
        console.log(this); //#document
        setTimeout(function(){
            console.log(this);//window
        }, 200);
    }, false);
```
setTimeoutl这个方法执行的函数this是全局对象,
在事件处理程序中this代表事件源DOM对象
###5，下面代码输出什么
```
    var john = {
        firstName: "John"
    }

    function func() {
        alert( this.firstName )
    }
    func.call(john);//John
```
使用了call方法，调用了func(),并且将内部this修改为john
###6，以下代码有什么问题，如何修改

```
var module= {
  bind: function(){
    $btn.on('click', function(){
      console.log(this) //this指什么
      this.showMsg();
    })
  },
  
  showMsg: function(){
    console.log('deejay');
  }
}
```
问题出在了在事件处理程序中this代表事件源DOM对象，所以点击的时候的this是当前点击的DOM元素，没有showMsg这个方法，解决方法如下：
```
    var module= {
        bind: function(){
            var self = this;
            console.log(self);
            $btn.on('click', function(){
                console.log(this); //this指的是当前触发点击事件的DOM元素
                self.showMsg()
            })
        },

        showMsg: function(){
            console.log('deejay');
        }
    }
```
这里设置一个self保存有showMsg()方法的this，即module，然后调用showMsg
##原型链
###7，有如下代码，解释Person、 prototype、__proto__、p、constructor之间的关联。
```
    function Person(name){
        this.name = name;
    }
    Person.prototype.sayName = function(){
        console.log('My name is :' + this.name);
    }
    var p = new Person("deejay")
    p.sayName();//My name is :deejay
```
本例中，Person作为构造函数，创建了它的实例p,实例p存在一个属性\_\_proto\_\_，等于Person.prototype,而Person中存在一个Person.prototype对象，其有一个属性constructor，这个属性又指向Person。
即有：
```
    p.__proto__ === Person.prototype;
    Person.prototype.constructor === Person;
```

###8，上例中，对对象 p可以这样调用 p.toString()。toString是哪里来的? 画出原型图?并解释什么是原型链。

![1.png](http://upload-images.jianshu.io/upload_images/7113407-312e708a999fa825.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

原型图如图所示，当调用p的toString()时，会先在实例中寻找，如果找不到，再到p.\_\_proto\_\_属性中，即Person.prototype中寻找，当在Person.prototype中寻找不到时，会到Person.prototype.\_\_proto\_\_中也就是Object.prototype中寻找，然后就寻找到了toString()。
所以整个过程为p 到 p.\_\_proto\_\_  再到 p.\_\_proto\_\_.\_\_proto\_\_中，逐级向上查找即为原型链。

###9，对String做扩展，实现如下方式获取字符串中频率最高的字符
```
var str = 'ahbbccdeddddfg';
var ch = str.getMostOften();
console.log(ch); //d , 因为d 出现了5次
```

给str的构造函数String的prototype添加函数
```
    String.prototype.getMostOften = function () {
//等价于 str.__proto__.getMostOften = ...
        var dict = {};
        var count;
        var maxvalue = 0;
        for (var i = 0; i < this.length; i ++) {
            if (dict[this[i]]) {
                dict[this[i]]++;
            }
            else {
                dict[this[i]] = 1;
            }
        }
        for (var key in dict) {
            if (dict[key] > maxvalue) {
                maxvalue = dict[key];
                count = key;
            }
        }
        return count;
    }
    var str = 'ahbbccdeddddfg';
    var ch = str.getMostOften();
    console.log(ch); //d , 因为d 出现了5次
```
###10， instanceOf有什么作用？内部逻辑是如何实现的？
instanceOf用来判断某个对象是否是某函数的实例

主要实现逻辑为，先判断p.\_\_proto\_\_  是否为Person.prototype，如果不是，接着判断p.\_\_proto\_\_ .\_\_proto\_\_ 是否为Person.prototype,...直到为null返回false，如果在查找途中发现是Person.prototype的实例，返回true
##继承
###11，继承有什么作用?

继承是指一个对象直接使用另一对象的属性和方法。

如果要创建一个子类，不用重新复制一遍父类的代码，可以直接继承然后进行添加子类自己的属性和方法
###12，下面两种写法有什么区别?
```
//方法1
function People(name, sex){
    this.name = name;
    this.sex = sex;
    this.printName = function(){
        console.log(this.name);
    }
}
var p1 = new People('前端', 2)

//方法2
function Person(name, sex){
    this.name = name;
    this.sex = sex;
}

Person.prototype.printName = function(){
    console.log(this.name);
}
var p1 = new Person('deejay', 21);
```
方法1是在每次创建一个实例的时候都会相应的给实例也创建一个printName()，如果创建了多个实例的话每个实例都会有一个相同的printName()。
方法2是在构造函数的prototype中添加一个printName()，作为所有实例的公共方法，这样多个所有被创建的实例自身只有自身的属性，而printName()是公用的，只有一个，节省了内存。

###13， Object.create 有什么作用？兼容性如何？
Object.create() 方法会使用指定的原型对象及其属性去创建一个新的对象。
Object.create是ES5方法

###14， hasOwnProperty有什么作用？ 如何使用？

是Object.prototype的一个方法，可以判断一个对象是否包含自定义属性而不是原型链上的属性，hasOwnProperty是JS中唯一一个处理属性但是不查找原型链的函数
```
    function Person(name,age) {
        this.name = name;;
        this.age = age;
    }
    Person.prototype.sayName = function () {
        console.log(this.name);
    }
    var p1 = new Person('deejay',21);
    p1.hasOwnProperty('name');//true
    p1.hasOwnProperty('age');//true
    p1.hasOwnProperty('sayName');//false
```
###15，如下代码中call的作用是什么?
```
function Person(name, sex){
    this.name = name;
    this.sex = sex;
}
function Male(name, sex, age){
    Person.call(this, name, sex);    //这里的 call 有什么作用
    this.age = age;
}
```
这里的call即为在Male构造函数内部调用Person构造函数，将Male创建的实例this传给Person，达到的效果就是**复制了Person构造函数中的所有属性**
###16，补全代码，实现继承 
```
function Person(name, sex){
    // todo ...
}

Person.prototype.getName = function(){
    // todo ...
};    

function Male(name, sex, age){
   //todo ...
}

//todo ...
Male.prototype.getAge = function(){
    //todo ...
};

var deejay= new Male('deejay', '男', 21);
deejay.printName();
```

实现如下：
```
    function Person(name, sex){
        this.name = name;
        this.sex = sex;
    }

    Person.prototype.printName = function(){
        console.log(this.name)
    };

    function Male(name, sex, age){
        Person.call(this,name,sex); // 复制Person内部的属性
        this.age = age;
    }
    Male.prototype = Object.create(Person.prototype); // 实现方法的继承
    Male.prototype.getAge = function(){
        console.log(this.age)
    };

    var deejay = new Male('deejay', '男', 21);
    deejay.printName();//deejay
```
