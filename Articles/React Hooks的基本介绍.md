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


> `useState(initialValue)`其**是一个惰性的初始值，一旦初始化之后，后续`initialValue`就算有更新也会被忽略**


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

### state hook的更新

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

**且`State Hook`对于`state`的更新方法，也像`class`那样可以传入一个函数进行函数式更新**：

```
setCounter((prevCounter) => {
    return prevCounter + 1;
})
```

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

一般使用`Class`组件，我们需要将订阅和取消订阅操作放到2个不同的钩子函数中，但是使用`useEffect`时，这样的操作是放到一起的。

**只要在`useEffect`中`return`出一个函数后，返回的这个函数就会在执行清除操作时（`React`会在组件卸载的时候执行清除操作）调用它**，这是`useEffect`的一个可选的清除机制

所以一般需要清除的`effect`的代码大概像这样：
```
    useEffect(() => {
        //    第一次渲染之后和每次更新之后都会执行
        Api.subscribeXXX(xxx);

        return () => { //  return的函数会在React执行清除操作时调用
            Api.unsubscribeXXX(xxx);
        }
    });
```

接下来看一些`effect`常见的进阶用法

### 使用多个` Effect `将不相关的逻辑分离开

使用`Class`组件的一个不好的地方就是开发者会被迫将不相关的逻辑放到同一个钩子函数中，跟`Vue`的`options Api`以及`composition Api`是一个道理。

而`useEffect`也像`useState`一样允许开发者定义多个，可以在同一个`useEffect`中专注于同一逻辑。例如：

```
export default () => {
    const [counter, setCounter] = useState(0);
    useEffect(() => { // 这个effect 只处理counter相关逻辑
        document.title = `current Counter: ${counter}`;
    });

    useEffect(() => { // 这个effect只处理订阅逻辑
        Api.subscribeXXX(xxx);

        return () => { //  return的函数会在React执行清除操作时调用
            Api.unsubscribeXXX(xxx);
        }
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

如上例所示，我们可以根据代码的用途去定义多个`effect`

### 由于effect在每次重渲染时都会执行导致的性能问题及解决方案

我们一直在强调，`effect`在**组件第一次渲染及之后每次更新都会执行**

这样做的好处是解决了`Class`组件中经常存在的忘记在` componentDidUpdate `钩子中添加组件更新后的逻辑的问题

但是这样每次渲染后都执行清理或者执行`effect`也带来了性能问题

传统的`Class`组件可以在`componentDidUpdate`中进行对比`prevProps `或` prevState`来进行跳过执行逻辑

相应的，使用`useEffect`也有对应的功能：

```
useEffect(() => { // 这个effect 只处理counter相关逻辑
    document.title = `current Counter: ${counter}`;
}, [counter]); // 仅在 counter 更改时更新
```

我们可以通过给`useEffect`传递**一个数组**作为其第二个参数来达到效果，**如果某些特定值在两次重渲染之间没有发生变化，就可以跳过对`effect`的调用**

值得注意的是，如果数组中有多个元素，即使**只有一个元素发生变化，`React `也会执行` effect`**

如果想执行**只运行一次的` effect`（仅在组件挂载和卸载时执行）**，可以**传递一个空数组（`[]`）作为第二个参数**。这就告诉` React `你的` effect `不依赖于` props `或` state `中的任何值，所以它永远都不需要重复执行。

如果你传入了一个空数组（`[]`），`effect `内部的` props `和` state `就会一直会是其初始值。

> 另外`React `会等待浏览器完成画面渲染之后才会延迟调用` useEffect`，因此会使得额外操作很方便。

## 自定义Hook

自定义` Hook `是一个函数，其名称**必须以` use `开头**，函数内部可以调用其他的` Hook`。

每次使用自定义` Hook `时，**其中的所有` state `和副作用都是完全隔离独立的**。

来看一个使用自定义`Hook`的例子

假设现在有一个记录页面已经打开了多少秒的组件如下：

```
export default () => {
    const [seconds, setSeconds] = useState(0);
    let timer;

    useEffect(() => {
        timer = setInterval(() => {
            setSeconds(seconds + 1);
        }, 1000);

        return () => {
            console.log("Total Seconds: ", seconds);
            clearInterval(timer);
        }
    });

    return (
        <div>
            <span>页面已经渲染了{seconds}秒</span>
        </div>
    )
}
```

这时别的组件刚好也需要这个计时功能，就可以将其内部计数的逻辑单独抽出来，定义为一个自定`Hook`,比如我们定义为`useSeconds`,内部逻辑为：

```
// useSeconds.js
import {useState, useEffect} from "react";

