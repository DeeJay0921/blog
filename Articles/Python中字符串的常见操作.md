---
title: Python中字符串的常见操作
date: 2018/06/09 00:00:01
tags: 
- Python
categories: 
- Python
---
Python中字符串的常见操作
<!--more-->

## 字符串切片和逆序问题

先来了解切片操作:

```
myStr = 'hello world'

print(myStr[0:3])  # 输出为hel

```
myStr[0:3] 0表示切片开始位置，3表示切到下标为3的位置就停止 但是**不包含myStr[3]**

然后我们在切片的时候，可以传入**第三个参数来表示每次切片的间隔**,

```
myStr = 'abcdefg'

print(myStr[0:4:1]) # abcd  等价于myStr[0:4]

print(myStr[0:4:2]) # ac
```

第三个参数代表每次取到字符之后，间隔指定值之后再去取下一个字符，不传的话，默认就是为1。

此外，第三个参数，如果为正，那么代表从下标0开始取，往后取到下标为-1的字符（即字符串最后一项)，**如果为负，代表从后往前取，即从下标-1到下标0这个方向开始切片,来看个例子

```
myStr = 'abcdefg'

print(myStr[0:4:1]) # abcd

print(myStr[0:4:-1]) # 输出为空

print(myStr[4:0:-1]) # ebcd

```
来分析myStr[0:4:-1]为何是空，指定第三个参数为-1之后，是从**右往左**开始取的，开始下标为4，截止下标为0，那么自然就取不到字符了，需要修改起始位置和截止位置。

那么现在我们得到了一个逆序的字符串，值得注意的是，输出的是：`ebcd`,因为我们截止的下标是0，那么切片就取不到第一个值，我们需要修改一下截止的下标。

有了上述知识，我们就可以实现字符的逆序操作：

```
myStr = 'abcdefg'

print(myStr[-1:0:-1]) # gfedcb

# 截止下标为0，所以取不到myStr[0],需要修改为：

print(myStr[-1::-1]) # gfedcba

# 删掉指定的截止下标之后，切片就会一直截取到整个字符串结束，我们就实现了逆序的操作

# 那么有了上面的思路，我们想到，其实开始的下标也是可以省略的，只要指定了截取的方向(即第三个参数)

print(myStr[::]) # abcdefg 等价于myStr[::1]

print(myStr[::-1]) # gfedcba
```

## 字符串常用api:

介绍一些Python中常用的操作字符串的api


- **find**  检测str是否包含在myStr中，如果包含返回**开始的下标**，不包含返回-1

```
myStr = 'Hello'

print(myStr.find('He')) # 0

print(myStr.find('llo')) # 2

print(mtStr.find('none')) # -1
```

`find()`默认是从左边开始查找，想从右边开始查找的话，可以使用`rfind()`

相似的api还有`index()`以及`rindex()`,不同的是,index如果没找到指定字符串的话会直接产生异常。

- **count** 返回str在指定start和end之间出现了多少次

```
myStr = 'Hello world world'

print(myStr.count('world')) # 2

print(myStr.count('world',0,5)) # 0

```

- **replace** 把myStr中的str1替换成str2,如果count 指定，则替换不超过count次

```
myStr = 'hello world world'

print(myStr.replace('world','WORLD')) # hello WOLRD WORLD  

print(myStr) # hello world world  原字符串并不会改变

print(myStr.replace('world','WORLD',1)) # hello WORLD world  指定count
```

- **split** 以str作为分隔符切片myStr,如果maxSplit有指定值，那就切成指定值个切片

```
myStr = 'hello world world'

print(myStr.split(' ')) # 按照空格切割 ['hello','world','world']

print(myStr) # hello world world 

print(myStr.split(' ', 1)) # ['hello', 'world world']
```

*关于`split`,直接写`str.split()`的话，意味着程序会自动按照空格，\t等分隔符来智能分割字符串*

```
myStr = 'abc dag \t dahj afh \t'

print(myStr.split()) # ['abc', 'dag', 'dahj', 'afh']

```


- **capitalize** 和 **title**

```
myStr = 'hello world world'

print(myStr.capitalize()) # Hello world world

print(myStr.title()) # Hello World World 

print(myStr) # hello world world 
```



- **startswith/endswith** 检查字符串是否已obj开头/结尾，是返回True,否返回False

```
myStr = 'hello world world'

print(myStr.startswith('hello')) # True

print(myStr.startswith('world')) # False

print(myStr.endswith('world')) # True

```

- **lower/upper** 转换为小写/大写

```
myStr = 'Hello World World'

print(myStr.lower()) # hello world world

print(myStr.upper()) # HELLO WORLD WORLD 

print(myStr) # Hello World World
```

- **ljust/rjust/center** 返回一个字符串左对齐/右对齐/居中，并使用空格填充至长度width的新字符串

```
myStr = 'hello world'

print(myStr.ljust(20)) #  'hello world         '

print( len(myStr.ljust(20)) ) # 20 长度已经改变 

print(myStr.rjust(20)) # '         hello world'

print(myStr.center(20)) # '    hello world     '

print(myStr) # 原字符串不改变
```

- **lstrip/rstrip** 删除字符串左边/右边的空白字符  strip 删除两端的空白字符

```
myStr1 = '  hello world'

print(myStr1.lstrip()) # hello world

myStr2 = 'hello world   '

print(myStr2.rstrip()) # hello world

myStr3 = '  hello world    '

print(myStr3.strip()) # hello world
```

- **partition/rpartition** 将myStr分为三部分，str前,str,以及str后

```
myStr = 'hello world world'

print(myStr.partition('world')) # ('hello ', 'world', ' world')

print(myStr.rpartition('world')) # ('hello world ', 'world', '')

```

- **splitlines**  按照行分隔，返回一个包含各行作为元素的列表

```
myStr = 'hello\nworld'

print(myStr.splitlines()) # ['hello', 'world']

```

- **isaplha/isdigit/isalnum** 所有字符都是字母/数字/数字或者字母

```
str1 = '1'

print(str1.isalpha()) # False 所有字符都是字母

print(str1.isdigit()) # True 所有字符都是数字

print(str1.isalnum()) # True 所有字符都是数字或者字母

str2 = '123hello'

print(str2.isalpha()) # False

print(str2.isdigit()) # False 

print(str2.isalnum()) # True
```

- **islower/isupper/istitle/isspace** 所有字符都是小写/大写/单词首字母大写/空白字符

```
print('hello'.islower()) # True

print('HELLO'.isupper()) # True

print('Hello'.istitle()) # True

print(''.isspace()) # False

print(' '.isspace()) # True
```

- **join** myStr中每个字符后面加入str,构造出一个新的字符串

```
li = ['start','learning','Python']

str1 = ' '

print(str1.join(li)) # start learning Python

str2 = '_'

print(str2.join(li)) # start_learning_Python
```
