---
title: npm-npmscript-gulp-webpack--相关问题
date: 2017/09/11 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Npm-logo.svg/1200px-Npm-logo.svg.png
tags: 
- 前端
- webpack
- gulp
- JS module
categories: 
- 前端
---
npm-npmscript-gulp-webpack--相关问题
<!--more-->

###1： 如何全局安装一个 node 应用?
- 全局安装： `npm install -g pkg`
package会被下载到特定的系统目录下，安装的package能够在所有目录下使用

###2： package.json 有什么作用？
package.json里面包含了包的各种信息，如名字作者版本号，还有依赖，开发依赖，github仓库等。

可以通过`npm init`创建一个package.json

在使用`npm install`别人的包时，会通过package.json中的依赖，将这个包的依赖都下载下来，不用自己一个个安装
###3： npm install --save app 与 npm install --save-dev app有什么区别?
- **npm install pkg --save**
自己要发布新的包，并且自己发布的包中引用了别人的包的时候，就要使用这个指令将别人的包下载下来，这条指令会使得package.json中多一个dependencies的属性，包含了引用的包的名字以及版本号。这样，别人在使用我们自己发布的包的时候，不需要将我们的包中引用的包一个个下载下来，直接使用**npm install**就能安装所有引用的模块。
- **npm install --save-dev pkg**
我们使用上述指令，package.json中会多一个devDependencies属性，这个属性中一般是自己在做开发测试的时候用的测试工具，别人引用的时候不会用到。当别人使用你的包时，进行`npm install`时，npm会把dependencies中的模块也一起下载下来，而不会把devDependencies的模块下载下来。

###4： node_modules的查找路径是怎样的?
安装之后，会在当前目录出现一个node_modules文件夹，当我们require('模块名')的时候，会先在当前目录下的node_modules文件夹下去找package.json文件中的main属性对应的参数js文件。去加载这个js文件作为模块。如果当前目录找不到node_modules就会向上一级去寻找到根目录.

###5： npm3与 npm2相比有什么改进？yarn和 npm 相比有什么优势? 

- npm3将依赖模块扁平化存放，减轻了npm2中过长依赖嵌套的问题


- Yarn默认每次安装应用都会创建或更新yarn.lock文件，以此保证其他机器安装相同版本的依赖
默认情况下Yarn的安装速度更快，Yarn并行下载和安装
Yarn支持离线安装
Yarn在每个安装包的代码执行前使用校验码验证包的完整性

###6： webpack是什么？和其他同类型工具比有什么优势？
- webpack 是一个现代 JavaScript 应用程序的模块打包器(module bundler)。当 webpack 处理应用程序时，它会递归地构建一个依赖关系图(dependency graph)，其中包含应用程序需要的每个模块，然后将所有这些模块打包成少量的 bundle - 通常只有一个，由浏览器加载。

- webpack将项目当做一个整体，通过给定的入口文件（如：index.js），找到你的项目的所有依赖文件，使用loaders和plugins来处理它们；（与gulp相比，gulp是对每一种资源单独处理，并没有整体的概念）
webpack将所有资源都视为模块，所以诸如less,json,jpg等各种资源都可以被处理；
webpack根据需要将文件切分，避免模块过多导致的请求过多，也避免只请求一次，文件过大导致的加载缓慢问题。（与r.js requirejs）
###7：npm script是什么？如何使用？
可以执行一些全局的代码或者是命令行。
```
"scripts": {
    "test": "echo deejay",
    "start": "echo hello"
},
```
其中test和start是默认的，可以直接通过npm test和npm start来运行。
但是如果要添加新的命令，如build,就只能通过npm run build来运行。
###8： 使用 webpack 替换 入门-任务15中模块化使用的 requriejs
[预览时要允许加载不安全脚本，否则无法看到懒加载效果](https://deejay0921.github.io/demos/senior/task5-webpack/senior5.html)
###9：gulp是什么？使用 gulp 实现图片压缩、CSS 压缩合并、JS 压缩合并
- 是一款nodejs应用
- 打造**前段工作流**的利器，打包，压缩，合并，git，远程操作
```
var gulp = require('gulp');

//引入组件
var minifycss = require('gulp-minify-css'), //css压缩
    uglify = require('gulp-uglify'), //js压缩
    concat = require('gulp-concat'), //合并文件

    rename = require('gulp-rename'), //重命名
    clean = require('gulp-clean'), // 清空文件夹

    minhtml = require('gulp-htmlmin'), // html压缩
    jshint = require('gulp-jshint'), //js代码规范性检查
    imagemin = require('gulp-imagemin'); //图片压缩

gulp.task('html',function () {
    return gulp.src('./src/*.html')
        .pipe(minhtml({collapseWhitespace: true}))
        .pipe(gulp.dest('dist'))
})

gulp.task('css',function () {
    gulp.src('./src/css/*.css')
        .pipe(concat('merge.min.css'))
        .pipe(renama({
            suffix: '.min'
        }))
        .pipe(minifycss())
        .pipe(gulp.dest('dist/css/'));
})
gulp.task('js',function () {
    gulp.src('./src/js/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('default'))
        .pipe(concat('merge.js'))
        .pipe(rename({
            suffix: '.min'
        }))
        .pipe(uglify())
        .pipe(gulp.dest('./dist/js/'))
})

gulp.task('img',function () {
    gulp.src('./src/img/*')
        .pipe(imagemin())
        .pipe(gulp.dest('./dist/imgs'));
})
gulp.task('clear',function () {
    gulp.src('./dist/*',{read: false})
        .pipe(clean());
});

gulp.task('build',['html','css','js','img']);
```
运行`gulp build`，如果需要清空dist内的文件，运行`gulp clear`
###10： 开发一个 node 命令行天气应用用于查询用户当前所在城市的天气，发布到 npm 上去。
[weather](https://www.npmjs.com/package/deejay-weather)
