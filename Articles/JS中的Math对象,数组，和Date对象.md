---
title: JS中的Math对象,数组，和Date对象
date: 2017/08/15 00:00:01
tags: 
- 前端
- JS
categories: 
- 前端
---
JS中的Math对象,数组，和Date对象
<!--more-->


# Math
Math对象是JS内置对象，提供了一些静态的数学属性和数学方法，使用时不用实例化。
- 属性：
  Math.E  
Math.LN2  
Math.LN10
Math.LOG2E
Math.LOG10E
**Math.PI**
Math.SQRT1_2
Math.SQRT2
- 方法：
1. 四舍五入 round
```
    Math.round(0.1); //0
    Math.round(0.5); //1
```
对于 负值的运算结果不同，体现在对于.5的处理
```
    Math.round(-1.1);//-1
    Math.round(-1.5); //-1
```
2. 绝对值abs，最大参数max，最小参数min
abs方法返回参数值的绝对值
```
    Math.abs(1)//1
    Math.abs(-1) //1
```
max方法返回最大的参数，min方法返回最小的参数
```
    Math.max(1,2,3); //3
    Math.min(1,2,3); //1
```
3. floor,ceil
分别返回小于或者大于参数值的**最大（小）整数**
```
    Math.floor(3.999); //3
    Math.floor(-3.999); //-4

    Math.ceil(3.999); //4
    Math.ceil(-3.999); //-3
```
4. pow,sqrt
pow返回一个参数为底数，第二个参数为幂的指数值
sqrt方法返回参数的平方根，如果参数为负值，返回NaN
```
    Math.pow(2,2); // 4
    Math.pow(2,3); // 8
    
    Math.sqrt(4); //2
    Math.sqrt(-4); //NaN
```
5. random
random方法**返回0-1之间的一个伪随机数，可能等于0，但是一定小于1**
通过组合使用random方法和floor或者ceil方法可以得到指定范围内的随机整数，示例：
```
    // 返回一个大于等于a，小于b之间的随机整数
    function random(a,b) {
        return a + Math.floor(Math.random() * (b-a));
    }
```
利用定义得到的random()函数，可以实现输出一些指定格式的结果:
```
    // 返回一个大于等于a，小于b之间的随机整数
    function random(a,b) {
        return a + Math.floor(Math.random() * (b-a));
    }

    // 得到一个随机的字符串
    function randomStr(len) { //len为随机字符串的长度
        var dict = '0123456789abcdefgihijklmnopqrstuvwxyz'; // 创建一个字符串，所有可能的字符取值
        var str = ''; // 要输出的随机字符串
        for (var i = 0; i < len; i ++) {
            str += dict[random(0,dict.length)];
            // 利用上面定义的random函数，生成0到dict字符串的长度的随机整数，作为下标，对应的字符即为输出的字符，赋给str
        }
        return str;
    }
    var str = randomStr(8);//指定8位的字符串
    console.log(str);

    //得到一个随机的IP地址 0.0.0.0 ~ 255.255.255.255
    function randomIP () {
        //同样要利用定义好的random()
        var arr = [];
        for (var i = 0; i < 4; i ++) { //IP 4位，循环4次
            arr.push(random(0,256)); // 写成255就不包括255了
        }
        return arr.join('.'); //返回.隔开的字符串
    }
    var IP = randomIP();
    console.log(IP);
```
# 数组
- 数组的创建的多种方式
  `var arr = new Array();`
使用构造函数创建空数组，一般不使用，而是用字面量创建数组:
  `var arr = [];`
在使用字面量创建数组的时候：
无论传入几个参数，**都会把参数当做初始化内容而不是长度**，例如`var arr = [5];`即创建的新数组只有1项，为5。
```
    var arr = [5];
    console.log(arr.length); //1
    var arr2 = [5,6];
    console.log(arr2.length) //2
```
但是需要注意的是，进行数组创建的时候，不要带多余的“，”  因为不同的浏览器解析结果不一致。例如：
```
    var arr = [1,2,3,]; //有多余的,
    console.log(arr.length); // 现代浏览器上结果为3，但是在低版本的IE上是长度为4的数组，最后一项为undefined
```
- 数组的索引
数组的值可以通过下标来进行访问，**下标可以是自然数也可以是变量或者表达式**，例如：
```
    var arr =[1,2,3,4];
    console.log(arr[0]); //1
    var i =1;
    console.log(arr[i]);//2
    console.log(arr[++i]);//3
```
数组也是对象，是一个特殊的对象，key为数值的对象。
- 数组的基本操作
```
    var a = [1,2,3];
    a[3] = 4; // 增加新元素
    console.log(a); //[1,2,3,4]
    //使用delete删除数组元素
    delete a[2];
    console.log(a[2]); //undefined  跟给a[2]赋值undefined相似，数组长度不会改变，也不会影响其他数据。
    console.log(a.length); //4 数组长度不会改变
```
- 栈方法pop()和push()
```
    var arr = [3,4,5];
    arr.push(100); //push里面传参
    console.log(arr); //3,4,5,100
    arr.pop(); //pop不需要传参
    console.log(arr); //3,4,5
```
- 队列方法 shift()和unshift()
```
    var arr =[1,2,3];
    arr.unshift(4);
    console.log(arr); //[4,1,2,3]
    arr.shift();
    console.log(arr);//[1,2,3]
```
- 终极神器splice()
splice方法可以一次性解决数组添加，删除。传入三个参数
第一个参数为开始的位置， 第二个参数为想要删除的长度，第三个参数为想要添加的项目,也可以写多个：
```
    var arr = [3,5,8,10];
    arr.splice(2,1);  // 删除第2个元素
    console.log(arr); //[3,5,10]
    var arr1 = [3,5,8,10];
    arr1.splice(1,1,7,8,9); //在第2位开始，删除1项，然后增加7,8,9三项
    console.log(arr1);//[3, 7, 8, 9, 8, 10]
    var arr2 = [3,5,8,10];
    arr2.splice(3,0,9); // 在第3项位置删除0项，即不删除，然后插入9的一项
    console.log(arr2);//[3, 5, 8, 9, 10]
```
- join方法
将数组连接成字符串,可以选择使用什么字符隔开每一项
```
    var arr = [1,2,3,4,5,6];
    //转化为字符串
    var str1 = arr.join('|');
    console.log(str1); //1|2|3|4|5|6
    var str2 = arr.join(''); 
    console.log(str2); // 123456
```

