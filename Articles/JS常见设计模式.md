---
title: JS常见设计模式
date: 2017/09/11 00:00:01
cover: https://file.webstacks.cn/2018/05/2018051903520188.png
tags: 
- 前端
- JS
- 设计模式
categories: 
- 前端
---
JS常见设计模式
<!--more-->

##常见的一些设计模式

###构造函数模式(Constructor)
```
// 构造函数

function Person(name,age) {
    this.name = name;
    this.age = age;
}
Person.prototype.sayName = function () {
    console.log(this.name);
};

var p1 = new Person('deejay',21);
p1.sayName();
```
###工厂模式(factory)
工厂模式和构造函数模式每次都创建了一个新的引用（因为有return，每次return出来的对象引用都是不同的），创建出来的对象是完全不同的引用，解决了引用类型存在的问题
```
// 工厂模式

function createPerson(name,age) {
    var person = {
        name: name,
        age: age,
        sayName: function () {
          console.log(this.name); // 这里面的this指向person
        },
    }
    return person;
}
createPerson('deejay',21);

```
**关于this的指向，[this指向](http://www.jianshu.com/p/d10a17623d50)**

###单例模式(singleton)
创建了之后，就不会变了，只会指向同一个引用， 并不会创建一个新的对象
一般用于节约内存，不想new一个新的对象出来的时候可以用

```
// 单例模式

var People = (function () { // 匿名函数 也叫lambda函数，在js中是立即执行的
    var instance;
    function init(name) {
        return {
            name: name,
        }
    }
    return {
        createPeople: function (name) {
            if(!instance) {
                instance = init(name);
            }
            return instance;
        }
    }
})();
People.createPeople('deejay');// {name: 'deejay'}
People.createPeople('hello');// {name: 'deejay'}


// 函数都是有return的，如果不写return的是undefined，写了就是代码return里的东西。
//阅读代码的时候，一般先看函数的参数和函数的return
```
###混合模式(mixin)
一般都是去混合原型,需要继承的时候这么写
```
var Person = function (name,age) {
    this.name = name;
    this.age = age;
}
Person.prototype.sayName = function () {
    console.log(this.name);
}

var Student = function (name,age,score) {
    Person.call(this,name,age);
    this.score = score;
}
// Student.prototype = Object.create(Person.prototype);
Student.prototype = create(Person.prototype);
function create(parentPrototype) {
    function F() {}; //创建一个空的构造函数
    F.prototype = parentPrototype; //让这个空构造函数的原型和传入的目标原型一致
    return new F(); //return出F()的实例
//    最终达到的效果就是,让Student的原型等于Person的实例。
}
Student.prototype.sayScore = function () {
    console.log(this.score);
}
var s1 = new Student('deejay',21,80);
s1.sayName();
s1.sayScore(); 
```

###模块模式(module)
通过闭包来实现一个模块
```
// 模块模式

var Person = (function () {
    var name = 'deejay';
    function sayName() {
        console.log(name);
    }
    return {
        name: name,
        sayName: sayName,
    }
})();
console.log(Person)
```



###发布订阅模式(publish / subscribe)
一般用来处理异步
```
// 订阅发布模式

var EventCenter = (function () {
    //以EventCenter.on('deejay',function () {console.log('deejay');})为例来分析一些代码
    var events = {}; //用来存储所有的key/value
    function on(evt,handler) {
        events[evt] = events[evt] || [];
        //events['deejay']此时不存在，所以events['deejay'] = [];
        events[evt].push({
            handler: handler
        });
    //    events['deejay']push进去了一个处理程序
    // 此时有 events['deejay'] = [{
    //         handlder: function () {
    //             console.log('deejay');
    //         }
    //     }]
    }
    function fire(evt,args) {
        //当使用fire来启动deejay事件时
        if (!events[evt]) {//如果events['deejay']不存在，那么直接return掉
            return;
        }
        for(var i = 0; i < events[evt].length; i++) {
            //如果存在，进行遍历，便利出events['deejay']中的所有处理程序，并且执行
            events[evt][i].handler(args);
        }
    }
    return {
        on: on,
        fire: fire
    }
})();

EventCenter.on('deejay',function () {
    console.log('deejay');
}) //挂载一个事件deejay
EventCenter.fire('deejay');//使用fire触发事件，输出deejay
```

##使用发布订阅模式写一个事件管理器，可以实现如下方式调用
[pub/sub pattern](https://github.com/DeeJay0921/demos/blob/master/senior/task6.js)
