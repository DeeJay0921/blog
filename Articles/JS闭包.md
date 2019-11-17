---
title: JS闭包
date: 2017/08/18 00:00:01
cover: https://i1.wp.com/programmingwithmosh.com/wp-content/uploads/2019/04/javascript-closure.png?ssl=1
tags: 
- 前端
- JS
- 闭包
categories: 
- 前端
---
JS闭包
<!--more-->

#一些基础概念

###变量的生命周期

- 全局作用域下的变量，定义的时候开始存在，页面关闭或者刷新就消失。
- 函数内部的变量，在你写的时候是不存在的，一般来说，只有调用这个函数的时候，这个变量才会被声明。那么这个函数内部的a什么时候消失呢，当a所在的作用域不见的时候a就消失。
```
function fn () {
    var a = 1;
    // return undefined; 所有函数不写return时，默认有这一句
}
// 浏览器执行到这一行，此时a不存在

fn();//这个时候a就诞生了
//fn()执行完了之后，a就消失了
fn();//这时候会产生一个新的a，并不是原来的a
//fn() 执行完成后，新的a又消失了
```
- **如果这个变量被引用着，就不能回收**
```
function fn () {
    var a = 1;
    var b = 2;
    window.xxx = a;
    // return undefined; 所有函数不写return时，默认有这一句
}
// 浏览器执行到这一行，此时a不存在

fn();//这个时候a就诞生了
//此时fn()执行完，b消失，a还存在，因为a被引用了。
console.log(window.xxx); //1
//等window消失a也就消失了
//其实可以认为函数执行完成后，a这个变量名是消失了的，只是这段内存没消失，还在继续被引用，等window消失的时候再释放这段内存
```
###var作用域
- 就近原则
```

    var a;
    function fn1 () {
        var a;
        function fn2 () {
            var a;
            a = 1; //给f2的a赋值
        }
    }
    
    function f2() {};
    function f1 () {
        function f2 () {}
        f2(); //调用的是f1内部的f2
    };
```
- 词法作用域
```
    var a;
    function fn1 () {
        var a;
        function fn2 () {
            var a;
            a = 1; //给f2的a赋值
        }
    }
```
不需要执行代码，分析语句的词法就能判断作用域叫词法作用域
- 同名的不同变量

