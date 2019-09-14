---
title: JSONP--跨域
date: 2017/08/23 00:00:01
tags: 
- 前端
- 跨域
- JSONP
categories: 
- 前端
---
JSONP--跨域
<!--more-->

######如何在本地伪装一个网站
1.编辑hosts
mac：sudo vi /etc/hosts
windows:  C:\Windows\System32\drivers\etc下修改hosts文件，注意管理员权限
2. 添加一行 127.0.0.1 xxx.com
3. 保存关闭
4. 访问xxx.com:端口号
######如何监听80端口
mac： sudo http-server -c-1 -p 80
windows: 
1,管理员身份运行git bash
2,http-server -c-1 -p 80

##1.浏览器的同源策略

- 同源策略（Same origin Policy）
浏览器出于安全发面的考虑，只允许本域下的接口交互。不同源的客户端脚本在没有明确授权的情况下，不能读写对方的资源。

本域（同源）指的是：
- 同协议： 如都是http或者https
- 同域名： 如都是http://123.com/a和http://123.com/b
- 同端口： 如都是80端口

同源的比如：
http://123/com/a/b.js和http://123/com/index.php（同源）

不同源的例子：
1 http://123/com/main.js 和 https://123/com/a.php (协议不同，http和https)
2 http://123/com/main.js 和 http://bbs.123/com/a.php(域名不同，必须完全相同才可以)
3 http://123/com/main.js 和 http://123/com:8080/a.php（端口不同，第一个默认是80）
需要注意的是，**对于当前页面来说，页面存放的JS文件的域不重要， 重要的是加载该JS页面所在的什么域**


下面这4组**都不是同源的：**
1. http://zhihu.com  VS  http://www.zhihu.com    没有任何关系，两个网站
2. http://zhihu.com  VS  https://zhihu.com   
3. http://zhihu.com VS http://zhihu.com:81  端口不一样
4. http://zhihu.com  VS  http://zhihu.com.cn  

只有**字符串完全匹配的才算同源**

不同源的不允许调用ajax

#2.跨域
跨域：跨域就是向不同源的地址请求资源或者是进行操作
##3.几种跨域方式
##JSONP

html标签中script标签可以引入其他域下的JS，比如引入线上的jQuery库。利用这个特性，可以实现跨域访问接口。需要后端支持
`echo $cb . '&&' . $cb . '(' . json_encoded($ret) . ')';`
1. 定义数据处理函数_fun
2. 创建script标签，src的地址执行后端参数，最后加个参数callback=_fun
3. 服务端在接收请求后，解析参数，计算返回数据，输出fun(data)字符串
4. fun(data)会放到script标签作为js执行。此时会调用fun函数，将data作为参数

JSONP跨域举例：
JSONP的使用思路是可以通过使用**新增script标签的src来访问其他域上的js**来进行的。
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>


<ul>
    <li>内容1</li>
    <li>内容2</li>
    <li>内容3</li>
</ul>
<button>换一组</button>

<script>
    var btn = document.querySelector('button');
    var ul = document.querySelector('ul');
    var body = document.querySelector('body');
    btn.addEventListener('click',function () {
        var newScript = document.createElement('script');
        newScript.src = 'http://localhost:8080/jsonp/?callback=getNews';
        body.appendChild(newScript);
        body.removeChild(newScript);
    })
    function getNews (arr) {
        var html = '';
        for (var i = 0; i < arr.length; i ++) {
            html += '<li>' + arr[i] + '</li>';
        }
        ul.innerHTML = html;
    }
</script>

</body>
</html>
```
上面的是前端代码，那么核心思想在于
```
        newScript.src = 'http://localhost:8080/jsonp/?callback=getNews';
        body.appendChild(newScript);
