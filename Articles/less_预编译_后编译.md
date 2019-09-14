---
title: less_预编译_后编译
date: 2017/09/13 00:00:01
tags: 
- 前端
- CSS
- less
categories: 
- 前端
---
less_预编译_后编译
<!--more-->

[less参考](http://www.bootcss.com/p/lesscss/)


###预编译 使用举例：
比如写less/sass的时候，定义的变量，发布前要进行预编译生成对应的css。

###后编译  使用举例：
比如说写浏览器兼容写法的css的时候，有很多行一样的比如-webkit,-moz,-o等，这种情况可以直接写最标准的css语法，最后进行后编译生成兼容版本的css。

less中需要注意的语法：
- 嵌套出现伪类的时候  &就代表当前选择器本身
例如要写出#header 下的.content的:after伪类：
```
#header {
  .content {
    display: none;

    &:after { /*&就代表.content本身*/
      content: '';
      display: block;
      clear: both;
    }
  }
}
```
就等同于css:
```
#header .content {
  display: none;
}
#header .content:after {
  /*&就代表.content本身*/
  content: '';
  display: block;
  clear: both;
}

```

- 要实现#header .content>p 的选择器效果的时候：
```
#header {
  .content {
    > p{
      color: red;
    }
  }
}
```

###关于postcss
先安装一个工具
`npm install postcss -g`
`npm install postcss-cli -g`

写一个标准的css文件：
```
@keyframes move {
  from {
    left: 0;
  }
  to {
    left: 200px;
  }
}
```
通过`postcss -u autoprefixer -r senior/*.css` 后编译 成为：
```
@-webkit-keyframes move {
  from {
    left: 0;
  }
  to {
    left: 200px;
  }
}
@keyframes move {
  from {
    left: 0;
  }
  to {
    left: 200px;
  }
}
```
自动就加了兼容的前缀
