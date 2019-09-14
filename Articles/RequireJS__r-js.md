---
title: RequireJS__r-js
date: 2017/09/02 00:00:01
tags: 
- 前端
- RequireJS
- JS module
categories: 
- 前端
---
RequireJS以一个相对于baseUrl的地址来加载所有的代码
<!--more-->

RequireJS以一个相对于baseUrl的地址来加载所有的代码。页面顶层<script>标签中含有一个特殊的属性data-main，require.js来使用它来启动脚本加载过程，而baseUrl一般设置到与该属性相一致的目录。

`<script data-main="scripts/main.js" src="scripts/require.js"></script>`

这样，使用requireJS之后就只需要写一个script标签即可，浏览器在执行的时候，不会识别data-main中的入口js文件，只会识别src中的require.js框架文件，然后在require.js的执行过程中，会去查找data-main中的入口文件，再去执行入口文件。

也可以手动在入口js中设置requirejs.config,在里面设置baseUrl和path，可以相对方便一点。
比如说有如下结构的文件：
```
    -project/
        -index.html
        -js/
            -app/
                -sub.js
            -lib/
                -jQuery.js
                - canvas.js
            -app.js
```
project文件夹下面有index.html和js文件夹，然后js文件夹下面又相对的有三个文件夹存放对应文件，app.js为入口文件，就可以在app.js中设置requirejs.config
```
    requirejs.config ({
        baseUrl: 'js/lib',
        
        path: {
            app: '../app',
        }
    });
```
设置了baseUrl之后，要require模块可以直接相对于baseUrl来写，比如我们要请求js/lib下的jquery.js,直接写成require('jquery')就可以，然后path内设置其他文件夹相对于baseUrl的路径，要require的话，设置了path之后也可以直接写，例如我们设置了app文件夹的path为../app，即相对于baseUrl的地址，我们要require app文件夹下的sub.js时就可以直接写成require('sub').
使用举例：
```
    //我们要使用上面例子中的依赖模块
    requirejs(['jquery','canvas','app/sub'], function ($, canvas, sub) {
        //上面三个依赖模块已经加载完，并且在这就可以使用了
    })
```
