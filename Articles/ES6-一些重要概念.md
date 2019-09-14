---
title: ES6-一些重要概念
date: 2017/10/22 00:00:01
tags: 
- 前端
- JS
- ES6
categories: 
- 前端
---
ES6-一些重要概念
<!--more-->

# 一，let/const的理解
[方应航：我用了两个月的时间才理解 let](https://zhuanlan.zhihu.com/p/28140450)

##let 添加了for if等块级作用域

通过例子来分析：

```
for (var i = 0; i < 10; i ++) {
    console.log(i)
}
console.log(i) //10
```
对于var来说，js只有函数作用域，所以上述for循环中的i就是全局的i，输出为10

for循环中换为let声明后： 
```
for (let i = 0; i < 10; i ++) {
    console.log(i)
}
console.log(i) //此时i为undefined
```
let将for的{}也视为一个作用域，外部拿不到这个i，为undefined

那么使用let之后具体内部的实现通过babel编译后为：
```
for (var _i = 0; _i < 10; _i ++) {
    console.log(_i)
}
console.log(i)
```
可以看到是通过替换变量名称实现的这个效果。


同样的，对于if判断，lt也实现了对应的块级作用域

一样，先看常规var声明的例子：
```
function fn () {
    var n = 0
    if (true) {
        var n = 1
    }
    console.log(n) //1
}
fn()
```
由于if没有块级作用域，所以输出的是n为1

那么将var替换为let之后的例子：
```
function fn () {
    let n = 0
    if (true) {
        let n = 1
    }
    console.log(n) // 0
}
fn()
```
使用let之后，if中的n就变为if块级作用域中的局部n，所以输出的n还是fn函数的n，不是if中的n

通过babel编译之后的代码为：
```
function fn () {
    var n = 0 // 这里的let编译成var 因为es5本身函数就是作用域
    if (true) {
        var _n = 1 //这里的let就是通过替换变量生成了if的作用域
    }
    console.log(n) // 0
}
fn()
```

这样就很能理解let到底做了什么事

##const  定义常量

const指令定义一个常量 

```
const b = 0;

// b = 1; //报错 常量不能再进行改写 为read-only
```
####const定义对象

```
const c = {
    a: 1
}
c.a = 2 // 不报错 因为JS中的对象仅仅是个指针，c中存储的地址并没有改变
```
```
c = {
    a: 2
} // 此时就会报错 因为地址变为了另外一个对象的地址
```

# 二，解构赋值 （Destructing）

ES6 允许按照一定模式，从数组和对象中提取值，对变量进行赋值。 

## 基本用法
```
let a = 1,
    b = 2,
    c = 3
//编译为
var a = 1,
    b = 2,
    c = 3
```
上述例子使用解构赋值： 
```
let [a, b, c] = [1, 2, 3]
//编译为
var a = 1,
    b = 2,
    c = 3
```
对于对象的解构赋值
```
let { d,e } = { d: 1,e: 2 }
//编译为
var _d$e = { d: 1,e: 2 },
    d = _d$e.d,
    e = _d$e.e;
```
所以对象的解构赋值是按照这个key的名字来进行的。

进行深度解构： 
```
let obj = {
    p: [
        'Hello',
        {y: 'World'}
    ]
};
let { p: [x, { y }] } = obj;
// 编译为
var obj = {
    p: ['Hello', { y: 'World' }]
};
var _obj$p = _slicedToArray(obj.p,2),
    x = _obj$p[0],
    y = _obj$p[1].y;
```
先克隆这个数据结构，按照数据结构进行层级展开，进行赋值。

###解构赋值的应用
```
//解构赋值的应用

function add ([x,y]) {
    return x + y;
}
add([1,2]);  //3
// 编译为
function add(_ref) {
    var _ref2 = _slicedToArray(_ref, 2),
        x = _ref2[0],
        y = _ref2[1];

    return x + y;
}
add([1, 2]); //3
```
在ES6中，我们可以通过解构赋值，进行`function ([x,y])`的传参，如果是ES5，那么只能写成`function(x,y)`

###字符串的解构赋值
```
// 字符串的解构赋值
const [a,b,c,d,e] = 'hello';
 -----------------------------
var _hello = 'hello',
    _hello2 = _slicedToArray(_hello, 5),
    a = _hello2[0],
    b = _hello2[1],
    c = _hello2[2],
    d = _hello2[3],
    e = _hello2[4];

```
### rest解构
```
const {p,...rest } = {p: 1,a: 2,c: 3}
---------------------------------------

    var _p$a$c = { p: 1, a: 2, c: 3 },
    p = _p$a$c.p,
    rest = _objectWithoutProperties(_p$a$c, ["p"]);
```

# 三，函数扩展

##函数参数的默认值

```
function log (x,y) {
    x = x ? x : true; //x没有就拿true
}
```
上述函数中设置默认值为true的方法

下面例子就是设置参数y的默认值为 'world'的方法

```
function log (x, y = 'world') { // 这就实现了 y=y?y:'world'
    console.log(x, y)
}
-----------------------
function log(x) {
    var y = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : 'world';

    console.log(x, y);
}
```
## rest参数
ES6中引入了rest参数，用于获取函数的多余参数，这样就不需要使用arguments对象了。rest参数搭配的变量是一个数组，该变量将多余的参数放入数组中。

举个求和函数的例子
```
function add (...values) {
    let sum = 0;
    for (var val of values) {
        sum += val
    }
    return sum;
}
add (2,5,3);
```

通过一个简单的例子来理解values：
```
function add (...values) {
    let sum = 0;
    console.log(values)
}
add (2,5,3);
-------------------------------------
function add() { // 这边是没有传参的，而是通过下面的for循环遍历arguments得到的values
    var sum = 0;

    for (var _len = arguments.length, values = Array(_len), _key = 0; _key < _len; _key++) {
        values[_key] = arguments[_key];
    }

    console.log(values);
}
add(2, 5, 3);
```
#### ...values前面还有参数的情况
```
function add (a,...values) {
    console.log(values)
}
add (2,5,3); //[5,3]
-------------------------------------------
function add(a) { //前面写定参数的话，那么参数就传入
    for (var _len = arguments.length, values = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {   //此时的_key序数号为1
        values[_key - 1] = arguments[_key];
    }

    console.log(values);
}
add(2, 5, 3);
```
其他更多传入的参数同理。
## 扩展运算符

扩展运算符就是三个点(...),好比**rest参数的逆运算**，将一个数组转为用逗号分隔的参数序列
## 箭头函数
ES6允许使用箭头 `=>`定义函数
```
var f = v => v;
------------------------
var f = function f(v) {
  return v;
};
```
```
var f1 = v => {
    return v
}
-------------------------
var f1 = function f1(v) {
    return v;
};
```
##箭头函数中的注意点

- this
```
var f1 = () => {
    console.log(this) //undefined
}
var f2 = function () {
    console.log(this)  //window
}
//es6和es5的函数比较
---------------------------------------
var f1 = function f1() {
    console.log(undefined); //undefined
};
var f2 = function f2() {
    console.log(this); //window
};
```
这里的**箭头函数中的this指向的是当前作用域所在的上下文的this**。再看一个例子

```
var f3 = function () {
    return () => {
        console.log(this)
    }
}
-----------------------------
var f3 = function f3() {
    var _this = this;

    return function () {
        console.log(_this);
    };
};
```
从这个例子分析，当前箭头函数的作用域为自身的这个{},这个{}所在的上下文即为f3函数的作用域的，所以这个箭头函数的this为f3中的this,**即上一级的this**
###绑定this
箭头函数可以绑定this对象，大大减少了显式绑定this对象的写法（call、apply、bind）。
函数绑定运算符是**并排的两个冒号（::），双冒号左边是一个对象，右边是一个函数**。该运算符会自动将左边的对象，作为上下文环境（即this对象），绑定到右边的函数上面。

#Promise对象
```
let p = new Promise( (resolve,reject) => {
    resolve(1)
})
console.log(p)
// p ===> fulfilled 1
// Promise的状态 fulfilled pending rejected
// Promise的值  有值 一直为空 有值
```

```
let p = new Promise( (resolve,reject) => {
    resolve(1)
})
console.log(p)
// // p ===> fulfilled 1
// // Promise的状态 fulfilled pending rejected
// // Promise的值  有值 一直为空 有值

let p1 = p.then(val => { //如果是then 那么这个函数作为resolve进行执行
    console.log(val) //1
})
let p2 = p.then(val => {
    console.log(val) //1  再调用一次 值还是1 只要状态不是pending时，这个状态就永久的保持并且不会改变了
//    所以再次调用也不会改变p，只是将其值输出
})
```
只要p的状态不是pending，而是确定有值了，这边p无论调用多少次，都不会改变。
```
let p = new Promise( (resolve,reject) => {
    // resolve(1);
    reject(2);
})
let p3 = p.catch(val => { //catch时，作为reject执行
    console.log(val) //2
})
```

```
let p = new Promise((resolve,reject) => {
    resolve(1)
})
let p1 = p.then(val => { //如果then里面的回调函数return出来一个值，那么这个值作为p1的promise value，并且状态变为fulfilled
    return 2
//    
})
```
如果then里面的回调函数return出来一个值，那么这个值作为p1的值，并且状态变为fulfilled。如果在回调中不写return，那么就把函数默认return 的undefined作为p1的值。

# Iterator(遍历器)

Iterator的作用： 
- 为各种数据结构提供一个统一简便的访问接口
- 使得各种数据结构的成员能够按某种次序排列
- ES6创造了一种新的for...of循环，Iterator接口主要用于for...of

```

// const s = (...rest) => {
//     for(let val of rest) { // 可以遍历数组
//         console.log(val)
//     }
// }
// s(1,2,3,4)

// const s = function() {
//     for(let val of arguments) {  //可以遍历 类数组对象
//         console.log(val)
//     }
// }
// s(1,2,3,4)

// const s = {
//     a: 1,
//     b: 2
// }
// for (let val of s) { //报错
//     console.log(val)
// }
```

# class
```
class Test {
    constructor () {
        this.a = 'a';
        this.b = 'b'
    }
    c () {
        console.log('C')
    }
}
--------------------------------
var Test = function () {
    function Test() {
        _classCallCheck(this, Test);

        this.a = 'a';
        this.b = 'b';
    }

    _createClass(Test, [{
        key: 'c',
        value: function c() {
            console.log('C');
        }
    }]);

    return Test;
}();
```
上述例子，es5的写法为：
```
//es5的写法为：
var Test = function () {
    this.a = 'a';
    this.b = 'b';
}
Test.prototype.c = function () {
    console.log('c');
}

```
#module

先将export import关键字处理为require函数（nodejs），在浏览器环境下没有require，只能通过webpack来进行处理require函数

#Generator (原则上在Nodejs中使用)
Generator 函数是 ES6 提供的一种异步编程解决方案，语法行为与传统函数完全不同。
形式上，Generator 函数是一个普通函数，但是有两个特征。一是，function关键字与函数名之间有一个星号；二是，函数体内部使用yield表达式，定义不同的内部状态（yield在英语里的意思就是“产出”）

```
function* helloWorldGenerator () {
    yield 'hello'; // 碰到yield会停止执行，需要手动next进行执行
    yield 'world';
    return 'ending'
}
var hw = helloWorldGenerator()
let a = hw.next();
let b = hw.next();
let c = hw.next();
console.log(a,b,c)
//{ value: 'hello', done: false } //done 为false代表还可以往下next()
// { value: 'world', done: false }
// { value: 'ending', done: true }
```
