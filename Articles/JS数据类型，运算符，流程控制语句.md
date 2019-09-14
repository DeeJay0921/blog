---
title: JS数据类型，运算符，流程控制语句
date: 2017/08/11 00:00:01
tags: 
- 前端
- JS
categories: 
- 前端
---
JS数据类型，运算符，流程控制语句
<!--more-->


##1.JavaScript 定义了几种数据类型? 哪些是原始类型?哪些是复杂类型?原始类型和复杂类型的区别是什么?

JS语言中定义了6种数据类型：
- number：整数和小数(例如1，3.14等)
- string：字符组成的文本，("hello,world")
- boolean： true和false两个特定值
- undefined： 表示“**未定义**”或者不存在，即目前**此处没有任何值**
- null： 表示“**空缺**”，即**此处本来应该有值，但是目前为空**。
- object：对象，即表示各种值组成的集合。

其中，**number,string,boolean为原始类型**(primitive type)，不可以再细分了。
**object对象类型为复杂类型**(complex type)，一个对象往往是多个原始类型的值的合成，可以看做是一个存放各种值的容器。
另外，undefined和null一般视为两个特殊值。

对象的细分，对象又可以分为三个子类型：
- 狭义的对象(object)
- 数组(array)
- 函数(function)
- 正则表达式(regexp)

对于null和undefined的区别
对于null和undefined：null表示空值，即该处现在的值为空，一般用法为：
- 作为函数的参数，表示该函数的参数是一个没有任何内容的对象
- 作为对象原型链的终点

undefined表示的是不存在值，就是此处目前不存在任何值，一般用法：
- 变量声明了，但是没有赋值，为undefined
- 调用函数时，应该提供的参数没有提供，该参数等于undefined
- 对象没有赋值的属性，该属性的值默认为undefined
- 函数没有返回值时，默认返回undefined
举例说明：
```
    var i;  // i声明了但是没有赋值 为undefined

    function f(x) {
        console.log(x)
    }
    f(); // 运行f()后，函数return的就是undefined，因为默认没有提供参数

    var obj = new Object();
    console.log(obj.p); // 调用一个对象不存在的属性，该属性的值默认是undefined

    var x = f();
    console.log(x); // 函数没有返回值的时候，默认返回的也是undefined
```

另外关于null和undefined还有：
```
    console.log( undefined == null); //true
    console.log( undefined === null); //false

    console.log(typeof undefined); //undefined
    console.log(typeof null)//object

```

Boolean
布尔值代表真假两个状态，只有true和false两个值
下列运算符会返回布尔值：
- 两元逻辑运算符： &&(and), ||(or)
- 前置逻辑元素符： !(not)
- 相等运算符： ===，!==, ==,!=
- 比较运算符： >, >=,<,<=

可以转换为false的类型
如果JS某个位置上应该是布尔值，会将现有的值转换成布尔值，**除了下列的6个值，其他值都被转换为true**，转换为false的值有：
- undefined
- null
- false
- 0
- NaN
- ""（空字符串）
**只有空字符串为false，但是空数组，空对象对应的布尔值都是true**

##2.typeof和instanceof的作用和区别?

JS有三种方法可以确定一个值到底是什么类型：
- typeof运算符
- instanceof 运算符
- Object.prototype.toString 方法

typeof运算符可以返回一个值的数据类型
- 对于原始类型返回number， string或者boolean
- 对于**函数返回function**
- 对于**数组，狭义对象，正则表达式都返回object**
- 对于**undefined返回undefined**
- 对于**null返回object**

**instanceof运算符是判断某个对象是不是某个构造函数创建的实例**，**在上面的例子中，除了原始类型，函数和undefined，其他的通过typeof运算符都返回object**，所以需要instaceof来区分
```
    var arr = [];
    var obj = {};
    console.log( arr instanceof  Array); //true
    console.log( obj instanceof Array); //false
```

- typeof undefined使用范例
typeof可以用来检查一个没有声明的变量，而不报错，实际应用中通常可以用在if判断语句条件中。
```
    if (typeof a === "undefined") {console.log('deejay');} //deejay
    if (a) {console.log*('deejay');}  //Uncaught ReferenceError: a is not defined
```
所示代码中，正确的写法为上面的。

##3.如何判断一个变量是否是数字、字符串、布尔、函数

number,string,boolean都是原始类型，可以通过typeof运算符判断，typeof运算符判断函数时也会返回function，所以可以通过typeof运算符进行判断
```    
    var num = 100;
    console.log(typeof num); //number
    var str = 'hello,deejay';
    console.log(typeof str); //string
    var bool = false;
    console.log(typeof bool); //boolean
    var a = function () {
        console.log("hello,deejay");
    }
    console.log(typeof a); //function
```

