---
title: Vue2.x+TypeScript开发指南
date: 2019/11/15 11:02:01
cover: https://ordina-jworks.github.io/img/vue-with-typescript/vue-plus-typescript.png
tags:
- 前端
- Vue
categories:
- 前端
---

本文主要介绍了Vue项目引入Typescript及相关组件ts形式的编写语法
<!--more-->


# Vue项目引入TypeScript



## 新项目支持TypeScript



对于新项目，新的`Vue CLI`可以直接配置好所有`TypeScript`的配置，只需要在`vue create`的时候添加`class-style`和`TypeScript`支持即可



## 现有项目引入TypeScript



### 引入依赖



对于老项目，需要先引入相关依赖：

- [vue-class-component](https://github.com/vuejs/vue-class-component)：强化 Vue 组件，使用 TypeScript/装饰器 增强 Vue 组件

- [vue-property-decorator](https://github.com/kaorun343/vue-property-decorator)：在 vue-class-component 上增强更多的结合 Vue 特性的装饰器

- [ts-loader](https://github.com/TypeStrong/ts-loader)：TypeScript 为 Webpack 提供了 ts-loader，其实就是为了让webpack识别 .ts .tsx文件



以及lint相关的：

- tslint-loader跟tslint：作用等同于eslint

- tslint-config-standard：tslint 配置 standard风格的约束



### 更改webpack配置



依赖引入之后，进行修改webpack配置，路径为：`build/webpack.base.conf.js`

1. 修改`entry`:

```javascript

entry: {

    app: './src/main.js'

}

```

入口的`main.js`可以修改为`main.ts`,内容保持不变。(其实也可以不改为ts文件，入口文件内容不多也没那么大必要去改)
2. 修改`resolve.extensions`:

```javascript

resolve: {

    extensions: ['.js', '.vue', '.json', '.ts'],

    alias: {

        'vue$': 'vue/dist/vue.esm.js',

        '@': resolve('src'),

        // ...

    }

},

```

添加`.ts`后缀之后引入`ts`文件就可以不写后缀了



3. 修改`module.rules`添加对`ts`文件的解析：

```javascript

    {

      test: /\.ts$/,

      exclude: /node_modules/,

      enforce: 'pre',

      loader: 'tslint-loader'

    },

    {

      test: /\.tsx?$/,

      loader: 'ts-loader',

      exclude: /node_modules/,

      options: {

        appendTsSuffixTo: [/\.vue$/],

      }

    },

```



4. 添加`tsconfig.json`
在项目根目录下下创建`tsconfig.json`,在这贴一份推荐配置：



```json

{

  "compilerOptions": {

    "target": "esnext",

    "module": "esnext",

    "strict": true,

    "strictPropertyInitialization": false,

    "jsx": "preserve",

    "importHelpers": true,

    "moduleResolution": "node",

    "experimentalDecorators": true,

    "esModuleInterop": true,

    "allowSyntheticDefaultImports": true,

    "sourceMap": true,

    "baseUrl": ".",

    "types": [

      "webpack-env",

      "mocha",

      "chai"

    ],

    "paths": {

      "@/*": [

        "src/*"

      ]

    },

    "lib": [

      "esnext",

      "dom",

      "dom.iterable",

      "scripthost"

    ]

  },

  "include": [

    "src/**/*.ts",

    "src/**/*.tsx",

    "src/**/*.vue",

    "tests/**/*.ts",

    "tests/**/*.tsx"

  ],

  "exclude": [

    "node_modules"

  ]

}

```



> 值得一提的是，`strictPropertyInitialization`这个选项建议修改为false，不然会要求每个属性值都必须有初始值。



5. 创建tslint.json

```json

{

  "extends": "tslint-config-standard",

  "globals": {

    "require": true

  }

}

```

6. 在`src`目录下创建`.d.ts`声明文件

```typescript

declare module "*.vue" {

  import Vue from 'vue'

  export default Vue

}

```

一般命名为`shims-vue.d.ts`意为告诉`TypeScript``*.vue`后缀的文件可以交给`vue`模块来处理

> 而在代码中导入`*.vue`文件的时候，需要写上`.vue` 后缀。因为Typescript只识别ts文件



> 当想在项目中使用`window`等变量时，也需要额外书写声明文件，如`declare let window: any`



经过上面几步相应配置就可以支持ts了，值得一提的是`typescript`版本可能会和`webpack`版本不兼容，所以升级的时候需要开发自己去注意一下版本适配的问题
### 重写`.vue`文件



配置好之后就可以开始重写组件了，主要借助了[vue-property-decorator](https://github.com/kaorun343/vue-property-decorator),



具体的装饰器的基本使用请参见文档，这里只提一些文档中没有细说的decorator的写法:



- 注册子组件的写法：

```vue

<script lang="ts">

    // @ is an alias to /src

    import HelloWorld from '@/components/HelloWorld.vue'

    import {Vue, Component } from "vue-property-decorator";



    @Component({

        components: { // 在这里注册子组件  后面紧跟本身组件的类即可

            HelloWorld

        }

    })

    export default class Home extends Vue{

        // ...

    }

</script>

```



- `@Watch`劫持数据中的属性的写法：

```vue

<script lang="ts">

    import {Vue, Component, Watch} from "vue-property-decorator";

    @Component

    export default class Home extends Vue{

        obj: any = {

            name: "ZZ",

        };

        @Watch("obj.name", {immediate: true, deep: true}) // 在这里直接watch属性即可

        nameChange(newVal: string) {

            console.log("obj.name ", newVal);

        }

    }

</script>

```



- 详细规定`@Prop`：

```vue

@Prop({

    type: [String, Number],

    default: "this is default msg"

}) readonly msg: string | number;

```



> 另外，对于自定义组件想`emit` `@Model`中定义的事件时，不能使用`@Emit`装饰器而是必须自己手动`emit`

- 注册钩子



除了`Vue`本身的生命周期钩子之外，其他插件的钩子要使用的话是需要提前注册的，比如我们熟悉的`Vue-router`提供的`beforeRouterEnter`等



`vue-class-component`提供了`Component.registerHooks`来注册钩子，在`vue-property-decorator`我们也可以直接调用



比如我们直接在`App.vue`里进行注册：



```vue

<script lang="ts">

    import {Vue, Component} from "vue-property-decorator";



    Component.registerHooks([

        "beforeRouteEnter"

    ]);



    @Component

    export default class App extends Vue {



    }

</script>

```



在子组件里面就可以自由使用这些钩子了:

```vue

export default class Home extends Vue{

    beforeRouteEnter():void {

        console.log("beforeRouteEnter");

    }

}

```
- 方法内部`this`的指向问题



使用ts的话，现有2种方法定义：

```vue

export default class Home extends Vue{

    foo = () => {

        console.log(this);

    }



    bar() {

        console.log(this);

    }

}

```



对于

```

foo = () => {

    console.log(this); // 内部this为Home的实例

}

```

这种定义方法，内部的this其实是为Home这个类的实例，而对于：

```text

bar() {

    console.log(this); // 内部this为Vue示例

}

```

其内部的`this`才为当前`Vue`组件的实例



所以注意要使用:

```text

bar() {

    console.log(this); // 内部this为Vue示例

}

```

这种格式。



- Mixin的实现



Mixin的实现就是一个简单的类的继承



假设现在要混入如下方法和属性：

```typescript

// TestMixin.ts

import {Vue, Component} from "vue-property-decorator";



@Component

export default class TestMixin extends Vue{

    mixinVal: string = "abc";



    mixinMethod(): void {

        console.log("mixinMethod");

    }

}

```

我们在要混入的组件中就可以直接继承该类而不是Vue了：

```typescript

import {Component} from "vue-property-decorator";



@Component

export default class Home extends TestMixin{

    created() {

        console.log(this.mixinVal);

        this.mixinMethod();

    }

}

```



- `vuex-class`的使用



我们使用了ts之后，对于`Vuex`的使用方式也要得到更新,首先安装`vuex-class`:



```text

npm install -S vuex-class

```



比方说我现在`store`文件夹下有一个`index.ts`如下，作为我`Vuex`的主模块来使用：



```typescript

import Vue from 'vue'

import Vuex from 'vuex'

Vue.use(Vuex);

export default new Vuex.Store({

    state: {

        stateVal1: "123"

    },

    getters: {

        getStateVal1: state => state.stateVal1

    },

    mutations: {
        changeStateVal(state, payload?: string): void {

            if(!payload) {

                state.stateVal1 = "abc";

            }else {

                state.stateVal1 = payload;

            }

        }

    },

    actions: {

        consoleStateVal(ctx): Promise<string> {

            return new Promise((resolve: (stateVal: string) => void) => {

                setTimeout(() => {

                    ctx.commit("changeStateVal", "def");

                    resolve(ctx.state.stateVal1);

                }, 3000)

            });

        }

    },

})

```



我们在组件内部就可以这么使用：

```vue

<script lang="ts">

    import {Vue, Component} from "vue-property-decorator";

    import {

        State,

        Getter,

        Action,

        Mutation,

        namespace

    } from 'vuex-class';

    const TestVuexModule = namespace('@/store/TestVuexModule.ts'); // 此处引入的是别的module中的store

    @Component
    export default class Home extends Vue{

        @State stateVal1; // 同名情况可以直接引用

        @Getter("getStateVal1") getStateVal; // 也可以重新命名 比方说store/index.ts下为getStateVal1, 在这重命名为为getStateVal

        @Mutation changeStateVal;

        @Action consoleStateVal;

        @TestVuexModule.State TestVal1; // 注册别的store中的状态

        created() {

            console.log(this.getStateVal);

            this.changeStateVal();

            console.log(this.getStateVal);

            this.consoleStateVal().then(res => {

                console.log("res", res);

                console.log(this.getStateVal);

            })

            console.log(this.TestVal1); // 获取到别的store中的state

        }

    }

</script>

```
