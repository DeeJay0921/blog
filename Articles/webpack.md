---
title: webpack介绍
date: 2017/09/10 00:00:01
cover: https://cms-assets.tutsplus.com/uploads/users/831/posts/25791/preview_image/webpack-tuts.jpg
tags: 
- 前端
- webpack
- JS module
categories: 
- 前端
---
webpack介绍
<!--more-->

##用法

###安装
前提是安装最新版的node.js，旧版本可能会导致缺少webpack功能或者缺少相关package包。
- 本地安装

`npm install webpack --save-dev`
`npm install webpack@<version> --save-dev`

如果使用了npm script，npm会首先在本地模块中寻找webpack。
```
  "scripts": {
    "start": "webpack --config mywebpack.config.js"
  },
```
上述是npm的标准配置，是推荐的做法

*当在本地安装了webpack之后，能在node_modules/.bin/webpack 找到它的二进制程序*
- 全局安装
`npm install webpack -g`

*注意，一般不推荐全局安装webpack,这会锁定webpack到指定版本，并且在使用不同的webpack版本的项目中可能会导致构建失败*

全局安装之后 webpack命令可以全局执行了
###使用举例
####demo1 ：简单使用
本地安装之后，可以在使用npm script,设置一个命令，
```
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1",
    "webpack": "webpack app.js app.merge.js"
  },
```
这样就可以不全局安装，只要本地安装了之后，就可以运行`npm run webpack`进行打包
那么有2个js文件，
cats.js
```
var cats = ['hauhua','xiaohei','abai','deejay'];

module.exports = cats;
```
只需要module.exports即可
app.js
```
var cats = require('./cats.js');

console.log(cats);a
```
要引用模块，require即可
然后使用webpack将app.js 打包成app.merge.js
`webpack app.js app.merge.js`
打包成的app.merge.js即为浏览器可以识别运行的js文件

####demo2 ： webpack配置文件的使用
如果项目工程比较复杂的情况下，一般也要写配置文件，并且要根据文件类型的不同，将其放到不同的文件夹下，一般源代码放到src文件下，生成的打包文件放到bin下，而node_modules文件夹就相当于加工的机器，src侠的相当于原材料，bin下的相当于加工好的成品，用户看到的只有成品。

对于上述例子，创建src,bin2个文件夹，把app.js cats.js放到src下，进行npm init,然后安装webpack,即：
`mkdir src`
`mkdir src`
`mv app.js cats.js src`
`npm init`
`npm install webpack -D`

当项目增加的很复杂时，不适用于命令行操作，我们可以创建一个配置文件。
`touch webpack.config.js`


webpack.config.js：
```
module.exports = {
    entry: './src/app.js',
    output: {
        path: './bin',
        filename: 'app.merge.js'
    }
}
```
写好配置文件之后 ， 直接命令行使用`webpack`即可得到app.merge.js

###loaders
webpack只支持原生的js打包，对于ES6，CoffeScript，TypeScript等，可以使用各种loader来进行转换，比如说ES6的babel-loader。

###plugins
有一些插件用于结果的优化等。
