---
title: Java中的Set和Map集合
date: 2018/07/19 00:00:01
tags: 
- Java
- 集合
- Collection
- Set
- Map
categories: 
- Java
---
Java中的Set和Map集合
<!--more-->

###Set
Set作为Collection的子体系，有一些特点：
- Set是无序的，存储和读取的顺序有可能不一样
- 不允许重复，要求每个元素是唯一的
- 无序所以没有索引


其中Set中的`add()`方法，在添加的时候,先比较元素的哈希值,如果和已有的元素相同则不添加，如果哈希值不一样，则再进行比较`==`或者`equals()`方法来进行比较，如果有一个为true即重复，则也不添加。

##### HashSet存储自定义类型数据并实现去重

```
package SetAndMapDemo;

import java.util.HashSet;

public class HashSetDemo1 {
	public static void main(String[] args) {
		// 有自定义的Stu类
		Stu s1 = new Stu("Yang", 23);
		Stu s2 = new Stu("DeeJay", 22);
		Stu s3 = new Stu("DeeJay", 22);
		// 创建HashSet对象
		HashSet<Stu> hs = new HashSet<Stu>();
		hs.add(s1);
		hs.add(s2);
		hs.add(s3);
		
		System.out.println(hs); // [Stu [name=DeeJay, age=22], Stu [name=DeeJay, age=22], Stu [name=Yang, age=23]]
		
		// 可以看到s2和s3是重复的，但是也被加进了HashSet中
		// 造成这种情况的原因是因为Set中add()方法的特殊性
	}
}

class Stu {
	String name;
	int age;
	
	public Stu(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Stu [name=" + name + ", age=" + age + "]";
	}
	
	
}

```

出现这种情况的原因是add()方法的特殊性：
其中Set中的`add()`方法，在添加的时候,先比较元素的哈希值,如果和已有的元素相同则不添加，如果哈希值不一样，则再进行比较`==`或者`equals()`方法来进行比较，如果有一个为true即重复，则也不添加。


如果要避免这种情况，需要重写一下equals方法:
```
package SetAndMapDemo;

import java.util.HashSet;

public class HashSetDemo1 {
	public static void main(String[] args) {
		// 有自定义的Stu类
		Stu s1 = new Stu("Yang", 23);
		Stu s2 = new Stu("DeeJay", 22);
		Stu s3 = new Stu("DeeJay", 22);
		// 创建HashSet对象
		HashSet<Stu> hs = new HashSet<Stu>();
		hs.add(s1);
		hs.add(s2);
		hs.add(s3);
		
		System.out.println(hs); // [Stu [name=DeeJay, age=22], Stu [name=DeeJay, age=22], Stu [name=Yang, age=23]]
		
		// 可以看到s2和s3是重复的，但是也被加进了HashSet中
		// 造成这种情况的原因是因为Set中add()方法的特殊性
	}
}

class Stu {
	String name;
	int age;
	
	public Stu(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Stu [name=" + name + ", age=" + age + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		Stu other = (Stu)obj; // 先做类型转换  否则拿不到子类型的对象的成员
		if(other.name.equals(this.name) && other.age == this.age) {
			return true;
		}else {
			return false;
		}
	}


}

```

重写了equals方法之后，还是可以重复添加，这是因为add()方法在进行添加时，是先进行hashCode的比较，如果hash值一样就不会进行equals的比较。

所以一般有约定：**重写了equals()，一定也要重写相应的hashCode()!!!**

所以我们还需要重写hashCode():
```
package SetAndMapDemo;

import java.util.HashSet;

public class HashSetDemo1 {
	public static void main(String[] args) {
		// 有自定义的Stu类
		Stu s1 = new Stu("Yang", 23);
		Stu s2 = new Stu("DeeJay", 22);
		Stu s3 = new Stu("DeeJay", 22);
		// 创建HashSet对象
		HashSet<Stu> hs = new HashSet<Stu>();
		hs.add(s1);
		hs.add(s2);
		hs.add(s3);
		
		System.out.println(hs); // [Stu [name=Yang, age=23], Stu [name=DeeJay, age=22]]
		
		// 重写了equals() 和 hashCode() 之后    做到了自定义类的不重复添加
	}
}

class Stu {
	String name;
	int age;
	
	public Stu(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "Stu [name=" + name + ", age=" + age + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		Stu other = (Stu)obj; // 先做类型转换  否则拿不到子类型的对象的成员
		if(other.name.equals(this.name) && other.age == this.age) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return -1; // 直接给所有的元素都返回-1的hash值
	}
}

```


##### 关于上例重写hashCode()和equals()的优化

上述例子中重写equals()和hashCode()值只是做一个实例，可以进一步优化重写的方法


hashCode():
```
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
```


equals():
```
	@Override
	public boolean equals(Object obj) {
		// 判断是否同一元素
		if (this == obj)
			return true;
		// 判断是否为null
		if (obj == null)
			return false;
		// 判断运行时类是否一直
		if (getClass() != obj.getClass())
			return false;
		Stu other = (Stu) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null) // name为String,是引用类型，有可能为null
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
```


### Collections 工具类

先来看Collection和Collections的区别:

Collection是集合体系的最顶层,包含了所有集合的共性.Interface Collection<E>
Collections则是个工具类.Class Collections

Collections工具类的方法,就是为了操作Colleciton.

`static int binarySearch(List list, T key)`二分查找
`static void copy(List dest, List src)` 把源列表中的数据覆盖到目标列表,注意目标列表dest的长度要大于等于源列表的长度
`static void fill(List list, Object obj)` 使用制定的元素填充制定的列表
`static void reverse(List list)`
`static void shuffle(List list)`随机置换
`static void sort(List<T> list)` 按照列表中元素的自然顺序进行按升序进行排序
`static void sort(List<T> list, Comparator<? super T> c)`
`static void swap(List list, int i, int j)` 指定索引的元素进行互换


```
code
```

### Map

java.util.Map

接口Map结构用来存储键值对

#### Map和Collection的区别：
- Map不是Collection的子结构
- Map是一个双列集合，常用处理有对应关系的数据
- Collection是一个单列集合，下面有不同的子体系，有的有索引有的没索引，有的允许重复有的不允许重复

####Map的常用功能


`int size()`
`void clear()`
`boolean isEmpty()`
`V remove(Object key)`
`boolean containsKey(Object key)`
`boolean containsValue(Object value)`
`Set<Map.Entry<K,V>> entrySet()`
`V get(Object key)`
`Set<K> keySet()`
`V put(K key, V value)`
`Collection<V> values()`

```
code
put 添加及修改  返回值为null和value

```

```
code

Set<K> keySet()
Collection<V> values()
```

```
code 
遍历Map
1.获取所有的Key，遍历获得所有的values,  keySet() values()
2.Set<Map.Entry<K,V>> entrySet()

```

#### 使用HashMap存储数据并进行遍历

- 第一种情况，String作为Key：
```
code
```
- 第二种情况，自定义类型作为Key：
```
code
类的定义
```

```
code
自定义相同成员的类添加时可以重复添加
```