###立即执行函数
- 如果想得到一个独立的作用域，必须要声明一个函数
- **如果想运行函数内部的代码，必须要执行函数**
立即执行函数即创建一个匿名函数作用域并且立即执行。前面可以是! + - 等符号。
- 关于有没有var声明的区别：
```

var a = 100;

!function() {
    var a = 1;
    !function() {
        a = 2; // 没有用var声明，所以不是当前作用域的a，而是上一级的作用域的a，所以改变了上一级a的值
        console.log(a);//2
    }();
    console.log(a);//2   a的值被改变 输出2
}();
console.log(a);//100
```
- 立即执行函数的传参:
```
    var a = 1;
    !function(a) { //这里的a是个形参
        console.log(a); //外面传入一个a，里面的作用域输出2
    }(2);
    console.log(a); //全局作用域下的a还是1


    //上面的形参a其实相当于下面这么写
    !function(){
        var a = arguments[0];
        console.log(a);//外面传入一个a，里面的作用域输出2
    }(2);
```
传参例子2
```

var a = 100;

!function(a){
    console.log(a);
}(a);
//这里的两个a，传入的a是外部传入的值，全局变量a，而匿名函数的a仅仅是个形参  等于不写参数，里面一个var a = arguments[0];
//只是在执行函数的时候恰好把传入参数的a赋值给了内部的a
```
###变量（声明）提升
- 浏览器在执行代码之前，会把所有声明提升到作用域的顶部
```
var a = 100;
var a = function () {};
function a() {};
console.log(a); //function
//等价于
var a;
var a;
function a () {};
a = 100;
a = function() {};
console.log(a); // 所以是function
```
- 函数内部的变量提升
```
//函数内部的声明提升，不会提升到全局或者外部的作用域的，就只会提升到当前作用域顶部
function f1() {
    // var a = 100; 变量提升等价于
    var a;
    a = 100;
}
f1();
//另外，上述代码和下面的是一样的
function f1() {
    a = 100;
    var a;
}
//只有在当前作用域中有var，就不会跑出当前作用域
//如果是
var a = 1;
function f1 () {
    a = 100;
//    后面语句里没有用var声明a，那么这个a就不是当前f1下的a。会在f1的上级作用域中寻找a，并且改变a的值
}
f1();
console.log(a); //100
```
- 带有迷惑性的变量提升
**先进行提升，在考虑代码**
```
var a = 100;  // 第一个a
function f1 () {
    var b =2;
    if (b === 1) {
        var a; //第二个a
    }
    a = 99;
}
//上述例子中，f1内部写的a = 99指的是谁？ 
// 进行变量提升，首先明确JS没有块级作用域，所以什么if for都不是新的作用域，变量提升的时候也不用考虑if的判断条件
//总而言之，先进行提升，在考虑代码，即f1内部等价于
var a = 100;
function f1 () {
    var b;
    var a;
    b = 2;
    if(b === 1){
        
    }
    a = 99;
}
//提升之后自然发现指的是f1内部的a
```
#闭包
```
var items = document.querySelectorAll('li');
for(var i = 0; i < items.length; i ++) {
    items[i].onclick = function () {
        console.log(i);
    }
}
```
上述例子分析：
```
var items = document.querySelectorAll('li');
for(var i = 0; i < items.length; i ++) {
    items[i].onclick = function () {
        console.log(i);
    }
}
console.log(i);

//进行变量提升
var items;
var i;
//这里提升了i，整个作用域中只有这一个i，所以循环到6之后，所有的i的值都为6
items = document.querySelectorAll('li');
for(i = 0; i < items.length; i ++) {
    items[i].onclick = function () {
        console.log(i); //C
    }
}
console.log(i); // D
// 变量提升完成后 D是一定比C先执行的
// 当D执行时 i已经循环为6，所以后面C输出的都是6


//所以如果要想使得每次输出的值不一样，就得使得每次的i不是同一个i
//要取得不同的i,就要创建新的作用域,那么我们来创建新的作用域:
var items;
var i;
items = document.querySelectorAll('li');
for(i = 0; i < items.length; i ++) {
    function temp (i) {//创建一个新的函数作用域，在这个作用域中传入每次循环得到的i的值，就能得到不一样的i了
    //    还要注意前面提到的问题，temp(i);中的i和函数的形参i不是同一个东西，具体见立即执行函数的传参
        items[i].onclick = function () {
            console.log(i); //接收到每次的i值然后赋给i，这样每次循环的i的值就不一样了
        }
    }
    temp(i); // 传入每次循环的i值

}
//那么前面提到的立即执行函数可以简化代码，不用写temp()，直接写个立即执行函数并且传入i就好了
var items;
var i;
items = document.querySelectorAll('li');
for(i = 0; i < items.length; i ++) {
    !function(i) {  //写成立即执行函数简化代码
        items[i].onclick = function () {
            console.log(i); //接收到每次的i值然后赋给i，这样每次循环的i的值就不一样了
        }
    }(i);

}

//上述代码就等价于下面的，temp函数时一个返回函数的函数，创造一个函数用于绑定事件
var items;
var i;
items = document.querySelectorAll('li');
for(i = 0; i < items.length; i ++) {
    function temp(i) {  // 这里temp(i)其实也是要提升的，但是只要知道内部的i跟外部i不一样就好了
        return function () {
            console.log(i);
        }
    }

    var fn = temp(i); //传入每次的i的值  fn必须为一个函数才能赋给onclick事件，所以temp的返回值必须也是一个函数

    items[i].onclick = fn;
}


// 有了返回值为一个函数的思想，我们可以直接给onclick赋给一个返回函数的函数，这样创建了一个新的作用域保存每次循环的i值，如下：
var items;
var i;
items = document.querySelectorAll('li');
for(i = 0; i < items.length; i ++) {
    items[i].onclick = function(i) {
        //函数内部作用域的i和外部的i不同
        return function (){
            console.log(i);
        }
    }(i); //传入每次循环的i值

}

```
总结一下思路：
- **要得到每次循环的i值，就必须得创建新的作用域。**因为全局作用域当中只有一个i，是不会变的，需要一个新的作用域来获取每次循环的i的值。
- 我们给onclick绑定的一定是一个函数，所以赋给onclick一个立即执行函数之后，这个立即执行的值，即**return的东西一定是一个函数**。这就是为什么一定要return一个函数。
















