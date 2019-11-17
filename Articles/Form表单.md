---
title: Form表单
date: 2017/07/29 23:22:24
cover: https://www.computerhope.com/jargon/s/standard-input.jpg
tags: 
- 前端
- Form
categories: 
- 前端
---
Form表单
<!--more-->

###一.form表单有什么作用？有哪些常用的input 标签，分别有什么作用？
- Form表单的作用是：收集用户通过页面填写的信息，然后传给后台。
- 常见的<input>标签及其作用
首先要注意**所有的表单都必须要放到<form></form>中**，否则提交是无效的。
下面列举出常见的<input>的type类型：
单行文本输入框type="text"：
```
<div class="text">
            <label for="username"></label>
            <input type="text" name="username" id="username" placeholder="输入用户名">
            <!--普通的单行文本输入框-->
        </div>
```
密码输入type="password"
```
        <div class="password">
            <label for="password"></label>
            <input type="password" name="password" id="password" placeholder="输入密码">
            <!--密码输入框，输入时默认是小圆黑点-->
        </div>
```
复选框type='checkbox'
```
        <div class="checkbox">
            <label>喜欢的运动</label>
            <input type="checkbox" name="sports" value="basketball">篮球
            <input type="checkbox" name="sports" value="baseball">棒球
            <input type="checkbox" name="sports" value="football"> 足球
            <!--复选框，使用的时候name要设置一致表明是一组选项。 value的值会根据用户填写选项返回给后台-->
        </div>
```
单选框type="radio"
```
        <div class="radio">
            <label>性别</label>
            <input type="radio" name="sex" value="male">man
            <input type="radio" name="sex" value="female">woman
            <!--单选框，其中name属性相同的为同一组选项，value的值会根据用户填写选项返回给后台-->
        </div>
```
上传文件 type="file"
```
        <div class="file">
            <input type="file" name="myFile" accept="image/gif">
            <!--用于上传文件-->
        </div>
```
隐藏域type="hidden"
```
        <div class="hidden">
            <input type="hidden" name="csrf" value="12u31uhuhdsg1dsyu12ghe1">
            <!--页面上用户看不到的隐藏域，可以用于防止csrf攻击，通过检查传到后台的value判断用户是否合法-->
        </div>
```
下面是提交和重置的type类型
提交
```
        <div class="submit">
            <input type="submit" value="submit">
            <!--提交-->
        </div>
        <div class="button">
            <button value="提交"></button>
            <!--提交-->
        </div>
```
重置
```
        <div class="reset">
            <input type="reset" value="reset">
            <!--重置已经输入的内容-->
        </div>
```

下面介绍其他相关标签：
下拉选择框select：
```
        <div class="select">
            <select name="sports">
                <option value="basketball">篮球</option>
                <option value="baseball">棒球</option>
                <option value="football" selected>足球</option>
            </select>
            <!--下拉选择框，其中<option>的 selected 表示初始默认选择项-->
        </div>
```
多行文本输入框textarea：
```
        <div class="textarea">
            <textarea name="textarea" id="textarea" cols="30" rows="10">
                可以输入多行的文本,而type="text"只允许输入单行文本
            </textarea>
        </div>
```

###二.post 和 get 方式的区别？
在form标签中，一般属性有action和method，action表示的是表单提交信息的地址，而method是表示用何种方式传递数据，有get和post两种方式，下面比较两种方式的区别。
```
<div class="form">
    <form action="/deejay" method="post">

    </form>
</div>
```
get一般用于获取和查询数据信息，post一般用于更新资源。
get一般就像数据库查询一样，仅仅获取资源信息，并不会修改服务器上的值，post是用于更新资源的，可能会修改服务器上的资源。
- 两种提交方式的区别
1. get
get请求的数据会加到URL之后，请求多个参数的时候用&隔开，传输的数据和URL依靠?分隔开来，例如
`[http://www.it315.org/counter.jsp?name=zhangsan&password=123]`。另外url的编码格式为ASCII码，所有的非ASCII字符都需要重新编码在进行传输。
2. post
用post方式提交数据的时候，请求的数据是放到HTTP包的包体中的，所以post提交的情况浏览器的地址栏不会改变。
- 传输数据的大小差异
由于上述方式的差异，get方式一般提交的数据**最多为1kb**（1024字节），而post没有限制，可以传输较多的数据。
- 安全性问题
使用get请求发送数据的时候，你的username和password都会出现在URL上，很容易泄露，并且get请求可以被缓存。post请求不可以被缓存，相比之下**post安全性要比get的安全性高**。
所以要满足**get是向服务器发送索取数据的一种请求，post是向服务器提交数据的一种请求**这一条件。

###三.在input里，name 有什么作用？
在input标签中，name属性表示的是input元素的名称，只有设置了name属性的表单元素才能成功传递他们的值。另外在单选框中，name属性还起到分组的作用。
### 四.radio 如何 分组?
在
```
        <input type="radio"name="sex1" value="man">男
        <input type="radio" name="sex1" value="woman">女

        <input type="radio" name="sex" value="man">男
        <input type="radio" name="sex" value="woman">女
```
中，**通过设置name的值来确定分组，name属性的值相同的为同一组**，同一组的只允许单选。

###五.placeholder 属性有什么作用?
```
        <div class="text">
            <input type="text" name="username" placeholder="请输入用户名">
        </div>
```
运行效果为
![placeholder效果](http://upload-images.jianshu.io/upload_images/7113407-7313f2095d938bc7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240),会给用户一个提示内容引导用户输入数据。

###六.type=hidden隐藏域有什么作用? 举例说明
`<input type="hidden">`称作表单隐藏域，对于用户来说是不可见的。用来传递参数和一些特殊的功能。可以暂时存储网站安全的信息以及一些其他数据。
隐藏域的优势在于支持所有的浏览器，在用户禁用cookie的时候也能使用。
一些具体的应用举例：
1. 一般页面中form中有多个提交按钮时，如果要判断用户是通过哪一个按钮提交，就可以在每一个按钮上加上一个隐藏域来确定。
2. 再或者要确定用户登录本页面时间长短，也可以通过设置隐藏域。
3. 可以防止CSRF攻击，请求时，通过在隐藏域value中存储一个后台提供的随机数，来判断登录的用户是否合法。
