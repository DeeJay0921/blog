---
title: Java中的泛型以及常见数据结构
date: 2018/08/11 00:00:01
cover: https://revistadigital.inesem.es/informatica-y-tics/files/2015/10/inesem-java-1024x768.jpg
tags: 
- Java
- 泛型
categories: 
- Java
---
Java中的泛型以及常见数据结构
<!--more-->

####集合的体系结构形成的原因：
  由于不同的数据结构，所以Java提供了不同的集合，但是不同的集合他们的功能都是相似的，向上提取共性，这就是集合体系结构形成的原因。

#### 体系结构
从最顶层开始学习，因为其包含了所有的共性。使用从最底层开始使用，因为最底层才是最具体的实现。


来看集合体系结构中的最顶层 Collection：

##Collection
几个共性的API

`boolean add(E e)`

`boolean contains(Object o)`

`boolean isEmpty()`

`boolean remove(Object o)`

`int size()`

`Object[] toArray()`

`Iterator<E> iterator()` // 返回一个迭代器对象
##Interface Iterator<E>

用于迭代Collection

`boolean hasNext()` 如果有下一项，返回true

`E next()` 返回下一个元素，如果迭代完成继续迭代，会抛出异常

```
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class IteratorDemo {
	public static void main(String[] args) {
		Collection c = new ArrayList();
		
		for(int i = 0 ; i < 10; i ++) {
			c.add(i);
		}
		
		Iterator it = c.iterator(); // 返回一个迭代器对象
		
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
}
```

关于迭代器，迭代器是依赖于集合的，相当于集合的一个副本。

当在迭代器在进行操作的时候，如果对集合进行改动，造成**迭代器和集合不一样的时候，迭代器会抛出异常。**

解决方案：
- 不使用迭代器
- 改动集合时，不对集合直接操作，而是操作迭代器
对于这种情况，我们不能使用Collectionl来创建集合对象，而是要使用List,因为要使用List中的`ListIterator <E>ListIterator()`中的方法来进行操作Iterator，不然Iterator中是没有操作方法的，只有hasNext() next() remove()

```
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class IteratorDemo {
	public static void main(String[] args) {
		Collection<String> c = new ArrayList<String>();
		c.add("Hello");
		c.add("java");
		
		Iterator<String> it = c.iterator();
		while(it.hasNext()) {
			if(it.next().equals("java")) {
				c.add("world"); // java.util.ConcurrentModificationException
			}
		}
		System.out.println(c);
	}
}

```
如上例所示，抛出了并发处理异常。

要使用迭代器完成对集合的改变，就不能使用Iterator<E>,因为其没有add方法，要使用ListIterator<E>。 要使用ListIterator，就得使用Collection的子体系结构List的ListIterator()方法来得到一个迭代器对象。具体来看例子：
```
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class IteratorDemo {
	public static void main(String[] args) {
//		Collection<String> c = new ArrayList<String>();
//		c.add("Hello");
//		c.add("java");
		// 使用List 而不是 Collection
		List<String> li = new ArrayList<String>();
		li.add("Hello");
		li.add("java");
		
//		Iterator<String> it = c.iterator();
		ListIterator<String> listIt = li.listIterator(); // ListIterator 具有add方法
//		while(it.hasNext()) {
//			if(it.next().equals("java")) {
//				c.add("world"); // java.util.ConcurrentModificationException
//			}
//		}
		while(listIt.hasNext()) {
			if(listIt.next().equals("java")) {
				listIt.add("world"); // 这样就可以操作迭代器而不是直接操作集合  就不会抛出异常
			}
		}
		System.out.println(li); // [Hello, java, world] 正常输出

	}
}
```

#### 集合的遍历方式

1. `toArray()`,将集合转换为数组，遍历数组
2. `iterator()`,可以返回一个迭代器对象，可以通过迭代器对象来迭代集合
```
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class IteratorDemo {
	public static void main(String[] args) {
		Collection c = new ArrayList();
		
		for(int i = 0 ; i < 10; i ++) {
			c.add(i);
		}
		// method1();
		// method2();
	}
	public void method1(Collection c) { // 使用Iterator
		Iterator it = c.iterator(); // 返回一个迭代器对象
		
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	public void method2(Collection c) { // 使用toArray()
		Object[] objArr = c.toArray();
		for(int i = 0 ; i < objArr.length; i ++) {
			System.out.println(objArr[i]);
		}
	}
}
```

### 泛型的概述和体现

由于集合是可以存储任意类型的对象的，所以当存储了不同类型的对象时，就有可能在进行转换时出现类型转换类型异常`ClassCastException`。所以Java提供了泛型。


泛型：是一种广泛的类型，将明确数据类型的工作提前到了编译时期，借鉴了数组的特点。

泛型的优点：
- 避免了类型转换的问题
- 简化代码书写

使用泛型的场景：
- 带<E>的api都可以使用泛型

