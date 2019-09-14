---
title: 字符串，JSON
date: 2017/08/14 00:00:01
tags: 
- 前端
- JSON
categories: 
- 前端
---
字符串，JSON
<!--more-->


## 多行,转义以及字符串常见方法
如果要在单引号字符串的内部，使用单引号（双引号同理），就必须在内部的单引号前面**加上反斜杠，用来转义**。
```
    var str = 'did she say \' hello \'';
    console.log(str); //did she say ' hello '
    var str1 = "did she say \" hello \"";
    console.log(str1); //did she say " hello "
```
字符串默认只能写在一行内，分成多行会报错。如果长字符串必须分成多行，可以**在每一行的尾部使用反斜杠**。
```
    var longStr = 'long \
    long\
    long\
    string';
    console.log(longStr); //long     long    long    string
```
- 字符串的截取
```
    var str = 'hello,world';
    var sub1 = str.substr(1,3); //第一个是开始位置，第二个是长度
    console.log(sub1); //ell
    var sub2 = str.substring(1,3); //第一个是开始位置，第二个是结束位置
    console.log(sub2); //el, 长度为第二个参数减去第一个参数
    var　sub3 = str.slice(1,3);
    console.log(sub3); //el 同subString()
    console.log(str); //hello,world  原本的字符串是保持不变的
```
- 字符串的查找
```
    var str = 'hello my world';
    var str1 = str.search('my');
    console.log(str1); // 6  查找不到的话返回-1
    var str2 = str.replace('my','your');
    console.log(str2); //hello your world 替换
    var str3 = str.match('my');
    console.log(str3); //["my", index: 6, input: "hello my world"]  　返回匹配的数组
```
- 字符串大小写转换
```
    var str = 'Hello DeeJay';
    var str1 = str.toUpperCase();
    console.log(str1); //HELLO DEEJAY
    var str2 = str.toLowerCase();
    console.log(str2); //hello deejay
    console.log(str);  //Hello DeeJay  原本的字符串不发生改变
```
- split()
```
    var str = 'hello';
    var str1 = str.split(''); //将字符串进行切割，成为数组
    console.log(str1); //  ["h", "e", "l", "l", "o"]
    var str2 = str.split('l');
    console.log(str2); // ["he", "", "o"] 按照l分开 
    //split还可以传入第二个代表长度的参数
```
###0、对于 HTTP 协议而言，HTML、CSS、JS、JSON 的本质都是什么？

为本质上只是符合通信格式的**字符串**。

###1、使用数组拼接出如下字符串 ，其中styles数组里的个数不定
```
var prod = {
    name: '女装',
    styles: ['短款', '冬季', '春装']
};
function getTpl(data){
//todo...
};
var result = getTplStr(prod);  //result为下面的字符串
```

将获取到的数据转化为html字符串：
```
    var prod = {
        name: '女装',
        styles: ['短款', '冬季', '春装']
    };
    function getTpl(data){
        //使用字符串拼接
        var str ='';
        str += '<ul>';
        str += '<li>' + data.name + '</li>';
        for (var i = 0; i < data.styles.length; i ++) {
            str += '<li>'+ data.styles[i] + '</li>';
        }
        str += '</ul>';
        return str;
    };
    function getTpl2(data) {
        //使用数组方法
        var arr = [];
        arr.push('<ul>');
        arr.push('<li>' + data.name + '</li>');
        for (var i = 0; i < data.styles.length; i ++) {
            arr.push('<li>' + data.styles[i] + '</li>');
        }
        arr.push('</ul>');
        return arr.join(' ');
    }
    var result = getTpl(prod);
    var result2 = getTpl2(prod);
    console.log(result);
    console.log(result2);
```


###2、写出两种以上声明多行字符串的方法
例如`var str = 'abcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancde'`这段字符串很长，如何多行优雅的显示

