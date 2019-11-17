---
title: 对ES6的再次补充理解
date: 2019/07/04 00:00:01
cover: https://cdn-images-1.medium.com/max/1600/1*SL4sWHdjGR3vo0x5ta3xfw.jpeg
tags: 
- Vue
- ES6
categories: 
- 前端
---
对ES6的再次补充理解
<!--more-->

# let
#### for(){}中的let
```
for (let i = 0; i < 3; i ++) {
    let i = 'ddd' 
    console.log(i) // 输出3次 ddd
}
```
for循环有一个特别之处，就是**设置循环变量的那部分是一个父作用域，而循环体内部是一个单独的子作用域**

所以可以在循环体内部可以再次声明一个i，和设置循环变量的i不是同一个

```
for (let i = 0; i < 3; i ++) {
    console.log(i)  // 输出 0 1 2
}
```
这次在循环体内部输出的i其实是for()中的i，即**它父作用域中的i**

#### let的暂时死区

看一个例子
```
var t = '123'
if(true) {
    t = '456' // ReferenceError
    let t
}
```
存在全局变量t，但是块级作用域内let又声明了一个局部变量t，导致后者**绑定这个块级作用域**，所以**在let声明变量前，对t赋值会报错**。

暂时死区（temporal dead zone，TDZ）的意思是：** 如果区块中存在let和const命令，这个区块对这些命令声明的变量，从一开始就形成了封闭作用域。凡是在声明之前就使用这些变量，就会报错**。

只要块级作用域内存在let命令，它所声明的变量就“绑定”（binding）这个区域，不再受外部的影响。

```
var t = '123'
if(true) {
    // TDZ开始
    t = '456' // ReferenceError
    console.log(t) // ReferenceError

    let t // 这时候TDZ才结束
    console.log(t) // undefined

    t = 'abc'
    console.log(t) // abc
}
```

上述例子显示的很明显，只要该作用域中有let，那么**从作用域开始的地方，TDZ就开始，一直到let声明，TDZ才结束。**

暂时性死区的本质：只要一进入当前作用域，所要使用的变量**就已经存在了，但是不可获取**，只有等到声明变量的那一行代码出现，才可以获取和使用该变量。

# 解构赋值

ES6可以**从数组和对象**中提取值，按照一定的格式对变量进行赋值，称为**解构赋值**。
```
let [a,b,c] = [1,2,3]
console.log(a,b,c)// 1 2 3 
```
从数组[1,2,3]中提取值，按照对应位置，对变量赋值。

解构赋值中需要注意的点: 
- 不完全解构
- 带默认值的解构赋值

#函数拓展
-  参数可以设置默认值（默认值的（）也是一个作用域）
- rest参数
- 箭头函数的this 

#数组扩展
- 扩展运算符
- Array.from()
- Array.of()

#对象扩展
- 对象属性和方法的简写
- Object.assign()

#Symbol
- Symbol() / Symbol.for()
- Symbol.keyFor()

# Set
- 构造函数上prototype的属性： 
1，constructor,没什么好说的，所有构造函数的prototype上的constructor都指向该构造函数本身，Set也不例外。
2，size，Set.prototype.size 返回Set的实例对象的成员总数
```
// Set数据结构
let set = new Set()
let arr = [2,1,1,1,1,2,3,4,5,4,5]
arr.forEach((e) => {
    set.add(e) // Set和数组类似  但是所有成员都是唯一的
})
log(set) //Set { 2, 1, 3, 4, 5 }
log(set.constructor) // constructor默认指向Set构造函数
log(set.size) // 返回实例的成员总数
```
- 对实例的操作方法
 1, add(value) 添加某个值，返回 **Set 结构本身**。
2, delete(value) 删除某个值，返回一个**布尔值**，表示删除是否成功。
3, has(value) 返回一个**布尔值**，表示该值是否为Set的成员。
4, clear()：清除所有成员，没有返回值。
```
let set = new Set([1,2,3,5])
set.delete(2) // 返回一个boolean值 删除传入的值
log(set) //Set { 1, 3, 4, 5 }
log(set.has(3)) //true  返回一个boolean值
set.clear() // clear() 清空实例中的所有值
log(set) // Set {}
```
- 对Set实力的遍历操作
1, keys()：返回键名的遍历器    由于Set实例没有键值只有键名，所以keys()和values()的行为完全一致
2, values()：返回键值的遍历器
3, entries()：返回键值对的遍历器
4, forEach()：使用回调函数遍历每个成员  可以有第二个参数，表示**绑定处理函数内部的this对象**。
Set的**遍历顺序就是插入顺序**，所以可以在Set里保存一些回调函数，遍历的时候就会按照添加时的顺序进行遍历。
```
// Set的遍历操作  Set的遍历顺序就是添加顺序，所以添加一些回调函数进行遍历时，就会按照添加的顺序进行遍历
let cbList = new Set();
let cb1 = () => {console.log('cb1')};
let cb2 = () => {console.log('cb2')};
let cb3 = () => {console.log('cb3')};
cbList.add(cb1)
cbList.add(cb2)
cbList.add(cb3)
cbList.forEach((cb) => {
    cb() // cb1 cb2 cb3
})
```
- 另外可以通过Array.from() / ...(或者扩展运算符) 将Set实例转换为数组  实现**数组去重**
```
// Array.from()可以转换Set实例为数组  实现数组去重
let dupeArr = [1,1,1,2,2,3]
let dedupeArr = Array.from(new Set(dupeArr)) // let dedupeArr = [...new Set(dupeArr)];
log(dedupeArr) // [1,2,3]
```
# WeakSet
WeakSet 和 Set有两个区别
1. WeakSet 的成员**只能是对象**，而不能是其他类型的值。
```
let ws = new WeakSet();
// ws.add(1); //  Invalid value used in weak set
// ws.add(Symbol()); //Invalid value used in weak set
```
2. WeakSet 中的对象都是**弱引用**（垃圾回收机制不考虑 WeakSet 对该对象的引用，也就是说，如果其他对象都不再引用该对象，那么垃圾回收机制会自动回收该对象所占用的内存，不考虑该对象还存在于 WeakSet 之中）,同时WeakSet也不可遍历。

