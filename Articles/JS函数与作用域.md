---
title: JS函数与作用域
date: 2017/08/11 23:00:01
tags: 
- 前端
- JS
categories: 
- 前端
---
JS函数与作用域
<!--more-->


#作用域
在JS中（非ES6），只有函数作用域，没有块作用域。
例如，for循环，while等{}内部的变量其实是和外部处于同一个作用域的：
```
  for (var i =1; i < 5; i ++) {
      var a = 3;
  }
  console.log(a); //3  此时没有输出undefined，说明a和for循环内部的a是同一个作用域。
```
所以只有**函数作用域**：
```
    function fn () {
        var a = 1;
        if (a > 2) {
            var b = 3;
        }
        console.log(b);
    }
    fn();
    console.log(a);
```
上述代码的运行结果等价于：
```
    function fn () {
        var a; // 进行变量的提升
        var b; // 定义b是在if的{}内进行的，但是没有块作用域，实质上作用域还是fn内部，a和b是同一个作用域
        a = 1;
        if (a > 2) { //a没有满足条件，所以不会执行给b赋值的语句，所以n仅仅声明了但是没有赋值，console.log(b)的结果是undefined
            b = 3;
        }
        console.log(b); //undefined
    }
    fn();
    console.log(a); // a is not defined  报错，因为在当前的作用域即全局作用域中，a并没有定义，a只在fn的作用域里定义了。
```
# var
- **var如果重复声明一个已经存在的变量时，原来的变量的值是不会变**的。
```
var a =1;
var a;
var a;
var a;
console.log(a); // 1      重复声明不会改变。
```
- 不加var的作用
**不写var会声明一个全局变量**，所以不建议不写var，即使需要全局变量，也要在全局作用域中使用var声明变量。
```
    function fn () {
        a = 1; //没有用var声明，a其实是一个全局变量，在外部作用域中也能访问
    }
    fn();
    console.log(a);//1 说明全局作用域中也能访问a
```


## 1.函数声明和函数表达式有什么区别


有三种声明函数的方式：
- 构造函数：` var doSomething = new Function("console.log('hello,deejay')");` 不推荐使用
- 函数声明:  
```
  function doSomething () {    // 函数声明
        console.log('hello,deejay');
  }
  doSomething();    // 调用也可以放到声明的前面
```
- 函数表达式：
```
  var doSomething = function () {  //函数表达式
    console.log("hello,deejay");  
  }
  doSomething(); // 表达式调用只能写在赋值声明后面
```
## 2.什么是变量的声明前置？什么是函数的声明前置

var和function的声明前置：**在一个作用域下**，var声明的变量和function声明的函数会前置
```
      console.log(a); // undefined
    var a = 3;
    console.log(a); // 3

    sayHello();

    function sayHello () {
        console.log('hello,deejay');
    }
```
上述代码在解析时其实为
```
    var a;
    function sayHello() {console.log('hello,deejay');} //解析时，var声明的变量和function声明的函数 会前置


    console.log(a); //undefined
    a = 3;
    console.log(a); //3
    sayHello();
```
另外，如果有变量名和函数声明的函数名相同的情况，后面的值会覆盖前面的值，产生报错。
对于函数表达式定义的函数，前置的方式跟var一个变量没什么区别。
```
    console.log(sayHello);//undefined  

    var sayHello = function () {
        console.log('hello,deejay');
    }

    sayHello(); // 特别要注意，对于函数表达式定义的函数，只能先定义，然后再调用，不然会报错。
```
上述代码等价于
```
    var sayHello;

    console.log(sayHello);//undefined

    sayHello = function () {
        console.log('hello,deejay');
    }

    sayHello(); // 特别要注意，对于函数表达式定义的函数，只能先定义，然后再调用，不然会报错。
```

- 函数内部的声明前置 
在一个作用域内，var定义的变量和function声明的函数会前置，那么在函数内部的作用域中，前置规则也是一样的。
```
        function doSomething () {
        console.log(a); // undefined
        var a = 3;
        console.log(a); //3

        //上面代码其实等价于下面代码：
//        var a;
//        console.log(a); // undefined
//        a = 3;
//        console.log(a); //3
    }
    doSomething(); //调用函数，进入函数作用域
```

