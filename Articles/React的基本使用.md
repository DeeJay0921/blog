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
