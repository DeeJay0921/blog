---
title: Python中的面向对象
date: 2018/06/09 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中的面向对象
<!--more-->

#　Python中的面向对象

### 面向过程和面向对象的区别

- 面向过程：根据业务逻辑从上到下写代码
- 面向对象：将数据和函数绑定到一起，进行封装，这样能够更快速的进行开发程序，减少重复代码的书写

### 类和对象的关系

在面向对象的概念中，对象是核心，那么为了将一堆具有相同特征和行为的对象进行抽象定义，就提出了一个概念叫**类**

举个例子：类就相当于小时候组装赛车的图纸，组装出来的赛车就是对象。

### 类的组成

类名，类的属性(数据)，类的方法(对数据操作的行为)

举个例子：
```
class Person:
    def sayHi(self):
        print('hello')
    
    def introduce(self):
        print('this is %s speaking'%self.name)

DeeJay = Person()

DeeJay.name = 'DeeJay'

DeeJay.sayHi() # hello
DeeJay.introduce() # this is DeeJay speaking
```

上面定义了一个Person类，DeeJay是这个类的一个对象。

`DeeJay = Person()`这一句就是创建了一个Person类的对象，其中`Person()`就是在内存中创建了一个对象，同时这个语句的返回值是这个对象的**引用**，将其赋给了DeeJay这个变量。

`DeeJay.name = 'DeeJay'`这一句就是给DeeJay这个对象增添一个属性name。

而`sayHi()`和`introduce()`都是Person类定义时候的方法。

值得一提的是上面方法定义时的参数`self`,类比一下JS中构造函数内部的`this`，很好理解,当然可以不写成self,但是必须保证要有一个形参

另外写一个JS中构造函数创建示例的例子，来进行对比理解：
```
function Person(name) {
    this.name = name
}
Person.prototype.sayHi = function() {
    console.log('hello')
}
Person.prototype.introduce = function() {
    console.log(`this is ${this.name} speaking`)
}

let DeeJay = new Person('DeeJay')
DeeJay.introduce()
```

### `__init__(self)`方法


#####　简介
```
class Person:
    
    def __init__(self):
        pass

    def introduce(self):
        print('this is %s speaking'%self.name)

DeeJay = Person()
```

介绍`__init__()`就得了解对象创建的流程，在上述例子`DeeJay = Person()`这一句创建对象的语句中，有如下流程：
1. 在内存中创建了一个对象
2. Python自动调用`__init__(self)`方法,此时传入的这个self就是创建的那个对象的**引用**
3. 执行`__init__()`方法中的一些默认属性的设定
4. 返回创建好的对象的**引用**,(前4步就是 `Person()`这个语句做的事)
5. 将引用赋值给DeeJay

#####  `__init__()`方法的具体使用举例：

```
class Person:
    
    def __init__(self,name,age):
        self.name = name
        self.age = age

    def introduce(self):
        print('this is %s speaking'%self.name)

DeeJay = Person('DeeJay',21)

DeeJay.introduce() # this is DeeJay speaking
```

上述例子中，`Person('DeeJay',21)`这个语句，在执行到调用__init__()的时候，self指向的是创建的这个对象，__init__()方法还可以接受除了self以外的参数，这些参数可以给创建的这个对象进行属性的初始化。

这么一写，就不需要创建好对象之后再进行属性的添加了。


### `__str__()`方法

上面的例子中，如果创建了DeeJay对象之后，使用`print(DeeJay)`来输出这个对象，得到的是：`<__main__.Person object at 0x7f39a0ca5a58>`

但是如果我们定义了`__str__()`方法之后，再进行print对象的时候，就会调用这个方法：

```
class Person:
    
    def __init__(self,name,age):
        self.name = name
        self.age = age

    def __str__(self):
        return "该对象的name为：%s,age为：%d"%(self.name,self.age)

    def introduce(self):
        print('this is %s speaking'%self.name)

DeeJay = Person('DeeJay',21)

Yang = Person('Yang',22)

# print(DeeJay) # <__main__.Person object at 0x7f39a0ca5a58>
print(DeeJay) # 该对象的name为：DeeJay,age为：21
print(Yang) # 该对象的name为：Yang,age为：22

```

值得注意的是，`__str__()`中是输出return的值。


### 私有属性

如果有一个对象，需要对其属性进行修改时，一般有2种方法：
- 对象.属性 = 数据 # 直接修改
- 对象.方法()  # 间接修改

一般为了保护对象的属性，即不能随意修改，不建议直接给对象属性直接赋值，一般我们可以采用定义一个方法，来进行给对象添加属性。

