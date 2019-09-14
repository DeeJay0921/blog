---
title: HTML5-
date: 2017/09/12 00:00:01
tags: 
- 前端
- HTML
categories: 
- 前端
---
HTML5是超文本标记语言的第五次重大修改。
<!--more-->

[详说 Cookie, LocalStorage 与 SessionStorage](http://jerryzou.com/posts/cookie-and-web-storage/)
[在HTML5的时代,重新认识Cookie](https://juejin.im/post/59708bbe518825103c098332)


#HTML5
HTML5是超文本标记语言的第五次重大修改。

###设计目的
HTML5的设计目的是为了在移动设备上支持多媒体。新的语法特征被引进以支持这一点，如video,audio,canvas标记。还引进了新的功能，可以真正的改变用户与文档的交互方式，包括：
- 新的解析规则增强了灵活性
-  新属性
- 淘汰过时的或冗余的属性
- 一个HTML5文档到另一个文档间的拖放功能
- 离线编辑
- 信息传递的增强
- 详细的解析规则
- 多用途互联网邮件扩展（MIME）和协议处理程序注册
- 在SQL数据库中存储数据的通用标准（Web SQL）

###特性
#####语义特性
赋予网页更好的意义和结构。
#####本地存储特性
LocalStorge Indexed DB
#####设备兼容特性
例如定位，操作摄像头等。
#####连接特性
websocket
#####多媒体特性
audio video 
#####性能
XMLHttpRequest2

###变化
- 新增header,footer,dialog,aside,figure等
- b和i标签仍然保留，但是意义不同，现在只是为了将一段文字标识出来，而不是为了设置粗体斜体。
- 全新的表单输入对象，包括日期，URL，Email地址
- 本地数据库
- canvas
- ...

###标签和元素变化
####DOCTYPE
XHTML
```
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
```
HTML5
```
<!doctype html>
```
####文档编码
XHTML
```
    <meta http-equiv="content-type" content="text/html" charset="utf-8">
```
HTML5
```
    <meta charset="UTF-8">
```
####具有boolean值的属性
例如disable,readonly等只写属性不写属性值时为true
```
<input type="checkbox" checked>
<input type="checkbox" checked="checked">
<input type="checkbox" checked="">
```
####省略属性值的引号
属性值可以用单引号或者双引号，在属性值不包括<,>,=,',=时可以省引号
（但是不建议省略）
`<input type=text>`

###常见新增元素

元素 |　描述
-- | --
canvas | 标签定义图形，比如图表和其他图像。基于JS的绘图API
audio | 定义音频内容
video | 定义视频（video或者movie）
source | 定义多媒体资源<video>和<audio>
embed | 定义嵌入的内容，比如插件
datalist | 定义选项列表。与input元素配合使用该元素，来定义input可能的值

###input新增type
- email
- url
- number
- range
- Date Picker
date
month
week
time
datatime

####不再使用frame框架
- frame
- frameset
- noframes

###新增属性
####全局属性
- contentEditable
- designMode
- hidden
- spellcheck
- tabindex
####表单相关
- autofocus
- placeholder
- form
- required
- formaction,formenctype,formmethod,formtarget,formnovalidate
- novalidate
