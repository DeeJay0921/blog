---
title: Java多线程
date: 2019/09/01 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
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
  - 只有start()方法才能开始并发执行，**注意，run()方法并不会使得线程并发执行，而是会等当前调用run()的线程执行完成后再进行下一步操作，实质上跟不开线程的执行时间是一样的**
  - 每多开一个线程，就多一个执行流
  - 方法栈内部的局部变量是线程私有的，即不同线程内部的方法中声明的变量完全不相干。
  - 静态变量/类变量是所有线程共享的，由于总要将多线程的运行结果收集起来，所以需要线程共享的变量，而这也就是多线程出现问题的原因。

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
> 原子操作指的是在多线程领域，在某一个时刻只能被一个线程执行的操作

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
- 对于CPU密集型应用稍有折扣（因为多线程本来就是为了提高CPU的利用率，CPU密集型本身就快将CPU跑满了）
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
    - 同步块同步了什么东西？
      1. synchronized(object) 将object当成一个锁
      2. static方法上使用synchronized,由于static是和类绑定，所以锁住的是当前的class对象，即把这个Class对象当成锁
      3. 对于实例对象来说，那么方法上的synchronized会将this当成锁，即锁住的是这个实例对象
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
 - AtomicInteger等原子类
- ConcurrentHashMap 任何使用HashMap有线程安全问题的地方，无脑使用ConcurrentHashMap替换即可
- `java.util.concurrent.locks.ReentrantLock`是一个`java.util.concurrent.locks.Lock`的实现，其是一个ReentrantLock，即可重入锁 比synchronized要强大，可以手动unlock

> 可重入锁: 一个线程在获取了锁之后，可以再次去获取同一个锁

# Object类里的线程方法

Java在语言级别上就支持了多线程，Java中的所有对象都可以成为锁
  
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


## 线程的状态
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


## 多线程的经典问题: 生产者/消费者模型

使用三种方法来实现生产者/消费者模型：

给定一个场景： 生产者给定10个随机整数给消费者使用，每次生产一个出来后，生产者不能再进行生产，得等到消费者消费这个整数之后才能继续生产

大概的使用方式为：
```
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container(); // 作为存储生产出来值的容器
        Object lock = new Object();

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }
}
```

### 使用wait/notify/notifyAll

首先通过`Optional`实现一个容器来存放生产的数据
```
// 作为容器的Container：
import java.util.Optional;

public class Container {
    private Optional<Integer> value = Optional.empty();

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
```

```
public class Producer extends Thread{
    private Container container;
    private final Object lock;

    Producer(Container container, Object lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            synchronized (lock) {
                while (container.getValue().isPresent()) { // 只要不为空就一直wait()
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 为空的话开始消费
                int random = new Random().nextInt();
                System.out.println("Producing... " + random);
                container.setValue(Optional.of(random));

                lock.notify(); // 生产完成后通知 Consumer
            }
        }
    }
}
```

```
public class Consumer extends Thread{
    private Container container;
    private final Object lock;

    Consumer(Container container, Object lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            synchronized (lock) {
                while (!container.getValue().isPresent()) { // 如果为空 则一直等待
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 如果不为空 开始进行消费
                System.out.println("Consuming... " + container.getValue().get());
                container.setValue(Optional.empty());
                // 消费完成 通知Producer进行生产
                lock.notify();
            }
        }
    }
}
```


### 使用Lock/Condition

还可以通过`Condition`来创建2个对应的条件，修改一下`Container`：

```
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Container {
    private Optional<Integer> value = Optional.empty();

    //    新增2个条件
    private Condition notConsumedYet; // 还没有被消费掉
    private Condition notProducedYet; // 还没有被生产出来

    public Container(ReentrantLock lock) { // 构造函数接收一个 可重入锁 来初始化2个条件
        this.notConsumedYet = lock.newCondition();
        this.notProducedYet = lock.newCondition();
    }

    public Condition getNotConsumedYet() {
        return notConsumedYet;
    }

    public Condition getNotProducedYet() {
        return notProducedYet;
    }

    public Optional<Integer> getValue() {
        return value;
    }

    public void setValue(Optional<Integer> value) {
        this.value = value;
    }
}
```
而`ReentrantLock`的`lock()`和`unlock()`的组合就可以视为等价于一个`synchronized`块，可以使用`Condition`修改之前的代码为：

```
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Producer extends Thread{
    private Container container;
    private final ReentrantLock lock;

    Producer(Container container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            try {
                lock.lock(); // 通过lock() 和 unlock()等价替换synchronized{}
                while (container.getValue().isPresent()) { // 只要不为空就一直wait()
                    try {
                        container.getNotConsumedYet().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 为空的话开始消费
                int random = new Random().nextInt();
                System.out.println("Producing... " + random);
                container.setValue(Optional.of(random));
                container.getNotProducedYet().signal(); // 生产完成后通知 Consumer
            }finally {
                lock.unlock(); // 无论发生什么事 都要保证锁被释放掉
            }
        }
    }
}
```
```
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends Thread{
    private Container container;
    private final ReentrantLock lock;

    Consumer(Container container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            try {
                lock.lock();
                while (!container.getValue().isPresent()) { // 如果为空 则一直等待
                    try {
                        container.getNotProducedYet().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 如果不为空 开始进行消费
                System.out.println("Consuming... " + container.getValue().get());
                container.setValue(Optional.empty());
                // 消费完成 通知Producer进行生产
                container.getNotConsumedYet().signal();
            } finally {
                lock.unlock(); // 无论发生什么事 都要保证锁被释放掉
            }
        }
    }
}
```

