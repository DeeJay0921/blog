---
title: Python中的异常处理
date: 2018/06/09 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中的异常处理
<!--more-->

## 异常介绍
先来看一个最简单的异常，输出一个没有定义的变量：

```
print(num) # NameError: name 'num' is not defined
```
那么上面就是出现异常的一个情况，`NameError`是异常的名字。

## 异常的捕获以及处理

我们可以用`try`和`except errorName:` 来对异常进行处理：
针对上面的例子来处理异常
```
try:
    print(num)
except NameError:
    print('变量未命名的异常进行处理')
```
把这个例子扩展一下：
```
try:
    print(num)
    print('----try----')
except NameError:
    print('变量未命名的异常进行处理')
    print('-----except----')

print('----outside---')
# 输出: 
# 变量未命名的异常进行处理
# -----except----
# ----outside---
```
程序执行的时候，在`try`中的异常之后的语句不进行执行，然后执行`except`中的语句，异常处理完成之后继续进行后面的代码。

`except`可以有多个  用来处理对应的异常

```
try:
    # print(num)
    open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')

print('----outside---')
# 输出 
# 没找到文件呀
# ----outside---
```
可以合并写为：
```
except (NameError,FileNotFoundError):
    print('处理异常')
```
except在上面的异常都没捕获的到情况下，可以写一个`Exception`用来兜底：
```
 try:
    233 / 0
    # print(num)
    # open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')
except Exception: # 上面的异常都没捕获到的话，这个Exception一定会捕获到
    print('上面的异常都没捕获到的话，这个Exception一定会捕获到')

print('----outside---')
# 输出 
# 上面的异常都没捕获到的话，这个Exception一定会捕获到
# ----outside---
```
还可以在Exception中看到具体的异常信息：
```
try:
    233 / 0
    # print(num)
    # open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')
except Exception as errName: # 上面的异常都没捕获到的话，这个Exception一定会捕获到
    print('上面的异常都没捕获到的话，这个Exception一定会捕获到')
    print(errName)

print('----outside---')
# 输出 
# 上面的异常都没捕获到的话，这个Exception一定会捕获到
# division by zero
# ----outside---
```
写成`except Exception as var`，信息就在var里。

如果还要对`try`中没有出现异常情况做处理，可以增加一个`else:`
```
try:
    233 / 1
    # 233 / 0
    # print(num)
    # open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')
except Exception as errName: # 上面的异常都没捕获到的话，这个Exception一定会捕获到
    print('上面的异常都没捕获到的话，这个Exception一定会捕获到')
    print(errName)
else:
    print('程序没有出现异常')

print('----outside---')
# 输出 
# 程序没有出现异常
# ----outside---
```
还有一个`finally`语句，指的是不管`try`中有没有异常 最后都会执行

```
try:
    233 / 1
    # 233 / 0
    # print(num)
    # open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')
except Exception as errName: # 上面的异常都没捕获到的话，这个Exception一定会捕获到
    print('上面的异常都没捕获到的话，这个Exception一定会捕获到')
    print(errName)
else:
    print('程序没有出现异常')
finally:
    print('不管try里有没有异常 最后都会执行')

print('----outside---')
# 输出 
# 程序没有出现异常
# 不管try里有没有异常 最后都会执行
# ----outside---
```
```
try:
    # 233 / 1
    233 / 0
    # print(num)
    # open('dontExistFile.txt')
except NameError:
    print('变量未命名的异常进行处理')
except FileNotFoundError:
    print('没找到文件呀')
except Exception as errName: # 上面的异常都没捕获到的话，这个Exception一定会捕获到
    print('上面的异常都没捕获到的话，这个Exception一定会捕获到')
    print(errName)
else:
    print('程序没有出现异常')
finally:
    print('不管try里有没有异常 最后都会执行')

print('----outside---')
# 输出 
# 上面的异常都没捕获到的话，这个Exception一定会捕获到
# divison by zero
# 不管try里有没有异常 最后都会执行
# ----outside---
```

## 异常的传递

先来看一个异常传递的例子：

```
def fn1():
    print('fn1-1')
    print(num)
    print('fn1-2')

def fn2():
    print('fn2-1')
    fn1()
    print('fn2-2')

def fn3():
    try:
        fn1()
        # fn2()
    except Exception as result:
        print("发生的异常为：%s"%result)

fn3() # 输出
# fn1-1
# 发生的异常为：name 'num' is not defined
```

