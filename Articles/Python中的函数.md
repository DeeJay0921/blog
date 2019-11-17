---
title: Python中的函数
date: 2018/06/09 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中的函数
<!--more-->

# Python中的函数

## 定义/调用

```
def sayHi():
    print('hello,function in Python')

sayHi()
```

*注意Python中没有JS一样的声明前置，所以定义必须要在调用之前*

## 函数中的参数

```
def say(str):
    print(str)

say('hello, this is argument')
```

对于Python中的函数参数，定义几个就传几个参，这个很好理解。但是也有一些高级的或者是简化的参数用法：
- 默认参数：
```
def powerNum(num,pow=2):
    res = 1
    while pow>0:
        pow -= 1
        res = res * num
    return res

print( powerNum(5) ) # 25  不传第二参数的话，默认使用pow=2来进行计算
# 等价于 print( powerNum(5,2) )

print( powerNum(2,3) ) # 8
```
默认参数使用的时候，一般放在必选参数后面，如果有多个默认参数，前面的想使用默认值，后面的想自行赋值的话，可以这么写：

```
def intro(name,age=18,word='hello,nice 2 meet u'):
    print('name:%s,age:%d,%s'%(name,age,word))

# 我们现在想使用默认的age，但是想自行赋值word

intro('DeeJay',word='this is DeeJay') # name:DeeJay,age:18,this is DeeJay

#  这么一来就使用了默认的age并且自定义了word
```
*值得注意的是，**默认参数必须指向不变的对象**,如果是个可变量的话，每次调用，默认函数指向的那个变量的也会改变*
```
def pushEndToList(li = []):
    li.append('END')
    print(li)

pushEndToList() # ['END']
pushEndToList() # ['END', 'END']
pushEndToList() # ['END', 'END', 'END']

# 可以看到 默认参数li=[] 指向的不是一个不变的对象，
# 调用函数的时候，li指向的对象的值变了，那么再次调用的时候li的值就已经改变了
# 所以，默认参数， 必须要指向一个不变的对象(比如None)
```
- 可变参数
有时候我们定义函数的时候并不清楚到底需要多少个参数，就可以使用可变参数，使参数的个数不固定
```
def sum(*num):
    print(num)
    print( type(num) ) # <class 'tuple'>
    res = 0
    for number in num:
        res += number
    return res

sum(1,2,3,4) # (1,2,3,4)

res = sum(1,2,3,4)
print(res) # 10
```
可变参数的用法已经很清楚了，另外我们可以看到，这个可变参数其实是一个tuple
- 关键词参数
```
def keywordFunction(name,age,**kw):
    print('%s,%s,%s'%(name,age,kw))


keywordFunction('DeeJay',22) # DeeJay,22,{} 可以不传关键词参数

keywordFunction('Yang',23,city='xian') # Yang,23,{'city': 'xian'}

keywordFunction('Wen',23,city='xian',job='frontEnd') # Wen,23,{'city': 'xian', 'job': 'frontEnd'}

```

关于函数的参数问题，我们来看一个综合的例子：

```
def fn(a,b,c=100,*args,**kw):
    print(a)
    print(b)
    print(c)
    print(args)
    print(kw)

fn(1,2,3,4,5) # 1 2 3 (4,5) {}
# 这个很好理解，因为没有传入kw,默认参数c被赋值了3，剩下的被当成args构造出一个元祖


fn(1,2,3,4,5,name='DeeJay',age=21) # 1 2 3 (4,5) {'name': 'DeeJay', 'age': 21}
# 这次传入了kw


# 然后来看一个例子，
A = (4,5)
B = {'name': 'DeeJay', 'age': 21}

# 定义了A和B，然后传给fn()
fn(1,2,3,A,B) 
# 输出结果为： 
# args == ((4, 5), {'name': 'DeeJay', 'age': 21})
# kw == {}

# 出现上述结果的原因是，没有按照格式来进行传入kw,即按照key=value的形式传入
# 所以A和B都被当成了args,那么如果我们想让A被当成args，B作为kw，可以这样写：

fn(1,2,3,*A,**B) # 此时args == (4,5) kw == {'name': 'DeeJay', 'age': 21}
# 这种方法成为拆包 
```

注意最后的`fn(1,2,3,*A,**B)`的写法，拆包的作用就是在传参的时候，将变量分解作为参数给函数。


## 函数的return值

在Python中，函数如果没有显示的写明return值，那么函数默认`return None`

此外直接写`return`也等价于`return None`

对于想返回多个值的函数，可以将多个值组合为一个list或者tuple等返回。

```
def fn():
    a = 10
    b = 20
    c = 30
    return a,b,c

res = fn()
print(res) # (10, 20, 30) 是一个tuple
```

## 局部变量和全局变量

Python中的函数中定义的变量为局部变量:

```
num = 99

def fn():
    num = 100

def printNum():
    print(num)

fn()
printNum() # 99
```
上述例子中，已经在全局环境中定义了num,所以在fn()中是在拘捕作用域中定义了一个**局部变量num**,而不是修改全局中的num,二者仅仅是变量名相同。

如果想要在fn()中修改全局的num，那么需要这么写：
```
num = 99

def fn():
    global num # 表明在内部作用域中没有定义新的局部变量num,这边的num就是全局变量
    num = 100

def printNum():
    print(num)

fn()
printNum() # 100 此时输出的就是修改过后的全局变量num 为100
```

**上述例子使用`global`来声明是操作一个全局变量而不是需要重新定义一个局部变量**

但是也有例外，对于dict和list，其实可以不写global，程序仍然会去使用全局变量，而不是默认先定义一个局部变量:

```
# 对于dict和list，如果是全局变量 其实可以不用写global，
# 也会默认使用全局的，而不是先定义一个

di = {'name':'DeeJay'}
li = [1,2,3]

def fn1():
    # 这边没有声明global
    di['age'] = 21
    li.append(4)

def fn2():
    print(di)
    print(li)

fn1()
fn2() # {'name': 'DeeJay', 'age': 21} [1, 2, 3, 4]
# 直接改变了全局中的di和li
```

   ### Python中的递归

Py中的递归没什么特别的地方，写个阶乘函数了解一下：
```
def factorial(n):
    if(n == 1):
        return 1
    return n * factorial(n-1)

res = factorial(4)
print(res)
```

### 匿名函数`lambda`

- 语法
`lambda args: expression`

```
lambda x: x*x
```
上述例子， `lambda x:`的`x`是参数，后面的`x*x`是作为函数return的值。

- 用法

使用匿名函数来进行`list.sort()`

list.sort()接受一个值为一个函数的key，将这个函数的返回值作为排序的依据，例子：

```
li = [1,-3,2]

li.sort(key=abs)

print(li) # [1, 2, -3]

# 将abs()的返回值作为排序依据
```

搭配匿名函数也可以进行排序：

```
li = [
    {'name': 'xiaohong','age': 21},
    {'name': 'xiaohong','age': 30},
    {'name': 'xiaohong','age': 24},
    {'name': 'xiaohong','age': 23},
]

# 现有如上list，想根据其中每个元素的age属性来进行排序：

li.sort(key=lambda person: person['age'])

print(li) # 输出：
# [{'name': 'xiaohong', 'age': 21}, 
# {'name': 'xiaohong', 'age': 23}, 
# {'name': 'xiaohong', 'age': 24}, 
# {'name': 'xiaohong', 'age': 30}]

```

匿名函数也可以作为参数传入别的函数进行调用：

```
def fn(a,b,fn):
    return fn(a,b)

res = fn(1,2,lambda num1,num2: num1 + num2)
print(res) # 3
```

以上就是匿名函数的基本用法
