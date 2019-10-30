
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
