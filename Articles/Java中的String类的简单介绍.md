---
title: Java中的String类的简单介绍
date: 2018/07/19 00:00:01
tags: 
- Java
- String
categories: 
- Java
---
Java中的String类的简单介绍
<!--more-->

String本质上是一个**字符数组**;

## 构造方法

String有很多构造方法，这边只介绍几个：
1. String(String original)
2. String(char[] value);
3. String(char[] value, int offset, int count);

来看几个例子：
1: String(String original)
```
public class TestString {
    public static void main(String[] args) {
        String s1 = new String("hello");
        System.out.println(s1); // String类的对象  直接输出时不会输出内存地址 而是内存地址指向的对象中包含的内容
    }
}
```
String是一种比较**特殊的引用类型**,直接输出的时候输出的不是内存地址，而是其指向的对象的内容。

2: String(char[] value);
```
public class TestString {
    public static void main(String[] args) {
        char[] chs = {'h','e','l','l','o'};
        String s2 = new String(chs);
        System.out.println(s2); // hello
    }
}
```
3.: String(char[] value, int offset, int count):

```
public class TestString {
    public static void main(String[] args) {
        char[] chs = {'h','e','l','l','o'};
        String s3 = new String(chs, 1, 3);
        System.out.println(s3); // ell 从第1个index开始，截取长度为3的字符串
    }
}
```

4: 字面量方式：
```
public class TestString {
    public static void main(String[] args) {
        String s4 = "hello";
        System.out.println(s4); // hello
    }
}
```

## 字面量创建和使用构造方法(即 new String())创建的区别

首先明确一点，字符串的内容是存储在方法区中的常量池里面的，这么做是为了字符串的重复使用。

来看例子:
```
public class TestString {
    public static void main(String[] args) {
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println(s1 == s2); // false
        
        String s3 = "hello";
        String s4 = "hello";
        System.out.println(s3 == s4); // true
        System.out.println(s1 == s3); // false
    }
}
```

那么使用`new String()`操作的时候，只要有`new`操作，那么肯定要在堆中开辟空间，所以s1,s2指向了堆中的不同地址的对象。但是由于字符串的内容是存储在方法区中的常量池里面的，所以s1,s2中的内容，还是一个地址，指向的是常量池中的内容即"hello"，s1和s2的内存地址不同，所以`s1 == s2`为false。

然而对于字面量方式创建字符串来说，并没有`new`操作，所以没有在堆中进行开辟空间，而是直接将s3,s4直接指向了常量池中的内容即"hello",所以`s3 == s4`是true(常量池中，重复使用)。

另外关于`s1 == s3`为false,是因为s1存的是指向的是堆中的对象的地址，这个堆中的对象的内容才是指向常量池中字符串的地址，而s3存的就是指向常量池中字符串的这个地址。所以二者不是一个地址。

pic

总结一下：
`new String()`创建的字符串是在堆内存，其的值又指向了方法区的常量池。
而字面量创建的字符串，直接就指向了方法区的常量池。


### String类常用的判断功能

- boolean equals(Object obj)
- boolean equalsIgnoreCase(String str)
- boolean startsWith(String str)
- boolean endsWith(String str)

```
public class TestString {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "Hello";
        String s3 = "hello";
        
        System.out.println( s1.equals(s2)); // false
        System.out.println( s1.equals(s3)); // true
        System.out.println( s1.equalsIgnoreCase(s2)); // true
    }
}
```
```
public class TestString {
    public static void main(String[] args) {
        String s1 = "hello";
        
        System.out.println( s1.startWith("hel")); // true
        System.out.println( s1.startWith("DeeJay")); // false
    }
}
```

### String类常用的获取功能

- int length()
- char charAt(int index)
- int indexOf(String str)
- String subString(int start)
- String subString(int start, int end)