来看一个简单的例子：
```
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public class GenericDemo {
	public static void main(String[] args) {
		Person p1 = new Person("zhangsan",18);
		Person p2 = new Person("lisi",20);
		
		Collection<Person> c = new ArrayList<Person>(); // 使用泛型 表明集合中存储Person类型的对象
		c.add(p1);
		c.add(p2);
		
		Iterator<Person> it = c.iterator(); // 使用泛型  表明迭代器中的类型也是Person类
		while(it.hasNext()) {
			it.next().show(); // 注意使用了泛型之后  这边的it.next() 返回的已经是Person类的对象了  直接直接调用方法。
		}
    }
}

class Person {
	String name;
	int age;
	public void show() {
		System.out.println(name + "---" + age);
	}
}
```

### foreach()

增强for循环，一般用于遍历集合或者数组

语法: 
```
for(Type var : CollectionOrArray) {
    // 可以直接使用var
}
```

来看一个例子：

```
Collection<String> c = new ArrayList<String>();
c.add("hello");
c.add("java");

for(String str : c) {
    System.out.print(str.toUpperCase()); // HELLO JAVA
}
```

注意点： 在增强for循环中不可以修改集合，否则也会出现并发修改异常，因为其底层结构就是迭代器。


# 常见数据结构：

### 数组：
`int[] arr = {1,2,3,4,5};`

数组的特点：
- 长度一旦定义，则不可改变
- 元素都有整数索引
- 只能存储同一类型的元素
- 既能存储基本类型也能存储引用类型。

一些问题：
- 如何获取元素3：
    - `arr[2]`
- 在元素3后面添加一个新的元素8：
    - 创建一个新的数组，长度为原数组+1，遍历原数组，以此插入新数组，到3时，再后面插入8


可以看到，数组**查找快**，但是**增删慢**

### 链表：
由链连接起来的一堆结点

结点分为三部分：
1. 地址值
2. 值
3. 下一个结点的地址值

假设现在有一个链表：
按照地址值，值，下一个地址值来表示：

0x0011 | 1 | 0x0022
-- | -- |--

0x0022 | 2 | 0x0033
-- | -- |--

0x0033 | 3 | 0x0044
-- | -- |--

0x0044 | 4 | 0x0055
-- | -- |--

0x0055 | 5 | 最后一个结点
-- | -- |--



一些问题：
- 如何获取结点3
    - 只能遍历链表，一个个查看，到第3个
- 要在结点2后面添加一个结点8，怎么操作：
    - 把结点2的下一个地址值修改为结点8的地址值，然后把结点8的地址值改为结点3的地址值
- 如何删除结点4
    - 直接把结点3的下一个地址值改为结点5即可

可以看到： 链表**查找慢，增删快**

### 栈

java中运行方法就会进栈，运行完后就会出栈。

特点：**先进后出**

跟弹夹差不多，先压进去的子弹最后被射出来。

### 队列
特点： **先进先出**

类比现实生活中排队

## java.util.List

List是Collection的子体系结构

并发修改异常处理中就使用过List的ListIterator()方法。简单介绍一下：



特点：
- 有序的
- 有整数索引
- 允许重复

List由于具有索引，所以相比于Collection具有一些特有功能：
- `void add(int index, E element)`
- `E get(int index)`
- `E remove(int index)`
- `E set(int index, E element)`
即增删改查

```
package ListDemo;

import java.util.ArrayList;
import java.util.List;

public class ListDemo {
	public static void main(String[] args) {
		List<String> li = new ArrayList<String>();
		
		// void add(int index, E element)
		li.add("Hello");
		li.add(0,"world");
		li.add(0,"java");
		System.out.println(li); // [java, world, Hello] 每次指定索引时，之前的元素向后移动
		
		
		// E get(int index)
		System.out.println(li.get(2)); // Hello
		
		// E remove(int index)
		System.out.println(li.remove(2)); // Hello
		System.out.println(li); // [java, world]
		
		// E set(int index, E element) 
		System.out.println(li.set(0,"Python")); // java
		System.out.println(li); // [Python, world]
	}
}

```


#### 关于java.util.List常见的子类
一般常见的子类有俩：
- `ArrayList`
    - 底层是数组结构，查询快，增删慢
- `LinkedList`
    - 底层是链表结构，查询慢，增删快

根据使用场景来使用不同的子类，一般不确定使用啥就使用ArrayList

### java.util.LinkedList

链表结构常见的几个api：
- `void addFirst(E e)`
- `void addLast(E e)`
- `E getFirst()`
- `E getLast()`
- `E removeFirst()`
- `E removeLast()`

```
package ListDemo;

import java.util.LinkedList;

public class LinkedListDemo {
	public static void main(String[] args) {
		LinkedList<Integer> li = new LinkedList<Integer>();
		
		for (int i = 0; i < 10; i++) {
			li.add(i);
		}
		
		System.out.println(li); // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		
		// void addFirst(E e)
		li.addFirst(-1);
		System.out.println(li); // [-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		
		// void addLast(E e)
		li.addLast(10);
		System.out.println(li); // [-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

		// E getFirst() E getLast()
		System.out.println(li.getFirst());// -1
		System.out.println(li.getLast()); // 10
		
		// E removeFirst() E removeLast()
		System.out.println(li.removeFirst()); // -1
		System.out.println(li); // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		System.out.println(li.removeLast()); // 10
		System.out.println(li); //[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

	}
}

```
