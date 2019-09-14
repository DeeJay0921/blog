---
title: HTML-知识点
date: 2017/07/29 19:22:24
tags: 
- HTML
categories: 
- 前端
---
HTML-知识点
<!--more-->
### HTML，XML，XHTML的区别
- XML
  XML是可扩展标记语言，（eXtensible Markup Language），XML和HTML一样，都是处于SGML（Standard Generalized Markup language）即标准通用置标语言，XML极其简单易于掌握和使用，是一种简单的数据存储的语言，可以跨平台，能有效的处理结构化文档信息。
- XHTML
  XHTML是可扩展超文本标识语言，（The Extensible HyperText Markup Language），是一种基于XML的置标语言，与HTML相似，略有微小的差异，是一种过渡技术，像一种类似HTML的XML。
- HTML
 HTML是超文本标记语言，（HyperText Mark-up Language），是目前网络上，应用最广泛的组成网页文档的语言。


###怎样理解HTML语义化
- HTML语义化
HTML语义化就是用正确的标签做正确的事情，使得整个HTML页面结构清晰，便于开发者阅读以及机器更好的解析。
- HTML语义化带来的优点
 1. 结构和样式是分离的，在没有CSS的情况下，HTML语义化可以使得整个页面也能很好的反映出页面结构，仍然具有良好的可读性（即裸奔时也好看）。
2. 便于SEO（Search Engine Optimization），即搜索引擎优化，可以和搜索引擎建立良好沟通，由于爬虫是依赖标签来确定上下文和各个关键字的权重的，采取优化策略或程序，提高网站在搜索结果中的排名。
3. 在团队开发的情况下，语义化的HTML结构可以使得一起开发的开发者更加容易的阅读，能提高团队的工作效率，并且也便于后期的维护。
4. 语义化的HTML结构同时也方便其他多种设备进行解析，例如最常用的移动端以及屏幕阅读器和盲人阅读器等等。
- 平时在写代码的过程中怎么注意HTML的语义化
1. <div>和<span>是无语义的，尽量少避免。
2. 在语义不明显时，既可以使用<>div或者<p>时，尽量用<p>, <p> 中的文字会自动换行，不需要使用<br/>而且换行的效果优于 <br />。段落与段落之间的空隙也可以利用 CSS 来控制，很容易而且清晰的区分出段落与段落。
3. <h1>~<h6> ，作为标题使用，并且依据重要性递减，<h1> 是最高的等级。
4. 有使用<input>标签时，必须要有相应的<label>标签，通过使用label标签的for属性和input的id属性配对起来。
5. 书写表格时，标题使用<caption>，表头使用<thead>，主题部分使用<tbody>，而底部使用<tfoot>，具体的单元格也要区分开来，表头是<th>，一般的单元格是<td>。
6. 需要使用强调和斜体的情况时，使用<strong>和<em>标签，不使用<b> <i>。


###怎样理解内容与样式分离的原则
- 一个页面由HTML，CSS和JS三部分组成，HTML表示了页面的结构和内容，CSS控制着页面内容显示的样式，JS控制着页面的交互功能行为等。
 - 在写一个HTML页面的时候，只需要考虑页面的结构和语义化即可，不需要考虑CSS样式的问题，让HTML能够充分体现页面的结构和内容，之后再去考虑写CSS控制样式。另外要注意，HTML中不允许出现元素样式，也不建议出现行内样式。
- 同样的，在写JS的时候，不要通过JS直接操作样式，而是通过给元素增加一个class来控制样式的变化。

###有哪些常见的meta标签
- 关键词
`<meta name="keywords" content="your tags" />`
- 页面描述
`<meta name="description" content="150 words" />`
- 搜索引擎索引方式
`<meta name="robots" content="index,follow" />`
　　all：文件将被检索，且页面上的链接可以被查询；
　　none：文件将不被检索，且页面上的链接不可以被查询；
　　index：文件将被检索；
　　follow：页面上的链接可以被查询；
　　noindex：文件将不被检索；
　　nofollow：页面上的链接不可以被查询。
- 页面重定向和刷新
`<meta http-equiv="refresh" content="0;url=" />`
- 定义网页作者 
`<meta name="author" content="author name" /> `
###文档声明的作用?严格模式和混杂模式指什么?<!doctype html> 的作用?
- Doctype作用
<!DOCTYPE>声明叫做文件类型定义（DTD），声明的作用为了告诉浏览器该文件的类型。让浏览器解析器知道应该用哪个规范来解析文档。<!DOCTYPE>声明必须在 HTML 文档的第一行，这并不是一个 HTML 标签。
- 严格模式和混杂模式
1. 严格模式：又称标准模式，是指浏览器按照 W3C 标准解析代码。
2. 混杂模式：又称怪异模式或兼容模式，是指浏览器用自己的方式解析代码。
- <!doctype html>
<!doctype html>是HTML5的声明，HTML5 没有 DTD ，因此也就没有严格模式与混杂模式的区别，HTML5 有相对宽松的语法，实现时，已经尽可能大的实现了向后兼容。（ HTML5 没有严格和混杂之分）

###浏览器乱码的原因是什么？如何解决
- 乱码的原因、
乱码的原因是你编写HTML文件之后保存的时候，采用的编码格式（一般编码格式有ASCII，ISOLatin-1，UNICODE，UTF-8，GBK）和在浏览器运行这个HTML文件时候的解码格式不匹配导致出现了乱码。
- 解决方法
 一般英文的编码方式都是一致的，所以出现乱码时尽量采用英文，可以避免出现乱码情况。
###常见的浏览器有哪些，什么内核
- IE       Trident内核
- Firefox       Gecko内核
- Safari、Chrome      WebKit内核
- Opera        Presto内核

### 列出常见的标签，并简单介绍这些标签用在什么场景
- 文档标题 
<title>
- 标题
 <h1>～<h6>不同级别的标题
- 段落
<p> 段落
- <div>
划分区域
-换行 
<br>
-表单 
<form>
- <a>
链接，跳转到指定地址
- 有序列表<ol><li> 和 无序列表 <ul><li>
 列表
