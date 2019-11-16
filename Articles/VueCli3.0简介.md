---
title: Vue-cli3.0使用介绍
date: 2019/11/11 15:02:01
cover: https://github.com/DeeJay0921/blog/blob/master/Covers/vue%20cli%203.jpg
tags: 
- 前端
- Vue
categories: 
- 前端
---

Vue-cli更新到3.0之后，脚手架发生了较大的变化，本文主要介绍一下相关使用
<!--more-->

对于本文中提到的具体配置可以查询[官方配置参考](https://cli.vuejs.org/zh/config/)


# 安装



新版的脚手架包名改为了`@vue/cli`



```

npm install -g @vue/cli

```

或者可以使用`npx`



```

npx @vue/cli create hello-world

```

直接创建项目



# 快速原型开发



通过安装`@vue/cli-service-global`可以对单个 `*.vue `文件进行快速原型开发

```

npm install -g @vue/cli-service-global

```



安装完成之后就可以直接在目标`.js`或 `.vue`文件的目录下直接运行`vue serve`启动一个服务器,入口可以是 `main.js`、`index.js`、`App.vue` 或 `app.vue` 中的一个。你也可以显式地指定入口文件,例如:

```

vue serve Test.vue

```



快速原型开发也是支持热更新及`ES6`的


另外可以通过

```

vue build Test.vue

```

将`.vue`文件进行打包，同样的也会在目录下创建一个`/dist`存放打包后的文件





# 正式创建项目



`@vue/cli`创建项目的指令为：



```

vue create dirName

```



创建后需要进行选择模板，`default`(默认配置)和`Manually select features`(手动配置)



`default`的话只有`babel`和`eslint`



所以一般建议使用`Manually select features`,支持`class`风格写法的`TypeScript`(使用了[vue-property-decorator](https://github.com/kaorun343/vue-property-decorator))



同时也支持单元测试及`css`预处理以及`eslint`等



做好模板选择之后等待安装完成即可



# 插件和Preset


## 插件



Vue Cli使用了一套基于插件的架构，其依赖都是以 `@vue/cli-plugin-` 开头的，例如：

```

"devDependencies": {

    "@vue/cli-plugin-babel": "^4.0.0",

    "@vue/cli-plugin-eslint": "^4.0.0",

    "@vue/cli-plugin-router": "^4.0.0",

    "@vue/cli-plugin-typescript": "^4.0.0",

    "@vue/cli-plugin-vuex": "^4.0.0",

}

```



而插件可以修改`webpack`的内部配置，也可以向`vue-cli-service`注入命令,在项目创建的过程中，绝大部分列出的特性都是通过插件来实现的。



> Vue Cli的有官方的[插件开发指南](https://cli.vuejs.org/zh/dev-guide/plugin-dev.html)



而我们通过`vue create`之后创建好的项目，还如果想要添加新的插件的话，可以使用`vue add`



例如我们之前创建的项目里面没有单元测试的插件，我们这时就可以直接调用:



```

vue add unit-mocha

```



这条指令就会被解析成去安装`@vue/cli-plugin-unit-mocha`



> 但是要注意的是，`vue add`只是为了安装和调用插件的，并不能用于普通的npm包



另外如果出于一些原因你的插件列在了该项目之外的其它` package.json` 文件里，

你可以在自己项目的` package.json `里设置 `vuePlugins.resolveFrom` 选项指向包含其它 `package.json` 的文件夹。

例如，如果你有一个 .config/package.json 文件：

```json

{

  "vuePlugins": {

    "resolveFrom": ".config"

  }

}

```



## Preset



一个Preset是一个包含创建新项目所需预定义选项和插件的`JSON`对象，让用户无需在命令提示中选择它们。



当我们使用`vue create`创建好一个项目之后，就会生成一个Preset，其路径在`~/.vuerc`下，windows对应的路径应该是`C:\Users\.vuerc`下



示例:

```json

{

  "useConfigFiles": true,

  "cssPreprocessor": "sass",

  "plugins": {

    "@vue/cli-plugin-babel": {},

    "@vue/cli-plugin-eslint": {

      "config": "airbnb",

      "lintOn": ["save", "commit"]

    },

    "@vue/cli-plugin-router": {},

    "@vue/cli-plugin-vuex": {}

  }

}

```

Preset 的数据会被插件生成器用来生成相应的项目文件。除了上述这些字段，你也可以为集成工具添加配置:

```

{

  "useConfigFiles": true,

  "plugins": {...},

  "configs": {

    "vue": {...},

    "postcss": {...},

    "eslintConfig": {...},

    "jest": {...}

  }

}

```



这些额外的配置将会根据 `useConfigFiles` 的值被合并到 `package.json` 或相应的配置文件中。



例如，当 `"useConfigFiles": true` 的时候，`configs`的值将会被合并到 `vue.config.js` 中。



另外还可以在Preset中指定插件的版本：

```

{

  "plugins": {

    "@vue/cli-plugin-eslint": {

      "version": "^3.0.0"

      // ... 该插件的其它选项

    }

  }

}

```



# CLI服务



介绍完插件和Preset之后，我们来看我们创建好的项目：



## 使用命令

我们在`package.json`中有这么一个依赖`@vue/cli-service`,使得我们可以使用一个`vue-cli-service`的命令来进行启动项目等指令。



默认的`package.json`为:



```json

{

  "scripts": {

    "serve": "vue-cli-service serve",

    "build": "vue-cli-service build"

  }

}

```



### vue-cli-service serve

```

用法：vue-cli-service serve [options] [entry]

选项：

  --open    在服务器启动时打开浏览器

  --copy    在服务器启动时将 URL 复制到剪切版

  --mode    指定环境模式 (默认值：development)

  --host    指定 host (默认值：0.0.0.0)

  --port    指定 port (默认值：8080)

  --https   使用 https (默认值：false)

```

`vue-cli-service serve`命令会启动一个开发服务器 (基于`webpack-dev-server`) 并附带开箱即用的模块热重载 (`Hot-Module-Replacement`)。



除了通过命令行参数，你也可以使用`vue.config.js`里的`devServer`字段配置开发服务器。



命令行参数`[entry]`将被指定为唯一入口，而非额外的追加入口。尝试使用`[entry]`覆盖`config.pages`中的`entry`将可能引发错误。


### vue-cli-service build

```

用法：vue-cli-service build [options] [entry|pattern]

选项：

  --mode        指定环境模式 (默认值：production)

  --dest        指定输出目录 (默认值：dist)

  --modern      面向现代浏览器带自动回退地构建应用

  --target      app | lib | wc | wc-async (默认值：app)

  --name        库或 Web Components 模式下的名字 (默认值：package.json 中的 "name" 字段或入口文件名)

  --no-clean    在构建项目之前不清除目标目录

  --report      生成 report.html 以帮助分析包内容

  --report-json 生成 report.json 以帮助分析包内容

  --watch       监听文件变化

```



`vue-cli-service build` 会在`dist/`目录产生一个可用于生产环境的包，带有`JS/CSS/HTML`的压缩，和为更好的缓存而做的自动的 `vendor chunk splitting`。它的` chunk manifest `会内联在` HTML `里。



这里还有一些有用的命令参数：



`--modern `使用现代模式构建应用，为现代浏览器交付原生支持的` ES2015 `代码，并生成一个兼容老浏览器的包用来自动回退。



`--target `允许你将项目中的任何组件以一个库或` Web Components `组件的方式进行构建。更多细节请查阅构建目标。



`--report `和` --report-json `会根据构建统计生成报告，它会帮助你分析包中包含的模块们的大小。



### vue-cli-service inspect

```

用法：vue-cli-service inspect [options] [...paths]

选项：

  --mode    指定环境模式 (默认值：development)

```

可以使用` vue-cli-service inspect `来审查一个 `Vue CLI `项目的` webpack config`





# 开发



## HTML和静态资源



`public`下的`index.html`文件应用了` html-webpack-plugin`从而作为模板，构建过程中会自动注入很多资源链接。



且会注入很多资源暗示（resource hint）比如`preload` `prefetch`等，还有用到的`CSS`和`JS`资源



### html模板插值

并且`html`可以用作模板进行插值：



```

`<%= VALUE %>` 用来做不转义插值；

`<%- VALUE %>` 用来做 HTML 转义插值；

`<% expression %>` 用来描述 JavaScript 流程控制。

```

### 使用CLI构建多页面应用



CLI3.0+ 使得我们可以非常方便的进行构建多页面应用，只需要在`vue.config.js`中配置`Pages`就行，例如:

```JS

module.exports = {

    pages: {

        index: {
            // page 的入口

            entry: 'src/main.ts',

            // 模板来源

            template: 'public/index.html',

            // 在 dist/index.html 的输出

            filename: 'index.html',

            // 当使用 title 选项时，

            // template 中的 title 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>

            title: 'Index Page',

        },

        // 当使用只有入口的字符串格式时，

        // 模板会被推导为 `public/subpage.html`

        // 并且如果找不到的话，就回退到 `public/index.html`。

        // 输出文件名会被推导为 `subpage.html`。

        subpage: 'src/subpage/main.ts'

    }

}

```



整体的项目结构为:



```text

--src

    --subpage

        --main.ts

    main.ts

```



即可完成多页面配置，就可以访问`http://localhost:8080/subpage.html/#/`了



### 处理静态资源

静态资源的处理方式分为2种：

1. 放置在`public`目录下/通过**绝对路径**引用，  这种情况资源直接拷贝，不进行`webpack`处理

2. 在`JS/TS`中被导入/在`template`或者`CSS`中被**相对路径**引用的， 这类资源会被`webpack`处理



#### 从相对路径导入



对于所有的相对路径引入，其资源 URL 都会被解析为一个模块依赖



例如，`url(./image.png)` 会被翻译为 `require('./image.png')`,会被编译为：



```javascript

h('img', { attrs: { src: require('./image.png') }})

```



然后会通过`file-loader`来计算出最终的文件路径，如果资源小于`4kb`，还会使用`url-loader`将其内联进来，避免多发http请求。



#### public文件夹



任何放置在` public `文件夹的静态资源都会被简单的复制，而不经过` webpack`。需要通过绝对路径来引用它们。



但是一般情况不建议把资源放到`public`文件夹下，因为经过`webpack`处理的资源会存在如下几个方面的优点：

1. 经过打包的`JS`和`css`会在一起，而不是分开的，可以节约http请求

2. 如果出现错误，打包的时候就能发现，而不是放到服务上时才发现资源缺失或者报错

3. 打包出的文件包含哈希值，可以排除缓存问题



关于`public`文件夹，如果前端应用没有部署在域名的根部的话，就得进行配置[publicPath](https://cli.vuejs.org/zh/config/#publicpath)配置



## webpack配置



### 修改webpack配置



`Vue CLI3.0`中配置`webpack`的方式都是通过`vue.config.js`。
简单来说分为2种方式：

1. 通过`vue.config.js`中的`configureWebpack`属性

2. 通过`webpack-chain`进行链式操作



#### `vue.config.js`中的`configureWebpack`



这个`configureWebpack`可以是一个对象也可以是一个返回对象的函数，这边有官方的2个例子：

```javascript

// vue.config.js

module.exports = {

  configureWebpack: {

    plugins: [

      new MyAwesomeWebpackPlugin()

    ]

  }

}

```

```javascript

// vue.config.js

module.exports = {

  configureWebpack: config => {

    if (process.env.NODE_ENV === 'production') {

      // 为生产环境修改配置...

    } else {

      // 为开发环境修改配置...

    }

  }

}

```



可以看到，通过这个选项可以进行配置的修改，具体详细的配置项可以参见[官方配置参考](https://cli.vuejs.org/zh/config/#publicpath)
#### 通过`webpack-chain`进行链式操作



比如说想进行修改`loader`选项:



```javascript

// vue.config.js

module.exports = {

  chainWebpack: config => {

    config.module

      .rule('vue')

      .use('vue-loader')

        .loader('vue-loader')

        .tap(options => {

          // 修改它的选项...

          return options

        })

  }

}

```



具体的例子可以参见[webpack-chain的文档](https://github.com/neutrinojs/webpack-chain#getting-started)



### 审查项目的 webpack 配置



因为`Vue CLI`对`webpack`配置做了抽象，所以想进行调整的时候通常会需要看到目前的`webpack`配置.



在前文中提到过，通过`vue-cli-service inspect`指令可以进行查看配置



所以也可以直接将`stdout`重定向到文件中方便进行查看



```text

vue-cli-service inspect > webpackConfig.txt

```



## 环境变量和模式



### 环境变量



可以在项目根目录创建如下文件进行环境变量的配置：



```text

.env                # 在所有的环境中被载入

.env.local          # 在所有的环境中被载入，但会被 git 忽略

.env.[mode]         # 只在指定的模式中被载入

.env.[mode].local   # 只在指定的模式中被载入，但会被 git 忽略

```



比如创建一个`.env`文件，内部为：

```text

VUE_APP_FOO=Bar

```

> 注意，键的开头必须为`VUE_APP`才可以在组件内部使用



被载入的变量将会对 vue-cli-service 的所有命令、插件和依赖可用，也可以在组件内部进行使用：



```typescript

    import {Component, Vue} from "vue-property-decorator"

    

    @Component

    export default class SubPageApp extends Vue {

        envVal: string = process.env.VUE_APP_FOO

        mounted() {

            console.log(process.env.VUE_APP_FOO);

        }

    }

```

### 模式



模式是 `Vue CLI` 项目中一个重要的概念。默认情况下，一个 `Vue CLI` `项目有三个模式：



- `development` 模式用于` vue-cli-service serve`

- `production` 模式用于` vue-cli-service build` 和 `vue-cli-service test:e2e`

- `test `模式用于` vue-cli-service test:unit`



另外除了` VUE_APP_* `变量之外，在你的应用代码中始终可用的还有两个特殊的变量：



- `NODE_ENV` - 会是 `"development"`、`"production"` 或 `"test"` 中的一个。具体的值取决于应用运行的模式。

- `BASE_URL` - 会和 `vue.config.js` 中的 `publicPath `选项相符，即你的应用会部署到的基础路径。



所有解析出来的环境变量都可以在` public/index.html` 中以`HTML`插值中介绍的方式使用。
