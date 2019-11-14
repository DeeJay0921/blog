
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
打开新页
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