```
public class TestString {
    public static void main(String[] args) {
        String s1 = "hello";
        
        System.out.println( s1.length() ); // 5
        System.out.println( s1.charAt(0)); // h
        System.out.println( s1.indexOf("h")); // 0
        System.out.println( s1.subString(2)); // llo
        System.out.println( s1.subString(2,4));  // ll
    }
}
```
### String类常见的转换功能

- char[] toCharArray()
- String toLowerCase()
- String toUpperCase()

```
public class TestString {
    public static void main(String[] args) {
        String s1 = "Hello";
        char[] chs = s1.toCharArray();
        for(int i = 0; i < chs.length; i ++) {
            System.out.println(chs[i]); // 依次输出 H e l l o
        }
        
        System.out.println( s1.toLowerCase() ); // hello
        System.out.println( s1.toUpperCase() ); // HELLO
    }
}
```
字符串的遍历方法：
    1. `length()`加上`charAt()`
    2. 把字符串转换为字符数组`toCharArray()`，然后遍历数组。
```
public class TestString {
    public static void main(String[] args) {
        String s1 = "hello wrold";
        for(int i = 0; i < s1.length(); i ++) {
            System.out.println(s1.charAt(i));
        }
        // 或者也可以使用toCharArray()
        char[] chs = s1.toCharArray();
        for(int i = 0; i < chs.length; i ++) {
            System.out.println(chs[i]);            
        }
    }
}
```

### String类的其他常用api

- String trim()
- String[] split(String str)

```
public class TestString {
    public static void main(String[] args) {
        String str = "a,b,c";
        String[] strArr = str.split(",");
        
        for(int i = 0; i < strArr.length; i ++) {
            System.out.println(strArr[i]);  // 依次输出 a b c           
        }
    }
}
```

### 关于StringBuilder

对于简单的字符串拼接问题，由于String不可变，每次进行拼接的时候，其实本质上是在先在常量池中新增一个空间存放"world",然后在新增一个空间存放`"helloworld"`,最后把str指向这个`"helloworld"`，而之前常量池中的`"hello"`和`"world"`就变成了垃圾。 
```
public class TestString {
    public static void main(String[] args) {
        String str = "hello";
        str += "world";
        
        System.out.println(str);
    }
}
```

而StringBuilder就可以解决这个问题，StringBuilder是一个**可变的**字符序列.

**String的内容是固定不可变的，而StringBuilder是可变的**

####关于StringBuilder的使用

```
public class TestStringBuilder {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder(); // 默认的无参的构造方法 创建的容量为16
        
        System.out.println(sb.capaticy()); // 16
        System.out.println(sb.length()); // 0

    }
}
```

### StringBuilder类常见操作

- public StringBuilder append(任意类型)
- public StringBuilder reverse()

```
public class TestStringBuilder {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder(); 
        StringBuilder sb2 = sb.append("Hello") ;
        System.out.println(sb); // Hello
        System.out.println(sb2); // Hello
        System.out.println(sb == sb2); // true
    }
}
```

来看这个例子 ，append返回的也是一个StringBuilder类的值，同时调用append()之后，原来的那个sb的值也发生了改变，最后发现sb 和 sb2 指向的是同一块地址，即说明StringBuilder是可变的(可修改)。

对于`append()`，其改变了自身之后，又返回了其自身，所以支持链式调用。

```
public class TestStringBuilder {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder(); 
        sb.append("Hello").append("World").append(true);
        System.out.println(sb); // HelloWorldtrue
    }
}
```

`reverse()`就是反转StringBuilder类的对象。

### String和StringBuilder类的相互转换

- StringBuilder ====> String:
 使用`public String toString()`
```
public class TestStringBuilder {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder(); 
        sb.append("hello").append("world");
        
        String s = sb.toString();
        System.out.println(s);
    }
}
```

- String  ====> StringBuilder:
使用构造函数 `StringBuilder(String str)`

```
public class TestStringBuilder {
    public static void main(String[] args) {
        String s = "Hello world";
        StringBuilder sb = new StringBuilder(s); 
        System.out.println(sb); // Hello world        
    }
}
```
