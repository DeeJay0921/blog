---
title: 从Python中的self说起（理解this与箭头函数）
date: 2018/02/13 00:00:01
tags: 
- 前端
- JS
- this
categories: 
- 前端
---
从Python中的self说起（理解this与箭头函数）
<!--more-->

先来看一段代码：
```
  class testThis:
    name = 'DeeJay'
    def sayName(self):
        print (self.name)
  x = testThis()
  x.sayName()
```
这段python代码中，testThis是一个类，拥有name属性和sayName()方法。

sayName()方法在调用的时候，输出了testThis类的name，我们注意到python中方法的第一个参数是self,这个self就类似于JS中的this，不同的地方在于：**python中的self是显式的写在了参数列表当中，而JS缺在参数列表中隐藏掉了this。**

###怎么理解this
this可以理解为就是一个普通的参数，类似于arguments[-1]（当然并不存在）。
那么在函数调用的时候，就会传一个this给当前被调用函数。写一段类似代码：
```
let object = {
  name: 'DeeJay',
  sayName: function () {
    console.log(this.name)
  }
}
object.sayName()
```
我们可以看到区别。JS中隐藏了像sayName方法传递this的过程。其实在调用sayName的时候，sayName内部的this就会传达。我们使用call方法，就可以显示的写出传递的this,比如上述例子写成`object.sayName.call(object)`,这么写就**可以理解this是在调用的时候确定的了**。

对于如何确定this的值，所有的function调用都转换为call写法，就很清晰明了。

##对于箭头函数

上述说的情况都是针对写了function关键字的情况，**在JS中，只要你写下了一个function，那么这个方法在被调用的时候一定是有this的**。

箭头函数的出现，就是为了弱化this，箭头函数本身很纯粹，没有this的说法（被调用的时候，没有传this的这种操作），所以如果被调用的时候，内部有this操作，就会寻求上一作用域中的this。

由于箭头函数没有this，被调用时也没有传递this的操作，所以箭头函数中的this不是被调用的时候确定的，而是写下代码的时候(函数定义的时候)就已经确定了。

基于上述内容，想在箭头函数中使用this的话，最好显示的规定一个this，传入想要的this进行调用。
```
let obj = {
  name: 'DeeJay',
  say: (self)=> {
    console.log(self.name)
  }
}
obj.say(obj) // 显式的传入想要的context
```
