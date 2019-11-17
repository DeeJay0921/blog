---
title: Collection体系原理了解
date: 2019/07/22 00:00:01
cover: https://marcus-biel.com/wp-content/uploads/2015/08/class-and-interface-hierarchy.png
tags: 
- Java
- 集合
- Collection
categories: 
- Java
---
Collection体系原理了解
<!--more-->

# Collection体系简介

- Collecion的体系
  - Collection的体系结构
[集合体系](https://juejin.im/entry/58fc6a07a0bb9f0065be95cc)
集合体系中有2大类，Collection和Map，其中Collection下又有List和Set。
  - List/Set约定
List有序，Set无序。
>有序无序意味着存在索引且从0开始

- Collection体系提供的常用方法
  - new: `new ArrayList(Collection)`, `new ArrayList()`
  - R: `size()`/ `isEmpty()`/ `contains()` / `for()` / `stream()`
  - C/U: `add()`/ `addAll()`/ `retainAll()`
  - D: `clear()`/ `remove()`/ `removeAll()`


# List
- 最常用的ArrayList
  - 本质上就是一个数组，但是在add的时候会去动态的增加自己的长度(即创建一个新的数组)
- 常见面试题： 动态扩容的实现
  - 创建一个更大的空间，然后把原先的所有元素拷贝过去

# Set
- 不允许有重复元素的集合
  - 判断重复调用的是`equals()`
- 如果要自己实现一个Set，怎么实现?
1.如果要自己实现一个Set，肯定能想到的是一个简单思路就是每次添加新元素时，查看现有的元素中存不存在目标元素，如果有则不加入。这样的思路的问题是，如果现有的元素已经有很多了的话，那么会比较很多次，性能会受影响。
2.所以举个例子：假设我们要把所有用户的姓名加入到一个自己实现的Set中来时，我们可以先根据用户的姓来进行比较添加，比如现有赵钱孙李四种姓氏，要添加赵四时，就只需要去跟赵姓中的元素们进行比较，如有相同则不添加，不需要跟整个现有集合进行比较。提高了性能。
3.上述的例子，就是hashCode的含义，对于Set集合，将Object们对应为一个个的int值，相同的int值对应的就相当于是一个姓氏，新加Object时，就只需要跟相同int值的Object们进行equals比较就好了。而这个将Object对应为int值的方法，就是`hashCode()`
> 上述的一个个int值对应的小集合，称为hash桶

- Java世界里的又一重要约定: **hashCode**
  - 同一个对象必须始终返回相同的hashCode
  - 两个对象的equals返回true，必须返回相同的hashCode
  - 两个对象不等，也可能返回相同的hashCode

### 哈希算法
- 哈希就是一个单向的映射
  因为int值是有限度的，总共也就42亿中hash值，而对象可能有更多种，所以只能从对象映射到int值，不能反向映射，所以是单向的。
- 例子：见从姓名到姓的哈希运算
- 从任意对象到一个整数的hashCode

## HashSet
- 最常用，最高效的Set实现
- HashSet的高效性
使用Hash算法使得HashSet的查找性能特别高
- 使用HashSet可以进行去重
- HashSet是无序的！如果需要排序得使用LinkedHashSet



# Map
- Map的简介与实战：
  - C/U: `put()`/ `putAll()`
  - R: 
    - `get()`/ `size()`
    - `containsKey()`/ `containsValue()`
    - `keySet()`/ `values()`/ `entrySet()`
> 关于keySet()，返回的是一个包含所有key的set，任何对这个set的修改都会反映到原先的Map上，反之亦然，切记！！
- D: `remove()`/ `clear()`

## HashMap
- 最常用，最高效的Map实现
- 常见面试题：
  - HashMap的扩容（resize()方法，本质思路还是一样的，创建一个更大的HashMap）
  - HashMap的线程不安全性（使用ConcurentHashMap）
  - HashMap在Java7+后的改变： 链表 -> 红黑树
是为了防止大量对象的hashCode值一样导致性能下降改变的。
- HashMap和HashSet在本质上其实是一种东西
> HashMap的key就是一个HashSet

## 有序集合TreeSet和TreeMap
和LinkedHashSet不同的是，TreeSet会对内部的元素进行一次默认的排序，而不是仅仅按照插入的顺序存储。
另外TreeSet其内部结构是一个二叉树，可以把查找的算法复杂度从o(n)下降到o(logn)（和ArrayList对比）。

# Collections工具类的常用工具方法
- emptySet,emptyMap
- synchronizedCollection: 将一个集合变成线程安全的。
- unmodifiableCollection: 将一个集合变成不可变的（Guava的Immutable）。

# Collection的其他实现
- Queue/Deque
- Vector/Stack
- LinkedList
- ConcurrentHashMap
- PriorityQueue

# Guava
- Lists/Sets/Maps工具类
- ImmutableMap/ImmutableSet
- Multiset/Multimap(对应多个值的Set和Map)
- BiMap（双向的Map，可以从value对应到key）
