---
title: Python中的列表生成式
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中的列表生成式
<!--more-->

# 列表生成式
抛出一个问题，创建一个从1到100的列表，该怎么写：

1. while
```
li = []
i = 1
while i <= 100:
    li.append(i)
    i += 1

print(li)
```

2. for
```
li = []

for i in range(1,101):
    li.append(i)

print(li)
```

关于`range()`用法特别类似于切片操作，是用来生成列表的，**在Python2中，返回的是列表，而且如果指定了一个特别大的列表，占用内
存会很高**。
但是在Python3中，range()返回的不再是列表，并且只有在需要的时候在生成对应的列表，从而解决了Python2中的问题。

# 用法

```
li = [i for i in range(1,10)]

print(li) # [1, 2, 3, 4, 5, 6, 7, 8, 9]

```

上述例子就是一个列表生成式，其中最左边的这个`i`代表着每次插入列表的值，而后面的`for i in range(1,10)`则代表着这个列表循环插值多少次，即列表长度。

```
li2 = [1 for i in range(1,10)]

print(li2) # [1, 1, 1, 1, 1, 1, 1, 1, 1]

```

这次插入的值为固定值1，所以li2为上例输出的结果。


列表生成式中也可以通过`if`来进行筛选：
```
li3 = [i for i in range(1,10) if i%2 ==0]

print(li3) # [2, 4, 6, 8]

```
通过if 筛选了偶数项

同时列表生成式还支持循环嵌套：

```
li4 = [i for i in range(3) for j in range(2)]

print(li4) # [0, 0, 1, 1, 2, 2]

```
两层嵌套，可以写的更直观一点，插入一个元祖看一下：

```
li5 = [(i,j) for i in range(3) for j in range(2)]

print(li5) # [(0, 0), (0, 1), (1, 0), (1, 1), (2, 0), (2, 1)]

```
但是不建议超过三层嵌套
