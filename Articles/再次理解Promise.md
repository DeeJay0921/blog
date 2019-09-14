---
title: 再次理解Promise
date: 2017/12/28 00:00:01
tags: 
- 前端
- JS
- 异步
- Promise
categories: 
- 前端
---
再次理解Promise
<!--more-->

对task queue 和 Event loop了解之后，再次理解一下Promise:
## Promise是什么

console.dirPromise);(
![Promise.png](http://upload-images.jianshu.io/upload_images/7113407-f6b633a255e48a66.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


这么一看，**Promise是一个对象，更是一个构造函数**。自己身上有all、reject、resolve等等，原型上有then、catch等方法。

## promise的用法
new一个构造函数的实例出来
```
  var p1 = new Promise (function (resolve,reject) {
          //做一些异步操作
    setTimeout(function(){
        console.log('执行完成');
        resolve('随便什么数据');
    }, 2000);
  })
```

Promise的构造函数接收一个参数，是函数，并且传入两个参数：resolve，reject, resolve在fullfiled状态时会执行，reject在Promise状态为rejected执行。

这时候只是new了一个实例出来，但是代码却执行了，所以一般要将Promise实例包在一个函数中，return出来。 需要执行的时候再去调用这个函数。

 ```
        function learnPromise () {
            var p = new Promise (function (resolve,reject) {
//                异步操作
                setTimeout(function () {
                    console.log('done')
                    resolve('fulfilled')
                },300)
            })
            return p
        }
```
这个learnPromise 函数最后return出来一个promise实例，前面输出的Promise原型上的then和catch方法在实例上也可以使用。
```
        learnPromise().then((msg)=> {
            console.log('现在的Promise状态是：' + msg); // 现在的Promise状态是：fulfilled
        })
```
这个then方法可以接收俩函数作为参数，一个resolve,一个reject，接收的这俩函数的参数就是learnPromise()中的 resolve('fulfilled')中的'fulfilled',即在**learnPromise中调用resolve/reject时传入的参数**，同时这个参数在Promise规范中是有限定的，**只传一个，不然会拿到undefined**,所以有多个参数的话，考虑可以传对象。

## promise的优势

上面的then方法中的resolve,reject这些函数，就跟平常的回调函数一样，采用promise的方式，可以把回调的写法分离出来，这样异步操作执行完后，进行链式调用，能在一定程度上避免**回调地狱**。

Promise的优势在于你在then方法里仍旧可以new Promise，继续return出一个promise对象。进行下一轮的then调用:

举例说明： 
```
        var num = 0  //现在有全局变量num,然后有三个return promise对象的函数。
        function pro1 () {
            return new Promise((resolve,reject)=> {
                setTimeout(()=> {
                    num += 1
                    resolve(num)
                },1000)
            })
        }
        function pro2 () {
            return new Promise((resolve,reject)=> {
                setTimeout(()=> {
                    num += 1
                    resolve(num)
                },1000)
            })
        }
        function pro3 () {
            return new Promise((resolve,reject)=> {
                setTimeout(()=> {
                    num += 1

                    resolve(num)
                },1000)
            })
        }
```
然后我们可以这么连续的进行链式调用： 
```
        pro1()
            .then((num)=> {
                console.log('此时num为：' + num)
                return pro2() // 在每一个then方法的回调中仍然可以return promise对象，所以可以接着then下去
            })
            .then((num)=> {
                console.log('此时num为：' + num)
                return pro3()
            })
            .then((num)=> {
                console.log('此时num为：' + num)
            })
```
输出为 ：
（过1000ms）
此时num为：1
（过1000ms）
此时num为：2
（过1000ms）
此时num为：3

这样就实现了链式调用。当然可以直接return 数据而不是promise对象。在后面的then中也能接收到前一个then中return的数据。

## catch和reject

上面提到的hen方法中可以接受俩函数作为参数，那么第二个参数就是reject,并且不是必需的。

这个reject也可以写在catch方法中作为参数。其实更推荐这种写法，有个**好处是异步任务执行完成后判断为fullfiled然后执行resolve时，这时候如果发生报错（抛出异常），会转而执行catch方法**。

## all 和 race

先来说all，Promise.all()接收一个数组，其中每项都是一个promise对象。all的逻辑是，**等所有的异步操作都进行完成之后才进行执行回调**，所以then方法中的回调是所有promise对象中的异步操作都完成之后进行的。

而race恰恰相反，**最快的那个异步操作执行完成之后，就开始执行then的回调**，但是**其余没结束的异步操作还会接着执行**。

先给出三个return promise对象的函数
```
    function pro1() {
        return new Promise (function (resolve,reject) {
//            异步操作
            var num = 'a'
            setTimeout(() => {
                console.log('异步开始执行')
                resolve(num)
            },300)
        })
    }
    function pro2() {
        return new Promise (function (resolve,reject) {
//            异步操作
            var num = 'b'
            setTimeout(() => {
                console.log('异步开始执行')
                resolve(num)
            },500)
        })
    }
    function pro3() {
        return new Promise (function (resolve,reject) {
//            异步操作
            var num = 'c'
            setTimeout(() => {
                console.log('异步开始执行')
                resolve(num)
            },700)
        })
    }
```
pro1()异步操作延时为300ms，pro2()500ms，pro3()700ms
先来看Promise.all()的例子：
```
      Promise.all([pro1(),pro2(),pro3()]).then((res) => {
        console.log(res)
    })
```
执行结果为：
```
异步开始执行  // pro1
异步开始执行  // pro2
异步开始执行  // pro3
["a", "b", "c"]
```

最后all()的then方法中得到的是一个数组res，这个数组中包含了**所有异步操作运行的结果**。

再来看Promise.race()的例子：
```
    Promise.race([pro1(),pro2(),pro3()]).then((res) => {
        console.log(res)
    })
```
执行结果为： 
```
异步开始执行 //pro1
a // pro1的then中的回调执行的结果
异步开始执行 //pro2
异步开始执行 //pro3
```
race()是**最快完成的异步任务完成之后就开始执行then中的回调，但是其他没完成的异步操作还是会继续执行**。
