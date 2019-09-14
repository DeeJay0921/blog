---
title: JS作用域链
date: 2017/08/11 23:00:01
tags: 
- 前端
- JS
- 作用域链
categories: 
- 前端
---
JS作用域链
<!--more-->


给出两个不同的例子：

### 范例1：
```
    var x = 10;
    bar();
    function foo() {
        console.log(x);
    }
    function bar () {
        var x = 30;
        foo();
    }
```
分析上面代码：全局作用域中定义了变量x,function foo()以及function bar()，所以对于function bar()和foo()来说，上一级的作用域都是全局作用域。程序调用bar()，进入bar()的作用域，bar()中作用域定义了变量x，调用foo(),而foo()中作用域中没有活动对象，向上一级即全局作用域中寻找变量x,全局环境中的x的值为10，所以输出结果为10。

- 详细分析:
  ```
  对于全局作用域来说：
    GlobalContext = { 
        AO: {   //全局作用域中的活动对象
            x: 10,  //有活动对象x var x =10;
            foo: function(){}, // 声明了函数foo和bar，也是活动对象
            bar: function(){},
        }
    }
    Global没有scope
    接下来进入barContext:
    barContext = { //bar的执行上下文（作用域）中的活动对象
        AO : {
            x: 30 //作用域内有活动对象x  var x = 30;
        },
        scope: GlobalContext.AO  // 代表bar()是在global作用域下声明的。
    }
    对于fooContext则有：
    fooContext = {
        AO: {
            //没有活动的对象
        },
        scope: GlobalContext.AO  //代表foo()是在global作用域下声明的。
    }
    运行过程：先进入bar(),然后进入foo(),foo()中没有活动对象x，则通过scope向上一级作用域的活动对象中寻找活动对象x，即全局作用域中的x，输出10。


###范例2:
```
    var x = 10;
    bar();
    function bar () {
        var x = 30;
        function foo () {
            console.log(x);
        }
        foo();
    }
```

- 分析上面代码：全局作用域中定义了变量x，function bar()。而在function bar()中定义了function foo(),所以对于foo()来说，上一级作用域是bar()的作用域，然后bar()的上一级作用域是全局作用域。调用bar()，进入bar()的作用域，bar作用域中定义了变量x，function foo(),调用foo(),进入foo()的作用域，作用域中没有活动对象x，所以向上一级作用域中寻找，寻找到了bar()中的活动对象x，输出结果为30。

同理：运行过程分析如下：

```
    GlobalContext = {
        AO: {
            x: 10,
            bar: function(){}
        }
    }
    barContext = {
        AO: {
            x: 30,
            foo: function () {}
        },
        scope: GlobalContext.AO  //bar()的上一级作用域为全局作用域
    }
    fooContext = {
        AO: {
            没有活动对象
        },
        scope: barContext.AO  //foo()的上一级作用域为bar()的作用域
    }
    运行过程:先进入bar()的作用域中，然后进入foo，没有活动对象x，然后向上一级作用域即bar的活动对象中寻找活动对象x，找到bar的AO中的x，输出30
```


### 范例3：

给出了一个复杂的作用域链关系：
```
    var a = 1;
    function fn (){
        console.log(a); //undefined
        var a = 5;
        console.log(a); //5
        a++;
        var a;
        fn3();
        fn2();
        console.log(a); //20

        function fn2() {
            console.log(a); //6
            a = 20; //改变了fn中的a(6 ----> 20)
        }
    }
    function fn3 () {
        console.log(a); //1
        a = 200; //改变了全局中的a (1 ----> 200)
    }
    fn();
    console.log(a);  //200
```
详细分析：
```
  开始执行程序时的状态值为：
    GlobalContext = {
        AO: {
            a:1,
            fn: function () {},
            fn3: function () {}
        },
    }
    fnContext = {
        AO: {
            a:undefined,//解析时的值是undefined,
            fn2: function () {}
        },
        scope: GlobalContext.AO // fn的上一级作用域为global
    }
    fn3Context = {
        AO: {
            没有任何活动对象，注意：a = 200，没有用var声明，不是当前作用域即fn3的作用域中的活动对象
        },
        scope: GlobalContext.AO  // fn3的上一级为global
    }
    fn2Context = {
        AO: {
            没有任何活动对象，注意：a = 20，没有用var声明，不是当前作用域即fn2的作用域中的活动对象
        }, 
        scope: fnContext.AO // fn2的上一级作用域为fn
    }
```
运行过程分析:
    运行过程：
1. 先进入fn,进行变量和函数提升，第一句console.log(a)输出undefined，第二句console.log(a)输出5,然后a++，a变为6。在这个过程中，fnContext.AO中的a的值是由undefined（解析变量提升时）到5（解析完成执行赋值语句a=5时）再到6（执行a++完成后），即fn()中的活动对象a此时为6.
  2. 接着调用fn3，进入fn3中，没有活动对象a，所以向上一级即全局作用域中寻找a，fn3中的console.log(a)输出为1，即是全局作用域中的a，接着**执行a=200语句，因为没有var声明，所以不是同一个作用域，即向上一级到全局中寻找a赋值为200，所以此时全局作用域中的a由1变为200**，全局中console.log(a)的结果为2003.
3. 接着进入fn2(),fn2中没有活动对象a，所以向上一级即fn中的活动对象中寻找a，此时fn中的活动对象a为6，所以fn2中的console.log(a)输出结果为6（fn中当前的活动对象a的值）。然后**再在fn2执行a=20的语句，由于没有用var声明，所以不是和fn2同一个作用域的a，即给向上一级作用域中的a赋值20，此时fn中的活动对象a变为20**，所以fn中的第三局console.log(a)输出结果是20。    
 - 容易出现错误的地方，fn中的活动对象a，在预解析时为undefined，然后赋值为5，然后a++为6，最后被fn2中的a=20赋值为20
    同理，全局中的a，也因为fn3中的a=200赋值为200。因为都没有用var声明。