```
    var str = 'abcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancde';

    //使用字符串拼接

    var str1 = '';
    str1 = '<ul>';
    str1 += '   <li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>';
    str1 += '   <li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>';
    str1 += '   <li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>';
    str1 += '</ul>';
    console.log(str1);

    //使用数组方法
    var str2 = [];
    str2.push('<ul>');
    str2.push('<li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>');
    str2.push('<li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>');
    str2.push('<li>' + 'abcdeabcdeabcdeancdeabcd' + '</li>');
    str2.push('</ul>');
    str2 = str2.join('  ');
    console.log(str2);

    // 使用反斜杠\来进行转义
    var str3 =
    '<ul>\
         <li>abcdeabcdeabcdeancdeabcd</li>\
         <li>abcdeabcdeabcdeancdeabcd</li>\
         <li>abcdeabcdeabcdeancdeabcd</li>\
    </ul>';
    console.log(str3);

    // 使用ES6语法 `` 来声明字符串
    var str4 =
            `<ul>
                <li>abcdeabcdeabcdeancdeabcd</li>
                <li>abcdeabcdeabcdeancdeabcd</li>
                <li>abcdeabcdeabcdeancdeabcd</li>
            </ul>`;
    console.log(str4);
```

针对多行JS字符串：
```
    var str = 'abcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancdeabcdeabcdeabcdeancde';

    var str1 = 'abcdeabcdeabcdeancd\
    abcdeabcdeabcdeancd\
    abcdeabcdeabcdeancd\
    ';
    console.log(str1);
    var str2 = `abcdeabcdeabcdeancd
    abcdeabcdeabcdeancd
    abcdeabcdeabcdeancd`;
    console.log(str2);
```

###3、补全如下代码,让输出结果为字符串: hello\\\deejay
```
var str = //补全代码
console.log(str)
```
```
    var str = 'hello \\\\deejay';
    console.log(str) //输出hello \\deejay
```


###4.以下代码输出什么?为什么
```
    var str = 'hello\ndeejay'
    console.log(str.length);
```

```
    var str = 'hello\ndeejay'
    console.log(str.length); // 输出12
```
因为字符串中转义的\n换行符也占一个长度空间

###5、写一个函数，判断一个字符串是回文字符串，如 abcdcba是回文字符串, abcdcbb不是
```
    function strReverse (str) {
//        str.split('');//将字符串在每个空字符串处隔开变为数组，即让字符串每个字符为一项构成一个数组，从而可以使用数组的方法
        if (str == str.split('').reverse().join('')) { // 变为数组倒序之后再转化为字符串
            return '传入字符串为回文';
        }
        else {
            return '传入字符串不是回文';
        }
    }
    var result = strReverse('abcdcba');
    console.log(result); // 传入字符串为回文
    var result2 = strReverse('abcdcbb');
    console.log(result2); //传入字符串不是回文
```

###6、写一个函数，统计字符串里出现出现频率最多的字符
```
    var str = 'hello deejayyyyyyyyyyy';
    
    function repeat(str) {
        var dict = {};//创建一个对象，熟悉设置为dict[str[i]],即str中的每个字符，值设定为每个字符出现的次数
        var maxKey;
        var maxValue = 0;

        for (var i = 0; i < str.length; i ++) {
            if (dict[str[i]] == undefined) {// str[i]即为字符串中的每个字符, dict[str[i]]即将dict的属性设置为字符串中的每个字符
                dict[str[i]] = 1;  // 如果遍历到不存在的属性，则添加到dicr属性中，并且值设为1，即新属性出现过1次了
            }
            else {
                dict[str[i]]++;  // 遍历到存在的属性，出现次数+1，
            }
        }
        for (var key in dict) {  // 得到了属性为str中的每个字符，值为每个对应的字符出现的次数的dict对象，遍历这个dict对象
            if (dict[key] > maxValue) {  //dict[key]即为属性为Key的属性值
                maxValue = dict[key]; //取得最大值，即为字符出现的最大次数
                maxKey = key; //key即为出现次数最多属性值对应的属性
            }
        }
        console.log('出现最多的字符为:'+maxKey,'总共出现了'+maxValue+'次');
    }
    repeat(str); //出现最多的字符为:y 总共出现了11次
```

