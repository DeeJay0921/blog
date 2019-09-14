---
title: Java中的面向对象的简单介绍（四）接口和多态
date: 2018/07/18 00:00:01
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象的简单介绍（四）接口和多态
<!--more-->

因为Java中继承具有单一性，只能继承一个父类，所以为了处理这种局限性，Java又提供了接口。

# 接口interface
是一个比抽象类还要抽象的类，因为其内部的所有方法全部都是抽象方法。
同时接口和类的关系也不再是继承(extends)而是实现(implements)


接口的特点：
- **只能有**抽象方法
- 只能有**常量**，默认使用`public static final`修饰
- 方法默认就被`public abstract`修饰,并且也只能被`public abstract`修饰
- 一样的，接口也不能实例化
- 类和接口的关系不再是extends，而是implements，并且也要实现它所有的方法(@Override)

```
public class InterfaceDemo {
    public static void main(String[] args) {
		Cat c = new Cat();
		c.eat(); // 猫吃鱼
		System.out.println(Animal.num); // 3 可以直接访问接口内部的常量
	}
}

interface Animal { // 声明接口和声明类很相似
	public static final int num = 3; // 接口内部只支持使用常量，并且默认使用public static final来修饰  当然可以直接写出int num = 3;
	public abstract void eat(); // 内部的方法默认使用public abstract修饰，同时也只允许使用public abstract修饰，也可以直接写成void eat();
}

class Cat implements Animal { // 类和接口的关系是implements
	@Override
	public void eat() { // 必须要重写接口中的所有方法
		System.out.println("猫吃鱼");
	}
}
```

###梳理一下接口和类之间的各种关系：
- 类和类： `extends`, 单一继承，多层继承
- 类和接口： `implements`, 多实现(可以实现多个接口)
- 接口和接口： `extends`，接口之间不可以实现只可以继承，同时也是单一继承，多层继承


###接口的优点:
- 一个类可以实现多个接口，解决了extends的单一性问题
- 接口的所有成员都是public，可以对外提供一套规范
- 降低了程序的耦合性


###接口和抽象类的比较：
- 相似点：
    - 都是不断的抽取出抽象的概念
- 不同点：
    - 抽象类也是类，只能单一继承，而类可以实现多个接口
    - 成员方面，抽象类可以有常量和成员变量，但是接口只能有变量。抽象类还可以有非抽象方法，接口必须都是抽象方法并且修饰符默认为`public abstract`
    - 构造方法方面，抽象类是有构造函数的，但是接口没有构造函数

# 匿名对象

没有变量引用的对象

```
public class AnooymousObjDemo {
    public static void main(String[] args) {
		new Test("DeeJay").show(); // name: DeeJay
	}
}

class Test {
	String name;
	public Test(String name) {
		this.name = name;
	}
	public void show() {
		System.out.println("name: "+ name);
	}
}
```


一般当方法只调用一次的时候可以使用匿名对象。

## final关键字
修饰符，可以修饰类，成员方法，以及成员变量

- final修饰的类不可以被继承。
- final修饰的成员方法不可以被重写
-  final修饰的成员变量不可以被赋值，哪怕赋的是原值。即是常量

另外自定义常量必须初始化，可以直接赋值，或者在构造方法中初始化都可。

```

public class FinalDemo {
	public static void main(String[] args) {
		Animal3 a3 = new Animal3();
		// a3.num = 10; // the final fields cannot be assigned  final修饰的成员变量不可以被赋值，哪怕赋的是原值。即是常量,一般命名使用大写。
	}
}

final class Animal {
	public void eat(){
		System.out.println("eating");
	}
}

// class Dog extends Animal {} // cannot subclass the final class   final修饰的类不可以被继承

class Animal2 {
	final public void eat(){ // final修饰成员方法
		System.out.println("eating");
	}
}
class Dog2 extends Animal2 {
	// public void eat() {} // cannot Override the final method  final修饰的成员方法不可以被重写
}

class Animal3 {
	final int num = 3;
}
```

# 多态

###多态的前提：
- 子父类的继承关系
- 方法的重写
- 父类引用指向子类对象 ` Dad d = new Child();`

