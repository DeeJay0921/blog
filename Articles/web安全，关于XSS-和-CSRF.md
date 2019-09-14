---
title: web安全，关于XSS-和-CSRF
date: 2017/10/08 00:00:01
tags: 
- 前端
- web安全
- XSS
- CSRF
categories: 
- 前端
---
web安全，关于XSS-和-CSRF
<!--more-->

#XSS
XSS:  Cross-Site Scripting

###原理概述：

简单来说

1. 正常用户 A 提交正常内容，显示在另一个用户 B 的网页上，没有问题。

2. 恶意用户 H 提交恶意内容，显示在另一个用户 B 的网页上，对 B 的网页随意篡改。

造成 XSS 有几个要点：

1. 恶意用户可以提交内容

2. 提交的内容可以显示在另一个用户的页面上

3. 这些内容未经过滤，直接运行在另一个用户的页面上

###举个例子

假设我们有一个评论系统。
用户 A 提交评论「Hello你好」到服务器，然后用户 B 来访问网站，看到了 A 的评论「Hello你好」，这里没有 XSS。

恶意用户 H 提交评论「<script>console.log(document.cookie)</script>」，然后用户 B 来访问网站，这段脚本在 B 的浏览器直接执行，恶意用户 H 的脚本就可以任意操作 B 的 cookie，而 B 对此毫无察觉。有了 cookie，恶意用户 H 就可以伪造 B 的登录信息，随意访问 B 的隐私了。而 B 始终被蒙在鼓里。

###XSS的成因及解决办法

一般XSS成因跟后端和前端都有关系

#####后端方面：

模板问题：

```
<p>
评论内容：<?php echo $content; ?>
</p>
```

$content 的内容，没有经过任何过滤，原样输出。

要解决这个原因，只需要**后台输出的时候，将可疑的符号 < 符号变成 < （HTML实体）就行**。

#####前端方面：

```
$p.html(content)

//或者

$p = $('<p>'+ content +'</p>')
```

content 内容又被原样输出了。**解决办法就是不要自己拼 HTML，尽量使用 text 方法。如果一定要使用 HTML，就把可疑符号变成 HTML 实体**。

#CSRF



##总结：

XSS的防范措施：后端模板方面，要将可疑符号<替换成HTML实体< ，前端在写代码的时候避免使用html方法或者拼接字符串。

#CSRF

Cross Site Request Forgery 跨站请求伪造

###原理：

攻击者构造网站后台某个功能接口的请求地址，诱导用户去点击或者用特殊方法让该请求地址自动加载。用户在登录状态下这个请求被服务端接收后会被误以为是用户合法的操作。对于 GET 形式的接口地址可轻易被攻击，对于 POST 形式的接口地址也不是百分百安全，攻击者可诱导用户进入带 Form 表单可用POST方式提交参数的页面。

###解决方案：

1. 服务端在收到路由请求时，生成一个随机数，在渲染请求页面时把随机数埋入页面（一般埋入 form 表单内，<input type="hidden" name="_csrf_token" value="xxxx">）
2. 服务端设置setCookie，把该随机数作为cookie或者session种入用户浏览器
3. 当用户发送 GET 或者 POST 请求时带上_csrf_token参数（对于 Form 表单直接提交即可，因为会自动把当前表单内所有的 input 提交给后台，包括_csrf_token）
4. 后台在接受到请求后解析请求的cookie获取_csrf_token的值，然后和用户请求提交的_csrf_token做个比较，如果相等表示请求是合法的。

[参考文章](https://zhuanlan.zhihu.com/p/22521378?refer=study-fe)
