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

### 中间操作

仍然返回`Stream`的操作：
- `filter`
- `sorted`
- `map`

### 终结操作

返回非`Stream`的操作，也包括`void`
- `forEach()`
- `count/max/min`
- `findFirst/findAny`
- `anyMatch/noneMatch`
- `collect`

> 一个流只能被消费一次，一旦有一次终结操作调用之后，这个流就被消费了，已经关闭了，不能再次使用

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

可以看到，代码简化了特别多，那么来详细的解释一下
```
    users.stream().filter(user -> user.getName().startsWith("张"))
            .sorted(Comparator.comparing(User::getAge))
            .map(User::getName)
            .collect(Collectors.toList());
```
这段代码中的逻辑