---
title: JS引用类型，对象拷贝
date: 2017/08/12 00:00:01
tags: 
- 前端
- JS
categories: 
- 前端
---
JS引用类型，对象拷贝
<!--more-->

##引用类型
引用类型变量**保存的仅仅是一个指针**，指针指向堆内存中保存对象的位置。
- 所以基本类型复制的时候仅仅复制值，复制前后的两个变量**相互独立**。
- 而引用类型复制的时候实质上仅仅是把保存对象的位置的地址复制传给了复制后的变量，两个变量实际上指向的还是同一个对象，所以通过一个变量改变对象的属性，另外一个变量引用对象时，属性也会发生变化。

对于函数的参数传递，不用去考虑按值传递还是按引用传递，只需要这么理解：
```
     function fn (n) {
//         var n = a; // 可以视为函数参数的传递，将a中的值传给n
         n++;
     }
     var a = 10;
     fn(a);
     console.log(a);   //10   虽然n变为11，但是a仍然为10
```
可以理解成函数内部有一句隐式的语句var n = a; 可以视为函数参数的传递，将a中的值传给n,**a是基本类型时传递的就是值，a是引用类型时a中保存的就是对象的地址，所以将地址传递给n**。
当a是引用类型时的例子
```
     function fn (n) {
//         var n = a; // 传递的是a的对象的地址
         n.age++;
     }
     var a = {
         name: 'deejay',
         sex: 'male',
         age: 21
     }
     fn(a);
     console.log(a);   // n.age =22 a.age =22,都为22，因为传递的是参数，实质上改变的是同一个对象

```
## 对象拷贝
- 浅拷贝：浅拷贝即**仅仅拷贝一层**的对象属性。举例说明：
```
    var oldObj = {
        name: 'deejay',
        age: 20,
        friend: {
            name: 'dee',
            age: 20
        }
    }
    function shallowCopy(oldObj) {
        var newObj = {}; // 要进行对象的拷贝，必须要新开辟一个空间存放对象，有不同的地址，才能相互独立。
        for(var key in oldObj) {
            newObj[key] = oldObj[key];//创建了一个新对象之后，遍历oldObj的属性，每一项都拷贝给newObj的属性
        }
        return newObj;
    }
    var newObj = shallowCopy(oldObj);
    console.log(newObj);
    newObj.age++;
    console.log(oldObj.age);  //20  newobj的age增加，oldobj的age仍然不变
    newObj.friend.age++;
    console.log(oldObj.friend.age); // 21  newobj中的friend中的age增加，oldobj的age也跟着一起增加了
```
上述例子中的拷贝对象oldObj有两层，浅拷贝只能使得拷贝后的对象newObj的第一层属性跟oldObj相互独立。
- 深拷贝： 针对浅拷贝的问题，深拷贝可以完全拷贝对象，不管有多少层属性，都可以相互独立，思路方法有：
1. 依靠递归：
基本思路如下：
```
    function deepCopy (oldObj) {
        var newObj = {}; //一样，还是先创建个新对象
        for(var key in oldObj) {  // 遍历oldObj中的所有属性
            if (typeof oldObj[key] === 'number' || typeof oldObj[key] === 'string') {
                //进行判断，属性值中是不是基本类型，即没有嵌套的情况下，依然按照浅拷贝的方式复制属性到newObj中，本例中oldObj属性只有number和string，就少写几种情况
                newObj[key] = oldObj[key];
            }
            else if (typeof oldObj[key] === 'Object') {  
                //当检测到有嵌套的情况时，使用递归，调用本身函数对嵌套的属性进行拷贝，如果嵌套的属性中还有嵌套的属性，那么继续递归直到没有嵌套为止，拷贝所有属性
                newObj[key] = deepCopy(oldObj[key]);
            }        
        }
         return newObj;
    }
```
上述代码仅仅表述了深拷贝的思路，那么实际书写代码的时候，还需要考虑oldObj中继承而来的属性，所以要针对我们自己定义的属性来进行拷贝，通过使用hasOwnProperty方法来判断，如果是我们自己定义的属性，那么再进行拷贝，在判断是否嵌套的条件中，typeof属性的值还可能是'number','string','boolean',还可能是null以及undefined。所以完整的深拷贝代码如下：
```
    var oldObj = {
        name: 'deejay',
        age: 20,
        friend: {
            name: 'dee',
            age: 20,
        }
    }
    function deepCopy (oldObj) {
        var newObj = {}; //创建一个地址不同的新对象
            for (var key in oldObj) { //遍历oldObj中的所有属性
                if (oldObj.hasOwnProperty(key)) {  // 针对自身拥有的定义的属性进行拷贝
                    if (typeof oldObj[key] === 'string' || typeof oldObj[key] === 'number' || typeof oldObj[key] === 'boolean'
                            || oldObj[key] === null || oldObj[key] === undefined) { //完整的判断是否嵌套的条件
                        newObj[key] = oldObj[key];
                    }
                    else {  // 不是上面判断的条件，即为嵌套的属性，通过递归进行拷贝
                        newObj[key] = deepCopy(oldObj[key]);
                    }
                }
            }
        return newObj;
    }
    var newObj = deepCopy(oldObj);
    console.log(newObj);
    newObj.age++;
    console.log(oldObj.age);  //20  newobj的age增加，oldobj的age仍然不变
    newObj.friend.age++;
    console.log(oldObj.friend.age); // 20  newobj中的friend中的age增加，oldObj仍然不变，newObj和oldObj是相互独立的
//    不管有多少层嵌套的属性，都是相互独立的，即为深拷贝
```
2. 使用JSON对象的stringify方法和parse方法：
```
    var oldObj = {
        name: 'deejay',
        age: 20,
        friend: {
            name: 'dee',
            age: 20,
        }
    }
    var newObj = JSON.parse(JSON.stringify(oldObj));
    /*stringify方法可以把一个对象转换为一个字符串，然后parse方法可以将字符串转化为一个对象，这样得到的新对象就跟原来对象完全独立了*/
    console.log(newObj);
    newObj.age = 25;
    console.log(oldObj.age);  // 20   newObj和oldObj完全独立，不受影响
    newObj.friend.age = 25;
    console.log(oldObj.friend.age); //20 newObj和oldObj完全独立，不受影响
```

