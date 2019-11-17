---
title: Python中的模块
date: 2018/06/09 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png
tags: 
- Python
categories: 
- Python
---
Python中的模块
<!--more-->


一个py文件就是一个模块

模块使用举例：
```
import os

print(os.__file__) # /usr/lib/python3.5/os.py

```

上面的`import os`就使用了一个内置模块`os`,通过`os.__file__`可以获得本地这个模块的路径

### 包管理工具pip

类似于Node中的`npm`,不多赘述，直接来看看使用。

直接`sudo pip3 install pygame`

安装好了模块之后，运行:
```
import os
import pygame

print(os.__file__) # /usr/lib/python3.5/os.py
print(pygame.__file__) # /usr/local/lib/python3.5/dist-packages/pygame/__init__.py

```


### 实现一个本地模块及模块的引用

先在当前目录下创建一个新的模块`testModule.py`

```
# testModule.py
def test1():
    print('----test1----')
```
对testModule模块进行引用

```
# import testModule

# testModule.test1() # ----test1----

from testModule import test1

test1()
```

有上述两种方式。

对于模块的查找，也是优先当前目录，然后才去系统的文件夹下寻找模块

## 模块中的`__name__`，`__all__`

### `__name__`

先来看`__name__`的用法：

修改testModule.py:

```
def test1():
    print('----test1----')


print(__name__) 
```

当通过`python3.5 testModule.py`时，输出`__main__`

我们现在在其他文件中引入这个testModule,然后调用test1():

```
import testModule

testModule.test1()
```

此时输出的`__name__`为testModule

所以:
- 当文件是被直接执行时，`__name__`的值为`__main__`
- 而当模块是被调用时，`__name__`为模块名

### `__all__`

`__all__`是针对`from moduleName import *`这种模块导入方式的：

当我们写下`from moduleName import *`，这个模块的所有全局变量，函数或者类，都可以被访问。为了自定义模块的输出，可以使用`__all__`来规定想输出的函数，变量或者类。

来看例子：
我们有如下模块
```

class Test(object):
    def sayHi(self):
        print('class Test')


def test1():
    print('---test1---')

num = 100
```

当在其他模块中引入时：
```
from testModule import *

t = Test()
t.sayHi()

test1()

print(num)
# 输出
# class Test
# ---test1---
# 100

```

所有的类，函数，全局变量都可以被访问。

现在我们在testModule.py中设置`__all__`：

```
__all__ = ['Test','test1']

class Test(object):
    def sayHi(self):
        print('class Test')


def test1():
    print('---test1---')

num = 100
```
我们在`__all__`中指定了想要暴露出来的东西，Test这个类和test1这个函数

此时运行结果为：
```
from testModule import *

t = Test()
t.sayHi() # class Test

test1() # ---test1---

print(num) # NameError: name 'num' is not defined

```

此时num就访问不到了，这就是`__all__`的作用。


### `__init__.py`

假设我们现在有sendMsg.py和recvMsg.py两个模块，因为这两个模块在功能上有一定的联系，所以我们可以创建一个新的文件夹，把这两个模块放进去，这个新的文件夹可以当做这2个模块的集合。

但是这样有个问题，需要我们在创建的文件夹中新创建一个`__init__.py`,否则py2中不允许导入(py3允许，但是也使用不了)

我们现在的文件结构是这样的：
```
├── Msg
│   ├── recvMsg.py
│   └── sendMsg.py
```

这个Msg就是我们创建好的集合，现在创建`__init__.py`

```
├── Msg
│   ├── __init__.py
│   ├── recvMsg.py
│   └── sendMsg.py
```

现在这个Msg就变成了一个包，然后我们可以在`__init__.py`中定义`__all__`变量来控制想暴露的模块：

```
# __init__.py
__all__ = ['sendMsg','recvMsg']
```

我们可以在外部：
```
from Msg import *

sendMsg.test1() # ----sendMsg----
recvMsg.test1() # ----recvMsg-----

```

