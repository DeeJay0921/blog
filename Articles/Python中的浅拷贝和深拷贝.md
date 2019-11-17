---
title: Python中的浅拷贝和深拷贝
date: 2018/06/09 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中的浅拷贝和深拷贝
<!--more-->

# 浅拷贝 赋值 和 `copy.copy()`

Python中的浅拷贝分为直接赋值和使用`copy`模块的`copy`方法来进行浅拷贝。

### 赋值 
先来看直接赋值的例子：

```
a = [1,2,3]

b = a

print( b is a) # True
```

很简单，就是两个变量指向同一个内存地址。



### `copy.copy()`

- 对于不可变类型来说
对于不可变类型，`copy.copy()`的表现形式和直接赋值是一样的，并不会重新创建一个新的对象，而是仅仅把新的变量指向原先的同一个地址上：
```
import copy

a = [1,2,3]
b= [4,5,6]

c = (a,b) # c为不可变类型

print( id(c) ) # 140065869930632

d = copy.copy(c)
print( id(d) ) # 140065869930632
print(d is c) # True
```
可以看到，对于不可变类型，使用`d = copy.copy(c)`就相当于直接赋值`d = c`
- 对于可变类型来说
对于可变类型，`copy.copy()`会创建于一块新的内存地址，然后**仅仅拷贝目标对象的第一层**,来看例子：
```
import copy

a = [1,2,3]
b= [4,5,6]

c = [a,b] # c为可变类型

d = copy.copy(c)

print( id(c) ) # 140393314515528
print( id(d) ) # 140393343993800
print(d is c) # False
# 说明创建了一块新的内存地址

a.append(4)
print(c) # [[1, 2, 3, 4], [4, 5, 6]]
print(d) # [[1, 2, 3, 4], [4, 5, 6]]
```
上述例子中，可以看到`copy()`是创建了一个新的内存地址的，但是并没有将内部的a和b的两个引用也一起拷贝，而是仅仅拷贝了第一层，所以改变了a指向的对象之后，浅拷贝出来的d也会相应的改变。

### 深拷贝 `copy.deepcopy()`

针对上面对于可变类型，`copy.copy()`仅仅会拷贝第一层的问题，我们可以使用`copy.deepcopy()`来进行深度拷贝，进行`deepcopy()`时，对象内部的引用指向的内存地址也会被拷贝，即会重新创建一份目标对象中的引用指向的内存地址。 来看下面例子：

```
import copy

a = [1,2,3]
b = [4,5,6]

c = [a,b]

d = c
e = copy.deepcopy(c)

a.append(4)
print(c) # [[1, 2, 3, 4], [4, 5, 6]]
print(e) # [[1, 2, 3], [4, 5, 6]]

# 说明深拷贝deepcopy的时候，如果拷贝的对象内部还接着有引用，那么依次进行内部的拷贝
```

如果上面例子不够直观，那么可以这么来理解：

```
import copy

a = [1,2,3]
b = [4,5,6]

c = [a,b]

d = c
e = copy.deepcopy(c)

print( id(c[0]) ) # 140623323590920
print( id(e[0]) ) # 140623323492232
# e的第一个元素的地址，和c第一个元素的地址是不一样的

print( c[0] is a ) # True
print( e[0] is a ) # False
# 意味着e内部将a和b两个变量指向的对象也进行了一份拷贝，而不是跟copy.copy()一样沿用
# c内部的a和b的地址(即只拷贝第一层)
```
