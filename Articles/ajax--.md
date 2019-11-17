---
title: ajax
date: 2017/08/22 00:00:01
cover: https://upload.wikimedia.org/wikipedia/commons/thumb/a/a1/AJAX_logo_by_gengns.svg/1200px-AJAX_logo_by_gengns.svg.png
tags: 
- 前端
- Ajax
categories: 
- 前端
---
ajax
<!--more-->

[XMLHttpRequest   参考资料](https://segmentfault.com/a/1190000004322487)
### get方式发送请求

```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
</head>
<body>

<div class="query-area">
    <input type="text" name="username">
    <button>查询</button>
</div>

<div class="detail-area">
    <ul>
        <!--相比于ejs之类的模板来说，通过前端JS来实现页面的转换，ul写为空即可-->
    </ul>
</div>


<script>
    var btn = document.querySelector('button');
    var input = document.querySelector('input');
    var detail = document.querySelector('ul');

    btn.addEventListener('click',function () {
        var xhr = new XMLHttpRequest(); // 1. 先创建一个XMLHttpRequest对象
        xhr.onreadystatechange = function () { // 4.监听对象的readystatechange事件
            if (xhr.readyState == 4 && (xhr.status == '200' || xhr.status == '304')) { // 5.判断是否已经拿到数据
//                readystate为4代表已经为最后一步，并且status为200或者为304时，200为请求ok，304代表是缓存
//                即请求时ok的，并且1也已经到达了第4步
                var friends = JSON.parse(xhr.responseText); //6. responseText得到返回的数据转化为个JSON对象
                var html = render(friends); // 7. 将得到的数据的josn对象传给我们写好的渲染函数，返回一个写好的html格式
                detail.innerHTML = html; // 8. 将渲染函数写好的页面传入
            }
        }
        xhr.open('get','/friends?username=' + input.value,true);//2. 设置一些向后端发送的参数,第一项为get或者post，第二项是一个url，将key和value拼接起来
//        第三项true/false代表异步和同步，默认为true，代表异步
        xhr.send(); // 3. open配置写好之后发送请求
    })

//    渲染页面的函数
    function render (friends) {
        var html = '';
        for (var i = 0; i <friends.length; i ++) {
            html += '<li>' + friends[i] + '</li>';
        }
        return html;
    }
</script>
</body>
</html>
```
router.js
```

//发送GET请求，无参数
//GET/query
//返回响应数据

deejay.get('/friends',function (req,res) {
   // req.query可以获取请求参数
   var username = req.query.username;
    var ret = ['nobody'];
    if (username === 'deejay') {
        ret = ['xiaoMing','xiaoGang'];
    }
    res.send(ret);  // res.send用于发送数据
});
```



##应用

###ajax封装
- 实现‘加载更多’
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            list-style: none;
        }
        #ct li {
            border: 1px solid #ccc;
            padding: 10px;
            margin-top: 10px;
            margin-left: 8px;
            margin-right: 8px;
            cursor: pointer;
        }
        #load-more {
            display: block;
            margin: 10px auto;
            text-align: center;
            cursor: pointer;
        }
        #load-more img {
            width: 40px;
            height: 40px;
        }
        .btn {
            display: inline-block;
            height: 40px;
            line-height: 40px;
            width: 80px;
            border: 1px solid #e27272;
            border-radius: 3px;
            text-align: center;
            text-decoration: none;
            color: #e27272;
        }
        .btn:hover,li:hover {
            background: green;
            color: #fff;
        }
    </style>
</head>
<body>

<ul id="ct">
    <li>内容1</li>
    <li>内容2</li>
    <!--加载更多，从后端得到更多的li放到ul中就可以了-->
</ul>
<a href="javascript:void(0)" id="load-more" class="btn">加载更多</a>

<script>
    //整体思路就是给按钮添加点击事件，写ajax得到数据，然后通过渲染添加到html上就可以了
    var btn = document.querySelector('#load-more');
    var ct = document.querySelector('#ct');
    var tempIndex = 3;//记录每次的变量

//    针对网速较慢并且重复点击的情况做得优化：第一种思路，如果一点时间内请求没到，忽略用户后面点击的。第二种思路，用户点击了多次，每次的输出内容都要显示
    var isDataArrive = true; // 设置判断变量，数据没拿到时设置为false


    btn.addEventListener('click',function () {

        if (!isDataArrive) {
            return; //如果数据没拿到，直接return掉，后面的点击就被忽略掉
        }

        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 &&　(xhr.status == 200 || xhr.status == 304)) {
                var content = JSON.parse(xhr.responseText);
//                console.log(content); //写到这先测试一下给的数据对不对
//                开始拼装html
//                var html = '';
//                for (var i = 0; i < content.length; i ++) {
//                    html += '<li>' + content[i] + '</li>';
//                }
//               拼装好html，应该是appendChild到ct后面，但是每次appendChild回影响性能，可以使用Fragment，所以可以改写为：
                var fragment = document.createDocumentFragment();
                for (var i = 0; i < content.length; i ++) {
                    var node = document.createElement('li');
                    node.innerText = content[i]; // 使用innerText不用innerHTML还有个好处是可以防止xss注入
                    fragment.appendChild(node);
                }
                ct.appendChild(fragment);
                tempIndex += 5;

                isDataArrive = true;//readyState === 4 表示数据已经拿到了
            }
        };
        xhr.open('get','/loadMore?index='+ tempIndex +'&length=5',true);
//       前端需要得到一个数组 ['content1','content2',...,'content5']
//       前端需要向后端发送的  /loadMore?index=3&length=5                  /loadMore为接口，后端对loadMore进行处理
//        {
//            index: 3,
//            length: 5,
//         }
/*          一般在请求数据之前都要想清楚前端需要得到什么数据，需要向后端发送的是什么接口，参数是怎么样的。
            1.GET
            2./loadMore
            3. {
                index: 3,
                length: 5,
            }
            4. ['content1','content2',...,'content5']
 */
        xhr.send();
        isDataArrive = false; // 没拿到数据设置为false
    })
</script>
</body>
</html>
```
router.js
```

//发送GET请求，无参数
//GET/query
//返回响应数据

app.get('/loadMore',function (req,res) {
    var currentIndex = req.query.index; // 拿到前端给的index，注意是字符串形式的
    var length = req.query.length; //拿到前端给的每次加载的长度
    var data = [];
    for (var i = 0; i < length; i ++) {
        data.push('内容' + (parseInt(currentIndex) + i));
    }
    res.send(data); // 将push好的数据发送给前端
});
```