```
class Dog():
    def get_age(self):
        return self.age

wangwang = Dog()
wangwang.age = 10 # 一般不这么写  而是通过定义一个方法来进行添加属性

print(wangwang.get_age())
```

所以可以写成：
```
class Dog():
    def __init__(self):
        self.__age = 0 # 定义了一个私有属性 __age

    def set_age(self,new_age):
        if(new_age > 0):
            self.__age = new_age
        else:
            self.__age = 0 # 可以在添加属性的时候，同时处理一些异常值的情况

    def get_age(self):
        return self.__age

wangwang = Dog()
wangwang.set_age(10)

print(wangwang.get_age()) # 10

wangwang.set_age(-10)
print(wangwang.get_age()) # 0
```

这样就通过方法来设置这个私有属性。注意私有属性前要加`__`,比如说上述例子中的`__age`,这样通过`wangwang.__age`就访问不到这个

### 私有方法

先来看一个例子：
```
class Dog:
    def print1(self):
        print('---1----')

    def print2(self):
        print('----2---')


dog = Dog()

dog.print1() # ---1----
dog.print2() # ----2---
```

Dog这个类创建出来的dog对象，可以使用定义的`print1()`和`print2()`这两个方法。

现在稍作修改,将`print1()`改为`__print1()`
```
class Dog:
    def __print1(self):
        print('---1----')

    def print2(self):
        print('----2---')


dog = Dog()

dog.print2() # ----2---
dog.__print1() # 报错了 AttributeError: 'Dog' object has no attribute '__print1'

```

那么`__print1()`就是一个私有方法，不允许直接让对象调用，只能在其他方法内部进行调用。

举个实际的例子来说明：

```
class Msg():
    def __init__(self,money):
        self.money = money

    def __sendMsg(self):
        print("发送短信")
        self.money -= 10

    def checkMoneyAndSendMsg(self):
        if(self.money > 10):
            self.__sendMsg() # 调用私有方法 
        else:
            print('余额不够')

message1 = Msg(100)
message1.checkMoneyAndSendMsg() # 发送短信

message2 = Msg(0)
message2.checkMoneyAndSendMsg() # 余额不够
```

这个Msg类定义了一个私有方法`__sendMsg()`用来发送短信，但是得先判断money属性是否足够，所有不允许直接使用对象调用，而是给个公有方法，判断之后在该方法里进行`self.__sendMsg()`来进行调用


###　`__del__()`

创建对象后,Python默认调用`__init__()`

删除对象时，则会调用`__del__()`

注意这个删除对象，值得是当前指向这个对象的所有引用都消失的时候，程序会自动调用`__del__()`

来看例子：

```
class Test():
    def __del__(self):
        print('I\'m dead!')

t1 = Test()
del t1 # I'm dead!
```

注意`del t1`仅仅是将t1对创建的那个对象的引用删除了，如果t1指向的对象同时还在其他地方被引用，那这个对象就没被删除,`__del__()`不会触发。

可以使用`sys`模块的`sys.getrefcount()`方法来判断当前目标有几个引用。

```
class T:
    pass

t1 = T()

import sys

print(sys.getrefcount(t1)) # 2

# 注意这边输出的是2  因为调用sys.getrefcount(t1) 的时候 将t1也传入了一次
# 所以输出的引用数为 2 

t2 = t1 # 现在将t1的引用复制一份给t2

print(sys.getrefcount(t1)) # 3

del t2

print(sys.getrefcount(t1)) # 2

del t1

print(sys.getrefcount(t1)) # 报错了  NameError: name 't1' is not defined

# 所以这个方法返回值最小就为2
```

## 继承

先来看一下最简单的一个继承：

```
class Animal():
    def eat(self):
        print('eating...')


class Dog(Animal): # Dog类继承Animal类
    def bark(self):
        print('wang wang')



wangcai = Dog()
wangcai.eat() # eating...
```

`class Dog(Animal):` 就实现了继承

子类可以继承父类(以及父类的父类等)的属性和方法

## 重写

子类可以在内部重新定义继承来的方法 称为重写

```
class Dog:
    def bark(self):
        print('汪汪汪')

class Husky(Dog):
    def bark(self):
        print('哈士奇在汪汪汪')


h = Husky()
h.bark() # 哈士奇在汪汪汪
```

这样就实现了重写，但是会有这样一个需求，子类在重写的方法中还想调用父类中被重写的方法，这时候有2种方法：

第一种：

