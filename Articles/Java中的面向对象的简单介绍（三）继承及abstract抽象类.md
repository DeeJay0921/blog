---
title: Java中的面向对象的简单介绍（三）继承及abstract抽象类
date: 2018/07/18 00:00:01
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象的简单介绍（三）继承及abstract抽象类
<!--more-->

# 继承 extends
多个类有共同的成员变量和成员方法，可以抽取到另外一个类(父类)，再让多个类去继承这个父类，这些类就可以获取到父类中的成员。

```
public class ExtendsDemo {
	public static void main(String[] args) {
		Dota dead = new Dota();
		dead.start(); // 启动游戏
		Lol l = new Lol();
		l.start(); // 启动游戏
	}
}

class Game {
	String name;
	double version;
	String agent;
	
	public void start() {
		System.out.println("启动游戏");
	}
	
	public void exit() {
		System.out.println("退出游戏");
	}
}

class Dota extends Game {} // Dota继承Game类
class Lol extends Game {} // Lol继承Game类
```

### Java中继承的特点
- 只支持单继承，不支持多继承（即只可以有一个父类）
`class sub extends Demo{} // ok`
`class sub extends Demo1,Demo2{} // error`
- 支持多层继承(继承体系)
```
class A{}
class B extends A{}
class C extends B{}
```
### Java中成员变量继承的特点

- 子类中只能获取父类中**非private**的成员变量
```

public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();
		c.sayName();
		c.sayAge();
	}
}
class Dad {
	String name = "dad";
	private int age = 40;
}

class Child extends Dad {
	public void sayName () {
		System.out.println(name); 
	}
	public void sayAge () {
		System.out.println(age); 
	}
}
```
上述例子中，提示`Dad.age`这个字段不可见。所以子类中只能获取父类中**非private的成员变量**
- 如果子类和父类中的成员变量名字一样，那么遵循**就近原则**
```
public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();
		c.sayName(); // child	就近原则  输出子类中的成员属性
	}
}
class Dad {
	String name = "dad";
}

class Child extends Dad {
	String name = "child";
	public void sayName () {
		System.out.println(name); 
	}
}
```
- 对于上述的就近原则，可以通过`super`关键字来获取父类中的成员变量和方法
```
public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();
		c.sayName(); // dad	
	}
}
class Dad {
	String name = "dad";
}

class Child extends Dad {
	String name = "child";
	public void sayName () {
		System.out.println(super.name);  // super.name 获取父类中的成员
	}
}
```
- 当涉及到局部变量和成员变量重名时，得使用`this`来获取当前类中的成员
```
public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();
		c.sayName(); 	
	}
}
class Dad {
	String name = "dad";
}

class Child extends Dad {
	String name = "child";
	public void sayName () {
		String name = "testName";
		System.out.println("父类：" + super.name);  // super.name 获取父类中的成员
		System.out.println("当前类：" + this.name);  // this.name 获取当前类中的成员
		System.out.println("局部变量：" + name);  // name 获取当前作用域中的局部变量
	}
}
```

### Java中成员方法继承的特点

方法继承支持重写(不是重载)，子类中的方法和父类中。的方法完全一致时，可以在子类中重写这个方法。子类的对象调用时调用的是子类重写的方法

关于重写有一些注意点：
- 在子类重写的方法中，可以使用`super.methodName()`来执行父类中的方法，这么写既可以使用父类的功能，也可以追加子类的逻辑。
- 重写前可以添加注解`@Override`加强规范
- 不能重写父类中private的成员变量，子类中方法的权限(如：public)必须要比父类中的权限等级高。

*定义方法的时候，可以不写权限修饰(比如: public), 那么就代表默认权限(如`void show() {}`)*

### Java中构造方法的执行顺序

1. 如果子类的构造方法中的**第一行代码中没有调用父类或者子类的构造方法时**，那么会**默认调用父类的无参构造方法**
```

public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();// Super Constructor---no Args 	Sub Constructor---no Args
	}
}
class Dad {
	public Dad () {
		System.out.println("Super Constructor---no Args");
	}
	public Dad (int num) {
		System.out.println("Super Constructor---have Args");
	}
}

class Child extends Dad {
	public Child () {
		System.out.println("Sub Constructor---no Args"); // 第一行没有调用父类构造方法，默认调用父类无参构造
	}
	public Child (int num) {
		System.out.println("Sub Constructor---have Args");
	
```

