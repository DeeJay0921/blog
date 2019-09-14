---
title: Java中的面向对象
date: 2019/07/14 00:00:01
tags: 
- Java
- 面向对象
categories: 
- Java
---
Java中的面向对象
<!--more-->

一些好用的插件:
> key Promoter X
> builder generator

# 对象基本概念
- 对象就是数据和行为的集合（主观能动性）
## 方法的重载(overload)
- 重载(overload)和重写/覆盖(override)的区别
overload是相同方法名，不同参数，而override指的是子类重写父类的相同方法
- 如何区分同名的不同重载方法
  - 根据传参类型
  - 隐式转换？
  - 如果能匹配到多个的情况下，会遵循最匹配的原则，如果传入的为null需要强制类型转换。
最匹配的原则举例：在下述例子中，正常情况下直接调用`print(1)`的时候会去找最匹配的`print(int i)`方法执行
```
public class Main {
    public static void main(String[] args) {
        // 强制令其选中方法print(Number i)
        print((Number) 1);
    }

    public static void print(int i) {
        System.out.println("I'm int!");
    }

    public static void print(Integer i) {
        System.out.println("I'm Integer!");
    }

    public static void print(Number i) {
        System.out.println("I'm Number!");
    }

    public static void print(Object i) {
        System.out.println("I'm Object!");
    }
}
```

对于传入null的时候，如果匹配到多个方法的情况时，需要强制指定类型。
```
import java.util.HashMap;

public class Main {
    public static void main(String[] args) { 
        // print(null); // 在这直接调用会报错 因为null的类型不明，所以需要强制指定类型
        // 例如强制要调用HashMap的方法
        print((HashMap) null);
    }

    public static void print(int i) {
        System.out.println("I'm int!");
    }

    public static void print(Integer i) {
        System.out.println("I'm Integer!");
    }

    public static void print(Number i) {
        System.out.println("I'm Number!");
    }

    public static void print(Object i) {
        System.out.println("I'm Object!");
    }

    public static void print(HashMap i) {
        System.out.println("I'm HashMap!");
    }
}
```
- java里面无法做到给参数赋上默认值，但是利用重载可以实现这一功能。
> 另外值得一提的是，重载**不支持**只修改返回值。
## 对象的初始化顺序：
> 基本的原则是：static方法=> static块 => 初始化成员变量 => 非static块 => constructor

详见以下例子：
```
public class Cat {
    public static int count = initStaticCount();

    static {
        System.out.println(2);
    }

    int age = initAge();
    String name;

    {
        System.out.println(4);
    }

    {
        System.out.println(5);
    }

    public int initAge() {
        System.out.println(3);
        return 0;
    }

    public static int initStaticCount() {
        System.out.println(1);
        return 0;
    }

    public Cat(String name) {
        this(0, name);
        System.out.println(7);
    }

    public Cat(int age, String name) {
        this.age = age;
        this.name = name;
        System.out.println(6);
    }
}
```
## Builder模式
当一个类拥有了太多的成员属性的时候，如果我们直接使用`new ClassName(member1, member2, member3....，memberN)`创建新的对象的时候，容易造成成员变量的错位或者丢失，如果没有IDE帮助的话也很难阅读，Builder模式就是为了解决这种情况。

来看具体的使用，
首先有如下的User类：
```
public class User {
    /** 用户的名 */
    private final String firstName;

    /** 用户的姓 */
    private final String lastName;

    /** 用户的电话 */
    private final String phoneNumber;

    /** 用户的地址 */
    private final String address;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    User(String firstName, String lastName, String phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
```
我们可以建立一个Builder去清晰的初始化User的每一个属性：
```
public class UserBuilder {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    
    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    public UserBuilder phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public UserBuilder address(String address) {
        this.address = address;
        return this;
    }
    
    public User build() {
        return new User(firstName, lastName, phoneNumber, address);
    }
}
```
这样我们可以通过如下方式获取一个User对象:
```
 User user = new UserBuilder()
                        .firstName("德华")
                        .lastName("刘")
                        .phoneNumber("我也不知道")
                        .address("可能在地球上吧")
                        .build();
```
而不是只能`new User("德华", "刘", "我也不知道", "可能在地球上吧");`这样去获取新的对象。

## 静态工厂方法模式
静态工厂模式的核心思想有2点
1. 使用静态工厂方法代替构造器
2. 将常规的构造器**私有化**（这一点很重要）

