---
title: Java中的集合类的简单介绍
date: 2018/07/19 00:00:01
tags: 
- Java
- 集合
- Collection
categories: 
- Java
---
Java中的集合类的简单介绍
<!--more-->

面向对象语言对事物的描述都是通过对象来体现的，那么肯定要涉及到对多个对象的操作。

其中肯定免不了对多个对象的存储，目前只有StringBuilder和数组可以作为容器来存储对象，但是StringBuilder存储的只能是字符串，而数组长度又是固定的，不能适应变化的需求。 所以Java中提供了集合类。


集合类的特点就是**长度可变**。

来看最常用的集合类`java.util.ArrayList<E>`.

##ArrayList

- 创建对象
```
import java.util.ArrayList;

public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<String>();
        System.out.println(arr); // 输出: []
    }
}
```
注意这边输出的不是arr的内存地址，而是ArrayList类的对象中的内容。

- 添加元素
`public boolean add(E e)`
`public void add(int index, E element)`

```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        
        arr.add(1);
        arr.add(2);
        arr.add(3);
        
        arr.add(1,4);
        System.out.println(arr); // 输出: [1,4,2,3]
    }
}
```
- 获取元素
`public E get(int index)`
- 集合长度
`public int size()`
```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        
        arr.add(1);
        arr.add(2);
        arr.add(3);
        
        System.out.println(arr.get(0)); // 输出: 1
        System.out.println(arr.size()); // 输出: 3
    }
}
```
- 删除元素
`public boolean remove(Object obj)`
`public E remove(int remove)` 返回被删除的元素
```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<String>();
        
        arr.add("Hello");
        arr.add("Java");
        arr.add("world");
        
        String deletedElement = arr.remove(0);
        
        System.out.println(deletedElement); // 输出: Hello
        System.out.println(arr); // 输出: [Java, world]
    }
}
```
```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<String>();
        
        arr.add("Hello");
        arr.add("Java");
        arr.add("world");
        
        boolean isDeleted = arr.remove("Java");
        
        System.out.println(isDeleted); // 输出: true
        System.out.println(arr); // 输出: [Hello, world]
    }
}
```
- 修改元素
`public E set(int index, E element)`
```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<String>();
        
        arr.add("Hello");
        arr.add("Java");
        arr.add("world");
        
        String modElement = arr.set(1,"Python");
        
        System.out.println(modElement); // 输出: Java 返回被修改的元素
        System.out.println(arr); // 输出: [Hello, Python, world]
    }
}
```

- 遍历操作 `size()`       `E get(int index)`
```
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> arr = new ArrayList<String>();
        
        arr.add("Hello");
        arr.add("Java");
        arr.add("world");
        
        for(int i = 0; i < arr.size(); i ++) {
            String s = arr.get(i);
            System.out.println(s);
        }
    }
}
```


