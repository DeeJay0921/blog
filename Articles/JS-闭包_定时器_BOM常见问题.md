---
title: JS-闭包_定时器_BOM常见问题
date: 2017/08/20 00:00:01
cover: https://www.geekstrick.com/wp-content/uploads/2017/12/Understanding-JavaScript-Closure.png
tags: 
- 前端
- JS
categories: 
- 前端
---
JS-闭包_定时器_BOM常见问题
<!--more-->


###1: 下面的代码输出多少？修改代码让 fnArr[i]() 输出 i。使用 两种以上的方法
```
    var fnArr = [];
    for (var i = 0; i < 10; i ++) {
        fnArr[i] =  function(){
    	    return i;
        };
    }
    console.log( fnArr[3]() );  //
```
输出结果为10，因为return i的i是全局中的i，循环结束i为10，不管fnArr[i]i为几都输出10。
想让fnArr[i] ()输出i，就得创建新的作用域来保存每次循环的i的值.另外需要注意，是想要**fnArr[i] ()**即调用的结果返回i，不是fnArr[i]。即fnArr[i] ()对应的是函数的返回值return i，fnArr[i]应该被赋值为一个可执行的函数。（如果是立即执行函数，必须要设置return一个函数，这个函数再return i），不然会导致fnArr[i]直接拿到i值，无法调用，调用结果为undefined（默认 return undefined）。
- 第一种方法   创建个立即执行函数的新作用域，在其作用域内将fnArr[i]赋值为函数。
```
var fnArr = [];
for (var i = 0; i < 10; i ++) {
    (function (i) { //接收每次循环的ｉ值，内部的ｉ即为这个ｉ

        fnArr[i] = function () { // 也确保了fnArr[i] 是一个可以调用的函数，不会使得fnArr[i]不经调用就直接拿到i值
            return i; // 这个i输出的是立即执行函数内部的i，即每次循环的i
        }
    })(i); // 传入每次循环的i值
}
console.log( fnArr[3]() );  //3
```

- 第二种方法： 将fnArr[i]赋值为一个立即执行函数，在立即执行函数的新的作用域内保存每次循环的i值，注意要设置返回为一个函数，再让这个函数返回i，防止fnArr[i]直接就拿到值
```
var fnArr = [];
for (var i = 0; i < 10; i ++) {
    fnArr[i] = (function (i) {
        return function () { // 要再次设置返回为一个函数，如果直接设置return i的话，fnArr[i]就直接拿到值了
            return i;
        }
    })(i);
}
console.log( fnArr[3]() );  //3
```

###2.封装一个汽车对象，可以通过如下方式获取汽车状态
```
var Car = (function(){
   var speed = 0;
   function setSpeed(s){
       speed = s
   }
   ...
   return {
      setSpeed: setSpeed,
      ...
   }
})()
Car.setSpeed(30);
Car.getSpeed(); //30
Car.accelerate();
Car.getSpeed(); //40;
Car.decelerate();
Car.decelerate();
Car.getSpeed(); //20
Car.getStatus(); // 'running';
Car.decelerate(); 
Car.decelerate();
Car.getStatus();  //'stop';
//Car.speed;  //error
```
要在外部作用域中访问内部作用域中的变量，就要用到闭包，内部作用域中的变量被其内部的作用域引用着，可以在外部作用域中访问这个引用方法来得到内部作用域的变量。
```
var Car = (function(){
    var speed = 0;
    function setSpeed(s){
        speed = s;
    }
    function getSpeed() {  // 设置闭包，内部的getSpeed作用域可以引用着外部的speed，在全局中使用这个getSpeed方法就能获取car作用域中的speed变量
        return speed;
    }
    function accelerate() {
        speed += 10;
    }
    function decelerate () {
        speed -= 10;
    }
    function getStatus () {
        if (speed === 0) {
            return 'stop';
        }
        else if (speed > 0) {
            return 'running';
        }
    }

    //因为car是个立即执行函数，执行完成之后作用域马上销毁，所以不设置return的话，无法使用内部设置的方法，所以设置返回对象。
    //外部通过返回的对象属性来找到对应的方法
    return {
        setSpeed: setSpeed,
        getSpeed: getSpeed,
        accelerate: accelerate,
        decelerate: decelerate,
        getStatus: getStatus
    }
})()

Car.setSpeed(30);
Car.getSpeed(); //30
Car.accelerate();
Car.getSpeed(); //40;
Car.decelerate();
Car.decelerate();
Car.getSpeed(); //20
Car.getStatus(); // 'running';
Car.decelerate();
Car.decelerate();
Car.getStatus();  //'stop';
Car.speed;  //error
```