**数组跟字符串使用join("")和split("")可以互相转换，使用对方的方法**

- concat()  连接数组，会生成一个新数组，不会影响原数组
```
    var a = [1,2,3];
    var b = [4,5];
    console.log(a.concat(b)); //[1, 2, 3, 4, 5]
    console.log(a); // [1, 2, 3] 原数组不受影响
    console.log(b); //[4, 5]
```
- reverse()方法用于将数组逆序，会修改原数组
 ```
    var arr = [1,2,3,4,5];
    arr.reverse();
    console.log(arr); //  [5, 4, 3, 2, 1] 会修改原数组
```
 - sort() 
sort方法在没有参数的时候，会**按照字母表升序排序**，有undefined放到最后，对于对象调用toString方法。
一般需要自定义一个排序方式，传入一个比较函数作为sort方法的参数，sort也会改变原数组。
```
    // 采用默认排序法时出现的问题
    var arr = [7,8,9,10,11];
    arr.sort();
    console.log(arr); //[10, 11, 7, 8, 9] 并没有按照意料中的方式排序
    // 所以需要定义一个自定义的函数，作为sort方法的参数进行排序
    var arr2 = [-4,1,18,22,3,9];
    function compare (a,b) {
        return a - b; //根据a-b的正负来排序
    }
    arr2.sort(compare);
    console.log(arr2);//[-4, 1, 3, 9, 18, 22]  写成b-a会降序
    //针对对象的属性的数值进行排序
    var student = [
        {
            name: 'a',
            age: 10
        },
        {
            name: 'c',
            age: 30
        },
        {
            name: 'b',
            age: 20
        },
    ];
    student.sort(function(stu1,stu2) {
        return stu1.age - stu2.age;
    })
    console.log(student); //{name: "a", age: 10} {name: "b", age: 20} {name: "c", age: 30} 按照对象的age属性排序
```
ES5 数组扩展了一些功能
- Array.isArray(obj)
Array对象的一个静态函数，判断一个对象是不是数组
```
    var a = [123];
    var b = new Date();
    console.log(Array.isArray(a)); //true
    console.log(Array.isArray(b)); //false
```
- .indexOf(element)/.lastIndexOf(element)
用于查找数组内**指定的元素的位置**，没找到返回-1，找到返回下标
-  **.forEach(element,index,array)**
遍历一个数组，参数为一个回调函数，回调函数有三个参数
1,当前元素  2，当前元素索引值  3，整个数组
```
    var arr = [1,2,3,4,5,6];
    arr.forEach(function (e,i,array) {
        array[i] = e + 1; //每项都加1
    });
    console.log(arr);//[2, 3, 4, 5, 6, 7]
    //forEach代替了通常用for循环遍历数组
```
- every(function (element,index,array))/some(function (element,index,array))
every和some类似于一种逻辑判定，回调函数返回一个**布尔值**
 every是所有函数的每个回调函数都返回true的时候才返回true，有false的时候就终止执行，返回false