使用工厂模式优点有：
1. 工厂方法可以自行命名，可读性良好，不像构造器的命名并没有什么实质性意义一样
2. 工厂方法内部处理非常灵活，可以有自己的一些判空逻辑等等。

来看一个使用例子：
```
public class Cat {
    private static final Cat INVALID_CAT = new Cat("Invalid cat", -1);
    private String name;
    private int age;
    
    // 工厂方法还可以自主命名 提高可读性
    public static Cat newCat(String name, int age) {
        if(age < 0 || name == null || "".equals(name)) {
            // 工厂方法可以在内部很灵活的做一些判空处理等等 本例中如果age小于0或者name是空字符串或者null时 返回预先创建好的INVALID_CAT
            return INVALID_CAT;
        }else {
            return new Cat(name, age);
        }
    }
    
    // 构造器要私有化
    private Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
```
这样外部直接调用`Cat.newCat(name, age)`就可以创建新的对象。

> 在上述例子中，有个地方需要注意下，如果要判断name是不是""，如果我们写出`name.equals("")`当name为null会出异常，所以可以写成`"".equals(name)` 这样就避免了出现异常

# 组合和继承

## 什么是继承
- 继承的本质就是提取出公用代码，避免重复（DRY原则）

## Java的继承体系
> 在Java中的所有对象，其实都隐式的继承了Object类
- 单根继承
    - 单根继承的好处
    因为所有对象都是Object类的子类，所以可以保证**所有的对象都有相同的行为，可以方便处理**
    >且Java是单一继承，一个类最多只能继承一个类

    - 对于C/C ++的多重继承
    其他非Java语言可以允许继承多个类，存在的问题是对于继承的类中存在相同方法时的处理。

- Object类中有哪些需要了解的方法

    - equals()
    一般重写equals到时候也要重写hashCode()

    - toString()
    输出一个对象的时候会默认隐式的调用同toString()




## 类的结构和初始化顺序

- 子类拥有父类的一切数据和行为
- 父类先于子类
  一定是从最开始的基类开始初始化创建一直到目标类
- 必须拥有匹配的构造器
    - `super`关键字

## 实例方法的覆盖（Override）

- 覆盖/重写 要使用@Override注解 来进行检查

## 向上/向下转型

- 一个子类类型的对象永远是一个父类类型的对象
  - 一只猫同时也是一个动物，同时也是一个对象（Object）
  - `instanceof`判断类型
  instaceof右边一定是一个类
  - null instanceof ?
  null instanceof XXX**永远返回**false

- 因此，当需要一个父类型的时候，总是可以传递一个子类型
- 也有些情况需要强制进行转型
    - 有些情况需要从父类转为子类，和其他强制类型转换是一样的。但是可能会发生转型失败的情况。

## final关键字
- final声明变量，变量为**不可变的**（必须初始化）
    - 局部变量/方法参数
    - 成员变量
    - 常量与单例
    常量的命名约定是**全大写用下划线分割**，如`final int MAX_VALUE = 100;`
> final声明的变量只能被赋值一次，一旦被赋值就不可以再被改写，可以保证该变量是线程安全的。

> 对于final声明对象的情况，也是一样的，不代表对象的内容不可以多次改写，仅仅代表着对象的地址不可变，即该地址只能指向目标对象。
- final在方法上的声明：
**禁止继承/重写此方法**，同时这个方法运行的时候是确定的，没有多态。
- final在类上的声明：
**禁止此类被别的类继承**
> 继承提供了灵活性，但是也埋下了隐患，String/Integer等类是final的原因就在于如果允许继承，那么可能会有其他类破坏这些类本身内部的一些约定。

# 多态

## 什么是多态
- 实例方法**默认就是多态**的
    - 在运行时根据this来决定调用哪个方法
    - 静态方法是**没有多态**的
    - 参数静态绑定，接受者动态绑定(Baseparam的例子)
        - 即多态只选择接收者的类型，而不会去选择参数的类型

- 例子： 
    - 例子1：Shape的多态
  假设现在有如下情况：
