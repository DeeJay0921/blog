---
title: Java中的函数(方法)的简单介绍
date: 2018/07/18 00:00:01
tags: 
- Java
categories: 
- Java
---
Java中的函数(方法)的简单介绍
<!--more-->

### 定义和调用

```
	/*	define：
	 *  	修饰符  返回值类型  方法名(type arg1, type arg2){
	 *  		函数逻辑;
	 *  		return 值;
	 *  	}
	 * */
```
举例：

```
package com.DeeJay;

public class MethodDemo {
	
	public static void main(String[] args) {
		int sum = getSum(1,2);
		System.out.println(sum); // 3
	}
	
	public static int getSum(int num1, int num2) {
		return num1 + num2;
	}
}

```

当没有返回值时可以给一个void作为返回值类型，同时void返回类型只支持直接调用。

### 重载

对于方法的重载，同名的函数，参数的个数或者类型不同即视为不同函数，称为函数的重载。

看下重载的例子：
```
package com.DeeJay;

public class MethodDemo {
	
	public static void main(String[] args) {
		int res1 = getSum(1,2);
		System.out.println(res1); // 3
		
		int res2 = getSum(1,2,3);
		System.out.println(res2); // 6
		
		float res3 = getSum(1.0F,2.0F);
		System.out.println(res3); // 3.0
	}
	
	public static int getSum(int num1, int num2) {
		return num1 + num2;
	}
	
	public static int getSum(int x, int y, int z) {
		return x + y + z;
	}
	
	public static float getSum(float x, float y) {
		return x + y;
	}
}

```


### 基本类型和引用类型的传参

和JS一样，附两个例子：

基本类型：

```
  public static void main(String[] args){
    int num = 0;
    basicType(num);
    System.out.println(num); // 0  基本类型不变
  }

  public static void basicType(int num){
    num += 1;
  }
```

引用类型：

```
    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5};
        referType(arr);
        for(int i = 0; i < arr.length; i ++) {
            System.out.println(arr[i]); // 输出2 4 6 8 10  引用类型发生改变
        }
    }
    public static void referType(int[] arr) {
        for(int i = 0; i < arr.length; i ++) {
            arr[i] = arr[i]*2;
        }
    }
```
