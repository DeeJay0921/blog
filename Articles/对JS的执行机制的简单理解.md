---
title: 对JS的执行机制的简单理解
date: 2017/12/27 00:00:01
cover: https://john-dugan.com/wp-content/uploads/2018/05/event-loop.png
tags: 
- 前端
- JS
- JS执行机制
categories: 
- 前端
---
对JS的执行机制的简单理解
<!--more-->

## 一，JS是单线程
JS从一开始设计就是单线程，这样可以避免一些复杂的同步问题。

HTML 5提出了web worker标准，可以允许JS创建多个线程，但是**子线程完全受主线程控制，且不得操作DOM**。**JS的单线程本质并没有发生改变**（也不可能改变）。
## 二，task queue
单线程的缺点是，所有任务都要排队，前一个任务执行完，后一个才能进行执行。这样带来的弊端就是，如果前一个任务耗时很长，并且不是因为cpu计算复杂等的原因引起的（比如ajax去请求数据），那么这时傻傻等着前一个任务执行完就很不合适。
为此，JS中的任务分为**同步任务**（synchronous）和**异步任务**（asynchronous）：
- 同步任务：  指的是在主线程上排队执行的任务，前一个任务执行完，后一个才能执行。
- 异步任务： 指的是 **不进入主线程，而是进入任务队列（task queue），当task queue中的某个任务执行完成了，才通知主线程，该任务才会进入主线程**。

#### ！具体的执行机制！
1. 所有的**同步任务**，都在主线程上执行，形成一个execution context task(执行栈)；
2. 主线程之外，还有一个**task queue**，只要**异步任务**执行完，就**在task queue中放置一个事件**；
3. 一旦execution context task中的同步任务执行完，就去**查看task queue中的事件**。找到**该事件（可以是多个）对应的异步任务**，这些**异步任务就进入execution context task**，开始进行执行。
4. 主线程**循环重复查看task queue**，有事件就再去找到对应的异步任务，加到执行栈中执行。只要**主线程空了，就去查看task queue找事件对应的异步任务进行执行**，不断循环。

![执行机制示意图](http://upload-images.jianshu.io/upload_images/7113407-3fbfb0b4ed2ecad1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 三，事件和回调函数
首先明确一点，**异步任务必须指定回调函数**，回调函数就是会被主线程挂起来暂缓执行的代码。例如监听click,scroll这些事件时，要设置相应的回调函数，ajax请求数据的时候，也会设置相应的回调。当**主线程开始执行异步任务的时候，就是执行对应的回调函数。**

task queue是一个**事件的队列**，设备每完成一个**异步任务**，就像task queue中添加一个事件，表示这个事件对应的异步任务可以进入执行栈进行执行了。

并且这个task queue是一个**先进先出**的数据结构，排在前面的事件，优先被主线程读取。只要主线程执行栈一为空，task queue中第一位事件就自动进入执行栈。但是因为setTimeOut等，主线程要先检查一下执行时间，某些事件要到了规定时间才能开始执行（即进入主线程执行栈）。

task queue中的事件，除了设备的事件外，也包括用户产生的事件（例如click,scroll）这种，所有的异步任务，都有对应的回调函数，这些事件发生后，这些事件就进入task queue，然后等待主线程读取。当主线程开始执行的时候，就执行这些事件对应的回调函数。
## 四，！Event Loop!

上面提到的主线程从task queue读取事件，执行其对应的异步任务这个过程是不断循环的，所以这个运行机制被称为 **Event Loop**.

![event loop 图解](http://upload-images.jianshu.io/upload_images/7113407-d1a7e2af0af40636.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在主线程运行的时候，产生了heap和stack,其中stack（栈）中的代码调用各种webAPI（比如click,scroll,ajax）这些操作，在执行的时候，将相应的事件加入task queue中，当stack中的代码执行完之后，就会去task queue中读取事件，依次执行这些事件对应的回调函数。

另外，**执行栈中的代码（同步任务）总是先于task queue的代码（异步任务）。**

## 五，对setTimeout/setInterval的理解

这俩运行机制都是一样的，task queue除了可以放置异步任务对应的事件，还能放置定时事件，即规定某些代码（setTimeout的回调）在多少时间后进行。

###### setTimeout(callback,0)
这个就是制定cb在**主线程最早空闲的时候运行**。意味着，要**等主线程所有的同步任务执行完，然后task queue中前面的事件对应的异步任务也执行完之后才执行**。

setTimeout做的事就把这个定时事件插入了task queue，执行要等主线程的执行栈清空才回去读取事件，所以如果前面的代码执行很耗时的话，setTimeout指定的cb不会在规定的时间执行。
