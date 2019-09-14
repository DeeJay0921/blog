---
title: Python中的set类型-以及-数组去重问题
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中的set类型-以及-数组去重问题
<!--more-->

Pyhton还有一种新的类型叫set, set中不可以有相同的元素：

```
li = [1,2,3,1,2,3]

tu = (1,2,3,1,2,3)

se = {1,2,3,1,2,3}

print(se) # {1,2,3}
```

## set的基本操作

- `add(element)`

```
name = {'Dee','Jay'}

name.add('Dee')
print(name) # {'Dee', 'Jay'}

name.add('Yang')
print(name) # {'Yang', 'Jay', 'Dee'}

```

- `clear()`

```
name = {'Dee','Jay'}

name.clear()

print(name) # set()
```

- `remove(element)` `discard(element)`

二者都是删除，remove不存在的元素会抛出异常，但是discard不会
```
name = {'a','s'}


# name.remove('as') # KeyError: 'as'

name.discard('ass') 
print(name) # {'a', 's'}

```

- `pop()`

```
name = {'a','s'}

pop_elment = name.pop()

print(pop_elment) # a
print(name) # {'s'}
```

注意这个`pop()`只是**随机**删除一个元素值。

但是对于**同一set,随机删除的这个元素是固定的**

- `issubset(set)` `issuperset(set)`
用来测试两个集合是否包含，

```
n1 = {1,2,3}
n2 = {1,2,3,4}

print( n1.issubset(n2) ) # True

print( n2.issuperset(n2) ) # True
```

- `set1.union(set2)`

返回一个包含2个集合中所有元素的集合
```
n1 = {1,2,3}
n2 = {4,5,6}

print( n1.union(n2) ) # {1, 2, 3, 4, 5, 6}
```

- `set1.intersection(set2)`

返回的是set1和set2的交集

```
n1 = {1,2,3}

n2 = {1,2,4}

print( n1.intersection(n2) ) # {1,2}
```

- `s1.difference(s2`)

返回包含s1中有，但是s2没有的元素的集合。

```
n1 = {1,2,3}

n2 = {1,2,4}

print( n1.difference(n2) ) # {3}
```

- `s1.symmetric_difference(s2`)

返回包含s1和s2中不相同的元素的集合。

```
n1 = {1,2,3}

n2 = {1,2,4}

print( n1.symmetric_difference(n2) ) # {3,4}
```


## 对于数组去重的思考

- 第一种自然是使用set:

```
li = [1,1,2,3,23,234,13]

result = list(set(li))

print(result) # [1, 2, 3, 234, 13, 23]

```

转换为set再转换为list即可。

- 第二种：

```
li = [1,1,2,3,23,234,13]

result = []

for i in li:
    if i not in result:
        result.append(i)

print(result) # [1, 2, 3, 23, 234, 13]

```

只能说Python真的太简洁了
