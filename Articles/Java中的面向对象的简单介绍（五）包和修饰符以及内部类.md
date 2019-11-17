---
title: Java中的面向对象的简单介绍（五）包和修饰符以及内部类
date: 2018/07/18 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象的简单介绍（五）包和修饰符以及内部类
<!--more-->

###包
特点：
- 可以有多层
- 不同包下面的文件名可以重复
- 包的声明必须要在第一行


### 不同包下的相互访问
相同包下的类可以直接访问，不需要其他的操作。

那么不同包下的类的相互访问的方式有：
1. 使用类的**全名**（即包名+类名），举例：`java.util.ArrayList list1 = new java.util.ArrayList();`
2. 使用关键字`import`直接把这个类名导入进来，举例：`import java.util.ArrayList;`
*也可以写`import java.util.*;`代表把这个包下的所有类都导入，但是并没有导入这个包的子包下的类*



### 权限修饰符

`public` `default`  `private` `protected`

修饰符 | 当前类中的权限 |  相同包下不同类中权限 | 不同包下的权限
-- | -- | -- | --
public | 可以访问 | 可以访问 | 可以访问
default | 可以访问 | 可以访问 | 不可以访问
private | 可以访问 | 不可以访问 | 不可以访问
protected | 可以访问 | 可以访问 | 不可以访问

这么一看default和protected的差别没体现出来

default是当前包下使用，而protected是让子类对象使用。（super.protectedMethod();）

## 修饰符

顺便总结一下所有的修饰符,true为可以修饰，false为不可以修饰


修饰符 | 类 | 成员变量 | 成员方法 | 构造方法
-- | -- | -- | -- | --
public | true | true | true | true
default | true | true | true | true 
protected | false | true | true |true
private | false | true | true | true
abstract | true | false | true | false
static | false | true | true | false
final | true | true | true | false

###一般常见的规则：
- 类： 一般使用public 修饰类，原则上一个java文件中只能有一个类，但是如果有多个类的话，类名和文件名相同的那个类，必须要用public修饰，并且其他类不可以使用public修饰



- 成员变量： 一般使用private修饰，并且提供getXxx()和setXxx()供外部使用。

- 成员方法： 一般方法都用public修饰，不想暴露出的话就用private,想让子类用就使用protected

- 构造方法： 一般也使用public修饰，但是如果不想让当前类实例化，可以private构造函数。


### 内部类

在另外一个类的内部的类

分类：
- 成员内部类
- 局部内部类
- 匿名内部类

内部类虽然在别的类的内部，但是编译时会有单独的class文件。

#### 成员内部类：
```
public class InnerClass {
	public static void main(String[] args) {
		Outer o = new Outer();
		o.method(); // Inner Function  3  
		
		// 如果想直接在外部调用Inner的对象调用方法的话  可以直接import 包名.Outer.Inner
		// 也可以直接写类的全名来创建对象
		Outer.Inner i = new Outer().new Inner();
		i.func(); // 这样就可以直接创建Inner的对象了
	}
}

class Outer {
	private int num = 3;
	public void method() {
		Inner i = new Inner();
		i.func();
	}
	class Inner { // 在类的内部定义的类  成员内部类  可以访问Outer类中的成员
		public void func() {
			System.out.println("Inner Function");
			System.out.println(num);
		}
	}
}
```
### 成员内部类的修饰符
关于成员内部类的修饰符，有几个特殊情况：
- private,如果使用private修饰内部类，那么只能在当前类中访问，不可以在外部使用：
```
public class InnerClass2 {
    public static void main(String[] args) {
		 // Outer.Inner i = new Outer().new Inner(); //  由于Inner被private修饰 提示Outer.Inner不可见
    }
}

class Outer {
    private int num = 3;
    public void method() {
        Inner i = new Inner();
        i.func();
    }
    private class Inner {  // 使用private修饰了成员内部类之后  外部不可见
        public void func() {
            System.out.println("Inner Function");
            System.out.println(num);
        }
    }
}
```
- static, 如果使用static修饰，那么不需要创建Outer类的实例就可以创建Inner的实例：
```
public class InnerClass2 {
    public static void main(String[] args) {
		 Outer.Inner2 i = new Outer.Inner2(); // 由于Inner被static修饰  所以不用创建Outer的实例  直接new Outer.Inner2()就可以创建这个实例了
    }
}

class Outer {
    private int num = 3;
    public void method() {
        Inner i = new Inner();
        i.func();
    }
	
	static class Inner2 {  // 使用static修饰了成员内部类之后  创建对象就不用创建Outer类的实例了。
        public void func() {
            System.out.println("Inner Function");
            System.out.println(num);
        }
    }
}
```

当然也可以使用其他修饰符如abstract, final等修饰内部类，但是没什么太大的实际意义。


#### 局部内部类

局部内部类的使用较少，在成员方法内部定义类，在方法外部就不可用。

```
// 局部内部类
public class InnerClass3 {
    public static void main(String[] args) {
        Outer o = new Outer();
		o.method(); // Inner Function
    }
}

class Outer {
    private int num = 3;
    public void method() {
		class Inner {  // 局部内部类 在方法内部定义
			public void func() {
				System.out.println("Inner Function");
			}
		}
		
		Inner i = new Inner(); // 先定义类后实例化
		i.func();
    }
}
```
#### 匿名内部类

可以看做是一个没有名字的局部内部类，也是定义在方法当中。

必须要在定义匿名内部类的时候创建其对象。

原理是：创建了继承这个类的子类或者是实现了这个接口的子类的对象。

举例来说明：
```
// 匿名内部类
public class InnerClass4 {
    public static void main(String[] args) {
        Outer o = new Outer();
        o.method(); // Inner Function
    }
}

interface Inner { // 创建一个接口或者类
	public abstract void func();
}

class Outer {
    public void method() {
		new Inner() { // 定义内部类
			@Override
			public void func() {
				System.out.println("Inner Function");
			}
		}.func(); // 可以直接调用方法，前面的就是一个实例对象了
		
		// 也可以这么写
		Inner i = new Inner() { // 由于实现了接口  可以向上转型
			@Override
			public void func() {
				System.out.println("Inner Function");
			}
		};
		i.func();
    }
}
```

