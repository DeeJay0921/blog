---
title: Python中关于字典的一些API
date: 2018/06/10 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中关于字典的一些API
<!--more-->

# Python中关于字典的一些API

### clear()

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

obj.clear()

print(obj) # {}
```

### pop(key) popitem()

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

popedGender = obj.pop('gender')

print(popedGender) # male

print(obj) # {'name': 'DeeJay', 'age': 22}

```

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

popItem = obj.popitem() # popitem()不接受参数

print(popItem) # ('gender', 'male')

print(obj) # {'name': 'DeeJay', 'age': 22}
 
```

### update(dict2)

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

obj2 = {
    'hobby': 'game'
}

obj.update(obj2)

print(obj) # {'name': 'DeeJay', 'age': 22, 'gender': 'male', 'hobby': 'game'}

print(obj2) # {'hobby': 'game'}
```

### copy()

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

obj2 = obj.copy() 

print(obj2) # {'name': 'DeeJay', 'age': 22, 'gender': 'male'}


```

对于`copy()`的拷贝，该API返回一个字典的*浅拷贝*，关于如何深拷贝，以后再进行介绍。

```
obj = {
    'name': 'DeeJay',
    'hobby': {
        'game': 'R6',
        'singer': 'krewella'
    }
}

obj2 = obj.copy() 

obj['name'] = 'deejay'

print(obj2) # {'name': 'DeeJay', 'hobby': {'game': 'R6', 'singer': 'krewella'}}

# 浅拷贝第一层是不同的引用，所有obj第一次改变，obj2并没有发生改变。

obj['hobby']['singer'] = 'TaylorSwift'

print(obj2) # {'name': 'DeeJay', 'hobby': {'game': 'R6', 'singer': 'TaylorSwift'}}

# 第二层就是相同的引用，所以obj改变，obj2也跟着变化了。
```

### values(),keys(),items()

```
obj = {
    'name': 'DeeJay',
    'age': 22,
    'gender': 'male'
}

values = obj.values()

keys = obj.keys()

items = obj.items()

print(values) # dict_values(['DeeJay', 22, 'male'])

print(keys) # dict_keys(['name', 'age', 'gender'])

print(items) # dict_items([('name', 'DeeJay'), ('age', 22), ('gender', 'male')])

```

*对于这三个方法，如果是Python2的话返回的都是列表*

### fromkeys(iterable,[defaultValue])

```
obj = dict.fromkeys(['key1','key2','key3'],'default')

print(obj) # {'key1': 'default', 'key2': 'default', 'key3': 'default'}

# fromkeys() 接受1-2个参数，第一个参数是一个可以迭代的对象，比如str,dict,tuple，list。
# 第二个参数(可选)为一个给定的默认value,不指定的话为None
# 最后返回的是一个新的dict，key为迭代的key，value为第二参数指定值
```

### setdefault(key,defaultValue)

`setdefault()`和`get()`类似，但是不同的地方在于，如果`setdefault()`方法没有找到指定的key，那么会相应的会创建一个key,其value为指定的第二参数，如果没指定即为`None`

```
obj = {}

obj.get('qq')

print(obj) # {}

obj.setdefault('qq',10086)

print(obj) # {'qq': 10086}

obj.setdefault('wechat')

print(obj) # {'qq': 10086, 'wechat': None}

```
