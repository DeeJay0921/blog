---
title: Java中的数组以及java-util-Random的简单介绍
date: 2018/07/17 00:00:01
tags: 
- Java
- 数组
categories: 
- Java
---
Java中的数组以及java-util-Random的简单介绍
<!--more-->

# Random

```
package com.DeeJay;

import java.util.Random;
public class RandomDemo {
	public static void main(String[] args) {
		Random r = new Random();
		
		int randomNumber = r.nextInt(10); // 可以选定范围 为 [0,10) 的一个区间
		System.out.println(randomNumber);
	}
}

```

# 数组

Java中的数组是存储**同一种数据类型**多个元素的容器。

数组既可以存储基本数据类型，也可以存储引用数据类型。

## 数组的定义

```
		// define:
		int[] arr1; // 1.推荐的定义方法  定义了一个int类型的数组，数组名为arr1
		int arr2[]; // 2.定义了一个int类型的变量，变量名是数组arr2
```

## 数组的初始化
即 开辟内存空间，给数组中每个元素赋值

数组初始化分为动态初始化和静态初始化。

```
		// 数组初始化（开辟内存空间，给数组中每个元素赋值）
		// 1.动态初始化（给定长度，由系统给出初始值）
		int[] arr3 = new int[10];
		/*
		 * new 代表为数组申请内存分配 ， 10 代表数组的长度
		 * */
		System.out.println(arr3); // [I@299a06ac 为arr3的地址值
		for(int i = 0; i < arr3.length; i ++) {
			System.out.println(arr3[i]); // 输出10个 0
		}
		
		// 2.静态初始化 （给出初始值，由系统决定长度）
		// 静态初始化的语法举例：
		int[] arr4 = new int[] {1,2,3,4,5};
		for(int i = 0; i < arr4.length; i++) {
			System.out.println(arr4[i]); // 输出1 2 3 4 5
		}
```

对于上述静态初始化的例子  可以简写为  `int[] arr5 = {1,2,3,4,5};` 

##  二维数组

### 二维数组的定义
```
		//define
		int[][] arr1; // 推荐的方式
		int arr2[][];
		int[] arr3[];
```

### 二维数组的初始化

一样分为动态和静态
```
		// 动态初始化
		// 写法：  数据类型[][] = new 数据类型[m][n];
		// m 代表这个二维数组中一维数组的个数，即二维数组的length, n代表的是一位数组的长度
		int[][] arr1 = new int[3][2];
		System.out.println(arr1.length); //3
```
```
		// 静态初始化
		// 数据类型[][] = new 数据类型[][]{ {}, {}, {}, ...};
		// 简写 :  数据类型[][] = { {}, {}, {}, ... };
//		int[][] arr2 = new int[][] {{1,2,3}, {4,5,6}, {7,8,9}};
		int[][] arr2 = {{1,2,3}, {4,5,6}, {7,8,9}};

		for(int i = 0; i < arr2.length; i++) {
			System.out.println(arr2[i]); // 依次输出[I@299a06ac [I@383534aa [I@6bc168e5  代表了三个元素即三个一维数组
			for(int j = 0; j < arr2[i].length; j ++) {
				System.out.println(arr2[i][j]); // 依次输出了这三个一维数组的每个元素
			}
		}
```


# Java中的栈和堆


- 栈中存放的是 **局部变量**（定义在方法内部的变量）；
- 堆中存放的是 **new出来的对象**；

`int[] arr3 = new int[10];` 这句语句，即把堆中的`new int[10];`创造的对象的地址(即`[I@299a06ac`)赋值给了这个arr3变量。

arr3这个变量在栈中，其存储的是一个地址(`[I@299a06ac`)，指向的是堆中`new int[10];`创建出来的那一个对象。

对于堆中的对象，每一种不同的类型的默认值也不一样：
类型 | 默认值
-- | --
byte,short,int,long | 0
float,double | 0.0
char | '\u0000'
boolean | false
引用类型(类，数组，接口) | null

比如上面的arr3,其每一项元素都为int,所以默认值都为0;

另外堆中的对象，在使用完毕后， 会在垃圾回收器空闲的时候被回收（不是立即）；
而在栈中的局部变量，使用完后是立即被回收的；