- 变量和函数命名冲突时
当命名发生冲突时，先进行前置，再进行覆盖
```
    var fn = 3;
    function fn () {}
    console.log(fn); //3
```
上述代码等价于
```
    var fn;
    function fn() {} //此时fn为函数
//    console.log(typeof fn); //function
    fn = 3;
//    console.log(typeof fn); //number
    console.log(fn); //3
```
同理：
```
    function fn() {}
    var fn = 3;
    console.log(fn); //3
```
等价于
```
    function fn() {}  // fn为一个全局函数
    var fn;   // 前面fn函数已经存在，此时兵没有给fn赋值，所以fn仍然是一个函数
 //   console.log(typeof fn);  //function
    fn = 3;  // 此时给fn赋值了之后，fn变为数值
 //    console.log(typeof fn); //number
    console.log(fn); // 3
```

- 函数名和参数名重名时，即如下情况：
```
    function fn (fn) {
        console.log(fn);

        var fn = 3;
        console.log(fn);
    }
    fn(5); //5 3
```
此时运行的过程等价于：
```
        function fn (fn) {
        var fn = 5;//这条语句是JS自动隐藏添加的，当传入参数时，给fn赋值

        var fn;  // 函数内部作用域变量提升
        console.log(fn);  // 此时输出的为传入的已经赋值的参数fn，而不是undefined

        fn = 3;
        console.log(fn); // 输出的是当前作用域内的局部变量fn
    }

```

## 3.arguments 是什么

在函数内部，可以使用arguments对象获取到该函数的所有传入参数，是一个类数组对象。
```
    var getInfo = function () {
        console.log(arguments[0]);
        console.log(arguments[1]);
        console.log(arguments[2]);

    }
    getInfo('deejay',21,'male');

```

## 4. 函数的"重载"怎样实现

**JS没有重载！** 同名的函数会覆盖，但是可以在函数体内针对**不同的参数**调用执行相应的逻辑

可以模拟重载，举例说明：
```
      var getInfo = function (name,age,sex) {
        if (name) {
            console.log(name);
        }
        if (age) {
           console.log(age);
        }
        if (sex) {
            console.log(sex)
        }
    }

    getInfo('deejay',21); //deejay 21 
    getInfo('deejay',21,'male'); //deejay 21 male
```


## 5.立即执行函数表达式是什么？有什么作用

```
    (function () {
        console.log('hello,deejay');
    })();
```
创建一个匿名函数并且立即调用它，一般用于隔离作用域（因为其内部的函数作用域不受外部作用域的影响）


## 6.求n!，用递归来实现
#####递归
1. 自己调用自己
2. 设定终止条件
3. 优点：算法简单
4. 缺点：效率低

求n!的递归实现：
```

    function fn (n) {
        if (n <= 0){
            console.log('n为正整数');
            return;
        }
        else if (n === 1) {
            return 1;
        }
        else if (n >=1 ){
            return n * fn(n - 1);
        }
    }
    var result = fn(5);
    console.log(result);
```


## 7. 分析输出结果
```
  function getInfo(name, age, sex){
		  console.log('name:',name);
		  console.log('age:', age);
		  console.log('sex:', sex);
		  console.log(arguments);
		  arguments[0] = 'valley';
		  console.log('name', name);
	  }

        getInfo('deejay', 21, '男');
        getInfo('dee', 3);
        getInfo('男');
```

输出结果为：
```
    function getInfo(name, age, sex){
        console.log('name:',name);
        console.log('age:', age);
        console.log('sex:', sex);
        console.log(arguments);
        arguments[0] = 'valley';
        console.log('name', name);
    }

    getInfo('deejay', 21, '男');
//    输出结果为：
//    name: deejay
//    age: 2
//    sex: 男
//    ['deejay',21,'男']
//    name valley

    getInfo('deejay', 3);
//    输出结果为
//    name: deejay
//    age: 3
//    sex: undefined
//    ['deejay',3]
//    name valley

    getInfo('男');
//    输出结果为
//    name: 男
//    age: undefined
//    sex: undefined
//    ['男']
//    name valley
```
## 8. 写一个函数，返回参数的平方和？
```
     function sumOfSquares(){
    
     }
      var result2 = sumOfSquares(1,3)
     console.log(result)  //29
     console.log(result2)  //10

```
平方和代码如下：
```
    function sumOfSquares(){
        var sum = 0;
        for (var i = 1; i <= arguments.length; i ++) {
            sum += Math.pow(arguments[i-1],2);
        }
        return sum;
    }
    var result = sumOfSquares(2,3,4);
    var result2 = sumOfSquares(1,3)
    console.log(result)  //29
    console.log(result2)  //10
```

