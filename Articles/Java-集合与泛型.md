---
title: Java-集合与泛型
date: 2019/02/22 00:00:01
tags: 
- Java
- 集合
- Collection
- 泛型
categories: 
- Java
---
Java-集合与泛型
<!--more-->


# 泛型编程

- 为什么需要泛型
当如果有不同数据类型的数据要进行相同操作的时候，就可以使用泛型
- 使用举例
我们用ArrayList来实现一个简单的stack数据结构，其中这个stack数据结构可以接收任意类型的数据对象，并且拥有`push`和`pop`方法。

```
public class testStack<T> { // 这里的T表示type 表示泛型  任意类型
    ArrayList<T> store = new ArrayList<T>();

    public void push(T item) {
        store.add(item);
    }

    public T pop() {
        T lastElement = store.get(store.size() - 1);
        store.remove(store.size() - 1);
        return lastElement;
    }
}
```
泛型解决的问题是：**代码逻辑一样，只是数据类型不一样**， 这时候就可以使用泛型。

> 上述例子中的testStack类也被称为泛型类，内部的`public T pop(){}`也被称为泛型方法，当单独定义泛型方法的时候一般用E来代表Element。

# 集合

- [集合详解](https://juejin.im/entry/58fc6a07a0bb9f0065be95cc)

![详细结构表](https://upload-images.jianshu.io/upload_images/7113407-b03ac1fd1d1e007e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 主要类型
集合主要类型有Collection和Map

#### Collection

Collection是一个接口，是`List`, `Set`以及`Queue`等接口的父接口。

任何实现了Collection接口的集合都支持增删改查。

- List
    - 有序并且可以重复
    - 可以精确的控制每个元素的插入位置，或删除某个位置元素
    - 常用的子类有：`ArrayList`， `LinkedList`， `Vector`，`Stack`
- Set
    - 不能加入重复元素，无序
    - 常用的子类有：`HashSet`， `TreeSet`
### Map
Map中的元素是一种Key-Value形式的映射。Key不可重复，Value可以重复。
- HashMap
    - 基于哈希表实现的一个Map的子类。
    - 无序排列，并且可以有并且也只可以有一个Key为null



> 对于链表来说，插入和删除的时间复杂度为`O(1)`（因为只需要调整指针就可以）,但是查找特定的元素或者查找第N个元素的复杂度为`O(n)`