##4.NaN是什么? 有什么特别之处?

NaN即Not a Number,表示非数字，**NaN和任何值都不相等，包括NaN**
`console.log(NaN == NaN); //false`

##5.如何把非数值转化为数值?

JS的数字类型没有整型和浮点数的区别，统一都是number类型，可以表示十进制，八进制，十六进制
```
  var a = 10; //十进制
  var b = 073 //八进制
  var c = 0xf3 //十六进制
```

浮点数:
浮点数是指数字包含小数点并且至少有以为数字的例如： `var a =.57;`
浮点数的精度不如整数，所以在进行小数运算的时候不要直接判断相等，尽量换成整数来进行判断

Infinity
**也是number类型**，代表无穷数

数值的转换： 
有三个函数可以把非数值转换为数值：
- Number()
- parseInt()
- parseFloat()
一般使用parseInt()和parseFloat(),具体用法举例：
 ```
    parseInt('0'); // 0 字符串为数字，直接转换为数字
    parseInt('0x11'); // 17 十六进制转换为十进制 1+1*16 = 17
    parseInt('0abc');//0
    parseInt('a234abc'); // 234 上述两种情况，以数字开头的截止到不是数字的那一位，转换成数字
    //其余情况转换结果全为NaN
    parseInt('010'); //10
```
但是有`parseInt('3.4')//转换为3`的情况，所以对于浮点数，要通过parseFloat()来进行转换。
```
    parseFloat('3.4') //3.4
    parseFloat('3.4abc') //3.4
```
在转换的过程中，开头0会被当成八进制，开头0x会被当成十六进制来转换。对于开头是0的情况，如果要按照十进制转换，可以使用第二个参数来指定。


##6.==与===有什么区别

==为近似相等,===为严格相等。
在使用==的时候，JS会做类型转换，有可能出现一些意料之外的效果。具体类型转换的规则为：
- 如果两个值的类型相同，那么执行严格意义上的相等
- 如果两个值的类型不同：
1. 一个是null一个是undefined，那么相等
2. 一个数字，一个字符串，将字符串转换为数字，然后比较
3. 如果一个值为true/false，转换为1/0比较
4. 如果一个值是对象，一个是数字或者字符串，则调用valueOf和toString进行转换然后比较
5. 其他情况皆不相等。
```
    console.log(NaN == NaN);  //false
    console.log(NaN === NaN);  //false
    console.log(null == undefined); //true
    console.log(2 == '2'); // true
```


##7.break与continue有什么区别

- break用于**强制退出循环体，执行整个循环体后面的语句**；
- continue 用于**退出当前的这次循环，执行下次循环**。

举例说明区别：
```
      for (var i = 1; i < 10; i ++) {
        if (i % 4 === 0) {
            break; // 当i循环到4时，直接break跳出整个循环体，执行整个for循环体后面的代码，不会继续输出
        }
        console.log(i); //所以结果为： 1，2,3   循环到4时，跳出整个for循环，所以没有console.log(4)
    }



    for (var j = 1; j < 10; j ++) {
        if (j % 4 === 0) {
            continue;  // 当j循环到4时， continue跳出本次的for循环， 进行下一次for循环，所以当j=4,j=8时，跳出了for循环不会输出
        }
        console.log(j);  // 所以输出结果为1,2,3,5,6,7,9
    }
```


##8.void 0 和 undefined在使用场景上有什么区别


void运算符的作用是 执行一个表达式，然后返回undefined。
在某些场景下(出现函数等)，undefined是可以被赋值的，举例说明：
```
      function fn () {
        var undefined = 3;
        var a;
        if (a === undefined) {
            console.log("a === undefined");
        }
        else {
            console.log('a !== undefined');
        }
    }
    fn(); // a !== undefined  说明这个undefined是可以被赋值的。
```
而对于 void 0来说，返回的始终是真实的undefined，不存在上述的这个问题，所以可以重写上述例子的函数为：
```
      function fn () {
        var undefined = 3;
        var a;
        if (a === void 0) {  //void 0 返回的永远都是真实的undefined
            console.log("a === undefined");
        }
        else {
            console.log('a !== undefined');
        }
    }
    fn(); // a === undefined
```

##9.以下代码的输出结果是?为什么?
```
console.log(1+1);    
console.log("2"+"4");  
console.log(2+"4"); 
console.log(+"4");
```

+操作符对不同的数据类型有不同的含义，例如:
- 两个操作数都是数字，做加法运算
- 都是字符串或者有一个是字符串，**会将另一个不是字符串的转换为字符串然后进行拼接**
- 有对象的时候会调用valueOf或者toString
- **只有一个字符串的时候会尝试进行数值转换**，转换为数字
- 只有一个数字的时候会返回其正数值
另外，特别地，有`true+true == 2;false+false ==0;`