分析上述的例子：
1. 调用fn3(),在`try`语句中调用fn1(),然后执行到`print(num)`这一句，抛出异常
2. 但是由于fn1()中没有对异常进行捕获和处理，所以程序将异常传递出来，传递到fn3()中的`try`中，在fn3()中被捕获。

上述例子中就存在着异常的传递，如果在fn3()中也没有使用`try`和`except`进行异常捕获和处理的话，那么就会把异常传递给程序默认的异常处理，即**报错崩溃**。


对于上述例子，如果我们在fn3()中，调用fn2()的话：
```
def fn1():
    print('fn1-1')
    print(num)
    print('fn1-2')

def fn2():
    print('fn2-1')
    fn1()
    print('fn2-2')

def fn3():
    try:
        # fn1()
        fn2()
    except Exception as result:
        print("发生的异常为：%s"%result)

fn3()
# 输出：
# fn2-1
# fn1-1
# 发生的异常为：name 'num' is not defined
```

这次的运行顺序也是一样的：
1. 调用fn2(),fn2()中调用fn1(),fn1()中然后抛出异常，fn1()自身没有异常处理操作，将异常传递给fn2()
2. fn2()中也没有异常处理，那么将异常传递给fn3()中，然后使用fn3()中的异常处理。

## 抛出自定义的异常

可以通过`raise()`来引发一个自定义的异常

首先来明确一下`except`的用法，我们写`except`后面跟一个异常名字，其实**异常本质上是一个类**，我们使用`except`后面加的是这个类的名字。

所以如果想抛出自定义的异常的话，我们需要先定义一个自定义的异常类：

```
class DefinedException(Exception):
    # DefinedException 就是我们创建的自定义的异常的类名。
    # 注意到我们自定义的异常类是继承自Exception这个类的， Exception是所有异常类的父类
    def __init__(self,length,atLeast):
        self.length = length
        self.atLeast = atLeast
    
    
```

在上面的例子中我们就定义了一个自定义的异常类，这个异常类接收两个参数作为属性。

接下来看这个自定义异常类的使用场景：
```
class DefinedException(Exception):
    # DefinedException 就是我们创建的自定义的异常的类名。
    # 注意到我们自定义的异常类是继承自Exception这个类的， Exception是所有异常类的父类
    def __init__(self,length,atLeast):
        self.length = length
        self.atLeast = atLeast
    

def main():
    try:
        inputStr = input('输入一串字符: ')
        if( len(inputStr) < 3):
            raise(DefinedException( len(inputStr),3) )
            # 注意这里的raise,传入的是异常类创建出来的一个实例对象
        
    except DefinedException as result:
        print('由于输入字符串过短，自定义异常触发')
        print('输入的字符串长度为：%d,至少应该输入的长度为：%d'%(result.length,result.atLeast))
        
    else:
        print('没发生异常')

main()
# 输出为：
# 输入一串字符: we
# 由于输入字符串过短，自定义异常触发
# 输入的字符串长度为：2,至少应该输入的长度为：3

```

我们在`raise()`中传递了一个自定义异常类创建出来的实例对象，从而触发了一个我们自己定义的异常。
值得一提的是，例子中的`except DefinedException as result:`这一句中的`result`就是我们上面`raise()`中传入的那个对象的引用。

## 异常处理中抛出异常的情况

来看一个在异常处理中不想对异常进行处理从而再次抛出异常的例子：

```
class Division:
    def __init__(self,switch):
        self.switch = switch
    
    def calc(self,a,b):
        try:
            return a/b
        except Exception as result:
            # 我们在异常处理中，可以选择进行处理，也可以不做处理，再次将异常抛出

            if self.switch: # 对异常进行处理的情况
                print('对异常进行处理,异常信息为：%s'%result)
            else: # 不对异常进行处理，而是再次抛出
                raise # 直接写raise就是将捕获的异常直接抛出
            

d1 = Division(True) # 对异常进行处理的情况
d2 = Division(False) # 不对异常进行处理

d1.calc(233,0) # 输出：对异常进行处理,异常信息为：division by zero

d2.calc(233,0) # 由于没有对异常进行处理反而是再次抛出，所以采用程序默认的异常处理办法
# 即报错，程序崩溃， ZeroDivisionError: division by zero

```