###通过作用域链理解闭包
第一个例子：
```
    //通过作用域链理解闭包


    //下例中定义一个数组，遍历数组给每一项赋给一个函数，最后输出的 fnArr[1]的执行结果（）   发现结果都是2，不管是fnArr[1]还是fnArr[0]
    // var fnArr = [];
    // for (var i = 0; i < 2; i ++) {
    //     fnArr[i] = function() {
    //         return i;
    //     }
    // }
    // console.log( fnArr[0]() ); //2
    // console.log( fnArr[1]() ); //2
    //分析： 当我们写一段函数体或者函数名的时候，就是一段代码，一个指针或者一个地址，只有后面加了括号（），它才会真正的去执行
    //没有执行的话，就相当于没有任何的作用，就是一段代码。
    //所以在上面的for循环中，赋给fnArr[i]的时候没有执行，当循环结束时,i的值为2，此时调用fnArr[1]()或者fnArr[0]并且输出,
    // 此时函数才会真正的执行，但是这个是函数里面没有i，只会向外部的全局作用域中寻找i，即for循环中的i，为2，所以不管fnArr[0]还是fnArr[1]都输出2


    //针对上面的例子，我们提出了对代码的改装要求，要求fnArr[0]就是输出0，fnArr[1]就是输出1。要做到这个效果，就要用到闭包
    // 第一种改装方法：
    // var fnArr = [];
    // for (var i = 0;i < 2; i ++) {
    //     (function(i){
    //         fnArr[i] = function () {
    //             return i;
    //         }
    //     })(i);
    // }
    // console.log( fnArr[0]() ); //0
    // console.log( fnArr[1]() ); //1
//    针对第一种改装方法，就等价于下面这么写，
//    第一种改装方法的改写1,去掉for循环：
//     var fnArr = [];
//     (function(i){
//         fnArr[i] = function () {
//             return i;
//         }
//     })(0);
//     (function(i){
//         fnArr[i] = function () {
//             return i;
//         }
//     })(1);
//     console.log( fnArr[0]() );
//     console.log( fnArr[1]() );
//    这种写法还有立即执行函数，不好理解，接着改写2，去掉立即执行函数,就等价于：
//    第一种改装方法的改写2：
    var fnArr = [];
    function fn1 (i) {
        fnArr[i] = function fn11() {
            return i;
        }
    }
    function fn2 (i) {
        fnArr[i] = function fn22 () {
            return i;
        }
    }
    fn1(0);
    fn2(1);

    console.log( fnArr[0]() );
    console.log( fnArr[1]() );

//    那么通过作用域链来分析改写2:
    globalContext = {
        AO : {
        //    活动对象,变量提升
            fnArr: undefined,
            fn1: function,
            fn2: function,
        }
    }
    fn1.[[scope]] = globalContext.AO;
    fn2.[[scope]] = globalContext.AO;

    fn1Context = {
        AO: {
            i: 0,
            fn11: function,
        },
        scope: fn1.[[scope]]
    }
    fn11.[[scope]] = fn1Context.AO;
    //运行到fn1的时候，没有自身的AO里面没有fnArr，所以就要到scope里面去找，fn1.[[scope]]是globalContext的AO，找到了fnArr
    //此时globalContext的AO的fnArr的值就由undefined变为[fn11]，然后什么都没发生，退出来进入fn2
    fn2Context = {
        AO : {
            i: 1,
            fn22: function,
        },
        scope: fn2.[[scope]],
    }
    fn22.[[scope]] = fn2Context.AO;
    //此时进入fn2，寻找fnArr和上面一样，过程结束后，globalContext的AO里面的fnArr的值为[fn11,fn22];
    //现在    console.log( fnArr[0]() ); 要输出fnArr[0]() ，即在globalContext中找到AO里面的fnArr[0]并且执行，此时进入fn11的context
    fn11Context = {
        AO: {
        //    AO是空的
        },
        scope: fn11.[[scope]],
    }
    // fn11没有AO,所以只能通过scope来寻找i，fn11.[[scope]]就是fn1Context.AO, 所以找到了i，i为0，
    // 一样的，当你输出fnArr[i]的时候，结果是调用fn22，然后找到fn2中的AO的i，为1
    fn22Context = {
        AO: {
            //    AO是空的
        },
        scope: fn22.[[scope]],
    }
    //那么对比我们一开始没有达成效果的例子，那个例子是因为在当前函数中找不到i，所以直接去全局中找到了i
    //我们现在改写了之后，在函数和全局之间又加了一层包装，在scope的中途就能获取到i，所以能够达成效果，这就是闭包
```

第二个例子：
```
    //
    function fn () {
        var s  = 1;
        function sum () {
            ++s;
            console.log(s);
        }
        return sum;
    }
    var mySum = fn();
    mySum(); //2
    mySum(); //3
    mySum(); //4
    var mySum2 = fn();
    mySum2(); //2
    mySum2(); //3
    //上述写法等价于
    // function fn () {
    //     var s = 1;
    //     return function () {
    //         ++s;
    //         console.log(s);
    //     }
    // }
    //同样的，使用作用域链的伪代码来分析：
    globalContext = {
        AO : {
            fn: function,
            mySum: undefined,
            mySum2: undefined,
        }
    }
    fn.[[scope]] = globalContext.AO;
    //mySum = fn()时，进入fn的执行上下文
    fnContext = {
        AO: {
            s: 1,
            sum: function,
        }
        scope: fn.[[scope]]
    }
    sum.[[scope]] = fnContext.AO;
    //执行完成之后退出fn,此时globalContext的AO里面的mySum的值由undefined变为fn（），即sum，调用mySum（），即执行sum，此时进入sum的context
    sumContext = {
        AO : {
        //    为空
        },
        scope: sum.[[scope]],
    }
//    sum没有AO，找不到s，所以到scope里面，即fn的AO里面找到s，此时s的值由1变为2.
//    再次执行mySum（），此时sum还是没有s，继续到scope中去找，即找到了fnContext的AO的s，此时s为2，所以由2变为3

//    然后执行到了mySum2 = fn(),此时，会初始化一个执行上下文，此时的s为1
    fnContext = {
        AO: {
            s: 1,
            sum: function,
        }
        scope: fn.[[scope]],
    }
    sum.[[scope]] = fnContext.AO;

```
- 另外闭包的概念： 就是外面作用域里一个变量，内部作用域里在使用这个变量。
[闭包 参考资料](https://zhuanlan.zhihu.com/p/22486908)
[异步 参考资料](https://zhuanlan.zhihu.com/p/22685960)