```
public class Shape {
    // 返回当前"形状"(Shape)的面积
    public double getArea() {
        return 0d;
    }
}
```
```
public class Square extends Shape{
    // 正方形的边长1
    private double sideLength;

    public Square(double sideLength) {
        this.sideLength = sideLength;
    }

    @Override
    public double getArea() {
        return sideLength * sideLength;
    }
}
```
```
public class Rectangle extends Shape{
    // 长方形的长
    private double a;
    // 长方形的宽
    private double b;

    public Rectangle(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public double getArea() {
        return a * b;
    }
}
```
有如上Shape父类，其2个子类分别Overide了父类的方法，在进行调用的时候：`new Square(1.0).getArea()`和`new Rectangle(1.0, 2.0).getArea()`时分别调用的是子类的方法，即**运行时根据this来决定调用哪个方法**

 - 例子2：HashSet的`addAll()`
    对于HashSet,其内部并没有override addAll()，其中的addAll()是继承自AbstractCollection，所以调用addAll()时会调用AbstractCollection中的addAll()，
但是AbstractCollection中的addAll()中调用了一下add()方法，而HashSet是自己override了add()的，所以依靠原则，实例方法都是多态的，以及运行时是根据this来判断调用哪个方法的，可以得到调用的是Hashset中的add()而不是AbstractCollection中的add()

## 多态的应用：设计模式之策略模式
来看策略模式的一个例子，假设现在有一个打折业务，需要判断用户是哪种折扣从而计算价格:
我们有3种情况，
    // NoDiscountStrategy 不打折
    // Discount95Strategy 全场95折
    // OnlyVipDiscountStrategy 只有VIP打95折，其他人保持原价
如果不使用策略模式，我们的代码实现大概像这样:
```
public class PriceCalculator {
    public static int calculatePrice(String discountStrategy, int price, User user) {
        switch (discountStrategy) {
            case "NoDiscount":
                return price;
            case "Discount95":
                return (int) (price * 0.95);
            case "OnlyVip":
                {
                    if (user.isVip()) {
                        return (int) (price * 0.95);
                    } else {
                        return price;
                    }
                }
            default:
                throw new IllegalStateException("Should not be here!");
        }
    }
}
```
这样的实现以后每次要加一个策略就要改一次PriceCalculator这个计算类，如果这个类已经发布，那改动代价就太大了，所以可以使用策略模式进行优化：
优化的核心思路是创建一个父级策略类，然后所有的折扣情况都是这个类的子类，调用的时候通过多态判断到底是调用哪个具体的子类折扣类。
顶级策略父类：
```
public abstract class DiscountStrategy {
//    顶级策略父类 强制要求子类实现discount方法去计算折扣
    abstract int discount(int price, User user);
}
```
95折策略子类实现举例：
```
public class Discount95Strategy extends DiscountStrategy{
    @Override
    int discount(int price, User user) {
        return (int) (price * 0.95);
    }
}
```
此时PriceCalculator里的实现就可以改为：
```
public class PriceCalculator {
     public static int calculatePrice(DiscountStrategy strategy, int price, User user) {
//         现在只需要直接调用strategy.discount(price, user);即可，就算之后有其他策略，新建一个DiscountStrategy的子类即可，无需修改此处代码
         return strategy.discount(price, user);
     }
}
```
现在只需要直接调用strategy.discount(price, user);即可，就算之后有其他策略，新建一个DiscountStrategy的子类即可，无需修改此处代码

# 抽象类和接口

## 抽象类
- 不可实例化
- 可以包含抽象方法 - 非private/static
- 可以包含成员变量
- 可以包含普通类的任何东西

> 抽象类和其他类没什么区别，仅仅是可以声明抽象方法，迫使其子类去强制override该方法。

## 接口
- 接口部分的实现了多继承
- 接口不是类
- 接口的扩展
- 接口只代表一种功能
- 一个类只能继承一个类，但是却能实现若干个接口

> 接口中的成员 `int a = 1;`等价于`public static final int a = 1;`即为常量，所以要遵循命名常量命名规则`int A  
 = 1;`
方法`void f();`则等价于`public void f();`

### 接口可以包含什么？
-
 若干个方法(默认为public)
- 若干个常量(默认为public static final)
- extends 另一个接口
- 默认方法（java 8 之后）
> 接口的特性决定了只要一个接口发布后，就**不可以再进行修改**了，否则所有实现该接口的类都会报错。
>这就抛出了一个问题，之前已经发布过的接口如果想再增加一个方法同时又不影响之前实现了这个接口的那些类正常工作的话，Java8之前是没办法做到的。Java8之后可以使用默认方法，让接口内的方法有方法体同时又不需要实现了该接口的类强制Override该方法。

