---
title: jQuery常用方法以及ajax
date: 2017/08/26 00:00:01
cover: http://www.aegisiscblog.com/wp-content/uploads/2016/10/JQ-AJAX.jpg
tags: 
- 前端
- JS
- jQuery
- Ajax
categories: 
- 前端
---
jQuery常用方法以及ajax
<!--more-->


##jQuery常用函数

####.each(function(index,Element))
遍历一个**jQuery对象**，为每个匹配元素执行一个函数
```
    $('li').each(function (index,element) {
//        需要注意的是这里的element是原生DOM对象,其实这里的element就等价于这个this
        console.log(index + ':' + $(this).text());
    })
```

####.map(callback(index,domElement))
通过一个函数匹配当前集合中的每个元素，产生一个包含新的jQuery对象
```
    var arrValue = $('li').map(function () {
        return $(this).text();
    })
    console.log(arrValue)
```
####jQuery.extend([deep,]target[,object1][,objectN])
- 当我们提供两个或多个对象给$.extend(),对象的所有属性都添加到目标对象(target参数)。
- 如果只有一个参数提供给$.extend()。这意味着目标参数被省略；在这种情况下，jquery对象本身被默认为目标对象。这样，我们可以在jQuery的命名空间下添加新的功能。这对于插件开发者希望向jquery中添加新函数时是很有用的

目标对象(第一个参数)将被修改，并且将通过$.extend()返回。然而，如果我们想保留原对象，我们可以通过传递一个空对象作为目标对象：
`var object = $.extend({},object1,oject2);`

在默认情况下，通过$.extend()合并操作不是递归的。

如果第一个对象的属性本身是一个对象或者数组，那么它将完全用第二个对象相同的key重写一个属性。这些值不会被合并。如果将true作为该函数的第一个参数，那么会在对象上进行递归的合并。

**使用范例：**
```
    var obj1 = {a:1};
    var obj2 = {b:2,c:3};
    var obj3 = {b:3,d:5};
    $.extend(obj1,obj2); //把obj2扩展到obj1上去，作一个遍历，如果obj2里面有的属性obj1里面也有，会进行覆盖，如果obj1里面没有，会做一次新增
    // obj1 == {a:1,b:2,c:3}
    $.extend(obj1,obj2,obj3);//修改的还是obj1，此时obj1 == {a:1,b:3,c:3,d:5} 已经存在的属性进行覆盖，没有的进行新增

    
//    如果想得到三个属性扩展的值，但是又不想修改obj1的话
    var obj4 = {};
    $.extend(obj4,obj1,obj2,obj3);
    //也可以向下面这么写，直接写个空对象，将扩展完成的对象赋给新定义的对象就好
    var obj5 = $.extend({},obj1,obj2,obj3);
```

####.clone([withDataAndEvents])
.clone方法**深度复制**所有匹配的元素的集合，包括所有匹配元素，匹配元素的下级元素，文字节点
通常我们将页面上一个元素插入到DOM里一个地方，他会被从老地方带走，类似于剪切的效果。
```
<div class="ct">
    <div class="goodbye">

        <div class="hello">
            hello
        </div>
        goodbye
    </div>
</div>
<script>
    $('.hello').appendTo($('.goodbye')); // 进行了剪切
</script>
```
但是如果我们需要的是复制不是剪切,就可以写为：
`$('.hello').clone().appendTo('.goodbye');`

####.index()  /.index(selector)/.index(element)
从给定集合中查找特定元素index
```
<ul>
    <li></li>
    <li class="active"></li>
    <li></li>
    <li></li>
</ul>
<script>
    $('.active').index(); //1
```
.index()中也可以传入参数，selector或者是element，表示在特定选择器或者元素下的index是多少

####.ready(handler)

当DOM准备就绪的时候，指定一个函数执行
等价于下面这两种写法：
```
$.(document).ready(handler)
$(handler)
```
实例：
```
$(function () {
    console.log('ready');
})
```
##jQuery的ajaxAPI用法：
###jQuery.ajax([settings])

