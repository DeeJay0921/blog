---
title: Python中的真假值
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中的真假值
<!--more-->

动态转换为False的值有： '', None , 0 , () ,[] , {} .

```
def FalseValue():
    if '':
        print('test')
    elif None:
        print('test')
    elif 0:
        print('test')
    elif():
        print('test')
    elif[]:
        print('test')
    elif{}:
        print('test')
    else:
        print('上述值全部动态转换为False')

FalseValue() # 上述值全部动态转换为False
```

而被转化为True的值有:非零数字，非空字符串，非空元祖，列表以及字典。

```
def TrueValue():
    if 1:
        print('True')

    if -1:
        print("True")

    if 'notEmptyString':
        print('True')
    if ('notEmptyTuple'):
        print('True')

    if ['notEmptyList']:
        print('True')

    if {'info': 'notEmpty'}:
        print('True')

TrueValue()
# 输出：
# True
# True
# True
# True
# True
# True
```