所以上述代码的运行结果为
```
    console.log(1+1); //2
    console.log("2" + "4") // 24 转换为字符串 '24'
    console.log(2 + '4'); // 24 转换为字符串 '24'
    console.log(+ '4'); // 4 转换为数值4
```
##10.以下代码的输出结果是?
```
var a = 1;  
a+++a;  
typeof a+2;
```

- 对于自增自减运算符(++和--)，举例说明：
**自增在前面，意味着先进行自增，再进行赋值;自增在后面，意味着先进行赋值语句，然后再执行自增运算**
```
    var a = 100;
    var b = a ++; // 此时b的值为100，先执行赋值之后再进行自增。
    console.log(a,b); //101，100  自增在后面，意味着先进行赋值语句，然后再执行自增运算


    var c = 100;
    var d = ++c; //此时先进行c的自增，然后再进行赋值，所以d也为101
    console.log(c,d); // 101,101  自增在前面，意味着先进行自增，再进行赋值
```

- 在本例中，还涉及到自增运算符和加号运算符的权重问题，**自增的权重是大于+的**。
```
    //自增的权重是大于+的，所以有a+++b时，等价于(a++) + b；
```
- typeof的优先级，typeof的优先级相当高，比加减乘除的优先级要高，所以有：

```
    typeof 2*3; // NaN
    //由于typeof的优先级较高，等价于 (typeof 2) * 3;而(typeof 2)的结果是一个字符串'number',字符串'number'和3相乘，结果自然为NaN

    typeof (2*3); // 'number'
    // 同理 这下先计算2 *3 ，等价于 typeof 6; 运行结果为字符串'number'

    typeof 2+3;//   'number3'
    // 同理，这下等价于 (typeof 2) + 3,(typeof 2)的运行结果为字符串的'number',在通过+运算符，将字符串连接，结果为'number3'

    typeof 'abc' + 'def'; // 'stringdef
    // 同理，等价于 (typeof 'abc') + 'def'; 而 (typeof 'abc')的结果为字符串的'string',在和字符串'def'用+运算符进行连接，结果为字符串的'stringdef'
```
总结：**typeof的优先级较高，另外运行结果为字符串形式的数据类型**。
进行代码的分析
```
    var a = 1;
    a+++a;   //等价于 (a++) + a 即1 + 1 =2，运行完成后a的值变为2
    typeof a+2; //此时a为2， 等价于 (typeof 2) + 2，运行结果为字符串的'number' + 2,结果为字符串的 'number2'
```

##11.以下代码的输出结果是? 为什么
```
 var a = 1;
 var b = 3;
 console.log( a+++b );
```

运行结果为：
```
    var a = 1;
    var b = 3;
    console.log(a+++b);  //自增权重大于加号运算符，所以a+++b等价于 (a++) + b ,即1 + 3 = 4，运行完成后，a的值变为2，输出结果为4
```

##12.遍历数组，把数组里的打印数组每一项的平方
```
 var arr = [3,4,5]
```


解决方法为：
```
    var arr =[3,4,5];
    for (var i = 0; i < arr.length; i ++) {
        console.log(Math.pow(arr[i],2));
    }
  //输出结果为9,16,25
```
##13.遍历 JSON, 打印里面的值
```
var obj = {
 name: 'hunger', 
 sex: 'male', 
 age: 28 
}
```

解决方法为：
```
      var obj = {
        name: 'deejay',
        age: 21,
        sex: 'male'
    }

    for (var deejay_value in obj) {
        console.log(obj[deejay_value]);
    }
//输出结果为 deejay 21 male
```

##14.以下代码输出结果是? 为什么 ?
```
var a = 1, b = 2, c = 3;
var val = typeof a + b || c >0
console.log(val) 

var d = 5;
var data = d ==5 && console.log('bb')
console.log(data)

var data2 = d = 0 || console.log('haha')
console.log(data2)
 
var x = !!"Hello" + (!"world", !!"from here!!");
console.log(x)
```

代码分析：
```
  var a = 1, b = 2, c = 3;
  var val = typeof a + b || c >0
  console.log(val) ; // 输出结果为 'number2'
  
  
  var d = 5;
  var data = d ==5 && console.log('bb')
  console.log(data) ; // bb          (d == 5) && 'bb'  --->'bb'  data的值为undefined


  var data2 = d = 0 || console.log('haha')
  console.log(data2);   // haha  data2的值为undefined

  var x = !!"Hello" + (!"world", !!"from here!!");
  console.log(x); // !!'hello' + (false,true)  ---->  !!'hello' + true    ----> true + true ----> 2



```
