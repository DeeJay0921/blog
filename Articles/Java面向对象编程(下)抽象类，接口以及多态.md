---
title: Java面向对象编程(下)抽象类，接口以及多态
date: 2019/02/09 00:00:01
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java面向对象编程(下)抽象类，接口以及多态
<!--more-->

# 抽象类
- 目的就是用于继承之后定义子类的类，必然会用于定义子类。
- 抽象类除了**不能实例化对象**之外，类的其他功能依然存在，成员变量，方法，构造方法的访问方式和普通类一样。
- 由于抽象类不能实例化对象，所以抽象类必须要被继承，才能被使用。
- 还可以使用`abstract`修饰方法，抽象方法只有声明，没有具体实现。
- 子类继承抽象类之后，对于父类的抽象方法必须要做Override，否则子类也必须为抽象类
- 另外`static`方法不可以被声明为抽象方法

来看一个抽象类的例子：
假设我们现在有一个抽象类Animal，内部有待子类去实现的方法。
```
package com.DeeJay;

public abstract class Animal {
    protected abstract void sayHi(); // 此方法即为抽象方法
//    等待Animal的子类去实现具体的方法细节

}
```
```
package com.DeeJay;

public class Cat extends Animal {
    @Override
    protected void sayHi() { // 子类中必须要去实现抽象父类中的抽象方法，不然子类也必须为抽象类
        System.out.println("meow~");
    }
}

```
```
package com.DeeJay;

public class Dog extends Animal{

    @Override
    protected void sayHi() { // 子类中必须要去实现抽象父类中的抽象方法，不然子类也必须为抽象类
        System.out.println("wangwangwang");
    }
}

```
上述例子中，Cat和Dog都继承了相同的抽象类Animal，子类中实现不同逻辑的抽象方法。

# 接口
- 接口是一系列抽象方法的集合(隐性的被修饰为public static, 写的时候不需要写出来)
- 语义上具有一定的特性，例如：
    - 行走：但是没有行走的定义
    - 显示：表示可以显示，但是也没有显示的定义
- 接口内部的成员变量，只能是static final的,因为接口**并不是类**，不能实例化对象，所以不会有状态，只会有属性。
- 类通过关键字`implements`表达实现一个接口，从而声明这个类具有接口定义的属性和方法
- 一个类可以实现多个接口

来看一个接口使用的简单例子：
假设我们现在有一个接口名为Displayable,所有实现该接口的类都要重写一个display方法来进行展示。
```
package com.DeeJay;

public interface Displayable {
    void display(); // 所有实现该接口的类都有一个display方法用于展示
}
```
然后其他支持展示的类都可以实现这个接口，根据自己内部不同的逻辑来实现具体方法
```
public class Music implements Displayable {
    @Override
    public void display() {
        System.out.println("it\'s Music");
    }
}
```
```
public class Movie implements Displayable {
    @Override
    public void display() { // 实现接口定义方法
        System.out.println("it\'s movie");
    }
}
```
使用这2个类
```
public class Main {

    public static void main(String[] args) {
        Music music = new Music();
        music.display(); // it's Music
        Movie movie = new Movie();
        movie.display(); // it's movie
    }

}
```
>在这边其实也可以定义一个抽象类，内部定义一个抽象方法display，然后让这2个类去继承这个抽象类，也能达到一样的效果，但是在语义上，肯定是实现接口要比继承抽象类来的贴切，只要实现了这个接口的类都可以展示，在逻辑和语义上都更加易于理解，也是推荐的做法。即**抽象类和接口表达的语义是不一样的**，类表示的是**某种同一类的事物**，而**接口表达是某些事物具有相同的特性**。

# 多态

- 多态表示一个类型(父类或者接口)的变量的方法的具体实现行为由变量指向的具体对象确定。
- 主要的实现方式：
    - 继承和接口
    - 父类和接口类型的变量赋值子类对象
    - 调用被Override的方法

#### 多态代码举例：
先看一个简单的例子，一个父类Animal
```
public class Animal {
    public String sayHi() {
        return "Animal";
    }
}
```
```
public class Cat extends Animal {
    @Override
    public String sayHi() {
        return "meow~";
    }
}
```
```
public class Dog extends Animal{
    @Override
    public String sayHi() {
        return "woof~";
    }
}
```
```
public class Main {

    public static void main(String[] args) {
        Cat cat = new Cat();
        sayHiFromAnimals(cat); // meow~
        Dog dog = new Dog();
        sayHiFromAnimals(dog);// woof~
    }

    public static void sayHiFromAnimals(Animal animal) { // 此处方法接收一个Animal类型的对象
        // 接收的对象为父类对象，但是调用sayHi方法时还是调用的具体子类的方法
        System.out.println(animal.sayHi());
    }
}
```
>上述例子中也可以直接写为`Animal cat = new Cat()`，向上转型达到的效果是一样的

对于接口来说也是一样的，只要实现了某个接口，就可以通过类似上述例子中进行使用。
