---
title: 异步_-promise
date: 2017/10/13 00:00:01
tags: 
- 前端
- JS
- 异步
- Promise
categories: 
- 前端
---
异步_-promise
<!--more-->

[阮一峰  js的4种异步编程方法](http://www.ruanyifeng.com/blog/2012/12/asynchronous%EF%BC%BFjavascript.html)
[其他的一些异步编程方法](https://zhuanlan.zhihu.com/p/25562598)
#js 异步编程方法

##一，回调函数

假定有两个函数f1和f2，后者等待前者的执行结果。

如果f1是一个很耗时的任务，可以考虑改写f1，把f2写成f1的回调函数。
```
　　function f1(callback){  //注意这时候因为有setTimeout,f1就不会阻塞后面的程序运行了
　　　　setTimeout(function () {
　　　　　　// f1的任务代码
　　　　　　callback();
　　　　}, 1000);
　　}
```
执行代码就变成下面这样：
  　`　f1(f2);`

采用这种方式，我们把同步操作变成了异步操作，f1不会堵塞程序运行，相当于先执行程序的主要逻辑，将耗时的操作推迟执行。

回调函数的总结:
- 优点是简单、容易理解和部署。
- 缺点是不利于代码的阅读和维护，各个部分之间高度[耦合](http://en.wikipedia.org/wiki/Coupling_(computer_programming))（Coupling），流程会很混乱，而且每个任务只能指定一个回调函数。

##二，事件监听

另一种思路是采用事件驱动模式。任务的执行不取决于代码的顺序，而取决于某个事件是否发生。
还是以f1和f2为例。首先，为f1绑定一个事件。
```
　　f1.on('done', f2); //监听f1的done事件，事件出发之后进行执行f2
```
上面这行代码的意思是，当f1发生done事件，就执行f2。然后，对f1进行改写：
```
　　function f1(){
　　　　setTimeout(function () {
　　　　　　// f1的任务代码
　　　　　　f1.trigger('done');
　　　　}, 1000);
　　}
```
f1.trigger('done')表示，执行完成后，立即触发done事件，从而开始执行f2。

这种方法的优点是比较容易理解，可以绑定多个事件，每个事件可以指定多个回调函数，而且可以["去耦合"](http://en.wikipedia.org/wiki/Decoupling)（Decoupling），有利于实现[模块化](http://www.ruanyifeng.com/blog/2012/10/javascript_module.html)。缺点是整个程序都要变成事件驱动型，运行流程会变得很不清晰。

## 三，事件的发布订阅模式

这种设计模式将事件理解为一个信号，某个任务执行完成，就向信号中心"发布"（publish）一个信号，其他任务可以向信号中心"订阅"（subscribe）这个信号，从而知道什么时候自己可以开始执行。 

[js 事件发布订阅模式范列](https://deejay0921.github.io/demos/senior/task6.js)

这种方法的性质与"事件监听"类似，但是明显优于后者。因为我们可以通过查看"消息中心"，了解存在多少信号、每个信号有多少订阅者，从而监控程序的运行

# Promise

Promises对象是CommonJS工作组提出的一种规范，目的是为异步编程提供统一接口

单说，它的思想是，每一个异步任务返回一个Promise对象，该对象有一个then方法，允许指定回调函数。比如，f1的回调函数f2,可以写成：

`　　f1().then(f2);
`

f1要进行如下改写（这里使用的是jQuery的实现)：
```
function f1(){
　　　　var dfd = $.Deferred();
　　　　setTimeout(function () {
　　　　　　// f1的任务代码
　　　　　　dfd.resolve();
　　　　}, 500);
　　　　return dfd.promise; // return出一个promise对象
　　}
```
这样写的优点在于，回调函数变成了链式写法，程序的流程可以看得很清楚，而且有一整套的配套方法，可以实现许多强大的功能。
比如，指定多个回调函数：

　　`f1().then(f2).then(f3);`

再比如，指定发生错误时的回调函数：

`　　f1().then(f2).fail(f3);`

而且，它还有一个前面三种方法都没有的好处：如果一个任务已经完成，再添加回调函数，该回调函数会立即执行。所以，你不用担心是否错过了某个事件或信号。这种方法的缺点就是编写和理解，都相对比较难。















