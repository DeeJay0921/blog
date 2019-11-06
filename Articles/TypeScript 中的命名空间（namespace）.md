---

title: TypeScript 中的命名空间（namespace）

date: 2019/11/06 09:34:01

tags: 

- 前端

- TS

categories: 

- 前端

---

TS中的外部模块即为一般意义上的“模块”，而内部模块被称为“命名空间”，即`namespace`。值得一提的是以前的`module X`即等价于现在的`namespace X`且任何使用`module`关键字来声明一个内部模块的地方都应该使用`namespace`关键字来替换

<!--more-->



# 命名空间

namespace的加入使得我们可以以一种新的方式组织代码，从而避免与其他对象产生命名冲突，先来看一个简单的不使用namespace的例子：



假设我们现在要实现一个对用户发言进行判断是否含有脏话的功能：



```typescript

interface DirtyWordsValidator { // 基类接口

    isValid(s: string): boolean;

}



const SHIT_WORD = "shit";

const FUCK_WORD = "fuck"; // 2个特定的dirty word



class ShitValidator implements DirtyWordsValidator{

    isValid(s: string): boolean {

        return s.indexOf(SHIT_WORD) !== -1;

    }

}



class FuckValidator implements DirtyWordsValidator {

    isValid(s: string): boolean {

        return s.indexOf(FUCK_WORD) !== -1;

    }

}

// 实现了上述检测器之后，就可以进行检测了：

let comments: string[] = ["I love it!", "Awesome!", "Ah, shit! Here we go again!", "What the fuck!"];



let validators: { [s: string]: DirtyWordsValidator } = {

    "shit": new ShitValidator(),

    "fuck": new FuckValidator(),

};

for (let comment of comments) {

    for (let validatorKey in validators) {

        let valid = validators[validatorKey].isValid(comment);

        console.log(`${comment} ${valid ? "doesn't contain" : "contains"} ${validatorKey}`);

    }

}

```



上述例子中我们将所有的检测器都放到了一起，接下来我们使用namespace组织一下代码,整理代码的思路是：

将所有与验证器相关的类型都放到一个namespace中，且我们想让定义的接口和类在namespace外部也是可以使用的，所以还需要进行`export`这些接口和类。

但是对于我们内部定义的SHIT_WORD和FUCK_WORD，就没有必要`export`出去。



来看优化后的代码：

```typescript

namespace Validation { // 将所有验证器放到一个命名空间中

    export interface DirtyWordsValidator { // 基类接口  外部需要访问  所以export出去

        isValid(s: string): boolean;

    }



    const SHIT_WORD = "shit";

    const FUCK_WORD = "fuck"; // 2个特定的dirty word  外部不需要 属于代码细节  不需要export



    export class ShitValidator implements DirtyWordsValidator{ // 外部需要访问  所以export出去

        isValid(s: string): boolean {

            return !(s.indexOf(SHIT_WORD) !== -1);

        }

    }



    export class FuckValidator implements DirtyWordsValidator { // 外部需要访问  所以export出去

        isValid(s: string): boolean {

            return !(s.indexOf(FUCK_WORD) !== -1);

        }

    }

}



let comments: string[] = ["I love it!", "Awesome!", "Ah, shit! Here we go again!", "What the fuck!"];



let validators: { [s: string]: Validation.DirtyWordsValidator } = { // 这边需要加上命名空间

    "shit": new Validation.ShitValidator(),

    "fuck": new Validation.FuckValidator(),

};



for (let comment of comments) {

    for (let validatorKey in validators) {

        let valid = validators[validatorKey].isValid(comment);

        console.log(`${comment} ${valid ? "doesn't contain" : "contains"} ${validatorKey}`);

    }

}

```

可以看到，我们最后在外部访问`namespace`里面`export`出来的接口或者类是，需要加上相应的命名空间，如：`Validation.DirtyWordsValidator`

## 分离到多文件



当项目逻辑变的比较大时，还可以将命名空间的逻辑分离到多个文件。即时逻辑在多个文件内，但其还是会被视为是**同一个命名空间**，使用时也会跟他们在同一个文件中一样。



> 但是要注意的是，由于多个文件之间存在依赖关系，所以需要引用标签来进行声明,如：`/// <reference path="./DirtyWordsValidator.ts" />`



分离后的代码为：

module/DirtyWordsValidator.ts

```typescript

namespace Validation {

    export interface DirtyWordsValidator { // 基类接口  外部需要访问  所以export出去

        isValid(s: string): boolean;

    }

}

```

module/ShitValidator.ts

```typescript

/// <reference path="./DirtyWordsValidator.ts" />

namespace Validation {

    const SHIT_WORD = "shit";

    export class ShitValidator implements DirtyWordsValidator{ // 外部需要访问  所以export出去

        isValid(s: string): boolean {

            return !(s.indexOf(SHIT_WORD) !== -1);

        }

    }

}

```

module/FuckValidator.ts

```typescript

/// <reference path="./DirtyWordsValidator.ts" />

namespace Validation {

    const FUCK_WORD = "fuck";

    export class FuckValidator implements DirtyWordsValidator { // 外部需要访问  所以export出去

        isValid(s: string): boolean {

            return !(s.indexOf(FUCK_WORD) !== -1);

        }

    }

}

```

index.ts

```typescript

/// <reference path="./module/DirtyWordsValidator.ts" />

/// <reference path="./module/ShitValidator.ts" />

/// <reference path="./module/FuckValidator.ts" />



let comments: string[] = ["I love it!", "Awesome!", "Ah, shit! Here we go again!", "What the fuck!"];



let validators: { [s: string]: Validation.DirtyWordsValidator } = { // 这边需要加上命名空间

    "shit": new Validation.ShitValidator(),

    "fuck": new Validation.FuckValidator(),

};



for (let comment of comments) {

    for (let validatorKey in validators) {

        let valid = validators[validatorKey].isValid(comment);

        console.log(`${comment} ${valid ? "doesn't contain" : "contains"} ${validatorKey}`);

    }

}

```
对于分离后的文件，因为存在多个文件，如果我们直接进行`tsc index.ts`的话，编译的js只有当前本身的逻辑，并不会存在依赖的其他部分，所以针对这种情况，我们有2种方法：

1. 通过`--outFile target.js`的形式，例如运行`tsc --outFile sample.js index.ts`,从而生成的`sample.js`中就会引用其他部分的逻辑，我们直接使用`sample.js`即可

2. 我们可以编译依赖到的每一个ts文件为js，然后通过`script`标签去将其**按顺序**引用在页面中（较麻烦）.



## 使用别名简化命名空间操作



针对上面的`Validation.DirtyWordsValidator`,我们可以通过`import alias = X.Y.Z`给其起一个别名。可以通过这种方式为任意标识符创建别名，不局限于namespace



```typescript

import ShitValidatorAlias = Validation.ShitValidator;

let isValid: boolean = new ShitValidatorAlias().isValid("Ah, shit! Here we go again!");

```



> `import alias = X.Y.Z`和`import x = require('name')`并不一样，后者是为了针对模块的exports对象设定的
