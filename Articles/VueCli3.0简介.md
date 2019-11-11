---
title: Vue-cli3.0使用介绍
date: 2019/11/11 15:02:01
cover: https://cn.bing.com/th?id=OIP.aViv0ZAFtpRDT7f-gPXjPwHaEe&pid=Api&rs=1
tags: 
- 前端
- Vue
categories: 
- 前端
---

Vue-cli更新到3.0之后，脚手架发生了较大的变化，本文主要介绍一下相关使用
<!--more-->

[`@vue\cli`官方文档](https://cli.vuejs.org/zh/guide/)



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