范例：
```
    $.ajax({
        url: 'xxx.php',
        method: 'GET',
        data: {
            name: 'bayon',
            age: 24,
            sex: 'male'
        }
    }).done(function(result) {
        console.log(result);
    }).fail(function (jqXHR,textStatus) {
        console.log(textStatus);
    })
```
关于settings中的具体一些参数参考[jquery ajax文档](http://www.css88.com/jqapi-1.9/jQuery.ajax/)，注意async,beforeSend,cache,complete,dataType

###对于jsonp的ajax写法
```
    $.ajax({
        url: 'deejay',
        dataType: 'jsonp', // dataType写成jsonp
        jsonp: 'callback', // 
        jsonpCallback: 'agfjkhahj',
    })
```

####jQuery.param(obj)
创建一个数组，一个普通的对象，或者一个jQuery对象的序列化表示形式，用于URL查询字符串或者Ajax请求。
使用解析：
```
    var myObject = {
        a: {
            one: 1,
            two: 2,
            three: 3,
        },
        b: [1,2,3]
    };
    $.param(myObject); //结果为"a%5Bone%5D=1&a%5Btwo%5D=2&a%5Bthree%5D=3&b%5B%5D=1&b%5B%5D=2&b%5B%5D=3"  序列化的对象
    decodeURIComponent($.param(myObject)); //"a[one]=1&a[two]=2&a[three]=3&b[]=1&b[]=2&b[]=3"
```

###.serialize()
用做将提交的表单元素的值编译成字符串，不接受任何参数
```
    $('form').on('submit',function (event) {
        event.preventDefault();
        console.log($(this).serialize());
    })
```

###jQuery ajax使用举例
实现新闻页面加载：
在进行ajax的书写过程中，要和后端约定好**接口名称（如/getNews）**和**请求方式（get/post）**,然后数据格式要根据实际需求来决定，比如说实现新闻页面，那么返回的应该是一个数组，数组中的每一项都是个对象，存放一些信息，应该是个json对象。
确定好返回数据的大致格式后，前端再看页面具体需要什么信息，即返回数组的每一项中的json中的信息应该包括哪些，在本例中，需要新闻页面的链接url，图片链接url，h2标题中的文字，新闻段落p中的文字，那么前端需要的数据**应该是：**
```
    [
        {
            link: '',
            img: '',
            h2: '',
            p: '',
        }
    ]
```
那么约定好这些之后，进行ajax的书写就不会出错了。
来具体实现新闻页面。
后端代码为：
```


app.get('/getNews',function(req,res) {
    var news = [
        {
            link: 'http://view.inews.qq.com/a/20160830A02SEB00',
            img: 'http://inews.gtimg.com/newsapp_match/0/530686980/0',
            h2: '中国轰6K研发险些被俄罗斯发动机厂商卡脖子',
            p: '近日，轰6K＂战神＂轰炸机首次公开亮相。在中国空军长春“追梦空天”活动中，轰6K轰炸机进行了公开展出，配套武器装备逐一亮相。殊不知轰-6K这一战略性的国产战机，险些也被外国供应商卡了脖子！',
        },
        {
            link: 'https://kuaibao.qq.com/s/ENT2017082601127604',
            img: 'http://inews.gtimg.com/newsapp_match/0/1963668798/0',
            h2: '凤凰传奇的玲花素颜长这样 认得出来吗',
            p: '视觉中国讯 8月25日，凤凰传奇玲花现身广州白云机场，素颜现身的她头戴棒球帽，黄色T恤上的老虎图案十分抢眼，下身着白色短裤，大秀美腿，等待保姆车时与助理热聊不断，看似心情相当好！ (来自:视觉中国)',
        },
        {
            link: 'https://kuaibao.qq.com/s/20170826A00BDJ00',
            img: 'http://inews.gtimg.com/newsapp_match/0/1963495052/0',
            h2: '59岁冯小刚坦言人生三大遗憾：对不起前妻和徐帆，后悔捧红宝强',
            p: '今年已经59岁的冯小刚人生堪称完美，作为导演，冯小刚执导了《甲方乙方》《大腕》《不见不散》等经典，作为演员，冯小刚凭《老炮》问鼎金马影帝，然而这样完美的人生却也充满遗憾，最近冯小刚就爆出了自己人生中的三大憾事。',
        },
        {
            link: 'https://kuaibao.qq.com/s/20170825A05HUK00',
            img: 'http://inews.gtimg.com/newsapp_match/0/1960889798/0',
            h2: '叛逆17年，害谢霆锋抛弃张柏芝，如今37岁当爸爸过成这样',
            p: '在2000年，陈冠希首次出演电影《特警新人类2》，算是正式进入了娱乐圈，其实在年轻的时候，陈冠希的事业发展相当好，公司给了他不错的资源，无论是在电影还是音乐上，陈冠希都发展得不错。',
        },
        {
            link: 'https://kuaibao.qq.com/s/20170826A003L900',
            img: 'http://inews.gtimg.com/newsapp_match/0/1963334820/0',
            h2: '谢霆锋未婚妻瞬间秒变美食厨娘，做饭有模有样，谁说不如张柏芝？',
            p: '谢霆锋和王菲的早年故事，想必是人尽皆知，当年也是娱乐圈最看好的一对，但是因各种原因各自结婚。',
        },
        {
            link: 'https://view.inews.qq.com/a/SSH2017082605027004',
            img: 'http://inews.gtimg.com/newsapp_match/0/1964287533/0',
            h2: '三兄妹为减轻父母负担离家出走 草丛过夜后被找回',
            p: '8月24日，山西运城盐湖区东留村三兄妹为减轻父母负担离家出走，并在草丛里睡了一晚。26日，运城市公安局盐湖分局工作人经警民联合寻找，三人已被父母接回家。',
        },
        {
            link: 'https://view.inews.qq.com/a/SSH2017082604728808',
            img: 'http://inews.gtimg.com/newsapp_match/0/1964237194/0',
            h2: '女子开车撞上公交车 却爱上了公交司机',
            p: '今天咱们来讲一段爱情故事，故事的主人公是咱济南的一个公交司机小郭，今年在驾驶公交车的时候，被一辆私家车追尾了。',
        }
    ];

    var index = req.query.index;
    var length = req.query.length;

    var sliceNews = news.slice(index*length,(parseInt(index*length))+(parseInt(length)));

    res.send(
        {
            status: 0,
            data: sliceNews,
        }
    )
})
```

前端代码为：
```
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
</head>
<style>
    * {
        margin: 0;
        padding: 0;
        list-style: none;
    }
    a {
        color: #333;
        text-decoration: none;
    }
    .container {
        max-width: 600px;
        margin: 0 auto;
    }
    .item {
        margin-top: 20px;
    }
    .clearfix:after {
        content: '';
        display: block;
        clear: both;
    }
    .item .thumb img {
        width: 50px;
        height: 50px;
    }
    .item .thumb {
        float: left;
    }
    .item h2 {
        margin-left: 60px;
        font-size: 14px;
    }
    .item p {
        margin-left: 60px;
        font-size: 14px;
        margin-top: 10px;
        color: #ccc;
    }
    .load-more {
        margin-top: 20px;
        padding: 8px 16px;
        border: none;
        outline: none;
        cursor: pointer;
    }
    .load-more:hover {
        background: #fe6629;
        border-radius: 5px;
        transition: all .5s;
    }
</style>
<body>
<div class="container">
    <ul class="news">
        <li class="item">
            <a href="#">
                <div class="thumb">
                    <img src="" alt="">
                </div>
                <h2></h2>
                <p></p>
            </a>
        </li>

    </ul>

    <button class="load-more">加载更多</button>
</div>

<script>
    var Pageindex = 0;
    var Pagelength = 2;
    $('.load-more').on('click',function () {
        $.ajax({
            url: '/getNews',
            method: 'get',
            data : {
                index: Pageindex,
                length: Pagelength,
            },
        }).done(function (ret) {
//          ret为接收到的后端返回的数据
            if (ret.status === 0) { //返回数据我们和后端约定好加个status值来确定是否正常
                render(ret.data); // 写一个渲染函数把后端拿到的数据append到html上
                Pageindex++;
            }
            else {
                alert('后端获取新闻出错');
            }
        }).fail(function () {
            alert('系统异常');
        })

        function render (arr) { // 设置渲染函数，把后端得到的数据拼接成html字符串然后放到页面上
            if (arr.length === 0) {
                $('.load-more').remove();
                $('.container').append($('<p>没有更多数据了</p>'));
            }
            var html = ''; //进行拼接html字符串
            $.each(arr, function () { // 遍历得到的数据
                html += '<li class="item">';
                html += '<a href="' + this.link + '">';
                html += '<div class="thumb"> ![]( ' + this.img + ' ) </div>';
                html += '<h2>' + this.h2 + '</h2>';
                html += '<p>' + this.p + '</p>';
                html += '</a></li>';
            })
            $('.news').append(html);
        }
    })



</script>
</body>
</html>
```
