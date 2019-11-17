---
title: JS-Math,Array,Date的使用举例
date: 2017/10/13 00:00:01
cover: https://tekraze.com/wp-content/uploads/2018/05/javascript.jpg
tags: 
- 前端
- JS
- Math
- Array
- Date
categories: 
- 前端
---
JS-Math,Array,Date的使用举例
<!--more-->

#Math

### 1、写一个函数，返回从min到max之间的 随机整数，包括min不包括max 
```
    function random(min,max) {
        return min + Math.floor(Math.random() * (max - min));
    };
```
###2、写一个函数，返回从min都max之间的 随机整数，包括min包括max 
```
    function random2(min,max) {
        return min + Math.floor(Math.random() * (max+1 - min));
    }
```

###3、写一个函数，生成一个长度为 n 的随机字符串，字符串字符的取值范围包括0到9，a到 z，A到Z。
```
    function random(min,max) {
        return min + Math.floor(Math.random() * (max - min));
    };
    function getRandStr(length) {
        var dict = '123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        var str = '';
        for (var i = 0;i < length; i ++) {
            str += dict[random(0,dict.length)];
        }
        console.log(str);
    }
    getRandStr(8);
    getRandStr(16);
```

###4、写一个函数，生成一个随机 IP 地址，一个合法的 IP 地址为 0.0.0.0~255.255.255.255
```
    function random(min,max) {
        return min + Math.floor(Math.random() * (max - min));
    };
    function getRandIP () {
        var arr = [];
        for (var i = 0; i < 4; i ++) {
            arr[i] = random(0,256);
        }
        console.log(arr.join('.'));
    }
    getRandIP();
```


###5、写一个函数，生成一个随机颜色字符串，合法的颜色为#000000~ #ffffff
```
    function random(min,max) {
        return min + Math.floor(Math.random() * (max - min));
    };
    function getRandColor () {
        var str = '#';
        var dict = '0123456789abcdef';
        for (var i = 0; i < 6; i ++) {
            str += dict[random(0,dict.length)];
        }
        console.log(str);
    }
    getRandColor();
```


#数组

###1、数组方法里push、pop、shift、unshift、join、splice分别是什么作用？用 splice函数分别实现push、pop、shift、unshift方法

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
用splice实现：
```
    //pop
    var arr = [1,2,3,4,5];
    arr.splice(arr.length-1,1);//arr.pop();
    console.log(arr);//[1, 2, 3, 4]

    //push
    var arr2 = [1,2,3,4,5];
    arr2.splice(arr2.length,0,6); // arr2.push(6);
    console.log(arr2);//[1, 2, 3, 4, 5, 6]

    //unshift
    var arr3 = [1,2,3,4,5];
    arr3.splice(0,0,0);//arr3.unshift(0);
    console.log(arr3); //[0, 1, 2, 3, 4, 5]

    //shift
    var arr4 = [1,2,3,4,5];
    arr4.splice(0,1);//arr4.shift();
    console.log(arr4);//[2, 3, 4, 5]
```
###2、写一个函数，操作数组，数组中的每一项变为原来的平方，在原数组上操作
```
    function squareArr(arr){
        arr.forEach(function (e,i,Array) {
            Array[i] = Math.pow(e,2);
        })
    }
    var arr = [2, 4, 6]
    squareArr(arr)
    console.log(arr) // [4, 16, 36]
```

###3、写一个函数，操作数组，返回一个新数组，新数组中只包含正数，原数组不变
```
    function filterPositive(arr){
        var arr2 = [];
        arr.map(function(e,i,Array){
            if (typeof e == "number" && Array[i] > 0)   {
                arr2.push(e);
            }
        })
        return  arr2;
    }
    var arr = [3, -1,  2,  'DeeJay', true]
    var newArr = filterPositive(arr)
    console.log(newArr) //[3, 2]
    console.log(arr) //[3, -1,  2,  'DeeJay', true]
```

