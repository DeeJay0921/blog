---
title: TypeScript 中的模块系统
date: 2019/11/08 19:42:01
cover: https://devblogs.microsoft.com/typescript/wp-content/uploads/sites/11/2018/08/typescriptfeature.png
tags:
- 前端
- TS
categories:
- 前端
---

TypeScript 中的模块系统的简单介绍

<!--more-->

TypeScript沿用了ECMAScript中模块的概念，直接来看相关使用：



# 导出



## 导出声明



对于任何声明，都能通过添加`export`关键字来导出

> 任何声明指的是（变量， 函数， 类， 类型别名， 接口）



```typescript

export class T {

    name: string

}



export interface Person {

    name: string

}



export let p: string = "abc";



export function fn(s: string):number {

    return s.length;

}



export type unionType = string | number;

```

要进行使用时即可直接`import`:



```typescript

import {unionType} from "./module/TestModule";



let s: unionType = "abc";

s = 123;

```

> 对于接口和类型别名这种声明，不可以使用`export default`来进行导出，因为其后面只能跟value而非type


## 导出语句



导出语句的方式允许在模块中重新定义导出的部分的名字：



```typescript

interface Person {

    name: string

}

export {Person as P};

```

```typescript

import {P} from "./module/TestModule";



let p: P = {

    name: "zZ"

};

```

## 重新导出



重新导出指的是可以在一个模块内部导出别的模块导出的部分：

比如说有模块1：

```typescript

interface Person {

    name: string

}



export { Person };

```

现有模块2对模块1的导出进行了重新导出且重命名了一下：

```typescript

export {Person as P } from "./TestModule";

```

此时别的模块可以直接使用模块2的重新导出：

```typescript

import {P} from "./module/TestModule2"; // 导出部分重命名过了  所以这块是P



let p: P = {

    name: "zZ"

};

```



> 值得注意的是：这种重新导出的方式，并没有在`TestModule2`中导入`TestModule`模块或定义一个新的局部变量。



对于一个模块有多个导出部分的情况而言，可以使用`export * from module`的方式全部导出：

```typescript

// TestModule

interface Person {

    name: string

}

class T {

    name: string;

    constructor(s: string) {

        this.name = s;

    }

}



export { Person, T };

```

```typescript

// TestModule2

export * from "./TestModule";

```

```typescript

import {Person} from "./module/TestModule2";

import {T} from "./module/TestModule2";



let p: Person = {

    name: "zZ"

};

let t: T = new T("ttt");

console.log(t.name);

```

# 导入



可以使用以下几种`import`方式来进行导入其他模块导出的内容



## 导入一个模块中的某个导出内容

```typescript

import {Person} from "./module/TestModule";



let p: Person = {

    name: "zZ"

};

```

导入的时候也可以进行重命名：

```typescript

import {Person as P} from "./module/TestModule";



let p: P = {

    name: "zZ"

};

```



## 将整个模块导入到一个变量，并通过它来访问模块的导出部分

```typescript

import * as module from "./module/TestModule";



let p: module.Person = {

    name: "zZ"

};

```

## 具有副作用的导入模块（不推荐）

当一些模块设置了一些没有导出的全局变量时，如果想要访问这些变量，可以通过这种方法来进行导入：

```typescript

import "./module/TestModule"

```

# 默认导出

每个模块都有一个`default`导出，默认导出使用`default`关键字标记，并且每个模块都只能有一个`default`导出



对于默认导出，需要一种特殊的导入形式来导入`default`导出



对于一些工具类库，通常会使用默认导出，如jQuery等：

```typescript

declare let $: jQuery;

export default $;

```

在其他地方就可以直接`import`：

```typescript

import $ from "jQuery";

```



而对于**类**和**函数声明**可以直接被标记为默认导出，且类和函数声明的名字可以省略掉：

```typescript

export default (s: string): number => {

    return s.length;

}

```

```typescript

import fn from "./module/TestModule";



let a: number = fn("abc");

```



当然`default`也可以是一个值


# `export = `和`import = require()`



针对CommonJS和AMD的`exports`变量,ts提供了`export =`语法来进行导出



同时通过`export =`语法导出的模块，必须要用特定的`import module = require("module")`来进行导入



```typescript

interface Person {

    name: string

}



export = Person;

```

```typescript

import Person = require("./module/TestModule");



let p: Person = {

    name: "zZ"

};

```
