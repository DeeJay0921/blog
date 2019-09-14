---
title: AMD_-CMD
date: 2017/09/02 00:00:01
tags: 
- 前端
- JS
- AMD
- CMD
- JS module
categories: 
- 前端
---
AMD_-CMD
<!--more-->

##模块化的价值
- 最主要的目的
解决命名冲突
依赖管理
- 其他价值
提高代码可读性
代码解耦，提高复用性

通行的JS模块规范主要有2种：CommonJS 和 AMD

##CommonJS
先从CommonJS说起，是由服务器端的JS应用带来的，是由NodeJS发扬光大的，标志着JS模块化编程正式登上舞台
- 1.定义模块： 根据CommonJS规范，一个单独的文件就是一个模块。每一个模块都是一个单独的作用域，也就是说，**在该模块内部定义的变量，无法被其他模块读取，除非定义为global对象的属性**。
- 2.模块输出： **模块只有一个出口，module.exports对象**，我们需要把模块希望输出的内容放到该对象
- 3.加载模块： **加载模块使用require方法，该方法读取一个文件并执行，返回文件内部的module.exports对象**

使用举例：
```
    //　模块定义　myModal.js
    var name = 'deejay';
    function sayName ()　{
        console.log(name);
    }
    function sayFullName(firstName) {
        console.log(firstName + name);
    }
    module.exports = {
        sayName: sayName,
        sayFullName: sayFullName,
    }



    // 加载模块
    var nameModule = require('./myModal.js');
    nameModule.sayName();
```
不同的实现对require时的路径有不同要求，一般情况可以省略js扩展名，可以使用相对路径，也可以使用绝对路径，甚至可以省略路径直接使用模块名（前提是该模块是系统内置模块）

####在浏览器端存在的问题
require是同步的，模块系统需要同步读取模块文件内容，并编译执行以得到模块接口。在服务器端实现没什么问题，但是在浏览器端实现问题却很多。

浏览器端加载js最佳最容易的方式是在document中插入script标签。但是脚本标签天生异步，传统CommonJS模块在浏览器环境中无法正常加载。

解决思路是之一是：开发一个服务器端的组件，对模块代码做静态分析，将模块与它的依赖列表一起返回给浏览器端。这很好用，但是需要服务器安装额外的组件，并因此要调整一系列底层架构。

另一种解决思路是，用一套标准模板来封装模块定义，但是对于模块应该怎么定义和怎么加载，又产生了分歧：

##AMD
Asynchronous Module Definition,异步模块定义。是一个在浏览器端模块化开发的规范。

由于不是JS原生支持，使用AMD规范进行页面开发时需要用到相应的库函数，也就是RequireJS

RequireJS主要解决2个问题：
- 1，多个JS文件可能有依赖关系，被依赖的文件需要早于依赖他的文件加载到浏览器
- 2，JS加载的时候浏览器会停止页面渲染，加载文件越多，页面失去响应时间越长

看一个使用RequireJS的例子：
```
    //定义模块  比如新建一个名为myModule.js的文件
    define(['依赖的JS模块'],function () {
        var name = 'deejay';
        function sayName() {
            console.log(name);
        }
        return {
            sayName: sayName,
        }
    });


    //要加载模块时
    require(['myModule.js'],function (my) { //my就是myModule.js return的东西
        my.sayName();
    })
```
####语法
RequireJS定义了一个函数define,它是全局变量，用来定义模块

`define(id?, dependencies?, factory);`
- 1,id： 可选参数，用来定义模块的标识，如果没有提供该参数，脚本文件名（）
- 2，dependencies： 是一个**当前模块依赖的模块名称数组**
- 3, factory：工厂方法，模块初始化要执行的函数或者对象。如果为函数，他应该只被执行一次。如果是对象，则此对象应该为模块的输出值。

在页面上使用require函数加载模块

`require([dependencies],function () {});`

require()函数接受2个参数
- 1，第一个参数是一个数组，表示所依赖的模块
- 2，第二个参数是一个回调函数，当前面指定的模块都加载完成之后，它将被调用。加载的模块会以参数形式传入该函数，从而在回调函数内部就可以使用这些模块。

require()函数在加载依赖的函数的时候是异步加载的，这样浏览器不会失去响应，它指定的回调函数，只有前面的模块都加载完成之后，才会运行，解决了依赖性的问题。
##CMD
Common Module Definition 通用模块定义,CMD规范是国内发展出来的，就像AMD有个RequireJS,CMD有个浏览器的实现SeaJS,SeaJS要解决的问题和RequireJS一样，只不过在模块定义方式和模块加载（可以说运行，解析）时机上有所不同。
###语法
Sea.js推崇一个模块一个文件，遵循统一的写法
###define

`define(id?,deps?,factory)`

因为CMD推崇：
- 1.一个文件一个模块，所以经常就用文件名作为模块id
- 2.CMD推崇依赖就近，所以一般不在define的参数中写依赖，在factory中写

factory有三个参数
`function (require,exports,module)`

###require
require是factory函数的第一个参数
`require(id)`
require是一个方法，接受模块标识作为唯一参数，用来获取其他模块提供的接口
###exports
exports是一个对象，用来向外提供模块接口
###module
module是一个对象，上面存储了与当前模块相关联的一些属性和方法

使用举例：
```
    //定义模块  比如新建一个myModule.js
    define(function (require, exports, module) {
        var $ = require('jquery.js'); // 依赖就近
        $('div').addClass('active');
    });

    //加载这个模块
    seajs.use(['myModule.js'],function () {

    });
```
AMD和CMD直观的看上去，差异就是seajs可以按需的去加载模块，而RequireJS要先写好依赖。现在基本上seajs已经废弃了，都是使用RequireJS（webpack之前）。

###AMD规范与CMD规范
```
    var modal = require('modal');
    $btn.on('click',function () {
        modal.open();
    });
    
```
要想实现这样的加载组件的效果：
###AMD规范
语法：
```
    denfine(id?,denpendencies?,factory) {}
    
//使用举例
    define('modal',['jqurey,dialog'],function ($,Dialog) {
        $('.modal').show();
        Dialog.open();
    });
    
```
实现AMD的库有RequireJS,curl,Dojo
###CommonJS规范
是服务器端的规范，NodeJS采用了这个规范。
- 1，在一个模块中，存在一个自由的变量'require'，它是一个函数。
这个require函数接受一个模块标识符
require返回外部模块所输出的API
如果出现依赖闭环，那么外部模块在被它的传递依赖所require的时候可能被并没有执行完成；在这种情况下，require返回的对象必须至少包含此外部模块在调用require函数（会进入当前模块执行环境）之前就已经准备完毕的输出。
如果请求的模块不能返回，那么require必须抛出一个错误。
- 2，在一个模块中，会存在一个名为exports的自由变量，它是一个对象，模块可以在执行的时候把自身的API加入到其中。
- 3，模块必须使用exports对象来作为输出的唯一表示。
###CMD规范
书写格式为：
```
    define(function (require,exports,module) ) {
        
    }
```
