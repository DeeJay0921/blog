---
title: Java中的包装类,以及自动装箱和拆箱
date: 2018/07/21 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- 包装类
categories: 
- Java
---
Java中的包装类,以及自动装箱和拆箱
<!--more-->

### 包装类
由于基本类型只能做一些简单的操作和运算，所以Java又封装了各种基本类型，提供了包装类。

包装类提供了更多复杂的方法和变量。

来给出各个基本类型的包装类型

基本类型 | 包装类型
-- | --
byte | Byte
short | Short
char | Character
int | Integer
long | Long
float | Float
double | Double
boolean | Boolean

以Integer为例说明：


#### 类型转换
String ===> int
1. `int intValue()`
2. `static int parseInt(String s)`

int ===> String
1. `+ ""`
2. `String toString()`
3. `static String toString(int i)`


###自动装箱和拆箱

来看一些自动装箱拆箱的例子

`Integer i = 10;`这句代码就是自动装箱，相当于`Integer i = new Integer(10);`

`Integer i  = 10; int a = i;` 这句给a赋值i的语句，就是自动拆箱。相当于`int a = i.intValue();`

```
Integer i1 = 10;
Integer i2 = 20;
Integer i3 = i1 + i2;
```
前2句是自动装箱，第3句是先拆箱求和之后再装箱，相当于`Integer  i3 = new Integer(i1.intValue() + i2.intValue());`

```
ArrayList li = new ArrayList ();
li.add(1); // 相当于li.add(new Integer(i))
```

## 关于自动装箱的意义和可能引发的问题

1. 自动装箱给了基础类型一些额外的方法
2. 另外对于ArrayList<>等容器，是只接受基础类型的
3. 对于引用类型的话，可以赋值为null

> 针对可能赋值为null的这个情况，有些时候可能引发NullPointerException
```
public static void main(String[] args) {
        Integer a = null;
        int b = a; // java.lang.NullPointerException
    }
```

## 自动装箱类型的比较

原始类型可以直接使用` == `来进行比较，而装箱类型必须使用`equals()`方法进行比较：
```
public static void main(String[] args) {
        Integer c = 1000;
        Integer d = 1000;
        System.out.println(c == d); // false
        System.out.println(c.equals(d)); // true
}
```
另外 Integer等是不可变的，这意味着你在进行如下操作时，其实是创建了一个新的number对象：
```
public static void main(String[] args) {
        Integer number = 1000; // Integer@969
        number = number + 1; // Integer@970  debug状态下可以看到number存储的地址已经发生了改变
        System.out.println("number = " + number);
    }
```

> Interger的缓存机制（详情搜IntegerCache），其实对于-128 => 127范围之间的数字，Integer内部做了缓存，是可以直接==判断相等的。

```
    public static void main(String[] args) {
        Integer c = -128;
        Integer d = -128;
        System.out.println(c == d); // true
        
        Integer e = 127;
        Integer f = 127;
        System.out.println(e == f); // true
    }
```