some是存在着有一个回调函数返回true时就终止执行返回true，否则返回false
对于空数组，every返回true，some返回false
```
    var arr = [1,2,3,-2,3];
    // 判断数组中每一项是否都为正值
    var resultEvery = arr.every(function (value) { // 只判断是不是整数，所以参数只用到value，写一个value就好
        return value > 0;
    });
    console.log(resultEvery);  //false
    // 判断数组中每一项是否存在正值
    var resultSome = arr.some(function (value){
        return  value > 0;
    });
    console.log(resultSome);  //true
```
- **map(function(element))**
和forEach类似，遍历数组，回调函数返回值组成**一个新数组返回**，新数组索引和原数组一致，**原数组不变**。
```
    var arr = [1,2,3,4,5];
    var arr2 = arr.map(function (element) { //遍历数组，返回一个新数组，每项是原数组的平方
        return  Math.pow(element,2);
    })
    console.log(arr); //[1, 2, 3, 4, 5]  原数组不发生改变
    console.log(arr2); //[1, 4, 9, 16, 25]

    //上述需求也可以利用forEach实现 为:
    var arr3 = [];
    arr.forEach(function (e,i,array) {
        arr3.push(Math.pow(e,2)); // 利用forEach来达到同样的效果，要提前创建一个新数组，将得到的平方值依次push进去
    })
    console.log(arr3); // [1, 4, 9, 16, 25]
    console.log(arr); //[1, 2, 3, 4, 5]
```
- **filter(function(element)) 过滤**
返回数组的一个子集，回调函数用于逻辑判断是否返回，返回true则把当前元素加到返回数组中，false则不加
新数组只包含返回true的值，索引缺失的不包括，原数组保持不变
```
    var arr = [1,2,3,4,5];
    console.log(arr.filter(function (e) {
        return e % 2 == 0; //返回偶数
    })); //[2, 4]
    console.log(arr); // [1, 2, 3, 4, 5] 原数组不变


    //针对对象进行过滤
    var student = [
        {
            username: 'aa',
            age: 20,
        },
        {
            username: 'bb',
            age: 30,
        },
        {
            username: 'ac',
            age: 25,
        },
    ];
    //要过滤出student中username包含a字符的项
    var result = student.filter(function (e) {
       return e.username.indexOf('a') > -1; //使用indexOf可以判断是否包含a字符
    });
    console.log(result);// {username: "aa", age: 20} {username: "ac", age: 25}
```
- reduce(function(v1,v2),value)/.reduceRight(function(v1,v2),value)
遍历数组，调用回调函数，将数组元素组合成一个值，reduce从索引最小值开始，reduceRight反向，方法有2个参数，    1，回调函数，把两个值合为一个，返回结果    2，value，一个初始值，可选
```
    var arr = [1,2,3,4,5];
    var arr1 = arr.reduce(function(v1,v2) {
        console.log('本次v1为:'+v1 ,'本次v2为:' + v2);
        return  v1  + v2;
    });
    console.log(arr1); //15
```

# Date
Date对象是JS提供的日期和时间的操作接口
Date对象有几个静态方法
- Date.now()
now方法返回当前距离 1970年1月1日00：00:00的毫秒数
`Date.now();//1502771978564`
- Date.parse()
解析字符串，返回距离 1970年1月1日00：00:00的毫秒数，解析失败返回NaN
`console.log(Date.parse('2017-1-1')); //1483200000000`
- Date.UTC()
默认返回当前时区的时间，接收年月日等变量作为参数，返回距离 1970年1月1日00：00:00的毫秒数
`Date.UTC(2015,0,1,2,3,4,567);//1420077784567`
- Date()
Date函数可以直接调用，返回当前时间
- get
```
    var d = new Date();
    console.log(d.getTime()); //返回距离1970年1月1日00：00:00的毫秒数
    console.log(d.getDate());//返回实例对象对应每个月的几号（从1开始）
    console.log(d.getDay());//周几，周日为0，周一为1
    console.log(d.getFullYear());//返回四位数的年份
    console.log(d.getMonth());//月份，0为1月
    console.log(d.getHours());//小时 0-23
    console.log(d.getMilliseconds());//毫秒 0-999
    console.log(d.getMinutes());//分钟 0-59
    console.log(d.getSeconds());//秒 0-59
    console.log(d.getTimezoneOffset());//时区差异
```
另外get还有对应的set方法
- Date对象的应用距离
```
    // 写一个函数getChIntv, 获取从当前时间到指定日期的间隔时间

    function getChIntv(dateStr) {
        var targetDate = new Date(dateStr);// 传入目标参数，创建目标时间的date对象
        var currentDate = new Date();//表示现在时间
        var interval = targetDate - currentDate; // 可以直接相减，默认调用valueOf函数生成毫秒数
        if (interval > 0) {
            //生成的interval为毫秒数，我们转换为秒数并且取整
            var totalSeconds = Math.floor(interval/1000); //换算成秒数并且取整的值
            var seconds = totalSeconds%60; //得到余下来的秒数，为我们最终显示的秒数，前面的可以整除的换算为分钟小时和天

            var totalMinutes = Math.floor(totalSeconds/60); //为总共的分钟数 这里用Math.floor取整
            var minutes = (totalMinutes%60); //同样，可以整除的去换算为小时和天数，余下来的分钟用于最终显示,

            var totalHours = Math.floor(totalMinutes/60); //总共的小时数 这里用Math.floor取整
            var hours = (totalHours%24); //余下来的小时数作为最终显示，可以整除的去换算成天数

            var totalDays = Math.floor(totalHours/24); //总共的天数，不用换算成年了，所以直接换算成天数就好
            console.log('距离目标日期还有：'+ totalDays + '天' + hours + '小时' + minutes + '分钟' + seconds + '秒');
        }
        else {
            console.log('目标日期已经过了');
        }
    }
    getChIntv('2017-1-1');
```