# Map
针对ES5中的对象结构（都是key/value格式），而key只可以是字符串的情况，ES6提出了全新的Map数据结构。Map结构类似于对象，也是键值对的形式，但是键值的范围不仅仅限于字符串。
Object: '字符串 - 值'
Map: '值 - 值'
```
const o = {greet: "hello, Map's key"};
const m = new Map();
m.set(o,"hello, Map's value");
log(m);
log(m.get(o)); //hello, Map's value
m.has(o) // true
m.delete(o) // true
m.has(o) // false
```
上述例子，介绍了将一个对象o ` {greet: "hello, Map's key"}`作为Map结构的key进行操作。

Map()构造函数也可以接收一个数组作为参数：
```
const arr = [
    ['name','DeeJay'],
    ['title','Learn Map']
];  // 数组是包含了一个个表示key/value对的数组的数组集合
const m = new Map(arr);
log(m.size); // 2
log(m.has('name')); // true
log(m.get('name')); // DeeJay
```
在新建Map时，指定了name和title作为key。

关于这一点，不仅仅是数组，**任何具有 Iterator 接口、且每个成员都是一个双元素的数组的数据结构**都可以当作Map构造函数的参数。

试一下用Set作为Map的参数
```
let s = new Set([
    ['name','yang'],
    ['age',21]
]);
let m = new Map(s); //Set作为Map()的参数
log(m.size); // 2
log(m.get('age')); // 21
```
```
let m1 = new Map([
    ["name","yang"],["age",21]
]);
let m2 = new Map(m1); //Map也可以用来作为参数生成Map
console.log(m2); 
console.log(m2.get('age')); // 21
```
Map结构只有对于是**同一个对象的引用**，才将其视为**同一个键**。来看下面的例子：
```
let m = new Map();
m.set('age',21); // 对于不是对象的'age'  因为地址不会变  就是同样的引用，可以get到
console.log(m.get('age')); // 21
m.set(['age'],21); // 这次存入的key为一个对象 ['age'],下面虽然也在get['age'],但是其实不是同一个对象，地址不一样。 所以get的值是undefined
console.log(m.get(['age'])); // undefined
```
```
const obj1 = {name: 'DeeJay'}; //两个对象的地址不一样  虽然值是一样的  但是在Map中被视为两个键
const obj2 = {name: 'DeeJay'};  // 说明了在Map中的键实际上是跟内存地址绑定的，只要内存地址不一样，就视为两个键。
let m = new Map();
m.set(obj1,111);
m.set(obj2,222)
console.log(m.get(obj1)); // 111
console.log(m.get(obj2)); // 222
```
通过上面的例子可以看出：**Map中的键实际上是跟内存地址绑定的，只要内存地址不一样，就视为两个键。**
对于简单类型的键值(Number,String,Boolean)来说，只要二者是严格相等的，那么Map就视为同一个键(0和-0 视为同一个键，NaN和NaN虽然不相等，但是在Map中视为同一个键)。

- Map的遍历
keys()：返回键名的遍历器。
values()：返回键值的遍历器。
entries()：返回所有成员的遍历器。
forEach()：遍历 Map 的所有成员。
一样的，Map遍历的顺序也是插入的顺序。
```
let m = new Map();
m.set('a',500);
m.set('b',600);
m.forEach( e => {
    console.log(e); // 500 600 遍历顺序就是插入顺序
}
```
- 一样的，使用...扩展运算符或者Array.from可以将Map转换为数组
```
let m = new Map();
m.set('a',500);
m.set('b',600);
// let arr = [...m];
let arr = Array.from(m);
log(arr); //[ [ 'a', 500 ], [ 'b', 600 ] ]
```

# Proxy
`let p = new Proxy(target, handler);`

如上，Proxy也是一个构造函数，接受两个**对象**作为参数，target为要进行代理的目标对象，handler也为对象，是一个配置对象，对于每一个要代理的操作，提供一个方法拦截对应的请求。

先来理解代理：对target目标对象的访问，都设置一层拦截层，可以对外界的操作进行过滤和改写，这就是代理Proxy。

先来看一个例子：
```
let targetObj = {}; // 目标对象
let p = new Proxy(targetObj, { // 为目标对象设置代理
    get() {
        return "U are getting TargetObj's values~";
    }
});
let res = p.time; // 设置了代理之后，要通过创建的Proxy实例来访问，直接访问targetObj不会通过代理
console.log(res); // U are getting TargetObj's values~
```
上述例子中：通过给target设置代理，通过Proxy的实力访问目标对象时，会执行我们在handler中规定的方法。

需要注意的是： **一定要通过Proxy实例来访问**，才会通过代理。