###多态的成员特点：
- 成员变量：成员变量在继承时没有重写的概念，也没有动态绑定的概念，所以运行时指向的是父类的成员变量。
- 成员方法： 成员方法运行时，指向的是子类中被重写的方法
- 静态方法： 调用的静态方法是父类的静态方法，因为static是跟着类型走的。

```

public class PolymorphismDemo {
    public static void main(String[] args) {
		Dad d = new Child();
		System.out.println(d.num); // 20 成员变量在继承时没有重写的概念，也没有动态绑定的概念。 所以这边的是父类的成员变量。
		d.show(); // child 成员方法运行时，指向的是子类中被重写的方法
		d.show2(); // dad static  d变量类型还是Dad型，所以调用的静态方法是Dad类的静态方法
	}
}

class Dad {
	int num = 20;
	public void show() {
		System.out.println("dad");
	}
	public static void show2() {
		System.out.println("dad static");
	}
}
class Child extends Dad {
	int num = 10;
	public void show() {
		System.out.println("child");
	}
	public static void show2() {
		System.out.println("child static");
	}
}
```

###多态中的类型转换: 
```
public class PolymorphismDemo {
    public static void main(String[] args) {
		//向下转型
		Dad2 d2 = new Child2();
		Child2 c2 = (Child2)d2; // 强制的进行了类型转换  从Dad2类型到了Child2类型。 向下转型
		c2.show(); // child 向下转型了之后就可以调用子类的方法
	}
}

// 关于多态中引用类型的转换  向上转换Dad d = new Child(); 已经隐式的进行了转换，从小到大向上转换即从子类型到父类型
// 对于向下转换即从父类转到子类，一般是由于想访问子类中特有的成员才执行的，需要强制进行类型转换
class Dad2 {}
class Child2 extends Dad2{
	public void show() {
		System.out.println("child");
	}
}
```

### 多态的优缺点：
缺点：
- 无法直接访问子类特有的成员，如果非要访问，需要向下转型
优点：
- 提高可维护性和可扩展性

对于提高可扩展性，来写一个例子：
```
package com.polymorphism;

public class PolymorphismDemo {
	public static void main(String[] args) {
		Factory f = new Factory();
		f.createPhone(new MiPhone());
		f.createPhone(new MeiZuPhone());
	}
}

class Factory {
	public void createPhone(MiPhone mp) { 
		System.out.println("create phone");
		mp.call();
	}
	
	public void createPhone(MeiZuPhone mzp) { // 随着手机类越来越多    Factory类中要一直新增createPhone的重载方法
		System.out.println("create phone");
		mzp.call();
	}
}

class MiPhone {
	public void call() {
		System.out.println("MI phone is calling");
	}
}

class MeiZuPhone {
	public void call () {
		System.out.println("MeiZuPhone is calling");
	}
}
```

上面这个例子中，Factory类中的方法随着手机类的增多，被迫要一次次的新增一个重载的方法。

我们可以使用多态来改写这个例子:
```
package com.polymorphism;

public class PolymorphismDemo2 {
	public static void main(String[] args) {
		Factory2 f2 = new Factory2();
		f2.createPhone(new MiPhone2());
		f2.createPhone(new MeiZuPhone2());
	}
}

class Factory2 {
	public void createPhone(Phone p) {  // 不需要每新增一个手机类就实现一次重载 
		System.out.println("create phone");
		p.call();
	}
}

interface Phone { // 创建一个公共的接口  让其他的手机类来实现这个接口   在Factory2的方法中就可以只传人实现了这个Phone 类型的变量
	public abstract void call(); 
}

class MiPhone2 implements Phone { // 实现公共接口   具体的方法在类内部进行定义
	public void call() {
		System.out.println("MI phone is calling");
	}
}

class MeiZuPhone2 implements Phone { // 实现公共接口   具体的方法在类内部进行定义
	public void call () {
		System.out.println("MeiZuPhone is calling");
	}
}
```

在改进的例子中，我们写了一个公共的接口Phone，所有的手机类都来实现这个接口，最后在Factory2中的方法中，我们只需要传入`Phone p`的参数就可以了，**不用关心它具体是什么类型**，从而就避免了一直重载方法的尴尬。