### 使用BlockingQueue

`BlockingQueue`是一个会阻塞的队列接口，可以等待该队列变为空或者非空。

此时的Main可以简化为：
```
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>(1); // 长度为1的容器

        Producer producer = new Producer(blockingDeque);
        Consumer consumer = new Consumer(blockingDeque);

        producer.start();
        consumer.start();
    }
}
```
而内部的`Producer`和`Consumer`改为：
```
import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class Producer extends Thread{
    private BlockingDeque<Integer> blockingDeque;

    Producer(BlockingDeque<Integer> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            int random = new Random().nextInt();
            System.out.println("Producing... " + random);
            try {
                blockingDeque.put(random); // 生产出来将其放入 blockingDeque 下次循环到这时 线程会自动停下来
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```
```
import java.util.concurrent.BlockingDeque;

public class Consumer extends Thread{
    private BlockingDeque<Integer> blockingDeque;

    Consumer(BlockingDeque<Integer> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            try {
                System.out.println("Consumering... " + blockingDeque.take()); // 在这消费，如果没有生产出来 也会自动停下来
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

> 注意，上述例子存在一个问题是，会重复执行2次`System.out.println("Producing... " + random);`这是因为执行了第二次`sout`之后线程才停止了，所以会输出2次，然后该例子消费者会等`blockingDeque.put(random);`执行了之后立即去消费，如果要改善这种方式，那么单靠一个`BlockingDeque`是做不到的，须得再引入一个`BlockingDeque`作为控制开关。

# 线程池和Callable/Future

Java自己是没有调度器的，每个线程调度都依赖于操作系统的线程调度，因此每个线程都要占用操作系统的资源，所以其非常的昂贵

而线程池就是预先定义好的若干个线程，有任务来了就丢到线程池里交给某个线程来处理，处理完该任务的线程又会重新进入等待。

Java中通过`Executors`来创建一个线程池，如：

```
ExecutorService threadPool = Executors.newFixedThreadPool(10); // 规模为10的一个线程池
```

返回值为一个`ExecutorService`类型的值，该类型有一个`submit()`方法，可以立即返回一个`Future`类型，该类型代表一个异步的计算结果，而`submit()`方法可以接受`Runnable/Callable`类型的值。

> `Callable`相比于`Runnable`,可以自定义返回值，且可以抛出异常，而`Runnable`既不能返回值，对于异常处理也只能忽略掉

来看一个使用的基本例子：

```
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        Future<Integer> future1 = threadPool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(10000);
                return 1;
            }
        }); // 调用 submit 之后 立即返回一个 future
//        boolean cancel = future.cancel(true);// 等不及了可以调用cancel()方法来取消任务

        Future<Object> future2 = threadPool.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new RuntimeException("测试Callable抛出异常");
            }
        });

        System.out.println("future1.get() = " + future1.get()); // 在计算完成后会执行这句
        // 在future1得到结果之前会一直在这阻塞 不会抛出异常  
        System.out.println("future2.get() = " + future2.get()); // 上面输出语句执行完后会接收到future2抛出的异常
    }
}
```

## 使用多线程解决WordCount问题

`Word Count`是一个著名的练手程序。一个文本文件包含若干行，每行包含若干个只包含小写字母的单词，单词之间以空格分割。我们要使用多线程对这个文本的每一行文件单独使用一个线程去分析结果，最后将结果汇总起来

完整例子如下：
```
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordCount {
    public static Map<String, Integer> count(File targetFile) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // 假设线程池规模为10
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(targetFile))) {
            List<Future<Map<String, Integer>>> futureList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Future<Map<String, Integer>> singleLineResult = threadPool.submit(new SingleLineWork(line));
                futureList.add(singleLineResult);
            }
            threadPool.shutdown(); // 关闭线程池 如果不关的话线程池内的现场会一直处于wait状态等待下次再有任务进来  视情况看关不关
            Map<String, Integer> totalResult = mergeSingleLineResultsToTotal(futureList); // 将各行结果汇总
            System.out.println("totalResult = " + totalResult);
            return totalResult;
        }
    }


    private static Map<String, Integer> mergeSingleLineResultsToTotal(List<Future<Map<String, Integer>>> futureList) throws ExecutionException, InterruptedException {
        Map<String, Integer> totalResult = new HashMap<>();
        for (Future<Map<String, Integer>> future : futureList) {
            Set<Map.Entry<String, Integer>> entries = future.get().entrySet();
            for (Map.Entry<String, Integer> entry : entries) {
                totalResult.put(entry.getKey(), totalResult.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        return totalResult;
    }

    private static class SingleLineWork implements Callable<Map<String, Integer>> {
        private String line;

        public SingleLineWork(String line) {
            this.line = line;
        }

        @Override
        public Map<String, Integer> call() {
            Map<String, Integer> singleLineMap = new HashMap<>();
            String[] split = line.split(" ");
            for (String s : split) {
                singleLineMap.put(s, singleLineMap.getOrDefault(s, 0) + 1);
            }
            return singleLineMap;
        }
    }
}

```
