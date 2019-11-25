---
title: Vue后端渲染之Nuxt.js
date: 2019/11/19 14:17:01
cover: https://moriohcdn.b-cdn.net/521f533697.png
tags: 
- 前端
- Vue
- NuxtJS
- 后端渲染
categories: 
- 前端
---

最近看大佬们闲聊说起页面优化，都觉得后端渲染效果立竿见影，就来学习一波NuxtJS。
<!--more-->

# 新建Nuxt项目



可以直接使用官方提供的`[create-nuxt-app](https://github.com/nuxt/create-nuxt-app)`来进行项目创建：



```

npx create-nuxt-app <项目名>

```

经过一些配置项的选择之后等待完成安装即可



另外也可以手动创建项目，创建好`package.json`:

```json

{

  "name": "my-app",

  "scripts": {

    "dev": "nuxt"

  }

}

```

然后运行：

```text

npm install --save nuxt

```

然后即可创建`pages`文件夹，创建一个`pages/index.vue`,启动项目`npm run dev`即可在` http://localhost:3000`访问到



# 目录结构



目录结构都是一些约定大于配置的东西，只有一些注意项：



- `components`文件夹下的组件不会像页面组件那样有`asyncData`方法的特性

- `nuxt.config.js`文件用于组织`Nuxt.js`应用的个性化配置，以便覆盖默认配置



另外还有约定的别名：

- `~` 或 `@`  `srcDir`

- `~~` 或 `@@``rootDir`

默认情况下，`srcDir` 和 `rootDir` 相同。
# 路由



一直在强调，`NuxtJS`是**约定大于配置**的，在`pages`文件夹下创建的组件都会被视为页面组件，根据嵌套关系和文件命名会自动生成路由配置文件



假设现有：

```text

pages/

--| user/

-----| index.vue

-----| one.vue

--| index.vue

```

自动生成的路由配置如下：

```text

router: {

  routes: [

    {

      name: 'index',

      path: '/',

      component: 'pages/index.vue'

    },

    {

      name: 'user',

      path: '/user',

      component: 'pages/user/index.vue'

    },

    {

      name: 'user-one',

      path: '/user/one',

      component: 'pages/user/one.vue'

    }

  ]

}

```
> 当有以**下划线作为前缀的` Vue `文件**时，会被视为动态路由，如：`_id.vue`



另外一提，`Nuxt`提供了一个新的路由跳转组件`<nuxt-link>`, 比起`Vue`的`<router-link>`新增了一些功能，建议使用`<nuxt-link>`



来看一个动态路由和路由参数校验的例子:



假设现在在`Pages/About/`下有一个`_id.vue`组件为：

```vue

<template>

    <div class="id">

        dynamic router params.id: {{id}}

    </div>

</template>



<script>

    export default {

        name: "dynamicId",

        asyncData({params}) {

            return {

                id: params.id // 在asyncData里面取到id作为本组件的data

            }

        },

        validate ({ params }) { // Nuxt.js提供了validate()方法可以让你在动态路由组件中定义参数校验方法。

            // 假设id不能大于100

            return Number(params.id) <= 100;

        }

    }

</script>

```

当我们访问`http://localhost:3000/about/100`时一切良好，当访问`http://localhost:3000/about/200`时提示页面找不到。



后续还有嵌套路由和动态嵌套路由，基本原理跟`Vue`是一模一样的



> 自动生成的router文件可以查看`.nuxt/router.js`
## 路由的全局守卫



关于全局守卫，完全可以当做一个插件来进行引用，我们可以建立：`/plugins/router.js`：

```js

export default ({app}) => {

    app.router.beforeEach((to,form,next) => {

        console.log(to)

        next();

    });

}

```

然后在`nuxt.config.js`中增加配置：

```text

plugins: [

    '@/plugins/element-ui',

    '@/plugins/router' // 增加全局守卫插件

],

```



# layouts



默认使用的是`layouts/default.vue`，可以理解为这个相当于`Vue`中的`App.vue`，我们可以通过修改这个组件来添加一些全局的东西。



另外也可以新增新的布局文件，比如我们可以新增一个`layouts/about.vue`:

`layouts/about.vue`:

```vue

<template>

    <div>

        <h1>This is About Page</h1>

        <nuxt />

    </div>

</template>



<script>

    export default {

        name: "about"

    }

</script>

```

然后在`pages/About/index.vue`中指定布局即可

```vue

<template>

    <div class="aboutIndex">

        pages/About/index.vue

    </div>

</template>



<script>

    export default {

        layout: "about" // 指定使用的布局

    }

</script>

```

定义了布局之后，任何指定该布局的页面都会使用该布局



当然也可以自定义404页面：



我们可以创建一个特殊的`layouts/error.vue`,虽然此文件放在 layouts 文件夹中, 但应该将它看作是一个 页面(page),且`error.vue`不需要包含` <nuxt/> `标签。



# 异步数据



## asyncData()



`asyncData()`方法可以在设置组件的数据之前能一步获取或者处理数据，最关键的是其支持异步数据. 



