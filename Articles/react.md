---
title: react
date: 2018/02/16 00:00:01
tags: 
- 前端
- JS
- React
categories: 
- 前端
---
初步理解react
<!--more-->

webpack --help -p :
`
               shortcut for --optimize-minimize --define
               process.env.NODE_ENV="production"   
`
## [理解JSX语法](https://babeljs.io/repl/)

JSX语法注意点：
- 是 XML 不是 HTML，所以所有的标签都要闭合，如 <img /> 或 <div />。
- 每段 JSX 里的 XML 只能有一个根元素，不然就报错。
- XML 里面可以用 `{}` 混入 任何 JS 代码

JSX语法的理解：分为tagname,attributes,children三项来理解

例如： `const jsx = <div>Hello Jsx</div>`
经过babel之后为：
```
var jsx = React.createElement(
  "div",
  null,
  "Hello Jsx"
);
```
这样可以理解为：
```
{
  tagname: "div",
  attributes: null,
  children: "Hello Jsx"
}
```
组件的tagname就不是标准的html标签了，如果有props，那么attributes也不是null了，对于children来说，如果内部还有嵌套标签，那么依次做一个递归。

##组件
react声明组件时，**第一个字母必须大写**。


两种写法：
1. class component
```
class Welcome extends React.Component {
    render () {
        return <h1>hello,{this.props.name}</h1>;
    }
}
```
或者：
2.functional component
```
function Welcome(props) {
    return <h1>hello,{props.name}</h1>
}
```

