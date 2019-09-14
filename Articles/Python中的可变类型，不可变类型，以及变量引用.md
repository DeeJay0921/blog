---
title: Python中的可变类型，不可变类型，以及变量引用
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中的可变类型，不可变类型，以及变量引用
<!--more-->
# Python中的可变类型，不可变类型，以及变量引用

# 可变与不可变

- 不可变类型：
即一旦定义不支持修改的类型，在Py中，`Number`,`String`以及`tuple`是不可变类型。

先来看一个例子：

```
str1 = 'hello Py'
print(id(str1)) # 140378141772080
str1 = 'hello Python'
print(id(str1)) # 140000092443568

```

可以看到，对str1进行重新赋值，id即指向内存的地址改变了，即str1是换了一块指向的地址，而不是对原来地址指向的值进行修改。所以可以证明string类型确实是不可变类型，只能重新赋值，而不能修改原来的值，比如进行`str1[0] = 'H'`的操作就会进行报错。

Number类型也是同理
```
num1 = 100
beforeId = id(num1)
num1 = 99
afterId = id(num1)

print(beforeId == afterId) # False 
```
tuple就不用多说，定义之后不支持修改。


- 可变类型
指的是支持修改的那些数据类型，即`list`和`dict`。

```
li = [1,2,3]
beforeId = id(li)

li[0] = 0
li.append(4)
afterId = id(li)

print(li) # [0,2,3,4]
print(beforeId == afterId) # True

```

dict也是同理:
```
di = {'name': 'DeeJay'}
beforeId = id(di)

di['name'] = 'yang'
di['age'] = 21
afterId = id(di)

print(di) # {'name': 'yang', 'age': 21}
print(beforeId == afterId) # True
```

说完了可变和不可变类型，在Python中，只有不可变类型才能作为dict的key值，比如str,num,tuple,而list和dict不能作为key值。

```
di = {
    'name': 'DeeJay',
    3.14: 'PI',
    (1,2,3): 'tupleValue'
}
```
不可变的类型作为key值都是合法的。


## 变量的赋值都是引用

在Python中，**所有的赋值操作**，都是让变量指向内存中存储值的空间。

先来看一个例子：

```
a = 100
b = a

print( id(a) ) # 11036256
print( id(b) ) # 11036256

# 可以看到a和b的地址是一样的 即只有一份空间存储了100这个值，而a和b都仅仅是
# 一个引用  存储了指向100这个值的地址
```

上述例子中，内存中只有一个存储100这个值的空间，但是有2个变量a,b都指向了它，这就是`b = a`做的事情。

那么有了上面的认知，加上可变和不可变类型的知识。接着看下面的例子：

```
# 对于不可变的类型
a = 'abc'
b = a
print(id(a)) # 139673935449312

a = 'def'
print(id(a)) # 139673934789352  a两次的地址已经发生了改变
print(id(b)) # 139673935449312
print(b) # abc
```

来分析上面的例子，对于不可变的类型，每次赋值都是使该变量指向另外一个地址，所以a在重新赋值之后，id(a)就改变了，但是对于b来说，b指向的a赋值前的那个地址，所以id(b)没变，相应的b指向的值也没有改变，还是abc,即赋值之后，再给a赋值，b并没有改变。

对于num来说也是同理。

再来看可变类型的例子：
```
# 对于可变类型

A = [1,2,3]
B = A
print(id(A)) # 140395751312840
A.append(4)
print(id(A)) # 140395751312840
print(id(B)) # 140395751312840
print(B) # [1,2,3,4]


C = {'name': 'DeeJay'}
D = C
print(id(C)) # 140678783125544
C['age'] = 21
print(id(C)) # 140678783125544
print(id(D)) # 140678783125544
print(D) # {'name': 'DeeJay', 'age': 21}
```

上述可变类型的例子也很好理解，可变类型支持修改，所以每次修改之后，id(A)并没有发生改变, 而`B = A`之后，B和A也是指向了同一地址，所以A的改变会引起B的改变。

这就是Python中的引用，其实可以类比与JS中的基本类型和引用类型,写个例子加深下理解：
```
var a = '123'
var b = a
a = 'hello JS'
console.log(b) // 123  基本类型


var obj1 = {name: 'DeeJay'}
var obj2 = obj1
obj1['age'] = 21
console.log(obj2) // 引用类型 { name: 'DeeJay', age: 21 }
```
