---
title: React Hooks的基本介绍
date: 2019/12/09 16:44:01
cover: https://showmethecode.com.br/images/posts/react-hooks/banner.png
tags: 
- 前端
- React
categories: 
- 前端
---

上篇介绍了React的基本使用，这篇博客介绍一下著名的React Hooks
<!--more-->

# Hooks简介

> 注意：React 16.8.0 是第一个支持 Hook 的版本

`Hooks `是**一些可以让你在函数组件里“钩入”` React state `及生命周期等特性的函数**。其提供了使函数式组件可以使用和`Class`组件一样的特性的方法，例如`useState`可以让函数式组件也拥有`state`等。

`Hooks`带来的好处有：
- 可以使用` Hook `从组件中提取状态逻辑，使得这些逻辑可以单独测试并复用。`Hook` 使你**在无需修改组件结构的情况下复用状态逻辑**
- `Hook `将**组件中相互关联的部分拆分成更小的函数（比如设置订阅或请求数据），而并非强制按照生命周期划分**。
- `Hook `使你**在非` class `的情况下可以使用更多的` React `特性**

## Hooks的使用原则
`Hook `就是` JavaScript `函数，但是使用它们会有两个额外的规则：

1. 只能在**函数最外层**调用` Hook`。不要在循环、条件判断或者子函数中调用。
2. 只能在` React `的**函数组件中**调用` Hook`。不要在其他` JavaScript `函数中调用。(当然自定义的`Hook`中也可以调用)


## State Hook

### State Hook的简单使用

先来看一个最简单的使用`useState()`的例子：
```
import React, {useState} from "react";

export default () => {
    const [counter,  setCounter] = useState(0); // 0代表给counter的初始值为0

    let btnClick = () => (
        setCounter(counter + 1)
    );

    return (
        <div>
            <h1>LearnHooks</h1>
            <div>current Counter: {counter}</div>
            <button onClick={btnClick}>click to plus counter</button>
        </div>
    )
}
```

上述组件如果使用`Class`组件的写法，等价于：
```
export default class LearnReact extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            counter: 0,
        }
    }

    btnClick = () => {
        this.setState((prevState) => {
            return {
                counter: prevState.counter + 1
            }
        })
    };

    render() {
        return (
            <div>
                <h1>LearnHooks</h1>
                <div>current Counter: {this.state.counter}</div>
                <button onClick={this.btnClick}>click to plus counter</button>
            </div>
        )
    }
}
```

仔细体会一下两种写法的差异和优劣性，下面来仔细分析一下`state hook`的使用:

1. `const [counter,  setCounter] = useState(0)`

    这句话定义了一个`counter`变量和一个用来修改定义的`counter`变量的方法`setCounter()`，定义的这个变量它与 `class `组件里面的` this.state `提供的功能完全相同。一般来说，在函数退出后变量就就会”消失”，**而` state `中的变量会被` React `保留**。
    
2. `useState(0)`
    
    `useState()`只接收一个参数即定义的`state`变量的初始值，可以是对象也可以是其他原始类型等等。而`Class`组件的初始值则一定是在`this.state`中的这个对象里的属性，这是一个区别点。

3. `useState()`的返回值

    经过上面的例子，我们已经可以得出，`useState`的返回值是**一个数组**，其内部元素依次为**当前` state `以及更新` state `的函数**, 每定义一个`state`都需要去成对的获取一下修改相应的`state`的方法。

### state Hook中的事件处理函数

对于上述例子，更新`state`时传入的事件处理函数，注意不能直接写成:
```
<button onClick={setCounter(counter + 1)}>click to plus counter</button> // error!
```
而是得写成回调形式，否则会直接执行一次`setCounter(counter + 1)`造成无限循环`render`:
```
<button onClick={() => setCounter(counter + 1)}>click to plus counter</button> // correct!
```

### 定义多个state

当想要定义多个`state`时，重复调用多次`useState`就行了：
```
const [age, setAge] = useState(42);
const [fruit, setFruit] = useState('banana');
const [todos, setTodos] = useState([{ text: '学习 Hook' }]);
```

但是在开发时要注意`state`的分离颗粒度。

### state hook的更新(替换而非合并)

> 值得注意的是, `useState`返回的修改`state`的方法对于`state`的修改，是**单纯的替换而不是合并**

`useState`返回的修改`state`的方法对于`state`的修改，是**单纯的替换而不是合并**，来看一个例子：

```
import React, {useState} from "react";

export default () => {
    const [obj, setObj] = useState({
        name: "Yang",
        age: 23,
        gender: "male"
    });

    let changeObj = () => (
        setObj({
            name: "Zhang"
        })
    );

    return (
        <div>
            <h1>LearnHooks</h1>
            {
                Object.keys(obj).map(key => {
                    return (
                        <div key={key}>key: {obj[key]}</div>
                    )
                })
            }
            <button onClick={changeObj}>click to changeObj</button>
        </div>
    )
}
```
上述例子中，调用`setObj({name: "Zhang"})`之后，

`obj`的值由`{name: "Yang", age: 23, gender: "male"}`直接变为了`{name: "Zhang"}`，

并没有像传统的`class`组件中调用`setState`那样对值进行合并，这一点要特别注意。

## Effect Hook

`Effect Hook`是针对于那些副作用操作（比如：**数据获取**，**设置订阅**以及手动**更改` React `组件中的` DOM`**等）而使用的。

和`class`组件做比较的话，`Effect Hook`可以视为`componentDidMount`，`componentDidUpdate `和` componentWillUnmount `这三个钩子的组合。

### 无需清除的effect的简单使用

来看一个简单的例子：
```
import React, {useState, useEffect} from "react";

export default () => {
    const [counter, setCounter] = useState(0);
    useEffect(() => {
        //    第一次渲染之后和每次更新之后都会执行
        document.title = `current Counter: ${counter}`
    });

    return (
        <div>
            <h1>LearnHooks</h1>
            <div>current Counter: {counter}</div>
            <button onClick={() => setCounter(counter + 1)}>click to plus counter</button>
        </div>
    )
}
```

仔细的来分析一下这个最简单的例子：

首先我们定义了一个无需清除的`useEffect`,其内部接收一个函数作为参数，其内部的逻辑**在默认情况下，在组件第一次渲染之后和每次更新之后都会执行**。

然后由于其定义在函数内部，所以当前函数组件的`state`和`props`我们都可以在`useEffect`内部访问到

`useEffect`传递的函数作为参数，会被称为`effect`被`React`保存起来，在这个函数内部，可以执行任意的副作用操作，**且`React`保证了每次运行` effect`的同时，`DOM `都已经更新完毕**。

> 注意：由于`useEffect`其是异步的，所以不会阻塞浏览器更新屏幕，这让你的应用看起来响应更快，但是如果需要`effect`同步执行，请使用` useLayoutEffect`

上面这样使用`useEffect`有一个潜在的好处是，开发者没必要去关心当前这个组件到底是第一次渲染还是处于更新状态

使用`Class`组件时经常会有`componentDidMount`和`componentDidUpdate`中存在相同逻辑的地方，`useEffect`使得这部分逻辑获得了简化


### 需要清除的effect

一般使用`Class`组件时，我们会在`componentDidMount`进行一些数据的订阅，在`componentWillUnmount`中取消这部分的订阅

对于这样的`effect`，我们使用`useEffect`时就要有不一样的逻辑了

