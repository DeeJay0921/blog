---
title: TypeScript中的声明文件
date: 2019/11/07 15:44:01
cover: https://devblogs.microsoft.com/typescript/wp-content/uploads/sites/11/2018/08/typescriptfeature.png
tags:
- 前端
- TS
categories:
- 前端
---
介绍如何书写规范的声明文件
<!--more-->


[官方模板](https://www.tslang.cn/docs/handbook/declaration-files/templates.html)

对于第三方库的声明文件，可以直接进行[TypeSeach搜索](https://microsoft.github.io/TypeSearch/)对应的@types包,如需引入es6的新方法，即可搜索`es6-shim`,安装`/@types/es6-shim`包即可

# 识别库的类型



声明文件的书写和组织方式是由外部库的使用方式决定的，在JS中一个库有多种使用方式，这就要求我们书写对应的声明文件去匹配他们



## 全局库（Global Libraries）



全局库是可以在全局命名空间下进行访问的，比如说`jQuery`等，这种全局库都是暴露出一个或者几个全局变量以供使用：

```typescript

$(() => { console.log('hello!'); } ); // $ 变量可以被简单的引用

```

在全局库的文档中，一般也会看到可以直接在`script`标签上进行引用库：

```html

<script src="http://a.great.cdn.for/someLib.js"></script>

```



>  UMD库的文档很难与全局库文档两者之间难以区分,写声明文件之前要分清是UMD库还是全局库



### 识别全局库

当你查看全局库的源代码时，你通常会看到：

- 顶级的`var`语句或`function`声明

- 一个或多个赋值语句到`window.someName`

- 假设`DOM`原始值像`document`或`window`是存在的



你不会看到：

- 检查是否使用或如何使用模块加载器，比如`require`或`define`

- `CommonJS/Node.js`风格的导入如`var fs = require("fs");`

- `define(...)`调用

- 文档里说明了如何去`require`或导入这个库



### 全局库的使用方式

```typescript

/// <reference types="someLib" />



function getThing(): someLib.thing;

```

即，需要增加`/// <reference types="..." />`指令，以找到相关的.d.ts文件。



### 全局库的声明文件模板：

```typescript

// 如果全局代码库，导出了一个名为myLib的函数

// 例如，window.myLib(xxx)

declare function myLib(a: string): string;

declare function myLib(a: number): number;



// 如果全局代码库，导出了一个自定义类型

// 例如，var x: myLib

interface myLib {

    name: string;

    length: number;

    extras?: string[];

}

// 如果全局代码库，导出了一个对象

// 例如，window.myLib.timeout, window.myLib.version, ...

declare namespace myLib {



    // window.myLib.timeout

    let timeout: number;



    // window.myLib.version

    const version: string;



    // window.myLib.Cat

    class Cat {

        constructor(n: number);

        readonly age: number;

        purr(): void;

    }



    // var x: window.myLib.CatSettings

    interface CatSettings {

        weight: number;

        name: string;

        tailLength?: number;

    }



    // var x: window.myLib.VetID

    type VetID = string | number;



    // window.myLib.checkCat(xxx)

    function checkCat(c: Cat, s?: VetID);

}

```



## 模块库（Modular Libraries）
模块库指的是，类似CommonJS，AMD（RequireJS），ES6 module这样的代码组织方式，



### 识别模块库

一般模块库的代码组织方式都会有如下类似的结构：

```

// CommonJS

var fs = require("fs");



// TypeScript 或 ES6

import fs = require("fs");



// AMD

define(..., ['someLib'], function(someLib) {



});

```

### 模块库的使用方式

如果我们依赖一个模块库，TypeScript要求在用户代码中这样使用它:

```typescript

// 直接import即可，TypeScript会根据模块名去寻找.d.ts文件

import * as moment from "moment";



function getThing(): moment;

```



### 模块库的模板

针对模块库可能会导出三种类型的东西：**对象**，**类**，**函数**，有对应的三种模板：



1. 导出一个对象

```typescript

// 如果模块库是UMD，导出一个全局对象myLib

export as namespace myLib;

// 如果模块库导出的对象有方法，例如导出一个这样的对象 {myMethod,myOtherMethod}

export function myMethod(a: string): string;

export function myOtherMethod(a: number): number;



// 如果模块库导出了一个类型，例如 {someType}

export interface someType {

    name: string;

    length: number;

    extras?: string[];

}



// 可以声明模块导出的对象，有哪些属性

export const myField: number;



// 导出一个名字空间，这个名字空间中可以有类型，属性，和方法

export namespace subProp {



    // import { subProp } from 'yourModule'; 其中subProp是一个名字空间

    // subProp.foo(); 名字空间中的方法



    // 或者 import * as yourMod from 'yourModule'; 其中 import * as yourMod 将整个模块看做yourMod

    // yourMod.subProp.foo();

    export function foo(): void;

}

```

2. 导出一个类

```typescript

// 如果模块库是UMD，导出一个全局对象myLib

export as namespace myClassLib;



// 表明模块只导出了一个类，

// 注意，ES module只能导出一个对象，不能导出一个类

export = MyClass;
// 声明导出的这个类的构造器，属性，和方法

declare class MyClass {



    // 构造器

    constructor(someParam?: string);



    // 属性

    someProperty: string[];



    // 方法

    myMethod(opts: MyClass.MyClassMethodOptions): number;

}



// 如果导出的这个类，还可以做为名字空间来使用

declare namespace MyClass {



    // 名字空间中的类型

    // const MyClass = require('yyy');

    // const x: MyClass.MyClassMethodOptions

    export interface MyClassMethodOptions {

        width?: number;

        height?: number;

    }

}

```

3. 导出一个方法

```typescript

// 如果模块库是UMD，导出一个全局函数myFuncLib

export as namespace myFuncLib;



// 表明模块只导出了一个函数，

// 注意，ES module只能导出一个对象，不能导出一个函数

export = MyFunction;
// 导出的函数可以具有多个重载版本

declare function MyFunction(name: string): MyFunction.NamedReturnType;

declare function MyFunction(length: number): MyFunction.LengthReturnType;



// 如果导出的这个函数，还可以做为名字空间来使用

declare namespace MyFunction {



    // 名字空间中的类型

    // const MyFunction = require('yyy');

    // const x: MyFunction.LengthReturnType

    export interface LengthReturnType {

        width: number;

        height: number;

    }



    // 名字空间中的类型

    // const MyFunction = require('yyy');

    // const x: MyFunction.NamedReturnType

    export interface NamedReturnType {

        firstName: string;

        lastName: string;

    }



    // 名字空间中的属性

    export const defaultName: string;



    // 名字空间中的属性

    export let defaultLength: number;

}

```
> 另外补充：ES6 module只能导出一个对象，而CommonJS还可以导出类或者函数。
> 十分常见的解决方法是定义一个default导出到一个可调用的/可构造的对象； 一会模块加载器助手工具能够自己探测到这种情况并且使用default导出来替换顶层对象。



## UMD

UMD模块是指那些既可以作为模块使用（通过导入）又可以作为全局（在没有模块加载器的环境里）使用的模块。

### 识别UMD库



UMD库会检查是否存在模块加载器环境。 这是非常形容观察到的模块，它们会像下面这样：

```

(function (root, factory) {

    if (typeof define === "function" && define.amd) {

        define(["libName"], factory);

    } else if (typeof module === "object" && module.exports) {

        module.exports = factory(require("libName"));

    } else {

        root.returnExports = factory(root.libName);

    }

}(this, function (b) {

```

即会有类似与`typeof define`，`typeof window`，或`typeof module`的关键字



其模板同模块库



## 模块库或UMD的插件（Module Plugin or UMD Plugin）



其插件仍然是一个模块库或者UMD库，使用方式也同模块库或者UMD库



### 模块库或UMD的插件的模板



```typescript

// 作为核心库的插件，这里要引入核心库本身

import * as m from 'someModule';
// 如果需要的话，也可以引入其他库

import * as other from 'anotherModule';



// 声明一个和核心库同名的module

declare module 'someModule' {



    // 添加插件中t添加的函数，类型

    // 注意，还可以使用unexport删除核心库中已经导出的名字



    // 插件中的函数

    export function theNewMethod(x: m.foo): other.bar;



    // 插件中的类型

    export interface SomeModuleOptions {

        someModuleSetting?: string;

    }



    // 插件中的类型

    export interface MyModulePluginOptions {

        size: number;

    }

}

```



## 全局代码库的插件（Global Plugin）



表现为给暴露出的全局对象加一个属性，使用方式即像其他全局变量的属性一样



### 全局代码库的插件的模板



```typescript
// 对被增加属性的全局变量进行声明，其中包括添加的属性

interface Number {

    toBinaryString(opts?: MyLibrary.BinaryFormatOptions): string;

    toBinaryString(callback: MyLibrary.BinaryFormatCallback, opts?: MyLibrary.BinaryFormatOptions): string;

}



// 全局添加了一个名字空间，其中包含类型，以及类型别名

declare namespace MyLibrary {



    // 类型别名

    // const x: window.MyLibrary.BinaryFormatCallback

    type BinaryFormatCallback = (n: number) => string;



    // 类型

    // const x: window.MyLibrary.BinaryFormatOptions

    interface BinaryFormatOptions {

        prefix?: string;

        padding: number;

    }

}

```

## 全局代码库的修改模块（Global-modifying Modules）



修改全局变量的属性。

###　全局代码库的修改模块的模板



```typescript

// 声明对全局空间造成的修改

declare global {



    // 类型

    interface String {

        fancyFormat(opts: StringFormatOptions): string;

    }

}
// 全局修改模块导出的类型

export interface StringFormatOptions {

    fancinessLevel: number;

}



// 全局修改模块导出的函数

export function doSomething(): void;



// 如果全局修改模块什么也没有导出

export { };

```