###1.引用类型有哪些？非引用类型有哪些

- 非引用类型：保存在栈内存中的简单数据段，数值，布尔值，null和undefined。

- 引用类型： 指的是那些保存在堆内存中的对象，**变量中保存的实际上只是一个指针**，这个**指针指向内存中的另一个位置，由该位置保存对象**，引用类型有对象，数组，函数，正则。


###2.如下代码输出什么？为什么
```
var obj1 = {a:1, b:2};
var obj2 = {a:1, b:2};
console.log(obj1 == obj2);
console.log(obj1 = obj2);
console.log(obj1 == obj2);
```

输出结果为
```
var obj1 = {a:1, b:2};
var obj2 = {a:1, b:2};
console.log(obj1 == obj2);  //false
console.log(obj1 = obj2); //{a:1,b:2}
console.log(obj1 == obj2); // true
```
解释：第一行，obj1和obj2内部保存的其实是两个对象的地址，两个对象是相互独立的，即不是同一个对象，即地址不相等，所以obj1 == obj2的值为false。
第二行和第三行，将obj2指向的对象的地址赋值给obj1，此时，obj1和obj2指向的是同一个对象，**第二行输出obj2指向的对象**，第三行，obj1和obj2指向的已经是同一个对象了，所以输出true。

###3.如下代码输出什么? 为什么
```
 var a = 1
 var b = 2
 var c = { name: 'deejay', age: 2 }
 var d = [a, b, c]

var aa = a
var bb = b
var cc = c
var dd = d

a = 11
b = 22
c.name = 'hello'
d[2]['age'] = 3

console.log(aa) 
console.log(bb) 
console.log(cc)
console.log(dd)
```
输出结果为：
```
    var a = 1
    var b = 2
    var c = { name: 'deejay', age: 2 }
    var d = [a, b, c]

    var aa = a
    var bb = b
    var cc = c
    var dd = d

    a = 11
    b = 22
    c.name = 'hello'
    d[2]['age'] = 3

    console.log(aa) //1
    console.log(bb) //2
    console.log(cc) //name:hello age:3
    console.log(dd) //1,2,name：hello  age: 3
```
基本类型和引用类型的不同，基本类型复制之后就是相对独立的，引用类型仅仅传递了一个指向对象的地址，实质上引用的还是同一个对象，所以通过指向这个对象的任何一个指针改变这个对象的属性，都会引起所有变量的变化。

