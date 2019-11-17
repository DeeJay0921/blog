---
title: 从process-nextTick(cb)-Promise说起，(js执行机制的再次理解)
date: 2017/12/29 00:00:01
cover: https://cdn.filestackcontent.com/Tmzc6mJ9Q4iUgBkI2EB7
tags: 
- 前端
- JS
- JS执行机制
- Promise
categories: 
- 前端
---
从process-nextTick(cb)-Promise说起，(js执行机制的再次理解)
<!--more-->

上次写了对JS的执行机制的一些初步理解，分为同步任务和异步任务，task queue，event loop等。
然后我看到了这么一段代码：
```
setTimeout(function() {
    console.log('setTimeout');
})

new Promise(function(resolve) {
    console.log('promise');
    resolve()
}).then(function() {
    console.log('then');
})

console.log('console');
```
最后的输出结果是我不理解的：
```
// promise
// console
// then
// setTimeout
```
那就从这开始说起
##  对同步任务和异步任务更精细的分类: macro-task和micro-task

除了广义的同步任务和异步任务，我们对任务有更精细的定义：

- macro-task(宏任务)：包括整体代码script，setTimeout，setInterval
- micro-task(微任务)：Promise，process.nextTick

不同类型的任务会进入对应的Event Queue，比如setTimeout和setInterval会进入 macro-task队列。
