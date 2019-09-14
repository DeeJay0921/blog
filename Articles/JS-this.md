---
title: JS-this
date: 2017/09/01 00:00:01
tags: 
- 前端
- JS
- this
categories: 
- 前端
---
JS对象，原型
<!--more-->


[转换为call/apply来看待this的值](https://zhuanlan.zhihu.com/p/23804247)

[彻底搞懂this](https://zhuanlan.zhihu.com/p/25991271)

[阮一峰-this原理](http://www.ruanyifeng.com/blog/2018/06/javascript-this.html)


#this
由于运行期绑定的特性，js中的this含义非常多，它可以是全局对象，当前对象或者任意对象，这完全取决于函数的调用方式
随着函数使用场合的不同，this的值会发生变化。但是有一个总的原则，就是**this指的是，调用函数的那个对象。**
##作为函数调用
在函数被直接调用时this绑定到全局对象。在浏览器中，window就是全局对象
```
    console.log(this);//window
    function fn1() {
        console.log(this); //window
    }
    fn1();
```
##内部函数
函数嵌套产生的内部函数的this不是其父函数，仍然是全局变量
```
    function fn0() {
        function fn () {
            console.log(this);//window
        }
        fn();
    }
    fn0();
```
##setTimeout,setInterval
这2个方法执行的函数this也是全局对象
```
    document.addEventListener('click',function (e) {
        console.log(this);//#document
        setTimeout(function () {
            console.log(this);//window
        },200)
    },false)
```
##作为构造函数去调用
所谓构造函数，就是通过这个函数生成一个新对象(object)。这时，**this就指这个新对象**。
new运算符接受一个函数F及其参数： new F(arguments)，这一过程分为3步：

- 1.创建类的实例。这步是把一个空的对象的proto属性设为F.prototype p1 = {};
- 2.初始化实例。函数F被传入参数并调用，关键字this被设定为该实例 p1.name = 'deejay' p1.sayName=... **此时的this为这个p1实例**
- 3.返回实例 return p1
##作为对象方法去调用
在JS中，函数也是对象，因此函数可以作为一个对象的属性，此时该函数被称为该对象的方法，在使用这种调用方式时，this被自然绑定到该对象。
```
    var obj1 = {
        name: 'deejay',
        age: 20,
        fn: function () {
            console.log(this); //obj1
        }
    };
    obj1.fn();
```

```
    var obj2 = {
        name: 'deejay',
        age: 20,
        obj3: {
            fn: function () {
                console.log(this); //obj3
            }
        },
    };
    obj2.obj3.fn(); //总结，谁最后点的fn(),this就为谁
```
###小陷阱
```
    var fn2 = obj1.fn;
    fn2();//window
```

##DOM对象绑定事件
在事件处理程序中this代表事件源DOM对象（低版本IE有bug，指向了window）
```
    document.addEventListener('click',function (e) {
        console.log(this);//#document
        var _document = this;
        setTimeout(function () {
            console.log(this);//window
            console.log(_document); // $document
        },200)
    },false);
```
setTimeout中是window，事件处理程序中是事件源DOM对象
##Function.prototype.bind
切换作用域，任何函数都有的一个方法
bind，返回一个函数，并且使函数内部的this为传入的第一个参数
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
#使用call和apply设置this
- **call,apply，调用一个函数，传入函数执行上下文及参数**
```
    fn.call(context,param1,param2...);
    fn.apply(context,paramArray;
```
语法很简单，第一个参数都是希望设置的this对象，不同之处在于call方法接收参数列表，而apply接收参数数组
使用举例：
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
**利用call/apply改变this解决问题**
```
    var arr = [1,2,4,5];
    Math.max.apply(null,arr); //得到最大值
```
```
//    让类数组对象arguments使用数组的方法
    function joinStr() {
//        return arguments.join('-');//error
        return [].join.call(arguments,'-'); //等价于下面的写法
//       return  Array.prototype.join.call(arguments,'-');
//        如果想使用bind
        var　newJoin = Array.prototype.join.bind(arguments);
        return newJoin('-');
    }
    var res = joinStr('a','b','c');
    console.log(res);//a-b-c
```
####caller
在函数A调用函数B时，被调用函数B会自动生成一个caller属性，指向调用它的函数对象，如果函数当前没被调用，或并未被其他函数调用，则caller为null
```
    function fn4() {
        console.log(fn4.caller); // null
        function fn() {
            console.log(fn.caller); //fn4()
        }
        fn();
    }
    fn4();
```
####arguments
在函数调用时，会自动在该函数内部生成一个名为arguments的隐藏对象
该对象类似于数组，可以使用[]运算符获取函数调用时的传递的实参
只有函数被调用时，arguments对象才会创建，未调用时其值为null
```
    function fn5(name,age) {
        console.log(arguments); //["dejay", 20, callee: ƒ, Symbol(Symbol.iterator): ƒ]
        name= 'xxx';
        console.log(arguments); //["xxx", 20, callee: ƒ, Symbol(Symbol.iterator): ƒ]
        arguments[1] = 30;
        console.log(arguments);//["xxx", 30, callee: ƒ, Symbol(Symbol.iterator): ƒ]
    }
    fn5('dejay',20);
```
####callee
当函数被调用时，它的arguments.callee对象就会指向自身，也就是一个对自己的引用。
由于arguments在函数调用时才有效，所以未调用时arguments.callee是不存在的，即为null.callee，并且引用它会有异常.

匿名函数的使用
```
    var i = 0;
    window.onclick = function () {
        console.log(i)
        if (i < 5) {
            i++;
            setTimeout(arguments.callee,200);
        }
    }
```
#关于this的指向的总结（具体见上述代码例子）：
##1,window的情况： 
- 作为函数调用
- 作为内部函数调用
- setTimeout,setInterval
- 小陷阱的情况也属于作为函数调用
##2,作为构造函数去调用，即new的时候：
- 指向构造函数创建的那个实例
##3，作为对象方法去调用（例如工厂模式时）：
- 在使用对象方法调用方式时，this被自然绑定到该对象。
##4，DOM对象绑定事件：
- 此时为事件源DOM对象
##5，apply/call
- this为apply/call指定的值
#函数的执行环境
JS中的函数既可以被当做普通函数执行，也可以作为对象的方法执行，这是导致this含义丰富的主要原因。
一个函数被执行时，会创建一个执行环境(ExecutionContext)，函数的所有的行为均发生在此执行环境中，构建该执行环境时，JS会首先创建arguments变量，其中包含调用函数时传入的参数。
接下来创建作用域链，然后初始化变量。首先初始化函数的形参表，值为arguments变量中对应的值，如果arguments变量中没有对应值，则该形参初始化为undefined。
如果该函数中含有内部函数，则初始化这些内部函数。如果没有，继续初始化该函数内定义的局部变量，需要注意的是这些变量初始化为undefined，其赋值操作在执行环境创建成功后，函数执行才会执行，这点对于我们理解JS中的变量作用域非常重要，最后this变量赋值，会根据函数调用方式不同，赋给this全局对象，当前对象等。
至此函数的执行环境创建成功，函数会开始逐行执行，所需变量均从之前构建好的执行环境中读取。
##三种变量
- 实例变量： （this）类的实例才能访问到的变量
- 静态变量： （属性） 直接类型对象能访问到的变量
- 私有变量： （局部变量）当前作用域内才有效的变量

```
    function ClassA() {
        var a = 1; //私有变量 只有函数内部可以访问
        this.b = 2; // 实例变量，只有实例可以访问
    }
    ClassA.c = 3; //静态变量，也就是属性，类型访问


    console.log(a); //error  外部不能访问函数内部的局部变量
    console.log(ClassA.b);//undefined  ClassA没有b这个属性 即静态变量
    console.log(ClassA.c); //3  ClassA的一个属性c

    var classa = new ClassA();
    console.log(classa.a); //undefined
    console.log(classa.b);// 2  实例对象才有实例变量
    console.log(classa.c); //undefined  
```
