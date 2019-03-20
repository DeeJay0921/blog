[面试--线程](https://github.com/Homiss/Java-interview-questions/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/Java%E9%9D%A2%E8%AF%95%E9%A2%98%E4%B9%8B%E5%A4%9A%E7%BA%BF%E7%A8%8B(%E4%B8%80).md)

[知乎--java中的多线程究竟在什么情况下使用？](https://www.zhihu.com/question/65200684)

[知乎--多线程有什么用？？](https://www.zhihu.com/question/19901763)
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
|方法|意义|
|:---:|:---:|
|getName|获取线程名称|
|getPriority|获取线程优先级|
|isAlive|判定线程是否仍在执行|
|join|等待一个线程终止|
|run|线程的入口|
|sleep|在一定时间内挂起线程|
|start|通过调用运行方法来启动线程|


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
