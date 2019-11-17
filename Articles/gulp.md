---
title: gulp介绍
date: 2017/09/09 00:00:01
cover: https://codehangar.io/content/images/2015/10/gulp-logo.jpg
tags: 
- 前端
- gulp
- JS module
categories: 
- 前端
---
gulp介绍
<!--more-->

#gulp介绍
[地址](http://gulpjs.com)
- 是一款nodejs应用
- 打造**前段工作流**的利器，打包，压缩，合并，git，远程操作
- ......

##gulp常见问题
###1，安装

`npm install gulp-cli -g` 全局安装
`npm install gulp -D` 在当前目录下安装（这样才能require到，全局是require不到的，详情见[npm](http://www.jianshu.com/p/9feb2223dc52)）
`touch gulpfile.js` 创建gulpfile.js文件
`gulp --help` 查看指令

- gulpfile.js
```
var gulp = require('gulp');
var pug = require('gulp-pug'); //模板文件 生成html
var less = require('gulp-less'); //将less转化为css
var minifyCSS = require('gulp-csso'); //压缩css

gulp.task('html',function () {  //基床上定义了一个事件叫html
   return gulp.src('client/templates/*.pug') //会把当前src下面的所有的.pug文件输进去(物料)
       .pipe(pug()) // 然后通过pipe()变成数据流  传给pug() 处理之后又传给后面的进行pipe
       .pipe(gulp.dest('build/html')) // 后面不需要处理了  就直接把它输出成html
});
// 定义好之后  只需要命令行执行 gulp html 就会执行这个事情


//同理 也可以定义事件css 将less文件转换为css并且压缩
gulp.task('css',function () {
   return gulp.src('client/templates/*.less')
       .pipe(less())
       .pipe(minifyCSS())
       .pipe(gulp.dest('build/css'))
});

// 定义事件default 直接简写 直接命令行执行 gulp   就会执行这个default事件，然后由于这个任务里面有html css两个任务 就会都执行
gulp.task('default',['html','css']);
```
###gulp使用
####具体使用举例1：（合并css文件）

文件夹结构如图
![1.png](http://upload-images.jianshu.io/upload_images/7113407-871ac1c29c8b9884.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

要将src文件夹下的几个css文件合并并且压缩到dist文件夹下的css中以便发布，具体操作流程如下：
- 1，在demo1下创建gulpfile.js
- 2，在demo1下创建package.json(可以通过npm init)
- 3，运行npm install gulp --save-dev或者（-D）来在本地添加gulp使得我们可以在gulpfile.js中require到gulp，完成后package.json中出现"devDependencies"。
- 4，这样就可以在gulpfile.js中var gulp = require('gulp');了，另外我们还需要cssnano（将css压缩）和concat（文件合并）两个插件,同样的，我们进行插件的安装
`npm install gulp-cssnano --save-dev`
 `npm install --save-dev gulp-concat`
- 5,进行gulpfile.js的书写
首先介绍gulp的几个重要方法
**gulp对象的4个方法**
具体使用见[API文档](https://github.com/gulpjs/gulp/blob/master/docs/API.md)
- gulp其实就是一个对象，其实只有gulp.src/gulp.dest/gulp.task/gulp.watch四个方法
- gulp.src的作用就是需要去处理哪些文件
- gulp.dest的作用就是输出文件
- gulp.task 创建一个任务 一个任务可以认为是一个流水线
- gulp.task中的参数中可以传入依赖的其他任务，这些任务是默认并行执行的并且是在当前任务之前执行的
- gulp.watch监测文件的改动


#####具体gulpfile.js的书写:

1. 引入模块跟插件
```
//本地安装gulp进行开发npm install gulp -D  使得可以require到gulp
var gulp = require('gulp');

//安装2个需要用到的插件
var cssnano = require('gulp-cssnano');
var concat = require('gulp-concat');
```
2. 通过gulp.task创建一个任务，叫做css
```
gulp.task('css', function() {

    // gulp.src('./src/css/*.css') //需要处理的文件是相对于gulpfile.js的路径即当前文件夹下的src/css下的所有的css文件
    //如果只想处理common.css和index.css，那么可以写成
    gulp.src(['./src/css/common.css','./src/css/index.css']) //这样就得到了数据流
        .pipe(concat('index-merge.css')) //将数据流通过pipe传递给下一个工序，这里是concat(),合并css文件并且命名为index-merge.css
        .pipe(cssnano())//然后进行压缩
        .pipe(gulp.dest('./dist/css')) //然后通过dest输出到指定目录下，这里是dist文件夹下

});
```
写好之后  可以通过终端 定位到当前目录下 运行gulp 指定task名即可,这里是`gulp css`
运行之后我们的两个css文件就合并成功了

接着我们可以把剩下的reset.css只进行压缩一下 也放到dist/css下
```
gulp.task('nanoReset',function () {
    gulp.src('./src/css/reset.css')
        .pipe(cssnano())
        .pipe(gulp.dest('./dist/css'))
})
```
写好之后运行`gulp nanoReset`
当然也可以将task名字命名为default 这样运行的时候就直接输入`gulp` 即可运行 ,例如：
```
gulp.task('default',function () {
    gulp.src('./src/css/reset.css')
        .pipe(cssnano())
        .pipe(gulp.dest('./dist/css'))
})
```
或者再创建一个task，名字为default,然后写上` gulp.task('default',['css']) `最后运行的时候输入`gulp`即可

###具体使用举例2：（实现一个保存代码之后浏览器马上刷新的效果）

用到一个nodejs应用browser-sync
```
var gulp = require('gulp');

//引入组件
var browserSync = require('browser-sync').create();
var fs = require('fs');

gulp.task('reload',function () { //定义一个任务reload，进行刷新
    browserSync.reload();
})

gulp.task('server',function () { //定义一个任务server 启动一个服务器，基准路径是./src
    browserSync.init({
        server:  {
            baseDir: "./src"
        }
    });
    gulp.watch(['**/*.css','**/*.js','**/*.html'],['reload']); //监听文件，只要文件发生变化就进行reload任务
})
```
###具体应用举例3（实现css,js,图片的压缩合并等）: 
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
