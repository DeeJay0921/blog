---
title: Web前后端基础架构原理
date: 2019/09/03 00:00:01
tags: 
- Web
- HTTP
- TCP
categories: 
- Java
---
Web前后端基础架构原理
<!--more-->

## 计算机网络是如何工作的
- 著名问题： 在打开一个URL的时候发生了什么？
     - 主机
     主机跟一般的计算机没什么区别，每个主机其IP（Internet Protocal）地址就是一个32位的地址（即一个int值），由于一个byte为-128到127，所以是255个数，这就是为什么主机的IP每位不能超过255。由于一个int值最大也就是42亿，所以会有不够用的情况(IPV4)。所以有了IPV6（增加了IP地址的位数）
     - 域名与DNS
    DNS(Domain Name Service)即域名服务,访问一个域名时，会去向DNS查询对应的IP地址和端口，来进行访问，当我们计算机本地的host文件里面有对应的域名配置的时候，就不会去查询DNS了，而是直接取我们本地配置的IP地址直接访问
    - 端口
    端口最多有65536个，HTTPS协议的默认端口为443，HTTP的默认为80
- TCP协议
Transport Control Protocal,可以理解为一条**双向的**高速公路，是一个基于字节流的一个协议，当我们访问一个域名时，其实就是在自己的机器上的一个端口和远程主机的一个端口之间开辟了一个双向的用于传输数据的高速公路，**双方都可以同时的发送和接收数据**。TCP协议其实只规定了**字节流在网络上如何发送和接收**
- HTTP协议
TCP协议之上有HTTP协议，Hyper Text Transport Prototal,超文本传输协议，超文本意味着不仅可以传输文本还可以发送图片等等。

## 使用Java发起HTTP请求
- 可以使用HTTPClient来进行请求，[相关文档](http://hc.apache.org/httpcomponents-client-ga/quickstart.html)
- 可以通过Jsoup来进行html的解析

## 关于Cookie
由于HTTP请求是**无状态**的，每一个请求都是独立的。所以需要状态维护的话就需要cookie。

当第一次进行类似登录的请求时，登录成功之后Server端的响应头会有一个Set-Cookie:xxxx，从此之后，之后这个域名所有发出的请求都会带有这个字符返回的字符。
