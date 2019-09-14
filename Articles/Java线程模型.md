---
title: Java线程模型
date: 2019/03/23 00:00:01
tags: 
- Java
- 线程
categories: 
- Java
---
Java线程模型
<!--more-->

[面试--线程](https://github.com/Homiss/Java-interview-questions/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/Java%E9%9D%A2%E8%AF%95%E9%A2%98%E4%B9%8B%E5%A4%9A%E7%BA%BF%E7%A8%8B(%E4%B8%80).md)

[知乎--java中的多线程究竟在什么情况下使用？](https://www.zhihu.com/question/65200684)

[知乎--多线程有什么用？？](https://www.zhihu.com/question/19901763)

[菜鸟教程--多线程编程](http://www.runoob.com/java/java-multithreading.html)
### Java线程的概念

Java内置支持多线程编程(multithreaded programming),多线程程序包含2条或者2条以上并发运行的部分，程序中每个这样的部分叫做**线程(thread)**,
每个线程都有独立的运行路径。因此多线程任务是多任务处理的一种特殊形式。

### 进程和线程的比较

- 进程：程序执行时所占用的所有的资源的总称或者说是容器
- 线程：是基本执行单元
![image](http://upload-images.jianshu.io/upload_images/7113407-592396d2ecdba143.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

简单总结： **进程是线程的容器。**

### 进程中的主线程

主线程的重要性体现在： 
- 他是**产生**其他子线程的线程
- 通常它必须是最后完成执行，因为它将执行各种关闭操作

主线程是运行程序时候自动创建的，但是其也是一个**Thread类的实例对象**

> 一般由主线程创建其他子线程，然后由主线程惯例其他线程。

### 关于Thread类的基本使用
Java的多线程系统建立于Thread类和Runable接口. Thread类定义了好几种方法来帮助管理线程：
方法|意义
----------------|--------------------
getName|获取线程名称
getPriority|获取线程优先级
isAlive|判定线程是否仍在执行
join|等待一个线程终止
run|线程的入口
sleep|在一定时间内挂起线程
start|通过调用运行方法来启动线程


### 继承Thread类创建新的线程

- 我们可以通过继承Thread类，Override run()方法，然后创建该类的实例来创建除了主线程之外的其他线程，并且在run()方法中赋予属于这个线程的代码逻辑，这个run()方法是新线程的入口。
- 这个新建的Thread的子类，会继承Thread类的一个start()方法，调用start()方法时，会自动生成一个线程，然后调用run()方法执行该线程的逻辑。

```
package com.DeeJay.ThreadDemo;

public class ThreadDemo extends Thread{ // 自定义线程类
//    运行的代码逻辑和主线程不同
    public ThreadDemo() {
//        创建一个新的子线程
        super("Child Thread");
//        显式的开始执行这个线程 ===> 做一些基本的初始化操作 ===> run()
        this.start();
    }

//    新的子线程的入口！
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i ++) {
                System.out.println("Child Thread: " + i);
                Thread.sleep(1000);
            }
        }catch (Exception e) {
            System.out.println("Child Thread Error!");
        }
    }
}

```
```
package com.DeeJay;

import com.DeeJay.ThreadDemo.ThreadDemo;

public class Main {
    public static void main(String[] args) {
//        创建新的子线程！
        ThreadDemo t = new ThreadDemo();
        // 需要注意的是此处代码执行并不会像单线程一样阻塞，而是会立即执行下面语句
//        调用完构造函数和start()之后， 子线程开始执行，主线程返回到Main(),开始自己的逻辑
//        主线程逻辑
        try {
            for (int i = 0; i < 5; i ++) {
                System.out.println("Main Thread: " + i);
                Thread.sleep(1000);
            }
        }catch (Exception e) {
            System.out.println("Main Thread Error!");
        }
    }
}
```
> 此处在定义新的线程类的时候，也可以不在构造函数内部直接执行start(),可以等新的线程类的实例对象创建后手动执行start()开始子线程逻辑。

### 实现Runable接口来创建新的线程

```
package com.DeeJay.ThreadDemo;

public class ThreadDemo implements Runnable{ // 改为实现Runnable接口
    private Thread t;

    public ThreadDemo(String name) {
//        选用带target的构造函数， 将this传过去，本质上就是把run()方法传递过去
        t = new Thread(this, name);
        t.start();
    }

    //    一样的  实现run() 作为线程的入口
    @Override
    public void run() {
        for (int i = 0; i < 5; i ++) {
            System.out.println("Runnable Thread: " + i);
        }
        System.out.println(t.getName() + " existing");
    }
}

```
```
package com.DeeJay;

import com.DeeJay.ThreadDemo.ThreadDemo;

public class Main {
    public static void main(String[] args) {
        new ThreadDemo("Child Thread");
    }
}
```
### 线程同步

当多个线程需要**共享资源**，他们就需要某种方法来确定资源在某一刻**仅被一个**线程占用着。

达到上述目的的过程就叫做**同步(synchronization)**

#### synchronized方法的使用

当一个线程在一个synchronized方法的内部，所有试图调用该方法（或者其他synchronized方法）的**同一个实例对象**的线程**必须等待**。为了退出管程，并释放对对象的控制权给其他等待的线程，拥有管程的线程仅需从synchronized方法返return即可。

>**一般如果一个对象的方法可能被多线程使用，则用synchronized关键字修饰**

来看一个具体的使用synchronized方法的例子：
Callme.java
```
package com.DeeJay;

public class Callme {
    public void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("]");
    }
}

```
Caller.java
```
package com.DeeJay;

public class Caller implements Runnable{
    private String msg;
    Thread t;
    Callme target;

    public Caller(String msg, Callme target) {
        this.msg = msg;
        this.t = new Thread(this);
        this.target = target;
        t.start();
    }

    @Override
    public void run() {
        target.call(this.msg);
    }
}

```
Main
```
package com.DeeJay;

public class Main {
    public static void main(String[] args) {
        Callme target = new Callme(); // 共享的资源

        Caller c1 = new Caller("hello", target);
        Caller c2 = new Caller("world", target);
        Caller c3 = new Caller("synchronized", target);

        try {
            c1.t.join();
            c2.t.join();
            c3.t.join();
        }catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n");
        System.out.println("Main Thread End!");
    }
}
```
上述例子中，主线程中new出来的target即为几个子线程公用的资源。当调用三个子线程时，三个子线程会并发执行，并不会等到一个执行完再执行下一个，所以看到的输出结果为：
```
[hello[world[synchronized]]]
```
我们想达到的效果是在一个子线程占用公用资源target时，不允许其他线程也使用该资源，为了达到这个效果，就可以给将target的这个call方法用`synchronized`修饰：
Callme.java
```
package com.DeeJay;

public class Callme {
    public synchronized void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("]");
    }
}
```
之后再运行，可以看到输出的为：
```
[hello][synchronized][world]
```
#### synchronized修饰语句块

对于上述的例子，还可以使用`synchronized`修饰代码块来达到相同的效果，synchronized修饰的代码块就是同步执行的，我们可以不用在Callme类中修改call方法为synchronized，而是在调用Callme实例对象的Caller类中，将代码块置为synchronized，这样代码块中的逻辑也达到了同步的效果。
Caller.java
```
package com.DeeJay;

public class Caller implements Runnable{
    private String msg;
    Thread t;
    Callme target;

    public Caller(String msg, Callme target) {
        this.msg = msg;
        this.t = new Thread(this);
        this.target = target;
        t.start();
    }

    @Override
    public void run() {
        synchronized (target) { // synchronized block
            target.call(this.msg);
        }
    }
}

```


#### 关于join
关于join()方法，join方法的作用是，当我们调用某个线程的这个方法时，这个方法会挂起**调用线程**，直到被调用线程结束执行，调用线程才会继续执行。

关键是理解**挂起调用线程**，首先举个例子：
子线程：
```
package com.DeeJay.ThreadDemo;

public class ThreadDemo extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Child Thread End!");
    }
}
```
Main:
```
package com.DeeJay;

import com.DeeJay.ThreadDemo.ThreadDemo;

public class Main {
    public static void main(String[] args) {
        ThreadDemo t = new ThreadDemo();
        t.start();
        try {
            t.join(); // 
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Main Thread End!");
    }
}
```
子线程的逻辑是等待1秒后输出信息，当调用join()后，子线程的调用线程此时是主线程，所以主线程会暂时挂起不继续执行，等待子线程执行完成后，才会继续执行主线程。这时候才输出主线程中的信息。所以此时输出信息应该为：
```
// 一秒后输出：
Child Thread End!
Main Thread End!
```
但是如果不调用子线程的join()，此时主线程并不会挂起，会和子线程并发执行，主线程并没有等待，所以会先于子线程执行：
子线程：
```
package com.DeeJay;

import com.DeeJay.ThreadDemo.ThreadDemo;

public class Main {
    public static void main(String[] args) {
        ThreadDemo t = new ThreadDemo();
        t.start();
        try {
//            t.join(); // 不调用join 在子线程执行的过程中 主线程并不会被挂起
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Main Thread End!");
    }
}
```
此时输出为：
```
Main Thread End!  // 马上输出
Child Thread End! // 过一秒后输出
```
当有多个子线程时，如果前面的子线程先于后面的子线程调用start()之前调用join()的话，此时调用线程还是主线程，主线程**依然会挂起**，所以后面子线程的start()的逻辑要等到前面子线程执行完后主线程继续开始执行才能执行到。所以会有单线程的表现形式，但是此时其实仍是多线程。
```
package com.DeeJay;

import com.DeeJay.ThreadDemo.ThreadDemo;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ThreadDemo t1 = new ThreadDemo();
        ThreadDemo t2 = new ThreadDemo();
        t1.start();
        t1.join(); //  此处调用t1的join() 会挂起主线程  t1执行结束后才唤醒主线程继续执行t2.start()
        t2.start();
        t2.join(); // 同理 t2开始执行时挂起主线程 t2执行完后唤醒主线程
        System.out.println("Main Thread End!");
    }
}
```
输出结果为：
```
Child Thread End! // 过1秒
Child Thread End! // 过2秒
Main Thread End! // 过2秒 
```
### 线程之间的通信

以下方法用来和其他线程进行交流, 并且**只能在synchronize修饰的代码块或者方法里调用:**

wait( ) 告知被调用的线程放弃管程进入睡眠直到其他线程进入相同管程并且调用notify( )。
notify( ) 恢复**相同对象**中第一个调用 wait( ) 的线程。
notifyAll( ) 恢复**相同对象**中所有调用 wait( ) 的线程。具有最高优先级的线程最先运行。

```java
package com.DeeJay.ThreadDemo;

public class Q {
    private int n; // 生产的对象
    private boolean isDataReady = false;

    public int get() throws InterruptedException {
        if(!isDataReady){
            wait(); // 暂停, 等待着put()来设置好这个值只后再读取
        }
        System.out.println("Get " + n);
        isDataReady = false;  // 标记已经取走了值
        notify(); // 通知put()可以设置另外一个值了
        return this.n;
    }

    public void put(int n) throws InterruptedException {
        // 如果这个值已经设置好了
        if (isDataReady) {
            wait(); // 等待get()把值取走
        }
        this.n = n; //设置一个新的值
        isDataReady = true; // 标记已经设置好了
        System.out.println("Put: " + n);
        notify(); // 通知线程get() 这个最新的值
    }
}

```


#### 关于死锁

死锁发生在当两个线程对一对同步对象有循环依赖关系时

比如说有ABC三个子线程，A依赖于B，B依赖于C，C依赖于A，就有可能出现死锁。
