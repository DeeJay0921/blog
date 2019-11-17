---
title: 由Vue+Typescirpt引出的一些学习记录(decorator)
date: 2019/07/04 00:00:01
cover: https://css-tricks.com/wp-content/uploads/2018/06/vue-typescript.jpg
tags: 
- Vue
- Typescirpt
- ES6
- decorator
categories: 
- 前端
---
由Vue+Typescirpt引出的一些学习记录(decorator)
<!--more-->

先来看一个在Vue中使用typescript的例子：

```
<script lang="ts">
  import Vue from 'vue'
  import Component from 'vue-class-component'

  @Component
  export default class App extends Vue {
    // 初始化数据
    msg = 123

    // 声明周期钩子
    mounted () {
      this.greet()
    }

    // 计算属性
    get computedMsg () {
      return 'computed ' + this.msg
    }

    // 方法
    greet () {
      alert('greeting: ' + this.msg)
    }
  }
</script>
```
上述代码中的`@Component`是什么？

# decorator
[decorator_阮一峰](http://es6.ruanyifeng.com/#docs/decorator)

来看Python中的decorator：
```
def my_decorator(fn):
  def inner(name):
    print 'Hello ' + fn(name)
  return inner

@my_decorator
def greet(name):
  return name

greet('Decorator!')
# Hello Decorator!
```

上述例子中的`@my_decorator`就是一个decorator，decorator接受一个函数作为参数，然后在内部定义一个新的函数作为结果return出来。

等价于：
```
greet = my_decorator(greet)

greet('Decorator!')
# Hello Decorator!
```
可以看到，其实就是一种语法糖，那么在JS中，也借助了Object.defineProperty()实现了对应的decorator

## JS中的Object.defineProperty()

Object.defineProperty的用法都不陌生，其用处就是改变一个对象的属性，包括新增属性或者更改已有属性。

`Object.defineProperty(obj, key, descriptor)`
- obj代表要更改的对象
- key代表要更改的对象的属性

> 关于descriptor，也是一个对象，其内部一些固定属性规定了obj中的key属性的一些特性，比如writable控制对象是否支持编辑更改等。

那么**decorator作为一个普通的函数，其用法和Object.defineProperty()是一样的**。

## 定义decorator作用于普通的方法
先来定义一个decorator设置目标属性只读：
```
function readonly(target, key, descriptor) {
  descriptor.writable = false
  return descriptor
}
```
创建一个类，使用decorator修饰其内部方法为readonly
```
class Dog {
  @readonly
  bark () {
    return 'wang!wang!'
  }
}

let dog = new Dog()
dog.bark = () => "moew"; // 这行会报错 Cannot assign to read only property 'bark' of object '#<Dog>'
```

在上述例子中,readonly的参数**target为Dog.prototype**

> 总结：**decorator的作用就是返回一个新的descriptor！**如果作用在方法上，那么decorator的第一个参数target为这个方法所属的类的prototype，比如上例中的Dog.prototype

### 定义decorator作用在类上

当decorator作用到类上时，其第一个**参数target此时是类本身**。

```
// 这里的 `target` 是类本身
function doge (target) {
  target.isDoge = true
}

@doge
class Dog {}

console.log(Dog.isDoge)
// true
```
如果要给实例对象加属性或者方法，可以使用target.prototype定义属性或者方法

> 总结：decorator修饰类时，target为类本身，修饰方法时，target为class.prototype

## decorator接收参数

如果decorator要接收参数的话，可以写成这样:
修饰类时：
```
function doge (isDoge) {
  return function(target) {
    target.isDoge = isDoge
  }
}

@doge(true)
class Dog {}

console.log(Dog.isDoge)
// true

@doge(false)
class Human {}
console.log(Human.isDoge)
// false
```
修饰方法时：
```
function enumerable (isEnumerable) {
  return function(target, key, descriptor) {
    descriptor.enumerable = isEnumerable
  }
}

class Dog {
  @enumerable(false)
  eat () { }
}
```

即，内部return一个函数来接收target,key,descriptor,外部函数用来接收自定义参数



# 使用Vue+Typescript(使用vue-property-decorator)

学习了decorator之后，再来看ts版本的vue代码，就很好理解了：

`import Component from 'vue-class-component'`

[vue-class-component](https://github.com/vuejs/vue-class-component)是对Vue组件进行了一层封装，让 Vue 组件语法在结合了 TypeScript 语法之后更加扁平化

在其基础上，又有[vue-property-decorator](https://github.com/kaorun343/vue-property-decorator)对前者做了进一步的继承以及增加了更多的decorator

所以我们直接使用[vue-property-decorator](https://github.com/kaorun343/vue-property-decorator)来进行Vue+Ts的开发即可，来看几个基本使用的例子：

## Prop

```
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component
export default class YourComponent extends Vue {
  @Prop(Number) readonly propA!: number; // Props一般都应该设为readonly 
  @Prop({ default: 'default value' }) readonly propB!: string
  @Prop([String, Boolean]) readonly propC!: string | boolean
}
```
展示了三种定义Prop的方式

## Model
`@Model(event?: string, options: (PropOptions | Constructor[] | Constructor) = {})`
```
import { Vue, Component, Model } from 'vue-property-decorator'

@Component
export default class YourComponent extends Vue {
  @Model('balabala', { type: Boolean }) readonly checked!: boolean
}
```
等价于js写法的：
```
export default {
  model: {
    prop: 'checked',
    event: 'balabala'
  },
  props: {
    checked: {
      type: Boolean
    },
  },
}
```
## Watch

`@Watch(path: string, options: WatchOptions = {})`

```
import { Vue, Component, Watch } from 'vue-property-decorator'

@Component
export default class YourComponent extends Vue {
  @Watch('child')
  onChildChanged(val: string, oldVal: string) { }

  @Watch('person', { immediate: true, deep: true })
  onPersonChanged1(val: Person, oldVal: Person) { }

  @Watch('person')
  onPersonChanged2(val: Person, oldVal: Person) { }
}
```


## 关于TS的一些小知识点

### 设置只读属性
在interface中定义属性只读:
```
interface ReadOnly {
    readonly a: number;// a属性即为只读，除了只能在对象刚刚创建的时候给其赋值
}
```

数组类型ReadonlyArray<T>
```
let ro: ReadonlyArray<number> = [1, 2, 3, 4];
ro[0] = 12; // error!
ro.push(5); // error!
ro.length = 100; // error!
a = ro; // error!
```
> 可以用类型断言重写：`let a = ro as number[];` a就不是只可读了

### 类和接口

当一个类实现了一个接口时，**只对其实例部分进行类型检查**

上面这句话决定了接口不能对类内部的构造方法constructor()进行检查

即对于下列情况是不会做检查的：
```
interface ClockConstructor {
    new (hour: number, minute: number);
}

class Clock implements ClockConstructor {
    constructor(h: number, m: number) { }
}
```

> constructor存在于类的静态部分，所以不在检查的范围内。

```
interface ClockConstructor {
    new (hour: number, minute: number): ClockInterface;
}
interface ClockInterface {
    tick();
}

function createClock(ctor: ClockConstructor, hour: number, minute: number): ClockInterface {
    return new ctor(hour, minute);
}

class DigitalClock implements ClockInterface {
    constructor(h: number, m: number) { }
    tick() {
        console.log("beep beep");
    }
}

let digital = createClock(DigitalClock, 12, 17);
```
ts官网举了一个例子：createClock的第一个参数为ClockConstructor 类型的变量，所以在运行let digital = createClock(DigitalClock, 12, 17);时回去进行检查DigitalClock 是否合法。

### 接口中的任意属性
在定义接口当中，如果想定义一些任意属性时，需要注意的点是： **一旦你定义了一个任意属性，那么其他属性的值类型都必须是任意属性的类型或其子类型**。

```
interface Person {
  name: string;
  age?: number;
  [anyNameUWant: string]: string;
}

let Yang: Person = {
  name: 'Yang',
  age: 24,
  gender: 'male'
}
```
上述例子会报错，任意属性的值允许是 string，但是可选属性 age 的值却是 number，number 不是 string 的子属性，所以报错了
