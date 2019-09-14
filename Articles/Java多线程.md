---
title: Java多线程
date: 2019/09/01 00:00:01
tags: 
- Java
- 线程
categories: 
- Java
---
Java多线程
<!--more-->

# 为什么需要多线程
- 参见之前的文章《CPU: 这个时间太慢了》
- 现代的CPU都是多核的
  受散热量制约，目前的CPU大多都是3Ghz，再高的话就需要专业设备来进行降温，所以当代提升性能的方式是堆核，造成了目前的CPU都是多核的情况
- Java的执行模型是同步/阻塞的
- 默认情况下只有一个线程：
    - 处理问题非常自然
    - 但是也有严重的性能问题


# 开启一个新的线程

- Thread
  - Java中唯一的代表线程的东西
  - 只有start()方法才能开始并发执行
  - 每多开一个线程，就多一个执行流
  - 方法栈（局部变量）是线程私有的
  - 静态变量/类变量是所有线程共享的

# 多线程引起的问题
> 多线程难的地方在于：要看着同一份代码，想象着不同的人在以疯狂的乱序执行它

举个例子，现有一个静态变量i,则其可以被多个线程共享，在每一个线程中将其加1，循环1000次，最终得到的结果不一定为1000(每次运行的结果也不一样)

```
public static int i = 0;

public static void main(String[] args) {
  for(int a = 0; a < 1000; a++){
    new Thread(() -> modifySharedVar).start();
  }
}

public static void modifySharedVar() {
  try{
    Thread.sleep(1);
  }
  catch(Exception e) {
    e.printStackTrace();
  }
  i++;
  System.out.println(i);
}
```

造成上述问题的愿意在于：`i++;`这个操作不是一个原子操作。其分为三步：
1. 取i的值
2. 将取的值+1
3. 将值写回i

这就有可能造成一个问题，假设此时i为0，线程1取到0然后加1，走完第2步时阻塞了未来得及将1写回i，这是线程2取i的值还是0，将其加1写回i，然后线程1恢复也将1写回i。

上述原因就造成了最终结果不为1000的情况


## 适合多线程的场景
- 对于IO密集型应用极其有用
  - 网络IO（通常包括数据库）
  - 文件IO
- 对于CPU密集型应用稍有折扣
- 性能提升的上限在哪？
  - CPU：占用100%

## 线程安全
- 享用了多线程的便利，就要承受多线程带来的问题
    - 原子性
    - 共享变量
    - 默认的实现几乎都不是线程安全的
 
## 线程不安全的表现
- 数据错误
    - i++
    - if-then-do

先来看一个例子：
```
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
  
public class Main {
    private static Map<Integer, Integer> map = new HashMap<Integer, Integer>();// HashMap非线程安全  死锁问题
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Thread(Main::putIfAbsent).start();
        }
    }


    private static void putIfAbsent() {
//        随机生成一个1-10之间的数字 如果不在Map中，就将其加入Map
        int r = new Random().nextInt(10);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!map.containsKey(r)) {
            map.put(r, r);
            System.out.println("put " + r);
        }
    }
}

```
上述例子中出现了一个的Map put多次相同key的情况

接下来手动实现一个死锁：
```
public class Main {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    static class Thread1 extends Thread{
        @Override
        public void run() {
            synchronized (lock1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock2) {
                    System.out.println("never print");
                }
            }

        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            synchronized (lock2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock1) {
                    System.out.println("never print");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }
}

```
分析一下上述死锁形成的原因：
1. Thread1和Thread2几乎是同时启动的，启动之后，T1先拿到lock1锁，然后休眠500ms，T2先拿到lock2锁，开始休眠100ms
2. T2休眠时间较短，先醒过来，然后T2需要拿到lock1锁，但是此时lock1被T1拿着，T2被迫一直在等lock1锁释放
3. T1后醒过来，此时T1想要拿到lock2锁，但是由于lock2锁在被T2拿着，所以T1也被迫等lock2释放
4. 2个线程拿着自己的锁，但是又在等对方释放自己需要的锁，形成死锁

#### 排查死锁问题：
-  ps aux | grep java (unix下)
- 使用jps 列出当前所有java进程（通用）
  1. 终端输入`jps`列出当前进程，上述例子中出现死锁的地方为Main，拿到PID  ,例如4756
  2. `jstack 4756`查看栈信息（一般信息较长，可以导出到文件中查看）,可以查看到信息"Found one Java-level deadlock:",可以看到详细的信息

> 预防死锁发生的原则：所有的线程都按照相同的顺序拿到锁

## 实现线程安全的手段
1. 使用不可变类
      - Integer/String/...等等