###3.下面这段代码输出结果是? 为什么?
```
var a = 1;
setTimeout(function(){
    a = 2;
    console.log(a);
}, 0);
var a ;
console.log(a);
a = 3;
console.log(a);
```
输出结果依次是1, 3,2
```
var a = 1;
setTimeout(function(){
    a = 2;
    console.log(a);//2
}, 0);
var a ;
console.log(a); //1
a = 3;
console.log(a); //3
```
上述代码为每条语句运行的结果，乍一看是输出2,1,3，但是实际情况是，**setTimeout就算设置间隔为0，也是会在其他代码加载完成之后执行**。再考虑上变量提升的话，实际的代码运行顺序如下：
```
var a;
var a;
a = 1;

console.log(a); //1
a = 3;
console.log(a); //3
setTimeout(function(){
    a = 2;
    console.log(a); //2
}, 0);
```
这样一来，每条语句的输出值已经各个语句的输出顺序就很明显了。

###4.下面这段代码输出结果是? 为什么?
```
var flag = true;
setTimeout(function(){
    flag = false;
},0)
while(flag){}
console.log(flag);
```
原理和第3题是一样的，**setTimeout就算设置间隔为0，也是会在其他代码加载完成之后执行**。所以在本题中，实际的运行顺序为：
```
var flag = true;

while(flag){} //永远在循环

console.log(flag); //不会输出true

setTimeout(function(){
    flag = false;
},0)
```
所以可以很清楚的看到，声明flag为true之后，就进入了while的无限循环，不会执行下面的输出指令，也不会执行后面的setTimeout，flag的值一直为true
###5.下面这段代码输出？如何输出delayer: 0, delayer:1...（使用闭包来实现）
```
for(var i=0;i<5;i++){
	setTimeout(function(){
         console.log('delayer:' + i );
	}, 0);
	console.log(i);
}
```
分析代码：还是一样，setTimeout执行的时候for语句早就循环完毕了，首先for语句中下面的console.log(i);会依次输出0,1,2,3,4，然后才会开始执行setTimeout中的语句，setTimeout中的函数中的console.log(i)就是全局中已经循环结果的i值，为5，所以会连续输出5个delayer: 5。
想要输出delayer: 0, delayer:1...，思路就是在创建一个新的作用域保存每次循环的i值。
```
for(var i=0;i<5;i++){
        (function (i) { //创建一个新作用域，传入每次循环的i值
            setTimeout(function(){
                console.log('delayer:' + i ); //这里输出的i就是上一级作用域中即立即执行函数中的i值了，而不是全局中的i值
            }, 0);
        })(i);

        console.log(i);
    }
```

###6.如何获取元素的真实宽高
- ie8及以前版本：通过currentStyle

alert(document.getElementById('id').currentStyle.width);

- ff,safari,opera,chrome,ie9及之后版本:通过window.getComputedStyle

var el=document.getElementById('id');

alert(window.getComputedStyle(el,null).width);
###7. URL 如何编码解码？为什么要编码？
- 编码：encodeURI()  encodeURIComponent()
两种方法都不会对ASCII字母，数字，以及`~!*()'`编码，encodeURIComponent()会对`@#$&()=:/,;?+`进行编码
- 解码： decodeURI()  decodeURIComponent()
- 意义：由于URL的编码格式采用的是ASCII码而不是Unicode，因此URL中不能包含任何非ASCII字符（例如中文），否则在客户端和服务端浏览器支持的字符集不同的情况下，就可能出现问题。这也就意味着，如果URL中有汉字或其它非规定字符，就必须编码后使用。
另外，HTTP协议中通过URL传参是通过键值对形式进行的，格式上是以？、&和=为特征标识进行解析，如果键或者值的内容中包含这些符号，就会造成解析错误，所以要进行编码，用不会造成歧义的符号代替有歧义的符号。

###8.补全如下函数，判断用户的浏览器类型

```
function isAndroid(){
}
function isIphone(){
}
function isIpad(){
}
function isIOS(){
}
```
```
    function isAndroid(){
        var AndroidReg = /Android/;
        return AndroidReg.test(window.navigator.userAgent);
    }
    function isIphone(){
        var IphoneReg = /Iphone/;
        return IphoneReg.test(window.navigator.userAgent);
    }
    function isIpad(){
        var IpadReg = /Ipad/;
        return IpadReg.test(window.navigator.userAgent);
    }
    function isIOS(){
        var IOSReg = /IOS/;
        return IOSReg.test(window.navigator.userAgent);
    }
```
