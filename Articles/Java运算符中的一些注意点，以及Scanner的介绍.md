---
title: Java运算符中的一些注意点，以及Scanner的介绍
date: 2018/07/16 00:00:01
tags: 
- Java
categories: 
- Java
---
Java运算符中的一些注意点，以及Scanner的介绍
<!--more-->

# 一，运算符的一些注意点

### 字符和int相加

```
		char a = 'A';
		int b = 1;
		
		int res1 = a + b;
		System.out.println(res1); // 66
//		java中字符和int相加 ,  选取字符的值来进行相加,  'A'为65，'a'为97， '0'为48
		System.out.println("----------");
```

### 字符串和int 相加
```
        int c = 10;
        String str = "Hello ";
        String res2 = str + c;
        System.out.println(res2); // Hello 10
//      字符串和int 相加  拼接为字符串
        System.out.println("----------");

        int num1 = 10;
        int num2 = 20;
        String str2 = " testStr ";
        System.out.println(str2 + num1 + num2); // testStr 1020
        System.out.println(num1 + num2 + str2); // 30 testStr
        System.out.println("----------");
```
### += 等赋值运算操作涉及隐式类型转换
```
        //关于 += 等赋值运算操作的时候，涉及到了隐式类型转换
        short s1 = 1; // s1 为short型
        s1 += 1; // 这时候加的1为int型  实际相当于(short)(s1 + 1);
        System.out.println(s1);
        System.out.println("----------");
```
### && 和 &的区别
```
        // && 和 &的区别
        
        int num3 = 10;
        int num4 = 20;
        System.out.println( (num3++ > 10) & (num4++ > 20) ); // false & false ===> false
        System.out.println(num3); // 11
        System.out.println(num4); // 21
        int num5 = 10;
        int num6 = 20;
        System.out.println( (num5++ > 10) && (num5++ > 20) ); // false && false ===> false
        System.out.println(num5); // 11
        System.out.println(num6); // 20
        // 可以看到结果是一样的，但是&把前后两项都进行了执行，而&&如果前面一项为false时，直接判断为false，后面的不进行执行
        System.out.println("----------");
```


# 二，使用Scanner获取键盘输入
 
### 简单使用

```
package com.DeeJay;

import java.util.Scanner; // 1.导入jdk中的Scanner包

public class ScannerDemo {
	public static void main(String[] args) {
		// 2.创建一个键盘录入对象
		Scanner sc = new Scanner(System.in);

		// 3.接收数据
		System.out.println("input a number: ");
		int inputNumber = sc.nextInt();

		// 输出结果
		System.out.println("the Number which u entered is: " + inputNumber);

		/*
		 * 	运行结果
		 * input a number: 89 
		 * the Number which u entered is: 89
		 */
	}
}
```

### sc.next()和sc.nextLine()

- next():
```
package com.DeeJay;

import java.util.Scanner; // 1.导入jdk中的Scanner包

public class ScannerDemo {
	public static void main(String[] args) {
		// 创建一个键盘录入对象
		Scanner sc = new Scanner(System.in);

		// 接收数据
		System.out.println("input a String: ");
		String inputString = sc.next();
		// 输出结果
		System.out.println("the String which u entered is: " + inputString);

		/*
		 * 	运行结果
		 * input a String: hello DeeJay
		 * the String which u entered is: hello
		 */
	}
}
```
- nextLine():
```
package com.DeeJay;

import java.util.Scanner; // 1.导入jdk中的Scanner包

public class ScannerDemo {
	public static void main(String[] args) {
		// 创建一个键盘录入对象
		Scanner sc = new Scanner(System.in);

		// 接收数据
		System.out.println("input a String: ");
		String inputString = sc.nextLine();
		// 输出结果
		System.out.println("the String which u entered is: " + inputString);

		/*
		 * 	运行结果
		 * input a String: hello DeeJay
		 * the String which u entered is: hello DeeJay
		 */
	}
}
```

#### next() 与 nextLine() 区别
- next():
1、一定要读取到有效字符后才可以结束输入。
2、对输入有效字符之前遇到的空白，next() 方法会自动将其去掉。
3、只有输入有效字符后才将其后面输入的空白作为分隔符或者结束符。
next() 不能得到带有空格的字符串。

- nextLine()：
1、以Enter为结束符,也就是说 nextLine()方法返回的是输入回车之前的所有字符。
2、可以获得空白。
