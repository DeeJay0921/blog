---
title: Python中值得关注的一些知识点
date: 2018/06/12 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中值得关注的一些知识点
<!--more-->

##  可变/不可变类型导致的差别，以及`a+=a`和`a = a+a`的区别
- 可变和不可变类型作为参数

先来看一个例子：

```
a = 100

def fn(num):
    num += num
    print(num)

fn(a) # 200
print(a) # 100
```
上述例子中，在fn内部的num输出为200，但是全局的a的值并没有改变，还是100。

再来看第二个例子：

```
a = [100]

def fn(num):
    num += num
    print(num)

fn(a) # [100,100]
print(a) # [100,100]
```

例子2中，全局的a也发生了改变。

先来分析这两个例子，例1中，我们传入的是一个**不可变类型**的a,而在例2中传入的是一个**可变类型**的a。

那来分析函数的运行，`num += num`即意味着要把num指向的值进行修改，如果传入的值是可变类型，自然就允许修改指向的值，并且指向的地址不会变，所有num修改了值，num和a指向的地址又是相同的，所以全局中的a也发生了改变。

但是如果是不可变的类型，那么就**在函数内部定义一个变量指向这个值**,此时这个num和a的地址就不相同了，自然全局中的a就不会变化了。

-  `a += a`和`a = a+a`在Python中的不同

再来看2个例子：

```
a = [100]

def fn(num):
    num += num
    print(num)

fn(a) # [100,100]
print(a) # [100,100]
```

```
a = [100]

def fn(num):
    num = num + num
    print(num)

fn(a) # [100,100]
print(a) # [100]
```

例1用了`num += num`,意味着修改num指向地址的值，而例2用的`num = num + num`却意味着，先把赋值符右边的`num + num`的值计算出来，再赋值给赋值符左边的num，这么一来，num等于是重定义了一下，地址已经不在和全局的a一样了。

```
num += num # 修改num这个地址指向的值

num = num + num # 即 num = [100] + [100]  重新定义了左边的num,地址已经改变了
```