2. synchronized
    - synchronized同步块
    - 同步块t同步了什么东西？
      1. synchronized(object) 将object当成一个锁
      2. static方法上使用synchronized,由于static是和类绑定，所以锁住的是当前的class对象，即把这个Class对象当成锁
      3. 如果不是static方法，那么方法上的synchronized会将this当成锁，即锁住的是这个实例对象
    - Collections.synchronized
    常见的Collection都不是线程安全的，Collections工具类提供了synchronizedXXX方法将对应的集合转为线程安全的。

现在使用synchronized来修复之前共享静态变量的问题：
```
public class Main {
    private static int i = 0;
    private static Object lock1 = new Object(); // 声明一个锁用来同步 保证同一时刻只有一个线程可以访问到i
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i ++) {
            new Thread(() -> ModifySharedVar()).start();
        }
    }
    private static void ModifySharedVar() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (lock1) { // 在这使用synchronized同步块来保证同时只有一个线程修改i
            i += 1;
        }

        System.out.println("i = " + i);
    }
}

```
synchronized还有多种变化形式，对于上述方法，可以直接写为：
```
private synchronized static void ModifySharedVar() { // 写到方法上
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        i += 1;
        System.out.println("i = " + i);
    }
```
达到的结果也是一样的。

对于实例上的方法，如果加了synchronized的，那么等价于：
```
    public synchronized void method1 () {
        doSomeThing();
    }
//    等价于：
    public void method2() {
        synchronized (this) {
            doSomeThing();
        }
    }
```
3. 使用第三方的实现类
JUC包
 - AtomicInteger
- ConcurrentHashMap 任何使用HashMap有线程安全问题的地方，无脑使用ConcurrentHashMap替换即可
- ReentrantLock 可重入锁 比synchronized要强大，可以手动unlock

## Object类里的线程方法
- 为什么Java中的所有对象都可以成为锁
  - Object.wait()/notify()/notifyAll()方法
   - 线程的状态与线程调度
  
wait()和notify()方法为线程协作提供了方法

wait()方法的注释：`Causes the current thread to wait until it is awakened, typically by being <em>notified</em> or <em>interrupted</em>.`
`@throws IllegalMonitorStateException if the current thread is not
     *         the owner of the object's monitor`
可以看到，如果调用wait方法的时候，没有持有当前调用wait方法的对象时，是会抛出`IllegalMonitorStateException`异常的:
```
//    public static void main(String[] args) throws InterruptedException {
//        lock1.wait(); // 会抛出异常
//    }

    public static void main(String[] args) throws InterruptedException {
        synchronized (lock1) {
            lock1.wait();
        }
    }
```
> monitor即为常规意义上的“拿到的锁”

值得注意的是，lock.wait()调用了之后，**lock这个锁会被释放掉!** （为之后notify做准备）

关于notify和notifyAll:
- notify会随机挑选一个线程来进行唤醒，挑选方式由JVM自己决定
- notifyAll则会唤醒所有的线程，这些被唤醒的线程则会开始重新竞争锁，但是只有拿到锁的那一个线程会继续执行逻辑，其余线程则又会陷入沉睡，且所有线程竞争时没有任何优先级


###### 线程的状态
线程的状态可以分为下列6种：
1. 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
2. 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。
线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
3. 阻塞(BLOCKED)：表示线程阻塞于锁。
4. 等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
5. 超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
6. 终止(TERMINATED)：表示该线程已经执行完毕。

上述修改共享静态变量的例子，只有当前拿到锁的线程为runnable状态，同一时间其他的线程为blocked状态

而对于调用了monitor.wait()方法的线程，则会进入waiting状态。

[相关文章: 线程的状态](https://blog.csdn.net/pange1991/article/details/53860651)


### 多线程的经典问题: 生产者/消费者模型

使用三种方法来实现生产者/消费者模型：
1. wait/notify/notifyAll

2. Lock/Condition
将lock变为一个ReentrantLock,将一个synchronized快变为一个try/catch，进入上锁，finally解锁

3. BlockingQueue


### 线程池和Callable/Future
- 什么是线程池
    - 线程是昂贵的（Java线程模型的缺陷）
     Java线程模型的缺陷是其自己是没有调度器的，每个线程调度都依赖于操作系统的线程调度，因此每个线程都要占用操作系统的资源，所以其非常的昂贵
    - 线程池是预先定义好的若干个线程
    不用每次都先创建新的线程
    - Java中的线程池
- Callable/Future
    - 类比Runnable, Callable可以返回值，抛出异常
    - Future代表一个未来才会返回的结果
   ```
    ExcutorService threadPool;
    Future f1 = threadPool.submit(Callable..)
  ```
- 多线程的WordCount
