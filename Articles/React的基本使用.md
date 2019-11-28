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
## 组件生命周期

[官方图示](http://projects.wojtekmaj.pl/react-lifecycle-methods-diagram/)

图示里面很清晰，当第一次挂载时，调用的钩子为：
1. **`constructor()`**
2. `static getDerivedStateFromProps()`
3. **`render()`**
4. **`componentDidMount()`**


> `componentWillMount()`已被废弃，不建议再使用


当组件的 props 或 state 发生变化时会触发更新, 调用的钩子为：
1. `static getDerivedStateFromProps()`
2. **`shouldComponentUpdate()`**
3. **`render()`**
4. `getSnapshotBeforeUpdate()`
5. **`componentDidUpdate()`**

> `componentWillUpdate()` `componentWillReceiveProps()`已被废弃

组件卸载时，调用的钩子为:
- `componentWillUnmount()`

另外的，当渲染过程，生命周期，或子组件的构造函数中抛出错误时，会调用：
1. `static getDerivedStateFromError()`
2. **`componentDidCatch()`**
接下来介绍一些常用的钩子函数：

### `render()`

`render() `方法是 class 组件中唯一必须实现的方法。

`render() `函数应该为**纯函数**，这意味着在不修改组件 state 的情况下，每次调用时都返回相同的结果，并且它**不会直接与浏览器交互。**

如需与浏览器进行交互，请在` componentDidMount() `或其他生命周期方法中执行你的操作。保持` render()` 为纯函数，可以使组件更容易思考。

### `constructor()`

只有一个原则，在`constructor()`中只做3件事：
1. `super(props)`，否则，`this.props `在构造函数中可能会出现未定义的 bug。
2. 通过给` this.state `赋值对象来初始化内部` state`
3. 为事件处理函数绑定实例,例如：`this.btnClick = this.btnClick.bind(this)`

在`constructor()`中没有任何必要调用`setState()`，如有需要直接给`this.state`赋值即可。

另外，给`state`赋值`props`的话，只会保存`props`的初始值，如果后续`props`发生变化，其`state`中的值是**不会**随之更新的，应当避免这种使用，直接使用`this.props.xxx`即可。

具体可以看这个例子：
```
//ParentComponent
clickBtn = () => {
    this.setState((state) => ({
        counter: state.counter + 1
    }))
}
render() {
    return (
        <div>
            <SubComponent counter={this.state.counter} />

            <button onClick={this.clickBtn}>
                click to add counter
            </button>
        </div>
    )
}
```

```
// SubComponent
constructor(props) {
    super(props);
    this.state = {
        counter: props.counter
    }
}
```
在上例中，父组件触发更新时，子组件中，`props`可以正常更新，但是`state.counter`并不会更新，仅仅还是第一次的`props.counter`的值

### `componentDidMount()`

`componentDidMount() `会在组件挂载后（插入` DOM `树中）立即调用。依赖于` DOM` 节点的初始化应该放在这里。如需通过网络请求获取数据，此处是实例化请求的好地方。

另外在`componentDidMount`中调用`setState()`的话，会再次触发`render()`,可能会有性能问题，所以尽量在`constructor()`中将`state`就初始化好。

### `componentDidUpdate(prevProps, prevState, snapshot)`

`componentDidUpdate() `会在更新后会被立即调用。首次渲染**不会执行此方法**。

当组件更新后，可以在此处对` DOM `进行操作。如果你对更新前后`的 props `进行了比较，也可以选择在此处进行网络请求。（例如，当` props `未发生变化时，则不会执行网络请求）。

```
componentDidUpdate(prevProps, prevState, snapshot) {
//    比如说可以这样使用
    if (this.props.userID !== prevProps.userID) {
        this.getData(this.props.userID);
    }
}
```
另外也可以在`componentDidUpdate()`中调用`setState()`，但是要预留好出口，即**一定要有条件判断，否则就会陷入死循环**。

其第三个参数`snapshot`是钩子`getSnapshotBeforeUpdate`的返回值(如果你定义了的话，没定义就是`undefined`)

### `componentWillUnmount()`

`componentWillUnmount()` 会在组件卸载及销毁之前直接调用。在此方法中执行必要的清理操作，例如清除 `timer`，取消网络请求或清除在` componentDidMount() `中创建的订阅等

另外，不该在`componentWillUnmount()` 调用`setState()`

### `shouldComponentUpdate(nextProps, nextState)`

当 `props/state `发生变化时，`shouldComponentUpdate() `会在渲染执行之前被调用。返回值默认为` true`。**首次渲染或使用` forceUpdate() `时不会调用该方法**。

一般是不需要定义该钩子去修改默认行为的，如需做一些性能优化的话，可以考虑`React.PureComponent`而不是去改其内部逻辑。

如果无论如何都要自己实现`shouldComponentUpdate()`,可以通过`this.props `与` nextProps` 以及` this.state `与`nextState` 进行比较，并返回` false` 以告知` React `可以跳过更新。

请注意，返回` false `**并不会阻止子组件在` state `更改时重新渲染**。

> `shouldComponentUpdate()`返回`false`之后，不会去调用`render()`和`componentWillUpdate(已废弃)`以及`componentDidUpdate()`

另外官方提到说，后续版本可能就算返回了`false`,也有可能导致组件重新渲染

<details>
    <summary>扩展了解: React.PureComponent</summary>
    
    React.PureComponent和常规的React.Component的区别在于:
    
    React.PureComponent中以浅层对比prop和state的方式来实现了shouldComponentUpdate()。
    
    其内部只对对象做了浅层比较，所以涉及到state/props嵌套较深的情况的时候，尽量不要用React.PureComponent,考虑其他办法，比如forceUpdate()等
    
    总的来说，对于结构简单的state/props，使用React.PureComponent可提高性能
</details>

### `static getDerivedStateFromProps(props, state)`

`getDerivedStateFromProps `会在**调用` render`方法之前**调用，并且在初始挂载及后续更新时都会被调用。它应返回一个对象来**更新` state`**，如果返回` null `则不更新任何内容。

本钩子访问不到组件实例，且用处真的不多，不做过多了解，只需要了解到每次渲染前都会触发`static getDerivedStateFromProps()`即可。

> 一般不需要使用本钩子，常用的场景都有简单的解决方案, 见: [you-probably-dont-need-derived-state](https://zh-hans.reactjs.org/blog/2018/06/07/you-probably-dont-need-derived-state.html)