```
class Dog:
    def bark(self):
        print('汪汪汪')

class Husky(Dog):
    def bark(self):
        print('哈士奇在汪汪汪')
        # 第一种方式
        Dog.bark(self)


h = Husky()
# 想在h.bark()中调用Dog类中被重写的bark()
h.bark() # 哈士奇在汪汪汪 汪汪汪
```
通过在子类的`bark()`中写上`Dog.bark(self)`即可调用，注意self不可省略

第二种，使用`super()`

```
class Dog:
    def bark(self):
        print('汪汪汪')

class Husky(Dog):
    def bark(self):
        print('哈士奇在汪汪汪')
        # 第二种方式
        super().bark()


h = Husky()
# 想在h.bark()中调用Dog类中被重写的bark()
h.bark() # 哈士奇在汪汪汪 汪汪汪
```

直接写`super().bark()`即可


### 私有属性和私有变量的继承机制

来介绍一下私有属性和方法的继承
```
class A:
    def __init__(self):
        self.__privateProperty = 'private'
        self.publicProperty = 'public'

    def __privateFn(self):
        print('privateFn')
    
    def publicFn(self):
        print('publicFn')

class B(A):
    pass

b = B()

b.publicFn() # publicFn
print(b.publicProperty) # public

b.__privateFn() # 报错 AttributeError: 'B' object has no attribute '__privateFn'
print(b.__privateProperty) # 报错 AttributeError: 'B' object has no attribute '__privateProperty'

```

通过例子我们可以看出，**私有属性和私有方法，是不可以直接被继承的**

但是也有例外，我们如果定义一个共有方法，在其中使用私有方法和属性的话，这个是允许的。

```
class A:
    def __init__(self):
        self.__privateProperty = 'private'
        self.publicProperty = 'public'

    def __privateFn(self):
        print('privateFn')
    
    def publicFn(self):
        print('publicFn')

    def anotherPublicFn(self): #定义一个共有方法来使用私有变量
        print(self.__privateProperty)
        self.__privateFn()

class B(A):
    pass

b = B()
b.anotherPublicFn() # private privateFn
```

如上述例子所示。

###  多继承

多继承指的是一个类同时继承多个类，举个例子，骡子继承驴和马

```
class Horse:
    def horseSay(self):
        print('我是马')

class Donkey:
    def DonkeySay(self):
        print('我是驴')

class Mule(Horse,Donkey): # 实现了多继承
    pass

mule = Mule()
mule.horseSay() # 我是马
mule.DonkeySay() # 我是驴
```

上述例子，Mule类就实现了对Horse类和Donkey类的多继承。

### 多继承中的注意点  `__mro__`

```
class Base(object):
    def test(self):
        print('Base')

class A(Base):
    def test(self):
        print('A')

class B(Base):
    def test(self):
        print('B')

class C(A,B):
    def test(self):
        print('C')

c = C()
c.test()
```

现在有如上例子，C类是对A和B多继承，A和B都继承自Base类，其中他们都有一个test方法，现在如果调用c.test()，到底输出什么？

我们可以输出`C.__mro__`,结果为：
```
(<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>, <class '__main__.Base'>, <class 'object'>)

```

输出了一个元祖，同时表明了去寻找方法的路径，即由C->A->B->Base->object依次查找，如果找不到就报错说没有该方法。

所以我们知道上面例子会输出啥，先查找到C中的test,如果C中没定义test(),就去A里面找,以此类推。

`__mro__`就是调用一个方法的搜索顺序


### 多态

多态：定义时的类型和运行时的类型不一样，此时就成为多态

```
class Dog(object):
    def print_self(self):
        print('汪汪汪')

class Husky(Dog):
    def print_self(self):
        print('我是哈士奇')

def introduce(obj): #定义一个函数
    obj.print_self() # 调用传入的obj的print_self()

dog = Dog()
husky = Husky()

introduce(dog) # 汪汪汪
introduce(husky) # 我是哈士奇
```

上述例子就是一个多态，`introduce()`方法定义时只知道要调用一个对象的方法，但是至于这个方法是调用父类还是子类，要等到开始运行的时候才能确定。

### 类属性  实例属性

先来搞清楚什么叫`类对象`，什么叫`实例对象`
我们在Python中定义了一个类之后，这个类也是一个对象，被称为类对象，
而通过类创建的对象，为了和类对象区分，称为示例对象。

类属性就是`类对象`所拥有的属性，它被`类对象`的`实例对象`所共有，在内存中只存在**一个副本**。