###7、写一个camelize函数，把my-short-string形式的字符串转化成myShortString形式的字符串，如
```
camelize("background-color") == 'backgroundColor'
camelize("list-style-image") == 'listStyleImage'
```
1. 
```
    function camelize(str) {
        var str1 = str.split(''); //转换为数组 以便使用splice方法
        for (var i = 0;i < str1.length; i ++) {
            if (str1[i] == '-') { //遍历数组，有为 - 的项的时候，删除-项以及后面的一项，并且用后面的一项的大写形式来替换
                str1.splice(i,2,str1[i+1].toUpperCase());
            }
        }
        var str2 = str1.join(''); //数组转化为字符串然后输出
        console.log(str2);
    }


    camelize("background-color") == 'backgroundColor'
    camelize("list-style-image") == 'listStyleImage'
```
2. 
```
    function camelize (str) {
        var str1 = str.split('-'); //将-隔开的字符串变为一项，这些项组成一个数组
        for (var i = 1;i < str1.length; i ++) {
            str1[i] = str1[i][0].toUpperCase() + str1[i].substring(1,str1[i].length);
            //从第二项开始遍历，然后将第二项开始的每一项的第一个字母大写，并且截取后面的内容重新赋值给该项
        }
        var str2 = str1.join(''); //转化为字符串输出
        console.log(str2);
    }


    camelize("background-color") // 'backgroundColor'
    camelize("list-style-image") // 'listStyleImage'
```

###8、写一个 ucFirst函数，返回第一个字母为大写的字符 （***）
ucFirst("deejay") == "Deejay"
```
    function ucFirst(str) {
        console.log(str[0].toUpperCase() + str.substring(1,str.length));
    }
    ucFirst('deejay'); //Deejay
```

###9、写一个函数truncate(str, maxlength), 如果str的长度大于maxlength，会把str截断到maxlength长，并加上...，如
```
truncate("hello, this is hunger valley,", 10) == "hello, thi...";
truncate("hello world", 20) == "hello world"
```


```
    function truncate (str,max) {
        if (str.length > max) {
            str = str.substring(0,max) + '...';
        }
        console.log(str);
    }
    truncate("hello, this is deejay,", 10) // "hello, thi...";
    truncate("hello world", 20) // "hello world"
```

###10、什么是 JSON格式数据？JSON格式数据如何表示对象？window.JSON 是什么？

- JSON，即JavaScript Object Notation。是一种轻量级的数据交换格式。
它是基于JavaScript（Standard ECMA-262 3rd Edition - December 1999）的一个子集，采用完全独立于语言的文本格式，但是也使用了类似于C语言家族的习惯（包括C, C++, C#, Java, JavaScript, Perl, Python等）。
这些特性使JSON成为理想的数据交换语言， 易于人阅读和编写，同时也易于机器解析和生成(网络传输速度)。
一种模仿JavaScript语法创造的数据的格式，比较香JavaScript里面的对象，但是其**本质是一个字符串**

JSON语法是JavaScript对象表示语法的子集。
1. 数据在名称/值对中
2. 数据由逗号分隔
3. 花括号保存对象
4. 方括号保存数组

JSON数据的**属性名称必须写在双引号中**。
示例：
```
    var json1 = {
        "name": "deejay",
        "age": 21,
        "sex": "male",
    }
    var jason2 = [
        {"name": "deejay","age": 21},
        {"name": "dee","age": 22}    
    ]
```

###11、如何把JSON 格式的字符串转换为 JS 对象？如何把 JS对象转换为 JSON 格式的字符串?

IE以上的浏览器都支持一个JSON对象。
JSON对象有两个静态函数：
- JSON.parse: 把字符串转换为JSON对象
- JSON.stringify: 把JSON对象转化为字符串
```
    var str = '{"name": "deejay", "age": 21}';
    var strParse = JSON.parse(str);
    console.log(strParse.name); //deejay
    var obj = {
        name: "deejay",
        age: 21
    }
    console.log(JSON.stringify(obj)); //{"name":"deejay","age":21} 为字符串
```
