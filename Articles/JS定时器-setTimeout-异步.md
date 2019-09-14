---
title: JS定时器-setTimeout-异步
date: 2017/08/19 00:00:01
tags: 
- 前端
- JS
- 异步
categories: 
- 前端
---
JS定时器-setTimeout-异步
<!--more-->

[异步  参考文章](https://zhuanlan.zhihu.com/p/22685960)

- 函数调用和函数的区别
```
// 函数和函数调用的区别

function fn () { //函数
    return 1;
}

fn(); //函数调用
console.log(fn); // ƒ fn() {return 1;}
console.log(fn()); //1

// 函数返回函数的时候调用当前函数即为return的那个函数
function fn2 () {
    return function () {
        return 2;
    }
}
fn2//是一个函数
fn2()//也是一个函数 function () { return 2; }
fn2()(); //2
```

###　定时器
- 先看一个例子
```
function fn (){
    console.log(1);
}
setTimeout(fn);
console.log(2);
//上述代码输出 2 1，先输出2后输出1
```
setTimeout设定了一个输出1的事件在0秒后执行，那么浏览器会先将目前的代码都执行完成后再回头执行setTimeout中的代码
```
function fn (){
    console.log(1);
}
var timeId = setTimeout(fn);
console.log('id:' + timeId);//1
console.log(2);
timeId = setTimeout(fn);
console.log('id:' + timeId);//2

//每次要执行setTimeout的函数的时候，就要销毁这个setTimeout，然后下次在定义个相同的setTimeout的时候，id是不一样的。
```

- 间隔永远要比要比设置的久
```
function fn (){
    console.log(1);
    console.log(new Date()-0);
}
setTimeout(fn);
console.log(new Date()-0);
// 设置了时间延时为0，但是输出的date还是有几十毫秒的差别，这是因为浏览器就算是延时为0，也要先将当前页面执行完，等‘闲下来’的时候再来执行setTimeout
// 所以这样造成的结果是，间隔永远要比要比设置的久
```

- 定时器的‘怠惰’问题
当浏览器失去焦点的时候，即用户不在关注浏览器的时候，定时器的输出会变慢，比如设置0.5秒执行一次，有可能最小化浏览器之后1秒才执行一次这么做是为了节省内存。

- 关于setInterval
setInteval只有一个timerId,和setTimeout不同

- setTimeout的函数的传参
```
    setTimeout(fn,500,1,2,3);
    function fn () {
        console.log(arguments); //[1,2,3]
    }
```

- 怎么取消定时器 **window.clearTimeout(timerId)**
```
    var timerId = setTimeout('console.log("设置了clearTimeout,这条会被阻止执行")',3000);
    window.clearTimeout(timerId); //阻止前面的setTimeout执行
    var timerId2 = setTimeout('console.log("没设置clearTimeout,这条会被执行")',3000);
```

### 异步 和 回调
看下面两个例子，异步就是不等结果，去执行别的代码，让一个回调函数来通知我结果
```
    function queue () {
        setTimeout(function () {
            return "您的号码是233";
        },60000); //60s之后才能拿到结果
    };

    var result = queue();
    console.log(result); //undefined 因为queue这个函数没有写return，所以默认返回undefined  拿不到想要的匿名函数的返回值
```

这时候想要拿到匿名函数的返回值,可以创建一个对象或者函数,传入queue中，queue中的匿名函数不用设置return，因为设置了也拿不到 然后不设置return，直接让这个创建的代理函数获取到值，就可以输出.
```
    function queue (agent) {　 //传入agent给形参
        setTimeout(function () {
            agent("您的号码是233"); // 匿名函数里不写return，写了也拿不到，直接用agent函数获取这个值
        },3000); //60s之后才能拿到结果
    };

//    这时候想要拿到匿名函数的返回值,可以创建一个对象或者函数,传入queue中，queue中的匿名函数不用设置return，因为设置了也拿不到
//    然后不设置return，直接让这个创建的代理函数获取到值，就可以输出
    function agent (result) { //创建一个函数
        console.log(result);
    }
    queue(agent); //将创建的函数传入queue
```
整体思路是，程序还要接着执行下面的，创建一个代理函数去得到值，等setTimeout的时间间隔到了之后拿到值之后返回给程序。

在上述例子中，**agent函数就是一个回调函数**。由全局传递给queue，然后queue调用之后又传回给全局。叫做回调函数。 **“我传给你，你调用再传回给我”**

**异步和回调往往是同时出现的。**因为异步了之后不回调，就相当于不要这个返回结果了。

### 相关的一些问题分析

```
for (var i = 0; i < 5; i ++) {
    console.log(i); //0 1 2 3 4
}

```
```
for (var i = 0; i < 5; i ++) {
    setTimeout(function () {  //for循环线结束，才运行setTimeout  这时候i的值都是5
        console.log(i);
    },1000*i);
}
```
第一次马上输出5  第二次是1000毫秒之后输出5 第三次是2000毫秒之后输出5 以此类推到第五次,j即每一秒打印一个
**setTimeout内部匿名函数要输出i时，找不到i，要到上一级也就是全局中找i，这时候i已经为5了**，而外部的时间间隔i跟全局的i是同一个东西，每次的值为0,1,2,3,4
```
for (var i = 0; i < 5; i ++) {
    (function () {
        setTimeout(function () {
            console.log(i);
        },i * 1000);
    })(i);
}
```
和第二题的结果一样，1000乘i的那个i是是全局中的i，每次为0,1,2,3,4

而setTimeout里面的函数的作用域中要输出的i不存在，要**到上一级中作用域即立即执行函数中找i，立即执行函数中也没有i**，再到全局作用域中找i，这时候for循环已经结束了，i为5，所以每次输出5,
```
for (var i = 0; i < 5; i ++) {
    (function (i) {
        setTimeout(function () {
            console.log(i);
        },i * 1000);
    })(i);
}
```
和第3题不同，这里立即执行函数写了一个形参，这个形参相当于一个变量声明var i = arguments[0];

那么setTimeout里面的函数在输出i时，找不到i，到上一级作用域即立即执行函数中去找，和第3题不同的是**这次立即执行函数里面有i了**，**不会再到全局作用域里找i了**。

写了形参就相当于定义了i，所以这时候输出的i是立即执行函数中的i，这个i传入的值就是当时的全局中i的值0,1,2,3,4

```
for (var i = 0; i < 5; i ++) {
    setTimeout((function (i) {
        console.log(i);
    })(i),i * 1000)
}
```
第5题仍然通过作用域链进行理解，setTimeout里面的函数的作用域中**要输出的i，就是立即执行函数中的i**，因为定义了形参，这个形参每次接收的i是**当时全局中的i的值为0,1,2,3,4**. 而时间间隔的i就是全局中的i，每次的值为0,1,2,3,4。
求得结果之后，针对第5题，我们可以简化一下代码，仔细分析一下
```
    for (var i = 0; i < 5; i ++) {
        var t1 = function (i) {
            console.log(i);
        };
        var t2 = t1(i);
        setTimeout(t2,1000*i);
    }

```
那么把代码拆开之后我们再来分析，此时setTimeout里面执行的是t2，t2是t1调用的结果，t1里面没有写return，那么默认返回undefined，所以t2为undefined，此时其实就相当于：
```
    setTimeout(undefined,0);
    setTimeout(undefined,1000);
    setTimeout(undefined,2000);
    setTimeout(undefined,3000);
    setTimeout(undefined,4000);
```
而输出的值是t1中的console.log(i);我们也可以很清晰的看到，这个i就是传入的当时的每次的i值0,1,2,3,4

### setTimeout的应用：做个倒计时器

```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>


<select placeholder="选择一个时间" name="" id="mySelect">
    <option value="1" selected>1分钟</option>
    <option value="5">5分钟</option>
    <option value="10">10分钟</option>
    <option value="20">20分钟</option>
</select>
<button id="start">开始计时</button>
<button id="pause" disabled>暂停计时</button>
<button id="resume" disabled>恢复计时</button>

<div id="show"></div>


<script>

    var show = document.querySelector('#show');
    var start = document.querySelector('#start');
    var pause = document.querySelector('#pause');
    var resume = document.querySelector('#resume');
    var select = document.querySelector('#mySelect');


//    var timeLeft;
//    function showtime () {
//        show.innerText = timeLeft + '秒';
//        timeLeft -= 1;
//        if (timeLeft === 0) {
//            return;
//        }
//        setTimeout(showtime,1000);
//    }
//    start.addEventListener('click',function () {
//        var seconds = 60 * parseInt(select.value);
//        timeLeft = seconds;
//        showtime();
//    });
// 上面的代码能实现效果但是存在bug，连续点击start倒计时会越来越快，有多个setTimeout在控制计时
// 所以要解决这个问题，在再一次点击start的时候，要将上一次的setTimeout清除掉,怎么得到上一次的setTimeout呢，可以通过设置timerId

    var timeLeft;
    var lastTimerId; // 全局中定义个lastTinmerId
    function showtime () {
        show.innerText = timeLeft + '秒';
        timeLeft -= 1;
        if (timeLeft === 0) {
            return;
        }
        lastTimerId = setTimeout(showtime,1000); //执行seTimeout时顺便给timerId赋值
    }
    start.addEventListener('click',function () {
        var seconds = 60 * parseInt(select.value);
        timeLeft = seconds;
//        执行函数前先清除上一次的setTimeout
        if (lastTimerId !== undefined) {
            window.clearTimeout(lastTimerId);
        }
        showtime();
        pause.disabled = false; //点了start才能点击pause
    });

//    接下来设置暂停和恢复
//    暂停的思路很简单,恢复的思路就是不要重新赋值timeLeft,保持暂停的时候的timeLeft就可以
    pause.addEventListener('click',function () {
        if (lastTimerId) {
            window.clearTimeout(lastTimerId);
        }
        resume.disabled = false; //点了pause才能点resume
        pause.disabled = true; //点了pause之后不能再点击pause
    })
    resume.addEventListener('click',function () {
        showtime(); //就一行代码， showTime()就是把当前的timeLeft打出来，并没有给timeLeft重新赋值
        pause.disabled = false; //点了resume之后就可以点pause
        resume.disabled = true; //点了resume之后，不可以继续点击resume
    })
</script>
</body>
</html>
```