下面的例子就是 `类属性`：
```
class People(object):
    name = 'DeeJay' # 共有的 类属性
    __age = 21 # 私有的 类属性

p = People()

print(p.name) # DeeJay
print(People.name) # DeeJay

print(p.__age) # AttributeError: 'People' object has no attribute '__age'
print(People.__age) # AttributeError: 'People' object has no attribute '__age'

```

上述例子中，在定义类的时候，直接给类定义了属性，那么这些属性就是`类属性`

那`实例对象`中的属性就叫`实例属性`,来看例子：

```
class Person(object):
    def __init__(self,name):
        self.name = name

p = Person('DeeJay')
print(p.name) # DeeJay
```
上述例子中，并没有在Person类中直接定义属性，而是在创建实例对象的时候，给实例对象内部创建了属性，所以被称为实例属性。

- `实例对象`和`类对象`的区别

那么对于`实例属性`来讲，它只和具体的实例有关系，并且一个实例对象和另一个实例对象之间是**不共享**的;

相应的对于`类属性`来说，`类属性`所属于类对象，并且多个实例对象之间，共享同一个`类属性`，另外前面已经提到过，`类属性`只会定义一次。

对于`类属性`来讲，一个类创建的所有对象中，都**共享**这个类中的`类属性`：
```
class Person(object):
    name = 'DeeJay'


p1 = Person()
p2 = Person()


print(p1.name) # DeeJay
Person.name = 'Yang' # 修改了类属性之后  由于多个实例之间共享 所以所有的实例都改变了
print(p1.name) # Yang
print(p2.name) # Yang
```

### 类方法  实例方法  静态方法

先来回顾一下类属性和实例属性以及实例方法：

```
class Game(object):
    num = 0 # Game类的类属性

    def __init__(self,name): # 这个方法  称为实例方法
        self.name = name # name为创建的实例的属性

rainbowSix = Game('rainbowSix Siege')
```

上述例子中,num为Game的类属性，`__init__(self)`是rainbowSix的`实例方法`，而name是rainbowSix的`实例属性`。

那么 如果想给Game类增加一个`类方法`，可以这么写：

```
class Game(object):
    num = 0 # Game类的类属性

    def __init__(self,name): # 这个方法  称为实例方法
        self.name = name # name为创建的实例的属性

    def printName(self): # printName 也是实例方法
        print(self.name)
    
    @classmethod # 类方法定义使用装饰器
    def get_Game_num(cls): # 形参不写self写cls表示传入的是class的引用
        return cls.num

rainbowSix = Game('rainBowSix Siege')
rainbowSix.printName() # rainBowSix Siege  实例对象调用实例方法
print(Game.get_Game_num()) # 0  类对象调用类方法 

# 值得注意的是，想调用类方法，实例对象也是可以的

print(rainbowSix.get_Game_num()) # 0
```
通过一个装饰器`@classmethod`来定义一个类方法

上述例子展示了怎么定义即调用一个类方法，再强调一下，调用类方法有2种方法:
- 使用类对象直接调用
- 通过该类创建的实例对象也可以调用类方法

其中不管怎么调用，`cls`形参指向的还是这个Game类。

接下来再来看静态方法，同样定义也是需要一个`@staticmethod`装饰器来定义一个静态方法。

不同的是，实例方法接收一个`self`表示实例对象，类方法接收一个`cls`参数表示接收一个类对象，而静态方法**可以**不接收参数(当然也可以接收自定义的一些参数)。

静态方法一般只完成一些基本的功能，和类以及实例都没什么太大的关系

```
class Game(object):
    num = 0 # Game类的类属性

    def __init__(self,name): # 这个方法  称为实例方法
        self.name = name # name为创建的实例的属性

    def printName(self): # printName 也是实例方法
        print(self.name)
    
    @classmethod # 定义类方法
    def get_Game_num(cls): # 形参不写self写cls表示传入的是class的引用
        return cls.num
    
    @staticmethod # 定义静态方法
    def introClass(): # 可以不接受任何参数
        print('这是一个关于游戏的class')


rainbowSix = Game('rainBowSix Siege')
# 关于静态方法的调用  同样也是2种方式 通过类或者是实例对象
rainbowSix.introClass() # 实例对象调用
Game.introClass() # 类对象调用
```

# 简单工厂模式

```
# 使用class中的方法来解耦
class factory:
    def method(self):
        pass

```

# 工厂方法模式

```
# 在基类中定义一些方法 但是不实现 留到子类中重写方法来实现功能
class Store(object:
    def select_car(self):
        pass

    def order(self):
        self.select_car()

class carStore(Store):
    def select_car(self): # 在子类中重写方法来实现功能
```