等待异步结果返回之后,`Nuxt.js `会将`asyncData`返回的数据融合组件` data`方法返回的数据一并返回给当前组件。



`asyncData`方法支持多种异步写法，返回一个`Promise`或者`async/await`都是可以的



返回一个`Promise`：
```vue

<script>

    let p = new Promise((resolve) => {

        setTimeout(() => {

            resolve("DeeJay");

        }, 3000)

    });

    export default {

        asyncData ({params}) {

            return p.then(res => {

                return {

                    name: res,

                    id: params.id

                }

            })

        },

    }

</script>

```



`async/await`写法：

```vue

<script>

    let p = new Promise((resolve) => {

        setTimeout(() => {

            resolve("DeeJay");

        }, 3000)

    });

    export default {

        async asyncData ({params}) {

            let res = await p;

            return {

                name: res,
打开新页
                id: params.id

            }

        },

    }

</script>

```

此外`asyncData`还提供了`callback`写法，对于第二个参数`callback`，我们可以打印出来为：

```

// 内部的callback实现，所以我们第一个参数如果为null则说明成功，如果不为null则说明在做错误处理

function (err, data) {

    if (err) {

      context.error(err);

    }

    

    data = data || {};

    resolve(data);

}

```

具体的用法就是在`Promise`写法或者`async/await`写法中不进行`return`，直接调用`callback`即可：



```vue

<script>

    let p = new Promise((resolve) => {

        setTimeout(() => {

            resolve("DeeJay");

        }, 3000)

    });

    export default {

        async asyncData ({params}, cb) {

            let res = await p;

            cb(null, {

                name: res,

                id: params.id

            });

            // return {

            //     name: res,

            //     id: params.id

            // }

        },

    }

</script>

```
这种写法多用于错误处理的情况



## asyncData中的第一个参数，即上下文对象



