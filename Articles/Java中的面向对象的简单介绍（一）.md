---
title: Java中的面向对象的简单介绍（一）
date: 2018/07/18 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象的简单介绍（一）
<!--more-->

##简介
一个简单的示例：
```
public class Student {
    // 成员变量
    String name;
    int age;
    
    //成员方法
    public void study() {
      System.out.println("学习");
    }
    public void eat() {
      System.out.println("吃饭");
    }
    
}
```
这个Student为一个事物描述类，不适合加main方法。

那么如果我们想使用这个Student类中的成员变量和方法，只需要创建一个该类的对象就好了。

再创建一个StudentUse类来使用：
```
public class StudentUse {
    public static void main(String[] args) {
        Student stu = new Student();
        System.out.println(stu); // com.DeeJay.Student@4fe5e2c3
    }
}
```
其中`Student stu = new Student();`就使用了Student类创建了一个Student类型的对象。

现在就可以通过创建的Student类的实例对象stu来使用Student类中的成员了:

```
public class StudentUse {
    public static void main(String[] args) {
        Student stu = new Student();
        
        stu.study(); // 学习
        stu.name = "DeeJay";
        System.out.println(stu.name); // DeeJay
    }
}
```

### 关于成员变量和局部变量的区别

其实这二者差别还是挺大的：

首先定义位置上：
- 成员变量在类中，方法外定义。
- 局部变量在方法内部或者是传形参时定义。

```
public class Test {
    String p; // p 为成员变量
    
    public void  test(String str) { // str为局部变量
        int str1 = str.toUpperCase(); // str1 也为局部变量
    }
}
```
其次在内存中的位置不同：
- 成员变量在堆中
- 局部变量在栈中

这种内存位置不同导致二者生命周期也不同：
- 成员变量随着对象的创建而存在，随着对象的消失而消失
- 局部变量随着方法开始调用被创建，随着方法调用结束而消失

另外初始化值也有差异：
- 成员变量是有默认值的(比如String型的为null,int型的为0等等)
- 而局部变量必须先定义然后使用。


## private 关键字

为了防止直接访问成员造成的问题，引入了private关键字

private可以修饰成员变量和成员方法，被修饰的成员**只可以在本类中才能被访问**


对于被private修饰的成员变量，提供相应的getXxx()和setXxx()来获取和设置成员的值，方法**要用public**修饰。

```
package com.DeeJay;

public class TestPrivate {
	
	private String name; // private修饰成员属性
	
	// 如果想进行设置  需要定义setXxx()
	public void setName(String argName) { // 在setXxx()内部可以对要给赋的值进行操作
		name = argName;
	}
	// 如果想进行取值   需要定义getXxx()
	public String getName() {
		return name;
	}
}

```

```
package com.DeeJay;

public class TestPrivateDemo {
	
	public static void main(String[] args) {
		TestPrivate t = new TestPrivate();

//		t.name = "DeeJay"; // 报错 	The field TestPrivate.name is not visible
		
		t.setName("DeeJay");
		
		System.out.println(t.getName()); // DeeJay
	}

}

```

使用private进行封装之后，必须通过方法来进行访问成员，提高了代码的安全性，同时把代码用方法进行封装，也提高了代码的复用性。


### Java中的this关键字

在上面这个TestPrivate 类的定义中，setName()中的赋值逻辑中，如果我们想写成：
```
    public void setName(String name) { // 在setXxx()内部可以对要给赋的值进行操作
        name = name;
    }
```
会出现问题，因为**在局部作用域中，如果有成员变量(即name)和局部变量(即形参name)同名的话，那么优先是被认为是局部变量(就近原则)；**

在这种情况下，引入了this关键字

```
    public void setName(String name) { // 在setXxx()内部可以对要给赋的值进行操作
        this.name = name;
    }
```

这么一来，问题就解决了；

和JS一样的，这个this指向的就是`TestPrivate t = new TestPrivate();`这条语句创建出来的`t`对象。


this的使用场景一般就是上述情况。


### 构造方法

构造方法即**给对象的数据进行初始化**

格式：**方法名和类名相同**,并且**没有返回值(连void也不能写)**


```

public class Student {
    public Student() { //这就是构造方法  没有返回值 也不能写void
        System.out.println("构造方法被调用");
    }
}
```

构造函数在进行`new`操作的时候就会被调用：
```
public class StudentUse {
    public static void main(String[] args) {
        Student stu = new Student(); // 此时就会调用Student 类的构造方法
    }
}
```

关于构造方法，有几个注意点：
- 如果我们没有显式的给定构造方法，那么系统自动给一个无参的构造方法，但是显式给定了之后，系统就不再提供默认无参构造方法了；
- 构造方法也支持重载；
```
public class Student {
    private String name;
    private int age;
    public Student() { //这就是构造方法  没有返回值 也不能写void
        System.out.println("构造方法被调用");
    }
        
    public Student(String name) {
        this.name = name;
    }
    
    public Student(int age) {
        this.age = age;
    }
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```





那么有了构造方法之后，给成员变量赋值的方法就有：
1. 使用setXxx();
2. 使用构造方法；
