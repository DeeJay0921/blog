---
title: BOM
date: 2017/08/20 00:00:01
cover: https://image.slidesharecdn.com/dombomajaxevent-110401021116-phpapp01/95/browser-object-model-27-728.jpg?cb=1301623910
tags: 
- 前端
- BOM
categories: 
- 前端
---
BOM
<!--more-->


##BOM

Browser Object Model

- **window.history**
控制浏览器的前进后退
console内输入,可以观察到效果
`window.history.back()`
`window.history.forward()`
`window.history.go(-1) // === back()`

- window.innerHeight
浏览器窗口内容区域高度
- window.length
针对页面中的iframe，有几个长度就是几
- **window.location**
操纵浏览器的刷新按钮和地址栏
`window.location.href = 'http://www.baidu.com'` 在当前页面打开百度,也可以写成`window.location = 'http://www.baidu.com'`
`location.protocol` 协议
`location.hostname` 域名
`location.port `端口
`location.host` 域名加端口
`location.pathname` 路径
`location.search` 返回？后面的东西
`location.hash` #后面的 锚点fragment  哈希
`location.origin` 协议加域名加端口
- window.name
a标签内设置target就能修改window.name
- **window.navigator**
浏览器的所有信息
`window.navigator.useragent` 可以用来区别不同浏览器
- window.screen
屏幕相关信息
`window.screen.availHeight`
`window,screen.height`
- window.self 
window.self是一个全局属性，var一个self变量就会覆盖window.self 

##打开新窗口
- window.open("http://www.baidu.com",'_self');
第一个参数为URL,第二个参数为windowName，第三个参数是window的一些属性features
- window.opener.location.reload()
用在iframe页面中，让打开这个iframe页面的页面刷新。
- 在页面正中央打开一个指定宽高的窗口
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <script src="test.js"></script>
</head>
<body>

<button id="btn">弹出窗口</button>
<script>
    var btn = document.querySelector('#btn');
    btn.addEventListener('click',function () {
        $.bom.openWindowAtCenter(1000,800,'http://baidu.com');
    })

</script>
</body>
</html>
```
test.js写为：
```
window.$ = function () {}
$.bom = {
    openWindowAtCenter : function (width,height,url) {
        window.open(url,'_blank',`
            width = ${width}px,
            height = ${height}px,
            screenX = ${screen.width/2 - width/2}px,
            screenY = ${screen.height/2 - height/2}px,
`)
    }
}
```

##修改查询参数
