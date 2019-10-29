
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