2. 可以使用`super()`来在子类的构造方法的第一行中调用父类的构造方法
```
public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();// Super Constructor---have Args 	Sub Constructor---no Args
	}
}
class Dad {
	public Dad () {
		System.out.println("Super Constructor---no Args");
	}
	public Dad (int num) {
		System.out.println("Super Constructor---have Args");
	}
}

class Child extends Dad {
	public Child () {
		super(1); // 调用了父类的构造方法
		System.out.println("Sub Constructor---no Args");
	}
	public Child (int num) {
		System.out.println("Sub Constructor---have Args");
	}
}
```
3. 不管有没有参数，父类的构造方法总是优先于子类的构造方法被执行
```
public class ExtendsDemo {
	public static void main(String[] args) {
		Child c = new Child();// Super Constructor---no Args 	Sub Constructor---have Args		Sub Constructor---no Args
	}
}
class Dad {
	public Dad () {
		System.out.println("Super Constructor---no Args");
	}
	public Dad (int num) {
		System.out.println("Super Constructor---have Args");
	}
}

class Child extends Dad {
	public Child () {
		this(1); // 调用了子类的有参构造方法
		System.out.println("Sub Constructor---no Args");
	}
	public Child (int num) {
		System.out.println("Sub Constructor---have Args");//有参构造第一行中没调用父类的构造方法，于是默认调用父类的无参构造
	}
}
```


###this 和 super的区别
this： 当前对象的引用
super：子类对象的父类引用

在构造方法中的第一行中，直接写`this()`或者`super()`就是默认调用构造方法。

*如果子类中没有制定的成员属性，而只有父类中有的时候，调用`this.xxx`就是访问的父类的成员属性*

###继承的优缺点
优点：
- 提高代码的复用性和可维护性

缺点：
- 类的耦合性增强了
- 开发的原则：高内聚低耦合
- 内聚：就是自己完成某件事的能力
- 耦合：类和类的关系

### 抽象类

abstract: 关键字，用于修饰方法和类

抽象方法：不同类的方法是相似的，但是具体逻辑不太一样，只能抽取声明，没有具体的方法体。 即没有方法体的方法就是抽象方法，抽象方法只能在抽象类中使用。

抽象类： 有抽象方法的类必须是抽象类。

如果有其他类继承了抽象类，那**必须要重写其中的抽象方法**，**或者这个类必须也是抽象类**。

```
public class ExtendsDemo {
    public static void main(String[] args) {}
}

abstract class Animal { // 有抽象方法的类必须是抽象类
	public abstract void eat(); // abstract修饰的是方法是抽象方法  没有方法体
}

class Cat extends Animal { // 如果一个类继承了抽象类，而且同时还不也是一个抽象类的话，那么必须要重写抽象类中的抽象方法
	@Override
	public void eat() { // 重写抽象类中的抽象方法
		System.out.println("猫吃鱼");
	}
}

abstract class Dog extends Animal {} // 抽象类的Dog  本身也是一个抽象类  就不需要重写抽象方法了
```
抽象类的特点：
- 抽象方法只能在抽象类中
- 抽象类不能实例化(不能new)
- 抽象类可以有非抽象类(非抽象类被继承不需要强制重写)

抽象类的成员的特点：
1. 成员变量
    - 可以有成员变量
    - 可以有常量(被final修饰)
2. 成员方法
    - 可以有抽象方法和非抽象方法
3. 构造方法
    - 可以有构造方法(但是不能创建对象)，因为需要对类中的成员变量进行初始化


**final**: 修饰类、成员变量、成员方法，修饰了之后不能被继承

抽象类的细节：
- abstract 不可以和 final, private 共存
- 可以有构造函数(因为有成员变量，要初始化成员变量，同时还要被继承，子类继承父类也要调用父类的构造函数)
- 内部也可以没有抽象方法(当不想让这个不可以类实例化的时候，当然也可以使用private构造函数来达到一样的效果)
