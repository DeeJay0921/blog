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

对于这种元素，如果想更新的话，可以再次调用`ReactDOM.render`去进行渲染。不过对于组件内部的`state`和`props`变化，`React`会自动更新。


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
### `setState(updater[, callback])`

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

> 另外如果`setState()`了之后想获得更新后的`state`的话，也可以在`componentDidUpdate(prevState, prevProps, snapShot)`中获取到


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

<details>
    <summary>setState扩展阅读：StackOverflow: Dan谈论setState()的更新队列原则</summary>
    
    [原文链接在这](https://stackoverflow.com/questions/48563650/does-react-keep-the-order-for-state-updates/48610973#48610973)
    总结一下的他的发言：
    1. 只要是在事件处理函数中调用的setState，不管有几个组件调用了无论多少次，最后都只会合成一次更新去调用一次render
    2. 在事件处理函数中，这种队列的合并总是会按照调用的顺序来进行合并的，即对于同一属性的更新，最后一次的更新永远会覆盖前面的
    3. 到React16及之前的版本，都只有事件处理函数式按照上述原则来的，在其他场景比如Ajax请求回调中，这种维持队列更新的原则就不成立
    4. React准备在未来的React17中将这种维持队列更新的原则应用到所有地方，
    但是在这之前如果想在事件处理函数之外的地方应用，请使用ReactDOM.unstable_batchedUpdates( () => {doSth;})
</details>


另外还可以看这个[Github Issue: gaearon解释为什么state设计成异步更新](https://github.com/facebook/react/issues/11527)


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

### `getSnapshotBeforeUpdate(prevProps, prevState)`

本钩子也**不常用**。

`getSnapshotBeforeUpdate() `在最近一次渲染输出（提交到` DOM `节点）之前调用。

它使得组件能在发生更改之前从` DOM `中捕获一些信息（例如，滚动位置）。

此生命周期的任何返回值将作为参数传递给` componentDidUpdate()`(第三个参数)。

来看具体用法:
```
class SubComponent extends React.Component {
    constructor(props) {
        super(props);
        this.spanRef = React.createRef();
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        // 定义了getSnapshotBeforeUpdate之后就要定义componentDidUpdate 否则会报warning
        // 因为getSnapshotBeforeUpdate()的返回值只有在componentDidUpdate中使用
        const spanNode = this.spanRef.current;
        return spanNode.innerText; // 无需返回值时返回null即可
    }

    componentDidUpdate(prevProps, prevState, snapShot) {
        // 在这可以获得getSnapshotBeforeUpdate返回的快照 
        // 比如更新后的props.counter为5 会console snapShot props.counter: 4
        console.log("snapShot", snapShot);
    }

    render() {
        return (
            <div>
                <h2>SubComponent</h2>
                <span ref={this.spanRef}>
                    props.counter: {this.props.counter}
                </span>
            </div>
        )
    }
}
```

### 定义错误边界组件
**任意的**`React`组件，只要定义了`static getDerivedStateFromError(error)`或者`componentDidCatch(error, info) `之后，就会成为一个错误边界，其子组件如果出错的话，错误会在这个错误边界组件中被上述2个方法捕获到，所以可以做一些错误处理，比如说更换为出错提示的UI等。

在这看一个错误边界组件的例子：
```
// ErrorBoundary.js
export default class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isError: false
        }
    }

    static getDerivedStateFromError(error) {
        //    getDerivedStateFromError将抛出的错误作为参数，并返回一个值以更新 state
        console.log("getDerivedStateFromError: ",error);
        return {
            isError: true
        }
    }

    render() {
        if(this.state.isError) {
            return <h1>出错啦~</h1>
        }
        return this.props.children;
    }
}
```

有了上述的错误处理组件之后，我们可以将之前的组件作为该错误处理组件的子组件：
```
<ErrorBoundary>
    <SubComponent counter={this.state.counter}/>
</ErrorBoundary>
```

### `static getDerivedStateFromError(error)`

 `getDerivedStateFromError`将抛出的错误作为参数，并返回一个值以更新` state`,比如上文声明的错误边界组件中的：
 
 ```
 static getDerivedStateFromError(error) {
    return {
        isError: true
    }
}
 ```
 
 > `getDerivedStateFromError()` 会在**渲染阶段**调用，因此不允许出现副作用。 如遇此类情况，请改用` componentDidCatch()`。
 
 ### `componentDidCatch(error, info)`
 
 `componentDidCatch`的第二个参数是一个带错误栈信息的错误信息对象
 
 `componentDidCatch() `会在**commit阶段**被调用(commit阶段见[图示](http://projects.wojtekmaj.pl/react-lifecycle-methods-diagram/))，因此允许执行副作用。 它应该用于记录错误之类的情况

### 废弃的一些钩子

- `componentWillMount()`
- `componentWillReceiveProps(nextProps)`
- `componentWillUpdate(nextProps, nextState)`

上述钩子已经被官方废弃，虽然仍然可以使用，但是并不推荐，官方同时也给出了[替代方案](https://zh-hans.reactjs.org/blog/2018/03/27/update-on-async-rendering.html)

## `component.forceUpdate(callback)`

默认情况下，**当组件的` state `或` props `发生变化时，组件将重新渲染**。如果` render() `方法依赖于其他数据，则可以调用 `forceUpdate()` 强制让组件重新渲染。

调用` forceUpdate() `将致使组件调用` render() `方法，此操作会跳过该组件的 `shouldComponentUpdate()`。

但其子组件会触发正常的生命周期方法，包括` shouldComponentUpdate()` 方法。**这意味着如果子组件的`shouldComponentUpdate`返回false的话，子组件不会被重新渲染**。

如果标记发生变化，`React `仍将只更新` DOM`。

> 通常你应该避免使用` forceUpdate()`，尽量`在 render() `中使用` this.props `和` this.state`

## `props`

对于`FunctionComponent`来讲，`props`就是函数的参数，对于`ClassComponent`，`props`会被挂到当前组件实例上，通过`this.props`可以进行访问。

> 一样的，`props`不允许子组件对其进行修改

### `props.children`

每个组件都可以获取到` props.children`。它包含组件的开始标签和结束标签之间的内容

```
<Welcome>Hello world!</Welcome>
```

在` Welcome `组件中获取` props.children`，就可以得到字符串` Hello world!`

对于` class `组件，请使用` this.props.children `来获取

`props.children `是一个特殊的` prop`，通常由` JSX `表达式中的子组件组成，而非组件本身定义

### `Render Props`

 `render prop`是指一种在` React `组件之间使用一个值为函数的` prop `共享代码的简单技术,即传入一个函数作为`props`给其他组件：
 
 ```
 <DataProvider render={data => (
  <h1>Hello {data.target}</h1>
)}/>
 ```

### `props`添加默认值`defaultProps`

`defaultProps `可以为` Class `组件添加默认` props`。这一般用于` props `未赋值，但又不能为` null `的情况:

```
SubComponent.defaultProps = {
    counter: 0
};
```
未指定`props.counter`的时候，会按照当前给定的默认值来取值

如果你正在使用像` transform-class-properties `的` Babel `转换工具，你也可以在` React `组件类中声明` defaultProps `作为`static`属性。
```
export default class SubComponent extends React.Component {
    static defaultProps = {
        counter: 0
    };

    render() {
        return (
            <div>
                <h2>SubComponent</h2>
                <span>
                    props.counter: {this.props.counter}
                </span>
            </div>
        )
    }
}
```
### 使用` propTypes `进行类型检查

> 自` React v15.5 `起，`React.PropTypes `已移入另一个包中。请使用` prop-types `库 代替。详情请见[博客](https://zh-hans.reactjs.org/blog/2017/04/07/react-v15.5.0.html#migrating-from-reactproptypes)

详细的用法可以见[npm usage](https://www.npmjs.com/package/prop-types)

<details>
    <summary>或者点击查看: prop-types使用举例</summary>
    
    import PropTypes from 'prop-types';
    MyComponent.propTypes = {
      // 你可以将属性声明为 JS 原生类型，默认情况下
      // 这些属性都是可选的。
      optionalArray: PropTypes.array,
      optionalBool: PropTypes.bool,
      optionalFunc: PropTypes.func,
      optionalNumber: PropTypes.number,
      optionalObject: PropTypes.object,
      optionalString: PropTypes.string,
      optionalSymbol: PropTypes.symbol,
    
      // 任何可被渲染的元素（包括数字、字符串、元素或数组）
      // (或 Fragment) 也包含这些类型。
      optionalNode: PropTypes.node,
    
      // 一个 React 元素。
      optionalElement: PropTypes.element,
    
      // 一个 React 元素类型（即，MyComponent）。
      optionalElementType: PropTypes.elementType,
    
      // 你也可以声明 prop 为类的实例，这里使用
      // JS 的 instanceof 操作符。
      optionalMessage: PropTypes.instanceOf(Message),
    
      // 你可以让你的 prop 只能是特定的值，指定它为
      // 枚举类型。
      optionalEnum: PropTypes.oneOf(['News', 'Photos']),
    
      // 一个对象可以是几种类型中的任意一个类型
      optionalUnion: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.number,
        PropTypes.instanceOf(Message)
      ]),
    
      // 可以指定一个数组由某一类型的元素组成
      optionalArrayOf: PropTypes.arrayOf(PropTypes.number),
    
      // 可以指定一个对象由某一类型的值组成
      optionalObjectOf: PropTypes.objectOf(PropTypes.number),
    
      // 可以指定一个对象由特定的类型值组成
      optionalObjectWithShape: PropTypes.shape({
        color: PropTypes.string,
        fontSize: PropTypes.number
      }),
      
      // An object with warnings on extra properties
      optionalObjectWithStrictShape: PropTypes.exact({
        name: PropTypes.string,
        quantity: PropTypes.number
      }),   
    
      // 你可以在任何 PropTypes 属性后面加上 `isRequired` ，确保
      // 这个 prop 没有被提供时，会打印警告信息。
      requiredFunc: PropTypes.func.isRequired,
    
      // 任意类型的数据
      requiredAny: PropTypes.any.isRequired,

      // 你可以指定一个自定义验证器。它在验证失败时应返回一个 Error 对象。
      // 请不要使用 `console.warn` 或抛出异常，因为这在 `onOfType` 中不会起作用。
      customProp: function(props, propName, componentName) {
        if (!/matchme/.test(props[propName])) {
          return new Error(
            'Invalid prop `' + propName + '` supplied to' +
            ' `' + componentName + '`. Validation failed.'
          );
        }
      },
    
      // 你也可以提供一个自定义的 `arrayOf` 或 `objectOf` 验证器。
      // 它应该在验证失败时返回一个 Error 对象。
      // 验证器将验证数组或对象中的每个值。验证器的前两个参数
      // 第一个是数组或对象本身
      // 第二个是他们当前的键。
      customArrayProp: PropTypes.arrayOf(function(propValue, key, componentName, location, propFullName) {
        if (!/matchme/.test(propValue[key])) {
          return new Error(
            'Invalid prop `' + propFullName + '` supplied to' +
            ' `' + componentName + '`. Validation failed.'
          );
        }
      })
    };
</details>

另外可以通过对`props.children`做类型限制来限制能在本组件中只接收一个元素：

```
import Props_Types from "prop-types";

export default class SubComponent extends React.Component {
    // static defaultProps = {
    //     counter: 0
    // };

    render() {
        return (
            <div>
                <h2>SubComponent</h2>
                <div>
                    {this.props.children}
                </div>
            </div>
        )
    }
}
SubComponent.propTypes = {
    children: Props_Types.element.isRequired // 
};
```
将`children`的类型设为`Props_Types.element`即可，我们设置了上述的检查后，如果这么使用的话:
```
<SubComponent>
    <h2>children1</h2>
    <h3>children2</h3>
</SubComponent>
```
就会看到报错： 
```
Failed prop type: Invalid prop `children` of type `array` supplied to `SubComponent`, expected a single ReactElement.
```

此时如果想要允许多个元素传入，可以将类型检查改为：
```
SubComponent.propTypes = {
    children: Props_Types.arrayOf(Props_Types.element)
};
```

# 事件处理

看完了组件的相关概念，来关心一下`React`中的事件处理

`React`中的事件处理都是驼峰写法: `<button onClick={this.clickBtn}></button>`

另外由于没有类似`Vue`的修饰符，所以对于组织默认行为或者阻止冒泡等需求，需要手动去调用：

```
clickBtn = (e) => {
    e.preventDefault(); // 在这里手动调用
    e.stopPropagation();
    this.setState((state) => ({
        counter: state.counter + 1
    }));
};

render() {
    return (
        <div>
            <button onClick={this.clickBtn}>
                click to add counter
            </button>
        </div>
    )
}
```

> 事件处理函数中的`e`是一个`React`自己合成的事件对象，具体文档见 [SyntheticEvent](https://zh-hans.reactjs.org/docs/events.html)

## React事件处理函数中的`this`

需要注意的是，`JSX `回调函数中的` this`, 如果你忘记绑定` this.handleClick `并把它传入了 `onClick`，当你调用这个函数的时候` this `的值为` undefined`而非当前组件实例。

分析一下原因，为什么调用的事件处理函数中的`this`是`undefined`?

先来看一个`JS`的小例子：
```
class TestThis {
    constructor() {
        this.attr = "aaa";
    }
    consoleAttr() {
        console.log(this.attr);
    }
}

const test = new TestThis();
test.consoleAttr();// aaa
let tempMethod = test.consoleAttr;
tempMethod.bind(test)(); // aaa 硬性绑定到当前实例即可获得this
tempMethod(); // Cannot read property 'attr' of undefined 即 this 为undefined

```
上述例子中将内部的方法重新赋值再次调用之后`this`指向就改变了。


明白上面例子后，再来了解一下`JSX`中传递的是什么东西: 


> With JSX you pass a function as the event handler, rather than a string.

官方文档说的很清楚，传递的是一个`function`，
```
render() {
    return (
        <div>
            <button onClick={this.clickBtn}>
                click to add counter
            </button>
        </div>
    )
}
// 在这里的<button onClick={this.clickBtn}>其实就等价于:
render() {
    const tempClickBtn = this.clickBtn;
    return (
        <div>
            <button onClick={tempClickBtn}>
                click to add counter
            </button>
        </div>
    )
}
```
所以造成了这种你在事件处理函数中得到的`this`其实是`undefined`的情况

那么为了避免这种情况，一般采用三种方法:
1. 在`constructor`中将每一个事件处理函数手动绑定到当前实例(真的不推荐，太麻烦)
2. 使用`public class fields`语法直接声明事件处理函数即可:
    ```
    clickBtn = (e) => {
        console.log(this);
    };
    ```
3. 在回调中使用箭头函数:
    ```
    clickBtn(e) {
        console.log(this);
    };
    
    render() {
        return (
            <div>
                <button onClick={(e) => this.clickBtn(e)}>
                    click to add counter
                </button>
            </div>
        )
    }
    ```

### 事件处理函数的参数

如果要想给事件处理方法传递更多参数时，只能通过：
1. 回调函数的写法:
    ```
    clickBtn(info, e) {
        console.log(info);
        console.log(e);
    };
    <!--回调函数的 e 必须显式的传递给事件处理方法 -->
    render() {
        return (
            <div>
                <button onClick={(e) => this.clickBtn("123", e)}>
                    click to add counter
                </button>
            </div>
        )
    }
    ```
2. bind的写法:
    ```
    clickBtn(info, e) {
        console.log(info);
        console.log(e);
    };
    // bind的写法不需要显式的传递 e
    render() {
        return (
            <div>
                <button onClick={this.clickBtn.bind(this, "123")}>
                    click to add counter
                </button>
            </div>
        )
    }
    ```


# React中的条件渲染

`React`中的条件渲染都是通过`if`或者`条件运算符`进行控制的。

例如:
```
export default class LearnReact extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isShow: true
        };
    }
    render() {
        if(this.state.isShow) {
            return (
                <div>isShow = true</div>
            )
        }
        return (
            <div>isShow = false</div>
        )
    }
}
```
上述例子就通过了`state.isShow`来控制不同的元素进行渲染

此外你还可以通过变量来储存元素，这对于大段的内容来说，可以进行局部的修改:
```
export default class LearnReact extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isShow: true
        };
    }
    toggleIsShow = () => {
        this.setState((state) => {
            return {
                isShow: !state.isShow
            }
        })
    };
    render() {
        let element;
        if(this.state.isShow) {
            element = <div>isShow = true</div>;
        }else {
            element = <div>isShow = false</div>;
        }
        return (
            <div>
                {element}
                <button onClick={this.toggleIsShow}>
                    Click to toggle isShow
                </button>
            </div>
        )
    }
}
```

上述例子就是在通过变量来控制局部渲染的例子。

另外还可以通过`&&`或者三目运算符等来进行控制渲染,比如：

```
let element;
if(this.state.isShow) {
    element = <div>isShow = true</div>;
}else {
    element = <div>isShow = false</div>;
}
```
可以简化为:
```
let element = this.state.isShow ? <div>isShow = true</div> : <div>isShow = false</div>;
```
这些`JS`的常规操作都可以在`JSX`中进行。

另外值得一提的是，可以在`render`函数中`return null`来隐藏组件组织组件渲染，且**该组件的生命周期钩子依旧会被调用**：

```
export default class SubComponent extends React.Component {
    componentDidMount() {
        console.log("SubComponent Mounted");
    }

    render() {
        if(!this.props.isShow) {
            return null;
        }
        return (
            <div>
                <h2>SubComponent</h2>
            </div>
        );
    }
}
```

# React中的列表渲染

进行列表渲染，首先需要给每一个渲染项一个`key`属性，且这个`key`属性只有放在就近的数组上下文中才有意义。

具体列表渲染的方式很简单，主要是通过`map`方法直接返回一个元素数组：

```
export default class LearnReact extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            nameList: ["Yang", "Zhang", "Wang"]
        };
    }
    render() {
        const nameElements = this.state.nameList.map((e, i) => {
            return <li key={i}>{e}</li>;
        });
        return (
            <div>
                <ul>
                    {nameElements}
                </ul>
            </div>
        )
    }
}
```
对于组件也是一样：
```
render() {
    const SubComponentList = this.state.nameList.map((e, i) => {
        return (
            <SubComponent
                key={i} 
                name={e}
            >{e}</SubComponent>
        );
    });
    return (
        <div>
            <ul>
                {SubComponentList}
            </ul>
        </div>
    )
}
```

也可以嵌入在`JSX`里，但是要注意可读性：
```
render() {
    return (
        <div>
            {
                this.state.nameList.map((e, i) => {
                    return (
                        <SubComponent
                            key={i}
                            name={e}
                        >{e}</SubComponent>
                    );
                })
            }
        </div>
    )
}
```

# React中的Form

> 扩展阅读: [What you need to know about forms in React](https://goshakkk.name/on-forms-react/)

由于表单元素会有一些内部的`state`，所以`React`中对于`Form`分成了**受控表单**和**非受控表单**两类。

**当一个表单有一个`value`作为`prop`时，它就成为了一个受控组件（当然`checkbox`和`radio`对应的`prop`是`checked`）**

## 受控表单

对于受控表单，给定一个`value`作为`prop`，这个`value`应当为组件内部的`state`,然后监听其内部值发生变化时进行修改`setState()`:

拿最简单的`<input type="text" />`举个例子：

```
export default class LearnReact extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            val: "",
        };
    }
    handleChange = (e) => {
        this.setState({
            val: e.target.value.toUpperCase()
        })
    };
    render() {
        return (
            <div>
                <input type="text" value={this.state.val} onChange={this.handleChange}/>
            </div>
        )
    }
}
```

上述例子对于`input`输入做了处理，每次值变化更新`state`且转换为大写，这就是受控组件的通常用法。

对于`<input type="text">`,`<textarea>` 和` <select>` 之类的标签传入的`prop`都为`value`

而对于`<input type="checkbox" />`和 `<input type="radio" />`则为`checked`

### 处理不同的受控表单的输入

当一个组件中拥有多个受控表单的时候，需要在同一函数中对其值做处理，这时有2种情况：
1. 表单类型互不相同，比如说有`<input type="text">` `<input type="checkbox" />` 和` <select>`等：
    ```
    export default class LearnReact extends React.Component {
        constructor(props) {
            super(props);
            this.state = {
                val: "",
                selectedVal: [],
                checked: false
            };
        }

        handleChange = (e) => {
            console.log(e.target.type);
            let stateKey;
            let propKey;
            switch (e.target.type) {
                case "text": {
                    stateKey = "val";
                    propKey = "value";
                    break;
                }
                case "checkbox": {
                    stateKey = "checked";
                    propKey = "checked";
                    break;
                }
                case "select-one": {
                    stateKey = "selectedVal";
                    propKey = "value";
                    break;
                }
                case "select-multiple": {
                    stateKey = "selectedVal";
                    propKey = "value";
                    break;
                }
             }
            this.setState({
                 [stateKey]: e.target[propKey]
            },() => {
                console.log(this.state);
            })
            };

            render() {
                return (
                    <div>
                        <input type="text" value={this.state.val} onChange={this.handleChange}/>
                        <br/>
                        <input type="checkbox" checked={this.state.checked} onChange={this.handleChange}/>
                        <br/>
                        <select value={this.state.selectedVal} onChange={this.handleChange} multiple={true}>
                            <option value="grapefruit">葡萄柚</option>
                            <option value="lime">酸橙</option>
                            <option value="coconut">椰子</option>
                            <option value="mango">芒果</option>
                        </select>
                    </div>
                )
            }
    };
    ```
2. 存在相同类型的表单，比如说存在2个`<input type="text">`