上文提到的asyncData中的第一个参数，即为上下文对象，其内部详细的属性见[这里](https://zh.nuxtjs.org/api/context/)



我们一般都只通过解构获取想要获得的对象，比如`req` `res` `params`以及`error`等



## 错误处理



错误处理有2种方法：

1. 上文提到的上下文对象中的`error`即为我们作为错误处理的方法

2. 使用`callback`



使用例子：



`context.error`:

```vue

<script>

    let p = new Promise((resolve, reject) => {

        setTimeout(() => {

            reject();

        }, 3000)

    });

    export default {

        async asyncData ({params, error}) {

            return p.then(res => {

                return {

                    name: res,

                    id: params.id

                }

            }).catch(() => {

                error({ statusCode: 404, message: 'Error msg!' })

            })

        },

    }

</script>

```
`callback`:

```vue

<script>

    let p = new Promise((resolve, reject) => {

        setTimeout(() => {

            reject();

        }, 3000)

    });

    export default {

        async asyncData ({params}, callback) {

            return p.then(res => {

                return {

                    name: res,

                    id: params.id

                }

            }).catch(() => {

                callback({ statusCode: 404, message: 'Error msg!' })

            })

        },

    }

</script>

```



# 插件



对于第三方的插件，直接`npm install`之后在组件内部引用使用即可



但是对于`Vue`的插件，需要在`/plugins/`下建立一个`js`文件，比如我们要引入`element-ui`



先建立`plugins/element-ui.js`:

```js

// plugins/element-ui.js

import Vue from 'vue'

import Element from 'element-ui'

import locale from 'element-ui/lib/locale/lang/en'

Vue.use(Element, { locale })

```

然后还需要在`nuxt.config.js`下进行配置：

```js

// nuxt.config.js

export default {

    // balabala 其他配置

    plugins: [

        '@/plugins/element-ui',

    ],

}

```



# Nuxt中使用Vuex



依旧是约定大于配置：`Nuxt`会自动找到`/store`目录，进行引用`Vuex`等工作



在此只介绍Nuxt官方推荐使用方式



`/store `目录下的每个` .js `文件会被转换成为状态树指定命名的子模块 （当然，`index `是根模块）



> `state`的值应该始终是`function`，为了避免返回引用类型，会导致多个实例相互影响。



来看具体的使用例子：

```js

// /store/index.js

export const state = () => ({

    counter: 0

});



export const mutations = {

    increment(state) {
打开新页
        state.counter++;

    }

};

```

再创建一个子模块`/store/otherModule.js`

```js

// /store/otherModule.js

export const state = () => ({

    otherCounter: 0

});



export const mutations = {

    add(state, number) {

        state.otherCounter += number;

    }

};

```

此时`nuxt`自动生成的`Store`为：

```js

new Vuex.Store({

  state: () => ({

    counter: 0

  }),

  mutations: {

    increment (state) {

      state.counter++

    }

  },

  modules: {

    otherModule: {

      namespaced: true,

      state: () => ({

        otherCounter: 0

      }),
      mutations: {

        add(state, number) {

            state.otherCounter += number;

        }

      }

    }

  }

})

```



在组件内部我们就可以进行使用了:



```vue

<template>

    <div class="id">

        <div>dynamic router params.id: {{id}}</div>

        <div>counter: {{counter}}</div>

        <div>otherCounter: {{otherCounter}}</div>

        <el-button @click="addCounter">click to plus one to counter</el-button>

        <el-button @click="addOtherCounter">click to plus one to otherCounter</el-button>

    </div>

</template>

<script>

    import { mapMutations } from 'vuex'

    export default {

        async asyncData ({params}) {

            return {

                id: params.id

            }

        },

        computed: {

            counter() {

                return this.$store.state.counter

            },

            otherCounter() {

      mutations: {

        add(state, number) {

            state.otherCounter += number;

        }

      }

    }

  }

})

```



在组件内部我们就可以进行使用了:



```vue

<template>

    <div class="id">

        <div>dynamic router params.id: {{id}}</div>

        <div>counter: {{counter}}</div>

        <div>otherCounter: {{otherCounter}}</div>

        <el-button @click="addCounter">click to plus one to counter</el-button>

        <el-button @click="addOtherCounter">click to plus one to otherCounter</el-button>

    </div>

</template>

<script>

    import { mapMutations } from 'vuex'

    export default {

        async asyncData ({params}) {

            return {

                id: params.id

            }

        },

        computed: {

            counter() {

                return this.$store.state.counter

            },

            otherCounter() {

                return this.$store.state.otherModule.otherCounter

            }

        },

        methods: {

            ...mapMutations({

                increment: "increment",

                add: 'otherModule/add',

            }),

            addCounter() {

                this.increment();

            },

            addOtherCounter() {

                this.add(1);

            }

        }

    }

</script>

```



# Nuxt支持TypeScript

> 笔者在这里踩过坑，所以注明以下配置都是针对` Nuxt 2.10`及以上的版本进行切换到TS，对于低版本的，安装`@nuxt/typescript`和`ts-node`即可

对于`Nuxt 2.10`及以上的版本来说，想要支持`TypeScript` 需要安装`@nuxt/types` `@nuxt/typescript-build`以及`@nuxt/typescript-runtime`

其中`@nuxt/typescript-build`和`@nuxt/typescript-runtime`都已经集成了`@nuxt/types`,没必要进行单独安装，另外`@nuxt/typescript-runtime`是可选安装的。


## @nuxt/typescript-build

如果只想对于`layouts`,` components`,` plugins`以及` middlewares`这几个文件夹下的做ts支持的话，我们只需要安装`@nuxt/typescript-build`即可

```
npm install --save-dev @nuxt/typescript-build
```

然后修改`nuxt.config.js`:
```
// nuxt.config.js
export default {
  buildModules: ['@nuxt/typescript-build']
}
```

最后创建一个`tsconfig.json`:
```
// tsconfig.json
{
  "compilerOptions": {
    "target": "esnext",
    "module": "esnext",
    "moduleResolution": "node",
    "lib": [
      "esnext",
      "esnext.asynciterable",
      "dom"
    ],
    "experimentalDecorators": true,
    "esModuleInterop": true,
    "allowJs": true,
    "sourceMap": true,
    "strict": true,
    "noEmit": true,
    "baseUrl": ".",
    "paths": {
      "~/*": [
        "./*"
      ],
      "@/*": [
        "./*"
      ]
    },
    "types": [
      "@types/node",
      "@nuxt/types"
    ]
  },
  "exclude": [
    "node_modules"
  ]
}
```

> 这边要注明一点，如果最后是想要通过`class API`的风格进行编写组件的话，需要引入`vue-property-decorator`，所以在`tsconfig.json`中需要加上`"experimentalDecorators": true,`这个选项，否则会有报错提示。

## @nuxt/typescript-runtime

`@nuxt/typescript-runtime`针对的是那些不会被`webpack`编译的文件，比如说`nuxt.config`,还有本地的模块以及`serverMiddlewares`等。

其内部使用了`ts-node`进行编译这些文件。

```
npm install @nuxt/typescript-runtime
```

安装完成后需要修改`npm scripts`:

```
"scripts": {
  "dev": "nuxt-ts",
  "build": "nuxt-ts build",
  "generate": "nuxt-ts generate",
  "start": "nuxt-ts start"
},
```
# 其余SSR替代方案

由于已有`Vue`项目迁移到`NuxtJS`项目需要较大工作量的重构（二者的规则约定差别较大）

所以如果需要进行SSR的页面较少、页面内容实时性要求较低的话，可以考虑:

- [Vue.js的预渲染插件](https://github.com/chrisvfritz/prerender-spa-plugin)
- [Vue HackerNews 2.0项目](https://github.com/vuejs/vue-hackernews-2.0/)
- [Vue.js原生的SSR](https://ssr.vuejs.org/zh/)


另附上相关阅读：
- [issue: 如何在已有项目引入Nuxt.js](https://github.com/nuxt/nuxt.js/issues/2596)
- [使用Nuxt.js改善现有项目](https://zhuanlan.zhihu.com/p/30025987)
- [使用Nuxt.js改造已有项目](https://blog.csdn.net/wopelo/article/details/80486874)
- [vue-hackernews-2.0 源码解读](https://wangfuda.github.io/2017/05/14/vue-hackernews-2.0-code-explain/)
