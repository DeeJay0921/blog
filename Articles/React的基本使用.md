---
title: React的基本使用
date: 2019/11/26 14:24:01
cover: https://blog.dashlane.com/wp-content/uploads/2016/02/react-logo-2.png
tags: 
- 前端
- React
categories: 
- 前端
---

最近准备好好从头开始系统的复习和学习一波React相关技术，本文作为第一篇，先来回顾一波React的基本使用
<!--more-->

# JSX

可以将JSX作为看做一个普通的表达式，其内部可以进行标签属性的绑定，事件的监听等操作。

例如：

```
const element = <img src={user.avatarUrl}></img>;
```

> 因为 `JSX` 语法上更接近` JavaScript` 而不是` HTML`，所以` React DOM `使用 `camelCase`（小驼峰命名）来定义属性的名称，而不使用`HTML` 属性名称的命名约定。
例如，`JSX` 里的 `class` 变成了 `className`，而` tabindex `则变为` tabIndex`。

另外值得一提的是，`React DOM `在渲染所有输入内容之前，默认会进行转义,可以有效的防止XSS注入。

`JSX`的写法在编译时其实会被转换为`React.createElement()`创建的对象,比如代码里面写成:
```
const element = (
  <h1 className="greeting">
    Hello, world!
  </h1>
);
```
就完全等价于：
```
const element = React.createElement(
  'h1',
  {className: 'greeting'},
  'Hello, world!'
);
```
这个函数创建了一个包含标签类型属性等信息的一个对象。

# 元素渲染

`React`是通过`ReactDOM.render`方法将其目标`React`元素挂载到`DOM`节点上的:

```
const element = <h1>Hello, world</h1>;
ReactDOM.render(element, document.getElementById('root'));
```

另外数据变化有涉及到视图的更新的时候，需要手动去`ReactDOM.render`方法（这点不像`Vue`是`MVVM`，可以直接驱动视图更新）

# 组件

`React`中的组件分为`Function Component`和`Class Component`,其中`Class Component`有一些额外的特性，之后的例子基本都采用这种写法

## `state`

组件内部有一些自己维护的数据状态，就可以使用`state`。`state`的初始化操作一般放到`class`的`constructor`中:
```
import React from "react";
class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}; // 初始化操作放在这里
    }
}
```
### `setState`

对于修改`state`的操作，`React`提供了`setState()`方法，这个方法是合并修改的，即`state`有多个属性如:
```
this.state = {
    name: "yang",
    age: 23
}
```
我们调用:
```
this.setState({
    name: "zhang"
})
```
之后，其`name`属性的改动会更新到`state`上，但是不会对`state`的`age`属性去做修改.
但是`setState`对于`state`的修改是一个异步的操作，其内部会对多个相同的`state`操作进行合并操作，所以调用`setState`之后如果马上去使用`state`的话，其内部的值是没更新的，见下例：
```
constructor(props) {
    super(props);
    this.state = {
        name: "Yang",
        age: 23,
    }
}
clickBtn = () => {
    this.setState({
        name: "zhang",
    });
    console.log(this.state); // {name: "Yang", age: 23} 在这state并没有同步更新
}
```

对于这种情况，`setState`方法可以传入第二个参数作为`callback`，其回调函数内部可以获得同步修改之后的值:
```
clickBtn = (e) => {
    this.setState({
        name: "Zhang"
    }, () => {
        console.log(this.state);// {name: "Zhang", age: 23}
    });
}
```

除此之外还会有一种情况，比方说我频繁的去调用`setState`，且每次的`state`的值的改动会依赖上一次的`state`的值，这种情况下，普通的调用`setState`并不会像同步的那样去更新`state`的值：
```
constructor(props) {
    super(props);
    this.state = {
        counter: 0
    }
}

clickBtn = () => {
    for (let i = 0; i < 5; i ++) {
        this.setState({
            counter: this.state.counter + 1
        }, () => {
            console.log(this.state); // console 5次 {counter: 1}
        });
    }
}
```
如上例所示，这样调用`setState`的话，`state`上一次的改动是异步操作，所以几次循环的`counter`值都为0，最后得到的`counter`为1。

为了针对上述这种情况，`setState`方法的第一个参数也可以传入一个函数，其函数的参数为`(state, props)`，即用上一个 `state `作为第一个参数，将此次更新被应用时的` props `做为第二个参数

那么我们做如下改动:
```
clickBtn = () => {
    for (let i = 0; i < 5; i ++) {
        this.setState((state, props) => {
            return {
                counter: state.counter + 1
            }
        }, () => {
            console.log(this.state);  // console 5次 {counter: 5}
        })
    }
}
```
即可达到效果，此时`counter`即为5了
