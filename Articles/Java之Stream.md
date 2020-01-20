---
title: Java之Stream
date: 2020/01/17 15:35:01
cover: https://cdn.journaldev.com/wp-content/uploads/2014/04/java-8-stream.jpg
tags: 
- Java
- Stream
categories: 
- Java
---
本文主要介绍了Java8引入的最重要的概念Stream
<!--more-->

# Stream

`Stream`应该算是`Java 8 `引入的最重要的概念了

> 对于新接触流的开发者，可以使用`IDEA`的插件 [Java Stream Debugger](http://plugins.jetbrains.com/plugin/9696-java-stream-debugger) 来进行调试及加深理解

## 使用`Stream`的好处

- 简化代码
- 不易出错
- 提高了可读性/可维护性

## `Stream`的`API`

### 创建`Stream`

总的来说有如下几种创建一个`Stream`的方式：
- `Collection.stream();`
- `Stream.of();`
- `String.chars();`
- `IntStream.range();`

前两种方式都比较常见，这边只来举例`String.chars()`和`IntStream.range()`的使用：

```java
// 使用 String.chars 创建一个流
public class Main {
    /**
     * 统计一个给定的字符串中，大写英文字母（A,B,C,...,Z）出现的次数。
     *
     * <p>例如，给定字符串"AaBbCc1234ABC"，返回6，因为该字符串中出现了6次大写英文字母ABCABC
     *
     * @param str 给定的字符串
     * @return 字符串中大写英文字母出现的次数
     */
    public static int countUpperCaseLetters(String str) {
        return (int) str.chars().filter(Character::isUpperCase).count();
    }

    public static void main(String[] args) {
        System.out.println("countUpperCaseLetters(\"AaBbCc1234ABC\") = " + countUpperCaseLetters("AaBbCc1234ABC"));;
    }
}
```

```java
// 使用 IntStream.range 创建流的例子
public class Main {
    /**
     * 打印从start到end区间所有的奇数，包括start和end本身（若符合条件）。 注意，数字之间用英文逗号分隔。
     *
     * <p>例如，start=1,end=5，则打印1,3,5 又如，start=-2,end=2，则打印-1,1
     *
     * @param start 区间开始
     * @param end 区间结束
     */
    public static void printOddNumbersBetween(int start, int end) {
        IntStream.range(start, end + 1) // Java的区间约定都是包含start不包含end 这里的例子需要包括end 所以+1
                .filter(Main::isOddNumber)
                .mapToObj(number -> number + ",")
                .forEach(System.out::print);
    }

    private static boolean isOddNumber(Integer number) {
        return number % 2 != 0;
    }

    public static void main(String[] args) {
        printOddNumbersBetween(1, 5);
    }
}
```
可以看到，使用流操作，比起写个循环依次判断的逻辑要简洁很多，且不易出bug

> 关于区间问题，几乎所有的编程语言的约定都是前包含后不包含

### 中间操作

仍然返回`Stream`的操作：
- `filter`
- `sorted`
- `map`

这几个都是常用操作，就不多举例了

来看一个不常用的`flatMap`的例子：

```java
public class Main {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("I am a boy", "I have a dog", "I am a girl", "I have a cat");
        List<String> flatList = list.stream()
                .map(str -> str.split(" ")) // 按照空格分开 list 中的4个元素由String类型变为了Array类型
                .flatMap(Stream::of) // flatMap 将分开的小数组一起压扁到大list里面
                .collect(Collectors.toList());
        System.out.println("flatList = " + flatList);// [I, am, a, boy, I, have, a, dog, I, am, a, girl, I, have, a, cat]
    }
}
```
> 对于`flatMap`的压扁操作，如果是多维的情况，那么每使用一次`flatMap`就会降低一个维度

### 终结操作

返回非`Stream`的操作，也包括`void`
- `forEach()`
- `count/max/min`
- `findFirst/findAny`
- `anyMatch/noneMatch`
- `collect`

> 一个流只能被消费一次，一旦有一次终结操作调用之后，这个流就被消费了，已经关闭了，不能再次使用

来看几个不常用的终结操作的例子：

```java
// anyMatch/noneMatch/findFirst/findAny
public class Main {
    static class User {
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

    public static void main(String[] args) {
        List<User> userList = Arrays.asList(new User("张三", 18),
                new User("张三丰", 100),
                new User("李四", 25));
        // 去查询列表内部是否存在姓 张 的用户
        boolean anyMatch = userList.stream().anyMatch(Main::IsZhangUser);
        System.out.println("anyMatch = " + anyMatch);
        // 去查询列表是否一个姓 杨 的用户都没有
        boolean noneMatch = userList.stream().noneMatch(user -> user.getName().startsWith("杨"));
        System.out.println("noneMatch = " + noneMatch);
        // 查询随便任何一个姓张的用户
        Optional<User> anyZhang = userList.stream().filter(Main::IsZhangUser).findAny();
        // 查询第一个姓张的用户
        Optional<User> firstZhang = userList.stream().filter(Main::IsZhangUser).findFirst();
        System.out.println("anyZhang = " + anyZhang.get().getName());
        System.out.println("firstZhang = " + firstZhang.get().getName());
    }
    private static boolean IsZhangUser (User user) {
        return user.getName().startsWith("张");
    }
}
```

上例展示了几个终结操作的用法，这里补充一下`Optional`的用法，一般像上例这样，拿到一个`Optional<User> anyZhang`,一般不要像下面这样使用`Optional`:

```
    public static void main(String[] args) {
        List<User> userList = Arrays.asList(new User("张三", 18),
                new User("张三丰", 100),
                new User("李四", 25));
        Optional<User> anyZhang = userList.stream().filter(Main::IsZhangUser).findAny();
        // 下面的写法是不推荐的！        
        if (anyZhang.isPresent()) {
            System.out.println("anyZhang = " + anyZhang.get().getName());
        }else {
            throw new IllegalStateException();
        }
    }
```

而`Optional`的正确用法应该也是通过函数式的使用：
```
    public static void main(String[] args) {
        List<User> userList = Arrays.asList(new User("张三", 18),
                new User("张三丰", 100),
                new User("李四", 25));
        Optional<User> anyZhang = userList.stream().filter(Main::IsZhangUser).findAny();
        // 推荐使用函数式的写法
        anyZhang.ifPresent(System.out::println);
        anyZhang.orElseThrow(IllegalStateException::new);
        // 函数式的写法的好处是可以支持链式调用
        System.out.println("anyZhang name = " + anyZhang.orElseThrow(IllegalStateException::new).getName());
    }
```

### 初次使用`Stream`

假设现有如下场景：

```java
public class Main {
    static class User {
        private String name;
        private Integer age;
    }

    public static void main(String[] args) {
        // 现有如下需求  需要从用户列表里面 过滤出姓张的用户 然后按照age进行排序，最后获取其name列表
        List<User> users = getUsers(); // 模拟一个users数据源

        List<User> zhangUsers = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("张")) {
                zhangUsers.add(user);
            }
        }

        Collections.sort(zhangUsers, (o1, o2) -> {
            if ((o1.age - o2.age) > 0) {
                return 1;
            }else if ((o1.age - o2.age) < 0) {
                return -1;
            }
            return 0;
        });
        
        List<String> names = new ArrayList<>();
        for (User user : zhangUsers) {
            names.add(user.name);
        }
        System.out.println("names = " + names);
    }

    private static List<User> getUsers() {
        return null;
    }
}
```

从上例中可以看到，在Java8之前为了实现该需求，会有很多行逻辑

那么针对上例，我们使用`Stream`的方式进行简化：

```java
public class Main {
    static class User {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

    public static void main(String[] args) {
        // 现有如下需求  需要从用户列表里面 过滤出姓张的用户 然后按照age进行排序，最后获取其name列表
        List<User> users = getUsers(); // 模拟一个users数据源

        List<String> names = users.stream().filter(user -> user.getName().startsWith("张"))
                .sorted(Comparator.comparing(User::getAge))
                .map(User::getName)
                .collect(Collectors.toList());

        System.out.println("names = " + names);
    }

    private static List<User> getUsers() {
        return null;
    }
}
```

可以看到，代码简化了特别多

### collector 与 Collectors

`collect`是最强大的操作,在进行了一堆的中间操作之后，我们可以使用`collect()`方法将这个流收集起来

该方法接收一个`Collector`类型的参数，一般通过使用JDK自带的`Collectors`工具类中的方法去进行收集

`Collectors`工具类中主要的方法有：
- `toSet`/`toList`/`toCollection`
- `joining`
- `toMap()`
- `groupingBy()`

关于`toSet()`和`toList`，文档里面提及，JDK并不会保证返回值是不可变的或者是序列化的或者是线程安全的，所以如果对返回值有要求的话，需要使用`toCollection`:

```
    public static void main(String[] args) {
        List<User> userList = Arrays.asList(new User("张三", 18),
                new User("张三丰", 100),
                new User("李四", 25));
        LinkedList<User> linkedList = userList.stream().filter(Main::IsZhangUser)
                .collect(Collectors.toCollection(LinkedList::new)); // 通过 toCollection 指定返回值的类型 
    }
```

同时`Collectors`工具类的文档里面提供了很多示例，如果有相应的需求也可以先去看看示例是怎么使用的，比方说我们现有如下需求：
```java
public class Main {
    // 对传入的List<User>进行如下处理：
    // 返回一个从部门名到这个部门的所有用户的映射。同一个部门的用户按照年龄进行从小到大排序。
    // 例如，传入的users是[{name=张三, department=技术部, age=40 }, {name=李四, department=技术部, age=30 },
    // {name=王五, department=市场部, age=40 }]
    // 返回如下映射：
    //    技术部 -> [{name=李四, department=技术部, age=30 }, {name=张三, department=技术部, age=40 }]
    //    市场部 -> [{name=王五, department=市场部, age=40 }]
    public static Map<String, List<User>> collect(List<User> users) {
        Map<String, List<User>> collectMap = users.stream()
                .sorted(Comparator.comparing(User::getAge))
                .collect(Collectors.groupingBy(User::getDepartment));
        return collectMap;
    }

    public static void main(String[] args) {
        System.out.println(
                collect(
                        Arrays.asList(
                                new User(1, "张三", 40, "技术部"),
                                new User(2, "李四", 30, "技术部"),
                                new User(3, "王五", 40, "市场部"))));
    }

    static class User {
        // 用户的id
        private final Integer id;
        // 用户的姓名
        private final String name;
        // 用户的年龄
        private final int age;
        // 用户的部门，例如"技术部"/"市场部"
        private final String department;

        public User(Integer id, String name, int age, String department) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.department = department;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getDepartment() {
            return department;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User person = (User) o;
            return Objects.equals(id, person.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
```

我们在上例中直接使用了`Collectors.groupingBy(User::getDepartment)`通过一个`User`到`String department`的映射,使用`groupingBy`将其分组

再比如说我们有如下场景：

```java
public class Problem5 {
    // 目标需求为： 把订单处理成ID->订单的映射
    // 例如，传入参数[{id=1,name='肥皂'},{id=2,name='牙刷'}]
    // 返回一个映射{1->Order(1,'肥皂'),2->Order(2,'牙刷')}
    public static Map<Integer, Order> toMap(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));
    }

    public static void main(String[] args) {
        System.out.println(toMap(Arrays.asList(new Order(1, "肥皂"), new Order(2, "牙刷"))));
    }

    static class Order {
        private Integer id;
        private String name;

        Order(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
```

上例中使用了`Collectors.toMap`方法，将`id`和`Order`类型的值作为`key`和`value`生成一个`Map<Integer, Order>`

注意上例中如果使用`groupingBy`的话，返回的`value`值是一个`List`,不能符合我们的需求

> `Function.identity()`等价于形如`t -> t`形式的`Lambda`表达式。

关于其余的`Collectors`的用法可以参见JDK中`Collectors`的文档

## 并发流`parallelStream`

可以通过使用并发流，提高那些相互独立的操作的性能，但是使用的时候要小心，得明确的知道自己在干什么,且必须得经过性能测试

使用并发流的小例子:
```java
import org.apache.commons.math3.primes.Primes;

import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        // 统计 0 到 100_0000之间有多少个质数
        long t0 = System.currentTimeMillis();
        long count1 = IntStream.range(0, 100_0000)
                .filter(Primes::isPrime) // 在这里使用了apache 的 commons-math3 库来过滤质数
                .count();
        System.out.println("count1 = " + count1);
        System.out.println("normal Stream: " + (System.currentTimeMillis() - t0));

        long t1 = System.currentTimeMillis();
        long count2 = IntStream.range(0, 100_0000)
                .parallel() // 采用并发流
                .filter(Primes::isPrime) // 在这里使用了apache 的 commons-math3 库来过滤质数
                .count();
        System.out.println("count2 = " + count2);
        System.out.println("parallel Stream:" + (System.currentTimeMillis() - t1));
    }
}
```