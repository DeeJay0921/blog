---
title: Java异常体系
date: 2019/09/04 00:00:01
tags: 
- Web
- 异常
- Exception
categories: 
- Java
---
Java异常体系
<!--more-->

从IOException中引出的问题：
- Java的异常体系
    - 在return语句之外，为方法提供了一种额外的出口，即 throw Exception
    - IOException通常代表**预期之内**的异常
- 万能解决方案
    1. 继续throw
    2. try/catch

### try/catch/finally
- 如果没有try，异常将击穿所有的栈帧
- catch可以将一个异常捕获到
- finally可以执行清理工作
- try-with-resources （JDK7以上）
对于所有实现了AutoCloseable接口的对象，可以直接声明在try()中，可以不必写finally，JDK会自动关闭该对象

> 如果当前的方法中throw了Exception，但是又没有可以处理的逻辑(try/catch)，那么这个异常会一直击穿所有的栈帧一直到被捕获。



> 关于finally，无论在前面的try/catch里面做了什么样的操作(例如return xxx;等)，Java中的finally都一定会被执行。且finally里面不建议使用return语句。还可以不写catch(不建议)，直接只写try和finally

## throw/throws
- throw抛出一个异常
- throws只是一个声明，代表方法可能会丢出一个异常


## Java的异常体系
- Throwable： 可以被抛出的东西（有毒），为所有的错误和异常的父类
    - Exception： checked exception （受检异常，有毒，代表预料之中的异常）
        - RuntimeException：（运行时异常，无毒，代表预料之外的异常）
    - Error：错误（无毒，代表严重的错误）

> 有毒和无毒的判定标准为，任何调用该方法的方法是否需要被迫继续进行throw或者try/catch，有毒的即为checked exception,无毒的为unchecked exception

- catch的级联与合并
catch可以连续写多个catch语句，其中的异常类型顺序要按照子类到父类即从小到大的来写。另外对于相同的处理逻辑，catch语句还可以合并：
```
try {
  doSomething();
}catch(NullPointerException | IllegalAccessException e) {
  similarHandle();
}
```

## Java的异常的栈轨迹
- 栈轨迹：stack trace 排查问题最重要的信息
- 异常链：查看报错信息，caused by即为原始的异常，如有多个caused by，即最底层的为最原始的异常，其余的都是对异常进行了包装处理。


## 异常的处理原则
- 能用if/else处理的，不要用异常
因为没有办法保证捕获到的异常是你期望的异常
- 尽早抛出异常
如果当前方法不能处理异常，那么该立即抛出，不应该再继续写之后的错误的处理逻辑
- 异常要准确，带有详细的信息
- 抛出异常也比不处理悄悄的执行错误的逻辑要好
- 先判断本方法是否有责任处理该异常
    - 不要在一个方法里处理不归自己管的异常
- 再判定本方法是否有能力处理这个异常
    - 如果本方法不能处理，则抛出异常
- 如非万分必要，不用忽略异常

## 常见的JDK内置异常
- NullPointerException
- ClassNotFoundException/NoClassesDefFoundException
- IllegalStateException
- IllegalArgumentException
- IllegalAccessException
- ClassCastException