export default function useSeconds() {
    const [seconds, setSeconds] = useState(0);
    let timer;

    useEffect(() => {
       timer = setInterval(() => {
           setSeconds(seconds + 1);
       },1000);
       return () => {
           console.log("Total Seconds: ", seconds);
           clearInterval(timer);
       }
    });

    return seconds;
}
```

此时我们就可以进行使用这个自定义`Hook`了，在原来的组件里：

```
import React from "react";
import useSeconds from "./useSeconds";

export default () => {
    const seconds = useSeconds();

    return (
        <div>
            <span>页面已经渲染了{seconds}秒</span>
        </div>
    )
}
```

在另外想复用的组件里也可以直接引入使用，且多个自定义`Hook`之间的`state`和`effect`是相互独立的。


当然由于自定义`Hook`就是一个函数，也可以通过调用使用传入参数传递信息。


从上例中我们可以看出：**自定义` Hook `解决了以前在` React `组件中无法灵活共享逻辑的问题。**

## useContext

接收一个` context `对象（`React.createContext `的返回值）并返回该` context `的当前值。该`Hook`能够读取` context `的值以及订阅` context `的变化。

来看一个简单使用的例子, 假设有如下`Theme`文件：

```
import React from "react";
const themes = {
    light: {
        foreground: "#000000",
        background: "#eeeeee"
    },
    dark: {
        foreground: "#ffffff",
        background: "#222222"
    }
};
const ThemeContext = React.createContext(themes.light);
export  {
    themes,
    ThemeContext,
}
```

此时在`<App />`中应用这个`Context`:
```
import React, {useState, useEffect} from 'react';
import LearnHooks from "./components/LearnHooks";
import { themes, ThemeContext } from "./components/Theme";

function App() {
    const [theme, setTheme] = useState(themes.dark);
    
    useEffect(() => {
        setTimeout(() => {
            setTheme(themes.light); // 3s后将主题改为白色
        }, 3000)
    });

    return (
        <div id="app">
            <ThemeContext.Provider value={theme}>
                <LearnHooks/>
            </ThemeContext.Provider>
        </div>
    );
}

export default App;

```
此时在我们的目标组件中，就可以进行使用`useContext`来进行获取`Context`了：

```
import React, {useContext} from "react";
import { ThemeContext } from "./Theme";

export default () => {
    const theme = useContext(ThemeContext); // 获取theme

    return (
        <p style={{ background: theme.background, color: theme.foreground }}>
            normal Text
        </p>
    )
}
```

## useReducer

用法:

```
const [state, dispatch] = useReducer(reducer, initialArg, init);
```

在 `state `逻辑较复杂且包含多个子值，或者下一个` state `依赖于之前的` state `等场景下，可以用来代替`useState()`,同时`useReducer`的优势在于还会对深层次组件更新做优化。

`useReducer`最多可以接收三个参数：
- 第一个参数`reducer`是`(state, action) => newState `类型的函数
- 第二个参数如果在第三个参数未传的情况下，是直接作为`state`的初始值的，但是如果传入了第三个参数，那么初始值为`init(initialArg)`
- 第三个参数是可选的一个函数，参数为`initialArg`, 返回`state`的初始值（传入`init`时为惰性的初始化`state`）。

看一个基本使用的例子：
```
import React, {useReducer} from "react";

function reducer(state, action) {
    switch (action.type) {
        case 'increment':
            return {count: state.count + 1};
        case 'decrement':
            return {count: state.count - 1};
        default:
            throw new Error();
    }
}

function init(initialCount) {
    return {count: initialCount};
}

export default () => {
    const [state, dispatch] = useReducer(reducer, 0, init);

    return (
        <div>
            Count: {state.count}
            <button onClick={() => dispatch({type: 'decrement'})}>-</button>
            <button onClick={() => dispatch({type: 'increment'})}>+</button>
        </div>
    )
}
```

## `useCallback` 和 `useMemo`

这2个`hook`都是作为性能优化手段来使用的，也能使用其特性达成一些特殊用途。且`useMemo`可以实现`useCallback`


相关用法：
```
// useCallback:
const memoizedCallback = useCallback(
  () => {
    doSomething(a, b);
  },
  [a, b],
);

