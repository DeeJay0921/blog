---
title: Java中的运算系统中需要注意的地方
date: 2019/06/23 00:00:01
tags: 
- Java
- 运算
categories: 
- Java
---
Java中的运算系统中需要注意的地方
<!--more-->

## Tips:
1. JDK ≈  JRE + javac
2. JAVA_HOME:  jdk安装的文件夹

## 关于包名
全限定类名 即 带包名的完整的类 防止类名冲突
> Full Qualified Name
```
com.github.DeeJay.pet1.Cat
```

# 基本运算符中需要注意的：

对于`+` `-` `*` `/` `%`等基本运算符，需要注意的一点是：

**Java中相同类型的数据进行计算的结果也会是同一类型**
即：
```
int res = 3 / 2;
System.out.println("res = " + res); // res = 1
```
而**不同类型的数据进行运算，最终的结果是按最高的类型来的**
```
double res = 3.0d / 2;
System.out.println("res = " + res); // res = 1.5
```

>  对于`%` 要注意的是，取余的结果是**带符号的**

来看一个例子：
```
    public static void main(String[] args) {
        int res = -9 % 5;
        System.out.println("res = " + res); // res = -4

        System.out.println("isOdd(3) = " + isOdd(3)); // isOdd(3) = true
        System.out.println("isOdd(3) = " + isOdd(-3)); // isOdd(3) = false
    }

//    判断一个数字是奇数
    public static boolean isOdd(int number) {
        return number % 2 == 1;
    }
```

由于`%`带有符号，导致判断是否奇数的函数出现了bug
可以改为：
```
//    判断一个数字是奇数
    public static boolean isOdd(int number) {
//        下面2种方式都行
//        return Math.abs(number) % 2 == 1;
        return number % 2 != 0;
    }
```
> a = a + 1;真实的运算过程为 1.将内存中存储的a的值传给CPU中的寄存器(register) 2.CPU执行+1计算 3.将计算后的结果重新写回存储之前a的内存中。


### 关于自增/减:
跟其他语言一样:
```text
public static void main(String[] args) {
    int a = 0;
    int b = 0;
    System.out.println("a++ = " + a++); // a++ = 0
    System.out.println("++b = " + ++b); // ++b = 1
}
```

## 关于字符串中的`+`

对于字符串中的`+`操作会进行字符串的拼接（即调用默认的toString()），但是由于**Java中的String不可变**，所以JDK会偷偷使用StringBuider来进行字符串的拼接，从而避免了创建很多零碎的小String对象。