###4.如下代码输出什么? 为什么
```
var a = 1
var c = { name: 'deejay', age: 2 }

function f1(n){
  ++n
}
function f2(obj){
  ++obj.age
}

f1(a) 
f2(c) 
f1(c.age) 
console.log(a) 
console.log(c)
```
上述代码可以理解为：
```
    var a = 1
    var c = { name: 'deejay', age: 2 }

    function f1(n){
//        var n = a;
//        var n = c.age
        ++n
    }
    function f2(obj){
//        var obj = c; 传递的是地址
        ++obj.age
    }

    f1(a)
    f2(c)
    f1(c.age)
    console.log(a)  //1
    console.log(c) //{ name: 'deejay', age: 3 }
```
调用f2的时候改变了c中的age

###5.过滤如下数组，只保留正数，直接在原数组上操作
```
var arr = [3,1,0,-1,-3,2,-5]
function filter(arr){
}
filter(arr)
console.log(arr) // [3,1,2]
```


```
    var arr = [3,1,0,-1,-3,2,-5]
    function filter(arr){
        for (var i = 0; i < arr.length; i ++) {
            if (arr[i] <= 0) {
                arr.splice(i,1);
                i -= 1; //因为删除了一位，i的取值要减1
                // 也可以使用递归，直接调用 filter(arr);
            }
        }
        return arr;
    }
    filter(arr)
    console.log(arr) // [3,1,2]
```

###6.过滤如下数组，只保留正数，原数组不变，生成新数组
```
var arr = [3,1,0,-1,-3,2,-5]
function filter(arr){
}
var arr2 = filter(arr)
console.log(arr2) // [3,1,2]
console.log(arr)  // [3,1,0,-1,-2,2,-5]
```

```
    var arr = [3,1,0,-1,-3,2,-5]
    function filter(arr){
        var arr2 = [];  //创建新数组，不会影响原数组
        for (var i =0; i < arr.length; i ++) {
            if (arr[i] > 0) {
                arr2.push(arr[i]);
            }
        }
        return  arr2;
    }
    var arr2 = filter(arr)
    console.log(arr2) // [3,1,2]
    console.log(arr)  // [3,1,0,-1,-2,2,-5]
```

###7.写一个深拷贝函数，用两种方式实现
- 递归方式： 
```
    var oldObj = {
        name: 'deejay',
        age: 20,
        friend: {
            name: 'dee',
            age: 20,
        }
    }
    function deepCopy (oldObj) {
        var newObj = {};
        for (var key in oldObj) {
            if (oldObj.hasOwnProperty(key)) {
                if (typeof oldObj[key] === 'number' ||　typeof oldObj[key] === 'string' || typeof oldObj[key] ==='boolean' ||  oldObj[key] === null|| oldObj[[key]=== undefined]) {
                    newObj[key] = oldObj[key];
                } 
                else {
                    newObj[key] = deepCopy(oldObj[key]);
                }
            }
        }
        return newObj;
    }
    var newObj = deepCopy(oldObj);
    console.log(newObj);
```

- JOSN方法实现：
```
    var oldObj = {
        name: 'deejay',
        age: 20,
        friend: {
            name: 'dee',
            age: 20,
        }
    }
    function deepCopy (oldObj) {
        var newObj = {};
        newObj = JSON.parse(JSON.stringify(oldObj));
        return  newObj;
    }
    var newObj = deepCopy(oldObj);
    console.log(newObj);
```