// useMemo:
const memoizedValue = useMemo(() => computeExpensiveValue(a, b), [a, b]);
```

> `useCallback(fn, deps) `相当于` useMemo(() => fn, deps)`。

2者都是返回一个`memoized`过后的函数/值，第二个参数和`useEffect`类似为依赖项，如果依赖项有改变的话，`memoized`的值或者函数才会得到更新。

如果依赖传入一个`[]`或者依赖未发生改变的话，其`memoized`的函数或者值的引用地址是不会改变的（同一块内存区域），利用这一特性，可以配合类似于`shouldComponentUpdate`的机制来进行避免重复渲染。

### 使用`useCallback`和`useMemo`的场景举例

了解了基本概念之后，[这篇文章](https://juejin.im/post/5db14bf2e51d452a161df297)举了个例子展示了`useCallback`及`useMemo`的使用。

大概例子是有这么一个防抖函数，在鼠标滑动的时候去触发：
```
// generateDebounce.js

function generateDebounce(func, delay=1000) {
    let timer;
    function debounce(...args) {
        debounce.cancel();
        timer = setTimeout(() => {
            console.count("func called");
            func.apply(this, args);
        }, delay);
    }

    debounce.cancel = function () {
        if (timer !== undefined) {
            clearTimeout(timer);
            timer = undefined;
        }
    };
    return debounce;
}
```
这个函数调用之后返回一个防抖函数`debounce`，然后在如下组件中进行防抖使用：

```
import React, {useState} from "react";
import generateDebounce from "./generateDebounce";

export default () => {
    const [count, setCount] = useState(0);
    const [bounceCount, setBounceCount] = useState(0);
    const debounceSetCount = generateDebounce(setBounceCount); // 每次更新渲染都会重新创建一个debounceSetCount

    const handleMouseMove = () => {
        setCount(count + 1);
        debounceSetCount(bounceCount + 1);
    };

    return (
        <div onMouseMove={handleMouseMove}>
            <p>普通移动次数: {count}</p>
            <p>防抖处理后移动次数: {bounceCount}</p>
        </div>
    )
}
```
在上述例子中，我们可以看到，虽然`bounceCount`增加的不多，但是其实内部的`console.count("func called");`执行的次数和未做防抖的次数`count`是一样的

也就是说并没有达到防抖的效果，造成这个现象的原因是：

每次执行`onMouseMove`都会导致组件的重新渲染，整个函数组件将会被重新执行

即意味着`const debounceSetCount = generateDebounce(setBounceCount);`这句每次都会执行，会创建很多个新的`debounceSetCount`，所以其不同的`debounce`其实是使用很多个不同的`timer`,这就造成了我们看到的调用次数并没有减少的情况

但是`bounceCount`增加的并没有像`count`那么快的原因就是在执行`onMouseMove`时疯狂的传入了很多次一样的参数，而在异步函数中执行增加操作时，其实都是一个相同的值在加一，所以`bounceCount`没有增加到函数调用次数那么大，但是本质上，函数还是调用了很多次的。

### 使用`useCallback`举例

花了这么多篇幅讲通这个例子的原路，现在来看怎么修复，我们通过`useCallback`创建一个`memoized`函数，依赖为`[]`, 这样一来，我们创建的这个`debounceSetCount`函数的引用就一直是同一个地址，这样就组件每次更新时，由于依赖为`[]`，函数一直不会更新，永远为同一个函数，即可达到效果

```
export default () => {
    const [count, setCount] = useState(0);
    const [bounceCount, setBounceCount] = useState(0);
    // const debounceSetCount = generateDebounce(setBounceCount);
    // 改用callback创建一个 memoized 函数，依赖为[]即永远保存同一块内存中的这个 debounceSetCount 函数
    const debounceSetCount = useCallback(generateDebounce(setBounceCount), []);
    
    // 省略下面代码。。。。
}
```

### 使用`useMemo`举例

上面例子中，也可以直接使用`useMemo`:

```
    // const debounceSetCount = generateDebounce(setBounceCount);
    // const debounceSetCount = useCallback(generateDebounce(setBounceCount), []);
    const debounceSetCount = useMemo(() => generateDebounce(setBounceCount), []);
