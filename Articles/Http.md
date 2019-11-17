---
title: Http
date: 2017/10/08 00:00:01
cover: https://sdtimes.com/wp-content/uploads/2015/02/0218.sdt-http2.jpg
tags: 
- 前端
- Http
categories: 
- 前端
---
Http协议（HyperText Transfer Protocol）是互联网上应用最为广泛的一种网络协议，所有的www文件都必须遵守这个标准。

提供了一种发布和接收HTML页面的方法。
<!--more-->

参考文章：
[阮一峰讲RESTful API](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)
[简述浏览器缓存是如何控制的](https://zhuanlan.zhihu.com/p/23299600?refer=study-fe)
#Http

Http协议（HyperText Transfer Protocol）是互联网上应用最为广泛的一种网络协议，所有的www文件都必须遵守这个标准。

提供了一种发布和接收HTML页面的方法。

##技术架构
http是一个客户端和服务端请求和应答的标准（TCP）。客户端是终端用户，服务器端是网站。

通过使用web服务器、网络爬虫或者其他的工具，客户端发起一个到服务器上指定端口（默认端口为80）的http请求。我们称这个客户端为用户代理（user agent）。应答的服务器上存储着一些资源，比如HTML文件和图像。我们称这个应答服务器为源服务器（origin server）。

在用户代理比如浏览器和源服务器之间存在着多个中间层，比如代理，网关，或者隧道（tunnels）。

##工作原理
一个HTTP操作称为一个事务，其工作工程分为4步：
- 1，客户机与服务器需要建立连接。只要单击某个超级链接，http的工作就开始了。
- 2，建立连接后，客户机发送一个请求给服务器，请求方式的格式为：统一资源标识符（URL）、协议版本号、后边是MIME信息，包括请求修饰符、客户机信息和可能的内容。
- 3，服务器接到请求后，给予相应的响应信息，其格式为一个状态行，包括信息的协议版本号、一个成功或者错误的代码，后边是MIME信息，包括服务器信息，实体信息和可能的内容。
- 4，客户端接收服务器所返回的信息通过浏览器显示在用户的显示屏上，然后客户机与服务器断开连接。

##URI 和 URL

- URI：统一资源标识符

- URL：统一资源定位符

与URI相比我们更熟悉URL，URL是使用浏览器等访问web页面的时候需要输入的网页地址

`http://www.baidu.com`

URI是更通用的资源标识符，URL是它的一个子集

URI由两个主要的子集构成

URL：通过描述资源的位置来描述资源

URN：通过名字来识别资源，和位置无关(一般不用)

##URL

一般我们使用的还是URL,我们常见的URL主要由三部分组成

- 1,方案，也就是我们常说的协议
- 2,服务器位置
- 3,资源路径

看个例子
`http://deejay0921.github.io/resume.index.html`

通常的URL由9部分组成

`<scheme>://<user>:<password>@<host>:<port>/<path>;<params>?<query>#<hash>`

- 对于web页面来说最常用的协议就是http和https
- user和password现在不常见了，不会在URL明文书写用户名和密码了，都是通过登录的方式
- 主机可以是IPO地址过着域名
- 端口号用来区分主机上的进程，方便找到web服务器，http默认是80
- path是资源的路径，也就是存放位置，不一定和物理路径完全对应，符合web服务器路由约定即可
- params，在一些协议中需要参数来访问资源，例如ftp是二进制还是文本传输，参数是名值对，用;隔开
- query：这个是get请求最常用的传递参数方式了 ?a=1&b=2&=3
- hash也成为片段，设计为标识文档的一部分，很多MVVM框架用作了路由功能

采用HTTP协议的时候，协议方案就是http，除此之外还有ftp、mailto、file等。看几个例子

`ftp://ftp.is.co.za.rfc/rfc1808.txt`
`http://deejay0921.github.io/resume.index.html`
`mailto:deejay0921@gmail.com`
`telnet://192.0.2.16:80`

上述代表着不同的协议

##相对URL
相对URL是URL一部分，从路径开始，前面某人使用当前文档的设置

`./image/logo.png`
`../script/a.js`
`/css/main.css`

##客户端向服务器发送请求的方法：
最常见的是GET和POST
### GET
GET是最常用的方法，通常用于请求服务器发送某个资源

我们平时在浏览器输入网页地址，就是给服务器发送了一个get请求，希望得到这个网页
###POST
POST用于想服务器发送数据，通常用来支持HTML的**表单**（input、select、textarea），表单中的数据会被发送到服务器
###HEAD

HEAD方法和GET类似，但是在服务器的响应中没有资源的内容，只有资源的一些基本信息，主要用于

- 在不获取资源的情况下获取资源信息（类型、大小等）
- 通过状态码产看资源是否存在
- 通过查看首部，测试资源是否被修改了

一般不用于得到文件本身，而是得到文件的一些相关的信息
###PUT
和GET从服务器获取资源相反，PUT用于想服务器写入资源。PUT的语义就是让服务器用请求的主体部分创建一个请求URL命名的文档，如果存在就替换
当然处于安全原因，并不是所有的服务器都实现，当然最近大热的[RESTful API](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)使它有了用武之地

###DELETE

DELETE方法用于要求服务器删除请求的URL，和PUT一样，服务器可能会不支持
###TRACE

客户端发送一个请求的时候，这个请求可能会穿过防火墙、代理、网关和一些其它应用程序，没个中间节点都可能修改HTTP请求，TRACE方法允许客户端在最终请求发往服务器的时候，看看它变成了什么样子

TRACE请求会在目的服务器端发送一个“闭环”诊断，行程最后一站服务器会弹回一条TRACE响应，并在响应主题中携带它收到的原始请求报文

###OPTIONS

OPTIONS方法用于请求 web服务器告知其支持的各种功能，即看当前服务器支持哪些方法

##状态码 Status Code

完整的 HTTP 1.1规范说明书来自于RFC 2616，HTTP 1.1的状态码被标记为新特性，用来表示请求的结果，状态码被分为五大类：

- 100-199 用于指定客户端应相应的某些动作。
- 200-299 用于表示请求成功。
-  300-399 用于已经移动的文件并且常被包含在定位头信息中指定新的地址信息。
- 400-499 用于指出客户端的错误。
- 500-599 用于支持服务器错误。

**常见的状态码有 200/206/301/302/304/403/404/500/502/504**

状态代码 | 状态信息 | 含义
- | - | -
100 |	Continue |	初始的请求已经接受，客户应当继续发送请求的其余部分。（HTTP 1.1新）
101	 |Switching Protocols |	服务器将遵从客户的请求转换到另外一种协议（HTTP 1.1新）
**200**	 |OK |	一切正常，对GET和POST请求的应答文档跟在后面。
201	 |Created	 |服务器已经创建了文档，Location头给出了它的URL。
202	 |Accepted |	已经接受请求，但处理尚未完成。
203 |	Non-Authoritative Information	 |文档已经正常地返回，但一些应答头可能不正确，因为使用的是文档的拷贝（HTTP 1.1新）。
204 |	No Content |	没有新文档，浏览器应该继续显示原来的文档。如果用户定期地刷新页面，而Servlet可以确定用户文档足够新，这个状态代码是很有用的。
205	 |Reset Content |	没有新的内容，但浏览器应该重置它所显示的内容。用来强制浏览器清除表单输入内容（HTTP 1.1新）。
**206** |	Partial Content |	客户发送了一个带有Range头的GET请求，服务器完成了它（HTTP 1.1新）。
300 |	Multiple Choices |	客户请求的文档可以在多个位置找到，这些位置已经在返回的文档内列出。如果服务器要提出优先选择，则应该在Location应答头指明。
**301**	 |Moved Permanently	 |客户请求的文档在其他地方，新的URL在Location头中给出，浏览器应该自动地访问新的URL。
**302**	 |Found |	类似于301， 但新的URL应该被视为临时性的替代，而不是永久性的。注意，在HTTP1.0中对应的状态信息是“Moved Temporatily”。出现该状态代码时，浏览器能够自动访问新的URL，因此它是一个很有用的状态代码。注意这个状态代码有时候可以和301替换使用。例如，如果浏览器错误地请求http://host/~user（缺少了后面的斜杠），有的服务器 返回301，有的则返回302。严格地说，我们只能假定只有当原来的请求是GET时浏览器才会自动重定向。请参见307。
303 |	See Other	 |类似于301/302，不同之处在于，如果原来的请求是POST，Location头指定的重定向目标文档应该通过GET提取（HTTP 1.1新）。
**304**	 |Not Modified |	客户端有缓冲的文档并发出了一个条件性的请求（一般是提供If-Modified-Since头表示客户只想比指定日期更新的文档）。服务器告 诉客户，原来缓冲的文档还可以继续使用。
305 |	Use Proxy |	客户请求的文档应该通过Location头所指明的代理服务器提取（HTTP 1.1新）。
307	 |Temporary Redirect |	和302 （Found）相同。许多浏览器会错误地响应302应答进行重定向，即使原来的请求是POST，即使它实际上只能在POST请求的应答是303时才能重定 向。由于这个原因，HTTP 1.1新增了307，以便更加清除地区分几个状态代码：当出现303应答时，浏览器可以跟随重定向的GET和POST请求；如果是307应答，则浏览器只 能跟随对GET请求的重定向。（HTTP 1.1新）
400	 |Bad Request	 |请求出现语法错误。
401 |	Unauthorized |	客户试图未经授权访问受密码保护的页面。应答中会包含一个WWW-Authenticate头，浏览器据此显示用户名字/密码对话框，然后在填 写合适的Authorization头后再次发出请求。
**403** |	Forbidden |	资源不可用。服务器理解客户的请求，但拒绝处理它。通常由于服务器上文件或目录的权限设置导致。
**404**	 |Not Found	 |无法找到指定位置的资源。这也是一个常用的应答。
405	 |Method Not Allowed	 |请求方法（GET、POST、HEAD、DELETE、PUT、TRACE等）对指定的资源不适用。（HTTP 1.1新）
406 |	Not Acceptable |	指定的资源已经找到，但它的MIME类型和客户在Accpet头中所指定的不兼容（HTTP 1.1新）。
407 |	Proxy Authentication Required	 |类似于401，表示客户必须先经过代理服务器的授权。（HTTP 1.1新）
408 |	Request Timeout |	在服务器许可的等待时间内，客户一直没有发出任何请求。客户可以在以后重复同一请求。（HTTP 1.1新）
409	 |Conflict |	通常和PUT请求有关。由于请求和资源的当前状态相冲突，因此请求不能成功。（HTTP 1.1新）
410	 |Gone	 |所请求的文档已经不再可用，而且服务器不知道应该重定向到哪一个地址。它和404的不同在于，返回407表示文档永久地离开了指定的位置，而 404表示由于未知的原因文档不可用。（HTTP 1.1新）
411 |	Length Required	 |服务器不能处理请求，除非客户发送一个Content-Length头。（HTTP 1.1新）
412	 |Precondition Failed	 |请求头中指定的一些前提条件失败（HTTP 1.1新）。
413 |	Request Entity Too Large |	目标文档的大小超过服务器当前愿意处理的大小。如果服务器认为自己能够稍后再处理该请求，则应该提供一个Retry-After头（HTTP 1.1新）。
414 |	Request URI Too Long |	URI太长（HTTP 1.1新）。
416	 |Requested Range Not Satisfiable |	服务器不能满足客户在请求中指定的Range头。（HTTP 1.1新）
**500** |	Internal Server Error |	服务器遇到了意料不到的情况，不能完成客户的请求。
501 |	Not Implemented |	服务器不支持实现请求所需要的功能。例如，客户发出了一个服务器不支持的PUT请求。
**502** |	Bad Gateway |	服务器作为网关或者代理时，为了完成请求访问下一个服务器，但该服务器返回了非法的应答。
503 |	Service Unavailable |	服务器由于维护或者负载过重未能应答。例如，Servlet可能在数据库连接池已满的情况下返回503。服务器返回503时可以提供一个 Retry-After头。
**504** |	Gateway Timeout	 |由作为代理或网关的服务器使用，表示不能及时地从远程服务器获得应答。（HTTP 1.1新）
505 |	HTTP Version Not Supported |	服务器不支持请求中所指明的HTTP版本。（HTTP 1.1新）

##报文 HTTP message
HTTP应用程序之间发送的数据块，我们在输入URL的时候，浏览器不仅仅发送了这些URL，还有附加的一些信息，就叫报文，比如requestHeader等。服务器和客户端相互发送的都叫报文。

HTTP报文是在HTTP应用程序之间发送的数据块。这些数据块以一些文本形式的元信息开头，描述报文的内容及含义，后面跟着可选的数据部分

##组成
HTTP报文是简单的格式化数据块，没个报文都包含一条来自客户端的请求或者一条来自服务器的响应，由3个部分组成

- 对报文进行描述的起始行 —— start line
- 包含属性的首部块 —— header
- 可选的包含数据的主体部分 —— body

```
HTTP/1.0 200 OK
content-type: text/plain
content-length: 19

Hi, I'm a message
```
###语法

报文分为请求报文和响应报文2种

####请求报文：

向web服务器请求一个动作
```
<method><request-URL><version>
<headers>

<entity-body>
```

![request.png](http://upload-images.jianshu.io/upload_images/7113407-3820f85f02e49fef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


例如get方法的start line :  GET http://www.baidu.com HTTP/1.1
一般GET请求报文没body
####响应报文

讲请求结果返回给客户端
```
<version><status><reason-phrase>
<headers>

<entity-body>

```


![response.png](http://upload-images.jianshu.io/upload_images/7113407-dcb7a6065e177fd4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
首部和方法配合，共同决定了服务器和客户端能做什么

一般开发者工具中查看到的ReponseHeader 点view source可以查看到第一部分start line和第二部分，第三部分回车不显示，而第四部分在Response里

###通用首部

客户端和服务器都可以实用的就是通用首部

首部 | 描述
-- | --
Connection|	客户端和服务器是否保持连接
Date	|日期，报文创建时间
Update|	给出了发送端可能想要升级使用新版本或协议
Via	|显示了报文经过的中间节点（代理、网关）
Trailer	|如果报文采用分块传输编码方式，可以利用这个首部列出位于报文trailer部分的首部集合
Trailer-Encoding|	告诉接收端对报文采用什么编码格式
Cache-Control|	随报文传送缓存指示
Pragma	|早期的随报文传送指示方式

###请求首部

首部|	描述
-- | --
Client-IP	|客户端IP
From	|客户端邮件地址
Host	|接收请求的服务器的主机名和端口号
Referer	|提供了包含当前请求URI的文档的URL，告诉服务器自己来源
User—Agent	|发起请求的客户端应用程序
Accept	|告诉服务器能够发送那些媒体类型
Accept-Charset	|告诉服务器能够发送那些字符集
Accept-Encoding	|告诉服务器能够发送那些编码
Accept-Language	|告诉服务器能够发送那些语言
Expect	|允许客户端列出请求所要求的服务器行为
If-Match|	如果ETag和文档当前ETag匹配，就获取文档
If-Modified-Since|	除非在某个指定日期之后修改过，否则限制这个请求
If-None-Match|	如果ETag和当前文档ETag不符合，获取资源
If-Range	|允许对文档否个范围内的条件请求
If-Unmodified-Since|	在某个指定日期之后没有修改过，否则现在请求
Cookie	|客户端字符串

cookie就是一串纯key/value的文本

###响应首部

首部	| 描述
-- | --
Age| 	响应持续时间
Server	| 服务器应用软件名称和版本
Allow	| 列出了可用的请求方法
Location| 	告诉客户端实在在哪里，用于定向
Content-Base	| 解析主体中相对URL的基础URL
Content-Encoding	| 主体编码格式
Content-Language	| 解析主体时适用的语言
Content-Length	| 主体的长度或尺寸
Content-Location	| 资源实际位置
Content-MD5	| 主体的MD5校验和
Content-Range	| 在整个资源中此实体部分的字节范围
Content-Type	| 主体的MIME
ETag	| 主体的实体标记
Expires| 	过期时间
Last-Modified	| 实体最后一次修改时间


###关于DNS
DNS可以理解为是一个很大的数据库，里面存储着域名和对应的ip
例如：
```
DNS存储了：

xxx.com  111.111.111.111
yyy.com 222.222.222.222
```
- 对应的IP则由域名持有者来进行修改上传到DNS
- 另外浏览器可能会对DNS有缓存
- 一个域名可以有多个对应的IP，比如baidu.com，`ping baidu.com`可以查看
修改host可以改变域名对应的端口号

###关于端口

TCP或者UDP对应的规则，一个端口对应一个服务
一个服务器有很多个端口（65536个），一个端口只能做一件事情，其中0-1023这1024个端口已经被限制为做特定的事情（保留端口）:
- 21端口： FTP
- 80端口： HTTP
- 53端口: DNS
- 443端口: HTTPS
- 1080端口: SOCKS代理

###[关于cookie](https://zhuanlan.zhihu.com/p/22396872)