#Date

###1、 写一个函数getChIntv，获取从当前时间到指定日期的间隔时间
```
var str = getChIntv("2017-02-08");
console.log(str);  // 距除夕还有 20 天 15 小时 20 分 10 秒
```
```
    function getChIntv (str) {
        var targetDate = new Date(str);
        var currentDate = new Date();
        var interval = targetDate - currentDate;
        var totalSeconds = Math.floor(interval/1000);
        var seconds = totalSeconds%60;
        var totalMinutes = Math.floor(totalSeconds/60);
        var minutes = totalMinutes%60;
        var totalHours = Math.floor(totalMinutes/60);
        var hours = totalHours%60;
        var totalDays = Math.floor(totalHours/24);
        if (interval > 0) {
            console.log('距除夕还有 ' + totalDays + '天' +　hours + '小时' + minutes + '分' + seconds + '秒');
        }
        else {
            console.log('距离目标日期已经过去了' + -totalDays + '天' +　-hours + '小时' + -minutes + '分' + -seconds + '秒');
        }
    }

    getChIntv("2017-02-08");
```

###2、把hh-mm-dd格式数字日期改成中文日期
```
var str = getChsDate('2015-01-08');
console.log(str);  // 二零一五年一月八日
```
```
    function getChsDate (str) {
        var dict = ['零' , '一' , '二' , '三' , '四' , '五' , '六' , '七' , '八' ,
            '九' , '十' , '十一' , '十二' , '十三' , '十四' , '十五' , '十六' , '十七' ,
            '十八' , '十九' , '二十' , '二十一' , '二十二' , '二十三' , '二十四' , '二十五' ,
            '二十六' , '二十七' , '二十八' , '二十九' , '三十' , '三十一'];
        var arr = str.split('-'); //分成年月日三个数组
        var yearArr = arr[0].split(''); //再把每个部分分成单项的数组
        var yearStr = '';
        yearArr.forEach(function(e,i,Array){
            yearStr += dict[parseInt(yearArr[i])];
        })
        var monthStr = dict[parseInt(arr[1])];
        var dayStr = dict[parseInt(arr[2])];
        console.log(yearStr + '年' + monthStr + '月' + dayStr + '日');
    }
     getChsDate('2015-11-31');
```

###3、写一个函数，参数为时间对象毫秒数的字符串格式，返回值为字符串。假设参数为时间对象毫秒数t，根据t的时间分别返回如下字符串:
- 刚刚（ t 距当前时间不到1分钟时间间隔）
- 3分钟前 (t距当前时间大于等于1分钟，小于1小时)
- 8小时前 (t 距离当前时间大于等于1小时，小于24小时)
- 3天前 (t 距离当前时间大于等于24小时，小于30天)
- 2个月前 (t 距离当前时间大于等于30天小于12个月)
- 8年前 (t 距离当前时间大于等于12个月)
 ```
function friendlyDate(time){
}
var str = friendlyDate( '1484286699422' ) //  1分钟前
var str2 = friendlyDate('1483941245793') //4天前
```
```
function friendlyDate(time){
  var targetDate = new Date(parseInt(time)),nowDate = new Date(),result = "";
  var offset = nowDate - targetDate;
  var totalMinute = Math.floor(Math.floor(offset/1000)/60);
  if(totalMinute === 0){
    result = "刚刚";
  }
 if(totalMinute > 0 && totalMinute < 60){
   result = "3分钟前";
 }
 if(totalMinute >59 && totalMinute < 1440){
   result = "8小时前";
 }
 if(totalMinute > 1439 && totalMinute < 43200){
   result = "3天前";
 } 
 if(totalMinute > 43199 && totalMinute < 518400){
   result = "2个月前";
 } 
 if(totalMinute >518399){
   result = "8年前";
 } 
  return result;
}
var str = friendlyDate( '1484286699422' ) 
var str2 = friendlyDate('1483941245793')
```