基于上述的原因，Java8之后想给List接口增加一个sort()方法，就是基于这样的原理，默认方法是一种妥协的产物。

但是默认方法的出现带来了新的问题，如果InterfaceA 和InterfaceB都有default void f(){}；的实现，这2个接口被一个类同时实现了的话，其实就出现了类似于C++中的菱形继承的问题（二义性）。这时候编译会不通过，不过可以选择在类中override该方法。

## 抽象类 vs 接口
### 先看相同点：
1. 都是抽象的，都不可以实例化
2. 都可以包含抽象方法(即没有方法体，且非static/private/final的方法)
### 不同点
1. 抽象类是类，可以包含类包含的一切东西，但是接口可以包含受限的成员(public static final)变量和(public)方法
2. 抽象类只能单一继承，但是接口是可以多继承的，甚至可以继承多次。

> Java设计的思想原则就是： 为了达到最大程度的灵活性以及最大程度的代码复用性。


### Comparable接口

[Java中的Collections工具类及Comparator和Comparable的区别](https://www.jianshu.com/p/7e03dc3ea7b7)

> 关于TreeSet和Comparable的小坑：对于TreeSet而言，如果其实现的Comparable的compareTo()对于2个不同的元素返回了0的话，那么TreeSet会视为为同一个元素，加入集合的时候就会去掉其中的一个。

### 自定义一个过滤器
假设现在有如下需求，有如下User类，有一些过滤条件要过滤出符合条件的User，比如filterUsersWithEvenId，filterZhangUsers，filterWangUsers。其中代码很明显存在大量重复，可以借助多态进行优化：
```
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class User {
    /** 用户ID，数据库主键，全局唯一 */
    private final Integer id;

    /** 用户名 */
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 过滤ID为偶数的用户
    public static List<User> filterUsersWithEvenId(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.id % 2 == 0) {
                results.add(user);
            }
        }
        return results;
    }

    // 过滤姓张的用户
    public static List<User> filterZhangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("张")) {
                results.add(user);
            }
        }
        return results;
    }

    // 过滤姓王的用户
    public static List<User> filterWangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("王")) {
                results.add(user);
            }
        }
        return results;
    }

}
```
分析上面代码，三个过滤方法中不同的地方仅仅在于判断的条件不同，我们可以使用Predicate谓词(判定)接口来进行多态的应用：
```
public class User {
    /** 用户ID，数据库主键，全局唯一 */
    private final Integer id;

    /** 用户名 */
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

        public static List<User> filterUsersWithEvenId(List<User> users) {
        return filter(users, new filterUsersWithEvenId());
    }
    static class filterUsersWithEvenId implements Predicate<User> {
        @Override
        public boolean test(User o) {
            return o.getId() % 2 == 0;
        }
    }
    public static List<User> filterZhangUsers(List<User> users) {
        return filter(users, new filterZhangUsers());
    }
    static class filterZhangUsers implements Predicate<User> {
        @Override
        public boolean test(User o) {
            return o.getName().startsWith("张");
        }
    }
    public static List<User> filterWangUsers(List<User> users) {
        return filter(users, new filterWangUsers());
    }
    static class filterWangUsers implements Predicate<User> {
        @Override
        public boolean test(User o) {
            return o.getName().startsWith("王");
        }
    }
    
    public static List<User> filter(List<User> users, Predicate<User> predicate) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (predicate.test(user)) { // 此处应用多态
                results.add(user);
            }
        }
        return results;
    }
}
```


### 内部类详解

#### 内部类
即定义在一个外围类内部的类
- 用途：实现更加精细的封装
- 可以访问外围类的实例方法
- 非静态内部类
  - 和一个外围类**实例**相绑定
   - 可以访问外围类**实例**的方法
- 静态内部类
  - **不和**外围类实例相绑定
  - **不可以**访问外围实例的方法

> 非静态内部类中，其实JDK悄悄的声明了一个外围类的实例，所以才可以访问到实例方法

使用原则： **永远使用**静态内部类，除非会报错（即要使用实例方法），因为非静态会占用额外的空间。

#### 匿名内部类
就是直接通过new的方式去创建一个匿名的继承了目标类或者接口的类。

> 匿名类没有名字，在mvn compile之后去查看字节码文件，可以看到其命名就是根据主类名加是第几个匿名类来命名的。