##组件中的数据源
1. props (props是父组件到子组件的，props should be pure,即不予许直接修改props）

2. state （state是自身维护的数据状态，但也只可以通过setState修改）


组件中不可以改变props的值，state是组件中可以改变的东西

但是要使用this.setState()方法才能改变state的值

关于setState() , [参考](https://zhuanlan.zhihu.com/p/25954470)

要理解2点：
- **setState不会立刻改变React组件中state的值**
- 函数式的setState用法(即setState()方法可以接受一个函数作为参数)


```
import React from 'react';
class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            date: new Date()
        }
        setInterval(() => {
            this.setState({
                date: new Date()
            })
        })
    }
    render () {
        return (
            <div>
                <h1>hello, {this.props.name}</h1>
                <h2>{this.state.date.toString()}</h2>
            </div>
        )
    }
}

export default Welcome;


```
##生命周期

[声明周期英文文档](https://facebook.github.io/react/docs/react-component.html#the-component-lifecycle)

React 的生命周期包括三个阶段：mount（挂载）、update（更新）和 unmount（移除）


####mount
mount 就是第一次让组件出现在页面中的过程。这个过程的关键就是 render 方法。React 会将 render 的返回值（一般是虚拟 DOM，也可以是 DOM 或者 null）插入到页面中。

这个过程会暴露几个钩子（hook）方便你往里面加代码：

- constructor()   初始化props 和 state
- componentWillMount() 准备插入render中return的内容
- render()  开始插入
- componentDidMount()  插入之后想进行的操作

####update

mount 之后，如果数据有任何变动，就会来到 update 过程，这个过程有 5 个钩子：

- 1.componentWillReceiveProps(nextProps) - 我要读取 props 啦！
- 2. shouldComponentUpdate(nextProps, nextState) - 请问要不要更新组件？true / false
- 3. componentWillUpdate() - 我要更新组件啦！
- 4. render() - 更新！
- 5. componentDidUpdate() - 更新完毕啦！

####unmount
当一个组件将要从页面中移除时，会进入 unmount 过程，这个过程就一个钩子：

- componentWillUnmount() - 我要死啦！

你可以在这个组件死之前做一些清理工作。

#### 一般在下列钩子中应用setState():
- componentWillMount
- componentDidMount
- componentWillReceiveProps

## 事件绑定
- 首先明确一个概念，就是在`<div onClick=fn></div>`这个DOM绑定中，`onClick`后面的`fn`**是一个函数，而不是一个函数执行的结果**，所以**不能写成**`<div onClick=fn()></div>`。（重点！）

明确了函数绑定时，写的**是一个函数而不是函数运行的结果**之后，继续明确第二个概念：

- bind（），对于一个函数`fn`来说,`fn.bind(window)`同样是一个函数，只不过运行的Context指定为了window。`fn.bind(window)`本身**并没有执行**（所以仍然是一个函数而不是函数运行的结果！）

明确了上述2点之后，再来看react中的事件绑定。

先来看一个App组件：
```
import React from 'react'
import ReactDom from 'react-dom'
import './index.css'

const rootDom = document.querySelector('#root')

class App extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      isOn: true
    }
  }
  render () {
    return (
      //这里点击的时候的this已经不是当前组件了，所以要bind为当前组件，不然是没有testClick函数的
      <div onClick={this.testClick.bind(this)}> 
        <button>{this.state.isOn ? 'ON' : 'OFF' }</button>
      </div>
    )
  }
  testClick () {
    this.setState( (prevState) => { //setState()也能传函数作为参数，没什么好说的
      return {
        isOn: !prevState.isOn
      }
    })
  }
}
ReactDom.render(<App></App>,rootDom)
```

这么一看就能理解react中的事件绑定了。

- 还有一个小问题，对于标签默认行为的阻止，不像Vue一样有`.prevent`这种修饰符，还是要自己写的。
来看个例子： 
```
import React from 'react'
import ReactDom from 'react-dom'
import './index.css'

const rootDom = document.querySelector('#root')

let App = () => {
  function preventClick (e) { // 注意这里的e，是react自己传过来的  本身没做任何操作
    e.preventDefault() //
    console.log('default click has been prevent')
  }
  return (
    <div>
      {/*上面已经说过，绑定事件只能写函数，不能写函数的运行结果，所以如果写了preventClick()就会报错*/}
      <a href="http://www.baidu.com" onClick={preventClick}>click me</a>
    </div>
  )
}
ReactDom.render(<App></App>,rootDom)
```

对于上述问题  又引申出了一个小问题，如果我想在阻止a标签的同时，事件处理函数还想接收一些其他的参数进行处理怎么写呢。

对于React，上面例子的`e.preventDefault()`中的e是React自己传过来的。我们如果既想写e，还想有自己规定的一些参数的话，要这么写：

```
let App = () => {
  function preventClick (str,e) { // 如果有其他自定义的参数，那么e永远是最后一项，而且同样也是React自己传过来的。
    e.preventDefault() //
    console.log(str)
  }
  return (
    <div>
      {/*上面已经说过，绑定事件只能写函数，不能写函数的运行结果，所以如果写了preventClick()就会报错*/}
      <a href="http://www.baidu.com" onClick={preventClick.bind(this,'testString')}>click me</a>
      {/*这里的preventClick.bind(this,'testString')同样也是一个函数，并不是函数运行结果*/}
    </div>
  )
}
```
上面的例子中，我们想在preventClick()中传一些自己用的参数，那么**在点击的时候，就要bind到组件本身上，然后再传入想要的参数**，注意e是不用我们自己写到参数中去的，回到函数本身的参数上，自定义的参数在前，**e永远是最后一项。**

## 摆脱React中操蛋的bind(this)的方法

上面的事件绑定中已经提到了bind，因为**事件触发时的this已经不是当前组件本身了，所以我们要将处理函数的this重新设置为当前组件**，所以有了随便一个函数后面都要`bind(this)`的情况。

先来看个例子：
```
class App extends React.Component {
  constructor(props) {
    super(props)
  }
  render (){
    return (
      <div>
        <button onClick={this.testClick.bind(this)}>click me</button>
      </div>
    )
  }
  testClick() { // 普通函数在被调用的时候，就有了this，所以要加bind()到App组件上
    console.log(this) 
  }
}
```
上述例子中，`testClick`是App组件上的函数，点击时的this已经不是App组件了，所以不能正确调用。要将其写为`this.testClick.bind(this)`而不是`this.testClick`。

为了摆脱上述这种烦人的写法，我们可以有如下几种方法：
1. 在`constructor`中将当前函数重新赋值。
```
class App extends React.Component {
  constructor(props) {
    super(props)
    this.testClick = this.testClick.bind(this) //这的this是App组件，直接将testClick重新赋值一下
  }
  render (){
    return (
      <div>
        <button onClick={this.testClick}>click me</button>
      </div>
    )
  }
  testClick() { // 普通函数在被调用的时候，就有了this，所以要加bind()到App组件上
    console.log(this)
  }
}
```
我们在constructor中给testClick重新赋了一次值，testClick变为了一个运行上下文为App组件的函数，下面直接`onClick={this.testClick}`即可。

2. public class fields syntax（不知道怎么翻译，就是利用箭头函数没有this的特性）
```
class App extends React.Component {
  constructor(props) {
    super(props)
  }
  render (){
    return (
      <div>
        <button onClick={this.testClick}>click me</button>
      </div>
    )
  }
  testClick = () => { //箭头函数没有本身的this 所以定义的时候this已经确定为App组件了，所以不用bind
    console.log(this)
  }
}
```
我们利用箭头函数本身没有this的特性，在App中定义testClick的时候，testClick的this就已经确定为当前上下文App组件了。所以后面直接调用`onClick={this.testClick}`即可。

3. 在回调中使用箭头函数

```
class App extends React.Component {
  constructor(props) {
    super(props)
  }
  render (){
    return (
      <div>
        <button onClick={(e) => {return this.testClick(e)}}>click me</button>
      </div>
    )
  }
  testClick() { 
    console.log(this)
  }
}
```
每次渲染组件时，都会生成一个全新的回调。但在有时候将其作为prop传给子组件时，会引发一次额外的渲染。所以，**建议用constructor 和public class fields syntax**两种方式避免`bind(this)`这种写法。

##值得一提的是：

上面说的想给事件处理函数传递另外自定义的参数时，**绑定事件的时候是不用写e的，因为react是自动帮你把e作为最后一个参数传递的**。

 `<button onClick={this.testClick.bind(this, id)}>click me</button>`

就像上述例子一样，我们想传一个额外的参数id，在id后面是不需要写e的，`this.testClick.bind(this, id,e)`这种是不用写的。

**但是！**  在我们提到的第3种方法，回调中使用函数的情况下，**是需要开发者自己写上e的！**

`<button onClick={(e) => this.testClick(id, e)}>click me</button>`

就像这样，自定义参数id的后面还要写上e。

详情参考文档：With an arrow function, we have to pass it explicitly, but with bind any further arguments are automatically forwarded.

[文档地址](https://reactjs.org/docs/handling-events.html)

### React中的ref

想直接修改原生DOM或者是组件的时候，可以使用ref。

1. 原生DOM上

```
class App extends React.Component {
  constructor(props) {
    super(props)
  }
  render() {
    return (
      <div>
        {/*ref callback*/}
        <input type="text" ref={(input) => this.testRef = input} />
        <input type="button" onClick={this.focus.bind(this)} value="Click" />
      </div>
    )
  }
  focus() {
    this.testRef.focus();
  }
}
```
上述例子就是一个原生DOM上使用ref的例子，react在原生的input加载完成后，通过一个回调函数，`(input) => this.testRef = input`,这里的**回调参数input就是底层的DOM，接受DOM作为参数，存到testRef中。引用的时候，直接就引用到了原生的DOM**。

2. ref ====> class Component 
```
// ref ===> class Component
class App extends React.Component {
  render() {
    return (
      <TestComponent ref={(testComponent) => this.classCom = testComponent}></TestComponent>
    )
  }
  componentDidMount () {
    console.log(this.classCom.state)
  }
}

class TestComponent extends React.Component {
  constructor() {
    super()
    this.state = {
      name: 'children Component'
    }
  }
  render() {
    return (
      <div>
        <input type="text"/>
      </div>
    )
  }
}
```

这里的ref回调函数中，`(testComponent) => this.classCom = testComponent`的参数为**已经加载的 React 实例**，我们可以在父组件中通过this.classCom访问到它。

3. ref ====> functional Component 

**你不能在函数式组件上使用 ref 属性，因为它们没有实例**,但是可以对其内部的原生DOM使用ref，参见第一条。


# React中突变数据的处理

经常会遇到设置了state之后，进行setState()了之后，页面没有进行渲染的情况。（尤其是使用PureComponent的时候）。

这种情况经常是由于操作习惯不好造成了**原数据结构的突变**。

- 先来解释什么是原数据突变

```
    let arr = [1]
    arr.push(2)
    console.log(arr) //[1, 2]
```
我们有一个数组，进行了**push操作**之后，**原先的数组的值改变了**，由`[1]`变为了`[1,2]`。

看下一个例子：

```
    let arr2 = [1]
    let arr3 = arr2.concat([2])
    console.log(arr2) //[1]
    console.log(arr3) //[1, 2]
```
同样是一个数组，进行了**concat操作**之后，**原先的arr2的值并没有发生改变**，我们把concat之后的结果赋给了一个新变量arr3来储存。

- React中setState()时我们该怎么写数据

上面我们提到了**push操作会改变原数据，这在React中是不应该的。**

setState时，我们**应该return的是一个新对象，而不是原对象**。所以我们要避免那些会修改原数据的操作，例如**push**。

比如说，我们在一个组件中通过push()修改了数组，然后有一个PureComponent子组件把这个数组作为props渲染页面，这时候虽然我们在父组件中修改了数组的值，但是setState()return的时候，原数据结构已经变了，这时候虽然本身的state已经改变了，但是传到子组件的props并没有改变。

为了避免这种情况发生，我们**一般可以采用concat**等不会引起原数据突变的操作。

如果非要用push的话，可以通过`Object.assgin()`或者`...spread语法`来进行一份数据的拷贝，在进行操作。

比如说上述的arr，我们可以这么操作：

```
    this.state = {
      arr: [1]
    };
//进行setState()
    this.setState( (prevState) => {
      return {
        arr: [...prevState.arr,2]
      }
    })
```


如果目标数据是对象的话，可以通过`Object.assgin()`来进行操作
