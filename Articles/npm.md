---
title: npm
date: 2017/09/02 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Npm-logo.svg/1200px-Npm-logo.svg.png
tags: 
- 前端
- JS
- npm
- JS module
categories: 
- 前端
---
Node Packcge Manager,node包管理器，主要功能是管理node包的安装，卸载，更新，查看，搜索，发布等。
<!--more-->

##npm是什么
Node Packcge Manager,node包管理器，主要功能是管理node包的安装，卸载，更新，查看，搜索，发布等。

[npm官网](https://www.npmjs.com)
##npm包的命令行指令
node包的安装分为**本地安装**和**全局安装**

- 本地安装：`npm install pkg`
package会被下载到当前所在目录，也只能在当前目录下使用

- 全局安装： `npm install -g pkg`
package会被下载到特定的系统目录下，安装的package能够在所有目录下使用

安装之后，会在当前目录出现一个node_modules文件夹，当我们require('模块名')的时候，会先在当前目录下的node_modules文件夹下去找package.json文件中的main属性对应的参数js文件。去加载这个js文件作为模块。如果当前目录找不到node_modules就会向上一级去寻找到根目录.



- **npm install pkg --save**
自己要发布新的包，并且自己发布的包中引用了别人的包的时候，就要使用这个指令将别人的包下载下来，这条指令会使得package.json中多一个dependencies的属性，包含了引用的包的名字以及版本号。这样，别人在使用我们自己发布的包的时候，不需要将我们的包中引用的包一个个下载下来，直接使用**npm install**就能安装所有引用的模块。
- **npm install --save-dev pkg**
我们使用上述指令，package.json中会多一个devDependencies属性，这个属性中一般是自己在做开发测试的时候用的测试工具，别人引用的时候不会用到。当别人使用你的包时，进行`npm install`时，npm会把dependencies中的模块也一起下载下来，而不会把devDependencies的模块下载下来。

##几个demo演示npm的用法
- **demo1  一般模块之间的引用**：
a.js:
```
// var aSayHello = require('./b');//得到的是exports对象，此时aSayHello不是一个函数，所以要写成
var aSayHello = require('./b').bSay; // 获取到的是exports对象，这个exports对象的bSay属性才是在b.js中定义的bSayHello函数
aSayHello();
```
b:js:
```
var bSayHello =  function () {
    console.log('hello deejay');
}
exports.bSay = bSayHello;
```
demo1里的注意事项，我们require得到的是exports这个对象，要使用赋给exports对象的属性，那就还要写上属性。另外require('./a')和require('./a.js')都是可以的，但是写成require('a')就是不对的。


- **demo2   引用外部的包：**
demo2有个实际需求，把markdown格式的文档转化为html格式的文档,我们需要使用外部的node包，进入[npm官网](https://www.npmjs.com),搜索相关工具，比如搜索marked，使用npm下载`npm install marked`,安装完成之后

a.js:
```
//根据文档用法来进行使用
var aMarked = require('marked');
var str = aMarked('# hello');
console.log(str);
```

demo2中的require('marked')，其中模块名就是npm安装的模块名，运行的时候会先在当前目录的node_modules文件夹下去找这个模块，如果找不到再去上一级目录去找，直到找到为止
- **demo3    自己发布包以及引用别人的包，--save：**
demo3的需求是，自己创建一个包，这个包引用别人的包，并且自己也要可以发布给别人使用。想发布的话，就要写package.json，可以直接npm init,可以依次输入自己创建的包的各项信息。这样就创建好了package.json。然后写好readme.md，再去创建index.js
index.js:
```
var marked = require('marked');
var str = marked('# hello deejay');
console.log(str);

module.exports = str;
```
module.exports就是我们暴露出来可以给别人使用的东西。另外我们是在package.json中的main中指定了比人是从index.js中读取的，所以在index.js中写代码。那么我们在demo3中用到了marked的包，这个包是别人的，所以我们需要进行测试，将marked包下载下来，但是指令不一样，指令写成：**npm install --save marked**，下载下来我们发现package.json中多了dependencies的属性，包含了引用的包的名字以及版本号。这样，别人在使用我们自己发布的包的时候，不需要将我们的包中引用的包一个个下载下来，直接使用**npm install**就能安装所有引用的模块。
上传的时候，使用**npm login**来进行登录，然后进行**npm publish**上传我们的包

- **demo4   全局安装，实现一个命令行工具：**
demo4要实现一个命令行工具，在任何目录下都能实现。要使用npm install -g.

a.js:
```
#!/usr/bin/env node

console.log('hello');
```
\#!/usr/bin/env node代表指明要用node来执行当前的文件。
设置package.json,添加bin属性
package.json:
```
{
  "name": "deejay-demo",
  "version": "1.0.0",
  "description": "print ''\u001b[Dhello'",
  "main": "./a.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "bin": {
    "deejay": "./a.js"
  },
  "author": "deejay",
  "license": "ISC"
}

```
bin属性中的deejay代表之后命令行的指令，发布之后全局安装到本地之后只需要输入deejay就会使用node运行"./a.js"。
安装时提示有：
C:\Users\DeeJay_Y\AppData\Roaming\npm\deejay -> C:\Users\DeeJay_Y\AppData\Roaming\npm\node_modules\deejay-demo\a.js
那么C:\Users\DeeJay_Y\AppData\Roaming\npm\node_modules就全是全局命令行工具所在的地方，npm install -g就是安装到这里的。
注意：这里的全局安装之后，如果不是在当前目录下的文件的话，**要使用模块的话是require不到的**。因为require只会在当前目录一级一级直到跟目录，是找不到-g下的目录的。
- **demo5    发布一个天气预报的命令行工具：**
当用户输入weather时，得到本地的天气，当用户输入weather 目标城市 的时候，得到目标城市的天气。
要想知道用户输入的目标城市，要用到process.argv。
####process.argv
process模块用来与当前进程互动，可以通过全局变量process访问，不必使用require命令加载。它是一个EventEmitter对象的实例。

process.argv返回命令行脚本的各个参数组成的数组。


先初始化package.json,然后加个bin属性：
package.json
```
{
  "name": "deejay-weather",
  "version": "1.0.0",
  "description": "'weather-forecast'",
  "main": "./weather.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "bin": {
    "weather": "./weather.js"
  },
  "author": "deejay",
  "license": "ISC",
  "keywords": [
    "weather"
  ],
  "dependencies": {
    "axios": "^0.16.2"
  }
}

```
weather.js:

使用axios来发送请求获得数据
```
#!/usr/bin/env node

var axios = require('axios');
// console.log(process.argv); // process.argv[2]代表了当前用户输入的weather 后面的目标城市


var data = {}; //用来存储用户输入的目标城市的天气
if (process.argv[2]) { // 即如果用户输入了目标城市的话
    data.params = {
        city: process.argv[2],
    }
}

axios.get('http://api.jirengu.com/weather.php', data)
    .then(function (response) {
        var weather = response.data.results[0].weather_data[0];
        console.log(response.data.results[0].currentCity);
        console.log('pm2.5: ' + response.data.results[0].pm25);
        console.log(weather.date);
        console.log(weather.temperature);
        console.log(weather.weather);
    })
    .catch(function (error) {
        console.log(error);
    });


```

###PS：关于package.json中的script。

可以执行一些全局的代码或者是命令行。
```
"scripts": {
    "test": "echo deejay",
    "start": "echo hello"
},
```
其中test和start是默认的，可以直接通过npm test和npm start来运行。
但是如果要添加新的命令，如build,就只能通过npm run build来运行。