```
创建一个script标签，设置src可以请求别的域的js，在src中设置好域，接口和回调参数等。在后端代码中做相应的处理即可
```
app.get('/jsonp',function (req,res) {
    var content = [
        "内容10",
        "内容4",
        "内容5",
        "内容6",
        "内容7",
        "内容8",
        "内容9",
    ];
    var data = [];
    for (var i = 0; i < 3; i ++) {
        var index = parseInt(Math.random()*content.length);
        data.push(content[index]);
        content.splice(index,1);
    }

    //进行后端的jsonp跨域处理
    var cb = req.query.callback;
    if (cb) { //如果存在回调函数的话
        res.send(cb + '(' + JSON.stringify(data) + ')' ); //拼接成最后能调用的语法格式
    }
    else {
        res.send(data);
    }
})
```
在后端中，我们拼接成回调函数调用的形式返回给前端，进行执行。
由于返回的是字符串要进行解析执行，所以**JSONP格式的跨域有被XSS注入的风险。处理也较为麻烦**。


##CORS
跨域资源共享（Cross-origin Resource Sharing）,**是一种ajax跨域请求资源的方式**，IE10以上支持。
实现方式：当使用XMLHttpRequest发送请求时，浏览器发现该请求不符合同源策略，会给该请求加一个请求头：Origin，后台进行一系列处理，如果确定接受请求的话就在返回结果中加入一个响应头:Access-Control-Allow-Origin;浏览器判断该响应头中是否包含Origin的值，如果有则处理响应，就能拿到响应数据，如果不包含浏览器直接驳回，无法拿到响应数据。
所以CORS的表象就是让你觉得它和同源的ajax请求没啥区别，代码完全一样


cors使用举例：
修改我们的hosts，使得当前域名可以通过index1.com:8080进行访问
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>


<ul>
    <li>内容1</li>
    <li>内容2</li>
    <li>内容3</li>
</ul>
<button>换一组</button>

<script>
    var btn = document.querySelector('button');
    var ul = document.querySelector('ul');
    btn.addEventListener('click',function () {
        var xhr = new XMLHttpRequest();
        xhr.open('get','http://localhost:8080/cors',true);
        xhr.send();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && (xhr.status ===200 || xhr.status === 304)) {
                var news = JSON.parse(xhr.responseText);
                render(news);

            }
        }
    })
    function render (arr) {
        var html = '';
        for (var i = 0; i < arr.length; i ++) {
            html += '<li>' + arr[i] + '</li>';
        }
        ul.innerHTML = html;
    }
</script>

</body>
</html>
```
这是前端代码，注意到我们在ajax发送中规定的接口是`xhr.open('get','http://localhost:8080/cors',true);`向http://localhost:8080发起请求，我们当前的hosts经过修改，url为http://index1.com:8080,所以为不同源，此时就会报错。这时候我们需要在后端进行设置，以server-mock为例，后端代码如下：
```
app.get('/cors',function (req,res) {
    var content = [
        "内容10",
        "内容4",
        "内容5",
        "内容6",
        "内容7",
        "内容8",
        "内容9",
    ];
    var data = [];
    for (var i = 0; i < 3; i ++) {
        var index = parseInt(Math.random()*content.length);
        data.push(content[index]);
        content.splice(index,1);
    }
    res.header("Access-Control-Allow-Origin","http://index1.com:8080");// 后端设置一个响应头，第二个参数代表只允许规定的域访问
    // res.header("Access-Control-Allow-Origin","*");//写成*代表不管什么域都允许请求数据
    res.send(data);
})
```
我们在后端中增加了`res.header("Access-Control-Allow-Origin","http://index1.com:8080");`设置了一个响应头，第二个参数代表只允许规定的域访问，也可以写成`*`代表所有其他域都允许请求数据。我们这时候就能在Network里面看到Response Headers里面的Access-Control-Allow-Origin被设置成了http://index1.com:8080，而Request Headers里面也有当浏览器发现该请求不符合同源策略，该请求加一个请求头Origin，其值为http://index1.com:8080。有了这个响应头，我们就能成功的跨域请求数据了。
CORS 的优点是使用ajax，**处理特别简单**。缺点是**ie10之后才兼容**。

##降域
对于同一网站下的二级域名等可以使用降域方法进行跨域
`document.domain = xxx.com;`

比如说http://b.123.com:8080/b.html的iframe嵌套在http://a.123.com:8080/a.html的页面内，此时两个子域不能进行跨域，但是只要给2个子域都加上
`document.domain = '123.com';`
就能实现跨域

##postMessage

对于不同的域下发送一个数据，如果对方接受任何一个数据，那就可以去使用，没有监听数据的话则不使用。
由页面A向页面B发送请求，在页面B中监听这个message事件，监听请求并且获取A发送的数据，即可实现跨域请求。

应用举例：
由http://127.0.0.1:8080/post.html页面进行发送数据
由http://localhost:8080/receive.html进行监听数据

http://127.0.0.1:8080/post.html为:
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>

<div class="ct">
    <h1>使用postMessage实现跨域</h1>
    <div class="main">
        <input type="text" placeholder="http://127.0.0.1:8080/post.html">
    </div>
    <iframe src="http://localhost:8080/receive.html" frameborder="0" ></iframe>
</div>
<script>
    var input = document.querySelector('input');
    input.addEventListener('input',function () {
        console.log(this.value);
        window.frames[0].postMessage(this.value,"*");
//        window.frame[0]代表receive.html
    })
    window.addEventListener('message',function (e) {// 注意监听数据时对象是window
        input.value = e.data;
    })
</script>
</body>
</html>
```
receive.html为

```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>
<input type="text" placeholder="http://localhost:8080/receive.html">
<script>
    var input = document.querySelector('input');
    window.addEventListener('message',function (e) { // 注意监听数据时对象是window
        input.value = e.data;
    })
    input.addEventListener('input',function() {
        window.parent.postMessage(this.value,"*");
//        window.parent代表包含当前iframe的页面，即post.html
    })
</script>
</body>
</html>
```
