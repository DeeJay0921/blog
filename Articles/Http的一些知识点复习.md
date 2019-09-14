---
title: Http的一些知识点复习
date: 2017/07/29 19:22:24
tags: 
- Http
categories: 
- 前端
---
Http的一些知识点复习
<!--more-->

之前学习过的[http的基本知识](https://www.jianshu.com/p/e6a8a05c77b8)，在学习Spring boot的时候复习一下相关概念

相关阅读：
[HTTP状态码](https://zh.wikipedia.org/wiki/HTTP%E7%8A%B6%E6%80%81%E7%A0%81 "null")
[HTTP协议入门 - 阮一峰](http://www.ruanyifeng.com/blog/2016/08/http.html "null")
[HTTP协议WIKI](http://www.ruanyifeng.com/blog/2016/08/http.html "null")
[HTTP RFC](https://tools.ietf.org/html/rfc7230 "null")

# HTTP协议
## HTTP协议是互联网的基础协议
- 本质上是Web前端程序和Web后端程序的通信的协议
- 定义了前端给后端发送请求的格式
- 同时也定义了后端解析前端发来的请求(HTTP请求)的方式
- 使用程序语言来描述, HTTP协议给后端程序定义了一个接口

## HTTP协议格式
### HTTP请求格式
#### 基本元素
- 请求路径 --- URL, 例如: `https://xxx.com/yyy/123`
- 请求操作命令(动词): GET, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH
- 客户端标记 User-Agent, 值的例子: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5)
- 传输的内容格式: Content-Type: application/json
- 接收类型 Accept, 值的例子: text/plain, text/html, image/jpeg
- Cookie: 用于传送和状态相关的信息的键值对


## HTTP回应格式
### 基本元素
- HTTP协议版本: HTTP/1.1
- 返回状态 Status: 200, 201, 404 (详细示例看后文)
- 时间, 例如: Sat, 29 Sep 2018 07:24:53 GMT
- 返回内容类型: text/plain, text/html, image/jpeg

### 示例
```
HTTP/1.1 200 OK
Date: Sat, 29 Sep 2018 07:24:53 GMT
Content-Type: image/vnd.microsoft.icon
Content-Length: 3638
Connection: keep-alive
Server: nginx/1.6.2
Last-Modified: Thu, 24 May 2018 05:56:53 GMT
ETag: "e36-56ced517f0753"
Cache-Control: max-age=2592000, public
X-ORCA-Accelerator: HIT from 006.mul.lax01.us.krill.zenlogic.net
X-Cache: HIT from 006.mul.lax01.us.krill.zenlogic.net
Accept-Ranges: bytes
```

### 总结
- 一个HTTP请求在语义上表达对一个互联网资源(URL)的操作(GET, POST, PUT)
- 然后Web后端返回资源操作的结果和相关信息

## 常见的Content-Type字段值
- text/plain
- text/html
- text/css
- image/jpeg
- image/png
- image/svg+xml
- audio/mp4
- video/mp4
- application/javascript
- application/pdf
- application/zip
- application/atom+xml

## HTTP状态码 Status

[HTTP状态码](https://zh.wikipedia.org/wiki/HTTP%E7%8A%B6%E6%80%81%E7%A0%81 "null")

*   1xx消息——请求已被服务器接收，继续处理
*   2xx成功——请求已成功被服务器接收、理解、并接受
*   3xx重定向——需要后续操作才能完成这一请求
*   4xx请求错误——请求含有词法错误或者无法被执行
*   5xx服务器错误——服务器在处理某个正确请求时发生错误

## 如何理解HTTP协议是无状态
### HTTP请求的实现应该是无状态的

- 每个请求都是独立的
- 每个请求都需要显示的附带状态信息. 比如告诉后端, 我是谁 (认证), 我现在在什么状态

### Spring实现响应GET, POST, PUT, DELETE操作请求
- GET /users HTTP/1.1
### 使用Postman发送GET, POST, PUT, DELETE操作请求
- 操作相关注解 @PostMapping, @GetMapping, @PutMapping, Deletemapping
- 参数相关注解 @RequestBody, @PathVariable



