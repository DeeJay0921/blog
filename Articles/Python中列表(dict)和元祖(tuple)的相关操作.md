---
title: Python中列表(dict)和元祖(tuple)的相关操作
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中列表(dict)和元祖(tuple)的相关操作
<!--more-->

# 列表(list)：

## 增删改查

有指定列表`lang = ['JavaScript', 'Python', 'Ruby']`

### 插入操作

- **append(element)**  类似于JS中的`push()`

```
lang = ['JavaScript', 'Python', 'Ruby']

lang.append('Java')

print(lang) # ['JavaScript', 'Python', 'Ruby', 'Java']

```

- **insert(index,element)**

```
lang = ['JavaScript', 'Python', 'Ruby']

lang.insert(0,'Java')

print(lang) # ['Java', 'JavaScript', 'Python', 'Ruby']

lang.insert(len(lang), 'PHP')

print(lang) # ['Java', 'JavaScript', 'Python', 'Ruby', 'PHP']

```

- 列表合并 列表相加或者使用`list1.extend(list2)`

```
lang = ['JavaScript', 'Python', 'Ruby']

Clang = ['c','c#','c++']

collection = lang + Clang

print(collection) # ['JavaScript', 'Python', 'Ruby', 'c', 'c#', 'c++']

```
使用`extend()`

```
lang = ['JavaScript', 'Python', 'Ruby']

Clang = ['c','c#','c++']
 
lang.extend(Clang)

print(lang) # ['JavaScript', 'Python', 'Ruby', 'c', 'c#', 'c++']

# 注意这边lang列表已经改变了

```

### 删除操作

- **pop()**

```
lang = ['JavaScript', 'Python', 'Ruby']

popedElement = lang.pop()

print(popedElement) # Ruby

print(lang) # ['JavaScript', 'Python'] 直接改变原列表lang

```

- **remove(element)**

```
lang = ['JavaScript', 'Python', 'Ruby']

lang.remove('Python')

print(lang) # ['JavaScript', 'Ruby']

```

关于`remove(element)`,从第一个元素开始找，找到匹配的元素就进行删除，如果后面还有相同的元素，**不继续进行删除**

 - **del list[index]**

列表的切片操作同字符串，不再赘述。

```
lang = ['JavaScript', 'Python', 'Ruby']

del lang[1]

print(lang) # ['JavaScript', 'Ruby']

```

### 修改操作    
- **list[index] = newVal**


```
lang = ['JavaScript', 'Python', 'Ruby']

lang[2] = 'Java'

print(lang) # ['JavaScript', 'Python', 'Java']

```

### 查询操作 in / not in

- **element in list** / **element not in list**

```
lang = ['JavaScript', 'Python', 'Ruby']

if 'Ruby' in lang:
    print('bingo!') # bingo!

```

```
lang = ['JavaScript', 'Python', 'Ruby']

if 'Java' not in lang:
    print('no Java')  # no Java
 
```
# 元祖(tuple)

### 简介

```
li = [1,2,3,4]

print( type(li) ) # <class 'list'>

tu = (1,2,3,4)

print( type(tu) ) # <class 'tuple'>

li[0] = 0

print(li) # [0, 2, 3, 4]

tu[0] = 0

print(tu) # TypeError: 'tuple' object does not support item assignment
```

tuple和list类似，但是*不支持修改*,一旦定义，只支持查询操作。

同时tuple也内置了`count()`和`index()`,用法也类似：

```
tu = (1,1,1,2,3)

print( tu.count(1) ) # 3

print( tu.index(3) ) # 4
```