另外，在导入Msg这个包的时候，会执行`__init__.py`这个文件，所以我们可以在内部引入包中的模块，比如sendMsg和recvMsg。

但是由于Python2和Python3在写法上有差异，在这给出一种通用的写法：
```
# __init__.py
# -*- coding:utf-8 -*-  

__all__ = ['sendMsg','recvMsg']

# import sendMsg,recvMsg # Py2写法 Py3报错
from . import sendMsg,recvMsg
```


### 模块的发布

先来准备一个文件夹，里面放入我们写好的包
```
.
├── prepare2Publish
│   ├── __init__.py
│   └── sendHello.py
└── setup.py

```
其中prepare2Publish就是我们想发布的包，我们要在setup.py中设置包的各种信息:
```
# setup.py

from distutils.core import setup

setup(
    name='DeeJay',
    version='1.0',
    description='a test module',
    author='DeeJay',
    py_modules=['prepare2Publish.sendHello']
)
```

然后要输入命令：`python3 setup.py build`来构建(`python setup.py build`为构建py2的包)：

build之后，现在目录结构为：

```
.
├── build
│   └── lib
│       └── prepare2Publish
│           ├── __init__.py
│           └── sendHello.py
├── prepare2Publish
│   ├── __init__.py
│   └── sendHello.py
└── setup.py
```
然后输入：`python3 setup.py sdist`进行压缩

压缩之后，目录结构为：

```
.
├── build
│   └── lib
│       └── prepare2Publish
│           ├── __init__.py
│           └── sendHello.py
├── dist
│   └── DeeJay-1.0.tar.gz
├── MANIFEST
├── prepare2Publish
│   ├── __init__.py
│   └── sendHello.py
└── setup.py

```

打成的这个压缩包就是我们最终的模块，可以拿来给别人使用。

解压之后可以通过`python3 setup.py install`就可以安装到本地的系统包里

另外如果我们想发布到pip官网上，可以使用`twine`，在~路径下配置好自己的`.pypirc`， 就可以将自己的压缩包发布到pip官网了
### import时模块文件的查询路径  `sys.path`

```

import sys

print( sys.path )

# 输出：
[
    '/home/deejay/learn-python', 
    '/usr/lib/python35.zip', 
    '/usr/lib/python3.5', 
    '/usr/lib/python3.5/plat-x86_64-linux-gnu', 
    '/usr/lib/python3.5/lib-dynload', 
    '/home/deejay/.local/lib/python3.5/site-packages', 
    '/usr/local/lib/python3.5/dist-packages', 
    '/usr/lib/python3/dist-packages'
]
```

sys.path是一个列表，其中每一个元素都是一个路径，第一个元素为当前目录下的路径

当我们导入一个模块的时候，程序会依次从`sys.path`中去寻找，如果最后没找到就抛出异常

此外我们可以通过给`sys.path` `append()`一个自定义的路径，让程序去指定目录寻找模块

例如:
```
    sys.path.append('/home')
```

### 模块的重新导入

重新导入指的是，在当前程序中导入了一个目标模块，然后在程序**没有退出的情况下**，修改了目标模块的代码。

此时在当前程序中，是不会更新模块的，就算再次`import`也无法更新.

这个时候，我们可以借助`imp`模块中的`reload()`方法来进行不退出程序并且可以更新模块的操作：

```
from imp import *

reload(targetModule)
```


### 模块的循环导入

循环导入即： 在a模块中导入b，同时在b模块中导入a。

```
# a.py

import b

def test():
    print('----a----')
    b.test()

test()
```

```
# b.py

import a

def test():
    print('----b----')
    a.test()

test()
```


对于这种情况，一个比较好的解决方法是：通过一个主模块来引入所有子模块，子模块中避免相互引入：

比如我们现在新增一个main.py:

```

# main.py

import a
import b

a.test()
b.test()
```