```

达到的效果是一样的。也能创建一个唯一的`debounceSetCount`函数

关于`useMemo`,官方建议我们，**先不要使用`useMemo`编写可用的代码，然后再引入`useMemo`仅仅作为性能优化的手段**，因为官方说了`useMemo`不一定能作为一个保证来使用

> 关于`useMemo`引自文档： **You may rely on useMemo as a performance optimization, not as a semantic guarantee.** In the future, React may choose to “forget” some previously memoized values and recalculate them on next render, e.g. to free memory for offscreen components. 

## useRef

用法：
```
const ref = useRef(initialValue);
```

和`Class`组件一样，`useRef`提供了在函数组件中使用`ref`的方法，其参数`initialValue`为给`ref`设置的初始值，该值在`useEffect`之中就已经被重新赋值为目标`DOM`

来看使用例子：
```
import React, {useRef, useEffect} from "react";


export default () => {
    const testRef = useRef(null); // 给null作为初始值
    console.log(testRef); // {current: null}

    useEffect(() => {
        console.log(testRef); // 输出 {current: div}
    });

    return (
        <div ref={testRef}>
            normal Text
        </div>
    )
}
```

> 当` ref `对象内容发生变化时，`useRef `并不会通知更新。且变更` .current `属性也不会引发组件重新渲染。


如果想要在` React `绑定或解绑` DOM `节点的` ref` 时运行某些代码，则需要使用回调` ref `来实现。

来看一个不使用`useRef`而是使用回调`ref`的例子：

```
export default () => {
    const [isShow, setIsShow] = useState(true);
    const callbackRef = useCallback((domNode) => {
        console.log(domNode); // 在ref附加到节点上时自动调用  在节点卸载时也会自动调用 输出null
    }, []);

    return (
        <React.Fragment>
            {
                isShow &&
                <h1 ref={callbackRef}>
                    <div>Hello, ref</div>
                </h1>
            }
            <button onClick={() => (setIsShow(false))}>click</button>
        </React.Fragment>
    )
}
```

我们分析下上述例子：使用`useCallback`声明一个`callbackRef`，传入的依赖为`[]`,所以其`ref`不会在组件重新渲染时改变。

**使用回调`ref`的优点是，节点发生变化的时候，会自动调用目标回调**，而使用`useRef`时，节点对象发生变化时，`useRef`并不会通知你（当然可以手动写一个`useEffect`去主动获取`ref`对象，是可以拿到最新的对象的）

## useImperativeHandle

用法：
```
useImperativeHandle(ref, createHandle, [deps])
```

`useImperativeHandle`是和`forwardRef`搭配使用实现`refs`转发的,来看使用例子，现有父组件：

```
export default () => {
    const supRef = useRef(null);

    useEffect(() => {
        console.log(supRef);
        supRef.current.focus();
    });

    return (
        <ImperativeHandle ref={supRef} />
    )
}
```
而子组件里的逻辑为：
```
// ImperativeHandle.js
import React, {useRef, useImperativeHandle} from "react";

export default React.forwardRef((props, ref) => {
    const subRef = useRef(null);

    useImperativeHandle(ref, () => subRef.current);

    return <input ref={subRef} />;
})
```

而`useImperativeHandle`的功能在于，**在使用` ref `时自定义暴露给父组件的实例值**,上述例子中我们通过使用：
```
useImperativeHandle(ref, () => subRef.current);
```
直接暴露出了整个`subRef.current`，我们可以自定义决定暴露出什么，比如我们改为暴露一个`subFocus`方法而不是暴露整个`subRef`:

```
// ImperativeHandle.js
    <!--省略其他代码-->
    useImperativeHandle(ref, () => { // 可以自定义决定暴露什么内容给父组件
        return {
            subFocus: () => {
                subRef.current.focus();
            }
        }
    });
```
在父组件中获取到的`supRef.current`也发生了相应的改变：
```
export default () => {
    const supRef = useRef(null);

    useEffect(() => {
        // 在这获取到的supRef.current 就是子组件通过 useImperativeHandle 自定义暴露出的内容
        supRef.current.subFocus(); // 调用暴露出的subFocus()
    });

    return (
        <ImperativeHandle ref={supRef} />
    )
}
```

