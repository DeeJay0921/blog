---

title: TypeScript 回顾整理

date: 2019/10/08 15:04:01

tags: 

- 前端

- TS

categories: 

- 前端

---

Vue3.0 最近发布了pre-alpha版本，基本都是由TS编写的，借此机会回顾一下TypeScript相关的概念和知识点

<!--more-->



# 基础用法

## 基本数据类型

JS中的原始基本数据类型：布尔值、数值、字符串、`null`、`undefined` 以及 [ES6 中的新类型 `Symbol`](http://es6.ruanyifeng.com/#docs/symbol)



### boolean

布尔值是最基础的数据类型，在 TypeScript 中，使用 boolean 定义布尔值类型：

```

let isDone: boolean = false;

```





**注意，使用构造函数 Boolean 创造的对象不是布尔值**：

```

let createdByNewBoolean: boolean = new Boolean(1);
// Type 'Boolean' is not assignable to type 'boolean'.

//   'boolean' is a primitive, but 'Boolean' is a wrapper object. Prefer using 'boolean' when possible.

```



事实上 **new Boolean() 返回的是一个 Boolean 对象**：



```

let createdByNewBoolean: Boolean = new Boolean(1);

```



直接调用 Boolean 也可以返回一个 boolean 类型：

```

let createdByBoolean: boolean = Boolean(1);

```



> 在 TypeScript 中，boolean 是 JavaScript 中的基本类型，而 Boolean 是 JavaScript 中的构造函数, 不属于基本类型。其他基本类型（除了 null 和 undefined）一样。



### number

```

let decLiteral: number = 6;

let hexLiteral: number = 0xf00d;

// ES6 中的二进制表示法

let binaryLiteral: number = 0b1010;

// ES6 中的八进制表示法

let octalLiteral: number = 0o744;
let notANumber: number = NaN;

let infinityNumber: number = Infinity;

```



上述的二进制和八进制编译为js之后都会转为十进制。



### string



```

let myName: string = 'Tom';

let myAge: number = 25;

let info: string = `Hello, i am ${myName} and i am ${myAge} years old`;

```



> 对于模板字符串来讲，编译为JS时会改为字符串拼接的形式

### 空值void



js中不存在void的概念，在ts中也只有声明一个函数没有返回值的时候才使用void(注意不是undefined!!!)



```

function alertName(): void {

    alert('My name is Tom');

}

```



而如果要声明一个void变量的话(无意义的操作)，只能赋值为null或者undefined

```

let voidVal: void = undefined;

```

### null和undefined



```

let u: undefined = undefined;

let n: null = null;

```



> 在TS中，**undefined和null是所有类型的子类！**这意味着你可以赋值给所有的类型一个null或者undefined



```

let num: number = undefined;

```



但是void类型就不行



## 任意值 Any



任意值（Any）用来表示允许赋值为任意类型



声明为any类型的变量可以进行下面的操作：

```

let myFavoriteNumber: any = 'seven';

myFavoriteNumber = 7;

```



### any的属性和方法

在任意值上访问任何属性都是允许的：

```

// 编译通过

let anyThing: any = 'hello';

console.log(anyThing.myName);

console.log(anyThing.myName.firstName);

```



```

// 编译通过

let anyThing: any = 'Tom';

anyThing.setName('Jerry');

anyThing.setName('Jerry').sayHello();

anyThing.myName.setFirstName('Cat');

```

> 声明一个变量为任意值之后，对它的任何操作，返回的内容的类型都是任意值。
### 未声明类型的变量

变量如果在声明的时候，未指定其类型，那么它会被识别为any类型：

```

// 编译通过

let something;

something = 'seven';

something = 7;



something.setName('Tom');

```



## 类型推断



TypeScript 会在没有明确的指定类型的时候推测出一个类型，这就是类型推论。



```

let myFavoriteNumber = 'seven';

myFavoriteNumber = 7;

// index.ts(2,1): error TS2322: Type 'number' is not assignable to type 'string'.

```

但是值得注意的是：

> 如果定义的时候没有赋值，不管之后有没有赋值，都会被推断成 any 类型而完全不被类型检查：



## 联合类型Union Types



```

let myFavoriteNumber: string | number;

myFavoriteNumber = 'seven';

myFavoriteNumber = 7;

```

### 访问联合类型的属性或者方法

当 TypeScript 不确定一个联合类型的变量到底是哪个类型的时候，我们只能访问此**联合类型的所有类型里共有的属性或方法**

```

function getLength(something: string | number): number {

    return something.length;

}



// index.ts(2,22): error TS2339: Property 'length' does not exist on type 'string | number'.

//   Property 'length' does not exist on type 'number'.

```



联合类型的变量在被赋值的时候，会根据类型推论的规则推断出一个类型，此后再被赋值为第二种类型之后，访问其不存在的属性或者方法就会报错



```

let myFavoriteNumber: string | number;

myFavoriteNumber = 'seven';

console.log(myFavoriteNumber.length); // 5

myFavoriteNumber = 7;

console.log(myFavoriteNumber.length); // 编译时报错



// index.ts(5,30): error TS2339: Property 'length' does not exist on type 'number'.

```

## 类型守卫（Type Guards）与类型区分（Differentiating Types）

假设现在有如下两个类型：

```

interface Fish {

    swim(): void;

    sayHi(): void;

}



interface Bird {

    fly(): void;

    sayHi(): void

}

```
对于联合类型`Fish | Bird`，引出来一个问题，当对于一个联合类型来讲，怎么确切的知道当前数据为哪个具体类型呢？



```

// 每一个成员访问都会报错

if(animal.swim) { // 报错 animal类型未知  不可访问swim方法

    animal.swim();

}else {

    animal.fly();

}

```

上例中由于类型未定，所以访问每一个类型独有的方法都会报错，所以为了使其生效，只能使用类型断言：



```

if((animal as Fish).swim) {

    (animal as Fish).swim();

}else {

    (animal as Bird).fly();

}

```



这里我们注意到，虽然在if语句里已经判断了类型，但是在分支内部想调用特有方法时，还是需要再使用一次断言`(animal as Fish).swim();`，而TypeScript里的类型守卫机制可以使我们一旦检查过类型，就能在之后的每个分支里清楚地知道变量的类型。

### 用户自定义类型守卫

类型守卫主要分为：

1. 使用类型判定

2. 使用in操作符

3. typeof类型守卫

4. instanceof类型守卫

#### 使用类型判定

要定义一个类型守卫，我们只要简单地定义一个函数，它的返回值是一个**类型谓词**：

```

function isFish(animal: Fish | Bird): animal is Fish {

    // return !!(animal as Fish).swim;

    return (animal as Fish).swim !== undefined;

}

```
> 在这个例子里，`animal is Fish`就是类型谓词。 谓词为`parameterName is Type`这种形式，`parameterName`必须是来自于当前函数签名里的一个参数名。



每当使用一些变量调用isFish时，TypeScript**会将变量缩减为那个具体的类型**，只要这个类型与变量的原始类型是兼容的。

```

if(isFish(animal)) {

    animal.swim(); // 调用类型判定之后  在这个if分支里 TS已经知道animal确定是Fish类型的了

    // 所以不需要再使用类型断言进行调用

}else {

    // 同理 在这个分支里 已经知道是Bird类型了

    animal.fly();

}

```



> 注意TypeScript不仅知道在if分支里pet是Fish类型； 它还清楚在else分支里，一定不是Fish类型，即一定是Bird类型。



#### 使用in操作符

in操作符可以作为类型细化表达式来使用。



对于`n in x`表达式，其中`n`是字符串字面量或字符串字面量类型且`x`是个联合类型，那么`true`分支的类型细化为有一个可选的或必须的属性`n`，`false`分支的类型细化为有一个可选的或不存在属性`n`。

```

if("swim" in animal) {

    animal.swim();

}else {

    animal.fly();

}

```

#### typeof类型守卫
对于原始类型来说，可以不用那么麻烦去写断言，直接通过typeof就可以进行类型判定：

```

function getLength(s: string | number) {

    if(typeof s === "string") {

        return s.length;

    }

    return s.toString().length;

}

```

这些*typeof类型守卫*只有两种形式能被识别：`typeof v === "typename"`和`typeof v !== "typename"`，`"typename"`必须是`"number"`，`"string"`，`"boolean"`或`"symbol"`。 但是TypeScript并不会阻止你与其它字符串比较，语言不会把那些表达式识别为类型守卫。



#### instanceof类型守卫

instanceof类型守卫是通过**构造函数**来细化类型的一种方式。

```

abstract class Fish {

    abstract swim(): void;

    abstract sayHi(): void;

}



abstract class Bird {

    abstract fly(): void;

    abstract sayHi(): void

}



let animal: Fish | Bird;

if(animal instanceof Fish) {

    animal.swim(); // 通过instanceof也可以进行区分

}else {

    animal.fly();

}

```

instanceof的右侧要求是一个构造函数，TypeScript将细化为：



1. 此构造函数的prototype属性的类型，如果它的类型不为any的话

2. 构造签名所返回的类型的联合



以此顺序。



## 交叉类型（Intersection Types）



交叉类型是将多个类型合并为一个类型。 这让我们可以把现有的多种类型叠加到一起成为一种类型，它包含了所需的所有类型的特性:



```

interface Fish {

    swim(): void;

    sayHi(): void;

}



interface Bird {

    fly(): void;

    sayHi(): void

}


let strangeAnimal: Fish & Bird = {

    fly(): void {

        console.log("flying");

    },

    swim(): void {

        console.log("swimming");

    },

    sayHi(): void {

        console.log("hi~");

    }

};



```



上例中的strangeAnimal的类型为`Fish & Bird`,这个类型的对象同时拥有了这两种类型的成员。这个变量既为Fish类型，同时也是Bird类型。

## 接口——对象的类型

TypeScript 中的接口是一个非常灵活的概念，除了可用于对类的一部分行为进行抽象以外，也常用于对「对象的形状（Shape）」进行描述。一般首字母大写.



### 基本用法



```

interface Person {

    name: string;

    age: number;

}



let tom: Person = {

    name: 'Tom',

    age: 25

};

```

此时约束的对象不允许缺少接口的定义的属性也不允许多加属性。



### 可选属性

```

interface Person {

    name: string;

    age?: number;

}



let tom: Person = {

    name: 'Tom'

};

```

此时age属性可有可无，但是依旧不可以多加属性



### 任意属性



```

interface Person {

    name: string;

    age?: number;

    [propName: string]: any;

}



let tom: Person = {

    name: 'Tom',

    gender: 'male'

};

```

此时可以多加任意的属性。



使用` [propName: string] `定义了任意属性取 string 类型的值。



需要注意的是，一旦定义了任意属性，那么确定属性和可选属性的类型都必须是它的类型的子集：



### 只读属性

```

interface Person {

    readonly id: number;

    name: string;

    age?: number;

    [propName: string]: any;

}



let tom: Person = {

    id: 89757,

    name: 'Tom',

    gender: 'male'

};



tom.id = 9527;



// index.ts(14,5): error TS2540: Cannot assign to 'id' because it is a constant or a read-only property.

```



只读属性只读的约束存在于第一次给对象赋值的时候，而不是第一次给只读属性赋值的时候：



## 数组的类型



数组定义有几种方法:

### 「类型 + 方括号」表示法

```

let fibonacci: number[] = [1, 1, 2, 3, 5];

```

number[]类型的数组中不允许出现第二种类型（如string）的数据，相应的，如果对数组进行操作进行新增时，也不允许加入其他类型的数据。

```

let fibonacci: number[] = [1, 1, 2, 3, 5];

fibonacci.push('8');



// Argument of type '"8"' is not assignable to parameter of type 'number'.

```

### 数组泛型

使用`Array<elemType> `来表示数组

```

let fibonacci: Array<number> = [1, 1, 2, 3, 5];

```



### 用接口表示数组(不常用)

```

interface NumberArray {

    [index: number]: number;

}

let fibonacci: NumberArray = [1, 1, 2, 3, 5];

```

注意这么写之后，变量其实不是数组类型，如push等方法是不可使用的。 本质上仅仅是个类数组类型。

### 类数组

对于arguments 等类数组， 不能用普通的数组的方式来描述，而应该用接口

TS中的内置对象中，就使用了这种方法：

```

interface IArguments {

    [index: number]: any;

    length: number;

    callee: Function;

}

```

上述的IArguments接口即规定了arguments的类型:

```

function sum() {

    let args: IArguments = arguments;

}

```

## 函数的类型



### 声明式定义

对于声明式的函数定义，规定输入参数的个数和类型即可以及输出的类型即可。

```

function sum(x: number, y: number): number {

    return x + y;

}

```

注意： 参数不得少于或者多余函数的规定



### 函数表达式



如果想通过表达式方式定义函数的话，可以直接写为：
```

let mySum = function (x: number, y: number): number {

    return x + y;

};

```

或者写为箭头函数形式:

```

let mySum = (x: number, y: number): number => {

    return x + y;

};

```

但是值得注意的是，这块的sum其实是有类型的，只不过没有显示定义而TS帮我们进行了类型推断而已，如果要显式的定义类型，需要写为：

```

let mySum: (x: number, y: number) => number = (x: number, y: number): number => {

    return x + y;

};

```

注意这边的第一个`=>`代表着函数的类型，规定了函数的返回值，而第二个`=>`则是箭头函数



### 接口表示函数



还可以使用接口表示函数：



```

interface SearchFunc {

    (source: string, subString: string): boolean;

}



let mySearch: SearchFunc;

mySearch = function(source: string, subString: string) {

    return source.search(subString) !== -1;

}

```

### 函数的可选参数


```

function buildName(firstName: string, lastName?: string) {

    if (lastName) {

        return firstName + ' ' + lastName;

    } else {

        return firstName;

    }

}

```

**可选参数必须接在必需参数后面**



### 函数参数的默认值



```

function buildName(firstName: string, lastName: string = 'Cat') {

    return firstName + ' ' + lastName;

}

```

### 函数的...rest参数（剩余参数）



```

function push(array: any[], ...items: number[]) {

    items.forEach(function(item) {

        array.push(item);

    });

}

push([], 1, 2, 3);

```

这边的...items代表1,2,3, items即为[1,2,3]为number[]类型



**rest 参数只能是最后一个参数**



### 函数的重载

```

function reverse(x: number): number;

function reverse(x: string): string;

function reverse(x: number | string): number | string {

    if (typeof x === 'number') {

        return Number(x.toString().split('').reverse().join(''));

    }

    return x.split('').reverse().join('');

}



let a: number = reverse(123);

let b: number = reverse("abc"); // 编译报错  Type 'string' is not assignable to type 'number' .

```

前2次函数定义代表传入的是number返回的也会是number，传入string返回的也是string，最后的才为函数的实现



> 重载时会从最前面的定义开始进行匹配，所以优先把最精确的写在最前



## 类型断言（Type Assertion）



用来手动指定一个值的类型



### 断言语法

1. `<类型>值`

2. `值 as 类型`

> 在tsx中只能使用 值 as 类型的语法



可以使用类型断言对联合类型进行断言，但是不可断言联合类型之外的类型：

```

function getLength(something: string | number): number {

    if ( (something as string).length ) {

        return (<string>something).length;

    }

    return something.toString().length;

}

```

对于上例来说，参数为string|number,这里的参数直接可以断言为string，但是不可以断言为string | number之外的类型

>  类型断言并不是类型转换



## 声明文件

// TODO

待补充



## 内置对象



### ECMAScript的内置对象

ECMAScript中一些内置对象如`Boolean`、`Error`、`Date`、`RegExp` 等,在TS中可以直接定义：

```

let b: Boolean = new Boolean(1);

let e: Error = new Error('Error occurred');

let d: Date = new Date();

let r: RegExp = /[a-z]/;

```

因为在 [TypeScript 核心库的定义文件](https://github.com/Microsoft/TypeScript/tree/master/src/lib)中定义了这些内置对象。



### DOM 和 BOM 的内置对象

例如`Document`、`HTMLElement`、`Event`、`NodeList` 等。



```

let body: HTMLElement = document.body;

let allDiv: NodeList = document.querySelectorAll('div');

document.addEventListener('click', function(e: MouseEvent) {

  // Do something

});

```



# 进阶



## 类型别名



类型别名用来给一个类型起个新名字。

```

type Name = string;

let s: Name = "abc";

```

## 字符串字面量类型



字符串字面量类型用来约束取值只能是某几个字符串中的一个。



```

type EventNames = 'click' | 'scroll' | 'mousemove';

function handleEvent(ele: Element, event: EventNames) {

    // do something

}



handleEvent(document.getElementById('hello'), 'scroll');  // 没问题

handleEvent(document.getElementById('world'), 'dbclick'); // 报错，event 不能为 'dbclick'



// index.ts(7,47): error TS2345: Argument of type '"dbclick"' is not assignable to parameter of type 'EventNames'.

```





## 元组



数组合并了相同类型的对象，而元组（Tuple）合并了不同类型的对象。



```

let tom: [string, number] = ['Tom', 25];

```



基本操作不赘述，值得注意的是，元组支持越界，但是**越界时新增的元素必须是前面规定的那些类型之一**



```

let tom: [string, number];

tom = ['Tom', 25];

tom.push('male'); // 越界时允许push的类型 本例中为 string | number

tom.push(true); // 编译报错
```
## 枚举

枚举（Enum）类型用于取值被限定在一定范围内的场景，比如一周只能有七天，颜色限定为红绿蓝等。

枚举成员会被赋值为从 0 开始递增的数字，同时也会对枚举值到枚举名进行反向映射

```
enum Days {Sun, Mon, Tue, Wed, Thu, Fri, Sat};

console.log(Days["Sun"] === 0); // true
console.log(Days["Mon"] === 1); // true
console.log(Days["Tue"] === 2); // true
console.log(Days["Sat"] === 6); // true

console.log(Days[0] === "Sun"); // true
console.log(Days[1] === "Mon"); // true
console.log(Days[2] === "Tue"); // true
console.log(Days[6] === "Sat"); // true
```

### 给枚举手动赋值

枚举可以在初始化的时候手动为其每一项赋值，**未被赋值的成员会上一个枚举项递增**。

```
enum Days {Sun = 7, Mon = 1, Tue, Wed, Thu, Fri, Sat};

console.log(Days["Sun"] === 7); // true
console.log(Days["Mon"] === 1); // true
console.log(Days["Tue"] === 2); // true
console.log(Days["Sat"] === 6); // true
```
本例中Tue开始就未赋值，所以会接着上一个枚举项即Mon开始接着递增。

> 赋值时允许多个项的值相等，但是会造成覆盖情况！

对于上述的覆盖情况，举个例子解释一下：

```
enum Colors = {red = 1, blue = 1, green};
```
会被编译为：
```
var Colors ;
(function (Colors ) {
    Colors [Colors ["red"] = 1] = "red";
    Colors [Colors ["blue "] = 1] = "blue ";
    Colors [Colors ["green"] = 2] = "green";
})(Colors || (Colors = {}));
```
实际最后的Colors为: 
```
{
    1: "blue",
    2: "green",
    blue: 1,
    red: 1,
    green: 2
}
```
可以看到，对于index的访问方式来说，由于下标相同，之前的`1: red`已经被覆盖为`1: blue`,所以要避免枚举值赋值重复。


### 枚举的计算所得项

对于上述的
```
enum Days {Sun, Mon, Tue, Wed, Thu, Fri, Sat};
```
即为常数项的枚举，在枚举中也可以使用计算所得项：
```
enum Color {Red, Green, Blue = "blue".length};
```
但是要注意的是：**如果紧接在计算所得项后面的是未手动赋值的项，那么它就会因为无法获得初始值而报错**

```
enum Color {Red = "red".length, Green, Blue};

// index.ts(1,33): error TS1061: Enum member must have initializer.
// index.ts(1,40): error TS1061: Enum member must have initializer.
```
详见[中文手册](https://zhongsp.gitbooks.io/typescript-handbook/content/doc/handbook/Enums.html)


### 常数枚举

注意和上面的常数项不是一个东西，常数枚举值得是通过`const`定义的枚举，即：

使用`const enum`定义的即为常数枚举，常数枚举**会在编译阶段被删除，并且不能包含计算成员。**

```
const enum Directions {
    Up,
    Down,
    Left,
    Right
}

let directions = [Directions.Up, Directions.Down, Directions.Left, Directions.Right];
```

编译为JS后代码为：

```
var directions = [0 /* Up */, 1 /* Down */, 2 /* Left */, 3 /* Right */];
```

### 外部枚举

指的是通过`declare enum`定义的枚举
```
declare enum Directions {
    Up,
    Down,
    Left,
    Right
}

let directions = [Directions.Up, Directions.Down, Directions.Left, Directions.Right];
```
编译为：
```
var directions = [Directions.Up, Directions.Down, Directions.Left, Directions.Right];
```

**declare 定义的类型只会用于编译时的检查，编译结果中会被删除。**

另外如果搭配`const`定义，会被编译为常数枚举。

## TS中的类

### public private 和 protected

TypeScript 可以使用三种访问修饰符（Access Modifiers），分别是 `public`、`private` 和 `protected`。

### constructor 设为private和protected
**防止类被继承(即final class)且不能实例化的话，在TS中要讲其constructor设为private**

```
class Animal {
    public name;
    private constructor (name) {
        this.name = name;
  }
}
class Cat extends Animal {
    constructor (name) {
        super(name);
    }
}

let a = new Animal('Jack');

// index.ts(7,19): TS2675: Cannot extend a class 'Animal'. Class constructor is marked as private.
// index.ts(13,9): TS2673: Constructor of class 'Animal' is private and only accessible within the class declaration.
```

而要类只不能实例话还可以被继承的话，要使用protected修饰constructor
```
class Animal {
    public name;
    protected constructor (name) {
        this.name = name;
  }
}
class Cat extends Animal {
    constructor (name) {
        super(name);
    }
}

let a = new Animal('Jack');

// index.ts(13,9): TS2674: Constructor of class 'Animal' is protected and only accessible within the class declaration.
```

此外修饰符还可以使用在构造函数参数中，等同于类中定义该**属性**，使代码更简洁：
```
class Animal {
    // public name: string;
    public constructor (public name) {
        this.name = name;
    }
}
```

### 抽象类

TS中的抽象类基本和Java一致，不多赘述：

```
abstract class Animal {
    public name;
    public constructor(name) {
        this.name = name;
    }
    public abstract sayHi();
}

let a = new Animal('Jack');
```
## 类与接口

接口还可以对类的一部分行为进行抽象
```
interface Alarm {
    alert();
}

interface Light {
    lightOn();
    lightOff();
}

class Car implements Alarm, Light {
    alert() {
        console.log('Car alert');
    }
    lightOn() {
        console.log('Car light on');
    }
    lightOff() {
        console.log('Car light off');
    }
}
```


> 补充一点： TS中接口可以继承类
```
// TS中接口可以继承类：
abstract class Point {
    x: number;
    y: number;
    abstract showPoint(): void;
}

interface Point3d extends Point {
    z: number;
}

let point3d: Point3d = {
    x: 1, y : 1, z: 1,
    showPoint(): void {
      console.log("");
    }
};
```

此外接口也可以继承接口
### 函数的属性和方法

函数可以拥有自己的属性和方法：
```
interface Counter {
    (start: number): string;
    interval: number;
    reset(): void;
}

let counter = <Counter>function (start: number) { };
counter.interval = 123;
counter.reset = function () { };

// 这里的counter既是一个函数，也拥有自己的属性和方法
```


## 泛型

泛型的概念不再赘述，来看基本语法：

```
function createArray<T>(length: number, value: T): Array<T> {
    let result: T[] = [];
    for (let i = 0; i < length; i++) {
        result[i] = value;
    }
    return result;
}

createArray<string>(3, 'x'); // ['x', 'x', 'x']
```

### 泛型约束

在函数内部使用泛型变量的时候，由于事先不知道它是哪种类型，所以不能随意的操作它的属性或方法

```
function getLength<T>(x: T): number() {
    return x.length;
}
// error TS2339: Property 'length' does not exist on type 'T'.
```
上述例子中由于T的类型不明，所以无法访问length属性。

这时可以对泛型进行约束，只允许这个函数传入那些包含 length 属性的变量，即泛型约束：

```
interface LengthAble {
    length: number;
}

function getLength<T extends LengthAble>(x: T): number {
    return x.length;
}
```
另外泛型之间也可以相互约束，比如在`function fn<T extends U, U>(x: T, y: U) {}`，强制要求了前一个参数的类型继承自后一个类型。

### 泛型接口

泛型也可以应用在接口上：


```
interface CreateArrayFunc {
    <T>(length: number, value: T): Array<T>;
}

let createArray: CreateArrayFunc;
createArray = function<T>(length: number, value: T): Array<T> {
    let result: T[] = [];
    for (let i = 0; i < length; i++) {
        result[i] = value;
    }
    return result;
}

createArray(3, 'x'); // ['x', 'x', 'x']
```

也可以直接将泛型写到接口上：
```
interface CreateArrayFunc<T> {
    (length: number, value: T): Array<T>;
}

let createArray: CreateArrayFunc<any>;
createArray = function<T>(length: number, value: T): Array<T> {
    let result: T[] = [];
    for (let i = 0; i < length; i++) {
        result[i] = value;
    }
    return result;
}

createArray(3, 'x'); // ['x', 'x', 'x']
```

### 泛型类
```
class GenericNumber<T> {
    zeroValue: T;
    add: (x: T, y: T) => T;
}

let myGenericNumber = new GenericNumber<number>();
myGenericNumber.zeroValue = 0;
myGenericNumber.add = function(x, y) { return x + y; };
```
### 泛型参数的默认类型

TS2.3以后新增了一个泛型参数的默认类型，使用泛型时没有在代码中直接指定类型参数，从实际值参数中也无法推测出时，会采用这个默认类型

```
function createArray<T = string>(length: number, value: T): Array<T> {
    let result: T[] = [];
    for (let i = 0; i < length; i++) {
        result[i] = value;
    }
    return result;
}
```
### 关于泛型的补充 keyof 用法



keyof返回的是一个联合属性



```

const a = {

    a: 1,

    b: 2,

};

keyof typeof a; // 'a' | 'b'



class A {

    c: number;

    d: number;

}



keyof A; // 'c' | 'd'

```



而keyof和泛型搭配，达到的一个效果就是可以通过索引类型查询和索引访问操作符：



```

//  假设现在我想去拿到一个obj内部的指定属性的值

function getPropertyFromObj<T, K extends keyof T>(obj: T, key: K): T[K] {

    return obj[key];

}

let obj = {a: 1, b: 2};

getPropertyFromObj(obj, "a");

getPropertyFromObj(obj, "c"); // Argument of type '"c"' is not assignable to parameter of type '"a" | "b"'.

```



可以看到keyof在泛型中的应用，`<T, K extends keyof T>`约束了K的类型只能是T的属性生成的联合类型，而返回值`: T[K]`规定了只能返回目标对象的属性值中联合类型。


## 声明合并



对于声明，首先明确在TS中的声明分为三类：**命名空间**， **类型**， **值**,对应的关系如下表：



| declaration type | namespace | type | value |
|------|------------|------------| ---- |
| Namespace  | √ | | √ |
| Class  | | √ | √ |
| Enum  | | √ | √ |
| Interface  | | √ | |
| Type Alias  | | √ | |
| Function  | | |√|
| Variable  | | |√|



可以看出，有些声明是有多重属性的，比如Class声明既是类型也是值，而Interface只是类型等。



### 合并接口

最简单也是最常见的合并类型，合并的机制是**将双方的成员放到一个同名接口中**：



```typescript

interface Box {

    width: string

}

interface Box {

    height: string

}



let box: Box = {

    width: "5cm",

    height: "5cm"
};

```

> 另外要注意的是：接口的非函数的成员应该是唯一的。 如果它们不是唯一的，那么它们必须是相同的类型。 否则会报错

```typescript

interface Box {

    width: string

}

interface Box {

    width: number // Subsequent property declarations must have the same type. Property 'width' must be of type 'string', but here has type 'number'.

    // 接口的非函数的成员应该是唯一的。 如果它们不是唯一的，那么它们必须是相同的类型。

}

```



而对于函数成员来讲，每个同名函数的声明都会被当做这个函数的一个重载。同时，**当接口合并时，后来的接口具有更高的优先级**：



```typescript

interface Cloner {

    clone(animal: Animal): Animal;

}



interface Cloner {

    clone(animal: Sheep): Sheep;

}



interface Cloner {

    clone(animal: Dog): Dog;

    clone(animal: Cat): Cat;

}

```

这三个接口合并成一个声明：

```typescript

interface Cloner {

    clone(animal: Dog): Dog;

    clone(animal: Cat): Cat;

    clone(animal: Sheep): Sheep;

    clone(animal: Animal): Animal;

}

```
可以发现，是**后来的接口中的函数成员出现在了最上面**。



对于上述这种合并原则，还有种特殊情况为当函数的参数的类型为**单一字符串字面量**时，拥有最高优先级，会被提升到最顶端：



```typescript

interface Document {

    createElement(tagName: any): Element;

}

interface Document {

    createElement(tagName: "div"): HTMLDivElement;

    createElement(tagName: "span"): HTMLSpanElement;

}

interface Document {

    createElement(tagName: string): HTMLElement;

    createElement(tagName: "canvas"): HTMLCanvasElement;

}

```

合并后为：

```typescript

interface Document {

    createElement(tagName: "canvas"): HTMLCanvasElement;

    createElement(tagName: "div"): HTMLDivElement;

    createElement(tagName: "span"): HTMLSpanElement;

    createElement(tagName: string): HTMLElement;

    createElement(tagName: any): Element;

}

```

### 合并命名空间



最开始我们了解到，命名空间的声明既属于命名空间的声明也属于值的声明，所以合并也是从这2个方面出发的：



对于命名空间的合并，模块导出的同名接口进行合并，构成单一命名空间内含合并后的接口。



对于命名空间里值的合并，如果当前已经存在给定名字的命名空间，那么后来的命名空间的导出成员会被加到已经存在的那个模块里。
来看合并命名空间的例子：

```typescript

namespace Animals {

    export class Zebra { }

}



namespace Animals {

    export interface Legged { numberOfLegs: number; }

    export class Dog { }

}

```

合并为：

```typescript

namespace Animals {

    export interface Legged { numberOfLegs: number; }



    export class Zebra { }

    export class Dog { }

}

```

但是对于非`export`的成员，仅仅在合并前的原有namespace里面可见，在另外的同名的namespace仍不可访问

```typescript

namespace Animal {

    let haveMuscles = true;



    export function animalsHaveMuscles() {

        return haveMuscles;

    }

}



namespace Animal {

    export function doAnimalsHaveMuscles() {

        return haveMuscles;  // Error, because haveMuscles is not accessible here

    }

}

```

### 命名空间和类与函数与枚举类型进行合并



命名空间可以和其他类型的声明进行合并，只要合并类型的定义符合将要合并类型的定义。合并结果包含二者的声明类型。



#### 合并命名空间和类



```typescript

class Album {

    label: Album.AlbumLabel;

}

namespace Album {

    export class AlbumLabel { }

}

```

合并结果是**一个类并带有一个内部类**。
除了内部类的模式，你在JavaScript里，创建一个函数稍后扩展它增加一些属性也是很常见的。 TypeScript使用声明合并来达到这个目的并保证类型安全。

 

```typescript

function buildLabel(name: string): string {

    return buildLabel.prefix + name + buildLabel.suffix;

}



namespace buildLabel {

    export let suffix = "";

    export let prefix = "Hello, ";

}



console.log(buildLabel("Sam Smith"));

```



> 注意：namespace中要合并的东西都要进行`export`



相似的，命名空间可以用来扩展枚举型：



```typescript

enum Color {

    red = 1,

    green = 2,

    blue = 4

}



namespace Color {

    export function mixColor(colorName: string) {

        if (colorName == "yellow") {

            return Color.red + Color.green;

        }

        else if (colorName == "white") {

            return Color.red + Color.green + Color.blue;

        }

        else if (colorName == "magenta") {

            return Color.red + Color.blue;

        }

        else if (colorName == "cyan") {

            return Color.green + Color.blue;

        }

    }

}

```
### 非法合并

类不能与其它类或变量合并。但是也有混入的方法解决
