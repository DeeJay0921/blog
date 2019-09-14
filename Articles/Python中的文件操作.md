---
title: Python中的文件操作
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中的文件操作
<!--more-->

# Python中的文件操作

## 文件的打开与关闭

一般操作文件的流程都很简单：
- 打开/新建 一个文件
- 读/写 文件
- 关闭文件

### 打开文件

使用`open()`函数来打开/新建一个文件

示例：`file = open('test.py','w')`

file表示`open()`的返回值，即已经访问到的文件

第二个选项,'w'表示只能写，'r'表示只能读，'a'表示追加等等，具体见：

访问模式 | 含义
-- | --
r | 只读方式打开文件
w | 只写方式打开文件
a | 追加方式打开/新建一个文件  如果已有内容，新内容追加在文件内容末尾
rb | 默认模式，以二进制的格式只读的打开一个文件  文件指针在文件开头
wb | 二进制只写访问文件  如果之前有内容则覆盖  文件不存在则先创建
ab | 同理，二进制追加
r+ | 打开一个文件用于读写，文件指针放在文件开头
w+ | 打开一个文件用于读写，文件存在则覆盖，不存在则先创建
a+ | 打开一个文件用于读写  文件内容存在则新内容追加到末尾  文件指针在文件末尾
rb+ | 二进制打开文件用于读写 文件指针在开头
wb+ | 二进制打开文件用于读写 有覆盖 没有先新建
ab+ | 二进制打开文件用于读写  指针在末尾 有则追加 没有先新建

### 关闭文件 file.close()

```
f = open('test.py','w+') # 打开文件 f为open()返回值即打开的文件
f.close() # 关闭文件 
```

### 文件的读写 read() write()

```
f = open('test.js','r')
con = f.read() # read()方法表示一次性把目标文件全部读完
print(con) # console.log('test.js')
f.close()

# 如果给read()传递了参数， 那么read(n)可以读到指定n长度的内容
# 可以一直调用read(n)直到读完目标文件 之后read()返回空字符串
```

使用`read()`一次性读完文件的内容之后，再次调用`read()`返回的是空字符串。
```
f = open('test.js','r')

while True:
    content = f.read(1) # read(n)即一次读取n字节长度的内容
    print(content)

    if len(content) == 0: # 等到读取完后 read()返回的空字符串，可以跳出循环
        break

```

```
f = open('test.js','a+')

length = f.write('\nconsole.log("hello world")') 

print(length) # 27
# write()函数返回的是写入的字符长度

f.close()

g = open('test.js','r')

print(g.read()) # 输出为：
# console.log('test.js')
# console.log("hello world")

g.close()
```
可以利用`read()`和`write()`来进行文件的复制：
```
f = open('test.js','r')
content = f.read()
f.close()

g = open('test_backup.js','w')
g.write(content)
g.close()
```

其实还有其他读文件的方法：

- `readlines()`:
```
f = open('test.js','r')

content = f.readlines()

print(content) # ["console.log('test.js')\n", 'console.log("hello world")']

```

- `readline()`
```
f = open('test.js','r')

line1 = f.readline()
line2 = f.readline()
line3 = f.readline()

print(line1)  # console.log('test.js')\n
print(line2) # console.log("hello world")
print(line3) # ''  空字符串
```

`readline()`是按照行来一行一行读取，而`readlines()`是连续调用`readline()`然后将结果组成一个list返回。


### 对于大文件的读写

对于大文件，肯定不能使用`read()`一下子都读到内存里。

我们可以使用合适的字节数，每次读取一点点，示例：
```
f = open('test.js','r')

while True:
    content = f.read(1024) # 假如一次读取1024字节
    print(content)

    if len(content) == 0:
        break

```

### 文件的定位

- 获取当前的位置 `tell()`
在读写文件的过程中，可以使用`tell()`来获取当前的位置
```
f = open('test.js','r')

f.read(5)

position = f.tell()
print(position) # 5

f.read(5)

position2 = f.tell()
print(position2) # 10
```

- 定位到文件的某个位置 `seek(offset,from)`
如果在读写文件的过程中，需要从另外一个位置进行操作的话，可以使用`seek()`

`seek(offset,from)`:
    - offset:偏移量
    - from：方向(0表示文件开头，1表示当前位置，2表示文件末尾)


```
# 假设我们现在从test.js的第二行开始读,即离文件开头23字节的地方开始读
f = open('test.js')

f.seek(23,0) # 23表示offset,0表示从文件开头开始

print(f.read()) # console.log("hello world")

```

关于`seek()`,我们在`read()`方法读完文件后，如果还想在读一遍，可以使用`seek(0,0)`将文件指针重新设为开头，再调用`read()`就又可以读取一遍。


### 文件的其他相关操作

还有一些文件的常规操作介绍一下：

- 文件重命名
`os`模块的`rename(old_file_name,new_file_name)`

```
import os

os.rename('test_backup.js','test_rename.js')
```
- 删除文件
`os`模块的`remove(file_name)`

```
import os

os.remove('test_rename.js')
```
- 创建文件夹
`os.mkdir(dir_name)`
- 获取当前目录
`os.getcwd()`
```
import os

print(os.getcwd()) # /home/deejay/learn-python

```
- 改变默认目录
`os.chdir()`
```
import os

print(os.getcwd()) # /home/deejay/learn-python

os.chdir('/usr/bin')

print(os.getcwd()) # /usr/bin

```
- 获取目录列表
`os.listdir('./')`改方法返回的是一个list,每个元素都是`ls`命令下的一个文件
- 删除文件夹
`os.rmdir(dir_name)`
