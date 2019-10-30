---
title: TypeScript中的映射类型及常见工具映射类型
date: 2019/10/30 11:05:01
tags: 
- 前端
- TS
categories: 
- 前端
---
TypeScript中的映射类型允许我们基于旧类型创建一个新类型，从而有一些很有用的使用方法
<!--more-->


## TS中的映射类型



假设我们现在有一个`Person`类型的接口：



```

interface Person {

    name: string;

    age: number;

}

```



我们经常会遇到一种需求是将其内部所有的属性都变为可选：

```

interface PersonPartial {

    name?: string;

    age?: number;

}

```

或者是将其内部属性都设置为只读:

```

interface PersonReadonly {

    readonly name: string;

    readonly age: number;

}

```



针对这种从一个**旧类型中创建一个新类型**的需求，TS提供了**映射类型**。

> 在映射类型里，新类型以**相同的形式**去转换旧类型里每个属性



针对上述的2种需求，使用映射类型可以直接生成一个新的类型：

```

interface Person {

    name: string;

    age: number;

}



type usrDefinedPartial<T> = {

    [P in keyof T]?: T[P];

}



let p: usrDefinedPartial<Person> = {}; // 都为可选属性了



type usrDefinedReadonly<T> = {

    readonly [P in keyof T]: T[P];

}



let p2: usrDefinedReadonly<Person> = {

    name: "p2",

    age: 20

};



// p2.name = "p22"; // Cannot assign to 'name' because it is a read-only property.

```



当然针对上面定义的`usrDefinedPartial<Person>`和`usrDefinedReadonly<Person>`,完全可以定义一个别名来方便使用：

```

type PartialPerson = usrDefinedPartial<Person>;

type ReadonlyPerson = usrDefinedReadonly<Person>;

```

如果想要在上面的类型的基础上新增一些属性的话，注意这个语法描述的**是类型而非成员**，所以我们不能直接这么写：

```

// 不能这么写

type addNewPropertyPerson<T> = {

    readonly [P in keyof T]: T[P];

    test: string; // 'string' only refers to a type, but is being used as a value here.

};

```



而是应该采用交叉类型：

```

interface TestAble {test: string}



type addNewPropertyPerson<T> = {

    readonly [P in keyof T]: T[P];

} & TestAble;



let p: addNewPropertyPerson<Person> = {

    name: "Yang",

    age: 21,

    test: "abc"

};



// p.name = "123"; // Cannot assign to 'name' because it is a read-only property.

```



也可以省略定义类型，直接使用字面量类型：

```

type addNewPropertyPerson<T> = {

    readonly [P in keyof T]: T[P];

} & {test: string};

```
看完映射类型的简单使用，接下来看一下映射类型的组成部分



现有如下2个类型：

```

type Keys = 'option1' | 'option2';

type Flags = { [K in Keys]: boolean };

```

其内部使用了`for ... in`，分为3个部分：

1. 类型变量`K`，其会在遍历时依次绑定到每个属性

2. 字符串联合类型变量`Keys`,提供了要迭代的属性名的集合（一般也会使用泛型）

3. 属性的结果类型，本例中为`boolean`



上述例子中定义的`Flags`类型，等价于：

```

type Flags = {

    option1: boolean;

    option2: boolean;

}

```



在实际的开发中，遇到的情况不止这么简单，经常会基于一些已经存在的类型，且按照一定的方式进行转换字段,

假设现在有如下类型：

```

interface Person {

    name: string;

    age: number

}

```

现在要基于`Person`类型，在`--strictNullChecks`下，创建一个允许属性值有`null`的新类型:

```

type NullAblePerson = {

    [P in keyof Person]: Person[P] | null;

}



let p: NullAblePerson = {

    name: null,

    age: null

}; // 在strictNullChecks也无报错

```

当然我们一般会选用更通用的泛型来进行声明映射类型:

```

type NullAble<T> = {

    [P in keyof T]: T[P] | null

};



type NullAblePerson = NullAble<Person>;

```

## 一些实用的工具类型



TS内置了一些工具映射类型方便我们进行操作：



### `Partial<T>`



并将它所有的属性设置为可选的。它的返回类型表示输入类型的所有子类型。

```

interface Person {

    name: string;

    age: number

}



let p: Partial<Person> = {}; // check pass

```

### `Readonly<T>`

构造类型T，并将它所有的属性设置为readonly，也就是说构造出的类型的属性不能被再次赋值。

```

interface Person {

    name: string;

    age: number

}



let p: Readonly<Person> = {

   name: "Y", age: 20

}; 



p.name = "Z"; // Cannot assign to 'name' because it is a read-only property.

```

还可用来表示在运行时会失败的赋值表达式（比如，当尝试给冻结对象的属性再次赋值时）:

```

//Object.freeze

function freeze<T>(obj: T): Readonly<T>;

```
### Record<K,T>

构造一个类型，其属性名的类型为K，属性值的类型为T。这个工具可用来将某个类型的属性映射到另一个类型上。

```

interface LineInfo {

    line: string

}



type lines = "line1" | "line2" | "line3";



let article: Record<lines, LineInfo> = {

    line1: {line: "line1"},

    line2: {line: "line2"},

    line3: {line: "line3"},

};

```

### Pick<T,K>

从类型T中挑选部分属性K来构造类型。

```

interface Person {

    name: string,

    age: number,

    gender: string

}



let p: Pick<Person, "name" | "gender"> = {

    name: "Y",

    gender: "Male"

};

```