## 9.如下代码的输出？为什么
```
	console.log(a);
	var a = 1;
	console.log(b);
```

输出解释如下：
```
    console.log(a);
    var a = 1;
    console.log(b);


//    等价于
    var a;
    console.log(a); //undefined  预解析，声明了a，但是没赋值，为undefined
    a = 1;
    console.log(b); //Uncaught ReferenceError: b is not defined  没有声明b，报错
```


## 10.如下代码的输出？为什么
```
	sayName('world');
	sayAge(10);
	function sayName(name){
		console.log('hello ', name);
	}
	var sayAge = function(age){
		console.log(age);
	};
```
解释如下：
```
    sayName('world');
    sayAge(10);
    function sayName(name){
        console.log('hello ', name);
    }
    var sayAge = function(age){
        console.log(age);
    };

//    表达式定义的函数，在进行前置的时候，跟用var声明的变量规则一样
//    等价于：
//    function sayName(name){
//        console.log('hello ', name);
//    }
//    var sayAge;
//    sayName('world');//hello, world
//    sayAge(10); //Uncaught TypeError: sayAge is not a function 此时sayAge()只是被声明，并不是一个函数， 报错
//    sayAge = function (age) {
//        console.log(age);
//    }
```

## 11. 如下代码输出什么? 写出作用域链查找过程伪代码
```
    var x = 10
    bar() 
    function foo() {
      console.log(x) //10
    }
    function bar(){
      var x = 30
      foo()
    }
```

伪代码如下：
```
    GlobalContext = {
        AO: {
            x: 10,
            foo: function () {},
            bar: function () {},
        },
    }
    foo.[[scope]] = GlobalContext.AO;
    bar.[[scope]] = GlobalContext.AO;

    fooContext = {
        AO: {},
        scope: GlobalContext.AO
    }

    barContext = {
        AO:{
            x:30,
        },
        scope: GlobalContext.AO
    }
```
可以看出输出的是GlobalContext.AO中的x的值，为10。
##12.如下代码输出什么? 写出作用域链查找过程伪代码
```
var x = 10;
bar() 
function bar(){
  var x = 30;
  function foo(){
    console.log(x) 
  }
  foo();
}	
```
伪代码如下：
```
    GlobalContext = {
        AO: {
            x:10,
            bar: function() {}
        },
    }
    bar.[[scope]] = GlobalContext.AO
    
    barContext = {
        AO : {
            x: 30,
            foo: function () {}
        },
        scope: GlobalContext.AO
    }
    
    fooContext = {
        AO: {},
        scope: barContext.AO
    }
```
很明显输出的是barContext.AO中的x值，为30

##13.以下代码输出什么? 写出作用域链的查找过程伪代码
```
var x = 10;
bar() 
function bar(){
  var x = 30;
  (function (){
    console.log(x)
  })()
}
```

伪代码如下：
```
    GlobalContext = {
        AO: {
            x: 10,
            bar: function() {}
        }
    }
    bar.[[scope]] = GlobalContext.AO
    
    barContext = {
        AO: {
            x: 30,
            匿名函数: function () {}
        },
        scope: GlobalContext.AO
    }
    
    匿名函数Context = {
        AO: {},
        scope: barContext.AO
    }
```
显然输出的是barContext.AO中的x值，为30

##14.以下代码输出什么？ 写出作用域链查找过程伪代码
```
var a = 1;

function fn(){
  console.log(a)
  var a = 5
  console.log(a)
  a++
  var a
  fn3()
  fn2()
  console.log(a)

  function fn2(){
    console.log(a)
    a = 20
  }
}

function fn3(){
  console.log(a)
  a = 200
}

fn()
console.log(a)
```


伪代码为：
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
最终的输出结果为：
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
按照运行顺序依次输出为: undefined 5 1 6 20 200
